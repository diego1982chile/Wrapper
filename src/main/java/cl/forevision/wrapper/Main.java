package cl.forevision.wrapper;

import cl.forevision.wrapper.helpers.*;
import org.apache.commons.lang3.StringUtils;

import java.io.Console;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import static cl.forevision.wrapper.model.ParameterEnum.*;

/**
 * Created by root on 01-03-23.
 */
public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    static LogHelper fh = LogHelper.getInstance();


    public static void main(String... args) throws Exception {

        // This block configure the logger with handler and formatter
        try {
            logger.addHandler(fh);

            Scanner input = new Scanner(System.in);

            Console console = System.console();

            String username = "";
            String password = "";
            String baseUrlToken = "";
            String baseUrlConfig = "";
            String scrapperPath = "";

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


        } catch (SecurityException e) {
            logger.log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
        }

    }

}
