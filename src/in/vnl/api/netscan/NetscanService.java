package in.vnl.api.netscan;

import in.vnl.EventProcess.DBDataService;
import in.vnl.api.common.ApiCommon;
import in.vnl.api.common.CommonService;
import in.vnl.api.common.livescreens.AutoOperationServer;
import in.vnl.api.threeg.ThreegOperations;
import in.vnl.msgapp.Common;
import in.vnl.msgapp.Operations;
import in.vnl.scheduler.NetscanSchedulerListener;
import in.vnl.scheduler.NetscanSingletonExecutor;
import in.vnl.api.netscan.NetscanJob;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
//import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

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

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;



@Path("/netscan")
public class NetscanService 
{
   static Logger fileLogger = Logger.getLogger("file");
   
   @GET
   @Path("/testService")
   @Produces(MediaType.APPLICATION_JSON)
   public Response activateBP(){
	   
	   LinkedHashMap<String,String> rs = new LinkedHashMap<String,String>();
	   rs.put("result", "success");
	   rs.put("message", "ok workinng");
	   return Response.status(201).entity(rs).build();
   }
   
  /* @POST
   @Path("/btsdevicetype")
   @Produces(MediaType.APPLICATION_JSON)
   public Response getbtsdevicetype()
   {	
	   	String a = new ApiCommon().getSufiConfigurationWithDefaultValues(1);
	    String query = "select * from deviceType where d_status = 'A' and code = 3";
		fileLogger.debug(query);
		JSONArray rs =  new Operations().getJson(query);
        return Response.status(201).entity(rs.toString()).build();
   }*/
   
   
   @POST
   @Path("/addScanner")
   @Produces(MediaType.APPLICATION_JSON)
   public Response addBts(@Context HttpServletRequest req,@FormParam("btsIp") String btsIp, @FormParam("btsType") String btsType,@FormParam("dtype") String dtype)
   {
	    	fileLogger.info("Inside Function : addBts"); 
	   		fileLogger.debug("all data is :"+btsIp+" "+btsType);
			Common co = new Common();			
			String query = "insert into btsmaster(ip,typeid,devicetypeid) values('"+btsIp+"','"+btsType+"',"+dtype+")";
			co.executeDLOperation(query);
			HashMap<String,String> rs = new HashMap<String,String>();
			rs.put("result", "sucsses");
	    	fileLogger.info("Exit Function : addBts"); 
            return Response.status(201).entity(rs).build();
    }
   
   @POST
   @Path("/removeScanner")
   @Produces(MediaType.APPLICATION_JSON)
   public Response removeBts(@Context HttpServletRequest req,@FormParam("btsIp") String btsIp, @FormParam("id") String id)
   {
	   	String query = "delete from btsmaster  where ip = '"+btsIp+"' and b_id = "+id;
		Common co = new Common();
		boolean result =  co.executeDLOperation(query);
		String rs = result == true?"{\"result\":\"success\"}":"{\"result\":\"fail\"}";	
        return Response.status(201).entity(rs).build();
    }
   
