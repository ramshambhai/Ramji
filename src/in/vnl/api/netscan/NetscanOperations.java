package in.vnl.api.netscan;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import in.vnl.EventProcess.DBDataService;
import in.vnl.api.common.*;
import in.vnl.api.netscan.livescreens.NetScanAlarmServer;
import in.vnl.api.threeg.ThreegOperations;
import in.vnl.api.threeg.livescreens.AlarmServer;
public class NetscanOperations 
{
	static Logger fileLogger = Logger.getLogger("file");
	static Logger statusLogger = Logger.getLogger("status");
	
	public static String url = "";
	static
	{
		url = new Common().getDbCredential().get("netscanserviceurl");
		
	}
	public Response executeActions(LinkedHashMap<String,String> data)
	{
		String operation = NetscanCommands.getFunctionName(data.get("cmdType"));
		if(operation == null)
		{
			return Response.status(201).entity("{}").build();
		}
		switch(operation)
		{
			case "getCurrentStausOfBts":
				return  getCurrentStatusOfBts(data);
			case "setCurrentStausOfBts":
				return  setCurrentStausOfBts(data);
			case "startFreqScan":
				return  startFreqScan(data);
			case "startCellScan":
				return  startCellScan(data);
			case "cellReport":
				return cellReport(data);
			case "freqReport":
				return freqReport(data);
			case "storeAlarm":
				return storeAlarm(data);
			case "insertCellReportData":
				return insertCellReportData(data);
			case "updateLac":
				return updateLac(data);
			case "udpateSubscriberTrackList":
				return udpateSubscriberTrackList(data);
			case "updateSubHold":
				return updateSubHold(data);
			case "triggerMes":
				return triggerMes(data);
			case "setSubHoldEvent":
				return setSubHoldEvent(data);
			case "setGEBMesEvent":
				return setGEBMesEvent(data);
			case "dedicatedMeasEvent":
				return dedicatedMeasEvent(data);	
			case "setSubRedirectionEvent":
				return setSubRedirectionEvent(data);
			case "updateSibInfo":
				return updateSibInfo(data);
			case "insertFreqReportData":
				return insertFreqReportData(data);
			case "insertGPSData":
				return insertGpsData(data);
			case "storeSystemManagerAlarm":
				return storeSystemManagerAlarm(data);
		}
		
		return Response.status(201).entity("{}").build();
	}
	
	public Response setCurrentStausOfBts(LinkedHashMap<String,String> data)
	{
		fileLogger.info("Inside Function : setCurrentStausOfBts");
		LinkedHashMap<String,String> queryParam = new LinkedHashMap<String,String>();
		queryParam.put("CMD_TYPE",data.get("cmdType"));
		queryParam.put("SYSTEM_CODE",data.get("systemCode"));
		queryParam.put("SYSTEM_ID",data.get("systemId"));
		try {
			String query = "select ip,code,statuscode from view_btsinfo where sytemid = '"+data.get("systemId")+"'";
			JSONArray scannerArr = new Operations().getJson(query);
			JSONObject scannerObj = scannerArr.getJSONObject(0);
			int statusCode = scannerObj.getString("statuscode")!=null?Integer.parseInt(scannerObj.getString("statuscode")):-1;
			int newStatusCode = new JSONObject(data.get("data")).getInt("STATUS_CODE");

			fileLogger.debug("@scanstatus setCurrentStausOfBts old statuscode is :"+statusCode);
			fileLogger.debug("@scanstatus setCurrentStausOfBts new statuscode is :"+newStatusCode);
			if((statusCode == 2 || statusCode == 3) && newStatusCode ==1) 
			{	
				fileLogger.debug("@scanstatus setCurrentStausOfBts changed inside if case");
				new ApiCommon().setGpsDataRequest(scannerObj.getString("code"), data.get("systemId"), scannerObj.getString("ip"));
				//new CommonService().restartScanner();
				if(!DBDataService.scannerManuallyStopped) {
					fileLogger.debug("@Since in setCurrentStausOfBts,scanstatus is changed restarting scanner and scannerManuallyStopped= "+DBDataService.scannerManuallyStopped);
					fileLogger.debug("@setCurrentStausOfBts HUA RESTART");
					DBDataService.statusScannerRestart=true;
					new CommonService().restartScanner();
					
				}
				
			}
			updateBtsStatus(data.get("data"),data.get("systemId"));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fileLogger.error("@setCurrentStausOfBts NumberFormatException exception :"+e.getMessage());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fileLogger.error("@setCurrentStausOfBts JSONException exception :"+e.getMessage());
		}
		fileLogger.info("Exit Function : setCurrentStausOfBts");
		return Response.status(201).entity("").build();
	}
	
	
	
	public Response getCurrentStatusOfBts(LinkedHashMap<String,String> data)
	{
		LinkedHashMap<String,String> queryParam = new LinkedHashMap<String,String>();
		queryParam.put("CMD_TYPE",data.get("cmdType"));
		queryParam.put("SYSTEM_CODE",data.get("systemCode"));
		queryParam.put("SYSTEM_ID",data.get("systemId"));
		Response rs =  new ApiCommon().sendRequestToUrl("http://"+data.get("systemIP")+url,queryParam,data.get("data"));
		String resultData = "";
		if(rs != null)
		{
			resultData = rs.readEntity(String.class);
			updateBtsStatus(resultData,data.get("systemIP"),data.get("id"));
		}
		
		return Response.status(201).entity("{}").build();
		//return Response.status(201).entity("").build();
	}
	
	
	
	public Response sendToServer(LinkedHashMap<String,String> data)
	{

		fileLogger.debug("@sendToServer method "+data );
/*		try {
			if(data.get("cmdType").equalsIgnoreCase("START_EXHAUSTIVE_SCAN"))
			{
				ApiCommon apicommon=new ApiCommon();
				apicommon.switchDsp("stop_scanner");
				fileLogger.debug("@sendToServer method stopping scanner" );
				Thread.sleep(1000);
				fileLogger.debug("@sendToServer method starting scanner" );
				apicommon.switchDsp("start_scanner");
				fileLogger.debug("@sendToServer method started scanner and going to wait for "+DBDataService.configParamMap.get("ScannerTimetoUp"));
				Thread.sleep(Long.parseLong(DBDataService.configParamMap.get("ScannerTimetoUp")));
				fileLogger.debug("@sendToServer out from wait");
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		LinkedHashMap<String,String> queryParam = new LinkedHashMap<String,String>();
		queryParam.put("CMD_TYPE",data.get("cmdType"));
		queryParam.put("SYSTEM_CODE",data.get("systemCode"));
		queryParam.put("SYSTEM_ID",data.get("systemId"));
		Response rs =  new ApiCommon().sendRequestToUrl("http://"+data.get("systemIP")+url,queryParam,data.get("data"));
		String resultData = "";
		fileLogger.debug("in line lambda2");
		fileLogger.debug("rs is:"+rs);
		fileLogger.debug("in line lambda2");
		
		if(rs != null)
		{
			resultData = rs.readEntity(String.class);
			executeResponse(data.get("cmdType"),data.get("systemCode"),data.get("systemId"),resultData);
			//updateBtsStatus(resultData,data.get("systemIP"),data.get("id"));
		}
		
	/*	if(rs.getStatus() == 400 && data.get("cmdType").equalsIgnoreCase("STOP_SCAN")) 
		{
			resultData = "alreadyStopped";
		}*/
		fileLogger.debug("in line lambda");
		fileLogger.debug("resultData is:"+resultData);
		fileLogger.debug("in line lambda");
		fileLogger.info("Exit Function : sendToServer");
		return Response.status(201).entity(resultData.toString()).build();
		//return Response.status(201).entity("").build();
	}
	
