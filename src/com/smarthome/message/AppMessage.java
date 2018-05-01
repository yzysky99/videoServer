package com.smarthome.message;

import net.sf.json.JSONObject;
public class AppMessage {
	private int cmd;
	private String name;
	private String password;
	private String mobilePhone;
	private String email;
	private String address;
	private String appMessage;
	
	public AppMessage(String appMessage) {
		this.appMessage = appMessage;
	}

	public int getCmd() {
		JSONObject json = JSONObject.fromObject(appMessage.toString());
        cmd = json.getInt("cmd");
        return cmd;
	}

	public void setCmd(int cmd) {
		this.cmd = cmd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
}
