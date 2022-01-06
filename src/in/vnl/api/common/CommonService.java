package in .vnl.api.common;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Paths;
//import java.util.Date;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Calendar;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.text.SimpleDateFormat;

import javax.ws.rs.Consumes;
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

import org.apache.commons.io.FileUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import javax.ws.rs.core.Response.ResponseBuilder;

import org.json.*;

import javax.json.JsonObject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import in .vnl.EventProcess.DBDataService;
import in .vnl.EventProcess.EventData;
import in .vnl.api.common.ApiCommon;
import in .vnl.api.threeg.ThreegOperations;
import in .vnl.api.twog.TwogOperations;
import in .vnl.api.twog.livescreens.TriggerCueServer;
import in .vnl.bist.Bist;
import in .vnl.msgapp.Common;
import in .vnl.msgapp.GPSSocketServer;
import in .vnl.msgapp.GeoSchedulerServer;
import in .vnl.msgapp.Operations;
import in .vnl.scheduler.NetscanSchedulerListener;
import in .vnl.scheduler.NetscanSingletonExecutor;
import in .vnl.api.common.livescreens.AutoOperationServer;
import in .vnl.api.common.livescreens.AutoStateServer;
import in .vnl.api.common.livescreens.ScanTrackModeServer;
import in .vnl.api.config.PossibleConfigurations;
import in .vnl.api.netscan.CurrentNetscanAlarm;
import in .vnl.api.netscan.NetscanOperations;
import in .vnl.api.netscan.NetscanTask;
import in .vnl.api.ptz.PTZ;
import in .vnl.report.Compress;
import in .vnl.report.ReportServer;
import in.vnl.sockets.*;
import in.vnl.sockets.UdpServerClient.*;

@Path("/common")
public class CommonService {
static int bmsDataPort;
 //static Logger logger = Logger.getLogger(CommonService.class);
 static Logger fileLogger = Logger.getLogger("file");
 static Logger statusLogger = Logger.getLogger("status");
 static String oprStartTime = null;

 @POST
 @Path("/getAllOperations")
 @Produces(MediaType.APPLICATION_JSON)
 public Response getAllOperations() {
  fileLogger.info("Inside Function : getAllOperations");
  String query = "select id,name,date_trunc('second',inserttime + '05:30:00'::interval) inserttime,note,opr_type,opr,duration,status,distance,date_trunc('second',stoptime + '05:30:00'::interval) stoptime from oprrationdata order by id desc";
  fileLogger.debug(query);
  JSONArray rs = new Operations().getJson(query);
  fileLogger.info("Exit Function : getAllOperations");
  return Response.status(201).entity(rs.toString()).build();
 }

 @POST
 @Path("/getallsystemusers")
 @Produces(MediaType.APPLICATION_JSON)
 public Response getAllSystemUsers() {
  fileLogger.info("Inside Function : getAllSystemUsers"); 
  String query = "select id,user_name from users";
  fileLogger.debug(query);
  JSONArray rs = new Operations().getJson(query);
  fileLogger.info("Exit Function : getAllSystemUsers"); 
  return Response.status(201).entity(rs.toString()).build();
 }

 @POST
 @Path("/getsysuserbasedonlevel")
 @Produces(MediaType.APPLICATION_JSON)
 public Response getSysUsersBasedOnLevel(@Context HttpServletRequest req) {
  fileLogger.info("Inside Function : getSysUsersBasedOnLevel"); 
  HttpSession session = req.getSession(false);
  String query = "select id,user_name from users where level<=" + session.getAttribute("level").toString();
  fileLogger.debug(query);
  JSONArray rs = new Operations().getJson(query);
  fileLogger.info("Exit Function : getSysUsersBasedOnLevel"); 
  return Response.status(201).entity(rs.toString()).build();
 }

 @GET
 @Path("/testapi")
 @Produces(MediaType.APPLICATION_JSON)
 public void testApi(@Context HttpServletRequest req) {
  new TRGLController().sendOffsetRequestToHummer();
 }


 @POST
 @Path("/getLocatorVersion")
 public String getLocatorVersion() {
	    fileLogger.info("Inside Function : getLocatorVersion"); 
		String absolutePath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		absolutePath = absolutePath.substring(0,absolutePath.lastIndexOf("WEB-INF"));
		Properties prop = new Properties();
		try
		{
			prop.load(new FileInputStream(absolutePath	+ "/resources/version.properties"));
		}
		catch(Exception E)
		{
			fileLogger.debug("version.properties not found" + E.getMessage());
		}
		HashMap<String,String> hm = new HashMap<String,String>();
		String locatorVersion = prop.getProperty("Version");
		fileLogger.info("Inside Function : getLocatorVersion"); 
		return locatorVersion;
		//return Response.status(201).entity(locatorVersion).build();
		//hm.put("version", prop.getProperty("Version"));
		
		//return hm;
		
	}
 
