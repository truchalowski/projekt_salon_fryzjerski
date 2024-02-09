package com.example.salon_fryzjerski_projekt;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.animation.FadeTransition;
import javafx.util.Duration;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class BookingController{

    @FXML private Button date1, date2, date3, date4, date5;
    @FXML private Button time1, time2, time3, time4, time5;
    @FXML private Button bookingSubmitButton;
    @FXML private Button leftArrow, rightArrow, leftArrowTime, rightArrowTime;
    @FXML private AnchorPane bookingTimesPane;
    @FXML private Label monthName;
    private int employeeId;
    private final LocalDate currentDate = LocalDate.now();
    private List<String> timeList;
    private List<LocalDate> dateList;
    private int currentDateListIndex = 0;
    private int currentTimeIndex = 0;

    private String lastSelectedDateButton = null;
    private String lastSelectedTimeButton = null;

    List<Button> allDateButtons = new ArrayList<>();
    List<Button> allTimeButtons = new ArrayList<>();

    SharedDataModel data = SharedDataModel.getInstance();
    int numberOfDaysToDisplay = 30;


    private List<String> getTimesToList(String date) {;
        List<String> timesList = new ArrayList<>();
        timesList = Client.getTimeSlots(RequestType.GetAvailableTimeSlotsForEmployee,employeeId,date);

        return timesList;
    }

    public void submitBookingButton(ActionEvent event) {
        if (lastSelectedDateButton != null && lastSelectedTimeButton != null) {
            try {

                data.setDateName(getDate());
                data.setTimeName(getTime());

                if (data.getIsLogged()){
                    switchToSceneBookingFinal(event);
                } else{
                    switchToSceneBookingUserInfo(event);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void initialize() {
        if (areAllObjectsNonNull()) {
            dateList = generateDateList(currentDate, numberOfDaysToDisplay);
            updateDateAndTimeButtons();
            initializeButtonSelection();
            setAllButtonsUnselected();
            employeeId = data.getEmployeeId();
            bookingTimesPane.setVisible(false);
            bookingSubmitButton.setVisible(false);
        }
    }

    private boolean areAllObjectsNonNull() {
        return date1 != null && date2 != null && date3 != null && date4 != null && date5 != null &&
                time1 != null && time2 != null && time3 != null && time4 != null && time5 != null &&
                bookingSubmitButton != null && leftArrow != null && rightArrow != null &&
                leftArrowTime != null && rightArrowTime != null && bookingTimesPane != null &&
                monthName != null;
    }


    private void updateDateAndTimeButtons() {
        updateDateButtons();
        updateTimeButtons();
        updateMonthLabel();
    }

    private List<LocalDate> generateDateList(LocalDate startDate, int days) {
        return IntStream.range(0, days).mapToObj(startDate::plusDays).collect(Collectors.toList());
    }

    private void initializeButtonSelection() {
        allDateButtons.add(date1);
        allDateButtons.add(date2);
        allDateButtons.add(date3);
        allDateButtons.add(date4);
        allDateButtons.add(date5);

        allTimeButtons.add(time1);
        allTimeButtons.add(time2);
        allTimeButtons.add(time3);
        allTimeButtons.add(time4);
        allTimeButtons.add(time5);

        allDateButtons.forEach(button -> button.setOnAction(event -> handleButtonSelection(button, allDateButtons)));
        allTimeButtons.forEach(button -> button.setOnAction(event -> handleButtonSelection(button, allTimeButtons)));
    }

    private void setAllButtonsUnselected() {
                List<Button> allButtons = List.of(date1, date2, date3, date4, date5, time1, time2, time3, time4, time5);
                for (Button button : allButtons) {
                        button.getStyleClass().add("booking-button-unselected");
                }
    }

    private void handleButtonSelection(Button button, List<Button> buttonsGroup) {
        if (buttonsGroup.equals(allDateButtons)) {
            lastSelectedDateButton = button.getText();
            timeList = getTimesToList(getDate());//Przypisanie listy godzin do dnia
            currentTimeIndex = 0;
            updateTimeButtons();
            animateVisibility(bookingTimesPane);
            lastSelectedTimeButton = null;
            bookingSubmitButton.setVisible(false);
            allTimeButtons.forEach(timeButton -> {
                timeButton.getStyleClass().removeAll("booking-button-selected", "booking-button-unselected");
                timeButton.getStyleClass().add("booking-button-unselected");
            });
        } else if (buttonsGroup.equals(allTimeButtons)) {
            if(lastSelectedTimeButton == null) {
                animateVisibility(bookingSubmitButton);
            }
            lastSelectedTimeButton = button.getText();
        }

        for (Button b : buttonsGroup) {
            b.getStyleClass().removeAll("booking-button-selected", "booking-button-unselected");
            b.getStyleClass().add("booking-button-unselected");
        }

        button.getStyleClass().removeAll("booking-button-selected", "booking-button-unselected");
        button.getStyleClass().add("booking-button-selected");
    }

    private void animateVisibility(Node node) {
        FadeTransition fade = new FadeTransition(Duration.millis(500), node);
        node.setVisible(true);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.play();
    }
    private void updateDateButtons() {

        if (dateList.size() >= currentDateListIndex + 5) {
            setDateButtonVisibilityAndText(date1, dateList.get(currentDateListIndex));
            setDateButtonVisibilityAndText(date2, dateList.get(currentDateListIndex + 1));
            setDateButtonVisibilityAndText(date3, dateList.get(currentDateListIndex + 2));
            setDateButtonVisibilityAndText(date4, dateList.get(currentDateListIndex + 3));
            setDateButtonVisibilityAndText(date5, dateList.get(currentDateListIndex + 4));
        }

        leftArrow.setDisable(currentDateListIndex == 0);
        rightArrow.setDisable(currentDateListIndex >= dateList.size() - 5);
    }

    private void updateTimeButtons() {
        setTimeButtonVisibilityAndText(time1, currentTimeIndex);
        setTimeButtonVisibilityAndText(time2, currentTimeIndex + 1);
        setTimeButtonVisibilityAndText(time3, currentTimeIndex + 2);
        setTimeButtonVisibilityAndText(time4, currentTimeIndex + 3);
        setTimeButtonVisibilityAndText(time5, currentTimeIndex + 4);

        if (timeList != null) {
            leftArrowTime.setDisable(currentTimeIndex == 0);
            rightArrowTime.setDisable(currentTimeIndex >= timeList.size() - 5);
        }
    }

    private String getTimeButtonText(int index) {
        if (timeList != null) {
            if (index >= 0 && index < timeList.size()) {
                return timeList.get(index);
            }
        }
        return "";
    }

    private String formatDate(LocalDate date) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE\ndd", Locale.forLanguageTag("pl-PL"));
            String formattedDate = date.format(formatter);

            String[] parts = formattedDate.split("\n");
            if (parts.length == 2) {
                String day = parts[0];
                String dayNumber = parts[1];

                if (day.length() > 1) {
                    day = day.substring(0, 1).toUpperCase(Locale.forLanguageTag("pl-PL")) + day.substring(1, day.length() - 1);
                }

                return day + "\n" + dayNumber;
            }

            return formattedDate;

    }

    private void setTimeButtonVisibilityAndText(Button button, int index) {
            String text = getTimeButtonText(index);
            button.setText(text);
            button.setVisible(!text.isEmpty());
    }

    private void setDateButtonVisibilityAndText(Button button, LocalDate date) {
            String text = formatDate(date);
            button.setText(text);
            button.setVisible(!text.isEmpty());
    }

    private void updateMonthLabel() {
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("LLLL", Locale.forLanguageTag("pl-PL"));

        LocalDate firstDate = dateList.get(currentDateListIndex);
        LocalDate lastDate = dateList.size() > currentDateListIndex + 4 ?
                dateList.get(currentDateListIndex + 4) : dateList.get(dateList.size() - 1);

        String firstMonth = firstDate.format(monthFormatter);
        String lastMonth = lastDate.format(monthFormatter);

        firstMonth = firstMonth.substring(0, 1).toUpperCase() + firstMonth.substring(1);
        lastMonth = lastMonth.substring(0, 1).toUpperCase() + lastMonth.substring(1);

        if (firstMonth.equals(lastMonth)) {
            monthName.setText(firstMonth);
        } else {
            monthName.setText(firstMonth + "-" + lastMonth);
        }
    }

    public void nextTime() {
        if (timeList != null) {
            if (currentTimeIndex < timeList.size() - 1) {
                currentTimeIndex += 5;
                updateDateAndTimeButtons();
            }
        }
        for (Button b : allTimeButtons) {
            if (b.getText().equals(lastSelectedTimeButton)) {
                b.getStyleClass().removeAll("booking-button-selected", "booking-button-unselected", "booking-button");
                b.getStyleClass().add("booking-button-selected");
            }
            else{
                b.getStyleClass().removeAll("booking-button-selected", "booking-button-unselected");
                b.getStyleClass().add("booking-button-unselected");
                b.getStyleClass().add("booking-button");
            }
        }

    }

    public void prevTime() {
        if (currentTimeIndex > 0) {
            currentTimeIndex -= 5;
            updateDateAndTimeButtons();
        }
        for (Button b : allTimeButtons) {
            if (b.getText().equals(lastSelectedTimeButton)) {
                b.getStyleClass().removeAll("booking-button-selected", "booking-button-unselected", "booking-button");
                b.getStyleClass().add("booking-button-selected");
            }
            else{
                b.getStyleClass().removeAll("booking-button-selected", "booking-button-unselected");
                b.getStyleClass().add("booking-button-unselected");
                b.getStyleClass().add("booking-button");
            }
        }
    }

    public void nextDate() {
        if (currentDateListIndex < dateList.size() - 5) {
            currentDateListIndex += 5;
            updateDateAndTimeButtons();

        }
        for (Button b : allDateButtons) {
            if (b.getText().equals(lastSelectedDateButton)) {
                b.getStyleClass().removeAll("booking-button-selected", "booking-button-unselected", "booking-button");
                b.getStyleClass().add("booking-button-selected");
            }
            else{
                b.getStyleClass().removeAll("booking-button-selected", "booking-button-unselected");
                b.getStyleClass().add("booking-button-unselected");
                b.getStyleClass().add("booking-button");
            }
        }
    }

    public void prevDate() {
        if (currentDateListIndex > 0) {
            currentDateListIndex -= 5;
            updateDateAndTimeButtons();
        }
        for (Button b : allDateButtons) {
            if (b.getText().equals(lastSelectedDateButton)) {
                b.getStyleClass().removeAll("booking-button-selected", "booking-button-unselected", "booking-button");
                b.getStyleClass().add("booking-button-selected");
            }
            else{
                b.getStyleClass().removeAll("booking-button-selected", "booking-button-unselected");
                b.getStyleClass().add("booking-button-unselected");
                b.getStyleClass().add("booking-button");
            }
        }
    }
    public String getDate() {
            String dayPart = lastSelectedDateButton.split("\n")[1];
            int day = Integer.parseInt(dayPart);

            LocalDate selectedDate = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), day);

           return selectedDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public String getTime() {
        if (lastSelectedTimeButton == null){
            return null;
        }
        return lastSelectedTimeButton;
    }

    public void goBackButton(ActionEvent event){
        switchToEmployee(event);
    }
    public void switchToEmployee(ActionEvent event) {
        switchScene(event, "booking-employee-view.fxml");
    }
    private void switchScene(ActionEvent event, String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException ignored) {
        }
    }
    public void switchToSceneBookingUserInfo(ActionEvent event) throws IOException {
        switchScene(event, "booking-user-info-view.fxml");
    }
    public void switchToSceneBookingFinal(ActionEvent event) throws IOException {
        switchScene(event, "booking-final-view.fxml");
    }
}