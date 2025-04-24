package com.example.futasbus.respone;

import java.io.Serializable;

public class TicketResponse implements Serializable {
    private int departureId;
    private String departure;
    private int destinationId;
    private String destination;
    private String departureDate;
    private String returnDate;
    private Boolean isRoundTrip;
    private int tickets;

    public TicketResponse(int departureId, int destinationId, String departure, String destination, String departureDate, String returnDate, Boolean isRoundTrip, int tickets) {
        this.departureId = departureId;
        this.destinationId = destinationId;
        this.departure = departure;
        this.destination = destination;
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.isRoundTrip = isRoundTrip;
        this.tickets = tickets;
    }

    public TicketResponse() {
    }

    public Boolean getRoundTrip() {
        return isRoundTrip;
    }

    public void setRoundTrip(Boolean roundTrip) {
        isRoundTrip = roundTrip;
    }

    public int getTickets() {
        return tickets;
    }

    public void setTickets(int tickets) {
        this.tickets = tickets;
    }

    public int getDepartureId() {
        return departureId;
    }

    public void setDepartureId(int departureId) {
        this.departureId = departureId;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public int getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(int destinationId) {
        this.destinationId = destinationId;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }
}
