package com.nishant.ticketService.models;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    private Integer seatId;

    @Column(name = "hold_time")
    private Date holdTime;

    @Column(name = "seat_status")
    private String seatStatus = "";

    @Column(name = "reserved_code")
    private String reservedCode = "";

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "seat_hold_id")
    private SeatHold seatHold;



    public SeatHold getSeatHold() {
        return seatHold;
    }

    public void setSeatHold(SeatHold seatHold) {
        this.seatHold = seatHold;
    }

    public Date getHoldTime() {
        return holdTime;
    }

    public void setHoldTime(Date holdTime) {
        this.holdTime = holdTime;
    }

    public String getSeatStatus() {
        return seatStatus;
    }

    public void setSeatStatus(String seatStatus) {
        this.seatStatus = seatStatus;
    }

    public Integer getSeatId() {
        return seatId;
    }

    public void setSeatId(Integer seatId) {
        this.seatId = seatId;
    }

    public String getReservedCode() { return reservedCode; }

    public void setReservedCode(String reservedCode) { this.reservedCode = reservedCode; }


}
