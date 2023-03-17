package cl.forevision.wrapper;

import cl.forevision.wrapper.controllers.MainController;
import cl.forevision.wrapper.helpers.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;

import java.io.Console;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import static cl.forevision.wrapper.model.ParameterEnum.*;

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

            Scanner input = new Scanner(System.in);

            Console console = System.console();

            String username = "diego.abelardo.soto@gmail.com";
            String password = "123";
            String baseUrlToken = "https://idp.internal.hhack.cl:8181/TokenService/api/";
            String baseUrlConfig = "https://cfg.internal.hhack.cl:8181/ScrapperConfig/api/";
            String scrapperPath = "/home/des01c7/IdeaProjects/Scrapper/target/Scrapper.jar";

            /*
            if (console == null)
            {
                while (StringUtils.isEmpty(username)) {
                    System.out.print("Enter USER_NAME: ");
                    username = input.nextLine();
                }

                while (StringUtils.isEmpty(password)) {
                    System.out.print("Enter PASSWORD: ");
                    password = input.nextLine();
                }

                while (StringUtils.isEmpty(baseUrlToken)) {
                    System.out.print("Enter BASE_URL_TOKEN: ");
                    baseUrlToken = input.nextLine();
                }

                while (StringUtils.isEmpty(baseUrlConfig)) {
                    System.out.print("Enter BASE_URL_CONFIG: ");
                    baseUrlConfig = input.nextLine();
                }

                while (StringUtils.isEmpty(scrapperPath)) {
                    System.out.print("Enter SCRAPPER_PATH: ");
                    scrapperPath = input.nextLine();
                }

            }
            else
            {
                while (StringUtils.isEmpty(username)) {
                    username = console.readLine("Enter USER_NAME: ");
                }

                while (StringUtils.isEmpty(password)) {
                    password = new String(console.readPassword("Enter PASSWORD: "));
                }

                while (StringUtils.isEmpty(baseUrlToken)) {
                    baseUrlToken = console.readLine("Enter BASE_URL_TOKEN: ");
                }

                while (StringUtils.isEmpty(baseUrlConfig)) {
                    baseUrlConfig = console.readLine("Enter BASE_URL_CONFIG: ");
                }

                while (StringUtils.isEmpty(scrapperPath)) {
                    scrapperPath = console.readLine("Enter SCRAPPER_PATH: ");
                }
            }
            */

            ConfigHelper.getInstance().setParameter(USER_NAME.getParameter(), username.trim());
            ConfigHelper.getInstance().setParameter(PASSWORD.getParameter(), password.trim());
            ConfigHelper.getInstance().setParameter(BASE_URL_TOKEN.getParameter(), baseUrlToken.trim());
            ConfigHelper.getInstance().setParameter(BASE_URL_CONFIG.getParameter(), baseUrlConfig.trim());
            ConfigHelper.getInstance().setParameter(SCRAPPER_PATH.getParameter(), scrapperPath.trim());

            TokenHelper.getInstance().start();

            while(ConfigHelper.getInstance().getParameter(TOKEN.getParameter()) == null) {
                Thread.sleep(1000);
            }

            logger.log(Level.INFO, "Loading parameters from ScrapperConfig...");
            ParamsHelper.getInstance().loadParameters();


            PollingHelper.getInstance().start();

            launch(args);


        } catch (SecurityException e) {
            logger.log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            /*
            Parent root = FXMLLoader.load(getClass().getResource("/views/main.fxml"));

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.load(getClass().getResource("/views/main.fxml").openStream());
            ControllerHelper.getInstance().setMainController(fxmlLoader.getController());
            */

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/main.fxml"));
            MainController mainController = new MainController();
            loader.setController(mainController);
            // calling load will now inject @FXML-annotated fields and call initialize() on myController
            Parent root = loader.load();
            ControllerHelper.getInstance().setMainController(mainController);

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();


        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void stop() throws Exception {
        System.out.println("Stage is closing");
        ProcessHelper.getInstance().stoptInstances();
        System.exit(0);
    }
}
