package org.ce216.gamecatalog;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Screen;
import javafx.stage.Stage;

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

    @FXML
    private ObservableList<Game> gamesObservableList = FXCollections.observableArrayList();

    @FXML
    private Button logout;


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

        System.out.println("initialize: " + loggedInUser.getGameCatalog());


        gamesObservableList.setAll(loggedInUser.getGameCatalog());
        libraryGames.setItems(gamesObservableList);

        libraryGames.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            libraryPane.getChildren().clear();
            if (newValue != null) {
                Pane libraryPaneGame = createGameLibrary(newValue);  // Seçilen oyun bilgilerini sağ panelde güncelle
                libraryPane.getChildren().addAll(libraryPaneGame);
            }
        });

        logout.setOnAction(event -> {
            try {
                InitalPage.setLoggedInUser(null);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("inital-page.fxml"));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(loader.load());
                Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                double screenWidth = screenBounds.getWidth();
                double screenHeight = screenBounds.getHeight();
                stage.setWidth(420);
                stage.setHeight(420);
                stage.setResizable(true);
                stage.setX((screenWidth - stage.getWidth()) / 2);
                stage.setY((screenHeight - stage.getHeight()) / 2);
                stage.setScene(scene);
                stage.setTitle("Login Page");
                stage.setResizable(false);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Logout Failed");
                alert.setHeaderText(null);
                alert.setContentText("An error occurred while logging out.");
                alert.showAndWait();
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

        addLibraryButton.setOnAction(event -> {
            User loggedInUser = InitalPage.getLoggedInUser();

            boolean alreadyExists = loggedInUser.getGameCatalog().stream()
                    .anyMatch(g -> g.getSteamID().equals(game.getSteamID()));

            if (!alreadyExists) {
                loggedInUser.getGameCatalog().add(game);

                try {
                    FileHandler.addGameToUserCatalog(loggedInUser.getUsername(), game.getSteamID());
                } catch (IOException e) {
                    e.printStackTrace();
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText("Could not save game to file");
                    errorAlert.setContentText("An error occurred while saving the game to your library.");
                    errorAlert.showAndWait();
                    return;
                }
                refreshLibrary();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Game Added");
                alert.setHeaderText(null);
                alert.setContentText(game.getTitle() + " has been added to your library!");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Already in Library");
                alert.setHeaderText(null);
                alert.setContentText(game.getTitle() + " is already in your library.");
                alert.showAndWait();
            }
        });

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

        deleteLibraryButton.setOnAction(event -> {
            User loggedInUser = InitalPage.getLoggedInUser();

            boolean removed = loggedInUser.getGameCatalog().removeIf(g -> g.getSteamID().equals(game.getSteamID()));

            if (removed) {
                try {
                    FileHandler.removeGameFromUserCatalog(loggedInUser.getUsername(), game.getSteamID());
                } catch (IOException e) {
                    e.printStackTrace();
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText("Could not remove game from file");
                    errorAlert.setContentText("An error occurred while removing the game from your library.");
                    errorAlert.showAndWait();
                    return;
                }

                refreshLibrary();
                libraryPane.getChildren().clear();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Game Removed");
                alert.setHeaderText(null);
                alert.setContentText(game.getTitle() + " has been removed from your library.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Not Found");
                alert.setHeaderText(null);
                alert.setContentText("This game was not found in your library.");
                alert.showAndWait();
            }
        });


        return pane;
    }

    private TextFlow boldLabel(String label, String value) {
        Text labelText = new Text(label + ": ");
        labelText.setFont(Font.font("Arial", FontWeight.BOLD, 12));

        Text valueText = new Text(value);
        valueText.setFont(Font.font("Arial", FontWeight.NORMAL, 12));

        return new TextFlow(labelText, valueText);
    }

    private void refreshLibrary() {
        User loggedInUser = InitalPage.getLoggedInUser();
        gamesObservableList.setAll(loggedInUser.getGameCatalog());
    }


}
