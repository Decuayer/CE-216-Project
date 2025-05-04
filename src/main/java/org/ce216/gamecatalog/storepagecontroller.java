package org.ce216.gamecatalog;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.ce216.gamecatalog.GameLoader.loadGamesFromJson;

public class storepagecontroller {
    @FXML
    private HBox titleBar;
    @FXML
    private Region dragRegion;
    @FXML
    private Button closeButton;
    @FXML
    private Button minimizeButton;
    @FXML
    private VBox gamesVBox;
    @FXML
    private SplitPane splitPane;
    @FXML
    private ScrollPane scrollPane;

    private static final String FILE_PATH = "data/games.json";

    private double offsetX = 0;
    private double offsetY = 0;


    public void handleMovementAction(MouseEvent event) {
        Stage stageCurrent = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stageCurrent.setX(event.getScreenX() - offsetX);
        stageCurrent.setY(event.getScreenY() - offsetY);
    }

    public void handleClickAction(MouseEvent event) {
        offsetX = event.getSceneX();
        offsetY = event.getSceneY();
    }

    public void CloseWindow(ActionEvent event) {
        Stage stageCurrent = (Stage) ((Node) event.getSource()).getScene().getWindow();
        closeButton.getScene().getWindow();
        stageCurrent.close();
    }

    public void MinWindow(ActionEvent event) {
        Stage stageCurrent = (Stage) ((Node) event.getSource()).getScene().getWindow();
        minimizeButton.getScene().getWindow();
        stageCurrent.setIconified(true);
    }

    public void setOnMouseEnteredY() {
        minimizeButton.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4);");
    }

    public void setOnMouseExitedY() {
        minimizeButton.setStyle("-fx-background-color: transparent;");
    }

    public void setOnMouseEnteredX() {
        closeButton.setStyle("-fx-background-color: rgba(196, 30, 58);");
    }

    public void setOnMouseExitedX() {
        closeButton.setStyle("-fx-background-color: transparent;");
    }

    public void loadGamesfromJson() {
        try {
            System.out.println(gamesVBox);
            JSONArray gamesarr = loadGamesFromJson(FILE_PATH);

            for (int i = 0; i < gamesarr.length(); i++) {
                JSONObject game = gamesarr.getJSONObject(i);

                String name = game.getString("title");
                String gameImage = game.getString("coverImagePath");
                List<String> genres = jsonArrayToList(game.getJSONArray("genre"));
                String developer = game.getString("developer");
                String publisher = game.getString("publisher");
                List<String> platforms = jsonArrayToList(game.getJSONArray("platforms"));
                List<String> translators = jsonArrayToList(game.getJSONArray("translators"));
                int releaseYear = game.getInt("releaseYear");
                String format = game.getString("format");
                String language = game.getString("language");
                double rating = game.getDouble("rating");
                List<String> tags = jsonArrayToList(game.getJSONArray("tags"));

                HBox gamepane = new HBox();
                gamepane.setSpacing(10);
                gamepane.setAlignment(Pos.CENTER_LEFT);
                gamepane.setPrefWidth(Double.MAX_VALUE);

                InputStream imageStream = getClass().getResourceAsStream(gameImage);
                ImageView imageView;
                if (imageStream != null) {
                    Image image = new Image(imageStream);
                    imageView = new ImageView(image);
                } else {
                    System.out.println("Resim bulunamadı: " + gameImage);
                    imageView = new ImageView();
                }

                imageView.setFitWidth(150);
                imageView.setFitHeight(150);
                imageView.setPreserveRatio(true);
                VBox imageBox = new VBox(imageView);
                imageBox.setAlignment(Pos.TOP_CENTER);

                VBox textBox = new VBox();
                textBox.setSpacing(5);
                textBox.setPrefWidth(400);
                textBox.setMinHeight(250);

                Label gameName = new Label(name);
                gameName.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");
                gameName.setWrapText(true);
                gameName.setMaxWidth(400);


                VBox leftText = new VBox(
                        new Label("Genres: " + String.join(", ", genres)),
                        new Label("Developer: " + developer),
                        new Label("Publisher: " + publisher),
                        new Label("Platforms: " + String.join(", ", platforms)),
                        new Label("Translators: " + String.join(", ", translators))
                );
                leftText.setSpacing(5);


                VBox rightText = new VBox(
                        new Label("Release Year: " + releaseYear),
                        new Label("Format: " + format),
                        new Label("Language: " + language),
                        new Label("Rating: " + rating),
                        new Label("Tags: " + String.join(", ", tags))
                );
                rightText.setSpacing(5);


                HBox infoBox = new HBox(leftText, rightText);
                infoBox.setSpacing(40);
                infoBox.setAlignment(Pos.TOP_LEFT);


                leftText.getChildren().forEach(node -> {
                    if (node instanceof Label label) {
                        label.setStyle("-fx-font-size: 13px; -fx-text-fill: white;");
                    }
                });
                rightText.getChildren().forEach(node -> {
                    if (node instanceof Label label) {
                        label.setStyle("-fx-font-size: 13px; -fx-text-fill: white;");
                    }
                });

                textBox.getChildren().addAll(gameName, infoBox);

                Button gameButton = new Button("Add to Library");
                gameButton.setStyle("-fx-background-color:  #27323f; " +
                        "-fx-text-fill: white; " +
                        "-fx-opacity: 0.7; " +
                        "-fx-background-radius:25; " +
                        "-fx-font-family: Arial; " +
                        "-fx-font-size: 18px; " +
                        "-fx-min-width: 180px; " +
                        "-fx-min-height: 50px;");
                gameButton.setMinWidth(150);
                gameButton.setOnAction(e -> {
                    System.out.println("Added to Library");
                });

                HBox buttonBox = new HBox(gameButton);
                buttonBox.setAlignment(Pos.CENTER);
                buttonBox.setPadding(new Insets(0, 10, 0, 10));

                gamepane.getChildren().addAll(imageBox, textBox, buttonBox);

                VBox.setMargin(gamepane, new Insets(10, 0, 30, 0));
                gamesVBox.getChildren().add(gamepane);

                System.out.println("Game image path: " + gameImage);

                HBox.setHgrow(textBox, Priority.ALWAYS);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    private List<String> jsonArrayToList(JSONArray jsonArray) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(jsonArray.getString(i));
        }
        return list;
    }




    @FXML
    public void initialize() throws IOException {
        loadGamesfromJson();
        SplitPane.setResizableWithParent(splitPane.getItems().get(0), false);
        SplitPane.setResizableWithParent(splitPane.getItems().get(1), false);


        Platform.runLater(() -> {
            scrollPane.lookupAll(".scroll-bar").forEach(bar -> bar.setOpacity(0));
            splitPane.lookupAll(".split-pane-divider").forEach(divider -> {
                divider.setOnMousePressed(e -> e.consume());
                divider.setOnMouseDragged(e -> e.consume());
                divider.setOnMouseReleased(e -> e.consume());
                divider.setStyle("-fx-background-color: transparent;");
            });
        });
    }
}