	public Response sendToNetscanServer(LinkedHashMap<String,String> data)
	{
		fileLogger.info("Inside Function : sendToNetscanServer");
		LinkedHashMap<String,String> queryParam = new LinkedHashMap<String,String>();
		queryParam.put("CMD_TYPE",data.get("cmdType"));
		queryParam.put("SYSTEM_CODE",data.get("systemCode"));
		queryParam.put("SYSTEM_ID",data.get("systemId"));
		Response rs =  new ApiCommon().sendRequestToUrl("http://"+data.get("systemIP")+url,queryParam,data.get("data"));
		String resultData = "";
		
		if(rs != null)
		{
			resultData = rs.readEntity(String.class);
			executeResponse(data.get("cmdType"),data.get("systemCode"),data.get("systemId"),resultData);
			//updateBtsStatus(resultData,data.get("systemIP"),data.get("id"));
		}
		
	if(rs.getStatus() == 200) 
		{
			resultData = "{\"result\":\"success\",\"msg\":\"Exhaustive Scan Started\"}";
		}
		fileLogger.debug("in line open lambda");
		fileLogger.debug("resultData is:"+resultData);
		fileLogger.debug("in line close lambda");
		fileLogger.info("Exit Function : sendToNetscanServer");
		return Response.status(201).entity(resultData).build();
		//return Response.status(201).entity("").build();
	}

	
	
	public Response setSufiConfig(LinkedHashMap<String,String> data)
	{
		LinkedHashMap<String,String> queryParam = new LinkedHashMap<String,String>();
		queryParam.put("CMD_TYPE",data.get("cmdType"));
		queryParam.put("SYSTEM_CODE",data.get("systemCode"));
		queryParam.put("SYSTEM_ID",data.get("systemId"));
		Response rs =  new ApiCommon().sendRequestToUrl("http://"+data.get("systemIP")+url,queryParam,data.get("data"));
		String resultData = "{}";
		return Response.status(201).entity(resultData).build();
		//return Response.status(201).entity("").build();
	}
	
	
	public Response getSufiConfig(LinkedHashMap<String,String> data)
	{
		LinkedHashMap<String,String> queryParam = new LinkedHashMap<String,String>();
		queryParam.put("CMD_TYPE",data.get("cmdType"));
		queryParam.put("SYSTEM_CODE",data.get("systemCode"));
		queryParam.put("SYSTEM_ID",data.get("systemId"));
		Response rs =  new ApiCommon().sendRequestToUrl("http://"+data.get("systemIP")+url,queryParam,data.get("data"));
		String resultData = "{}";
		if(rs != null)
		{
			resultData = rs.readEntity(String.class);
		}
		return Response.status(201).entity(resultData).build();
	}
	public Response startFreqScan(LinkedHashMap<String,String> data)
	{
		LinkedHashMap<String,String> queryParam = new LinkedHashMap<String,String>();
		queryParam.put("CMD_TYPE",data.get("cmdType"));
		queryParam.put("SYSTEM_CODE",data.get("systemCode"));
		queryParam.put("SYSTEM_ID",data.get("systemId"));
		Response rs =  new ApiCommon().sendRequestToUrl("http://"+data.get("systemIP")+url,queryParam,data.get("data"));
		String resultData = "{}";
		/*if(rs != null)
		{
			resultData = rs.readEntity(String.class);
		}*/
		if(rs.getStatus() == 200) 
		{
			resultData = "{\"result\":\"success\",\"msg\":\"Freq Scan Started\"}";
		}
		return Response.status(201).entity(resultData).build();
	}
	
	public Response cellReport(LinkedHashMap<String,String> data)
	{
		fileLogger.info("Inside Function : cellReport");
		LinkedHashMap<String,String> queryParam = new LinkedHashMap<String,String>();
		queryParam.put("CMD_TYPE",data.get("cmdType"));
		queryParam.put("SYSTEM_CODE",data.get("systemCode"));
		queryParam.put("SYSTEM_ID",data.get("systemId"));
		fileLogger.debug("line no 1");
		Response rs =  new ApiCommon().sendRequestToUrl("http://"+data.get("systemIP")+url,queryParam,data.get("data"));
		String resultData = "{}";
		String tech = null;
		Long timestamp = null;
		fileLogger.debug("line no 2");
		JSONArray cellReportData = new JSONArray();
		if(rs != null)
		{
			resultData = rs.readEntity(String.class);
			//fileLogger.debug("++++++++++++++resultdata++++++++++++++++++");
			fileLogger.debug(resultData);
			//fileLogger.debug("++++++++++++++++++++++++++++++++");
			try
			{
				fileLogger.debug("line no 3");
				JSONObject jo = new JSONObject(resultData);
				fileLogger.debug("line no 4");
				tech = jo.getString("TECH");
				fileLogger.debug("line no 5");
				timestamp = jo.getLong("TIMESTAMP");
				fileLogger.debug("line no 6");
				
			}
			catch(Exception e)
			{
				fileLogger.debug("line no 7");
				fileLogger.error("Error Function cellReport : "+e.getMessage());
			}
			
			fileLogger.debug("line no 8");
			executeResponse(data.get("cmdType"),data.get("systemCode"),data.get("systemId"),resultData);
			cellReportData = getCellReportFromDb(tech,timestamp,true);
			//updateBtsStatus(resultData,data.get("systemIP"),data.get("id"));
		}
		
		fileLogger.info("Exit Function : cellReport");
		return Response.status(201).entity(cellReportData.toString()).build();
	}
	
	
	
	
	public Response freqReport(LinkedHashMap<String,String> data)
	{
		fileLogger.info("Inside Function : freqReport");
		
		LinkedHashMap<String,String> queryParam = new LinkedHashMap<String,String>();
		queryParam.put("CMD_TYPE",data.get("cmdType"));
		queryParam.put("SYSTEM_CODE",data.get("systemCode"));
		queryParam.put("SYSTEM_ID",data.get("systemId"));
		
		Response rs =  new ApiCommon().sendRequestToUrl("http://"+data.get("systemIP")+url,queryParam,data.get("data"));
		String resultData = "{}";
		String tech = null;
		Integer timestamp = null;
		JSONArray freqReportData = new JSONArray();
		if(rs != null)
		{
			resultData = rs.readEntity(String.class);
			try
			{
				JSONObject jo = new JSONObject(resultData);
				tech = jo.getString("TECH");
				timestamp = jo.getInt("TIMESTAMP");
				
			}
			catch(Exception e)
			{
				fileLogger.error("Error Funition cellReport : "+e.getMessage());
			}
			
			
			executeResponse(data.get("cmdType"),data.get("systemCode"),data.get("systemId"),resultData);
			freqReportData = getFreqReportFromDb(tech,timestamp,true);
			
		}
		
		fileLogger.info("Exit Function : freqReport");
		return Response.status(201).entity(freqReportData.toString()).build();
	}
	
	
	public JSONArray getCellReportFromDb(String tech,long timestamp,boolean latest)
	{
		String query = null;
		if(!latest)
		{
			query = "select * from view_netscan_cell_scan_report where tech_name = '"+tech+"' and  rpt_timestamp="+timestamp ;
		}
		else
		{
			query = "select * from view_netscan_cell_scan_report where tech_name = '"+tech+"' and rpt_timestamp="+timestamp+" order by id desc limit 1" ;
		}
		
		   JSONArray rs =  new Operations().getJson(query);
		   
		   return rs;
	}
	
	public JSONArray getFreqReportFromDb(String tech,int timestamp,boolean latest)
	{
		String query = null;
		if(!latest)
		{
			query = "select * from view_netscan_freq_scan_report where tech_name = '"+tech+"' and  rpt_timestamp="+timestamp ;
		}
		else
		{
			query = "select * from view_netscan_freq_scan_report where tech_name = '"+tech+"' and rpt_timestamp="+timestamp+" order by id desc limit 1" ;
		}
		
		   JSONArray rs =  new Operations().getJson(query);
		   
		   return rs;
	}
	
