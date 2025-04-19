import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;


public class UIManager extends Application {
    private TableView<Game> tableView = new TableView<>();
    private ObservableList<Game> gameList = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        System.out.println("UI is being initialized...");
        primaryStage.setTitle("🎮 Game Collection Manager");

        // === Buttons ===
        Button libraryButton = new Button("📚 Library");
        libraryButton.setOnAction(e -> showLibrary());

        Button addButton = new Button("➕ Add Game");
        addButton.setOnAction(e -> addGame());

        Button removeButton = new Button("🗑️ Remove Selected");
        removeButton.setOnAction(e -> removeSelectedGame());

        HBox topBar = new HBox(libraryButton);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setSpacing(10);
        topBar.setStyle("-fx-padding: 10; -fx-background-color: #f4f4f4;");


        TableColumn<Game, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Game, String> developerColumn = new TableColumn<>("Developer");
        developerColumn.setCellValueFactory(new PropertyValueFactory<>("developer"));

        TableColumn<Game, Integer> yearColumn = new TableColumn<>("Release Year");
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("releaseYear"));

        tableView.getColumns().addAll(titleColumn, developerColumn, yearColumn);
        tableView.setItems(gameList);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setPlaceholder(new Label("No games in library."));
        tableView.setStyle("-fx-font-size: 13px;");

        // === Bottom buttons ===
        HBox bottomBar = new HBox(10, addButton, removeButton);
        bottomBar.setAlignment(Pos.CENTER);
        bottomBar.setStyle("-fx-padding: 10;");

        // === Layout ===
        VBox layout = new VBox(15, topBar, tableView, bottomBar);
        layout.setStyle("-fx-padding: 20; -fx-background-color: white; -fx-font-family: 'Segoe UI';");

        Scene scene = new Scene(layout, 700, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void addGame() {
        Game newGame = new Game("Example Game", Arrays.asList("RPG"), "Dev Studio", "Big Publisher",
                Arrays.asList("PC"), Arrays.asList("English"), "123456", 2024,
                10.5, "Digital", "English", 4.5, Arrays.asList("Action"), "cover.jpg");
        gameList.add(newGame);
    }

    private void removeSelectedGame() {
        Game selectedGame = tableView.getSelectionModel().getSelectedItem();
        if (selectedGame != null) {
            gameList.remove(selectedGame);
        }
    }
    private void showLibrary() {
        System.out.println("Library button clicked!"); // Debugging

        gameList.clear();

        gameList.addAll(
                new Game("The Witcher 3", Arrays.asList("RPG"), "CD Projekt Red", "CD Projekt",
                        Arrays.asList("PC", "PS4"), Arrays.asList("English"), "123456", 2015,
                        150.0, "Digital", "English", 4.9, Arrays.asList("Open World", "Fantasy"), "witcher3.jpg"),

                new Game("Cyberpunk 2077", Arrays.asList("RPG"), "CD Projekt Red", "CD Projekt",
                        Arrays.asList("PC", "PS5"), Arrays.asList("English"), "789012", 2020,
                        80.0, "Digital", "English", 4.2, Arrays.asList("Sci-Fi", "Open World"), "cyberpunk2077.jpg"),

                new Game("Elden Ring", Arrays.asList("Action RPG"), "FromSoftware", "Bandai Namco",
                        Arrays.asList("PC", "Xbox"), Arrays.asList("English"), "345678", 2022,
                        120.0, "Digital", "English", 4.8, Arrays.asList("Soulslike", "Open World"), "eldenring.jpg")
        );

        tableView.refresh();
    }

   /* private void updateCoverImage(Game game) {
        if (game != null && game.getCoverImagePath() != null) {
            try {
                Image image = new Image(Files.newInputStream(Paths.get("covers/" + game.getCoverImagePath())));
                coverImageView.setImage(image);
            } catch (IOException e) {
                System.err.println("Image load failed: " + e.getMessage());
                coverImageView.setImage(null); // or load a default fallback image
            }
        } else {
            coverImageView.setImage(null);
        }
        }
    */
}
