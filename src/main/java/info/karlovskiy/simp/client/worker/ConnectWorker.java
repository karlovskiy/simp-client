package info.karlovskiy.simp.client.worker;

import info.karlovskiy.simp.client.Application;

import javax.swing.SwingWorker;

/**
 * Here will be javadoc
 *
 * @author karlovskiy
 * @since 1.0, 10/22/14
 */
public class ConnectWorker extends SwingWorker<Void, Void> {

    private final String host;
    private final int port;
    private final String user;

    public ConnectWorker(String host, int port, String user) {
        this.host = host;
        this.port = port;
        this.user = user;
    }

    @Override
    protected Void doInBackground() throws Exception {
        Application application = Application.getInstance();
        application.connect(user, host, port);
        return null;
    }
}