   @POST
   @Path("/scanerInfo")
   @Produces(MediaType.APPLICATION_JSON)
   public Response getBTS()
   {	

    	fileLogger.info("Inside Function : getBTS"); 
	   //String query = "select  btsmaster.b_id,btsmaster.ip,btsmaster.status,btsnetworktype.name from btsmaster inner join btsnetworktype on(typeid = n_id)";
	   String query = "select  * from view_btsinfo where code = 3";
		fileLogger.debug(query);
		JSONArray rs =  new Operations().getJson(query);
    	fileLogger.info("Exit Function : getBTS"); 
        return Response.status(201).entity(rs.toString()).build();
    }
   
   
   @POST
   @Path("/getcellsreport")
   @Produces(MediaType.APPLICATION_JSON)
   public Response getCellsReport(@Context HttpServletRequest req)
   {
    	fileLogger.info("Inside Function : getCellsReport"); 
	   //String query = "select  btsmaster.b_id,btsmaster.ip,btsmaster.status,btsnetworktype.name from btsmaster inner join btsnetworktype on(typeid = n_id)";
        HttpSession session=req.getSession(false);
		String time = session.getAttribute("startTime").toString(); 
	    //String query = "select * from view_netscan_cell_scan_report where rpt_timestamp between "+co.convertToMilliSec(startTime)+" and "+co.convertToMilliSec(endTime)+" order by id desc" ;
		String query = "select oprlogs_current.*,tech_type_master.tech_id from oprlogs_current inner join tech_type_master on (oprlogs_current.packet_type = tech_type_master.tech_name) where inserttime >='"+time+"' order by inserttime desc;";
	    JSONArray rs =  new Operations().getJson(query);
		fileLogger.info("Exit Function : getCellsReport"); 
        return Response.status(201).entity(rs.toString()).build();
    }
   
	@POST
	@Path("/getcdrdataforsibinfo")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCdrDataForSibInfo(@Context HttpServletRequest req) { // String
																			// query
																			// =
																			// "select
																			// btsmaster.b_id,btsmaster.ip,btsmaster.status,btsnetworktype.name
																			// from
																			// btsmaster
																			// inner
																			// join
																			// btsnetworktype
																			// on(typeid
																			// =
																			// n_id)";
		HttpSession session = req.getSession(false);
		String time = session.getAttribute("startTime").toString();
		// String query = "select * from view_netscan_cell_scan_report where
		// tech_id in(select neighbour_tech_id from neighbour_tech_type_mapping
		// where tech_id in (select tech_id from tech_type_master where
		// tech_name='"+techName+"')) and rpt_timestamp between
		// "+co.convertToMilliSec(startTime)+" and
		// "+co.convertToMilliSec(endTime)+" order by id desc" ;
		String query = "select oprlogs_current.*,tech_type_master.tech_id from oprlogs_current inner join tech_type_master on (oprlogs_current.packet_type = tech_type_master.tech_name) where (mcc::numeric>0 and mnc::numeric>0 and lac::numeric>0 and cell::numeric>0 and psc::numeric>0 and uarfcn::numeric>0) and (mcc is not null and mnc is not null and lac is not null and cell is not null and psc is not null and uarfcn is not null) and packet_type in ('UMTS','Loc_3g') and inserttime >='"
				+ time
				+"' union select oprlogs_current.*,tech_type_master.tech_id from oprlogs_current inner join tech_type_master on (oprlogs_current.packet_type = tech_type_master.tech_name) where (mcc::numeric>0 and mnc::numeric>0 and tac::numeric>0 and cell::numeric>0 and pci::numeric>0 and earfcn::numeric>0) and (mcc is not null and mnc is not null and tac is not null and cell is not null and pci is not null and earfcn is not null) and packet_type in ('LTE','Loc_4g') and inserttime >='"
				+ time
				+ "' union select oprlogs_current.*,tech_type_master.tech_id from oprlogs_current inner join tech_type_master on (oprlogs_current.packet_type = tech_type_master.tech_name) where (mcc::numeric>0 and mnc::numeric>0 and lac::numeric>0 and cell::numeric>0 and arfcn::numeric>0) and (mcc is not null and mnc is not null and lac is not null and cell is not null and arfcn is not null) and packet_type in ('GSM','Loc_2g') and inserttime >='"
				+ time
				+ "'  union select *,9999 from oprlogs_current where packet_type in ('Loc_3g') and inserttime >='"
				+ time
				+ "'  union select *,9999 from oprlogs_current where packet_type in ('Loc_4g') and inserttime >='"
				+ time + "' union select *,9999 from oprlogs_current where packet_type in ('Loc_2g') and inserttime >='"
				+ time + "' order by inserttime desc;";
		JSONArray rs = new Operations().getJson(query);
		return Response.status(201).entity(rs.toString()).build();
	}
   
