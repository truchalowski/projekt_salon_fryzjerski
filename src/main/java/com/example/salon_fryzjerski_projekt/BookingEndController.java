package com.example.salon_fryzjerski_projekt;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.application.Platform;

import java.io.IOException;

public class BookingEndController {
    public void handleHelloButton(ActionEvent event){
        try{
            switchToSceneHello(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void handleExitButton(){
        Platform.exit();
    }
    private void switchScene(ActionEvent event, String fxmlFile) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
    }

    public void switchToSceneHello(ActionEvent event) throws IOException {
        switchScene(event, "hello-view.fxml");
    }
}
