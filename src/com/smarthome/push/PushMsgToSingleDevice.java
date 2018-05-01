package com.smarthome.push;

import net.sf.json.JSONObject;

import com.baidu.yun.core.log.YunLogEvent;
import com.baidu.yun.core.log.YunLogHandler;
import com.baidu.yun.push.auth.PushKeyPair;
import com.baidu.yun.push.client.BaiduPushClient;
import com.baidu.yun.push.constants.BaiduPushConstants;
import com.baidu.yun.push.exception.PushClientException;
import com.baidu.yun.push.exception.PushServerException;
import com.baidu.yun.push.model.PushMsgToSingleDeviceRequest;
import com.baidu.yun.push.model.PushMsgToSingleDeviceResponse;

public class PushMsgToSingleDevice {
	
	String apiKey = "zreuRNG8QtmiMZp16D7fEZal";
	String secretKey = "S9YtYdL3pq79bCL2L1rmtFnNSF3tojMM";
	PushKeyPair pair = new PushKeyPair(apiKey, secretKey);
	
	BaiduPushClient pushClient;
	
	public PushMsgToSingleDevice() {
		 pushClient = new BaiduPushClient(pair,
					BaiduPushConstants.CHANNEL_REST_URL);
		
		pushClient.setChannelLogHandler(new YunLogHandler() {
			@Override
			public void onHandle(YunLogEvent event) {
				System.out.println(event.getMessage());
			}
		});
	}
	
	public boolean pushDataToDevice(JSONObject data) {
		try {
			// 4. specify request arguments
			//创建 Android的通知
			JSONObject notification = new JSONObject();
			notification.put("title", "info");
			notification.put("description","device info");
			notification.put("notification_builder_id", 0);
			notification.put("notification_basic_style", 4);
			notification.put("open_type", 1);
			notification.put("url", "http://push.baidu.com");
			JSONObject jsonCustormCont = new JSONObject();
			jsonCustormCont.put("data", data.toString()); //自定义内容，key-value
			notification.put("custom_content", jsonCustormCont);
		
			PushMsgToSingleDeviceRequest request = new PushMsgToSingleDeviceRequest()
					.addChannelId("3483085307751252425")
					.addMsgExpires(new Integer(3600)). // message有效时间
					addMessageType(0).// 1：通知,0:透传消息. 默认为0 注：IOS只有通知.
					addMessage(notification.toString()).
					addDeviceType(3);// deviceType => 3:android, 4:ios
			// 5. http request
			PushMsgToSingleDeviceResponse response = pushClient
					.pushMsgToSingleDevice(request);
			// Http请求结果解析打印
			System.out.println("msgId: " + response.getMsgId() + ",sendTime: "
					+ response.getSendTime());
		} catch (PushClientException e) {
				e.printStackTrace();
		} catch (PushServerException e) {
				System.out.println(String.format(
						"requestId: %d, errorCode: %d, errorMessage: %s",
						e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
		}
		return true;
		
	}

	
}
