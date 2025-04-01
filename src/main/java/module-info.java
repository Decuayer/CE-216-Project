module org.ce216.gamecatalog {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;


    opens org.ce216.gamecatalog to javafx.fxml;
    exports org.ce216.gamecatalog;
}