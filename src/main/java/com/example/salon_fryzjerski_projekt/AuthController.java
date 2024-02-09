package com.example.salon_fryzjerski_projekt;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.regex.Pattern;

public class AuthController{
    String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    String nameRegex = "^[A-Z][a-z]+$";
    String phoneRegex = "^\\d{9}$";
    Pattern namePattern = Pattern.compile(nameRegex);
    Pattern phonePattern = Pattern.compile(phoneRegex);

    @FXML private TextField loginField, nameField, surnameField, phoneField;
    @FXML private PasswordField passwordField, passwordRepeatField;
    @FXML private Label loginLabel, passwordLabel, passwordRepeatLabel, nameLabel, surnameLabel, phoneLabel;
    @FXML private Text loginDataErrorMsg;

    private String username, password, passwordRepeat, name, surname, phone;

    SharedDataModel data = SharedDataModel.getInstance();

    public void initialize() {
        loginDataErrorMsg.setVisible(false);
    }
    public void handleLoginAction(ActionEvent event) {
        if(!areAllObjectsNonNullForLogin()){
            return;
        }
        username = loginField.getText().trim();
        password = passwordField.getText().trim();

        loginDataErrorMsg.setVisible(false);
        if (!handleErrorsLogin()) {
            try {

                User user = Client.getUserData(RequestType.GetUserData,username,password);
                if (user.getAccount_type() != "") {
                    data.setName(user.getName());
                    data.setSurname(user.getSurname());
                    data.setUserId(user.getAccount_id());
                    data.setAccountType(user.getAccount_type());
                    data.setPhone(user.getPhoneNumber());

                    data.setIsLogged(true);
                    switchToHome(event);
                }
                else{
                    loginField.setText("");
                    passwordField.setText("");
                    loginDataErrorMsg.setVisible(true);
                }
                } catch (Exception e){
                    throw new RuntimeException(e);
                }
        }

    }
    public void handleRegisterAction(ActionEvent event) {
        if(!areAllObjectsNonNullForRegister()){
            return;
        }
        username = loginField.getText().trim();
        password = passwordField.getText().trim();
        passwordRepeat = passwordRepeatField.getText().trim();
        name = nameField.getText().trim();
        surname = surnameField.getText().trim();
        phone = phoneField.getText().trim();

        if (!handleErrorsRegister()) {
            try {
                Client.sendRequest(RequestType.UserData, username, password , name, surname, phone );

                data.setName(name);
                data.setSurname(surname);
                data.setPhone(phone);
                data.setAccountType("klient");
                data.setIsLogged(true);

                User client = Client.getUserData(RequestType.GetUserData,username,password);

                data.setUserId(client.getAccount_id());
                System.out.println(data.getUserId());

                switchToHome(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean areAllObjectsNonNullForRegister() {
        return loginField != null && passwordField != null && passwordRepeatField != null && loginLabel != null&&
                passwordLabel != null && passwordRepeatLabel != null && nameField != null && surnameField != null && phoneField != null &&
                nameLabel != null && surnameLabel != null && phoneLabel != null;
    }
    private boolean areAllObjectsNonNullForLogin() {
        return loginField != null && passwordField != null && loginLabel != null&&
                passwordLabel != null;
    }


    public boolean handleErrorsRegister(){
        passwordLabel.setText("");
        passwordRepeatLabel.setText("");
        loginLabel.setText("");
        phoneLabel.setText("");
        nameLabel.setText("");
        surnameLabel.setText("");
        boolean isError = false;

        if (!namePattern.matcher(name).matches()) {
            nameLabel.setText("Nieprawidłowa nazwa");
            isError = true;
        }

        if (!namePattern.matcher(surname).matches()) {
            surnameLabel.setText("Nieprawidłowa nazwa");
            isError = true;
        }

        if (!phonePattern.matcher(phone).matches()) {
            phoneLabel.setText("Nieprawidłowa nazwa");
            isError = true;
        }

        if (password.length() < 8){
            passwordLabel.setText("Hasło musi zawierać przynajmniej 8 znaków");
            isError = true;
        }

        if (!password.equals(passwordRepeat)) {
            passwordRepeatLabel.setText("Hasła nie są takie same");
            isError = true;
        }

        if (username.isEmpty()) {
            loginLabel.setText("Pole jest wymagane");
            isError = true;
        } else {
            String emailRegex = EMAIL_REGEX;
            Pattern pattern = Pattern.compile(emailRegex);
            if (!pattern.matcher(username).matches()) {
                loginLabel.setText("Nieprawidłowy format");
                isError = true;
            }
        }

        if (password.isEmpty()){
            passwordLabel.setText("Pole jest wymagane");
            isError = true;
        }

        if (passwordRepeat.isEmpty()){
            passwordRepeatLabel.setText("Pole jest wymagane");
            isError = true;
        }
        if (phone.isEmpty()){
            phoneLabel.setText("Pole jest wymagane");
            isError = true;
        } else {
            if (phone.length() != 9){
                phoneLabel.setText("Numer telefonu musi zawierać 9 cyfr");
                isError = true;
            }
        }

        if (name.isEmpty()){
            nameLabel.setText("Pole jest wymagane");
            isError = true;
        }

        if (surname.isEmpty()){
            surnameLabel.setText("Pole jest wymagane");
            isError = true;
        }

        return isError;
    }

    public boolean handleErrorsLogin() {
        passwordLabel.setText("");
        loginLabel.setText("");

        boolean isError = false;


        if (username.isEmpty()) {
            loginLabel.setText("Pole jest wymagane");
            isError = true;
        }

        if (password.isEmpty()) {
            passwordLabel.setText("Pole jest wymagane");
            isError = true;
        }

        return isError;
    }

    private void switchScene(ActionEvent event, String fxmlFile) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
    }

    public void switchToHome(ActionEvent event) throws IOException {
        switchScene(event, "hello-view.fxml");


    }

}