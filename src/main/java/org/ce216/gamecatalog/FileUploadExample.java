package org.ce216.gamecatalog;
import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class FileUploadExample extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Button to open the file chooser
        Button openButton = new Button("Open File");

        // ImageView to display the selected image
        ImageView imageView = new ImageView();
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);

        openButton.setOnAction(e -> {
            // Create a FileChooser
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));

            // Show open file dialog
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                // Define the path to the resources/image directory
                String imagePath = "src/main/resources/org/ce216/gamecatalog/images/"; // Replace with actual relative path

                // Ensure the directory exists
                File directory = new File(imagePath);
                if (!directory.exists()) {
                    directory.mkdirs(); // Create directory if it doesn't exist
                }

                // Copy the file to the resources/image directory
                try {
                    File destination = new File(imagePath + file.getName());
                    Files.copy(file.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("File copied to: " + destination.getAbsolutePath());

                    // Load the image into the ImageView
                    Image image = new Image("file:" + destination.getAbsolutePath());
                    imageView.setImage(image);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        VBox vbox = new VBox(openButton, imageView);
        Scene scene = new Scene(vbox, 400, 300);
        primaryStage.setTitle("File Upload Example");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


