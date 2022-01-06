package in.vnl.api.common;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
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

import in.vnl.EventProcess.DBDataService;
import in.vnl.api.common.ApiCommon;
import in.vnl.api.threeg.ThreegOperations;
import in.vnl.api.twog.TwogOperations;
import in.vnl.api.twog.livescreens.TriggerCueServer;
import in.vnl.msgapp.Common;
import in.vnl.msgapp.GPSSocketServer;
import in.vnl.msgapp.GeoSchedulerServer;
import in.vnl.msgapp.OffsetSocketServer;
import in.vnl.msgapp.Operations;
import in.vnl.scheduler.NetscanSingletonExecutor;
import in.vnl.sockets.UdpServerClient;
import in.vnl.api.common.livescreens.AutoOperationServer;
import in.vnl.api.common.livescreens.AutoStateServer;
import in.vnl.api.config.PossibleConfigurations;
import in.vnl.api.netscan.CurrentNetscanAlarm;
import in.vnl.api.netscan.NetscanOperations;
import in.vnl.api.netscan.NetscanTask;
import in.vnl.report.ReportServer;

@Path("/trgl")
public class TrglTrigger 
{
	static Logger fileLogger = Logger.getLogger("file");
	static String  oprStartTime=null;
	
   @POST
   @Path("/getAllOperations")
   @Produces(MediaType.APPLICATION_JSON)
   public Response getAllOperations()
   {	
	   fileLogger.info("Inside Function : getAllOperations");
	   String query = "select id,name,date_trunc('second',inserttime + '05:30:00'::interval) inserttime,note,opr_type,gsm_bands,umts_bands,lte_bands,duration,status,date_trunc('second',stoptime + '05:30:00'::interval) stoptime from oprrationdata order by id desc";
		fileLogger.debug(query);
		JSONArray rs =  new Operations().getJson(query);
		  fileLogger.info("Exit Function : getAllOperations");
        return Response.status(201).entity(rs.toString()).build();
    }
   
   @POST
   @Path("/getallsystemusers")
   @Produces(MediaType.APPLICATION_JSON)
   public Response getAllSystemUsers()
   {
	   fileLogger.info("Inside Function : getAllSystemUsers");
	   String query = "select id,user_name from oprrationdata";
		fileLogger.debug(query);
		JSONArray rs =  new Operations().getJson(query);
		fileLogger.info("Exit Function : getAllSystemUsers");
        return Response.status(201).entity(rs.toString()).build();
    }
   
   @POST
   @Path("/getoperators")
   @Produces(MediaType.APPLICATION_JSON)
   public Response getOperators()
   {
	   fileLogger.info("Inside Function : getOperators");
	   String query = "SELECT opr, string_agg(plmn::text, ', ') AS plmn_list FROM plmn_opr where substring(plmn::text,1,3) in('404','405','130') or plmn in(33402,33420) GROUP  BY opr";
		fileLogger.debug(query);
		JSONArray rs =  new Operations().getJson(query);
		  fileLogger.info("Exit Function : getOperators");
        return Response.status(201).entity(rs.toString()).build();
    }
   
  
   
  
   
   public String getScanJsonInOrder(JSONObject scanDataObj,String scanType){
		  fileLogger.info("Inside Function : getScanJsonInOrder");
	   String jsonStringInOrder="";
	   try{
	   if(scanType.equals("start")){
		   jsonStringInOrder+="{\"CMD_CODE\":\"START_EXHAUSTIVE_SCAN\",\"RSSI_THRESHOLD\":"+scanDataObj.getString("RSSI_THRESHOLD")+",\"REPETITION_FLAG\":"+scanDataObj.getString("REPETITION_FLAG")+",\"REPITITION_FREQ\":"+scanDataObj.getString("REPITITION_FREQ")+",\"SCAN_LIST\":"+scanDataObj.getJSONArray("SCAN_LIST").toString()+"}";
	   }else{
		   
	   }
	   }catch(Exception E){
		   fileLogger.error("Exception");
	   }
	   fileLogger.info("Exit Function : getScanJsonInOrder");
       return jsonStringInOrder;
   }

   
   public JSONObject getBandWiseJson(String oprGsmBand,String tech){
	   fileLogger.info("Inside Function : getScanJsonInOrder");
	   String[] bandArr=oprGsmBand.split(",");
	   JSONObject tempBandObj=new JSONObject();
	   
	   try {
		tempBandObj.put("TECH",tech);
		   JSONArray tempBandArr=new JSONArray();
		   for(String band:bandArr)
			{
				JSONObject bandObj = new JSONObject();
				bandObj.put("BAND",band);
				tempBandArr.put(bandObj);
			}
		   tempBandObj.put("BAND_LIST",tempBandArr);
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		fileLogger.error("JSON Exception in getBandWiseJson in CommonService");
		e.printStackTrace();
	}
	   fileLogger.info("Exit Function : getScanJsonInOrder");
	return tempBandObj;
   }


