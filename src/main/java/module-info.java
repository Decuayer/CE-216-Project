module org.ce.gamecatalog {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    requires org.controlsfx.controls;



    opens org.ce.gamecatalog to javafx.fxml;
    exports org.ce.gamecatalog;
}