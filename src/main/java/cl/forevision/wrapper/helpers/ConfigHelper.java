package cl.forevision.wrapper.helpers;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by root on 01-03-23.
 * This class holds required parameters during program runtime, namely:
 * Api URLs, Credentials
 */
public class ConfigHelper {

    public static final ConfigHelper instance = new ConfigHelper();

    public Map<String, String> CONFIG = new HashMap<>();

    private static final Logger logger = Logger.getLogger(ConfigHelper.class.getName());

    /**
     * Constructor privado para el Singleton del Factory.
     */
    public ConfigHelper() {

    }

    public static ConfigHelper getInstance() {
        return instance;
    }

    public void setParameter(String name, String value) {
        CONFIG.put(name, value);
    }

    public String getParameter(String name) {
        return CONFIG.get(name);
    }

}
