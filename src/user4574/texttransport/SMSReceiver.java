package user4574.texttransport;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttTopic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SMSReceiver extends BroadcastReceiver {
	public void onReceive(Context context, Intent intent) {
		Bundle pdusBundle = intent.getExtras();
		Object[] pdus = (Object[]) pdusBundle.get("pdus");
		SmsMessage message = SmsMessage.createFromPdu((byte[]) pdus[0]);
		try {
			if (Start.client != null) {
				MqttTopic t = Start.client.getTopic("/texttransport/" + message.getOriginatingAddress().substring(1) + "/received");
				t.publish(message.getMessageBody().getBytes(), 2, false);
			}
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
}