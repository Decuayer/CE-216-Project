package org.ce.gamecatalog;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// TODO
// Terminale kayıt oldunuz hata falan diye yazılan şeyler ya popup ekran şekilde çıkacak ya da menün içersinde bir yerde belirecek.



public class RegisterPage {
    @FXML
    private TextField registeragefield;
    @FXML
    private TextField usernameregisterfield;
    @FXML
    private TextField emailaddresfield;
    @FXML
    private TextField passwordregisterfield;
    @FXML
    private TextField passwordregisteragainfield;
    @FXML
    private ComboBox<String> registerselectionBox;
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
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setTitle("Main Screen");
        stage.show();
    }

    public void switchScenetoInitialPage(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/ce/gamecatalog/inital-page.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setTitle("Initial Page");
        stage.show();
    }


    //kayıt json methodu.
    public void registerButton(ActionEvent event) throws Exception {
        try {
            String username = usernameregisterfield.getText().trim();
            String password = passwordregisterfield.getText();
            String passwordConfirm = passwordregisteragainfield.getText();
            int age = Integer.parseInt(registeragefield.getText().trim());
            String email = emailaddresfield.getText().trim();
            String favoriteGenre = registerselectionBox.getSelectionModel().getSelectedItem();

            if (username.isEmpty()) {
                System.out.println("Username cannot be empty");
                return;
            }

            if (password.isEmpty() || passwordConfirm.isEmpty()) {
                System.out.println("Password fields cannot be empty");
                return;
            }

            if (!password.equals(passwordConfirm)) {
                System.out.println("Passwords do not match");
                return;
            }

            if (age < 0 || age > 149) {
                System.out.println("Age must be between 0 and 149");
                return;
            }

            if (!isValidEmail(email)) {
                System.out.println("Invalid email format");
                return;
            }

            List<Game> userLibrary = new ArrayList<>();
            List<Game> gameLibrary = new ArrayList<>();


            String hashedPassword = User.hashPassword(password);

            User newUser = new User(username, hashedPassword, age, email, gameLibrary, userLibrary, favoriteGenre);

            List<User> users = FileHandler.loadFromJSONUsers();


            users.removeIf(u -> u == null || u.getUsername() == null);

            boolean userExists = users.stream()
                    .anyMatch(u -> u.getUsername().equalsIgnoreCase(username));

            if (userExists) {
                System.out.println("Username already exists");
                return;
            }

            users.add(newUser);

            FileHandler.saveUsersToJSON(users);

            System.out.println("User added successfully");

            switchScenetoInitialPage(event);


        } catch (NumberFormatException nfe) {
            System.out.println("Age must be a valid number");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error occurred");
        }
    }

    private boolean isValidEmail(String email) {
        if (email == null) return false;
        email = email.toLowerCase();
        return email.matches("^[\\w\\.-]+@(hotmail|gmail)\\.com$");
    }
}


