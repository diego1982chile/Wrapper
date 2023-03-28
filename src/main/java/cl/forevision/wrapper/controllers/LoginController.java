package cl.forevision.wrapper.controllers;

/**
 * Created by root on 08-03-23.
 */

import cl.forevision.wrapper.helpers.ConfigHelper;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginController {

    @FXML
    private Button signInBtn;

    private static final Logger logger = Logger.getLogger(LoginController.class.getName());

    @FXML
    public void initialize() {

        signInBtn.setOnMouseEntered(e -> signInBtn.setStyle("-fx-background-color: red"));
        signInBtn.setOnMouseExited(e -> signInBtn.setStyle("-fx-background-color: yellow"));

        signInBtn.setOnAction(event -> {
            logger.log(Level.INFO, "HOLA");
        });

    }


    @FXML
    public void exitApplication(ActionEvent event) {
        Platform.exit();
    }


}