   @POST
   @Path("/addGPSAccurecyNumber")
   @Produces(MediaType.APPLICATION_JSON)
   public Response addGPSAccurecyNumber(@Context HttpServletRequest req,@FormParam("accuracy") String accuracy)
   {
	        fileLogger.info("Inside Function : addGPSAccurecyNumber"); 
	   		fileLogger.debug("all data is :"+accuracy+" "+accuracy);
			Common co = new Common();			
			String query = "insert into gps_accuracy(accuracy) values("+accuracy+")";
			boolean status=co.executeDLOperation(query);
			HashMap<String,String> rs = new HashMap<String,String>();
			try{
			if(status){
			rs.put("result", "success");
			}else{
			rs.put("result", "fail");
			}
			}catch(Exception E){
			rs.put("result", "fail");
			fileLogger.error("Exception: "+E.getMessage());
			}
			fileLogger.info("Exit Function : addGPSAccurecyNumber"); 
            return Response.status(201).entity(rs).build();
    }



	
   /*
    * This Method will get all the bts present in to the btsmaster info
    * */
   @POST
   @Path("/getgpsaccuracy")
   @Produces(MediaType.APPLICATION_JSON)
   public Response getGpsAccuracy()
   {	
	   fileLogger.info("Inside Function : getGpsAccuracy"); 
	   //String query = "select  btsmaster.b_id,btsmaster.ip,btsmaster.status,btsnetworktype.name from btsmaster inner join btsnetworktype on(typeid = n_id)";
	   String query = "select  * from gps_accuracy order by id desc limit 1";
		fileLogger.debug(query);
		JSONArray rs =  new Operations().getJson(query);
		 fileLogger.info("Exit Function : getGpsAccuracy"); 
        return Response.status(201).entity(rs.toString()).build();
    }

	
   /*
    * This Method will get all the bts present in to the btsmaster info
    * */
   @POST
   @Path("/btsinfo")
   @Produces(MediaType.APPLICATION_JSON)
   public Response getBTS()
   {	
	   fileLogger.info("Inside Function : getBTS"); 
	   //String query = "select  btsmaster.b_id,btsmaster.ip,btsmaster.status,btsnetworktype.name from btsmaster inner join btsnetworktype on(typeid = n_id)";
	   //String query = "select  * from view_btsinfo order by grp_id,code";
	   String query="SELECT vb.*,case when code in(0,1,2) then oprname(concat(config -> \'SYS_PARAMS\'->\'CELL_INFO\'->\'PLMN_ID\' ->>\'MCC\'," +
	   				"config -> \'SYS_PARAMS\'->\'CELL_INFO\'->\'PLMN_ID\' ->>\'MNC\')::numeric) when code=\'5\' then oprname(ncd.plmn::numeric) end  oprname" +
	   				" from view_btsinfo vb left join n_cells_data ncd on vb.ip=ncd.ip where type=\'S\' or type is null  order by grp_id,code";
		fileLogger.debug(query);
		JSONArray rs =  new Operations().getJson(query);
		   fileLogger.info("Exit Function : getBTS"); 
        return Response.status(201).entity(rs.toString()).build();
    }
   
   /*
    * This Method will get all the 3G bts present in to the btsmaster info
    * */
   @POST
   @Path("/getall3gdevicelist")
   @Produces(MediaType.APPLICATION_JSON)
   public Response getAll3gDeviceList()
   {	
	   fileLogger.info("Inside Function : getAll3gDeviceList"); 
	   //String query = "select  btsmaster.b_id,btsmaster.ip,btsmaster.status,btsnetworktype.name from btsmaster inner join btsnetworktype on(typeid = n_id)";
	   String query = "select  * from view_btsinfo where lower(use_type_name)='3g locator' order by grp_id,code";
		fileLogger.debug(query);
		JSONArray rs =  new Operations().getJson(query);
		  fileLogger.info("Exit Function : getAll3gDeviceList"); 
        return Response.status(201).entity(rs.toString()).build();
    }
   
   /*
    * This Method will get all the bts present in to the btsmaster info
    * */
   @POST
   @Path("/getHwCapabilityTypes")
   @Produces(MediaType.APPLICATION_JSON)
   public Response getHwCapabilityTypes()
   {	
	   fileLogger.info("Inside Function : getHwCapabilityTypes"); 
	   //String query = "select  btsmaster.b_id,btsmaster.ip,btsmaster.status,btsnetworktype.name from btsmaster inner join btsnetworktype on(typeid = n_id)";
	   String query = "select  id,name from hw_capability where status=TRUE order by name";
		fileLogger.debug(query);
		JSONArray rs =  new Operations().getJson(query);
		   fileLogger.info("Exit Function : getHwCapabilityTypes"); 
        return Response.status(201).entity(rs.toString()).build();
    }
   
   /*
    * This Method will get all the bts present in to the btsmaster info
    * */
   @POST
   @Path("/getUseTypes")
   @Produces(MediaType.APPLICATION_JSON)
   public Response getUseTypes()
   {	
	   fileLogger.info("Inside Function : getUseTypes"); 
	   //String query = "select  btsmaster.b_id,btsmaster.ip,btsmaster.status,btsnetworktype.name from btsmaster inner join btsnetworktype on(typeid = n_id)";
	   String query = "select id,name,type,modal_id from use_type where status=TRUE";
		fileLogger.debug(query);
		JSONArray rs =  new Operations().getJson(query);
		   fileLogger.info("Exit Function : getUseTypes");
        return Response.status(201).entity(rs.toString()).build();
    }
   
