package cl.forevision.wrapper.helpers;

import cl.forevision.wrapper.model.Retailer;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static cl.forevision.wrapper.model.ParameterEnum.*;

/**
 * Created by root on 01-03-23.
 * This class manages processes being executed during current execution
 */
public class ProcessHelper {

    private static final ProcessHelper instance = new ProcessHelper();

    Map<String, Process> scrapperInstances = new HashMap<>();

    /** Logger para la clase */
    private static final Logger logger = Logger.getLogger(ProcessHelper.class.getName());

    String id;

    /**
     * Constructor privado para el Singleton del Factory.
     */
    private ProcessHelper() {
        id = "Wrapper_" + LocalDate.now().toString();

    }

    public static ProcessHelper getInstance() {
        return instance;
    }

    public void restartInstances() throws Exception {

        for (String s : scrapperInstances.keySet()) {
            scrapperInstances.get(s).destroy();
        }

        for (Retailer retailer : RetailerHelper.getInstance().getRetailers()) {
            String retailerName = retailer.getName();
            String baseUrlToken = ConfigHelper.getInstance().getParameter(BASE_URL_TOKEN.getParameter());
            String baseUrlConfig = ConfigHelper.getInstance().getParameter(BASE_URL_CONFIG.getParameter());
            String username = ConfigHelper.getInstance().getParameter(USER_NAME.getParameter());
            String password = ConfigHelper.getInstance().getParameter(PASSWORD.getParameter());
            String scrapperPath = ConfigHelper.getInstance().getParameter(SCRAPPER_PATH.getParameter());
            String[] arguments = new String[] {"java", "-jar", scrapperPath, "-retailer", retailerName, "-base_url_token", baseUrlToken, "-base_url_config", baseUrlConfig, "-username", username, "-password", password};
            Process proc = new ProcessBuilder(arguments).start();
            scrapperInstances.put(retailer.getName(), proc);
        }

    }

}
