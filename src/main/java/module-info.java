module org.ce.gamecatalog {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.xml;
    requires org.controlsfx.controls;
    requires org.json;


    opens org.ce.gamecatalog to javafx.fxml;
    exports org.ce.gamecatalog;
}