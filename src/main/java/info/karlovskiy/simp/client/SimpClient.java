package info.karlovskiy.simp.client;

import java.util.Properties;
import java.util.logging.Logger;

/**
 * @author karlovskiy
 * @since 1.0, 10/21/14
 */
public class SimpClient {

    private static final Logger logger = Logger.getLogger(SimpClient.class.getName());

    public static void main(String[] args) {

        logger.info("Starting SIMP client application");

        Properties appProps = new Properties();
        String defaultHost = System.getProperty("info.karlovskiy.simp.client.defaultHost");
        String defaultPort = System.getProperty("info.karlovskiy.simp.client.defaultPort");
        appProps.setProperty("defaultHost", defaultHost != null ? defaultHost : "localhost");
        appProps.setProperty("defaultPort", defaultPort != null ? defaultPort : "7777");
        appProps.setProperty("defaultClient", System.getProperty("user.name"));

        Application app = Application.getInstance();
        app.setProperties(appProps);

        logger.info("Stert building GUI");
        app.show();
    }

}
