package com.siemens.aas4jfx;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.controlsfx.glyphfont.FontAwesome;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class FileSelector extends HBox {

    final TextField tf;
    File selected;

    File initialFile;
    File initialDirectory;

    Window owner;

    public FileSelector() {
        this(null, null);
    }

    public FileSelector(File initialFile, FileChooser.ExtensionFilter def) {
        this.initialFile = initialFile;

        tf = new TextField();
        tf.setEditable(false);
        tf.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                if (mouseEvent.getClickCount() == 2) {
                    if (selected != null) {
                        Desktop desktop = Desktop.getDesktop();
                        try {
                            desktop.open(selected.getParentFile()); // open folder
                            // desktop.open(selected); // open file
                        } catch (IOException ignored) {
                        }
                    }
                }
            }
        });
        // drag and drop file
        tf.setOnDragOver(event -> {
            if (event.getDragboard().hasFiles()) {
                /* allow for both copying and moving, whatever user chooses */
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });
        tf.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                if (!db.getFiles().isEmpty()) {
                    selected = db.getFiles().get(0);
                    setInitialDirectory(selected);
                    tf.setText(selected.getAbsolutePath());
                    success = true;
                }
            }
            /* let the source know whether the string was successfully transferred and used */
            event.setDropCompleted(success);

            event.consume();
        });

        this.getChildren().add(tf);
        HBox.setHgrow(tf, Priority.ALWAYS);

        javafx.scene.control.Button buttonSelect = new Button();
        buttonSelect.setTooltip(new Tooltip("Select"));

        buttonSelect.setGraphic(new FontAwesome().create(FontAwesome.Glyph.FOLDER_OPEN));
        buttonSelect.setOnAction(e -> {
            try {
                selected = getFileChooserXML(initialFile, def).showOpenDialog(owner);

                if (selected != null) {
                    setInitialDirectory(selected);
                    tf.setText(selected.getAbsolutePath());
                }
            } catch (Exception ex) {
                DialogUtil.showAlertDialog(ex, owner);
            }
        });
        this.getChildren().add(buttonSelect);
    }

    public void setInitialDirectory(File f) {
        if (f != null) {
            if (f.isDirectory()) {
                initialDirectory = f;
            } else if (f.getParentFile() != null && f.getParentFile().isDirectory()) {
                initialDirectory = f.getParentFile();
            }
        }
    }


    public File getSelectedFile() {
        return this.selected;
    }

    public void setOwner(Window owner) {
        this.owner = owner;
    }

    public static final String FILE_EXTENSION_XML = ".xml";
    public static final String FILE_EXTENSION_JSON = ".json";
    public static final String FILE_EXTENSION_ALL = ".*";

    public static final FileChooser.ExtensionFilter extFilterXML = new FileChooser.ExtensionFilter("XML", "*" + FILE_EXTENSION_XML);
    public static final FileChooser.ExtensionFilter extFilterJSON = new FileChooser.ExtensionFilter("JSON", "*" + FILE_EXTENSION_JSON);
    public static final FileChooser.ExtensionFilter extFilterAll = new FileChooser.ExtensionFilter("All", "*" + FILE_EXTENSION_ALL);

    public FileChooser getFileChooserXML(File fileProj, FileChooser.ExtensionFilter def) {
        FileChooser fileChooser = new FileChooser();
        // fileChooser.getExtensionFilters().add(extFilterXML);
        // fileChooser.getExtensionFilters().add(extFilterJSON);
        if (def != null) {
            fileChooser.getExtensionFilters().add(def);
        }
        fileChooser.getExtensionFilters().add(extFilterAll);

        fileChooser.setInitialDirectory(initialDirectory);

        return fileChooser;
    }
}
