package com.smarthome.server;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

import com.smarthome.message.ImageMessage;

public class ImageServer {

    private static final int DEVICE_PORT = 8099;
    
  public void service(){
  	  ServerSocket serverSocket = null;
	  Socket socket = null;
		try {
			serverSocket = new ServerSocket(DEVICE_PORT);
			 System.out.println("DEVICE_PORT: " + DEVICE_PORT); 
			 while(true) {
				  socket = serverSocket.accept();  
				  new Thread(new SendImageTask(socket)).start();
			 }
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			 try {
			    if(socket != null){
			    	socket.close();  
			    }
			    
			    if(serverSocket != null){
			    	serverSocket.close();  
			    }
			    
		    } catch (IOException e) {
				e.printStackTrace();
			} 
		}
  }
  
}
