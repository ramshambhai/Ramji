package in.vnl.msgapp;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Timer;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;



//public class NibAlarmClient implements ServletContextListener,IOCallback
public class NibAlarmClient implements IOCallback
{
	static Logger fileLogger = Logger.getLogger("file");
	
	public static SocketIO socket;
	
	public NibAlarmClient()
	{
		try
		{
			//socket = new SocketIO();
			//socket.connect("http://192.168.15.225:8080", this);
			
			
		}
		catch(Exception E)
		{
			fileLogger.debug(E.getMessage());
		}		
	}
	/*public void restartSocket(String ip);
	{
		socket.disconnect();		
		socket.connect(url, callback);
	}*/
	/*@Override
	public void contextDestroyed(ServletContextEvent event) 
	{
		fileLogger.debug("Application is undeploying");
	}
	@Override
	public void contextInitialized(ServletContextEvent event) 
	{
				
		fileLogger.debug("DBService Timer task started");		
	}*/
	
	
	
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
		fileLogger.debug("Retrying.......");
		//new NibAlarmClient();
	}
	@Override
	public void onDisconnect() {
		fileLogger.debug("Connection terminated.");
		//new NibAlarmClient();
	}
	@Override
	public void onConnect() {
		fileLogger.debug("Connection established");
	}
	@Override
	public void on(String event, IOAcknowledge ack, Object... args) {
		fileLogger.debug("Server triggered event '" + event + "'");
		ArrayList<String> pack = formtaPacket(args[0].toString());
		new SocketServer().sendText(pack);
		insertCDRData(pack);
		fileLogger.debug("Server triggered event '" + args[0] + "'");
	}
	public ArrayList<String> formtaPacket(String dataPacket)
	{
		String aa = dataPacket;
		//String aa = "'{\"value\":\"Normal_LU    IMSI:404045000053267 IMEI:867466028723794 MSISDN:                 PTMSI:0x00000000 TMSI:0x00000000 OL:0xfffe NL:0x0006 CID:0x0001 Ta:0   RxL:-110 TStmp:310170-18:22:26 FTyp:FAIL_NO_SUITABLE_CELLS_IN_LOCATION_AREA\",\"channel\":\"stdout\"}'";
		
		//aa = aa.substring(1,aa.length()-1);
		//fileLogger.debug("LENGTH : "+aa.length());
		fileLogger.debug("data formated"+aa);
		ArrayList<String> finalPacket = new ArrayList<String>();
		ArrayList<String> returnPacket = new ArrayList<String>();
		try
		{	
			
			JSONObject dd = new JSONObject(aa);
			fileLogger.debug(dd.toString());
			String [] bb = dd.get("value").toString().split(" ");
			ArrayList<String> packet = new ArrayList<String>();
			Collections.addAll(packet, bb);
			
			for(int i=0;i< packet.size();i++)
			{
				
				if(!packet.get(i).trim().equalsIgnoreCase(""))
				{
					
					finalPacket.add(packet.get(i));
				}
			}
			for(int i=0;i< finalPacket.size();i++)
			{
				
				if(finalPacket.get(i).contains(":"))
				{
					String[] spilPack = finalPacket.get(i).split(":");
					if(spilPack.length == 1)
						returnPacket.add("null");
					else
						returnPacket.add(finalPacket.get(i).split(":")[1]);
				}
				else
				{
					returnPacket.add(finalPacket.get(i));
				}			
			}
			
			fileLogger.debug(returnPacket.toString());			
		}
		catch(Exception E)
		{
			fileLogger.debug(E.getMessage());
		}
		return returnPacket;
	}
	
	public String insertCDRData(ArrayList<String> packet)
	{
		if(packet.size()>0)
		{
			StringBuilder packetString = new StringBuilder();		
			
			for(int i=0;i<packet.size();i++)
			{
				if(i==0)
					packetString.append("'"+packet.get(i)+"'");
				else
					packetString.append(",'"+packet.get(i)+"'");			
			}
			Common co = new Common();				
			String query = "INSERT INTO cdrlogs(packet_type, imsi, imei, msisdn,ptmsi, tmsi, ol, nl, cid, ta, rxl,tstmp, ftyp)"
					+ "values("+packetString.toString()+")";		
			co.executeDLOperation(query);
		}
		return "";
	}
	
	
}
