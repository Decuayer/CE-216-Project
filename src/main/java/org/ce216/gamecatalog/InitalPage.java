package org.ce216.gamecatalog;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.List;

public class InitalPage {

    @FXML private TextField usernameField;
    @FXML private TextField passwordField;

    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private Button btClose;
    @FXML
    private Button btMin;

    protected double offsetX;
    protected double offsetY;


    public void switchScenetoMainMenu(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main-screen.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setTitle("Main Screen");
        stage.show();
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        stage.setX(visualBounds.getMinX());
        stage.setY(visualBounds.getMinY());
        stage.setWidth(visualBounds.getWidth());
        stage.setHeight(visualBounds.getHeight());

    }

    public void switchScenetoRegisterPage(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("register-page.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Register Page");
        stage.show();
    }

    public void CloseWindow(ActionEvent event) {
        Stage stageCurrent = (Stage) ((Node) event.getSource()).getScene().getWindow();
        btClose.getScene().getWindow();
        stageCurrent.close();
    }

    public void MinWindow(ActionEvent event) {
        Stage stageCurrent = (Stage) ((Node) event.getSource()).getScene().getWindow();
        btMin.getScene().getWindow();
        stageCurrent.setIconified(true);
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
    public void setOnMouseEnteredY() {
        btMin.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4);");
    }
    public void setOnMouseExitedY() {
        btMin.setStyle("-fx-background-color: transparent;");
    }
    public void setOnMouseEnteredX() {
        btClose.setStyle("-fx-background-color: rgba(196, 30, 58);");
    }
    public void setOnMouseExitedX() {
        btClose.setStyle("-fx-background-color: transparent;");
    }

    //login methodu
    public void loginButton(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if(username.isEmpty() || password.isEmpty()) {
            System.out.println("Username or Password is empty");
            return;
        }

        try {
            List<User> users = UserManager.getUsers();

            //kullanıcı var mı bakıyo
            User user = users.stream().filter(u -> u.getUsername().equals(username)).findFirst().orElse(null);

            if (user == null) {
                System.out.println("User not found");
                return;
            }

            if (user.getPasswordHash().equals(user.hashPassword(password))) {
                System.out.println("User logged in");
                switchScenetoMainMenu(event);
            } else {
                System.out.println("Password is wrong");
            }

        }  catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error logging in");
        }

    }
}
