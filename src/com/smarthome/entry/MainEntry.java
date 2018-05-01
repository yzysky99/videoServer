package com.smarthome.entry;

import java.io.IOException;
import java.util.logging.Logger;

import com.smarthome.server.TcpAppServer;
import com.smarthome.server.TcpDeviceServer;


public class MainEntry {
    private static Logger logger = Logger.getLogger(MainEntry.class+"");
    private static final int APP_PORT = 8000;
    private static final int DEVICE_PORT = 8100;
    
    public static void main(String[] args) {
    	
        logger.info("server start error ");
    	 try {
    		 TcpAppServer tcpAppServer = new TcpAppServer(APP_PORT);
//    		 TcpDeviceServer tcpDeviceServer = new TcpDeviceServer(DEVICE_PORT);
    		 
//    		IoSession ioSession = tcpDeviceServer.userSessionsMap.get("1234");
//    		ImageServer imageServer = new ImageServer();
//    		imageServer.service();
    	
		} catch (IOException e) {
			// TODO Auto-generated catch block
	        logger.info("server start error "+ e.getMessage().toString());
			e.printStackTrace();
		}
    }
           
}