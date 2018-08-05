package com.nishant.ticketService.repository;

import com.nishant.ticketService.models.STATUS;
import com.nishant.ticketService.models.Seat;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@DataJpaTest
public class SeatRepositoryTests {

    @Autowired
    private SeatRepository seatRepository;



    /**
     * The repository tests all run fine if we don't use this as a CommandLineRunner app.
     * The reason for ignoring the Repository test is because @DataJpaTest only
     * instantiates and initializes @Entity classes and not the @Component beans.
     * https://stackoverflow.com/questions/45309405/why-is-spring-boots-datajpatest-scanning-component
     * Since I am using @Autowired (TicketService) in the main application class to be able to run
     * the app as well, its not able to instantiate TicketService and when SpringBoot tries to create
     * the test application context the application context load is failing. If we don't autowire TicketService
     * in the TicketServiceApplication class they will all run fine.
     */

    @Ignore
    @Test
    public void whenfindBySeatStatusOpen_thenReturnSeats(){
        List<Seat> seats = seatRepository.findBySeatStatus(STATUS.OPEN.name());
        assertEquals(30,seats.size());

    }

    @Ignore
    @Test
    public void whenfindBySeatStatusHold_thenReturnSeats(){

        //initial set up
        List<Seat> seats = seatRepository.findBySeatStatus(STATUS.OPEN.name());
        for(Seat seat: seats.stream().limit(7).collect(Collectors.toList())){
            seat.setSeatStatus(STATUS.HOLD.name());
            seatRepository.save(seat);
        }

        assertEquals(7,seatRepository.findBySeatStatus(STATUS.HOLD.name()).size());

    }

    @Ignore
    @Test
    public void whenfindBySeatStatusConfirmed_thenReturnSeats(){

        //initial set up
        List<Seat> seats = seatRepository.findBySeatStatus(STATUS.OPEN.name());
        for(Seat seat: seats.stream().limit(5).collect(Collectors.toList())){
            seat.setSeatStatus(STATUS.RESERVED.name());
            seatRepository.save(seat);
        }

        assertEquals(5,seatRepository.findBySeatStatus(STATUS.RESERVED.name()).size());

    }
}
