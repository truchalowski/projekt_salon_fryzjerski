package com.example.salon_fryzjerski_projekt;

public class Branch {
    private final int id;
    private final String street, name;

    public Branch(int id, String name, String street) {
        this.id = id;
        this.name = name;
        this.street = street;
    }
    public Branch(int id, String name){
        this.id = id;
        this.name = name;
        this.street = "";
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getStreet() { return street; }
}

