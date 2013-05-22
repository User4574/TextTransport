package user4574.texttransport;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;

import android.telephony.SmsManager;

public class MqttMsgCallback implements MqttCallback {
	
	SmsManager sms;
	
	public MqttMsgCallback (SmsManager sms) {
		this.sms = sms;
	}
	
	public void connectionLost(Throwable exception) {}
	public void deliveryComplete(MqttDeliveryToken token) {}
	public void messageArrived(MqttTopic topic, MqttMessage message) throws Exception {
		String[] topicarr = topic.getName().split("/");
		sms.sendTextMessage(topicarr[2], null, message.toString(), null, null);
	}

}
