package in.vnl.api.twog.livescreens;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;
import org.apache.log4j.Logger;

import javax.websocket.*;
import javax.websocket.server.*;

import org.json.JSONArray;
import org.json.JSONObject;

@ServerEndpoint("/triggercuetracker")
public class TriggerCueServer
{
	static Logger fileLogger = Logger.getLogger("file");
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
			//fileLogger.debug(e.getMessage());
		}
	}
	
	@OnMessage
	public void onMessage(Session session,String clientMsg)
	{
		fileLogger.info("Inside Function : onMessage");		
		//fileLogger.debug(clientMsg);
		try
		{
			session.getBasicRemote().sendText(clientMsg);
		}
		catch(Exception e)
		{
			fileLogger.debug(e.getMessage());
		}
		fileLogger.info("Exit Function : onMessage");		
	}
	
	@OnError
	public void onError(Session session,Throwable error)
	{
		fileLogger.error("error occured");
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
		fileLogger.info("Inside Function : sendText");		
		for(Session ses:sessionObjs)
		{
			try
			{
				ses.getBasicRemote().sendText(msg);
			}
			catch(Exception E)
			{
				fileLogger.debug(E.getMessage());
			}			
		}	fileLogger.info("Exit Function : sendText");	
	}
	
	public  void sendText(ArrayList<String> packet)
	{	
		fileLogger.info("Inside Function : sendText");	
		String pack = convertDataToJson(packet);
		for(Session ses:sessionObjs)
		{
			try
			{
				ses.getBasicRemote().sendText(pack);
			}
			catch(Exception E)
			{
				fileLogger.debug(E.getMessage());
			}			
		}
		fileLogger.info("Exit Function : sendText");	
	}
	
	public String convertDataToJson(ArrayList<String> packet)
	{
		fileLogger.info("Inside Function : convertDataToJson");			
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
			fileLogger.error("Exeption while authenticating the user "+E.getMessage());
		}
		fileLogger.info("Exit Function : convertDataToJson");		
		return ja.toString();
	}
}

