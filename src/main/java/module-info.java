module com.example.salon_fryzjerski_projekt {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires com.jfoenix;
    requires java.sql;

    opens com.example.salon_fryzjerski_projekt to javafx.fxml;
    exports com.example.salon_fryzjerski_projekt;
}