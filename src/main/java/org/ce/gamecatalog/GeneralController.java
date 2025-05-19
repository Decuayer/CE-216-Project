package org.ce.gamecatalog;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

public class GeneralController implements Initializable {

    @FXML
    private HBox gamesContainerSearch;
    @FXML
    private HBox gamesContainerHighlights;

    @FXML
    private ListView<Game> libraryGames;
    @FXML
    private VBox libraryPane;

    @FXML
    private ObservableList<Game> gamesObservableList = FXCollections.observableArrayList();

    @FXML
    private ListView<Game> allGamesList;

    @FXML
    private Button logout;

    @FXML
    private Button addGameButton;

    @FXML
    private ListView<String> gameJsonFileList;
    private static final String ADDED_FOLDER_PATH = "data/added";

    // SEARCH
    @FXML
    private TextField searchbar;
    @FXML
    private ComboBox<String> genreComboBox;
    @FXML
    private ComboBox<String> tagsComboBox;
    @FXML
    private ComboBox<String> sortComboBox;
    @FXML
    private Button searchButton;

    private List<Game> allGames;



    // ACCOUNT PAGE SECTION
    @FXML
    private Text accountUsername;
    @FXML
    private Text accountEmail;
    @FXML
    private Text accountAge;
    @FXML
    private Text accountRD;
    @FXML
    private Text accountFavoriteGenre;

    @FXML
    private ScrollPane StoreGames;
    @FXML
    private ScrollPane StoreGames1;

    final double[] dragStartX = {0};
    final double[] dragStartX1 = {0};
    final double[] dragStartX3 = {0};

    @FXML
    private SplitPane storeSplitPane;
    @FXML
    private ScrollPane storeScrollPane;
    @FXML
    private AnchorPane storeanchorpane;
    @FXML
    private SplitPane libSplitPane;
    @FXML
    private SplitPane jsonSplitPane;
    @FXML
    private SplitPane accountSplitPane;
    @FXML
    private TabPane mainTabPane;
    @FXML
    private Tab storetappane;
    @FXML
    private Tab librarytabpane;
    @FXML
    private Tab jsontabpane;
    @FXML
    private Tab accountTabPane;
    @FXML
    private Tab helptabpane;


    Font tabFont = Font.font("System", 22);
    private Label storeLabel = new Label("Store");
    private Label libraryLabel = new Label("Library");
    private Label jsonLabel = new Label("Json");
    private Label helpLabel = new Label("Help");
    private Label accountLabel = new Label("Account");
    private Map<Tab, Label> tabLabels = new HashMap<>();

    private double initialContainerWidth;
    private double initialContainerHeight;

    private Label noResultsLabel = new Label("No games found matching your search. New games will be added soon.");


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Login Control
        User loggedInUser = InitalPage.getLoggedInUser();
        userInfo(loggedInUser);


        //ui için hepsi

        storetappane.setGraphic(storeLabel);
        librarytabpane.setGraphic(libraryLabel);
        jsontabpane.setGraphic(jsonLabel);
        helptabpane.setGraphic(helpLabel);
        accountTabPane.setGraphic(accountLabel);

        tabLabels.put(storetappane, storeLabel);
        tabLabels.put(librarytabpane, libraryLabel);
        tabLabels.put(jsontabpane, jsonLabel);
        tabLabels.put(helptabpane, helpLabel);
        tabLabels.put(accountTabPane, accountLabel);

        applyTabLabelStyles();

        highlightSelectedTab(storetappane);

        mainTabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            highlightSelectedTab(newTab);
        });


        allGamesList.skinProperty().addListener((obs, oldSkin, newSkin) -> {
            Set<javafx.scene.Node> nodes = allGamesList.lookupAll(".scroll-bar");
            for (javafx.scene.Node node : nodes) {
                if (node instanceof ScrollBar scrollBar) {
                    if (scrollBar.getOrientation() == Orientation.VERTICAL) {
                        scrollBar.setVisible(false);
                        scrollBar.setPrefWidth(0);
                    }
                }
            }
        });

        RadialGradient radialGradient = new RadialGradient(
                0, 0, 0.5, 0.5, 0.5, true,
                CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#1f0036")),
                new Stop(1, Color.web("#0d0010"))
        );

        Background bg = new Background(new BackgroundFill(radialGradient, CornerRadii.EMPTY, Insets.EMPTY));

        StoreGames.setBackground(bg);
        StoreGames.setBorder(Border.EMPTY);

        StoreGames1.setBackground(bg);
        StoreGames1.setBorder(Border.EMPTY);

        storeScrollPane.setBackground(bg);
        storeScrollPane.setBorder(Border.EMPTY);

        storeSplitPane.setBackground(bg);
        storeSplitPane.setBorder(Border.EMPTY);

        storeanchorpane.setBackground(bg);
        storeanchorpane.setBorder(Border.EMPTY);

        jsonSplitPane.setBackground(bg);
        jsonSplitPane.setBorder(Border.EMPTY);

        Platform.runLater(() -> {
            jsonSplitPane.lookupAll(".split-pane-divider").forEach(divider -> {
                divider.setStyle("-fx-background-color: white;");
                divider.setMouseTransparent(true);
                if (divider instanceof Region) {
                    ((Region)divider).setPrefWidth(2);
                }
            });
        });

        Platform.runLater(() -> {
            accountSplitPane.lookupAll(".split-pane-divider").forEach(divider -> {
                divider.setStyle("-fx-background-color: white;");
                divider.setMouseTransparent(true);
                if (divider instanceof Region) {
                    ((Region)divider).setPrefWidth(2);
                }
            });
        });


        Platform.runLater(() -> {
            libSplitPane.lookupAll(".split-pane-divider").forEach(divider -> {
                divider.setStyle("-fx-background-color: white;");
                divider.setMouseTransparent(true);
                if (divider instanceof Region) {
                    ((Region)divider).setPrefWidth(2);
                }
            });
        });
        libraryGames.setCellFactory(lv -> new ListCell<Game>() {

            private final ImageView imageView = new ImageView();
            private final HBox content = new HBox(10);
            private final Label label = new Label();

            {
                label.setTextFill(Color.WHITE);
                label.setFont(Font.font(16));
                content.setAlignment(Pos.CENTER_LEFT);
                content.getChildren().addAll(imageView, label);
                content.setPadding(new Insets(10));
                content.setBackground(new Background(
                        new BackgroundFill(Color.rgb(20, 10, 40, 0.15), new CornerRadii(5), Insets.EMPTY)));
            }

            @Override
            protected void updateItem(Game game, boolean empty) {
                super.updateItem(game, empty);

                if (empty || game == null) {
                    setText(null);
                    setGraphic(null);
                    setBackground(null);
                } else {
                    label.setText(game.getTitle());

                    String imagePath = game.getCoverImagePath();
                    Image image;
                    try {
                        File file = new File(imagePath);
                        if (file.exists()) {
                            image = new Image(file.toURI().toString(), 45, 45, true, true);
                        } else {
                            URL imageUrl = getClass().getResource(imagePath.startsWith("/") ? imagePath : "/" + imagePath);
                            if (imageUrl != null) {
                                image = new Image(imageUrl.toExternalForm(), 45, 45, true, true);
                            } else {
                                image = new Image(getClass().getResource("/org/ce/gamecatalog/images/lol.png").toExternalForm(), 45, 45, true, true);
                            }
                        }
                    } catch (Exception e) {
                        image = new Image(getClass().getResource("/org/ce/gamecatalog/images/lol.png").toExternalForm(), 45, 45, true, true);
                    }
                    imageView.setImage(image);
                    imageView.setFitWidth(45);
                    imageView.setFitHeight(45);
                    imageView.setPreserveRatio(true);

                    setText(null);
                    setGraphic(content);

                    setBackground(Background.EMPTY);
                    setPadding(new Insets(5, 0, 5, 0));
                }
            }
        });


        StoreGames.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        StoreGames.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        StoreGames.viewportBoundsProperty().addListener((obs, oldVal, newVal) -> {
            gamesContainerSearch.setPrefWidth(newVal.getWidth());
           // gamesContainerSearch.setPrefHeight(newVal.getHeight()); açma burayı
        });

        gamesContainerSearch.setSpacing(20);
        gamesContainerSearch.setPadding(new Insets(10));


        genreComboBox.setStyle(
                "-fx-background-radius: 25; " +
                        "-fx-border-radius: 25; " +
                        "-fx-background-color: #3C0061; " +
                        "-fx-text-fill: white;"
        );
        genreComboBox.setButtonCell(new ListCell<>() {  // prompt text beyaz yapmak için koskoca fonksiyon lazım amk olmuyo başka searchbar
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Genre");
                    setTextFill(Color.WHITE);
                } else {
                    setText(item);
                    setTextFill(Color.WHITE);
                }
            }
        });

        tagsComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Genre");
                    setTextFill(Color.WHITE);
                } else {
                    setText(item);
                    setTextFill(Color.WHITE);
                }
            }
        });

        sortComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Genre");
                    setTextFill(Color.WHITE);
                } else {
                    setText(item);
                    setTextFill(Color.WHITE);
                }
            }
        });



        storeScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        storeScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        StoreGames.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        StoreGames.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        StoreGames.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-background-color:  linear-gradient(from 0% 0% to 100% 100%, #0d0010, #1f0036);" +
                        "-fx-padding: 0;"
        );

        StoreGames.getContent().setOnMousePressed(event -> {
            dragStartX[0] = event.getSceneX();
        });

        StoreGames.getContent().setOnMouseDragged(event -> {
            double dragDistance = dragStartX[0] - event.getSceneX();
            double hValue = StoreGames.getHvalue();
            double width = StoreGames.getContent().getBoundsInLocal().getWidth();
            double viewportWidth = StoreGames.getViewportBounds().getWidth();

            double deltaH = dragDistance / (width - viewportWidth);
            StoreGames.setHvalue(Math.min(Math.max(hValue + deltaH, 0), 1));

            dragStartX[0] = event.getSceneX();
        });

        StoreGames1.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        StoreGames1.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        StoreGames1.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-padding: 0;"
        );

        StoreGames1.getContent().setOnMousePressed(event -> {
            dragStartX1[0] = event.getSceneX();
        });

        StoreGames1.getContent().setOnMouseDragged(event -> {
            double dragDistance = dragStartX1[0] - event.getSceneX();
            double hValue = StoreGames1.getHvalue();
            double width = StoreGames1.getContent().getBoundsInLocal().getWidth();
            double viewportWidth = StoreGames1.getViewportBounds().getWidth();

            double deltaH = dragDistance / (width - viewportWidth);
            StoreGames1.setHvalue(Math.min(Math.max(hValue + deltaH, 0), 1));

            dragStartX1[0] = event.getSceneX();
        });

        Platform.runLater(() -> {
            storeSplitPane.lookupAll(".split-pane-divider").forEach(divider -> {
                if (divider instanceof Region) {
                    ((Region) divider).setStyle(
                            "-fx-background-color: transparent;" +
                                    "-fx-border-width: 0;" +
                                    "-fx-padding: 0;" +
                                    "-fx-opacity: 0;"
                    );
                }
            });
        });

        Platform.runLater(() -> {
            initialContainerWidth = gamesContainerSearch.getWidth();
            initialContainerHeight = gamesContainerSearch.getHeight();
        });



