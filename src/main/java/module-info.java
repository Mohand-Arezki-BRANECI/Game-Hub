module com.gamehub {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens com.gamehub to javafx.fxml;
    exports com.gamehub;
}