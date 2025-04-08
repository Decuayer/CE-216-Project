package org.ce216.gamecatalog;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;


public class MainScreenController {
    @FXML
    private Button testButton;
    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    protected double offsetX;
    protected double offsetY;
    @FXML
    private Button btCloseMain;
    @FXML
    private Button btMinMain;
    @FXML
    private Button btFullScrenMain;

    @FXML
    private void handleButtonClick() {
        System.out.println("Test button clicked!");
    }
    public void handleClickAction(MouseEvent event) {
        offsetX = event.getSceneX();
        offsetY = event.getSceneY();
    }
    public void handleMovementAction(MouseEvent event) {
        Stage stageCurrent = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stageCurrent.setX(event.getScreenX() - offsetX);
        stageCurrent.setY(event.getScreenY() - offsetY);
    }
    public void CloseWindow(ActionEvent event) {
        Stage stageCurrent = (Stage) ((Node) event.getSource()).getScene().getWindow();
        btCloseMain.getScene().getWindow();
        stageCurrent.close();
    }

    public void MinWindow(ActionEvent event) {
        Stage stageCurrent = (Stage) ((Node) event.getSource()).getScene().getWindow();
        btMinMain.getScene().getWindow();
        stageCurrent.setIconified(true);
    }
    public void setOnMouseEnteredY() {
        btMinMain.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4);");
    }
    public void setOnMouseExitedY() {
        btMinMain.setStyle("-fx-background-color: transparent;");
    }
    public void setOnMouseEnteredX() {
        btCloseMain.setStyle("-fx-background-color: rgba(196, 30, 58);");
    }
    public void setOnMouseExitedX() {
        btCloseMain.setStyle("-fx-background-color: transparent;");
    }
    public void setOnMouseEnteredA() {
        btFullScrenMain.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4);");
    }
    public void setOnMouseExitedA() {
        btFullScrenMain.setStyle("-fx-background-color: transparent;");
    }
    public void makeFullScren(ActionEvent event) {
        Stage stageCurrent = (Stage) ((Node) event.getSource()).getScene().getWindow();
        btFullScrenMain.getScene().getWindow();
        stageCurrent.setFullScreen(true);
    }
}
