package in.vnl.api.netscan;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.vnl.EventProcess.DBDataService;
import in.vnl.api.common.ApiCommon;
import in.vnl.api.common.CommonService;
import in.vnl.api.common.livescreens.AutoOperationServer;
import in.vnl.api.common.livescreens.DeviceStatusServer;
import in.vnl.api.common.livescreens.ScanTrackModeServer;
import in.vnl.api.twog.TwogOperations;
import in.vnl.msgapp.Common;
import in.vnl.msgapp.Operations;
import in.vnl.scheduler.NetscanSchedulerListener;
public class NetscanJob {
	static Logger fileLogger = Logger.getLogger("file");
	public void executeNetscanJob(){
		fileLogger.info("Inside Function : executeNetscanJob");
		//fileLogger.debug("in executenetscanjob");
		Operations operations = new Operations();
		
		try {
/*			boolean isScanRunningBeforeTracking = DBDataService.isScanRunningBeforeTracking;
			if(isScanRunningBeforeTracking){
				return;
			}*/
			JSONArray netscanArray = operations.getJson("select * from view_btsinfo where code=3");
			int switchCount=operations.getJson("select count(*) count from view_btsinfo where code=8 limit 1").getJSONObject(0).getInt("count");
			JSONArray systemManagerArray = operations.getJson("select * from view_btsinfo where code=10 limit 1");
			CommonService commonService = new CommonService();
			NetscanOperations netscanOperations = new NetscanOperations();
			if(netscanArray.length()==0){
				fileLogger.debug("Network Scanner not present");
			}else{
				JSONObject netscanData = netscanArray.getJSONObject(0);
				fileLogger.debug("@scan about to stop netscan");
				//JSONArray sectorAntennaArray=operations.getJson("select * from antenna where atype='1' and inscanning is true order by id");
				LinkedHashMap<String,String> param = new LinkedHashMap<String,String>();
				param = commonService.prepareParamForStopNetscan(netscanData,"GSM");
				netscanOperations.sendToServer(param);
				//param = commonService.prepareParamForStopNetscan(netscanData,"UMTS");
				//netscanOperations.sendToServer(param);
				//param = commonService.prepareParamForStopNetscan(netscanData,"LTE");
				//netscanOperations.sendToServer(param);
				
				Thread.sleep(1000);
				fileLogger.debug("@scan stopped netscan");
				
				startSysManagerNetworkScanner(switchCount,netscanData,systemManagerArray);
				
/*				if(switchCount==0){
					if(systemManagerArray.length()==0)
					{
						startBasicNetworkScanner(false,netscanData);
					}
					else
					{
						startSysManagerNetworkScanner(false,netscanData,systemManagerArray);
					}
				}else{
					if(systemManagerArray.length()==0){
						startBasicNetworkScanner(true,netscanData);
					}else{
						startSysManagerNetworkScanner(true,netscanData,systemManagerArray);
					}
					
				}*/
			}
		}catch(Exception E){
			fileLogger.error("Exception in method:NetscanJob  class:executeNetscanJob :"+E.getMessage());
		}
		fileLogger.info("Exit Function : executeNetscanJob");
	}
	
