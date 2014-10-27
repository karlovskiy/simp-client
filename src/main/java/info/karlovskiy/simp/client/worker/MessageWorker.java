package info.karlovskiy.simp.client.worker;

import info.karlovskiy.simp.client.Application;

import javax.swing.SwingWorker;

/**
 * Here will be javadoc
 *
 * @author karlovskiy
 * @since 1.0, 10/22/14
 */
public class MessageWorker extends SwingWorker<Void, Void> {

    private final String message;
    private final String user;

    public MessageWorker(String message, String user) {
        this.message = message;
        this.user = user;
    }

    @Override
    protected Void doInBackground() throws Exception {
        Application application = Application.getInstance();
        application.sendMessage(message, user);
        return null;
    }
}
