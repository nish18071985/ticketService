package com.nishant.ticketService.service;

import com.nishant.ticketService.models.STATUS;
import com.nishant.ticketService.models.Seat;
import com.nishant.ticketService.models.SeatHold;
import com.nishant.ticketService.repository.SeatHoldRepository;
import com.nishant.ticketService.repository.SeatRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


@Service
public class TicketServiceImpl implements TicketService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    SeatRepository seatRepository;


    @Autowired
    SeatHoldRepository seatHoldRepository;

    @Value("${hold.timeout:0}")
    String holdTimeOut;


    @Override
    public int numSeatsAvailable() {
        cleanUpTimeouts();
        return seatRepository.findNumberOfSeatsBySeatStatus(STATUS.OPEN.name());
    }


    @Override
    public SeatHold findAndHoldSeats(int numSeats, String customerEmail) {

        SeatHold seatHold = new SeatHold();
        seatHold.setSeatHoldId(-1);


        if(numSeats>0 && numSeatsAvailable()>=numSeats){
            List<Seat> seatsAvailable = seatRepository.findBySeatStatus(STATUS.OPEN.name()).stream().limit(numSeats).collect(Collectors.toList());

            Random rand = new Random();
            int seatHoldId = rand.nextInt(Integer.MAX_VALUE);


            Date dateHold = new Date();
            dateHold.setTime(dateHold.getTime() + Integer.parseInt(holdTimeOut) * 1000);


            seatHold.setEmailAddress(customerEmail);
            seatHold.setSeatHoldId(seatHoldId);

            for(Seat seat: seatsAvailable){
                seat.setSeatStatus(STATUS.HOLD.name());
                seat.setHoldTime(dateHold);
                seat.setSeatHold(seatHold);
            }


            seatHold.setSeats(seatsAvailable);
            seatHoldRepository.save(seatHold);
        }


        return seatHold;
    }

    @Override
    public String reserveSeats(int seatHoldId, String customerEmail) {

        cleanUpTimeouts();

        if(seatHoldId>=0 && seatHoldRepository.existsByIdAndEmailAddressAnd(seatHoldId, customerEmail)){
            String reservedCode = RandomStringUtils.randomAlphanumeric(10);
            seatHoldRepository.findById(seatHoldId).ifPresent(seatHold -> {
                List<Seat> seats = seatHold.getSeats();
                for(Seat seat : seats){
                    seat.setSeatStatus(STATUS.RESERVED.name());
                    seat.setHoldTime(null);
                    seat.setReservedCode(reservedCode);
                    seat.setSeatHold(seatHold);
                }

                seatHold.setSeats(seats);
                seatHoldRepository.save(seatHold);
            });

            return reservedCode;
        }
        return "";
    }


    /**
     * This method is used to clear the seats after the hold timeout for them have expired
     * We compare the timeout to the current datetime and if it has expired then the HOLD
     * seats are set to OPEN again. This method is called before getting numberofopenseats,
     * holding seats and reserving seats so that we consider the seats which are no longer
     * required to hold.
     */

    void cleanUpTimeouts(){
        if(seatRepository.findNumberOfSeatsBySeatStatus(STATUS.HOLD.name())!=0){
            Date date = new Date();
            int seatHoldId = -1;
            List<Seat> seatsHold = seatRepository.findBySeatStatus(STATUS.HOLD.name());
            for(Seat seat: seatsHold){
                if(date.compareTo(seat.getHoldTime())>=0){
                    seat.setSeatStatus(STATUS.OPEN.name());
                    seatHoldId = seat.getSeatHold().getSeatHoldId();
                    seat.setHoldTime(null);
                    seat.setSeatHold(null);
                }
            }

            if(seatHoldId!=-1){
                seatRepository.saveAll(seatsHold);
                seatHoldRepository.deleteById(seatHoldId);

            }
        }
    }

}