   /*
    * This Method will get all the bts present in to the btsmaster info
    * */
   @POST
   @Path("/getantennatypes")
   @Produces(MediaType.APPLICATION_JSON)
   public Response getAntennaTypes()
   {	
	   fileLogger.info("Inside Function : getAntennaTypes");
	   //String query = "select  btsmaster.b_id,btsmaster.ip,btsmaster.status,btsnetworktype.name from btsmaster inner join btsnetworktype on(typeid = n_id)";
	   String query = "select  * from antenna_type where type_id=4";
		fileLogger.debug(query);
		JSONArray rs =  new Operations().getJson(query);
		  fileLogger.info("Exit Function : getAntennaTypes");
        return Response.status(201).entity(rs.toString()).build();
    }
   
   @POST
   @Path("/getAntennaProfiles")
   @Produces(MediaType.APPLICATION_JSON)
   public Response getAntennaProfiles()
   {	
	   fileLogger.info("Inside Function : getAntennaProfiles");
	   //String query = "select  btsmaster.b_id,btsmaster.ip,btsmaster.status,btsnetworktype.name from btsmaster inner join btsnetworktype on(typeid = n_id)";
	   String query = "select * from antenna a join antenna_type at on a.atype::integer=at.type_id order by a.profile_name";
		fileLogger.debug(query);
		JSONArray rs =  new Operations().getJson(query);
		   fileLogger.info("Exit Function : getAntennaProfiles");
        return Response.status(201).entity(rs.toString()).build();
    }
   