 @POST
 @Path("/getoperators")
 @Produces(MediaType.APPLICATION_JSON)
 public Response getOperators() {
  fileLogger.info("Inside Function : getOperators"); 
  String query = "SELECT opr, string_agg(plmn::text, ', ') AS plmn_list FROM plmn_opr where substring(plmn::text,1,3) in('404','405','130') or plmn in(33402,33420) GROUP  BY opr";
  fileLogger.debug(query);
  JSONArray rs = new Operations().getJson(query);
  fileLogger.info("Exit Function : getOperators"); 
  return Response.status(201).entity(rs.toString()).build();
 }

/* @POST
 @Path("/getoperators")
 @Produces(MediaType.APPLICATION_JSON)
 public Response getCurrentAndOffsetAngle() {
  fileLogger.info("Inside Function : getOperators"); 
  String query = "select ";
  fileLogger.debug(query);
  JSONArray rs = new Operations().getJson(query);
  fileLogger.info("Exit Function : getOperators"); 
  return Response.status(201).entity(rs.toString()).build();
 }*/
 @POST
 @Path("/restartoperation")
 @Produces(MediaType.APPLICATION_JSON)
 public Response restartOperation(@Context HttpServletRequest req, @FormParam("oprId") String oprId) {
  fileLogger.info("Inside Function : restartOperation"); 
  //fileLogger.debug("oprId is :("+oprId+")";

  JSONObject oprDataJson = null;;
  String oprName = null;
  String oprNote = null;
  String oprType = null;
  String oprDuration = null;
  String oprGsmBand = null;
  String oprUmtsBand = null;
  String oprLteBand = null;
  String oprConfig = null;
  ArrayList < String > givenPLMNs = null;
  try {
   oprDataJson = new Operations().getJson("select * from oprrationdata where id=" + oprId).getJSONObject(0);
   oprName = oprDataJson.getString("name");
   oprNote = oprDataJson.getString("note");
   oprType = oprDataJson.getString("opr_type");
   oprDuration = oprDataJson.getString("duration");
   oprGsmBand = oprDataJson.getString("gsm_bands");
   oprUmtsBand = oprDataJson.getString("umts_bands");
   oprLteBand = oprDataJson.getString("lte_bands");
   oprConfig = oprDataJson.getString("config");
   givenPLMNs = new ArrayList < String > (Arrays.asList(oprDataJson.getString("plmn").split("\\s*,\\s*")));
  } catch (Exception E) {

  }
  Common co = new Common();
  long scanTime = Long.parseLong(co.getDbCredential().get("scantime"));
  new CurrentOperation(null);
  HashMap < String, String > rs = new HashMap < String, String > ();
  Operations operation = new Operations();
  JSONObject jsonObject = new JSONObject();
  NetscanOperations netscanOperations = new NetscanOperations();
  JSONObject netscanData = new JSONObject();
  LinkedHashMap < String, String > param = new LinkedHashMap < String, String > ();
  int preScanMaxId = 0;
  LinkedHashMap < String, String > parameter = new LinkedHashMap < String, String > ();


  if (oprConfig == null || oprConfig.equals("") || oprConfig.equals("{[]}")) {
   fileLogger.debug("Resuming from Network Scanner");
   JSONArray netscanInfoArr = new Operations().getJson("select count(*) as scanner_count from view_btsinfo where code=3");
   try {
    if (netscanInfoArr.getJSONObject(0).getInt("scanner_count") != 1) {
     HashMap < String, String > rsOpr = new HashMap < String, String > ();
     rsOpr.put("result", "failure");
     rsOpr.put("message", "Network Scanner not available for Operation.");
     return Response.status(201).entity(rsOpr).build();
    }
   } catch (JSONException e1) {
    // TODO Auto-generated catch block
    e1.printStackTrace();
   }
   boolean status = co.executeDLOperation("update oprrationdata set status='1' where id=" + oprId);
   try {
    if (status) {
     rs.put("result", "success");
     //new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Operation Updated\"}");
     new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Operation Resumed\"}");
     jsonObject = operation.getJson("select id,inserttime from oprrationdata where id=" + oprId).getJSONObject(0);
     HttpSession session = req.getSession(false);
     session.setAttribute("startTime", jsonObject.getString("inserttime"));
    } else {
     co.executeDLOperation("update oprrationdata set status='2',stoptime=timezone('utc'::text, now()) where id=(select max(id) from oprrationdata)");
     new AutoOperationServer().sendText("{\"result\":\"fail\",\"msg\":\"Unable To Resume Operation\"}");
     rs.put("result", "failure");
     return Response.status(201).entity(rs).build();
    }
   } catch (Exception E) {
    co.executeDLOperation("update oprrationdata set status='2',stoptime=timezone('utc'::text, now()) where id=(select max(id) from oprrationdata)");
    new AutoOperationServer().sendText("{\"result\":\"fail\",\"msg\":\"Unable To Resume Operation\"}");
    rs.put("result", "failure");
    return Response.status(201).entity(rs).build();
   }
   try {
    netscanData = operation.getJson("select * from view_btsinfo where code=3 limit 1").getJSONObject(0);
    JSONObject scanDataObj = new JSONObject();
    scanDataObj.put("CMD_CODE", "START_EXHAUSTIVE_SCAN");
    scanDataObj.put("RSSI_THRESHOLD", "-110");

    if (oprType.equals("1")) {
     scanDataObj.put("REPETITION_FLAG", "1");
     scanDataObj.put("REPITITION_FREQ", "10");
    } else {
     scanDataObj.put("REPETITION_FLAG", "0");
     scanDataObj.put("REPITITION_FREQ", "0");
    }



    scanDataObj.put("CMD_CODE", "START_EXHAUSTIVE_SCAN");
    JSONObject gsmBandObj = getBandWiseJson(oprGsmBand, "GSM");
    JSONObject umtsBandObj = getBandWiseJson(oprUmtsBand, "UMTS");
    JSONObject lteBandObj = getBandWiseJson(oprLteBand, "LTE");
    fileLogger.debug("1acommonservice");
    JSONArray fullBandArr = new JSONArray();
    fullBandArr.put(gsmBandObj);
    fullBandArr.put(umtsBandObj);
    fullBandArr.put(lteBandObj);
    scanDataObj.put("SCAN_LIST", fullBandArr);
    String scanDataStr = getScanJsonInOrder(scanDataObj, "start");
    parameter.put("cmdType", "START_EXHAUSTIVE_SCAN");
    parameter.put("systemCode", "3");
    parameter.put("systemIP", netscanData.getString("ip"));
    parameter.put("systemId", netscanData.getString("sytemid"));
    parameter.put("id", netscanData.getString("b_id"));
    parameter.put("data", scanDataStr);
    //new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Data setup done\"}");	
    preScanMaxId = operation.getJson("select max(id) max_count from netscan_cell_scan_report").getJSONObject(0).getInt("max_count");
    fileLogger.debug("preScanMaxId:" + preScanMaxId);
    fileLogger.debug("1ecommonservice");

    HashMap < String, ArrayList < HashMap < String, String >>> devicesMapOverTech = new Operations().getAllBtsInfoByTech();
    new ApiCommon().lockUnlockAllDevices(devicesMapOverTech, 1);
    new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Stopping Scan\"}");
    param = prepareParamForStopNetscan(netscanData, "GSM");
    netscanOperations.sendToServer(param);
//    param = prepareParamForStopNetscan(netscanData, "UMTS");
//    netscanOperations.sendToServer(param);
//    param = prepareParamForStopNetscan(netscanData, "LTE");
//    netscanOperations.sendToServer(param);

    fileLogger.debug("1fcommonservice");

    //Thread.sleep(60000);

    //new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Starting Scan\"}");

    netscanOperations.sendToServer(parameter);
    new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Scanning Started\"}");
   } catch (Exception E) {
    rs.put("result", "failure");
    co.executeDLOperation("update oprrationdata set status='2',stoptime=timezone('utc'::text, now()) where id=(select max(id) from oprrationdata)");
    new AutoOperationServer().sendText("{\"result\":\"fail\",\"msg\":\"Network Scanning Error\"}");
    new AutoOperationServer().sendText("{\"result\":\"fail\",\"msg\":\"Operation Interrupted\"}");
    fileLogger.error("Exception: " + E.getMessage());
    rs.put("result", "failure");
    return Response.status(201).entity(rs).build();
   }
   if (oprType.equals("1")) {
    try {


     while (true) {
      //if(iterationCount != 0) 
      //{
      String query1 = "select extract(epoch from t_stoptime)*1000 as stoptime from oprrationdata order by id desc limit 1";
      fileLogger.debug(query1);
      JSONArray rs1 = new Operations().getJson(query1);
      fileLogger.debug("line 355:" + System.currentTimeMillis() + "," + rs1.getJSONObject(0).getLong("stoptime"));
      if (System.currentTimeMillis() >= rs1.getJSONObject(0).getLong("stoptime")) {



       param = prepareParamForStopNetscan(netscanData, "GSM");
       netscanOperations.sendToServer(param);
       param = prepareParamForStopNetscan(netscanData, "UMTS");
       netscanOperations.sendToServer(param);
       param = prepareParamForStopNetscan(netscanData, "LTE");
       co.executeDLOperation("update oprrationdata set status='0',stoptime=timezone('utc'::text, now()) where id=(select max(id) from oprrationdata)");
       new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Operation Completed\"}");
       rs.put("result", "success");
       return Response.status(201).entity(rs).build();
       //break;
      }
      //}

     }
    } catch (Exception E) {
     rs.put("result", "failure");
     co.executeDLOperation("update oprrationdata set status='2',stoptime=timezone('utc'::text, now()) where id=(select max(id) from oprrationdata)");
     new AutoOperationServer().sendText("{\"result\":\"fail\",\"msg\":\"Scan Error\"}");
     new AutoOperationServer().sendText("{\"result\":\"fail\",\"msg\":\"Operation Interrupted\"}");
     fileLogger.error("Exception: " + E.getMessage());
     return Response.status(201).entity(rs).build();
    }

    //new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"finished\"}");
    //return  Response.status(201).entity("{'result':'success','msg':'finished'}").build(); 
   } else {

    try {
     //Thread.sleep(scanTime);


     new CurrentNetscanAlarm();

     long startTime = System.currentTimeMillis();
     while ((System.currentTimeMillis() - startTime) < scanTime) {
      fileLogger.debug("desc in loop is :" + CurrentNetscanAlarm.desc);
      if (CurrentNetscanAlarm.desc != null && CurrentNetscanAlarm.desc.contains("Scan Successfully stopped  for EXHAUSTIVE_SCAN")) {
       new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Scanning Completed\"}");
       fileLogger.debug("successfully stopped exhaustive scan alarm occured at time:" + new Date());
       break;
      }

     }


     int afterScanMaxId = operation.getJson("select max(id) max_count from netscan_cell_scan_report").getJSONObject(0).getInt("max_count");
     fileLogger.debug("first scan value" + preScanMaxId + "," + afterScanMaxId);
     fileLogger.debug("afterScanMaxId:" + afterScanMaxId);
     if (preScanMaxId >= afterScanMaxId) {
      netscanOperations.sendToServer(parameter);
      //Thread.sleep(scanTime);

      while ((System.currentTimeMillis() - startTime) < scanTime) {
       fileLogger.debug("desc in 2nd loop is :" + CurrentNetscanAlarm.desc);
       if (CurrentNetscanAlarm.desc != null && CurrentNetscanAlarm.desc.contains("Scan Successfully stopped  for EXHAUSTIVE_SCAN")) {
        new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Scanning Completed\"}");
        fileLogger.debug("successfully stopped 2nd exhaustive scan alarm occured at time:" + new Date());
        break;
       }

      }


      int afterSecondScanMaxId = operation.getJson("select max(id) max_count from netscan_cell_scan_report").getJSONObject(0).getInt("max_count");
      fileLogger.debug("second scan value" + preScanMaxId + "," + afterSecondScanMaxId);

      fileLogger.debug("afterSecondScanMaxId:" + afterSecondScanMaxId);
      if (preScanMaxId >= afterSecondScanMaxId) {
       fileLogger.debug("No Netscan Records Present.About to Exit.Operation Countinue");

       co.executeDLOperation("update oprrationdata set status='0',stoptime=timezone('utc'::text, now()) where id=(select max(id) from oprrationdata)");
       new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"No Scanning Records Found\"}");
       new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Operation Completed\"}");
       rs.put("result", "failure");
       return Response.status(201).entity(rs).build();

      } else {


       //param = prepareParamForStopNetscan(netscanData,"GSM");
       //netscanOperations.sendToServer(param);
       //param = prepareParamForStopNetscan(netscanData,"UMTS");
       //netscanOperations.sendToServer(param);
       //param = prepareParamForStopNetscan(netscanData,"LTE");
       //new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Stoping scan\"}");
       //Thread.sleep(60000);
       //new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Starting scan\"}");
       //netscanOperations.sendToServer(param);


       new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Tracking Started\"}");
       //String response=new ApiCommon().startTracking(givenPLMNs);
       String response = "";
       JSONObject responseJson = new JSONObject(response);
       if (responseJson.getString("result").equals("success")) {
        co.executeDLOperation("update oprrationdata set status='0',stoptime=timezone('utc'::text, now()) where id=(select max(id) from oprrationdata)");
        new AutoOperationServer().sendText(response);
        new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Operation Completed\"}");
       } else {
        co.executeDLOperation("update oprrationdata set status='2',stoptime=timezone('utc'::text, now()) where id=(select max(id) from oprrationdata)");
        new AutoOperationServer().sendText(response);
        new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Operation Interrupted\"}");
       }
       return Response.status(201).entity(response).build();
      }
     } else {
      //new NetscanOperations().sendToServer(param);
      fileLogger.debug("Started Tracking");
      new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Tracking Started\"}");
      //String rs1=new ApiCommon().startTracking(givenPLMNs);
      String rs1 = "";
      JSONObject rs1Json = new JSONObject(rs1);
      if (rs1Json.getString("result").equals("success")) {
       co.executeDLOperation("update oprrationdata set status='0',stoptime=timezone('utc'::text, now()) where id=(select max(id) from oprrationdata)");
       new AutoOperationServer().sendText(rs1);
       new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Operation Completed\"}");
      } else {
       co.executeDLOperation("update oprrationdata set status='2',stoptime=timezone('utc'::text, now()) where id=(select max(id) from oprrationdata)");
       new AutoOperationServer().sendText(rs1);
       new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Operation Interrupted\"}");
      }
      return Response.status(201).entity(rs1).build();
      //return Response.status(201).entity(rs1).build();



     }
    } catch (Exception E) {
     rs.put("result", "failure");
     co.executeDLOperation("update oprrationdata set status='2',stoptime=timezone('utc'::text, now()) where id=(select max(id) from oprrationdata)");
     new AutoOperationServer().sendText("{\"result\":\"fail\",\"msg\":\"Operation Interrupted\"}");
     fileLogger.error("Exception: " + E.getMessage());
     return Response.status(201).entity(rs).build();
    }
    //co.executeDLOperation("update oprrationdata set status='0',stoptime=timezone('utc'::text, now()) where id=(select max(id) from oprrationdata)");
    //new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"finished\"}");
    // return Response.status(201).entity(rs).build();
   }

  } else {

   fileLogger.debug("Restarting from tracking");
   boolean status = co.executeDLOperation("update oprrationdata set status='1' where id=" + oprId);
   try {
    if (status) {
     rs.put("result", "success");
     //new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Operation Updated\"}");
     new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Operation Resumed\"}");
     jsonObject = operation.getJson("select id,inserttime from oprrationdata where id=" + oprId).getJSONObject(0);
     HttpSession session = req.getSession(false);
     session.setAttribute("startTime", jsonObject.getString("inserttime"));
    } else {
     co.executeDLOperation("update oprrationdata set status='2',stoptime=timezone('utc'::text, now()) where id=(select max(id) from oprrationdata)");
     new AutoOperationServer().sendText("{\"result\":\"fail\",\"msg\":\"Unable To Resume Operation\"}");
     rs.put("result", "failure");
     return Response.status(201).entity(rs).build();
    }
   } catch (Exception E) {
    co.executeDLOperation("update oprrationdata set status='2',stoptime=timezone('utc'::text, now()) where id=(select max(id) from oprrationdata)");
    new AutoOperationServer().sendText("{\"result\":\"fail\",\"msg\":\"Unable To Resume Operation\"}");
    rs.put("result", "failure");
    return Response.status(201).entity(rs).build();
   }
   new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Tracking Started\"}");
   //String response=new ApiCommon().startTracking(givenPLMNs);
   String response = "";
   try {
    JSONObject responseJson = new JSONObject(response);
    if (responseJson.getString("result").equals("success")) {
     co.executeDLOperation("update oprrationdata set status='0',stoptime=timezone('utc'::text, now()) where id=(select max(id) from oprrationdata)");
     new AutoOperationServer().sendText(response);
     new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Operation Completed\"}");
    } else {
     co.executeDLOperation("update oprrationdata set status='2',stoptime=timezone('utc'::text, now()) where id=(select max(id) from oprrationdata)");
     new AutoOperationServer().sendText(response);
     new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Operation Interrupted\"}");
    }
   } catch (JSONException e) {}
   new AutoOperationServer().sendText(response);
   fileLogger.info("Exit Function : restartOperation");
   return Response.status(201).entity(response).build();
  }

 }

 /*   @POST
    @Path("/addiOperation")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addiOperation(@Context HttpServletRequest req,@FormParam("oprName") String oprName, @FormParam("oprNote") String oprNote, @FormParam("oprType") String oprType ,@FormParam("oprDuration") String oprDuration,@FormParam("oprGsmBand") String oprGsmBand,@FormParam("oprUmtsBand") String oprUmtsBand, @FormParam("oprLteBand") String oprLteBand, @FormParam("oprPlmnFull") String oprPlmnFull,@FormParam("oldData") String oldData,@FormParam("distance") int distance)
    {
 	   		fileLogger.debug("oprName is :("+oprName+")oprNote is :("+oprNote+")oprType is :("+oprType+")oprDuration is :("+oprDuration+")oprGsmBand is :("+oprGsmBand+")oprUmtsBand is :("+oprUmtsBand+")oprLteBand is :("+oprLteBand+")");
 	   		new CurrentOperation(null);
 			Common co = new Common();	
 			fileLogger.debug("oprPlmnFull :"+oprPlmnFull);
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
 			ArrayList<String> givenPLMNs = new ArrayList<String>(Arrays.asList(oprPlmnFull.split("\\s*,\\s*")));
 			fileLogger.debug("givenPLMNs :"+givenPLMNs);
 			String query = "insert into oprrationdata(name,note,inserttime,opr_type,gsm_bands,umts_bands,lte_bands,duration,status,t_stoptime,plmn,distance,use_last_scanned_data) values('"+oprName+"','"+oprNote+"',timezone('utc'::text, now()),'"+oprType+"','"+oprGsmBand+"','"+oprUmtsBand+"','"+oprLteBand+"',"+oprDuration+",'1',timezone('utc'::text, now())+Interval '"+oprDuration+" min','"+oprPlmnFull+"',"+distance+",'"+oldData+"')";	 
 			boolean status=co.executeDLOperation(query);
 			StringBuilder scanMessage=new StringBuilder("");
 			try{
 				if(status)
 				{
 					rs.put("result", "success");
 					if(oprType.equals("1")){
 						new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Operation Created for Scanning\"}");
 					}else{
 						new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Operation Created for Scanning and Tracking\"}");
 					}
 					query="select id,inserttime from oprrationdata where id=(select max(id) from oprrationdata)";
 					jsonObject=operation.getJson(query).getJSONObject(0);
 					HttpSession session=req.getSession(false);
 					session.setAttribute("startTime",jsonObject.getString("inserttime"));
 					
 					if(!oprType.equals("1") && oldData.equalsIgnoreCase("true")) 
 					{
 						query="select useOldNetworkScannerData()";
 						operation.getJson(query);
 						String response = "";
 						if(oprType.equals("2")) 
 						{
 							response=new ApiCommon().startTracking(givenPLMNs);
 							
 							String result = new JSONObject(response).getString("result");
 							if(result.equalsIgnoreCase("fail")) 
 							{
 								co.executeDLOperation("update oprrationdata set status='2',stoptime=timezone('utc'::text, now()) where id=(select max(id) from oprrationdata)");
 							}
 						}
 						else 
 						{
 							response = "{\"result\":\"success\",\"msg\":\"Trigger Mode Started\"}";
 						}
 						
 						new AutoOperationServer().sendText(response);
 						return Response.status(201).entity(response).build();
 					}	
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
 			try{
 			fileLogger.debug("1bcommonservice");
 	   		netscanData =  operation.getJson("select * from view_btsinfo where code=3").getJSONObject(0);
 	   		JSONObject scanDataObj=new JSONObject();
 	   		scanDataObj.put("CMD_CODE","START_EXHAUSTIVE_SCAN");
 	   		scanDataObj.put("RSSI_THRESHOLD","-110");
 	   		
 	   		
 	   		if(oprType.equals("1")) 
        		{
        			scanDataObj.put("REPETITION_FLAG","1");
            		scanDataObj.put("REPITITION_FREQ","10");
        		}
        		else 
        		{
        			scanDataObj.put("REPETITION_FLAG","0");
            		scanDataObj.put("REPITITION_FREQ","0");
        		}
 	   		
 	   		
 	   		scanDataObj.put("CMD_CODE","START_EXHAUSTIVE_SCAN");
 	   		
 	   		String[] gsmBandArr=oprGsmBand.split(",");

 	   		JSONObject gsmBandObj=getBandWiseJson(oprGsmBand,"GSM");
 	   		JSONObject umtsBandObj=getBandWiseJson(oprUmtsBand,"UMTS");
 	   		JSONObject lteBandObj=getBandWiseJson(oprLteBand,"LTE");
 	   		fileLogger.debug("1acommonservice at time:"+new Date());
 	   		JSONArray fullBandArr=new JSONArray();
 	   		fullBandArr.put(gsmBandObj);
 	   		fullBandArr.put(umtsBandObj);
 	   		fullBandArr.put(lteBandObj);
 	   		scanDataObj.put("SCAN_LIST",fullBandArr);
 	   		
 	   		//String scanDataStr=scanDataObj.toString();
 	   		String scanDataStr=getScanJsonInOrder(scanDataObj,"start");
 	   		parameter.put("cmdType", "START_EXHAUSTIVE_SCAN");
 	   		parameter.put("systemCode", "3");
 		    parameter.put("systemIP",netscanData.getString("ip"));
 		    parameter.put("systemId",netscanData.getString("sytemid"));
 		    parameter.put("id",netscanData.getString("b_id"));
 		    parameter.put("data", scanDataStr);
 			fileLogger.debug("1gcommonservice");
 			
 			//new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Data setup done\"}");
 	   		
 			preScanMaxId=operation.getJson("select max(id) max_count from netscan_cell_scan_report").getJSONObject(0).getInt("max_count");
 	   		//int duration=Integer.parseInt(oprDuration);
 	   		fileLogger.debug("preScanMaxId:"+preScanMaxId+" at time:"+new Date());
 	   		fileLogger.debug("1ecommonservice");
 	   		HashMap<String,ArrayList<HashMap<String,String>>> devicesMapOverTech = new Operations().getAllBtsInfoByTech();
 	   		new ApiCommon().lockUnlockAllDevices(devicesMapOverTech,1);
 				param = prepareParamForStopNetscan(netscanData,"GSM");
 				netscanOperations.sendToServer(param);
 				param = prepareParamForStopNetscan(netscanData,"UMTS");
 				netscanOperations.sendToServer(param);
 				param = prepareParamForStopNetscan(netscanData,"LTE");
 				netscanOperations.sendToServer(param);
 				
 				fileLogger.debug("1fcommonservice at time:"+new Date());
 				
 	   		
 				//new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Stopping Scan\"}");	
 				//Thread.sleep(60000);
 				//new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Starting Scan\"}");
 				
 				
 				JSONArray countOpr1= new Operations().getJson("select count(*) as countopr from oprrationdata where status  = '1' ");
 	   			
 	   			
 				if(countOpr1.getJSONObject(0).getInt("countopr") >=1)
 				{
 					netscanOperations.sendToServer(parameter);
 				}
 				int scanCount=0;
 				if(!oprGsmBand.equals("")){
 					scanMessage.append("GSM");
 					scanCount++;
 				}
 				if(!oprUmtsBand.equals("")){
 					if(scanCount==0){
 						scanMessage.append("UMTS");
 						scanCount++;
 					}else{
 						scanMessage.append(",UMTS");
 					}
 				}
 				if(!oprLteBand.equals("")){
 					if(scanCount==0){
 						scanMessage.append("LTE");
 					}else{
 						scanMessage.append(",LTE");	
 					}
 				}
 				new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Scan Started for "+scanMessage.toString()+"\"}");
 			}
 			catch(Exception E)
 			{
 				co.executeDLOperation("update oprrationdata set status='2',stoptime=timezone('utc'::text, now()) where id=(select max(id) from oprrationdata)");
 				new AutoOperationServer().sendText("{\"result\":\"fail\",\"msg\":\"Network Scanning Error\"}");
 				new AutoOperationServer().sendText("{\"result\":\"fail\",\"msg\":\"Operation Interrupted\"}");
 				fileLogger.debug("Exception: "+E.getMessage()+" at time:"+new Date());
 				rs.put("result", "failure");
 				return Response.status(201).entity(rs).build();
 			}
 	   		if(oprType.equals("1")) 
 	   		{
 	   			
 	   		try{
 				while(true) 
 				{
 					//if(iterationCount != 0) 
 					//{
 		   			String query1 = "select extract(epoch from t_stoptime)*1000 as stoptime from oprrationdata order by id desc limit 1";
 					fileLogger.debug(query+" at time:"+new Date());
 					JSONArray rs1 =  new Operations().getJson(query1);
 						fileLogger.debug("line 355:"+System.currentTimeMillis()+","+rs1.getJSONObject(0).getLong("stoptime"));
 						if (System.currentTimeMillis()>=rs1.getJSONObject(0).getLong("stoptime")) 
 						{
 							
 							
 							
 							param = prepareParamForStopNetscan(netscanData,"GSM");
 			   				netscanOperations.sendToServer(param);
 			   				param = prepareParamForStopNetscan(netscanData,"UMTS");
 			   				netscanOperations.sendToServer(param);
 			   				param = prepareParamForStopNetscan(netscanData,"LTE");
 			   				co.executeDLOperation("update oprrationdata set status='0',stoptime=timezone('utc'::text, now()) where id=(select max(id) from oprrationdata)");
 			   				new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Scan Completed\"}");
 			   				rs.put("result", "success");
 							return Response.status(201).entity(rs).build();
 			   				//break;
 						}
 					//}
 					
 		   		}
 	   		}catch(Exception E){
 				rs.put("result", "failure");
 				co.executeDLOperation("update oprrationdata set status='2',stoptime=timezone('utc'::text, now()) where id=(select max(id) from oprrationdata)");
 				new AutoOperationServer().sendText("{\"result\":\"fail\",\"msg\":\"Scan Failed\"}");
 				new AutoOperationServer().sendText("{\"result\":\"fail\",\"msg\":\"Operation Interrupted\"}");
 				fileLogger.debug("Exception: "+E.getMessage()+" at time:"+new Date());
 				return Response.status(201).entity(rs).build();
 			}
 	   		}
 	   		else{
 	   		
 	   		try{
 	   			
 	   		
 	   		new CurrentNetscanAlarm();
 	   		fileLogger.debug("Clearing Previous Alarms"+" at time:"+new Date());
 	   		long startTime = System.currentTimeMillis();
 	   		fileLogger.debug("Clearing Previous Alarms_2");
 	   		fileLogger.debug("scanTime is :"+scanTime);
 	   		fileLogger.debug("complete is :"+((System.currentTimeMillis() - startTime) < scanTime));
    			while((System.currentTimeMillis() - startTime) < scanTime) 
 	   		{
    				fileLogger.debug("desc in loop is :"+CurrentNetscanAlarm.desc);
    				if(CurrentNetscanAlarm.desc!=null && CurrentNetscanAlarm.desc.contains("Scan Successfully stopped  for EXHAUSTIVE_SCAN")) 
 	   			{
 	   				new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Scan Completed\"}");
 	   				fileLogger.debug("successfully stopped exhaustive scan alarm occured at time:"+new Date());
 	   				break;
 	   			}
 	   			
 	   		}	
    			fileLogger.debug("Clearing Previous Alarms_3 at time:"+new Date());
 	   		int afterScanMaxId=operation.getJson("select max(id) max_count from netscan_cell_scan_report").getJSONObject(0).getInt("max_count");
 	   		
 	   		fileLogger.debug("first scan value"+preScanMaxId+","+afterScanMaxId+" at time:"+new Date());
 	   		fileLogger.debug("afterScanMaxId:"+afterScanMaxId);
 	   		
 	   		if(preScanMaxId>=afterScanMaxId){
 	   			JSONArray countOpr= new Operations().getJson("select count(*) as countopr from oprrationdata where status  = '1' ");
 	   			
 	   			
 	   			if(countOpr.getJSONObject(0).getInt("countopr") >=1)
 	   			{
 	   				netscanOperations.sendToServer(parameter);
 	   				new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Scan Started again for "+scanMessage.toString()+"\"}");
 	   			
 	   			
 		   			new CurrentNetscanAlarm();
 		   			startTime = System.currentTimeMillis();
 		   			while((System.currentTimeMillis() - startTime) < scanTime) 
 			   		{
 		   				fileLogger.debug("desc in 2nd loop is :"+CurrentNetscanAlarm.desc);
 			   			if(CurrentNetscanAlarm.desc!=null && CurrentNetscanAlarm.desc.contains("Scan Successfully stopped  for EXHAUSTIVE_SCAN")) 
 			   			{
 			   				new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Scan Completed\"}");
 			   				fileLogger.debug("successfully stopped 2nd exhaustive scan alarm occured at time:"+new Date());
 			   				break;
 			   			}
 			   			
 			   		}
 	   			}
 		   			int afterSecondScanMaxId=operation.getJson("select max(id) max_count from netscan_cell_scan_report").getJSONObject(0).getInt("max_count");
 		   			fileLogger.debug("second scan value"+preScanMaxId+","+afterSecondScanMaxId+" at time:"+new Date());
 		   			
 		   			fileLogger.debug("afterSecondScanMaxId:"+afterSecondScanMaxId);
 	   			
 	   			if(preScanMaxId>=afterSecondScanMaxId)
 	   			{
 	   				fileLogger.debug("No Netscan Records Present.About to Exit.Operation Countinue at time:"+new Date());
 	   				
 	   				co.executeDLOperation("update oprrationdata set status='0',stoptime=timezone('utc'::text, now()) where id=(select max(id) from oprrationdata)");
 	   			    new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"No Scanning Records Found\"}");
 	   			    new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Operation Completed\"}");
 	   			    rs.put("result", "failure");
 	   			    return Response.status(201).entity(rs).build();
 	   			    
 	   			}
 	   			else
 	   			{

 	   				new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Tracking Started\"}");
 	   				String response=new ApiCommon().startTracking(givenPLMNs);
 	   				JSONObject responseJson=new JSONObject(response);
 	   				if(responseJson.getString("result").equals("success")){
 	   					co.executeDLOperation("update oprrationdata set status='0',stoptime=timezone('utc'::text, now()) where id=(select max(id) from oprrationdata)");
 	   					new AutoOperationServer().sendText(response);
 	   					new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Operation Completed\"}");
 	   				}else{
 	   					co.executeDLOperation("update oprrationdata set status='2',stoptime=timezone('utc'::text, now()) where id=(select max(id) from oprrationdata)");
 	   					new AutoOperationServer().sendText(response);
 	   					new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Operation Interrupted\"}");
 	   				}
 	   				
 	   				return Response.status(201).entity(response).build();
 	   			}
 	   		}else{
 	   				fileLogger.debug("started tracking at time:"+new Date());
 	   				new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Tracking Started\"}");
 	   				String rs1=new ApiCommon().startTracking(givenPLMNs);
 	   				JSONObject rs1Json=new JSONObject(rs1);
 	   				if(rs1Json.getString("result").equals("success")){
 	   					co.executeDLOperation("update oprrationdata set status='0',stoptime=timezone('utc'::text, now()) where id=(select max(id) from oprrationdata)");
 	   					new AutoOperationServer().sendText(rs1);
 	   					new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Operation Completed\"}");
 	   				}else{
 	   					co.executeDLOperation("update oprrationdata set status='2',stoptime=timezone('utc'::text, now()) where id=(select max(id) from oprrationdata)");
 	   					new AutoOperationServer().sendText(rs1);
 	   					new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Operation Interrupted\"}");
 	   				}
 	   				return Response.status(201).entity(rs1).build();
 	   				//return Response.status(201).entity(rs1).build();
 	   				
 	   				
 	   				
 	   		}
 			}catch(Exception E){
 			rs.put("result", "failure");
 			co.executeDLOperation("update oprrationdata set status='2',stoptime=timezone('utc'::text, now()) where id=(select max(id) from oprrationdata)");
 			new AutoOperationServer().sendText("{\"result\":\"fail\",\"msg\":\"Error\"}");	
 			new AutoOperationServer().sendText("{\"result\":\"fail\",\"msg\":\"Operation Interrupted\"}");	
 			fileLogger.debug("Exception: "+E.getMessage()+" at time:"+new Date());
 			E.printStackTrace();
 			return Response.status(201).entity(rs).build();
 			}
 			}
     }*/

 public String getScanJsonInOrder(JSONObject scanDataObj, String scanType) {
  fileLogger.info("Inside Function : getScanJsonInOrder");
  String jsonStringInOrder = "";
  try {
   if (scanType.equals("start")) {
    jsonStringInOrder += "{\"CMD_CODE\":\"START_EXHAUSTIVE_SCAN\",\"RSSI_THRESHOLD\":" + scanDataObj.getInt("RSSI_THRESHOLD") + ",\"REPETITION_FLAG\":" + scanDataObj.getInt("REPETITION_FLAG") + ",\"REPITITION_FREQ\":" + scanDataObj.getInt("REPITITION_FREQ") + ",\"SCAN_LIST\":" + scanDataObj.getJSONArray("SCAN_LIST").toString() + "}";
   } else {

   }
  } catch (Exception E) {
   fileLogger.error("Exception: " + E.getMessage());
  }
  fileLogger.info("Exit Function : getScanJsonInOrder");
  return jsonStringInOrder;
 }

 @POST
 @Path("/stopOperation")
 @Produces(MediaType.APPLICATION_JSON)
 public Response stopOperation(@Context HttpServletRequest req) {
  return stopRunningOperationFalcon();
 }

 public Response stopRunningOperationFalcon() {
  fileLogger.info("Inside Function : stopRunningOperationFalcon");
  //fileLogger.debug("in stopRunningOperationFalcon");
  DBDataService dbDataService = DBDataService.getInstance();
  String currentEventName = dbDataService.getCurrentEventName();
  try {
	  JSONArray hummerDataArray = new Operations().getJson("select * from view_btsinfo where code = 12");
		if(hummerDataArray.length()>0){
			new TRGLController().sendTrackingStatusToHummer("no");
		}
   fileLogger.debug("returned from dbDataService.stopRunningOperation()");
   fileLogger.debug("dbDataService.getCurrentEventData().getOprName()");
   if (currentEventName == null) {
    boolean updateStatus = new Common().executeDLOperation("update oprrationdata set status='0' where id=(select max(id) from oprrationdata)");
    if (updateStatus) {
     //new AuditHandler().audit_operation(Integer.toString(oprId),"Create"); 
     return Response.status(201).entity("{\"result\":\"success\",\"msg\":\"Operation Stopped Successfully\"}").build();
    }
   } else if (currentEventName.equals(Constants.schedulerEvent) || currentEventName.equals(Constants.automaticEvent)) {
    EventData currentEventData = dbDataService.getCurrentEventData();
    dbDataService.stopRunningOperation();
    boolean updateStatus = new Common().executeDLOperation("update oprrationdata set status='0',stoptime=timezone('utc'::text, now()) where id=(select max(id) from oprrationdata)");
    if (updateStatus) {
     LinkedHashMap < String, String > oprStopLog = new LinkedHashMap < String, String > ();
     oprStopLog.put("oprType", currentEventName);
     oprStopLog.put("action", "Stop");
     new AuditHandler().auditOprStop(oprStopLog);
     //new AuditHandler().audit_operation(Integer.toString(oprId),"Create"); 
     return Response.status(201).entity("{\"result\":\"success\",\"msg\":\"Operation Stopped Successfully\"}").build();
    }
    //addToEventPriorityQueue(eventObj.getString("name"),"scheduler",eventObj.getString("note"),eventObj.getString("note"),operators,eventObj.getString("distance"),"localhost",-1,"-1","-1","-1",-1,-1,eventObj.getString("inserttime"),true);
   } else {
    boolean updateStatus = new Common().executeDLOperation("update oprrationdata set status='0' where id=(select max(id) from oprrationdata)");
    if (updateStatus) {
     //new AuditHandler().audit_operation(Integer.toString(oprId),"Create"); 
     return Response.status(201).entity("{\"result\":\"success\",\"msg\":\"Operation Stopped Successfully\"}").build();
    }
   }
  } catch (UnknownHostException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  } catch (IOException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  }
  fileLogger.info("Exit Function : stopRunningOperationFalcon");
  return Response.status(201).entity("{\"result\":\"failure\",\"msg\":\"Problem in Stopping Operation\"}").build();
 }

 public Response stopRunningOperation() {
  fileLogger.info("Inside Function : stopRunningOperation");
	 //fileLogger.debug("in stopRunningOperation");
  DBDataService dbDataService = DBDataService.getInstance();
  String currentEventName = dbDataService.getCurrentEventName();
  try {
	 JSONArray hummerDataArray = new Operations().getJson("select * from view_btsinfo where code = 12");
		if(hummerDataArray.length()>0){
			new TRGLController().sendTrackingStatusToHummer("no");
		}
   EventData currentEventData = dbDataService.getCurrentEventData();
   dbDataService.stopRunningOperation();
   fileLogger.debug("returned from dbDataService.stopRunningOperation()");
   fileLogger.debug("dbDataService.getCurrentEventData().getOprName()");
   if (currentEventName == null) {
    boolean updateStatus = new Common().executeDLOperation("update oprrationdata set status='0' where id=(select max(id) from oprrationdata)");
    //if(updateStatus){
    //new AuditHandler().audit_operation(Integer.toString(oprId),"Create"); 
    return Response.status(201).entity("{\"result\":\"success\",\"msg\":\"Operation Stopped Successfully\"}").build();
    //} 
   } else if (currentEventName.equals(Constants.schedulerEvent) || currentEventName.equals(Constants.automaticEvent)) {
    boolean updateStatus = new Common().executeDLOperation("update oprrationdata set status='0',stoptime=timezone('utc'::text, now()) where name='" + dbDataService.getCurrentEventData().getOprName() + "'");
    //if(updateStatus){
    //new AuditHandler().audit_operation(Integer.toString(oprId),"Create"); 
    return Response.status(201).entity("{\"result\":\"success\",\"msg\":\"Operation Stopped Successfully\"}").build();
    // } 
    //addToEventPriorityQueue(eventObj.getString("name"),"scheduler",eventObj.getString("note"),eventObj.getString("note"),operators,eventObj.getString("distance"),"localhost",-1,"-1","-1","-1",-1,-1,eventObj.getString("inserttime"),true);
   }
  } catch (UnknownHostException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  } catch (IOException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  }
  fileLogger.info("Exit Function : stopRunningOperation");
  return Response.status(201).entity("{\"result\":\"failure\",\"msg\":\"Problem in Stopping Operation\"}").build();
 }

 /*   public Response stopRunningOperation(){
 	   final HashMap<String,ArrayList<HashMap<String,String>>> devicesMapOverTech = new Operations().getAllBtsInfoByTech(); 
 	   new ApiCommon().lockUnlockAllDevices(devicesMapOverTech,1);
 		NetscanOperations netscanOperations=new NetscanOperations();
 		LinkedHashMap<String,String> param = new LinkedHashMap<String,String>();
 		JSONObject netscanData=null;
 		
 		String query1="select opr_type,use_last_scanned_data from oprrationdata where status ='1' and id =(select max(id) from oprrationdata)";
 		JSONArray js = new Operations().getJson(query1);
 		
 		try {
 			int type = Integer.parseInt(js.getJSONObject(0).getString("opr_type"));
 			String use_last_scanned_data = js.getJSONObject(0).getString("use_last_scanned_data");
 			
 			if(type  == 1 || use_last_scanned_data.equalsIgnoreCase("false")) 
 			{
 				try{
 					netscanData =  new Operations().getJson("select * from view_btsinfo where code=3").getJSONObject(0);
 					}catch(Exception E){
 						E.printStackTrace();
 					}
 					param=prepareParamForStopNetscan(netscanData,"GSM");
 					netscanOperations.sendToServer(param);
 					param=prepareParamForStopNetscan(netscanData,"UMTS");
 					netscanOperations.sendToServer(param);
 					param=prepareParamForStopNetscan(netscanData,"LTE");
 					netscanOperations.sendToServer(param);
 			}
 		} catch (NumberFormatException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		} catch (JSONException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
 		boolean updateStatus=new Common().executeDLOperation("update oprrationdata set status='0',t_stoptime=inserttime,stoptime=timezone('utc'::text, now()) where id=(select max(id) from oprrationdata)");
 	   if(updateStatus){
 		   if(CurrentOperation.tt != null)
 		   CurrentOperation.tt.interrupt();
 		   return Response.status(201).entity("{\"result\":\"success\",\"msg\":\"Operation Stopped Successfully\"}").build();   
 	   }
 	       return Response.status(201).entity("{\"result\":\"success\",\"msg\":\"Problem in Stopping Operation\"}").build(); 
    }*/

 public LinkedHashMap < String, String > prepareParamForStopNetscan(JSONObject netscanData, String tech) {
  JSONObject scanStopDataObj = new JSONObject();
  fileLogger.info("Inside Function : prepareParamForStopNetscan");
  LinkedHashMap < String, String > param = new LinkedHashMap < String, String > ();
  try {
   scanStopDataObj.put("cmdType", "STOP_SCAN");
   scanStopDataObj.put("systemId", netscanData.getString("sytemid"));
   scanStopDataObj.put("systemCode", "3");
   scanStopDataObj.put("systemIp", netscanData.getString("ip"));
   scanStopDataObj.put("id", netscanData.getString("b_id"));
   JSONObject scanTypeObj = new JSONObject();

   scanTypeObj.put("CMD_CODE", "STOP_SCAN");
   scanTypeObj.put("TECH", tech);
   scanTypeObj.put("SCAN_TYPE", "3");
   scanStopDataObj.put("data", scanTypeObj);
   String scanStopDataStr = scanStopDataObj.toString();

   param.put("cmdType", "STOP_SCAN");
   param.put("systemCode", "3");
   param.put("systemIP", netscanData.getString("ip"));
   param.put("systemId", netscanData.getString("sytemid"));
   param.put("id", netscanData.getString("b_id"));
   param.put("data", scanStopDataStr);
  } catch (JSONException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  }
  fileLogger.info("Exiting Function : prepareParamForStopNetscan");
  return param;
 }

 public JSONObject getBandWiseJson(String oprGsmBand, String tech) {
  fileLogger.info("Inside Function : getBandWiseJson");
  String[] bandArr = oprGsmBand.split(",");
  JSONObject tempBandObj = new JSONObject();

  try {
   tempBandObj.put("TECH", tech);
   JSONArray tempBandArr = new JSONArray();
   for (String band: bandArr) {
    JSONObject bandObj = new JSONObject();
    bandObj.put("BAND", band);
    tempBandArr.put(bandObj);
   }
   tempBandObj.put("BAND_LIST", tempBandArr);
  } catch (JSONException e) {
   // TODO Auto-generated catch block
   fileLogger.error("JSON Exception in getBandWiseJson in CommonService");
   e.printStackTrace();
  }
  fileLogger.info("Exit Function : getBandWiseJson");
  return tempBandObj;
 }


 @POST
 @Path("/addGPSAccurecyNumber")
 @Produces(MediaType.APPLICATION_JSON)
 public Response addGPSAccurecyNumber(@Context HttpServletRequest req, @FormParam("accuracy") String accuracy) {
  fileLogger.info("Inside Function : addGPSAccurecyNumber");
  fileLogger.debug("all data is :" + accuracy + " " + accuracy);
  Common co = new Common();
  String query = "insert into gps_accuracy(accuracy) values(" + accuracy + ")";
  boolean status = co.executeDLOperation(query);
  HashMap < String, String > rs = new HashMap < String, String > ();
  try {
   if (status) {
    rs.put("result", "success");
   } else {
    rs.put("result", "fail");
   }
  } catch (Exception E) {
   rs.put("result", "fail");
   fileLogger.error("Exception: " + E.getMessage());
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
 public Response getGpsAccuracy() { //String query = "select  btsmaster.b_id,btsmaster.ip,btsmaster.status,btsnetworktype.name from btsmaster inner join btsnetworktype on(typeid = n_id)";
  fileLogger.info("Inside Function : getGpsAccuracy");
  String query = "select  * from gps_accuracy order by id desc limit 1";
  fileLogger.debug(query);
  JSONArray rs = new Operations().getJson(query);
  fileLogger.info("Exit Function : getGpsAccuracy");
  return Response.status(201).entity(rs.toString()).build();
 }


 /*
  * This Method will get all the bts present in to the btsmaster info
  * */
 @POST
 @Path("/btsinfo")
 @Produces(MediaType.APPLICATION_JSON)
 public Response getBTS() { //String query = "select  btsmaster.b_id,btsmaster.ip,btsmaster.status,btsnetworktype.name from btsmaster inner join btsnetworktype on(typeid = n_id)";
  fileLogger.info("Inside Function : getBTS");
	 //String query = "select  * from view_btsinfo order by grp_id,code";
  String query = "SELECT vb.*,case when code in(0,1,2) then oprname(concat(config -> \'SYS_PARAMS\'->\'CELL_INFO\'->\'PLMN_ID\' ->>\'MCC\'," +
   "config -> \'SYS_PARAMS\'->\'CELL_INFO\'->\'PLMN_ID\' ->>\'MNC\')::numeric) when code=\'5\' then oprname(ncd.plmn::numeric) end  oprname" +
   " from view_btsinfo vb left join n_cells_data ncd on vb.ip=ncd.ip where type=\'S\' or type is null  order by grp_id,code";
  fileLogger.debug(query);
  JSONArray rs = new Operations().getJson(query);
  fileLogger.info("Exit Function : getBTS");
  return Response.status(201).entity(rs.toString()).build();
 }

 @POST
 @Path("/scanandtrackmode")
 @Produces(MediaType.APPLICATION_JSON)
 public Response getScanAndTrackMode() { //String query = "select  btsmaster.b_id,btsmaster.ip,btsmaster.status,btsnetworktype.name from btsmaster inner join btsnetworktype on(typeid = n_id)";
  fileLogger.info("Inside Function : getScanAndTrackMode"); 
	 //String query = "select  * from view_btsinfo order by grp_id,code";
  String query = "select * from running_mode";
  fileLogger.debug(query);
  JSONArray rs = new Operations().getJson(query);
  fileLogger.info("Exit Function : getScanAndTrackMode"); 
  return Response.status(201).entity(rs.toString()).build();
 }

 @POST
 @Path("/systemlocation")
 @Produces(MediaType.APPLICATION_JSON)
 public Response getSystemLocation() { //String query = "select  btsmaster.b_id,btsmaster.ip,btsmaster.status,btsnetworktype.name from btsmaster inner join btsnetworktype on(typeid = n_id)";
  fileLogger.info("Inside Function : getSystemLocation"); 
	 //String query = "select  * from view_btsinfo order by grp_id,code";
  String query = "select lat,lon from gpsdata order by id desc limit 1";
  fileLogger.debug(query);
  JSONArray rs = new Operations().getJson(query);
  fileLogger.info("Exit Function : getSystemLocation"); 
  return Response.status(201).entity(rs.toString()).build();
 }


 @POST
 @Path("/usedspace")
 @Produces(MediaType.APPLICATION_JSON)
 public Response getUsedSpace() { //String query = "select  btsmaster.b_id,btsmaster.ip,btsmaster.status,btsnetworktype.name from btsmaster inner join btsnetworktype on(typeid = n_id)";
  //String query = "select  * from view_btsinfo order by grp_id,code";
  String usedSpace = new CmsTrigger().getUsedSpacePercentage();
  return Response.status(201).entity(usedSpace).build();
 }


 /*
  * This Method will get all the 3G bts present in to the btsmaster info
  * */
 @POST
 @Path("/getall3gdevicelist")
 @Produces(MediaType.APPLICATION_JSON)
 public Response getAll3gDeviceList() { //String query = "select  btsmaster.b_id,btsmaster.ip,btsmaster.status,btsnetworktype.name from btsmaster inner join btsnetworktype on(typeid = n_id)";
  String query = "select  * from view_btsinfo where lower(use_type_name)='3g locator' order by grp_id,code";
  fileLogger.debug(query);
  JSONArray rs = new Operations().getJson(query);
  fileLogger.info("Exit Function : getAll3gDeviceList");
  return Response.status(201).entity(rs.toString()).build();
 }

 /*
  * This Method will get all the bts present in to the btsmaster info
  * */
 @POST
 @Path("/getHwCapabilityTypes")
 @Produces(MediaType.APPLICATION_JSON)
 public Response getHwCapabilityTypes() { //String query = "select  btsmaster.b_id,btsmaster.ip,btsmaster.status,btsnetworktype.name from btsmaster inner join btsnetworktype on(typeid = n_id)";
  fileLogger.info("Inside Function : getHwCapabilityTypes");
	 String query = "select  id,name from hw_capability where status=TRUE order by name";
  fileLogger.debug(query);
  JSONArray rs = new Operations().getJson(query);
  fileLogger.info("Exit Function : getHwCapabilityTypes");
  return Response.status(201).entity(rs.toString()).build();
 }

 /*
  * This Method will get all the bts present in to the btsmaster info
  * */
 @POST
 @Path("/getUseTypes")
 @Produces(MediaType.APPLICATION_JSON)
 public Response getUseTypes() { //String query = "select  btsmaster.b_id,btsmaster.ip,btsmaster.status,btsnetworktype.name from btsmaster inner join btsnetworktype on(typeid = n_id)";
  fileLogger.info("Inside Function : getUseTypes");
	 String query = "select id,name,type,modal_id,show_name from use_type where status=TRUE";
  fileLogger.debug(query);
  JSONArray rs = new Operations().getJson(query);
  fileLogger.info("Exit Function : getUseTypes");
  return Response.status(201).entity(rs.toString()).build();
 }

 /*
  * This Method will get all the bts present in to the btsmaster info
  * */
 @POST
 @Path("/getantennatypes")
 @Produces(MediaType.APPLICATION_JSON)
 public Response getAntennaTypes() { //String query = "select  btsmaster.b_id,btsmaster.ip,btsmaster.status,btsnetworktype.name from btsmaster inner join btsnetworktype on(typeid = n_id)";
 fileLogger.info("Inside Function : getAntennaTypes");
	 String query = "select  * from antenna_type where type_id=4";
  fileLogger.debug(query);
  JSONArray rs = new Operations().getJson(query);
  fileLogger.info("Exit Function : getAntennaTypes");
  return Response.status(201).entity(rs.toString()).build();
 }

 @POST
 @Path("/getAntennaProfiles")
 @Produces(MediaType.APPLICATION_JSON)
 public Response getAntennaProfiles() { //String query = "select  btsmaster.b_id,btsmaster.ip,btsmaster.status,btsnetworktype.name from btsmaster inner join btsnetworktype on(typeid = n_id)";
  fileLogger.info("Inside Function : getAntennaProfiles");
	 String query = "select * from antenna a join antenna_type at on a.atype::integer=at.type_id order by a.profile_name";
  fileLogger.debug(query);
  JSONArray rs = new Operations().getJson(query);
  fileLogger.info("Exit Function : getAntennaProfiles");
  return Response.status(201).entity(rs.toString()).build();
 }

 @POST
 @Path("/checkoperation")
 @Produces(MediaType.APPLICATION_JSON)
 public Response checkOperation(@Context HttpServletRequest req) {
  String query = "select count(*) count from oprrationdata where status!='0'";
  int count = -1;
  HashMap < String, String > rs = new HashMap < String, String > ();
  new CommonOperation().updateScanningAndTrackingAntennaInDevices();
  try {
   count = new Operations().getJson(query).getJSONObject(0).getInt("count");
  } catch (JSONException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  }
  if (count == 0) {
   rs.put("result", "success");
  } else {
   rs.put("result", "failure");
  }
  return Response.status(201).entity(rs).build();
 }

 @POST
 @Path("/validateOperation")
 @Produces(MediaType.APPLICATION_JSON)
 public Response validateOperation(@Context HttpServletRequest req, @FormParam("oprName") String oprName, @FormParam("oprType") String oprType, @FormParam("old_data") String old_data) {


  String res = new OperationCalculations().validateOpertaion(oprType, old_data, oprName);

  return Response.status(201).entity(res).build();
 }

 @POST
 @Path("/addAntennaProfile")
 @Produces(MediaType.APPLICATION_JSON)
 public Response addAntennaProfile(@Context HttpServletRequest req, @FormParam("antennaScanning") boolean antennaScanning, @FormParam("antennaTracking") boolean antennaTracking, @FormParam("antennaName") String antennaName, @FormParam("antennaTxPower") String antennaTxPower, @FormParam("antennaType") String antennaType, @FormParam("antennaBand") String antennaBand, @FormParam("antennaGain") String antennaGain, @FormParam("antennaElevation") String antennaElevation, @FormParam("antennaHBW") String antennaHBW, @FormParam("antennaVBW") String antennaVBW, @FormParam("antennaTilt") String antennaTilt, @FormParam("antennaAzimuth") String antennaAzimuth, @FormParam("antennaTerrain") String antennaTerrain) {
  Common co = new Common();
  HashMap < String, String > rs = new HashMap < String, String > ();
  String query = "INSERT INTO antenna(profile_name, txpower, atype, band, gain, elevation, hbw, vbw, tilt, azimuth, terrain,inscanning,intracking) VALUES('" + antennaName + "','" + antennaTxPower + "','" + antennaType + "','" + antennaBand + "','" + antennaGain + "','" + antennaElevation + "','" + antennaHBW + "','" + antennaVBW + "','" + antennaTilt + "','" + antennaAzimuth + "','" + antennaTerrain + "'," + antennaScanning + "," + antennaTracking + ") returning id";
  int id = co.executeQueryAndReturnId(query);
  rs.put("result", "success");
  rs.put("id", Integer.toString(id));
  new CommonOperation().updateScanningAndTrackingAntennaInDevices();
  return Response.status(201).entity(rs).build();
 }

 @POST
 @Path("/updateantennaprofile")
 @Produces(MediaType.APPLICATION_JSON)
 public Response updateAntennaProfile(@Context HttpServletRequest req, @FormParam("antennaProfileData") String antennaProfileData) {
  Common co = new Common();
  HashMap < String, String > rs = new HashMap < String, String > ();
  try {
   LinkedHashMap < String, String > log = new LinkedHashMap < String, String > ();

   JSONObject antennaProfileStr = new JSONObject(antennaProfileData);
   String query = "";
   JSONArray antennaProfileList = antennaProfileStr.getJSONArray("profileData");
   List < String > queryList = new ArrayList < String > ();

   int s1Angle=Integer.parseInt(antennaProfileStr.getString("s1Angle"));
   log.put("Antenna Update Count", Integer.toString(antennaProfileList.length()));

   fileLogger.debug("INside fxn move s1Angle:"+s1Angle);
   int maxAngle=Integer.parseInt(DBDataService.getConfigParamMap().get("maxAngle"));
   int minAngle=Integer.parseInt(DBDataService.getConfigParamMap().get("minAngle"));
   

   for (int i = 0; i < antennaProfileList.length(); i++) {
    JSONObject antennaProfile = antennaProfileList.getJSONObject(i);

    query = "update antenna set txpower='" + antennaProfile.getString("txPower") + "',band='" + antennaProfile.getString("band") + "',gain='" + antennaProfile.getString("gain") + "',elevation='" + antennaProfile.getString("elevation") + "',hbw='" + antennaProfile.getString("hbw") + "',vbw='" + antennaProfile.getString("vbw") + "',tilt='" + antennaProfile.getString("tilt") + "',azimuth='" + antennaProfile.getString("azimuth") + "',terrain='" + antennaProfile.getString("terrain") + "',inscanning=" + antennaProfile.getBoolean("inScanning") + ",intracking=" + antennaProfile.getBoolean("inTracking")+ " where id='" + antennaProfile.getString("id") + "'";
   
    

//       int currentAngle = -1;
//        JSONArray PTZserverIP=new Operations().getJson("select ip from view_btsinfo where code = 9");
//     	
//     	String PTZip = PTZserverIP.getJSONObject(0).getString("ip");
//     	  
     		
	  
	   
	//	String currentAngleSaved1="";
	//	int currentAngleSaved=-1;
//	//	String[]  ptzArr = {};
//		try {
//		//	//currentAngleSaved1 = PTZ.getCurrentAngle(PTZip);
//		//	fileLogger.info("Inside Function : checkantenatypeandmovesection ... currentAngleSaved1 = "+currentAngleSaved1);
//		//	//currentAngleSaved1.split(":");
//		//	ptzArr = currentAngleSaved1.split(":");
//		//	fileLogger.debug("INside fxn move ptzArr:"+ptzArr);
//		//	currentAngleSaved=Integer.parseInt(ptzArr[0]);
//		} catch (IOException e2) {
//			// TODO Auto-generated catch block
//			e2.printStackTrace();
//		}
		
    
    
    
    
    if(antennaProfile.getString("name").equalsIgnoreCase("s1") && (antennaProfile.getBoolean("inTracking")|| antennaProfile.getBoolean("inScanning") ))
	{
    	if(s1Angle > maxAngle ||  s1Angle <minAngle)
    	{
    		  rs.put("result", "error");
    		  return Response.status(201).entity(rs).build();

    	}
	}

    else if(antennaProfile.getString("name").equalsIgnoreCase("s2") && (antennaProfile.getBoolean("inTracking")|| antennaProfile.getBoolean("inScanning") ))
	{
    	if(s1Angle+60 > maxAngle ||  s1Angle < minAngle)
    	{
    		 rs.put("result", "error");
   		  return Response.status(201).entity(rs).build();

    	}
	}
    else if(antennaProfile.getString("name").equalsIgnoreCase("s3") && (antennaProfile.getBoolean("inTracking")|| antennaProfile.getBoolean("inScanning") ))
  	{
      	if(s1Angle+120 > maxAngle ||  s1Angle < minAngle)
      	{
      		 rs.put("result", "error");
   		  return Response.status(201).entity(rs).build();

      	}
  	}

    	
    queryList.add(query);
    log.put(antennaProfile.getString("name") + "-scanning", Boolean.toString(antennaProfile.getBoolean("inScanning")));
    log.put(antennaProfile.getString("name") + "-tracking", Boolean.toString(antennaProfile.getBoolean("inTracking")));

   }
   String query1 = "update antenna set antenna_type=" +  antennaProfileStr.getInt("antennaTypeRotate")+ ",antenna_angle=" +  antennaProfileStr.getInt("s1Angle"); 
   queryList.add(query1);
   co.executeBatchOperation(queryList);
   String angleOffset = antennaProfileStr.getString("angleOffset");
   String current_angle=antennaProfileStr.getString("s1Angle");
   String typeFixedOrRotating=antennaProfileStr.getString("antennaTypeRotate");
   if(typeFixedOrRotating.equalsIgnoreCase("2")) {
	   typeFixedOrRotating="Rotating";
   }
   else {
	   typeFixedOrRotating="Fixed";
   }
   fileLogger.debug("Inside updateAntennaProfile current_angle = "+current_angle);
   fileLogger.debug("Inside updateAntennaProfile typeFixedOrRotating = "+typeFixedOrRotating);
   
   
   log.put("Current Angle", current_angle);
   log.put("TYPE", typeFixedOrRotating);
   
   
   String oldAngleOffset = antennaProfileStr.getString("oldAngleOffset");
   if (!angleOffset.equals(oldAngleOffset)) {
    query = "update antenna set angle_offset=" + antennaProfileStr.getString("angleOffset") + " where atype='1'";
    co.executeQueryAndReturnId(query);
	try {
		DBDataService.setAngleOffset(Integer.parseInt(antennaProfileStr.getString("angleOffset")));
	} catch (Exception e) {
		fileLogger.error("exception in commonservice while updating angle offset");
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    new CMSController().sendOffsetToCMS(angleOffset);
    log.put("angle offset", antennaProfileStr.getString("angleOffset"));
   }

   //if (antennaProfileStr.getBoolean("scanAntChangeStatus")) {
    //stop the scanner scheduler
    try {
     DBDataService dbDataService = DBDataService.getInstance();
     dbDataService.updateAntennaMode();
     //set interrupted flag to true
     //stop the running scanning
     NetscanSchedulerListener netscanSchedulerListener = new NetscanSchedulerListener();
     netscanSchedulerListener.stopNetscanScheduler();
     //stop the scanner scheduler
     dbDataService.shutdownScannerScheduler();
     Thread.sleep(1000);
     //start the scanner scheduler
     netscanSchedulerListener.startNetscanScheduler();

    } catch (Exception e) {
     // TODO Auto-generated catch block
     e.printStackTrace();
    }
   //}

   new AuditHandler().auditAntennaProfile(log);

   rs.put("result", "success");
   new CommonOperation().updateScanningAndTrackingAntennaInDevices();
   return Response.status(201).entity(rs).build();
  } catch (JSONException e) {
   // TODO Auto-generated catch block

	  e.printStackTrace();
  }

  return null;
 }

 /* @POST
   @Path("/updateantennaprofile")
   @Produces(MediaType.APPLICATION_JSON)
   public Response updateAntennaProfile(@Context HttpServletRequest req,@FormParam("antennaScanning") boolean antennaScanning,@FormParam("antennaTracking") boolean antennaTracking,@FormParam("antennaName") String antennaName, @FormParam("antennaTxPower") String antennaTxPower,@FormParam("antennaType") String antennaType, @FormParam("antennaBand") String antennaBand,@FormParam("antennaGain") String antennaGain, @FormParam("antennaElevation") String antennaElevation,@FormParam("antennaHBW") String antennaHBW, @FormParam("antennaVBW") String antennaVBW,@FormParam("antennaTilt") String antennaTilt, @FormParam("antennaAzimuth") String antennaAzimuth,@FormParam("antennaTerrain") String antennaTerrain,@FormParam("ptzSelectionStatus") String ptzSelectionStatus)
   {
			Common co = new Common();
			HashMap<String,String> rs = new HashMap<String,String>();
			String query="select count(*) count from oprrationdata where status!='1'";
			int count=-1;
			try {
				count=new Operations().getJson(query).getJSONObject(0).getInt("count");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(count!=0){
				rs.put("result", "failure");
				rs.put("message","An Operation is in STARTED or INTERRUPTED state");
			}else{
				query = "INSERT INTO antenna(profile_name, txpower, atype, band, gain, elevation, hbw, vbw, tilt, azimuth, terrain,inscanning,intracking) VALUES('"+antennaName+"','"+antennaTxPower+"','"+antennaType+"','"+antennaBand+"','"+antennaGain+"','"+antennaElevation+"','"+antennaHBW+"','"+antennaVBW+"','"+antennaTilt+"','"+antennaAzimuth+"','"+antennaTerrain+"',"+antennaScanning+","+antennaTracking+") returning id";
				int id=co.executeQueryAndReturnId(query);
				rs.put("result", "success");
				rs.put("id",Integer.toString(id));
                new CommonOperation().updateScanningAndTrackingAntennaInDevices();
			}
            return Response.status(201).entity(rs).build();
    }*/

 @POST
 @Path("/addDevice")
 @Produces(MediaType.APPLICATION_JSON)
 public Response addDevice(@Context HttpServletRequest req, @FormParam("deviceIp") String deviceIp,@FormParam("SystemManagerIP") String SystemManagerIP, @FormParam("hwCapabilityTypeId") String hwCapabilityTypeId, @FormParam("useTypeId") String useTypeId, @FormParam("paGain") String paGain, @FormParam("paPower") String paPower) {
  
  Common co = new Common();
  int deviceTypeId = getDeviceTypeId(useTypeId);
  int deviceCode = getDeviceTypeCode(useTypeId);
  int groupId = createGroup(deviceIp + "_" + System.currentTimeMillis());
  int networkId = getNetworkId("2G");
  String antennaProfileId = "";
  String query = "";
  String antennaMappedToDevice = "";

  if (deviceCode == 3) {
   HashMap < String, String > antennaMap = new CommonOperation().getScanningAndTrackingAntenna();
   antennaMappedToDevice = antennaMap.get("scanning");
  } else if (deviceCode == 5) {
   HashMap < String, String > antennaMap = new CommonOperation().getScanningAndTrackingAntenna();
   antennaMappedToDevice = antennaMap.get("tracking");
  }

  if (hwCapabilityTypeId.equalsIgnoreCase("5")) {
   //new Common().getDbCredential().get("paport");
   //new UdpServerClient().send(deviceIp, Integer.parseInt(new Common().getDbCredential().get("paport")), "12");
   if (antennaProfileId.equals("")) {
    query = "INSERT INTO btsmaster(ip,devicetypeid,grp_id,typeid,hw_capability_id,antenna_id,config_applied_status,config,systemmanager) VALUES ('" + deviceIp + "'," + deviceTypeId + "," + groupId + "," + networkId + "," + hwCapabilityTypeId + ",null,'n','{\"controller_conf\":\"12\"}' , '"+SystemManagerIP + "' );";
   } else {
    query = "INSERT INTO btsmaster(ip,devicetypeid,grp_id,typeid,hw_capability_id,antenna_id,config_applied_status,config,systemmanager) VALUES ('" + deviceIp + "'," + deviceTypeId + "," + groupId + "," + networkId + "," + hwCapabilityTypeId + "," + antennaProfileId + ",'n','{\"controller_conf\":\"12\"}' , '"+SystemManagerIP + "' );";
   }
  } else if (hwCapabilityTypeId.equalsIgnoreCase("6")) {
   //new Common().getDbCredential().get("lnaport");
   //new UdpServerClient().send(deviceIp, Integer.parseInt(new Common().getDbCredential().get("lnaport")), "11");
   if (antennaProfileId.equals("")) {
    query = "INSERT INTO btsmaster(ip,devicetypeid,grp_id,typeid,hw_capability_id,antenna_id,config_applied_status,config,systemmanager) VALUES ('" + deviceIp + "'," + deviceTypeId + "," + groupId + "," + networkId + "," + hwCapabilityTypeId + ",null,'n','{\"controller_conf\":\"11\"}' , '"+SystemManagerIP + "' );";
   } else {
    query = "INSERT INTO btsmaster(ip,devicetypeid,grp_id,typeid,hw_capability_id,antenna_id,config_applied_status,config,systemmanager) VALUES ('" + deviceIp + "'," + deviceTypeId + "," + groupId + "," + networkId + "," + hwCapabilityTypeId + "," + antennaProfileId + ",'n','{\"controller_conf\":\"11\"}' , '"+SystemManagerIP + "' );";
   }
  } else if (hwCapabilityTypeId.equalsIgnoreCase("4")) {
   String defaultNetscanConfig = new ApiCommon().getNetscanConfigurationWithDefaultValues();
   if (antennaProfileId.equals("")) {
    query = "INSERT INTO btsmaster(ip,devicetypeid,grp_id,typeid,hw_capability_id,antenna_id,config_applied_status,config,active_antenna_id,systemmanager) VALUES ('" + deviceIp + "'," + deviceTypeId + "," + groupId + "," + networkId + "," + hwCapabilityTypeId + ",null,'n','" + defaultNetscanConfig + "',1 , '"+SystemManagerIP + "' );";
   } else {
    query = "INSERT INTO btsmaster(ip,devicetypeid,grp_id,typeid,hw_capability_id,antenna_id,config_applied_status,config,active_antenna_id,systemmanager) VALUES ('" + deviceIp + "'," + deviceTypeId + "," + groupId + "," + networkId + "," + hwCapabilityTypeId + "," + antennaProfileId + ",'n','" + defaultNetscanConfig + "',1 , '"+SystemManagerIP + "' );";
   }
  } else if (hwCapabilityTypeId.equalsIgnoreCase("1")) {
   String defaultNetscanConfig = new ApiCommon().getNetscanConfigurationWithDefaultValues();
   if (antennaProfileId.equals("")) {
    query = "INSERT INTO btsmaster(ip,devicetypeid,grp_id,typeid,hw_capability_id,antenna_id,config_applied_status,pa_gain,pa_power,antenna_apply,systemmanager) VALUES ('" + deviceIp + "'," + deviceTypeId + "," + groupId + "," + networkId + "," + hwCapabilityTypeId + ",null,'n'," + paGain + "," + paPower + ",'" + antennaMappedToDevice + "' , '"+SystemManagerIP + "' );";
   } else {
    query = "INSERT INTO btsmaster(ip,devicetypeid,grp_id,typeid,hw_capability_id,antenna_id,config_applied_status,config,systemmanager) VALUES ('" + deviceIp + "'," + deviceTypeId + "," + groupId + "," + networkId + "," + hwCapabilityTypeId + "," + antennaProfileId + ",'n','" + defaultNetscanConfig + "' , '"+SystemManagerIP + "' );";
   }
  } else {
   if (antennaProfileId.equals("")) {
    query = "INSERT INTO btsmaster(ip,devicetypeid,grp_id,typeid,hw_capability_id,antenna_id,config_applied_status,antenna_apply,systemmanager) VALUES ('" + deviceIp + "'," + deviceTypeId + "," + groupId + "," + networkId + "," + hwCapabilityTypeId + ",null,'n','" + antennaMappedToDevice + "' , '"+SystemManagerIP + "' );";
   } else {
    query = "INSERT INTO btsmaster(ip,devicetypeid,grp_id,typeid,hw_capability_id,antenna_id,config_applied_status,pa_gain,pa_power,antenna_apply,systemmanager) VALUES ('" + deviceIp + "'," + deviceTypeId + "," + groupId + "," + networkId + "," + hwCapabilityTypeId + "," + antennaProfileId + ",'n'," + paGain + "," + paPower + ",'" + antennaMappedToDevice + "' , '"+SystemManagerIP + "' );";
   }
  }


  boolean status = co.executeDLOperation(query);
  HashMap < String, String > rs = new HashMap < String, String > ();
  if (status) {
   new TwogOperations().updateStatusOfBts("'" + deviceIp + "'");
   rs.put("result", "success");

   LinkedHashMap < String, String > log = new LinkedHashMap < String, String > ();
   log.put("ip", deviceIp);
   log.put("Action", "Added");
   new AuditHandler().audit_inventory(log);


  } else {
   rs.put("result", "failure");
  }
  return Response.status(201).entity(rs).build();
 }

 @POST
 @Path("/lockUnlockCell")
 @Produces(MediaType.APPLICATION_JSON)
 public Response lockUnlockCell(@Context HttpServletRequest req, @FormParam("flag") String flag, @FormParam("ip") String ip) {
  String rs = setLockUnlock(ip, flag);
  rs = (rs != null) ? rs : "{\"STATUS\":\"FAIL\"}";
  return Response.status(201).entity(rs.toString()).build();
 }

 public String setLockUnlock(String ip, String flag) {
  Common co = new Common();
  HashMap < String, String > ll = co.getDbCredential();

  String myURL = "http://" + ip + "/cgi-bin/processData_CLI.sh";
  String mdr = "";
  String CMD_TYPE = "SET_SET_CELL_LOCK";
  String CMD_CODE = "SET_CELL_LOCK";

  if (flag.equalsIgnoreCase("2")) {
   CMD_TYPE = "SET_SET_CELL_UNLOCK";
   CMD_CODE = "SET_CELL_UNLOCK";
  }

  List < NameValuePair > params = new ArrayList < NameValuePair > ();


  params.add(new BasicNameValuePair("CMD_TYPE", CMD_TYPE));
  params.add(new BasicNameValuePair("CMD_CODE", CMD_CODE));
  params.add(new BasicNameValuePair("CELL_ID", "0"));
  params.add(new BasicNameValuePair("LAC", "0"));

  try {
   mdr = co.callPostDataUrl(myURL, params);
  } catch (Exception E) {

  }

  if ((!(mdr==null || mdr.equals("")))&& flag.equalsIgnoreCase("2")) {
	  new AuditHandler().audit_configuration_2g(ip);
	  
  }
  return mdr;
 }

 @GET
 @Path("/updateStatusOfGivenBts")
 @Produces(MediaType.TEXT_HTML)
 public String updateStatusOfGivenBts(@Context HttpServletRequest req, @QueryParam("ip") String ip) {
  updateStatusOfGivenBts(ip);
  return "success";
 }

 @GET
 @Path("/updateStatusAtLockUnlock")
 @Produces(MediaType.TEXT_HTML)
 public String updateStatusAtLockUnlock(@Context HttpServletRequest req, @QueryParam("devices") String devices) {
  updateStatusOfGivenBts(devices);
  return "success";
 }

 public String updateStatusOfGivenBts(String ip) {
  fileLogger.info("Inside Function : updateStatusOfGivenBts");
  Common co = new Common();
  Statement smt = null;
  Connection con = co.getDbConnection();

  try {
   smt = con.createStatement();
   String queryAppend = "";
   if (ip.equalsIgnoreCase("all")) {
    queryAppend = " where ip not in('0.0.0.0','1.1.1.1') and code in(0,1,2,5)";
   } else {
    queryAppend = " where ip='" + ip + "'";
   }
   String query = "select * from view_btsinfo" + queryAppend;
   fileLogger.debug(query);
   ResultSet rs = smt.executeQuery(query);

   while (rs.next()) {
    LinkedHashMap < String, String > param = new LinkedHashMap < String, String > ();
    param.put("cmdType", "GET_GET_CURR_STATUS");
    param.put("CMD_CODE", "GET_CURR_STATUS");
    param.put("systemIP", rs.getString("ip"));
    param.put("systemId", rs.getString("sytemid"));
    param.put("systemCode", rs.getString("code"));
    param.put("id", rs.getString("b_id"));
    new TwogOperations().getCurrentStatusOfBts(param);
   }
  } catch (Exception E) {
   fileLogger.error("Exeption  updateStatusOfGivenBts :" + E.getMessage());
  } finally {
   try {
    smt.close();
    con.close();
   } catch (Exception E) {

   }
  }

  fileLogger.info("Exit Function : updateStatusOfGivenBts");
  return "";
 }

 public String getCurrentStatusOfBts(LinkedHashMap < String, String > data) {
  String myURL = "http://" + data.get("systemIP") + "/cgi-bin/processData_CLI.sh";
  String mdr = "";
  Common co = new Common();
  List < NameValuePair > params = new ArrayList < NameValuePair > ();
  params.add(new BasicNameValuePair("CMD_TYPE", data.get("cmdType")));
  params.add(new BasicNameValuePair("CMD_CODE", data.get("CMD_CODE")));

  try {
   mdr = co.callPostDataUrl(myURL, params);

   if (mdr != null && !mdr.contains("ERR")) {
    this.updateBtsStatus(mdr, data.get("systemIP"));
   } else if (mdr == null) {

    JSONObject jb = new JSONObject();

    jb.put("CMD_CODE", "SET_CURR_STATUS");
    jb.put("SYSTEM_STATUS", 2);
    jb.put("CELL_OP_STATE", 0);
    jb.put("CELL_ADM_STATE", 0);
    this.updateBtsStatus(jb.toString(), data.get("systemIP"));

   }
  } catch (Exception E) {

  }
  return mdr;
 }

 public void updateBtsStatus(String jsonData, String ip) {
	 fileLogger.info("Inside Function : updateBtsStatus");
  try {
   Common.log("updateBtsStatus" + jsonData);
   JSONObject jo = new JSONObject(jsonData);
   fileLogger.debug(jo.toString());


   String cmdCode = jo.getString("CMD_CODE");
   int systemStatus = jo.getInt("SYSTEM_STATUS");
   int cellStatus = jo.getInt("CELL_OP_STATE");
   int adminState = jo.getInt("CELL_ADM_STATE");
   Common co = new Common();
   String query = "update btsmaster set status=" + systemStatus + " ,cellstatus = " + cellStatus + ",adminstate=" + adminState + " where ip = '" + ip + "'";
   co.executeDLOperation(query);
  } catch (Exception E) {

   fileLogger.error("Parsing CommonService Json updateBtsStatus Exception msg : " + E.getMessage());

  }
   fileLogger.info("Exit Function : updateBtsStatus");
 }


/*
	This function is used to add targets using a file instead of adding manually one by one
*/
 public Response uploadTarget(HttpServletRequest req, String targetImsi,String targetImei,String targetName,  String targetType) 
 {
	 Common co = new Common();
	  String query = "";
	  String deleteQuery = "";
	  if (targetImsi.equalsIgnoreCase(""))
		  
	  {
		  deleteQuery = "delete from target_list where imei = '"+targetImei+"' and imsi is null";
		  query = "insert into target_list(imsi,imei,name,type) values(null,'" + targetImei + "','" + targetName + "','" + targetType + "') returning id";
	  } 
	  else if (targetImei.equalsIgnoreCase("")) 
	  {
		  deleteQuery = "delete from target_list where imsi = '"+targetImsi+"' and imei is null";
		  query = "insert into target_list(imsi,imei,name,type) values('" + targetImsi + "',null,'" + targetName + "','" + targetType + "') returning id";
	  } 
	  
	  else 
	  {
		  deleteQuery = "delete from target_list where imsi = '"+targetImsi+"' or imei ='"+targetImei+"'";
		  query = "insert into target_list(imsi,imei,name,type) values('" + targetImsi + "','" + targetImei + "','" + targetName + "','" + targetType + "') returning id";
	  }
	  //String query = "insert into target_list(imsi,imei) values("+targetImsi+","+targetImei+") returning id";
	  co.executeDLOperation(deleteQuery);
	  int id = co.executeQueryAndReturnId(query);
	  HashMap < String, String > rs = new HashMap < String, String > ();
	  if (id != 0) {
	   rs.put("result", "success");
	   rs.put("id", Integer.toString(id));
	   LinkedHashMap < String, String > targetLog = new LinkedHashMap < String, String > ();
	   targetLog.put("action", "Target Add");
	   targetLog.put("imsi", targetImsi);
	   targetLog.put("imei", targetImei);
	   targetLog.put("name", targetName);
	   targetLog.put("type", targetType);
	   //new AuditHandler().auditLog(log, 3);
	   new AuditHandler().auditTarget(targetLog);
	   //auditTarget
	  } else {
	   rs.put("result", "fail");
	  }
	  return Response.status(201).entity(rs).build();
 }
 
 
 
/*
	This function is used to add targets using a file instead of adding manually one by one
	Required Parameters : NAME,IMSI,IMEI,TYPE which are in the CSV file
	
*/
 @POST
 @Path("/addMultipleTarget")
 @Produces(MediaType.APPLICATION_JSON)
 public Response addMultipleTarget(@Context HttpServletRequest req, @FormParam("data")  String data) {
	 fileLogger.info("Inside Function : addMultipleTarget");
	 fileLogger.debug(data);
	 HashMap < String, String > rs = new HashMap < String, String > ();
	   try {
		JSONArray ja = new JSONArray(data);
		for(int i =0;i<ja.length();i++) 
		{
			JSONObject jo = ja.getJSONObject(i);
			uploadTarget(req, jo.getString("targetImsi"), jo.getString("targeImei"), jo.getString("targetName"), jo.getString("targetType"));
		}
		rs.put("result", "success");
	} catch (Exception e) {
		fileLogger.debug("addMultipleTarget "+e.getMessage());
		rs.put("result", "fail");
	}
	  fileLogger.info("Exit Function : addMultipleTarget");
return Response.status(Constants.Created).entity(rs).build();
 }
 



 @POST
 @Path("/addTarget")
 @Produces(MediaType.APPLICATION_JSON)
 public Response addTarget(@Context HttpServletRequest req, @FormParam("targetImsi") String targetImsi, @FormParam("targetImei") String targetImei, @FormParam("targetName") String targetName, @FormParam("targetType") String targetType) {
  Common co = new Common();
  String query = "";
  if (targetImsi.equalsIgnoreCase("")) {
   query = "insert into target_list(imsi,imei,name,type) values(null,'" + targetImei + "','" + targetName + "','" + targetType + "') returning id";
  } else if (targetImei.equalsIgnoreCase("")) {
   query = "insert into target_list(imsi,imei,name,type) values('" + targetImsi + "',null,'" + targetName + "','" + targetType + "') returning id";
  } else {
   query = "insert into target_list(imsi,imei,name,type) values('" + targetImsi + "','" + targetImei + "','" + targetName + "','" + targetType + "') returning id";
  }
  //String query = "insert into target_list(imsi,imei) values("+targetImsi+","+targetImei+") returning id";					
  int id = co.executeQueryAndReturnId(query);
  HashMap < String, String > rs = new HashMap < String, String > ();
  if (id != 0) {
   rs.put("result", "success");
   rs.put("id", Integer.toString(id));
   LinkedHashMap < String, String > targetLog = new LinkedHashMap < String, String > ();
   targetLog.put("action", "Target Add");
   targetLog.put("imsi", targetImsi);
   targetLog.put("imei", targetImei);
   targetLog.put("name", targetName);
   targetLog.put("type", targetType);
   //new AuditHandler().auditLog(log, 3);
   new AuditHandler().auditTarget(targetLog);
   //auditTarget
  } else {
   rs.put("result", "fail");
  }
  return Response.status(201).entity(rs).build();
 }

 @POST
 @Path("/getTargetList")
 @Produces(MediaType.APPLICATION_JSON)
 public Response getTargetList() {
  fileLogger.info("Inside Function : getTargetList");
  String query = "select  * from target_list where istarget=TRUE order by imsi";
  fileLogger.debug(query);
  JSONArray rs = new Operations().getJson(query);
  fileLogger.info("Exit Function : getTargetList");
  return Response.status(201).entity(rs.toString()).build();
 }

 @POST
 @Path("/deleteTarget")
 @Produces(MediaType.APPLICATION_JSON)
 public Response deleteTarget(@Context HttpServletRequest req, @FormParam("targetIds") String targetIds) {
  Common co = new Common();
  JSONArray targetArr = new Operations().getJson("select * from target_list where id in (" + targetIds + ")");
  String query = "delete from target_list where id in (" + targetIds + ")";
  boolean status = co.executeDLOperation(query);
  HashMap < String, String > rs = new HashMap < String, String > ();
  if (status) {
   rs.put("result", "success");
   LinkedHashMap < String, String > targetLog = new LinkedHashMap < String, String > ();
   targetLog.put("action", "Target Delete");
   try {
    if (targetArr.length() > 0) {
     for (int i = 0; i < targetArr.length(); i++) {
      targetLog.put("target" + i, targetArr.getJSONObject(i).toString());
     }
    }
   } catch (JSONException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
   }
   new AuditHandler().auditTarget(targetLog);
  } else {
   rs.put("result", "failure");
  }
  return Response.status(201).entity(rs).build();
 }

 @POST
 @Path("/deleteantennaprofile")
 @Produces(MediaType.APPLICATION_JSON)
 public Response deleteAntennaProfile(@Context HttpServletRequest req, @FormParam("id") String id) {
  fileLogger.info("Inside Function : deleteAntennaProfile");
  Common co = new Common();
  HashMap < String, String > rs = new HashMap < String, String > ();
  int recCountOfId = 0;
  try {
   recCountOfId = new Operations().getJson("select count(*) count from cdrlogs where ant_id=" + id).getJSONObject(0).getInt("count");
  } catch (JSONException e) {
   // TODO Auto-generated catch block
   fileLogger.error("Exception while getting cdrcount for particular antenna id,message:" + e.getMessage());;
  }
  if (recCountOfId != 0) {
   rs.put("result", "failure");
   rs.put("message", "used error");
  } else {
   String query = "delete from antenna where id=" + id;
   boolean status = co.executeDLOperation(query);
   if (status) {
    rs.put("result", "success");
   } else {
    rs.put("result", "failure");
    rs.put("message", "db error");
   }
  }
  fileLogger.info("Exit Function : deleteAntennaProfile");
  return Response.status(201).entity(rs).build();
 }

 @POST
 @Path("/setSelectedNibIp")
 @Produces(MediaType.APPLICATION_JSON)
 public Response setSelectedNibIp(@Context HttpServletRequest req, @FormParam("selectedNibIp") String selectedNibIp) {
  HttpSession session = req.getSession(false);
  session.setAttribute("currentNib", selectedNibIp);
  HashMap < String, String > rs = new HashMap < String, String > ();
  rs.put("result", "success");
  return Response.status(201).entity(rs).build();
 }


 @POST
 @Path("/deleteDevice")
 @Produces(MediaType.APPLICATION_JSON)
 public Response deleteDevice(@Context HttpServletRequest req, @FormParam("groupName") String groupName) {
  fileLogger.info("Inside Function : deleteDevice");
  HashMap < String, String > rs = new HashMap < String, String > ();
  Common co = new Common();
  String query = "";
  int adminState;
  int count = 0;
  Operations operations = new Operations();
  try {
   query = "select grp_id from groups where grp_name='" + groupName + "'";
   JSONArray groupIdArray = operations.getJson(query);
   int groupId = groupIdArray.getJSONObject(0).getInt("grp_id");
   query = "select status from config_status";
   JSONArray statusArray = operations.getJson(query);
   int autoScannedCellsRunningStatus = statusArray.getJSONObject(0).getInt("status");
   if (autoScannedCellsRunningStatus == 1) {
    rs.put("result", "fail");
    rs.put("message", "auto_scanned_cells_running");
   } else {
    query = "select adminstate from btsmaster where grp_id = '" + groupId + "'";
    JSONArray adminStateArray = operations.getJson(query);
    for (int i = 0; i < adminStateArray.length(); i++) {
     adminState = adminStateArray.getJSONObject(i).getInt("adminstate");
     if (adminState == 2) {
      count++;
     }
    }
    if (count > 0) {
     rs.put("result", "fail");
     rs.put("message", "unlocked");
    } else {
     boolean groupDeleteStatus = deleteGroup(groupName);
     boolean deviceDeleteStatus = true;
     if (groupDeleteStatus) {
      query = "delete from btsmaster where grp_id = '" + groupId + "'";
      deviceDeleteStatus = co.executeDLOperation(query);
     }
     if (deviceDeleteStatus) {
      rs.put("result", "success");
      rs.put("message", "deleted");

      LinkedHashMap < String, String > log = new LinkedHashMap < String, String > ();
      log.put("Group Name", groupName);
      log.put("Group Name", "Deleted");
      new AuditHandler().audit_inventory(log);

     } else {
      rs.put("result", "fail");
      rs.put("message", "error");
     }
    }
   }
  } catch (Exception E) {
   rs.put("result", "fail");
   rs.put("message", "error");
   fileLogger.error("Exception :" + E.getMessage());
  }
  fileLogger.info("Exit Function : deleteDevice");
  return Response.status(201).entity(rs).build();
 }

 @POST
 @Path("/getNCells")
 @Produces(MediaType.APPLICATION_JSON)
 public Response getNCells(@Context HttpServletRequest req, @FormParam("ip") String ip, @FormParam("code") String code) {
  int deviceCode = Integer.parseInt(code);
  String query = "";
  if (deviceCode == 5) {
   query = "select  * from n_cells_data where ip='" + ip + "'";
  } else if ((deviceCode == 0 || deviceCode == 1 || deviceCode == 2)||(deviceCode == 13 || deviceCode == 14 || deviceCode == 15 )) {
   query = "select lce.lac,vb.config from view_btsinfo vb left join lac_change_event lce on vb.b_id=lce.sufi_id where vb.ip='" + ip + "' order by lce.logtime desc limit 1";
  }
  JSONArray rs = new Operations().getJson(query);
  return Response.status(201).entity(rs.toString()).build();
 }

 @POST
 @Path("/getSelectedAntennaProfile")
 @Produces(MediaType.APPLICATION_JSON)
 public Response getSelectedAntennaProfile(@Context HttpServletRequest req, @FormParam("antennaProfileName") String antennaProfileName) {
  fileLogger.info("Inside Function : getSelectedAntennaProfile");
  String query = "select  * from antenna where profile_name='" + antennaProfileName + "'";
  fileLogger.debug(query);
  JSONArray rs = new Operations().getJson(query);
  fileLogger.info("Exit Function : getSelectedAntennaProfile");
  return Response.status(201).entity(rs.toString()).build();
 }

 @POST
 @Path("/add3gDevice")
 @Produces(MediaType.APPLICATION_JSON)
 public Response add3gDevice(@Context HttpServletRequest req, @FormParam("groupName") String groupName, @FormParam("SystemManagerIP") String SystemManagerIP, @FormParam("useTypeId") String useTypeId, @FormParam("ofIp") String ofIp, @FormParam("ppfIp") String ppfIp, @FormParam("spfIp") String spfIp, @FormParam("hwCapabilityTypeIdOf") String hwCapabilityTypeIdOf, @FormParam("hwCapabilityTypeIdPpf") String hwCapabilityTypeIdPpf, @FormParam("hwCapabilityTypeIdSpf") String hwCapabilityTypeIdSpf, @FormParam("paGain") String paGain, @FormParam("paPower") String paPower) {
  ApiCommon apiCommon = new ApiCommon();
  String defaultSufiConfig = apiCommon.getSufiConfigurationWithDefaultValues();
  int deviceTypeIdOf = getDeviceTypeId(useTypeId, "SuFi OF");
  String configOf = apiCommon.getSufiConfigurationWithDefaultValues(defaultSufiConfig, 1);
  int deviceTypeIdPpf = getDeviceTypeId(useTypeId, "SuFi PPF");
  String configPpf = apiCommon.getSufiConfigurationWithDefaultValues(defaultSufiConfig, 2);
  int deviceTypeIdSpf = getDeviceTypeId(useTypeId, "SuFi SPF");
  String configSpf = apiCommon.getSufiConfigurationWithDefaultValues(defaultSufiConfig, 3);
  int groupId = createGroup(groupName);
  int networkId = getNetworkId("3G");
  Common co = new Common();
  /*HashMap<String,String> hm=new HashMap<String,String>();
  hm.put("SuFi SPF","4");
  DID=hm.get("SUFI SPF");*/
  HashMap < String, String > rs = new HashMap < String, String > ();
  if (groupId != 0) {
   HashMap < String, String > antennaMap = new CommonOperation().getScanningAndTrackingAntenna();
   String inTracking = antennaMap.get("tracking");
   String query1 = "INSERT INTO btsmaster(ip,devicetypeid,grp_id,typeid,config,hw_capability_id,config_applied_status,pa_gain,pa_power,antenna_apply,systemmanager) VALUES ('" + ofIp + "'," + deviceTypeIdOf + "," + groupId + "," + networkId + ",'" + configOf + "'," + hwCapabilityTypeIdOf + ",'n'," + paGain + "," + paPower + ",'" + inTracking + "' , '"+SystemManagerIP + "' );";
   String query2 = "INSERT INTO btsmaster(ip,devicetypeid,grp_id,typeid,config,hw_capability_id,config_applied_status,antenna_apply,systemmanager) VALUES ('" + ppfIp + "'," + deviceTypeIdPpf + "," + groupId + "," + networkId + ",'" + configPpf + "'," + hwCapabilityTypeIdPpf + ",'n','" + inTracking + "' , '"+SystemManagerIP + "' );";
   String query3 = "INSERT INTO btsmaster(ip,devicetypeid,grp_id,typeid,config,hw_capability_id,config_applied_status,antenna_apply,systemmanager) VALUES ('" + spfIp + "'," + deviceTypeIdSpf + "," + groupId + "," + networkId + ",'" + configSpf + "'," + hwCapabilityTypeIdSpf + ",'n','" + inTracking + "' , '"+SystemManagerIP + "' );";
   co.executeDLOperation(query1);
   co.executeDLOperation(query2);
   co.executeDLOperation(query3);
   new TwogOperations().updateStatusOfBts("'" + ofIp + "','" + ppfIp + "','" + spfIp + "'");
   rs.put("result", "success");
   LinkedHashMap < String, String > log = new LinkedHashMap < String, String > ();
   log.put("ip", ofIp + "," + ppfIp + "," + spfIp);
   log.put("Action", "Added");
   new AuditHandler().audit_inventory(log);
  } else {
   rs.put("result", "fail");
   rs.put("message", "Group already present");
  }

  return Response.status(201).entity(rs).build();
 }

 public int createGroup(String groupName) {
  Common co = new Common();
  String query = "insert into groups(grp_name) values('" + groupName + "')returning grp_id";
  int id = co.executeQueryAndReturnId(query);
  return id;
 }

 public int getNetworkId(String networkType) {
  Common co = new Common();
  String query = "select n_id from btsnetworktype where name='" + networkType + "' and status='A'";
  int nid = co.executeQueryAndReturnId(query);
  return nid;
 }

 public int getDeviceTypeId(String useTypeId) {
  Common co = new Common();
  String query = "select d_id from devicetype where use_type_id=" + useTypeId;
  int didof = co.executeQueryAndReturnId(query);
  return didof;
 }

 public int getDeviceTypeId(String useTypeId, String deviceName) {
  Common co = new Common();
  String query = "select d_id from devicetype where use_type_id=" + useTypeId + " and dname='" + deviceName + "'";
  int didof = co.executeQueryAndReturnId(query);
  return didof;
 }

 public int getDeviceTypeCode(String useTypeId) {
  Common co = new Common();
  String query = "select code from devicetype where use_type_id=" + useTypeId;
  int deviceCode = co.executeQueryAndReturnId(query);
  return deviceCode;
 }

 public boolean deleteGroup(String groupName) {
  Common co = new Common();
  String query1 = "delete from groups where grp_name='" + groupName + "'";
  boolean status = co.executeDLOperation(query1);

  return status;
 }

 @POST
 @Path("/getDeviceDetails")
 @Produces(MediaType.APPLICATION_JSON)
 public Response getDeviceDetails(@FormParam("ip") String ip) {
  fileLogger.info("Inside Function : getDeviceDetails");
  String query = "select * from view_btsinfo where ip = '" + ip + "'";
  fileLogger.debug(query);
  JSONArray rs = new Operations().getJson(query);
  fileLogger.info("Exit Function : getDeviceDetails");
  return Response.status(201).entity(rs.toString()).build();
 }

 @POST
 @Path("/getlockunlockstatus")
 @Produces(MediaType.APPLICATION_JSON)
 public Response getLockUnlockStatus(@Context HttpServletRequest req, @FormParam("ip") String ip) {
  JSONObject rs = new JSONObject();
  String query = "select adminstate from btsmaster where ip='" + ip + "'";
  try {
   try {
    JSONArray lockUnlockStatusArray = new Operations().getJson(query);
    if (lockUnlockStatusArray.getJSONObject(0).getInt("adminstate") == 1) {
     rs.put("result", "successful");
     rs.put("message", "locked");

    } else {
     rs.put("result", "successful");
     rs.put("message", "unlocked");
    }
   } catch (Exception E) {
    rs.put("result", "failed");
    rs.put("message", "Problem in getting lock unlock data");
   }
  } catch (Exception E) {}
  return Response.status(201).entity(rs.toString()).build();
 }

 @POST
 @Path("/startconfigondevices")
 @Produces(MediaType.TEXT_PLAIN)
 public Response trackMobileOnGivenPacketList(@Context HttpServletRequest req, @FormParam("configData") String configData) {
  fileLogger.info("Inside Function : trackMobileOnGivenPacketList");
  String updateStatusVerdict = "";
  try {

   String query = "select count(*) from oprrationdata where status ='1'";
   int count = Integer.parseInt(new Operations().getJson(query).getJSONObject(0).getString("count"));
   if (count > 0) {
    updateStatusVerdict = "Fail&Please stop currently running  operation";
    return Response.status(201).entity(updateStatusVerdict).build();
   }
   query = "select status from manual_override";
   String manualStatus = new Operations().getJson(query).getJSONObject(0).getString("status");
   if (manualStatus.equalsIgnoreCase("false") || manualStatus.equalsIgnoreCase("f")) {
    updateStatusVerdict = "Fail&Manual Override is not enabled";
    return Response.status(201).entity(updateStatusVerdict).build();
   }

  } catch (Exception e) {
   fileLogger.error("error in startconfigondevices is :" + e.getMessage());
   updateStatusVerdict = "Fail&Exception";
   return Response.status(201).entity(updateStatusVerdict).build();
  }

  DBDataService dbDataService = DBDataService.getInstance();
  dbDataService.storeTriggerOnDb(new Date(), new CurrentOperationType().getTransId(), "Falcon", "Processing Manual Transmission");
  new TriggerCueServer().sendText("event");
  new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Processing manual event\"}");

  String config = "";
  boolean updateStatus = false;
  fileLogger.debug("@manual 1");
  try {
   JSONObject configObj = new JSONObject(configData);
   int repFreq = configObj.getInt("repitition_freq");
   int duration = configObj.getInt("duration");

   config = configObj.getJSONArray("config_data").toString();

   //HashMap<String,String> rsOpr = new HashMap<String,String>();
   DBDataService.isInterrupted = false;
   fileLogger.debug("@manual 2");

   LinkedHashMap < String, String > queLog = new LinkedHashMap < String, String > ();
   queLog.put("action", "Processing Manual Transmission");
   queLog.put("repitition freq", "" + repFreq);
   queLog.put("cell active time(in secs)", "" + duration);
   new AuditHandler().auditManualTransmission(queLog);

   dbDataService.executeManualOperation(config, repFreq);
   fileLogger.debug("@manual 17");
   updateStatus = new Common().executeDLOperation("update config_status set status=1");
   dbDataService.setCurrentManualEventName("manual");
  } catch (JSONException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  }
  fileLogger.debug("@manual 18");
  updateStatusVerdict = updateStatus == true ? "SUCCESS" : "FAIL";
  fileLogger.info("Exit Function : trackMobileOnGivenPacketList");
  return Response.status(201).entity(updateStatusVerdict).build();
   //rsOpr.put("result", "success");
  //rsOpr.put("message", "Operation successfully created");
  //return Response.status(201).entity(rsOpr).build();
 }

 /*@POST
		   @Path("/startconfigondevices")
		   @Produces(MediaType.TEXT_PLAIN)
		   public Response trackMobileOnGivenPacketList(@Context HttpServletRequest req,@FormParam("configData") String configData)
		   {
			   JSONObject configJson = null;
			   JSONObject rs = new JSONObject();
			   final Operations operations = new Operations();
			   JSONArray json2gArray=new JSONArray();
			   JSONArray json3gArray=new JSONArray();
			   Common common=new Common();
			   
			   final HashMap<String,ArrayList<HashMap<String,String>>> devicesMapOverTech = operations.getAllBtsInfoByTech();
			   
			   try{
				   configJson = new JSONObject(configData);
				   
				   final int repititionFreq = configJson.getInt("repitition_freq");
				   
				   JSONArray dataArray = configJson.getJSONArray("config_data");
				   
				   for(int i=0;i<dataArray.length();i++){
					   String tech=dataArray.getJSONObject(i).getString("tech");
					   if(tech.equalsIgnoreCase("2g")){
						   json2gArray.put(dataArray.getJSONObject(i)); 
						   fileLogger.debug("line 659 :"+json2gArray.length());
					   }else if(tech.equalsIgnoreCase("3g")){
						   json3gArray.put(dataArray.getJSONObject(i));
						   fileLogger.debug("line 662 :"+json3gArray.length());
					   }else{
						   JSONArray tempJsonArr=dataArray.getJSONObject(i).getJSONArray("data");
						   for(int j=0;j<tempJsonArr.length();j++){
							 JSONObject tempJsonObj=tempJsonArr.getJSONObject(j); 
							 if(tempJsonObj.getString("flag").equalsIgnoreCase("self")){
								 if(tempJsonObj.getString("arfcn").equalsIgnoreCase("") || tempJsonObj.getString("bsic").equalsIgnoreCase("")){
									 json3gArray.put(dataArray.getJSONObject(i));
									 fileLogger.debug("line 670 :"+json2gArray.length());
								 }else if(tempJsonObj.getString("uarfcn").equalsIgnoreCase("") || tempJsonObj.getString("psc").equalsIgnoreCase("")){
									 json2gArray.put(dataArray.getJSONObject(i));
									 fileLogger.debug("line 673 :"+json2gArray.length());
								 }
							 }
						   }
					   }
				   }
				   
				   fileLogger.debug("line 679 :"+json2gArray.length());
				   ArrayList<String> plmn2gList= new ArrayList<String>();
				   for(int i=0;i<json2gArray.length();i++){
					   plmn2gList.add(json2gArray.getJSONObject(i).getString("plmn"));
				   }
				   ArrayList<String> plmn3gList= new ArrayList<String>();
				   for(int i=0;i<json3gArray.length();i++){
					   plmn3gList.add(json3gArray.getJSONObject(i).getString("plmn"));
				   }
				   
				   Set<String> plmn2gSet = new HashSet<String>(plmn2gList);
				   Set<String> plmn3gSet = new HashSet<String>(plmn3gList);
				   ArrayList<JSONObject> json2gList=getSortedList(json2gArray);
				   ArrayList<JSONObject> json3gList=getSortedList(json3gArray);
			       int json2gListSize=json2gList.size();
			       int json3gListSize=json3gList.size();
				   int largeListSize=json2gListSize>=json3gListSize?json2gListSize:json3gListSize;
			       ArrayList<JSONObject> newJson2gList=new ArrayList<JSONObject>();
			       ArrayList<JSONObject> newJson3gList=new ArrayList<JSONObject>();
			       JSONObject jobj= new JSONObject();
			       jobj.put("dummyCheck","false");
			       jobj.put("plmn","0");
			       jobj.put("duration","0");
			       for(int i=0;i<largeListSize;i++){
			           if(i<json2gListSize && i<json3gListSize){
			               if(json2gList.get(i).getString("plmn").equals(json3gList.get(i).getString("plmn"))){
			            	   newJson2gList.add(json2gList.get(i));
			            	   newJson3gList.add(json3gList.get(i));
			               }else{
			            	   newJson2gList.add(json2gList.get(i));
						       jobj.put("plmn",json2gList.get(i).getString("plmn"));
						       jobj.put("duration",json2gList.get(i).getString("duration"));
			            	   newJson3gList.add(jobj);
			            	   
			            	   newJson3gList.add(json3gList.get(i));
						       jobj.put("plmn",json3gList.get(i).getString("plmn"));
						       jobj.put("duration",json3gList.get(i).getString("duration"));
			                   newJson2gList.add(jobj);
			               }
			           }else if(i<json2gListSize && i>=json3gListSize){
			        	   		newJson2gList.add(json2gList.get(i));
							    jobj.put("plmn",json2gList.get(i).getString("plmn"));
							    jobj.put("duration",json2gList.get(i).getString("duration"));
			        	   		newJson3gList.add(jobj);
			               
			           }else if(i>=json2gListSize && i<json3gListSize){
			        	   		newJson3gList.add(json3gList.get(i));
							    jobj.put("plmn",json3gList.get(i).getString("plmn"));
							    jobj.put("duration",json3gList.get(i).getString("duration"));
			        	   		newJson2gList.add(jobj);
			               
			           }
			           }
			       JSONArray  newJson2gArray = new JSONArray();
			       JSONArray  newJson3gArray = new JSONArray();
			       for(int i=0;i<newJson2gList.size();i++){
			    	   newJson2gArray.put(newJson2gList.get(i));
			    	   newJson3gArray.put(newJson3gList.get(i));
			       }
				   
				   final JSONArray JSONArrayFor2g=newJson2gArray;
				   final JSONArray JSONArrayFor3g=newJson3gArray;
				   

				   setDbCounterValue("update config_status set start_time=now(),counter=0");
					JSONArray jarr=getDbCounterValue("select start_time from config_status");
					oprStartTime=jarr.getJSONObject(0).getString("start_time");
				   
					//Two Threads running simultaneously to run 2g and 3g separately	
				   new Thread(){
					   public void run(){
				    try{
					if(JSONArrayFor2g.length()>0 && devicesMapOverTech.get("2G").size()>0)
					{
							for(int j=0;j<devicesMapOverTech.get("2G").size();j++)
							{
							final HashMap<String,String> hm=devicesMapOverTech.get("2G").get(j);
							new Thread(){
								public void run(){
									try{
									
									HashMap<String,String> aa = udpateDbCounter(devicesMapOverTech.get("2G").size()+devicesMapOverTech.get("3G").size());
										
									int threadCount = Integer.parseInt(aa.get("count"));
									String startTime = aa.get("startTime");
									
									String threadName=Thread.currentThread().getName();
									
									fileLogger.debug("Thread "+threadName+" starts");
									
									for(int i=0;i<repititionFreq;i++){
										for(int k=0;k<JSONArrayFor2g.length();k++){
											fileLogger.debug("starttime:"+startTime);
											fileLogger.debug("oprStartTime:"+startTime);
											if(!startTime.equalsIgnoreCase(oprStartTime)){
												fileLogger.debug("interrupted");
												throw new Exception("thread interrupted");
											}
									
										fileLogger.debug("2g json array length:"+JSONArrayFor2g.length());
										fileLogger.debug("Thread "+threadName+" with repitition ("+(i+1)+") out of total "+repititionFreq);
										JSONObject tempJsonObjectFor2g=JSONArrayFor2g.getJSONObject(k);
										try{	
											if(!tempJsonObjectFor2g.has("dummyCheck")){
											operations.locateWithNeighbour(tempJsonObjectFor2g,hm.get("ip"),-1);
									        operations.createNeighbourServerData(tempJsonObjectFor2g,hm.get("ip"));
											}else{
												fileLogger.debug("dummy packet creation in 2G for synchronization with other technologies");
											}
										}
										catch(Exception E)
										{
											fileLogger.debug("exception in 2g packet thread :"+E.getMessage());
										}
									        
										if(devicesMapOverTech.get("3G").size()>0 || !tempJsonObjectFor2g.has("dummyCheck")) 
										{
											fileLogger.debug(threadName+" gone in sleep at "+new Date());
									        Thread.sleep(tempJsonObjectFor2g.getInt("duration")*60*1000);
									        fileLogger.debug(threadName+" waken up from sleep at "+new Date());
										}	
											
										}
									}
									
									checkDbCounter();
								}catch(Exception E){
									fileLogger.debug("Exception occurs in 2g inner running thread");
									fileLogger.debug(E.getMessage());
								}
								}
							}.start();
							}
					}
				    }catch(Exception E){
				    	fileLogger.debug("Exception occurs in 2g outer running thread");
				    }
					   }
				   }.start();
				   
				   
				   new Thread(){
					   public void run(){
						   try{
						   if(JSONArrayFor3g.length()>0 && devicesMapOverTech.get("3G").size()>0){
						    for(int j=0;j<devicesMapOverTech.get("3G").size();j++){
						    	final HashMap<String,String> hm=devicesMapOverTech.get("3G").get(j);
								new Thread(){
									public void run(){
										try{
											
											HashMap<String,String> aa = udpateDbCounter(devicesMapOverTech.get("2G").size()+devicesMapOverTech.get("3G").size());
											
											int threadCount = Integer.parseInt(aa.get("count"));
											String startTime = aa.get("startTime");
											fileLogger.debug("current thread count"+threadCount);
											
											String threadName=Thread.currentThread().getName();
										
										fileLogger.debug("Thread "+threadName+" starts");
										
										for(int i=0;i<repititionFreq;i++){
											for(int k=0;k<JSONArrayFor3g.length();k++)
											{
											
											fileLogger.debug("starttime:"+startTime);
											fileLogger.debug("oprStartTime:"+startTime);
											if(!startTime.equalsIgnoreCase(oprStartTime))
											{
												fileLogger.debug("interrupted");
												throw new Exception("thread interrupted");
											}
											
											fileLogger.debug("3g json array length:"+JSONArrayFor3g.length());
											fileLogger.debug("Thread "+threadName+" with repitition ("+(i+1)+") out of total "+repititionFreq);
											JSONObject tempJsonObjectFor3g=JSONArrayFor3g.getJSONObject(k);
											fileLogger.debug("line 879:"+tempJsonObjectFor3g.toString());
											try{
												if(!tempJsonObjectFor3g.has("dummyCheck"))
												{
													LinkedHashMap<String,String> param=operations.setParamsForConfig(tempJsonObjectFor3g,hm.get("ip"),-1);
													new Common().sendConfigurationToNode(Integer.parseInt(param.get("adminState")),param);
													param.put("ip",hm.get("ip"));
													operations.setSufiConfigOnDb(param);
												}
												else
												{
													fileLogger.debug("dummy packet creation in 3G for synchronization with other technologies");	
												}
											}
											catch(Exception E)
											{
												fileLogger.debug("exception:"+E.getMessage());
											}
										        
												if(devicesMapOverTech.get("2G").size()>0 || !tempJsonObjectFor3g.has("dummyCheck")) 
												{
													fileLogger.debug(threadName+" gone in sleep at "+new Date());
											        Thread.sleep(tempJsonObjectFor3g.getInt("duration")*60*1000);
											        fileLogger.debug(threadName+" waken up from sleep at "+new Date());
												}
												
											}
										}
										checkDbCounter();
										
									}catch(Exception E){
										fileLogger.debug("Exception occurs in 3g inner running thread");
										fileLogger.debug(E.getMessage());
										
									}
									}
								}.start();
						    }
						   }
						   }catch(Exception E){
							   fileLogger.debug("Exception occurs in 3g running thread");
						   }
					   }
				   }.start();
			   }catch(Exception E){
				   fileLogger.debug("Exception in method trackMobileOnGivenPacketList :"+E.getMessage());
			   }
			   boolean updateStatus=common.executeDLOperation("update config_status set status=1");
			   String updateStatusVerdict= updateStatus==true?"SUCCESS":"FAIL";
			   
			   return Response.status(201).entity(updateStatusVerdict).build();
     		   }
		   */

 public synchronized void setDbCounterValue(String query) {
  new Common().executeDLOperation(query);
 }
 public synchronized JSONArray getDbCounterValue(String query) {
  return new Operations().getJson(query);
 }


 public HashMap < String, String > udpateDbCounter(int size) throws Exception {
  fileLogger.info("Inside Function : udpateDbCounter");
  int threadCount = 0;
  JSONArray ja = null;
  String startTime = "";


  synchronized(this) {
   ja = getDbCounterValue("select counter,start_time from config_status");
   fileLogger.debug(Thread.currentThread().getName() + "------" + ja.getJSONObject(0).getInt("counter"));
   threadCount = ja.getJSONObject(0).getInt("counter") + 1;
   startTime = ja.getJSONObject(0).getString("start_time");

   fileLogger.debug("current thread count" + threadCount + " - Size" + size);
   if (threadCount == size) {
    setDbCounterValue("update config_status set status=1,counter=" + threadCount);
    DBDataService.getInstance().setCurrentManualEventName("manual");
    new AutoStateServer().sendText("start");
   } else {
    setDbCounterValue("update config_status set counter=" + threadCount);
   }
   fileLogger.info("Exit Function : udpateDbCounter");
  }

  HashMap < String, String > aa = new HashMap < String, String > ();
  aa.put("count", threadCount + "");
  aa.put("startTime", startTime);

  return aa;
 }




 public void checkDbCounter() throws Exception {

  int threadCount = 0;
  JSONArray ja = null;

  synchronized(this) {
   ja = getDbCounterValue("select counter from config_status");
   threadCount = ja.getJSONObject(0).getInt("counter") - 1;
   if (threadCount == 0) {
    setDbCounterValue("update config_status set status=0,counter=0");
    DBDataService.getInstance().setCurrentManualEventName(null);
    new AutoStateServer().sendText("stop");
   } else {
    setDbCounterValue("update config_status set counter=" + threadCount);
   }
  }

 }

 @POST
 @Path("/stopconfigondevices")
 @Produces(MediaType.TEXT_PLAIN)
 public Response stopConfigondevices(@Context HttpServletRequest req) {

  return stopRunningManualOperation();

 }

 /* @POST
		   @Path("/stopconfigondevices")
		   @Produces(MediaType.TEXT_PLAIN)
		   public Response trackMobileOnGivenPacketList(@Context HttpServletRequest req)
		   {
			   
			   	boolean updateStatus=new Common().executeDLOperation("update config_status set status=0,start_time=now()");
				JSONArray jarr=getDbCounterValue("select start_time from config_status");
				try 
				{
					fileLogger.debug("befoer stop sunil"+oprStartTime);
					oprStartTime=jarr.getJSONObject(0).getString("start_time");
					fileLogger.debug("after stop sunil"+oprStartTime);
				}
				catch(Exception e) 
				{
					fileLogger.debug(e.getMessage());
					e.printStackTrace();
				}
				String updateStatusVerdict= updateStatus==true?"SUCCESS":"FAIL";
				return Response.status(201).entity(updateStatusVerdict).build();
		   }*/

 public Response stopRunningManualOperation() {
  fileLogger.info("Inside Function : stopRunningManualOperation");
 // fileLogger.debug("in stopRunningManualOperation");
  DBDataService dbDataService = DBDataService.getInstance();
  try {
   dbDataService.stopRunningManualOperation();
   fileLogger.debug("returned from stopRunningManualOperation()");
   new ScanTrackModeServer().sendText("track&Idle");
   JSONArray hummerDataArray = new Operations().getJson("select * from view_btsinfo where code = 12");
   if (hummerDataArray.length() > 0) {
	   new TRGLController().sendTrackingStatusToHummer("no");
   	 //  new TRGLController().sendTrackingSectorToHummer("-1");
   }
   boolean updateStatus = new Common().executeDLOperation("update config_status set status=0,start_time=now()");
   dbDataService.setCurrentManualEventName(null);
   //new AutoStateServer().sendText("stop");
   String updateStatusVerdict = updateStatus == true ? "SUCCESS" : "FAIL";
   return Response.status(201).entity(updateStatusVerdict).build();
   //return Response.status(201).entity("{\"result\":\"success\",\"msg\":\"Operation Stopped Successfully\"}").build();
   //addToEventPriorityQueue(eventObj.getString("name"),"scheduler",eventObj.getString("note"),eventObj.getString("note"),operators,eventObj.getString("distance"),"localhost",-1,"-1","-1","-1",-1,-1,eventObj.getString("inserttime"),true);
  }catch (UnknownHostException e) {
	   // TODO Auto-generated catch block
	   e.printStackTrace();
	  }catch (Exception e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  } 
  
  //catch (IOException e) {
   // TODO Auto-generated catch block
 ///  e.printStackTrace();
  //}
  
  fileLogger.info("Exit Function : stopRunningManualOperation");
  return Response.status(201).entity("{\"result\":\"failure\",\"msg\":\"Problem in Stopping\"}").build();
 }

 @POST
 @Path("/setselectedconfigondb")
 @Produces(MediaType.TEXT_PLAIN)
 public Response setSelectedConfigOnDb(@Context HttpServletRequest req, @FormParam("fullConfigData") String fullConfigData, @FormParam("packetIds") String packetIds, @FormParam("repititionFreq") String repititionFreq, @FormParam("cellActiveTime") String cellActiveTime) {
  String query = "update config_status set config_data='" + fullConfigData + "',ids='" + packetIds + "',frequency=" + repititionFreq + ",duration=" + cellActiveTime;
  boolean updateStatus = new Common().executeDLOperation(query);
  String updateStatusVerdict = updateStatus == true ? "SUCCESS" : "FAIL";
  return Response.status(201).entity(updateStatusVerdict).build();
 }


 	
	@POST
 	@Path("/center_cdr")
 	@Produces(MediaType.APPLICATION_JSON)
 	public Response getSelfLocFromCDR(@Context HttpServletRequest req,String data) 
 	{
		 fileLogger.info("Inside Function : getSelfLocFromCDR");	
 		// MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters(); 
 		 //String nameParam = queryParams.getFirst("name");
 		 String startTime = null,endTime = null,seprator = null;
 		 try 
 		 {
			JSONObject receiveddJsonData = new JSONObject(data);
			startTime = receiveddJsonData.getString("START_TIME");
			endTime = receiveddJsonData.getString("STOP_TIME");			
 		 } 
 		 catch (Exception e)
 		 {}
 		  //startTime="2019-09-24 10:07:42.978348";
 		  //endTime="2019-09-24 10:10:31.563884";
 		 
 		 String query = "select * from cdrlogs  where self_loc != '0.0,0.0'  and inserttime >= '" +getUTCTime(startTime,null) +"' and inserttime <= '"+ getUTCTime(endTime,null)+"'  order by inserttime desc limit 1";
	     fileLogger.debug(query);
	     JSONArray rs = new Operations().getJson(query);
	     JSONObject jo = new JSONObject();
		try {
			jo = rs.getJSONObject(0);
		} catch (Exception e) {
			// TODO: handle exception
		}
		 fileLogger.info("Exit Function : getSelfLocFromCDR");
		return Response.status(201).entity(jo.toString()).build();
	    }
 	
 	@POST
 	@Path("/get_cue")
 	@Produces(MediaType.APPLICATION_JSON)
 	//@FormParam("name") String name
 	//, @Context UriInfo uriInfo,
	public Response getCueData(@Context HttpServletRequest req,String data) 
 	{
 		
 		 String startTime = null,endTime = null,seprator = null;
 		 try 
 		 {
			JSONObject receiveddJsonData = new JSONObject(data);
			startTime = receiveddJsonData.getString("START_TIME");
			endTime = receiveddJsonData.getString("STOP_TIME");			
 		 } 
		 
 		 catch (Exception e)
 		 {
 			 
 		 }
 		//String query = "select arrival_date \"DATE\", trigger_source \"SOURCE\",detail \"DETAIL\" ,cue_id \"CUE_ID\" from trigger_cue where trigger_type = 'Cue' and arrival_date >= '" +getUTCTime(startTime,null) +"' and arrival_date <= '"+ getUTCTime(endTime,null)+"'  order by arrival_date";
 		String query = "select arrival_date \"DATE\", trigger_source \"SOURCE\",detail \"DETAIL\" ,cue_id \"CUE_ID\" from trigger_cue where trigger_type = 'Cue' and arrival_date >= '" +startTime +"' and arrival_date <= '"+ endTime+"'  order by arrival_date";
 		fileLogger.debug(query);
 		 JSONArray rs = new Operations().getJson(query);
	     return Response.status(201).entity(rs.toString()).build();
	  }
 	
 	 
 	 	




 /*@GET
 @Path("/genrateConfiguration")
 @Produces(MediaType.APPLICATION_JSON)*/
 @POST
 @Path("/genrateConfiguration")
 @Produces(MediaType.APPLICATION_JSON)
  //JSONObject configs =  new PossibleConfigurations().start();
 public Response genrateConfiguration(@Context HttpServletRequest req, @FormParam("oprList") String oprList,@FormParam("celllist") String celllist, @FormParam("antennaList") String antennaList) {
	 
  String[] oprs = oprList.split(",");
  String tmpCellList="";
  if(celllist.length()!=0) {
	  String[] cellListArray=celllist.split(",");
	  
	  for(int i=0;i<cellListArray.length;i++) {
		  String tmpCellList1=cellListArray[i].split("-")[1];
		  tmpCellList+=tmpCellList1+",";
	  }
	  if(tmpCellList.length()>0) {
		  
		  tmpCellList=tmpCellList.substring(0, tmpCellList.length() - 1);
	  }
  }
  if(tmpCellList=="")
	  tmpCellList=null;

  return Response.status(201).entity(new PossibleConfigurations().start(oprs, antennaList,tmpCellList)).build();
 }




 @GET
 @Path("/getcurrentconfigdata")
 @Produces(MediaType.APPLICATION_JSON)
 public Response getCurrentConfigData(@Context HttpServletRequest req) {
  fileLogger.info("Inside Function : getCurrentConfigData");
  String query = "select  * from config_status";
  fileLogger.debug(query);
  JSONArray rs = new Operations().getJson(query);
  fileLogger.info("Exit Function : getCurrentConfigData");
  return Response.status(201).entity(rs.toString()).build();
 }

 @GET
 @Path("/getcurrentscannedopr")
 @Produces(MediaType.APPLICATION_JSON)
 public Response getCurrentScannedOpr(@Context HttpServletRequest req) {
  //String query = "select opr from view_current_scanned_opr";
  String query = "select distinct(opr) from view_current_scanned_opr_last_24_hours";
  JSONArray jo = new Operations().getJson(query);
  return Response.status(201).entity(jo.toString()).build();
 }
 
 @POST
 @Path("/getTechAndCells")
 @Produces(MediaType.APPLICATION_JSON)
 
 public Response getTechAndCells(@Context HttpServletRequest req, @FormParam("oprList") String oprList, @FormParam("antennaList") String antennaList) {
//  String query = "select plmn from view_current_scanned_opr";
 
	 oprList=oprList.replaceAll(",","','");
	 
	 String result3="";
	 String aa="",bb="";
 
	 if(antennaList.equalsIgnoreCase("1")) {
		 
		 String query2="select checkov1()";
		 JSONArray result2 = new Operations().getJson(query2);
		 try {
			 result3= result2.getJSONObject(0).getString("checkov1");
		 } catch (JSONException e) {
			 // TODO Auto-generated catch block
			 e.printStackTrace();
		 }
		 
		 
		 
		 
	 }
	
	 if(result3.equalsIgnoreCase("1") )
		 aa=" and oprlogs_current.antenna_id = 23 ";
	 else
		 aa=" and oprlogs_current.antenna_id in ( "+antennaList +")";
 
 
  // String query="select oprlogs_current .*,rpt_data#>(ARRAY['REPORT'] || ARRAY[oprlogs_current.index_id::text]|| ARRAY['NEIGHBORS']) nbr,opr from oprlogs_current left join netscan_cell_scan_report ncsr on(oprlogs_current.packet_id::numeric = ncsr.id) left join view_current_scanned_opr_last_24_hours vcso on oprlogs_current.mcc||oprlogs_current.mnc=vcso.plmn where opr in( '"+oprList+"') and (oprlogs_current.inserttime >= (timezone('utc'::text, now()) - '24:00:00'::interval) or count = 'locator_count')  AND oprlogs_current.inserttime <= timezone('utc'::text, now()) and cell!='1' and   ( oprlogs_current.band, oprlogs_current.packet_type) in   (select name,tech1 from supported_band) and (lac!= '0' or tac !='0') and cell != '0'  "+aa+" order by packet_type,inserttime desc";
	 String query="select distinct oprlogs_current.packet_type,cell,mcc,mnc,opr,lac,tac,earfcn,uarfcn,arfcn,psc,bcc,pci,ncc from oprlogs_current  left join view_current_scanned_opr_last_24_hours vcso on oprlogs_current.mcc||oprlogs_current.mnc=vcso.plmn where opr in( '"+oprList+"') and (oprlogs_current.inserttime >= (timezone('utc'::text, now()) - '24:00:00'::interval) or count = 'locator_count')  AND oprlogs_current.inserttime <= timezone('utc'::text, now()) and cell!='1' and   ( oprlogs_current.band, oprlogs_current.packet_type) in   (select name,tech1 from supported_band) and (lac!= '0' or tac !='0') and cell != '0'  "+aa+" order by opr,packet_type ";
	 fileLogger.info("TechandCells query = "+query);
  JSONArray jo = new Operations().getJson(query);
  return Response.status(201).entity(jo.toString()).build();
 }
 

 @GET
 @Path("/getcurrentscannedoperators")
 @Produces(MediaType.APPLICATION_JSON)
 public Response getCurrentScannedOperators(@Context HttpServletRequest req) {
  String query = "select plmn,opr from view_current_scanned_opr_last_24_hours order by opr";
  JSONArray jo = new Operations().getJson(query);
  return Response.status(201).entity(jo.toString()).build();
 }

 public static ArrayList < JSONObject > getSortedList(JSONArray array) {
  fileLogger.info("Inside Function : getSortedList");
  ArrayList < JSONObject > list = new ArrayList < JSONObject > ();
  try {
   for (int i = 0; i < array.length(); i++) {
    list.add(array.getJSONObject(i));
   }
   Collections.sort(list, new Comparator < JSONObject > () {

    public int compare(JSONObject a, JSONObject b) {
     int plmnA = 0;
     int plmnB = 0;

     try {
      plmnA = a.getInt("plmn");
      plmnB = b.getInt("plmn");
     } catch (JSONException e) {
      //do something
     }
     if (plmnA == plmnB) return 0;
     else if (plmnA > plmnB) return 1;
     else return -1;
    }
   });
  } catch (Exception E) {
   fileLogger.error("Exception occurs in getSortedList method");
  }
  fileLogger.info("Exit Function : getSortedList");
  return list;
 }


 @GET
 @Path("/getcurrentcdrdata")
 @Produces(MediaType.APPLICATION_JSON)
 public Response getCurrentCdrData(@Context HttpServletRequest req, @QueryParam("size") String size, @QueryParam("searchKey") String searchKey) {
  //String query = "select distinct cdrlogs_current.*,mobiletype.mobile_type from cdrlogs_current left join mobiletype on (cdrlogs_current.imsi = mobiletype.imsi::character varying) where oprid = (select max(id) from oprrationdata) order by inserttime;";
  HttpSession ss = req.getSession();
  String queryFilter = "";
  String sizeFilter = "";
  if (ss.getAttribute("mobtime") != null) {
   String mobtime = ss.getAttribute("mobtime").toString();
   queryFilter = " and inserttime >= '" + mobtime + "'";
  }
  String searchFilter = "";
  if (!searchKey.equals("")) {
   searchFilter = " and (cdrlogs.imei like '%" + searchKey + "%' or cdrlogs.imei like '%" + searchKey + "%' or mobiletype.mobile_type like '%" + searchKey + "%')";
  }


  // String query="select a.*,cdroprreport.oprname,cdroprreport.country,cdroprreport.c_opr,cdroprreport.c_count from (select distinct cdrlogs_current.*,case when mobiletype.mobile_type is null then 'Not_Available' else mobiletype.mobile_type end mobile_type from cdrlogs_current left join mobiletype on (cdrlogs_current.imei = mobiletype.imei::character varying) where oprid = (select max(id) from oprrationdata) "+queryFilter+" order by inserttime desc) a  inner join cdroprreport on (a.id = cdroprreport.id);";
  if (!size.equalsIgnoreCase("all")) {
   sizeFilter = " limit " + size;
  }
  try {
   String query = "select a.stype,a.imsi,a.imei,a.ta,a.mobile_type,(a.inserttime + time '05:30') as tt,cdroprreport.oprname,cdroprreport.country,cdroprreport.c_opr,cdroprreport.c_count,cdroprreport.realrxl,cdroprreport.band,cdroprreport.mcc,cdroprreport.mnc from (select distinct cdrlogs.*,case when mobiletype.mobile_type is null then '0' else mobiletype.mobile_type end mobile_type from cdrlogs left join mobiletype on (cdrlogs.imei = mobiletype.imei::character varying or cdrlogs.imsi = mobiletype.imsi::character varying) where oprid = (select max(id) from oprrationdata) and cdrlogs.imei!='000000000000000'" + queryFilter + searchFilter + ") a  inner join cdroprreport on (a.id = cdroprreport.id) order by a.inserttime desc" + sizeFilter;
   JSONArray listDetail = new Operations().getJson(query);
   query = "select count(*) as count from (select distinct cdrlogs.*,case when mobiletype.mobile_type is null then '0' else mobiletype.mobile_type end mobile_type from cdrlogs left join mobiletype on (cdrlogs.imei = mobiletype.imei::character varying or cdrlogs.imsi = mobiletype.imsi::character varying) where oprid = (select max(id) from oprrationdata) and cdrlogs.imei!='000000000000000'" + queryFilter + searchFilter + ") a  inner join cdroprreport on (a.id = cdroprreport.id)";
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
 public Response getCurrentComingNetworkData(@Context HttpServletRequest req) {
  //String query = "select distinct cdrlogs_current.*,mobiletype.mobile_type from cdrlogs_current left join mobiletype on (cdrlogs_current.imsi = mobiletype.imsi::character varying) where oprid = (select max(id) from oprrationdata) order by inserttime;";
  HttpSession ss = req.getSession();
  String queryFilter = "";
  if (ss.getAttribute("celltime") != null) {
   String cellTime = ss.getAttribute("celltime").toString();
   queryFilter = " and inserttime >= '" + cellTime + "'";
  }

  // String query="select a.*,cdroprreport.oprname,cdroprreport.country,cdroprreport.c_opr,cdroprreport.c_count from (select distinct cdrlogs_current.*,case when mobiletype.mobile_type is null then 'Not_Available' else mobiletype.mobile_type end mobile_type from cdrlogs_current left join mobiletype on (cdrlogs_current.imei = mobiletype.imei::character varying) where oprid = (select max(id) from oprrationdata) "+queryFilter+" order by inserttime desc) a  inner join cdroprreport on (a.id = cdroprreport.id);";
  //String query="select mcc,mnc,lac,tac,cell,packet_type,band,arfcn,uarfcn,earfcn,freq,bcc,ncc,psc,pci,rssi,snr,operators,packet_id,index_id,(inserttime + time '05:30') as tt from oprlogs_current where oprid = (select max(id) from oprrationdata) "+queryFilter+" order by inserttime asc;";
  String query = "select mcc,mnc,lat,lon,lac,tac,cell,packet_type,band,arfcn,uarfcn,earfcn,freq,bcc,ncc,psc,pci,rssi,snr,operators,packet_id,index_id,(inserttime + time '05:30') as tt from oprlogs_current where oprid = (select max(id) from oprrationdata) " + queryFilter + " order by inserttime asc;";
  JSONArray jo = new Operations().getJson(query);
  return Response.status(201).entity(jo.toString()).build();
 }

 @POST
 @Path("/deleteoperation")
 @Produces(MediaType.APPLICATION_JSON)
 public Response deleteOpr(@Context HttpServletRequest req, @FormParam("oprId") String oprId) {
  Common co = new Common();
  String query = "delete from oprrationdata where id in (" + oprId + ")";
  boolean status = co.executeDLOperation(query);
  HashMap < String, String > rs = new HashMap < String, String > ();
  if (status) {
   rs.put("result", "success");
  } else {
   rs.put("result", "failure");
  }
  return Response.status(201).entity(rs).build();
 }

 @POST
 @Path("/deletealloperation")
 @Produces(MediaType.APPLICATION_JSON)
 public Response deleteAllOpr(@Context HttpServletRequest req) {
  Common co = new Common();
  String query = "delete from oprrationdata where status in ('0','2')";
  boolean status = co.executeDLOperation(query);
  HashMap < String, String > rs = new HashMap < String, String > ();
  if (status) {
   rs.put("result", "success");
  } else {
   rs.put("result", "failure");
  }
  return Response.status(201).entity(rs).build();
 }


 @GET
 @Path("/reportserver")
 @Produces(MediaType.APPLICATION_JSON)
 public Response getreport(@Context HttpServletRequest req, @QueryParam("oprId") String oprId) {
  String separator = "\t";
  String res = new ReportServer().createReports(oprId, null, null, separator, "reports","active");
  return Response.status(201).entity(res).build();
 }

 @GET
 @Path("/databackup")
 @Produces(MediaType.APPLICATION_JSON)
 public String getReportBackup(@Context HttpServletRequest req, @QueryParam("startTime") String startTime, @QueryParam("endTime") String endTime) {
  String separator = "\t";
  String res = new ReportServer().createReports(null, startTime, endTime, separator, "reports","active");

  

  try {
	  DateFormat formatterUTC = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	  formatterUTC.setTimeZone(TimeZone.getTimeZone("UTC")); // UTC timezone
	  Date tempStartTime = formatterUTC.parse(startTime);
	  Date tempEndTime = formatterUTC.parse(endTime);
	  DateFormat formatterIST = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	  formatterIST.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
	  startTime = formatterIST.format(tempStartTime);
	  endTime = formatterIST.format(tempEndTime);

   //Report Download audit logs
   LinkedHashMap < String, String > log = new LinkedHashMap < String, String > ();
   log.put("source", "Falcon");
   log.put("start time", startTime);
   log.put("end time", endTime);
   new AuditHandler().auditReportDownload(log);
   //report download audit logs

  } catch (ParseException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  }

  return res;
 }

 @GET
 @Path("/systemlogs")
 @Produces(MediaType.APPLICATION_JSON)
 public String getSystemLogs() {
  String res = new ReportServer().createSystemLogs();

  //system logs audit log
  LinkedHashMap < String, String > log = new LinkedHashMap < String, String > ();
  log.put("action", "System Logs");
  new AuditHandler().auditBackup(log);
  //system logs audit log

  return res;
 }

 @GET
 @Path("/fulldbbackup")
 @Produces(MediaType.APPLICATION_JSON)
 public String fullDbBackup() {
  fileLogger.info("Inside Function : fullDbBackup");
  String res = new DbBackup().performBackUp();
  try {
   JSONObject backupObj = new JSONObject(res);
   if (backupObj.getString("result").equalsIgnoreCase("success")) {
    String childDirectoryPath = backupObj.getString("dir");
    //String sysLogZippedFile = childDirectoryPath+".tar.gz";
    //Compress compress=new Compress(sysLogZippedFile, "DB_Backup");
    //childDirectoryPath=childDirectoryPath.substring(1);
    //compress.writedir(Paths.get(childDirectoryPath));
    fileLogger.debug("@fulldbbackup root directory path is :" + childDirectoryPath);
    ReportServer.zipFolder(childDirectoryPath, childDirectoryPath + ".zip");
    try {
     FileUtils.deleteDirectory(new File(childDirectoryPath));
    } catch (Exception e) {
     fileLogger.error("@fulldbbackup exception is :" + e.getMessage());
     // TODO Auto-generated catch block
     e.printStackTrace();
    }

    //full db backup audit log
    LinkedHashMap < String, String > log = new LinkedHashMap < String, String > ();
    log.put("action", "Full Backup");
    new AuditHandler().auditBackup(log);
    //full db backup audit log

   }
  } catch (JSONException e) {
   // TODO Auto-generated catch block
   fileLogger.error("Exception in fullDbBackup :" + e.getMessage());
   e.printStackTrace();
  }
  /* catch (IOException e) {
  					// TODO Auto-generated catch block
  					fileLogger.debug("Exception in fullDbBackup :"+e.getMessage());
  					e.printStackTrace();
  				}*/
  catch (Exception e) {
   // TODO Auto-generated catch block
   fileLogger.error("Exception in fullDbBackup :" + e.getMessage());
   e.printStackTrace();
  }
  fileLogger.info("Exit Function : fullDbBackup");
  return res;
 }

 @GET
 @Path("/datapurge")
 @Produces(MediaType.APPLICATION_JSON)
 public String purgeData(@Context HttpServletRequest req, @QueryParam("startTime") String startTime, @QueryParam("endTime") String endTime) {

  List < String > queryList = new ArrayList < String > ();
  ArrayList < String > queryTableList = new ArrayList < String > ();
  queryTableList.add("log_audit");
  queryTableList.add("cdrlogs_current");
  queryTableList.add("cdrlogs");
  queryTableList.add("oprlogs_current");
  queryTableList.add("oprlogs");
  queryTableList.add("tracked_imsi");
  queryTableList.add("trigger_cue");
  queryTableList.add("log_evnts");
  queryTableList.add("currentrunningoperationevnets");
  for (String tableName: queryTableList) {
   if (tableName.equals("cdrlogs") || tableName.equals("cdrlogs_current") || tableName.equals("oprlogs") || tableName.equals("oprlogs_current")) {
    queryList.add("delete from " + tableName + " where inserttime between '" + startTime + "' and '" + endTime + "'");
   } else if (tableName.equals("log_audit") || tableName.equals("tracked_imsi") || tableName.equals("currentrunningoperationevnets")) {
    queryList.add("delete from " + tableName + " where logtime between '" + startTime + "' and '" + endTime + "'");
   } else if (tableName.equals("log_evnts")) {
    queryList.add("delete from " + tableName + " where log_date between '" + startTime + "' and '" + endTime + "'");
   } else {
    queryList.add("delete from " + tableName + " where process_date between '" + startTime + "' and '" + endTime + "'");
   }
  }
  new Common().executeBatchOperation(queryList);
  return "{\"result\":\"success\",\"msg\":\"\"}";
 }

 @GET
 @Path("/dataresetall")
 @Produces(MediaType.APPLICATION_JSON)
 public String resetAllData(@QueryParam("eventDataType") String eventDataType) {

  List < String > queryList = new ArrayList < String > ();
  LinkedHashMap < String, String > log = new LinkedHashMap < String, String > ();
  ArrayList < String > queryTableList = new ArrayList < String > ();
  if (eventDataType.equalsIgnoreCase("all")) {
   //DBDataService.setAntennaToVehicleDiffAngle(361);
   queryTableList.add("oprlogs_current");
   queryTableList.add("oprlogs");
   queryTableList.add("cdrlogs_current");
   queryTableList.add("cdrlogs");
   queryTableList.add("tracked_imsi");
   queryTableList.add("trigger_cue");
   queryTableList.add("log_evnts");
   queryTableList.add("log_audit");
   queryTableList.add("currentrunningoperationevnets");
   //queryTableList.add("netscan_gps_data");
   //queryTableList.add("gpsdata");
   log.put("type", "All");
  } else if (eventDataType.equalsIgnoreCase("cell_infrastructure")) {
   queryTableList.add("oprlogs_current");
   queryTableList.add("oprlogs");
   log.put("type", "Cell Infrastructure");
  } else if (eventDataType.equalsIgnoreCase("data_events")) {
   queryTableList.add("cdrlogs_current");
   queryTableList.add("cdrlogs");
   queryTableList.add("tracked_imsi");
   queryTableList.add("trigger_cue");
   queryTableList.add("log_evnts");
   queryTableList.add("currentrunningoperationevnets");
   log.put("type", "Data Events");
  } else if (eventDataType.equalsIgnoreCase("audit_logs")) {
   queryTableList.add("log_audit");
   log.put("type", "Audit Logs");
  } else if (eventDataType.equalsIgnoreCase("gps_data")) {
   //DBDataService.setAntennaToVehicleDiffAngle(361);
   queryTableList.add("netscan_gps_data");
   queryTableList.add("gpsdata");
   log.put("type", "GPS Data");
  }

  for (String tableName: queryTableList) {
   queryList.add("delete from " + tableName);
  }
  new AuditHandler().auditDataReset(log);
  //data reset all audit logs

  new Common().executeBatchOperation(queryList);
  return "{\"result\":\"success\",\"msg\":\"\"}";
 }

 @POST
 @Path("/getCurrentEvents")
 @Produces(MediaType.APPLICATION_JSON)
 public Response getreport(@Context HttpServletRequest req) {

  String query = "select event_msg,date_trunc('second',logtime + '05:30:00'::interval) logtime1 from currentrunningoperationevnets where evt_tgr_id = (select max(evt_tgr_id) from currentrunningoperationevnets) order by logtime";
  JSONArray js = new Operations().getJson(query);
  return Response.status(201).entity(js.toString()).build();
 }

 public void stopAllOpr() {
  new Common().executeDLOperation("update oprrationdata set status='0',stoptime=timezone('utc'::text, now()) where status!='0' and id=(select max(id) from oprrationdata)");
 }

 @POST
 @Path("/getallsysconfigs")
 @Produces(MediaType.APPLICATION_JSON)
 public Response getAllSysConfigs(@Context HttpServletRequest req) {
  fileLogger.info("Inside Function : getAllSysConfigs");
  //fileLogger.debug("logger is :"+logger);
  fileLogger.debug("in getallsysconfigs");
  String query = "select * from system_properties order by id";
  JSONArray js = new Operations().getJson(query);
  fileLogger.debug("out getallsysconfigs");
  fileLogger.info("Exit Function : getAllSysConfigs");
  return Response.status(201).entity(js.toString()).build();
 }

 @POST
 @Path("/updatesysconfig")
 @Produces(MediaType.APPLICATION_JSON)
 public Response updateSysConfig(@Context HttpServletRequest req, @FormParam("name") String name, @FormParam("value") String value, @FormParam("code") int code) {
  fileLogger.info("Inside Function : updateSysConfig");
  //fileLogger.debug("In updateSysConfig");
  fileLogger.debug("name is :" + name + "value is :" + value + "code is :" + code);
  HashMap < String, String > resultMap = new HashMap < String, String > ();
  String query = "";
  String query2 = "";
  String query3 = "";
  String query4 = "";
  boolean scanningInProgress=false;
  if (name.equalsIgnoreCase("schedulertime")) {
   //NetscanSingletonExecutor netscanSingletonExecutorInstance=NetscanSingletonExecutor.getInstance();
   //netscanSingletonExecutorInstance.shutdownScannerSchedulerOperation();
   // ScheduledExecutorService scheduler = netscanSingletonExecutorInstance.getScheduler();
   query = "update system_properties set value='" + value + "' where key='schedulertime'";
   //NetscanTask netscanTask=new NetscanTask();
   //scheduler.scheduleAtFixedRate(netscanTask, 5, (Integer.parseInt(value)*60), TimeUnit.SECONDS);

   //stop the scanner scheduler
   try {
    DBDataService dbDataService = DBDataService.getInstance();
    //set interrupted flag to true
    //stop the running scanning
    NetscanSchedulerListener netscanSchedulerListener = new NetscanSchedulerListener();
    netscanSchedulerListener.stopNetscanScheduler();
    //stop the scanner scheduler
    dbDataService.shutdownScannerScheduler();
    Thread.sleep(1000);
    //start the scanner scheduler
    netscanSchedulerListener.startNetscanScheduler();

   } catch (Exception e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
   }
  }
  
  
  
  
  
  
  else if (name.equalsIgnoreCase("hold_time")) {
	    query = "update system_properties set value='" + value + "' where key='hold_time'";
	    DBDataService.configParamMap.put(("HoldTime"),value);
	    try {
	    	DBDataService dbDataService = DBDataService.getInstance();
	  
	   } catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	   }
	  }
  
  
  
  else if (name.equalsIgnoreCase("operation_mode")) {
	    query = "update system_properties set value='" + value + "' where key='operation_mode'";
	    try {
	    	DBDataService dbDataService = DBDataService.getInstance();
	    	if(value.equalsIgnoreCase("1")) {
			    String tracktimeUpdate2="update system_properties set value =0   where key='hold_time';";
				 
			    new Common().executeDLOperation(tracktimeUpdate2);
			    DBDataService.configParamMap.put(("HoldTime"),"0");
			    

	    	}
	    	else {
	    		String tracktimeUpdate2="update system_properties set value =10   where key='hold_time';";
				 
			    new Common().executeDLOperation(tracktimeUpdate2);
			    DBDataService.configParamMap.put(("HoldTime"),"10");
	    	}
	   } catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	   }
	  } 
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  else if (name.equalsIgnoreCase("system_mode")) {
   int gpsNode = DBDataService.getGpsNode();
   int systemType = DBDataService.getSystemType();
   int systemMode = code;

   if (systemMode == 1) {
    DBDataService.setAntennaToVehicleDiffAngle(361);
    if (systemType == 2) {
     resultMap.put("result", "not qualified");
     resultMap.put("msg", "System Type configured is Integrated");
     return Response.status(201).entity(resultMap).build();
    }
    /*							  if(gpsNode==1){
    								  resultMap.put("result","not qualified");
    								  resultMap.put("msg","GPS selection is from STRU");
    								  return Response.status(201).entity(resultMap).build();
    							  }*/
   }

   DBDataService.setSystemMode(code);
   query = "update system_properties set value='" + value + "',code=" + code + " where key='" + name + "'";

   if (gpsNode == 1) {
    if (systemMode == 0) {
     if (systemType == 2) {
      //take from hummer static lat,long and offset  ,integrated,static,stru
      //new TRGLController().sendOffsetRequestToHummer();
     } else {
      //take from stru static lat,long and offset  ,standlone,static,stru
       try {
       JSONArray ptzDataArray = new Operations().getJson("select * from view_btsinfo where code = 9");
       JSONObject hummerDataObject = ptzDataArray.getJSONObject(0);
       new PTZ().sendGPSCommand(hummerDataObject.getString("ip"), Integer.parseInt(DBDataService.getConfigParamMap().get("ptz_port")), true, false);
       new PTZ().sendGPSCommand(hummerDataObject.getString("ip"), Integer.parseInt(DBDataService.getConfigParamMap().get("ptz_port")), false, false);
      } catch (UnknownHostException e) {
       fileLogger.error("UnknownHostException occurs");
       // TODO Auto-generated catch block
       e.printStackTrace();
      } catch (IOException e) {
       fileLogger.error("IOException occurs");
       // TODO Auto-generated catch block
       e.printStackTrace();
      } catch (JSONException e) {
       fileLogger.error("JSONException occurs");
      } catch (Exception e) {
       fileLogger.error("Exception occurs");
      }
     }
    } else {
     if (systemType == 2) {
      //take from hummer continuous lat,long and offset  ,integrated,moving,stru
     } else {
      //take from stru continuous lat,long and offset  ,standlone,moving,stru
       try {
       JSONArray ptzDataArray = new Operations().getJson("select * from view_btsinfo where code = 9");
       JSONObject hummerDataObject = ptzDataArray.getJSONObject(0);
       new PTZ().sendGPSCommand(hummerDataObject.getString("ip"), Integer.parseInt(DBDataService.getConfigParamMap().get("ptz_port")), true, true);
      } catch (UnknownHostException e) {
       // TODO Auto-generated catch block
       e.printStackTrace();
      } catch (IOException e) {
       // TODO Auto-generated catch block
       e.printStackTrace();
      } catch (JSONException e) {
       e.printStackTrace();
      } catch (Exception e) {
       e.printStackTrace();
      }
     }
    }
   }else if(gpsNode == 2){
	   if (systemMode == 0) {
		     if (systemType == 2) {
		      //take from hummer static lat,long and offset  ,integrated,static,stru
		      new TRGLController().sendOffsetRequestToHummer();
		     } else {
		      //take from stru static lat,long and offset  ,standlone,static,stru
/*		      try {
		       JSONArray ptzDataArray = new Operations().getJson("select * from view_btsinfo where code = 9");
		       JSONObject hummerDataObject = ptzDataArray.getJSONObject(0);
		       new PTZ().sendGPSCommand(hummerDataObject.getString("ip"), Integer.parseInt(DBDataService.getConfigParamMap().get("ptz_port")), true, false);
		       new PTZ().sendGPSCommand(hummerDataObject.getString("ip"), Integer.parseInt(DBDataService.getConfigParamMap().get("ptz_port")), false, false);
		      } catch (UnknownHostException e) {
		       fileLogger.debug("UnknownHostException occurs");
		       // TODO Auto-generated catch block
		       e.printStackTrace();
		      } catch (IOException e) {
		       fileLogger.debug("IOException occurs");
		       // TODO Auto-generated catch block
		       e.printStackTrace();
		      } catch (JSONException e) {
		       fileLogger.debug("JSONException occurs");
		      } catch (Exception e) {
		       fileLogger.debug("Exception occurs");
		      }*/
		     }
		    } else {
		     if (systemType == 2) {
		      //take from hummer continuous lat,long and offset  ,integrated,moving,stru
		     } else {
		      //take from stru continuous lat,long and offset  ,standlone,moving,stru
/*		      try {
		       JSONArray ptzDataArray = new Operations().getJson("select * from view_btsinfo where code = 9");
		       JSONObject hummerDataObject = ptzDataArray.getJSONObject(0);
		       new PTZ().sendGPSCommand(hummerDataObject.getString("ip"), Integer.parseInt(DBDataService.getConfigParamMap().get("ptz_port")), true, true);
		      } catch (UnknownHostException e) {
		       // TODO Auto-generated catch block
		       e.printStackTrace();
		      } catch (IOException e) {
		       // TODO Auto-generated catch block
		       e.printStackTrace();
		      } catch (JSONException e) {
		       e.printStackTrace();
		      } catch (Exception e) {
		       e.printStackTrace();
		      }*/
		     }
		    }
   }


  } else if (name.equalsIgnoreCase("system_type")) {
   int systemMode = DBDataService.getSystemMode();
   int gpsNode = DBDataService.getGpsNode();
 
   int systemType = DBDataService.getSystemType();
   
//   if(systemType!= code){
//	   resultMap.put("result", "not qualified");
//	     resultMap.put("msg", "System Type not Changed");
//	     return Response.status(201).entity(resultMap).build();
//	    
//   }
   scanningInProgress=true;
   if (code == 2) {
    if (systemMode == 1) {
     resultMap.put("result", "not qualified");
     resultMap.put("msg", "System Mode configured is Moving");
     return Response.status(201).entity(resultMap).build();
    }
    if (gpsNode == 1) {
//     resultMap.put("result", "not qualified");
//     resultMap.put("msg", "GPS selection configured is STRU");
//     return Response.status(201).entity(resultMap).build();
    }
   }else{
	  if(gpsNode == 2){
		     resultMap.put("result", "not qualified");
		     resultMap.put("msg", "GPS selection configured is Hummer");
		     return Response.status(201).entity(resultMap).build(); 
	  }
   }
   DBDataService.setSystemType(code);
   query = "update system_properties set value='" + value + "',code=" + code + " where key='" + name + "'";
   //
   if (code == 0){
	 //integrated to standalone
	 query2 = "update antenna set inscanning=false,intracking=false";
	query3 = "update antenna set inscanning=true,intracking=true where id=1";
     // query2 = "update antenna set inscanning=false,intracking=false where id=18 or id=20 or id=22";
     query4 = "update manual_override set status=true";
   }else if(code == 1){
	   query2 = "update antenna set inscanning=false,intracking=false";
	   query3 = "update antenna set inscanning=true,intracking=true where atype='1'";
	   query4 = "update manual_override set status=true";
   } else {
	   //standalone to integrated 
	   query2 = "update antenna set inscanning=false,intracking=false";
	   query3 = "update antenna set inscanning=true,intracking=true where atype='1'";
   }


   if (gpsNode == 1) {
    if (systemMode == 0) {
     if (systemType == 2) {
      //take from hummer static lat,long and offset  ,integrated,static,stru
      //new TRGLController().sendOffsetRequestToHummer();
     } else {
      //take from stru static lat,long and offset  ,standlone,static,stru
	   try {
       JSONArray ptzDataArray = new Operations().getJson("select * from view_btsinfo where code = 9");
       JSONObject hummerDataObject = ptzDataArray.getJSONObject(0);
       new PTZ().sendGPSCommand(hummerDataObject.getString("ip"), Integer.parseInt(DBDataService.getConfigParamMap().get("ptz_port")), true, false);
       new PTZ().sendGPSCommand(hummerDataObject.getString("ip"), Integer.parseInt(DBDataService.getConfigParamMap().get("ptz_port")), false, false);
      } catch (UnknownHostException e) {
       // TODO Auto-generated catch block
       e.printStackTrace();
      } catch (IOException e) {
       // TODO Auto-generated catch block
       e.printStackTrace();
      } catch (JSONException e) {

      }
     }
    } else {
     if (systemType == 2) {
      //take from hummer continuous lat,long and offset  ,integrated,moving,stru
     } else {
      //take from stru continuous lat,long and offset  ,standlone,moving,stru
    	try {
       JSONArray ptzDataArray = new Operations().getJson("select * from view_btsinfo where code = 9");
       JSONObject hummerDataObject = ptzDataArray.getJSONObject(0);
       new PTZ().sendGPSCommand(hummerDataObject.getString("ip"), Integer.parseInt(DBDataService.getConfigParamMap().get("ptz_port")), true, true);
      } catch (UnknownHostException e) {
       // TODO Auto-generated catch block
       e.printStackTrace();
      } catch (IOException e) {
       // TODO Auto-generated catch block
       e.printStackTrace();
      } catch (JSONException e) {

      }
     }
    }
   }else if(gpsNode==2){
	    if (systemMode == 0) {
	        if (systemType == 2) {
	         //take from hummer static lat,long and offset  ,integrated,static,stru
	         new TRGLController().sendOffsetRequestToHummer();
	        } else {
	         //take from stru static lat,long and offset  ,standlone,static,stru
/*	         try {
	          JSONArray ptzDataArray = new Operations().getJson("select * from view_btsinfo where code = 9");
	          JSONObject hummerDataObject = ptzDataArray.getJSONObject(0);
	          new PTZ().sendGPSCommand(hummerDataObject.getString("ip"), Integer.parseInt(DBDataService.getConfigParamMap().get("ptz_port")), true, false);
	          new PTZ().sendGPSCommand(hummerDataObject.getString("ip"), Integer.parseInt(DBDataService.getConfigParamMap().get("ptz_port")), false, false);
	         } catch (UnknownHostException e) {
	          // TODO Auto-generated catch block
	          e.printStackTrace();
	         } catch (IOException e) {
	          // TODO Auto-generated catch block
	          e.printStackTrace();
	         } catch (JSONException e) {

	         }*/
	        }
	       } else {
	        if (systemType == 2) {
	         //take from hummer continuous lat,long and offset  ,integrated,moving,stru
	        } else {
	         //take from stru continuous lat,long and offset  ,standlone,moving,stru
/*	         try {
	          JSONArray ptzDataArray = new Operations().getJson("select * from view_btsinfo where code = 9");
	          JSONObject hummerDataObject = ptzDataArray.getJSONObject(0);
	          new PTZ().sendGPSCommand(hummerDataObject.getString("ip"), Integer.parseInt(DBDataService.getConfigParamMap().get("ptz_port")), true, true);
	         } catch (UnknownHostException e) {
	          // TODO Auto-generated catch block
	          e.printStackTrace();
	         } catch (IOException e) {
	          // TODO Auto-generated catch block
	          e.printStackTrace();
	         } catch (JSONException e) {

	         }*/
	        }
	       }
   }

  } else if (name.equalsIgnoreCase("gps_node")) {
   int systemMode = DBDataService.getSystemMode();
   int systemType = DBDataService.getSystemType();
   int gpsNode = code;
   if (gpsNode == 1) {
//    if (systemType == 2) {
//	   resultMap.put("result", "not qualified");
//     resultMap.put("msg", "System Type configured is Integrated");
//     return Response.status(201).entity(resultMap).build();
//    }
   
   }
   else if(gpsNode == 2){
	   if(systemType!=2){
		     resultMap.put("result", "not qualified");
		     resultMap.put("msg", "Hummer selection is for Integrated System Type only");
		     return Response.status(201).entity(resultMap).build();
	   }
	   
   }

   query = "update system_properties set value='" + value + "',code=" + code + " where key='" + name + "'";
   DBDataService.setGpsNode(code);
   try {

    if (gpsNode == 1) {
     if (systemMode == 0) {
    //  if (systemType == 2) {
       //take from hummer static lat,long and offset  ,integrated,static,stru
       //new TRGLController().sendOffsetRequestToHummer();
     // } 
     // else {
       //take from stru static lat,long and offset  ,standlone,static,stru
        try {
        JSONArray ptzDataArray = new Operations().getJson("select * from view_btsinfo where code = 9");
        JSONObject hummerDataObject = ptzDataArray.getJSONObject(0);
        new PTZ().sendGPSCommand(hummerDataObject.getString("ip"), Integer.parseInt(DBDataService.getConfigParamMap().get("ptz_port")), true, false);
        new PTZ().sendGPSCommand(hummerDataObject.getString("ip"), Integer.parseInt(DBDataService.getConfigParamMap().get("ptz_port")), false, false);
       } catch (UnknownHostException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
       } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
       } catch (JSONException e) {

       
      }
     } else {
      if (systemType == 2) {
       //take from hummer continuous lat,long and offset  ,integrated,moving,stru
      } else {
       //take from stru continuous lat,long and offset  ,standlone,moving,stru
    	try {
        JSONArray ptzDataArray = new Operations().getJson("select * from view_btsinfo where code = 9");
        JSONObject hummerDataObject = ptzDataArray.getJSONObject(0);
        new PTZ().sendGPSCommand(hummerDataObject.getString("ip"), Integer.parseInt(DBDataService.getConfigParamMap().get("ptz_port")), true, true);
       } catch (UnknownHostException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
       } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
       } catch (JSONException e) {

       }
      }
     }
    }else if(gpsNode==2){
        if (systemMode == 0) {
            if (systemType == 2) {
             //take from hummer static lat,long and offset  ,integrated,static,stru
             new TRGLController().sendOffsetRequestToHummer();
            } else {
             //take from stru static lat,long and offset  ,standlone,static,stru
/*             try {
              JSONArray ptzDataArray = new Operations().getJson("select * from view_btsinfo where code = 9");
              JSONObject hummerDataObject = ptzDataArray.getJSONObject(0);
              new PTZ().sendGPSCommand(hummerDataObject.getString("ip"), Integer.parseInt(DBDataService.getConfigParamMap().get("ptz_port")), true, false);
              new PTZ().sendGPSCommand(hummerDataObject.getString("ip"), Integer.parseInt(DBDataService.getConfigParamMap().get("ptz_port")), false, false);
             } catch (UnknownHostException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
             } catch (IOException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
             } catch (JSONException e) {

             }*/
            }
           } else {
            if (systemType == 2) {
             //take from hummer continuous lat,long and offset  ,integrated,moving,stru
            } else {
             //take from stru continuous lat,long and offset  ,standlone,moving,stru
/*             try {
              JSONArray ptzDataArray = new Operations().getJson("select * from view_btsinfo where code = 9");
              JSONObject hummerDataObject = ptzDataArray.getJSONObject(0);
              new PTZ().sendGPSCommand(hummerDataObject.getString("ip"), Integer.parseInt(DBDataService.getConfigParamMap().get("ptz_port")), true, true);
             } catch (UnknownHostException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
             } catch (IOException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
             } catch (JSONException e) {

             }*/
            }
           }
    }

   } catch (Exception e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
   }

  } else {
   query = "update system_properties set value='" + value + "' where key='" + name + "'";
  }

  Common common = new Common();
  if (common.executeDLOperation(query)) {
   if (name.equalsIgnoreCase("system_type")) {
    common.executeDLOperation(query2);
    common.executeDLOperation(query3);
    if (code != 2) {
     common.executeDLOperation(query4);
    }
   }
   resultMap.put("result", "success");
   /*						  	LinkedHashMap<String,String> sysConfigLog = new LinkedHashMap<String,String>();
   						  	sysConfigLog.put("parameter",name);
   						  	sysConfigLog.put("value",value);
   					   		new AuditHandler().auditSysConfig(sysConfigLog);*/
   //new AuditHandler().auditLog(log, 3);
   if(scanningInProgress){
	   //Restarting Scanner
	   fileLogger.debug("@updateSysConfig HUA RESTART");
	   restartScanner();
   }
  } else {
   resultMap.put("result", "failure");
  }
  //fileLogger.debug("out updateSysConfig"); 
  fileLogger.info("Exit Function : updateSysConfig");
  return Response.status(201).entity(resultMap).build();
 }

 @POST
 @Path("/getalldeplconfigs")
 @Produces(MediaType.APPLICATION_JSON)
 public Response getAllDeplConfigs(@Context HttpServletRequest req) {
  fileLogger.info("Inside Function : getAllDeplConfigs");
  //fileLogger.debug("logger is :"+logger);
 // fileLogger.debug("in getAllDeplConfigs");
  String query = "select * from depl_config order by id";
  JSONArray js = new Operations().getJson(query);
 // fileLogger.debug("out getAllDeplConfigs");
  fileLogger.info("Exit Function : getAllDeplConfigs");
  return Response.status(201).entity(js.toString()).build();
 }

 @POST
 @Path("/updatedeplconfig")
 @Produces(MediaType.APPLICATION_JSON)
 public Response updateDeplConfig(@Context HttpServletRequest req, @FormParam("name") String name, @FormParam("value") String value) {
  fileLogger.info("Inside Function : updateDeplConfig");
 // fileLogger.debug("In updateDeplConfig");
  fileLogger.debug("name is :" + name + "value is :" + value);
  HashMap < String, String > resultMap = new HashMap < String, String > ();
  String query = "";
  query = "update depl_config set value='" + value + "' where key='" + name + "'";


  if (new Common().executeDLOperation(query)) {
   resultMap.put("result", "success");
   /*						  	LinkedHashMap<String,String> sysConfigLog = new LinkedHashMap<String,String>();
   						  	sysConfigLog.put("parameter",name);
   						  	sysConfigLog.put("value",value);
   					   		new AuditHandler().auditSysConfig(sysConfigLog);*/
   //new AuditHandler().auditLog(log, 3);
  } else {
   resultMap.put("result", "failure");
  }
  fileLogger.info("Exit Function : updateDeplConfig");
  //fileLogger.debug("out updateDeplConfig");
  return Response.status(201).entity(resultMap).build();
 }

 /*			   @GET
 			   @Path("/triggerevent")
 			   @Produces(MediaType.APPLICATION_JSON)
 			   public Response triggerEvent(int freq,double lat,double lon ,int timeout,int validity,String ip)
 			   {	
 				   try{
 				   DBDataService dbDataService = DBDataService.getInstance();
 				   Operations operations = new Operations();
 				   String queStatus = operations.getJson("select value from system_properties where key='queuestatus'").getJSONObject(0).getString("value"); 
 				   JSONObject oprDataObj = operations.getJson("select * from oprrationdata where id=(select max(id) from oprrationdata)").getJSONObject(0);
 				    int oprStatus = oprDataObj.getInt("state");
 				    String oprType = oprDataObj.getString("opr_type");
 				    if(!queStatus.equalsIgnoreCase("disable") || (Integer.parseInt(oprType)!=1 && oprStatus==1)){
 				    	fileLogger.debug("Not Permitting Trigger to process");
 				    	fileLogger.debug("Queue Status is :"+queStatus+":OprStatus is :"+oprStatus+":oprType is :"+oprType);
 				   }else{
 					   fileLogger.debug("Permitted for the trigger to process");
 					   String eventName="";
 					   String note=null;
 					   String operators=oprDataObj.getString("plmn");
 					   int coverageDistance = oprDataObj.getInt("distance");
 					   int frequency=-1;
 				   	   double latitude=-1;
 				   	   double longitude=-1;
                         if(freq!=-1){
                         	eventName="trgl";
                         	frequency=freq;
                         }else if(lat!=-1){
                         	eventName="ugs";
                         	latitude=lat;
                         	longitude=lon;
                         }
                         String deviceIp=ip;
                         int periodicity=-1;
 				   		Date date=new Date();
 				   		dbDataService.addToEventPriorityQueue(null,eventName,note,oprType,operators,coverageDistance,deviceIp,frequency,latitude,longitude,periodicity,timeout,validity,date,true);
 				   }
 				   }catch(Exception E){
 					   
 				   }
 				   
 				   if(new Integer(freq)!=null){
 					   return triggerTracking(freq);
 				   }else{
 					   return triggerPosition(lat,lon);
 				   }
 				   
 				   return null;
 			   }*/

 public Response triggerTracking(int freq) {

  OperationCalculations oc = new OperationCalculations();

  LinkedHashMap < String, String > res = oc.checkIfLastOperationIsTriggerOperation();

  if (res.get("result").equalsIgnoreCase("true")) {

   //calulate arfcn and uarfcn
   String fcns = oc.calulateArfcnsFromFreq(freq, -1);
   //String result = new ApiCommon().startTracking(null,fcns,fcns,freq);

  }

  return Response.status(201).entity(res).build();

 }

 public Response triggerPosition(double lat, double lon) {

  OperationCalculations oc = new OperationCalculations();
  LinkedHashMap < String, String > res = oc.checkIfLastOperationIsTriggerOperation();
  if (res.get("result").equalsIgnoreCase("true")) {

   //getting AntennaId
   int antennaId = oc.getAntennaIdFromPosition(lat, lon);
   String result = new ApiCommon().startTracking(null, antennaId);
   // String result = new ApiCommon().startTracking(null,fcns,fcns,freq);
  }

  return Response.status(201).entity(res).build();

 }


 @POST
 @Path("/stopNetscanOperation")
 @Produces(MediaType.APPLICATION_JSON)
 public Response stopNetscanOperation(@Context HttpServletRequest req) {


  NetscanOperations netscanOperations = new NetscanOperations();
  LinkedHashMap < String, String > param = new LinkedHashMap < String, String > ();
  JSONObject netscanData = null;
  try {
   netscanData = new Operations().getJson("select * from view_btsinfo where code=3").getJSONObject(0);
  } catch (Exception E) {

  }
  param = prepareParamForStopNetscan(netscanData, "GSM");
  netscanOperations.sendToServer(param);
//  param = prepareParamForStopNetscan(netscanData, "UMTS");
//  netscanOperations.sendToServer(param);
//  param = prepareParamForStopNetscan(netscanData, "LTE");
//  netscanOperations.sendToServer(param);
  boolean updateStatus = new Common().executeDLOperation("update oprrationdata set status='0',t_stoptime=inserttime,stoptime=timezone('utc'::text, now()) where id=(select max(id) from oprrationdata where opr_type='1')");
  if (updateStatus) {

   return Response.status(201).entity("{\"result\":\"success\",\"msg\":\"Operation Stopped Successfully\"}").build();
  }
  return Response.status(201).entity("{\"result\":\"success\",\"msg\":\"Problem in Stopping Operation\"}").build();
 }

 @POST
 @Path("/addGPS")
 @Produces(MediaType.APPLICATION_JSON)
 public Response addGPS(@Context HttpServletRequest req, @FormParam("lat") String lat, @FormParam("lon") String lon) {
  fileLogger.info("Inside Function : addGPS");
  Operations operations = new Operations();
  JSONArray lastGpsArr = operations.getJson("select lat,lon from gpsdata order by logtime desc limit 1");
  double oldLat = 0.00;
  double oldLon = 0.00;
  if (lastGpsArr.length() != 0) {
   try {
    JSONObject lastGpsObject = lastGpsArr.getJSONObject(0);
    oldLat = lastGpsObject.getDouble("lat");
    oldLon = lastGpsObject.getDouble("lon");
    fileLogger.debug("@gpsdata oldLat is :" + oldLat + ",oldLon is :" + oldLon);
   } catch (JSONException e) {
    fileLogger.error("exception in gpsData message:" + e.getMessage());
    // TODO Auto-generated catch block
    
    e.printStackTrace();
   }
  }

  int angleOffset = 0;
  JSONArray angleOffsetArr = operations.getJson("select angle_offset from antenna where atype='1' limit 1");
  if (angleOffsetArr.length() != 0) {
   try {
    angleOffset = angleOffsetArr.getJSONObject(0).getInt("angle_offset");
   } catch (JSONException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
   }
  }
  String query = "insert into netscan_gps_data (ip,lat,lon,tstmp) values('manual'," + lat + "," + lon + "," + System.currentTimeMillis() + ")";
  new Common().executeDLOperation(query);
  ArrayList < String > serverData = new ArrayList < String > ();

  serverData.add("0");
  serverData.add("0");
  serverData.add("0");
  serverData.add("0");
  serverData.add("" + lat);
  serverData.add("0");
  serverData.add("" + lon);

  int systemMode = DBDataService.getSystemMode();
  fileLogger.debug("@gpsdata systemMode is :" + systemMode);
  if (lastGpsArr.length() != 0) {
   double newLat = Double.parseDouble(lat);
   double newLon = Double.parseDouble(lon);
   fileLogger.debug("@gpsdata newLat is :" + newLat + ",newLon is :" + newLon);

   double distance = Operations.distanceFromLatLon(oldLat, oldLon, newLat, newLon, "K") * 1000;
   distance = Math.round(distance * 100.0) / 100.0;
   double gpsAccuracy = 0.00;
   JSONArray gpsAccuracyArr = operations.getJson("select accuracy from gps_accuracy order by id desc limit 1");
   if (gpsAccuracyArr.length() != 0) {
    try {
     gpsAccuracy = gpsAccuracyArr.getJSONObject(0).getDouble("accuracy");
    } catch (JSONException e) {
     // TODO Auto-generated catch block
     e.printStackTrace();
    }
   }

   if (systemMode == 0) {

    if (distance > gpsAccuracy) {
     int bearing = Common.calcBearingBetweenTwoGpsLoc(oldLat, oldLon, newLat, newLon);
     if (DBDataService.getAntennaToVehicleDiffAngle() > 360) {
      int antennaToVehicleDiffAngle = bearing - angleOffset;
      DBDataService.setAntennaToVehicleDiffAngle(antennaToVehicleDiffAngle);
     }
     angleOffset = Common.calcNewAngleOffset(bearing);
     fileLogger.debug("@gpsdata bearing is :" + bearing);
     new Common().executeDLOperation("update antenna set angle_offset=" + angleOffset + " where atype='1'");
	 DBDataService.setAngleOffset(angleOffset);
     serverData.add("stationary");
     serverData.add(Integer.toString(angleOffset));
     new GPSSocketServer().sendText(serverData);
     return Response.status(201).entity("ok").build();
    }
   } else {
    if (distance > gpsAccuracy) {
     int bearing = Common.calcBearingBetweenTwoGpsLoc(oldLat, oldLon, newLat, newLon);
     if (DBDataService.getAntennaToVehicleDiffAngle() > 360) {
      int antennaToVehicleDiffAngle = bearing - angleOffset;
      DBDataService.setAntennaToVehicleDiffAngle(antennaToVehicleDiffAngle);
     }
     angleOffset = Common.calcNewAngleOffset(bearing);
     fileLogger.debug("@gpsdata bearing is :" + bearing);
     new Common().executeDLOperation("update antenna set angle_offset=" + angleOffset + " where atype='1'");
     DBDataService.setAngleOffset(angleOffset);
     serverData.add("moving");
     serverData.add(Integer.toString(angleOffset));
     new GPSSocketServer().sendText(serverData);
     return Response.status(201).entity("ok").build();
    }
   }

  } else {
   if (systemMode == 0) {
    serverData.add("stationary");
   } else {
    serverData.add("moving");
   }
   serverData.add(Integer.toString(angleOffset));
   new GPSSocketServer().sendText(serverData);
   return Response.status(201).entity("ok").build();
  }
  fileLogger.info("Exit Function : addGPS");
  return Response.status(201).entity("ok").build();

 }

 @POST
 @Path("/addOperation")
 @Produces(MediaType.APPLICATION_JSON)
 public Response addOperation(@Context HttpServletRequest req, @FormParam("oprName") String oprName, @FormParam("oprNote") String oprNote, @FormParam("oprType") String oprType, @FormParam("oprDuration") long oprDuration, @FormParam("oprPlmnFull") String oprPlmnFull, @FormParam("oldData") String oldData) {
	 fileLogger.info("Inside Function : addOperation");
	 fileLogger.debug("oprName is :(" + oprName + ")oprNote is :(" + oprNote + ")oprType is :(" + oprType + ")oprDuration is :(" + oprDuration + ")oprPlmnFull is :(" + oprPlmnFull + ")");
  HashMap < String, String > rsOpr = new HashMap < String, String > ();
  try {
   String selectedOperator = oprPlmnFull;
   String operators = null;
   if (!oprPlmnFull.equals("all")) {
    String[] oprs = oprPlmnFull.split(",");
    fileLogger.debug("oprs is :" + oprs.toString());
    ArrayList < String > oprList = new PossibleConfigurations().getPlmns(oprs);
    fileLogger.debug("opr list is :" + oprs.toString());
    String[] arr = new String[oprList.size()];
    arr = oprList.toArray(arr);
    StringBuilder strBuilder = new StringBuilder("");
    for (String str: arr) {
     strBuilder.append(str);
     strBuilder.append(",");
    }
    oprPlmnFull = strBuilder.toString();
    fileLogger.debug("oprPlmnFull is :" + oprPlmnFull);
    oprPlmnFull = oprPlmnFull.substring(0, oprPlmnFull.length() - 1);
    operators = oprPlmnFull;
   }
   DBDataService dbDataService = DBDataService.getInstance();
   String eventName = "";
   long periodicity = -1;
   CurrentOperationType.generateCueId();
   String cueId = CurrentOperationType.getCueId();
   Date date = new Date();
   if (oprType.equals("1")) {
	eventName = Constants.schedulerEvent;
    periodicity = oprDuration;
    //dbDataService.storeTriggerOnDb(date,cueId,"Falcon","Received Scheduled Transmission,Cue ID:"+cueId);
    //new TriggerCueServer().sendText("event");
   } else if (oprType.equals("2")) {
    //eventName = "automatic";
    eventName = Constants.automaticEvent;
    //dbDataService.storeTriggerOnDb(date,cueId,"Falcon","Received Automatic Transmission,Cue ID:"+cueId);
    //new TriggerCueServer().sendText("event");
   }
   String note = oprNote;
   Operations operations = new Operations();
   int distance = operations.getJson("select value from system_properties where key='coverage'").getJSONObject(0).getInt("value");
   int coverageDistance = distance;
   String device_ip = "localhost";
   double frequency = -1;
   double latitude = -1;
   double longitude = -1;
   int timeout = -1;
   long validity = -1;
   //String transId = new CurrentOperationType().getTransId();
   String transId = null;
   String query = "insert into oprrationdata(name,note,inserttime,opr_type,duration,status,plmn,distance,opr) values('" + oprName + "','" + oprNote + "',timezone('utc'::text, now()),'" + oprType + "'," + oprDuration + ",'1','" + oprPlmnFull + "'," + distance + ",'" + selectedOperator + "') returning id";
   int oprId = new Common().executeQueryAndReturnId(query);
   //new AuditHandler().audit_operation(Integer.toString(oprId), oprType);
   new AuditHandler().audit_operation(Integer.toString(oprId), "Create", cueId);
   dbDataService.addToEventPriorityQueue(oprName, eventName, note, oprType, operators, coverageDistance, "localhost", frequency, latitude, longitude, periodicity, timeout, validity, date, true, null, -1, -1.0, null, cueId);

   /*String detail="";
					   		if(eventName.equalsIgnoreCase("oxfam")){
								detail="Automatic Transmission started";
							}else{
								detail="Scheduled Transmission started";
							}*/

   /*HashMap<String,String> queLog = new HashMap<String,String>();
							queLog.put("event_name", eventName);
							queLog.put("detail", detail);
							queLog.put("date", new Date().toString());
							queLog.put("periodicity", Long.toString(periodicity));
							queLog.put("coverage", Integer.toString(coverageDistance));
				   			//new AuditHandler().auditLog(log, 3);
				   			new AuditHandler().audit_que(queLog);*/
  } catch (Exception e) {
   // TODO Auto-generated catch block
   fileLogger.error("Error method addOperation :desc start");
   rsOpr.put("result", "failure");
   rsOpr.put("message", "Exception");
   e.printStackTrace();

   fileLogger.error("Error method addOperation :desc end");
   return Response.status(201).entity(rsOpr).build();
  }
  rsOpr.put("result", "success");
  rsOpr.put("message", "Operation successfully created");
  fileLogger.info("Exit Function : addOperation"); 
  return Response.status(201).entity(rsOpr).build();
 }

 public Response startTriggerOrOperation(String eventName, String note, String oprType, String operators, int coverageDistance, String deviceIp, int frequency, double latitude, double longitude, long periodicity, long timeout, long validity, Date date) {
  fileLogger.info("Inside Function : startTriggerOrOperation");
  new CurrentOperation(null);
  Common co = new Common();
  fileLogger.debug("oprPlmnFull :" + operators);
  HashMap < String, String > rs = new HashMap < String, String > ();
  Operations operation = new Operations();
  JSONObject jsonObject = new JSONObject();
  fileLogger.debug("1dcommonservice");
  NetscanOperations netscanOperations = new NetscanOperations();
  JSONObject netscanData = new JSONObject();
  LinkedHashMap < String, String > param = new LinkedHashMap < String, String > ();
  int preScanMaxId = 0;
  LinkedHashMap < String, String > parameter = new LinkedHashMap < String, String > ();
  fileLogger.debug("1ccommonservice");
  long scanTime = Long.parseLong(co.getDbCredential().get("scantime"));
  ArrayList < String > givenPLMNs = new ArrayList < String > (Arrays.asList(operators.split("\\s*,\\s*")));
  fileLogger.debug("givenPLMNs :" + givenPLMNs);
  //String query = "insert into oprrationdata(name,note,inserttime,opr_type,gsm_bands,umts_bands,lte_bands,duration,status,t_stoptime,plmn,distance,use_last_scanned_data) values('"+oprName+"','"+oprNote+"',timezone('utc'::text, now()),'"+oprType+"','"+oprGsmBand+"','"+oprUmtsBand+"','"+oprLteBand+"',"+oprDuration+",'1',timezone('utc'::text, now())+Interval '"+oprDuration+" min','"+oprPlmnFull+"',"+distance+",'"+oldData+"')";	 
  String query = "";
  boolean status = co.executeDLOperation(query);
  try {
   if (status) {
    rs.put("result", "success");
    if (oprType.equals("1")) {
     new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Operation Created for Scheduler\"}");
    } else if (oprType.equals("2")) {
     new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Operation Created for Automate\"}");
    } else {
     new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Operation Created for External Trigger\"}");
    }
    query = "select id,inserttime from oprrationdata where id=(select max(id) from oprrationdata)";
    jsonObject = operation.getJson(query).getJSONObject(0);
    //HttpSession session=req.getSession(false);
    HttpSession session = null;
    session.setAttribute("startTime", jsonObject.getString("inserttime"));
   } else {
    co.executeDLOperation("update oprrationdata set status='2',stoptime=timezone('utc'::text, now()) where id=(select max(id) from oprrationdata)");
    new AutoOperationServer().sendText("{\"result\":\"fail\",\"msg\":\"Unable To Create Operation\"}");
    rs.put("result", "failure");
    return Response.status(201).entity(rs).build();
   }
  } catch (Exception e) {
   fileLogger.error("Exception occurs:" + e.getMessage());
  }

  if (oprType.equals("1")) {
   fileLogger.debug("Starting Scheduler Tracking");
   //boolean scannerDataAvailability=isScannerDataAvailable();
   /*if(scannerDataAvailability){
   	//goForTracking();
   }else{
   	//startScanning();
   }*/
   fileLogger.debug("Ending Scheduler Tracking");
  } else if (oprType.equals("2")) {
   fileLogger.debug("Starting Automate Tracking");
   //boolean scannerDataAvailability=isScannerDataAvailable();
   /*if(scannerDataAvailability){
   	//goForTracking();
   }else{
   	//startScanning();
   }*/

   fileLogger.debug("Ending Automate Tracking");
  } else {
   fileLogger.debug("Starting External Trigger Tracking");
   //boolean scannerDataAvailability=isScannerDataAvailable();
   /*if(scannerDataAvailability){
   	//goForTracking();
   }else{
   	//startScanning();
   }*/
   fileLogger.debug("Starting External Trigger Tracking");
  }
  fileLogger.info("Exit Function : startTriggerOrOperation");
  return null;
 }

 @POST
 @Path("/changepassword")
 @Produces(MediaType.APPLICATION_JSON)
 public Response changePassword(@Context HttpServletRequest request, @FormParam("type") String type, @FormParam("userId") String userId, @FormParam("userName") String userName, @FormParam("oldPassword") String oldPassword, @FormParam("changedPassword") String changedPassword, @FormParam("confirmPassword") String confirmPassword) {
   fileLogger.info("Inside Function : changePassword");
	 HashMap < String, String > rsOpr = new HashMap < String, String > ();
  try {
   String query = "";
   String passwordDB = "";
   String loggedUser = "";
   String changedUser = "";
   HttpSession session = request.getSession();
   loggedUser = session.getAttribute("username").toString();
   if (!type.equalsIgnoreCase("admin")) {
    userId = session.getAttribute("userid").toString();
    query = "select * from users where id=" + userId;
    Operations operations = new Operations();
    JSONObject userDataObj = operations.getJson(query).getJSONObject(0);
    passwordDB = userDataObj.getString("password");
    changedUser = loggedUser;
   } else {
    changedUser = userName;
   }
   fileLogger.debug("type:" + type + " passworddb:" + passwordDB + " oldPASSWORD:" + oldPassword);
   if (type.equalsIgnoreCase("admin") || (!type.equalsIgnoreCase("admin") && passwordDB != null && passwordDB.equalsIgnoreCase(oldPassword))) {
    query = "update users set password = '" + changedPassword + "' where id=" + userId;
    boolean status = new Common().executeDLOperation(query);
    if (status) {
     rsOpr.put("result", "success");
     rsOpr.put("message", "Password Changed Successfully");
     LinkedHashMap < String, String > log = new LinkedHashMap < String, String > ();
     log.put("action", "Change Password");
     log.put("logged user", loggedUser);
     log.put("password changed user", changedUser);
     new AuditHandler().auditUserManagement(log);
    } else {
     rsOpr.put("result", "failure");
     rsOpr.put("message", "Confirm Password mismatch");
    }
   } else {
    rsOpr.put("result", "failure");
    rsOpr.put("message", "Error");
   }
  } catch (Exception E) {
   E.printStackTrace();
   rsOpr.put("result", "failure");
   rsOpr.put("message", "Exception occurs");
  }
  fileLogger.info("Exit Function : changePassword");
  return Response.status(201).entity(rsOpr).build();
 }

 /* @POST
			   @Path("/FETCH_REPORT")
			   @Produces("application/zip")
			   public Response reciveFromDevice(@Context HttpServletRequest req,String data)
			   {   
				   
				   
				   String startTime = null,endTime = null,seprator = null;
				try {
					JSONObject revicedJsonData = new JSONObject(data);
					startTime = revicedJsonData.getString("START_TIME");
					endTime = revicedJsonData.getString("STOP_TIME");
					seprator = revicedJsonData.getInt("REPORT_TYPE") == 1 ? "," : "\t";
				} catch (Exception e) {
					// TODO: handle exception
				}
				   
				    
					String filePath = new ReportServer().createReport(startTime, endTime, seprator);
				   	File file = new File(filePath);
				   	String fileName = file.getName();
				   
			        ResponseBuilder responseBuilder = Response.ok((Object) file);
			        responseBuilder.header("Content-Disposition", "attachment; filename=\""+fileName+"\"");
			        return responseBuilder.build();
			   }*/

 @POST
 @Path("/getbackuphistory")
 @Produces(MediaType.APPLICATION_JSON)
 public Response getBackupHistory() {
  fileLogger.info("Inside Function : getBackupHistory");
  String absolutePath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
  absolutePath = absolutePath.substring(0, absolutePath.lastIndexOf("WEB-INF"));
  String path = absolutePath + "resources/";
  Common common = new Common();
  String backupDir = common.getDbCredential().get("backupdir");
  String completePath = path + backupDir;
  fileLogger.debug("complete path is :" + completePath);
  JSONArray fileArray = common.listFiles(completePath);
  JSONObject responseObj = new JSONObject();
  try {
   responseObj.put("dirName", backupDir);
   responseObj.put("fileNames", fileArray);

  } catch (JSONException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  }
  fileLogger.info("Exit Function : getBackupHistory");
  return Response.status(201).entity(responseObj.toString()).build();
 }

 @POST
 @Path("/deletebackuphistory")
 @Produces(MediaType.APPLICATION_JSON)
 public Response deleteBackupHistory(@Context HttpServletRequest req, @FormParam("dirName") String dirName, @FormParam("fileName") String fileName) {
  fileLogger.info("Inside Function : deleteBackupHistory");
  String absolutePath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
  absolutePath = absolutePath.substring(0, absolutePath.lastIndexOf("WEB-INF"));
  String path = absolutePath + "resources/";
  String completePath = path + dirName;
  fileName = completePath + "/" + fileName;
  fileLogger.debug("delete file is :" + fileName);
  File file = new File(fileName);
  JSONObject responseObject = new JSONObject();
  try {
   if (file.exists()) {
    file.delete();
    responseObject.put("result", "success");
   } else {
    responseObject.put("result", "failure");
    responseObject.put("message", "File does not exist");
   }
  } catch (JSONException e) {
   // TODO Auto-generated catch block
   fileLogger.error("Exception occurs in deleteBackupHistory with message:" + e.getMessage());
  }
  fileLogger.info("Exit Function : deleteBackupHistory");
  return Response.status(201).entity(responseObject.toString()).build();
 }


 public int getSectorFromAntennaId(int antennaId) {
  Operations operations = new Operations();
  int sector = -1;
  try {
   /*int sectorAntennaCount = operations.getJson("select count(*) count from antenna where atype='1' and inscanning is true").getJSONObject(0).getInt("count");
   if(sectorAntennaCount==0){
   	return 5;
   }*/
   String angleCovered = operations.getJson("select angle_covered from antenna where id=" + antennaId).getJSONObject(0).getString("angle_covered");
   if (angleCovered != null && angleCovered != "") {
    String[] angleCoverageArr = angleCovered.split("-");
    int minAngle = Integer.parseInt(angleCoverageArr[0]);
    int maxAngle = Integer.parseInt(angleCoverageArr[1]);
    int angleDiff = maxAngle - minAngle;
    //sector = sectorAntennaCount-(maxAngle/angleDiff);
    sector = maxAngle / angleDiff;
   } else {
    return 5;
   }
  } catch (JSONException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  }

  return sector;

 }



 @GET
 @Path("/bist")
 @Produces(MediaType.APPLICATION_JSON)
 public Response startBist() {
  Bist bb = Bist.getInstance();
  String data = bb.start().toString();
  bb = null;
  return Response.status(201).entity(data).build();
 }

 //@Produces(MediaType.APPLICATION_JSON) 

 @GET
 @Path("/manualstatus")
 @Produces(MediaType.TEXT_PLAIN)
 public Response getManualStatus() {
  String manualOverrideStatus = "";
  try {
   manualOverrideStatus = new Operations().getJson("select status from manual_override").getJSONObject(0).getString("status");
  } catch (JSONException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  }
  return Response.status(201).entity(manualOverrideStatus).build();
 }

 @GET
 @Path("/updatemanualstatus")
 @Produces(MediaType.TEXT_PLAIN)
 public Response updateManualStatus(@QueryParam("manualCheck") boolean manualCheck) {
  fileLogger.info("Inside Function : updateManualStatus");
  DBDataService dbDataService = DBDataService.getInstance();
  boolean manualOverrideStatus = false;
  try {
   if (manualCheck) {
    dbDataService.clearProrityQueue(); //clear priority queue
    String currentEventName = dbDataService.currentEventName;
    if (currentEventName != null) {
     dbDataService.stopRunningOperation();
     boolean updateStatus = new Common().executeDLOperation("update oprrationdata set status='0',stoptime=timezone('utc'::text, now()) where id=(select max(id) from oprrationdata)");
     if (updateStatus) {
      //new AuditHandler().audit_operation(Integer.toString(oprId),"Create"); 
      fileLogger.debug("@updated non manual status on manual override");
      //return Response.status(201).entity("{\"result\":\"success\",\"msg\":\"Operation Stopped Successfully\"}").build();   
     }
    }
   } else {
    int manualOperationStatus = new Operations().getJson("select status from config_status").getJSONObject(0).getInt("status");
    if (manualOperationStatus == 1) {
     dbDataService.stopRunningManualOperation();
     boolean updateStatus = new Common().executeDLOperation("update config_status set status=0");
     dbDataService.setCurrentManualEventName(null);
     if (updateStatus) {
      //new AuditHandler().audit_operation(Integer.toString(oprId),"Create"); 
      fileLogger.debug("@updated manual status on manual override");
      //return Response.status(201).entity("{\"result\":\"success\",\"msg\":\"Operation Stopped Successfully\"}").build();   
     }

    }
    //dbDataService.releaseWait();
   }
   int updateStatus = new Common().executeQueryAndReturnId("update manual_override set status=" + manualCheck + "");
   manualOverrideStatus = true;
  } catch (Exception e) {
   manualOverrideStatus = false;
   // TODO Auto-generated catch block
   e.printStackTrace();
  }
  fileLogger.info("Exit Function : updateManualStatus");
  return Response.status(201).entity(Boolean.toString(manualOverrideStatus)).build();
 }

 @GET
 @Path("/factoryresetconfig")
 @Produces(MediaType.APPLICATION_JSON)
 public Response factoryResetConfig() {
  fileLogger.info("Inside Function : factoryResetConfig");
  DBDataService dbDataService = DBDataService.getInstance();
  Operations operations = new Operations();
  ApiCommon apiCommon = new ApiCommon();
  HashMap < String, ArrayList < HashMap < String, String >>> devicesMapOverTech = operations.getAllBtsInfoByTech();
  //2g lock
  JSONObject responseObject = new JSONObject();
  try {

   dbDataService.clearProrityQueue(); //clear priority queue
   String currentEventName = dbDataService.currentEventName;
   if (currentEventName != null) {
    dbDataService.stopRunningOperation();
    boolean updateStatus = new Common().executeDLOperation("update oprrationdata set status='0',stoptime=timezone('utc'::text, now()) where id=(select max(id) from oprrationdata)");
    if (updateStatus) {
     //new AuditHandler().audit_operation(Integer.toString(oprId),"Create"); 
     fileLogger.debug("@updated non manual status on manual override");
     //return Response.status(201).entity("{\"result\":\"success\",\"msg\":\"Operation Stopped Successfully\"}").build();   
    }
   }
   int manualOperationStatus = new Operations().getJson("select status from config_status").getJSONObject(0).getInt("status");
   if (manualOperationStatus == 1) {
    dbDataService.stopRunningManualOperation();
    boolean updateStatus = new Common().executeDLOperation("update config_status set status=0");
    dbDataService.setCurrentManualEventName(null);
    if (updateStatus) {
     //new AuditHandler().audit_operation(Integer.toString(oprId),"Create"); 
     fileLogger.debug("@updated manual status on manual override");
     //return Response.status(201).entity("{\"result\":\"success\",\"msg\":\"Operation Stopped Successfully\"}").build();   
    }

   }
   //dbDataService.releaseWait();
   int updateStatus = new Common().executeQueryAndReturnId("update manual_override set status=false");
   //manualOverrideStatus=true;



   Common common = new Common();
   String query = "delete from antenna";
   common.executeDLOperation(query); //delete antenna
   query = "delete from target_list";
   common.executeDLOperation(query); //delete target list
   query = "delete from system_properties";
   common.executeDLOperation(query); //delete system properties
   query = "insert into antenna SELECT id, profile_name, txpower, atype, band, gain, elevation, hbw, vbw, tilt, azimuth, terrain, ptz_sel_status, angle_covered, angle_offset, intracking, inscanning FROM antenna_saved_config where saved_profile_id=0";
   common.executeDLOperation(query); //insert factory configuration of antenna
   query = "insert into system_properties SELECT id, key, display_name, value, status, type,code FROM system_properties_saved_config where saved_profile_id=0";
   common.executeDLOperation(query); //insert factory configuration of system properties
   //apiCommon.lockUnlockAllDevicesPerTech(devicesMapOverTech,1,"2G");
   //3g lock
   //apiCommon.lockUnlockAllDevicesPerTech(devicesMapOverTech,1,"3G");
   //3g default config
   String defaultSufiConfig = apiCommon.getSufiConfigurationWithDefaultValues();
   String configOf = apiCommon.getSufiConfigurationWithDefaultValues(defaultSufiConfig, 1);
   HashMap < String, String > device3g = devicesMapOverTech.get("3G").get(0);
   common.sendDefaultConfigurationToNode(device3g); //set 3g default config
   //sector antenna for scanning and tracking
   common.executeDLOperation("update antenna set inscanning=true,intracking=true where atype='1'");
   common.executeDLOperation("update antenna set inscanning=false,intracking=false where atype!='1'");


   //audit log for factory reset configuration
   LinkedHashMap < String, String > log = new LinkedHashMap < String, String > ();
   log.put("action", "Factory Reset Configuration");

   query = "select profile_name,inscanning,intracking,angle_offset from antenna_saved_config where saved_profile_id=0";
   JSONArray antennaList = operations.getJson(query);
   StringBuilder antennaStr = new StringBuilder();
   String angleOffset = "";
   for (int i = 0; i < antennaList.length(); i++) {
    JSONObject tempJson = antennaList.getJSONObject(i);
    antennaStr.append("(name-" + tempJson.getString("profile_name") + ",scanning-" + tempJson.getString("inscanning") + ",tracking-" + tempJson.getString("intracking") + ")");
    if (tempJson.getString("atype").equals("1")) {
     angleOffset = Integer.toString(tempJson.getInt("angle_offset"));
    }
   }
   antennaStr.append(",Angle Offset " + angleOffset);
   log.put("Antenna Configuration", antennaStr.toString());

   query = "select name,imsi,imei,type from target_list_saved_config where saved_profile_id=0";
   JSONArray targetList = operations.getJson(query);
   StringBuilder targetStr = new StringBuilder();
   for (int i = 0; i < targetList.length(); i++) {
    JSONObject tempJson = targetList.getJSONObject(i);
    targetStr.append("(name-" + tempJson.getString("name") + ",imsi-" + tempJson.getString("imsi") + ",imei-" + tempJson.getString("imei") + ",type-" + tempJson.getString("type") + ")");
   }
   log.put("Target Configuration", targetStr.toString());

   query = "select display_name,value from system_properties_saved_config where saved_profile_id=0 and status is true";
   JSONArray sysConfigList = operations.getJson(query);

   StringBuilder sysConfigStr = new StringBuilder();
   for (int i = 0; i < sysConfigList.length(); i++) {
    JSONObject tempJson = sysConfigList.getJSONObject(i);
    sysConfigStr.append("(name-" + tempJson.getString("display_name") + ",value-" + tempJson.getString("value") + ")");
   }
   log.put("System Configuration", sysConfigStr.toString());

   new AuditHandler().auditProfileAction(log);
   //audit log for factory reset configuration

   responseObject.put("result", "success");
  } catch (JSONException e) {
   try {
    responseObject.put("result", "success");
    responseObject.put("message", "Exception occurs");
    e.printStackTrace();
   } catch (JSONException e1) {
    // TODO Auto-generated catch block
    e1.printStackTrace();
   }
  } catch (Exception e) {
   try {
    responseObject.put("result", "success");
    responseObject.put("message", "Exception occurs");
    e.printStackTrace();
   } catch (JSONException e1) {
    // TODO Auto-generated catch block
    e1.printStackTrace();
   }
  }

  //switch default
  fileLogger.info("Exit Function : factoryResetConfig");
  return Response.status(201).entity(responseObject.toString()).build();
 }

 @GET
 @Path("/applicationstarttime")
 @Produces(MediaType.TEXT_PLAIN)
 public String getApplicationStartTime() {
  String res = SoftVerListener.applicationStartTime;
  return res;
 }

 @GET
 @Path("/getcurrentconfig")
 @Produces(MediaType.APPLICATION_JSON)
 public Response getCurrentConfig(@Context HttpServletRequest req) {
  fileLogger.info("Inside Function : getCurrentConfig");
  //fileLogger.debug("logger is :"+logger);
  //fileLogger.debug("in getAllCurrentConfig");
  JSONObject responseObj = new JSONObject();

  try {
   String query = "select * from antenna a join antenna_type at on a.atype::integer=at.type_id order by a.profile_name";
   JSONArray js = new Operations().getJson(query);
   responseObj.put("antenna", js);
   query = "select * from target_list order by id";
   js = new Operations().getJson(query);
   responseObj.put("target_list", js);
   query = "select * from system_properties where status=true order by id";
   js = new Operations().getJson(query);
   responseObj.put("system_properties", js);
  } catch (JSONException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  }
  fileLogger.info("Exit Function : getCurrentConfig");
 // fileLogger.debug("out getAllCurrentConfig");
  return Response.status(201).entity(responseObj.toString()).build();
 }

 @GET
 @Path("/getprofileconfig")
 @Produces(MediaType.APPLICATION_JSON)
 public Response getProfileConfig(@Context HttpServletRequest req, @QueryParam("profileId") int profileId) {
  //fileLogger.debug("logger is :"+logger);
  //fileLogger.debug("in getProfileConfig");
  fileLogger.info("Inside Function : getProfileConfig");
  JSONObject responseObj = new JSONObject();

  try {
   String query = "select * from antenna_saved_config a join antenna_type at on a.atype::integer=at.type_id where saved_profile_id=" + profileId + " order by a.profile_name";
   JSONArray js = new Operations().getJson(query);
   responseObj.put("antenna", js);
   query = "select * from target_list_saved_config where saved_profile_id=" + profileId + " order by id";
   js = new Operations().getJson(query);
   responseObj.put("target_list", js);
   query = "select * from system_properties_saved_config where saved_profile_id=" + profileId + " and status=true order by id";
   js = new Operations().getJson(query);
   responseObj.put("system_properties", js);
  } catch (JSONException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  }
  //fileLogger.debug("out getProfileConfig");
  fileLogger.info("Exit Function : getProfileConfig");
  return Response.status(201).entity(responseObj.toString()).build();
 }

 @GET
 @Path("/getconfigprofiles")
 @Produces(MediaType.APPLICATION_JSON)
 public Response getConfigProfiles(@Context HttpServletRequest req) {
  //fileLogger.debug("logger is :"+logger);
  //fileLogger.debug("in getConfigProfiles");
  JSONObject responseObj = new JSONObject();
  String query = "select * from config_profiles order by prof_name";
  JSONArray responseArr = new Operations().getJson(query);
  //fileLogger.debug("out getConfigProfiles");
  return Response.status(201).entity(responseArr.toString()).build();
 }

 @POST
 @Path("/saveprofile")
 @Produces(MediaType.APPLICATION_JSON)
 public Response saveProfile(@Context HttpServletRequest req, @FormParam("profileName") String profileName) {
  //fileLogger.debug("logger is :"+logger);
	 fileLogger.info("Inside Function : saveProfile");
  fileLogger.debug("in saveProfile with profname :" + profileName);
  HashMap < String, String > resultMap = new HashMap < String, String > ();
  try {
   Common common = new Common();
   int duplicateNameCount = new Operations().getJson("select count(*) count from config_profiles where lower(prof_name)='" + profileName.toLowerCase() + "'").getJSONObject(0).getInt("count");
   if (duplicateNameCount != 0) {
    resultMap.put("result", "failure");
    resultMap.put("message", "Profile name already exists");
   } else {
    String query = "insert into config_profiles(prof_name) values('" + profileName + "') returning id";
    int profileId = common.executeQueryAndReturnId(query);
    query = "INSERT INTO antenna_saved_config(id, profile_name, txpower, atype, band, gain, elevation, hbw, vbw, tilt, azimuth, terrain, ptz_sel_status,angle_covered, angle_offset, intracking, inscanning, saved_profile_id) select *," + profileId + " from antenna";
    common.executeDLOperation(query);
    query = "INSERT INTO target_list_saved_config(id, imsi, imei, istarget, name, type, saved_profile_id) select *," + profileId + " from target_list";
    common.executeDLOperation(query);
    query = "INSERT INTO system_properties_saved_config(id, key, display_name, value, status, type,code, saved_profile_id) select *," + profileId + " from system_properties";
    common.executeDLOperation(query);

    //audit log for save profile
    LinkedHashMap < String, String > log = new LinkedHashMap < String, String > ();
    log.put("action", "Save Profile");
    log.put("name", profileName);
    Operations operations = new Operations();

    query = "select profile_name,inscanning,intracking,angle_offset,atype from antenna_saved_config where saved_profile_id=" + profileId;
    JSONArray antennaList = operations.getJson(query);
    StringBuilder antennaStr = new StringBuilder();
    String angleOffset = "";
    for (int i = 0; i < antennaList.length(); i++) {
     JSONObject tempJson = antennaList.getJSONObject(i);
     antennaStr.append("(name-" + tempJson.getString("profile_name") + ",scanning-" + tempJson.getString("inscanning") + ",tracking-" + tempJson.getString("intracking") + ")");
     if (tempJson.getString("atype").equals("1")) {
      angleOffset = Integer.toString(tempJson.getInt("angle_offset"));
     }
    }
    antennaStr.append(",Angle Offset " + angleOffset);
    log.put("Antenna Configuration", antennaStr.toString());

    query = "select name,imsi,imei,type from target_list_saved_config where saved_profile_id=" + profileId;
    JSONArray targetList = operations.getJson(query);
    StringBuilder targetStr = new StringBuilder();
    for (int i = 0; i < targetList.length(); i++) {
     JSONObject tempJson = targetList.getJSONObject(i);
     targetStr.append("(name-" + tempJson.getString("name") + ",imsi-" + tempJson.getString("imsi") + ",imei-" + tempJson.getString("imei") + ",type-" + tempJson.getString("type") + ")");
    }
    log.put("Target Configuration", targetStr.toString());

    query = "select display_name,value from system_properties_saved_config where saved_profile_id=" + profileId + " and status is true";
    JSONArray sysConfigList = operations.getJson(query);

    StringBuilder sysConfigStr = new StringBuilder();
    for (int i = 0; i < sysConfigList.length(); i++) {
     JSONObject tempJson = sysConfigList.getJSONObject(i);
     sysConfigStr.append("(name-" + tempJson.getString("display_name") + ",value-" + tempJson.getString("value") + ")");
    }
    log.put("System Configuration", sysConfigStr.toString());

    new AuditHandler().auditProfileAction(log);
    //audit log for save profile

    resultMap.put("result", "success");
    resultMap.put("id", Integer.toString(profileId));
   }
  } catch (Exception e) {
   resultMap.put("result", "failure");
   resultMap.put("message", "Exception occurs");
   // TODO Auto-generated catch block
   e.printStackTrace();
  }fileLogger.info("Exit Function : saveProfile");
  //fileLogger.debug("out saveProfile");
  return Response.status(201).entity(resultMap).build();
 }

 @POST
 @Path("/deleteprofile")
 @Produces(MediaType.APPLICATION_JSON)
 public Response deleteProfile(@Context HttpServletRequest req, @FormParam("profileId") int profileId, @FormParam("profileName") String profileName) {
  //fileLogger.debug("logger is :"+logger);
  //fileLogger.debug("in deleteProfile");
  fileLogger.info("Inside Function : deleteProfile");
  HashMap < String, String > resultMap = new HashMap < String, String > ();
  try {
   Common common = new Common();
   String query = "delete from antenna_saved_config where saved_profile_id=" + profileId;
   common.executeDLOperation(query);
   query = "delete from target_list_saved_config where saved_profile_id=" + profileId;
   common.executeDLOperation(query);
   query = "delete from system_properties_saved_config where saved_profile_id=" + profileId;
   common.executeDLOperation(query);
   query = "delete from config_profiles where id=" + profileId;

   //audit log for delete profile
   LinkedHashMap < String, String > log = new LinkedHashMap < String, String > ();
   log.put("action", "Delete Profile");
   log.put("name", profileName);
   new AuditHandler().auditProfileAction(log);
   //audit log for delete profile

   common.executeDLOperation(query);
   resultMap.put("result", "success");
  } catch (Exception e) {
   resultMap.put("result", "failure");
   resultMap.put("message", "Exception occurs");
   // TODO Auto-generated catch block
   e.printStackTrace();
  }
  fileLogger.info("Exit Function : deleteProfile");
  //fileLogger.debug("out deleteProfile");
  return Response.status(201).entity(resultMap).build();
 }

 @POST
 @Path("/applyprofile")
 @Produces(MediaType.APPLICATION_JSON)
 public Response applyProfile(@Context HttpServletRequest req, @FormParam("profileId") int profileId, @FormParam("profileName") String profileName) {
  //fileLogger.debug("logger is :"+logger);
  fileLogger.info("Inside Function : applyProfile");
  fileLogger.debug("in applyProfile with profileId :" + profileId);
  HashMap < String, String > resultMap = new HashMap < String, String > ();
  try {
   Common common = new Common();
   String query = "delete from antenna";
   common.executeDLOperation(query);
   query = "delete from target_list";
   common.executeDLOperation(query);
   query = "delete from system_properties";
   common.executeDLOperation(query);
   query = "insert into antenna SELECT id, profile_name, txpower, atype, band, gain, elevation, hbw, vbw, tilt, azimuth, terrain, ptz_sel_status, angle_covered, angle_offset, intracking, inscanning FROM antenna_saved_config where saved_profile_id=" + profileId;
   common.executeDLOperation(query);
   query = "insert into target_list SELECT id, imsi, imei, istarget, name, type FROM target_list_saved_config where saved_profile_id=" + profileId;
   common.executeDLOperation(query);
   query = "insert into system_properties SELECT id, key, display_name, value, status, type,code FROM system_properties_saved_config where saved_profile_id=" + profileId;
   common.executeDLOperation(query);

   //audit log for apply profile
   LinkedHashMap < String, String > log = new LinkedHashMap < String, String > ();
   log.put("action", "Apply Profile");
   log.put("name", profileName);
   Operations operations = new Operations();

   query = "select profile_name,inscanning,intracking,angle_offset,atype from antenna_saved_config where saved_profile_id=" + profileId;
   JSONArray antennaList = operations.getJson(query);
   StringBuilder antennaStr = new StringBuilder();
   String angleOffset = "";
   for (int i = 0; i < antennaList.length(); i++) {
    JSONObject tempJson = antennaList.getJSONObject(i);
    antennaStr.append("(name-" + tempJson.getString("profile_name") + ",scanning-" + tempJson.getString("inscanning") + ",tracking-" + tempJson.getString("intracking") + ")");
    if (tempJson.getString("atype").equals("1")) {
     angleOffset = Integer.toString(tempJson.getInt("angle_offset"));
    }
   }
   antennaStr.append(",Angle Offset " + angleOffset);
   log.put("Antenna Configuration", antennaStr.toString());

   query = "select name,imsi,imei,type from target_list_saved_config where saved_profile_id=" + profileId;
   JSONArray targetList = operations.getJson(query);
   StringBuilder targetStr = new StringBuilder();
   for (int i = 0; i < targetList.length(); i++) {
    JSONObject tempJson = targetList.getJSONObject(i);
    targetStr.append("(name-" + tempJson.getString("name") + ",imsi-" + tempJson.getString("imsi") + ",imei-" + tempJson.getString("imei") + ",type-" + tempJson.getString("type") + ")");
   }
   log.put("Target Configuration", targetStr.toString());

   query = "select display_name,value from system_properties_saved_config where saved_profile_id=" + profileId + " and status is true";
   JSONArray sysConfigList = operations.getJson(query);

   StringBuilder sysConfigStr = new StringBuilder();
   for (int i = 0; i < sysConfigList.length(); i++) {
    JSONObject tempJson = sysConfigList.getJSONObject(i);
    sysConfigStr.append("(name-" + tempJson.getString("display_name") + ",value-" + tempJson.getString("value") + ")");
   }
   log.put("System Configuration", sysConfigStr.toString());

   new AuditHandler().auditProfileAction(log);
   //audit log for apply profile
   resultMap.put("result", "success");
  } catch (Exception e) {
   resultMap.put("result", "failure");
   resultMap.put("message", "Exception occurs");
   // TODO Auto-generated catch block
   e.printStackTrace();
  }
  fileLogger.info("Exit Function : applyProfile");
  //fileLogger.debug("out saveProfile");
  return Response.status(201).entity(resultMap).build();
 }

 @GET
 @Path("/getscanschedulerstatus")
 @Produces(MediaType.APPLICATION_JSON)
 public Response getScanSchedulerStatus(@Context HttpServletRequest req) {
  //fileLogger.debug("logger is :"+logger);
 // fileLogger.debug("in getScanSchedulerStatus");
	  fileLogger.info("Inside Function : getScanSchedulerStatus");
  HashMap < String, String > rs = new HashMap < String, String > ();
  String query = "select count(*) from view_btsinfo where code=3";
  JSONArray scannerNodeArr = new Operations().getJson(query);
  if (scannerNodeArr.length() != 0) {
   rs.put("result", "success");
   if (NetscanSchedulerListener.scheduler != null) {
    rs.put("message", "started");
   } else {
    rs.put("message", "stopped");
   }
  } else {
   rs.put("result", "fail");
  }
  fileLogger.info("Exit Function : getScanSchedulerStatus");
 // fileLogger.debug("out getScanSchedulerStatus");
  return Response.status(201).entity(rs).build();
 }

 @POST
 @Path("/getsessionparams")
 @Produces(MediaType.APPLICATION_JSON)
 public Response getSessionParams(@Context HttpServletRequest req) {
	  fileLogger.info("Inside Function : getSessionParams");
  //fileLogger.debug("logger is :"+logger);
  //fileLogger.debug("in getSessionParams");
  HashMap < String, String > rs = new HashMap < String, String > ();
  HttpSession session = req.getSession(false);
  rs.put("userName", session.getAttribute("userName").toString());
  rs.put("role", session.getAttribute("role").toString());
  rs.put("username", session.getAttribute("username").toString());
  fileLogger.info("Exit Function : getSessionParams");
  //fileLogger.debug("out getSessionParams");
  return Response.status(201).entity(rs).build();
 }

 @POST
 @Path("/getsystemtype")
 @Produces(MediaType.APPLICATION_JSON)
 public Response getSystemType(@Context HttpServletRequest req) {
  //fileLogger.debug("logger is :"+logger);
  //fileLogger.debug("in getSystemType")
  fileLogger.info("Inside Function : getSystemType");
  JSONObject responseObj = new JSONObject();
  String query = "select code,value from system_properties where key='system_type'";
  JSONArray responseArr = new Operations().getJson(query);
 // fileLogger.debug("out getSystemType");
  fileLogger.info("Exit Function : getSystemType");
  return Response.status(201).entity(responseArr.toString()).build();
 }

 @POST
 @Path("/getsystemmode")
 @Produces(MediaType.APPLICATION_JSON)
 public Response getSystemMode(@Context HttpServletRequest req) {
  //fileLogger.debug("logger is :"+logger);
	 fileLogger.info("Inside Function : getSystemMode");
  //fileLogger.debug("in getSystemMode");
  JSONObject responseObj = new JSONObject();
  String query = "select code from system_properties where key='system_mode'";
  JSONArray responseArr = new Operations().getJson(query);
 // fileLogger.debug("out getSystemMode");
	 fileLogger.info("Exit Function : getSystemMode");
  return Response.status(201).entity(responseArr.toString()).build();
 }
 
 @POST
 @Path("/getthresholdusedspace")
 @Produces(MediaType.APPLICATION_JSON)
 public Response getThresholdUsedSpace(@Context HttpServletRequest req) {
  //fileLogger.debug("logger is :"+logger);
	 fileLogger.info("Inside Function : getThresholdUsedSpace");
  //fileLogger.debug("in getthresholdusedspace");
  JSONObject responseObj = new JSONObject();
  try{
	  responseObj.put("usedspacelimit",new Common().getColumnValues("db", "usedspacelimit"));
  }catch(JSONException e){
	  fileLogger.error("exception in getthresholdusedspace with message :" + e.getMessage());
  }
  fileLogger.info("Exit Function : getThresholdUsedSpace");
 // fileLogger.debug("out getthresholdusedspace");
  return Response.status(201).entity(responseObj.toString()).build();
 }

 @POST
 @Path("/getoctasicpowerstatus")
 @Produces(MediaType.APPLICATION_JSON)
 public Response getOctasicPowerStatus(@Context HttpServletRequest req) {
  //fileLogger.debug("logger is :"+logger);
  //fileLogger.debug("in getOctasicPowerStatus");
	 fileLogger.info("Inside Function : getOctasicPowerStatus");
  JSONObject responseObj = new JSONObject();
  boolean isOctasicPowerOn = DBDataService.isOctasicPowerOn();
  fileLogger.debug("isOctasicPowerOn is :" + isOctasicPowerOn);
  try {
   responseObj.put("octasicPowerStatus", isOctasicPowerOn);
  } catch (JSONException e) {
   fileLogger.error("exception in getoctasicpowerstatus with message :" + e.getMessage());
   // TODO Auto-generated catch block
   e.printStackTrace();
  }
 //fileLogger.debug("out getOctasicPowerStatus");
  fileLogger.info("Exit Function : getOctasicPowerStatus");
  return Response.status(201).entity(responseObj.toString()).build();
 }
 
 @POST
 @Path("/add4gDevice")
 @Produces(MediaType.APPLICATION_JSON)
 public Response add4gDevice(@Context HttpServletRequest req, @FormParam("groupName") String groupName,  @FormParam("SystemManagerIP") String SystemManagerIP, @FormParam("useTypeId") String useTypeId, @FormParam("ofIp") String ofIp, @FormParam("ppfIp") String ppfIp, @FormParam("spfIp") String spfIp, @FormParam("antennaProfileIdOf") String antennaProfileIdOf, @FormParam("antennaProfileIdPpf") String antennaProfileIdPpf, @FormParam("antennaProfileIdSpf") String antennaProfileIdSpf, @FormParam("hwCapabilityTypeIdOf") String hwCapabilityTypeIdOf, @FormParam("hwCapabilityTypeIdPpf") String hwCapabilityTypeIdPpf, @FormParam("hwCapabilityTypeIdSpf") String hwCapabilityTypeIdSpf, @FormParam("paGain") String paGain, @FormParam("paPower") String paPower) {
  ApiCommon apiCommon = new ApiCommon();
  String defaultSufiConfig = apiCommon.getSufiConfigurationWithDefaultValues("4g");
  int deviceTypeIdOf = getDeviceTypeId(useTypeId, "E SuFi OF");
  String configOf = apiCommon.getESufiConfigurationWithDefaultValues(defaultSufiConfig, 1);
  int deviceTypeIdPpf = getDeviceTypeId(useTypeId, "E SuFi PPF");
  String configPpf = apiCommon.getESufiConfigurationWithDefaultValues(defaultSufiConfig, 2);
  int deviceTypeIdSpf = getDeviceTypeId(useTypeId, "E SuFi SPF");
  String configSpf = apiCommon.getESufiConfigurationWithDefaultValues(defaultSufiConfig, 3);
  int groupId = createGroup(groupName);
  int networkId = getNetworkId("4G");
  Common co = new Common();
  /*HashMap<String,String> hm=new HashMap<String,String>();
  hm.put("SuFi SPF","4");
  DID=hm.get("SUFI SPF");*/
  HashMap < String, String > rs = new HashMap < String, String > ();
  if (groupId != 0) {
   String query1 = "INSERT INTO btsmaster(ip,devicetypeid,grp_id,typeid,config,hw_capability_id,antenna_id,config_applied_status,pa_gain,pa_power,systemmanager) VALUES ('" + ofIp + "'," + deviceTypeIdOf + "," + groupId + "," + networkId + ",'" + configOf + "'," + hwCapabilityTypeIdOf + "," + antennaProfileIdOf + ",'n'," + paGain + "," + paPower +  " , '"+SystemManagerIP + "' );";
   String query2 = "INSERT INTO btsmaster(ip,devicetypeid,grp_id,typeid,config,hw_capability_id,antenna_id,config_applied_status,systemmanager) VALUES ('" + ppfIp + "'," + deviceTypeIdPpf + "," + groupId + "," + networkId + ",'" + configPpf + "'," + hwCapabilityTypeIdPpf + "," + antennaProfileIdPpf + ",'n' , '"+SystemManagerIP + "' );";
   String query3 = "INSERT INTO btsmaster(ip,devicetypeid,grp_id,typeid,config,hw_capability_id,antenna_id,config_applied_status,systemmanager) VALUES ('" + spfIp + "'," + deviceTypeIdSpf + "," + groupId + "," + networkId + ",'" + configSpf + "'," + hwCapabilityTypeIdSpf + "," + antennaProfileIdSpf + ",'n' , '"+SystemManagerIP + "' );";
   co.executeDLOperation(query1);
   co.executeDLOperation(query2);
   co.executeDLOperation(query3);

   new TwogOperations().updateStatusOfBts("'" + ofIp + "','" + ppfIp + "','" + spfIp + "'");
   rs.put("result", "success");
  } else {
   rs.put("result", "fail");
   rs.put("message", "Group already present");
  }

  return Response.status(201).entity(rs).build();
 }
 
 @GET
 @Path("/getsectorantennastatusforscanning")
 @Produces(MediaType.APPLICATION_JSON)
 public Response getSectorAntennaStatusForScanning() { //String query = "select  btsmaster.b_id,btsmaster.ip,btsmaster.status,btsnetworktype.name from btsmaster inner join btsnetworktype on(typeid = n_id)";
	 fileLogger.info("Inside Function : getSectorAntennaStatusForScanning");
	 String query = "select count(*) as count from antenna where atype='1' and inscanning is true";
  fileLogger.debug(query);
  JSONArray rs = new Operations().getJson(query);
  int count=0;
  try{
	  count=rs.getJSONObject(0).getInt("count");
  }catch(Exception e){
	  fileLogger.error("Exception occurs in getSectorAntennaStatusForScanning with message :"+e.getMessage());
  }
  HashMap<String,Boolean> resultMap = new HashMap<String,Boolean>();
  if(count==0){
	  resultMap.put("secAntStatusForScan",false);
  }else{
	  resultMap.put("secAntStatusForScan",true);
  }
  fileLogger.info("Exit Function : getSectorAntennaStatusForScanning");
  return Response.status(201).entity(resultMap).build();
 }
 
 
 @POST
 @Path("/stopcueoperation")
 @Produces(MediaType.APPLICATION_JSON)
 public Response stopCueOperation(@Context HttpServletRequest req) {
  return stopCueOperation();
 }

 public Response stopCueOperation() {
	 fileLogger.info("Inside Function : stopCueOperation");
	// fileLogger.debug("in stopCueOperationFalcon");
	 DBDataService dbDataService = DBDataService.getInstance();
	 String currentEventName = dbDataService.getCurrentEventName();
	 EventData eventData = dbDataService.getCurrentEventData();
	 try {
		 JSONArray hummerDataArray = new Operations().getJson("select * from view_btsinfo where code = 12");
			if(hummerDataArray.length()>0){
				new TRGLController().sendTrackingStatusToHummer("no");
			}
		 fileLogger.debug("dbDataService.getCurrentEventData().getOprName()");
		 if(currentEventName == null) {
			 //boolean updateStatus = new Common().executeDLOperation("update oprrationdata set status='0' where id=(select max(id) from oprrationdata)");
			 //if (updateStatus) {
			 //new AuditHandler().audit_operation(Integer.toString(oprId),"Create"); 
			 //currentEventName=currentEventName.equals("ugs")?"Oxfam":"Hummer";
			 currentEventName="Oxfam";
			 LinkedHashMap < String, String > oprStopLog = new LinkedHashMap < String, String > ();
			 oprStopLog.put("oprType", currentEventName);
			 oprStopLog.put("action", "Stop");
			 new AuditHandler().auditOprStop(oprStopLog);
			 //dbDataService.storeTriggerOnDb(eventData.getDate(),eventData.getCueId(),currentEventName,currentEventName+" Cue operation manually stopped","stop",0);
			 dbDataService.storeTriggerOnDb(eventData.getDate(),eventData.getCueId(),currentEventName,currentEventName+" Cue operation manually stopped");
		     new TriggerCueServer().sendText("event");
			 return Response.status(201).entity("{\"result\":\"success\",\"msg\":\"Operation Stopped Successfully\"}").build();
			 //}
		 } else if(currentEventName.equals("ugs") || currentEventName.equals("trgl")) {
			 currentEventName=currentEventName.equals("ugs")?"Oxfam":"Hummer";
			 dbDataService.stopCueOperation();
			 //boolean updateStatus = new Common().executeDLOperation("update oprrationdata set status='0',stoptime=timezone('utc'::text, now()) where id=(select max(id) from oprrationdata)");
			 //if (updateStatus) {
			 LinkedHashMap < String, String > oprStopLog = new LinkedHashMap < String, String > ();
			 oprStopLog.put("oprType", currentEventName);
			 oprStopLog.put("action", "Stop");
			 new AuditHandler().auditOprStop(oprStopLog);
			 //new AuditHandler().audit_operation(Integer.toString(oprId),"Create");
			 //dbDataService.storeTriggerOnDb(eventData.getDate(),eventData.getCueId(),currentEventName,currentEventName+" Cue operation manually stopped","stop",0);
			 dbDataService.storeTriggerOnDb(eventData.getDate(),eventData.getCueId(),currentEventName,currentEventName+" Cue operation manually stopped");
		     new TriggerCueServer().sendText("event");
			 return Response.status(201).entity("{\"result\":\"success\",\"msg\":\"Operation Stopped Successfully\"}").build();
			 //}
			 //addToEventPriorityQueue(eventObj.getString("name"),"scheduler",eventObj.getString("note"),eventObj.getString("note"),operators,eventObj.getString("distance"),"localhost",-1,"-1","-1","-1",-1,-1,eventObj.getString("inserttime"),true);
		 }else{
			 //boolean updateStatus = new Common().executeDLOperation("update oprrationdata set status='0' where id=(select max(id) from oprrationdata)");
			 //if (updateStatus) {
				 //new AuditHandler().audit_operation(Integer.toString(oprId),"Create");
			 currentEventName=currentEventName.equals("ugs")?"Oxfam":"Hummer";
			 LinkedHashMap < String, String > oprStopLog = new LinkedHashMap < String, String > ();
			 oprStopLog.put("oprType", currentEventName);
			 oprStopLog.put("action", "Stop");
			 new AuditHandler().auditOprStop(oprStopLog);
			 //dbDataService.storeTriggerOnDb(eventData.getDate(),eventData.getCueId(),currentEventName,currentEventName+" Cue operation manually stopped","stop",0);
			 dbDataService.storeTriggerOnDb(eventData.getDate(),eventData.getCueId(),currentEventName,currentEventName+" Cue operation manually stopped");
		     new TriggerCueServer().sendText("event");
		     return Response.status(201).entity("{\"result\":\"success\",\"msg\":\"Operation Stopped Successfully\"}").build();
			 //}
		 }
	 } catch (UnknownHostException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  } catch (IOException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  }
	 fileLogger.info("Exit Function : stopCueOperation");
  return Response.status(201).entity("{\"result\":\"failure\",\"msg\":\"Problem in Stopping Operation\"}").build();
 
 }
 
 @GET
 @Path("/getmaninloopstatus")
 @Produces(MediaType.APPLICATION_JSON)
 public Response getManInLoopStatus(@Context HttpServletRequest req) {
	 fileLogger.info("Inside Function : getManInLoopStatus");
  //fileLogger.debug("logger is :"+logger);
  //fileLogger.debug("in getManInLoopStatus");
	 fileLogger.info("Exit Function : getManInLoopStatus");
  return Response.status(201).entity("{\"manInLoopStatus\":\""+new Common().getColumnValues("db", "man_in_loop")+"\"}").build();
 }
  


	public void restartScanner(){
		try {
			 fileLogger.info("Inside Function : restartScanner fxn1");
		     DBDataService dbDataService = DBDataService.getInstance();
		     //set interrupted flag to true
		     //stop the running scanning
		     fileLogger.info("Inside Function : restartScanner fxn2");
		     NetscanSchedulerListener netscanSchedulerListener = new NetscanSchedulerListener();
		     fileLogger.info("Inside Function : restartScanner fxn3");
		     netscanSchedulerListener.stopNetscanScheduler();
		     //stop the scanner scheduler
		     fileLogger.info("Inside Function : restartScanner fxn4");
		     dbDataService.shutdownScannerScheduler();
		     fileLogger.info("Inside Function : restartScanner fxn5");
		     Thread.sleep(1000);
		     //start the scanner scheduler
		     fileLogger.info("Inside Function : restartScanner fxn6");
		     netscanSchedulerListener.startNetscanScheduler();
		     fileLogger.info("Exit Function : restartScanner fxn7");
	
		    } catch (Exception e) {
		     // TODO Auto-generated catch block
		     e.printStackTrace();
		    }
	}
	
	public static String getUTCTime(String date,String tz ){
		try{
		DateFormat formatterIST = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		formatterIST.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
		Date tempStartTime = formatterIST.parse(date);
		//Date tempEndTime = formatterIST.parse(endTimeIST);
		DateFormat formatterUTC = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		formatterUTC.setTimeZone(TimeZone.getTimeZone("UTC")); // UTC timezone
		date=formatterUTC.format(tempStartTime);
		//endTimeUTC=formatterUTC.format(tempEndTime);
		return date;
		}
		catch(Exception ex){
			return "";
		
		}
	
	}

	 @POST
	 @Path("/bmsData")
	 @Produces(MediaType.APPLICATION_JSON)
	 //public 	javax.xml.ws.Response<Map<String, Object>> BMSData(@Context HttpServletRequest request)
	public 	Response BMSData(@Context HttpServletRequest request,@FormParam("btnData") JSONObject btnData) throws JSONException
		{
			
		 	
			String cmd=getCMDfromCommands(btnData.getInt("ID"));
			fileLogger.debug("cmd = "+cmd);
			DBDataService dbDataService = DBDataService.getInstance();
			
			JSONObject response = new JSONObject();
			try 
			{
				int data2 = btnData.getInt("ID");
				String data =Integer.toString(data2); 

				
				String btn_PR = btnData.getString("pr_key");
				String prkey=btn_PR;
				String ip = btnData.getString("ip");
				fileLogger.debug("  data2 = "+data2+"  data = "+data+"  btn_PR = "+btn_PR+" prkey = "+prkey+" ip = "+ip);
				try {
						bmsDataPort= Integer.parseInt(DBDataService.configParamMap.get("bmsDataPort"));
					}
				catch (NumberFormatException e)
				{
					bmsDataPort = 0;
				}
				Date today = Calendar.getInstance().getTime();
				SimpleDateFormat crunchifyFormat = new SimpleDateFormat("MMM dd yyyy HH:mm:ss.SSS zzz");
				String currentTime = crunchifyFormat.format(today);
				fileLogger.debug(" currentTime "+ currentTime);
				long epochTime=-1;
				try {
					Date date = crunchifyFormat.parse(currentTime);
					fileLogger.debug(" date "+ date);
					epochTime = date.getTime()/1000L;
					
					fileLogger.debug(" epochTime "+ epochTime);

				}
				catch(Exception ex){
					fileLogger.debug("Error in getting the date / time");
				}
				
				String data_cmd=cmd;
				if(data.equals("11")) 
				{
					btn_PR = Long.toString(epochTime);
					//data=data+","+prkey;
					data_cmd=cmd+","+btn_PR;
				}
				
				else{
					if(btn_PR.length()>0){
						data_cmd=cmd+","+btn_PR;
					}
				}
				String result="0,0,0";
				if(!data.equals("10")){
					 result= UdpServerClient.sendBMS(ip,bmsDataPort,data_cmd);
				}
				
				fileLogger.debug(" result "+ result);
				String[] resultData = result.split(",");
				fileLogger.debug(" resultData[0] "+ resultData[0]);
				fileLogger.debug(" resultData[1] "+ resultData[1]);
				fileLogger.debug(" resultData[2] "+ resultData[2]);
				if(resultData[2].equals("0") || resultData[2].equals("0*")) 
				{
					switch(data) 
					{
						case "1" :
							dbDataService.saveBMSconfig(ip, "load", "", "1");
						break;
						case "2" :
							dbDataService.saveBMSconfig(ip, "load", "", "2");
						break;
						
						case "5" :
							dbDataService.saveBMSconfig(ip, "periodicity", "", btn_PR);
						break;
						case "11" :
						case "4" :
						case "10" :
							result = synchBMSStatus(ip);
							resultData = result.split(",");
							break;
					}
					response.put("result", "success");
				}
				else 
				{
					response.put("result", "fail");
				}
				
				
				
				response.put("message", "Added");
				if(resultData.length >=4) 
				{
					response.put("data", ip+","+getResultData(result));
				}
				
				//responseEntity =new ResponseEntity<Map<String, Object>>(response,HttpStatus.OK);
//				return (javax.xml.ws.Response<Map<String, Object>>) Response.status(201).entity(response).build();
//				return responseEntity;
				return Response.status(201).entity(response.toString()).build();
			}
			catch(Exception exception) {
				fileLogger.error(exception.toString());
				response.put("result", "fail");
				response.put("message", exception.getMessage());
				//responseEntity = new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
				//responseEntity =	return Response.status(201).entity(bmsData.toString()).build();				return responseEntity;
				return Response.status(201).entity(response.toString()).build();
			}	
		}

		public String getResultData(String result) 
		{
			String[] resultData = result.split(",");
			StringBuilder returnData = new StringBuilder("");
			
			if(resultData.length >=4) 
			{
				//String aa returnData = resultData[resultData.length-1].replace("*", "");
				//returnData = resultData
				//returnData.
				
				for(int i=3;i<resultData.length;i++) 
				{
					if(i==resultData.length-1) 
					{
						returnData.append(resultData[i]);
					}
					else 
					{
						returnData.append(resultData[i]+",");
					}
				}
				
				
			}
			return returnData.toString().replace("*", "");
		}
		
		public String synchBMSStatus(String ip) 
		{
			String cmd="";
			fileLogger.debug("inside synchBMSStatus");
		    DBDataService dbDataService = DBDataService.getInstance();
			//String ip,int port,String data
		    cmd=getCMDfromCommands(7);
			
			String result = UdpServerClient.sendBMS(ip,bmsDataPort,cmd);
			dbDataService.saveBMSconfig(ip, "load", "", getResultData(result));
			cmd=getCMDfromCommands(8);
			result = UdpServerClient.sendBMS(ip,bmsDataPort,cmd);
			dbDataService.saveBMSconfig(ip, "periodicity", "", getResultData(result));
			cmd=getCMDfromCommands(9);
			result = UdpServerClient.sendBMS(ip,bmsDataPort,cmd);
			dbDataService.saveBMSconfig(ip, "systemtime", "", getResultData(result));
			return result;
		}
		public String getCMDfromCommands(int id){
			
			String query="select * from commands where id="+ id;
			fileLogger.debug("query = "+query);
			JSONArray rs = new Operations().getJson(query);
			fileLogger.debug(query);
			String cmd="";
			try {
				cmd = rs.getJSONObject(0).getString("cmd");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return cmd;
		}


//		public String executeUdpBMS(String ip,String id,String msg) 
//		{
//			Commands cmd = cs.getCommand(Long.parseLong(id));
//			String result =  "0,0,0";
//			if(cmd.getCmd() != null && !cmd.getCmd().equalsIgnoreCase("")) 
//			{
//				String port = env.getProperty("bms_udp_port");
//				//@sandeep
//				
//				if(msg != null && !msg.equalsIgnoreCase("")) 
//				{
//					msg = ","+msg;
//				}
//				result = dataServiceObj.sendBMS(ip,Integer.parseInt(port),cmd.getCmd()+""+msg);
//			}
//			return result;
//		}
		
	
		
		@POST
		 @Path("/getPeriodicity")
		 @Produces(MediaType.APPLICATION_JSON)
		public int getPeriodicityfunction(){
			String query="";
			int value=0;
			String ip="";
			try{
				 JSONArray DataArray = new Operations().getJson("SELECT * FROM public.view_btsinfo where code = 16");
				 if (DataArray != null) {
					 JSONObject DataObject = DataArray.getJSONObject(0);
						
					  ip = DataObject.getString("ip");
				 }
			
				query="select value from bmsconfig  where ip='"+ ip+"' and name='periodicity'  order by id desc limit 1";
				//Pr_text id of text field
				JSONArray rs = new Operations().getJson(query);
				
				try {
					value = rs.getJSONObject(0).getInt("value");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
				
			}
			catch(Exception e) 
			{
			
			}
				return value;
			 }
			
			
		
		@POST
		@Path("/OctasicPowerOffOnCall")
		@Produces(MediaType.TEXT_HTML)		
		public String OctasicPowerOffOnCall(@FormParam("ip") String ip){
			
			
			
			if(DBDataService.isOctasicPowerOn()) {
				boolean result= new ApiCommon().OctasicPowerOffOn(ip);
				if(result) {
					return "Reboot SuccessFully Executed";
				}
				else {
					return "Failure in rebooting";
				}
			}
			else
			{
				String msg="Reboot already in progress";
				fileLogger.debug(msg);
				return msg;
			
			}
		}
		
		@GET
		@Path("/OctasicShutDown")
		public String OctasicShutDown(){
			String msg=null;
			String result= new ApiCommon().switchDsp("shutdown_octasic",0,0,999,"");
			if(result==null) {
				msg="Problem in System Manager shutdown";
				
			}
			else
			{
				 msg="System Manager shutdown suceessfully";
				fileLogger.debug(msg);
				
		}
			return msg;
		}
		
		
		
		
		
		
		
		
		
		

		 @POST
		 @Path("/getNeighboursDataa")
		 @Produces(MediaType.APPLICATION_JSON)
		 public Response getNeighboursDataa(@Context HttpServletRequest req, @FormParam("packetID") int packetID,@FormParam("tech") String tech ,@FormParam("ueafrcn") String uearfcn,@FormParam("ant") String ant) {
		//public void getNeighboursDataa(int packetID,String tech){
			ArrayList<String> arfcnList2= new ArrayList<String>();
			String query  ="";
			JSONArray js=new JSONArray();
			JSONObject jsonObject  = new JSONObject();
			JSONArray ja_data = new JSONArray();
			JSONArray jneighbours = new JSONArray();
			int tech_id=-1;
			switch(tech)
			{
				case "GSM": tech_id=1;
							break;
				case "Loc_2g": tech_id=1;
								break;
							

				case "UMTS": tech_id=2;
								break;

				case "Loc_3g": tech_id=2;
							break;
				
				case "LTE": tech_id=3;
							break;
				case "Loc_4g": tech_id=3;
								break;
			}
			try {
				// query  = "select rpt_data from netscan_cell_scan_report where id= 110708";
				query  = "select rpt_data from netscan_cell_scan_report where id= "+packetID +" ;";//+" and tech_id= '"+tech_id+"';";
				 fileLogger.info("Ibbnside Function : getNeighboursDataa query = "+query);
				js = new Operations().getJson(query);
				String strr=(js.getJSONObject(0).getString("rpt_data"));
				 jsonObject = new JSONObject(strr);
				 JSONObject jjobj = new JSONObject();
				 ja_data = jsonObject.getJSONArray("REPORT");
				jsonObject=ja_data.getJSONObject(0).getJSONObject("NEIGHBORS");
				
				
				ja_data = jsonObject.getJSONArray("INTRA_FREQ_NEIGH");
				
				int lengthArfcn=ja_data.length();
				System.out.println(lengthArfcn);
				String arfcnStringList="";
				String	bb=" and oprlogs_current.antenna_id in (select id from antenna where  profile_name = '"+ant+"') " ;
				if(tech_id==1) {
					for(int k=0;k<lengthArfcn;k++)
					{
						arfcnStringList=arfcnStringList+" , '"+ja_data.getJSONObject(k).getInt("ARFCN")+"' ";
					}
					arfcnStringList=arfcnStringList.substring(2);
					query = "select distinct mcc ,mnc ,lac,tac,earfcn,arfcn,uarfcn,cell,ncc,bcc,psc,pci from oprlogs_current where  packet_type = '"+tech +"' and arfcn in ( "+arfcnStringList + " ) and inserttime >= (timezone('utc'::text, now()) - '24:00:00'::interval) AND inserttime <= timezone('utc'::text, now()) "+bb+"  ;";
				}
				else if(tech_id==2) {
					for(int k=0;k<lengthArfcn;k++)
					{
						arfcnStringList=arfcnStringList+" , '"+ja_data.getJSONObject(k).getInt("PSC")+"' ";
					}
					arfcnStringList=arfcnStringList.substring(2);
					query = "select  distinct mcc ,mnc ,lac,tac,earfcn,arfcn,uarfcn,cell,ncc,bcc,psc,pci from oprlogs_current where  uarfcn = '"+uearfcn +"' and psc in ( "+arfcnStringList + " ) and inserttime >= (timezone('utc'::text, now()) - '24:00:00'::interval) AND inserttime <= timezone('utc'::text, now())  "+bb+"  ;";
				}
				else
				{
					for(int k=0;k<lengthArfcn;k++)
					{
						arfcnStringList=arfcnStringList+" , '"+ja_data.getJSONObject(k).getInt("PCI")+"' ";
					}
					arfcnStringList=arfcnStringList.substring(2);
					query = "select distinct mcc ,mnc ,lac,tac,earfcn,arfcn,uarfcn,cell,ncc,bcc,psc,pci  from oprlogs_current where  earfcn = '"+ uearfcn+ "' and pci in ( "+arfcnStringList + " ) and inserttime >= (timezone('utc'::text, now()) - '24:00:00'::interval) AND inserttime <= timezone('utc'::text, now())  "+bb+"  ;";
				}
				
				fileLogger.info("Inside Function : getNeighboursDataa querywa = "+query);
				JSONArray rs = new Operations().getJson(query);
		
				String tempArfcnnn=arfcnStringList;
				String temparfcn2="";
				String bsci="";
				int ncc=-99999999;

				for(int l=0;l<rs.length();l++)
				{
					jjobj = new JSONObject();
					if(tech_id==1) 
					{
						temparfcn2=""+rs.getJSONObject(l).getInt("arfcn");
					try {
						ncc=Integer.parseInt(rs.getJSONObject(l).getString("ncc"));
						ncc=ncc*8;
						bsci=rs.getJSONObject(l).getString("bcc")+""+ncc;
						}
					catch(Exception e)			
					{
						bsci="";
					}
					}
					else if(tech_id==2){
						temparfcn2=""+rs.getJSONObject(l).getInt("psc");
					}
					else
					{
						temparfcn2=""+rs.getJSONObject(l).getInt("pci");
					}	
					tempArfcnnn=tempArfcnnn.replaceAll("'"+temparfcn2+"\'","-9999");

					jjobj.put("tech", tech);
					jjobj.put("plmn", rs.getJSONObject(l).getString("mcc")+rs.getJSONObject(l).getString("mnc"));
					jjobj.put("lactac", rs.getJSONObject(l).getString("lac")+rs.getJSONObject(l).getString("tac"));
					jjobj.put("auearfcn", rs.getJSONObject(l).getString("arfcn")+rs.getJSONObject(l).getString("uarfcn")+rs.getJSONObject(l).getString("earfcn"));
					jjobj.put("cell", rs.getJSONObject(l).getString("cell"));
			
					int rssi=0;
					
					jjobj.put("bscipscpci", bsci+rs.getJSONObject(l).getString("psc")+rs.getJSONObject(l).getString("pci"));
					if (tech_id==1 )
					{
						jjobj.put("rssi",getrssi(""+rs.getJSONObject(l).getInt("arfcn"), "","gsm",ant));
					}
					else if (tech_id==2)
					{
						jjobj.put("rssi",getrssi(""+rs.getJSONObject(l).getInt("uarfcn"), ""+rs.getJSONObject(l).getInt("psc"),"umts",ant));
					}
						
					else
					{
						jjobj.put("rssi",getrssi(""+rs.getJSONObject(l).getInt("earfcn"),""+rs.getJSONObject(l).getInt("pci"),"lte",ant));
						
					}
					//jjobj.put("rssi",rs.getJSONObject(l).getString("rssi"));
					
					jneighbours.put(jjobj);
						
					
					
				}
				
				String[] tempArfcnnnArr=tempArfcnnn.split(",");
				
				for(int k=0;k<tempArfcnnnArr.length;k++)
				{
					if(!tempArfcnnnArr[k].trim().equalsIgnoreCase("-9999") && (!(tempArfcnnnArr[k].trim().equalsIgnoreCase(""))) )
					{
						jjobj = new JSONObject();
						jjobj.put("tech", tech);
						jjobj.put("plmn", "NA");
						jjobj.put("lactac", "NA");
						jjobj.put("cell", "NA");
						if (tech_id==1 )
						{
							jjobj.put("auearfcn", tempArfcnnnArr[k].trim().replaceAll("'", ""));
							jjobj.put("bscipscpci","NA");
						}
						else
						{
							jjobj.put("auearfcn",uearfcn );
							jjobj.put("bscipscpci", tempArfcnnnArr[k].trim().replaceAll("'", ""));
						}				
					//	jjobj.put("bscipscpci","" );
						jjobj.put("rssi", "NA");
						jneighbours.put(jjobj);
						
					}
				}
				
				
				
//					for(int k=0;k<lengthArfcn;k++)
//					{
//						jjobj = new JSONObject();
//						String temparfcn="";
//						if(tech_id==1) 
//						{
//							 temparfcn=""+ja_data.getJSONObject(k).getInt("ARFCN");
//						}
//						else if(tech_id==2){
//							 temparfcn=""+ja_data.getJSONObject(k).getInt("PSC");
//						}
//						else
//						{
//							 temparfcn=""+ja_data.getJSONObject(k).getInt("PCI");
//						}
//							boolean flagArfcnfound=false;
//							arfcnList2.add(temparfcn);
//							for(int l=0;l<rs.length();l++)
//							{
//								String temparfcnOprlogsCurrent="";
//								if(tech_id==1) 
//								{
//									 temparfcnOprlogsCurrent=rs.getJSONObject(l).getString("arfcn");
//								}
//								else if(tech_id==2){
//									 temparfcnOprlogsCurrent=rs.getJSONObject(l).getString("psc");
//								}
//								else
//								{
//									 temparfcnOprlogsCurrent=rs.getJSONObject(l).getString("pci");
//								}
//								
//								if(temparfcnOprlogsCurrent!=null && temparfcn!=null) {
//									if(temparfcnOprlogsCurrent.equalsIgnoreCase(temparfcn))
//									{
//										jjobj.put("tech", tech);
//										jjobj.put("plmn", rs.getJSONObject(l).getString("mcc")+rs.getJSONObject(l).getString("mnc"));
//										jjobj.put("lactac", rs.getJSONObject(l).getString("lac")+rs.getJSONObject(l).getString("tac"));
//										jjobj.put("auearfcn", rs.getJSONObject(l).getString("arfcn")+rs.getJSONObject(l).getString("uarfcn")+rs.getJSONObject(l).getString("earfcn"));
//										jjobj.put("cell", rs.getJSONObject(l).getString("cell"));
//										int ncc=-99999999;
//										String bsci="";
//										
//										if(rs.getJSONObject(l).getString("ncc")==null|| rs.getJSONObject(l).getString("ncc")=="" || rs.getJSONObject(l).getString("bcc")!=null|| rs.getJSONObject(l).getString("bcc")=="") {
//											bsci="";
//											
//										}
//										else {
//											ncc=Integer.parseInt(rs.getJSONObject(l).getString("ncc"));
//											ncc=ncc*8;
//											bsci=rs.getJSONObject(l).getString("bcc")+""+ncc;
//										
//										}
//										
//										
//										jjobj.put("bscipscpci", bsci+rs.getJSONObject(l).getString("psc")+rs.getJSONObject(l).getString("pci"));
//										jjobj.put("rssi",rs.getJSONObject(l).getString("rssi"));
//										
//										flagArfcnfound =true;
//										break;
//									}
//								}
//								
//							}
//							if(!flagArfcnfound)
//							{
//								jjobj.put("tech", tech);
//								jjobj.put("plmn", "NA");
//								jjobj.put("lactac", "NA");
//								jjobj.put("auearfcn", "NA");
//								jjobj.put("bscipscpci", "NA");
//								jjobj.put("cell", "NA");
//								jjobj.put("rssi", "NA");
//								
//							}
//						  jneighbours.put(jjobj);
//					}
					
				
		
				
				
			    System.out.println(jneighbours);
		
				
			} 
			catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		
		}
		//int a=  ja_data.getJSONObject(0).getInt("ARFCN");
		System.out.println(arfcnList2);

		  fileLogger.info("Exit Function : getNeighboursDataa");
		  return Response.status(201).entity(jneighbours.toString()).build();


		
		
		
		}
		
		
		 public static Integer getrssi(String arfcn,String pci,String type,String antenna)
			{
				String aa="";
				String bb="";
				//Integer ret =-999;
				if(arfcn != null && type.equalsIgnoreCase("gsm")) 
				{
					aa=" and arfcn ='"+arfcn+"' ";
				}
				else if(arfcn != null && type.equalsIgnoreCase("umts"))
				{
					aa=" and uarfcn='"+arfcn+"' and psc ='"+pci+"'";
				}else if(arfcn != null && type.equalsIgnoreCase("lte")){
					aa=" and earfcn ='"+arfcn+"'  and pci ='"+pci+"'";
				}
			String result3="-999";

			bb=" and oprlogs_current.antenna_id in (select id from antenna where  profile_name = '"+antenna+"') " ;
				
			
			String query ="select rssi from oprlogs_current where  (inserttime >= (timezone('utc'::text, now()) - '24:00:00'::interval) or count = 'locator_count')  AND inserttime <= timezone('utc'::text, now())  "+aa+" "+bb+"  order by inserttime desc limit 1";                        
			fileLogger.info("Inside Function : getrssi Query  1st time = "+query);
			JSONArray result4 = new Operations().getJson(query);
			if( result4.length()!= 0)
					
			{
				try {
					result3= result4.getJSONObject(0).getString("rssi");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

					}
			else
			{
				query ="select rssi from oprlogs_current where  (inserttime >= (timezone('utc'::text, now()) - '24:00:00'::interval) or count = 'locator_count')  AND inserttime <= timezone('utc'::text, now())  "+aa+"  order by rssi::integer limit 1";	
				fileLogger.info("Inside Function : getrssi Query  2nd time = "+query);
				 result4 = new Operations().getJson(query);
				if( result4.length()!= 0)
						{
					try {
						result3= result4.getJSONObject(0).getString("rssi");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

						}

			}
				return Integer.parseInt(result3);
				
			}		
		
	
		 public static String getNeighboursDataCell_Infrastructure_Report(int packetID,String tech ) {
		//public void getNeighboursDataa(int packetID,String tech){
			ArrayList<String> arfcnList2= new ArrayList<String>();
			String arfcnStringList="";
			String NeighboursEUArfcn="";
			String query  ="";
			JSONArray js=new JSONArray();
			JSONObject jsonObject  = new JSONObject();
			JSONArray ja_data = new JSONArray();
			JSONArray jneighbours = new JSONArray();
			int tech_id=-1;
			switch(tech)
			{
				case "GSM": tech_id=1;
							break;
				case "Loc_2g": tech_id=1;
								break;
							

				case "UMTS": tech_id=2;
								break;

				case "Loc_3g": tech_id=2;
							break;
				
				case "LTE": tech_id=3;
							break;
				case "Loc_4g": tech_id=3;
								break;
			}
			try {
				// query  = "select rpt_data from netscan_cell_scan_report where id= 110708";
				query  = "select rpt_data from netscan_cell_scan_report where id= "+packetID +" ;";//+" and tech_id= '"+tech_id+"';";
				 fileLogger.info("Ibbnside Function : getNeighboursDataa query = "+query);
				js = new Operations().getJson(query);
				String strr=(js.getJSONObject(0).getString("rpt_data"));
				 jsonObject = new JSONObject(strr);
				 JSONObject jjobj = new JSONObject();
				 ja_data = jsonObject.getJSONArray("REPORT");
				jsonObject=ja_data.getJSONObject(0).getJSONObject("NEIGHBORS");
				
				
				ja_data = jsonObject.getJSONArray("INTRA_FREQ_NEIGH");
				
				int lengthArfcn=ja_data.length();
				
				
				
				switch(tech_id){
				case 1: NeighboursEUArfcn+="ARFCN: ";
					break;
				case 2:	NeighboursEUArfcn+="PSC: ";
					break;
				case 3:	NeighboursEUArfcn+="PCI: ";
					break;	
				}
				
				
				
				
				if(tech_id==1) {
					for(int k=0;k<lengthArfcn;k++)
					{
						arfcnStringList=arfcnStringList+" , "+ja_data.getJSONObject(k).getInt("ARFCN")+" ";
					}
					arfcnStringList=arfcnStringList.substring(2);
					
					
				}
				else if(tech_id==2) {
					for(int k=0;k<lengthArfcn;k++)
					{
						arfcnStringList=arfcnStringList+" , "+ja_data.getJSONObject(k).getInt("PSC")+" ";
					}
					arfcnStringList=arfcnStringList.substring(2);
					
				}
				else
				{
					for(int k=0;k<lengthArfcn;k++)
					{
						arfcnStringList=arfcnStringList+" , "+ja_data.getJSONObject(k).getInt("PCI")+" ";
					}
					arfcnStringList=arfcnStringList.substring(2);
					
				}
				
				NeighboursEUArfcn+=arfcnStringList;
				
				
				fileLogger.info("Inside Function : getNeighboursDataa querywa = "+query);
				JSONArray rs = new Operations().getJson(query);
		
				String tempArfcnnn=arfcnStringList;
				
			
			
				
			} 
			catch (JSONException e) {
				
				e.printStackTrace();
		
		}
		
			System.out.println(arfcnList2);
			fileLogger.info(" Function : getNeighboursDataa arfcnList2 = "+arfcnList2);
		  fileLogger.info("Exit Function : getNeighboursDataa");
		  return NeighboursEUArfcn;

		}
		 @POST
		 @Path("/getDeviceIpFromDCODE")
		 @Produces(MediaType.APPLICATION_JSON)
		 public Response getDeviceIpFromDCODE(@FormParam("dcode") int i) {
		  fileLogger.info("Inside Function : getDeviceDetails");
		  String query = "select * from view_btsinfo where dcode = "+i +";";
		  JSONArray rs = new Operations().getJson(query);
		  fileLogger.debug("Fxn getDeviceIp rs ="+rs);
		  fileLogger.info("Exit Function : getDeviceDetails");
		  return Response.status(201).entity(rs.toString()).build();
		 }
	
	 	
		 @POST
		 @Path("/makeGraph")
		 @Produces(MediaType.APPLICATION_JSON)
		 public Response makeGraph(@FormParam("temp") int temp) {
		  fileLogger.info("Inside Function : makeGraph");
		  String query = "select config from view_btsinfo where dcode=21;";
		  JSONArray rs = new Operations().getJson(query);
		  
		  
		  
		  
		  fileLogger.debug("Fxn getDeviceIp rs ="+rs);
		  fileLogger.info("Exit Function : makeGraph");
		  return Response.status(201).entity(rs.toString()).build();
		 }
	 	 
	 	 
}



	

