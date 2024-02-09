package com.example.salon_fryzjerski_projekt;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class AccountAdminPanelBranch {
    @FXML Label nameLabel, surnameLabel, peselLabel, phoneLabel, addressLabel, incomeLabel, startTimeLabel, endTimeLabel;
    @FXML TextField name, surname, pesel, phone, address, income, startTime, endTime;
    @FXML Button addEmployeeButton, addTimeButton, adminButton1, adminButton2;
    @FXML ComboBox<String> combobox;
    @FXML VBox VBox1;
    @FXML AnchorPane AnchorPane1;

    private  List<Branch> branchList = new ArrayList<>();
    String nameString, surnameString, peselString, phoneString, addressString, incomeString;
    String startTimeString, endTimeString;

    Integer branchId;

    String nameRegex = "^[A-Z][a-z]+$";
    String phoneRegex = "^\\d{9}$";
    String peselRegex = "^\\d{11}$";
    String incomeRegex = "^\\d+$";
    String timeRegex = "^(?:[0-1]\\d|2[0-3]):[0-5]\\d$";
    Pattern namePattern = Pattern.compile(nameRegex);
    Pattern phonePattern = Pattern.compile(phoneRegex);
    Pattern peselPattern = Pattern.compile(peselRegex);
    Pattern incomePattern = Pattern.compile(incomeRegex);
    Pattern timePattern = Pattern.compile(timeRegex);

    private void addBranchData(){
        branchList = Client.getBranches(RequestType.GetBranch);
    }

    public void handleAddEmployeeButton(){
        nameString = name.getText().trim();
        surnameString = surname.getText().trim();
        peselString = pesel.getText().trim();
        phoneString = phone.getText().trim();
        addressString = address.getText().trim();
        incomeString = income.getText().trim();

        if (!handleEmployeeErrors()) {

            Client.sendRequest(RequestType.AddEmployee,nameString,surnameString,"pracownik",peselString,addressString,phoneString,incomeString,branchId.toString());

            phoneLabel.setText("");
            nameLabel.setText("");
            surnameLabel.setText("");
            peselLabel.setText("");
            addressLabel.setText("");
            incomeLabel.setText("");

            phone.setText("");
            name.setText("");
            surname.setText("");
            pesel.setText("");
            address.setText("");
            income.setText("");

        }
    }

    public void handleAddTimeButton() {
        startTimeString = startTime.getText().trim();
        endTimeString = endTime.getText().trim();

        if(!handleTimeErrors()){

            Client.sendRequest(RequestType.ChangeBranchTime,branchId.toString(),startTimeString,endTimeString);

            startTime.setText("");
            endTime.setText("");
            startTimeLabel.setText("");
            endTimeLabel.setText("");
        }
    }

    public void initialize() {
        if (areAllObjectsNonNull()) {
            VBox1.setVisible(false);
            AnchorPane1.setVisible(false);
            addBranchData();
            populateComboBox();

            adminButton2.getStyleClass().add("panel-selected");
            adminButton1.getStyleClass().add("default-button");

            combobox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
                if(newValue != null) {
                    Branch selectedBranch = getBranchByName(newValue);
                    if (selectedBranch != null) {
                        branchId = selectedBranch.getId();
                        System.out.println("Selected Branch ID: " + branchId);
                    }
                }
                VBox1.setVisible(true);
                AnchorPane1.setVisible(true);
            });
        }
    }
    private boolean areAllObjectsNonNull() {
        return nameLabel != null && surnameLabel != null && peselLabel != null && phoneLabel != null && addressLabel != null &&
                incomeLabel != null && startTimeLabel != null && endTimeLabel != null && name != null && surname != null &&
                pesel != null && phone != null && address != null && income != null && startTime != null && endTime != null &&
                addEmployeeButton != null && addTimeButton != null && combobox != null && VBox1 != null && AnchorPane1 != null;
    }

    private void populateComboBox() {
        ObservableList<String> branchNames = FXCollections.observableArrayList();
        for (Branch branch : branchList) {
            branchNames.add(branch.getName());
        }
        combobox.setItems(branchNames);
    }

    private Branch getBranchByName(String branchName) {
        for (Branch branch : branchList) {
            if (branch.getName().equals(branchName)) {
                return branch;
            }
        }
        return null;
    }
    public boolean handleEmployeeErrors(){
        phoneLabel.setText("");
        nameLabel.setText("");
        surnameLabel.setText("");
        peselLabel.setText("");
        addressLabel.setText("");
        incomeLabel.setText("");
        boolean isError = false;

        if (!namePattern.matcher(nameString).matches()) {
            nameLabel.setText("Nieprawidłowa nazwa");
            isError = true;
        }

        if (!namePattern.matcher(surnameString).matches()) {
            surnameLabel.setText("Nieprawidłowa nazwa");
            isError = true;
        }

        if (!phonePattern.matcher(phoneString).matches()) {
            phoneLabel.setText("Nieprawidłowa nazwa");
            isError = true;
        }

        if (!peselPattern.matcher(peselString).matches()) {
            peselLabel.setText("Nieprawidłowa nazwa");
            isError = true;
        }

        if (!incomePattern.matcher(incomeString).matches()) {
            incomeLabel.setText("Nieprawidłowa nazwa");
            isError = true;
        }

        if (phoneString.isEmpty()){
            phoneLabel.setText("Pole jest wymagane");
            isError = true;
        } else {
            if (phoneString.length() != 9){
                phoneLabel.setText("Numer telefonu musi zawierać 9 cyfr");
                isError = true;
            }
        }

        if (peselString.isEmpty()){
            peselLabel.setText("Pole jest wymagane");
            isError = true;
        } else {
            if (peselString.length() != 11){
                peselLabel.setText("Pesel musi zawierać 11 cyfr");
                isError = true;
            }
        }

        if (nameString.isEmpty()){
            nameLabel.setText("Pole jest wymagane");
            isError = true;
        }

        if (nameString.isEmpty()){
            surnameLabel.setText("Pole jest wymagane");
            isError = true;
        }

        if (addressString.isEmpty()){
            addressLabel.setText("Pole jest wymagane");
            isError = true;
        }

        if (incomeString.isEmpty()){
            incomeLabel.setText("Pole jest wymagane");
            isError = true;
        }

        return isError;

    }

    private boolean handleTimeErrors(){
        startTimeLabel.setText("");
        endTimeLabel.setText("");

        boolean isError = false;

        if (!timePattern.matcher(startTimeString).matches()) {
            startTimeLabel.setText("Nieprawidłowa nazwa");
            isError = true;
        }

        if (!timePattern.matcher(endTimeString).matches()) {
            endTimeLabel.setText("Nieprawidłowa nazwa");
            isError = true;
        }

        if (startTimeString.isEmpty()){
            startTimeLabel.setText("Pole jest wymagane");
            isError = true;
        }

        if (endTimeString.isEmpty()){
            endTimeLabel.setText("Pole jest wymagane");
            isError = true;
        }

        return isError;
    }

    private void switchScene(ActionEvent event, String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException ignored) {
        }
    }

    public void switchSceneToAdminPanel(ActionEvent event) {
        switchScene(event, "account-admin-panel.fxml");
    }
}
