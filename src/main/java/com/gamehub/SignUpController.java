package com.gamehub;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class SignUpController {

    @FXML
    Button createAccountBtn, loginBtn;
    @FXML
    TextField email, pseudo;
    @FXML
    PasswordField password, cpassword;
    @FXML

    public void switchToLoginScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void handleQuitGameHub() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Exit");
        alert.setHeaderText("Are you sure you want to exit?");

        if (alert.showAndWait().get() == javafx.scene.control.ButtonType.OK) {
            Platform.exit();
        }
    }

    public boolean isEmpty(String string){
        return string.equals("");
    }
    public void handleCreateAccountBtn(){

        String userEmail = email.getText();
        String userPseudo = pseudo.getText();
        String userPassword = password.getText();
        String copyUserPassword = cpassword.getText();

        if (isEmpty(userEmail) || isEmpty(userPseudo) || isEmpty(userPassword)){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("At least one field is empty");
            alert.show();
        }
        else if (userPassword.equals(copyUserPassword)) {
            Connection connection = null;
            DatabaseConnector databaseConnector = new DatabaseConnector();
            try {
                connection = databaseConnector.getConnection();

                int row = databaseConnector.executeSignupQuery(connection, userPseudo, userEmail, userPassword);
                if(row == 1){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Account created");
                    alert.setHeaderText("Your account has been created!");
                    alert.show();

                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    try {
                        connection.close();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("Passwords don't match");
            alert.show();

        }

    }


}





