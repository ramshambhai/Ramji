package in.vnl.api.common;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;

import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.vnl.EventProcess.DBDataService;
import in.vnl.msgapp.Common;
import in.vnl.msgapp.OffsetSocketServer;
import in.vnl.msgapp.Operations;


public class TRGLController
{
	   static Logger fileLogger = Logger.getLogger("file");
	   static Logger statusLogger = Logger.getLogger("status");
	
	public TRGLController()
	{}
	
	public TRGLController(String timeStamp,String id,String ip,String freq,String angle,String sector,String bandwidth,String timeout,String validity,String cueId) 
	{
		fileLogger.info("Inside Function : TRGLController");
		try {
			int systemTypeCode=DBDataService.getSystemType();
			JSONArray hummerDataArray = new Operations().getJson("select * from view_btsinfo where code = 12");
			JSONObject hummerDataObject = hummerDataArray.getJSONObject(0);
			InetAddress inetAddr = InetAddress.getByName(hummerDataObject.getString("ip"));
			if(systemTypeCode==2 && inetAddr.isReachable(1000)){
				//Operations operations = new Operations();
				String msg = createEventMessageFormat(timeStamp,id,ip,freq,angle,sector,timeout,validity,"8",bandwidth,cueId);
				//sotreEvent(msg,type);
				sendEventToTRGL(msg,"TRIGGER");
			}
			}catch (Exception e) {
			fileLogger.error("exception while sending to trgl in TRGLController with message :"+e.getMessage());
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}fileLogger.info("Exit Function : TRGLController");
	}
	
	public void sendEventToTRGL(String data,String urlEndPoint) {
		fileLogger.info("Inside Function : sendEventToTRGL");
		try {
			int systemTypeCode=DBDataService.getSystemType();
			JSONArray hummerDataArray = new Operations().getJson("select * from view_btsinfo where code = 12");
			JSONObject hummerDataObject = hummerDataArray.getJSONObject(0);
			InetAddress inetAddr = InetAddress.getByName(hummerDataObject.getString("ip"));
			if(systemTypeCode==2 && inetAddr.isReachable(1000)){
				new ApiCommon().sendRequestToUrl("http://"+hummerDataObject.getString("ip") + ":9000/api/tmdas/"+urlEndPoint+"", null, data);
			}
			}catch (Exception e) {
			fileLogger.error("exception while sending to trgl in sendEventToTRGL with message :"+e.getMessage());
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		fileLogger.info("Exit Function : sendEventToTRGL");
	}
	
	public Response sendEventToTRGL(String urlEndPoint) {
		fileLogger.info("Inside Function : sendEventToTRGL");
		Response response = null;
		try {
			int systemTypeCode=DBDataService.getSystemType();
			JSONArray hummerDataArray = new Operations().getJson("select * from view_btsinfo where code = 12");
			JSONObject hummerDataObject = hummerDataArray.getJSONObject(0);
			InetAddress inetAddr = InetAddress.getByName(hummerDataObject.getString("ip"));
			if(systemTypeCode==2 && inetAddr.isReachable(1000))
				response=new ApiCommon().sendRequestToUrl("http://"+hummerDataObject.getString("ip") + ":9000/api/"+urlEndPoint+"", null);
			}catch (Exception e) {
				fileLogger.error("exception in sendEventToTRGL 1 argument with message :+"+e.getMessage());
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		fileLogger.info("Exit Function : sendEventToTRGL");
		return response;
	}

	public boolean sotreEvent(String eventMessage,String type) {
		
		String query =  "insert into log_evnts(event_type_id,event_data) values("+type+",'"+eventMessage+"')";
		return new Common().executeDLOperation(query);
	}
	
	public String createEventMessageFormat(String timeStamp,String id,String ip,String freq,String angle,String sector,String timeout,String validity,String eventType,String bandwidth,String cueId) 
	{
		
		JSONObject dd = new JSONObject();
		String msg = null;
		try 
		{
			dd.put("TIME_STAMP", timeStamp);
			dd.put("TRANS_ID", id);
			dd.put("DEVICE_IP", ip);
			dd.put("FREQ", freq);
			dd.put("ANGLE", angle);
			dd.put("SECTOR", sector);
			dd.put("EVENT_TYPE", eventType);
			dd.put("TIME_OUT", timeout);
			dd.put("VALIDITY", validity);
			dd.put("BANDWIDTH", bandwidth);
			dd.put("CUE_ID", cueId);
			msg = dd.toString();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return msg;
	}

	public String genrateTransId() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String createTrackingMessageFormat(String radiatingAntenna) 
	{
		
		JSONObject dd = new JSONObject();
		
		String msg = null;
		try 
		{
			dd.put("S1", "1");
			dd.put("S2", "1");
			dd.put("S3", "1");
			dd.put("OH1", "1");
			dd.put("OV1", "1");
			
			switch(radiatingAntenna){
			case "S1":
				dd.put("S1", "2");
				break;
			case "S2":
				dd.put("S2", "2");
				break;
			case "S3":
				dd.put("S3", "2");
				break;
			case "OH1":
				dd.put("OH1", "2");
				break;
			case "OV1":
				dd.put("OV1", "2");
				break;
			}
			msg = dd.toString();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return msg;
	}
	
	public String createTrackingStatusMessageFormat(String radiatingStatus) 
	{
		
		JSONObject dd = new JSONObject();
		
		String msg = null;
		try 
		{
			dd.put("radiating", radiatingStatus);
			
			msg = dd.toString();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return msg;
	}
	public void sendTrackingSectorToHummer(String radiatingAntenna) {
		
		try {
			String msg = createTrackingMessageFormat(radiatingAntenna);
			sendEventToTRGL(msg,"Switch");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
public void sendTrackingStatusToHummer(String radiatingSatus) {
	if (DBDataService.getSystemType()==2 )
	{
		try {
			String msg = createTrackingStatusMessageFormat(radiatingSatus);
			sendEventToTRGL(msg,"ChangeMode");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
	
	public void sendOffsetRequestToHummer() {
		fileLogger.info("Inside Function : sendOffsetRequestToHummer");
		try {
			fileLogger.debug("in sendOffsetRequestToHummer");
			Response response=sendEventToTRGL("getPtzOffset"); 
			String resultData = "{}";
			if(response != null)
			{
/*				resultData = response.readEntity(String.class);
				fileLogger.debug("resultdata in sendOffsetRequestToHummer :"+resultData);
				try
				{
					int offset = Integer.parseInt(resultData);
					String query="update antenna set angle_offset="+offset+" where atype='1'";
					new Common().executeDLOperation(query);
					
					
					JSONArray gpsDataArray=new Operations().getJson("select lat,lon from gpsdata order by logtime desc limit 1");
					if(gpsDataArray.length()>0){
					JSONObject gpsDataObj=gpsDataArray.getJSONObject(0);
					JSONObject finalDataObj=new JSONObject();
					finalDataObj.put("lat",gpsDataObj.getString("lat"));
					finalDataObj.put("lon",gpsDataObj.getString("lon"));
					finalDataObj.put("offset",offset+"");
					new OffsetSocketServer().sendText(finalDataObj.toString());
					}
				}
				catch(Exception e)
				{
					fileLogger.debug("Exception in inner catch in sendOffsetRequestToHummer with message :"+e.getMessage());
				}*/
			}
		} catch (Exception e) {
			fileLogger.error("Exception in outer catch in sendOffsetRequestToHummer with message :"+e.getMessage());
			e.printStackTrace();
		}
		fileLogger.info("Exit Function : sendOffsetRequestToHummer");
	}
	
}
