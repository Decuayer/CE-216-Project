module org.ce216.gamecatalog {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.ce216.gamecatalog to javafx.fxml;
    exports org.ce216.gamecatalog;
}