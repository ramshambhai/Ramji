package in.vnl.bist;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.vnl.api.common.ApiCommon;
import in.vnl.api.common.CommonService;
import in.vnl.api.netscan.NetscanOperations;
import in.vnl.api.threeg.ThreegOperations;
import in.vnl.msgapp.Common;
import in.vnl.msgapp.Operations;
public class Bist
{
	static Logger fileLogger = Logger.getLogger("file");
	private static Bist obj = null;
	
	
	HashMap<String,ArrayList<HashMap<String,String>>> devicesMapOverTech = null;
	HashMap<String,String> hm2g=null;
	HashMap<String,String> hm3g=null;
	String imsi = null;
	String imei = null;
	String plmn = null;
	String lac = null;
	String cell = null;
	String fcn = null;
	public boolean cdr = false;
	public boolean netscan = false;
	
	boolean result = false;
	/*HashMap<String,String> hm=devicesMapOverTech.get("2G").get(0);
	new CommonService().setLockUnlock(hm.get("ip"),"2");*/
	private Bist(){}
	
	public static Bist getInstance() 
	{
		
		if(obj == null) 
		{
			obj= new Bist();
			
		}
		
		
		return obj;
	}
	
	
	public String getFileContent( FileInputStream fis ) throws IOException {
	    StringBuilder sb = new StringBuilder();
	    Reader r = new InputStreamReader(fis, "UTF-8");  //or whatever encoding
	    char[] buf = new char[1024];
	    int amt = r.read(buf);
	    while(amt > 0) {
	        sb.append(buf, 0, amt);
	        amt = r.read(buf);
	    }
	    return sb.toString();
	}
	
	
	public void setupBistTestCases(JSONArray ja) throws JSONException 
	{
		for(int i=0;i<ja.length();i++) 
		{
			
			
			JSONObject jo = ja.getJSONObject(i);
			
			
			String lac = jo.getString("lac");
			//String mcc = jo.getString("mcc");
			//String mnc = jo.getString("mnc");
			String plmn = jo.getString("plmn");
			String cell = jo.getString("cell");
			String bsic = jo.getString("bsic");
			String rssi = jo.getString("rssi");
			String imsi = jo.getString("imsi");
			String imei = jo.getString("imei");
			String band = jo.getString("band");
			String arfcn = jo.getString("arfcn");
			String uarfcn = jo.getString("uarfcn");
			String psc = jo.getString("psc");
			
			StringBuilder jsonObjet = new StringBuilder();
			jsonObjet.append("{");
			jsonObjet.append("\"band\":\""+band+"\",");
			jsonObjet.append("\"psc\":\""+psc+"\",");
			jsonObjet.append("\"uarfcn\":\""+uarfcn+"\",");
			jsonObjet.append("\"plmn\":\""+plmn+"\",");
			jsonObjet.append("\"bsic\":\""+bsic+"\",");
			jsonObjet.append("\"arfcn\":\""+arfcn+"\",");
			jsonObjet.append("\"cell\":\""+cell+"\",");
			jsonObjet.append("\"rssi\":\""+rssi+"\",");
			jsonObjet.append("\"lac\":\""+lac+"\"");
			jsonObjet.append("}");
			
			
			
			String query = "update bist_tests set "
							+ "PLMN='"+plmn+
							"' ,lac='"+lac+
							"' ,cell ='"+cell+
							"' ,imsi='"+imsi+
							"', imei='"+imei+
							"', config = jsonb_set(config,'{0,data,0}',config#>'{0,data,0}' || '"+jsonObjet.toString()+"') "
							+ " where band = '"+jo.getString("band")+"' and tech ='"+jo.getString("tech")+"';";
			new Common().executeDLOperation(query);
		}
	}
	
	
	public void intializeBist() 
	{	
		devicesMapOverTech = new Operations().getAllBtsInfoByTech();
		hm2g=devicesMapOverTech.get("2G").get(0);
		hm3g=devicesMapOverTech.get("3G").get(0);
		//new ApiCommon().lockUnlockAllDevices(devicesMapOverTech,1);
		
		updateTestResult(-1,"FAIL");
		switchDsp("sufi");
		stopAllScan();
	}
	 
	 /**
	  * Update test all results	  * 
	  *  */
	 public void updateTestResult(int id,String result) 
	 {
		String query = "update bist_tests set result = '"+result+"' where id = "+id+";";
		if(id == -1)
		{
			query = "update bist_tests set result = '"+result+"';";
		}
		new Common().executeDLOperation(query);
	 }
	 
