package cl.forevision.wrapper.helpers;

import cl.forevision.wrapper.model.Retailer;
import cl.forevision.wrapper.model.Schedule;
import cl.forevision.wrapper.model.exceptions.MissingParameterException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.jcabi.aspects.RetryOnFailure;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static cl.forevision.wrapper.model.ParameterEnum.BASE_URL_CONFIG;

/**
 * Created by des01c7 on 18-12-20.
 */
public class ScheduleHelper {

    public static final ScheduleHelper instance = new ScheduleHelper();

    static LogHelper fh;

    private static Logger logger;

    private static String SCHEDULES_ENDPOINT;

    private List<Schedule> schedules = new ArrayList<>();

    /**
     * Constructor privado para el Singleton del Factory.
     */
    private ScheduleHelper() {

        fh = LogHelper.getInstance();
        logger = Logger.getLogger(ScheduleHelper.class.getName());
        logger.addHandler(fh);

        SCHEDULES_ENDPOINT = ConfigHelper.getInstance().getParameter(BASE_URL_CONFIG.getParameter()) + "schedules";

    }

    public static ScheduleHelper getInstance() {
        return instance;
    }

    @RetryOnFailure(attempts = 3, delay = 1, unit = TimeUnit.SECONDS, types = {RuntimeException.class})
    public List<Schedule> getSchedules() throws Exception {

        List<Schedule> schedules = new ArrayList<>();

        try {

            HttpURLConnection conn = ConnectionHelper.getInstance().getConnection(SCHEDULES_ENDPOINT);

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output;

            while ((output = br.readLine()) != null) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                ObjectReader objectReader = mapper.reader().forType(new TypeReference<List<Schedule>>() {
                });

                schedules = objectReader.readValue(output);

                if(schedules.isEmpty()) {
                    throw new MissingParameterException("Empty schedule list retrieved from ScrapperConfig!!");
                }
            }

            conn.disconnect();

        }
        catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

        return schedules;

    }



}
