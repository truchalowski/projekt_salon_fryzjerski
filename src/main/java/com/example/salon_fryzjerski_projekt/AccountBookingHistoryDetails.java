package com.example.salon_fryzjerski_projekt;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.text.Text;


import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class AccountBookingHistoryDetails {
    @FXML Text bookingDesc, bookingTypeTime, bookingDate, bookingTime, bookingType, bookingEmployee, bookingBranch, bookingCity, bookingPrice, bookingName, bookingSurname, bookingPhone;
    @FXML
    Button submitButton;
    SharedDataModel data = SharedDataModel.getInstance();


    public void handleSubmit(ActionEvent event){
        try {
            Client.sendRequest(RequestType.DeleteReservation,data.getBookingId().toString());
            goBack(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void initialize() {
        changeTextValue();
        if (Objects.equals(data.getAcccountType(), "pracownik")){
            submitButton.setVisible(false);
        }
    }

    private String formatTimeForDisplay(String time) {
        LocalTime localTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm:ss"));
        long hours = Duration.between(LocalTime.MIDNIGHT, localTime).toHours();
        long minutes = Duration.between(LocalTime.MIDNIGHT, localTime).toMinutesPart();

        if (hours > 0) {
            return String.format("%d:%02d h", hours, minutes).trim();
        } else {
            return minutes + " min";
        }

    }

    public void changeTextValue() {
        BookingDetails bookingDetails = Client.getBookingDetalis(RequestType.GetBookingDetails,data.getBookingId());

        bookingBranch.setText(bookingDetails.getBranchName() + " - " + bookingDetails.getBranchStreet());
        bookingCity.setText(bookingDetails.getCityName());
        bookingType.setText(bookingDetails.getType());
        bookingDesc.setText(bookingDetails.getDescription());
        bookingTypeTime.setText(formatTimeForDisplay(bookingDetails.getDuration()));
        bookingEmployee.setText(bookingDetails.getEmployeeName() + " " + bookingDetails.getEmployeeSurname());
        bookingDate.setText(bookingDetails.getDate());
        bookingTime.setText(bookingDetails.getTime());
        bookingPrice.setText(bookingDetails.getPrice());

        bookingName.setText(bookingDetails.getClientName());
        bookingSurname.setText(bookingDetails.getClientSurname());
        bookingPhone.setText(bookingDetails.getPhoneNumber());
    }


    public void goBackButton(ActionEvent event){
        if(data.getIsLogged()){
            switchToBooking(event);
        }
        else {
            switchToBookingUserInfo(event);
        }
    }
    public void switchToBookingUserInfo(ActionEvent event) {
        switchScene(event, "booking-user-info-view.fxml");
    }
    public void switchToBooking(ActionEvent event) {
        switchScene(event, "booking-view.fxml");
    }

    private void switchScene(ActionEvent event, String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void goBack(ActionEvent event) throws IOException {
        switchScene(event, "account-booking-history.fxml");
    }
}
