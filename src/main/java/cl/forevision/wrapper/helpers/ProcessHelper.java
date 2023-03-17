package cl.forevision.wrapper.helpers;

import cl.forevision.wrapper.model.Retailer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import static cl.forevision.wrapper.model.ParameterEnum.*;

/**
 * Created by root on 01-03-23.
 * This class manages processes being executed during current execution
 */
public class ProcessHelper {

    private static final ProcessHelper instance = new ProcessHelper();

    /** Logger para la clase */
    private static final Logger logger = Logger.getLogger(ProcessHelper.class.getName());

    String id;

    ExecutorService service;

    /**
     * Constructor privado para el Singleton del Factory.
     */
    public ProcessHelper() {
        id = UUID.randomUUID().toString();
    }

    public static ProcessHelper getInstance() {
        return instance;
    }

    public void restartInstances() throws Exception {

        if (service != null) {
            service.shutdownNow();
        }

        service = Executors.newFixedThreadPool(RetailerHelper.getInstance().getRetailers().size());

        for (Retailer retailer : RetailerHelper.getInstance().getRetailers()) {
            service.submit(new ScrapperTask(retailer.getName()));
        }

    }

    public void stoptInstances() throws Exception {

        if (service != null) {
            service.shutdownNow();
        }

    }

}
