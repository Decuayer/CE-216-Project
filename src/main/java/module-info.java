module org.ce216.gamecatalog {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    requires javafx.graphics;
    requires java.xml;


    opens org.ce216.gamecatalog to javafx.fxml, com.google.gson;
    exports org.ce216.gamecatalog;
}