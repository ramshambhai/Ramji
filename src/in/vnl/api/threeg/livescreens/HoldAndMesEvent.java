package in.vnl.api.threeg.livescreens;

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

@ServerEndpoint("/mesevent")
public class HoldAndMesEvent
{
	static Logger fileLogger = Logger.getLogger("file");
	//static Session sessionObj = null;
	static Vector<Session> sessionObjs = new Vector<Session>();
	@OnOpen
	public void onOpen(Session session,EndpointConfig conf)
	{
		try
		{
			session.getBasicRemote().sendText("Connection Established Session Id-"+session.getId()+",Total Session-"+sessionObjs.size());
			
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

	 @OnClose
	 public void closedConnection(Session session) 
	 { 
	  //Stringqueue.remove(session);
	  removeClosedSessions(session);
	  fileLogger.debug("session closed: "+session.getId());
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
		}
	fileLogger.info("Exit Function : sendText");
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
				fileLogger.error("Exception While Sending the hold meas event "+E.getMessage());
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