// bölücü hareketini engelliyo
        storeSplitPane.getDividers().forEach(divider -> {
            final double initialPos = divider.getPosition();

            divider.positionProperty().addListener((obs, oldPos, newPos) -> {
                if (!newPos.equals(initialPos)) {
                    divider.setPosition(initialPos);
                }
            });
        });

        // ui sonu


        // SEARCH METHOD

        loadFilters();

        searchButton = new Button("Search");
        searchButton.setOnAction(e -> performSearch());
        performSearch();

        searchbar.textProperty().addListener((observable, oldValue, newValue) -> performSearch());
        genreComboBox.valueProperty().addListener((observable, oldValue, newValue) -> performSearch());
        tagsComboBox.valueProperty().addListener((observable, oldValue, newValue) -> performSearch());
        sortComboBox.valueProperty().addListener((observable, oldValue, newValue) -> performSearch());

        // REFRESH METHOD
        refreshGames();

        // ADD GAME SECTION
        addGameButton.setOnAction(e -> showAddGameDialog());

        // HIGHLIGHTS BY RATING
        createSortedGameListByRating(allGames);

        // LIBRARY SECTION
        gamesObservableList.setAll(loggedInUser.getGameCatalog());
        libraryGames.setItems(gamesObservableList);

        libraryGames.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            libraryPane.getChildren().clear();
            if (newValue != null) {
                Pane libraryPaneGame = createGameLibrary(newValue);
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
        pane.setPrefHeight(600);
        pane.setMinWidth(400);
        pane.setMaxWidth(400);
        pane.setMinHeight(600);
        pane.setMaxHeight(600);
        pane.setPadding(new Insets(15));


        // ui için
        RadialGradient radialGradient = new RadialGradient(
                0, 0, 0.5, 0.5, 0.5, true,
                CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#1f0036")),
                new Stop(1, Color.web("#0d0010"))
        );

        pane.setBackground(new Background(new BackgroundFill(radialGradient, new CornerRadii(0), Insets.EMPTY)));


        pane.setBorder(Border.EMPTY);




        // For Photo
        VBox firstPane = new VBox();
        firstPane.setPrefHeight(120);
        firstPane.setMaxHeight(120);
        firstPane.setMinHeight(120);
        firstPane.setAlignment(Pos.CENTER);

        // Photo
        Image image;
        String imagePath = game.getCoverImagePath();
        try {
            File file = new File(imagePath);
            if(file.exists()) {
                image = new Image(file.toURI().toString(), 200, 150, true, true);
            }else {
                URL imageUrl = getClass().getResource(imagePath.startsWith("/") ? imagePath : "/" + imagePath);
                if(imageUrl != null) {
                    image = new Image(imageUrl.toExternalForm(), 200, 150, true, true);
                } else {
                    throw new FileNotFoundException("Image not found: " + imagePath);
                }
            }
        } catch (Exception ex) {
            System.out.println("Image loading error: " + ex.getMessage());
            image = new Image(getClass().getResource("/org/ce/gamecatalog/images/lol.png").toExternalForm(), 200, 150, true, true);
        }

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
        titleLabel.setTextFill(Color.WHITE);

        // Description
        VBox descBox = new VBox(5);
        Label descTitle = new Label("Description");
        descTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        descTitle.setStyle("-fx-text-fill: white;");
        Text descriptionText = new Text(game.getDescription());
        descriptionText.setFill(Color.WHITE);
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
        detailButton.setStyle("-fx-background-radius: 25; -fx-background-color:  #3C0061; -fx-text-fill: white;");
        addLibraryButton.setStyle("-fx-background-radius: 25; -fx-background-color:  #3C0061; -fx-text-fill: white;");




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

        detailButton.setOnAction(event -> {
            showGameDetailPopup(game);
        });


        pane.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #0d0010, #1f0036);");
        storeScrollPane.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #0d0010, #1f0036);");
        storeScrollPane.getContent().setStyle("-fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #0d0010, #1f0036);");


        return pane;
    }


    private Pane createGameLibrary(Game game) {
        VBox pane = new VBox(30);
        pane.setPadding(new Insets(15));

        // ui için
        Platform.runLater(() -> {
            RadialGradient radialGradient = new RadialGradient(
                    0, 0, 0.5, 0.5, 0.5, true,
                    CycleMethod.NO_CYCLE,
                    new Stop(0, Color.web("#1f0036")),
                    new Stop(1, Color.web("#0d0010"))
            );

            pane.setBackground(new Background(new BackgroundFill(radialGradient, new CornerRadii(0), Insets.EMPTY)));
            pane.setBorder(new Border(new BorderStroke(Color.web("#ccc"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

            libraryGames.setBackground(new Background(new BackgroundFill(radialGradient, null, null)));
        });

        // Fotoğraf için VBox
        VBox firstPane = new VBox();
        firstPane.setPrefHeight(Control.USE_COMPUTED_SIZE);
        firstPane.setMaxHeight(Double.MAX_VALUE);
        firstPane.setMinHeight(0);
        firstPane.setAlignment(Pos.CENTER);

        // Fotoğraf yükleme
        Image image;
        String imagePath = game.getCoverImagePath();
        try {
            File file = new File(imagePath);
            if (file.exists()) {
                image = new Image(file.toURI().toString(), 200, 150, true, true);
            } else {
                URL imageUrl = getClass().getResource(imagePath.startsWith("/") ? imagePath : "/" + imagePath);
                if (imageUrl != null) {
                    image = new Image(imageUrl.toExternalForm(), 200, 150, true, true);
                } else {
                    throw new FileNotFoundException("Image not found: " + imagePath);
                }
            }
        } catch (Exception ex) {
            System.out.println("Image loading error: " + ex.getMessage());
            image = new Image(getClass().getResource("/org/ce/gamecatalog/images/lol.png").toExternalForm(), 200, 150, true, true);
        }
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);

        // Diğer bilgiler için VBox
        VBox secondPane = new VBox(20);

        VBox titleBox = new VBox();
        Label titleLabel = new Label(game.getTitle());
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        titleLabel.setStyle("-fx-text-fill: white;");

        VBox descBox = new VBox(5);
        Label descTitle = new Label("Description");
        descTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        descTitle.setStyle("-fx-text-fill: white;");
        Text descriptionText = new Text(game.getDescription());
        descriptionText.setFill(Color.WHITE);
        descriptionText.setWrappingWidth(370);
        TextFlow descriptionFlow = new TextFlow(descriptionText);

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

        // Butonlar
        Button detailButton = new Button("Detail");
        detailButton.setPrefSize(140, 46);
        detailButton.setFont(Font.font(16));
        detailButton.setStyle("-fx-background-radius: 25; -fx-background-color:  #3C0061; -fx-text-fill: white;");

        Button deleteLibraryButton = new Button("Delete Library");
        deleteLibraryButton.setPrefSize(140, 46);
        deleteLibraryButton.setFont(Font.font(16));
        deleteLibraryButton.setStyle("-fx-background-radius: 25; -fx-background-color:  #3C0061; -fx-text-fill: white;");

        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(detailButton, deleteLibraryButton);

        titleBox.getChildren().addAll(titleLabel);
        descBox.getChildren().addAll(descTitle, descriptionFlow);
        secondPane.getChildren().addAll(titleBox, descBox, infoBox, buttonBox);
        firstPane.getChildren().addAll(imageView);
        pane.getChildren().addAll(firstPane, secondPane);

        // Buton eventleri
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

        detailButton.setOnAction(event -> {
            showGameDetailPopup(game);
        });



        Platform.runLater(() -> {
            libSplitPane.lookupAll(".split-pane-divider").forEach(divider -> {
                divider.setStyle("-fx-background-color: white;");
                divider.setMouseTransparent(true);
                if (divider instanceof Region) {
                    ((Region)divider).setPrefWidth(2); // İnce beyaz çizgi
                }
            });
        });
        libSplitPane.setOnMousePressed(event -> event.consume());
        libSplitPane.setOnMouseDragged(event -> event.consume());
        libSplitPane.setOnMouseReleased(event -> event.consume());

        pane.prefHeightProperty().bind(libraryPane.heightProperty());

        return pane;
    }


    public void showAddGameDialog() {
        Stage addGameStage = new Stage();
        addGameStage.setTitle("Add New Game");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHalignment(HPos.RIGHT);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.ALWAYS);

        grid.getColumnConstraints().addAll(col1, col2);


        TextField titleField = new TextField("Test111");
        TextField genreField = new TextField("Test"); // comma-separated
        TextField developerField = new TextField("Test");
        TextField publisherField = new TextField("Test");
        TextField platformsField = new TextField("Test"); // comma-separated
        TextField translatorsField = new TextField("Test"); // comma-separated
        TextField steamIDField = new TextField("Test");
        TextField releaseYearField = new TextField("1");
        TextField playTimeField = new TextField("1");
        TextField formatField = new TextField("Test");
        TextField languageField = new TextField("Test");
        TextField ratingField = new TextField("1");
        TextField tagsField = new TextField("Test"); // comma-separated
        TextField coverImagePathField = new TextField();
        coverImagePathField.setEditable(false); // Elle yazılamasın
        TextArea descriptionArea = new TextArea("Test");

        Button browseButton = new Button("Browse...");
        browseButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Cover Image");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
            );
            File selectedFile = fileChooser.showOpenDialog(addGameStage);
            if (selectedFile != null) {
                try {
                    // Dosyayı resources/images içine kopyala
                    Path destinationDir = Paths.get("src/main/resources/org/ce/gamecatalog/images/");
                    Files.createDirectories(destinationDir); // klasör yoksa oluştur

                    Path destinationPath = destinationDir.resolve(selectedFile.getName());
                    Files.copy(selectedFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

                    // TextField'a göreli path'i yaz
                    coverImagePathField.setText("/org/ce/gamecatalog/images/" + selectedFile.getName());
                } catch (IOException ex) {
                    ex.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to copy image to resources/images.");
                    alert.showAndWait();
                }
            }
        });

        HBox coverImageBox = new HBox(10, coverImagePathField, browseButton);
        coverImageBox.setAlignment(Pos.CENTER_LEFT);

        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Genre:"), 0, 1);
        grid.add(genreField, 1, 1);
        grid.add(new Label("Developer:"), 0, 2);
        grid.add(developerField, 1, 2);
        grid.add(new Label("Publisher:"), 0, 3);
        grid.add(publisherField, 1, 3);
        grid.add(new Label("Platforms:"), 0, 4);
        grid.add(platformsField, 1, 4);
        grid.add(new Label("Translators:"), 0, 5);
        grid.add(translatorsField, 1, 5);
        grid.add(new Label("SteamID:"), 0, 6);
        grid.add(steamIDField, 1, 6);
        grid.add(new Label("Release Year:"), 0, 7);
        grid.add(releaseYearField, 1, 7);
        grid.add(new Label("Playtime (hrs):"), 0, 8);
        grid.add(playTimeField, 1, 8);
        grid.add(new Label("Format:"), 0, 9);
        grid.add(formatField, 1, 9);
        grid.add(new Label("Language:"), 0, 10);
        grid.add(languageField, 1, 10);
        grid.add(new Label("Rating (0-10):"), 0, 11);
        grid.add(ratingField, 1, 11);
        grid.add(new Label("Tags:"), 0, 12);
        grid.add(tagsField, 1, 12);
        grid.add(new Label("Cover Image Path:"), 0, 13);
        grid.add(coverImageBox, 1, 13);
        grid.add(new Label("Description:"), 0, 14);
        grid.add(descriptionArea, 1, 14);

        Button addButton = new Button("Add Game");
        Button cancelButton = new Button("Cancel");


        addButton.setOnAction(e -> {
            System.out.println(coverImagePathField.getText());
            try {
                Game newGame = new Game(
                        titleField.getText(),
                        Arrays.asList(genreField.getText().split(",")),
                        developerField.getText(),
                        publisherField.getText(),
                        Arrays.asList(platformsField.getText().split(",")),
                        Arrays.asList(translatorsField.getText().split(",")),
                        steamIDField.getText(),
                        Integer.parseInt(releaseYearField.getText()),
                        Integer.parseInt(playTimeField.getText()),
                        formatField.getText(),
                        languageField.getText(),
                        Double.parseDouble(ratingField.getText()),
                        Arrays.asList(tagsField.getText().split(",")),
                        coverImagePathField.getText(),
                        descriptionArea.getText()
                );
                FileHandler.addGameToJson(newGame);


                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Game Added");
                alert.setContentText("Game \"" + newGame.getTitle() + "\" was successfully added.");
                alert.showAndWait();

                addGameStage.close();
            } catch (Exception ex) {
                ex.printStackTrace();

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Could Not Add Game");
                alert.setContentText(ex.getMessage());
                alert.showAndWait();
            } finally {
                refreshGames();
            }
        });

        cancelButton.setOnAction(e -> addGameStage.close());

        HBox buttonBox = new HBox(10, addButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox layout = new VBox(20, grid, buttonBox);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout);
        addGameStage.setScene(scene);
        addGameStage.setWidth(700);
        addGameStage.setHeight(780);
        addGameStage.show();
    }


    private TextFlow boldLabel(String label, String value) {
        Text labelText = new Text(label + ": ");
        labelText.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        labelText.setFill(Color.WHITE);

        Text valueText = new Text(value);
        valueText.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        valueText.setFill(Color.WHITE); //yazıların rengi

        return new TextFlow(labelText, valueText);
    }


    private void refreshLibrary() {
        User loggedInUser = InitalPage.getLoggedInUser();
        gamesObservableList.setAll(loggedInUser.getGameCatalog());
    }

    private void refreshGames() {
        FileHandler fh = new FileHandler();
        gamesContainerSearch.getChildren().clear();
        gamesContainerHighlights.getChildren().clear();
        gameJsonFileList.getItems().clear();
        try {
            allGames = fh.loadFromJSONGames();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for(Game game: allGames) {
            Pane gamePane = createGamePane(game);
            gamesContainerSearch.getChildren().add(gamePane);
        }
        searchbar.clear();
        tagsComboBox.getSelectionModel().clearSelection();
        genreComboBox.getSelectionModel().clearSelection();
        genreComboBox.getSelectionModel().clearSelection();
        initializeAllGames();
        initializeGameJsonFileList();
    }


    private void showGameDetailPopup(Game game) {
        Stage popupStage = new Stage();
        popupStage.setTitle(game.getTitle() + " - Details");

        VBox detailPane = new VBox(10);
        detailPane.setPadding(new Insets(15));
        detailPane.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #0d0010, #1f0036);");
        detailPane.setAlignment(Pos.TOP_LEFT);

        Image image;
        String imagePath = game.getCoverImagePath();
        try {
            File file = new File(imagePath);
            if(file.exists()) {
                image = new Image(file.toURI().toString(), 200, 150, true, true);
            }else {
                URL imageUrl = getClass().getResource(imagePath.startsWith("/") ? imagePath : "/" + imagePath);
                if(imageUrl != null) {
                    image = new Image(imageUrl.toExternalForm(), 200, 150, true, true);
                } else {
                    throw new FileNotFoundException("Image not found: " + imagePath);
                }
            }
        } catch (Exception ex) {
            System.out.println("Image loading error: " + ex.getMessage());
            image = new Image(getClass().getResource("/org/ce/gamecatalog/images/lol.png").toExternalForm(), 200, 150, true, true);
        }
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);

        Label title = new Label(game.getTitle());
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setStyle("-fx-text-fill: white;");

        Text descriptionText = new Text(game.getDescription());
        descriptionText.setFill(Color.WHITE);

        TextFlow descriptionFlow = new TextFlow(descriptionText);
        descriptionFlow.setMaxWidth(400);

        VBox infoBox = new VBox(5);
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

        ScrollPane scrollPane = new ScrollPane();

        VBox content = new VBox(15, imageView, title, descriptionFlow, infoBox);
        content.setPadding(new Insets(10));
        content.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #1a001f, #3f0073);");

        content.prefHeightProperty().bind(scrollPane.heightProperty());


        scrollPane.setContent(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-background-insets: 0;" +
                        "-fx-padding: 0;"
        );




        Scene popupScene = new Scene(scrollPane, 500, 600);
        popupStage.setScene(popupScene);
        popupStage.show();
    }

    @FXML
    private void handleAddGameJson(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Game JSON File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON files", "*.json"));

        File selectedFile = fileChooser.showOpenDialog(null); // veya sahne referansını ver

        if (selectedFile != null) {
            File copiedFile = null;  // try dışına alındı
            try {
                File targetDir = new File("data/added");
                if (!targetDir.exists()) {
                    targetDir.mkdirs();
                }

                copiedFile = new File(targetDir, selectedFile.getName());
                Files.copy(selectedFile.toPath(), copiedFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);

                FileHandler.importGameFromJsonFile(copiedFile.getPath());

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Successful");
                alert.setHeaderText("Game added successfully");
                alert.setContentText("Added file: " + copiedFile.getName());
                alert.showAndWait();

            } catch (IOException e) {
                if (copiedFile != null && copiedFile.exists()) {
                    copiedFile.delete();
                }

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Could Not Add Game");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            } finally {
                refreshGames();
            }
        }
    }

    private void initializeGameJsonFileList() {
        File addedFolder = new File(ADDED_FOLDER_PATH);

        if (addedFolder.exists() && addedFolder.isDirectory()) {
            File[] files = addedFolder.listFiles();

            if (files != null) {
                List<String> fileNames = new ArrayList<>();
                for (File file : files) {
                    if (file.isFile()) {
                        fileNames.add(file.getName());
                    }
                }
                gameJsonFileList.getItems().addAll(fileNames);
            }
        } else {
            System.out.println("Folder not found: " + ADDED_FOLDER_PATH);
        }
    }

    private void initializeAllGames() {
        try {
            allGamesList.getItems().clear();
            List<Game> games = FileHandler.loadFromJSONGames();
            allGamesList.getItems().addAll(games);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteSelectedGame(ActionEvent event) {
        Game selectedGame = allGamesList.getSelectionModel().getSelectedItem();

        if (selectedGame != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Deletion Confirmation");
            confirm.setHeaderText("Delete Game");
            confirm.setContentText("\" Are you sure you want to delete the game titled " + selectedGame.getTitle() + "\"?");

            Optional<ButtonType> result = confirm.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    FileHandler.removeGameFromJson(selectedGame.getSteamID());

                    allGamesList.getItems().remove(selectedGame);

                    Alert success = new Alert(Alert.AlertType.INFORMATION);
                    success.setTitle("Successful");
                    success.setHeaderText("Game Deleted");
                    success.setContentText("The selected game has been successfully deleted.");
                    success.showAndWait();

                    refreshGames();
                } catch (IOException e) {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Error");
                    error.setHeaderText("Deletion Failed");
                    error.setContentText(e.getMessage());
                    error.showAndWait();
                }
            }
        } else {
            Alert warning = new Alert(Alert.AlertType.WARNING);
            warning.setTitle("Warning");
            warning.setHeaderText("No Game Selected");
            warning.setContentText("Please select a game to delete.");
            warning.showAndWait();
        }
    }

    @FXML
    private void handleDeleteMatchingGames(ActionEvent event) {
        String selectedFileName = gameJsonFileList.getSelectionModel().getSelectedItem();

        if (selectedFileName == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No File Selected");
            alert.setContentText("Please select a JSON file from the list.");
            alert.showAndWait();
            return;
        }

        File selectedFile = new File("data/added", selectedFileName);

        try {
            int deletedCount = FileHandler.removeGamesMatchingSteamIDsFromFile(selectedFile);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Deletion Complete");
            alert.setHeaderText("Games Deleted");
            alert.setContentText(deletedCount + " game(s) were removed from your collection.");
            alert.showAndWait();

            refreshGames();

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Deletion Failed");
            alert.setContentText("An error occurred while deleting games: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleEditSelectedGame(ActionEvent event) {
        Game selectedGame = allGamesList.getSelectionModel().getSelectedItem();

        if (selectedGame == null) {
            Alert warning = new Alert(Alert.AlertType.WARNING);
            warning.setTitle("Warning");
            warning.setHeaderText("No Game Selected");
            warning.setContentText("Please select a game to edit.");
            warning.showAndWait();
            return;
        }

        Stage editStage = new Stage();
        editStage.setTitle("Edit Game - " + selectedGame.getTitle());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHalignment(HPos.RIGHT);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.ALWAYS);

        grid.getColumnConstraints().addAll(col1, col2);

        TextField titleField = new TextField(selectedGame.getTitle());
        TextField genreField = new TextField(String.join(",", selectedGame.getGenre()));
        TextField developerField = new TextField(selectedGame.getDeveloper());
        TextField publisherField = new TextField(selectedGame.getPublisher());
        TextField platformsField = new TextField(String.join(",", selectedGame.getPlatforms()));
        TextField translatorsField = new TextField(String.join(",", selectedGame.getTranslators()));
        TextField steamIDField = new TextField(selectedGame.getSteamID());
        steamIDField.setEditable(false);
        TextField releaseYearField = new TextField(String.valueOf(selectedGame.getReleaseYear()));
        TextField playTimeField = new TextField(String.valueOf(selectedGame.getPlayTime()));
        TextField formatField = new TextField(selectedGame.getFormat());
        TextField languageField = new TextField(selectedGame.getLanguage());
        TextField ratingField = new TextField(String.valueOf(selectedGame.getRating()));
        TextField tagsField = new TextField(String.join(",", selectedGame.getTags()));
        TextField coverImagePathField = new TextField(selectedGame.getCoverImagePath());
        coverImagePathField.setEditable(false);
        TextArea descriptionArea = new TextArea(selectedGame.getDescription());

        Button browseButton = new Button("Browse...");
        browseButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Cover Image");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
            );
            File selectedFile = fileChooser.showOpenDialog(editStage);
            if (selectedFile != null) {
                try {
                    Path destinationDir = Paths.get("src/main/resources/org/ce/gamecatalog/images/");
                    Files.createDirectories(destinationDir);

                    Path destinationPath = destinationDir.resolve(selectedFile.getName());
                    Files.copy(selectedFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

                    coverImagePathField.setText("/org/ce/gamecatalog/images/" + selectedFile.getName());
                } catch (IOException ex) {
                    ex.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to copy image to resources/images.");
                    alert.showAndWait();
                }
            }
        });

        HBox coverImageBox = new HBox(10, coverImagePathField, browseButton);
        coverImageBox.setAlignment(Pos.CENTER_LEFT);

        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Genre:"), 0, 1);
        grid.add(genreField, 1, 1);
        grid.add(new Label("Developer:"), 0, 2);
        grid.add(developerField, 1, 2);
        grid.add(new Label("Publisher:"), 0, 3);
        grid.add(publisherField, 1, 3);
        grid.add(new Label("Platforms:"), 0, 4);
        grid.add(platformsField, 1, 4);
        grid.add(new Label("Translators:"), 0, 5);
        grid.add(translatorsField, 1, 5);
        grid.add(new Label("SteamID:"), 0, 6);
        grid.add(steamIDField, 1, 6);
        grid.add(new Label("Release Year:"), 0, 7);
        grid.add(releaseYearField, 1, 7);
        grid.add(new Label("Playtime (hrs):"), 0, 8);
        grid.add(playTimeField, 1, 8);
        grid.add(new Label("Format:"), 0, 9);
        grid.add(formatField, 1, 9);
        grid.add(new Label("Language:"), 0, 10);
        grid.add(languageField, 1, 10);
        grid.add(new Label("Rating (0-10):"), 0, 11);
        grid.add(ratingField, 1, 11);
        grid.add(new Label("Tags:"), 0, 12);
        grid.add(tagsField, 1, 12);
        grid.add(new Label("Cover Image Path:"), 0, 13);
        grid.add(coverImageBox, 1, 13);
        grid.add(new Label("Description:"), 0, 14);
        grid.add(descriptionArea, 1, 14);

        Button editButton = new Button("Edit Game");
        Button cancelButton = new Button("Cancel");

        editButton.setOnAction(e -> {
            try {
                Game updatedGame = new Game(
                        titleField.getText(),
                        Arrays.asList(genreField.getText().split(",")),
                        developerField.getText(),
                        publisherField.getText(),
                        Arrays.asList(platformsField.getText().split(",")),
                        Arrays.asList(translatorsField.getText().split(",")),
                        steamIDField.getText(),
                        Integer.parseInt(releaseYearField.getText()),
                        Double.parseDouble(playTimeField.getText()),
                        formatField.getText(),
                        languageField.getText(),
                        Double.parseDouble(ratingField.getText()),
                        Arrays.asList(tagsField.getText().split(",")),
                        coverImagePathField.getText(),
                        descriptionArea.getText()
                );

                FileHandler.updateGameInJson(updatedGame);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Game Updated");
                alert.setContentText("Game \"" + updatedGame.getTitle() + "\" has been updated.");
                alert.showAndWait();

                editStage.close();
                refreshGames();
            } catch (Exception ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Could Not Update Game");
                alert.setContentText(ex.getMessage());
                alert.showAndWait();
            }
        });

        cancelButton.setOnAction(e -> editStage.close());

        HBox buttonBox = new HBox(10, editButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox layout = new VBox(20, grid, buttonBox);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout);
        editStage.setScene(scene);
        editStage.setWidth(700);
        editStage.setHeight(780);
        editStage.show();
    }



    private void userInfo(User user) {
        accountUsername.setText(" " + user.getUsername());
        accountEmail.setText(" " + user.getEmail());
        accountAge.setText(" " + String.valueOf(user.getAge()));
        accountRD.setText(" " + "2023-05-14");
        accountFavoriteGenre.setText(" " + user.getFavoriteGenre());
    }


    private void loadFilters() {
        try {
            sortComboBox.getItems().addAll(
                    "Rating: High to Low",
                    "Rating: Low to High",
                    "Playtime: High to Low",
                    "Playtime: Low to High",
                    "Release Year: Newest First",
                    "Release Year: Oldest First"
            );

            allGames = FileHandler.loadFromJSONGames();

            Set<String> genres = new HashSet<>();
            Set<String> tags = new HashSet<>();

            for (Game game : allGames) {
                genres.addAll(game.getGenre());
                tags.addAll(game.getTags());
            }

            ObservableList<String> genreList = FXCollections.observableArrayList(genres);
            ObservableList<String> tagList = FXCollections.observableArrayList(tags);

            genreList.add(0, "Genre");
            tagList.add(0, "Tags");

            genreComboBox.setItems(genreList);
            tagsComboBox.setItems(tagList);

            genreComboBox.getSelectionModel().clearSelection();
            tagsComboBox.getSelectionModel().clearSelection();

        } catch (IOException e) {
            System.err.println("Failed to load games: " + e.getMessage());
        }
    }

    private List<Game> performSearch() {
        String query = searchbar.getText().trim().toLowerCase();
        String selectedGenre = genreComboBox.getValue();
        String selectedTag = tagsComboBox.getValue();
        String selectedSort = sortComboBox.getValue();

        List<Game> results = allGames.stream()
                .filter(game -> game.getTitle().toLowerCase().contains(query))
                .filter(game -> selectedGenre == null || selectedGenre.equals("Genre") || game.getGenre().contains(selectedGenre))
                .filter(game -> selectedTag == null || selectedTag.equals("Tags") || game.getTags().contains(selectedTag))
                .collect(Collectors.toList());

        if (selectedSort != null) {
            switch (selectedSort) {
                case "Rating: High to Low":
                    results.sort(Comparator.comparingDouble(Game::getRating).reversed());
                    break;
                case "Rating: Low to High":
                    results.sort(Comparator.comparingDouble(Game::getRating));
                    break;
                case "Playtime: High to Low":
                    results.sort(Comparator.comparingDouble(Game::getPlayTime).reversed());
                    break;
                case "Playtime: Low to High":
                    results.sort(Comparator.comparingDouble(Game::getPlayTime));
                    break;
                case "Release Year: Newest First":
                    results.sort(Comparator.comparingInt(Game::getReleaseYear).reversed());
                    break;
                case "Release Year: Oldest First":
                    results.sort(Comparator.comparingInt(Game::getReleaseYear));
                    break;
            }
        }

        // hbox temizleme
        gamesContainerSearch.getChildren().clear();

        if (results.isEmpty()) {
            // games not found yazısı
            VBox emptyBox = new VBox(noResultsLabel);
            emptyBox.setAlignment(Pos.CENTER);
            emptyBox.setPrefWidth(gamesContainerSearch.getWidth());
            emptyBox.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #0d0010, #1f0036);");
            noResultsLabel.setFont(new Font("Arial", 20));
            emptyBox.setPrefWidth(StoreGames.getViewportBounds().getWidth());
            emptyBox.setPrefHeight(StoreGames.getViewportBounds().getHeight());
            noResultsLabel.setTextFill(Color.WHITE);
            StoreGames.setContent(emptyBox);
        } else {
            // scrollpane içeriği deşiyo
            StoreGames.setContent(gamesContainerSearch);

            for (Game game : results) {
                Pane gamePane = createGamePane(game);
                gamesContainerSearch.getChildren().add(gamePane);
            }
        }

        return results;
    }



    @FXML
    private void handleChangePasswordButton(ActionEvent event) {
        User loggedInUser = InitalPage.getLoggedInUser();

        if (loggedInUser == null) {
            System.out.println("No user logged in.");
            return;
        }

        Stage stage = new Stage();
        stage.setTitle("Change Password");

        PasswordField oldPasswordField = new PasswordField();
        oldPasswordField.setPromptText("Old Password");

        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPromptText("New Password");

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm New Password");

        Label messageLabel = new Label();

        Button changeButton = new Button("Change Password");
        changeButton.setOnAction(e -> {
            String oldPass = oldPasswordField.getText();
            String newPass = newPasswordField.getText();
            String confirmPass = confirmPasswordField.getText();

            if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                messageLabel.setText("Please fill all fields.");
                messageLabel.setStyle("-fx-text-fill: red;");
                return;
            }

            if (!loggedInUser.getPasswordHash().equals(loggedInUser.hashPassword(oldPass))) {
                messageLabel.setText("Old password is incorrect.");
                messageLabel.setStyle("-fx-text-fill: red;");
                return;
            }

            if (!newPass.equals(confirmPass)) {
                messageLabel.setText("New passwords do not match.");
                messageLabel.setStyle("-fx-text-fill: red;");
                return;
            }

            if (oldPass.equals(confirmPass)) {
                messageLabel.setText("New password cannot be the same as the old one.");
                messageLabel.setStyle("-fx-text-fill: red;");
                return;
            }

            try {
                loggedInUser.setPassword(newPass);
                List<User> users = FileHandler.loadFromJSONUsers();

                for (int i = 0; i < users.size(); i++) {
                    if (users.get(i).getUsername().equals(loggedInUser.getUsername())) {
                        users.set(i, loggedInUser);
                        break;
                    }
                }

                FileHandler.saveUsersToJSON(users);

                messageLabel.setText("Password changed successfully.");
                messageLabel.setStyle("-fx-text-fill: green;");
                stage.close();

            } catch (IOException ex) {
                ex.printStackTrace();
                messageLabel.setText("Error saving user data.");
                messageLabel.setStyle("-fx-text-fill: red;");
            }
        });

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(oldPasswordField, newPasswordField, confirmPasswordField, changeButton, messageLabel);

        Scene scene = new Scene(layout, 300, 250);
        stage.setScene(scene);

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    public HBox createSortedGameListByRating(List<Game> games) {
        List<Game> sortedGames = games.stream()
                .sorted(Comparator.comparingDouble(Game::getRating).reversed())
                .collect(Collectors.toList());


        for (Game game : sortedGames) {
            Pane gamePane = createGamePane(game);
            gamesContainerHighlights.getChildren().add(gamePane);
        }

        return gamesContainerHighlights;
    }

    private void highlightSelectedTab(Tab selectedTab) {
        tabLabels.forEach((tab, label) -> {
            if (tab.equals(selectedTab)) {
                label.setTextFill(Color.PURPLE);
                label.setUnderline(true);
                label.setFont(Font.font(label.getFont().getFamily(), FontWeight.BOLD, label.getFont().getSize())); // Kalın yap
            } else {
                label.setTextFill(Color.BLACK);
                label.setUnderline(false);
                label.setFont(Font.font(label.getFont().getFamily(), FontWeight.NORMAL, label.getFont().getSize())); // Normal yap
            }
        });
    }

    private void applyTabLabelStyles() {
        for (Label label : tabLabels.values()) {
            label.setFont(Font.font("System", 22));          // büyük font
            label.setMaxWidth(80);                           // en fazla 80px
            label.setMinWidth(Region.USE_PREF_SIZE);         // minimumu otomatik
            label.setWrapText(false);
            label.setEllipsisString("...");                  // sığmazsa üç nokta koy
        }
    }





}