   @POST
   @Path("/alarms")
   @Produces(MediaType.APPLICATION_JSON)
   public Response alarms(@FormParam("startTime") String startTime ,@FormParam("endTime") String endTime)
   {	
		fileLogger.info("Inside Function : alarms"); 
	   //String query = "select  btsmaster.b_id,btsmaster.ip,btsmaster.status,btsnetworktype.name from btsmaster inner join btsnetworktype on(typeid = n_id)";
	   Common co = new Common();
	   String query = "select  * from net_scan_alarms where tstmp between "+co.convertToMilliSec(startTime)+" and "+co.convertToMilliSec(endTime)+" order by tstmp desc";
		fileLogger.debug(query);
		JSONArray rs =  new Operations().getJson(query);
		fileLogger.info("Exit Function : alarms"); 
        return Response.status(201).entity(rs.toString()).build();
    }
   
   
   @POST
   @Path("/getfreqsreport")
   @Produces(MediaType.APPLICATION_JSON)
   public Response getfreqsreport(@FormParam("startTime") String startTime ,@FormParam("endTime") String endTime)
   {	//String query = "select  btsmaster.b_id,btsmaster.ip,btsmaster.status,btsnetworktype.name from btsmaster inner join btsnetworktype on(typeid = n_id)";
	   Common co = new Common();
	   String query = "select * from view_netscan_freq_scan_report where rpt_timestamp between "+co.convertToMilliSec(startTime)+" and "+co.convertToMilliSec(endTime)+" order by id desc" ;
	   //fileLogger.debug(query);
		JSONArray rs =  new Operations().getJson(query);
        return Response.status(201).entity(rs.toString()).build();
    }
   
   @POST
   @Path("/networkScannerDetailInfo")
   @Produces(MediaType.APPLICATION_JSON)
   public Response getNetworkScannerDetailInfo(@Context HttpServletRequest req,@FormParam("ip") String ip, @FormParam("id") String id)
   {	 
		fileLogger.info("Inside Function : getNetworkScannerDetailInfo"); 
	   //String query = "select  btsmaster.b_id,btsmaster.ip,btsmaster.status,btsnetworktype.name from btsmaster inner join btsnetworktype on(typeid = n_id)";
	   String query = "select  * from view_netscan_detail_status where b_id = "+id;
		fileLogger.debug(query);
		JSONArray rs =  new Operations().getJson(query);
		fileLogger.info("Exit Function : getNetworkScannerDetailInfo"); 
        return Response.status(201).entity(rs.toString()).build();
    }
   
   @POST
   @Path("/clientopr")
   @Produces(MediaType.APPLICATION_JSON)
   public Response reciveFromClient(@FormParam("cmdType") String cmdType,@FormParam("systemCode") String systemCode,@FormParam("systemId") String systemId,@FormParam("systemIp") String systemIp,@FormParam("id") String id,@Context HttpServletRequest req,@FormParam("data") String data)
   {   
	   LinkedHashMap<String,String> param = new LinkedHashMap<String,String>();
	   param.put("cmdType", cmdType);
	   param.put("systemCode", systemCode);
	   param.put("systemIP", systemIp);
	   param.put("systemId", systemId);
	   param.put("id", id);
	   param.put("data", data);
	   return new NetscanOperations().executeActions(param);
   }
   
   
   @POST
   @Path("/comopr")
   @Produces(MediaType.APPLICATION_JSON)
   public Response reciveFromClientForDirectCall(@FormParam("cmdType") String cmdType,@FormParam("systemCode") String systemCode,@FormParam("systemId") String systemId,@FormParam("systemIp") String systemIp,@FormParam("id") String id,@Context HttpServletRequest req,@FormParam("data") String data)
   {   
	   LinkedHashMap<String,String> param = new LinkedHashMap<String,String>();
	   param.put("cmdType", cmdType);
	   param.put("systemCode", systemCode);
	   param.put("systemIP", systemIp);
	   param.put("systemId", systemId);
	   param.put("id", id);
	   param.put("data", data);
	   return new NetscanOperations().sendToNetscanServer(param);
   }
   
