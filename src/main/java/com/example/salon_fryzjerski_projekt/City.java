package com.example.salon_fryzjerski_projekt;

public class City {
    private final int id;
    private final String city;

    public City(int id, String city) {
        this.id = id;
        this.city = city;
    }

    public int getId() {
        return id;
    }

    public String getCity() {
        return city;
    }
}
