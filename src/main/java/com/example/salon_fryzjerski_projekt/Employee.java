package com.example.salon_fryzjerski_projekt;
public class Employee extends User {

    private String address;
    private float income;
    private String imagePath;

    public Employee(int account_id, String name, String surname, String address, float icome, String phoneNumber){
        super(account_id,name,surname,"pracownik",phoneNumber);

        setAddress(address);
        setIncome(icome);
    }

    public Employee(int account_id, String name, String surname, String phoneNumber){
        super(account_id,name,surname,"pracownik",phoneNumber);

        setAddress(" ");
        setIncome(0);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getIncome() {
        return income;
    }

    public void setIncome(float income) {
        this.income = income;
    }


    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
