package com.smarthome.device;

import com.smarthome.message.AppMessage;
import com.smarthome.message.DeviceMessage;

public class DeviceCmd {

	String message;
	public DeviceCmd(String message) {
		this.message = message;
	}

	public void appMsg() {
		DeviceMessage deviceMsg = new DeviceMessage(message);
//		int cmd = deviceMsg.getCmd();
//		switch (cmd) {
//		case 1://login
////			userLogin();
//			break;
//		case 2://logout
//				
//			break;
//		case 3://syncDevice
//			
//			break;
//		case 4://sendCmd
//			
//			break;
//		case 5://bind
//			
//			break;
//		default:
//			break;
//		}
	}

}
