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
9) A sample set is provided that runs the application.

## Design

2 tables are considered Seat and SeatHold. These tables hold a relationship among themselves on the Seat Hold ID field.
The structure of the table is as follows:

`Seat table`

| Seat ID  | Hold Time | Seat status  | Reserved Code | Seat Hold ID |          
| ------------- | ------------- | ----------- | ------- | ---------- |  

`Seat Hold table`

| Seat Hold ID  | Customer Email | Seats |
| ------------- | ------------- | ----------- |  

Initially all the seats will be in `OPEN` status. 

Once a request is being made for holding the seats, the seat status will be upgraded to `HOLD` along with placing a timeout on them. A new entry will be placed in the seat hold table referencing to the hold seats with the customer email and a random generated Id.

When a request is placed to reserve the seats a check is made if those seats where in `HOLD` or not. Based on that seats are moved to `RESERVED` status and a random generated string is . returned as reservation code.


## Building the application

By running the following command we can build the application into a JAR and run the test cases

`mvn clean install`

## Running the application

Application can be run using the following command. A set of pre-defined interaction is provided with the application.

`mvn spring-boot:run`
