package com.siemens.aas4jfx;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Window;

import java.io.PrintWriter;
import java.io.StringWriter;

public class DialogUtil {

    public static void showAlertDialog(Throwable e, Window owner) {
        showAlertDialog(e, "", owner);
    }

    public static void showAlertDialog(Throwable e, String msg, Window owner) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Issues encountered. " + (msg == null ? "" : msg));
        alert.setContentText(e.getMessage());

        // create expandable exception
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        // Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);

        // alert.initStyle(StageStyle.UNDECORATED);
        if (owner != null) {
            // needed for stages that use stage.setAlwaysOnTop(true)
            alert.initOwner(owner);
        }
        alert.showAndWait();
    }
}
