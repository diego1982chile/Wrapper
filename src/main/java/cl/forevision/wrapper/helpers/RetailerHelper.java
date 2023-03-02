package cl.forevision.wrapper.helpers;

import cl.forevision.wrapper.Log;
import cl.forevision.wrapper.helpers.LogHelper;
import cl.forevision.wrapper.model.Retailer;
import cl.forevision.wrapper.model.exceptions.MissingParameterException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static cl.forevision.wrapper.model.ParameterEnum.BASE_URL_CONFIG;
import static cl.forevision.wrapper.model.ParameterEnum.TOKEN;

/**
 * Created by des01c7 on 18-12-20.
 */
public class RetailerHelper {

    public static final RetailerHelper instance = new RetailerHelper();

    static LogHelper fh;

    private static Logger logger;

    private static String RETAILERS_ENDPOINT;

    private List<Retailer> retailers = new ArrayList<>();

    /**
     * Constructor privado para el Singleton del Factory.
     */
    private RetailerHelper() {

        fh = LogHelper.getInstance();
        logger = Logger.getLogger(RetailerHelper.class.getName());
        logger.addHandler(fh);

        RETAILERS_ENDPOINT = ConfigHelper.getInstance().getParameter(BASE_URL_CONFIG.getParameter()) + "retailers";

    }

    public static RetailerHelper getInstance() {
        return instance;
    }


    public List<Retailer> getRetailers() throws Exception {

        List<Retailer> retailers = new ArrayList<>();

        try {

            HttpURLConnection conn = ConnectionHelper.getInstance().getConnection(RETAILERS_ENDPOINT);

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output;

            while ((output = br.readLine()) != null) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                ObjectReader objectReader = mapper.reader().forType(new TypeReference<List<Retailer>>() {
                });

                retailers = objectReader.readValue(output);

                if(retailers.isEmpty()) {
                    throw new MissingParameterException("Empty retailer list retrieved from ScrapperConfig!!");
                }
            }

            conn.disconnect();

        }
        catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

        return retailers;

    }



}
