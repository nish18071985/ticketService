package com.nishant.ticketService.repository;

import com.nishant.ticketService.models.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat,Integer>{


  /**
   * number of seats for a particular seat status
   *
   * @param seatStatus status of the seat
   * @return int total number of seats for the status
   */
  @Query("SELECT COUNT(seat.seatId) FROM Seat seat WHERE seat.seatStatus = :seatStatus")
  int findNumberOfSeatsBySeatStatus(@Param("seatStatus") String seatStatus);


  /**
   * gets all the seats for a seat status
   *
   * @param seatStatus status of the seat
   * @return List<Seat> List of seats for a particular seat status
   */
  List<Seat> findBySeatStatus(String seatStatus);

}
