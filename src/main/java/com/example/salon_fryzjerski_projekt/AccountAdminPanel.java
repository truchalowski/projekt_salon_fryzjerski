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


public class AccountAdminPanel {
    @FXML private ComboBox<String> bookingBranchCombobox;
    @FXML private Text textName1, textName2, textName3, textName4, textName5, textName6;
    @FXML private VBox employeeBox1, employeeBox2, employeeBox3, employeeBox4, employeeBox5, employeeBox6;
    @FXML private Button selectButton1, selectButton2, selectButton3, selectButton4,selectButton5, selectButton6,
    adminButton1, adminButton2;
    @FXML private Text maxPaginationText, paginationText;
    @FXML private Button prevButton, nextButton;

    private List<Branch> branchList = new ArrayList<>();
    private List<Employee> employeeList = new ArrayList<>();
    String cityName = null;
    int index = 0;
    int maxPages = 0;


    private void getDataTobranchList(){
        branchList = Client.getBranches(RequestType.GetBranch);
    }

    private void getDataToTextList(String cityName){
        System.out.println(cityName);
        employeeList = Client.getEmployee(RequestType.GetEmployeeByBranch,cityName);
    }
    public void getBookingBranchDetails(int buttonIndex, ActionEvent event) {
        int listIndex = index + buttonIndex;
        int employeeId = employeeList.get(listIndex).getAccount_id();

        Client.sendRequest(RequestType.DeleteEmployee,Integer.toString(employeeId));

        reloadPage(event);
    }

    public void initialize() {
        if (areAllObjectsNonNull()) {
            getDataTobranchList();
            ObservableList<String> observablebranchList = FXCollections.observableArrayList(getCityNames());
            bookingBranchCombobox.setItems(observablebranchList);
            prevButton.setDisable(true);
            nextButton.setDisable(true);

            adminButton1.getStyleClass().add("panel-selected");
            adminButton2.getStyleClass().add("default-button");

            setSelectButtonVisibility();
        }
    }

    private boolean areAllObjectsNonNull() {
        return bookingBranchCombobox != null && maxPaginationText != null &&
                paginationText != null;
    }

    private List<String> getCityNames() {
        List<String> names = new ArrayList<>();
        for (Branch branch : branchList) {
            names.add(branch.getName());
        }
        return names;
    }
    public void setCityName(){
        employeeList.clear();
        paginationText.setText("0");
        cityName = bookingBranchCombobox.getSelectionModel().getSelectedItem();
        if (cityName != null && !cityName.isEmpty()) {
            getDataToTextList(cityName);
            index = 0;
            maxPages = (int) Math.ceil((double) employeeList.size() / 6) - 1;
            setValueToText();
            setButtonVisibility();
        } else {
            prevButton.setDisable(true);
            nextButton.setDisable(true);
        }
    }

    private void setSelectButtonVisibility(){
        selectButton1.setDisable(textName1.getText().isEmpty());
        selectButton2.setDisable(textName2.getText().isEmpty());
        selectButton3.setDisable(textName3.getText().isEmpty());
        selectButton4.setDisable(textName4.getText().isEmpty());
        selectButton5.setDisable(textName5.getText().isEmpty());
        selectButton6.setDisable(textName6.getText().isEmpty());
    }

    private void setButtonVisibility(){
        employeeBox1.setVisible(!textName1.getText().isEmpty());
        employeeBox2.setVisible(!textName2.getText().isEmpty());
        employeeBox3.setVisible(!textName3.getText().isEmpty());
        employeeBox4.setVisible(!textName4.getText().isEmpty());
        employeeBox5.setVisible(!textName5.getText().isEmpty());
        employeeBox6.setVisible(!textName6.getText().isEmpty());

        setSelectButtonVisibility();

        prevButton.setDisable(paginationText.getText().equals("0"));
        nextButton.setDisable(paginationText.getText().equals(String.valueOf(maxPages)));
    }

    public void findBookingBranchTextIndex(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();

        if (clickedButton.equals(selectButton1)) {
            getBookingBranchDetails(0, event);
        } else if (clickedButton.equals(selectButton2)) {
            getBookingBranchDetails(1, event);
        } else if (clickedButton.equals(selectButton3)) {
            getBookingBranchDetails(2, event);
        } else if (clickedButton.equals(selectButton4)) {
            getBookingBranchDetails(3, event);
        } else if (clickedButton.equals(selectButton5)) {
            getBookingBranchDetails(4, event);
        } else if (clickedButton.equals(selectButton6)) {
            getBookingBranchDetails(5, event);
        }
    }


    private void setValueToText() {
        List<Text> branchTexts = List.of(textName1, textName2, textName3, textName4, textName5, textName6);

        maxPaginationText.setText(String.valueOf(maxPages));
        for (int i = 0; i < 6; i++) {
            int listIndex = index + i;
            if (listIndex < employeeList.size()) {
                branchTexts.get(i).setText(employeeList.get(listIndex).getName() + " " + employeeList.get(listIndex).getSurname());
            } else {
                branchTexts.get(i).setText("");
            }
        }
    }


    public void nextPage(){
        index += 6;
        paginationText.setText(String.valueOf(index / 6));
        setValueToText();
        setButtonVisibility();
    }

    public void prevPage(){
        index -= 6;
        paginationText.setText(String.valueOf(index / 6));
        setValueToText();
        setButtonVisibility();
    }
    private void switchScene(ActionEvent event, String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException ignored) {
        }
    }

    public void reloadPage(ActionEvent event) {
        switchScene(event, "account-admin-panel.fxml");
    }

    public void switchSceneToAdminPanelBranch(ActionEvent event) {
        switchScene(event, "account-admin-panel-branch.fxml");
    }

}
