package graphvisualizer.containers;

import graphvisualizer.graphview.SmartGraphPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import java.io.File;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MenuPane extends VBox {
    private Button randomGraphButton;
    private Button runKosarajuButton;
    private Button runTarjanButton;
    private Button strongConnectivityButton;
    private Button resetButton;
    private TextArea statusBox;

    public MenuPane() {
        setSpacing(40);
        setAlignment(Pos.TOP_CENTER);
        createButton();
        createStatusBox();
        loadStylesheet();
    }

    private void loadStylesheet() {
        try {
            getStylesheets().add(new File("menu.css").toURI().toURL().toExternalForm());
            this.getStyleClass().add("menu");
        } catch (MalformedURLException ex) {
            Logger.getLogger(SmartGraphPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void createButton() {
        randomGraphButton = new Button("Random DiGraph");
        randomGraphButton.getStyleClass().add("function-button");
        getChildren().add(randomGraphButton);

        runKosarajuButton = new Button("Visualize Kosaraju");
        runKosarajuButton.getStyleClass().add("function-button");
        getChildren().add(runKosarajuButton);

        runTarjanButton = new Button("Visualize Tarjan");
        runTarjanButton.getStyleClass().add("function-button");
        getChildren().add(runTarjanButton);

        resetButton = new Button("reset");
        resetButton.getStyleClass().add("reset-button");
        getChildren().add(resetButton);
    }

    private void createStatusBox() {
        statusBox = new TextArea();
        statusBox.setWrapText(true);
        statusBox.setEditable(false);
        statusBox.setFocusTraversable(false);
        statusBox.getStyleClass().add("status-box");
        getChildren().add(statusBox);
        setVgrow(statusBox, Priority.ALWAYS);
    }

    public TextArea getStatusBox() {
        return statusBox;
    }

    public void setRandomGraphButtonAction(EventHandler<ActionEvent> actionEvent) {
        randomGraphButton.setOnAction(actionEvent);
    }

    public void setRunKosarajuButtonAction(EventHandler<ActionEvent> actionEvent) {
        runKosarajuButton.setOnAction(actionEvent);
    }

    public void setRunTarjanButtonAction(EventHandler<ActionEvent> actionEvent) {
        runTarjanButton.setOnAction(actionEvent);
    }

    public void setResetButtonAction(EventHandler<ActionEvent> actionEvent) {
        resetButton.setOnAction(actionEvent);
    }
}
