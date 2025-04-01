package org.ce216.gamecatalog;

import javafx.fxml.FXML;
import javafx.scene.control.*;


public class MainScreenController {
    @FXML
    private Button testButton;

    @FXML
    private void handleButtonClick() {
        System.out.println("Test button clicked!");
    }
}
