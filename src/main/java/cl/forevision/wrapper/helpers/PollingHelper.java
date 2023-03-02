package cl.forevision.wrapper.helpers;

import cl.forevision.wrapper.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by root on 01-03-23.
 * This class is responsible for detect any change in the configuration state
 * Depending on the change it should:
 * - DO_NOTHING
 * - RESTART PROPER Scrapper Agent
 * - RESTART ALL Scrapper Agents
 */
public class PollingHelper {

    public static final PollingHelper instance = new PollingHelper();

    private static Timer timer;

    static LogHelper fh;

    private List<Account> accounts = new ArrayList<>();
    private List<Retailer> retailers = new ArrayList<>();
    private List<Client> clients = new ArrayList<>();
    private List<Parameter> parameters = new ArrayList<>();
    private List<Schedule> schedules = new ArrayList<>();

    private static Logger logger;

    /**
     * Constructor privado para el Singleton del Factory.
     */
    public PollingHelper() {
        timer  = new Timer();
        fh = LogHelper.getInstance();
        logger = Logger.getLogger(PollingHelper.class.getName());
        logger.addHandler(fh);
    }

    public static PollingHelper getInstance() {
        return instance;
    }

    public void start() {
        timer.schedule( new TimerTask() {
            public void run() {
                try {
                    poll();
                } catch (Exception e) {
                    logger.log(Level.SEVERE, e.getMessage());
                }
            }
        }, 0, 30*60*1000);
    }

    private void populateRecords() throws Exception {
        retailers = RetailerHelper.getInstance().getRetailers();
    }

    private void poll() throws Exception {
        retailers = RetailerHelper.getInstance().getRetailers();
    }

}
