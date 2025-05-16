package org.ce.gamecatalog;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class SearchEngineUI extends Application {
    private TextField searchField;
    private ListView<String> resultsList;
    private ObservableList<String> displayedResults;

    private List<Game> allGames = new ArrayList<>();
    private ComboBox<String> genreComboBox;
    private ComboBox<String> tagComboBox;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Game Library Search");

        searchField = new TextField();
        searchField.setPromptText("Type to search for a game...");
        displayedResults = FXCollections.observableArrayList();
        resultsList = new ListView<>(displayedResults);


        genreComboBox = new ComboBox<>();
        genreComboBox.setPromptText("Select Genre");
        genreComboBox.setEditable(false);


        tagComboBox = new ComboBox<>();
        tagComboBox.setPromptText("Select Tag");
        tagComboBox.setEditable(false);


        loadFilters();

        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> performSearch());
        performSearch();

        searchField.textProperty().addListener((observable, oldValue, newValue) -> performSearch());
        genreComboBox.valueProperty().addListener((observable, oldValue, newValue) -> performSearch());
        tagComboBox.valueProperty().addListener((observable, oldValue, newValue) -> performSearch());


        HBox filterBox = new HBox(10);
        filterBox.getChildren().addAll(genreComboBox, tagComboBox);

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        HBox searchBox = new HBox(10);
        searchBox.getChildren().addAll(searchField, searchButton);

        layout.getChildren().addAll(searchBox, filterBox, resultsList);

        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadFilters() {
        try {
            allGames = FileHandler.loadFromJSONGames();


            Set<String> genres = new HashSet<>();
            Set<String> tags = new HashSet<>();
            for (Game game : allGames) {
                genres.addAll(game.getGenre());
                tags.addAll(game.getTags());
            }

            genreComboBox.setItems(FXCollections.observableArrayList(genres));
            tagComboBox.setItems(FXCollections.observableArrayList(tags));
        } catch (IOException e) {
            System.err.println("Failed to load games: " + e.getMessage());
        }
    }

    private void performSearch() {
        String query = searchField.getText().trim().toLowerCase();
        String selectedGenre = genreComboBox.getValue();
        String selectedTag = tagComboBox.getValue();


        List<Game> results = allGames.stream()
                .filter(game -> game.getTitle().toLowerCase().contains(query))
                .filter(game -> selectedGenre == null || game.getGenre().contains(selectedGenre))
                .filter(game -> selectedTag == null || game.getTags().contains(selectedTag))
                .collect(Collectors.toList());

        displayedResults.clear();
        for (Game game : results) {
            displayedResults.add(game.getTitle()); //add the game to display results
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}