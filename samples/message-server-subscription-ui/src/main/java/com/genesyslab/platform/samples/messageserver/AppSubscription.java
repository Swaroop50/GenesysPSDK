/*
 * Copyright (c) 2006 - 2019 Genesys Telecommunications Laboratories, Inc.
 * All rights reserved.
 *
 * For more  information on the Genesys Telecommunications Laboratories, Inc.
 * please see <http://www.genesyslab.com/>.
 */
package com.genesyslab.platform.samples.messageserver;

import com.genesyslab.platform.commons.log.Log;
import com.genesyslab.platform.commons.log.SimpleLoggerFactoryImpl;

/**
 * GUI example application.
 */
public class AppSubscription {

    static  {
        Log.setLoggerFactory(new SimpleLoggerFactoryImpl());
    }

    /**
     *  It runs GUI example application that demonstrate how to work with a message server
     *  (connect/disconnect, send/receive messages, subscribe/unsubscribe).
     *  <br>You can execute few instances and listen the messages sent from another instances if you subscribe for it.
     * @param args
     */
    public static void main(String[] args) {
        Logic app = new Logic();
        DialogSubscription form = new DialogSubscription(app);
        form.setVisible(true);
    }

}
