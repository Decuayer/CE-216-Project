module org.ce.gamecatalog {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    requires javafx.graphics;
    requires java.xml;


    opens org.ce.gamecatalog to javafx.fxml;
    exports org.ce.gamecatalog;
}