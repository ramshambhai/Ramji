package in.vnl.api.common;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.io.IOException;
//import java.util.Date;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import java.text.SimpleDateFormat;

import javax.ws.rs.FormParam;
//import java.util.Calendar;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import in.vnl.api.common.ApiCommon;
import in.vnl.api.threeg.ThreegOperations;
import in.vnl.api.twog.TwogOperations;
import in.vnl.msgapp.Common;
import in.vnl.msgapp.GeoSchedulerServer;
import in.vnl.msgapp.Operations;
import in.vnl.sockets.UdpServerClient;
import in.vnl.api.common.livescreens.AutoOperationServer;
import in.vnl.api.common.livescreens.AutoStateServer;
import in.vnl.api.config.PossibleConfigurations;
import in.vnl.api.netscan.CurrentNetscanAlarm;
import in.vnl.api.netscan.NetscanOperations;
import in.vnl.report.ReportServer;

public class CommonOperation {
	   static Logger fileLogger = Logger.getLogger("file");
	
	   public void updateScanningAndTrackingAntennaInDevices(){
		   Common common = new Common();
		   HashMap<String,String> antennaMap= getScanningAndTrackingAntenna();	
		   String query="update btsmaster set antenna_apply='"+antennaMap.get("scanning")+"' where devicetypeid=(select d_id from devicetype where code=3)"; 
		   common.executeDLOperation(query);
		   query="update btsmaster set antenna_apply='"+antennaMap.get("tracking")+"' where devicetypeid=(select d_id from devicetype where code in(0,1,2,5))"; 
		   common.executeDLOperation(query);
		   }
	   
	   public HashMap<String,String> getScanningAndTrackingAntenna(){
		   fileLogger.info("Inside Function : getScanningAndTrackingAntenna");
		   String query="select id,inscanning,intracking from antenna";
		   HashMap<String,String> antennaMap= new HashMap<String,String>();
		   try {
			Operations operations = new Operations();
				JSONArray antennaList=operations.getJson(query);
				   String scanningAntenna="";
				   String trackingAntenna="";
				   
				   
				   for(int i=0;i<antennaList.length();i++){
					   
					   JSONObject antennaData=antennaList.getJSONObject(i);
					   
					   if(antennaData.getString("inscanning").equalsIgnoreCase("t")){
						   scanningAntenna=antennaData.getInt("id")+",";
					   }
					   if(antennaData.getString("intracking").equalsIgnoreCase("t")){
						   trackingAntenna=antennaData.getInt("id")+",";
					   }
				   }
				   fileLogger.debug("@antenna scanning:"+scanningAntenna);
				   fileLogger.debug("@antenna tracking:"+trackingAntenna);
				   if(scanningAntenna.length()>0){
				   scanningAntenna=scanningAntenna.substring(0,scanningAntenna.length()-1);
				   }
				   if(trackingAntenna.length()>0){
				   trackingAntenna=trackingAntenna.substring(0,trackingAntenna.length()-1);
				   }
				   antennaMap.put("scanning", scanningAntenna);
				   antennaMap.put("tracking", trackingAntenna);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			//fileLogger.debug("********************************************************************************");
			fileLogger.error("Exception  Class : CommonOperations, Method : getScanningAndTrackingAntenna, MSG : " + e.getMessage());
			fileLogger.debug("********************getScanningAndTrackingAntenna stack trace***********************************");
			e.printStackTrace();
			//fileLogger.debug("********************************************************************************");
		}	   
		  // fileLogger.debug("Exiting from getScanningAndTrackingAntenna");
		   fileLogger.info("Exit Function : getScanningAndTrackingAntenna");
		   return antennaMap;
	   }
	   
		public Response sendToFinley(LinkedHashMap<String,String> data,String url)
		{
		    fileLogger.info("Inside Function : sendToFinley");
			LinkedHashMap<String,String> queryParam = new LinkedHashMap<String,String>();
			queryParam.put("CMD_TYPE",data.get("cmdType"));
			queryParam.put("SYSTEM_CODE",data.get("systemCode"));
			queryParam.put("SYSTEM_ID",data.get("systemId"));
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
				resultData = "{\"result\":\"success\",\"msg\":\"Sent Successfully\"}";
			}
			fileLogger.debug("in line open lambda");
			fileLogger.debug("resultData is:"+resultData);
			fileLogger.debug("in line close lambda");
			fileLogger.info("Exit Function : sendToFinley");
			return Response.status(201).entity(resultData).build();
			//return Response.status(201).entity("").build();
		}
		
		public String restartSystem(){
			 Runtime runtime=Runtime.getRuntime();
			 String operatingSystem = System.getProperty("os.name").toLowerCase();
			 try {
			 if(operatingSystem.indexOf("linux")!=-1){
				 runtime.exec("reboot");
			 }else if(operatingSystem.indexOf("win")!=-1){
				 runtime.exec("shutdown -r");
			 }else{
				 runtime.exec("reboot"); 
			 }
			 return "true";
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "false";
			}
		}
}
