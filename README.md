# Ticket Service Application

This application facilitates the discovery, temporary hold, and final reservation of seats within a high-demand performance venue.


## Assumptions
1) Considered total num of seats as 30. Intital data is loaded through data.sql.
2) The hold timeout of 5 seconds is considered. Same can we found in application.properties.
3) Seats are assigned based on the first available seat. 
4) If at anytime number of seats requested for hold is greater than the number of available seats that application handles    that and returns back a seatHold of -1 to handle it gracefully.
5) Method cleanUpTimeouts checks the hold timeout and makes the seat available once the timeout expires.
6) In memory H2 database is being used to build the application along with Springboot and Spring JPA.
7) SeatHold is a random generated positive number and reservation code is a alphanumeric string of size 10.
8) A seat can hold 3 status OPEN, HOLD, RESERVED.


| Seat ID  | Hold Time | Seat status  | Reserved Code | Seat Hold ID |          
| ------------- | ------------- | ----------- | ------- | ---------- |            

| Seat Hold ID  | Customer Email | Seats |
| ------------- | ------------- | ----------- |  
