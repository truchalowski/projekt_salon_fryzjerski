package com.example.salon_fryzjerski_projekt;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class topNavbarController {
    @FXML private Button loginNavButton, registerNavButton;
    @FXML private MenuButton profileMenuButton;
    @FXML private MenuItem profileMenuItem1, profileMenuItem2;
    @FXML private HBox logRegBox;

    String name, surname;
    boolean isLogged;

    SharedDataModel data = SharedDataModel.getInstance();

    public void setNameValues(){
        name = data.getName();
        surname = data.getSurname();
    }

    public void initialize() {
        isLogged = data.getIsLogged();
        if (areAllObjectsNonNull()) {
            updateButtonVisibility();
            setNameValues();
            profileMenuButton.setText(name + " " + surname);
        }
    }
    public void handleLogOut(ActionEvent event) {
        try {
            data.setIsLogged(false);
            data.setAccountType("");
            switchSceneLogout(profileMenuItem2, "hello-view.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean areAllObjectsNonNull() {
        return profileMenuButton != null && loginNavButton != null && registerNavButton != null &&
                profileMenuItem1 != null && profileMenuItem2 != null;
    }
    private void updateButtonVisibility() {
        loginNavButton.setVisible(!isLogged);
        loginNavButton.setDisable(isLogged);
        registerNavButton.setVisible(!isLogged);
        registerNavButton.setDisable(isLogged);
        logRegBox.setVisible(!isLogged);
        logRegBox.setDisable(isLogged);
        profileMenuButton.setVisible(isLogged);
        profileMenuButton.setDisable(!isLogged);
    }
    private void switchScene(ActionEvent event, String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException ignored) {
        }
    }
    private void switchSceneLogout(MenuItem menuItem, String fxmlFile) throws IOException {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage stage = (Stage) menuItem.getParentPopup().getOwnerWindow();
            stage.getScene().setRoot(root);
        } catch (IOException ignored) {
        }
    }

    public void switchToAccountDetails(ActionEvent event) {
        String accountType = data.getAcccountType();
        if(Objects.equals(accountType, "pracownik") || Objects.equals(accountType, "klient")) {
            if (event.getSource() instanceof MenuItem) {
                try {
                    switchSceneFromMenuItem((MenuItem) event.getSource(), "account-booking-history.fxml");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } else{
            if (event.getSource() instanceof MenuItem) {
                try {
                    switchSceneFromMenuItem((MenuItem) event.getSource(), "account-admin-panel.fxml");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void switchSceneFromMenuItem(MenuItem menuItem, String fxmlFile) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
        Stage stage = (Stage) menuItem.getParentPopup().getOwnerWindow();
        stage.getScene().setRoot(root);
    }

    public void switchToSceneHello(ActionEvent event) throws IOException {
        switchScene(event, "hello-view.fxml");
    }

    public void switchToSceneLogin(ActionEvent event) throws IOException {
        switchScene(event, "login-view.fxml");
    }

    public void switchToSceneRegister(ActionEvent event) throws IOException {
        switchScene(event, "register-view.fxml");
    }

}
