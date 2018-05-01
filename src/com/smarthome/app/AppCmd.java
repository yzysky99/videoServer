package com.smarthome.app;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.mina.core.session.IoSession;

//import sun.security.jgss.LoginConfigImpl;
import net.sf.json.JSONObject;

import com.mysql.jdbc.log.Log;
import com.smarthome.dao.UserDao;
import com.smarthome.dao.UserDaoImpl;
import com.smarthome.domain.User;
import com.smarthome.message.AppMessage;

public class AppCmd {

	private String appMsg;
	private IoSession session;	
	private Set<String> names = Collections
		.synchronizedSet(new HashSet<String>());
	
	private Set<IoSession> sessions = Collections
		.synchronizedSet(new HashSet<IoSession>());
	  
	public AppCmd(IoSession session, String appMsg, Set<String> names, Set<IoSession> sessions) {
		this.appMsg = appMsg;
		this.session = session;
		this.names = names;
		this.sessions = sessions;
	}

	public void appMsg() {
		AppMessage appMessage = new AppMessage(appMsg);
		int cmd = appMessage.getCmd();
		System.out.print("cmd:" + cmd);
		switch (cmd) {
		case 0://register
			register(appMsg);
			break;
		case 1://login
			userLogin(appMsg);
			break;
		case 2://logout
				
			break;
		case 3://syncDevice
			
			break;
		case 4://sendCmd
			
			break;
		case 5://bind
			
			break;
			
			
		case 100://register
			register(appMsg);
			break;
					
		case 101://unregist
			
			break;
	
		case 102://login
			userLogin(appMsg);
			break;
					
		case 103://logout
			
			break;
			
		case 104://addDevice
			
			break;
					
		case 105://deleteDevice
			
			break;
			
		case 106://sendCmd
			
			break;
					
		case 107://getData
			
			break;
			
		default:
			break;
		}
	}

	private void register(String appMsg) {
		String name;
		String password;
		String mobilePhone;
		String email;
		String address;
		
		System.out.print(appMsg.toString());
		
		JSONObject json = JSONObject.fromObject(appMsg);
        name = (String) json.get("name");
        password = (String) json.get("password");
        mobilePhone = (String) json.get("mobilePhone");
        email = (String) json.get("email");
        address = (String) json.get("address");
        
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        user.setMobilePhone(mobilePhone);
        user.setEmail(email);
        user.setAddress(address);
        
        UserDaoImpl userDaoImpl = new UserDaoImpl();
    	JSONObject regMsg = new JSONObject();
		regMsg.put("Type", "register");
	
		try {
			if(userDaoImpl.findUserByName(user.getName())!=null){
				regMsg.put("Content", "failed");
			} else {
				regMsg.put("Content", "ok");
			}
			userDaoImpl.addUser(user);
			session.write(regMsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void userLogin(String appMsg) {
		String password;
		String name;
		
		System.out.print(appMsg.toString());
		
		JSONObject regMsg = new JSONObject();
		regMsg.put("Type", "login");
		
	    String sessionUser = (String) session.getAttribute("user");
		 if (sessionUser != null) {
			 regMsg.put("Content", "failed LOGIN ERROR user " + sessionUser
	                 + " already logged in.");
	         session.write(regMsg);
	         return;
	     }
		 System.out.print("11111");
		 if (names.contains(sessionUser)) {
			 regMsg.put("Content", "failed LOGIN ERROR user " + sessionUser
	                 + " already logged in.");
	         return;
	     }
		 System.out.print("22222");
		JSONObject json = JSONObject.fromObject(appMsg);
        name = (String) json.get("name");
        password = (String) json.get("password");
        
        if (name == null || name.isEmpty() || password == null || password.isEmpty()) {
        	regMsg.put("Content", "failed name or password is empty ");
            session.write(regMsg);
		} 
        System.out.print("33333");
        UserDaoImpl userDaoImpl = new UserDaoImpl();
		try {
			User user = userDaoImpl.findUserByName(name);
			if(user !=null && password.equals(user.getPassword())){
				//Login ok
				 System.out.print("44444");
				regMsg.put("Content", "ok");
	            session.write(regMsg);
	            System.out.print("55555");
	        	names.add(name);
	            sessions.add(session);
	            session.setAttribute("user", name);
			} else {
				 System.out.print("66666");
					regMsg.put("Content", "failed");
		            session.write(regMsg);
		            System.out.print("777777");
			}
		} catch (Exception e) {
			System.out.print(e.getMessage());
			e.printStackTrace();
		}
		 System.out.print("000000");
	}
}
