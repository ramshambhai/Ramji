package in.vnl.api.common.livescreens;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;

import javax.websocket.*;
import javax.websocket.server.*;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

@ServerEndpoint("/devicestatus")
public class DeviceStatusServer
{
	static Logger statusLogger = Logger.getLogger("status");
	//static Session sessionObj = null;
	static Vector<Session> sessionObjs = new Vector<Session>();
	@OnOpen
	public void onOpen(Session session,EndpointConfig conf)
	{
		try
		{
			session.getBasicRemote().sendText("Connection Established");
			updateSessionsList(session);
		}
		catch(Exception e)
		{
			//statusLogger.debug(e.getMessage());
		}
	}
	
	@OnMessage
	public void onMessage(Session session,String clientMsg)
	{
		//statusLogger.debug(clientMsg);
		try
		{
			session.getBasicRemote().sendText(clientMsg);
		}
		catch(Exception e)
		{
			statusLogger.debug(e.getMessage());
		}
	}
	
	@OnError
	public void onError(Session session,Throwable error)
	{
		statusLogger.debug("error occured");
	}
	
	public static void updateSessionsList(Session ses)
	{
		sessionObjs.add(ses);
	}
	
	public static void removeClosedSessions(Session ses)
	{
		sessionObjs.remove(ses);
	}
	
	public  void sendText(String msg)
	{
		for(Session ses:sessionObjs)
		{
			try
			{
				ses.getBasicRemote().sendText(msg);
			}
			catch(Exception E)
			{
				statusLogger.debug(E.getMessage());
			}			
		}
	}
	

	public  void sendText(ArrayList<String> packet)
	{	
		String pack = convertDataToJson(packet);
		for(Session ses:sessionObjs)
		{
			try
			{
				ses.getBasicRemote().sendText(pack);
			}
			catch(Exception E)
			{
				statusLogger.debug(E.getMessage());
			}			
		}
	}
	
	public String convertDataToJson(ArrayList<String> packet)
	{
				
		JSONArray ja = new JSONArray();
		try
		{
						
			
			for(String data:packet)
			{
				ja.put(data);
			}			
		}
		catch(Exception E)
		{
			statusLogger.debug("Exeption while authenticating the user "+E.getMessage());
		}
		return ja.toString();
	}
}
