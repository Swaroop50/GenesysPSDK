/*
 * Copyright (c) 2006 - 2019 Genesys Telecommunications Laboratories, Inc.
 * All rights reserved.
 *
 * For more  information on the Genesys Telecommunications Laboratories, Inc.
 * please see <http://www.genesyslab.com/>.
 */
package com.genesyslab.platform.samples.messageserver;

import com.genesyslab.platform.commons.protocol.*;
import com.genesyslab.platform.management.protocol.MessageServerProtocol;
import com.genesyslab.platform.management.protocol.messageserver.LogCategory;
import com.genesyslab.platform.management.protocol.messageserver.LogLevel;
import com.genesyslab.platform.management.protocol.messageserver.requests.RequestLogMessage;
import com.genesyslab.platform.management.protocol.messageserver.requests.subscription.RequestSubscribeAll;
import com.genesyslab.platform.management.protocol.messageserver.requests.subscription.RequestUnsubscribeAll;


/**
 * It is an example that describe how to work with a message server.
 * (connect, disconnect, subscribe, unsubscribe, send message, receive messages)
 */
public class Logic {

    public static String DEFAULT_HOST = "psdkw2k8-env";
    public static int DEFAULT_PORT = 3007;

    public static int DEFAULT_CLIENT_ID = 567;
    public static String DEFAULT_CLIENT_NAME = "testApp";
    public static int DEFAULT_CLIENT_TYPE = 2;
    public static String DEFAULT_CLIENT_HOST = "UA-MPOPEL-LT";

    private final MessageServerProtocol protocol;

    public Logic() {
        protocol = new MessageServerProtocol();
        protocol.setTimeout(5000);
    }

    public void registerChannelListener(ChannelListener listener) {
        protocol.addChannelListener(listener);
    }
    public boolean isOpened() {
        return ChannelState.Opened.equals(protocol.getState());
    }

    /**
     * Sets client info before opening protocol.
     * @param clientId
     * @param clientName
     * @param clientType
     * @param clientHost
     */
    public void setClientInfo(int clientId, String clientName, int clientType, String clientHost) {
        protocol.setClientId(Integer.valueOf(clientId));
        protocol.setClientName(clientName);
        protocol.setClientType(clientType);
        protocol.setClientHost(clientHost);
    }

    /**
     * Opens connection to a message server specified by {@code host} and {@code port} arguments
     * using client information that should be set before using {@link #setClientInfo(int, String, int, String)}.
     * @param host
     * @param port
     */
    public void connect(String host, int port) {
        protocol.setEndpoint(new Endpoint(host, port));
        protocol.openAsync();
    }

    /**
     * Closes the connection to the message server that has been opened before using {@link #connect(String, int)}
     */
    public void disconnect() {
        protocol.closeAsync();
    }

    /**
     * Gets default timeout for open/close/send operations.
     * @return timeout in milliseconds.
     */
    public long getTimeout() {
        return protocol.getTimeout();
    }

    /**
     * Subscribes for all events.
     * @return true if request has been sent successfully.
     */
    public boolean subscribeAll() {
        RequestSubscribeAll requestSubscribeAll = RequestSubscribeAll.create();
        try {
            protocol.send(requestSubscribeAll);
            return true;
        } catch (ProtocolException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Unsubscribes from all events.
     * @return true if request has been sent successfully.
     */
    public boolean unsubscribeAll() {
        RequestUnsubscribeAll requestUnsubscribeAll = RequestUnsubscribeAll.create();
        try {
            protocol.send(requestUnsubscribeAll);
            return true;
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     *
     * @param listener
     */
    public void executeMessageReceiver(final MessageListener listener) {

        if (listener == null) {
            throw new NullPointerException("listener");
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (!Thread.interrupted() && ChannelState.Opened.equals(protocol.getState())) {
                    try {
                        Message msg = protocol.receive();

                        if (msg != null) {
                            listener.onMessage(msg);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        new Thread(runnable, "Message receiver"){ {setDaemon(true);} }.start();
    }

    public void sendAlarm(String text) {
        RequestLogMessage request = RequestLogMessage.create();
        request.setEntryCategory(LogCategory.Application);
        request.setEntryId(30302);
        request.setEntryText(text);
        request.setLevel(LogLevel.Alarm);
        request.setClientHost(protocol.getClientHost());
//        AttributeList attrs = new AttributeList();
//        for (int i = 0; i < 1000; i++) {
//            attrs.put("key#" + i, "string value = " + i);
//        }
//        request.setAttributes(attrs);

        try {
            protocol.send(request);
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
    }

    public interface MessageListener {
        void onMessage(Message message);
    }
}
