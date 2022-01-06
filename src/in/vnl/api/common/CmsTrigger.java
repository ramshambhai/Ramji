package in.vnl.api.common;
//import java.util.Date;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import javax.ws.rs.core.Response.ResponseBuilder;

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
import in.vnl.msgapp.Common;
import in.vnl.msgapp.GeoSchedulerServer;
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
import java.net.*; 
import java.io.*; 
import java.util.*; 
import java.net.InetAddress;

@Path("/cms")
public class CmsTrigger 
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
			   
	@GET
    @Path("/GET_INVENTORY_DETAILS")
	@Produces(MediaType.APPLICATION_JSON)
    public Response getInventoryDetails(@Context HttpServletRequest req)
	 {	
		fileLogger.info("Inside Function : getInventoryDetails");
		fileLogger.debug("get_inventory_details:"+new Date());
		JSONObject inventoryWrapObj=new JSONObject();
		JSONArray inventoryArr = new JSONArray();
		JSONObject inventoryObj=new JSONObject();  
		String deviceIp=Common.getSystemIPAddress();
		Operations operations = new Operations();
		String lat="-1";
		String lon="-1";
		JSONArray gpsArray=null;
		JSONObject gpsObject=null;
		try {
			gpsArray=operations.getJson("select lat,lon from gpsdata order by logtime desc limit 1");
			
			if(gpsArray.length()!=0){
				gpsObject=gpsArray.getJSONObject(0);
				lat=gpsObject.getString("lat");
			if(lat==null || lat.equalsIgnoreCase("")){
				gpsArray=operations.getJson("select lat,lon from netscan_gps_data order by logtime desc limit 1");
				if(gpsArray.length()!=0){
				gpsObject=gpsArray.getJSONObject(0);
				lat=gpsObject.getString("lat");
				if(lat==null || lat.equalsIgnoreCase("")){
					lat="";
					lon="";
				}else{
					lon=gpsObject.getString("lon");
				}
			}else{
				lat="";	
				lon="";
			}
			}else{
				lon=gpsObject.getString("lon");
			}
			}else{
				gpsArray=operations.getJson("select lat,lon from netscan_gps_data order by logtime desc limit 1");
				if(gpsArray.length()!=0){
				gpsObject=gpsArray.getJSONObject(0);
				lat=gpsObject.getString("lat");
				if(lat==null || lat.equalsIgnoreCase("")){
					lat="";
					lon="";
				}else{
					lon=gpsObject.getString("lon");
				}
			}else{
				lat="";	
				lon="";
			}
			}
			String angleOffset="";
			JSONArray angleOffsetArr = operations.getJson("select angle_offset from antenna where atype='1' limit 1");
			if(angleOffsetArr.length()!=0){
				angleOffset = angleOffsetArr.getJSONObject(0).getString("angle_offset");
			}else{
				angleOffset="0";
			}
		   	inventoryObj.put("DEVICE_IP",deviceIp);
		   	inventoryObj.put("LATITUDE",lat);
		   	inventoryObj.put("LONGITUDE",lon);
		   	inventoryObj.put("COVERAGE","2.5");
		   	inventoryObj.put("OFFSET",angleOffset);
		   	inventoryObj.put("STATE","1");
            inventoryArr.put(inventoryObj);
            inventoryWrapObj.put("DeviceInfo",inventoryArr);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fileLogger.info("Exit Function : getInventoryDetails");
	   	return Response.status(201).entity(inventoryWrapObj.toString()).build();
	}
	
	@GET
    @Path("/GET_SUB_INVENTORY")
	@Produces(MediaType.APPLICATION_JSON)
    public Response getSubInventoryDetails(@Context HttpServletRequest req)
	 {	
		fileLogger.info("Inside Function : getSubInventoryDetails");
		fileLogger.debug("GET_SUB_INVENTORY:"+new Date()); 
		
		Operations operations = new Operations();
		JSONArray subInventoryList= new JSONArray(); 
	
			try {
				subInventoryList=operations.getSubInventory("select show_name,ip,status from view_btsinfo where ip not in('0.0.0.0','1.1.1.1')");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				fileLogger.error("exception in getSubInventoryDetails :"+e.getMessage());
				e.printStackTrace();
			}
		 fileLogger.info("Exit Function : getSubInventoryDetails");
		 return Response.status(201).entity(subInventoryList.toString()).build();
	}
	
	@GET
    @Path("/RESTART")
	@Produces(MediaType.APPLICATION_JSON)
    public Response restartSystem(@Context HttpServletRequest req)
	 {	
		//fileLogger.info("Inside Function : restartSystem");
		String rebootStatus=new CommonOperation().restartSystem();	
		JSONObject inventoryObj=new JSONObject();
			try {
				inventoryObj.put("result",rebootStatus);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	   // fileLogger.info("Exit Function : restartSystem");
		return Response.status(201).entity(inventoryObj.toString()).build();
	}
	
	@GET
    @Path("/GET_STATUS")
	@Produces(MediaType.APPLICATION_JSON)
    public Response getFalconStatus(@Context HttpServletRequest req)
	 {	
		fileLogger.info("Inside Function : getFalconStatus");
		fileLogger.debug("get_status:"+new Date());
		JSONObject inventoryObj=new JSONObject();
		Common common = new Common();
		
		   	try {
		   	    int usedSpaceLimit=Integer.parseInt(common.getDbCredential().get("usedspacelimit"));
		   	    String usedSpacePercentage=getUsedSpacePercentage();
		   	    String freezeFlag="0";
		   	    if(Integer.parseInt(usedSpacePercentage)>usedSpaceLimit){
		   	    	freezeFlag="1";
		   	    }
				inventoryObj.put("SPACE",usedSpacePercentage+"%");
				JSONObject trackStatusObject=new Operations().getJson("select mode_status,applied_antenna from running_mode where mode_type='track'").getJSONObject(0);
				String modeStatus=trackStatusObject.getString("mode_status");
				String trackAntenna="";
				if(modeStatus.equalsIgnoreCase("idle")){
					inventoryObj.put("MODE","Idle");
				}else{
					trackAntenna=trackStatusObject.getString("applied_antenna");
					inventoryObj.put("MODE",modeStatus+"("+trackAntenna+")");
				}
				inventoryObj.put("FREEZE",freezeFlag);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
		   	fileLogger.info("Exit Function : getFalconStatus");
			return Response.status(201).entity(inventoryObj.toString()).build();
	}
	
	public String getUsedSpacePercentage(){
		fileLogger.info("Inside Function : getUsedSpacePercentage");
		String userDir="";
/*		if(userDir==null){
			userDir="/opt/Locator";
		}*/
		
		String osName=System.getProperty("os.name").toLowerCase();
		fileLogger.debug("os.name is :"+osName);
		if(osName.indexOf("win")!=-1){
			userDir=System.getProperty("user.dir");
			userDir=userDir.substring(0,userDir.indexOf("\\")+1);
		}else if(osName.indexOf("linux")!=-1 || osName.indexOf("unix")!=-1){
			userDir=System.getenv("OMC_DASHBOARD");
			int firstSlashIndex=userDir.indexOf("/");
			int secondSlashIndex=userDir.indexOf("/", firstSlashIndex+1);
			userDir=userDir.substring(0,secondSlashIndex+1);
		}else{
			userDir="/opt/Locator";
		}
		 File file = new File(userDir);
		 fileLogger.debug("userDir is :"+userDir);
		        long totalSpace = file.getTotalSpace();
		        long usableSpace = file.getUsableSpace();
		        int percentLeft = (int)((usableSpace*100)/totalSpace);

		        String usedSpacePercentage = String.valueOf(100-percentLeft);
/*		        int 
		        int thresholdUsedSpace = Integer.parseInt(new Common().getColumnValues("db", "usedspacelimit"));
		        if(usedSpacePercentage>=thresholdUsedSpace){
		        	
		        }*/
		    	fileLogger.info("Exit Function : getUsedSpacePercentage");
		        return usedSpacePercentage;
	}
	
	public JSONArray getJson(String query)
	{
		fileLogger.info("Inside Function : getJson()");
		Common co = new Common();
		Statement smt = null;
		Connection con = co.getDbConnection();
		
		JSONArray ja = new JSONArray();	
		
		try
		{
			smt = con.createStatement();			
			//String query = "select oprrationdata.id,btsoprparma.id btsprmid,oprrationdata.name,loc,btstype.name as bts,atype,again,aheight,aelevation,adirection,ttype from oprrationdata inner join btsoprparma on (oprrationdata.id = btsoprparma.oprid) left join btstype on(btsoprparma.btsid = btstype.id) order by btsprmid desc;";
			fileLogger.debug(query);
			ResultSet rs = smt.executeQuery(query);
			ResultSetMetaData rm = rs.getMetaData();
			int totalColumns = rm.getColumnCount();
			ArrayList<String> columns = new ArrayList<String>();
			
			for(int i=1;i<=totalColumns;i++ )
			{
				columns.add(rm.getColumnName(i));
			}
			int count = 0;
			while(rs.next())
			{				
				
				count++;
				if(count >= 100000)
				{
					JSONObject jb = new JSONObject();
					jb.put("result","fail");
					jb.put("msg","Limit exceed");
					ja = null;
					ja = new JSONArray();
					ja.put(jb);
					break;
				}
				
				JSONObject jb = new JSONObject();
				for(String cname:columns)
				{
					if(rs.getString(cname) == null)
					{
						jb.put(cname, "");
					}
					else
					{
						jb.put(cname, rs.getString(cname));
					}
					
				}
				ja.put(jb);
			}			
		}
		catch(Exception E)
		{
			fileLogger.error("Exception while authenticating the user "+E.getMessage());
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
		fileLogger.info("Exit Function : getJson");
		return ja;
	}
	
       @POST
	   @Path("/FETCH_REPORT")
	   @Produces("application/zip")
	   public Response reciveFromDevice(@Context HttpServletRequest req,String data)
	   {   
		   
    	   fileLogger.info("Inside Function : reciveFromDevice");
		   String startTimeUTC = null;
		   String endTimeUTC = null;
		   String seprator = null;
		   String startTimeIST=null;
		   String endTimeIST=null;
		try {
			fileLogger.debug("@cms_fetch_report data is :"+data);
			JSONObject revicedJsonData = new JSONObject(data);
			startTimeIST = revicedJsonData.getString("START_TIME");
			endTimeIST = revicedJsonData.getString("STOP_TIME");
			
			DateFormat formatterIST = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			formatterIST.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
			Date tempStartTime = formatterIST.parse(startTimeIST);
			Date tempEndTime = formatterIST.parse(endTimeIST);
			DateFormat formatterUTC = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			formatterUTC.setTimeZone(TimeZone.getTimeZone("UTC")); // UTC timezone
			startTimeUTC=formatterUTC.format(tempStartTime);
			endTimeUTC=formatterUTC.format(tempEndTime);
			seprator = revicedJsonData.getInt("REPORT_TYPE") == 1 ? "," : "\t";
		} catch (Exception e) {
			// TODO: handle exception
		}
		   
		    
			String responseJsonStr = new ReportServer().createReports(null,startTimeUTC, endTimeUTC, seprator,"reports","active");
			String fileName="";
			   try {
				//Report Download audit logs
				   LinkedHashMap < String, String > log = new LinkedHashMap < String, String > ();
				   log.put("source", "Finley");
				   log.put("start time", startTimeIST);
				   log.put("end time", endTimeIST);
				   new AuditHandler().auditReportDownload(log);
				   //report download audit logs
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				fileLogger.error("exception in audit log of report from finley  message:"+e1.getMessage());
				//e1.printStackTrace();
			}
			try {
				JSONObject responseJsonObj=new JSONObject(responseJsonStr);
				fileName = responseJsonObj.getString("msg");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String absolutePath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
			absolutePath = absolutePath.substring(0,absolutePath.lastIndexOf("WEB-INF"));
			File file = new File(absolutePath	+ "/resources/reports/"+fileName+"");
		    fileName = file.getName();
		   
	        ResponseBuilder responseBuilder = Response.ok((Object) file);
	        responseBuilder.header("Content-Disposition", "attachment; filename=\""+fileName+"\"");
	        fileLogger.info("Exit Function : reciveFromDevice");
	        return responseBuilder.build();
	   }

			  
	@POST
    @Path("/EVENT_REPORT")
	@Produces(MediaType.APPLICATION_JSON)
    public Response getEvent(@Context HttpServletRequest req,String data)
	 {	 
		   fileLogger.info("Inside Function : getEvent");
		   fileLogger.debug("EVENT_REPORT:"+new Date());
		   String startTime = null,endTime = null;
		   int type=-1;
		   String queryPart="";
		   String query="";
		   JSONArray eventArray=new JSONArray();
			try {
				JSONObject receivedJsonData = new JSONObject(data);
				startTime = receivedJsonData.getString("START_TIME");
				endTime = receivedJsonData.getString("STOP_TIME");
				type = receivedJsonData.getInt("TYPE");
				if(type==2){
					queryPart=" and event_type_id=7";
				}else{
					
				}
				query = "select event_data from log_evnts where log_date>='"+startTime+"'::timestamp - interval '05:30:00' and log_date<='"+endTime+"'::timestamp - interval '05:30:00'"+queryPart+"";
			        
			    eventArray=new Operations().getEventReport(query);
			}catch (Exception e) {
				// TODO: handle exception
			}
			fileLogger.info("Exit Function : getEvent");
					   	return Response.status(201).entity(eventArray.toString()).build();
			   }
	
	@POST
    @Path("/ACK_ALARM")
	@Produces(MediaType.APPLICATION_JSON)
    public Response ackAlarm(@Context HttpServletRequest req,String data)
	 {	
		fileLogger.info("Inside Function : ackAlarm");
		fileLogger.debug("ACK_ALARM:"+new Date());
		String transId="";
		JSONObject returnedResponse=new JSONObject();
		try {
			JSONObject receivedJsonData = new JSONObject(data);
			transId = receivedJsonData.getString("TRANS_ID");
			String query = "update log_evnts  SET event_data = event_data || '{\"ACKNOWLEDGE\": \"1\"}'; where event_data @> '{\"TRANS_ID\": \"'"+transId+"'\"}'";
			if(new Common().executeDLOperation(query)){
				returnedResponse.put("RESULT", "Success");
			}else{
				returnedResponse.put("RESULT", "Fail");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		    fileLogger.info("Exit Function : ackAlarm");
			return Response.status(201).entity(returnedResponse.toString()).build();
			   }
	
   	@GET
	@Path("/cells")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCells()
	{	
	   	String cells = new CMSController().CurrentCellsToCMS();   
		return Response.status(201).entity(cells).build();
	}
   
   	@GET
    @Path("/powerStatus")
	@Produces(MediaType.APPLICATION_JSON)
    public Response powerStatus(@Context HttpServletRequest req) {
   		CommonService cs=new CommonService();
   		fileLogger.info("Inside Function : powerStatus");
   		ArrayList<HashMap<String,String>> bmsData= new ArrayList<HashMap<String,String>>();
   		CommonService cm = new  CommonService();
   		try {
				 JSONArray DataArray = new Operations().getJson("SELECT * FROM public.view_btsinfo where code = 16");
				 if (DataArray != null) {
				 for(int j=0;j<DataArray.length();j++){
					 JSONObject DataObject = DataArray.getJSONObject(j);
						
						String ip = DataObject.getString("ip");
						LinkedHashMap<String,String> hs = new LinkedHashMap<String,String>();
						
						hs.put("IP",ip);
						hs.put("Name","BMS");
						
						try 
						{
							//String result = UdpServerClient.sendBMS(ip,6,"GET_STATUS");
							int bmsDataPort;
							
							try {
									bmsDataPort= Integer.parseInt(DBDataService.configParamMap.get("bmsDataPort"));
								}
							catch (NumberFormatException e)
							{
								bmsDataPort = 0;
							}
							
							String cmd=cs.getCMDfromCommands(6);
							String result = UdpServerClient.sendBMS(ip,bmsDataPort,cmd);
							String[] resultData = result.split(",");
							
							if(resultData[2].equals("0") || resultData[2].equals("0*")) 
							{
							
								String[] data = cm.getResultData(result).split(",");
								hs.put("Voltage(V)", String.format("%.1f", Double.parseDouble(data[15])/1000));
								hs.put("SOC(%)", data[17]);
								hs.put("Current(A)", String.format("%.2f", Double.parseDouble(data[16])/1000));
								hs.put("Wattage(W)", String.format("%.1f", Double.parseDouble(data[15])*Double.parseDouble(data[16])/1000000));
								
							}
							else 
							{
								throw (new Exception());
							}
						}
						catch(Exception e) 
						{
							hs.put("Voltage", "N/A");
							hs.put("Current", "N/A");
							hs.put("Wattage", "N/A");
						}
						bmsData.add(hs);
				 	}
			 	}
				 else {
						LinkedHashMap<String,String> hs = new LinkedHashMap<String,String>();
						hs.put("Result", "BMS not added");
						bmsData.add(hs);
						
					}
				 
				
				 } 
				catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
   				return Response.status(201).entity(bmsData).build();
	}
}


