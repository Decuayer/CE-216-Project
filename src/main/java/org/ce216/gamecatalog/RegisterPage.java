package org.ce216.gamecatalog;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// TODO
// Terminale kayıt oldunuz hata falan diye yazılan şeyler ya popup ekran şekilde çıkacak ya da menün içersinde bir yerde belirecek.



public class RegisterPage {
    @FXML private TextField registeragefield;
    @FXML private TextField usernameregisterfield;
    @FXML private TextField emailaddresfield;
    @FXML private TextField passwordregisterfield;
    @FXML private TextField passwordregisteragainfield;
    @FXML private ComboBox<String> registerselectionBox;
    //bunları jsona ekliycez her user için


    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private Button btCloseReg;
    @FXML
    private Button btMinReg;
    protected double offsetX;
    protected double offsetY;

    public void switchScenetoMainMenu(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main-screen.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setTitle("Main Screen");
        stage.show();
    }
    public void switchScenetoNextRegisterMenu(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("next-register-page.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Register Page");
        stage.show();
    }
    public void CloseWindow(ActionEvent event) {
        Stage stageCurrent = (Stage) ((Node) event.getSource()).getScene().getWindow();
        btCloseReg.getScene().getWindow();
        stageCurrent.close();
    }

    public boolean isAgeValid() {
        String ageText = registeragefield.getText().trim();


        if (!ageText.matches("\\d+")) {
            System.out.println("Yaş sadece rakamlardan oluşmalı.");
            return false;
        }

        int age = Integer.parseInt(ageText);


        if (age < 0 || age > 99) {
            System.out.println("Yaş 0 ile 99 arasında olmalı.");
            return false;
        }

        return true;
    }

    public void MinWindow(ActionEvent event) {
        Stage stageCurrent = (Stage) ((Node) event.getSource()).getScene().getWindow();
        btMinReg.getScene().getWindow();
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
        btMinReg.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4);");
    }
    public void setOnMouseExitedY() {
        btMinReg.setStyle("-fx-background-color: transparent;");
    }
    public void setOnMouseEnteredX() {
        btCloseReg.setStyle("-fx-background-color: rgba(196, 30, 58);");
    }
    public void setOnMouseExitedX() {
        btCloseReg.setStyle("-fx-background-color: transparent;");
    }

    //kayıt json methodu.
    public void registerButton(ActionEvent event) throws Exception {
        System.out.println("girdi");
        try {
            String username = usernameregisterfield.getText();
            String password = passwordregisterfield.getText();
            int age = Integer.parseInt(registeragefield.getText());
            String email  = emailaddresfield.getText();
            String favoriteGenre = registerselectionBox.getSelectionModel().getSelectedItem();

            List<Game> userLibrary = new ArrayList<>();
            List<Game> gameLibrary = new ArrayList<>();

            User newUser = new User (username,password,age,email,gameLibrary,userLibrary,favoriteGenre);

            List<User> users = FileHandler.loadFromJSONUsers();

            boolean userExists = users.stream().anyMatch(u -> u.getUsername().equalsIgnoreCase(username));
            if (userExists) {
                System.out.println("Username already exists");
                return;
            }

            users.add(newUser);
            // TODO
            // User Manager classı kaldırıldı GSON kütüphnaesi kaldırıldığı için
            // File Handler içerisine saveUsers() fonksiyonu yazılacak.
            // FONKSİYONUN İSMİ DÜZGÜN OLSUN => saveFromUsersToJSON()
            // UserManager.saveUsers(users); (KALDIRILDI).


            System.out.println("User added");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error occured");
        }
        switchScenetoMainMenu(event);
    }

}

