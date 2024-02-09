package com.example.salon_fryzjerski_projekt;

public class SharedDataModel {
    private static final SharedDataModel instance = new SharedDataModel();
    private boolean isLogged;
    private String accountType;
    private String cityName;
    private String branchName;
    private String branchStreet;
    private String typeTextName;
    private String typeDescName;
    private String typePriceName;
    private String typeTimeName;
    private String employeeName;
    private String dateName;
    private String timeName;
    private String name;
    private String surname;
    private String phone;
    private int userId;
    private int employeeId;
    private int typeId;
    private int branchId;
    private int cityId;
    private int bookingId;
    private String typeDuration;



    private SharedDataModel(){}

    public static SharedDataModel getInstance(){
        return instance;
    }
//accountType
    public boolean getIsLogged() {
        return isLogged;
    }

    public void setIsLogged(boolean isLogged) {
        this.isLogged = isLogged;
    }
    public String getAcccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    ///////////////////////////////////////////////////////////////////////////////

    public String getCityName() {
        return cityName;
    }
    public void setCityName(String cityName) {
        this.cityName = cityName;
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

    public String getTypeTextName(){
        return typeTextName;
    }
    public void setTypeTextName(String typeTextName){
        this.typeTextName = typeTextName;
    }

    public String getTypeDescName(){
        return typeDescName;
    }
    public void setTypeDescName(String typeDescName){
        this.typeDescName = typeDescName;
    }



    public String getTypePriceName(){
        return typePriceName;
    }
    public void setTypePriceName(String typePriceName){
        this.typePriceName = typePriceName;
    }

    public String getTypeTimeName(){
        return typeTimeName;
    }
    public void setTypeTimeName(String typeTimeName){
        this.typeTimeName = typeTimeName;
    }

    public String getEmployeeName(){
        return employeeName;
    }
    public void setEmployeeName(String employeeName){
        this.employeeName = employeeName;
    }
    public String getDateName(){
        return dateName;
    }
    public void setDateName(String dateName){
        this.dateName = dateName;
    }
    public String getName(){
        return name;
    }
    public String getTimeName(){
        return timeName;
    }
    public void setTimeName(String timeName){
        this.timeName = timeName;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getSurname(){
        return surname;
    }
    public void setSurname(String surname){
        this.surname = surname;
    }
    public String getPhone(){
        return phone;
    }
    public void setPhone(String phone){
        this.phone = phone;
    }
    public Integer getEmployeeId(){
        return employeeId;
    }
    public void setEmployeeId(int employeeId){
        this.employeeId = employeeId;
    }
    public Integer getTypeId(){
        return typeId;
    }
    public void setTypeId(int typeId){
        this.typeId = typeId;
    }
    public Integer getBranchId(){
        return branchId;
    }
    public void setBranchId(int branchId){
        this.branchId = branchId;
    }
    public Integer getCityId(){
        return cityId;
    }
    public void setCityId(int cityId){
        this.cityId = cityId;
    }
    public Integer getUserId(){
        return userId;
    }
    public void setUserId(int userId){
        this.userId = userId;
    }
    public Integer getBookingId(){
        return bookingId;
    }
    public void setBookingId(int bookingId){
        this.bookingId = bookingId;
    }
    public String getTypeDuration(){
        return typeDuration;
    }
    public void setTypeDuration(String typeDuration){
        this.typeDuration = typeDuration;
    }
}

