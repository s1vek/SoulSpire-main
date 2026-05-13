module com.example.soulspire {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.logging;
    requires java.desktop;
    requires org.json;

    opens com.example.soulspire to javafx.fxml;
    opens com.example.soulspire.UI to javafx.fxml;

    exports com.example.soulspire;
}