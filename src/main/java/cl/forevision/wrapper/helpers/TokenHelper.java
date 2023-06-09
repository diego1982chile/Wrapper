package cl.forevision.wrapper.helpers;

import cl.forevision.wrapper.model.Token;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcabi.aspects.RetryOnFailure;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static cl.forevision.wrapper.model.ParameterEnum.*;

/**
 * Created by des01c7 on 18-12-20.
 */
public class TokenHelper {

    private static final TokenHelper instance = new TokenHelper();

    private static Timer timer;

    /** Logger para la clase */
    private static Logger logger;

    static LogHelper fh;

    private static String TOKEN_ENDPOINT;

    /**
     * Constructor privado para el Singleton del Factory.
     */
    private TokenHelper() {

        TOKEN_ENDPOINT = ConfigHelper.getInstance().getParameter(BASE_URL_TOKEN.getParameter()) + "auth";

        timer  = new Timer();
        fh = LogHelper.getInstance();
        logger = Logger.getLogger(TokenHelper.class.getName());
        logger.addHandler(fh);

        /*
        try {
            updateToken();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

    }

    public static TokenHelper getInstance() {
        return instance;
    }

    public void start() {

        timer  = new Timer();

        timer.schedule( new TimerTask() {
            public void run() {
                try {
                    updateToken();
                } catch (IOException e) {
                    logger.log(Level.SEVERE, e.getMessage());
                }
            }
        }, 0, 30*60*1000);
    }

    @RetryOnFailure(attempts = 3, delay = 1, unit = TimeUnit.SECONDS, types = {RuntimeException.class})
    private void updateToken() throws IOException {

        HttpURLConnection conn = ConnectionHelper.getInstance().getConnection(TOKEN_ENDPOINT);

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

        String output;

        while ((output = br.readLine()) != null) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode tree = mapper.readTree(output);

            Token token = mapper.treeToValue(tree, Token.class);

            ConfigHelper.getInstance().setParameter(TOKEN.getParameter(), token.getToken());
        }

        conn.disconnect();

    }

    public boolean canLogin() throws IOException {

        HttpURLConnection conn = ConnectionHelper.getInstance().getConnection(TOKEN_ENDPOINT);

        return conn.getResponseCode() == 200;

    }

    public void stop() {
        logger.log(Level.INFO, "Stopping TokenHelper...");
        timer.cancel();
        timer.purge();
    }

}


