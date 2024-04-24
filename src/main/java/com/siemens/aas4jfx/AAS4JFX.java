package com.siemens.aas4jfx;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.controlsfx.glyphfont.FontAwesome;
import org.eclipse.digitaltwin.aas4j.v3.dataformat.xml.XmlDeserializer;

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
        tabPane.getTabs().add(getTabValidation(stage.getOwner()));
        // setup
        BorderPane bp = new BorderPane();
        bp.setBottom(l);
        bp.setCenter(tabPane);

        Scene scene = new Scene(bp, 640, 480);
        stage.setScene(scene);
        stage.setTitle("AAS4JFX");

        stage.show();
    }

    Tab getTabValidation(Window owner) {
        Tab t = new Tab("Validation");
        BorderPane bp = new BorderPane();
        t.setContent(bp);

        // create buttons etc
        Text text1 = new Text("AAS XML File:");
        FileSelector fileSelector = new FileSelector();
        Button buttonValidate = new Button("Validate");
        buttonValidate.setGraphic(new FontAwesome().create(FontAwesome.Glyph.CHECK));

        // create a grid pane
        GridPane gridPane = new GridPane();

        // same constraints to have a consistent look
        ColumnConstraints column1 = new ColumnConstraints(100);
        column1.setHalignment(HPos.RIGHT);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setHgrow(Priority.ALWAYS);

        gridPane.getColumnConstraints().addAll(column1, column2);

        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        gridPane.add(text1, 0, 1);
        gridPane.add(fileSelector, 1, 1);
        gridPane.add(buttonValidate, 1, 3);

        bp.setCenter(gridPane);

        buttonValidate.setOnAction(a -> {
            if (fileSelector.getSelectedFile() == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("No File selected");
                alert.showAndWait();
            } else {
                try {
                    new XmlDeserializer().read(fileSelector.getSelectedFile());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Validation successful.");
                    alert.showAndWait();
                } catch (Exception e) {
                    DialogUtil.showAlertDialog(e, owner);
                }
            }
        });

        return t;
    }


    public static void main(String[] args) {
        launch();
    }
}