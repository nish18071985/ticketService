package com.nishant.ticketService.repository;

import com.nishant.ticketService.models.SeatHold;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatHoldRepository extends JpaRepository<SeatHold, Integer> {


    /**
     * checks if the combination of seatHoldId and custumoer address is valid
     *
     * @param seatHoldId the seat hold identifier
     * @param emailAddress the email address of the customer to which the
    seat hold is assigned
     * @return boolean if the seatholid exists for the customer email address
     */

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM SeatHold c WHERE c.seatHoldId = :seatHoldId AND c.emailAddress = :emailAddress")
    boolean existsByIdAndEmailAddressAnd(@Param("seatHoldId") Integer seatHoldId, @Param("emailAddress") String emailAddress);


}