	/**
	* Start or stop network scanner
	* */	 
	public void startStopNetworkScanner(int flag,String tech,String band,String fcn,String scanType) 
	{
		NetscanOperations netscanOperations=new NetscanOperations();
		LinkedHashMap<String,String> param = new LinkedHashMap<String,String>();
		JSONObject netscanData=null;
		CommonService commonService = new CommonService();
		try
		{
			netscanData =  new Operations().getJson("select * from view_btsinfo where code=3").getJSONObject(0);

		if(flag == 0){
			param = prepareParamForStopNetscan(netscanData,tech,scanType);
			netscanOperations.sendToServer(param);
		}else{
			LinkedHashMap<String,String> parameter = new LinkedHashMap<String,String>();
			parameter.put("cmdType", "START_CELL_SCAN");
			parameter.put("systemCode", "3");
		    parameter.put("systemIP",netscanData.getString("ip"));
		    parameter.put("systemId",netscanData.getString("sytemid"));
		    parameter.put("id",netscanData.getString("b_id"));
            String config="{\"CMD_CODE\":\"START_CELL_SCAN\",\"NEIGHBOR_SCAN\":0,\"RSSI_THRESHOLD\":-110,\"REPETITION_FLAG\":0,\"REPITITION_FREQ\":null,\"TECH\":\"GSM\",\"FREQ_INFO\":[{\"BAND\":\"E_900\",\"FREQ_LIST\":[{\"FREQ\":70}]}]}";
		    JSONObject scanDataObj=new JSONObject(config);
			scanDataObj.put("REPETITION_FLAG","0");
			scanDataObj.put("REPITITION_FREQ","0");
			scanDataObj.put("TECH",tech);
			String scanDataStr=getScanJsonInOrder(scanDataObj,"start",band,fcn);
		    parameter.put("data", scanDataStr);
		    netscanOperations.sendToServer(parameter);
			
		}
		//@vishal to start and stop 
		}
		catch(Exception E)
		{
			
		}
	}
	
	   public String getScanJsonInOrder(JSONObject scanDataObj,String scanType,String band,String fcn){
			fileLogger.info("Inside Function : getScanJsonInOrder");		
		   String jsonStringInOrder="";
		   try{
		   if(scanType.equals("start")){
			   //jsonStringInOrder+="{\"CMD_CODE\":\"START_CELL_SCAN\",\"RSSI_THRESHOLD\":"+scanDataObj.getString("RSSI_THRESHOLD")+",\"REPETITION_FLAG\":"+scanDataObj.getString("REPETITION_FLAG")+",\"REPITITION_FREQ\":"+scanDataObj.getString("REPITITION_FREQ")+",\"SCAN_LIST\":"+scanDataObj.getJSONArray("SCAN_LIST").toString()+"}";
	           jsonStringInOrder+="{\"CMD_CODE\":\"START_CELL_SCAN\",\"NEIGHBOR_SCAN\":0,\"RSSI_THRESHOLD\":"+scanDataObj.getString("RSSI_THRESHOLD")+",\"REPETITION_FLAG\":"+scanDataObj.getString("REPETITION_FLAG")+",\"REPITITION_FREQ\":"+scanDataObj.getString("REPITITION_FREQ")+",\"TECH\":\""+scanDataObj.getString("TECH")+"\",\"FREQ_INFO\":[{\"BAND\":\""+band+"\",\"FREQ_LIST\":[{\"FREQ\":"+fcn+"}]}]}";

		   }else{
			   
		   }
		   }catch(Exception E){
			   fileLogger.error("Exception");
		   }
			fileLogger.info("Exit Function : getScanJsonInOrder");	
	       return jsonStringInOrder;
	   }
	
