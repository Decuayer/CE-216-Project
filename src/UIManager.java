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
        primaryStage.setTitle("Game Collection Manager");

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

        HBox buttonBox = new HBox(10, addButton, removeButton);
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
}
