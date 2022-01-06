package in.vnl.api.common;

import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import com.sun.javafx.css.CalculatedValue;

import in.vnl.msgapp.Operations;
import in.vnl.EventProcess.DBDataService;
import in.vnl.api.config.PossibleConfigurations;

public class OperationCalculations 
{
	static Logger fileLogger = Logger.getLogger("file");
	public int calulatePower(String ip,double freq,int distance) 
	{
		
		fileLogger.info("Inside Function : calulatePower");
		//20log10(f*d)-47.56
		
		//fileLogger.debug("***************************power*******************************");
		fileLogger.debug(ip+"-"+freq+"-"+distance) ;
		//fileLogger.debug("***************************power*******************************");
		String query = "select * from btsmaster where ip = '"+ip+"'";
		int result = -1;
		try {
			JSONObject jj = new Operations().getJson(query).getJSONObject(0);
			
			int gain = jj.getInt("pa_gain");
			int power = jj.getInt("pa_power");
			
			if(distance == -1) 
			{
				return power-gain;
			}
			
			//int txpower = (int)(20*Math.log10((double)(freq*distance))+(32.44-80));changed on 14 dec 2018
			int txpower = (int)(20*Math.log10((double)(freq*distance))+(32.44-70));
			if(txpower > power) 
			{
				result = power-gain;
			}
			else
				result = txpower-gain;
			
			fileLogger.debug("result"+"-"+result) ;
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fileLogger.info("Exit Function : calulatePower");
		return result;
	}
	
	
	
	public String  calulateArfcns(int frequency,int angle) 
	{
		return "";
	}
	
	
	public String validateOpertaion(String operationType,String withScan,String oprName) 
	{
		fileLogger.info("Inside Function : validateOpertaion");
		String query=null;
		String msg="OK";
		int count = 0;
		JSONArray js=null;
		Operations operations=new Operations();
		
		try 
		{
			//fileLogger.debug("**********************Validating******************************");
			fileLogger.debug(operationType+"-"+withScan+"-"+oprName);
			//Thread.sleep(60000);
			//fileLogger.debug("**********************Validating******************************");
		}
		catch(Exception e) 
		{
			
		}
		
		
		try 
		{
			int systemTypeCode=DBDataService.getSystemType();
			if(systemTypeCode==2){
				query="select status from manual_override";
				js = operations.getJson(query);
				String manualStatus = js.getJSONObject(0).getString("status");
				if(manualStatus.equalsIgnoreCase("t") || manualStatus.equalsIgnoreCase("true")){
					msg="Manual Overide is Enabled";
					return msg;
				}
			}else{
				query="select status from config_status";
				js = operations.getJson(query);
				int manualState = js.getJSONObject(0).getInt("status");
				if(manualState==1){
					msg="Manual Tracking is running.Please stop Manual Tracking.";
					return msg;
				}
			}
			
			query="select count(*) from oprrationdata where status ='1'";
			js = operations.getJson(query);
			count = Integer.parseInt(js.getJSONObject(0).getString("count"));
			if(count > 0) 
				msg="Please stop currently running  operations";
		}
		catch(Exception e) 
		{
			fileLogger.debug(e.getStackTrace().toString());
			msg = "Exception";
		}
		
		
		   
		   JSONArray operationsInfoArr=operations.getJson("select count(*) as dup_count from oprrationdata where lower(name)='"+oprName.toLowerCase()+"'");	
		   try 
		   {
			if(operationsInfoArr.getJSONObject(0).getInt("dup_count")>0)
			{
				   
				   msg= "Operation Name should be unique";
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			msg = "Exception";
			e.printStackTrace();
		}
		
		try {
			int manualEventStatus=operations.getJson("select status from config_status").getJSONObject(0).getInt("status");
			if(manualEventStatus==1){
				msg="Manual Event is running";
			}
		} catch (JSONException e) {
				msg="Exception";
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		   String falconType=DBDataService.configParamMap.get("falcontype");
		   int systemTypeCode=DBDataService.getSystemType();
		   String fetchDeviceQuery="";
		   if(falconType.equalsIgnoreCase("standard")){
			   if(systemTypeCode==0){
				   fetchDeviceQuery="select count(*) as device_count from view_btsinfo where ip not in('0.0.0.0','1.1.1.1') and code not in(3,11,12,9,8,16) and lower(status) in('not reachable','system down')";  
			   }else{
				   fetchDeviceQuery="select count(*) as device_count from view_btsinfo where ip not in('0.0.0.0','1.1.1.1') and code not in(3,11,12,9,16) and lower(status) in('not reachable','system down')"; 
			   }
			   JSONArray devicesInfoArr=operations.getJson(fetchDeviceQuery);
		   try {
			if(devicesInfoArr.getJSONObject(0).getInt("device_count")>0)
			{
				msg="All Devices should be reachable";
			}
		} catch (JSONException e2) {
			// TODO Auto-generated catch block
			msg = "Exception";
			e2.printStackTrace();
		}
		}else{
			//optimization to be done
			   if(systemTypeCode==0){
				   fetchDeviceQuery="select code,status from view_btsinfo where ip not in('0.0.0.0','1.1.1.1') and code not in(3,11,12,9,8,16)";  
			   }else{
				   fetchDeviceQuery="select code,status from view_btsinfo where ip not in('0.0.0.0','1.1.1.1') and code not in(3,11,12,9,16)"; 
			   }
			JSONArray deviceInfoArr = operations.getJson(fetchDeviceQuery);
			int norDeviceRest = 0;
			int reachableDeviceBts = 0;
			try {
				for (int i = 0; i < deviceInfoArr.length(); i++) {
					JSONObject obj = deviceInfoArr.getJSONObject(i);
					int code = obj.getInt("code");
					String status = obj.getString("status");
					if (code == 0 || code == 5 || code == 13 || code ==10) {
						if (!status.equalsIgnoreCase("not reachable") && !status.equalsIgnoreCase("system down")) {
							reachableDeviceBts++;
						}
					} else {
						if (status.equalsIgnoreCase("not reachable") || status.equalsIgnoreCase("system down")) {
							norDeviceRest++;
						}
					}

				}
	            fileLogger.debug("@validate norDeviceRest is :"+norDeviceRest+ " and reachableDeviceBts is :"+reachableDeviceBts);
				if (norDeviceRest != 0 || reachableDeviceBts == 0) {
					msg = "All Devices should be reachable";
				}

			} catch (JSONException e3) {
				// TODO Auto-generated catch block
				msg = "Exception";
				e3.printStackTrace();
			}
		}
			fileLogger.info("Exit Function : validateOpertaion");
		   return msg;
				
		}
		
	public LinkedHashMap<String,String> checkIfLastOperationIsTriggerOperation() 
	{
		
		LinkedHashMap<String,String> result = new LinkedHashMap<String,String>();
		
		try {
			JSONArray data = new Operations().getJson("select * from oprrationdata where id = (select max(id) from oprrationdata)");
			JSONObject oprData = data.getJSONObject(0);
			if(oprData.getString("opr_type").equalsIgnoreCase("3")) 
			{
				result.put("result", "true");
			}
			else 
			{
				result.put("result", "false");
				result.put("msg", "Trigger operation not defined");
			}
			
			//result.put("useLastScannedData", oprData.getString("use_last_scanned_data"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			result.put("result", "false");
			result.put("msg", "Exception occured");
		}
		
		return result;
		
	}
	
	public String  calulateArfcnsFromFreq(double frequency,int angle) 
	{
		
		double freq= frequency * 1000;
		String query = "select *,(case when (dl_frq_start <= "+freq+" and dl_frq_end >= "+freq+") then 1 else (case when (ul_frq_start <= "+freq+" and ul_frq_end >= "+freq+") then 2 else 0 end) end) freq_type from arfcn_freq_band_map where ((dl_frq_start <= "+freq+" and dl_frq_end >= "+freq+") or (ul_frq_start <= "+freq+" and ul_frq_end >= "+freq+"))";
		JSONArray aa = new Operations().getJson(query);
		
		StringBuilder arfcns = new StringBuilder();
		

		for(int i=0;i<aa.length();i++) 
		{
			
			try 
			{
				JSONObject jj = aa.getJSONObject(i);
				int offset=0;
				if(jj.getString("tech").equalsIgnoreCase("4g"))
				{
					offset=10;
				}
				else
				{
					offset=5;
				}
				if(jj.getInt("freq_type") == 1) 
				{
					
	
					int arfcnValue = calulateArfcn(jj.getInt("dl_fcn_start"),frequency,jj.getDouble("dl_frq_start"),offset);
					arfcns.append(",'"+arfcnValue+"'");
					
					
				}
				else if(jj.getInt("freq_type") == 2) 
				{
					int arfcnValue = calulateArfcn(jj.getInt("ul_fcn_start"),frequency,jj.getDouble("ul_frq_start"),offset);
					arfcns.append(",'"+arfcnValue+"'");
				}
			} 
			catch (JSONException e) 
			{
				e.printStackTrace();
			}
			
		}
		if(arfcns.length()>0)
			arfcns.replace(0, 1, " ");
		return arfcns.toString();
	}
	
	public String calulateArfcnsFromFreqBandwidth(double frequency,double bandwidth) 
	{
		
		//String query = "select *,(case when (dl_frq_start <= "+frequency+"*1000 and dl_frq_end >=  "+frequency+"*1000 ) then 1 else (case when (ul_frq_start <=  "+frequency+"*1000  and ul_frq_end >=  "+frequency+"*1000 ) then 2 else 0 end) end) freq_type from arfcn_freq_band_map where ((dl_frq_start <=  "+frequency+"*1000  and dl_frq_end >=  "+frequency+"*1000 ) or (ul_frq_start <=  "+frequency+"*1000  and ul_frq_end >=  "+frequency+"*1000 ))";
		int offset=5;
		//sirvaib double tempBandwidth=-10000.0;
		double rbw = bandwidth/2;
		double frqstart = (frequency -rbw)*1000;
	//sirvaib	double tempDiff=0.0;
		double frqend = (frequency+rbw)*1000;
		//sirvaib String tech="";
	/*	sirvaib
		if (bandwidth==0.2) {
			tech="'2G'";
			
		}
		else if(bandwidth==5.0)
		{
			tech="'3G','4G'";
		}
		else if (bandwidth >5.0)
		{
			tech ="'4G'";
		}
		String query = "select *,(dl_frq_start -ul_frq_start) band_sep from arfcn_freq_band_map where (((ul_frq_start <=  "+frqstart+"  and ul_frq_end >=  "+frqstart+" ) or (ul_frq_start <=  "+frqend+"  and ul_frq_end >=  "+frqend+" )) and tech in ("+ tech + " ) )";
*/
		String query = "select *,(dl_frq_start -ul_frq_start) band_sep from arfcn_freq_band_map where ((ul_frq_start <=  "+frqstart+"  and ul_frq_end >=  "+frqstart+" ) or (ul_frq_start <=  "+frqend+"  and ul_frq_end >=  "+frqend+" ))";
		fileLogger.debug("@calulateArfcnsFromFreqBandwidth query ==  "+ query);
		
		JSONArray aa = new Operations().getJson(query);
		
		StringBuilder arfcns = new StringBuilder();
		

		for(int i=0;i<aa.length();i++) 
		{
			
			try 
			{
//sirvaib				tempDiff=0.0;
				JSONObject jj = aa.getJSONObject(i);
/*sirvaib				
				if(jj.getString("tech").equalsIgnoreCase("4g"))
				{
					offset=10;
					tempBandwidth=10.0*1000;
				}
				
				else if(jj.getString("tech").equalsIgnoreCase("3g"))
				{
					offset=5;
					tempBandwidth=2.5*1000;
					
				}
				else
				{
					offset=5;
					tempBandwidth=0.0*1000;
				}
				
				frqstart-=tempBandwidth;
				frqend+=tempBandwidth;
*/
				if(jj.getInt("ul_frq_start") > frqstart) 
				{
//sirvaib					tempDiff=jj.getInt("ul_frq_start")-frqstart;
					frqstart=jj.getInt("ul_frq_start");
//sirvaib						frqend+=tempBandwidth+tempDiff;
				}
				
				if(jj.getInt("ul_frq_end") < frqend) 
				{
//sirvaib	    	tempDiff=frqstart-jj.getInt("ul_frq_end");
					frqend=jj.getInt("ul_frq_end");
/*sirvaib	
					frqstart-=tempDiff;
					if(jj.getInt("ul_frq_start") > frqstart) 
					{
						
						frqstart=jj.getInt("ul_frq_start");
						
					}
*/
				}
				//sirvaib addition below remove
				if(jj.getString("tech").equalsIgnoreCase("4g"))
					offset=10;
				else
					offset=5;
				//sirvaib addition above remove
				double dlFrqstart=(frqstart+jj.getDouble("band_sep"))/1000;
				double dlFrqend=(frqend+jj.getDouble("band_sep"))/1000;
				int startArfcnVal = calulateArfcn(jj.getInt("dl_fcn_start"),dlFrqstart,jj.getDouble("dl_frq_start"),offset);
				int endArfcnVal=calulateArfcn(jj.getInt("dl_fcn_start"),dlFrqend,jj.getDouble("dl_frq_start"),offset);
				for(int arfcnValue=startArfcnVal;arfcnValue<=endArfcnVal;arfcnValue++){
					
					arfcns.append(",'"+arfcnValue+"'");
				}
				
/*				JSONObject jj = aa.getJSONObject(i);
				if(jj.getInt("freq_type") == 1) 
				{
					
					
					int arfcnValue = calulateArfcn(jj.getInt("dl_fcn_start"),frequency,jj.getDouble("dl_frq_start"));
					arfcns.append(",'"+arfcnValue+"'");
					
					
					
				}
				else if(jj.getInt("freq_type") == 2) 
				{
					
					int arfcnValue = calulateArfcn(jj.getInt("ul_fcn_start"),frequency,jj.getDouble("ul_frq_start"));
					arfcns.append(",'"+arfcnValue+"'");
				}*/
			} 
			catch (JSONException e) 
			{
				e.printStackTrace();
			}
			
		}
		if(arfcns.length()>0)
			arfcns.replace(0, 1, " ");
		return arfcns.toString();
	}
	

	public double calulateFreqFromArfcn(int arfcn, String tech) 
	{
		fileLogger.info("Inside Function : calulateFreqFromArfcn");
		int offset =0;
		tech =tech.toUpperCase();
		if (tech.equalsIgnoreCase("4g"))
			offset=10;
		else
			offset=5;

		String query = "select *,(case when (dl_fcn_start <= "+arfcn+" and dl_fcn_end >="+arfcn+") then 1 else (case when (ul_fcn_start <= "+arfcn+" and ul_fcn_end >= "+arfcn+") then 2 else 0 end) end) freq_type from arfcn_freq_band_map where tech ='" + tech + "' and ((dl_fcn_start <= "+arfcn+" and dl_fcn_end >= "+arfcn+") or (ul_fcn_start <= "+arfcn+" and ul_fcn_end >= "+arfcn+")) limit 1";
		JSONArray aa = new Operations().getJson(query);
		
			
		//StringBuilder freqs = new StringBuilder();
		double freq = -1;
		for(int i=0;i<aa.length();i++) 
		{
			
			try 
			{
				JSONObject jj = aa.getJSONObject(i);
				
				fileLogger.debug("freq_type : "+jj.getInt("freq_type") +"-"+jj.getInt("dl_fcn_start")+"-"+ jj.getInt("dl_frq_start"));
				if(jj.getInt("freq_type") == 1) 
				{
					freq = calulateFreq(arfcn, jj.getDouble("dl_fcn_start"), jj.getDouble("dl_frq_start"),offset);
					
					
					
				}
				else if(jj.getInt("freq_type") == 2) 
				{
					
					fileLogger.debug("freq_type : "+jj.getInt("freq_type") +"-"+jj.getInt("dl_fcn_start")+"-"+ jj.getInt("dl_frq_start"));
					freq = calulateFreq(arfcn,jj.getDouble("ul_fcn_start"),jj.getDouble("ul_frq_start"),offset);
				}
			} 
			catch (JSONException e) 
			{
				e.printStackTrace();
			}
			
		}
		fileLogger.info("Exit Function : calulateFreqFromArfcn");
		return freq;
	}
	
	public double calulateFreqFromArfcn(int arfcn) 
	{
		fileLogger.info("Inside Function : calulateFreqFromArfcn");
		String query = "select *,(case when (dl_fcn_start <= "+arfcn+" and dl_fcn_end >="+arfcn+") then 1 else (case when (ul_fcn_start <= "+arfcn+" and ul_fcn_end >= "+arfcn+") then 2 else 0 end) end) freq_type from arfcn_freq_band_map where ((dl_fcn_start <= "+arfcn+" and dl_fcn_end >= "+arfcn+") or (ul_fcn_start <= "+arfcn+" and ul_fcn_end >= "+arfcn+")) limit 1";
		JSONArray aa = new Operations().getJson(query);
		
		StringBuilder freqs = new StringBuilder();
		double freq = -1;
		for(int i=0;i<aa.length();i++) 
		{
			
			try 
			{
				JSONObject jj = aa.getJSONObject(i);
				
				fileLogger.debug("freq_type : "+jj.getInt("freq_type") +"-"+jj.getInt("dl_fcn_start")+"-"+ jj.getInt("dl_frq_start"));
				if(jj.getInt("freq_type") == 1) 
				{
					freq = calulateFreq(arfcn, jj.getDouble("dl_fcn_start"), jj.getDouble("dl_frq_start"));
					
					
					
				}
				else if(jj.getInt("freq_type") == 2) 
				{
					
					fileLogger.debug("freq_type : "+jj.getInt("freq_type") +"-"+jj.getInt("dl_fcn_start")+"-"+ jj.getInt("dl_frq_start"));
					freq = calulateFreq(arfcn,jj.getDouble("ul_fcn_start"),jj.getDouble("ul_frq_start"));
				}
			} 
			catch (JSONException e) 
			{
				e.printStackTrace();
			}
			
		}
		fileLogger.info("Exit Function : calulateFreqFromArfcn");
		return freq;
	}
	
	public int calulateArfcn(int arfcnStart,double freq,double startFreq,int offset) 
	{
		fileLogger.info("Inside Function : calulateArfcn");
		//startFreq=Math.round((startFreq/1000)*10.0)/10.0;
		int arfcn = (int)((offset*(freq*1000-startFreq)/1000)+arfcnStart);
		fileLogger.debug("ARFCN"+freq);
		fileLogger.info("Exit Function : calulateArfcn");
		return arfcn;
	}
	
	public double calulateFreq(int arfcn,double arfcnStart,double startFreq,int offset) 
	{
		fileLogger.info("Inside Function : calulateFreq");
		double freq = ((arfcn-arfcnStart)/offset)+(startFreq/1000);
		fileLogger.debug("FREQ"+freq);
		fileLogger.info("Exit Function : calulateFreq");
		return freq;
	}
	
	public double calulateFreq(int arfcn,double arfcnStart,double startFreq) 
	{
		fileLogger.info("Inside Function : calulateFreq");
		double freq = ((arfcn-arfcnStart)/5)+(startFreq/1000);
		fileLogger.debug("FREQ"+freq);
		fileLogger.info("Exit Function : calulateFreq");
		return freq;
	}
	
	public double calculateAngleFromPosition(double lat,double lon) 
	{
		double antennaAngle=-1;
		try {
			JSONObject gpsPosObj= new PossibleConfigurations().getSystemPossition();
			  double longitude1 = lon;
			  double longitude2 = gpsPosObj.getDouble("lon");
			  double latitude1 = Math.toRadians(lat);
			  double latitude2 = Math.toRadians(gpsPosObj.getDouble("lat"));
			  double longDiff= Math.toRadians(longitude2-longitude1);
			  double y = Math.sin(longDiff)*Math.cos(latitude2);
			  double x = Math.cos(latitude1)*Math.sin(latitude2)-Math.sin(latitude1)*Math.cos(latitude2)*Math.cos(longDiff);
			  antennaAngle=(Math.toDegrees(Math.atan2(y, x))+360)%360;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return antennaAngle;
	}
	
	public int getAntennaIdFromAngle(double antennaAngle){
		double angleOffset=0;
		try {
			angleOffset=new Operations().getJson("select angle_offset from antenna where atype='1' limit 1").getJSONObject(0).getDouble("angle_offset");
			double relAngle=antennaAngle-angleOffset;
			if(relAngle<=60){
				return 1;
			}else if(relAngle>60 && relAngle<=120){
				return 18;
			}else{
				return 20;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 1;
	}
	
	public int getAntennaIdFromPosition(double lat,double lon){
		double antennaAngle = calculateAngleFromPosition(lat,lon);
		int antennaId=getAntennaIdFromAngle(antennaAngle);
		return antennaId;
	}
	
	public int getAntennaIdFromSector(String antennaName){
		int antennaId=0;
		try {
			antennaId=new Operations().getJson("select id from antenna where profile_name='"+antennaName+"'").getJSONObject(0).getInt("id");
		      
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return antennaId;
	}
}
