package com.example.salon_fryzjerski_projekt;

import java.util.UUID;

public class User  {

    private int account_id;
    private String name;
    private String surname;
    private String account_type;

    private String phoneNumber;

    public User(int account_id, String name, String surname, String account_type, String phoneNumber){
        setAccount_id(account_id);
        setName(name);
        setSurname(surname);
        setAccount_type(account_type);
        setPhoneNumber(phoneNumber);
    }
    
    public int getAccount_id() {
        return account_id;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