	   public LinkedHashMap<String,String>  prepareParamForStopNetscan(JSONObject netscanData, String tech,String scanType){
		   JSONObject scanStopDataObj=new JSONObject();
		   LinkedHashMap<String,String> param = new LinkedHashMap<String,String>();
		  try {
			scanStopDataObj.put("cmdType","STOP_SCAN");
			  scanStopDataObj.put("systemId",netscanData.getString("sytemid"));
			  scanStopDataObj.put("systemCode",scanType);
			  scanStopDataObj.put("systemIp",netscanData.getString("ip"));
			  scanStopDataObj.put("id",netscanData.getString("b_id"));
			JSONObject scanTypeObj=new JSONObject();
			
			scanTypeObj.put("CMD_CODE", "STOP_SCAN");
			scanTypeObj.put("TECH", tech);
			scanTypeObj.put("SCAN_TYPE", "2");
			scanStopDataObj.put("data",scanTypeObj);
			String scanStopDataStr=scanStopDataObj.toString();
			   
			   param.put("cmdType","STOP_SCAN");
			   param.put("systemCode", scanType);
			   param.put("systemIP", netscanData.getString("ip"));
			   param.put("systemId", netscanData.getString("sytemid"));
			   param.put("id", netscanData.getString("b_id"));
			   param.put("data", scanStopDataStr);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	       return param;
   }
	
	
	/**
	 * switch dsp 
	 * */
	public void switchDsp(String sufi) 
	{
		LinkedHashMap<String,String> netscanDspMap= new LinkedHashMap<String,String>();
		
		if(sufi.equalsIgnoreCase("sufi")) 
		{
			netscanDspMap.put("data", "{\"CMD_CODE\":\"SET_SYSTEM_CONFIG\",\"OPERATION\":\"1\",\"DSP0_NODE\":\"3\",\"DSP1_NODE\":\"1\"}");
		}
		else 
		{
			netscanDspMap.put("data", "{\"CMD_CODE\":\"SET_SYSTEM_CONFIG\",\"OPERATION\":\"1\",\"DSP0_NODE\":\"1\",\"DSP1_NODE\":\"3\"}");
		}
		
		
		 try {
				JSONArray systemManagerArray = new Operations().getJson("select * from view_btsinfo where code=10 limit 1");
				   JSONObject systemManagerObject=systemManagerArray.getJSONObject(0);
					netscanDspMap.put("systemIP", systemManagerObject.getString("ip"));
					netscanDspMap.put("CMD_TYPE", "SET_SYSTEM_CONFIG");
					netscanDspMap.put("SYSTEM_CODE",systemManagerObject.getString("code"));
					netscanDspMap.put("SYSTEM_ID", systemManagerObject.getString("sytemid"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		
		
		new NetscanOperations().sendCommandToNetscanServer(netscanDspMap);
	}
	
	/**
	 * Returns bist test cases
	 * */
	public JSONArray getTestCases() 
	{
		JSONArray testCases = new Operations().getJson("select * from bist_tests where status = true order by test_order");
		return testCases;
	}
	
	/**
	 * Lock 
	 * */
	public void lock(String device) 
	{
		HashMap<String,String> hm=devicesMapOverTech.get(device).get(0);
		if(device.equalsIgnoreCase("2g")){
			new CommonService().setLockUnlock(hm.get("ip"),"1");
		}else{
			LinkedHashMap<String,String> data = new LinkedHashMap<String,String>();
			data.put("cmdType", "SET_CELL_LOCK");
			data.put("systemCode", hm.get("dcode"));
			data.put("systemId", hm.get("sytemid"));
			data.put("systemIP", hm.get("ip"));
			data.put("data", "{\"CMD_CODE\":\"SET_CELL_LOCK\",\"CELL_ID\":\"0\",\"LAC\":\"0\"}");
			boolean lockStatus=new ThreegOperations().setCellLockUnlockDdp(data);
		}
	}
	
	/**
	 * Unlock 
	 * */
	public void unlock(String device) 
	{
		HashMap<String,String> hm=devicesMapOverTech.get(device).get(0);
		if(device.equalsIgnoreCase("2g")){
			new CommonService().setLockUnlock(hm.get("ip"),"2");
		}else{
			LinkedHashMap<String,String> data = new LinkedHashMap<String,String>();
			data.put("cmdType", "SET_CELL_UNLOCK");
			data.put("systemCode", hm.get("dcode"));
			data.put("systemId", hm.get("sytemid"));
			data.put("systemIP", hm.get("ip"));
			data.put("data", "{\"CMD_CODE\":\"SET_CELL_UNLOCK\",\"CELL_ID\":\"0\",\"LAC\":\"0\"}");
			boolean lockStatus=new ThreegOperations().setCellLockUnlockDdp(data);
			
		}
	}
	
	/**
	 * set config on to the device
	 * */
	public void setConfig(JSONObject config,String tech) 
	{
		String ip="";
		if(tech.equalsIgnoreCase("gsm")) 
		{
			ip=hm2g.get("ip");
			String result = new Operations().locateWithNeighbour(config,ip,-999);
		}
		else 
		{
			ip=hm3g.get("ip");
			LinkedHashMap<String,String> param=new Operations().setParamsForConfig(config,ip,-999);
			boolean result = new Common().sendConfigurationToNode(Integer.parseInt(param.get("adminState")),param);
		}
	}
	
	/**
	 * Switch antena
	 * */
	public boolean antenaSwitch(String band,String tech,int sector) 
	{
		band = convertToBandFromNuber(tech,band);
		return new ApiCommon().switchAntena(band,tech,"8",sector);
	}
	
	
	/**
	 * PA switch
	 * */
	public boolean pASwitch(String band,String tech,int sector) 
	{
		band = convertToBandFromNuber(tech,band);
		return new ApiCommon().switchAntena(band+"-NA",tech,"7",sector);
	}
	
	/**
	 * make program wait 
	 **/
	public void waitForResult(Long waitTime) 
	{
		fileLogger.info("Inside Function : waitForResult");	
		try {
			synchronized (Bist.getInstance()) {
				new Operations().setBistObject(Bist.getInstance());
				fileLogger.debug("@stepbist wait start:"+new Date());
				this.cdr=true;
				this.netscan=true;
				fileLogger.debug("@waittime :"+waitTime);
				this.wait(waitTime);
				getInstance().wait(waitTime);
				this.cdr=false;
				this.netscan=false;
				fileLogger.debug("@stepbist wait end:"+new Date());
				//new Operations().setBistObject(null);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		fileLogger.info("Exit Function : waitForResult");	
	}
	
	
	/**
	 * compares the opr data
	 * **/
	public void compareOprData(String PLMN,String lac,String cell,String fcn,String tech) 
	{
		
		switch(tech) 
		{
		case "GSM":
			if(PLMN.equalsIgnoreCase(this.imsi) || imei.equalsIgnoreCase(this.imei)) 
			{
				this.result = true;
				new Operations().setBistObject(null);
				synchronized (Bist.getInstance()) {
					Bist.getInstance().notify();
	            }
				
			}
			break;
		case "UMTS":
			if(imsi.equalsIgnoreCase(this.imsi) || imei.equalsIgnoreCase(this.imei)) 
			{
				this.result = true;
				new Operations().setBistObject(null);
				synchronized (Bist.getInstance()) {
					Bist.getInstance().notify();
	            }
			}
			break;
		}
	}
	
	/**
	 * compares the cdr
	 * **/
	public void compareCdr(String imsi,String imei) 
	{
		if(imsi.equalsIgnoreCase(this.imsi) || imei.equalsIgnoreCase(this.imei)) 
		{
			this.result = true;
			new Operations().setBistObject(null);
			synchronized (Bist.getInstance()) {
				Bist.getInstance().notify();
            }
		}
	}
	
	
	public String getSwitchTechType(String tech,String type) 
	{
		String switchTech = null;
		switch(type) 
		{
			case "tracking":
				switchTech = (tech.equalsIgnoreCase("GSM"))?"2G":"3G";
				break;
			case "scaning":
				switchTech = "tx2orx2";
				break;
		}
		return switchTech;
	} 
	
	
	/**
	 * Convert to band from number
	 * */
	public String convertToBandFromNuber(String tech,String bandNumber)
	{
		String bandg = "900";
		if(tech.equalsIgnoreCase("2g"))
		{
			switch(bandNumber){
			case "1":
				bandg="850";
				break;
			case "2":
				bandg="900";
				break;
			case "4":
				bandg="1800";
				break;
			}
		}
		else if(tech.equalsIgnoreCase("3g"))
		{
			bandg="850";
			switch(bandNumber){
			case "5":
				bandg="850";
				break;
			case "8":
				bandg="900";
				break;
			case "3":
				bandg="1800";
				break;
			case "1":
				bandg="2100";
			}
		}
		return bandg;
	}
	
	
	
	public void setImsi(String imsi)
	{
		this.imsi = imsi;
	}
	
	public void setImei(String imei)
	{
		this.imei = imei;
	}
	
	
	public void stopAllScan()
	{
		startStopNetworkScanner(0,"GSM",null,null,"3");
		startStopNetworkScanner(0,"UMTS",null,null,"3");
		startStopNetworkScanner(0,"LTE",null,null,"3");
		startStopNetworkScanner(0,"GSM",null,null,"2");
		startStopNetworkScanner(0,"UMTS",null,null,"2");
		startStopNetworkScanner(0,"LTE",null,null,"2");
	}
	/**
	 * Get Test Results
	 * */
	public JSONArray getTestResults() 
	{
		return new Operations().getJson("SELECT NAME,PLMN,TECH,BAND,IMEI,IMSI FROM bist_tests");
	}
	
	public JSONArray start() 
	{
		startTesting();
		return getTestResults();
	}
	
	
	
	
	public boolean startTesting() 
	{
		fileLogger.info("Inside Function : startTesting");	
		try {
			intializeBist();
		} catch (Exception e) {
			fileLogger.debug("@bist : "+e.getMessage());
			e.printStackTrace();
		}
		
		try 
		{
			JSONArray testCases = getTestCases();
			for (int i = 0; i < testCases.length(); i++) 
			{
				JSONObject jo = testCases.getJSONObject(i);
				/*************************************************************/
				int id					= jo.getInt("id");
				String type				= jo.getString("type");
				String tech 			= jo.getString("tech");
				String device 			= (tech.equalsIgnoreCase("GSM"))?"2G":"3G";
				String switchTech		= getSwitchTechType(tech,type);
				JSONObject config 		= new JSONArray(jo.getString("config")).getJSONObject(0);
				JSONObject packet       = config.getJSONArray("data").getJSONObject(0);
				String fcn = "";
				if(tech.equalsIgnoreCase("GSM")){
					fcn = packet.getString("arfcn");
				}else{
					fcn = packet.getString("uarfcn");
				}
				String band 			= jo.getString("band");
				String sectorId 		= jo.getString("sector");
				String waitTime 		= jo.getString("wait_time");
				String imsi 			= jo.getString("imsi");
				String imei 			= jo.getString("imei");
				this.imsi 				= imsi;
				this.imei 				= imei;
				this.plmn 				= jo.getString("plmn");
				this.lac 				= jo.getString("lac");
				this.cell 				= jo.getString("cell");
				//this.fcn 				= jo.getString("fcn");
				//JSONObject steps 		= new JSONObject();
				String[] steps = jo.getString("steps").split(","); 
				
				/************************************************************/
				for(String step:steps)
				{
				
					String result = "FAIL";
					fileLogger.debug("@stepbist :"+step);
					switch(step) 
					{
						case "update_result_start" :
							updateTestResult(id,"FAIL");
							try {
								Thread.sleep(4000);
							}catch (Exception e) {
								e.printStackTrace();
							}
						break;
						case "lock" :
							lock(device);
							try {
								Thread.sleep(4000);
							}catch (Exception e) {
								e.printStackTrace();
							}
						break;
						case "unlock" :
							unlock(device);
							try {
								Thread.sleep(4000);
							}catch (Exception e) {
								e.printStackTrace();
							}
						break;
						case "update_result_end" :
							updateTestResult(id,result);
							try {
								Thread.sleep(4000);
							}catch (Exception e) {
								e.printStackTrace();
							}
						break;
						case "config" :
							setConfig(config,tech);
							try {
								Thread.sleep(4000);
							}catch (Exception e) {
								e.printStackTrace();
							}
						break;
						case "antena_switch" :
							antenaSwitch(band, switchTech, Integer.parseInt(sectorId));
							try {
								Thread.sleep(4000);
							}catch (Exception e) {
								e.printStackTrace();
							}
						break;
						case "pa_switch" :
							pASwitch(band,switchTech, Integer.parseInt(sectorId));
							try {
								Thread.sleep(4000);
							}catch (Exception e) {
								e.printStackTrace();
							}
						break;
						case "wait" :
							waitForResult(Long.parseLong(waitTime));
							try {
								Thread.sleep(4000);
							}catch (Exception e) {
								e.printStackTrace();
							}
						break;
						case "switch_dps_one_to_sufi" :
							switchDsp("notSufi");
						break;
						case "update_result_success" :
							result = (this.result)?"OK":"FAIL";
							updateTestResult(id,result);
							try {
								Thread.sleep(4000);
							}catch (Exception e) {
								e.printStackTrace();
							}
						break;
						case "remove_imsi" :
							setImsi("-1");
						break;
						case "remove_imei" :
							setImei("-1");
						break;
						case "start_scan":
							startStopNetworkScanner(1,tech.toUpperCase(),band,fcn,"3");
							break;
						case "stop_scan":
							startStopNetworkScanner(0,tech.toUpperCase(),band,fcn,"3");
							break;
					}
					
					if(!this.result) 
					{
						//break;
					}
				}
			} 
		} catch (Exception e) {
			// TODO: handle exception
		}
		fileLogger.info("Exit Function : startTesting");	
		return this.result;
	}
}

