package cl.forevision.wrapper.helpers;

import cl.forevision.wrapper.controllers.MainController;
import cl.forevision.wrapper.model.Retailer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import sun.misc.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

import static cl.forevision.wrapper.model.ParameterEnum.*;
import static cl.forevision.wrapper.model.ParameterEnum.SCRAPPER_PATH;

/**
 * Created by root on 05-03-23.
 */
public class ScrapperTask implements Runnable {

    private String retailer;

    private static Logger logger;

    static LogHelper fh;


    public ScrapperTask(String retailer) {
        this.retailer = retailer;
        fh = LogHelper.getInstance();
        logger = Logger.getLogger(TokenHelper.class.getName());
        logger.addHandler(fh);

    }

    @Override
    public void run() {

        try {
            String client = "bless";
            String retailerName = retailer;
            String baseUrlToken = ConfigHelper.getInstance().getParameter(BASE_URL_TOKEN.getParameter());
            String baseUrlConfig = ConfigHelper.getInstance().getParameter(BASE_URL_CONFIG.getParameter());
            String username = ConfigHelper.getInstance().getParameter(USER_NAME.getParameter());
            String password = ConfigHelper.getInstance().getParameter(PASSWORD.getParameter());
            String scrapperPath = ConfigHelper.getInstance().getParameter(SCRAPPER_PATH.getParameter());
            String[] arguments = new String[] {"java", "-jar", scrapperPath, "-client", client, "-retailer", retailerName, "-base_url_token", baseUrlToken, "-base_url_config", baseUrlConfig, "-username", username, "-password", password};

            System.out.println("Started " + retailer);
            ProcessBuilder pb = new ProcessBuilder(arguments);
            pb.redirectErrorStream(true);

            Thread.sleep(3000);

            MainController mainController = ControllerHelper.getInstance().getMainController();

            PlatformHelper.run(() -> {
                mainController.addTab(retailer);
            });

            Process p5 = pb.start();

            ProcessHelper.getInstance().registerInstance(retailer, p5);

            OutputStream pOut = p5.getOutputStream();;
            System.setOut(new PrintStream(pOut));
            InputStream pIn = p5.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(pIn));


            while (p5.isAlive()) {
                Thread.sleep(100);
                //System.out.println(UUID.randomUUID().toString());
                String line = reader.readLine();

                if(!line.isEmpty()) {
                    mainController.appendText(retailer, line);
                }
            }



        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

}
