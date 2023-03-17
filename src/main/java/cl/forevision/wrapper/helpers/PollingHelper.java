package cl.forevision.wrapper.helpers;

import cl.forevision.wrapper.model.*;
import com.google.common.collect.Sets;
import javafx.application.Platform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
        }, 0, 60*1000);
    }


    private void poll() throws Exception {
        retailers = RetailerHelper.getInstance().getRetailers();

        List<Account> accounts = AccountHelper.getInstance().getAccounts();
        List<Retailer> retailers = RetailerHelper.getInstance().getRetailers();
        List<Parameter> parameters = ParamsHelper.getInstance().getParameters();
        List<Schedule> schedules = ScheduleHelper.getInstance().getSchedules();

        if (hasChanged(accounts, retailers, parameters, schedules)) {
            ProcessHelper.getInstance().restartInstances();
            this.accounts = accounts;
            this.retailers = retailers;
            this.parameters = parameters;
            this.schedules = schedules;
        }

    }

    private boolean hasChanged(List<Account> accounts, List<Retailer> retailers, List<Parameter> parameters, List<Schedule> schedules) {

        boolean hasNotChanged;

        hasNotChanged = Sets.intersection(accounts.stream().collect(Collectors.toSet()), this.accounts.stream().collect(Collectors.toSet()))
                .equals(accounts.stream().collect(Collectors.toSet()));

        if (!hasNotChanged) {
            return true;
        }

        hasNotChanged = Sets.intersection(retailers.stream().collect(Collectors.toSet()), this.retailers.stream().collect(Collectors.toSet()))
                .equals(retailers.stream().collect(Collectors.toSet()));

        if (!hasNotChanged) {
            return true;
        }

        hasNotChanged = Sets.intersection(parameters.stream().collect(Collectors.toSet()), this.parameters.stream().collect(Collectors.toSet()))
                .equals(parameters.stream().collect(Collectors.toSet()));

        if (!hasNotChanged) {
            return true;
        }

        hasNotChanged = Sets.intersection(schedules.stream().collect(Collectors.toSet()), this.schedules.stream().collect(Collectors.toSet()))
                .equals(schedules.stream().collect(Collectors.toSet()));

        if (!hasNotChanged) {
            return true;
        }

        return false;

    }

}
