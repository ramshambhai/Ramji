package in.vnl.EventProcess;
import in.vnl.api.common.ApiCommon;
import in.vnl.api.common.AuditHandler;
import in.vnl.api.common.CMSController;
import in.vnl.api.common.CommonService;
import in.vnl.api.common.Constants;
import in.vnl.api.common.CurrentOperation;
import in.vnl.api.common.CurrentOperationType;
import in.vnl.api.common.HummerCue;
import in.vnl.api.common.OperationCalculations;
import in.vnl.api.common.TRGLController;
import in.vnl.api.common.livescreens.AutoOperationServer;
import in.vnl.api.common.livescreens.AutoStateServer;
import in.vnl.api.common.livescreens.DeviceStatusServer;
import in.vnl.api.common.livescreens.ScanTrackModeServer;
import in.vnl.api.fourg.FourgOperations;
import in.vnl.api.netscan.NetscanJob;
import in.vnl.api.netscan.NetscanOperations;
import in.vnl.api.ptz.PTZ;
import in.vnl.api.threeg.ThreegOperations;
import in.vnl.api.twog.livescreens.TriggerCueServer;
import in.vnl.msgapp.Common;
import in.vnl.msgapp.Operations;
import in.vnl.scheduler.NetscanSchedulerListener;

import java.io.BufferedReader;
import java.io.*;
import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;
//import java.util.Optional;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.print.attribute.DateTimeSyntax;
import javax.print.attribute.standard.PrinterLocation;
import javax.resource.spi.RetryableUnavailableException;
import javax.resource.spi.work.RetryableWorkRejectedException;
import javax.transaction.Transactional;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.glassfish.jersey.client.ClientProperties;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class DBDataService {	
	
	static Logger fileLogger = Logger.getLogger("file");
	PriorityBlockingQueue<EventData> priorityQueue = new PriorityBlockingQueue<EventData>(11, new EventDataComparator());
	String prevTime;
	public static boolean systemManagerUpdatedStatus = true;
	public static boolean isfirsttimeUp=true;
	public static boolean statusScannerRestart=false;
	public static boolean isTRGLTriggeredEvent = false;
	public static boolean isUGSTriggeredEvent = false;
	public static boolean isAutomaticTriggeredEvent = false;
	public static boolean isSchedulerTriggeredEvent = false;
	public static boolean isScanRunningBeforeTracking = false;
	public static boolean scannerManuallyStopped=false;
	public static Map<String, Integer> priority = new HashMap<String, Integer>();
	public static Map<String, String> configParamMap = new HashMap<String, String>();
	
	public static ArrayList<HummerCue> trglFreqList= new ArrayList<HummerCue>();
	
	public static String manualEvent = "manual";
	public static String ugsEvent = "ugs";
	public static String trglEvent = "trgl";
	//public static String schedulerEvent = "scheduler";
	//public static String automaticEvent = "automatic";
	//public static String autoEvent = "auto";
	
	private String wait="";
	public String currentEventName = null;
	public String currentManualEventName = null;
	public EventData currentEventData=null;

	Future future;
	public ExecutorService executorTimerService = null;
	public ExecutorService executorManualService = null;
	public ScheduledExecutorService scheduledExecutorService = null;
	public static boolean isInterrupted = false;
	public static boolean isAllowedToNotify = false;
	public static boolean isOctasicPowerOn = true;
	private static DBDataService dataServiceObj;
	public static double configurdTxPower=0.0;
	public static double configuredFreq=0.0;
	public static boolean stopScanFromTrackPart=false;
	public static boolean IsRestartRequired=false; 
	public static int systemType=-1;
	public static int systemMode=-1;
	public static int holdtime=0;
	public static int gpsNode=-1;
	public static String SystemId="";
	public static int antennaToVehicleDiffAngle=361;
	public static int angleOffset=-1;
	public static int rotatedMode=1;
	
	private DBDataService(){
        
    }
	
	
    /**
     * Create a static method to get instance.
     */
    public static DBDataService getInstance(){
        if(dataServiceObj == null){
        	dataServiceObj = new DBDataService();
        }
        return dataServiceObj;
    }
    
	public String getCurrentEventName() {
		return currentEventName;
	}
	public void setCurrentEventName(String currentEventName) {
		this.currentEventName = currentEventName;
	}
	
	public String getCurrentManualEventName() {
		return currentManualEventName;
	}
	public void setCurrentManualEventName(String currentManualEventName) {
		this.currentManualEventName = currentManualEventName;
	}
	
	public EventData getCurrentEventData() {
		return currentEventData;
	}
	public void setCurrentEventData(EventData currentEventData) {
		this.currentEventData = currentEventData;
	}
	
	public ExecutorService getExecutorTimerService() {
		return executorTimerService;
	}
	public void setExecutorTimerService(ExecutorService executorTimerService) {
		this.executorTimerService=executorTimerService;
	}
	
	public void setExecutorTimerManualService(ExecutorService executorManualService) {
		this.executorManualService=executorManualService;
	}
	
	public ScheduledExecutorService getScheduledExecutorService() {
		return scheduledExecutorService;
	}
	public void setScheduledExecutorService(ScheduledExecutorService scheduledExecutorService) {
		this.scheduledExecutorService = scheduledExecutorService;
	}
	
	public static Map<String, String> getConfigParamMap() {
		return configParamMap;
	}

	public static void setConfigParamMap(Map<String, String> configParamMap) {
		DBDataService.configParamMap = configParamMap;
	}
	
	public void clearProrityQueue(){
		this.priorityQueue.clear();
	}
	
	public static int getSystemType() {
		return systemType;
	}
	public static void setSystemType(int systemType) {
		DBDataService.systemType = systemType;
	}

	
	public static int getSystemMode() {
		return systemMode;
	}
	public static void setSystemMode(int systemMode) {
		DBDataService.systemMode = systemMode;
	}
	
	public static int getGpsNode() {
		return gpsNode;
	}
	public static void setGpsNode(int gpsNode) {
		DBDataService.gpsNode = gpsNode;
	}
	public static String getSystemId() {
		return SystemId;
	}
	public static void setSystemId (String systemid) {
		DBDataService.SystemId = systemid;
	}
	
	public static boolean isOctasicPowerOn() {
		return isOctasicPowerOn;
	}
	
	public static void setOctasicPowerOn(boolean isOctasicPowerOn) {
		DBDataService.isOctasicPowerOn = isOctasicPowerOn;
	}
	
	public static int getAntennaToVehicleDiffAngle() {
		return antennaToVehicleDiffAngle;
	}
	
	public static void setAntennaToVehicleDiffAngle(int antennaToVehicleDiffAngle) {
		DBDataService.antennaToVehicleDiffAngle = antennaToVehicleDiffAngle;
	}
	
	public static int getAngleOffset() {
		return angleOffset;
	}
	public static void setAngleOffset(int angleOffset) {
		DBDataService.angleOffset = angleOffset;
	}
	
	
	public static int getRotatedMode() {
		return rotatedMode;
	}

	public static void setRotatedMode(int rotatedMode) {
		DBDataService.rotatedMode = rotatedMode;
	}

	
	public void updatePriorityMap(){
		fileLogger.info("in updatePriorityMap");
		Operations operations =new Operations();
		JSONArray eventPriorityArray=operations.getJson("select * from event_priority");
		for(int i=0;i<eventPriorityArray.length();i++){
			JSONObject eventPriorityObj;
			try {
				eventPriorityObj = eventPriorityArray.getJSONObject(i);
			priority.put(eventPriorityObj.getString("event_name"),eventPriorityObj.getInt("priority"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		fileLogger.info("out updatePriorityMap");
	}
	
	public void updateConfigParamMap() {
		fileLogger.info("Inside Function : updateConfigParamMap");	
		String absolutePath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		absolutePath = absolutePath.substring(0, absolutePath.lastIndexOf("WEB-INF"));
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(absolutePath + "/resources/config/db.properties"));
		} catch (Exception E) {
			fileLogger.debug("db .properties not found" + E.getMessage());

		}

		// fileLogger.debug(new FileInputStream(absolutePath +
		// "/resources/event.properties"));
		configParamMap.put("user", prop.getProperty("dbuser"));
		configParamMap.put("pass", prop.getProperty("dbpassword"));
		configParamMap.put("db", prop.getProperty("dbname"));
		configParamMap.put("port", prop.getProperty("dbport"));
		configParamMap.put("host", prop.getProperty("dbhost"));
		configParamMap.put("RecadgTime_GPS",prop.getProperty("RecadgTime_GPS"));
		configParamMap.put("nib", prop.getProperty("nib"));
		configParamMap.put("allnibs", prop.getProperty("allnibs"));
		configParamMap.put("mapserver", prop.getProperty("mapserver"));
		configParamMap.put("3gserviceurl", prop.getProperty("3gserviceurl"));
		configParamMap.put("netscanserviceurl", prop.getProperty("netscanserviceurl"));
		configParamMap.put("statusUpdatePeriodicity", prop.getProperty("statusUpdatePeriodicity"));
		configParamMap.put("bmsDataPort", prop.getProperty("bmsDataPort"));
		configParamMap.put("lat", prop.getProperty("lat"));
		configParamMap.put("lon", prop.getProperty("lon"));
		configParamMap.put("udpserverport", prop.getProperty("udpserverport"));
		configParamMap.put("paport", prop.getProperty("paport"));
		configParamMap.put("lnaport", prop.getProperty("lnaport"));
		configParamMap.put("scantime", prop.getProperty("scantime"));
		configParamMap.put("tracktime", prop.getProperty("tracktime"));
		configParamMap.put("backupdir", prop.getProperty("backupdir"));
		configParamMap.put("usedspacelimit", prop.getProperty("usedspacelimit"));
		configParamMap.put("sysmanwait", prop.getProperty("sysmanwait"));
		configParamMap.put("falcontype", prop.getProperty("falcontype"));
		configParamMap.put("ptz_port", prop.getProperty("ptz_port"));
		configParamMap.put("ptz_speed", prop.getProperty("ptz_speed"));
		configParamMap.put("octasicBootUpTime", prop.getProperty("octasicBootUpTime"));
		configParamMap.put("TrackingcontinueuponFailure", prop.getProperty("TrackingcontinueuponFailure"));
		configParamMap.put("NoOfRetriesLockUnlock", prop.getProperty("NoOfRetriesLockUnlock"));
		configParamMap.put("octasic_off_on_time", prop.getProperty("octasic_off_on_time"));
		configParamMap.put("struoffset", prop.getProperty("struoffset"));
		configParamMap.put("ScannerTimetoUp",prop.getProperty("ScannerTimetoUp"));
		configParamMap.put("HoldTime",prop.getProperty("HoldTime2g"));
		configParamMap.put("addition_time",prop.getProperty("addition_time"));
		configParamMap.put("maxAngle",prop.getProperty("maxAngle"));
		configParamMap.put("minAngle",prop.getProperty("minAngle"));
		configParamMap.put("TiltMax",prop.getProperty("TiltMax"));
		configParamMap.put("TiltMin",prop.getProperty("TiltMin"));
		
		
		configParamMap.put("Jammerserviceurl",prop.getProperty("Jammerserviceurl"));
		configParamMap.put("faultIP",prop.getProperty("faultIP"));
		configParamMap.put("faultPort",prop.getProperty("faultPort"));
		configParamMap.put("UIscannerStopStart_restartScanner",prop.getProperty("UIscannerStopStart_restartScanner"));
		//CommonService.NoOfRetriesLockUnlock=Integer.parseInt(prop.getProperty("NoOfRetriesLockUnlock"));
		//CommonService.octasicPowerOffToOnTime=Integer.parseInt(prop.getProperty("octasic_off_on_time"));
		ThreegOperations.NoOfRetriesLockUnlock=Integer.parseInt(prop.getProperty("NoOfRetriesLockUnlock"));
		//ThreegOperations.octasicPowerOffToOnTime=Integer.parseInt(prop.getProperty("octasic_off_on_time"));
		FourgOperations.NoOfRetriesLockUnlock=Integer.parseInt(prop.getProperty("NoOfRetriesLockUnlock"));
		//FourgOperations.octasicPowerOffToOnTime=Integer.parseInt(prop.getProperty("octasic_off_on_time"));
		update_pow_compensation();
		update_power_setting();
	}
	public void update_pow_compensation(){
		String absolutePath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		absolutePath = absolutePath.substring(0, absolutePath.lastIndexOf("WEB-INF"));
		absolutePath+="resources/config/pow_compensation.csv";
		   
		//String csvFile = absolutePath;
		 
		   String csvFile = System.getenv("OMC_DASHBOARD")+"/pow_compensation.csv";
		    csvFile="F:\\SignalJammerFalcon\\Installer\\common_files\\pow_compensation.csv";
		   fileLogger.info("update_pow_compensation Path = "+csvFile);
	       String line = "";
	       String cvsSplitBy = ",";
	       Common co = new Common();	
	       try {
	       co.executeDLOperation("delete from pow_compensation; ");
	       BufferedReader br = new BufferedReader(new FileReader(csvFile));
	    	   br.readLine();
	    	   
	           while ((line = br.readLine()) != null) {

	        	   String[] data = line.split(cvsSplitBy);
	        	  try {
	        	   co.executeDLOperation("insert into pow_compensation(tech,band,compensation,comp_single_no_rffe,comp_single_rffe,ant_type,system_id) values('"+data[0]+"' , '"+data[1]+"' ,"+data[3]+" ,"+data[2]+" ,"+data[3]+" ,"+data[4]+" ,'"+data[5]+"')" );                                                                                                                 
	        	  }
	        	  catch (Exception e) {
	   	           e.printStackTrace();
	   	      	}
	               
	           }
			} catch (Exception e) {

	           e.printStackTrace();

	       }
	}
	
	
	
	public void update_power_setting(){
		String absolutePath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		absolutePath = absolutePath.substring(0, absolutePath.lastIndexOf("WEB-INF"));
		absolutePath+="resources/config/power_setting.csv";
			//String csvFile = absolutePath;
			String csvFile =System.getenv("OMC_DASHBOARD")+"/power_setting.csv";
			csvFile="F:\\SignalJammerFalcon\\Installer\\common_files\\power_setting.csv";
			fileLogger.info("update_power_setting Path = "+csvFile);
	       String line = "";
	       String cvsSplitBy = ",";
	       Common co = new Common();
	       try {
	       co.executeDLOperation("delete from power_setting; ");
	       BufferedReader br = new BufferedReader(new FileReader(csvFile));
	    	   br.readLine();
	    	   
	           while ((line = br.readLine()) != null) {

	        	   String[] data = line.split(cvsSplitBy);
	        	  try {
	        		String query="insert into power_setting(tech,band,subband_start,subband_stop,power_setting,l1_attn,system_id) values('"  +data[0]+"' , '"+data[1]+"' ,"+data[2]+" ,"+data[3]+" ,'"+data[4]+"' ,'"+data[5]+"'  , '" +data[6]+"')" ;
	        	   co.executeDLOperation(query);                                                                                                                 
	        	  }
	        	  catch (Exception e) {
	   	           e.printStackTrace();
	   	      	}
	               
	           }
			} catch (Exception e) {

	           e.printStackTrace();

	       }
	}
	
	
	
	public void updateSystemType(){
		fileLogger.info("Inside Function : updateSystemType");	
		//fileLogger.debug("in updateSystemType");
		try {
			int switchCode=new Operations().getJson("select code from system_properties where key='system_type'").getJSONObject(0).getInt("code");
            setSystemType(switchCode);
		} catch (JSONException e) {
			fileLogger.error("exception in updateSwitchUsability");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fileLogger.info("Exit Function : updateSystemType");	
	}

	public void updateAntennaMode(){
		fileLogger.info("Inside Function : updateSystemMode");	
		//fileLogger.debug("in updateSystemMode");
		try {
			int antennaMode=new Operations().getJson("select antenna_type from antenna where id =1").getJSONObject(0).getInt("antenna_type");
			
			setRotatedMode(antennaMode);
			
		} catch (JSONException e) {
			fileLogger.error("exception in updateSwitchUsability");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fileLogger.info("Exit Function : updateSystemMode");	
	}
	
	public void updateSystemMode(){
		fileLogger.info("Inside Function : updateSystemMode");	
		//fileLogger.debug("in updateSystemMode");
		try {
			int systemMode=new Operations().getJson("select code from system_properties where key='system_mode'").getJSONObject(0).getInt("code");
				setSystemMode(systemMode);
		} catch (JSONException e) {
			fileLogger.error("exception in updateSwitchUsability");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fileLogger.info("Exit Function : updateSystemMode");	
	}
	
	public void updateGpsNode(){
		fileLogger.info("Inside Function : updateGpsNode");	
		//fileLogger.debug("in updateGpsNode");
		try {
			int gpsNode=new Operations().getJson("select code from system_properties where key='gps_node'").getJSONObject(0).getInt("code");
				setGpsNode(gpsNode);
		} catch (JSONException e) {
			fileLogger.error("exception in updateSwitchUsability");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fileLogger.info("Exit Function : updateGpsNode");	
	}
	
	public void updateSystem_id(){
		fileLogger.info("Inside Function : updateSystem_id");	
		try {
			String system_id=new Operations().getJson("select system_id from btsmaster where devicetypeid=14").getJSONObject(0).getString("system_id");
			if(system_id!=null&&system_id!="")
				setSystemId(system_id);
			else
				setSystemId("Default");
			
		} catch (JSONException e) {
			fileLogger.error("exception in updateSystem_id");
			e.printStackTrace();
		}
		fileLogger.info("Exit Function : updateGpsNode");	
	}
	
	public void updateAngleOffset(){
		fileLogger.info("Inside Function : updateAngleOffset");	
		//fileLogger.debug("in updateGpsNode");
		try {
			int angleOffset=new Operations().getJson("select angle_offset from antenna where atype='1' limit 1").getJSONObject(0).getInt("angle_offset");
				setAngleOffset(angleOffset);
		} catch (JSONException e) {
			fileLogger.error("exception in updateSwitchUsability");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fileLogger.info("Exit Function : updateAngleOffset");	
	}
	
	public void executeQueue() {
		fileLogger.info("Inside Function : executeQueue");	
		fileLogger.debug("in executeQueue");
		fileLogger.debug("priority is :"+priority);
		fileLogger.debug("priority queue size :"+priorityQueue);
		try {
			EventData eventData;
			boolean valid=false;
			Common common = new Common();
			while((eventData=priorityQueue.take())!=null) {
				fileLogger.debug("currentEventName is :"+currentEventName+" and eventData.getEventName() is :"+eventData.getEventName());
				String manualOverrideStatus="f";
				int systemTypeCode=getSystemType();
				boolean canOperationRun=true;
				if(systemTypeCode==2){
				try {
					manualOverrideStatus = new Operations().getJson("select status from manual_override").getJSONObject(0).getString("status");
				} catch (JSONException e2) {
					// TODO Auto-generated catch block
					fileLogger.debug(e2.getStackTrace().toString());
					//e2.printStackTrace();
				}
				if((manualOverrideStatus.equalsIgnoreCase("t") || manualOverrideStatus.equalsIgnoreCase("true"))){
					canOperationRun=false;
				}
				}
				if(canOperationRun){
					try {
						String scannerDetQuery = "select * from view_btsinfo where code=3";
						JSONObject scannerDetJsonObject = new Operations().getJson(scannerDetQuery).getJSONObject(0);
						new ApiCommon().setGpsDataRequest(scannerDetJsonObject.getString("code"),scannerDetJsonObject.getString("sytemid"),scannerDetJsonObject.getString("ip"));
					} catch (Exception e2) {
						fileLogger.error("@automatic exception occurs message:"+e2.getMessage());
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					currentEventName = eventData.getEventName();
				valid = false;
				if(currentEventName.equals(Constants.automaticEvent)) {
					fileLogger.debug("in "+Constants.automaticEvent);
					int countOpr=-1;
					/*try {
						countOpr = new Operations().getJson("select count(*) count from oprrationdata where status ='0' and name!='"+eventData.getOprName()+"'").getJSONObject(0).getInt("count");
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}*/
					countOpr=0;
					if(countOpr==0){ 
					try {
						setCurrentEventData(eventData);
						valid = true;
						DBDataService.isAutomaticTriggeredEvent = true;
						//CurrentOperationType.trans_id=eventData.getTransId();
						CurrentOperationType.trigger=true;
                        //new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Processing Automatic Event\"}");
						startAutomaticOperation(eventData.getEventName(),eventData.getNote(),eventData.getOprType(),eventData.getOperators(),eventData.getCoverageDistance(),eventData.getDeviceIp(),eventData.getFrequency(),eventData.getLatitude(),eventData.getLongitude(),eventData.getPeriodicity(),eventData.getTimeout(),eventData.getValidity(),eventData.getDate(),eventData.getCueId());
                        //sendTriggerToCMS();
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}else{
						
					}
					
					fileLogger.debug("out "+ Constants.automaticEvent);
				}else if(currentEventName.equals(Constants.schedulerEvent)) {
					//check validity for event
					fileLogger.debug("in schedulerEvent");
					String query="select count(*) count from oprrationdata where status ='0' and name='"+eventData.getOprName()+"'";
					int count=-1;
					try {
						count = new Operations().getJson(query).getJSONObject(0).getInt("count");
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if(count==0){
						setCurrentEventData(eventData);
						valid = true;
						DBDataService.isSchedulerTriggeredEvent = true;
						//CurrentOperationType.trans_id=eventData.getTransId();
						CurrentOperationType.trigger=true;
						//new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Processing Scheduler Event\"}");
						startSchedulerOperation(eventData.getEventName(),eventData.getNote(),eventData.getOprType(),eventData.getOperators(),eventData.getCoverageDistance(),eventData.getDeviceIp(),eventData.getFrequency(),eventData.getLatitude(),eventData.getLongitude(),eventData.getPeriodicity(),eventData.getTimeout(),eventData.getValidity(),eventData.getDate(),eventData.getCueId());
						//sendTriggerToCMS();
					}else{
						
					}
					fileLogger.debug("out schedulerEvent");
				}else if(currentEventName.equals(ugsEvent)) {
					fileLogger.debug("in ugsEvent");
					//check validity for event
					Date date = new Date();
					long duration  = date.getTime() - eventData.getDate().getTime();
					long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
					if(eventData.getValidity()>diffInMinutes){
						setCurrentEventData(eventData);
						
						DBDataService.isUGSTriggeredEvent = true;
/*						CurrentOperationType.trans_id=eventData.getTransId();
						CurrentOperationType.trigger=true;*/
						//new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Processing Oxfam Event\"}");
						valid = true;
						startUgsOperation(eventData.getEventName(),eventData.getNote(),eventData.getOprType(),eventData.getOperators(),eventData.getCoverageDistance(),eventData.getDeviceIp(),eventData.getFrequency(),eventData.getLatitude(),eventData.getLongitude(),eventData.getPeriodicity(),eventData.getTimeout(),eventData.getValidity(),eventData.getDate(),eventData.getSector(),eventData.getCueId());
						//sendTriggerToCMS();
						
					}else{
						storeTriggerOnDb(date,eventData.getCueId(),"Oxfam","Discarded(validity expired),Cue ID:"+eventData.getCueId()+",Coordinates(Lat:"+eventData.getLatitude()+",Lon:"+eventData.getLongitude()+"),Sector:"+eventData.getSector()+",Validity:"+eventData.getValidity()+",Timeout:"+eventData.getTimeout());
						new TriggerCueServer().sendText("event");
						LinkedHashMap<String,String> queLog = new LinkedHashMap<String,String>();					   		
						queLog.put("action", "Discarded(validity expired)");
			   			queLog.put("source","Oxfam");
						queLog.put("trans id",eventData.getTransId());
						queLog.put("cue id",eventData.getCueId());
						new AuditHandler().audit_que(queLog);
					}
					fileLogger.debug("out ugsEvent");
				}
				else if(currentEventName.equals(trglEvent)) {
					fileLogger.debug("in trglEvent");
					//check validity for event
					Date date = new Date();
					long duration  = date.getTime() - eventData.getDate().getTime();
					long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
					for(int cueIndex=0;cueIndex<trglFreqList.size();cueIndex++){
						HummerCue hummerCue = trglFreqList.get(cueIndex);
						if(hummerCue.getFrequency()==eventData.getFrequency() && hummerCue.getBandwidth()==eventData.getBandwidth() && hummerCue.getSector().equalsIgnoreCase(eventData.getSector())){
							trglFreqList.remove(hummerCue);
							hummerCue=null;
						}
					}
					if(eventData.getValidity()>diffInMinutes){
						setCurrentEventData(eventData);
						valid = true;
						DBDataService.isTRGLTriggeredEvent = true;
/*						CurrentOperationType.trans_id=eventData.getTransId();
						CurrentOperationType.trigger=true;*/
						//new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Processing Hummer Event\"}");
						startTrglOperation(eventData.getEventName(),eventData.getNote(),eventData.getOprType(),eventData.getOperators(),eventData.getCoverageDistance(),eventData.getDeviceIp(),eventData.getFrequency(),eventData.getLatitude(),eventData.getLongitude(),eventData.getPeriodicity(),eventData.getTimeout(),eventData.getValidity(),eventData.getDate(),eventData.getAngle(),eventData.getBandwidth(),eventData.getSector(),eventData.getCueId());
						//sendTriggerToCMS();
					}else{
						storeTriggerOnDb(date,eventData.getCueId(),"Hummer","Discarded(validity expired),Cue ID:"+eventData.getCueId()+",Frequency:"+eventData.getFrequency()+",Bandwidth:"+eventData.getBandwidth()+",Sector:"+eventData.getSector()+",Validity:"+eventData.getValidity()+",Timeout:"+eventData.getTimeout());
						new TriggerCueServer().sendText("event");
						LinkedHashMap<String,String> queLog = new LinkedHashMap<String,String>();	
						queLog.put("action", "Discarded(validity expired)");
			   			queLog.put("source","Hummer");
						queLog.put("trans id",eventData.getTransId());
						queLog.put("cue id",eventData.getCueId());
						new AuditHandler().audit_que(queLog);
					}
					fileLogger.debug("out trglEvent");
				}
				if(valid){
				synchronized(wait) {
		            try {
						wait.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
		        }
				}
			}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		fileLogger.info("Exit Function : executeQueue");	
		//fileLogger.debug("out executeQueue");
	}
	public void storeTriggerOnDb(Date date,String cueId,String source,String detail){
		Common common = new Common();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String timeZone = "IST";
		sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
		String dateStr=sdf.format(date);
		//common.executeDLOperation("update trigger_cue set trigger_status='n' where trigger_id=(select max(trigger_id) from trigger_cue)");
		String query="insert into trigger_cue(arrival_date,trigger_source,trigger_type,trigger_status,detail,cue_id) " +
				"values('"+dateStr+"','"+source+"','Cue','p','"+detail+"','"+cueId+"')";
		fileLogger.debug("StoreTriggeronDB query = "+query);
		common.executeDLOperation(query);
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String timeZone = "IST";
//		sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
//		String dateStr=sdf.format(date);
		new CMSController().sendCueToCMS(dateStr,source,detail,cueId);
	}
	
	public void stopCueOperation() throws UnknownHostException, IOException
	{
		     fileLogger.info("Inside Function : stopCueOperation");	
				try{
			//fileLogger.debug("In stopCueOperation");
			if(currentEventName!=null && (currentEventName.equals("ugs") || currentEventName.equals("trgl"))){
				fileLogger.debug("@cue stop cue operation for eventName :"+currentEventName);
				DBDataService.isTRGLTriggeredEvent = false;
				DBDataService.isUGSTriggeredEvent = false;
				DBDataService.isAutomaticTriggeredEvent = false;
				DBDataService.isSchedulerTriggeredEvent = false;
				currentEventName = null;
				shutdownOtherOperation();
			}
		//shutdownOtherOperation();
		   final HashMap<String,ArrayList<HashMap<String,String>>> devicesMapOverTech = new Operations().getAllBtsInfoByTech(); 
		   new ApiCommon().lockUnlockAllDevices(devicesMapOverTech,1);
		  // new CommonService().updateStatusOfGivenBts("all");
		   //new DeviceStatusServer().sendText("all");
		   	new DeviceStatusServer().sendText("ok");
			//NetscanOperations netscanOperations=new NetscanOperations();
			//LinkedHashMap<String,String> param = new LinkedHashMap<String,String>();
/*			JSONObject netscanData=null;
			try{
			netscanData =  new Operations().getJson("select * from view_btsinfo where code=3").getJSONObject(0);
			}catch(Exception E){
				
			}*/


/*		try {
			fileLogger.debug("DBDataService: Waiting for Lock Unlock Time");
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fileLogger.debug("In sleep for 5 seconds");*/
/*		DBDataService.isInterrupted=true;
		synchronized(wait) {
			wait.notify();
        }*/

		}finally{
			//DBDataService.isInterrupted=true;
			fileLogger.debug("DBDataService.isAutomaticTriggeredEvent is :"+DBDataService.isAutomaticTriggeredEvent);
			fileLogger.debug("out stopRunningOperation");
/*			synchronized(wait) {
				wait.notify();
	        }*/
		}
			     fileLogger.info("Exit Function : stopCueOperation");	
	}
	
	//For adding Manual events and auto events and blacklist events
	
	public void addToEventPriorityQueue(String oprName, String eventName, String note, String oprType, String operators, int coverageDistance,String deviceIp,double frequency,double latitude,double longitude,long periodicity, int timeout, long validity, Date date, boolean isNew,String transId,int angle,double bandwidth,String sector,String cueId) {
	     fileLogger.info("Inside Function : addToEventPriorityQueue");	
		fileLogger.debug("In addToEventPriorityQueue");
		fileLogger.debug("automaticEvent is :"+Constants.automaticEvent+" and eventName is :"+eventName);
		if(eventName.equalsIgnoreCase(Constants.automaticEvent)) {
			fileLogger.debug("in automaticEvent add");
			
			try {
				fileLogger.debug("@priority is :"+priority.toString());
				priorityQueue.put(new EventData(oprName,priority.get(eventName), eventName, note, oprType, operators, coverageDistance, deviceIp, frequency, latitude,longitude,periodicity,timeout,validity,date,isNew,transId,angle,bandwidth,sector,cueId));
			    fileLogger.debug("eventName is :"+eventName);
				fileLogger.debug("priority of eventName is :"+priority.get(eventName));
				fileLogger.debug("currentEventName is :"+currentEventName);
				if(currentEventName!=null && priority.get(eventName)<priority.get(currentEventName)) {
					try {
						fileLogger.debug("about to stop automtic operation immediately after some incoming operation");
						stopRunningOperation();
						Thread.sleep(2000);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						fileLogger.error("exception on automatic addToEventPriorityQueue :"+e.getMessage());
						e.printStackTrace();
					}
				}
 			} catch (Exception e1) {
				fileLogger.error("exception in addToEventPriorityQueue message:"+e1.getMessage());
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//priorityQueue.size()
		}
		else if(eventName.equalsIgnoreCase(Constants.schedulerEvent)) {
			fileLogger.debug("in schedulerEvent add");
			fileLogger.debug("priority is :"+priority.toString());
			try {
				priorityQueue.put(new EventData(oprName,priority.get(eventName), eventName, note, oprType, operators, coverageDistance, deviceIp, frequency, latitude,longitude,periodicity,timeout,validity,date,isNew,transId,angle,bandwidth,sector,cueId));
				if(currentEventName!=null && priority.get(eventName)<priority.get(currentEventName)){
					try {
						stopRunningOperation();
						Thread.sleep(2000);
					} catch (Exception e) {
						fileLogger.error("exception on scheduler addToEventPriorityQueue :"+e.getMessage());
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				fileLogger.error("in add error");
				e1.printStackTrace();
				fileLogger.error("out add error");
			}
			fileLogger.debug("currentEventName is :"+currentEventName);
		}
		else if(eventName.equalsIgnoreCase(ugsEvent)){
			fileLogger.debug("in ugsEvent add");
			try {
				priorityQueue.put(new EventData(oprName,priority.get(eventName), eventName, note, oprType, operators, coverageDistance, deviceIp, frequency, latitude,longitude,periodicity,timeout,validity,date,isNew,transId,angle,bandwidth,sector,cueId));
				int schedulerCount=0;
				EventData evntData = null;
				if(currentEventName!=null && priority.get(eventName)<priority.get(currentEventName)) {
					try {
						if(currentEventName.equalsIgnoreCase("scheduler")){
							schedulerCount++;
							evntData=DBDataService.getInstance().getCurrentEventData();
						}
						stopRunningOperation();
						Thread.sleep(2000);
					} catch (Exception e) {
						fileLogger.error("exception on oxfam addToEventPriorityQueue :"+e.getMessage());
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(schedulerCount!=0){
					priorityQueue.put(evntData);
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else if(eventName.equalsIgnoreCase(trglEvent)) {
			fileLogger.debug("in trglEvent add");
			boolean freqAvailable = false;
			try {
				Date currTime = new Date();
				for(int cueList=0;cueList<trglFreqList.size();cueList++){
					HummerCue hummerCue=trglFreqList.get(cueList);
					String tempSector = hummerCue.getSector();
					if(tempSector.equalsIgnoreCase(sector)){
						long elapseTime  = currTime.getTime() - hummerCue.getDate().getTime();
						long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(elapseTime);
						if(hummerCue.getValidity()>diffInMinutes){
						
						double tempFreq=hummerCue.getFrequency();
						double tempBandwidth=hummerCue.getBandwidth()/2;
						double maxFreq=tempFreq+tempBandwidth;
						double minFreq=tempFreq-tempBandwidth;
/*sirvaib						
						double minActFrq=frequency-bandwidth;
						double maxActFrq=frequency+bandwidth;
						if(minActFrq>=minFreq && maxActFrq<=maxFreq ){
						freqAvailable = true;
						break;

						}
sirvaib*/
						
						
						if(frequency>=minFreq && frequency<=maxFreq ){
							freqAvailable = true;
							break;
						}
					}
					}
						
				}
				
				if(!freqAvailable){
					if(currentEventData!=null && currentEventData.getEventName().equalsIgnoreCase("trgl")){
						String currSector = currentEventData.getSector();
						if(currSector.equalsIgnoreCase(sector)){
							double currFreq = currentEventData.getFrequency();
							double currBandwidth = currentEventData.getBandwidth()/2;
							double maxFreq=currFreq+currBandwidth;
							double minFreq=currFreq-currBandwidth;
							
//sirvaib							double minActFrq=frequency-bandwidth;
//sirvaib							double maxActFrq=frequency+bandwidth;
/*sirvaib							if(minActFrq>=minFreq && maxActFrq<=maxFreq ){
													
								freqAvailable = true;
							}
*sirvaib*/
					

							if(frequency>=minFreq && frequency<=maxFreq ){
								freqAvailable = true;
							}
						}
					}
				}
			
				int schCount=0;
				EventData evData = null;
				//freqAvailable=false;
				if(!freqAvailable){
					trglFreqList.add(new HummerCue(frequency,bandwidth,sector,date,validity));
					priorityQueue.put(new EventData(oprName,priority.get(eventName), eventName, note, oprType, operators, coverageDistance, deviceIp, frequency, latitude,longitude,periodicity,timeout,validity,date,isNew,transId,angle,bandwidth,sector,cueId));
					if(currentEventName!=null && priority.get(eventName)<priority.get(currentEventName)) {
						try {
							if(currentEventName.equalsIgnoreCase("scheduler")){
								schCount++;
								evData=DBDataService.getInstance().getCurrentEventData();
							}
							stopRunningOperation();
							Thread.sleep(2000);
						} catch (Exception e) {
							fileLogger.error("exception on hummer addToEventPriorityQueue :"+e.getMessage());
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				//stopRunningOperation();
				if(schCount!=0){
					priorityQueue.put(evData);
				}
				}else{
					storeTriggerOnDb(date,cueId,"Hummer","Discarded(frequency already received),Cue ID:"+cueId+",Frequency:"+frequency+",Bandwidth:"+bandwidth+",Sector:"+sector+",Validity:"+validity+",Timeout:"+timeout);
					new TriggerCueServer().sendText("event");
					fileLogger.debug("@note frequency coming for trgl exists !no addition to priority queue at :"+new Date());
					fileLogger.debug("@trglfreq is :"+trglFreqList);
				}
				} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//shutdownTimer();
		}
		fileLogger.debug("priorityQueue size :"+priorityQueue.size());
		//fileLogger.debug("out addToEventPriorityQueue");
		   fileLogger.info("Exit Function : addToEventPriorityQueue");	
	}

	public void startAutomaticOperation(String eventName,String note,String oprType,String operators,int coverageDistance,String deviceIp,double frequency,double latitude,double longitude,long periodicity,int timeout,long validity,Date date,String cueId){
		   fileLogger.info("Inside Function : startAutomaticOperation");	
		ArrayList<String> plmns = null;
		
		//CurrentOperationType.generateCueId();
		new CurrentOperationType().setCueId(cueId);
		storeTriggerOnDb(date,cueId,"Falcon","Processing  Continous Mode transmission");
        new TriggerCueServer().sendText("event");
        new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Processing Continous Mode Event\"}");
		//String cueId=CurrentOperationType.getCueId();
		LinkedHashMap<String,String> queLog = new LinkedHashMap<String,String>();					   		
		queLog.put("action", "Processing " + Constants.automaticEvent +" Transmission");
		queLog.put("id",cueId);
		new AuditHandler().auditOprAction(queLog);
        
		if(operators!=null){
        	plmns=new ArrayList<String>(Arrays.asList(operators.split(",")));
        }else{
        	
        }
		final ArrayList<String> givenPLMNs = plmns;
		ExecutorService executorTimerService = Executors.newSingleThreadExecutor();
        setExecutorTimerService(executorTimerService);
        //DBDataService.isInterrupted = false;
		future = executorTimerService.submit(new Runnable() {
		    public void run() {
		        try {
		        	DBDataService.isInterrupted = false;
		        	Common common = new Common();
		        	String response = "{}";
		        	fileLogger.debug("@"+Constants.automaticEvent+"opr about while in startAutomaticOperation");
		        	
		        	try {
						int systemType=getSystemType();
						int systemMode=getSystemMode();
						int gpsNode=getGpsNode();
						
						if(gpsNode==0){
							if(systemMode==0){
								if(systemType==2){
									//take from hummer static lat,long and offset  ,integrated,static,stru
									//new TRGLController().sendOffsetRequestToHummer();
								}else{
									//take from stru static lat,long and offset  ,standlone,static,stru
/*									try {
										JSONArray ptzDataArray = new Operations().getJson("select * from view_btsinfo where code = 9");
										JSONObject hummerDataObject = ptzDataArray.getJSONObject(0);
										new PTZ().sendGPSCommand(hummerDataObject.getString("ip"),Integer.parseInt(DBDataService.getConfigParamMap().get("ptz_port")), false, false);
									} catch (UnknownHostException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}catch(JSONException e){
										
									}*/
								}
							}else{
								if(systemType==2){
									//take from hummer continuous lat,long and offset  ,integrated,moving,stru
								}else{
									//take from stru continuous lat,long and offset  ,standlone,moving,stru
/*									try {
										JSONArray ptzDataArray = new Operations().getJson("select * from view_btsinfo where code = 9");
										JSONObject hummerDataObject = ptzDataArray.getJSONObject(0);
										new PTZ().sendGPSCommand(hummerDataObject.getString("ip"),Integer.parseInt(DBDataService.getConfigParamMap().get("ptz_port")), true, true);
									} catch (UnknownHostException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}catch(JSONException e){
										
									}*/
								}
							}
						}else if(gpsNode==1){
							if(systemMode==0){
								if(systemType==2){
									//take from hummer static lat,long and offset  ,integrated,static,stru
									//new TRGLController().sendOffsetRequestToHummer();
								}else{
									//take from stru static lat,long and offset  ,standlone,static,stru
									try {
										JSONArray ptzDataArray = new Operations().getJson("select * from view_btsinfo where code = 9");
										JSONObject hummerDataObject = ptzDataArray.getJSONObject(0);
										new PTZ().sendGPSCommand(hummerDataObject.getString("ip"),Integer.parseInt(DBDataService.getConfigParamMap().get("ptz_port")), true, false);
										new PTZ().sendGPSCommand(hummerDataObject.getString("ip"),Integer.parseInt(DBDataService.getConfigParamMap().get("ptz_port")), false, false);
									} catch (UnknownHostException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}catch(JSONException e){
										
									}
								}
							}else{
								if(systemType==2){
									//take from hummer continuous lat,long and offset  ,integrated,moving,stru
								}else{
									//take from stru continuous lat,long and offset  ,standlone,moving,stru
									try {
										JSONArray ptzDataArray = new Operations().getJson("select * from view_btsinfo where code = 9");
										JSONObject hummerDataObject = ptzDataArray.getJSONObject(0);
										new PTZ().sendGPSCommand(hummerDataObject.getString("ip"),Integer.parseInt(DBDataService.getConfigParamMap().get("ptz_port")), true, true);
									} catch (UnknownHostException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}catch(JSONException e){
										
									}
								}
							}
						}else{
							if(systemMode==0){
								if(systemType==2){
									//take from hummer static lat,long and offset  ,integrated,static,stru
									new TRGLController().sendOffsetRequestToHummer();
								}else{
									//take from stru static lat,long and offset  ,standlone,static,stru
/*									try {
										JSONArray ptzDataArray = new Operations().getJson("select * from view_btsinfo where code = 9");
										JSONObject hummerDataObject = ptzDataArray.getJSONObject(0);
										new PTZ().sendGPSCommand(hummerDataObject.getString("ip"),Integer.parseInt(DBDataService.getConfigParamMap().get("ptz_port")), false, false);
									} catch (UnknownHostException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}catch(JSONException e){
										
									}*/
								}
							}else{
								if(systemType==2){
									//take from hummer continuous lat,long and offset  ,integrated,moving,stru
								}else{
									//take from stru continuous lat,long and offset  ,standlone,moving,stru
/*									try {
										JSONArray ptzDataArray = new Operations().getJson("select * from view_btsinfo where code = 9");
										JSONObject hummerDataObject = ptzDataArray.getJSONObject(0);
										new PTZ().sendGPSCommand(hummerDataObject.getString("ip"),Integer.parseInt(DBDataService.getConfigParamMap().get("ptz_port")), true, true);
									} catch (UnknownHostException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}catch(JSONException e){
										
									}*/
								}
							}
						}
					} catch (Exception e1) {
						fileLogger.error("exception in sending gps request in "+Constants.automaticEvent);
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		        	
		        	
		        	while(true)
		        	{
		        	fileLogger.debug("@"+Constants.automaticEvent +"opr at:"+new Date());
		        	if(Thread.currentThread().isInterrupted() || DBDataService.isInterrupted){
		        		DBDataService.isInterrupted = true;
		        		break;
		        	} 
		        	
					//boolean availability=checkOldScannerDataOrRunNew();
					//fileLogger.debug("@automaticopr availability:"+availability);
					//if(availability){
					  try {
				        	if(Thread.currentThread().isInterrupted() || DBDataService.isInterrupted){
				        		DBDataService.isInterrupted = true;
				        		break;
				        	}
				        	
						response=goToCommonTrackingStarter(givenPLMNs,null,null,null,-1,-1,-1,Constants.automaticEvent);
		  				//String response=new ApiCommon().startTracking(givenPLMNs);
		  				//JSONObject responseJson=new JSONObject(response);
						new ScanTrackModeServer().sendText("track&Idle");
/*						JSONArray hummerDataArray = new Operations().getJson("select * from view_btsinfo where code = 12");
						if(hummerDataArray.length()>0){
							new TRGLController().sendTrackingSectorToHummer("-1");
						}
*/
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					  JSONObject responseJson=null;
					  try {
	    					responseJson = new JSONObject(response);
	    				} catch (JSONException e) {
	    					// TODO Auto-generated catch block
	    					e.printStackTrace();
	    				}
					//}
					

/*    				try {
    					if(responseJson.getString("result").equals("success")){
    						common.executeDLOperation("update oprrationdata set status='0',stoptime=timezone('utc'::text, now()) where id=(select max(id) from oprrationdata)");
    						new AutoOperationServer().sendText(response);
    						new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Operation Completed\"}");
    					}else{
    						common.executeDLOperation("update oprrationdata set status='2',stoptime=timezone('utc'::text, now()) where id=(select max(id) from oprrationdata)");
    						new AutoOperationServer().sendText(response);
    						new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Operation Interrupted\"}");
    					}
    				} catch (JSONException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}*/
		        	}
		        	
		        }catch(Exception E){
		        	
		        }finally{
					//sanjaybharmoria currentEventName = null;
		        	stopScanFromTrackPart=false;
					synchronized(wait) {
						wait.notify();
			        }
				}
		    }});
		   fileLogger.info("Exit Function : startAutomaticOperation");
	}
	
	public void startSchedulerOperation(String eventName,String note,String oprType,String operators,int coverageDistance,String deviceIp,double frequency,double latitude,double longitude,long periodicity,int timeout,long validity,Date date,String cueId){
		   fileLogger.info("Inside Function : startSchedulerOperation");
		//fileLogger.debug("In method startSchedulerOperation");
		//CurrentOperationType.generateCueId();
		new CurrentOperationType().setCueId(cueId);
		storeTriggerOnDb(date,cueId,"Falcon","Processing Scheduled Transmission");
		new TriggerCueServer().sendText("event");
		new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Processing Scheduler Event\"}");
		
		LinkedHashMap<String,String> queLog = new LinkedHashMap<String,String>();					   		
		queLog.put("action", "Processing Scheduled Transmission");
		queLog.put("id",cueId);
		new AuditHandler().auditOprAction(queLog);
		
		ArrayList<String> plmns = null; 
		if(operators!=null){
        	plmns=new ArrayList<String>(Arrays.asList(operators.split(",")));
        }else{
        	
        }
		//new CurrentOperationType().genrateTransId();
		CurrentOperationType.getCueId();
		final ArrayList<String> givenPLMNs = plmns;
		fileLogger.debug("periodicity is :"+periodicity);
        final long trackPeriodicity = periodicity;
        ScheduledExecutorService scheduledExecutorService =   Executors.newScheduledThreadPool(1);
        setScheduledExecutorService(scheduledExecutorService);        
        //DBDataService.isInterrupted = false;
        scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
		    public void run() {
		    	try{
		    	DBDataService.isInterrupted = false;
		    	fileLogger.debug("in run of scheduleAtFixedRate of startSchedulerOperation");
		    	
	        	try {
					int systemType=getSystemType();
					int systemMode=getSystemMode();
					int gpsNode=getGpsNode();
					fileLogger.debug("@sendtotrgl systemType is :"+systemType+ " :systemMode :"+systemMode+" :gpsNode :"+gpsNode);
					if(gpsNode==0){
						if(systemMode==0){
							if(systemType==2){
								//take from hummer static lat,long and offset  ,integrated,static,stru
								//fileLogger.debug("@sendtotrgl about to send");
								//new TRGLController().sendOffsetRequestToHummer();
							}else{
								//take from stru static lat,long and offset  ,standlone,static,stru
/*								try {
									JSONArray ptzDataArray = new Operations().getJson("select * from view_btsinfo where code = 9");
									JSONObject hummerDataObject = ptzDataArray.getJSONObject(0);
									new PTZ().sendGPSCommand(hummerDataObject.getString("ip"),Integer.parseInt(DBDataService.getConfigParamMap().get("ptz_port")), true, false);
									new PTZ().sendGPSCommand(hummerDataObject.getString("ip"),Integer.parseInt(DBDataService.getConfigParamMap().get("ptz_port")), false, false);
								} catch (UnknownHostException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}catch(JSONException e){
									
								}*/
							}
						}else{
							if(systemType==2){
								//take from hummer continuous lat,long and offset  ,integrated,moving,stru
							}else{
								//take from stru continuous lat,long and offset  ,standlone,moving,stru
/*								try {
									JSONArray ptzDataArray = new Operations().getJson("select * from view_btsinfo where code = 9");
									JSONObject hummerDataObject = ptzDataArray.getJSONObject(0);
									new PTZ().sendGPSCommand(hummerDataObject.getString("ip"),Integer.parseInt(DBDataService.getConfigParamMap().get("ptz_port")), true, true);
								} catch (UnknownHostException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}catch(JSONException e){
									
								}*/
							}
						}
					}else if(gpsNode==1){
						if(systemMode==0){
							if(systemType==2){
								//take from hummer static lat,long and offset  ,integrated,static,stru
								//new TRGLController().sendOffsetRequestToHummer();
							}else{
								//take from stru static lat,long and offset  ,standlone,static,stru
								try {
									JSONArray ptzDataArray = new Operations().getJson("select * from view_btsinfo where code = 9");
									JSONObject hummerDataObject = ptzDataArray.getJSONObject(0);
									new PTZ().sendGPSCommand(hummerDataObject.getString("ip"),Integer.parseInt(DBDataService.getConfigParamMap().get("ptz_port")), true, false);
									new PTZ().sendGPSCommand(hummerDataObject.getString("ip"),Integer.parseInt(DBDataService.getConfigParamMap().get("ptz_port")), false, false);
								} catch (UnknownHostException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}catch(JSONException e){
									
								}
							}
						}else{
							if(systemType==2){
								//take from hummer continuous lat,long and offset  ,integrated,moving,stru
							}else{
								//take from stru continuous lat,long and offset  ,standlone,moving,stru
								try {
									JSONArray ptzDataArray = new Operations().getJson("select * from view_btsinfo where code = 9");
									JSONObject hummerDataObject = ptzDataArray.getJSONObject(0);
									new PTZ().sendGPSCommand(hummerDataObject.getString("ip"),Integer.parseInt(DBDataService.getConfigParamMap().get("ptz_port")), true, true);
								} catch (UnknownHostException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}catch(JSONException e){
									
								}
							}
						}
					}else{
						if(systemMode==0){
							if(systemType==2){
								//take from hummer static lat,long and offset  ,integrated,static,stru
								fileLogger.debug("@sendtotrgl about to send");
								new TRGLController().sendOffsetRequestToHummer();
							}else{
								//take from stru static lat,long and offset  ,standlone,static,stru
/*								try {
									JSONArray ptzDataArray = new Operations().getJson("select * from view_btsinfo where code = 9");
									JSONObject hummerDataObject = ptzDataArray.getJSONObject(0);
									new PTZ().sendGPSCommand(hummerDataObject.getString("ip"),Integer.parseInt(DBDataService.getConfigParamMap().get("ptz_port")), true, false);
									new PTZ().sendGPSCommand(hummerDataObject.getString("ip"),Integer.parseInt(DBDataService.getConfigParamMap().get("ptz_port")), false, false);
								} catch (UnknownHostException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}catch(JSONException e){
									
								}*/
							}
						}else{
							if(systemType==2){
								//take from hummer continuous lat,long and offset  ,integrated,moving,stru
							}else{
								//take from stru continuous lat,long and offset  ,standlone,moving,stru
/*								try {
									JSONArray ptzDataArray = new Operations().getJson("select * from view_btsinfo where code = 9");
									JSONObject hummerDataObject = ptzDataArray.getJSONObject(0);
									new PTZ().sendGPSCommand(hummerDataObject.getString("ip"),Integer.parseInt(DBDataService.getConfigParamMap().get("ptz_port")), true, true);
								} catch (UnknownHostException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}catch(JSONException e){
									
								}*/
							}
						}
					}
				} catch (Exception e1) {
					fileLogger.error("exception in sending gps request in "+Constants.automaticEvent);
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		    	
		    	
		    	
		        Common common = new Common();
		        //boolean availability=checkOldScannerDataOrRunNew();
		        String response="{}";
		        //if(availability){

				int effectiveTrackTime=(int)trackPeriodicity;
				//if manuallY
	        	if(Thread.currentThread().isInterrupted() || DBDataService.isInterrupted){
	        		DBDataService.isInterrupted = true;
	        	}else{
				response=goToCommonTrackingStarter(givenPLMNs,null,null,null,-1,-1,-1,Constants.schedulerEvent);
	        	}
				new ScanTrackModeServer().sendText("track&Idle");
/*				JSONArray hummerDataArray = new Operations().getJson("select * from view_btsinfo where code = 12");
				if(hummerDataArray.length()>0){
					new TRGLController().sendTrackingSectorToHummer("-1");
				}*/
		       //}
						JSONObject responseJson=null;
						try {
							responseJson = new JSONObject(response);
						}catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
/*						try {
							if(responseJson.getString("result").equals("success")){
								common.executeDLOperation("update oprrationdata set status='0',stoptime=timezone('utc'::text, now()) where id=(select max(id) from oprrationdata)");
								new AutoOperationServer().sendText(response);
								new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Operation Completed\"}");
							}else{
								common.executeDLOperation("update oprrationdata set status='2',stoptime=timezone('utc'::text, now()) where id=(select max(id) from oprrationdata)");
								new AutoOperationServer().sendText(response);
								new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Operation Interrupted\"}");
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}*/
		    	}catch(Exception e){
		    		
		    	}finally{
					//sanjay bharmoria currentEventName = null;
		    		stopScanFromTrackPart=false;
					synchronized(wait) {
						wait.notify();
			        }
				}
		    }}, 0L, trackPeriodicity, TimeUnit.MINUTES);
 	   fileLogger.info("Exit Function : startSchedulerOperation");
	}
	
	public void startUgsOperation(String eventName,String note,String oprType,String operators,int coverageDistance,String deviceIp,double frequency,double latitude,double longitude,long periodicity,int timeout,long validity,Date date,String sector,String cueId){
		//final double lat=latitude;
		//final double lon=longitude;
		final int runTime=timeout;
		final String antennaName=sector;
		new CurrentOperationType().setCueId(cueId);
		storeTriggerOnDb(date,cueId,"Oxfam","Processing,Cue ID:"+cueId+",Coordinates(Lat:"+latitude+",Lon:"+longitude+"),Sector:"+sector+",Validity:"+validity+",Timeout:"+timeout);
		new TriggerCueServer().sendText("event");
		new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Processing Oxfam Event\"}");
		
		LinkedHashMap<String,String> queLog = new LinkedHashMap<String,String>();					   		
		queLog.put("action", "Processing");
			queLog.put("source","Oxfam");
			queLog.put("cue id",cueId);
		new AuditHandler().audit_que(queLog);
		
		//CurrentOperationType.trans_id=eventData.getTransId();
		//new CurrentOperationType().genrateTransId();
		CurrentOperationType.trigger=true;
		ExecutorService executorTimerService = Executors.newSingleThreadExecutor();
		setExecutorTimerService(executorTimerService);
		//DBDataService.isInterrupted = false;
		future = executorTimerService.submit(new Runnable() {
		    public void run() {
		        try {
		        	DBDataService.isInterrupted = false;
		        	System.out.println(DBDataService.isInterrupted);
		 		   OperationCalculations oc = new OperationCalculations();
				   //int antennaId = oc.getAntennaIdFromPosition(lat,lon);
		 		   int antennaId=oc.getAntennaIdFromSector(antennaName);
				   //boolean availability=checkOldScannerDataOrRunNew();
				   String response = null;
				   //if(availability){
				   //String result = new ApiCommon().startTracking(null,null,null,-1,antennaId,runTime);
			        	if(Thread.currentThread().isInterrupted() || DBDataService.isInterrupted){
			        		DBDataService.isInterrupted = true;
			        	}else{
			        		response =goToCommonTrackingStarter(null,null,null,null,-1,antennaId,runTime,"ugs");
			        	}
				   new ScanTrackModeServer().sendText("track&Idle");
/*				   JSONArray hummerDataArray = new Operations().getJson("select * from view_btsinfo where code = 12");
				   if(hummerDataArray.length()>0){
					   new TRGLController().sendTrackingSectorToHummer("-1");
					}*/
				   //}
		        }catch(Exception E){
		        }finally{
					//sanjay bharmoria currentEventName = null;
		        	stopScanFromTrackPart=false;
					synchronized(wait) {
						wait.notify();
			        }
				}
		        }});
	}
	
	public void startTrglOperation(String eventName,String note,String oprType,String operators,int coverageDistance,String deviceIp,double frequency,double latitude,double longitude,long periodicity,int timeout,long validity,Date date,int angle,double bandwidth,String sector,String cueId){
		final double freq=frequency;
		final int runTime=timeout;
		final int antennaAngle=angle;
		final double bandWidth=bandwidth;
		final String antennaName = sector;
		new CurrentOperationType().setCueId(cueId);
		storeTriggerOnDb(date,cueId,"Hummer","Processing,Cue ID:"+cueId+",Frequency:"+frequency+",Bandwidth:"+bandwidth+",Sector:"+sector+",Validity:"+validity+",Timeout:"+timeout);
		new TriggerCueServer().sendText("event");
		new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Processing Hummer Event\"}");
		
		LinkedHashMap<String,String> queLog = new LinkedHashMap<String,String>();					   		
		queLog.put("action", "Processing");
			queLog.put("source","Hummer");
			queLog.put("cue id",cueId);
		new AuditHandler().audit_que(queLog);
		
		//final String cueId=cue;
		//CurrentOperationType.trans_id=eventData.getTransId();
		//new CurrentOperationType().genrateTransId();
		CurrentOperationType.trigger=true;
		ExecutorService executorTimerService = Executors.newSingleThreadExecutor();
		setExecutorTimerService(executorTimerService);
		//DBDataService.isInterrupted = false;
				future = executorTimerService.submit(new Runnable() {
				    public void run() {
				        try {
				        	   DBDataService.isInterrupted = false;
						   	   OperationCalculations oc = new OperationCalculations();
							   String fcns = oc.calulateArfcnsFromFreqBandwidth(freq,bandWidth);
							   //boolean availability=checkOldScannerDataOrRunNew();
							   //if(availability){
								   
								   //int antennaId=new OperationCalculations().getAntennaIdFromAngle(antennaAngle);
							   	int antennaId=new OperationCalculations().getAntennaIdFromSector(antennaName);
							   //String response = new ApiCommon().startTracking(null,fcns,fcns,freq,antennaId,runTime);
								   String response = null;
								   if(Thread.currentThread().isInterrupted() || DBDataService.isInterrupted){
									   DBDataService.isInterrupted = true;
								   }else{
						        		response =goToCommonTrackingStarter(null,fcns,fcns,fcns,freq,antennaId,runTime,"trgl");
								   }
							   new ScanTrackModeServer().sendText("track&Idle");
/*								JSONArray hummerDataArray = new Operations().getJson("select * from view_btsinfo where code = 12");
								if(hummerDataArray.length()>0){
									new TRGLController().sendTrackingSectorToHummer("-1");
								}*/
							   //}
				        }catch(Exception E){
				        	E.printStackTrace();
				        }finally{
							//sanjay bharmoria currentEventName = null;
				        	stopScanFromTrackPart=false;
							synchronized(wait) {
								wait.notify();
					        }
						}
/*				        try {
							stopRunningOperation();
						} catch (UnknownHostException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} */  
				    }});
	}
	
	public void stopRunningOperation() throws UnknownHostException, IOException
	{
		   fileLogger.info("Inside Function : stopRunningOperation");
		try{
			fileLogger.debug("In stopRunningOperation");
			if(currentEventName!=null && currentEventName.equals("scheduler")){
				fileLogger.debug("@scheduleropr stop scheduler operation");
				DBDataService.isTRGLTriggeredEvent = false;
				DBDataService.isUGSTriggeredEvent = false;
				DBDataService.isAutomaticTriggeredEvent = false;
				DBDataService.isSchedulerTriggeredEvent = false;
				//sanjay bharmoria currentEventName = null;
			
				shutdownSchedulerOperation();
			
			}else if(currentEventName!=null){
				fileLogger.debug("@otheropr stop other operation");
				DBDataService.isTRGLTriggeredEvent = false;
				DBDataService.isUGSTriggeredEvent = false;
				DBDataService.isAutomaticTriggeredEvent = false;
				DBDataService.isSchedulerTriggeredEvent = false;
				//sanjay currentEventName = null;
			
				shutdownOtherOperation();
			
		}
		//shutdownOtherOperation();
		   final HashMap<String,ArrayList<HashMap<String,String>>> devicesMapOverTech = new Operations().getAllBtsInfoByTech(); 
		   new ApiCommon().lockUnlockAllDevices(devicesMapOverTech,1);
		  // new CommonService().updateStatusOfGivenBts("all");
		   //new DeviceStatusServer().sendText("all");
		   	new DeviceStatusServer().sendText("ok");
			NetscanOperations netscanOperations=new NetscanOperations();
			LinkedHashMap<String,String> param = new LinkedHashMap<String,String>();
/*			JSONObject netscanData=null;
			try{
			netscanData =  new Operations().getJson("select * from view_btsinfo where code=3").getJSONObject(0);
			}catch(Exception E){
				
			}*/


/*		try {
			fileLogger.debug("DBDataService: Waiting for Lock Unlock Time");
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fileLogger.debug("In sleep for 5 seconds");*/
/*		DBDataService.isInterrupted=true;
		synchronized(wait) {
			wait.notify();
        }*/

		}finally{
			//DBDataService.isInterrupted=true;
			fileLogger.debug("DBDataService.isAutomaticTriggeredEvent is :"+DBDataService.isAutomaticTriggeredEvent);
			fileLogger.debug("out stopRunningOperation");
/*			synchronized(wait) {
				wait.notify();
	        }*/
		}
		  fileLogger.info("Exit Function : stopRunningOperation");
	}
	
	public void stopRunningOperationWithoutReleasing() throws UnknownHostException, IOException
	{
		  fileLogger.info("Inside Function : stopRunningOperationWithoutReleasing");
		if(currentEventName.equals("scheduler")){
			shutdownSchedulerOperation();
		}else{
			shutdownOtherOperation();
		}
		   final HashMap<String,ArrayList<HashMap<String,String>>> devicesMapOverTech = new Operations().getAllBtsInfoByTech(); 
		   new ApiCommon().lockUnlockAllDevices(devicesMapOverTech,1);
		  // new CommonService().updateStatusOfGivenBts("all");
		   //new DeviceStatusServer().sendText("all");
		   	new DeviceStatusServer().sendText("ok");
			NetscanOperations netscanOperations=new NetscanOperations();
			LinkedHashMap<String,String> param = new LinkedHashMap<String,String>();
/*			JSONObject netscanData=null;
			try{
			netscanData =  new Operations().getJson("select * from view_btsinfo where code=3").getJSONObject(0);
			}catch(Exception E){
				
			}*/

		fileLogger.debug("In stopRunningOperation");
		DBDataService.isTRGLTriggeredEvent = false;
		DBDataService.isUGSTriggeredEvent = false;
		DBDataService.isAutomaticTriggeredEvent = false;
		DBDataService.isSchedulerTriggeredEvent = false;
		//sanjay bharmoria currentEventName = null;
		try {
			fileLogger.debug("DBDataService: Waiting for Lock Unlock Time");
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fileLogger.debug("In sleep for 5 seconds");
		DBDataService.isInterrupted=true;
/*		synchronized(wait) {
			wait.notify();
        }*/
		fileLogger.debug("DBDataService.isAutomaticTriggeredEvent is :"+DBDataService.isAutomaticTriggeredEvent);
		  fileLogger.info("Exit Function : stopRunningOperationWithoutReleasing");
		//fileLogger.debug("out stopRunningOperation");
	}
	
	public void releaseWait(){
		synchronized(wait) {
			wait.notify();
		}
	}
	
	public void stopRunningManualOperation() throws UnknownHostException, IOException
	{
		  fileLogger.info("Inside Function : stopRunningManualOperation");
			shutdownManualOperation();
		   final HashMap<String,ArrayList<HashMap<String,String>>> devicesMapOverTech = new Operations().getAllBtsInfoByTech(); 
		   boolean cmdStatus= new ApiCommon().sendOctasicCmd(7,"Tx Controller","TDD_PA_OFF");
//			if(!cmdStatus){
//				new AutoOperationServer()
//				.sendText("{\"result\":\"fail\",\"msg\":\"PA Disable CMD  failed:LTE\"}");
//				//return false;
//			}else{
				fileLogger.debug("@sleep about to sleep in SendOctasicCmd TX Controller Switching");
//				
//			}
			
		    new ApiCommon().lockUnlockAllDevices(devicesMapOverTech,1);
			//NetscanOperations netscanOperations=new NetscanOperations();
			//LinkedHashMap<String,String> param = new LinkedHashMap<String,String>();
/*			JSONObject netscanData=null;
			try{
			netscanData =  new Operations().getJson("select * from view_btsinfo where code=3").getJSONObject(0);
			}catch(Exception E){
				
			}*/

		fileLogger.debug("In stopManualRunningOperation");
		//sanjay bharmoria currentEventName = null;
/*		try {
			fileLogger.debug("DBDataService: Waiting for Lock Unlock Time");
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fileLogger.debug("In sleep for 2 seconds");*/
		//fileLogger.debug("out stopManualRunningOperation");
		fileLogger.info("Exit Function : stopRunningManualOperation");
	}
	
	public void shutdownOtherOperation()
	{
		fileLogger.info("Inside Function : shutdownOtherOperation");
		//fileLogger.debug("in shutdownOtherOperation");
		
		
		fileLogger.debug("@rachel "+executorTimerService);
		if(executorTimerService!=null)
		{
			
			fileLogger.debug("@rachel shutting down");
			DBDataService.isInterrupted = true;
			executorTimerService.shutdownNow();
			executorTimerService=null;
			fileLogger.debug("@rachel done");
		}
		//fileLogger.debug("@rachel "+executorTimerService.isShutdown());
		//fileLogger.debug("@rachel "+executorTimerService.isTerminated());
		new ScanTrackModeServer().sendText("track&Idle");
/*		JSONArray hummerDataArray = new Operations().getJson("select * from view_btsinfo where code = 12");
		if(hummerDataArray.length()>0){
			new TRGLController().sendTrackingSectorToHummer("-1");
		}*/
		fileLogger.info("Exit Function : shutdownOtherOperation");
		//fileLogger.debug("out shutdownOtherOperation");
	}
	
	public void shutdownManualOperation()
	{
		fileLogger.info("Inside Function : shutdownManualOperation");
		//fileLogger.debug("in shutdownManualOperation");
		
		
		fileLogger.debug("@kiara "+executorManualService);
		if(executorManualService!=null)
		{
			
			fileLogger.debug("@kiara shutting down");
			executorManualService.shutdownNow();
			executorManualService=null;
			fileLogger.debug("@kiara done");
		}
		//fileLogger.debug("@kiara "+executorManualService.isShutdown());
		//fileLogger.debug("@kiara "+executorManualService.isTerminated());
		new ScanTrackModeServer().sendText("track&Idle");
/*		JSONArray hummerDataArray = new Operations().getJson("select * from view_btsinfo where code = 12");
		if(hummerDataArray.length()>0){
			new TRGLController().sendTrackingSectorToHummer("-1");
		}*/
		fileLogger.info("Exit Function : shutdownManualOperation");
		//fileLogger.debug("out shutdownManualOperation");
	}
	
	public void executeManualOperation(String configData,int repFreq){
		fileLogger.info("Inside Function : executeManualOperation");
		fileLogger.debug("@manual 3");
		final String config=configData;
		final int repititionFreq=repFreq;
		new CurrentOperationType().genrateTransId();
		ExecutorService executorManualService = Executors.newSingleThreadExecutor();
		setExecutorTimerManualService(executorManualService);
		
		DBDataService.isSchedulerTriggeredEvent = true;

		CurrentOperationType.trigger=true;
		   executorManualService.submit(new Runnable() {
			    public void run() {
			        try {
			        	fileLogger.debug("@manual 4");
			        	Common common = new Common();
			        	String response = "{}";
			        	fileLogger.debug("@abc about while in executeManualOperation");
							new AutoStateServer().sendText("start");
							fileLogger.debug("@manual 5");
							currentEventName = "manual";
							goToManualTrackingStarter(config, null, null, -1, -1, -1, "manual",repititionFreq);
							boolean updateStatus=new Common().executeDLOperation("update config_status set status=0,start_time=now()");
							DBDataService.getInstance().setCurrentManualEventName(null);
							new AutoStateServer().sendText("stop");
			  				//String response=new ApiCommon().startTracking(givenPLMNs);
			  				//JSONObject responseJson=new JSONObject(response);
							new ScanTrackModeServer().sendText("track&Idle"); 
/*							JSONArray hummerDataArray = new Operations().getJson("select * from view_btsinfo where code = 12");
							if(hummerDataArray.length()>0){
								new TRGLController().sendTrackingSectorToHummer("-1");
							}*/
	    				JSONObject responseJson=null;
	    					responseJson = new JSONObject(response);
	/*    				try {
	    					if(responseJson.getString("result").equals("success")){
	    						common.executeDLOperation("update oprrationdata set status='0',stoptime=timezone('utc'::text, now()) where id=(select max(id) from oprrationdata)");
	    						new AutoOperationServer().sendText(response);
	    						new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Operation Completed\"}");
	    					}else{
	    						common.executeDLOperation("update oprrationdata set status='2',stoptime=timezone('utc'::text, now()) where id=(select max(id) from oprrationdata)");
	    						new AutoOperationServer().sendText(response);
	    						new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Operation Interrupted\"}");
	    					}
	    				} catch (JSONException e) {
	    					// TODO Auto-generated catch block
	    					e.printStackTrace();
	    				}*/
			        	
			        }catch(Exception E){
			        	new ScanTrackModeServer().sendText("track&Idle"); 
/*						JSONArray hummerDataArray = new Operations().getJson("select * from view_btsinfo where code = 12");
						if(hummerDataArray.length()>0){
							new TRGLController().sendTrackingSectorToHummer("-1");
						}*/
			        	fileLogger.error("Exception in running manual tracking :"+E.getMessage());
			        	E.printStackTrace();
			        }finally{
			        	stopScanFromTrackPart=false;
			        }
			    }});
			fileLogger.info("Exit Function : executeManualOperation");
	}
	
	public void shutdownSchedulerOperation()
	{
		//fileLogger.info("Inside Function : shutdownSchedulerOperation");
		//fileLogger.debug("in shutdownSchedulerOperation");
		if(scheduledExecutorService!=null){
			DBDataService.isInterrupted = true;
			scheduledExecutorService.shutdownNow();
			scheduledExecutorService=null;
			//DBDataService.isInterrupted = true;
		}
		new ScanTrackModeServer().sendText("track&Idle");
/*		JSONArray hummerDataArray = new Operations().getJson("select * from view_btsinfo where code = 12");
		if(hummerDataArray.length()>0){
			new TRGLController().sendTrackingSectorToHummer("-1");
		}*/
		//fileLogger.debug("out shutdownSchedulerOperation");
	}
	
	public void shutdownScannerScheduler()
	{
		fileLogger.info("Inside Function : shutdownScannerScheduler");
		ScheduledExecutorService scheduledExecutorService=NetscanSchedulerListener.scheduler;
		//fileLogger.debug("in shutdownScannerScheduler");
		if(scheduledExecutorService!=null){
			NetscanSchedulerListener.isScannerInterrupted = true;
			scheduledExecutorService.shutdownNow();
			scheduledExecutorService=null;
			NetscanSchedulerListener.scheduler=null;
		}
		
		new ScanTrackModeServer().sendText("scan&Idle");
		//fileLogger.debug("out shutdownScannerScheduler");
		fileLogger.info("Exit Function : shutdownScannerScheduler");
	}
	
	public boolean checkOldScannerDataOrRunNew(){
		boolean availability=false;
		availability=checkOldScannerDataAvailable();
		if(!availability){
			runNewScannerCycle();
			availability=checkOldScannerDataAvailable();
			if(availability){
				return true;
			}else{
				return false;
			}
		}else{
			return true;
		}
	}
	
	public boolean checkOldScannerDataAvailable(){
		fileLogger.info("Inside Function : checkOldScannerDataAvailable");
		Operations operations=new Operations();
			operations.getJson("select useoldnetworkscannerdatalast24hours()");
			try {
				int sectorAntennaCount=operations.getJson("select count(*) count from antenna where atype='1' and intracking is true").getJSONObject(0).getInt("count");
				fileLogger.debug("@trackscan 1");
				if(sectorAntennaCount>0){
					int systemTypeCode=DBDataService.getSystemType();
					int count=operations.getJson("select count(*) count from oprlogs_current where ((oprlogs_current.inserttime >= (timezone('utc'::text, now()) - '24:00:00'::interval) AND oprlogs_current.inserttime <= timezone('utc'::text, now())) OR oprlogs_current.count::text = 'locator_count'::text) and antenna_id in (select id from antenna where atype='1' and intracking is true)").getJSONObject(0).getInt("count");
					fileLogger.debug("@trackscan 2");
					
					if (count<1 && systemTypeCode==0)
					{
						int count1=operations.getJson("select count(*) count from oprlogs_current where ((oprlogs_current.inserttime >= (timezone('utc'::text, now()) - '24:00:00'::interval) AND oprlogs_current.inserttime <= timezone('utc'::text, now())) OR oprlogs_current.count::text = 'locator_count'::text) and antenna_id in (select id from antenna where atype='3' and inscanning is true )").getJSONObject(0).getInt("count");
						count=count1;
					}
					
					if(count<1){
						return false;
					}else{
						return true;
					}
					
				}
				int verticalOmniAntennaCount=operations.getJson("select count(*) count from antenna where atype='3' and intracking is true").getJSONObject(0).getInt("count");
				fileLogger.debug("@trackscan 3");
				if(verticalOmniAntennaCount>0){
					int count=operations.getJson("select count(*) count from oprlogs_current where ((oprlogs_current.inserttime >= (timezone('utc'::text, now()) - '24:00:00'::interval) AND oprlogs_current.inserttime <= timezone('utc'::text, now())) OR oprlogs_current.count::text = 'locator_count'::text) and antenna_id in (select id from antenna where atype='3' and intracking is true)").getJSONObject(0).getInt("count");
					fileLogger.debug("@trackscan 4");	
					if(count<1){
							return false;
						}else{
							return true;
						}
					}
				
				int horizontalOmniAntennaCount=operations.getJson("select count(*) count from antenna where atype='2' and intracking is true").getJSONObject(0).getInt("count");
				if(verticalOmniAntennaCount>0){
					int count=operations.getJson("select count(*) count from oprlogs_current where ((oprlogs_current.inserttime >= (timezone('utc'::text, now()) - '24:00:00'::interval) AND oprlogs_current.inserttime <= timezone('utc'::text, now())) OR oprlogs_current.count::text = 'locator_count'::text) and antenna_id in (select id from antenna where atype='2' and intracking is true)").getJSONObject(0).getInt("count");
						if(count<1){
							return false;
						}else{
							return true;
						}
					}
			}catch (JSONException e) {
				fileLogger.error("exception in checkOldScannerDataAvailable message:"+e.getMessage());
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			fileLogger.info("Exit Function : checkOldScannerDataAvailable");
		return false;
	}
	
	public void runNewScannerCycle(){
		//isScanRunningBeforeTracking=true;
		new NetscanJob().executeNetscanJob();
		//isScanRunningBeforeTracking=false;
	}
	
	public String goToCommonTrackingStarter(ArrayList<String> givenPLMNs,String arfcnList,String uarfcnList,String earfcnList,double freq,int antennaId,int trackTime,String eventType){
		fileLogger.info("Inside Function : goToCommonTrackingStarter");
		JSONArray hummerDataArray = new Operations().getJson("select * from view_btsinfo where code = 12");
		if(hummerDataArray.length()>0){
			new TRGLController().sendTrackingStatusToHummer("yes");
		}
		
		//fileLogger.debug("in goToCommonTrackingStarter");
		Operations operations = new Operations();
		String response="{}";
		ApiCommon apiCommon = new ApiCommon();
		try {
			
			boolean availability=checkOldScannerDataOrRunNew();
			
			if(availability){
			int switchCount=operations.getJson("select count(*) count from view_btsinfo where code=8 limit 1").getJSONObject(0).getInt("count");
			JSONArray sysManagerArray = operations.getJson("select * from view_btsinfo where code=10 limit 1");
			boolean sysManagerAvailability = false;
			boolean switchAvailability = false;
			if(sysManagerArray.length()!=0){
				sysManagerAvailability=true;
			}
			
			boolean switchUsed=false;
			int systemTypeCode=DBDataService.getSystemType();
			if(systemTypeCode==1 || systemTypeCode==2){
				switchUsed=true;
			}
			
					fileLogger.debug("in sysManagerAvailability in switchAvailability");
					JSONArray sectorAntennaArray=new Operations().getJson("select * from antenna where atype='1' and intracking is true order by id");
					if(eventType.equalsIgnoreCase(Constants.automaticEvent) || eventType.equalsIgnoreCase(Constants.schedulerEvent)){
					if(sectorAntennaArray.length()!=0){

						stopScannerOnSector(operations);						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
/*				    	long futureTime=-1;
						if(trackTime!=-1){
							Date currDate = new Date();
						    Calendar calendar = Calendar. getInstance();
						    calendar.setTime(currDate);
						    calendar.add(Calendar.MINUTE,trackTime);
						    futureTime=calendar.getTimeInMillis();
						}*/
				    	
						for(int i=0;i<sectorAntennaArray.length();i++){
							if(Thread.currentThread().isInterrupted() || DBDataService.isInterrupted){
								DBDataService.isInterrupted = true;
								fileLogger.debug("@interrupt 15 interrupted exception occurs");
								return "{\"result\":\"success\",\"msg\":\"Operation Stopped\"}";
							}
					    	if(i!=0 && !switchUsed){
					    		break;
					    	}
					    	response=apiCommon.startTracking(givenPLMNs, arfcnList, uarfcnList, earfcnList, freq, sectorAntennaArray.getJSONObject(i).getInt("id"), trackTime, sysManagerAvailability, switchAvailability);	
							if(DBDataService.isInterrupted){
								new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Operation Stopped\"}");
							}
/*							if(trackTime!=-1){
								if (System.currentTimeMillis()>=futureTime) 
								{
									DBDataService.isInterrupted = true;
									fileLogger.debug("@operation complete operation time expired");
									new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Operation Stopped\"}");
									break;
								}
							}*/
							
						}
						stopScanFromTrackPart=false;
					}else{
						JSONArray omniAntennaArray=new Operations().getJson("select * from antenna where atype='3' and intracking is true order by id");	
						for(int i=0;i<omniAntennaArray.length();i++){
							if(Thread.currentThread().isInterrupted() || DBDataService.isInterrupted){
								DBDataService.isInterrupted = true;
								fileLogger.debug("@interrupt 16 interrupted exception occurs");
								return "{\"result\":\"success\",\"msg\":\"Operation Stopped\"}";
							}
							response=apiCommon.startTracking(givenPLMNs, arfcnList, uarfcnList, earfcnList, freq, omniAntennaArray.getJSONObject(i).getInt("id"), trackTime, sysManagerAvailability, switchAvailability);	
							if(DBDataService.isInterrupted){
								new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Operation Stopped\"}");
							}
						}
						
					}
					}else{
					  JSONArray aTypeArray = operations.getJson("select atype from antenna where id="+antennaId+" and intracking is true");
					  if(aTypeArray.length()>0){
							
						  stopScannerOnSector(operations);						
						  System.out.println(DBDataService.isInterrupted);
						  response=apiCommon.startTracking(givenPLMNs, arfcnList, uarfcnList, earfcnList, freq, antennaId, trackTime, sysManagerAvailability, switchAvailability);
							  new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Operation Stopped\"}");  
						  /*if(DBDataService.isInterrupted){
								new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Operation Stopped\"}");
						  }*/
					  }else{
						  new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Sector Antenna not selected\"}");
						  new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Operation Stopped\"}");
						  fileLogger.debug("antennId not present ");
					  }
					}
		}
			fileLogger.debug("out goToCommonTrackingStarter");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			fileLogger.error("exception in goToCommonTrackingStarter message :"+e.getMessage());
			//e.printStackTrace();
		}/*finally{
			synchronized(wait) {
				wait.notify();
	        }
		}*/
		 hummerDataArray = new Operations().getJson("select * from view_btsinfo where code = 12");
		if(hummerDataArray.length()>0){
			new TRGLController().sendTrackingStatusToHummer("No");
		}
		fileLogger.info("Exit Function : goToCommonTrackingStarter");
        return response;
	}
	
	// Check PTZ is rotating at particular angle ......... Sandeep
	public static String checkantenatypeandmovesection(int profileValue )  {
		try {
			fileLogger.info("Inside Function : checkantenatypeandmovesection");
			Operations operations = new Operations();
			int currentAngleSaved ;
			String currentAngleSaved1;
			int angleReturn = 0;
			PTZ ptz = new PTZ();
			String returnResult="";
			 
			int maxAngle=Integer.parseInt(DBDataService.getConfigParamMap().get("maxAngle"));
			
			
			JSONArray PTZserverIP=operations.getJson("select ip from view_btsinfo where code = 9");
			fileLogger.info("Inside Function : checkantenatypeandmovesection PTZserverIP = "+PTZserverIP);
			String PTZip = PTZserverIP.getJSONObject(0).getString("ip");
			fileLogger.info("Inside Function : checkantenatypeandmovesection PTZip = "+PTZip);
			//currentAngleSaved1 = PTZ.getCurrentAngle(PTZip);
			//String[] ptzArr = currentAngleSaved1.split(":");
			//fileLogger.info("Inside Function : checkantenatypeandmovesection currentAngleSaved1 = "+currentAngleSaved1);
			//currentAngleSaved1.split(":");
			//currentAngleSaved=Integer.parseInt(ptzArr[0]);
			//fileLogger.info("Inside Function : checkantenatypeandmovesection currentAngleSaved = "+currentAngleSaved);
			//System.out.println("Ange :"+ ptzArr[0] +" Tilt:"+ptzArr[1]);
	
	
			
			JSONArray s1Angle=operations.getJson("select antenna_angle,antenna_type from antenna where id =1");
			int s1AngleSaved = Integer.parseInt(s1Angle.getJSONObject(0).getString("antenna_angle"));
			fileLogger.info("Inside Function : checkantenatypeandmovesection s1AngleSaved = "+s1AngleSaved);
			int AngleType = Integer.parseInt(s1Angle.getJSONObject(0).getString("antenna_type"));
			fileLogger.info("Inside Function : checkantenatypeandmovesection AngleType = "+AngleType);
			System.out.println(s1AngleSaved +"Angle Type"+ AngleType);
			if(AngleType == 2) {
	 
				// When  Antenna rotating ......
			 if(profileValue==1) {
				 fileLogger.info("Inside Function : checkantenatypeandmovesection profileValue==1  ");
	
					angleReturn = s1AngleSaved;
	
				}else if(profileValue==2 ){
					fileLogger.info("Inside Function : checkantenatypeandmovesection profileValue==2  ");
					angleReturn = s1AngleSaved + 60 ;
	
				} else if(profileValue==3) {
					fileLogger.info("Inside Function : checkantenatypeandmovesection profileValue==3  ");
					angleReturn = s1AngleSaved+120 ;
	
				} 
				if (angleReturn >360)
					angleReturn=angleReturn-360;
	
	//			if (angleReturn>currentAngleSaved ) {
	//				angleReturn = angleReturn-currentAngleSaved ;
	//			}
	//			else
	//			{
	//				if (angleReturn==currentAngleSaved) 
	//				{
	//					returnResult= "{\"result\":\"success\",\"msg\":\"STRU Angle to  is moving \"}";
	//					return returnResult;
	//				}
	//				ptz.jumpToPositionPacket(PTZip,0, 0, true);
	//				try {
	//					Thread.sleep(5000);
	//				} catch (InterruptedException e) {
						// TODO Auto-generated catch block
	//					e.printStackTrace();
		//			}
	//			}
				
				fileLogger.info("Inside Function : checkantenatypeandmovesection  angleReturn = "+angleReturn);
				fileLogger.info("Inside Function : checkantenatypeandmovesection  maxAngle = "+maxAngle);
			
				if(angleReturn > maxAngle)
				{
					new AutoOperationServer().sendText("{\"result\":\"fail\",\"msg\":\"Max Angle at which Antenna can be rotated Reached\"}");
					fileLogger.info("Inside Function : checkantenatypeandmovesection  angleReturn > maxAngle");
					
				
					return "Error:Max Angle at which Antenna can be rotated Reached";
					
				}
				
				ptz.jumpToPositionPacket(PTZip,angleReturn, 0, true);
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	
				currentAngleSaved1 = PTZ.getCurrentAngle(PTZip);
				fileLogger.info("Inside Function : checkantenatypeandmovesection ... currentAngleSaved1 = "+currentAngleSaved1);
				//currentAngleSaved1.split(":");
				String[] ptzArr = currentAngleSaved1.split(":");
				currentAngleSaved=Integer.parseInt(ptzArr[0]);
				
				fileLogger.debug(" checkantenatypeandmovesection , Again going to check current angle and currentAngleSaved =" + currentAngleSaved);
				fileLogger.debug(" checkantenatypeandmovesection , Again going to check current angle and angleReturn =" + angleReturn);
				
				//currentAngleSaved = Integer.parseInt(PTZ.getCurrentAngle(PTZip));
				if (!(currentAngleSaved > angleReturn- 3 && currentAngleSaved < angleReturn+3 )) {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					currentAngleSaved1 = PTZ.getCurrentAngle(PTZip);
					//currentAngleSaved1.split(":");
					ptzArr = currentAngleSaved1.split(":");
					currentAngleSaved=Integer.parseInt(ptzArr[0]);
					fileLogger.debug(" checkantenatypeandmovesection , Again going to check current angle and currentAngleSaved =" + currentAngleSaved);
					fileLogger.debug(" checkantenatypeandmovesection , Again going to check current angle and angleReturn =" + angleReturn);
				}
					
				if (currentAngleSaved > angleReturn- 3 && currentAngleSaved < angleReturn+3 )
				{
					returnResult= "{\"result\":\"success\",\"msg\":\"STRU Angle to  is moving \"}";
					
	
				}		
				else {
					//Check PT is not rotating angle submitted angleReturn and angle returned by PT currentAngleSaved
					//returnResult= "{\"result\":\"msg\":\"Check PT is not rotating angle submitted angleReturn and angle returned by PT currentAngleSaved \"}";
					returnResult = "{\"result\":\"fail\",\"msg\":\"Check PT is not rotating \"}";
				}
				
			} 
			
			/*else {
				
				ptz.jumpToPositionPacket(PTZip,0, 0, true);
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}		
			}*/
			fileLogger.info("Exit Function : checkantenatypeandmovesection");
			return returnResult;
	
			}
		catch(Exception e) {
			fileLogger.info("Inside Function : checkantenatypeandmovesection ... Exception occurred  = "+e);
			return "Failure";
			
		}
		
	}

	
		 
			
	

	public String goToManualTrackingStarter(String config,String arfcnList,String uarfcnList,int freq,int antennaId,int trackTime,String eventType,int repititionFreq) throws IOException{
		fileLogger.info("Inside Function : goToManualTrackingStarter");
		//fileLogger.debug("in goToManualTrackingStarter");
		Operations operations = new Operations();
		String response="{}";
		boolean result=false;
		ApiCommon apiCommon = new ApiCommon();
		try {
			JSONArray hummerDataArray = new Operations().getJson("select * from view_btsinfo where code = 12");
			if(hummerDataArray.length()>0){
				new TRGLController().sendTrackingStatusToHummer("yes");
			}
			fileLogger.debug("@manual 6");
			int switchCount=operations.getJson("select count(*) count from view_btsinfo where code=8 limit 1").getJSONObject(0).getInt("count");
			JSONArray sysManagerArray = operations.getJson("select * from view_btsinfo where code=10 limit 1");
			boolean sysManagerAvailability = false;
			boolean switchAvailability = false;
			if(sysManagerArray.length()!=0){
				sysManagerAvailability=true;
			}

			boolean switchUsed=false;
			int systemTypeCode=DBDataService.getSystemType();
			if(systemTypeCode==1 || systemTypeCode==2){
				switchUsed=true;
			}

					fileLogger.debug("@manual 7");
					fileLogger.debug("in sysManagerAvailability in switchAvailability");
					JSONArray sectorAntennaArray=new Operations().getJson("select * from antenna where atype='1' and intracking is true order by id");
					if(eventType.equalsIgnoreCase("manual")){
						fileLogger.debug("@manual 8");
					if(sectorAntennaArray.length()!=0){
				    	JSONObject scanModeObject = new Operations().getJson("select * from running_mode where mode_type='scan'").getJSONObject(0);
				    	String modeStatus = scanModeObject.getString("mode_status");
				    	boolean isScanningOnSector=false;
				    	if(!modeStatus.equalsIgnoreCase("idle") && !scanModeObject.getString("applied_antenna").equalsIgnoreCase("ov1")){
				    		isScanningOnSector = true;
				    	}
				    	if(isScanningOnSector){
							JSONArray netscanArray = operations.getJson("select * from view_btsinfo where code=3");
							if(netscanArray.length()>0){
							JSONObject netscanData = netscanArray.getJSONObject(0);	
				    		CommonService commonService = new CommonService();
				    		NetscanOperations netscanOperations = new NetscanOperations();
							fileLogger.debug("@scan about to stop netscan");
							LinkedHashMap<String,String> param = new LinkedHashMap<String,String>();
							stopScanFromTrackPart=true;
							try {
								param = commonService.prepareParamForStopNetscan(netscanData,"GSM");
								netscanOperations.sendToServer(param);
//								param = commonService.prepareParamForStopNetscan(netscanData,"UMTS");
//								netscanOperations.sendToServer(param);
//								param = commonService.prepareParamForStopNetscan(netscanData,"LTE");
//								netscanOperations.sendToServer(param);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								fileLogger.error("exception while stop scanner in manual tracking :"+e1.getMessage());
							}
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							fileLogger.debug("@scan stopped netscan");
							}
				    		}
						for(int i=0;i<sectorAntennaArray.length();i++){
							if(Thread.currentThread().isInterrupted() || DBDataService.isInterrupted){
								DBDataService.isInterrupted = true;
								fileLogger.debug("@interrupt 19 interrupted exception occurs");
								return "{\"result\":\"success\",\"msg\":\"Operation Stopped\"}";
							}
							//response=
					    	if(i!=0 && !switchUsed){
					    		break;
					    	}
								String configPerAntenna=getConfigPerGivenAntenna(config,sectorAntennaArray.getJSONObject(i).getString("id"));
								result=apiCommon.autoManualTackMobileOnGivenPacketList(configPerAntenna, freq, sectorAntennaArray.getJSONObject(i).getInt("id"), trackTime, sysManagerAvailability, switchAvailability,repititionFreq);	
								if(DBDataService.isInterrupted){
									return "{\"result\":\"success\",\"msg\":\"Operation Stopped\"}";
								}
								fileLogger.debug("@manual 9");
								if(!result) 
								 {
									 //return "{\"result\":\"fail\",\"msg\":\"Tracking failed\"}"; 
									new AutoOperationServer().sendText("{\"result\":\"fail\",\"msg\":\"Tracking failed\"}");
								   }
								else{
								   //return "{\"result\":\"success\",\"msg\":\"Tracking finished\"}";
									new AutoOperationServer().sendText("{\"result\":\"fail\",\"msg\":\"Tracking finished\"}");
								}
						}
						stopScanFromTrackPart=false;
					}else{
						JSONArray omniAntennaArray=new Operations().getJson("select * from antenna where atype='3' and intracking is true order by id");	
						for(int i=0;i<omniAntennaArray.length();i++){
							if(Thread.currentThread().isInterrupted() || DBDataService.isInterrupted){
								DBDataService.isInterrupted = true;
								fileLogger.error("@interrupt 20 interrupted exception occurs");
								return "{\"result\":\"success\",\"msg\":\"Operation Stopped\"}";
							}
							//response=
							String configPerAntenna=getConfigPerGivenAntenna(config,omniAntennaArray.getJSONObject(i).getString("id"));
							fileLogger.debug("@manual 10");
							result=apiCommon.autoManualTackMobileOnGivenPacketList(configPerAntenna,freq, omniAntennaArray.getJSONObject(i).getInt("id"), trackTime, sysManagerAvailability, switchAvailability,repititionFreq);	
							if(DBDataService.isInterrupted){
								return "{\"result\":\"success\",\"msg\":\"Operation Stopped\"}";
							}
							if(!result) 
							 {
								 return "{\"result\":\"fail\",\"msg\":\"Tracking failed\"}";   
							   }
							else{
							   return "{\"result\":\"success\",\"msg\":\"Tracking finished\"}";
							}
						}
						
					}
					}else{
					  JSONArray aTypeArray = operations.getJson("select atype from antenna where id="+antennaId+" and intracking is true");
					  if(aTypeArray.length()>0){
						  //response=
						  fileLogger.debug("@manual 11");
						  String configPerAntenna=getConfigPerGivenAntenna(config,Integer.toString(antennaId));
						  result=apiCommon.autoManualTackMobileOnGivenPacketList(configPerAntenna, freq, antennaId, trackTime, sysManagerAvailability, switchAvailability,repititionFreq);	 
							if(DBDataService.isInterrupted){
								return "{\"result\":\"success\",\"msg\":\"Operation Stopped\"}";
							}
						  if(!result) 
							 {
								 return "{\"result\":\"fail\",\"msg\":\"Tracking failed\"}";   
							   }
							else{
							   return "{\"result\":\"success\",\"msg\":\"Tracking finished\"}";
							}
					  }else{
						  fileLogger.debug("antennId not present ");
					  }
					}
					
			fileLogger.debug("out goToManualTrackingStarter");
		} catch (JSONException e) {
			// TODO Auto-generated catch block	
			e.printStackTrace();
			return "{\"result\":\"success\",\"msg\":\"Tracking Stopped\"}";
		}finally{
			stopScanFromTrackPart=false;
		}
		JSONArray hummerDataArray = new Operations().getJson("select * from view_btsinfo where code = 12");
		if(hummerDataArray.length()>0){
			new TRGLController().sendTrackingStatusToHummer("no");
		}
		fileLogger.info("Exit Function : goToManualTrackingStarter");
		return "{\"result\":\"success\",\"msg\":\"Tracking Completed\"}";
	}
	
	public String getConfigPerGivenAntenna(String config,String antennaId){
		fileLogger.info("Inside Function : getConfigPerGivenAntenna");
		fileLogger.debug("@manual 14");
		 JSONObject configPerAntenna = new JSONObject();
		 fileLogger.debug("@mnaul 14a config is :"+config);
		try {
			//JSONObject configJson = new JSONObject(config);
			//JSONArray dataArray = configJson.getJSONArray("config_data");
			JSONArray dataArray = new JSONArray(config);
			String packetAntennaId="";
			JSONArray finalDataArray = new JSONArray();
			fileLogger.debug("@manual 15");
			for(int i=0;i<dataArray.length();i++){
				JSONObject tempConfig=dataArray.getJSONObject(i);
				packetAntennaId=tempConfig.getString("antennaId");
				if(antennaId.equals(packetAntennaId)){
					finalDataArray.put(tempConfig);
				}
			}
			fileLogger.debug("@manual 16");
			configPerAntenna.put("config_data",finalDataArray);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fileLogger.info("Exit Function : getConfigPerGivenAntenna");
		return configPerAntenna.toString();
	}
	public void saveBMSconfig(String ip,String name,String tag,String value){
		fileLogger.debug("Inside SaveBMSconfig ");
		fileLogger.debug(" ip= "+ ip+ " name= "+ name+" tag= "+ tag+"  value=  "+value);
		String query ="insert into bmsconfig(ip,name,tag,value) values('"+ip+"','"+name+"','"+tag+"','"+value+"')";
		fileLogger.debug(query);
		Common co = new Common();
		co.executeDLOperation(query);
		
	}

	
	
	
	
	
	
	
	
	
	
	public void stopScannerOnSector(Operations operations){
		
		
		JSONObject scanModeObject;
		try {
			scanModeObject = new Operations().getJson("select * from running_mode where mode_type='scan'").getJSONObject(0);
		
		String modeStatus = scanModeObject.getString("mode_status");
		boolean isScanningOnSector=false;
		if(!modeStatus.equalsIgnoreCase("idle") && !scanModeObject.getString("applied_antenna").equalsIgnoreCase("ov1")){
			isScanningOnSector = true;
		}
		if(isScanningOnSector){
			JSONArray netscanArray = operations.getJson("select * from view_btsinfo where code=3");
			if(netscanArray.length()>0){
				JSONObject netscanData = netscanArray.getJSONObject(0);	
				CommonService commonService = new CommonService();
				NetscanOperations netscanOperations = new NetscanOperations();
				fileLogger.debug("@scan about to stop netscan");
				LinkedHashMap<String,String> param = new LinkedHashMap<String,String>();
				stopScanFromTrackPart=true;
				try {
					param = commonService.prepareParamForStopNetscan(netscanData,"GSM");
					netscanOperations.sendToServer(param);

						
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					fileLogger.debug("@scan stopped netscan");
					}
				}
				
			
			} catch (JSONException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
