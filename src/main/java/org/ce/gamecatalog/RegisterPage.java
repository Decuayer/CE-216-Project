package org.ce.gamecatalog;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
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

