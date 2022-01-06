package in.vnl.api.common.livescreens;

import in.vnl.msgapp.Common;
import in.vnl.msgapp.Operations;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;

import javax.websocket.*;
import javax.websocket.server.*;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@ServerEndpoint("/scanandtrackmodeevent")
public class ScanTrackModeServer
{
	static Logger fileLogger = Logger.getLogger("file");
	//static Session sessionObj = null;
	static Vector<Session> sessionObjs = new Vector<Session>();
	@OnOpen
	public void onOpen(Session session,EndpointConfig conf)
	{
		fileLogger.info("Inside Function : onOpen");	
		try
		{
			session.getBasicRemote().sendText("Connection Established");
			updateSessionsList(session);
		}
		catch(Exception e)
		{
			fileLogger.error(e.getMessage());
			//fileLogger.debug(e.getMessage());
		}
		fileLogger.info("Exit Function : onOpen");	
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
			fileLogger.error(e.getMessage());
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
		insertEvents(msg);
		for(Session ses:sessionObjs)
		{
			try
			{
				ses.getBasicRemote().sendText(msg);
			}
			catch(Exception E)
			{
				fileLogger.error(E.getMessage());
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
	
	public boolean insertEvents(String msg) 
	{
		try {
			String query="";
			String[] msgArr=msg.split("&");
			if(msgArr[0].equalsIgnoreCase("scan")){
				if(msgArr[1].equalsIgnoreCase("idle")){
					query="update running_mode set mode_status='Idle' where mode_type='scan'";
				}else{
					query="update running_mode set mode_status='Active',applied_antenna='"+msgArr[2]+"' where mode_type='scan'";
				}
			}else{
				if(msgArr[1].equalsIgnoreCase("idle")){
					query="update running_mode set mode_status='Idle' where mode_type='track'";
					//query="update running_mode set mode_status='"+msgArr[1]+"' where mode_type='track'";
				}else{
					if(msgArr.length==2){
						query="update running_mode set mode_status='Active',applied_antenna=null where mode_type='track'";
					}else{
						query="update running_mode set mode_status='Active',applied_antenna='"+msgArr[2]+"' where mode_type='track'";
					}
				}
			}
			new Common().executeDLOperation(query);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
}

