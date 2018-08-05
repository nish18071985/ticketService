package com.nishant.ticketService.service;


import com.nishant.ticketService.models.STATUS;
import com.nishant.ticketService.models.Seat;
import com.nishant.ticketService.models.SeatHold;
import com.nishant.ticketService.repository.SeatHoldRepository;
import com.nishant.ticketService.repository.SeatRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TicketServiceTests {

    @Mock
    SeatRepository seatRepository;

    @Mock
    SeatHoldRepository seatHoldRepository;

    @Spy
    TicketServiceImpl ticketService;


    @Before
    public void initObjects(){
        ReflectionTestUtils.setField(ticketService,"seatRepository",seatRepository);
        ReflectionTestUtils.setField(ticketService,"seatHoldRepository",seatHoldRepository);
        ReflectionTestUtils.setField(ticketService,"holdTimeOut","10");

    }

    @Test
    public void testNumberOfOpenSeatsAvailable(){
        Mockito.doNothing().when(ticketService).cleanUpTimeouts();
        when(seatRepository.findNumberOfSeatsBySeatStatus(STATUS.OPEN.name())).thenReturn(2);

        assertEquals("Verify the number of open seats", 2, ticketService.numSeatsAvailable());
        verify(ticketService, times(1)).cleanUpTimeouts();
        verify(seatRepository, times(1)).findNumberOfSeatsBySeatStatus(STATUS.OPEN.name());
    }

    @Test
    public void testFindAndHoldSeatsLessThanTotalAvailableSeats(){

        //initial set up
        int numOfHoldSeats = 3; //requested seats to hold is 3 and total number of available seats is 4
        String customerEmail = "abc@gmail.com";
        List<Seat> seats = new ArrayList<>();

        seats.add(new Seat(){{
            setSeatId(1);
            setSeatStatus(STATUS.OPEN.name());
        }});

        seats.add(new Seat(){{
            setSeatId(2);
            setSeatStatus(STATUS.OPEN.name());
        }});

        seats.add(new Seat(){{
            setSeatId(3);
            setSeatStatus(STATUS.OPEN.name());
        }});

        seats.add(new Seat(){{
            setSeatId(4);
            setSeatStatus(STATUS.OPEN.name());
        }});


        Mockito.doNothing().when(ticketService).cleanUpTimeouts();
        when(seatRepository.findNumberOfSeatsBySeatStatus(STATUS.OPEN.name())).thenReturn(4);
        when(seatHoldRepository.save(Mockito.any())).thenReturn(new SeatHold());
        when(seatRepository.findBySeatStatus(STATUS.OPEN.name())).thenReturn(seats);


        SeatHold seatHold = ticketService.findAndHoldSeats(numOfHoldSeats,customerEmail);
        verify(ticketService, times(1)).cleanUpTimeouts();
        verify(seatRepository, times(1)).findNumberOfSeatsBySeatStatus(STATUS.OPEN.name());
        verify(seatRepository, times(1)).findBySeatStatus(STATUS.OPEN.name());

        Assert.assertNotNull("Valid seatHold Id",seatHold.getSeatHoldId());
        Assert.assertNotNull("Valid list of hold seats",seatHold.getSeats());
        assertEquals("Verify the number of hold seats", numOfHoldSeats, seatHold.getSeats().size());
        assertEquals("Verify customer email address",customerEmail, seatHold.getEmailAddress());

        for(Seat seat: seatHold.getSeats()){
            assertEquals("Verify the status of each hold seat", STATUS.HOLD.name(), seat.getSeatStatus());
        }
    }


    @Test
    public void testFindAndHoldSeatsEqualToTotalAvailableSeats(){

        //initial set up
        int numOfHoldSeats = 4;
        String customerEmail = "abc@gmail.com";
        List<Seat> seats = new ArrayList<>();

        seats.add(new Seat(){{
            setSeatId(1);
            setSeatStatus(STATUS.OPEN.name());
        }});

        seats.add(new Seat(){{
            setSeatId(2);
            setSeatStatus(STATUS.OPEN.name());
        }});

        seats.add(new Seat(){{
            setSeatId(3);
            setSeatStatus(STATUS.OPEN.name());
        }});

        seats.add(new Seat(){{
            setSeatId(4);
            setSeatStatus(STATUS.OPEN.name());
        }});


        Mockito.doNothing().when(ticketService).cleanUpTimeouts();
        when(seatRepository.findNumberOfSeatsBySeatStatus(STATUS.OPEN.name())).thenReturn(4);
        when(seatHoldRepository.save(Mockito.any())).thenReturn(new SeatHold());
        when(seatRepository.findBySeatStatus(STATUS.OPEN.name())).thenReturn(seats);


        SeatHold seatHold = ticketService.findAndHoldSeats(numOfHoldSeats,customerEmail);

        Assert.assertNotNull("Valid seatHold Id",seatHold.getSeatHoldId());
        Assert.assertNotNull("Valid list of hold seats",seatHold.getSeats());
        assertEquals("Verify the number of hold seats", numOfHoldSeats, seatHold.getSeats().size());
        assertEquals("Verify customer email address",customerEmail, seatHold.getEmailAddress());

        for(Seat seat: seatHold.getSeats()){
            assertEquals("Verify the status of each hold seat", STATUS.HOLD.name(), seat.getSeatStatus());
        }
    }


    @Test
    public void testFindAndHoldSeatsGreaterThanTotalAvailableSeats(){

        //initial set up
        int numOfHoldSeats = 5;
        String customerEmail = "abc@gmail.com";

        Mockito.doNothing().when(ticketService).cleanUpTimeouts();
        SeatHold seatHold = ticketService.findAndHoldSeats(numOfHoldSeats,customerEmail);

        assertEquals("Negative seatHold Id",Integer.valueOf(-1),seatHold.getSeatHoldId());
        assertEquals("Verify the number of hold seats", 0, seatHold.getSeats().size());
        assertEquals("Verify customer email address","", seatHold.getEmailAddress());

    }


    @Test
    public void testFindAndHoldSeatsWithNegativeNumOfSeatsRequested(){

        //initial set up
        int numOfHoldSeats = -5;
        String customerEmail = "abc@gmail.com";

        SeatHold seatHold = ticketService.findAndHoldSeats(numOfHoldSeats,customerEmail);

        assertEquals("Negative seatHold Id",Integer.valueOf(-1),seatHold.getSeatHoldId());
        assertEquals("Verify the number of hold seats", 0, seatHold.getSeats().size());
        assertEquals("Verify customer email address","", seatHold.getEmailAddress());

    }


    @Test
    public void testFindAndHoldSeatsWithZeroNumOfSeatsRequested(){

        //initial set up
        int numOfHoldSeats = 0;
        String customerEmail = "abc@gmail.com";

        SeatHold seatHold = ticketService.findAndHoldSeats(numOfHoldSeats,customerEmail);

        assertEquals("Negative seatHold Id",Integer.valueOf(-1),seatHold.getSeatHoldId());
        assertEquals("Verify the number of hold seats", 0, seatHold.getSeats().size());
        assertEquals("Verify customer email address","", seatHold.getEmailAddress());

    }


    @Test
    public void testReserveSeatsWithValidSeatHoldId(){

        //initial set up

        int seatHoldId = 123456;
        String customerEmail = "abc@gmail.com";

        List<Seat> seats = new ArrayList<>();

        seats.add(new Seat(){{
            setSeatId(1);
            setSeatStatus(STATUS.HOLD.name());
        }});

        seats.add(new Seat(){{
            setSeatId(2);
            setSeatStatus(STATUS.HOLD.name());
        }});

        seats.add(new Seat(){{
            setSeatId(3);
            setSeatStatus(STATUS.HOLD.name());
        }});

        SeatHold seatHold = new SeatHold(){{
            setSeatHoldId(seatHoldId);
            setSeats(seats);
            setEmailAddress(customerEmail);
        }};



        Mockito.doNothing().when(ticketService).cleanUpTimeouts();
        when(seatHoldRepository.existsByIdAndEmailAddressAnd(seatHoldId, customerEmail)).thenReturn(true);
        when(seatHoldRepository.findById(seatHoldId)).thenReturn(Optional.of(seatHold));
        when(seatHoldRepository.save(Mockito.any())).thenReturn(new SeatHold());


        String reservationCode = ticketService.reserveSeats(seatHoldId,customerEmail);
        verify(ticketService, times(1)).cleanUpTimeouts();
        verify(seatHoldRepository, times(1)).existsByIdAndEmailAddressAnd(seatHoldId, customerEmail);
        verify(seatHoldRepository, times(1)).findById(seatHoldId);
        verify(seatHoldRepository, times(1)).save(Mockito.any());

        for(Seat seat: seatHold.getSeats()){
            assertEquals("Verify the status of each reserved seat", STATUS.RESERVED.name(), seat.getSeatStatus());
            assertNull("Verify seat Hold time is cleared as seats are reserved",seat.getHoldTime());
        }

        assertNotEquals("Verify there is a valid alphanumeric reservation code","",reservationCode);
    }



    @Test
    public void testReserveSeatsWithInValidNegativeSeatHoldId(){

        //initial set up
        int seatHoldId = -1;
        String customerEmail = "abc@gmail.com";

        Mockito.doNothing().when(ticketService).cleanUpTimeouts();

        String reservationCode = ticketService.reserveSeats(seatHoldId,customerEmail);
        verify(ticketService, times(1)).cleanUpTimeouts();
        verify(seatHoldRepository, times(0)).existsByIdAndEmailAddressAnd(seatHoldId, customerEmail);
        verify(seatHoldRepository, times(0)).findById(seatHoldId);
        verify(seatHoldRepository, times(0)).save(Mockito.any());

        assertEquals("Verify there is a empty reservation code","",reservationCode);
    }


}
