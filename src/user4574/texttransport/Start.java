package user4574.texttransport;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.SmsManager;

public class Start extends Activity implements MqttCallback {
	
	public static MqttClient client;
	private SmsManager sms = SmsManager.getDefault();
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		try {
			client = new MqttClient("tcp://192.168.0.222:1883", MqttClient.generateClientId(), new AndroidPersistence());
			client.setCallback(this);
			client.connect();
			client.subscribe("/texttransport/+/send");
		} catch (MqttException e) {
			e.printStackTrace();
		}
		
		setContentView(R.layout.start);
	}
	
	public void connectionLost(Throwable exception) {}
	public void deliveryComplete(MqttDeliveryToken token) {}
	public void messageArrived(MqttTopic topic, MqttMessage message) throws Exception {
		String[] topicarr = topic.getName().split("/");
		sms.sendTextMessage(topicarr[2], null, message.toString(), null, null);
	}
	
}