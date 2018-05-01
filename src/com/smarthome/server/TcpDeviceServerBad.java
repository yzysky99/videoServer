package com.smarthome.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.logging.Logger;

import net.sf.json.JSONObject;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.smarthome.RecvDataHandler;
import com.smarthome.image.ImageProtocolCodecFactory;
import com.smarthome.push.PushMsgToSingleDevice;

public class TcpDeviceServerBad extends IoHandlerAdapter {
	public static Logger logger = Logger.getLogger(RecvDataHandler.class + "");

    public static final int MAX_RECEIVED = 100000;

  //保存APP和device的session
    public static HashMap<String, IoSession> userSessionsMap = new HashMap<String, IoSession>();
    public static HashMap<String, IoSession> deviceSessionsMap = new HashMap<String, IoSession>();

    public TcpDeviceServerBad(int port) throws IOException {
        NioSocketAcceptor acceptor = new NioSocketAcceptor();
        
        DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
        chain.addLast( "logger", new LoggingFilter() );

        chain.addLast("code",new ProtocolCodecFilter(new ImageProtocolCodecFactory("utf-8")));
        
        acceptor.setHandler(this);

        acceptor.bind(new InetSocketAddress(port));

        System.out.println("Server started..." + port);
    }
    
    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {

    	System.out.println("messageReceived..." + message);
    	
//    	PushMsgToSingleDevice pushMsgToSingleDevice = new PushMsgToSingleDevice();
//    	pushMsgToSingleDevice.pushDataToDevice((JSONObject)message);
    }
    
    @Override
	public void messageSent(IoSession session, Object message) throws Exception {
//		 System.out.println("Message Sent..." + message);
	}

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        System.out.println("Session closed...");

//        userSessionsMap.remove("1234");
    }
    
    
    @Override
    public void sessionCreated(IoSession session) throws Exception {
        System.out.println("Session created...");
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
//        System.out.println("Session idle...");
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        System.out.println("Session Opened...");
        
//        userSessionsMap.put("1234", session);
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        cause.printStackTrace();
        session.closeNow();
//        userSessionsMap.remove("1234");
//        logger.info("exceptionCaught...");
    }
  
}
