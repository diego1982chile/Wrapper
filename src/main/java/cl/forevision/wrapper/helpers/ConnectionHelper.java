package cl.forevision.wrapper.helpers;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static cl.forevision.wrapper.model.ParameterEnum.TOKEN;

/**
 * Created by root on 01-03-23.
 * This class holds required parameters during program runtime, namely:
 * Api URLs, Credentials
 */
public class ConnectionHelper {

    public static final ConnectionHelper instance = new ConnectionHelper();

    private static final Logger logger = Logger.getLogger(ConnectionHelper.class.getName());

    /**
     * Constructor privado para el Singleton del Factory.
     */
    public ConnectionHelper() {

    }

    public static ConnectionHelper getInstance() {
        return instance;
    }

    public HttpURLConnection getConnection(String path) throws IOException {

        URL url = new URL(path);

        HttpURLConnection conn;;

        if(url.openConnection() instanceof HttpsURLConnection) {
            conn = (HttpsURLConnection) url.openConnection();
        }
        else {
            conn = (HttpURLConnection) url.openConnection();
        }

        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + ConfigHelper.getInstance().getParameter(TOKEN.getParameter()));

        return conn;
    }

}
