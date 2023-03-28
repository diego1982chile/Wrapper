package cl.forevision.wrapper;

import cl.forevision.wrapper.controllers.MainController;
import cl.forevision.wrapper.helpers.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by root on 01-03-23.
 */
public class Main extends Application {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    static LogHelper fh = LogHelper.getInstance();


    public static void main(String... args) throws Exception {

        // This block configure the logger with handler and formatter
        try {
            logger.addHandler(fh);


            launch(args);


        } catch (SecurityException e) {
            logger.log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/main.fxml"));
            MainController mainController = new MainController(primaryStage);
            loader.setController(mainController);
            // calling load will now inject @FXML-annotated fields and call initialize() on myController
            Parent root = loader.load();


            ControllerHelper.getInstance().setMainController(mainController);

            Scene scene = new Scene(root);
            scene.getStylesheets().add("/css/custom-style.css");

            //scene.getStylesheets().add(getClass().getResource("/css/custom-style.css").toExternalForm());

            primaryStage.setScene(scene);
            primaryStage.setTitle("Scrapper Launcher");
            primaryStage.setWidth(1000);
            primaryStage.setHeight(600);
            //primaryStage.setResizable(false);
            primaryStage.show();


        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void stop() throws Exception {
        System.out.println("Stage is closing");
        ProcessHelper.getInstance().stopInstances();
        System.exit(0);
    }
}
