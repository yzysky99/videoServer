/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package com.smarthome.server;

import java.awt.Image;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.LoggerFactory;

import com.smarthome.RecvDataHandler;
import com.smarthome.app.AppCmd;
import com.smarthome.message.AppMessage;
import com.smarthome.utils.Constants;


public class TcpAppServer extends IoHandlerAdapter {
	public static Logger logger = LoggerFactory.getLogger(RecvDataHandler.class + "");

    public static final int MAX_RECEIVED = 100000;
    
    private final Set<IoSession> sessions = Collections
				.synchronizedSet(new HashSet<IoSession>());

    private final Set<String> users = Collections
            .synchronizedSet(new HashSet<String>());
    
    private final Set<String> devices = Collections
            .synchronizedSet(new HashSet<String>());
    
	private final Set<String> names = Collections
				.synchronizedSet(new HashSet<String>());
//	
	//保存APP和device的session
//	HashMap<String, IoSession> userSessionsMap = new HashMap<String, IoSession>();
//	HashMap<String, IoSession> deviceSessionsMap = new HashMap<String, IoSession>();
	
	//保存session
//	public static ConcurrentHashMap<Long, IoSession> sessionsConcurrentHashMap = new ConcurrentHashMap<Long, IoSession>();

	
	private String userId; //用户ID，唯一
	private String deviceId; //设备ID，唯一，MAC地址
	private String deviceType; //设备类型，灯、插座、空调等
	
    public TcpAppServer(int port) throws IOException {
        IoAcceptor acceptor = new NioSocketAcceptor();
        
        DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
        chain.addLast( "logger", new LoggingFilter() );
        chain.addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"),LineDelimiter.WINDOWS.getValue(),LineDelimiter.WINDOWS.getValue())));
        
        acceptor.getSessionConfig().setReadBufferSize(2048);
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
        
        acceptor.setHandler(this);

        acceptor.bind(new InetSocketAddress(port));

        System.out.println("Server started..." + port);
    }
    
    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
		Logger log = LoggerFactory.getLogger(TcpAppServer.class);
    	System.out.println("messageReceived..." + message.toString());
    	
    	JSONObject json = JSONObject.fromObject(message.toString());
    	String msgType = (String) json.get("msgType");
		if (msgType.equalsIgnoreCase("app")) {
			JSONObject appObject = json.getJSONObject("appMsg");
			AppCmd appCmd = new AppCmd(session, appObject.toString(), names, sessions);
			appCmd.appMsg();
		} else if (msgType.equalsIgnoreCase("device")) {
			deviceMsg(session, message.toString());
		}
    }
    
    private IoSession findUserSessionByDeviceId(String deviceId){
    	IoSession session = null;
    	return session;
    }
    
    private IoSession findDeviceSessionByUserId(String userId){
    	IoSession session = null;
    	return session;
    	
    }
    
    private void deviceMsg(IoSession session, String message) {
     	JSONObject jsonObject = JSONObject.fromObject(message.toString());
     	JSONObject deviceJsonObject = jsonObject.getJSONObject("device");
     	
     	 String device = (String) session.getAttribute("device");
     	 
     	String deviceId = deviceJsonObject.getString("device_id");
     	int cmd = deviceJsonObject.getInt("cmd");
        
     	  System.out.println("cmd: " + cmd);
     	  
     	 switch (cmd){
     	 case Constants.LOGIN:
     		String name = "rtmp_device";
     		String rtmpUrl = "rtmp://119.23.240.132:1935/live/12345";
     		JSONObject respLogin = new JSONObject();
     		respLogin.put("status", "success");
     		
     		JSONObject respDevice = new JSONObject();
     		respDevice.put("device_id", deviceId);
     		respDevice.put("name", name);
     		respDevice.put("rtmpUrl", rtmpUrl);
     		
     		JSONArray usersjJsonArray = new JSONArray();	
     		usersjJsonArray.add("111111");
     		usersjJsonArray.add("222222");
     		usersjJsonArray.add("333333");
     		respDevice.put("users", usersjJsonArray);
     		respLogin.put("device", respDevice);
     		
     		session.write(respLogin.toString());
            System.out.println("resp data: " + respLogin.toString());
            
            if (devices.contains(device)) {
                System.out.println("device session is add!");
                return;
            }
            
     		 sessions.add(session);
             session.setAttribute("ID", deviceId);
             break;
         case Constants.SEND_DATA:
        	 String data = deviceJsonObject.getString("data");
        	
    		JSONObject respSendData = new JSONObject();
    		respSendData.put("status", "success");
    		respSendData.put("device_id", deviceId);
//    		respSendData.put("name", name);
    		
    		session.write(respSendData.toString());
    	      System.out.println("resp data: " + respSendData.toString());
    	 
             break;
             
         case Constants.LOGOUT:
     		JSONObject respLogout = new JSONObject();
     		respLogout.put("status", "success");
     		respLogout.put("device_id", deviceId);
     		session.write(respLogout.toString());
     	    System.out.println("resp data: " + respLogout.toString());
     	      
             session.closeNow();
        	 break;

         default:
             System.out.println("Unhandled cmd: " + cmd);
             break;
         }
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		 System.out.println("Message Sent..." + message);
	}

    @Override
    public void sessionClosed(IoSession session) throws Exception {
	    String user = (String) session.getAttribute("user");
        names.remove(user);
        sessions.remove(session);
        System.out.println("Session closed...");
    }
    
    
    @Override
    public void sessionCreated(IoSession session) throws Exception {
    	session.getService().getManagedSessions();
    	
        System.out.println("Session created...");
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
//        System.out.println("Session idle...");
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        System.out.println("Session Opened...");
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        cause.printStackTrace();
        session.closeNow();
        logger.info("exceptionCaught...");
    }
  
}
