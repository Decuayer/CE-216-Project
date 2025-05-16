package org.ce.gamecatalog;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.Scene;


public class UIManager extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/ce/gamecatalog/inital-page.fxml"));
            Parent root = loader.load();
            Image icon = new Image(getClass().getResourceAsStream("/org/ce/gamecatalog/images/logo.png"));
            primaryStage.getIcons().add(icon);
            primaryStage.setTitle("Login Page");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

