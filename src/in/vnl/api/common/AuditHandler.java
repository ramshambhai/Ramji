package in.vnl.api.common;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.vnl.api.twog.TwogOperations;
import in.vnl.msgapp.Common;
import in.vnl.msgapp.Operations;

public class AuditHandler 
{
	static Logger fileLogger = Logger.getLogger("file");
	public boolean auditLog(LinkedHashMap<String,String> data,int type) 
	{
		
		    fileLogger.info("Inside Function : auditLog");
			Common co = new Common();
		
		 	String query = "select max(id) id from oprrationdata";
			int id =-1;
					
			try {
				JSONObject rs =  new Operations().getJson(query).getJSONObject(0);
				if(rs.getString("id")!=null && !rs.getString("id").equals("")){
				id = rs.getInt("id");
				}
			} catch (JSONException e) {
				fileLogger.error("@cell exception in getting max operation id");
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			
			ObjectMapper om = new ObjectMapper();
			String jsonData = "";
			try {
				jsonData = om.writeValueAsString(data);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			
			try {

				if(data.get("component_type").equalsIgnoreCase("SPOILER")) {
					jsonData=jsonData.replaceAll("\\\\","").replaceAll("\\{","").replaceAll("\\}","");;
					jsonData="{"+jsonData+"}";
					jsonData=jsonData.substring(0, jsonData.length() - 2)+"}";
					
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(id != -1 ) 
			{
				co.executeDLOperation("INSERT INTO public.log_audit(opr_id,log_type_id, log_data) VALUES ("+id+", "+type+", '"+jsonData+"')");
			}
			else 
			{
				co.executeDLOperation("INSERT INTO public.log_audit(log_type_id, log_data) VALUES ("+type+", '"+jsonData+"')");
			}
		    fileLogger.info("Exit Function : auditLog");
		return true;	
	}
	
	
	public void auditStatus(LinkedHashMap<String,String> data,int currentStatus,int lastStatus) 
	{
		fileLogger.info("Inside Function : auditStatus");
		String status="";
		
		if(currentStatus != lastStatus)
		{
			String query = "select * from view_btsinfo where ip = '"+data.get("systemIP")+"'";
			JSONArray result = new Operations().getJson(query);
			try {
				status = result.getJSONObject(0).getString("status");
				LinkedHashMap<String,String> log = new LinkedHashMap<String,String>();
				log.put("component_type", data.get("name"));
				log.put("IP", data.get("systemIP"));
				log.put("system_status", status);
				//new changes 
				if ((data.get("devicetypeid").equals("14") || data.get("devicetypeid").equals("11"))&& (currentStatus==1)) 
				//if((Integer.parseInt(result.getJSONObject(0).getString("devicetypeid")) == 14) && (currentStatus==1)) 
				{
					//log.put("Temperature", data.get("tmp"));
					log.put("Temperature", data.get("TEMP"));
				     fileLogger.debug("Theee data= "+ data);
					if(data.get("devicetypeid").equals("14")) {
						log.put("CPU_Usage",data.get("CPU"));
						try {
						log.put("Disk_Usage",data.get("DISK"));
						log.put("Memory_Usage",data.get("MEM"));
						}
						catch(Exception ex) {
						fileLogger.info("Exception occurred auditStatus inside1 "+ ex.getMessage() +" And "+data.get("DISK") + " and "+data.get("MEM"));
						}
					}
					
					for ( String key : data.keySet() ) {
					    fileLogger.debug("Key = "+key+ " and value = " + data.get(key));
					}

				}
				
				new AuditHandler().auditLog(log, 2);
			} catch (Exception e) {
				fileLogger.info("Exception occureed auditStatus "+ e.getMessage());
			}
		}
		else if ((data.get("devicetypeid").equals("14") || data.get("devicetypeid").equals("11"))&& (currentStatus==1)) 
		{
			String query = "select * from view_btsinfo where ip = '"+data.get("systemIP")+"'";
			JSONArray result = new Operations().getJson(query);
			try {
				status = result.getJSONObject(0).getString("status");
				LinkedHashMap<String,String> log = new LinkedHashMap<String,String>();
				log.put("component_type", data.get("name"));
				log.put("IP", data.get("systemIP"));
				log.put("system_status", status);
				//if ((data.get("devicetypeid")=="14")&& (currentStatus==1)) 
				//if((Integer.parseInt(result.getJSONObject(0).getString("devicetypeid")) == 14) && (currentStatus==1)) 
				//log.put("Temperature", data.get("tmp"));
				log.put("Temperature", data.get("TEMP"));
				fileLogger.debug("TEMP= "+data.get("TEMP"));
				if(data.get("devicetypeid").equals("14")) {
				//log.put("CPU_Usage",data.get("cpu_usage"));
				log.put("CPU_Usage",data.get("CPU"));
				try 
				{
					log.put("Disk_Usage",data.get("DISK"));
					log.put("Memory_Usage",data.get("MEM"));
				}
				catch(Exception ex) {
					fileLogger.info("Exception occurred auditStatus inside1 "+ ex.getMessage() +" And "+data.get("DISK") + " and "+data.get("MEM"));
				}
				fileLogger.debug("CPU_Usage= "+data.get("CPU"));
				}
				for ( String key : data.keySet() ) {
				      fileLogger.debug("Key = "+key+ " and value = " + data.get(key));
				}

				new AuditHandler().auditLog(log, 2);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		fileLogger.debug("@version status lastStatus:"+lastStatus+":currentStatus"+currentStatus+"ip:"+data.get("systemIP"));
		if((lastStatus == 2 || lastStatus == 3) && (currentStatus ==0 ||  currentStatus == 1)) 
		{	
			fileLogger.debug("@version for :"+data.get("systemIP"));
			final LinkedHashMap<String,String> param = data;
		    Thread t = new Thread()
		     {
		          public void run()
		          {
		        	  fileLogger.debug("starting Thread to get status for "+param.get("systemIP"));
		        	  new TwogOperations().getCurrentSoftVerOfDevices(param);
				     //new DeviceStatusServer().sendText("ok");
				     fileLogger.debug("Ending Thread to get status for "+param.get("systemIP"));
		          }
		     };
		     t.start();	 
		}
		fileLogger.info("Exit Function : auditStatus");
	}
	
	
	public void auditIDSStatus(LinkedHashMap<String,String> data,int currentStatus,int lastStatus) 
	{
		fileLogger.info("Inside Function : auditIDSStatus");
		String status="";
		if(currentStatus != lastStatus)
		{
			String query = "select * from view_btsinfo where ip = '"+data.get("systemIP")+"'";
			JSONArray result = new Operations().getJson(query);
			try {
				status = result.getJSONObject(0).getString("status");
				LinkedHashMap<String,String> log = new LinkedHashMap<String,String>();
				log.put("component_type", data.get("name"));
				log.put("IP", data.get("systemIP"));
				log.put("system_status", status);
				new AuditHandler().auditLog(log, 2);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
/*		fileLogger.debug("@version status lastStatus:"+lastStatus+":currentStatus"+currentStatus+"ip:"+data.get("systemIP"));
		if((lastStatus == 2 || lastStatus == 3) && (currentStatus ==0 ||  currentStatus == 1)) 
		{	
			fileLogger.debug("@version for :"+data.get("systemIP"));
			final LinkedHashMap<String,String> param = data;
		    Thread t = new Thread()
		     {
		          public void run()
		          {
		        	  fileLogger.debug("starting Thread to get status for "+param.get("systemIP"));
		        	  new TwogOperations().getCurrentSoftVerOfDevices(param);
				     //new DeviceStatusServer().sendText("ok");
				     fileLogger.debug("Ending Thread to get status for "+param.get("systemIP"));
		          }
		     };
		     t.start();	 
		}*/
		fileLogger.info("Exit Function : auditIDSStatus");
	}
	
	
	public void audit_operation(String oprId,String action,String id) 
	{
		fileLogger.info("Inside Function : audit_operation");
		String query = "select oprrationdata.*,opr_type.opr_name from oprrationdata left join opr_type on (opr_type.id = oprrationdata.opr_type::numeric) where oprrationdata.id = "+oprId;
		JSONArray result = new Operations().getJson(query);
		LinkedHashMap<String,String> log = new LinkedHashMap<String,String>();
		try {
			
			if(result.length()>0){
			String oprPlmn=result.getJSONObject(0).getString("plmn");
			String queryAppend="";
			String oprList="";
			if(!oprPlmn.equalsIgnoreCase("all")){
				queryAppend=" where plmn in ("+oprPlmn+")";
				oprList = new Operations().getJson("select string_agg(opr,',') list from plmn_opr"+queryAppend+"").getJSONObject(0).getString("list"); 
			}else{
				oprList="";
			}
			String oprType=result.getJSONObject(0).getString("opr_type");
			String eventName="";
			if(oprType.equals("1")){
				//eventName="scheduler";
				eventName=Constants.schedulerEvent;
			}else if(oprType.equals("2")){
				//eventName=automatic";
				eventName=Constants.automaticEvent;
			}
			log.put("action", action);
			log.put("id",id);
			log.put("name", result.getJSONObject(0).getString("name"));
			log.put("opr_type", eventName);
			log.put("Operator List", oprList);
			log.put("FSL_coverage", result.getJSONObject(0).getString("distance"));
			new AuditHandler().auditLog(log, 3);
			}
		} catch (Exception e) {
			fileLogger.error("Error in auditing create operation");
			// TODO: handle exception
		}
		fileLogger.info("Exit Function : audit_operation");
	}
	
	public void audit_configuration(String ip) 
	{
		fileLogger.info("Inside Function : audit_configuration");
		String query = "select * from view_btsinfo where ip='"+ip+"'";
		JSONArray result = new Operations().getJson(query);
		LinkedHashMap<String,String> log = new LinkedHashMap<String,String>();
		try {
			
			JSONObject js = new JSONObject(new String((String) result.getJSONObject(0).get("config")));
			String tech = result.getJSONObject(0).getString("typename");
			
			log.put("TECH",tech);
			log.put("MCC", js.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").getJSONObject("PLMN_ID").getString("MCC"));
			log.put("MNC", js.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").getJSONObject("PLMN_ID").getString("MNC"));
			if(tech.equalsIgnoreCase("3G"))
			{
				log.put("UL_UARFCN", js.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").getString("UL_UARFCN"));
				log.put("Power", js.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").getString("TOTAL_TX_POWER"));
			}
			else if(tech.equalsIgnoreCase("4G"))
			{
				log.put("UL_EARFCN", js.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").getString("UL_EARFCN"));
				log.put("Power", js.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").getString("Reference_Signal_Power"));
				log.put("L1_Attn", js.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").getString("L1_ATT"));
			}
			else
			{
				log.put("UL_E/U/ARFCN", js.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").getString("UL_UARFCN"));
				log.put("Power", js.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").getString("TOTAL_TX_POWER"));
			}
			log.put("ip", ip);
			new AuditHandler().auditLog(log, 4);
		} catch (Exception e) {
			// TODO: handle exception
		}
		fileLogger.info("Exit Function : audit_configuration");
	}
	
	public void audit_configuration_2g(String ip) 
	{
		fileLogger.info("Inside Function : audit_configuration_2g");
		String query = "select * from n_cells_data where ip='"+ip+"'";
		JSONArray result = new Operations().getJson(query);
		LinkedHashMap<String,String> log = new LinkedHashMap<String,String>();
		try {
			
			JSONObject data = result.getJSONObject(0);
			
			log.put("TECH","2G");
			log.put("PLMN", data.getString("plmn"));//.substring(0, 4)); Modified by Sanjay Sir
			//log.put("MNC", data.getString("plmn").substring(4));
			log.put("ARFCN", data.getString("arfcn"));
			log.put("Power", data.getString("rxs"));
			log.put("ip", ip);
			new AuditHandler().auditLog(log, 4);
		} 
		catch (Exception e) {
			// TODO: handle exception
		}
		fileLogger.info("Exit Function : audit_configuration_2g");
	}
	
	public void audit_oprData(JSONObject data) 
	{
		fileLogger.info("Inside Function : audit_oprData");
		String lat_long=null;
		try{
			lat_long=(data.getString("lat")+","+data.getString("lon"));
			if (lat_long.equals("-1,-1")){
				lat_long="0.0,0.0";
			}
		}
		catch(Exception ex){
			fileLogger.error("Error while fetching lat_long ");
		}
		try {
			LinkedHashMap<String,String> log = new LinkedHashMap<String,String>();
			log.put("TECH",data.getString("packet_type"));
			log.put("Country_Name",data.getString("country"));
			log.put("Operator_Name",data.getString("operators"));
			log.put("name",data.getString("name"));
			log.put("MCC",data.getString("mcc"));
			log.put("MNC",data.getString("mnc"));
			log.put("LAC",data.getString("lac"));
			log.put("CELL",data.getString("cell"));
			log.put("ARFCN",data.getString("arfcn"));
			log.put("UARFCN",data.getString("uarfcn"));
			log.put("PSC",data.getString("psc"));
			log.put("EARFCN",data.getString("earfcn"));
			log.put("PCI",data.getString("pci"));
			log.put("TAC",data.getString("tac"));
			//log.put("EARFCN",data.getString("earfcn"));
			log.put("DL_Absolute_Frequency",data.getString("freq"));
			//log.put("UL_Absolute_Frequency","NA");
			log.put("BCC",data.getString("bcc"));
			log.put("NCC",data.getString("ncc"));
			log.put("Receive_Level_dBm",data.getString("rssi"));
			//log.put("Scanned_Cell_Coordinate",data.getString("sysloc"));
			log.put("Scanned_Cell_Coordinate",lat_long);
			log.put("ANTENNA_NAME",data.getString("profile_name"));
			log.put("ANGLE_OFFSET",data.getString("off_angle"));
			log.put("SELF_LOCATION",data.getString("self_loc"));
			String bandwidth=data.getString("bandwidth");
			if(bandwidth.equalsIgnoreCase("null")|| bandwidth.equalsIgnoreCase("")||bandwidth==null) {
				bandwidth="";
			}
			if(data.getString("packet_type").equalsIgnoreCase("GSM")) {
				bandwidth="0.2MHz";
			}
			log.put("bandwidth",bandwidth);
			
			
			
			
			
			
			
			
			
			
			
			
			
			int packet_id=Integer.parseInt(data.getString("packet_id"));
			
			String neighboursReceived=CommonService.getNeighboursDataCell_Infrastructure_Report( packet_id,data.getString("packet_type"));
			log.put("NEIGHBOURS",neighboursReceived);		
			fileLogger.debug("**audit_oprData neighboursReceived");
			
			
			new AuditHandler().auditLog(log, 6);
			
		} catch (Exception e) {
			//fileLogger.debug("********************************************************************************");
			fileLogger.error("Exception  Class : AuditHandler, Method : audit_oprData, MSG : " + e.getMessage());
			fileLogger.debug("********************audit_oprData stack trace***********************************");
			e.printStackTrace();
			//fileLogger.debug("********************************************************************************");
		}
		fileLogger.info("Exit Function : audit_oprData");
	}
	
	
	
	public void audit_que(LinkedHashMap<String,String> log) 
	{
		fileLogger.info("Inside Function : audit_que");
		try {
			new AuditHandler().auditLog(log, 7);
			
		} catch (Exception e) {
			//fileLogger.debug("********************************************************************************");
			fileLogger.error("Exception  Class : AuditHandler, Method : audit_que, MSG : " + e.getMessage());
			fileLogger.debug("********************audit_que stack trace***********************************");
			e.printStackTrace();
			//fileLogger.debug("********************************************************************************");
		}
		fileLogger.info("Exit Function : audit_que");
	}
	
	public void auditConfig(String ip,String action) 
	{
		fileLogger.info("Inside Function : auditConfig");
		try {
			LinkedHashMap<String,String> log = new LinkedHashMap<String,String>();
			log.put("IP",ip);
			log.put("Action",action);
			new AuditHandler().auditLog(log, 4);
			
		} catch (Exception e) {
			//fileLogger.debug("********************************************************************************");
			fileLogger.error("Exception  Class : AuditHandler, Method : auditConfig, MSG : " + e.getMessage());
			fileLogger.debug("********************auditConfig stack trace***********************************");
			e.printStackTrace();
			//fileLogger.debug("********************************************************************************");
		}
		fileLogger.info("Exit Function : auditConfig");
	}
	
	public void auditConfigExt(LinkedHashMap<String,String> log) 
	{
		fileLogger.info("Inside Function : auditConfigExt");
		try {
			new AuditHandler().auditLog(log, 4);
			
		} catch (Exception e) {
			//fileLogger.debug("********************************************************************************");
			fileLogger.error("Exception  Class : AuditHandler, Method : auditConfig, MSG : " + e.getMessage());
			fileLogger.debug("********************auditConfig stack trace***********************************");
			e.printStackTrace();
			//fileLogger.debug("********************************************************************************");
		}
		fileLogger.info("Exit Function : auditConfigExt");
	}
	
	
	
	public void auditConfigJammer(LinkedHashMap<String,String> log) 
	{
		fileLogger.info("Inside Function : auditConfigJammer");
		try {
			
			
			new AuditHandler().auditLog(log, 4);
			
			
		} catch (Exception e) {
			//fileLogger.debug("********************************************************************************");
			fileLogger.error("Exception  Class : AuditHandler, Method : auditConfig, MSG : " + e.getMessage());
			fileLogger.debug("********************auditConfig stack trace***********************************");
			e.printStackTrace();
			//fileLogger.debug("********************************************************************************");
		}
		fileLogger.info("Exit Function : auditConfigJammer");
	}
	public void audit_inventory(LinkedHashMap<String,String> log) 
	{
		fileLogger.info("Inside Function : auditConfigExt");
		try {
			new AuditHandler().auditLog(log, 8);
			
		} catch (Exception e) {
			//fileLogger.debug("********************************************************************************");
			fileLogger.error("Exception  Class : AuditHandler, Method : audit_inventory, MSG : " + e.getMessage());
			fileLogger.debug("********************audit_inventory stack trace***********************************");
			e.printStackTrace();
			//fileLogger.debug("********************************************************************************");
		}
		fileLogger.info("Exit Function : auditConfigExt");
	}
	
	public void auditTarget(LinkedHashMap<String,String> log) 
	{
		fileLogger.info("Inside Function : auditTarget");
		try {
			new AuditHandler().auditLog(log, 10);
			
		} catch (Exception e) {
			//fileLogger.debug("********************************************************************************");
			fileLogger.error("Exception  Class : AuditHandler, Method : auditTarget, MSG : " + e.getMessage());
			fileLogger.debug("********************auditTarget stack trace***********************************");
			e.printStackTrace();
			//fileLogger.debug("********************************************************************************");
		}
		fileLogger.info("Exit Function : auditTarget");
	}
	
	public void auditOprStop(LinkedHashMap<String,String> log) 
	{
		fileLogger.info("Inside Function : auditOprStop");
		try {
			new AuditHandler().auditLog(log, 3);
			
		} catch (Exception e) {
			//fileLogger.debug("********************************************************************************");
			fileLogger.error("Exception  Class : AuditHandler, Method : auditOprStop, MSG : " + e.getMessage());
			fileLogger.debug("********************auditOprStop stack trace***********************************");
			e.printStackTrace();
			//fileLogger.debug("********************************************************************************");
		}
		fileLogger.info("Exit Function : auditOprStop");
	}
	
	public void auditSysConfig(LinkedHashMap<String,String> log) 
	{
		fileLogger.info("Inside Function : auditSysConfig");
		try {
			new AuditHandler().auditLog(log, 12);
			
		} catch (Exception e) {
			//fileLogger.debug("********************************************************************************");
			fileLogger.error("Exception  Class : AuditHandler, Method : auditSysConfig, MSG : " + e.getMessage());
			fileLogger.debug("********************auditSysConfig stack trace***********************************");
			e.printStackTrace();
			//fileLogger.debug("********************************************************************************");
		}
		fileLogger.info("Exit Function : auditSysConfig");
	}
	
	public void auditUserManagement(LinkedHashMap<String,String> log) 
	{
		fileLogger.info("Inside Function : auditUserManagement");
		try {
			new AuditHandler().auditLog(log, 13);
			
		} catch (Exception e) {
			//fileLogger.debug("********************************************************************************");
			fileLogger.error("Exception  Class : AuditHandler, Method : auditUserManagement, MSG : " + e.getMessage());
			fileLogger.debug("********************auditUserManagement stack trace***********************************");
			e.printStackTrace();
			//fileLogger.debug("********************************************************************************");
		}
		fileLogger.info("Exit Function : auditUserManagement");
	}
	
	public void auditProfileAction(LinkedHashMap<String,String> log) 
	{
		fileLogger.info("Inside Function : auditProfileAction");
		try {
			new AuditHandler().auditLog(log, 14);
			
		} catch (Exception e) {
			//fileLogger.debug("********************************************************************************");
			fileLogger.error("Exception  Class : AuditHandler, Method : auditProfileAction, MSG : " + e.getMessage());
			fileLogger.debug("********************auditProfileAction stack trace***********************************");
			e.printStackTrace();
			//fileLogger.debug("********************************************************************************");
		}
		fileLogger.info("Exit Function : auditProfileAction");
	}
	
	public void auditBackup(LinkedHashMap<String,String> log) 
	{
		fileLogger.info("Inside Function : auditBackup");
		try {
			new AuditHandler().auditLog(log, 15);
			
		} catch (Exception e) {
			//fileLogger.debug("********************************************************************************");
			fileLogger.error("Exception  Class : AuditHandler, Method : auditBackup, MSG : " + e.getMessage());
			fileLogger.debug("********************auditBackup stack trace***********************************");
			e.printStackTrace();
			//fileLogger.debug("********************************************************************************");
		}
		fileLogger.info("Exit Function : auditBackup");
	}
	
	public void auditReportDownload(LinkedHashMap<String,String> log) 
	{
		fileLogger.info("Inside Function : auditReportDownload");
		try {
			new AuditHandler().auditLog(log, 16);
			
		} catch (Exception e) {
			//fileLogger.debug("********************************************************************************");
			fileLogger.error("Exception  Class : AuditHandler, Method : auditReportDownload, MSG : " + e.getMessage());
			fileLogger.debug("********************auditReportDownload stack trace***********************************");
			e.printStackTrace();
			//fileLogger.debug("********************************************************************************");
		}
		fileLogger.info("Exit Function : auditReportDownload");
	}
	
	public void auditDataReset(LinkedHashMap<String,String> log) 
	{
		fileLogger.info("Inside Function : auditDataReset");
		try {
			new AuditHandler().auditLog(log, 17);
			
		} catch (Exception e) {
			//fileLogger.debug("********************************************************************************");
			fileLogger.error("Exception  Class : AuditHandler, Method : auditDataReset, MSG : " + e.getMessage());
			fileLogger.debug("********************auditDataReset stack trace***********************************");
			e.printStackTrace();
			//fileLogger.debug("********************************************************************************");
		}
		fileLogger.info("Exit Function : auditDataReset");
	}
	
	public void auditAntennaProfile(LinkedHashMap<String,String> log) 
	{
		fileLogger.info("Inside Function : auditAntennaProfile");
		try {
			new AuditHandler().auditLog(log, 18);
			
		} catch (Exception e) {
			//fileLogger.debug("********************************************************************************");
			fileLogger.error("Exception  Class : AuditHandler, Method : auditAntennaProfile, MSG : " + e.getMessage());
			fileLogger.debug("********************auditAntennaProfile stack trace***********************************");
			e.printStackTrace();
			//fileLogger.debug("********************************************************************************");
		}
		fileLogger.info("Exit Function : auditAntennaProfile");
	}
	
	public void auditCellAction(LinkedHashMap<String,String> log) 
	{
		fileLogger.info("Inside Function : auditCellAction");
		try {
			new AuditHandler().auditLog(log, 19);
			
		} catch (Exception e) {
			//fileLogger.debug("********************************************************************************");
			fileLogger.error("Exception  Class : AuditHandler, Method : auditCellAction, MSG : " + e.getMessage());
			fileLogger.debug("********************auditCellAction stack trace***********************************");
			e.printStackTrace();
			//fileLogger.debug("********************************************************************************");
		}
		fileLogger.info("Exit Function : auditCellAction");
	}
	
	public void auditManualTransmission(LinkedHashMap<String,String> log) 
	{
		fileLogger.info("Inside Function : auditManualTransmission");
		try {
			new AuditHandler().auditLog(log, 20);
			
		} catch (Exception e) {
		//	fileLogger.debug("********************************************************************************");
			fileLogger.error("Exception  Class : AuditHandler, Method : audit_que, MSG : " + e.getMessage());
			fileLogger.debug("********************audit_que stack trace***********************************");
			e.printStackTrace();
			//fileLogger.debug("********************************************************************************");
		}
		fileLogger.info("Exit Function : auditManualTransmission");
	}
	
	public void auditFaultLog(LinkedHashMap<String,String> log) 
	{
		fileLogger.info("Inside Function : auditFaultLog");
		try {
			new AuditHandler().auditLog(log, 9);
			
		} catch (Exception e) {
			//fileLogger.debug("********************************************************************************");
			fileLogger.error("Exception  Class : AuditHandler, Method : auditFault, MSG : " + e.getMessage());
			fileLogger.debug("********************audit_que stack trace***********************************");
			e.printStackTrace();
			//fileLogger.debug("********************************************************************************");
		}
		fileLogger.info("Exit Function : auditFaultLog");
	}
	
	public void auditOprAction(LinkedHashMap<String,String> log) 
	{
		fileLogger.info("Inside Function : auditOprAction");
		try {
			new AuditHandler().auditLog(log, 3);
			
		} catch (Exception e) {
			//fileLogger.debug("********************************************************************************");
			fileLogger.error("Exception  Class : AuditHandler, Method : audit_que, MSG : " + e.getMessage());
			fileLogger.debug("********************audit_que stack trace***********************************");
			e.printStackTrace();
			//fileLogger.debug("********************************************************************************");
		}
		fileLogger.info("Exit Function : auditOprAction");
	}
	
}
