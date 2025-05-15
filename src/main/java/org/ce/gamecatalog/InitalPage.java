package org.ce.gamecatalog;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;


// TODO
// Terminale giriş yaptınız veya hata falan diye yazılan şeyler ya popup ekran şekilde çıkacak ya da menün içersinde bir yerde belirecek.


public class InitalPage {

    // Giriş yapan kullanıcı bilgisi
    private static User loggedInUser;

    public static User getLoggedInUser() {
        return loggedInUser;
    }
    public static void setLoggedInUser(User user) {
        loggedInUser = user;
    }


    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private Text loginText;

    public void switchScenetoMainMenu(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main-screen.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double screenWidth = screenBounds.getWidth();
        double screenHeight = screenBounds.getHeight();
        stage.setWidth(1300);
        stage.setHeight(850);
        stage.setResizable(true);
        // Ekranın ortasına açılmasını sağlıyor
        stage.setX((screenWidth - stage.getWidth()) / 2);
        stage.setY((screenHeight - stage.getHeight()) / 2);
        //
        stage.setTitle("Main Page");
        stage.setScene(scene);
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


    // Login Function
    public void loginButton(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if(username.isEmpty() || password.isEmpty()) {
            setMessage("Username or Password is empty", Color.RED);
            return;
        }

        try {
            List<User> users = FileHandler.loadFromJSONUsers();

            // Username Control
            User user = users.stream().filter(u -> u.getUsername().equals(username)).findFirst().orElse(null);

            if (user == null) {
                setMessage("User not found", Color.RED);
                return;
            }

            if (user.getPasswordHash().equals(user.hashPassword(password))) {
                setMessage("User logged in " + user.getUsername(), Color.GREEN);
                loggedInUser = user;
                PauseTransition pause = new PauseTransition(Duration.seconds(2));
                pause.setOnFinished(e -> {
                    try {
                        switchScenetoMainMenu(event);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
                pause.play();
            } else {
                setMessage("Password is wrong", Color.RED);
            }

        }  catch (IOException e) {
            e.printStackTrace();
            setMessage("Error logging in", Color.RED);
        }

    }

    private void setMessage(String message, Color color) {
        loginText.setText(message);
        loginText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        loginText.setFill(color);
    }



}
