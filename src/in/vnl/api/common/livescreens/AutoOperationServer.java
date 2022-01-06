package in.vnl.api.common.livescreens;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import javax.websocket.EndpointConfig;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;

import in.vnl.msgapp.Common;
import in.vnl.msgapp.Operations;

@ServerEndpoint("/autopr")
public class AutoOperationServer {
	static Logger fileLogger = Logger.getLogger("file");
	// static Session sessionObj = null;
	static Vector<Session> sessionObjs = new Vector<Session>();

	@OnOpen
	public void onOpen(Session session, EndpointConfig conf) {
		try {
			session.getBasicRemote().sendText("Connection Established");
			updateSessionsList(session);
		} catch (Exception e) {
			//fileLogger.debug(e.getMessage());
		}
	}

	@OnMessage
	public void onMessage(Session session, String clientMsg) {
		fileLogger.info("Inside Function : onMessage");	
		// fileLogger.debug(clientMsg);
		try {
			session.getBasicRemote().sendText(clientMsg);
		} catch (Exception e) {
			fileLogger.error(e.getMessage());
		}
		fileLogger.info("Exit Function : onMessage");	
	}

	@OnError
	public void onError(Session session, Throwable error) {
		fileLogger.error("error occured");
	}

	public static void updateSessionsList(Session ses) {
		try {
			sessionObjs.add(ses);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void removeClosedSessions(Session ses) {
		try {
			sessionObjs.remove(ses);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendText(String msg) {
		fileLogger.info("Inside Function : sendText");	
		int eventId=insertEvents(msg);
		String msg1="";
		try{
			String query  = "select event_msg,date_trunc('second',logtime + '05:30:00'::interval) logtime1 from currentrunningoperationevnets where id="+eventId+"";
			JSONArray js = new Operations().getJson(query);
			fileLogger.debug("Query:"+ query);
			if(js.length()>0){
				msg1=js.getJSONObject(0).toString();
		}
		} catch(Exception E) {
			fileLogger.error(E.getMessage());
			msg1="";
		}
		
		if (msg1.length()>0)
		{
		try{
			Vector<Session> sessionObjs1 = new Vector<Session>(sessionObjs);
			//sessionObjs1=sessionObjs;
			for(Session ses : sessionObjs1) {
				try{
				
					ses.getBasicRemote().sendText(msg1);
					
					} catch(Exception E) {
						try
						{
						sessionObjs.remove(ses);
						ses.close();
						ses=null;
						}
						catch (Exception e)
						{
							fileLogger.error("Error in close web socket session"+ E.getMessage());
						}
						fileLogger.error(E.getMessage());
					}
				}
			} catch(Exception E) {
				fileLogger.error(E.getMessage());
				E.printStackTrace();
			}
		}
		fileLogger.info("Exit Function : sendText");	
	}

	public void sendText(ArrayList<String> packet) {
		fileLogger.info("Inside Function : sendText");	
		String pack = convertDataToJson(packet);
		for (Session ses : sessionObjs) {
			try {
				ses.getBasicRemote().sendText(pack);
			} catch (Exception E) {
				fileLogger.error(E.getMessage());
			}
		}
		fileLogger.info("Exit Function : sendText");	
	}

	public String convertDataToJson(ArrayList<String> packet) {
		fileLogger.info("Inside Function : convertDataToJson");	
		JSONArray ja = new JSONArray();
		try {

			for (String data : packet) {
				ja.put(data);
			}
		} catch (Exception E) {
			fileLogger.error("Exeption while authenticating the user " + E.getMessage());
		}
		fileLogger.info("Exit Function : convertDataToJson");	
		return ja.toString();
	}
	
	public int insertEvents(String msg) 
	{
		int eventId=-1;
		int triggerId=-1;
		try {
			JSONArray ja = new Operations().getJson("select id from oprrationdata order by id desc limit 1");
			int oprId = -1;
			if(ja.length()>0){
				oprId = ja.getJSONObject(0).getInt("id");
			}
			JSONArray eventTriggerArray = new Operations().getJson("select max(trigger_id) as trigger_id from trigger_cue  where detail like '%Processing%'");
			String tempTriggerId=eventTriggerArray.getJSONObject(0).getString("trigger_id");
			if(tempTriggerId!=null && !tempTriggerId.equalsIgnoreCase("")){
				triggerId=Integer.parseInt(tempTriggerId);
			}
			String query = "insert into currentRunningOperationEvnets(event_msg,opr_id,evt_tgr_id) values('"+msg+"',"+oprId+","+triggerId+") returning id";
			eventId=new Common().executeQueryAndReturnId(query);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return eventId; 
	}
	
	
}
