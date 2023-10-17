module com.bohdan.casino {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires com.almasb.fxgl.all;

    opens com.bohdan.casino to javafx.fxml;
    exports com.bohdan.casino;
}