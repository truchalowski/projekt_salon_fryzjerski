package com.example.salon_fryzjerski_projekt;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class BookingTypeController {
    @FXML private Text headText1, headText2, headText3;
    @FXML private Text descText1, descText2, descText3;
    @FXML private Text priceText1, priceText2, priceText3;
    @FXML private Text timeText1, timeText2, timeText3;
    @FXML private Text bookingTypePaginationText, bookingTypeMaxPaginationText;
    @FXML private Button bookingTypeSelectButton1, bookingTypeSelectButton2, bookingTypeSelectButton3;
    @FXML private Button bookingTypeNextButton, bookingTypePrevButton;

    private List<BookingType> bookingTypes = new ArrayList<>();
    int index = 0;
    int maxPages = 0;

    SharedDataModel data = SharedDataModel.getInstance();

    public void getBookingTypeDetails(int buttonIndex, ActionEvent event) {
        int listIndex = index + buttonIndex;
        String textName, descName, priceName, timeName;
        int id;
        try {
            if (listIndex < bookingTypes.size()) {
                id = bookingTypes.get(listIndex).getId();
                textName = bookingTypes.get(listIndex).getHeadText();
                descName = bookingTypes.get(listIndex).getDescText();
                priceName = bookingTypes.get(listIndex).getPriceText();
                timeName = bookingTypes.get(listIndex).getTimeText();

                data.setTypeId(id);
                data.setTypeDescName(descName);
                data.setTypePriceName(priceName);
                data.setTypeTextName(textName);
                data.setTypeTimeName(timeName);
            }
            switchToSceneBookingEmployee(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void getDataToTextList(){
        bookingTypes = Client.getService(RequestType.GetHairdressingService);
    }


    public void initialize(){
        if (areAllObjectsNonNull()) {
            System.out.println(data.getBranchName());
            getDataToTextList();
            setValueToText();
            maxPages = (int) Math.ceil((double) bookingTypes.size() / 3) - 1;
            bookingTypeMaxPaginationText.setText(String.valueOf(maxPages));
            setButtonVisibility();
        }
    }

    private boolean areAllObjectsNonNull() {
        return headText1 != null && headText2 != null && headText3 != null &&
                descText1 != null && descText2 != null && descText3 != null &&
                priceText1 != null && priceText2 != null && priceText3 != null &&
                timeText1 != null && timeText2 != null && timeText3 != null &&
                bookingTypePaginationText != null && bookingTypeSelectButton1 !=null &&
                bookingTypeSelectButton2 != null && bookingTypeSelectButton3 != null;
    }
    private void setButtonVisibility(){
        bookingTypeSelectButton1.setVisible(!headText1.getText().isEmpty() && !priceText1.getText().isEmpty() && !timeText1.getText().isEmpty());
        bookingTypeSelectButton2.setVisible(!headText2.getText().isEmpty() && !priceText2.getText().isEmpty() && !timeText2.getText().isEmpty());
        bookingTypeSelectButton3.setVisible(!headText3.getText().isEmpty() && !priceText3.getText().isEmpty() && !timeText3.getText().isEmpty());
        bookingTypePrevButton.setDisable(bookingTypePaginationText.getText().equals("0"));
        bookingTypeNextButton.setDisable(bookingTypePaginationText.getText().equals(String.valueOf(maxPages)));
    }
    private void setValueToText() {
        List<Text> headTexts = List.of(headText1, headText2, headText3);
        List<Text> descTexts = List.of(descText1, descText2, descText3);
        List<Text> priceTexts = List.of(priceText1, priceText2, priceText3);
        List<Text> timeTexts = List.of(timeText1, timeText2, timeText3);


        for (int i = 0; i < 3; i++) {
            int listIndex = index + i;
            if (listIndex < bookingTypes.size()) {
                BookingType bookingType = bookingTypes.get(listIndex);
                headTexts.get(i).setText(bookingType.getHeadText());
                descTexts.get(i).setText(bookingType.getDescText());
                priceTexts.get(i).setText(bookingType.getPriceText() + " zł");
                timeTexts.get(i).setText(formatTimeForDisplay(bookingType.getTimeText()));
            } else {
                headTexts.get(i).setText("");
                descTexts.get(i).setText("");
                priceTexts.get(i).setText("");
                timeTexts.get(i).setText("");
            }
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


    public void nextPage(){
        index += 3;
        bookingTypePaginationText.setText(String.valueOf(index / 3));
        setValueToText();
        setButtonVisibility();
    }

    public void prevPage(){
        index -= 3;
        bookingTypePaginationText.setText(String.valueOf(index / 3));
        setValueToText();
        setButtonVisibility();
    }
    public void findBookingTypeTextIndex(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();

        if (clickedButton.equals(bookingTypeSelectButton1)) {
            getBookingTypeDetails(0, event);
        } else if (clickedButton.equals(bookingTypeSelectButton2)) {
            getBookingTypeDetails(1, event);
        } else if (clickedButton.equals(bookingTypeSelectButton3)) {
            getBookingTypeDetails(2, event);
        }
    }

    public void goBackButton(ActionEvent event){
        switchToBookingBranch(event);
    }
    public void switchToBookingBranch(ActionEvent event) {
        switchScene(event, "booking-branch-view.fxml");
    }
    private void switchScene(ActionEvent event, String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException ignored) {
        }
    }

    public void switchToSceneBookingEmployee(ActionEvent event) throws IOException {
        switchScene(event, "booking-employee-view.fxml");
    }
}
