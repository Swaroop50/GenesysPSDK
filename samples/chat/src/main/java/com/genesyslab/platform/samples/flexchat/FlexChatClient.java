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

package com.genesyslab.platform.samples.flexchat;

import java.util.HashMap;
import java.util.Map;

import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.Message;
import com.genesyslab.platform.webmedia.protocol.FlexChatProtocol;
import com.genesyslab.platform.webmedia.protocol.flexchat.MessageText;
import com.genesyslab.platform.webmedia.protocol.flexchat.FlexTranscript;
import com.genesyslab.platform.webmedia.protocol.flexchat.RequestResult;
import com.genesyslab.platform.webmedia.protocol.flexchat.events.EventStatus;
import com.genesyslab.platform.webmedia.protocol.flexchat.requests.RequestJoin;
import com.genesyslab.platform.webmedia.protocol.flexchat.requests.RequestLogin;
import com.genesyslab.platform.webmedia.protocol.flexchat.requests.RequestLogout;
import com.genesyslab.platform.webmedia.protocol.flexchat.requests.RequestRefresh;

/**
 * Sample demonstrates how multiple clients can interact with ChatServer using one connection.
 *
 */
public class FlexChatClient {

	private static FlexChatProtocol flexChat;
	private static String queue = "MM8.0:MultimediaSDK_Process";
	
	private static Map<String, ClientContext> clientMap = new HashMap<String, ClientContext>();
	
	public static void main(String[] args) throws Exception{
				
		flexChat = new FlexChatProtocol(new Endpoint("<chat-server host>", 8011));
		
		try{
			
			String nickNameOne = "AgentOne";
			String nickNameTwo = "AgentTwo";
					
			//setAutoRegister(true) can be useful only for one client. 
			//In this case channel will automatically send RequestLogin and store userId/secureKey obtained from response in the protocol object.
			//However, in most cases one connection is used for many chat clients.
			//In this case, recommended way is:
			// - setAutoRegister(false);
			// - send RequestLogin for each client explicitly
			// - store userId/secureKey and specify them explicitly in further chat requests.
			flexChat.setAutoRegister(false);
			flexChat.open();			
			System.out.println("Opened! ");			
						
			//Login to chat, store userId/secureKey obtained in response
			loginChat(nickNameOne);			
			loginChat(nickNameTwo);
			
			//Following method explicitly specify userId, secureKey in chat requests				
            //join
			joinChat(nickNameOne);				
			joinChat(nickNameTwo);
			
			//write something to chat
			writeIntoChat(nickNameOne, "Hello from "+nickNameOne);					
			writeIntoChat(nickNameTwo, "Hello from "+nickNameTwo);
			
			//logout
			logoutChat(nickNameOne);
			logoutChat(nickNameTwo);			
		}
		catch(Throwable t) {
			t.printStackTrace();
		}
		finally {
			flexChat.close();
			System.out.println("Closed! ");
		}		
	}
		
	
	private static RequestResult loginChat(String nickName) throws Exception {
		Message response = flexChat.request(RequestLogin.create(nickName, null, null));	
		RequestResult result = null;
		
		if(response instanceof EventStatus) {
			EventStatus status = (EventStatus)response;
			result = status.getRequestResult();							
			if(result.equals(RequestResult.Success)) {
				ClientContext context = new  ClientContext(nickName, status.getUserId(), status.getSecureKey());								
				clientMap.put(nickName, context);
			}
		}
		else 		
			throw new RuntimeException("Server didn't respond for login request.");	
		
		System.out.println(nickName + " login: " + RequestResult.Success);	
		return RequestResult.Success;
	}
	
	
	private static RequestResult joinChat(String nickName) throws Exception {
		RequestResult ret = null;
		ClientContext context = clientMap.get(nickName);
		if(context == null) {
			throw new RuntimeException("User wasn't logged in!");
		}
		
		//Set userId and secure key explicitly
		Message response = flexChat.request(RequestJoin.create(context.getUserId(), 
				context.getSecureKey(), null, queue, null));
		
		if(response instanceof EventStatus) {								
			EventStatus status = (EventStatus)response;			
			updateConext(nickName, status);												
			ret =  status.getRequestResult();
		}
		else
			ret = RequestResult.Error;	
		
		System.out.println(nickName + " join: " + ret);	
		return ret;
	}
	
	private static RequestResult writeIntoChat(String nickName, String message) throws Exception {
		RequestResult ret = null;
		ClientContext context = clientMap.get(nickName);
		if(context == null) {
			throw new RuntimeException("User wasn't logged in!");
		}
		
		//Set userId and secure key explicitly		
		RequestRefresh refresh = RequestRefresh.create(context.getUserId(), 
				context.getSecureKey(), 
				context.getPosition(), MessageText.create(message));
		
		Message response = flexChat.request(refresh);
		
		if(response instanceof EventStatus) {	
			EventStatus status = (EventStatus)response;									
			updateConext(nickName, status);						
			ret = status.getRequestResult();
		}
		else
			ret = RequestResult.Error;		
		
		System.out.println(nickName + " write to chat: " + ret);	
		return ret;
	}
	
	private static RequestResult logoutChat(String nickName) throws Exception {
		ClientContext context = clientMap.get(nickName);
		if(context == null) {
			System.out.println("Already logout.");
			return null;
		}		
		RequestLogout logout = RequestLogout.create(context.getUserId(), context.getSecureKey(), context.getPosition());		
		Message response = flexChat.request(logout);	
		RequestResult result = null;
		
		if(response instanceof EventStatus) {
			EventStatus status = (EventStatus)response;
			result = status.getRequestResult();							
			if(result.equals(RequestResult.Success)) {
				clientMap.remove(nickName);					
			}			
		}
		else 		
			throw new RuntimeException("Server didn't respond for login request.");	
		
		System.out.println(nickName + " logout: " + RequestResult.Success);	
		return RequestResult.Success;
	}
	
	
	public static void updateConext(String nickname, EventStatus status) {
		ClientContext context = clientMap.get(nickname);
		if(context == null) {
			return;
		}
		
		//store chat position and chat session
		FlexTranscript transcript = status.getFlexTranscript();
		if(transcript!=null) {
			String sId = transcript.getSessionId();
			if(sId!= null) 
				context.updatetSession(sId);
			Integer pos = transcript.getLastPosition();
			if(pos!=null)
				context.updatePosition(pos);
		}
		
		//Optionally, update secure key. In current server implementation secureKey is persistent.
		context.updateKey(status.getSecureKey());
	}
	
	//Store userId and secure key for logged in client
	public static class ClientContext {
		private final String nick;
		private final String id;		
		private String key;
		private Integer chatPosition;
		private String sessionId = "";
		
		public ClientContext(String nickname, String userId, String secureKey) {
			nick = nickname;
			id = userId;
			key = secureKey;	
			
		}
		public String getUserId() {
			return id;
		}		
		public String getSecureKey() {
			return key;
		}
		public void updateKey(String key) {
			this.key = key;
		}
		public void updatePosition(Integer pos) {
			chatPosition = pos;
		}
		public Integer getPosition() {
			return chatPosition;
		}
		public String getSession() {
			return sessionId;
		}		
		public void updatetSession(String session) {
			this.sessionId=session;
		}
		
		@Override
		public String toString() {
			return nick; 
		}
	}

}



