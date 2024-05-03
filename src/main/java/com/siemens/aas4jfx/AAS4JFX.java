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
import org.eclipse.digitaltwin.aas4j.v3.dataformat.json.JsonSchemaValidator;
import org.eclipse.digitaltwin.aas4j.v3.dataformat.xml.XmlSchemaValidator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Set;

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
        tabPane.getTabs().add(getTabValidationXML(stage.getOwner()));
        tabPane.getTabs().add(getTabValidationJSON(stage.getOwner()));
        // setup
        BorderPane bp = new BorderPane();
        bp.setBottom(l);
        bp.setCenter(tabPane);

        Scene scene = new Scene(bp, 640, 480);
        stage.setScene(scene);
        stage.setTitle("AAS4JFX");

        stage.show();
    }

    static final String UTF8_BOM = "\uFEFF";

    String readFileToString(File f) throws IOException {
        // String xml = new String(Files.readAllBytes(Paths.get(fileSelector.getSelectedFile().getAbsolutePath())));
        String content = Files.readString(f.toPath());
        if (content.startsWith(UTF8_BOM)) {
            content = content.substring(1);
        }
        return content;
    }

    Tab getTabValidationXML(Window owner) {
        Tab t = new Tab("Validation (XML)");
        BorderPane bp = new BorderPane();
        t.setContent(bp);

        // create buttons etc
        Text text1 = new Text("AAS XML File:");
        FileSelector fileSelector = new FileSelector(null, FileSelector.extFilterXML);
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
                    String xml = readFileToString(fileSelector.getSelectedFile());
                    Set<String> errorMessages = new XmlSchemaValidator().validateSchema(xml);
                    if (errorMessages == null || errorMessages.isEmpty()) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Validation successful.");
                        alert.showAndWait();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setHeaderText("Validation failed.");
                        ListView lw = new ListView<String>();
                        for (String err : errorMessages) {
                            lw.getItems().add(err);
                        }
                        alert.getDialogPane().setContent(lw);
                        alert.setWidth(500);
                        alert.setHeight(400);
                        alert.setResizable(true);
                        alert.showAndWait();
                    }
                    // Environment env = new XmlDeserializer().read(fileSelector.getSelectedFile());
                } catch (Exception e) {
                    DialogUtil.showAlertDialog(e, owner);
                }
            }
        });

        return t;
    }

    Tab getTabValidationJSON(Window owner) {
        Tab t = new Tab("Validation (JSON)");
        BorderPane bp = new BorderPane();
        t.setContent(bp);

        // create buttons etc
        Text text1 = new Text("AAS JSON File:");
        FileSelector fileSelector = new FileSelector(null, FileSelector.extFilterJSON);
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
                    // https://github.com/eclipse-aas4j/aas4j/blob/main/dataformat-json/src/test/java/org/eclipse/digitaltwin/aas4j/v3/dataformat/json/JsonDeserializerTest.java
                    String json = readFileToString(fileSelector.getSelectedFile());
                    Set<String> errorMessages = new JsonSchemaValidator().validateSchema(json);
                    if (errorMessages == null || errorMessages.isEmpty()) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Validation successful.");
                        alert.showAndWait();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setHeaderText("Validation failed.");
                        ListView lw = new ListView<String>();
                        for (String err : errorMessages) {
                            lw.getItems().add(err);
                        }
                        alert.getDialogPane().setContent(lw);
                        alert.setWidth(500);
                        alert.setHeight(400);
                        alert.setResizable(true);
                        alert.showAndWait();
                    }
                    // Environment env = new JsonDeserializer().read(new FileInputStream(fileSelector.getSelectedFile()), Environment.class);
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