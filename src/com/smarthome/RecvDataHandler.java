package com.smarthome;

import java.util.Date;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import java.util.logging.Logger;

public class RecvDataHandler extends IoHandlerAdapter {
	public static Logger logger = Logger.getLogger(RecvDataHandler.class + "");

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		String str = message.toString();
		System.out.println(str);
        if( str.trim().equalsIgnoreCase("quit") ) {
            session.close();
            return;
        }
        
        Date date = new Date();
        session.write( date.toString() );
        System.out.println("Message written...");
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		logger.info("messageSent...");
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		logger.info("sessionClosed...");
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		logger.info("sessionCreated...");
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		logger.info("sessionOpened...");
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		logger.info("sessionIdle...");
		  System.out.println( "IDLE " + session.getIdleCount( status ));
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		logger.info("exceptionCaught...");
		cause.printStackTrace();
	}

}
