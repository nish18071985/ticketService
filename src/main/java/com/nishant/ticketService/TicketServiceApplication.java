package com.nishant.ticketService;

import com.nishant.ticketService.models.STATUS;
import com.nishant.ticketService.models.Seat;
import com.nishant.ticketService.models.SeatHold;
import com.nishant.ticketService.repository.SeatHoldRepository;
import com.nishant.ticketService.repository.SeatRepository;
import com.nishant.ticketService.service.TicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TicketServiceApplication implements CommandLineRunner{

    @Autowired
    TicketService ticketService;

    @Autowired
    SeatRepository seatRepository;

    @Autowired
    SeatHoldRepository seatHoldRepository;

    @Value("${hold.timeout:0}")
    String holdTimeOut;

    public static void main(String[] args) {
        SpringApplication.run(TicketServiceApplication.class, args);
    }

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public void run(String... args) throws Exception {
        LOGGER.info("Number of open seats available: " + ticketService.numSeatsAvailable());

        LOGGER.info("Requesting 5 seats to hold");
        SeatHold seatHold = ticketService.findAndHoldSeats(5, "nish@gmail.com");
        int seatholdId = seatHold.getSeatHoldId();
        LOGGER.info("Seat Hold id : " + seatholdId);
        LOGGER.info("customer email : " + seatHold.getEmailAddress());

        for(Seat seat: seatHold.getSeats()){
            LOGGER.info("Seat id: " + seat.getSeatId());
            LOGGER.info("Seat status : " + seat.getSeatStatus());
        }

        LOGGER.info("Requesting to reserve the  5 seats which we requested to hold");
        String reservationCode = ticketService.reserveSeats(seatholdId, "nish@gmail.com");
        LOGGER.info("Reservation code : "+ reservationCode);

        LOGGER.info("Number of open seats available: " + ticketService.numSeatsAvailable());
        LOGGER.info("Requesting 10 seats to hold");
        SeatHold seatHold1 = ticketService.findAndHoldSeats(10, "abc@gmail.com");
        int seatholdId1 = seatHold1.getSeatHoldId();
        LOGGER.info("Seat Hold id : " + seatholdId1);
        LOGGER.info("customer email : " + seatHold1.getEmailAddress());
        LOGGER.info("Number of open seats available: " + ticketService.numSeatsAvailable());


        LOGGER.info("Letting the timeout to expire (5 sec)");
        Thread.sleep(5000);
        LOGGER.info("Number of open seats available: " + ticketService.numSeatsAvailable());

        LOGGER.info("Requesting 25 seats to hold");
        SeatHold seatHold2 = ticketService.findAndHoldSeats(25, "xyz@gmail.com");
        int seatholdId2 = seatHold2.getSeatHoldId();
        LOGGER.info("Seat Hold id : " + seatholdId2);
        LOGGER.info("customer email : " + seatHold2.getEmailAddress());
        LOGGER.info("Number of open seats available: " + ticketService.numSeatsAvailable());

        LOGGER.info("Requesting to reserve the  25 seats which we requested to hold");
        String reservationCode1 = ticketService.reserveSeats(seatholdId2, "xyz@gmail.com");
        LOGGER.info("Reservation code : "+ reservationCode1);

        LOGGER.info("Number of open seats available: " + ticketService.numSeatsAvailable());
        LOGGER.info("Requesting 2 seats to hold");
        SeatHold seatHold3 = ticketService.findAndHoldSeats(2, "pqr@gmail.com");
        int seatholdId3 = seatHold3.getSeatHoldId();
        LOGGER.info("Invalid Seat Hold id : " + seatholdId3);

    }
}
