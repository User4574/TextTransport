package user4574.texttransport;

import java.util.ArrayList;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
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
	private MqttConnectOptions opts;
	private SmsManager sms = SmsManager.getDefault();
	
	private ArrayList<View> inputs;
	
	private Button conn;
	private EditText ent;
	private EditText port;
	private RadioGroup rg;
	private String uri = "";
	private EditText un;
	private EditText pw;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start);
		
		inputs = new ArrayList<View>();
		
		conn = (Button) findViewById(R.id.connectbutton);
		conn.setEnabled(false);
		conn.setOnClickListener(new ConnectListener());
		
		ent = (EditText) findViewById(R.id.uri);
		ent.addTextChangedListener(new URIWatcher());
		
		port = (EditText) findViewById(R.id.port);
		
		rg = (RadioGroup) findViewById(R.id.protocol);

		un = (EditText) findViewById(R.id.username);
		pw = (EditText) findViewById(R.id.password);
		
		inputs.add(findViewById(R.id.uri));
		inputs.add(findViewById(R.id.port));
		inputs.add(findViewById(R.id.tcp));
		inputs.add(findViewById(R.id.ssl));
		inputs.add(findViewById(R.id.username));
		inputs.add(findViewById(R.id.password));
	}
	
	private void setInputsEnabled(boolean enabled) {
		for (View v : inputs)
			v.setEnabled(enabled);
	}
	
	protected void onResume() {
		super.onResume();
		if (client == null || !client.isConnected()) {
			setInputsEnabled(true);
			ent.setText("");
		}
	}
	
	class ConnectListener implements OnClickListener {
		public void onClick(View arg0) {
			if (client != null && client.isConnected()) {
				try {
					client.disconnect();
					setInputsEnabled(true);
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
					if (!un.getText().toString().equals("")) {
						opts = new MqttConnectOptions();
						opts.setUserName(un.getText().toString());
						String pass = pw.getText().toString();
						if (!pass.equals("")) opts.setPassword(pass.toCharArray());
						client.connect(opts);
					} else
						client.connect();
					client.subscribe("/texttransport/+/send");
					setInputsEnabled(false);
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