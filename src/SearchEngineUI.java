import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class SearchEngineUI extends Application {
    private TextField searchField;
    private ListView<String> resultsList;
    private ObservableList<String> displayedResults;

    private List<Game> allGames = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Game Library Search");


        searchField = new TextField();
        searchField.setPromptText("Type to search for a game...");


        displayedResults = FXCollections.observableArrayList();
        resultsList = new ListView<>(displayedResults);


        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> performSearch());


        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(searchField, searchButton, resultsList);

        //this calls performSearch on every typing(real time search)
        searchField.textProperty().addListener((observable, oldValue, newValue) -> performSearch());


        Scene scene = new Scene(layout, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void performSearch() {
        String query = searchField.getText().trim();

        List<Game> results = SearchEngine.searchByTitle(allGames, query); //searchEngine here


        displayedResults.clear();
        for (Game game : results) {
            displayedResults.add(game.getTitle()); //add the game to display results
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
