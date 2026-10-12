module com.demo.userlog {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    //requires eu.hansolo.tilesfx;
    requires java.desktop;
    requires java.sql;
    requires mail;

    opens com.demo.userlog to javafx.fxml;
    exports com.demo.userlog;
}