package cl.forevision.wrapper.helpers;

import cl.forevision.wrapper.controllers.MainController;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by root on 01-03-23.
 * This class holds required parameters during program runtime, namely:
 * Api URLs, Credentials
 */
public class ControllerHelper {

    public static final ControllerHelper instance = new ControllerHelper();

    MainController mainController;

    private static final Logger logger = Logger.getLogger(ControllerHelper.class.getName());

    /**
     * Constructor privado para el Singleton del Factory.
     */
    public ControllerHelper() {

    }

    public static ControllerHelper getInstance() {
        return instance;
    }

    public MainController getMainController() {
        return mainController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
