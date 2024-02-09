package com.example.salon_fryzjerski_projekt;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AccountBookingHistory {
    @FXML private HBox textField1, textField2, textField3, textField4, textField5;
    @FXML private Text text1, text2, text3, text4, text5, currentPage, maxPage;
    @FXML private Button button1, button2, button3, button4, button5, rightArrow, leftArrow;

    SharedDataModel data = SharedDataModel.getInstance();
    private List<Booking> textData = new ArrayList<>();
    int index = 0;

    private void addData(){
        if(data.getAcccountType().equals("klient"))
            textData = Client.getBookingInfo(RequestType.GetClientReservation,data.getUserId());
        else if (data.getAcccountType().equals("pracownik"))
            textData = Client.getBookingInfo(RequestType.GetEmployeeReservation,data.getUserId());

    }

    public void initialize() {
        if(areAllFxComponentsNonNull()){
            addData();
            maxPage.setText(String.valueOf((textData.size() - 1) / 5));
            leftArrow.setDisable(true);
            if (textData.size() <= 5) rightArrow.setDisable(true);
            getDataToText();
        }
    }

    private boolean areAllFxComponentsNonNull() {
        return textField1 != null && textField2 != null && textField3 != null && textField4 != null && textField5 != null &&
                text1 != null && text2 != null && text3 != null && text4 != null && text5 != null && currentPage != null && maxPage != null &&
                button1 != null && button2 != null && button3 != null && button4 != null && button5 != null &&
                rightArrow != null && leftArrow != null;
    }

    private void getDataToText() {
        HBox[] textFields = {textField1, textField2, textField3, textField4, textField5};
        Text[] texts = {text1, text2, text3, text4, text5};

        for (int i = 0; i < textFields.length; i++) {
            if (index + i < textData.size()) {
                setTextFieldVisibilityAndText(textFields[i], texts[i], textData.get(index + i).getBookingInfo());
            } else {
                textFields[i].setVisible(false);
                texts[i].setText("");
            }
        }
    }
    public void switchSceneToBookingDetails(int buttonIndex, ActionEvent event){
        System.out.println(textData.get(buttonIndex + index).getId());
        data.setBookingId(textData.get(buttonIndex + index).getId());
        switchToAccountBookingDetails(event);
    }



    private void setTextFieldVisibilityAndText(HBox textField, Text text, String value) {
        if (!Objects.equals(value, "")){
            textField.setVisible(true);
            text.setText(value);
        }
        else{
            textField.setVisible(false);
        }
    }

    public void findBookingBranchTextIndex(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();

        if (clickedButton.equals(button1)) {
            switchSceneToBookingDetails(0, event);
        } else if (clickedButton.equals(button2)) {
            switchSceneToBookingDetails(1, event);
        } else if (clickedButton.equals(button3)) {
            switchSceneToBookingDetails(2, event);
        } else if (clickedButton.equals(button4)) {
            switchSceneToBookingDetails(3, event);
        } else if (clickedButton.equals(button5)) {
            switchSceneToBookingDetails(4, event);
        }
    }

    public void handleLeftArrowAction(){
        index -= 5;
        currentPage.setText(String.valueOf(index / 5));
        System.out.println(index);
        getDataToText();
        rightArrow.setDisable(false);
        if (index == 0){
            leftArrow.setDisable(true);
        }
    }

    public void handleRightArrowAction(){
        index += 5;
        currentPage.setText(String.valueOf(index / 5));
        System.out.println(index);
        getDataToText();
        leftArrow.setDisable(false);
        System.out.println(index + " " + textData.size());
        if (index + 5 >= textData.size()){
            System.out.println(index + " " + textData.size());
            rightArrow.setDisable(true);
        }
    }

    private void switchScene(ActionEvent event, String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void switchToAccountBookingDetails(ActionEvent event) {
        switchScene(event, "account-booking-history-details.fxml");
    }
}

