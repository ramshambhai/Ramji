package in.vnl.api.common;

import java.net.InetAddress;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.vnl.EventProcess.DBDataService;
import in.vnl.msgapp.Common;
import in.vnl.msgapp.Operations;


public class CMSController implements CMSActions 
{
	static Logger fileLogger = Logger.getLogger("file");	
	
	public CMSController()
	{
		
	}
	
	public CMSController(String timeStamp,String id,String ip,String ta,String lat,String lon,String imsi,String eventType,String imei,String freq,String rxl,String cueId,String distance,String angle,String sector) 
	{
		fileLogger.info("Inside function : CMSController");
		fileLogger.debug("@cms in constructor cmscontroller");
		try {
			int systemTypeCode=DBDataService.getSystemType();
			JSONArray CMS = new Operations().getJson("select * from view_btsinfo where code = 11");
			JSONObject CMSData = CMS.getJSONObject(0);
			InetAddress inetAddr = InetAddress.getByName(CMSData.getString("ip"));
			if(systemTypeCode==2 && inetAddr.isReachable(1000)){
				fileLogger.debug("@cms in constructor cmscontroller");
				String msg = createEventMessageFormat(timeStamp,id,ip,ta, lat, lon, imsi, eventType, imei, freq, rxl,cueId,distance,angle,sector);
				sotreEvent(msg,eventType);
				sendEventToCMS(msg);
			}
		}catch (Exception e) {
			fileLogger.error("exception while sending event on cmscontroller with message :"+e.getMessage());
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		fileLogger.info("Exit Function : CMSController");
	}	
	
	@Override
	public void sendEventToCMS(String data) {
		try {
			fileLogger.info("Inside Function : sendEventToCMS");
			// TODO Auto-generated method stub
			fileLogger.debug("@cms about to send");
			JSONArray CMS = new Operations().getJson("select * from view_btsinfo where code = 11");
			JSONObject CMSData = CMS.getJSONObject(0);
			new ApiCommon().sendRequestToUrl("http://"+CMSData.getString("ip") + ":9000/finley/event/tmdas", null, data);
			fileLogger.debug("@cms sent");
		} catch (Exception e) {
			// TODO: handle exception
		}
		fileLogger.info("Exit Function : sendEventToCMS");
	}
	
	public void sendCueToCMS(String date,String source,String detail,String cueId){
		
		try {
			int systemTypeCode=DBDataService.getSystemType();
			JSONArray CMS = new Operations().getJson("select * from view_btsinfo where code = 11");
			JSONObject CMSData = CMS.getJSONObject(0);
			InetAddress inetAddr = InetAddress.getByName(CMSData.getString("ip"));
			if(systemTypeCode==2 && inetAddr.isReachable(1000)){
				String msg = createCueMessageFormat(date,source,detail,cueId);
				sendCueEventToCMS(msg);
			}
		} catch (Exception e) {
			fileLogger.error("exception in sendCueToCMS with message :"+e.getMessage());
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}
	
	public void sendOffsetToCMS(String offset){
		try {
			int systemTypeCode=DBDataService.getSystemType();
			JSONArray CMS = new Operations().getJson("select * from view_btsinfo where code = 11");
			JSONObject CMSData = CMS.getJSONObject(0);
			InetAddress inetAddr = InetAddress.getByName(CMSData.getString("ip"));
			if(systemTypeCode==2 && inetAddr.isReachable(1000)){
				String msg = createOffsetMessageFormat(offset);
				sendOffsetEventToCMS(msg);
			}
		}catch (Exception e) {
			fileLogger.error("exception in sendOffsetToCMS with message :"+e.getMessage());
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}
	
	public void sendCueEventToCMS(String data) {
		try {
			// TODO Auto-generated method stub
			fileLogger.debug("@cms about to send cue to finley");
			JSONArray CMS = new Operations().getJson("select * from view_btsinfo where code = 11");
			JSONObject CMSData = CMS.getJSONObject(0);
			new ApiCommon().sendRequestToUrl("http://"+CMSData.getString("ip") + ":9000/finley/event/cue/FALCON", null, data);
			fileLogger.debug("@cms sent");
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	public void sendOffsetEventToCMS(String data) {
		fileLogger.info("Inside Function : sendOffsetEventToCMS");
		try {
			// TODO Auto-generated method stub
			fileLogger.debug("@cms about to send offset to finley");
			JSONArray CMS = new Operations().getJson("select * from view_btsinfo where code = 11");
			JSONObject CMSData = CMS.getJSONObject(0);
			new ApiCommon().sendRequestToUrl("http://"+CMSData.getString("ip") + ":9000/finley/inventory/tmdas/offset", null, data);
			fileLogger.debug("@cms sent offset");
		} catch (Exception e) {
			// TODO: handle exception
		}
		fileLogger.info("Exit Function : sendOffsetEventToCMS");
	}

	@Override
	public boolean sotreEvent(String eventMessage,String type) {
		
		String query =  "insert into log_evnts(event_type_id,event_data) values("+type+",'"+eventMessage+"')";
		return new Common().executeDLOperation(query);
	}

	@Override
	public String createEventMessageFormat(String timeStamp,String id,String ip,String ta,String lat,String lon,String imsi,String eventType,String imei,String freq,String rxl,String cueId,String distance,String angle,String sector) 
	{
		fileLogger.info("Inside Function : createEventMessageFormat");
		fileLogger.debug("@cms before packing message");
		JSONObject dd = new JSONObject();
		//ObjectMapper om = new ObjectMapper();
		String msg = null;
		try 
		{
			dd.put("TIME_STAMP", timeStamp);
			dd.put("TRANS_ID", id);
			dd.put("DEVICE_IP", ip);
			dd.put("OBJECT_TYPE", "1");
			dd.put("TA", ta);
			dd.put("LATITUDE", lat);
			dd.put("LONGITUDE", lon);
			dd.put("EVENT_TYPE", eventType);
			dd.put("IMSI", imsi);
			dd.put("IMEI", imei);
			dd.put("FREQ", freq);
			dd.put("RXL", rxl);
			dd.put("ACKNOWLEDGE", "0");
			dd.put("CUE_ID", cueId);
			dd.put("PROBABLE_DISTANCE", distance);
			dd.put("ANGLE",angle);
			dd.put("SECTOR",sector);
			msg = dd.toString();
			fileLogger.debug("@cms msg is :"+msg);
			fileLogger.debug("@cms after packing message");
		} catch (Exception e) {
			// TODO: handle exception
		}
		fileLogger.info("Exit Function : createEventMessageFormat");
		return msg;
	}
	
	public String createCueMessageFormat(String date,String source,String detail,String cueId) 
	{
		fileLogger.info("Inside Function : createCueMessageFormat");
		fileLogger.debug("@cms before packing cue message");
		JSONObject dd = new JSONObject();
		//ObjectMapper om = new ObjectMapper();
		String msg = null;
		try 
		{
			dd.put("DATE", date);
			dd.put("SOURCE", source);
			dd.put("DETAIL", detail);
			dd.put("CUE_ID", cueId);
			msg = dd.toString();
			fileLogger.debug("@cms cue msg is :"+msg);
			fileLogger.debug("@cms after packing cue message");
		} catch (Exception e) {
			// TODO: handle exception
		}
		fileLogger.info("Exit Function : createCueMessageFormat");
		return msg;
	}
	
	public String createOffsetMessageFormat(String offset) 
	{
		fileLogger.info("Inside Function : createOffsetMessageFormat");
		fileLogger.debug("@cms before packing offset message");
		JSONObject dd = new JSONObject();
		//ObjectMapper om = new ObjectMapper();
		String msg = null;
		try 
		{
			dd.put("OFFSET", offset);
			msg = dd.toString();
			fileLogger.debug("@cms offset msg is :"+msg);
			fileLogger.debug("@cms after packing offset message");
		} catch (Exception e) {
			// TODO: handle exception
		}
		fileLogger.info("Exit Function : createOffsetMessageFormat");
		return msg;
	}

	@Override
	public String genrateTransId() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void sendCurrentModeCMS() {
		// TODO Auto-generated method stub
		fileLogger.info("Inside Function : sendCurrentModeCMS");
		try{
			int systemTypeCode=DBDataService.getSystemType();
			JSONArray CMS = new Operations().getJson("select * from view_btsinfo where code = 11");
			JSONObject CMSData = CMS.getJSONObject(0);	
			InetAddress inetAddr = InetAddress.getByName(CMSData.getString("ip"));
			if(systemTypeCode==2 && inetAddr.isReachable(1000)){
				JSONArray data = new Operations().getJson("SELECT * from system_modes where id = (select mode_id from current_system_mode order by log_time desc limit 1);");
				if(data.length()>0) 
				{
					try {
						ObjectMapper oo = new ObjectMapper();
						String modeData = oo.writeValueAsString( data.getJSONObject(0));
						new ApiCommon().sendRequestToUrl(CMSData.getString("ip") + ":9000/tmdas/event", null, modeData);
				
					}catch (Exception e) {
						//fileLogger.debug("********************************************************************************");
						fileLogger.error("Exception  Class : CMSController, Method : sendCurrentModeCMS, MSG : " + e.getMessage());
						fileLogger.debug("********************sendCurrentModeCMS stack trace***********************************");
						e.printStackTrace();
						//fileLogger.debug("********************************************************************************");
					}
				}
			}
		}catch(Exception ex){
			
		}
		fileLogger.info("Exit Function : sendCurrentModeCMS");
		}
	
	
	
	public String CurrentCellsToCMS() {
		// TODO Auto-generated method stub
		JSONArray cellData = new Operations().getJson("SELECT oc.*,profile_name as antenna from oprlogs_current oc left join antenna ant on oc.antenna_id=ant.id;");
		return cellData.toString();
	}
	
}
