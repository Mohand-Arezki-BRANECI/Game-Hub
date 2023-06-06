package com.gamehub;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class GameHubController {
    @FXML
    Button disconnect, snakeGame;
    @FXML
    Label userPseudoLabel;

    public void displayUserPseudo(String userPseudo){
        userPseudoLabel.setText(userPseudo);
    }


    public void handleDisconnectBtn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    public void handleSnakeGameBtn() throws IOException {
        Snake snakeGame = new Snake();
        snakeGame.start(new Stage());
    }

}