   @POST
   @Path("/opr")
   @Produces(MediaType.APPLICATION_JSON)
   public Response reciveFromDevice(@QueryParam("CMD_TYPE") String cmdType,@QueryParam("SYSTEM_CODE") String systemCode,@QueryParam("SYSTEM_ID") String systemId,@Context HttpServletRequest req,String data)
   {   
		fileLogger.info("Inside Function : reciveFromDevice"); 
	   LinkedHashMap<String,String> param = new LinkedHashMap<String,String>();
	   param.put("cmdType", cmdType);
	   param.put("systemCode", systemCode);
	   param.put("systemId", systemId);
	   param.put("data", data);
	   //  fileLogger.debug("*********************************************opr data************************8");
	   fileLogger.debug(cmdType);
	   fileLogger.debug(systemCode);
	   fileLogger.debug(systemId);
	   fileLogger.debug(data);
	 //  fileLogger.debug("*********************************************opr data************************8")
		fileLogger.info("Exit Function : reciveFromDevice"); 
	   return new NetscanOperations().executeActions(param);
   }
   
   
   @POST
   @Path("/neighbours")
   @Produces(MediaType.APPLICATION_JSON)
   public Response getNeighboursForCell(@FormParam("index") String index,@FormParam("id") String id)
   {   
		fileLogger.info("Inside Function : getNeighboursForCell"); 
	   String query = "select rpt_data#>'{REPORT,"+index+",NEIGHBORS}' as neigh from netscan_cell_scan_report where id ="+id;
		fileLogger.debug(query);
		JSONArray rs =  new Operations().getJson(query);
		fileLogger.info("Exit Function : getNeighboursForCell"); 
       return Response.status(201).entity(rs.toString()).build();
	   //return new NetscanOperations().executeActions(param);
   }
   
   @POST
   @Path("/cellscanreports")
   @Produces(MediaType.APPLICATION_JSON)
   public Response cellscanreports(@FormParam("startTime") String startTime ,@FormParam("endTime") String endTime)
   {
		fileLogger.info("Inside Function : cellscanreports"); 
	   //String query = "select  btsmaster.b_id,btsmaster.ip,btsmaster.status,btsnetworktype.name from btsmaster inner join btsnetworktype on(typeid = n_id)";
	   Common co = new Common();
	   String query = "select  * from netscan_cell_scan_report where rpt_timestamp between "+co.convertToMilliSec(startTime)+" and "+co.convertToMilliSec(endTime)+" order by rpt_timestamp desc";
		fileLogger.debug(query);
		JSONArray rs =  new Operations().getJson(query);
		fileLogger.info("Exit Function : cellscanreports"); 
        return Response.status(201).entity(rs.toString()).build();
   }
   
   /*@POST
   @Path("/getumtsdata")
   @Produces(MediaType.APPLICATION_JSON)
   public Response getumtsdata(@Context HttpServletRequest req,@FormParam("ip") String ip,@FormParam("techName") String techName ,@FormParam("startTime") String startTime ,@FormParam("endTime") String endTime)
   {	//String query = "select  btsmaster.b_id,btsmaster.ip,btsmaster.status,btsnetworktype.name from btsmaster inner join btsnetworktype on(typeid = n_id)";
	   	/*Common co = new Common();
	   	String query = "select  * from view_netscan_cell_scan_report where rpt_timestamp between "+co.convertToMilliSec(startTime)+" and "+co.convertToMilliSec(endTime)+"  and btsip='"+ip+"' and tech_id in(select neighbour_tech_id from neighbour_tech_type_mapping where tech_id in (select tech_id from tech_type_master where tech_name='"+techName+"')) order by rpt_timestamp desc";
		fileLogger.debug(query);
		JSONArray rs =  new Operations().getJson(query);*/
		
