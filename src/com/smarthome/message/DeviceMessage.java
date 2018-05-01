package com.smarthome.message;

import net.sf.json.JSONObject;
public class DeviceMessage {
	
	private String msgType; //app or device
	private String cmd; //
	
	private String userId; //用户ID，唯一
	private String deviceId; //设备ID，唯一，MAC地址
	private String deviceType; //设备类型，灯、插座、空调等
	private String message;
	
	public DeviceMessage(String message) {
		this.message = message;
	}
	
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	public String getCmd() {
		return cmd;
	}
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	
}
