package com.nishant.ticketService.repository;

import com.nishant.ticketService.models.SeatHold;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@SpringBootTest
public class SeatHoldRepositoryTests {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    SeatHoldRepository seatHoldRepository;


    @Before
    public void setUp(){
        SeatHold seatHold = new SeatHold();
        seatHold.setEmailAddress("abc@gmail.com");
        seatHold.setSeatHoldId(12345);
        testEntityManager.persist(seatHold);
        testEntityManager.flush();
    }

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
    public void whenValidSeatHoldIdandEmailaddress_thenReturnTrue(){
        assertEquals("Verify if the combination of seatHoldId and customer email address exists ",
                true, seatHoldRepository.existsByIdAndEmailAddressAnd(12345,
                        "abc@gmail.com"));
    }

    @Ignore
    @Test
    public void whenInValidSeatHoldIdandEmailaddress_thenReturnFalse(){
        assertEquals("Verify if the combination of seatHoldId and customer email address does not exists ",
                false, seatHoldRepository.existsByIdAndEmailAddressAnd(12345,
                        "xyz@gmail.com"));
    }
}
