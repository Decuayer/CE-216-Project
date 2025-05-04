package org.ce216.gamecatalog;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class StoreController implements Initializable {

    @FXML
    private ScrollPane StoreGames;

    @FXML
    private HBox gamesContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("StoreController initialize() çalıştı!");
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
    }

    private Pane createGamePane(Game game) {
        Pane pane = new Pane();
        pane.setPrefWidth(400);

        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream(game.getCoverImagePath())));
        imageView.setFitHeight(150);
        imageView.setFitWidth(200);
        imageView.setLayoutX(100);
        imageView.setLayoutY(44);

        TextFlow description = new TextFlow();
        description.setLayoutX(59);
        description.setLayoutY(221);
        description.setPrefHeight(234);
        description.setPrefWidth(282);
        Text text = new Text(game.getTitle() + "\nLorem ipsum...");
        text.setWrappingWidth(270);
        description.getChildren().add(text);

        Button detailButton = new Button("Detail");
        detailButton.setPrefSize(140, 46);
        detailButton.setLayoutX(130);
        detailButton.setLayoutY(496);
        detailButton.setFont(Font.font(24));

        pane.getChildren().addAll(imageView, description, detailButton);

        return pane;
    }

}
