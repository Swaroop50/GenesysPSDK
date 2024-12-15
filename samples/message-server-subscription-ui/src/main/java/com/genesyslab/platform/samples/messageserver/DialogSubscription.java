/*
 * Copyright (c) 2006 - 2019 Genesys Telecommunications Laboratories, Inc.
 * All rights reserved.
 *
 * For more  information on the Genesys Telecommunications Laboratories, Inc.
 * please see <http://www.genesyslab.com/>.
 */
package com.genesyslab.platform.samples.messageserver;//package

import com.genesyslab.platform.commons.protocol.ChannelClosedEvent;
import com.genesyslab.platform.commons.protocol.ChannelErrorEvent;
import com.genesyslab.platform.commons.protocol.ChannelListener;
import com.genesyslab.platform.commons.protocol.Message;

import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;

/**
 * GUI example dialog.
 */
public class DialogSubscription extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

    private Logic logic;

    private JButton buttonConnect;
    private JButton buttonDisconnect;
    private JButton buttonSubscribe;
    private JButton buttonUnsubscribe;
    private JTextArea textArea;
    private ScrollPane scrollPane;
    private TextField fieldHost;
    private TextField fieldPort;
    private TextField fieldClientId;
    private TextField fieldClientName;
    private TextField fieldClientType;
    private TextField fieldClientHost;
    private Logic.MessageListener messageListener;
    private JButton buttonSend;

    public DialogSubscription(final Logic logic) throws HeadlessException {

        this.logic = logic;

        setTitle("Message server subscription example");

        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800,700);
        getContentPane().setLayout(new FlowLayout());


        addEndpointPanel();
        addClientInfoPanel();
        addControlPanel();
        addLogPanel();


        // create message listener
        messageListener = new Logic.MessageListener() {
            @Override
            public void onMessage(Message message) {
                log("Received    " + message);
            }
        };

        // register channel listener

        logic.registerChannelListener(new ChannelListener() {
            @Override
            public void onChannelOpened(EventObject event) {
                log("Connected");

                logic.executeMessageReceiver(messageListener);


                fieldHost.setEditable(false);
                fieldPort.setEditable(false);
                buttonConnect.setEnabled(false);
                buttonUnsubscribe.setEnabled(false);
                buttonSend.setEnabled(true);

                buttonDisconnect.setEnabled(true);
                buttonSubscribe.setEnabled(true);

            }

            @Override
            public void onChannelClosed(ChannelClosedEvent event) {
                if (event.getCause() == null) {
                    log("Disconnected");
                }
                else {
                    log("Connection error\n   " + (event.getCause() != null ? "" + getExceptionInfo(event.getCause()) : ""));
                }
                buttonConnect.setEnabled(true);
                buttonDisconnect.setEnabled(false);
                buttonSubscribe.setEnabled(false);
                buttonUnsubscribe.setEnabled(false);
                buttonSend.setEnabled(false);

                enableInputFields();
            }

            @Override
            public void onChannelError(ChannelErrorEvent event) {
                log("ChannelError " + (event.getCause() != null ? "" + getExceptionInfo(event.getCause()) : ""));
            }
        });



    }

    /**
     * Add GUI component to the example dialog.
     */
    private void addClientInfoPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(null, "Client info", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        add(panel);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        panel.add(new Label("Client id:", Label.RIGHT));
        fieldClientId = new TextField(""+Logic.DEFAULT_CLIENT_ID);
        panel.add(fieldClientId);

        panel.add(new Label("Client name:", Label.RIGHT));
        fieldClientName = new TextField(Logic.DEFAULT_CLIENT_NAME);
        fieldClientName.setColumns(15);
        panel.add(fieldClientName);

        panel.add(new Label("Client type:", Label.RIGHT));
        fieldClientType = new TextField(""+ Logic.DEFAULT_CLIENT_TYPE);
        panel.add(fieldClientType);

        panel.add(new Label("Client host:", Label.RIGHT));
        fieldClientHost = new TextField(Logic.DEFAULT_CLIENT_HOST);
        fieldClientHost.setColumns(25);
        panel.add(fieldClientHost);
    }

    /**
     * Generates string info that represent a passed exception as argument.
     */
    private String getExceptionInfo(Throwable exc) {
        StringBuilder sb = new StringBuilder(exc.toString());

        exc = exc.getCause();
        while (exc != null) {
            sb.append( "\n   cause: " );
            sb.append( exc );

            exc = exc.getCause();
        }

        return sb.toString();
    }


    /**
     * Add GUI component to the example dialog.
     */
    private void addLogPanel() {

        scrollPane = new ScrollPane();
        scrollPane.setSize(780, 420);
        add(scrollPane);

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);

        scrollPane.add(textArea);
        scrollPane.setWheelScrollingEnabled(true);

    }

    /**
     * Add GUI component to the example dialog.
     */
    private void addControlPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(null, null, TitledBorder.LEADING, TitledBorder.TOP, null, null));
        add(panel);
        panel.setLayout(new FlowLayout(FlowLayout.TRAILING, 5, 5));

        JPanel panel1 = new JPanel();
        panel.add(panel1);
        panel1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        buttonConnect = new JButton("Connect");
        buttonConnect.setEnabled(true);
        buttonConnect.setActionCommand("connect");
        buttonConnect.addActionListener(this);
        panel1.add(buttonConnect);

        buttonDisconnect = new JButton("Disconnect");
        buttonDisconnect.setEnabled(false);
        buttonDisconnect.setActionCommand("disconnect");
        buttonDisconnect.addActionListener(this);
        panel1.add(buttonDisconnect);

        buttonSend = new JButton("Send message");
        buttonSend.setEnabled(false);
        buttonSend.setActionCommand("send");
        buttonSend.addActionListener(this);
        panel1.add(buttonSend);

        JPanel panel2 = new JPanel();
        panel.add(panel2);
        panel2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));


        buttonSubscribe = new JButton("Subscribe all");
        buttonSubscribe.setEnabled(false);
        buttonSubscribe.setActionCommand("subscribeAll");
        buttonSubscribe.addActionListener(this);
        panel2.add(buttonSubscribe);

        buttonUnsubscribe = new JButton("Unsubscribe all");
        buttonUnsubscribe.setEnabled(false);
        buttonUnsubscribe.setActionCommand("unsubscribeAll");
        buttonUnsubscribe.addActionListener(this);
        panel2.add(buttonUnsubscribe);

        JButton buttonClear = new JButton("Clear logs");
        buttonClear.setEnabled(true);
        buttonClear.setActionCommand("clear");
        buttonClear.addActionListener(this);
        panel2.add(buttonClear);

    }



    /**
     * Add GUI component to the example dialog.
     */
    private void addEndpointPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(null, "Message server", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        add(panel);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        panel.add(new Label("Host:", Label.RIGHT));
        fieldHost = new TextField(Logic.DEFAULT_HOST);
        fieldHost.setColumns(34);
        panel.add(fieldHost);

        panel.add(new Label("Port:", Label.RIGHT));
        fieldPort = new TextField(""+ Logic.DEFAULT_PORT);
        panel.add(fieldPort);
    }



    /**
     * Process commands that is generated by GUI buttons.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String act = e.getActionCommand();
        if ("connect".equals(act)) {
            doActionConnect();
        }
        else if ("disconnect".equals(act)) {
            doActionDisconnect();
        }
        else if ("send".equals(act)) {
            doActionSend();
        }
        else if ("subscribeAll".equals(act)) {
            doActionSubscribe();
        }
        else if ("unsubscribeAll".equals(act)) {
            doActionUnsubscribe();
        }
        else if ("clear".equals(act)) {
            textArea.setText("");
        }
    }

    /**
     * Request a message text from user and send it.
     */
    private void doActionSend() {
        String text = JOptionPane.showInputDialog(this, "Input your message here", "Send message", JOptionPane.OK_CANCEL_OPTION);
        if (text == null || text.length() == 0) {
            return;
        }

        log("Sending '" + text + "' ...");

        logic.sendAlarm(text);
    }

    /**
     * Unsubscribes from any events.
     */
    private void doActionUnsubscribe() {
        log("Unsubscribing ...");

        buttonSubscribe.setEnabled(false);
        buttonUnsubscribe.setEnabled(false);

        if (logic.unsubscribeAll()) {
            log("Unsubscribed");
            buttonSubscribe.setEnabled(true);
            buttonUnsubscribe.setEnabled(false);
        }
        else {
            log("Failed to unsubscribe");
            buttonSubscribe.setEnabled(false);
            buttonUnsubscribe.setEnabled(true);
        }
    }

    /**
     * Subscribes for all events.
     */
    private void doActionSubscribe() {
        log("Subscribing ...");

        buttonSubscribe.setEnabled(false);
        buttonUnsubscribe.setEnabled(false);


        if (logic.subscribeAll()) {
            log("Subscribed");
            buttonSubscribe.setEnabled(false);
            buttonUnsubscribe.setEnabled(true);
        }
        else {
            log("Failed to subscribe");
            buttonSubscribe.setEnabled(true);
            buttonUnsubscribe.setEnabled(false);
        }
    }

    /**
     * Disconnects from the message server.
     */
    private void doActionDisconnect() {
        log("Disconnecting ...");

        disableControlButtons();

        logic.disconnect();
    }

    /**
     * Connects to a message server which host, port and client information are specified in the GUI dialog.
     */
    private void doActionConnect() {
        String host = fieldHost.getText().trim();

        if (host.length() == 0) {
            showMessageBox("[Connecting] Empty host: " + host);
            return;
        }

        int port;
        try {
            port = Integer.parseInt(fieldPort.getText().trim());
            if (port <= 0 || port > 0xfff) {
                showMessageBox("[Connecting] Invalid port number: " + port);
                return;
            }
        }
        catch (NumberFormatException ex) {
            showMessageBox("[Connecting] Invalid port number: " + fieldPort.getText().trim());
            return;
        }


        int clientId;
        try {
            clientId = Integer.parseInt(fieldClientId.getText().trim());
        }
        catch (NumberFormatException ex) {
            showMessageBox("[Connecting] Invalid client type: " + fieldClientId.getText().trim());
            return;
        }

        String clientName = fieldClientName.getText().trim();


        int clientType;
        try {
            clientType = Integer.parseInt(fieldClientType.getText().trim());
        }
        catch (NumberFormatException ex) {
            showMessageBox("[Connecting] Invalid client type: " + fieldClientType.getText().trim());
            return;
        }

        String clientHost = fieldClientHost.getText().trim();

        log("Connecting to " + host + ":" + port + " ( timeout = "+ logic.getTimeout()+" seconds )  ...");

        disableInputFields();
        disableControlButtons();

        logic.setClientInfo(clientId, clientName, clientType, clientHost);
        logic.connect(host, port);
    }


    private void disableControlButtons() {
        buttonConnect.setEnabled(false);
        buttonDisconnect.setEnabled(false);
        buttonSend.setEnabled(false);
        buttonSubscribe.setEnabled(false);
        buttonUnsubscribe.setEnabled(false);
    }

    private void disableInputFields() {
        fieldHost.setEditable(false);
        fieldPort.setEditable(false);
        fieldClientId.setEditable(false);
        fieldClientName.setEditable(false);
        fieldClientType.setEditable(false);
        fieldClientHost.setEditable(false);
    }

    private void enableInputFields() {
        fieldHost.setEditable(true);
        fieldPort.setEditable(true);
        fieldClientId.setEditable(true);
        fieldClientName.setEditable(true);
        fieldClientType.setEditable(true);
        fieldClientHost.setEditable(true);
    }


    private void showMessageBox(String msg) {
        log(msg);
        JOptionPane.showMessageDialog(this, msg);
    }

    private void log(String msg) {
        textArea.append(msg + "\n");
        scrollPane.setScrollPosition(0, Integer.MAX_VALUE);
    }



}
