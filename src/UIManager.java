import java.util.Arrays;
import java.util.List;
import javafx.application.Application;
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
        primaryStage.setTitle("Game Collection Manager");

        Button libraryButton = new Button("Library");
        libraryButton.setOnAction(e -> showLibrary());

        TableColumn<Game, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Game, String> developerColumn = new TableColumn<>("Developer");
        developerColumn.setCellValueFactory(new PropertyValueFactory<>("developer"));

        TableColumn<Game, Integer> yearColumn = new TableColumn<>("Release Year");
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("releaseYear"));

        tableView.getColumns().addAll(titleColumn, developerColumn, yearColumn);
        tableView.setItems(gameList);

        Button addButton = new Button("Add Game");
        addButton.setOnAction(e -> addGame());

        Button removeButton = new Button("Remove Selected");
        removeButton.setOnAction(e -> removeSelectedGame());

        HBox buttonBox = new HBox(10, addButton, removeButton, libraryButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox layout = new VBox(10, tableView, buttonBox);
        layout.setAlignment(Pos.CENTER);
        primaryStage.setScene(new Scene(layout, 600, 400));
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

        // Clear existing list
        gameList.clear();

        // Add some sample games
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

        tableView.refresh(); // Refresh the TableView to show new games
    }

}
