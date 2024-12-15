//===============================================================================
//Genesys Platform SDK Samples
//===============================================================================

//===============================================================================
//Any authorized distribution of any copy of this code (including any related
//documentation) must reproduce the following restrictions, disclaimer and copyright
//notice:

//The Genesys name, trademarks and/or logo(s) of Genesys shall not be used to name
//(even as a part of another name), endorse and/or promote products derived from
//this code without prior written permission from Genesys Telecommunications
//Laboratories, Inc.

//The use, copy, and/or distribution of this code is subject to the terms of the Genesys
//Developer License Agreement.  This code shall not be used, copied, and/or
//distributed under any other license agreement.

//THIS CODE IS PROVIDED BY GENESYS TELECOMMUNICATIONS LABORATORIES, INC.
//("GENESYS") "AS IS" WITHOUT ANY WARRANTY OF ANY KIND. GENESYS HEREBY
//DISCLAIMS ALL EXPRESS, IMPLIED, OR STATUTORY CONDITIONS, REPRESENTATIONS AND
//WARRANTIES WITH RESPECT TO THIS CODE (OR ANY PART THEREOF), INCLUDING, BUT
//NOT LIMITED TO, IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
//PARTICULAR PURPOSE OR NON-INFRINGEMENT. GENESYS AND ITS SUPPLIERS SHALL
//NOT BE LIABLE FOR ANY DAMAGE SUFFERED AS A RESULT OF USING THIS CODE. IN NO
//EVENT SHALL GENESYS AND ITS SUPPLIERS BE LIABLE FOR ANY DIRECT, INDIRECT,
//CONSEQUENTIAL, ECONOMIC, INCIDENTAL, OR SPECIAL DAMAGES (INCLUDING, BUT
//NOT LIMITED TO, ANY LOST REVENUES OR PROFITS).

//Copyright (c) 2007 - 2019 Genesys Telecommunications Laboratories, Inc. All rights reserved.
//===============================================================================

package com.genesyslab.platform.samples.stat;


import com.genesyslab.platform.commons.threading.AsyncInvoker;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Platform SDK channel may invoke its handlers using default channel thread (Single Thread Invoker). 
 * If handler processing long-time operation, it is possible to assign MultiThread invoker 
 * (for example based on Java Thread Pool) to allow task processing in separate threads.
 * However, in this case event delivering order (server message, connection events) is not guaranteed.
 * 
 * See http://docs.genesys.com/Documentation/PSDK/latest/Developer/ConnectingtoaServer#AsyncInvokers
 */
public class MultiThreadInvoker implements AsyncInvoker {
	private final ExecutorService executor = Executors.newCachedThreadPool();
			
	@Override
 	public void invoke(Runnable target) {		
		executor.execute(target);
 	}
 
 	@Override
 	public void dispose() {
 		executor.shutdown();
 	}
}
