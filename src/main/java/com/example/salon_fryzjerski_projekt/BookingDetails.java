package com.example.salon_fryzjerski_projekt;

public class BookingDetails {

    private String branchName;
    private String branchStreet;
    private String cityName;
    private String type;
    private String description;
    private String duration;
    private String employeeName;
    private String employeeSurname;
    private String clientName;
    private String clientSurname;
    private String phoneNumber;
    private String date;
    private String time;
    private String price;

    public BookingDetails(String branchName, String branchStreet, String cityName, String type, String description, String duration, String employeeName,String employeeSurname,String clientName,String clientSurname, String phoneNumber, String date, String time, String price) {
        this.setBranchName(branchName);
        this.setBranchStreet(branchStreet);
        this.setEmployeeName(employeeName);
        this.setEmployeeSurname(employeeSurname);
        this.setClientName(clientName);
        this.setClientSurname(clientSurname);
        this.setPhoneNumber(phoneNumber);
        this.setCityName(cityName);
        this.setType(type);
        this.setDescription(description);
        this.setDuration(duration);

        this.setDate(date);
        this.setTime(time);
        this.setPrice(price);
    }


    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBranchStreet() {
        return branchStreet;
    }

    public void setBranchStreet(String branchStreet) {
        this.branchStreet = branchStreet;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeSurname() {
        return employeeSurname;
    }

    public void setEmployeeSurname(String employeeSurname) {
        this.employeeSurname = employeeSurname;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientSurname() {
        return clientSurname;
    }

    public void setClientSurname(String clientSurname) {
        this.clientSurname = clientSurname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
