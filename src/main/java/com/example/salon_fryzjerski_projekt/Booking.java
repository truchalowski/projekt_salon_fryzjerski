package com.example.salon_fryzjerski_projekt;

public class Booking {
    private final int id;
    private final String bookingInfo;

    public Booking(int id, String date, String time , String name) {
        this.id = id;
        this.bookingInfo = date + " " + time + " " + name;
    }

    public int getId() {
        return id;
    }

    public String getBookingInfo() {
        return bookingInfo;
    }
}
