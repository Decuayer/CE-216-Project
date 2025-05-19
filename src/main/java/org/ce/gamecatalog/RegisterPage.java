package org.ce.gamecatalog;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class RegisterPage {
    @FXML private TextField registeragefield;
    @FXML private TextField usernameregisterfield;
    @FXML private TextField emailaddresfield;
    @FXML private TextField passwordregisterfield;
    @FXML private TextField passwordregisteragainfield;
    @FXML private ComboBox<String> registerselectionBox;


    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private Text registerText;

    public void switchScenetoMainMenu(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main-screen.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
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


    public void registerButton(ActionEvent event) throws Exception {
        try {
            String username = usernameregisterfield.getText();
            String password = passwordregisterfield.getText();
            String passwordConfirm = passwordregisteragainfield.getText();
            int age = Integer.parseInt(registeragefield.getText());
            String email  = emailaddresfield.getText();
            String favoriteGenre = registerselectionBox.getSelectionModel().getSelectedItem();

            if (username.isEmpty()) {
                setMessage("Username cannot be empty", Color.RED);
                return;
            }
            if (password.isEmpty() || passwordConfirm.isEmpty()) {
                setMessage("Password fields cannot be empty", Color.RED);
                return;
            }
            if (!password.equals(passwordConfirm)) {
                setMessage("Passwords do not match", Color.RED);
                return;
            }
            if (age < 0 || age > 149) {
                setMessage("Age must be between 0 and 149", Color.RED);
                return;
            }
            if (!isValidEmail(email)) {
                setMessage("Invalid email format", Color.RED);
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
                setMessage("Username already exists", Color.RED);
                return;
            }
            users.add(newUser);
            FileHandler.saveUsersToJSON(users);
            setMessage("User added successfully " + newUser.getUsername(), Color.GREEN);
            switchScenetoInitialPage(event);
        } catch (NumberFormatException nfe) {
            setMessage("Age must be a valid number", Color.RED);
        } catch (Exception e) {
            e.printStackTrace();
            setMessage("Error occured", Color.RED);
        }
    }

    private boolean isValidEmail(String email) {
        if (email == null) return false;
        email = email.toLowerCase();
        return email.matches("^[\\w\\.-]+@(hotmail|gmail)\\.com$");
    }


    private void setMessage(String message, Color color) {
        registerText.setText(message);
        registerText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        registerText.setFill(color);
    }

}

