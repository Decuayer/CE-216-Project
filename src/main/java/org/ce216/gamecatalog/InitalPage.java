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


// TODO
// Terminale giriş yaptınız veya hata falan diye yazılan şeyler ya popup ekran şekilde çıkacak ya da menün içersinde bir yerde belirecek.


public class InitalPage {

    // Giriş yapan kullanıcı bilgisi
    private static User loggedInUser;

    public static User getLoggedInUser() {
        return loggedInUser;
    }


    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Stage stage;
    @FXML
    private Scene scene;

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
            System.out.println("Username or Password is empty");
            return;
        }

        try {
            List<User> users = FileHandler.loadFromJSONUsers();

            // Username Control
            User user = users.stream().filter(u -> u.getUsername().equals(username)).findFirst().orElse(null);

            if (user == null) {
                System.out.println("User not found");
                return;
            }
            System.out.println("Sifre: 1234");

            if (user.getPasswordHash().equals(user.hashPassword(password))) {
                System.out.println("User logged in");
                loggedInUser = user;
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
