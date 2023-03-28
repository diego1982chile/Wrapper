package cl.forevision.wrapper.helpers;


import cl.forevision.wrapper.Log;
import cl.forevision.wrapper.controllers.MainController;

import java.io.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Created by des01c7 on 18-12-20.
 */
public class LogHelper extends Handler {

    private static final LogHelper instance = new LogHelper();

    private List<Log> logs = new ArrayList<>();

    /**
     * Constructor privado para el Singleton del Factory.
     */
    private LogHelper() {
        /*
        this.fileControlList = new ArrayList<FileControl>();
        this.fileControlMap = new ConcurrentHashMap<String,FileControl>();
        */
    }

    public static LogHelper getInstance() {
        return instance;
    }


    public void log(Log log) {
        /* Se actualiza el mapa por nombres */
        logs.add(log);
    }

    public List<Log> getLogs() {
        return logs;
    }

    @Override
    public void publish(LogRecord record) {

        Instant instance = Instant.ofEpochMilli(record.getMillis());
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instance, ZoneId.systemDefault());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss");
        String string = localDateTime.format(formatter);

        String level = "INFO";

        switch (record.getLevel().getName()) {
            case "SEVERE":
                level = "ERROR";
                break;
            case "WARNING":
                level = "WARNING";
                break;
            default:
                break;
        }

        Log log = new Log(string, record.getSourceClassName(), record.getSourceMethodName(), record.getMessage(), level);

        MainController mainController = ControllerHelper.getInstance().getMainController();

        PlatformHelper.run(() -> {
            mainController.appendText("launcher", log.getMessage());
        });

        if(!logs.contains(log)) {
            System.out.println(record.getMessage());
            logs.add(log);
        }

        if(record.getLevel().equals(Level.SEVERE)) {
            try {
                ErrorHelper.getInstance().sendMail();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() throws SecurityException {
    }


    public void reset() {
        logs.clear();
    }


}
