package info.karlovskiy.simp.client.ui;

import info.karlovskiy.simp.client.Application;
import info.karlovskiy.simp.client.connection.ResponseType;
import info.karlovskiy.simp.client.worker.ConnectWorker;
import info.karlovskiy.simp.client.worker.DisconnectWorker;
import info.karlovskiy.simp.client.worker.MessageWorker;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

/**
 * Here will be javadoc
 *
 * @author karlovskiy
 * @since 1.0, 10/21/14
 */
public class MainPanel extends JPanel {

    private JLabel status;
    private JButton connect;
    private JTextArea msgArea;
    private JTextArea usersArea;
    private JTextField nickname;

    private ConnectedUsers connectedUsers;

    public MainPanel() {
        connectedUsers = new ConnectedUsers();

        setLayout(new BorderLayout());

        final JTextField host = new JTextField();
        host.setText(Application.getProperty("defaultHost"));
        final JTextField port = new JTextField();
        port.setText(Application.getProperty("defaultPort"));
        nickname = new JTextField();
        nickname.setText(Application.getProperty("defaultClient"));

        Dimension labelDimension = new Dimension(110, 18);

        JLabel hostLabel = new JLabel("Host: ");
        hostLabel.setPreferredSize(labelDimension);
        hostLabel.setLabelFor(host);

        JLabel portLabel = new JLabel("Port: ");
        portLabel.setPreferredSize(labelDimension);
        portLabel.setLabelFor(port);

        JLabel nicknameLabel = new JLabel("Nickname: ");
        nicknameLabel.setPreferredSize(labelDimension);
        nicknameLabel.setLabelFor(nickname);

        connect = new JButton("Connect");
        connect.setPreferredSize(new Dimension(110, 25));
        connect.setActionCommand("connect");

        status = new JLabel("Disconnected...");
        status.setForeground(Color.RED);
        status.setFont(new Font("Serif", Font.BOLD, 11));


        JPanel textControlsPane = new JPanel();
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        textControlsPane.setLayout(gridbag);

        JComponent[] labels = {hostLabel, portLabel, nicknameLabel};
        JComponent[] textFields = {host, port, nickname};
        addGridBagRows(labels, textFields, textControlsPane);

        c.gridwidth = GridBagConstraints.RELATIVE;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(5, 0, 0, 0);
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0.0;
        textControlsPane.add(connect, c);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(5, 5, 0, 5);
        c.fill = GridBagConstraints.NONE;
        c.weightx = 1.0;
        textControlsPane.add(status, c);


        textControlsPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Server"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        usersArea = new JTextArea();
        usersArea.setFont(new Font("Serif", Font.PLAIN, 12));
        usersArea.setLineWrap(true);
        usersArea.setWrapStyleWord(true);
        usersArea.setEditable(false);
        JScrollPane usersScrollPane = new JScrollPane(usersArea);
        usersScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        usersScrollPane.setPreferredSize(new Dimension(150, 120));
        usersScrollPane.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder("Users"),
                                BorderFactory.createEmptyBorder(5, 5, 5, 5)),
                        usersScrollPane.getBorder()));


        JPanel leftPane = new JPanel(new BorderLayout());
        leftPane.add(textControlsPane, BorderLayout.CENTER);
        leftPane.add(usersScrollPane, BorderLayout.EAST);

        msgArea = new JTextArea();
        msgArea.setFont(new Font("Serif", Font.PLAIN, 12));
        msgArea.setLineWrap(true);
        msgArea.setWrapStyleWord(true);
        msgArea.setEditable(false);
        JScrollPane msgScrollPane = new JScrollPane(msgArea);
        msgScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        msgScrollPane.setPreferredSize(new Dimension(400, 400));
        msgScrollPane.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder("Messages"),
                                BorderFactory.createEmptyBorder(5, 5, 5, 5)),
                        msgScrollPane.getBorder()));


        final JTextArea txtArea = new JTextArea();
        txtArea.setFont(new Font("Serif", Font.PLAIN, 12));
        txtArea.setLineWrap(true);
        txtArea.setWrapStyleWord(true);
        txtArea.setEditable(true);
        JScrollPane txtScrollPane = new JScrollPane(txtArea);
        txtScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        txtScrollPane.setPreferredSize(new Dimension(400, 34));

        final JButton send = new JButton("Send");

        JPanel txtPanel = new JPanel(new BorderLayout());
        txtPanel.add(txtScrollPane, BorderLayout.CENTER);
        txtPanel.add(send, BorderLayout.EAST);
        txtPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createEmptyBorder(),
                                BorderFactory.createEmptyBorder(5, 5, 5, 5)),
                        txtPanel.getBorder()));

        add(leftPane, BorderLayout.NORTH);
        add(msgScrollPane, BorderLayout.CENTER);
        add(txtPanel, BorderLayout.SOUTH);

        connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String actionCommand = e.getActionCommand();
                if ("connect".equals(actionCommand)) {
                    String hostString = host.getText();
                    int portInt = Integer.parseInt(port.getText());
                    String user = nickname.getText();
                    new ConnectWorker(hostString, portInt, user)
                            .execute();
                } else if ("disconnect".equals(actionCommand)) {
                    new DisconnectWorker()
                            .execute();
                }
            }
        });

        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = txtArea.getText();
                String user = nickname.getText();
                if (message != null && !message.isEmpty()) {
                    txtArea.setText("");
                    new MessageWorker(message, user)
                            .execute();
                }
            }
        });

        txtArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                int modifiers = e.getModifiers();
                if (code == KeyEvent.VK_ENTER && modifiers == KeyEvent.CTRL_MASK) {
                    send.doClick();
                }
            }
        });

    }

    public void onMessage(String user, String message) {
        msgArea.append(user + ": " + message + "\n");
    }

    public void onUserConnectedOrDisconnected(ResponseType responseType, String user) {
        if (ResponseType.USER_CONNECTED == responseType) {
            connectedUsers.add(user);
            msgArea.append("Server: '" + user + "' connected\n");
        } else if (ResponseType.USER_DISCONNECTED == responseType) {
            connectedUsers.remove(user);
            msgArea.append("Server: '" + user + "' disconnected\n");
        }
        usersArea.setText(connectedUsers.asText());
    }

    public void onConnect(List<String> users) {
        status.setText("Connected...");
        status.setForeground(Color.GREEN);

        connect.setText("Disconnect");
        connect.setActionCommand("disconnect");

        connectedUsers.init(users);
        usersArea.setText(connectedUsers.asText());
    }

    public void onDisconnect() {
        status.setText("Disconnected...");
        status.setForeground(Color.RED);

        connect.setText("Connect");
        connect.setActionCommand("connect");

        usersArea.setText("");
    }

    public void onUserAlreadyExists() {
        msgArea.append("Server: user '" + nickname.getText() + "' is already connected.\n");
    }

    public void onServerUnavailable() {
        msgArea.append("Server unavailable.\n");
    }

    private void addGridBagRows(JComponent[] left, JComponent[] right, Container container) {
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.EAST;
        int size = left.length;
        if (right.length != size) {
            throw new IllegalArgumentException("Number of left and right components not the same.");
        }
        for (int i = 0; i < size; i++) {
            c.gridwidth = GridBagConstraints.RELATIVE; // next-to-last
            c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.NONE; // reset to default
            c.weightx = 0.0; // reset to default
            container.add(left[i], c);

            c.gridwidth = GridBagConstraints.REMAINDER; // end row
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1.0;
            container.add(right[i], c);
        }
    }
}
