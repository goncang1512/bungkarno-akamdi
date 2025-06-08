module com.example.bungkarnoacademy {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    requires java.prefs;
    requires javafx.media;


    opens controller to javafx.fxml;
    opens com.example.bungkarnoacademy to javafx.fxml;
    opens components to javafx.fxml;
    exports com.example.bungkarnoacademy;
    exports controller;
}