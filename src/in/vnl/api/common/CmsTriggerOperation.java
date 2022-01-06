package in.vnl.api.common;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.vnl.msgapp.Common;
import in.vnl.msgapp.GPSSocketServer;
import in.vnl.msgapp.Operations;
import in.vnl.msgapp.SocketServer;
import in.vnl.api.common.*;
import in.vnl.api.netscan.livescreens.NetScanAlarmServer;
import in.vnl.api.threeg.ThreegOperations;
import in.vnl.api.threeg.livescreens.AlarmServer;
public class CmsTriggerOperation 
{
	static Logger fileLogger = Logger.getLogger("file");
	public static String url = "";
	static
	{
		url = new Common().getDbCredential().get("netscanserviceurl");
		
	}	
	
	   public JSONArray getTechType()
	   {
		   String query = "select * from tech_type_master where tech_status ='A'";
		   JSONArray rs =  new Operations().getJson(query);
		   return rs;
	   }
	   
	   public LinkedHashMap<String,String> convertToFormatForExecuteAction(String cmdType,String systemCode,String systemId,String data )
	   {
		   LinkedHashMap<String,String> param = new LinkedHashMap<String,String>();
		   param.put("cmdType", cmdType);
		   param.put("systemCode", systemCode);
		   param.put("systemId", systemId);
		   param.put("data", data);
		   return param;
	   }   
	   
		public String sendCommandToNetscanServer(LinkedHashMap<String,String> data)
		{
			fileLogger.info("Inside Function : sendCommandToNetscanServer");
			LinkedHashMap<String,String> queryParam = new LinkedHashMap<String,String>();
			queryParam.put("CMD_TYPE",data.get("cmdType"));
			queryParam.put("SYSTEM_CODE",data.get("systemCode"));
			queryParam.put("SYSTEM_ID",data.get("systemId"));
			Response rs =  new ApiCommon().sendRequestToUrl("http://"+data.get("systemIP")+url,queryParam,data.get("data"));
			String resultData = "";
			
			if(rs != null)
			{
				resultData = rs.readEntity(String.class);
			}
			
			fileLogger.debug("in line open lambda");
			fileLogger.debug("resultData is:"+resultData);
			fileLogger.debug("in line close lambda");
			fileLogger.info("Exit Function : sendCommandToNetscanServer");
			return resultData;
			
			
			//return Response.status(201).entity("").build();
		}		
	   
		public Response sendToFinleyServer(LinkedHashMap<String,String> data)
		{
			fileLogger.info("Inside Function : sendToFinleyServer");
			LinkedHashMap<String,String> queryParam = new LinkedHashMap<String,String>();
			queryParam.put("CMD_TYPE",data.get("cmdType"));
			queryParam.put("SYSTEM_CODE",data.get("systemCode"));
			queryParam.put("SYSTEM_ID",data.get("systemId"));
			String url="/tmdas/event";
			Response rs =  new ApiCommon().sendRequestToUrl("http://"+data.get("systemIP")+url,queryParam,data.get("data"));
			String resultData = "";
			
			if(rs != null)
			{
				resultData = rs.readEntity(String.class);
				//executeResponse(data.get("cmdType"),data.get("systemCode"),data.get("systemId"),resultData);
				//updateBtsStatus(resultData,data.get("systemIP"),data.get("id"));
			}
			
		if(rs.getStatus() == 200) 
			{
				resultData = "{\"result\":\"success\",\"msg\":\"data sent to finley server\"}";
			}
			//fileLogger.debug("in line open lambda");
			fileLogger.debug("resultData is:"+resultData);
			//fileLogger.debug("in line close lambda");
			fileLogger.info("Exit Function : sendToFinleyServer");
			return Response.status(201).entity(resultData).build();
			//return Response.status(201).entity("").build();
		}
	
}

