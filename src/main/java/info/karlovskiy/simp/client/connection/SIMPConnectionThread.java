package info.karlovskiy.simp.client.connection;

import info.karlovskiy.simp.client.Application;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Here will be javadoc
 *
 * @author karlovskiy
 * @since 1.0, 10/21/14
 */
public class SIMPConnectionThread extends Thread {

    private static final Logger logger = Logger.getLogger(SIMPConnectionThread.class.getName());

    private SIMPConnection connection;
    private CountDownLatch latch;

    public SIMPConnectionThread(CountDownLatch latch, String threadName, String host, int port) throws Exception {
        super(threadName);
        this.latch = latch;
        InetAddress addr = InetAddress.getByName(host);
        Socket socket = new Socket(addr, port);
        try {
            connection = new SIMPConnection(socket);
        } catch (IOException e) {
            socket.close();
            throw e;
        }
        Application application = Application.getInstance();
        application.setConnection(connection);
    }

    public void run() {
        Application application = Application.getInstance();
        try {
            latch.countDown();
            logger.info("Start handling server responses");
            while (true) {
                ResponseType type = connection.readResponse();
                if (type == ResponseType.CONNECT_SUCCESSFULLY) {
                    List<String> users = connection.readConnectSuccessfully();
                    application.onConnect(users);
                } else if (type == ResponseType.USER_CONNECTED || type == ResponseType.USER_DISCONNECTED) {
                    String user = connection.readUser();
                    application.onUserConnectedOrDisconnected(type, user);
                } else if (type == ResponseType.MESSAGE) {
                    String user = connection.readUser();
                    String message = connection.readMessage();
                    application.onMessage(user, message);
                } else if (type == ResponseType.ERROR) {
                    int errorCode = connection.readError();
                    if (errorCode == ResponseType.USER_ALREADY_EXISTS) {
                        application.onUserAlreadyExists();
                    } else if (errorCode == ResponseType.SERVER_UNAVAILABLE) {
                        application.onServerUnavailable();
                    }
                }
            }

        } catch (IOException e) {
            application.onDisconnect();
            logger.log(Level.SEVERE, "SIMP connection error", e);
        } finally {
            try {
                connection.close();
            } catch (IOException ignored) {
            }
        }
    }
}
