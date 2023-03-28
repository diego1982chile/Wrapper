package cl.forevision.wrapper.helpers;

import cl.forevision.wrapper.model.Account;
import cl.forevision.wrapper.model.Retailer;
import cl.forevision.wrapper.model.exceptions.MissingParameterException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.jcabi.aspects.RetryOnFailure;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static cl.forevision.wrapper.model.ParameterEnum.BASE_URL_CONFIG;

/**
 * Created by root on 10-09-21.
 */
public class AccountHelper {

    public static final AccountHelper instance = new AccountHelper();

    public Map<String, String> CONFIG = new HashMap<>();

    private static String ACCOUNTS_ENDPOINT;

    private static final Logger logger = Logger.getLogger(ConfigHelper.class.getName());

    /**
     * Constructor privado para el Singleton del Factory.
     */
    public AccountHelper() {
        ACCOUNTS_ENDPOINT = ConfigHelper.getInstance().getParameter(BASE_URL_CONFIG.getParameter()) + "accounts/";
    }

    @RetryOnFailure(attempts = 3, delay = 1, unit = TimeUnit.SECONDS, types = {RuntimeException.class})
    public List<Account> getAccounts() throws Exception {

        List<Account> accounts = new ArrayList<>();

        try {

            HttpURLConnection conn = ConnectionHelper.getInstance().getConnection(ACCOUNTS_ENDPOINT);

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output;

            while ((output = br.readLine()) != null) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                ObjectReader objectReader = mapper.reader().forType(new TypeReference<List<Account>>() {
                });

                accounts = objectReader.readValue(output);

                if(accounts.isEmpty()) {
                    throw new MissingParameterException("Empty account list retrieved from ScrapperConfig!!");
                }
            }

            conn.disconnect();

        }
        catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

        return accounts;

    }



    public static AccountHelper getInstance() {
        return instance;
    }
}
