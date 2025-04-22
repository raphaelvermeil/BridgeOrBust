module com.example.bridgeorbust {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires javafx.media;
    requires java.desktop;

    opens com.example.bridgeorbust to javafx.fxml;
    exports com.example.bridgeorbust;
    exports com.example.bridgeorbust.physicsSimulation;
    opens com.example.bridgeorbust.physicsSimulation to javafx.fxml;
    exports com.example.GUI;
    opens com.example.GUI to javafx.fxml;
}