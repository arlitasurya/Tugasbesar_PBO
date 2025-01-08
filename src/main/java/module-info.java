module com.tugasbesar {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.tugasbesar to javafx.fxml;
    exports com.tugasbesar;
}