       /* HttpSession session=req.getSession(false);
		fileLogger.debug("the selected nib ip");
		String time = session.getAttribute("startTime").toString();
		String query = "select oprlogs_current.*,tech_type_master.tech_id from oprlogs_current inner join tech_type_master on (oprlogs_current.packet_type = tech_type_master.tech_name) where inserttime >='"+time+"' and packet_type = '"+techName+"' order by inserttime desc;";
		JSONArray rs =  new Operations().getJson(query);
		return Response.status(201).entity(rs.toString()).build();  
   }*/
   
   @POST
   @Path("/getumtsdata")
   @Produces(MediaType.APPLICATION_JSON)
   public Response getumtsdata(@Context HttpServletRequest req,@FormParam("techName") String techName)
   {	
        HttpSession session=req.getSession(false);
		String time = session.getAttribute("startTime").toString();
		String query = "select oprlogs_current.*,tech_type_master.tech_id from oprlogs_current inner join tech_type_master on (oprlogs_current.packet_type = tech_type_master.tech_name) where (mcc::numeric>0 and mnc::numeric>0 and lac::numeric>0 and cell::numeric>0 and psc::numeric>0 and uarfcn::numeric>0) and (mcc is not null and mnc is not null and lac is not null and cell is not null and psc is not null and uarfcn is not null) and inserttime >='"+time+"' and packet_type in ('UMTS','Loc_3g') union select *,9999 from oprlogs_current where packet_type in ('Loc_3g') and inserttime >='"+time+"' order by inserttime desc;";
		JSONArray rs =  new Operations().getJson(query);
		return Response.status(201).entity(rs.toString()).build();  
   }
   
   @POST
   @Path("/stopscanning")
   @Produces(MediaType.APPLICATION_JSON)
   public Response stopScanning(@Context HttpServletRequest req)
   {	
		fileLogger.info("Inside Function : stopScanning"); 
	  //  fileLogger.debug("in stopScanning");
	    HashMap<String,String> rs = new HashMap<String,String>();
		try {
	    DBDataService dbDataService = DBDataService.getInstance();
	    if(NetscanSchedulerListener.scheduler!=null){
	    	//set interrupted flag to true
	    	//stop the running scanning
	    	NetscanSchedulerListener netscanSchedulerListener = new NetscanSchedulerListener();
	    	netscanSchedulerListener.stopNetscanScheduler();
			//stop the scanner scheduler
	    	fileLogger.info("Inside Function : stopScanning stopNetscanScheduler has been called");
			dbDataService.shutdownScannerScheduler();
			fileLogger.info("Inside Function : stopScanning shutdownScannerScheduler has been called");
			rs.put("result", "success");
			dbDataService.scannerManuallyStopped=true;
			fileLogger.info("Inside Function : stopScanning scannerManuallyStopped set to true");
	    }else{
	    	rs.put("result", "fail");
	    	rs.put("message","Scanner already in stopped state");
	    }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rs.put("result", "fail");
			rs.put("message", "Error occurs");
		}
		fileLogger.info("Exit Function : stopScanning");
		//fileLogger.debug("in stopScanning");
       return Response.status(201).entity(rs).build();
   }
   
