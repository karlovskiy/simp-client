package info.karlovskiy.simp.client.worker;

import info.karlovskiy.simp.client.Application;

import javax.swing.SwingWorker;

/**
 * Here will be javadoc
 *
 * @author karlovskiy
 * @since 1.0, 10/22/14
 */
public class DisconnectWorker extends SwingWorker<Void, Void> {

    @Override
    protected Void doInBackground() throws Exception {
        Application application = Application.getInstance();
        application.disconnect();
        return null;
    }
}
