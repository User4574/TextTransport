package user4574.texttransport;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;

import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttPersistable;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

public class AndroidPersistence implements MqttClientPersistence {
	
	private HashMap<String, MqttPersistable> store;
	
	public AndroidPersistence() {
		store = new HashMap<String, MqttPersistable>();
	}

	public void clear() throws MqttPersistenceException {
		store.clear();
	}

	public void close() throws MqttPersistenceException {
		store = null;
	}

	public boolean containsKey(String arg0) throws MqttPersistenceException {
		return store.containsKey(arg0);
	}

	public MqttPersistable get(String arg0) throws MqttPersistenceException {
		return store.get(arg0);
	}

	public Enumeration<String> keys() throws MqttPersistenceException {
		return new Vector<String>(store.keySet()).elements();
	}

	public void open(String arg0, String arg1) throws MqttPersistenceException {
		store = new HashMap<String, MqttPersistable>();
	}

	public void put(String arg0, MqttPersistable arg1) throws MqttPersistenceException {
		store.put(arg0, arg1);
	}

	public void remove(String arg0) throws MqttPersistenceException {
		store.remove(arg0);
	}
}
