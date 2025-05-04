package org.ce216.gamecatalog;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GeneralController implements Initializable {

    @FXML
    private HBox gamesContainer;
    @FXML
    private ListView<Game> libraryGames;
    @FXML
    private VBox libraryPane;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Login Control
        User loggedInUser = InitalPage.getLoggedInUser();

        // STORE SECTION
        FileHandler fh = new FileHandler();
        List<Game> gameList;
        try {
            gameList = fh.loadFromJSONGames();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for(Game game: gameList) {
            Pane gamePane = createGamePane(game);
            gamesContainer.getChildren().add(gamePane);
        }

        // LIBRARY SECTION
        List<Game> userGameList = loggedInUser.getGameCatalog();

        ObservableList<Game> gamesObservableList = FXCollections.observableArrayList(userGameList);
        libraryGames.setItems(gamesObservableList);

        libraryGames.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            libraryPane.getChildren().clear();
            if (newValue != null) {
                Pane libraryPaneGame = createGameLibrary(newValue);  // Seçilen oyun bilgilerini sağ panelde güncelle
                libraryPane.getChildren().addAll(libraryPaneGame);
            }
        });

    }

    private Pane createGamePane(Game game) {
        VBox pane = new VBox(30);
        pane.setPrefWidth(400);
        pane.setPadding(new Insets(15));
        pane.setStyle("-fx-background-color: #fff; -fx-border-color: #ccc; -fx-border-radius: 10; -fx-background-radius: 10;");

        // For Photo
        VBox firstPane = new VBox();
        firstPane.setPrefHeight(120);
        firstPane.setMaxHeight(120);
        firstPane.setMinHeight(120);
        firstPane.setAlignment(Pos.CENTER);

        // Photo
        Image image = new Image(getClass().getResourceAsStream(game.getCoverImagePath()), 200, 150, true, true);
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);

        // For Others
        VBox secondPane = new VBox(20);

        // Title
        VBox titleBox = new VBox();
        Label titleLabel = new Label(game.getTitle());
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        // Description
        VBox descBox = new VBox(5);
        Label descTitle = new Label("Description");
        descTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        Text descriptionText = new Text(game.getDescription());
        descriptionText.setWrappingWidth(370);
        TextFlow descriptionFlow = new TextFlow(descriptionText);

        // Infromations
        VBox infoBox = new VBox(2);
        infoBox.getChildren().addAll(
                boldLabel("Genre", String.join(", ", game.getGenre())),
                boldLabel("Developer", game.getDeveloper()),
                boldLabel("Publisher", game.getPublisher()),
                boldLabel("Platforms", String.join(", ", game.getPlatforms())),
                boldLabel("Translators", String.join(", ", game.getTranslators())),
                boldLabel("Steam ID", game.getSteamID()),
                boldLabel("Release Year", String.valueOf(game.getReleaseYear())),
                boldLabel("Playtime", game.getPlaytime() + " hrs"),
                boldLabel("Format", game.getFormat()),
                boldLabel("Language", game.getLanguage()),
                boldLabel("Rating", game.getRating() + "/10"),
                boldLabel("Tags", String.join(", ", game.getTags()))
        );


        // Bottom Buttons
        Button detailButton = new Button("Detail");
        detailButton.setPrefSize(140, 46);
        detailButton.setFont(Font.font(16));
        Button addLibraryButton = new Button("Add Library");
        addLibraryButton.setPrefSize(140, 46);
        addLibraryButton.setFont(Font.font(16));
        HBox buttonBox = new HBox(20);
        buttonBox.setLayoutX(60);
        buttonBox.setLayoutY(496);
        buttonBox.setAlignment(Pos.CENTER);



        titleBox.getChildren().addAll(titleLabel);
        descBox.getChildren().addAll(descTitle, descriptionFlow);
        buttonBox.getChildren().addAll(detailButton, addLibraryButton);

        firstPane.getChildren().addAll(imageView);
        secondPane.getChildren().addAll(titleBox, descBox,infoBox,buttonBox);

        pane.getChildren().addAll(firstPane, secondPane);

        return pane;
    }


    private Pane createGameLibrary(Game game) {
        VBox pane = new VBox(30);
        pane.setPadding(new Insets(15));
        pane.setStyle("-fx-background-color: #fff; -fx-border-color: #ccc;");



        // For Photo
        VBox firstPane = new VBox();
        firstPane.setPrefHeight(Control.USE_COMPUTED_SIZE);
        firstPane.setMaxHeight(Double.MAX_VALUE);
        firstPane.setMinHeight(0);
        firstPane.setAlignment(Pos.CENTER);

        // Photo
        Image image = new Image(getClass().getResourceAsStream(game.getCoverImagePath()), 200, 150, true, true);
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);

        // For Others
        VBox secondPane = new VBox(20);

        // Title
        VBox titleBox = new VBox();
        Label titleLabel = new Label(game.getTitle());
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        // Description
        VBox descBox = new VBox(5);
        Label descTitle = new Label("Description");
        descTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        Text descriptionText = new Text(game.getDescription());
        descriptionText.setWrappingWidth(370);
        TextFlow descriptionFlow = new TextFlow(descriptionText);

        // Infromations
        VBox infoBox = new VBox(2);
        infoBox.getChildren().addAll(
                boldLabel("Genre", String.join(", ", game.getGenre())),
                boldLabel("Developer", game.getDeveloper()),
                boldLabel("Publisher", game.getPublisher()),
                boldLabel("Platforms", String.join(", ", game.getPlatforms())),
                boldLabel("Translators", String.join(", ", game.getTranslators())),
                boldLabel("Steam ID", game.getSteamID()),
                boldLabel("Release Year", String.valueOf(game.getReleaseYear())),
                boldLabel("Playtime", game.getPlaytime() + " hrs"),
                boldLabel("Format", game.getFormat()),
                boldLabel("Language", game.getLanguage()),
                boldLabel("Rating", game.getRating() + "/10"),
                boldLabel("Tags", String.join(", ", game.getTags()))
        );


        // Bottom Buttons
        Button detailButton = new Button("Detail");
        detailButton.setPrefSize(140, 46);
        detailButton.setFont(Font.font(16));
        Button deleteLibraryButton = new Button("Delete Library");
        deleteLibraryButton.setPrefSize(140, 46);
        deleteLibraryButton.setFont(Font.font(16));
        HBox buttonBox = new HBox(20);
        buttonBox.setLayoutX(60);
        buttonBox.setLayoutY(496);
        buttonBox.setAlignment(Pos.CENTER);



        titleBox.getChildren().addAll(titleLabel);
        descBox.getChildren().addAll(descTitle, descriptionFlow);
        buttonBox.getChildren().addAll(detailButton, deleteLibraryButton);

        firstPane.getChildren().addAll(imageView);
        secondPane.getChildren().addAll(titleBox, descBox,infoBox,buttonBox);

        pane.getChildren().addAll(firstPane, secondPane);

        return pane;
    }

    private TextFlow boldLabel(String label, String value) {
        Text labelText = new Text(label + ": ");
        labelText.setFont(Font.font("Arial", FontWeight.BOLD, 12));

        Text valueText = new Text(value);
        valueText.setFont(Font.font("Arial", FontWeight.NORMAL, 12));

        return new TextFlow(labelText, valueText);
    }


}
