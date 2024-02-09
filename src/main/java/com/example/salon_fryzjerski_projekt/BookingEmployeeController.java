package com.example.salon_fryzjerski_projekt;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.net.URL;
import java.io.InputStream;


public class BookingEmployeeController {
    @FXML private Text employeeText1, employeeText2, employeeText3;
    @FXML private Button bookingEmployeePrevButton, bookingEmployeeNextButton;
    @FXML private ImageView employeeImage1, employeeImage2, employeeImage3;
    @FXML private Button employeeSelectButton1, employeeSelectButton2, employeeSelectButton3, randomEmployee;
    @FXML private Text bookingEmployeeMaxPaginationText, bookingEmployeePaginationText;

    private List<Employee> employees = new ArrayList<>();
    int index = 0;
    int maxPages = 0;

    SharedDataModel data = SharedDataModel.getInstance();

    String picturesPath = "/com/example/salon_fryzjerski_projekt/pictures/";
    String defaultImg = "male-avatar.png";

    public void getBookingTypeDetails(int buttonIndex, ActionEvent event) {
        int listIndex = index + buttonIndex;
        String employeeName;
        int id;
        try {
            if (listIndex < employees.size()) {
                employeeName = employees.get(listIndex).getName() +  " " + employees.get(listIndex).getSurname();
                id = employees.get(listIndex).getAccount_id();

                data.setEmployeeId(id);
                data.setEmployeeName(employeeName);
            }
            switchToSceneBooking(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void getDataToTextList(){
        employees = Client.getEmployee(RequestType.GetEmployeeByBranch,data.getBranchName());
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void initialize(){
        if (areAllObjectsNonNull()) {
            getDataToTextList();
            setValueToElements();
            maxPages = (int) Math.ceil((double) employees.size() / 3) - 1;
            bookingEmployeeMaxPaginationText.setText(String.valueOf(maxPages));
            setButtonVisibility();
            //helloButton
        }
    }

    private boolean areAllObjectsNonNull() {
        return employeeText1 != null && employeeText2 != null && employeeText3 != null &&
                bookingEmployeePrevButton != null && bookingEmployeeNextButton != null &&
                employeeImage1 != null && employeeImage2 != null && employeeImage3 != null &&
                employeeSelectButton1 != null && employeeSelectButton2 != null && employeeSelectButton3 != null &&
                bookingEmployeeMaxPaginationText != null && bookingEmployeePaginationText != null &&
                randomEmployee != null;
    }
    private void setButtonVisibility() {
        setVisibilityBasedOnText(employeeText1, employeeSelectButton1, employeeImage1);
        setVisibilityBasedOnText(employeeText2, employeeSelectButton2, employeeImage2);
        setVisibilityBasedOnText(employeeText3, employeeSelectButton3, employeeImage3);

        bookingEmployeePrevButton.setDisable(bookingEmployeePaginationText.getText().equals("0"));
        bookingEmployeeNextButton.setDisable(bookingEmployeePaginationText.getText().equals(String.valueOf(maxPages)));
    }

    private void setVisibilityBasedOnText(Text textComponent, Button button, ImageView image) {
        boolean isVisible = !textComponent.getText().isEmpty();
        button.setVisible(isVisible);
        image.setVisible(isVisible);
    }
    private void setValueToElements() {
        List<Text> employeeTexts = List.of(employeeText1, employeeText2, employeeText3);
        List<ImageView> employeeImages = List.of(employeeImage1, employeeImage2, employeeImage3);

        for (int i = 0; i < 3; i++) {
            int listIndex = index + i;
            if (listIndex < employees.size()) {
                employeeTexts.get(i).setText(employees.get(listIndex).getName() + " " + employees.get(listIndex).getSurname());
            } else {
                employeeTexts.get(i).setText("");
            }

            if (listIndex < employees.size()) {
                String fullImagePath = picturesPath + employees.get(listIndex).getImagePath();
                try {
                    URL imageUrl = getClass().getResource(fullImagePath);
                    Image image = imageUrl != null ? new Image(imageUrl.openStream()) : null;
                    if (image == null || image.isError()) {
                        InputStream defaultStream = getClass().getResourceAsStream(picturesPath + defaultImg);
                        if (defaultStream != null) {
                            image = new Image(defaultStream);
                            if (image.isError()) {
                                image = null;
                            }
                        } else {
                            image = null;
                        }
                    }
                    employeeImages.get(i).setImage(image);
                } catch (IOException e) {
                    employeeImages.get(i).setImage(null);
                    throw new RuntimeException(e);
                }
            } else {
                employeeImages.get(i).setImage(null);
            }
        }
    }

    public void nextPage(){
        index += 3;
        bookingEmployeePaginationText.setText(String.valueOf(index / 3));
        setValueToElements();
        setButtonVisibility();
    }

    public void prevPage(){
        index -= 3;
        bookingEmployeePaginationText.setText(String.valueOf(index / 3));
        setValueToElements();
        setButtonVisibility();
    }
    public void findBookingTypeTextIndex(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();

        if (clickedButton.equals(employeeSelectButton1)) {
            getBookingTypeDetails(0, event);
        } else if (clickedButton.equals(employeeSelectButton2)) {
            getBookingTypeDetails(1, event);
        } else if (clickedButton.equals(employeeSelectButton3)) {
            getBookingTypeDetails(2, event);
        }
    }

    public void goBackButton(ActionEvent event){
        switchToBookingType(event);
    }
    public void switchToBookingType(ActionEvent event) {
        switchScene(event, "booking-type-view.fxml");
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
    public void switchToSceneBooking(ActionEvent event) throws IOException {
        switchScene(event, "booking-view.fxml");
    }
    public void handleRandomEmployeeClick(ActionEvent event) {
            List<Employee> randomEmployee = Client.getEmployee(RequestType.GetOneEmployeeByBranch,data.getBranchName());
            data.setUserId(randomEmployee.get(0).getAccount_id());
            data.setEmployeeName(randomEmployee.get(0).getName() + " " + randomEmployee.get(0).getSurname());
            switchScene(event, "booking-view.fxml");
    }
}
