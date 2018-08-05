package com.nishant.ticketService.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "seat_hold")
public class SeatHold {

  @Id
  @Column(name = "seat_hold_id")
  private Integer seatHoldId;

  private String emailAddress =  "";

  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "seatHold", orphanRemoval = true)
  private List<Seat> seats = new ArrayList<>();


  public Integer getSeatHoldId() {
    return seatHoldId;
  }

  public void setSeatHoldId(Integer seatHoldId) {
    this.seatHoldId = seatHoldId;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public List<Seat> getSeats() {
    return seats;
  }

  public void setSeats(List<Seat> seats) {
    this.seats = seats;
  }



}