	public void startBasicNetworkScanner(boolean switchAvailableStatus,JSONObject netscanData){
		fileLogger.info("Inside Function : startBasicNetworkScanner");
		//fileLogger.debug("in startBasicNetworkScanner");
		Operations operations = new Operations();
		NetscanOperations netscanOperations = new NetscanOperations();
		LinkedHashMap<String,String> param = new LinkedHashMap<String,String>();
		CommonService commonService = new CommonService();
		//stopping netscanner if in running state
		try {
			JSONArray omniAntennaArray=operations.getJson("select * from antenna where atype='3' and inscanning is true order by id");
			if(omniAntennaArray.length()!=0){
			JSONObject omniAntennaObject=omniAntennaArray.getJSONObject(0);
			//data structure to send to start netscanner
			LinkedHashMap<String,String> parameter = new LinkedHashMap<String,String>();
			parameter.put("cmdType", "START_EXHAUSTIVE_SCAN");
			parameter.put("systemCode", "3");
		    parameter.put("systemIP",netscanData.getString("ip"));
		    parameter.put("systemId",netscanData.getString("sytemid"));
		    parameter.put("id",netscanData.getString("b_id"));
		    String config=netscanData.getString("config");
			JSONObject scanDataObj=new JSONObject(config);
			scanDataObj.put("REPETITION_FLAG","0");
			scanDataObj.put("REPITITION_FREQ","0");
			String scanDataStr=commonService.getScanJsonInOrder(scanDataObj,"start");
		    parameter.put("data", scanDataStr);
		    int deviceId=netscanData.getInt("b_id");
	    	//switchSectorAntenna((i+1),'Netscanner',''); //switch to antenna 1	
	    	new CurrentNetscanAlarm();
			netscanOperations.sendToServer(parameter);
			new Common().executeDLOperation("update btsmaster set active_antenna_id="+omniAntennaObject.getInt("id")+" where b_id="+deviceId);
			String currentDesc="";
			while(true){
				try{
					if(CurrentNetscanAlarm.desc!=null && currentDesc.equals(CurrentNetscanAlarm.desc)){
						fileLogger.debug("desc in loop is :"+CurrentNetscanAlarm.desc);
					}else{
						Thread.sleep(100);
					}
				}catch(Exception E){
					fileLogger.error("exception in waiting alarm or thread sleep in startBasicNetworkScanner");
				}
	   			if((CurrentNetscanAlarm.desc!=null && CurrentNetscanAlarm.desc.contains("Scan Successfully stopped  for EXHAUSTIVE_SCAN")) || (CurrentNetscanAlarm.opTech!=null && CurrentNetscanAlarm.opTech.toLowerCase().contains("none"))) 
	   			{
	   				///new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Scanning Completed\"}");
	   				fileLogger.debug("successfully stopped exhaustive scan alarm occured at time:"+new Date());
	   				new Common().executeDLOperation("update btsmaster set active_antenna_id=null where b_id="+deviceId);
	   				break;
	   			}
			}
			scanDataObj.put("REPETITION_FLAG","1");
	   		scanDataObj.put("REPITITION_FREQ","10");
			scanDataStr=commonService.getScanJsonInOrder(scanDataObj,"start");
			parameter.put("data", scanDataStr);
			netscanOperations.sendToServer(parameter);
			omniAntennaArray=operations.getJson("select * from antenna where atype='3' and inscanning is true order by id");
			omniAntennaObject=omniAntennaArray.getJSONObject(0);
			new Common().executeDLOperation("update btsmaster set active_antenna_id="+omniAntennaObject.getInt("id")+" where b_id="+deviceId);
		}
		}  catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		fileLogger.info("Exit Function : startBasicNetworkScanner");
	}
	
