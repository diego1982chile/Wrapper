package cl.forevision.wrapper.helpers;

import cl.forevision.wrapper.model.Parameter;
import cl.forevision.wrapper.model.exceptions.MissingParameterException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static cl.forevision.wrapper.model.ParameterEnum.*;


/**
 * Created by des01c7 on 18-12-20.
 */
public class ParamsHelper {

    private static final ParamsHelper instance = new ParamsHelper();

    /** Logger para la clase */
    private static Logger logger;

    static LogHelper fh;

    private static String PARAMETERS_ENDPOINT;

    private List<Parameter> parameters;


    /**
     * Constructor privado para el Singleton del Factory.
     */
    private ParamsHelper() {

        PARAMETERS_ENDPOINT = ConfigHelper.getInstance().getParameter(BASE_URL_CONFIG.getParameter()) + "parameters";

        fh = LogHelper.getInstance();
        logger = Logger.getLogger(ParamsHelper.class.getName());
        logger.addHandler(fh);

    }

    public void loadParameters() throws Exception {

        Thread.sleep(2000);

        populateParameters();

        String errorTo = parameters.stream()
                .filter(e -> e.getName().equals(ERROR_TO.getParameter()))
                .map(Parameter::getValue)
                .findFirst()
                .orElseThrow(() -> new MissingParameterException("No parameter " + ERROR_TO.getParameter() + " found"));

        ConfigHelper.getInstance().setParameter(ERROR_TO.getParameter(), errorTo);

        String mailFromPassword = parameters.stream()
                .filter(e -> e.getName().equals(MAIL_FROM_PASSWORD.getParameter()))
                .map(Parameter::getValue)
                .findFirst()
                .orElseThrow(() -> new MissingParameterException("No parameter " + MAIL_FROM_PASSWORD.getParameter() + " found"));

        ConfigHelper.getInstance().setParameter(MAIL_FROM_PASSWORD.getParameter(), mailFromPassword);

        String mailFromUser = parameters.stream()
                .filter(e -> e.getName().equals(MAIL_FROM_USER.getParameter()))
                .map(Parameter::getValue)
                .findFirst()
                .orElseThrow(() -> new MissingParameterException("No parameter " + MAIL_FROM_USER.getParameter() + " found"));

        ConfigHelper.getInstance().setParameter(MAIL_FROM_USER.getParameter(), mailFromUser);

        String mailTo = parameters.stream()
                .filter(e -> e.getName().equals(MAIL_TO.getParameter()))
                .map(Parameter::getValue)
                .findFirst()
                .orElseThrow(() -> new MissingParameterException("No parameter " + MAIL_TO.getParameter() + " found"));

        ConfigHelper.getInstance().setParameter(MAIL_TO.getParameter(), mailTo);

    }

    public static ParamsHelper getInstance() {
        return instance;
    }

    private void populateParameters() throws IOException, MissingParameterException {

        HttpURLConnection conn = ConnectionHelper.getInstance().getConnection(PARAMETERS_ENDPOINT);

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

        String output;

        while ((output = br.readLine()) != null) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            ObjectReader objectReader = mapper.reader().forType(new TypeReference<List<Parameter>>() {
            });

            parameters = objectReader.readValue(output);

            if(parameters.isEmpty()) {
                throw new MissingParameterException("Empty parameter list retrieved from ScrapperConfig!!");
            }
        }

        conn.disconnect();

    }

    public List<Parameter> getParameters() throws IOException, MissingParameterException {

        List<Parameter> parameters = new ArrayList<>();

        HttpURLConnection conn = ConnectionHelper.getInstance().getConnection(PARAMETERS_ENDPOINT);

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

        String output;

        while ((output = br.readLine()) != null) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            ObjectReader objectReader = mapper.reader().forType(new TypeReference<List<Parameter>>() {
            });

            parameters = objectReader.readValue(output);

            if(parameters.isEmpty()) {
                throw new MissingParameterException("Empty parameter list retrieved from ScrapperConfig!!");
            }
        }

        conn.disconnect();

        return parameters;

    }



}


