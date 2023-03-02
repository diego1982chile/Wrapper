package cl.forevision.wrapper.helpers;

import cl.forevision.wrapper.model.Retailer;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by root on 01-03-23.
 * This class holds processes being executed during current execution
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
            String[] arguments = new String[] {"Scrapper.jar", "arg0", "arg1", "etc"};
            Process proc = new ProcessBuilder(arguments).start();
            scrapperInstances.put(retailer.getName(), proc);
        }

    }

}