	public void startSysManagerNetworkScanner(int switchCount,JSONObject netscanData,JSONArray systemManagerArray) throws IOException{
		fileLogger.info("Inside Function : startSysManagerNetworkScanner");
		ScanTrackModeServer scanTrackModeServer = new ScanTrackModeServer();
		//fileLogger.debug("in startSysManagerNetworkScanner");
		Operations operations = new Operations();
		NetscanOperations netscanOperations = new NetscanOperations();
		CommonService commonService = new CommonService();
		HashMap<String,ArrayList<HashMap<String,String>>> devicesMapOverTech = operations.getAllBtsInfoByTech();
		try {	
			
			boolean switchUsed=false;
			int systemTypeCode=operations.getJson("select code from system_properties where key='system_type'").getJSONObject(0).getInt("code");
			if(systemTypeCode==1 || systemTypeCode==2 || systemTypeCode==0){
				switchUsed=true;
			}
			
			
			//JSONObject systemManagerObject=systemManagerArray.getJSONObject(0);
/*			LinkedHashMap<String,String> netscanMap= new LinkedHashMap<String,String>();
			netscanMap.put("systemIP", systemManagerObject.getString("ip"));
			netscanMap.put("data", "{\"CMD_CODE\":\"GET_SYSTEM_STATUS\"}");
			netscanMap.put("CMD_TYPE", "GET_CURR_STATUS");
			netscanMap.put("SYSTEM_CODE", systemManagerObject.getString("code"));
			netscanMap.put("SYSTEM_ID", systemManagerObject.getString("sytemid"));
			String netscanStatusStr=netscanOperations.sendCommandToNetscanServer(netscanMap);
			fileLogger.debug("getstatus sent");
			JSONObject netscanStatus = new JSONObject(netscanStatusStr);*/
			JSONArray omniAntennaArray=operations.getJson("select * from antenna where atype='3' and inscanning is true order by id");
			//JSONObject omniAntennaObject=omniAntennaArray.getJSONObject(0);
			JSONArray sectorAntennaArray=operations.getJson("select * from antenna where atype='1' and inscanning is true order by id");
			LinkedHashMap<String,String> parameter = new LinkedHashMap<String,String>();
			parameter.put("cmdType", "START_EXHAUSTIVE_SCAN");
			parameter.put("systemCode", "3");
		    parameter.put("systemIP",netscanData.getString("ip"));
		    parameter.put("systemId",netscanData.getString("sytemid"));
		    parameter.put("id",netscanData.getString("b_id"));
		    String config=netscanData.getString("config");
			JSONObject scanDataObj=new JSONObject(config);
			scanDataObj.put("REPETITION_FLAG","0");
			scanDataObj.put("REPITITION_FREQ","0");
			String scanDataStr=commonService.getScanJsonInOrder(scanDataObj,"start");
		    parameter.put("data", scanDataStr);
			boolean trackingStatusOnDsp0 = false;
			//respawn scanner
			if(!DBDataService.statusScannerRestart) {
				respawnScanner(netscanOperations);
			}
			DBDataService.statusScannerRestart=false;
			if(switchUsed && (sectorAntennaArray.length()!=0)){

				trackingStatusOnDsp0= netscanOperations.isTrackingRunning();
			}
			
			fileLogger.debug("@scanscheduler before sending scanning command");
	    	if(NetscanSchedulerListener.isScannerInterrupted){
	    		fileLogger.debug("scanscheduler interrupted 1");
	    		NetscanSchedulerListener.isScannerInterrupted=false;
	    		return;
	    	}
			
			if(!trackingStatusOnDsp0){
				//LinkedHashMap<String,String> netscanDspMap= new LinkedHashMap<String,String>();
/*				if((switchAvailableStatus==true) && (sectorAntennaArray.length()!=0)){
					fileLogger.debug("@scan about to dsp switch");
					netscanDspMap.put("systemIP", systemManagerObject.getString("ip"));
					netscanDspMap.put("data", "{\"CMD_CODE\":\"SET_SYSTEM_CONFIG\",\"OPERATION\":\"1\",\"DSP0_NODE\":\"1\",\"DSP1_NODE\":\"3\"}");
					netscanDspMap.put("CMD_TYPE", "SET_SYSTEM_CONFIG");
					netscanDspMap.put("SYSTEM_CODE",systemManagerObject.getString("code"));
					netscanDspMap.put("SYSTEM_ID", systemManagerObject.getString("sytemid"));
					netscanOperations.sendCommandToNetscanServer(netscanDspMap);
				}*/
				new Common().executeDLOperation("update btsmaster set active_antenna_id=null where b_id="+netscanData.getInt("b_id"));
				scanTrackModeServer.sendText("scan&Idle");
/*				LinkedHashMap<String,String> parameter = new LinkedHashMap<String,String>();
				parameter.put("cmdType", "START_EXHAUSTIVE_SCAN");
				parameter.put("systemCode", "3");
			    parameter.put("systemIP",netscanData.getString("ip"));
			    parameter.put("systemId",netscanData.getString("sytemid"));
			    parameter.put("id",netscanData.getString("b_id"));
			    String config=netscanData.getString("config");
				JSONObject scanDataObj=new JSONObject(config);
				scanDataObj.put("REPETITION_FLAG","0");
				scanDataObj.put("REPITITION_FREQ","0");
				String scanDataStr=commonService.getScanJsonInOrder(scanDataObj,"start");
			    parameter.put("data", scanDataStr);*/
			    int deviceId=netscanData.getInt("b_id");
						fileLogger.debug("@mia sector length :"+sectorAntennaArray.length());
						fileLogger.debug("@mia omni length :"+omniAntennaArray.length());
					if(sectorAntennaArray.length()!=0){
						scanDataObj.put("REPETITION_FLAG","0");
				   		scanDataObj.put("REPITITION_FREQ","0");
				   		fileLogger.debug("@scanscheduler about to go for sector antenna");
				    for(int i=0;i<sectorAntennaArray.length();i++){
				    	if(NetscanSchedulerListener.isScannerInterrupted){
				    		fileLogger.debug("scanscheduler interrupted 2");
				    		NetscanSchedulerListener.isScannerInterrupted=false;
				    		return;
				    	}
				    	fileLogger.debug("@mia in sector antenna array switching");
				    	//switchSectorAntenna((i+1),'Netscanner',''); //switch to antenna 1	
				    	if(i!=0 && !switchUsed){
				    		break;
				    	}
				    	JSONObject trackModeObject = new Operations().getJson("select * from running_mode where mode_type='track'").getJSONObject(0);
				    	String modeStatus = trackModeObject.getString("mode_status");
				    	boolean isTrackingOnSector=false;
				    	if((!modeStatus.equalsIgnoreCase("idle") && !trackModeObject.getString("applied_antenna").equalsIgnoreCase("ov1")) || (DBDataService.stopScanFromTrackPart)){
				    		isTrackingOnSector = true;
				    	}
				    	if(!isTrackingOnSector){
				    	int antennaId = sectorAntennaArray.getJSONObject(i).getInt("id");
				    	
				    	int sectorId = new CommonService().getSectorFromAntennaId(antennaId);
				    	if(NetscanSchedulerListener.isScannerInterrupted){
				    		fileLogger.debug("scanscheduler interrupted 3");
				    		NetscanSchedulerListener.isScannerInterrupted=false;
				    		return;
				    	}
				    	String falconType=DBDataService.configParamMap.get("falcontype");
				    	if(falconType.equalsIgnoreCase("standard")){
				    		HashMap<String,String> statusMap=new ApiCommon().lockUnlockAllDevicesOnAutoTrack(devicesMapOverTech,1);
				    		if(NetscanSchedulerListener.isScannerInterrupted){
				    			fileLogger.debug("scanscheduler interrupted 4");
				    			NetscanSchedulerListener.isScannerInterrupted=false;
				    			return;
				    		}
				    		if(statusMap.get("status").equals("failure")){
				    			new AutoOperationServer().sendText("{\"result\":\"fail\",\"msg\":\"Connection Error on Locking "+statusMap.get("ip")+"\"}");
				    		}
				    	}else{
				    		//commented by Vaibhav on 9Dec 11:51 AM as not req tracking not running already
				    		//new ApiCommon().lockUnlockAllDevices(devicesMapOverTech, 1);
					    	if(NetscanSchedulerListener.isScannerInterrupted){
					    		fileLogger.debug("scanscheduler interrupted 3");
					    		NetscanSchedulerListener.isScannerInterrupted=false;
					    		return;
					    	}
				    	}
						
				    	boolean isSucceded=true;
				    	isSucceded=new ApiCommon().switchAntena("0", "scanner", "8", sectorId); //switch to sector antenna in loop from sector 1 to 3
				    	if(NetscanSchedulerListener.isScannerInterrupted){
				    		fileLogger.debug("scanscheduler interrupted 5");
				    		NetscanSchedulerListener.isScannerInterrupted=false;
				    		return;
				    	}
				    	if(NetscanSchedulerListener.isScannerInterrupted){
				    		isSucceded=new ApiCommon().switchAntena("NA", "scanner", "7", sectorId); //switch to sector antenna in loop from sector 1 to 3
				    		fileLogger.debug("scanscheduler interrupted 6");
				    		NetscanSchedulerListener.isScannerInterrupted=false;
				    		return;
				    	}
				    	
				    	new CurrentNetscanAlarm();
				    	if(NetscanSchedulerListener.isScannerInterrupted){
				    		fileLogger.debug("scanscheduler interrupted 7");
				    		NetscanSchedulerListener.isScannerInterrupted=false;
				    		return;
				    	}
						netscanOperations.sendToServer(parameter);
				    	if(NetscanSchedulerListener.isScannerInterrupted){
				    		fileLogger.debug("scanscheduler interrupted 8");
				    		NetscanSchedulerListener.isScannerInterrupted=false;
				    		return;
				    	}
						//scanTrackModeServer.sendText("scan&Active&Sector"+(i+1));
						JSONObject sectorAntennaObject = sectorAntennaArray.getJSONObject(i);
						new Common().executeDLOperation("update btsmaster set active_antenna_id="+sectorAntennaObject.getInt("id")+" where b_id="+deviceId);
						String antennaProfileName = operations.getJson("select profile_name from antenna where id="+sectorAntennaObject.getInt("id")).getJSONObject(0).getString("profile_name");
						new ScanTrackModeServer().sendText("scan&Active&"+antennaProfileName);
						String currentDesc="";
						while(true){
							try{
						    	if(NetscanSchedulerListener.isScannerInterrupted){
						    		fileLogger.debug("scanscheduler interrupted 9");
						    		NetscanSchedulerListener.isScannerInterrupted=false;
						    		return;
						    	}
								if(CurrentNetscanAlarm.desc!=null && !currentDesc.equals(CurrentNetscanAlarm.desc)){
									fileLogger.debug("desc in loop is :"+CurrentNetscanAlarm.desc);
									currentDesc=CurrentNetscanAlarm.desc;
									
								}else{
									Thread.sleep(100);
								}
							}catch(Exception E){
								
							}
				   			if((CurrentNetscanAlarm.desc!=null && CurrentNetscanAlarm.desc.contains("Scan Successfully stopped  for EXHAUSTIVE_SCAN"))) 
				   			{
				   				//new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Scanning Completed\"}");
				   				fileLogger.debug("successfully stopped exhaustive scan alarm occured at time:"+new Date());
				   				new Common().executeDLOperation("update btsmaster set active_antenna_id=null where b_id="+deviceId);
				   				scanTrackModeServer.sendText("scan&Idle");
				   				try {
									Thread.sleep(2000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
				   				break;
				   			}else if((CurrentNetscanAlarm.opTech!=null && CurrentNetscanAlarm.opTech.toLowerCase().contains("none"))){
				   				i--;
				   				break;
				   			}
						}
						fileLogger.debug("@mia out sector antenna array switching");
				    }
				    }
				    fileLogger.debug("@mia for loop end");
				    fileLogger.debug("@mia omniAntennaArray.length() again:"+omniAntennaArray.length());
				    if(omniAntennaArray.length()!=0){
				    	if(NetscanSchedulerListener.isScannerInterrupted){
				    		fileLogger.debug("scanscheduler interrupted 10");
				    		NetscanSchedulerListener.isScannerInterrupted=false;
				    		return;
				    	}
						scanDataObj.put("REPETITION_FLAG","1");
						scanDataObj.put("REPITITION_FREQ","10");
						fileLogger.debug("In switch for omni");
						scanDataStr=commonService.getScanJsonInOrder(scanDataObj,"start");
						fileLogger.debug("json coming for omni scan");
						parameter.put("data", scanDataStr);
						int antennaId = omniAntennaArray.getJSONObject(0).getInt("id");
				    	
				    	int sectorId = new CommonService().getSectorFromAntennaId(antennaId);
				    	fileLogger.debug("sector id coming for antenna id :"+sectorId);
				    	
				    	new ApiCommon().switchAntena("0", "scanner", "8", sectorId); //switch to sector antenna in loop from sector 1 to 3
				    	if(NetscanSchedulerListener.isScannerInterrupted){
				    		fileLogger.debug("scanscheduler interrupted 11");
				    		NetscanSchedulerListener.isScannerInterrupted=false;
				    		return;
				    	}
				    	new ApiCommon().switchAntena("NA", "scanner", "7", sectorId); //switch to sector antenna in loop from sector 1 to 3
				    	if(NetscanSchedulerListener.isScannerInterrupted){
				    		fileLogger.debug("scanscheduler interrupted 12");
				    		NetscanSchedulerListener.isScannerInterrupted=false;
				    		return;
				    	}
						
						//switchAntenna();//switch omni antenna
						
						netscanOperations.sendToServer(parameter);
				    	if(NetscanSchedulerListener.isScannerInterrupted){
				    		fileLogger.debug("scanscheduler interrupted 13");
				    		NetscanSchedulerListener.isScannerInterrupted=false;
				    		return;
				    	}
						//scanTrackModeServer.sendText("scan&Active&Omni");
						omniAntennaArray=operations.getJson("select * from antenna where atype='3' and inscanning is true order by id");
						JSONObject omniAntennaObject=omniAntennaArray.getJSONObject(0);
						new Common().executeDLOperation("update btsmaster set active_antenna_id="+omniAntennaObject.getInt("id")+" where b_id="+deviceId);
						String antennaProfileName = operations.getJson("select profile_name from antenna where id="+omniAntennaObject.getInt("id")).getJSONObject(0).getString("profile_name");
						new ScanTrackModeServer().sendText("scan&Active&"+antennaProfileName);
				    }
					}else if(omniAntennaArray.length()!=0){
				    	if(NetscanSchedulerListener.isScannerInterrupted){
				    		fileLogger.debug("scanscheduler interrupted 14");
				    		NetscanSchedulerListener.isScannerInterrupted=false;
				    		return;
				    	}
						scanDataObj.put("REPETITION_FLAG","0");
						scanDataObj.put("REPITITION_FREQ","0");
						
					int antennaId = omniAntennaArray.getJSONObject(0).getInt("id");
				    	
				    	int sectorId = new CommonService().getSectorFromAntennaId(antennaId);
				    	fileLogger.debug("sector id coming for antenna id :"+sectorId);
				    	
				    	new ApiCommon().switchAntena("0", "scanner", "8", sectorId); //switch to sector antenna in loop from sector 1 to 3
				    	if(NetscanSchedulerListener.isScannerInterrupted){
				    		fileLogger.debug("scanscheduler interrupted 11");
				    		NetscanSchedulerListener.isScannerInterrupted=false;
				    		return;
				    	}
				    	new ApiCommon().switchAntena("NA", "scanner", "7", sectorId); //switch to sector antenna in loop from sector 1 to 3
				    	if(NetscanSchedulerListener.isScannerInterrupted){
				    		fileLogger.debug("scanscheduler interrupted 12");
				    		NetscanSchedulerListener.isScannerInterrupted=false;
				    		return;
				    	}
						
						//switchAntenna();//switch omni antenna
						
						netscanOperations.sendToServer(parameter);
						
				    	new CurrentNetscanAlarm();
						netscanOperations.sendToServer(parameter);
				    	if(NetscanSchedulerListener.isScannerInterrupted){
				    		fileLogger.debug("scanscheduler interrupted 15");
				    		NetscanSchedulerListener.isScannerInterrupted=false;
				    		return;
				    	}
						//scanTrackModeServer.sendText("scan&Active&Omni");
						JSONObject omniAntennaObject=omniAntennaArray.getJSONObject(0);
						new Common().executeDLOperation("update btsmaster set active_antenna_id="+omniAntennaObject.getInt("id")+" where b_id="+deviceId);
						String antennaProfileName = operations.getJson("select profile_name from antenna where id="+omniAntennaObject.getInt("id")).getJSONObject(0).getString("profile_name");
						new ScanTrackModeServer().sendText("scan&Active&"+antennaProfileName);
						String currentDesc="";
						while(true){
							try{
						    	if(NetscanSchedulerListener.isScannerInterrupted){
						    		fileLogger.debug("scanscheduler interrupted 16");
						    		NetscanSchedulerListener.isScannerInterrupted=false;
						    		return;
						    	}
								if(CurrentNetscanAlarm.desc!=null && currentDesc.equals(CurrentNetscanAlarm.desc)){
									fileLogger.debug("desc in loop is :"+CurrentNetscanAlarm.desc);
								}else{
									Thread.sleep(100);
								}
							}catch(Exception E){
								fileLogger.error("exception  occurs in internal startSysManagerNetworkScanner with message :"+E.getMessage());
							}
				   			if((CurrentNetscanAlarm.desc!=null && CurrentNetscanAlarm.desc.contains("Scan Successfully stopped  for EXHAUSTIVE_SCAN")) || (CurrentNetscanAlarm.opTech!=null && CurrentNetscanAlarm.opTech.toLowerCase().contains("none"))) 
				   			{
				   				//new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Scanning Completed\"}");
				   				fileLogger.debug("successfully stopped exhaustive scan alarm occured at time:"+new Date());
								new Common().executeDLOperation("update btsmaster set active_antenna_id=null where b_id="+deviceId);
				   				scanTrackModeServer.sendText("scan&Idle");
				   				try {
									Thread.sleep(2000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
				   				break;
				   			}
						}
						scanDataObj.put("REPETITION_FLAG","1");
				   		scanDataObj.put("REPITITION_FREQ","10");
						scanDataStr=commonService.getScanJsonInOrder(scanDataObj,"start");
						parameter.put("data", scanDataStr);
						netscanOperations.sendToServer(parameter);
						//scanTrackModeServer.sendText("scan&Active&Omni");
						omniAntennaArray=operations.getJson("select * from antenna where atype='3' and inscanning is true order by id");
						omniAntennaObject=omniAntennaArray.getJSONObject(0);
						new Common().executeDLOperation("update btsmaster set active_antenna_id="+omniAntennaObject.getInt("id")+" where b_id="+deviceId);
						antennaProfileName = operations.getJson("select profile_name from antenna where id="+omniAntennaObject.getInt("id")).getJSONObject(0).getString("profile_name");
						new ScanTrackModeServer().sendText("scan&Active&"+antennaProfileName);
					}
/*				    if((switchAvailableStatus==true) && (sectorAntennaArray.length()!=0)){
				    }else{
						netscanDspMap.put("data", "{\"CMD_CODE\":\"SET_SYSTEM_CONFIG\",\"OPERATION\":\"1\",\"DSP0_NODE\":\"3\",\"DSP1_NODE\":\"1\"}");
						netscanOperations.sendCommandToNetscanServer(netscanMap);	
				    }*/
			}else{
				
			    fileLogger.debug("@mia omniAntennaArray.length() again:"+omniAntennaArray.length());
			    if(omniAntennaArray.length()!=0){
			    	if(NetscanSchedulerListener.isScannerInterrupted){
			    		fileLogger.debug("scanscheduler interrupted 17");
			    		NetscanSchedulerListener.isScannerInterrupted=false;
			    		return;
			    	}
					scanDataObj.put("REPETITION_FLAG","1");
					scanDataObj.put("REPITITION_FREQ","10");
					fileLogger.debug("In switch for omni");
					scanDataStr=commonService.getScanJsonInOrder(scanDataObj,"start");
					fileLogger.debug("json coming for omni scan");
					parameter.put("data", scanDataStr);
					int antennaId = omniAntennaArray.getJSONObject(0).getInt("id");
			    	
			    	int sectorId = new CommonService().getSectorFromAntennaId(antennaId);
			    	fileLogger.debug("sector id coming for antenna id :"+sectorId);
			    	
			    	// Unnesscary command to OMNI 
			    /*	new ApiCommon().switchAntena("0", "scanner", "8", sectorId); //switch to sector antenna in loop from sector 1 to 3
			    	if(NetscanSchedulerListener.isScannerInterrupted){
			    		fileLogger.debug("scanscheduler interrupted 18");
			    		NetscanSchedulerListener.isScannerInterrupted=false;
			    		return;
			    	}
			    	new ApiCommon().switchAntena("NA", "scanner", "7", sectorId); //switch to sector antenna in loop from sector 1 to 3
			    	if(NetscanSchedulerListener.isScannerInterrupted){
			    		fileLogger.debug("scanscheduler interrupted 19");
			    		NetscanSchedulerListener.isScannerInterrupted=false;
			    		return;
			    	}*/
					
					//switchAntenna();//switch omni antenna
					
					netscanOperations.sendToServer(parameter);
			    	if(NetscanSchedulerListener.isScannerInterrupted){
			    		fileLogger.debug("scanscheduler interrupted 20");
			    		NetscanSchedulerListener.isScannerInterrupted=false;
			    		return;
			    	}
					//scanTrackModeServer.sendText("scan&Active&Omni");
					omniAntennaArray=operations.getJson("select * from antenna where atype='3' and inscanning is true order by id");
					JSONObject omniAntennaObject=omniAntennaArray.getJSONObject(0);
					new Common().executeDLOperation("update btsmaster set active_antenna_id="+omniAntennaObject.getInt("id")+" where b_id="+netscanData.getInt("b_id"));
					String antennaProfileName = operations.getJson("select profile_name from antenna where id="+omniAntennaObject.getInt("id")).getJSONObject(0).getString("profile_name");
					new ScanTrackModeServer().sendText("scan&Active&"+antennaProfileName);
			    }
				fileLogger.debug("Tracking is going on.");
			}
			fileLogger.debug("out startSysManagerNetworkScanner");
		} catch (JSONException e) {
			fileLogger.error("exception  occurs in outer startSysManagerNetworkScanner with message :"+e.getMessage());
		}
		fileLogger.info("Inside Exit : startSysManagerNetworkScanner");
	}
	
	
	public void startCompleteBasicNetworkScanner(){
		fileLogger.info("Inside Function : startCompleteBasicNetworkScanner");
		//fileLogger.debug("in startBasicNetworkScanner");
		Operations operations = new Operations();
		NetscanOperations netscanOperations = new NetscanOperations();
		LinkedHashMap<String,String> param = new LinkedHashMap<String,String>();
		CommonService commonService = new CommonService();
		//stopping netscanner if in running state
		JSONArray netscanArray = operations.getJson("select * from view_btsinfo where code=3");
		try {
			JSONObject netscanData = netscanArray.getJSONObject(0);
			JSONArray omniAntennaArray=operations.getJson("select * from antenna where atype='3' and inscanning is true order by id");
			if(omniAntennaArray.length()!=0){
			JSONObject omniAntennaObject=omniAntennaArray.getJSONObject(0);
			//data structure to send to start netscanner
			LinkedHashMap<String,String> parameter = new LinkedHashMap<String,String>();
			parameter.put("cmdType", "START_EXHAUSTIVE_SCAN");
			parameter.put("systemCode", "3");
		    parameter.put("systemIP",netscanData.getString("ip"));
		    parameter.put("systemId",netscanData.getString("sytemid"));
		    parameter.put("id",netscanData.getString("b_id"));
		    String config=netscanData.getString("config");
			JSONObject scanDataObj=new JSONObject(config);
			scanDataObj.put("REPETITION_FLAG","1");
			scanDataObj.put("REPITITION_FREQ","10");
			String scanDataStr=commonService.getScanJsonInOrder(scanDataObj,"start");
		    parameter.put("data", scanDataStr);
		    int deviceId=netscanData.getInt("b_id");
	    	//switchSectorAntenna((i+1),'Netscanner',''); //switch to antenna 1	
	    	new CurrentNetscanAlarm();
			netscanOperations.sendToServer(parameter);
			new Common().executeDLOperation("update btsmaster set active_antenna_id="+omniAntennaObject.getInt("id")+" where b_id="+deviceId);
			String currentDesc="";
			while(true){
				try{
					if(CurrentNetscanAlarm.desc!=null && currentDesc.equals(CurrentNetscanAlarm.desc)){
						fileLogger.debug("desc in loop is :"+CurrentNetscanAlarm.desc);
					}else{
						Thread.sleep(100);
					}
				}catch(Exception E){
					
				}
	   			if((CurrentNetscanAlarm.desc!=null && CurrentNetscanAlarm.desc.contains("Scan Successfully stopped  for EXHAUSTIVE_SCAN")) || (CurrentNetscanAlarm.opTech!=null && CurrentNetscanAlarm.opTech.toLowerCase().contains("none"))) 
	   			{
	   				//new AutoOperationServer().sendText("{\"result\":\"success\",\"msg\":\"Scanning Completed\"}");
	   				fileLogger.debug("successfully stopped exhaustive scan alarm occured at time:"+new Date());
	   				break;
	   			}
			}
			scanDataObj.put("REPETITION_FLAG","0");
	   		scanDataObj.put("REPITITION_FREQ","0");
			scanDataStr=commonService.getScanJsonInOrder(scanDataObj,"start");
			parameter.put("data", scanDataStr);
			netscanOperations.sendToServer(parameter);
			omniAntennaArray=operations.getJson("select * from antenna where atype='3' and inscanning is true order by id");
			omniAntennaObject=omniAntennaArray.getJSONObject(0);
			new Common().executeDLOperation("update btsmaster set active_antenna_id="+omniAntennaObject.getInt("id")+" where b_id="+deviceId);
		}
		}  catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fileLogger.info("Exit Function : startCompleteBasicNetworkScanner");
	}
			
	public void respawnScanner(NetscanOperations netscanOperations ){
		DBDataService.scannerManuallyStopped=true;
		
		try
		{
			if(!netscanOperations.isTrackingRunning())
				{
				
				    ApiCommon apicommon=new ApiCommon();
//				    apicommon.switchDsp("stop_scanner",0,0);
				    fileLogger.debug("@respawnScanner restart being called" );
				    Operations operations = new Operations();
				    
				    JSONArray netscanArray = operations.getJson("select * from view_btsinfo where code=3;");

				    String SystemManagerToSwitchDSP="";
					try {
						SystemManagerToSwitchDSP=netscanArray.getJSONObject(0).getString("systemmanager");
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				    String respData=apicommon.switchDsp("restart",99,1,999,SystemManagerToSwitchDSP);
				    

				    
					fileLogger.debug("@respawnScanner restart  called done" );
					fileLogger.debug("@sendToServer method restarting scanner" );
				//	Thread.sleep(Long.parseLong(DBDataService.configParamMap.get("sysmanwait")));
					//fileLogger.debug("@sendToServer method starting scanner" );
//					apicommon.switchDsp("start_scanner",0,0);
					fileLogger.debug("@sendToServer method started scanner and going to wait for "+DBDataService.configParamMap.get("ScannerTimetoUp"));
					Thread.sleep(Long.parseLong(DBDataService.configParamMap.get("ScannerTimetoUp")));
					fileLogger.debug("@sendToServer out from wait");
					
					
					try {
						int switchCount=operations.getJson("select count(*) count from view_btsinfo where code=8 limit 1").getJSONObject(0).getInt("count");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					JSONArray systemManagerArray = operations.getJson("select * from view_btsinfo where code=10 limit 1");
					
					CommonService commonService = new CommonService();
					//NetscanOperations netscanOperations = new NetscanOperations();
					
					JSONObject netscanData=null;
					try {
						netscanData = netscanArray.getJSONObject(0);
						fileLogger.debug("@netscanData= " +netscanData);
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String ipScanner="";
					try {
						ipScanner=netscanData.getString("ip");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					fileLogger.debug("@ipScanner= " +ipScanner);
					
					fileLogger.debug("@Updating Btsstatus of scanner now" );
					
					new TwogOperations().updateStatusOfBts(ipScanner);
					fileLogger.debug("@Updating Btsstatus of scanner out " );
				}
				else
				{
					fileLogger.debug("@sendToServer : cann't re-spawn scanner as tracking already running.");	
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				fileLogger.error("@sendToServer out from wait ERROR ="+ e.getMessage() );
				DBDataService.scannerManuallyStopped=false;
				e.printStackTrace();
			}
		DBDataService.scannerManuallyStopped=false;
		
	}
}