   @POST
   @Path("/checkoperation")
   @Produces(MediaType.APPLICATION_JSON)
   public Response checkOperation(@Context HttpServletRequest req)
   {
	   
			String query="select count(*) count from operations where status!='1'";
			int count=-1;
			HashMap<String,String> rs = new HashMap<String,String>();
			new CommonOperation().updateScanningAndTrackingAntennaInDevices();
			try {
				count=new Operations().getJson(query).getJSONObject(0).getInt("count");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(count==0){
				rs.put("result","success");
			}else{
				rs.put("result","failure");
			}
			return Response.status(201).entity(rs).build();
    }
   
   @POST
   @Path("/validateOperation")
   @Produces(MediaType.APPLICATION_JSON)
   public Response validateOperation(@Context HttpServletRequest req,@FormParam("oprName") String oprName, @FormParam("oprType") String oprType, @FormParam("old_data") String old_data)
   {	   
	   
	   
	   String res = new OperationCalculations().validateOpertaion(oprType, old_data, oprName);
	   
	   return Response.status(201).entity(res).build();
   }
   
   @POST
   @Path("/addAntennaProfile")
   @Produces(MediaType.APPLICATION_JSON)
   public Response addAntennaProfile(@Context HttpServletRequest req,@FormParam("antennaScanning") boolean antennaScanning,@FormParam("antennaTracking") boolean antennaTracking,@FormParam("antennaName") String antennaName, @FormParam("antennaTxPower") String antennaTxPower,@FormParam("antennaType") String antennaType, @FormParam("antennaBand") String antennaBand,@FormParam("antennaGain") String antennaGain, @FormParam("antennaElevation") String antennaElevation,@FormParam("antennaHBW") String antennaHBW, @FormParam("antennaVBW") String antennaVBW,@FormParam("antennaTilt") String antennaTilt, @FormParam("antennaAzimuth") String antennaAzimuth,@FormParam("antennaTerrain") String antennaTerrain)
   {
			Common co = new Common();
			HashMap<String,String> rs = new HashMap<String,String>();
			String query = "INSERT INTO antenna(profile_name, txpower, atype, band, gain, elevation, hbw, vbw, tilt, azimuth, terrain,inscanning,intracking) VALUES('"+antennaName+"','"+antennaTxPower+"','"+antennaType+"','"+antennaBand+"','"+antennaGain+"','"+antennaElevation+"','"+antennaHBW+"','"+antennaVBW+"','"+antennaTilt+"','"+antennaAzimuth+"','"+antennaTerrain+"',"+antennaScanning+","+antennaTracking+") returning id";
				int id=co.executeQueryAndReturnId(query);
				rs.put("result", "success");
				rs.put("id",Integer.toString(id));
				new CommonOperation().updateScanningAndTrackingAntennaInDevices();
            return Response.status(201).entity(rs).build();
    }
		   
		   public static ArrayList<JSONObject> getSortedList(JSONArray array) {
			   fileLogger.info("Inside Function : getSortedList");
	            ArrayList<JSONObject> list = new ArrayList<JSONObject>();
	            try{
	            for (int i = 0; i < array.length(); i++) {
	                    list.add(array.getJSONObject(i));
	            }
	            Collections.sort( list, new Comparator<JSONObject>() {

	                public int compare(JSONObject a, JSONObject b) {
                         int plmnA=0;
                         int plmnB=0;

	                    try {
	                    	plmnA=a.getInt("plmn");
	                    	plmnB=b.getInt("plmn");
	                    } 
	                    catch (JSONException e) {
	                        //do something
	                    }
	                    if(plmnA==plmnB) return 0;
	                    else if(plmnA>plmnB) return 1;
	                    else return -1;
	                }
	            });
	            }catch(Exception E){
	            	fileLogger.error("Exception occurs in getSortedList method");
	            }
	            fileLogger.info("Exit Function : getSortedList");
	            return list;
	}
		   
		   
		   @GET
		   @Path("/getcurrentcdrdata")
		   @Produces(MediaType.APPLICATION_JSON)
		   public Response getCurrentCdrData(@Context HttpServletRequest req,@QueryParam("size") String size,@QueryParam("searchKey") String searchKey)
		   {	   
			   //String query = "select distinct cdrlogs_current.*,mobiletype.mobile_type from cdrlogs_current left join mobiletype on (cdrlogs_current.imsi = mobiletype.imsi::character varying) where oprid = (select max(id) from oprrationdata) order by inserttime;";
			   HttpSession ss = req.getSession();
			   String queryFilter = "";
			   String sizeFilter="";
			   if(ss.getAttribute("mobtime") != null) 
			   {
				   String mobtime = ss.getAttribute("mobtime").toString();
				   queryFilter = " and inserttime >= '"+mobtime+"'";
			   }
			   String searchFilter="";
			   if(!searchKey.equals("")){
				   searchFilter=" and (cdrlogs.imei like '%"+searchKey+"%' or cdrlogs.imei like '%"+searchKey+"%' or mobiletype.mobile_type like '%"+searchKey+"%')";
			   }
			   
			   
			  // String query="select a.*,cdroprreport.oprname,cdroprreport.country,cdroprreport.c_opr,cdroprreport.c_count from (select distinct cdrlogs_current.*,case when mobiletype.mobile_type is null then 'Not_Available' else mobiletype.mobile_type end mobile_type from cdrlogs_current left join mobiletype on (cdrlogs_current.imei = mobiletype.imei::character varying) where oprid = (select max(id) from oprrationdata) "+queryFilter+" order by inserttime desc) a  inner join cdroprreport on (a.id = cdroprreport.id);";
			   if(!size.equalsIgnoreCase("all")){
				   sizeFilter=" limit "+size;
			   }
			   try {
				String query="select a.stype,a.imsi,a.imei,a.ta,a.mobile_type,(a.inserttime + time '05:30') as tt,cdroprreport.oprname,cdroprreport.country,cdroprreport.c_opr,cdroprreport.c_count,cdroprreport.realrxl,cdroprreport.band,cdroprreport.mcc,cdroprreport.mnc from (select distinct cdrlogs.*,case when mobiletype.mobile_type is null then '0' else mobiletype.mobile_type end mobile_type from cdrlogs left join mobiletype on (cdrlogs.imei = mobiletype.imei::character varying or cdrlogs.imsi = mobiletype.imsi::character varying) where oprid = (select max(id) from oprrationdata) and cdrlogs.imei!='000000000000000'"+queryFilter+searchFilter+") a  inner join cdroprreport on (a.id = cdroprreport.id) order by a.inserttime desc"+sizeFilter;
				JSONArray listDetail = new Operations().getJson(query);
				query="select count(*) as count from (select distinct cdrlogs.*,case when mobiletype.mobile_type is null then '0' else mobiletype.mobile_type end mobile_type from cdrlogs left join mobiletype on (cdrlogs.imei = mobiletype.imei::character varying or cdrlogs.imsi = mobiletype.imsi::character varying) where oprid = (select max(id) from oprrationdata) and cdrlogs.imei!='000000000000000'"+queryFilter+searchFilter+") a  inner join cdroprreport on (a.id = cdroprreport.id)";
				   listDetail.put(new Operations().getJson(query).getJSONObject(0));
				   return Response.status(201).entity(listDetail.toString()).build();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				   return Response.status(201).entity("").build();
		   }
		   
		   @GET
		   @Path("/getcurrentcomingnetworkdata")
		   @Produces(MediaType.APPLICATION_JSON)
		   public Response getCurrentComingNetworkData(@Context HttpServletRequest req)
		   {	   
			   //String query = "select distinct cdrlogs_current.*,mobiletype.mobile_type from cdrlogs_current left join mobiletype on (cdrlogs_current.imsi = mobiletype.imsi::character varying) where oprid = (select max(id) from oprrationdata) order by inserttime;";
			   HttpSession ss = req.getSession();
			   String queryFilter = "";
			   if(ss.getAttribute("celltime") != null) 
			   {
				   String cellTime = ss.getAttribute("celltime").toString();
				   queryFilter = " and inserttime >= '"+cellTime+"'";
			   }
			   
			  // String query="select a.*,cdroprreport.oprname,cdroprreport.country,cdroprreport.c_opr,cdroprreport.c_count from (select distinct cdrlogs_current.*,case when mobiletype.mobile_type is null then 'Not_Available' else mobiletype.mobile_type end mobile_type from cdrlogs_current left join mobiletype on (cdrlogs_current.imei = mobiletype.imei::character varying) where oprid = (select max(id) from oprrationdata) "+queryFilter+" order by inserttime desc) a  inner join cdroprreport on (a.id = cdroprreport.id);";
			   //String query="select mcc,mnc,lac,tac,cell,packet_type,band,arfcn,uarfcn,earfcn,freq,bcc,ncc,psc,pci,rssi,snr,operators,packet_id,index_id,(inserttime + time '05:30') as tt from oprlogs_current where oprid = (select max(id) from oprrationdata) "+queryFilter+" order by inserttime asc;";
			   String query="select mcc,mnc,lat,lon,lac,tac,cell,packet_type,band,arfcn,uarfcn,earfcn,freq,bcc,ncc,psc,pci,rssi,snr,operators,packet_id,index_id,(inserttime + time '05:30') as tt from oprlogs_current where oprid = (select max(id) from oprrationdata) "+queryFilter+" order by inserttime asc;";
			   JSONArray jo = new Operations().getJson(query);
			   return Response.status(201).entity(jo.toString()).build();
		   }
		   
/*		   @POST
		   @Path("/deleteoperation")
		   @Produces(MediaType.APPLICATION_JSON)
		   public Response deleteOpr(@Context HttpServletRequest req,@FormParam("oprId") String oprId)
		   {
			   		Common co = new Common();	
					String query = "delete from oprrationdata where id in ("+oprId+")";
					boolean status=co.executeDLOperation(query);
					HashMap<String,String> rs = new HashMap<String,String>();
					if(status){
						rs.put("result", "success");
						}else{
						rs.put("result", "failure");	
						}
			            return Response.status(201).entity(rs).build();
		    }
		   
		   @POST
		   @Path("/deletealloperation")
		   @Produces(MediaType.APPLICATION_JSON)
		   public Response deleteAllOpr(@Context HttpServletRequest req)
		   {
			   		Common co = new Common();	
					String query = "delete from oprrationdata where status in ('0','2')";
					boolean status=co.executeDLOperation(query);
					HashMap<String,String> rs = new HashMap<String,String>();
					if(status){
						rs.put("result", "success");
						}else{
						rs.put("result", "failure");	
						}
			            return Response.status(201).entity(rs).build();
		    }*/
			
			
		   @GET
		   @Path("/reportserver")
		   @Produces(MediaType.APPLICATION_JSON)
		   public Response getreport(@Context HttpServletRequest req,@QueryParam("oprId") String oprId)
		   {	   
			   String res = new ReportServer().createReports(oprId,null,null,null,"reports","active");
			   return Response.status(201).entity(res).build();
		   }
		   
		   @POST
		   @Path("/getCurrentEvents")
		   @Produces(MediaType.APPLICATION_JSON)
		   public Response getreport(@Context HttpServletRequest req)
		   {	    
			   String query  = "select event_msg,date_trunc('second',logtime + '05:30:00'::interval) logtime1 from currentrunningoperationevnets where opr_id = (select max(id) from oprrationdata) order by logtime";
			   JSONArray js = new Operations().getJson(query);
			   return Response.status(201).entity(js.toString()).build();
		   }
			   
			   public void stopAllOpr(){
				   new Common().executeDLOperation("update oprrationdata set status='0',stoptime=timezone('utc'::text, now()) where status!='0' and id=(select max(id) from oprrationdata)");
			   }
			   
			   @POST
			   @Path("/TRIGGER")
			   @Produces(MediaType.APPLICATION_JSON)
			   public Response triggerEvent(@Context HttpServletRequest req,String data)
			   {	
				   int oprStatus ;
				   fileLogger.info("Inside Function : triggerEvent");
				   fileLogger.debug("scanreport time:"+new Date());
				   fileLogger.debug("final data :"+data);
				   try{
					 int systemTypeCode=DBDataService.getSystemType();
					 if(systemTypeCode==2){
				   String manualStatus=new Operations().getJson("select status from manual_override").getJSONObject(0).getString("status");
				   fileLogger.debug("manualStatus is :"+manualStatus);
				   
				   DBDataService dbDataService = DBDataService.getInstance();
				   
				   Operations operations = new Operations();
				   JSONObject oprDataObj = null;
				   String oprType = "1";
				   try {
						   oprDataObj=operations.getJson("select * from oprrationdata where id=(select max(id) from oprrationdata)").getJSONObject(0);
					       oprStatus = oprDataObj.getInt("status");
					       oprType = oprDataObj.getString("opr_type");
					       fileLogger.info("Function : triggerEvent oprStatus and oprType  : "+ oprStatus +" and "+oprType);
				   }
				   catch(Exception ex) {
					   fileLogger.info("Function : triggerEvent Exception occurred : "+ ex.getMessage());
				   }
					   JSONObject triggerData = new JSONObject(data);
					   String timestamp=triggerData.getString("TIME_STAMP");
					   String transId=triggerData.getString("TRANS_ID");
					   String cueId=triggerData.getString("CUE_ID");
					   String deviceIp=triggerData.getString("DEVICE_IP");
					   double freq=triggerData.getDouble("FREQ");
					   freq=Math.floor(freq * 10.0) / 10.0;
					   fileLogger.info("Function : triggerEvent Dataaa= : "+ timestamp +" and "+transId +" and "+cueId +" and "+deviceIp +" and "+freq);
					   int trglAngle=(int)triggerData.getDouble("ANGLE");
					   String eventType=Integer.toString(triggerData.getInt("EVENT_TYPE"));
					   double lat=triggerData.getDouble("LATITUDE");
					   double lon=triggerData.getDouble("LONGITUDE");
					   int timeout=triggerData.getInt("TIME_OUT");
					   int validity=triggerData.getInt("VALIDITY");
					   double bandwidth=triggerData.getDouble("BANDWIDTH");
					   String sector=triggerData.getString("SECTOR");
					   Operations operation=new Operations();
					   String eventName="";
					   String note=null;
					   
					   //String operators=oprDataObj.getString("plmn");
					   String operators=null;
					   int coverageDistance = operations.getJson("select value from system_properties where key='coverage'").getJSONObject(0).getInt("value");
					   double frequency=-1;
				   	   double latitude=-1;
				   	   double longitude=-1;
				   	   int angle=-1;
				   	   Date date=new Date();
                        if(freq!=-1){
                        	eventName="trgl";
                        	frequency=freq;
                        	angle=trglAngle;
                        	
    						//storeTriggerOnDb(new Date(),null,"Hummer","Frequency:"+frequency+",Bandwidth:"+bandwidth+",Sector:"+sector+"");
                        	//new TriggerCueServer().sendText("event");
                        	//dbDataService.storeTriggerOnDb(date,cueId,"Hummer","Received,Cue ID:"+cueId+",Frequency:"+frequency+",Bandwidth:"+bandwidth+",Sector:"+sector+"");
                    		//new TriggerCueServer().sendText("event");
                        }else if(lat!=-1){
                        	eventName="ugs";
                        	latitude=lat;
                        	longitude=lon;
    						//storeTriggerOnDb(eventData.getDate(),eventData.getTransId(),"Oxfam","Coordinates(Lat:"+latitude+",Lon:"+longitude+"),Sector:"+sector+"");
    						//new TriggerCueServer().sendText("event")
                        	//dbDataService.storeTriggerOnDb(date,cueId,"Oxfam","Received,Cue ID:"+cueId+",Coordinates(Lat:"+latitude+",Lon:"+longitude+"),Sector:"+sector+"");
                    		//new TriggerCueServer().sendText("event");
                        }
                        
                        int periodicity=-1;	
				   		String detail="";
				   		String peer="";
					   	LinkedHashMap<String,String> queLog = new LinkedHashMap<String,String>();
					   if(!(manualStatus.equalsIgnoreCase("t") || manualStatus.equalsIgnoreCase("true"))){
					   		dbDataService.addToEventPriorityQueue(null,eventName,note,oprType,operators,coverageDistance,deviceIp,frequency,latitude,longitude,periodicity,timeout,validity,date,true,transId,angle,bandwidth,sector,cueId);
					   		queLog.put("action", "Receive"); 
				   }else{
					   fileLogger.info("Function : triggerEvent Inside action, Receive&Discarded(Manual Enabled)" );
					   queLog.put("action", "Receive&Discarded(Manual Enabled)"); 
					   if(eventName.equalsIgnoreCase("oxfam")){
						   dbDataService.storeTriggerOnDb(date,cueId,"Oxfam","Discarded(Manual Enabled),Cue ID:"+cueId+",Coordinates(Lat:"+lat+",Lon:"+lon+"),Sector:"+sector+",Validity:"+validity+",Timeout:"+timeout);
					   }else{
						   dbDataService.storeTriggerOnDb(date,cueId,"Hummer","Discarded(Manual Enabled),Cue ID:"+cueId+",Frequency:"+frequency+",Bandwidth:"+bandwidth+",Sector:"+sector+",Validity:"+validity+",Timeout:"+timeout);
					   }
					   		new TriggerCueServer().sendText("event");	
				   }
					   
				   		if(eventName.equalsIgnoreCase("oxfam")){
				   			fileLogger.info("Function : triggerEvent Inside action,eventName.equalsIgnoreCase(oxfam)" );
				   			//detail="Trigger coming from Oxfam ,details: lat:"+latitude+",lon:"+longitude+",sector:"+sector+",timeout:"+timeout+",validity:"+validity+"";
				   			queLog.put("source","Oxfam");
				   			queLog.put("latitude",Double.toString(latitude));
				   			queLog.put("longitude",Double.toString(longitude));
				   			queLog.put("sector",sector);
				   			fileLogger.info("Function : triggerEvent req data =" +queLog);
				   		}else{
				   			//detail="Trigger coming from Hummer ,details: freq:"+frequency+",sector:"+sector+",bandwidth:"+bandwidth+",timeout:"+timeout+",validity:"+validity+"";
				   			queLog.put("source","Hummer");
				   			queLog.put("frequency",Double.toString(frequency));
				   			queLog.put("sector",sector);
				   			queLog.put("bandwidth",Double.toString(bandwidth));
				   			fileLogger.info("Function : triggerEvent req data2 =" +queLog);
				   		}
			   		
				   		queLog.put("trans id",transId);
				   		queLog.put("cue id",transId);
				   		
				   		queLog.put("timeout", Integer.toString(timeout));
				   		queLog.put("validity", Integer.toString(validity));
				   		fileLogger.info("Function : triggerEvent req data3 =" +queLog);
				   		//new AuditHandler().auditLog(log, 3);
				   		new AuditHandler().audit_que(queLog);
					 }
				   }catch(Exception E){
					   E.printStackTrace();
				   }
				   
				   /*if(new Integer(freq)!=null){
					   return triggerTracking(freq);
				   }else{
					   return triggerPosition(lat,lon);
				   }*/
				   fileLogger.info("Exit Function : triggerEvent");
				   return null;
			   }
			   
			   @POST
			   @Path("/offset")
			   @Produces(MediaType.APPLICATION_JSON)
			   public Response offsetEvent(@Context HttpServletRequest req,String data)
			   {	
				   fileLogger.info("Inside Function : offsetEvent");
				   fileLogger.debug("offsetEvent time:"+new Date());
				   fileLogger.debug("offsetEvent data :"+data);
				   try{
					   JSONObject gpsObj=new JSONObject(data);
					 int systemTypeCode=DBDataService.getSystemType();
					 int gpsNode=DBDataService.getGpsNode();
					 if(systemTypeCode==2 && gpsNode==2){
						 String offset=Integer.toString(gpsObj.getInt("OFFSET"));
						 String latitude=gpsObj.getString("LATITUDE");
						 String longitude=gpsObj.getString("LONGITUDE");
							if(latitude.charAt(0)=='0' || longitude.charAt(0)=='0'){
								fileLogger.debug("invalid gps coordinates from hummer");
								return null;
							}
							String query="update antenna set angle_offset="+offset+" where atype='1'";
							Common common = new Common();
							common.executeDLOperation(query);
							
							try {
								DBDataService.setAngleOffset(Integer.parseInt(offset));
							} catch (Exception e) {
								fileLogger.error("exception in trgltrigger while updating angle offset");
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							ArrayList<String> serverData = new ArrayList<String>();
							
							serverData.add("0");
							serverData.add("0");
							serverData.add("0");
							serverData.add("0");
							serverData.add(""+latitude);
							serverData.add("0");
							serverData.add(""+longitude);
							serverData.add("moving");
							serverData.add(offset);
							new GPSSocketServer().sendText(serverData);
							query="insert into gpsdata(lat,lon) values('"+latitude+"','"+longitude+"')";
							common.executeDLOperation(query); 
					 }
				   }catch(Exception E){
					   E.printStackTrace();
				   }
				   
				   /*if(new Integer(freq)!=null){
					   return triggerTracking(freq);
				   }else{
					   return triggerPosition(lat,lon);
				   }*/
				   fileLogger.info("Exit Function : offsetEvent");
				   return null;
			   }
			      
/*			   public Response triggerTracking(int freq)
			   {	   
				   
				   OperationCalculations oc = new OperationCalculations();
				   
				   LinkedHashMap<String,String> res = oc.checkIfLastOperationIsTriggerOperation();
				   
				   if(res.get("result").equalsIgnoreCase("true")) 
				   {
					   
					   //calulate arfcn and uarfcn
					   String fcns = oc.calulateArfcnsFromFreq(freq, -1);
					   String result = new ApiCommon().startTracking(null,fcns,fcns,freq);
				   }
				   
				   return Response.status(201).entity(res).build();
				   
			   }*/
			   
			   public Response triggerPosition(double lat,double lon)
			   {	   
				   
				   OperationCalculations oc = new OperationCalculations();
				   LinkedHashMap<String,String> res = oc.checkIfLastOperationIsTriggerOperation();
				   if(res.get("result").equalsIgnoreCase("true")) 
				   {
					   
					   //getting AntennaId
					   int antennaId = oc.getAntennaIdFromPosition(lat,lon);
					   String result = new ApiCommon().startTracking(null,antennaId);
					  // String result = new ApiCommon().startTracking(null,fcns,fcns,freq);
				   }
				   
				   return Response.status(201).entity(res).build();
				   
			   }
			  
			@POST
			@Path("/addGPS")
			@Produces(MediaType.APPLICATION_JSON)
			public Response addGPS(@Context HttpServletRequest req,@FormParam("lat") String lat, @FormParam("lon") String lon)
			{	   
				   
				   
				   String query = "insert into netscan_gps_data (ip,lat,lon,tstmp) values('manual',"+lat+","+lon+","+System.currentTimeMillis()+")";
				   new Common().executeDLOperation(query);
				   
				   return Response.status(201).entity("ok").build();
				   
			} 
			   

			 
/*			   public Response startTriggerOrOperation(String eventName,String note,String oprType,String operators,int coverageDistance,String deviceIp,int frequency,double latitude,double longitude,long periodicity,long timeout,long validity,Date date){
			   		new CurrentOperation(null);
					Common co = new Common();	
					fileLogger.debug("oprPlmnFull :"+operators);
					HashMap<String,String> rs = new HashMap<String,String>();
					Operations operation=new Operations();
					JSONObject jsonObject=new JSONObject();
					fileLogger.debug("1dcommonservice");
					NetscanOperations netscanOperations=new NetscanOperations();
					JSONObject netscanData=new JSONObject();
					LinkedHashMap<String,String> param = new LinkedHashMap<String,String>();
					int preScanMaxId=0;
					LinkedHashMap<String,String> parameter = new LinkedHashMap<String,String>();
					fileLogger.debug("1ccommonservice");
					long scanTime=Long.parseLong(co.getDbCredential().get("scantime"));
					ArrayList<String> givenPLMNs = new ArrayList<String>(Arrays.asList(operators.split("\\s*,\\s*")));
					fileLogger.debug("givenPLMNs :"+givenPLMNs);
					String query = "insert into oprrationdata(name,note,inserttime,opr_type,gsm_bands,umts_bands,lte_bands,duration,status,t_stoptime,plmn,distance,use_last_scanned_data) values('"+oprName+"','"+oprNote+"',timezone('utc'::text, now()),'"+oprType+"','"+oprGsmBand+"','"+oprUmtsBand+"','"+oprLteBand+"',"+oprDuration+",'1',timezone('utc'::text, now())+Interval '"+oprDuration+" min','"+oprPlmnFull+"',"+distance+",'"+oldData+"')";	 
					boolean status=co.executeDLOperation(query);
					try{
						if(status)
						{
						rs.put("result", "success");
						if(oprType.equals("1")){
							new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Operation Created for Scheduler\"}");
						}else if(oprType.equals("2")){
							new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Operation Created for Automate\"}");
						}else{
							new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Operation Created for External Trigger\"}");
						}
							query="select id,inserttime from oprrationdata where id=(select max(id) from oprrationdata)";
							jsonObject=operation.getJson(query).getJSONObject(0);
							HttpSession session=req.getSession(false);
							session.setAttribute("startTime",jsonObject.getString("inserttime"));	
						}
						else
						{
							co.executeDLOperation("update oprrationdata set status='2',stoptime=timezone('utc'::text, now()) where id=(select max(id) from oprrationdata)");
							new AutoOperationServer().sendText("{\"result\":\"fail\",\"msg\":\"Unable To Create Operation\"}");
							rs.put("result", "failure");
							return Response.status(201).entity(rs).build();
						}
					}catch(Exception e){
						fileLogger.debug("Exception occurs:"+e.getMessage());
					}
					
			   		if(oprType.equals("1")) {
			   			fileLogger.debug("Starting Scheduler Tracking");
			   			//boolean scannerDataAvailability=isScannerDataAvailable();
			   			if(scannerDataAvailability){
			   				//goForTracking();
			   			}else{
			   				//startScanning();
			   			}
			   			fileLogger.debug("Ending Scheduler Tracking");
					}else  if(oprType.equals("2")){
						fileLogger.debug("Starting Automate Tracking");
						//boolean scannerDataAvailability=isScannerDataAvailable();
						if(scannerDataAvailability){
							//goForTracking();
						}else{
							//startScanning();
						}
						
						fileLogger.debug("Ending Automate Tracking");
					}else{	
						fileLogger.debug("Starting External Trigger Tracking");
						//boolean scannerDataAvailability=isScannerDataAvailable();
						if(scannerDataAvailability){
							//goForTracking();
						}else{
							//startScanning();
						}
						fileLogger.debug("Starting External Trigger Tracking");
					}
			   		return null;
			   }*/
			   
				@POST
				@Path("/changepassword")
				@Produces(MediaType.APPLICATION_JSON)
		        public Response ChangePassword(HttpServletRequest request,@FormParam("userId") String userId,@FormParam("oprPassword") String oldPassword,@FormParam("changedPassword") String changedPassword,@FormParam("confirmPassword") String confirmPassword)
		        {
	  				HashMap<String,String> rsOpr = new HashMap<String,String>();
		            try
		            {  	 		
		    	 	String query="select * from users where id="+userId;   	 		
		    	 	Operations operations = new Operations();
		    	 	JSONObject userDataObj = operations.getJson(query).getJSONObject(0);
			        boolean empty = true;
			        String passwordDB = userDataObj.getString("password");
		    	 	if(passwordDB != null && passwordDB.equalsIgnoreCase(oldPassword))
		    	 	{
		    	 	     query="update user_master set password = '"+changedPassword+"' where id="+userId;
		    	 	     boolean status=new Common().executeDLOperation(query);
		    	 	     if(status){
			  				  rsOpr.put("result", "success");
			  				  rsOpr.put("message", "Password Changed Successfully");  
		    	 	     }else{
			  				   rsOpr.put("result", "failure");
			  				   rsOpr.put("message", "Error in Database Transcation"); 
		    	 	     }  	 			
		    	        }
		    	        else
		    	        {
			  				   rsOpr.put("result", "failure");
			  				   rsOpr.put("message", "Incorrect Old Password");
		    	 	}    			                                         
		    	 	}catch(Exception E)
		            {
		  				   rsOpr.put("result", "failure");
		  				   rsOpr.put("message", "Exception occurs");
		            }
		    	 	return Response.status(201).entity(rsOpr).build();
		    }
			   
			   
}

