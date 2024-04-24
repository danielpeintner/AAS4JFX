package com.siemens.aas4jfx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class AAS4JFX extends Application {

    @Override
    public void start(Stage stage) {
        // Bottom
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        Label l = new Label("JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        // Center
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.getTabs().add(getTabValidation());
        // setup
        BorderPane bp = new BorderPane();
        bp.setBottom(l);
        bp.setCenter(tabPane);

        Scene scene = new Scene(bp, 640, 480);
        stage.setScene(scene);
        stage.setTitle("AAS4JFX");
        stage.show();
    }

    private static void openFile(File f) {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                new Thread(() -> {
                    try {
                        Desktop.getDesktop().open(f);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            } else {
                throw new Exception("Error opening file " + f);
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Information Dialog");
            alert.setContentText("Error opening: " + f);
            alert.showAndWait();
        }
    }

    Tab getTabValidation() {
        Tab t = new Tab("Validation");
        BorderPane bp = new BorderPane();
        t.setContent(bp);

        Button button = new Button("TEST");
        bp.setTop(button);
        button.setOnAction(a -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setContentText("bla");
            alert.showAndWait();
        });

        return t;
    }


    public static void main(String[] args) {
        launch();
    }
}