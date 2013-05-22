package user4574.texttransport;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class Start extends Activity {
	
	public static MqttClient client;
	private SmsManager sms = SmsManager.getDefault();
	
	private Button conn;
	private EditText ent;
	private EditText port;
	private RadioGroup rg;
	private String uri = "";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start);
		
		conn = (Button) findViewById(R.id.connectbutton);
		conn.setEnabled(false);
		conn.setOnClickListener(new ConnectListener());
		
		ent = (EditText) findViewById(R.id.uri);
		ent.addTextChangedListener(new URIWatcher());
		
		port = (EditText) findViewById(R.id.port);
		
		rg = (RadioGroup) findViewById(R.id.protocol);
	}
	
	class ConnectListener implements OnClickListener {
		public void onClick(View arg0) {
			if (client != null && client.isConnected()) {
				try {
					client.disconnect();
					ent.setEnabled(true);
					conn.setText(R.string.connect);
				} catch (MqttException e) {
					e.printStackTrace();
				}
			} else if (!uri.equals("")) {
				String server = (rg.getCheckedRadioButtonId() == R.id.ssl) ? "ssl" : "tcp";
				server += "://" + uri;
				if(!port.getText().toString().equals(""))
					server += ":" + port.getText().toString();
				try {
					client = new MqttClient(server, MqttClient.generateClientId(), new AndroidPersistence());
					client.setCallback(new MqttMsgCallback(sms));
					client.connect();
					client.subscribe("/texttransport/+/send");
					ent.setEnabled(false);
					conn.setText(R.string.disconnect);
				} catch (MqttException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	class URIWatcher implements TextWatcher {
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
		public void onTextChanged(CharSequence s, int start, int before, int count) {}
		public void afterTextChanged(Editable s) {
			uri = s.toString();
			if (uri.equals(""))
				conn.setEnabled(false);
			else
				conn.setEnabled(true);
		}
	}
}