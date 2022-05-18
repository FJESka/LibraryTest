module library.library {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;

    opens library.library to javafx.fxml;
    exports library.library;

    opens itemSearch to javafx.fxml;
    exports itemSearch;

    opens adminInterface to javafx.fxml;
    exports adminInterface;

    opens loginform to javafx.fxml;
    exports loginform;

    opens Overdue to javafx.fxml;
    exports Overdue;
}