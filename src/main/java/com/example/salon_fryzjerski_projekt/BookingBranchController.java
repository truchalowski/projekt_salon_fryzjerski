package com.example.salon_fryzjerski_projekt;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BookingBranchController {
    @FXML private ComboBox<String> bookingBranchCombobox;
    @FXML private Text branchName1, branchName2, branchName3, branchName4, branchName5, branchName6;
    @FXML private VBox branchBox1, branchBox2, branchBox3, branchBox4, branchBox5, branchBox6;
    @FXML private Button branchSelectButton1, branchSelectButton2, branchSelectButton3, branchSelectButton4,branchSelectButton5, branchSelectButton6;
    @FXML private Text bookingBranchMaxPaginationText, bookingBranchPaginationText;
    @FXML private Button bookingBranchPrevButton, bookingBranchNextButton;

    private List<City> cityList = new ArrayList<>();
    private List<Branch> branchList = new ArrayList<>();
    String cityName = null;
    int index = 0;
    int maxPages = 0;

    SharedDataModel data = SharedDataModel.getInstance();

    private void getDataToCityList(){

        cityList = Client.getCity(RequestType.GetCity);
    }
    private void getDataToTextList(String cityName){
        branchList = Client.getBranchesInCity(RequestType.GetBranchesInCity,cityName);
    }

    public void getBookingBranchDetails(int buttonIndex, ActionEvent event) {
        int selectedIndex = bookingBranchCombobox.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            int listIndex = index + buttonIndex;
            if (listIndex < branchList.size()) {
                String branchCity = cityList.get(selectedIndex).getCity();
                String branchName = branchList.get(listIndex).getName();
                String branchStreet = branchList.get(listIndex).getStreet();
                int cityId = cityList.get(selectedIndex).getId();
                int branchId = branchList.get(listIndex).getId();

                data.setBranchName(branchName);
                data.setCityName(branchCity);
                data.setBranchStreet(branchStreet);
                data.setCityId(cityId);
                data.setBranchId(branchId);
            }
        }

        switchToSceneBookingType(event);
    }

    public void initialize() {
        if (areAllObjectsNonNull()) {
            getDataToCityList();
            ObservableList<String> observableCityList = FXCollections.observableArrayList(getCityNames());
            bookingBranchCombobox.setItems(observableCityList);
            bookingBranchPrevButton.setDisable(true);
            bookingBranchNextButton.setDisable(true);

            setTimeButtonVisibility();
        }
    }

    private boolean areAllObjectsNonNull() {
        return bookingBranchCombobox != null && bookingBranchMaxPaginationText != null &&
                bookingBranchPaginationText != null;
    }

    private List<String> getCityNames() {
        List<String> names = new ArrayList<>();
        for (City city : cityList) {
            names.add(city.getCity());
        }
        return names;
    }
    public void setCityName(){
        branchList.clear();
        bookingBranchPaginationText.setText("0");
        cityName = bookingBranchCombobox.getSelectionModel().getSelectedItem();
        if (cityName != null && !cityName.isEmpty()) {
            getDataToTextList(cityName);
            index = 0;
            maxPages = (int) Math.ceil((double) branchList.size() / 6) - 1;
            setValueToText();
            setButtonVisibility();
        } else {
            bookingBranchPrevButton.setDisable(true);
            bookingBranchNextButton.setDisable(true);
        }
    }

    private void setTimeButtonVisibility(){
        branchSelectButton1.setDisable(branchName1.getText().isEmpty());
        branchSelectButton2.setDisable(branchName2.getText().isEmpty());
        branchSelectButton3.setDisable(branchName3.getText().isEmpty());
        branchSelectButton4.setDisable(branchName4.getText().isEmpty());
        branchSelectButton5.setDisable(branchName5.getText().isEmpty());
        branchSelectButton6.setDisable(branchName6.getText().isEmpty());
    }

    private void setButtonVisibility(){
        branchBox1.setVisible(!branchName1.getText().isEmpty());
        branchBox2.setVisible(!branchName2.getText().isEmpty());
        branchBox3.setVisible(!branchName3.getText().isEmpty());
        branchBox4.setVisible(!branchName4.getText().isEmpty());
        branchBox5.setVisible(!branchName5.getText().isEmpty());
        branchBox6.setVisible(!branchName6.getText().isEmpty());

        setTimeButtonVisibility();

        bookingBranchPrevButton.setDisable(bookingBranchPaginationText.getText().equals("0"));
        bookingBranchNextButton.setDisable(bookingBranchPaginationText.getText().equals(String.valueOf(maxPages)));
    }

    public void findBookingBranchTextIndex(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();

        if (clickedButton.equals(branchSelectButton1)) {
            getBookingBranchDetails(0, event);
        } else if (clickedButton.equals(branchSelectButton2)) {
            getBookingBranchDetails(1, event);
        } else if (clickedButton.equals(branchSelectButton3)) {
            getBookingBranchDetails(2, event);
        } else if (clickedButton.equals(branchSelectButton4)) {
            getBookingBranchDetails(3, event);
        } else if (clickedButton.equals(branchSelectButton5)) {
            getBookingBranchDetails(4, event);
        } else if (clickedButton.equals(branchSelectButton6)) {
            getBookingBranchDetails(5, event);
        }
    }


    private void setValueToText() {
        List<Text> branchTexts = List.of(branchName1, branchName2, branchName3, branchName4, branchName5, branchName6);

        bookingBranchMaxPaginationText.setText(String.valueOf(maxPages));
        for (int i = 0; i < 6; i++) {
            int listIndex = index + i;
            if (listIndex < branchList.size()) {
                branchTexts.get(i).setText(branchList.get(listIndex).getName() + " - " +
                        branchList.get(listIndex).getStreet());
            } else {
                branchTexts.get(i).setText("");
            }
        }
    }


    public void nextPage(){
        index += 6;
        bookingBranchPaginationText.setText(String.valueOf(index / 6));
        setValueToText();
        setButtonVisibility();
    }

    public void prevPage(){
        index -= 6;
        bookingBranchPaginationText.setText(String.valueOf(index / 6));
        setValueToText();
        setButtonVisibility();
    }

    public void goBackButton(ActionEvent event){
        switchToSceneHello(event);
    }
    public void switchToSceneHello(ActionEvent event) {
        switchScene(event, "hello-view.fxml");
    }
    private void switchScene(ActionEvent event, String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException ignored) {
        }
    }

    public void switchToSceneBookingType(ActionEvent event) {
        switchScene(event, "booking-type-view.fxml");
    }
}