	public Response insertCellReportData(LinkedHashMap<String,String> msgdata)
	{
		fileLogger.info("Inside Function : insertCellReportData");
		String jsonData = msgdata.get("data");
		
		try
		{
			
			String btsIp = "";
	        String deviceName=""; 
	        
			ApiCommon aco = new ApiCommon();
			JSONArray btsDetail = aco.getSufiDetail(msgdata.get("systemId"));
	        
			if(btsDetail.length() >=1){
	        	btsIp = btsDetail.getJSONObject(0).get("ip").toString();
	        	deviceName= btsDetail.getJSONObject(0).get("dname").toString();
	        }
			JSONObject jo = new JSONObject(jsonData);
			fileLogger.debug("Data received for cell report : "+jo.toString());
			String reportFrom = "S";
			if(jo.has("report_from"))
			{
				reportFrom = "O";
			}
			
			int id=-1;
			JSONArray techTypes = getTechType();
			
			for(int i =0;i<techTypes.length();i++)
			{
				if(techTypes.getJSONObject(i).get("tech_name").toString().equalsIgnoreCase(jo.getString("TECH")))
				{
					id = techTypes.getJSONObject(i).getInt("tech_id");
				}
			}
			
			String tstmp = new Common().convertMilliSecToDateTimeFormat(jo.getLong("TIMESTAMP"));
			int activeAntennaId = -1;
			JSONArray netscanArray = new Operations().getJson("select active_antenna_id from view_btsinfo where code=3");
			if(netscanArray.length()!=0){
				activeAntennaId=netscanArray.getJSONObject(0).getInt("active_antenna_id");
			}
			
			JSONArray gpsArr=new Operations().getJson("select lat,lon from gpsdata order by logtime desc limit 1");
			String selfLoc=null;
			double selfLat=-1.0;
			double selfLon=-1.0;
			try {
				if(gpsArr.length()>0){
				JSONObject gpsObj=gpsArr.getJSONObject(0);
				selfLat=gpsObj.getDouble("lat");
				selfLon=gpsObj.getDouble("lon");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(selfLat==-1.0 || selfLon==-1.0){
				selfLoc="NA";
			}else{
				selfLat=Math.round(selfLat*1000000.0)/1000000.0;
				selfLon=Math.round(selfLon*1000000.0)/1000000.0;
				selfLoc=selfLat+","+selfLon;
			}
			
			String query = "insert into netscan_cell_scan_report(sufi_id,btsIp,deviceName,tech_id,rpt_timestamp,report_from,rpt_data,antenna_id) values("+msgdata.get("systemId")+",'"+btsIp+"','"+deviceName+"',"+id+",'"+jo.getLong("TIMESTAMP")+"','"+reportFrom+"','"+jo+"',"+activeAntennaId+") returning id;";
			int idofpacket = new Common().executeQueryAndReturnId(query);
			parseCellScanDataForOprData(jo,btsIp,idofpacket,tstmp,activeAntennaId,selfLoc);
			
		}
		catch(Exception e)
		{
			fileLogger.error("Error  Cell Report : "+e.getMessage());
		}
		fileLogger.info("Exit Function : insertCellReportData");
		return Response.status(201).entity("{}").build();
	}
	
	
	/*
	 * This function will be used to parse data 
	 * */
	public void parseCellScanDataForOprData(final JSONObject jo,final String ip,final int id,final String tstmp,final int activeAntennaId,final String selfLoc) 
	{
		fileLogger.info("Inside Function : parseCellScanDataForOprData");
		try 
		{
			new Thread()
		     {
		          public void run()
				    {
					     try 
					     {   	  
				        	  	JSONArray REPORTS = jo.getJSONArray("REPORT");
					  			
					  			for(int i =0;i<REPORTS.length();i++) 
					  			{
					  				LinkedHashMap<String,String> hp = new LinkedHashMap<String,String>();
					  				JSONObject  ja1 =  REPORTS.getJSONObject(i);
					  				
					  				Iterator keys = ja1.keys();
					  				
					  				while( keys.hasNext() ) 
					  				{
					  					String key = (String)keys.next();
					  					if(!key.equalsIgnoreCase("NEIGHBORS") && !key.equalsIgnoreCase("PLMN_LIST"))
					  					hp.put(key, ja1.get(key).toString());
					  				}
					  				
					  				JSONArray PLMN = ja1.getJSONArray("PLMN_LIST");
					  				for(int j=0;j<PLMN.length();j++) 
					  				{
					  					
					  					JSONObject  ja2 =  PLMN.getJSONObject(j);
					  					hp.put("MCC", ja2.getString("MCC"));
					  					hp.put("MNC", ja2.getString("MNC"));
					  					hp.put("TECH", jo.getString("TECH"));
					  					new Operations().oprData(hp,ip,id+"",tstmp,i,activeAntennaId,selfLoc);
					  					
					  				}
					  				
					  			}
					  			
						    }
				  		catch(Exception E) 
				  		{
				  			fileLogger.debug(E.getMessage());	
				  			E.printStackTrace();
				  		}
					}
			 }.start();
			
			
		}
		catch(Exception E) 
		{
			
		}
		
		    
		fileLogger.info("Exit Function : parseCellScanDataForOprData");
			
	}
	
	
	
	public Response insertFreqReportData(LinkedHashMap<String,String> msgdata)
	{
		fileLogger.info("Inside Function : insertFreqReportData");
		String jsonData = msgdata.get("data");
		
		try
		{
			
			String btsIp = "";
	        String deviceName=""; 
	        
			ApiCommon aco = new ApiCommon();
			JSONArray btsDetail = aco.getSufiDetail(msgdata.get("systemId"));
	        if(btsDetail.length() >=1){
	        	btsIp = btsDetail.getJSONObject(0).get("ip").toString();
	        	deviceName= btsDetail.getJSONObject(0).get("dname").toString();
	        }
			
	        JSONObject jo = new JSONObject(jsonData);
			
	        fileLogger.debug("Data received for freq report : "+jo.toString());
			String reportFrom = "S";
			if(jo.has("report_from"))
			{
				reportFrom = "O";
			}
			
			int id=-1;
			JSONArray techTypes = getTechType();
			
			for(int i =0;i<techTypes.length();i++)
			{
				if(techTypes.getJSONObject(i).get("tech_name").toString().equalsIgnoreCase(jo.getString("TECH")))
				{
					id = techTypes.getJSONObject(i).getInt("tech_id");
				}
			}
			
			String query = "insert into netscan_freq_scan_report(sufi_id,btsIp,deviceName,tech_id,rpt_timestamp,report_from,rpt_data) values("+msgdata.get("systemId")+",'"+btsIp+"','"+deviceName+"',"+id+",'"+jo.getLong("TIMESTAMP")+"','"+reportFrom+"','"+jo+"')";
			new Common().executeDLOperation(query);
		}
		catch(Exception e)
		{
			fileLogger.error("Error  freq Report : "+e.getMessage());
		}
		fileLogger.info("Exit Function : insertFreqReportData");
		return Response.status(201).entity("{}").build();
	}
	
	/**/
	
	/*public void insertFreqReportData(LinkedHashMap<String,String> msgdata,String trtype)
	{
		String jsonData = msgdata.get("data");
		try
		{
			JSONObject jo = new JSONObject(jsonData);
			fileLogger.debug("Data received for freq report : "+jo.toString());
			Iterator itr = jo.keys();
			List<String> la = new ArrayList<String>();
			while(itr.hasNext())
			{
				JSONObject ju = jo.getJSONObject((String)itr.next());
				JSONObject reports = ju.getJSONObject("REPORT");
				
				String UARFCN = reports.has("UARFCN")?Integer.toString(reports.getInt("UARFCN")):"";
				String ARFCN = reports.has("ARFCN")?Integer.toString(reports.getInt("ARFCN")):"";
				String EARFCN = reports.has("EARFCN")?Integer.toString(reports.getInt("EARFCN")):"";
				
				String NCC = reports.has("NCC")?Integer.toString(reports.getInt("NCC")):"";
				String BCC = reports.has("BCC")?Integer.toString(reports.getInt("BCC")):"";
				String SNR = reports.has("SNR")?Integer.toString(reports.getInt("SNR")):"";
				String TA = reports.has("TA")?Integer.toString(reports.getInt("TA")):"";
				String RSCP = reports.has("RSCP")?Integer.toString(reports.getInt("RSCP")):"";
				String ECNO = reports.has("ECNO")?Integer.toString(reports.getInt("ECNO")):"";
				String PD = reports.has("PD")?Integer.toString(reports.getInt("PD")):"";
				
				String PCI = reports.has("PCI")?Integer.toString(reports.getInt("PCI")):"";
				String RSRP = reports.has("RSRP")?Integer.toString(reports.getInt("RSRP")):"";
				String RSRQ = reports.has("RSRQ")?Integer.toString(reports.getInt("RSRQ")):"";
				String MCC = "";
				String MNC = "";
				if(reports.has("PLMN"))
				{
					MCC=Integer.toString(reports.getInt("MCC"));
					MNC=Integer.toString(reports.getInt("MNC"));
				}
				
				String query = "INSERT INTO netscan_cell_report("+
            "transiction_id, freq, band, earfcn, cell_id, pci, mcc, mnc,"+ 
            "rsrp, rssi, rsrq, time_r, lat, long, pd, uarfcn, psc, rscp,"
            +"ecno, tech, time_m, arfcn, ncc, bcc, snr, ta)"
				+"VALUES ('"+(trtype+(System.currentTimeMillis() / 1000))+"',"+reports.getInt("FREQ")+", "
				+ ""+reports.getInt("BAND")+", "+reports.getInt("EARFCN")+", "+reports.getInt("CELL_ID")+", "+reports.getInt("PCI")+","
				+ " "+reports.getInt("MCC")+", "+reports.getInt("MNC")+", "+reports.getInt("RSRP")+","+reports.getInt("RSSI")+","
				+ " "+reports.getInt("RSRQ")+", "
				+ ""+reports.getLong("TIMESTAMP")+", "+reports.getInt("LAT")+", "+reports.getInt("LONG")+", "+reports.getInt("PD")+", "
				+ ""+reports.getInt("UARFCN")+", "+reports.getInt("PSC")+", "+reports.getInt("RSCP")+", "+reports.getInt("ECNO")+","
				+ "'"+ju.getString("TECH")+"', "+ju.getLong("TIMESTAMP")+", "+reports.getInt("ARFCN")+", "
				+ ""+reports.getInt("NCC")+", "
				+ ""+reports.getInt("BCC")+", "
				+ ""+reports.getInt("SNR")+", "
				+ ""+reports.getInt("TA")+");";
			
				la.add(query);
			}
			new Common().executeBatchOperation(la);
		}
		catch(Exception e)
		{
			fileLogger.debug("Error  Cell Report : "+e.getMessage());
		}
	}*/
	
	public Response startCellScan(LinkedHashMap<String,String> data)
	{
		LinkedHashMap<String,String> queryParam = new LinkedHashMap<String,String>();
		queryParam.put("CMD_TYPE",data.get("cmdType"));
		queryParam.put("SYSTEM_CODE",data.get("systemCode"));
		queryParam.put("SYSTEM_ID",data.get("systemId"));
		Response rs =  new ApiCommon().sendRequestToUrl("http://"+data.get("systemIP")+url,queryParam,data.get("data"));
		String resultData = "{}";
		/*if(rs != null)
		{
			resultData = rs.readEntity(String.class);
		}*/
		if(rs.getStatus() == 200) 
		{
			resultData = "{\"result\":\"success\",\"msg\":\"Cell Scan Started\"}";
		}
		
		return Response.status(201).entity(resultData).build();
	}
	
	
	public Response setCellLock(LinkedHashMap<String,String> data)
	{
		createServerCallQueryParam(data);
		String resultData = "{}";
		return Response.status(201).entity(resultData).build();
		//return Response.status(201).entity("").build();
	}
	
	
	public Response setCellUnlock(LinkedHashMap<String,String> data)
	{
		createServerCallQueryParam(data);
		String resultData = "{}";
		return Response.status(201).entity(resultData).build();
		//return Response.status(201).entity("").build();
	}
	
	public Response setRedirectionInfo(LinkedHashMap<String,String> data)
	{
		createServerCallQueryParam(data);
		String resultData = "{}";
		return Response.status(201).entity(resultData).build();
		//return Response.status(201).entity("").build();
	}
	
	public Response udpateSubscriberTrackList(LinkedHashMap<String,String> data)
	{
		createServerCallQueryParam(data);
		String resultData = "{}";
		return Response.status(201).entity(resultData).build();
	}
	
	public Response updateSubHold(LinkedHashMap<String,String> data)
	{
		createServerCallQueryParam(data);
		String resultData = "{}";
		return Response.status(201).entity(resultData).build();
	}
	
	public Response triggerMes(LinkedHashMap<String,String> data)
	{
		createServerCallQueryParam(data);
		String resultData = "{}";
		return Response.status(201).entity(resultData).build();
	}
	
	public Response storeAlarm(LinkedHashMap<String,String> msgdata)
	{
		boolean result = new ApiCommon().storeAlarm(msgdata,"scan");
		return new ApiCommon().createResponseMsg(result,null);
	}
	
	public Response storeSystemManagerAlarm(LinkedHashMap<String,String> msgdata)
	{
		boolean result = new ApiCommon().storeSystemManagerAlarm(msgdata,"system_manager");
		return new ApiCommon().createResponseMsg(result,null);
	}
	
	public Response updateSibInfo(LinkedHashMap<String,String> data)
	{
		createServerCallQueryParam(data);
		String resultData = "{}";
		return Response.status(201).entity(resultData).build();
	}
	public Response setSubRedirectionEvent(LinkedHashMap<String,String> msgdata)
	{	
		fileLogger.info("Inside Function : setSubRedirectionEvent");
		try
			{
				String jsonData = msgdata.get("data");  
				JSONObject jo = new JSONObject(jsonData);
				fileLogger.debug(jo.toString());		
						
				
		        String cmdCode = jo.getString("CMD_CODE");
		        String subId = jo.getString("SUB_ID");
		        String dest = jo.getString("DEST");
		        Common co  = new Common();
				String query ="insert into redirection_event (sub_id, dest)values"
						+ "("+subId+","+dest+")";
				co.executeDLOperation(query);
			}
			catch(Exception E)
			{
				fileLogger.error("Parsing Redirection Event Data Exception msg : "+E.getMessage());
			}
			String resultData = "{}";
			fileLogger.info("Exit Function : setSubRedirectionEvent");
			return Response.status(201).entity(resultData).build();
	}
	
	
	public Response setSubHoldEvent(LinkedHashMap<String,String> msgdata)
	{	
		fileLogger.info("Inside Function : setSubHoldEvent");
		try
			{
				String jsonData = msgdata.get("data");
				JSONObject jo = new JSONObject(jsonData);
				fileLogger.debug(jo.toString());		
						
				
		        String cmdCode = jo.getString("CMD_CODE");
		        String subId = jo.getString("SUB_ID");
		        String ecno = jo.getString("ECNO");
		        String rscp = jo.getString("RSCP");
		        Common co  = new Common();
				String query ="insert into mes_hold_event (event_type, sub_id, c_ecno, c_RSCP) values"
						+ "('"+cmdCode+"',"+subId+","+ecno+","+rscp+")";
				co.executeDLOperation(query);
			}
			catch(Exception E)
			{
				fileLogger.error("Parsing setSubHoldEvent Data Exception msg : "+E.getMessage());
			}
			String resultData = "{}";
			fileLogger.info("Exit Function : setSubHoldEvent");
			return Response.status(201).entity(resultData).build();
	}
	
	
	
	public Response dedicatedMeasEvent(LinkedHashMap<String,String> msgdata)
	{		
		fileLogger.info("Inside Function : dedicatedMeasEvent");
		try
			{
				String jsonData = msgdata.get("data");
				JSONObject jo = new JSONObject(jsonData);
				fileLogger.debug(jo.toString());		
						
				
		        String cmdCode = jo.getString("CMD_CODE");
		        JSONObject params = jo.getJSONObject("PARAMS");
		        String subId = params.getString("SUB_ID");
		        
		        
		        JSONArray interFreqMeas = params.getJSONArray("INTER_FREQ_MEAS");
		        JSONArray intraFreqMeas = params.getJSONArray("INTER_FREQ_MEAS");
		        JSONArray interRatMeas = params.getJSONArray("INTER_RAT_MEAS");
		        
		        Common co  = new Common();
		        List<String> la = new ArrayList<String>();
		        Long eventCode = System.currentTimeMillis();
		        for(int i=0;i<interFreqMeas.length();i++)
		        {
		        	
		        	String query ="INSERT INTO DEDICATEDmeasevent("+
		        			"event_code, event_type, sub_id, freq_type, psc, cell, ecno,rscp, rssi, lac, mcc, mnc)"
		        			+ "values("+eventCode+",'"+cmdCode+"',"+subId+",'INTER_FREQ_MEAS',"
		        					+ ""+interFreqMeas.getJSONObject(i).getString("PSC")+","
		        					+ ""+interFreqMeas.getJSONObject(i).getString("CELL_ID")+","
		        					+ ""+interFreqMeas.getJSONObject(i).getString("ECNO")+","
		        					+ ""+interFreqMeas.getJSONObject(i).getString("RSCP")+","
		        					+ ""+interFreqMeas.getJSONObject(i).getString("RSSI")+","
		        					+ ""+interFreqMeas.getJSONObject(i).getString("LAC")+","
		        					+ ""+interFreqMeas.getJSONObject(i).getJSONObject("PLMN_ID").getString("MCC")+","
		        					+ ""+interFreqMeas.getJSONObject(i).getJSONObject("PLMN_ID").getString("MNC")+""
		        					+ ");";	
		        	
		        	la.add(query);
		        }
		        
		        for(int i=0;i<intraFreqMeas.length();i++)
		        {
		        	
		        	String query ="INSERT INTO DEDICATEDmeasevent("+
		        			"event_code, event_type, sub_id, freq_type, psc, cell, ecno,rscp,rssi,lac, mcc, mnc)"
		        			+ "values("+eventCode+",'"+cmdCode+"',"+subId+",'INTRA_FREQ_MEAS',"
		        					+ ""+intraFreqMeas.getJSONObject(i).getString("PSC")+","
		        					+ ""+intraFreqMeas.getJSONObject(i).getString("CELL_ID")+","
		        					+ ""+intraFreqMeas.getJSONObject(i).getString("ECNO")+","
		        					+ ""+intraFreqMeas.getJSONObject(i).getString("RSCP")+","
		        					+ ""+interFreqMeas.getJSONObject(i).getString("RSSI")+","
		        					+ ""+intraFreqMeas.getJSONObject(i).getString("LAC")+","
		        					+ ""+intraFreqMeas.getJSONObject(i).getJSONObject("PLMN_ID").getString("MCC")+","
		        					+ ""+intraFreqMeas.getJSONObject(i).getJSONObject("PLMN_ID").getString("MNC")+""
		        					+ ");";	
		        	
		        	la.add(query);
		        }
		        
		        for(int i=0;i<interRatMeas.length();i++)
		        {
		        	
		        	String query ="INSERT INTO DEDICATEDmeasevent("+
		        			"event_code, event_type, sub_id, freq_type,cell,RSSI,lac,mcc,mnc)"
		        			+ "values("+eventCode+",'"+cmdCode+"',"+subId+",'INTER_RAT_MEAS',"
		        					//+ ""+interRatMeas.getJSONObject(i).getString("PSC")+","
		        					+ ""+interRatMeas.getJSONObject(i).getString("CELL_ID")+","
		        					//+ ""+interRatMeas.getJSONObject(i).getString("ECNO")+","
		        					//+ ""+interRatMeas.getJSONObject(i).getString("RSCP")+","
		        					+ ""+interFreqMeas.getJSONObject(i).getString("RSSI")+","
		        					+ ""+interRatMeas.getJSONObject(i).getString("LAC")+","
		        					+ ""+interRatMeas.getJSONObject(i).getJSONObject("PLMN_ID").getString("MCC")+","
		        					+ ""+interRatMeas.getJSONObject(i).getJSONObject("PLMN_ID").getString("MNC")+""
		        					+ ");";	
		        	
		        	la.add(query);
		        }
		        		        		        
		        co.executeBatchOperation(la);
				
				
			}
			catch(Exception E)
			{
				fileLogger.error("Parsing setSubHoldEvent Data Exception msg : "+E.getMessage());
			}
			String resultData = "{}";
			fileLogger.info("Exit Function : dedicatedMeasEvent");
			return Response.status(201).entity(resultData).build();
	}
	
	
	//setSubRedirectionEvent
	
	public Response setGEBMesEvent(LinkedHashMap<String,String> msgdata)
	{	
		fileLogger.info("Inside Function : setGEBMesEvent");
		try
			{
				String jsonData = msgdata.get("data");
				JSONObject jo = new JSONObject(jsonData);
				fileLogger.debug(jo.toString());		
						
				
		        String cmdCode = jo.getString("CMD_CODE");
		        JSONObject params = jo.getJSONObject("PARAMS");
		        String subId = params.getString("SUB_ID");
		        
		        JSONObject currCells = params.getJSONObject("CURRENT_CELL_MEAS");
		        
		        
		        String ecno = currCells.getString("ECNO");
		        String rscp = currCells.getString("RSCP");
		        JSONArray monitorCells = params.getJSONArray("MONITORED_CELL_MEAS");
		        
		        StringBuilder columnString = new StringBuilder();
		        StringBuilder valueString = new StringBuilder();
		        
		        for(int i=0;i<monitorCells.length();i++)
		        {
		        	columnString.append(",m"+(i+1)+"_psc,m"+(i+1)+"_ecno");
		        	
		        	valueString.append(","+monitorCells.getJSONObject(i).get("PSC")+","+monitorCells.getJSONObject(i).get("ECNO"));	
		        }		
		        		        		        
		        Common co  = new Common();
				String query ="insert into mes_hold_event (event_type, sub_id, c_ecno, c_RSCP"+columnString.toString()+") values"
						+ "('"+cmdCode+"',"+subId+","+ecno+","+rscp+valueString.toString()+")";
				co.executeDLOperation(query);
			}
			catch(Exception E)
			{
				fileLogger.error("Parsing setSubHoldEvent Data Exception msg : "+E.getMessage());
			}
			String resultData = "{}";
			fileLogger.info("Exit Function : setGEBMesEvent");
			return Response.status(201).entity(resultData).build();
	}
	
	
	public void  updateStatusOfAllBts()
	{
		fileLogger.info("Inside Function : setGEBMesEvent");
		Common co = new Common();
		Statement smt = null;
		Connection con = co.getDbConnection();
		
		try
		{
			smt = con.createStatement();			
			String query = "select * from btsmaster";
			fileLogger.debug(query);
			ResultSet rs = smt.executeQuery(query);
			
			while(rs.next())
			{
				LinkedHashMap<String,String> param = new  LinkedHashMap<String,String>();
				param.put("cmdType", "GET_CURR_STATUS");
			    param.put("systemCode", rs.getString("devicetypeid"));
			    param.put("systemIP", rs.getString("ip"));
			    param.put("systemId", rs.getString("sytemid"));
			    param.put("id", rs.getString("b_id"));
			    param.put("data", "{{\"CMD_CODE\":\"GET_CURR_STATUS\"}}");
			    getCurrentStatusOfBts(param);
			}			
		}
		catch(Exception E)
		{
			fileLogger.error("Exeption  updateStatusOfAllBts :"+E.getMessage());
		}
		finally
		{
			try
			{
				smt.close();
				con.close();
			}
			catch(Exception E)
			{
				
			}
		}	
		fileLogger.info("Exit Function : setGEBMesEvent");
	}
	
	public Response updateLac(LinkedHashMap<String,String> data)
	{
		LinkedHashMap<String,String> queryParam = new LinkedHashMap<String,String>();
		queryParam.put("CMD_TYPE",data.get("cmdType"));
		queryParam.put("SYSTEM_CODE",data.get("systemCode"));
		queryParam.put("SYSTEM_ID",data.get("systemId"));
		updateLacForSufi(data.get("data"),data.get("systemId"));
		return Response.status(201).entity("").build();
	}
	
	
	
	
	public void updateLacForSufi(String jsonData,String id)
	{
		fileLogger.info("Inside Function : updateLacForSufi");
		try
		{
			
			JSONObject jo = new JSONObject(jsonData);
			fileLogger.debug(jo.toString());		
					
			
	        String cmdCode = jo.getString("CMD_CODE");
	        String lac = jo.getString("LAC");
			Common co  = new Common();
			String query ="update configurations set lac="+lac+" where systemid = "+id;
			co.executeDLOperation(query);
		}
		catch(Exception E)
		{
			fileLogger.error("Parsing Json updateBtsStatus Exception msg : "+E.getMessage());
		}
		fileLogger.info("Exit Function : updateLacForSufi");
		
	}

	
	
	public void updateBtsStatus(String jsonData,String id)
	{
		fileLogger.info("Inside Function : updateBtsStatus");
		try
		{
			JSONObject jo = new JSONObject(jsonData);
			fileLogger.debug(jo.toString());
			
	        String cmdCode = jo.getString("CMD_CODE");
	        int nodeType = jo.getInt("NODE_TYPE");
	        int nodeId = jo.getInt("NODE_ID");
	        int systemStatus = jo.getInt("STATUS_CODE");
	        //String cellStatus = jo.getString("CELL_STATUS");
			Common co  = new Common();
			//String query ="update btsmaster set status="+systemStatus+",cellstatus = "+cellStatus+" where sytemid = "+id;
			//co.executeDLOperation(query);
			String query ="update btsmaster set status="+systemStatus+" ,cellstatus = null where sytemid = "+id;
			co.executeDLOperation(query);
			
			String opTech = jo.getString("OP_TECH");
			opTech = opTech.replace("'", "");
			
			
			String query2 ="update net_scanner_status set STATUS_CODE="+systemStatus+" ,soft_state = "+jo.getInt("SOFT_STATE")+",timestamp="+jo.getLong("TIMESTAMP")+" ,gps_status = "+jo.getInt("GPS_STATUS")+",dps_status="+jo.getInt("DSP_STATUS")+",repeitition_flage="+jo.getInt("REPETITION_FLAG")+",REPETITION_FREQ ="+jo.getInt("REPETITION_FREQ")+",OP_TECH= '"+opTech+"' where b_id="+id;
			co.executeDLOperation(query2);
		}
		catch(Exception E)
		{
			fileLogger.error("Parsing Json updateBtsStatus Exception msg : "+E.getMessage());
		}
		fileLogger.info("Exit Function : updateBtsStatus");
	}
	
	public void updateBtsStatus(String jsonData,String ip,String id)
	{
		fileLogger.info("Inside Function : updateBtsStatus");
		try
		{
			JSONObject jo = new JSONObject(jsonData);
			statusLogger.debug(jo.toString());		
					
			
	        String cmdCode = jo.getString("CMD_CODE");
	        int nodeType = jo.getInt("NODE_TYPE");
	        int nodeId = jo.getInt("NODE_ID");
	        int systemStatus = jo.getInt("STATUS_CODE");
			Common co  = new Common();
			String query ="update btsmaster set status="+systemStatus+" ,cellstatus = null where ip = '"+ip+"' and b_id="+id;
			statusLogger.debug(query);
			co.executeDLOperation(query);
			
			String opTech = jo.getString("OP_TECH");
			CurrentNetscanAlarm.opTech=opTech;
			
			opTech = opTech.replace("'", "");
			
			String query2 ="update net_scanner_status set STATUS_CODE="+systemStatus+" ,soft_state = "+jo.getInt("SOFT_STATE")+",timestamp="+jo.getLong("TIMESTAMP")+" ,gps_status = "+jo.getInt("GPS_STATUS")+",dps_status="+jo.getInt("DSP_STATUS")+",repeitition_flage="+jo.getInt("REPETITION_FLAG")+",repetition_freq="+jo.getInt("REPITITION_FREQ")+",op_tech= '"+opTech+"' where b_id="+id;
			statusLogger.debug(query2);
			co.executeDLOperation(query2);
		}
		catch(Exception E)
		{
			statusLogger.error("Parsing Json updateBtsStatus Exception msg : "+E.getMessage());
		}
		fileLogger.info("Exit Function : updateBtsStatus");
	}
	
	public void updateSystemManagerStatus(String jsonData,String ip,String id)
	{
		statusLogger.debug("in updateSystemManagerStatus");
		try
		{
			
			JSONObject jo = new JSONObject(jsonData);
			statusLogger.debug(jo.toString());		
	        int systemStatus = jo.getInt("STATUS_CODE");
	        double temperature = Double.parseDouble(jo.getString("TEMP"));
	        temperature=Math.round(temperature*100.0)/100.0;
	        int currStatus=0;
	        //Only Status Captured , setting up , running ,and not reachable
			Common co  = new Common();
			if(systemStatus==1){
				currStatus=1;
			}
			String query ="update btsmaster set status="+currStatus+",tmp="+temperature+" where ip = '"+ip+"' and b_id="+id;
			statusLogger.debug(query);
			co.executeDLOperation(query);
		}
		catch(Exception E)
		{
			statusLogger.error("Parsing Json updateSystemManagerStatus Exception msg : "+E.getMessage());
		}
		statusLogger.debug("out updateSystemManagerStatus");
	}
	
	public void updateIDSStatus(int status,String ip,String id)
	{
		statusLogger.debug("in updateSystemManagerStatus");
		try
		{
			
			//JSONObject jo = new JSONObject(jsonData);
			statusLogger.debug("new code while updating status in updateIDSStatus");		
	        //int systemStatus = jo.getInt("STATUS_CODE");
	        //double temperature = Double.parseDouble(jo.getString("TEMP"));
	        //temperature=Math.round(temperature*100.0)/100.0;
	        //int currStatus=0;
			Common co  = new Common();
			/*if(systemStatus==1){
				currStatus=1;
			}*/
			String query ="update btsmaster set status="+status+" where ip = '"+ip+"' and b_id="+id;
			statusLogger.debug(query);
			co.executeDLOperation(query);
		}
		catch(Exception E)
		{
			statusLogger.error("Parsing Json updateSystemManagerStatus Exception msg : "+E.getMessage());
		}
		statusLogger.debug("out updateSystemManagerStatus");
	}
	
	public void updateDeviceSoftVer(String jsonData,String ip,String id)
	{
		fileLogger.info("Inside Function : updateDeviceSoftVer");
		//fileLogger.debug("in updateSystemManagerSoftVer");
		try
		{
			
			JSONObject jo = new JSONObject(jsonData);
			fileLogger.debug(jo.toString());		
	        String swVersion = jo.getString("SW_VERSION");
			String query ="update btsmaster set sw_version='"+swVersion+"' where ip = '"+ip+"' and b_id="+id;
			fileLogger.debug(query);
			new Common().executeDLOperation(query);
		}
		catch(Exception E)
		{
			fileLogger.error("Parsing Json updateSystemManagerSoftVer Exception msg : "+E.getMessage());
		}
		fileLogger.info("Exit Function : updateDeviceSoftVer");
		
		//fileLogger.debug("out updateSystemManagerSoftVer");
	}
	
	   public Response createServerCallQueryParam(LinkedHashMap<String,String> data)
	   {
		   LinkedHashMap<String,String> queryParam = new LinkedHashMap<String,String>();
			queryParam.put("CMD_TYPE",data.get("cmdType"));
			queryParam.put("SYSTEM_CODE",data.get("systemCode"));
			queryParam.put("SYSTEM_ID",data.get("systemId"));
			Response rs =  new ApiCommon().sendRequestToUrl("http://"+data.get("systemIP")+url,queryParam,data.get("data"));
			return rs;
	   }
	   
	   
	   
	   
	   
	   
	   public void set2gHoldStatus(String sub_id)
	   {
			fileLogger.info("Inside Function : set2gHoldStatus");
		   Common co = new Common();
			Statement smt = null;
			Connection con = co.getDbConnection();
			
			try
			{
				smt = con.createStatement();			
				String query = "select * from btsmaster";
				fileLogger.debug(query);
				ResultSet rs = smt.executeQuery(query);
				
				while(rs.next())
				{
					LinkedHashMap<String,String> param = new  LinkedHashMap<String,String>();
					param.put("cmdType", "GET_CURR_STATUS");
				    param.put("systemCode", rs.getString("devicetypeid"));
				    param.put("systemIP", rs.getString("ip"));
				    param.put("systemId", rs.getString("sytemid"));
				    param.put("id", rs.getString("b_id"));
				    param.put("data", "{{\"CMD_CODE\":\"SET_SUB_2G_HOLD_STATUS\",\"SUB_ID\":\""+sub_id+"\"}}");
				   
				    send2gHoldStatus(param);
				}			
			}
			catch(Exception E)
			{
				fileLogger.error("Exeption  updateStatusOfAllBts :"+E.getMessage());
			}
			finally
			{
				try
				{
					smt.close();
					con.close();
				}
				catch(Exception E)
				{
					
				}
			}	fileLogger.info("Exit Function : set2gHoldStatus");
	   }
	   
	   public Response send2gHoldStatus(LinkedHashMap<String,String> data)
	   {
		   	LinkedHashMap<String,String> queryParam = new LinkedHashMap<String,String>();
		   	queryParam.put("CMD_CODE",data.get("cmdType"));
			queryParam.put("SYSTEM_CODE",data.get("systemCode"));
			queryParam.put("SYSTEM_ID",data.get("systemId"));
			Response rs =  new ApiCommon().sendRequestToUrl("http://"+data.get("systemIP")+url,queryParam,data.get("data"));
			return rs;
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
	   
	   public void executeResponse(String cmdType,String systemCode,String systemId,String data)
	   {
		   
		   try
		   {
			   
			   JSONObject jo = new JSONObject(data);
			   String operation = NetscanCommands.getFunctionName(cmdType);
				if(operation == null)
				{
					//return Response.status(201).entity("{}").build();
				}
				switch(operation)
				{
					case "cellReport":
						executeActions(convertToFormatForExecuteAction(jo.getString("CMD_CODE"),systemCode,systemId,data));
					break;
					case "freqReport":
						executeActions(convertToFormatForExecuteAction(jo.getString("CMD_CODE"),systemCode,systemId,data));
					break;
					case "insertGPSData":
						executeActions(convertToFormatForExecuteAction(jo.getString("CMD_CODE"),systemCode,systemId,data));
					break;
				}
		   }
		   catch(Exception e)
		   {
			   
		   }
	   }
	   
	   
	   public Response insertGpsData(LinkedHashMap<String,String> msgdata) 
	   {
			fileLogger.info("Inside Function : insertGpsData");
		   try {
				fileLogger.debug("@in insertGpsData");
				fileLogger.debug("@gpsdata is :"+msgdata);
			   	JSONObject j1 = new JSONObject(msgdata.get("data"));
				JSONObject jo = j1.getJSONArray("REPORT").getJSONObject(0);
				
				
				String ip = 	msgdata.get("systemIP");
				Long tstmp =  jo.getLong("GPS_TIMESTAMP");
				Double lat = 	jo.getDouble("LAT");
				if(lat!=-1.0){
			    fileLogger.debug("@gpsdata valid data");
				Double lon = 	jo.getDouble("LONG");
				Double altitude = jo.getDouble("ALTITUDE");
				int ACTIVE_SATELLITES = 	jo.getInt("NUM_ACTIVE_SATELLITES");
				String SATELLITES_ID_LIST = jo.getString("SATELLITES_ID_LIST");
				int SATELLITES_IN_VIEW = jo.getInt("SATELLITES_IN_VIEW");
				String packet = msgdata.get("data");
				String query = "insert into netscan_gps_data(ip,tstmp,lat,lon,altitude,ACTIVE_SATELLITES,SATELLITES_ID_LIST,SATELLITES_IN_VIEW,packet) "+
						"values('"+ip+"',"+tstmp+","+lat+","+lon+","+altitude+","+ACTIVE_SATELLITES+",'"+SATELLITES_ID_LIST+"',"+SATELLITES_IN_VIEW+",'"+packet+"')";
				ArrayList<String> serverData = new ArrayList<String>();
				
				serverData.add("0");
				serverData.add("0");
				serverData.add("0");
				serverData.add("0");
				serverData.add(""+lat);
				serverData.add("0");
				serverData.add(""+lon);
				int gpsNode=DBDataService.getGpsNode();
				int systemMode=DBDataService.getSystemMode();
/*				int systemMode=0;
				JSONArray systemModeArr = new Operations().getJson("select code from system_properties where key='system_mode'");
				if(systemModeArr.length()!=0){
					try {
						systemMode = systemModeArr.getJSONObject(0).getInt("code");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}*/
				fileLogger.debug("@gpsdata systemMode is :"+systemMode);
				if(gpsNode==1){ //0 denotes to System manager and 1 denotes to STRU 
					fileLogger.debug("discarded as gpsNode is STRU");
					return Response.status(201).entity("OK").build();
				}
				Operations operations = new Operations();
				JSONArray lastGpsArr=operations.getJson("select lat,lon from gpsdata order by logtime desc limit 1");
				double oldLat=0.00;
				double oldLon=0.00;
				
				if(lastGpsArr.length()!=0){
					try {
						JSONObject lastGpsObject = lastGpsArr.getJSONObject(0);
						oldLat=lastGpsObject.getDouble("lat");
						oldLon=lastGpsObject.getDouble("lon");
						fileLogger.debug("@gpsdata oldLat is :"+oldLat+",oldLon is :"+oldLon);
					} catch (JSONException e) {
						fileLogger.debug("exception in gpsData message:"+e.getMessage());
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				double newLat=lat;
				double newLon=lon;
				fileLogger.debug("@gpsdata newLat is :"+newLat+",newLon is :"+newLon);
				
				double distance = Operations.distanceFromLatLon(oldLat, oldLon, newLat, newLon, "K")*1000;
				distance = Math.round(distance*100.0)/100.0;
				double gpsAccuracy=0.00;
				JSONArray gpsAccuracyArr = operations.getJson("select accuracy from gps_accuracy order by id desc limit 1");
				if(gpsAccuracyArr.length()!=0){
					try {
						gpsAccuracy = gpsAccuracyArr.getJSONObject(0).getDouble("accuracy");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
				if(systemMode==0){	
					if(distance>gpsAccuracy){
/*					int bearing=Common.calcBearingBetweenTwoGpsLoc(oldLat,oldLon,newLat,newLon);
					if(DBDataService.getAntennaToVehicleDiffAngle()>360){
						int antennaToVehicleDiffAngle = bearing-angleOffset;
						DBDataService.setAntennaToVehicleDiffAngle(antennaToVehicleDiffAngle);
					}
					angleOffset=Common.calcNewAngleOffset(bearing);
					
					fileLogger.debug("@gpsdata Operations bearing is :"+bearing);
					new Common().executeDLOperation("update antenna set angle_offset="+angleOffset+" where atype='1'");*/
					serverData.add("stationary");
					serverData.add("NA");
					new GPSSocketServer().sendText(serverData);
					new Common().executeDLOperation(query);
					return Response.status(201).entity("OK").build();
					}
				}else{
					if(distance>gpsAccuracy){
						int angleOffset=0;
						JSONArray angleOffsetArr = new Operations().getJson("select angle_offset from antenna where atype='1' limit 1");
						if(angleOffsetArr.length()!=0){
							try {
								angleOffset = angleOffsetArr.getJSONObject(0).getInt("angle_offset");
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					int bearing=Common.calcBearingBetweenTwoGpsLoc(oldLat,oldLon,newLat,newLon);
					if(DBDataService.getAntennaToVehicleDiffAngle()>360){
						int antennaToVehicleDiffAngle = bearing-angleOffset;
						DBDataService.setAntennaToVehicleDiffAngle(antennaToVehicleDiffAngle);
					}
					angleOffset=Common.calcNewAngleOffset(bearing);
					
					fileLogger.debug("@gpsdata operations bearing is :"+bearing);
					new Common().executeDLOperation("update antenna set angle_offset="+angleOffset+" where atype='1'");
					DBDataService.setAngleOffset(angleOffset);
					serverData.add("moving");
					serverData.add(Integer.toString(angleOffset));
					new GPSSocketServer().sendText(serverData);
					new Common().executeDLOperation(query);
					return Response.status(201).entity("OK").build();
					}
				}

				}else{
					if(systemMode==0){
						serverData.add("stationary");
					}else{
						serverData.add("moving");
					}
					serverData.add("NA");
					new GPSSocketServer().sendText(serverData);
					new Common().executeDLOperation(query);
					return Response.status(201).entity("OK").build();
				}
				}else{
					fileLogger.debug("@gpsdata invalid data");
				}
			} catch (JSONException e) {
				fileLogger.error("@gpsdata exception occurs");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			fileLogger.info("Exit Function : insertGpsData");
		   return Response.status(201).entity("OK").build();
		   
	   }
	   
	   
		public String sendCommandToNetscanServer(LinkedHashMap<String,String> data)
		{
			
			String resultData = "";
			try {
			fileLogger.info("Inside Function : sendCommandToNetscanServer");
			fileLogger.debug("in sendCommandToNetscanServer");
			LinkedHashMap<String,String> queryParam = new LinkedHashMap<String,String>();
			queryParam.put("CMD_TYPE",data.get("cmdType"));
			queryParam.put("SYSTEM_CODE",data.get("systemCode"));
			queryParam.put("SYSTEM_ID",data.get("systemId"));
			Response rs =  new ApiCommon().sendRequestToUrl("http://"+data.get("systemIP")+url,queryParam,data.get("data"));
			
			
			if(rs != null)
			{
				resultData = rs.readEntity(String.class);
			}
			
			fileLogger.debug("in line open lambda");
			fileLogger.debug("resultData is:"+resultData);
			fileLogger.debug("in line close lambda");
			fileLogger.debug("out sendCommandToNetscanServer");
			fileLogger.info("Exit Function : sendCommandToNetscanServer");
			}
			catch (Exception e)
			{
				fileLogger.error("Error in System Manager response function:"+ e.getMessage())	;
				resultData="";
			}
			return resultData;
			
			
			//return Response.status(201).entity("").build();
		}
		
/*		public boolean isTrackingRunningOnDsp0(JSONObject netscanStatus){
			fileLogger.debug("in isTrackingRunningOnDsp0");
			try {
				if(netscanStatus.getInt("DSP0_NODE")==3){
					return true;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			fileLogger.debug("out isTrackingRunningOnDsp0");
			return false;
		}*/
		
		public boolean isTrackingRunning(){
			fileLogger.info("Inside Function : isTrackingRunning");
			//fileLogger.debug("in isTrackingRunning");
			try {
				String modeStatus=new Operations().getJson("select mode_status from running_mode where mode_type='track'").getJSONObject(0).getString("mode_status");
			if(modeStatus!=null && modeStatus.equalsIgnoreCase("idle")){
				return false;
			}else{
				return true;
			}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			fileLogger.info("Exit Function : isTrackingRunning");
			//fileLogger.debug("out isTrackingRunning");
			return false;
		}
		
		public void syncSystemTimeWithScanner()
		{
			fileLogger.info("Inside Function : syncSystemTimeWithScanner");
			//fileLogger.info("inside getSystemTimeFromServer");
			try {
				int systemTypeCode=new Operations().getJson("select code from system_properties where key='system_type'").getJSONObject(0).getInt("code");
				if(systemTypeCode!=2){
					final LinkedHashMap<String, String> queryParam = new LinkedHashMap<String,String>();
					final String url=DBDataService.configParamMap.get("netscanserviceurl");
					JSONArray scannerNodeData=new Operations().getJson("select * from view_btsinfo where code=3");
					JSONObject scannerData=scannerNodeData.getJSONObject(0);
					queryParam.put("CMD_TYPE","GET_CURR_STATUS");
					queryParam.put("SYSTEM_CODE",scannerData.getString("code"));
					queryParam.put("SYSTEM_ID",scannerData.getString("sytemid"));
					final String scannerIp = scannerData.getString("ip");
		 
					Thread thread = new Thread()
					{
						public void run()
						{
							try{
								fileLogger.debug("@setscannertime about to start the thread");
								boolean flag = true;
								int count=0;
								while(flag){
									
									fileLogger.debug("@setscannertime about to get scanner time in attempt :"+(++count));
									fileLogger.debug("scannerIp is :"+scannerIp);
									fileLogger.debug("url is :"+url);
									fileLogger.debug("queryParam is :"+queryParam);
									String resultData = "";
									Response rs= null;
									try {
										rs = new ApiCommon().sendRequestToUrl("http://"+scannerIp+url,queryParam,"{\"CMD_CODE\":\"GET_CURR_STATUS\"}");
									} catch (Exception e) {
										// TODO Auto-generated catch block
										fileLogger.error("@setscannertime exception is :"+e.getMessage());
									}
									fileLogger.debug("@setscannertime at attempt :"+count+": response value is :"+rs);
									if(rs != null)
									{
										resultData = rs.readEntity(String.class);
										JSONObject jo = new JSONObject(resultData);
										Long scannerTime = jo.getLong("TIMESTAMP");
										Date receivedTime = new Date(scannerTime);
										fileLogger.debug("@setscannertime date received from scanner is :"+receivedTime);
										if(scannerTime != -1)
										{
											setSystemTime(receivedTime);
											flag=false;
										}
									}
								}
							}catch(Exception E){
								fileLogger.error("exception while seting time"+E.getMessage());
							}
						}
					};
					thread.start();
				}
			}catch (JSONException e) {
				// TODO Aut=o-generated catch block
				fileLogger.error("exception occurs in getSystemTimeFromServer before thread start");
			}
			fileLogger.info("Exit Function : syncSystemTimeWithScanner");
		  // fileLogger.info("exiting from getSystemTimeFromServer");
		}

		public void setSystemTime(Date receivedTime)
		{
			fileLogger.info("Inside Function : setSystemTime");
		 fileLogger.debug("@setscannertime inside setSystemTime");
		 SimpleDateFormat convertedTimeFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
		 String systemRequiredTime = convertedTimeFormat.format(receivedTime);
		 //ProcessBuilder processBuilder = new ProcessBuilder();
		 try {
			Runtime.getRuntime().exec(new String[]{"date","-s",systemRequiredTime});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			fileLogger.error("@setscannertime exception in setSystemTime message :"+e.getMessage());
		}
			fileLogger.info("Exit Function : setSystemTime");
		 //processBuilder.command("bash", "-s", systemRequiredTime);
		 //ileLogger.debug("@setscannertime exiting from setSystemTime");
		}

}
