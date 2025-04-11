module org.ce216.gamecatalog {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    requires javafx.graphics;
    requires com.google.gson;


    opens org.ce216.gamecatalog to javafx.fxml, com.google.gson;
    exports org.ce216.gamecatalog;
}