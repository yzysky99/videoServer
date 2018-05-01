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
import net.sf.json.JSONObject;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
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
import com.smarthome.push.PushMsgToSingleDevice;


public class TcpDeviceServer extends IoHandlerAdapter {
	public static Logger logger = LoggerFactory.getLogger(RecvDataHandler.class + "");

    public static final int MAX_RECEIVED = 100000;
    
    private final Set<IoSession> sessions = Collections
				.synchronizedSet(new HashSet<IoSession>());

	private final Set<String> names = Collections
				.synchronizedSet(new HashSet<String>());

	//保存APP和device的session
	HashMap<String, IoSession> userSessionsMap = new HashMap<String, IoSession>();
	HashMap<String, IoSession> deviceSessionsMap = new HashMap<String, IoSession>();
	
	//保存session
	public static ConcurrentHashMap<Long, IoSession> sessionsConcurrentHashMap = new ConcurrentHashMap<Long, IoSession>();

//    private final Set<IoSession> sessions = Collections
//            .synchronizedSet(new HashSet<IoSession>());
//
//    private final Set<String> users = Collections
//            .synchronizedSet(new HashSet<String>());
    
	
	private String userId; //用户ID，唯一
	private String deviceId; //设备ID，唯一，MAC地址
	private String deviceType; //设备类型，灯、插座、空调等
	
    public TcpDeviceServer(int port) throws IOException {
        NioSocketAcceptor acceptor = new NioSocketAcceptor();
        
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
    	
    	  String user = (String) session.getAttribute("user");
        sessions.add(session);
        session.setAttribute("name", user);
    	
//    case ChatCommand.LOGIN:
//
//        if (user != null) {
//            session.write("LOGIN ERROR user " + user
//                    + " already logged in.");
//            return;
//        }
//
//        if (result.length == 2) {
//            user = result[1];
//        } else {
//            session.write("LOGIN ERROR invalid login command.");
//            return;
//        }
//
//        // check if the username is already used
//        if (users.contains(user)) {
//            session.write("LOGIN ERROR the name " + user
//                    + " is already used.");
//            return;
//        }
//
//        sessions.add(session);
//        session.setAttribute("user", user);
//        MdcInjectionFilter.setProperty(session, "user", user);
//     // Allow all users
//        users.add(user);
//        session.write("LOGIN OK");
//        break;
        
    	
		Logger log = LoggerFactory.getLogger(TcpDeviceServer.class);
    	System.out.println("messageReceived..." + message.toString());
    	
    	JSONObject json = JSONObject.fromObject(message.toString());
    	
    	PushMsgToSingleDevice pushMsgToSingleDevice = new PushMsgToSingleDevice();
    	pushMsgToSingleDevice.pushDataToDevice(json);
    	session.write("OK");
    }
    
    
    private void deviceMsg(String message) {
		// TODO Auto-generated method stub
		
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
