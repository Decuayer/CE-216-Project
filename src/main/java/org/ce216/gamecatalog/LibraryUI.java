package org.ce216.gamecatalog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

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

//Used a listview instead of
public class LibraryUI extends Application {

    private TableView<Game> tableView = new TableView<>();
    //ObservableList<Game> gameList = FXCollections.observableArrayList();
    private ObservableList<Game> gameList = FXCollections.observableArrayList();
    private ListView<Game> gameListView = new ListView<>(gameList);
    public void start(Stage primaryStage) {

        primaryStage.setTitle("🎮 Game Collection Manager");
        Button libraryButton = new Button("📚 Library");
        libraryButton.setOnAction(e -> showLibrary());
        Button addButton = new Button("➕ Add Game");
        addButton.setOnAction(e -> addGame());
        Button removeButton = new Button("🗑️ Remove Selected");
        removeButton.setOnAction(e -> removeSelectedGame());
        Button resetButton = new Button("🔄 Reset Library");
        resetButton.setOnAction(e -> resetLibrary());
        HBox topBar = new HBox(libraryButton);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-padding: 10; -fx-background-color: #f0f0f0;");
        gameListView.setPrefWidth(200);
        gameListView.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Game game, boolean empty) {
                super.updateItem(game, empty);
                setText(empty || game == null ? null : game.getTitle());
            }
        });

        Label titleLabel = new Label();
        Label developerLabel = new Label();
        Label yearLabel = new Label();
        Label genreLabel = new Label();
        Label tagsLabel = new Label();
        Label playtimeLabel = new Label();

        VBox gameInfoBox = new VBox(10);
        gameInfoBox.getChildren().addAll(titleLabel, developerLabel, yearLabel, genreLabel, tagsLabel, playtimeLabel);
        gameInfoBox.setStyle("-fx-padding: 10;");
        gameInfoBox.setPrefWidth(400);

        gameListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                titleLabel.setText("🎮 Title: " + newVal.getTitle());
                developerLabel.setText("👨‍💻 Developer: " + newVal.getDeveloper());
                yearLabel.setText("📅 Release Year: " + newVal.getReleaseYear());
                genreLabel.setText("🎲 Genre: " + String.join(", ", newVal.getGenre()));
                tagsLabel.setText("🏷️ Tags: " + String.join(", ", newVal.getTags()));
            }
        });

        HBox contentBox = new HBox(gameListView, gameInfoBox);
        contentBox.setSpacing(20);
        contentBox.setStyle("-fx-padding: 15;");

        HBox bottomBar = new HBox(10, addButton, removeButton,resetButton);
        bottomBar.setAlignment(Pos.CENTER);
        bottomBar.setStyle("-fx-padding: 10;");

        VBox layout = new VBox(10, topBar, contentBox, bottomBar);
        layout.setStyle("-fx-padding: 20; -fx-font-family: 'Segoe UI'; -fx-background-color: #ffffff;");

        Scene scene = new Scene(layout, 700, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void addGame() {
        Game newGame = new Game("Example Game", Arrays.asList("RPG"), "Dev Studio", "Big Publisher",
                Arrays.asList("PC"), Arrays.asList("English"), "999999", 2024,
                10.5, "Digital", "English", 4.5, Arrays.asList("Action"), "cover.jpg");

        gameList.add(newGame);

        FileHandler.saveToJSON(gameList, "game.json");
    }


    private void removeSelectedGame() {
        Game selectedGame = gameListView.getSelectionModel().getSelectedItem();
        if (selectedGame != null) {
            gameList.remove(selectedGame);

            FileHandler.saveToJSON(new ArrayList<>(gameList), "game.json");
            System.out.println("Selected game has been removed");
        }
    }


    private void showLibrary() {
        System.out.println("Library button clicked!");

        gameList.clear();

        try {
            gameList.addAll(FileHandler.loadFromJSON("game.json"));
        } catch (IOException e) {
            System.err.println("Failed to load games: " + e.getMessage());Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load game list from file.");
        }

    }
    private void resetLibrary() {

        ObservableList<Game> defaultGames = FXCollections.observableArrayList(
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

        gameList.setAll(defaultGames);
        FileHandler.saveToJSON(new ArrayList<>(defaultGames), "game.json");
        System.out.println("Library reset to default.");
    }




}