   @POST
   @Path("/startscanning")
   @Produces(MediaType.APPLICATION_JSON)
   public Response startScanning(@Context HttpServletRequest req)
   {	
	   
//	   	ApiCommon apiCommmonObj= new ApiCommon();
//		String respData=apiCommmonObj.switchDsp("gsm");
//		long sysManWait=4000l;
//		try{
//			sysManWait=Long.parseLong(DBDataService.configParamMap.get("sysmanwait"));
//			fileLogger.debug("@sysManWait is :"+sysManWait);
//		}catch(Exception e){
//			fileLogger.error("exception in getting sysManWait message :"+sysManWait);
//		}
//		
//		
//		
//		
//		if(respData.equals("")){
//			new AutoOperationServer()
//			.sendText("{\"result\":\"fail\",\"msg\":\"System Manager Switching failed:GSM\"}");
//			return false;
//		}else{
//			fileLogger.debug("@sleep about to sleep in GSM System Manager Switching");
//			Thread.sleep(sysManWait);
//		}
//		
//		
//		
//		
//		
//
//		respData=apiCommmonObj.switchDsp("sufi");
//		if(respData.equals("")){
//			new AutoOperationServer().sendText("{\"result\":\"fail\",\"msg\":\"System Manager Switching failed:UMTS\"}");
//			return false;
//		}else{
//			fileLogger.debug("@sleep about to sleep in UMTS System Manager Switching");
//			Thread.sleep(sysManWait);
//		}
		   Operations operations=new Operations();
		   fileLogger.info("Inside Function : startscanning");
		   NetscanOperations netscanOperations=new NetscanOperations();

	   	   String fetchDeviceQuery="select * from view_btsinfo where code in(10) limit 1;";
	       JSONArray devicesInfoArr=operations.getJson(fetchDeviceQuery);
	       JSONObject obj= new JSONObject();
	       int status=1;
		try {
			obj = devicesInfoArr.getJSONObject(0);
			status = obj.getInt("statuscode");
			if(status!=1)
			{ 
				new NetscanJob().respawnScanner(netscanOperations);
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	 		HashMap<String,String> rs = new HashMap<String,String>();
	       try {
		   if(NetscanSchedulerListener.scheduler==null){
			   NetscanSchedulerListener netscanSchedulerListener = new NetscanSchedulerListener();
			   
			   
			   
			   netscanSchedulerListener.startNetscanScheduler();
			   rs.put("result", "success");
			   DBDataService.scannerManuallyStopped=false;
		   }else{
			   rs.put("result", "fail");
			   rs.put("message", "Scanner already in started state");
		   }
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		rs.put("result", "fail");
		rs.put("result", "Error occurs");
	}
	   fileLogger.info("Exit Function : stopScanning");
	
		return Response.status(201).entity(rs).build(); 
   }
   
   @POST
   @Path("/restartScanning")
   @Produces(MediaType.APPLICATION_JSON)
   public Response restartScanning(@Context HttpServletRequest req)
   {	
	   fileLogger.info("Inside Function : restartScanning-1");
		HashMap<String,String> rs = new HashMap<String,String>();
		rs.put("result", "success");
		fileLogger.info("Exit Function : restartScanning-2");
		return Response.status(201).entity(rs).build();  
   }
   
   	@POST
	@Path("/getltedata")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getltesdata(@Context HttpServletRequest req, @FormParam("techName") String techName) {
   	 // fileLogger.info("Inside Function : getltesdata");
   				String query = "select oprlogs_current.*,tech_type_master.tech_id from oprlogs_current inner join tech_type_master on (oprlogs_current.packet_type = tech_type_master.tech_name) where (mcc::numeric>0 and mnc::numeric>0 and tac::numeric>0 and cell::numeric>0 and pci::numeric>0 and earfcn::numeric>0) and (mcc is not null and mnc is not null and tac is not null and cell is not null and pci is not null and earfcn is not null) and" 
                       +" inserttime >= (timezone('utc'::text, now()) - '24:00:00'::interval) AND inserttime <= timezone('utc'::text, now())"
                       +" and packet_type in ('LTE','Loc_4g') union select *,9999 from oprlogs_current where packet_type in ('Loc_4g')and" 
                       +" inserttime >= (timezone('utc'::text, now()) - '24:00:00'::interval) AND inserttime <= timezone('utc'::text, now()) order by inserttime desc;";
		JSONArray rs = new Operations().getJson(query);
		//  fileLogger.info("Exit Function : getltesdata");
		return Response.status(201).entity(rs.toString()).build();
	}
}