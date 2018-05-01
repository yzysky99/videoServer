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

public class SendImageTask implements Runnable {
	private Socket socket;  
	InputStream inputStream = null;
	FileOutputStream fileOutputStream = null;
	OutputStream outputStream = null;
	  
	public SendImageTask(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
		  inputStream = socket.getInputStream();  
		  fileOutputStream = new FileOutputStream("server.jpg");  
          byte[] buf = new byte[1024];  
          int len = 0;  
          while ((len = inputStream.read(buf)) != -1)  
          {  
        	  fileOutputStream.write(buf,0,len);  
          }
          
          sendImage(fileOutputStream);
//		  outputStream = socket.getOutputStream();  
//		  outputStream.write("ok".getBytes());  
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			 try {
			    if(inputStream != null){
			    	inputStream.close();
			    }
			    if(fileOutputStream != null){
			    	fileOutputStream.close();  
			    }
			    if(socket != null){
			    	socket.close();  
			    }
			    
			    if(outputStream != null){
			    	outputStream.close();  
			    }
		    } catch (IOException e) {
				e.printStackTrace();
			} 
		}
	}

	  private void sendImage(FileOutputStream fos) throws IOException {
		  IoSession session = TcpDeviceServerBad.userSessionsMap.get("1234");
		  if(session == null) {
			  System.out.println("session in null");
			  return;
		  }
		  
		  CharsetEncoder encoder = Charset.forName("utf-8").newEncoder();
		  System.out.println("client connect:" + session);
		  ImageMessage imageMessage = new ImageMessage();

		  FileInputStream fileInputStream = new FileInputStream("server.jpg");
		  FileChannel fileChannel = fileInputStream.getChannel();
		  String imageNameStr = "imageName";

		  imageMessage.setImageName(imageNameStr);
		  imageMessage.setImageNameLength(imageMessage.getImageName().length());
		  
		  ByteBuffer byteBuffer = ByteBuffer.allocate((int) fileChannel.size());
		  byteBuffer.clear();
		  fileChannel.read(byteBuffer);
		  
		  imageMessage.setImageLength(fileChannel.size());
		  imageMessage.setImage(byteBuffer.array());
		  imageMessage.setAllLength((int)(4+4+8+ imageMessage.getImageLength()+ imageMessage.getImageNameLength()));
		  System.out.println("mes.getImagelongth():"+ imageMessage.getImageLength());
		  
		  IoBuffer ioBuffer = IoBuffer.allocate((int) (4+4+8+ imageMessage.getImageLength()+ imageMessage.getImageNameLength()));
		  ioBuffer.putInt(imageMessage.getAllLength());
		  ioBuffer.putInt(imageMessage.getImageNameLength());
		  ioBuffer.putString(imageMessage.getImageName(),encoder );
		  ioBuffer.putLong(imageMessage.getImageLength());
		  ioBuffer.put(byteBuffer);
		  ioBuffer.put(imageMessage.getImage());
		  System.out.println("send remaining:"+ ioBuffer.limit());
		  ioBuffer.flip();
		  System.out.println("write data!");
		  session.write(ioBuffer);
	  	}
	  
}
