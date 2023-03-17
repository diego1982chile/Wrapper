package cl.forevision.wrapper.controllers;

/**
 * Created by root on 08-03-23.
 */

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class MainController {

    @FXML
    private TextArea console;
    private PrintStream ps ;

    @FXML
    private TabPane tabPane;

    @FXML
    public void initialize() {
        //tabPane = new TabPane();

        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        //ps = new PrintStream(new Console(console));

    }

    public void button(ActionEvent event) {
        System.setOut(ps);
        System.setErr(ps);
        System.out.println("Hello World");
    }

    public class Console extends OutputStream {
        private TextArea console;

        public Console(TextArea console) {
            this.console = console;
        }

        public void appendText(String valueOf) {
            Platform.runLater(() -> console.appendText(valueOf));
        }

        public void write(int b) throws IOException {
            appendText(String.valueOf((char)b));
        }
    }

    private Tab makeTab(String text, String background) {

        return new Tab(text);

    }

    @FXML
    public void addTab(String name) {
        Tab tab = new Tab(name);
        tabPane.getTabs().add(tab);
    }

    @FXML
    public void exitApplication(ActionEvent event) {
        Platform.exit();
    }


}
