package info.karlovskiy.simp.client;

import info.karlovskiy.simp.client.connection.ResponseType;
import info.karlovskiy.simp.client.connection.SIMPConnection;
import info.karlovskiy.simp.client.connection.SIMPConnectionThread;
import info.karlovskiy.simp.client.ui.MainPanel;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import java.awt.Image;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

/**
 * Here will be javadoc
 *
 * @author karlovskiy
 * @since 1.0, 10/22/14
 */
public class Application {

    private static final Logger logger = Logger.getLogger(Application.class.getName());

    private Properties properties;
    private MainPanel mainPanel;
    private SIMPConnection connection;

    public static Application getInstance() {
        return ApplicationHolder.INSTANCE;
    }

    public static String getProperty(String key) {
        return ApplicationHolder.INSTANCE.properties.getProperty(key);
    }

    public void show() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                JFrame frame = new JFrame("TCP/IP Chat");
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

                mainPanel = new MainPanel();
                frame.add(mainPanel);

                Image[] icons = new Image[]{
                        new ImageIcon(Application.class.getResource("icon128.png")).getImage(),
                        new ImageIcon(Application.class.getResource("icon64.png")).getImage(),
                        new ImageIcon(Application.class.getResource("icon32.png")).getImage(),
                        new ImageIcon(Application.class.getResource("icon16.png")).getImage()
                };
                frame.setIconImages(Arrays.asList(icons));

                frame.setSize(400, 500);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    public void connect(String user, String host, int port) throws Exception {
        logger.info("Connecting to server " + host + " on port " + port + " by user " + user);
        CountDownLatch latch = new CountDownLatch(1);
        SIMPConnectionThread serverConnection = new SIMPConnectionThread(latch, "SIMP connection", host, port);
        serverConnection.setDaemon(true);
        serverConnection.start();
        latch.await();
        logger.info("Connection established");
        connection.writeConnect(user);
    }

    public void disconnect() throws IOException {
        connection.writeDisconnect();
    }

    public void sendMessage(String message, String user) throws IOException {
        connection.writeMessage(user, message);
    }

    public void onMessage(final String user, final String message) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                mainPanel.onMessage(user, message);
            }

        });
    }

    public void onUserConnectedOrDisconnected(final ResponseType responseType, final String user) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                mainPanel.onUserConnectedOrDisconnected(responseType, user);
            }

        });
    }

    public void onUserAlreadyExists() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                mainPanel.onUserAlreadyExists();
            }

        });
    }

    public void onServerUnavailable() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                mainPanel.onServerUnavailable();
            }

        });
    }

    public void onConnect(final List<String> users) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                mainPanel.onConnect(users);
            }
        });
    }

    public void onDisconnect() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                mainPanel.onDisconnect();
            }
        });
    }

    public synchronized void setConnection(SIMPConnection connection) {
        this.connection = connection;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    private static class ApplicationHolder {
        private static final Application INSTANCE = new Application();
    }

    private Application() {
    }
}
