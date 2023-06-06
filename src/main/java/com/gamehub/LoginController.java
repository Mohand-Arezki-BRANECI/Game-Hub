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

public class LoginController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    Button signUpBtn, loginBtn;
    @FXML
    TextField email;
    @FXML
    PasswordField password;
    @FXML
    MenuItem quitGameHub;


    public void switchToSignUpScene(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("SignUp.fxml"));
        scene = new Scene(root);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }


    public void handleLoginBtn(ActionEvent event){
        String userEmail = email.getText();
        String userPassword = password.getText();

        Connection connection = null;
        DatabaseConnector databaseConnector = new DatabaseConnector();
        try {
            connection = databaseConnector.getConnection();

            ResultSet resultSet = databaseConnector.executeLoginQuery(connection, userEmail, userPassword);

            if(resultSet.next()){
                String pseudo = resultSet.getString("pseudo");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("GameHub.fxml"));
                root = loader.load();
                GameHubController gameHubController = loader.getController();
                gameHubController.displayUserPseudo("Welcome " + pseudo);
                scene = new Scene(root);
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            }else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid email or password");
                alert.show();
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }finally {
            if (connection != null) {
                try {
                    connection.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void handleQuitGameHub() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Exit");
        alert.setHeaderText("Are you sure you want to exit?");

        if (alert.showAndWait().get() == javafx.scene.control.ButtonType.OK) {
            Platform.exit();
        }
    }

}