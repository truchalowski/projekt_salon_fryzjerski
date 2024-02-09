package com.example.salon_fryzjerski_projekt;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.regex.Pattern;

public class BookingUserInfoController {
    String nameRegex = "^[A-Z][a-z]+$";
    String phoneRegex = "^\\d{9}$";
    Pattern namePattern = Pattern.compile(nameRegex);
    Pattern phonePattern = Pattern.compile(phoneRegex);


    @FXML TextField nameField, surnameField, phoneField;
    @FXML Label nameLabel, surnameLabel, phoneLabel;
    String phone, name, surname;
    SharedDataModel data = SharedDataModel.getInstance();

    public void handleSubmit(ActionEvent event){
        if(!areAllObjectsNonNull()){
            return;
        }
        phone = phoneField.getText().trim();
        name = nameField.getText().trim();
        surname = surnameField.getText().trim();

        if (!handleErrors()) {
            try {
                data.setPhone(phone);
                data.setSurname(surname);
                data.setName(name);
                switchToFinal(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean areAllObjectsNonNull(){
        return phoneField != null && nameField != null && surnameField != null &&
                phoneLabel != null && nameLabel != null && surnameLabel != null;
    }

    public boolean handleErrors(){
        phoneLabel.setText("");
        nameLabel.setText("");
        surnameLabel.setText("");
        boolean isError = false;

        if (!namePattern.matcher(name).matches()) {
            nameLabel.setText("Nieprawidłowa nazwa");
            isError = true;
        }

        if (!namePattern.matcher(surname).matches()) {
            surnameLabel.setText("Nieprawidłowa nazwa");
            isError = true;
        }

        if (!phonePattern.matcher(phone).matches()) {
            phoneLabel.setText("Nieprawidłowa nazwa");
            isError = true;
        }

        if (phone.isEmpty()){
            phoneLabel.setText("Pole jest wymagane");
            isError = true;
        } else {
            if (phone.length() != 9){
                phoneLabel.setText("Numer telefonu musi zawierać 9 cyfr");
                isError = true;
            }
        }

        if (name.isEmpty()){
            nameLabel.setText("Pole jest wymagane");
            isError = true;
        }

        if (surname.isEmpty()){
            surnameLabel.setText("Pole jest wymagane");
            isError = true;
        }

        return isError;

    }

    public void goBackButton(ActionEvent event){
        switchToBooking(event);
    }
    public void switchToBooking(ActionEvent event) {
        try {
            switchScene(event, "booking-view.fxml");
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }
    private void switchScene(ActionEvent event, String fxmlFile) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
    }

    public void switchToFinal(ActionEvent event) throws IOException {
        switchScene(event, "booking-final-view.fxml");
    }
}
