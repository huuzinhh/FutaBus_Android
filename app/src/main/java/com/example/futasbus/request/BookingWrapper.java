package com.example.futasbus.request;

public class BookingWrapper {
    private BookingRequest bookingData;
    private BookingRequest bookingDataReturn;

    public BookingWrapper(BookingRequest bookingData, BookingRequest bookingDataReturn) {
        this.bookingData = bookingData;
        this.bookingDataReturn = bookingDataReturn;
    }

    public BookingWrapper() {
    }

    public BookingRequest getBookingData() {
        return bookingData;
    }

    public void setBookingData(BookingRequest bookingData) {
        this.bookingData = bookingData;
    }

    public BookingRequest getBookingDataReturn() {
        return bookingDataReturn;
    }

    public void setBookingDataReturn(BookingRequest bookingDataReturn) {
        this.bookingDataReturn = bookingDataReturn;
    }
}
