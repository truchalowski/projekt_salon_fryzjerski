package com.example.salon_fryzjerski_projekt;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.Objects;

public class SceneController {
    @FXML Button helloButton;

    SharedDataModel data = SharedDataModel.getInstance();


    public void initialize(){
        String accountType = data.getAcccountType();
        if(Objects.equals(accountType, "admin") || Objects.equals(accountType, "pracownik")) {
            helloButton.setVisible(false);
        }
    }
    private void switchScene(ActionEvent event, String fxmlFile) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
    }

    public void switchToSceneBookingBranch(ActionEvent event) throws IOException {
        switchScene(event, "booking-branch-view.fxml");
    }
}
