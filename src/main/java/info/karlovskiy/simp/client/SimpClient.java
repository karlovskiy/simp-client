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

        if (args.length < 2) {
            throw new IllegalArgumentException("Illegal program arguments. Must be default host and port.");
        }

        Properties appProps = new Properties();
        appProps.setProperty("defaultHost", args[0]);
        appProps.setProperty("defaultPort", args[1]);
        appProps.setProperty("defaultClient", System.getProperty("user.name"));

        Application app = Application.getInstance();
        app.setProperties(appProps);

        logger.info("Stert building GUI");
        app.show();
    }

}
