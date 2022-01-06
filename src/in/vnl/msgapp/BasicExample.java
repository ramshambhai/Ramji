package in.vnl.msgapp;


import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class BasicExample implements IOCallback {
	static Logger fileLogger = Logger.getLogger("file");
	private SocketIO socket;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			//new BasicExample();
			SocketIO socket = new SocketIO("http://192.168.15.225:8080/");
			socket.connect(new IOCallback() {
				@Override
				public void onMessage(JSONObject json, IOAcknowledge ack) {
					try {
						fileLogger.debug("Server said:" + json.toString(2));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onMessage(String data, IOAcknowledge ack) {
					fileLogger.error("Server said: " + data);
				}

				@Override
				public void onError(SocketIOException socketIOException) {
					fileLogger.error("an Error occured");
					socketIOException.printStackTrace();
				}

				@Override
				public void onDisconnect() {
					fileLogger.debug("Connection terminated.");
				}

				@Override
				public void onConnect() {
					fileLogger.debug("Connection established");
				}

				@Override
				public void on(String event, IOAcknowledge ack, Object... args) {
					fileLogger.debug("Server triggered event '" + event + "'");
				}
			});
			
			// This line is cached until the connection is establisched.
			socket.send("Hello Server!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public BasicExample() throws Exception {
		socket = new SocketIO();
		socket.connect("http://192.168.15.225:8080", this);

		// Sends a string to the server.
		socket.send("Hello Server");

		// Sends a JSON object to the server.
		socket.send(new JSONObject().put("key", "value").put("key2",
				"another value"));

		// Emits an event to the server.
		socket.emit("event", "argument1", "argument2", 13.37);
	}

	@Override
	public void onMessage(JSONObject json, IOAcknowledge ack) {
		try {
			fileLogger.debug("Server said:" + json.toString(2));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onMessage(String data, IOAcknowledge ack) {
		fileLogger.debug("Server said: " + data);
	}

	@Override
	public void onError(SocketIOException socketIOException) {
		fileLogger.error("an Error occured");
		socketIOException.printStackTrace();
	}

	@Override
	public void onDisconnect() {
		fileLogger.debug("Connection terminated.");
	}

	@Override
	public void onConnect() {
		fileLogger.debug("Connection established");
	}

	@Override
	public void on(String event, IOAcknowledge ack, Object... args) {
		fileLogger.debug("Server triggered event '" + event + "'");
		
		fileLogger.debug("Server triggered event '" + args[0] + "'");
	}
}