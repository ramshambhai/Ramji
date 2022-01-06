package in.vnl.api.Jammer;

import java.util.LinkedHashMap;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.vnl.api.common.ApiCommon;
import in.vnl.api.common.AuditHandler;
import in.vnl.api.twog.TwogOperations;
import in.vnl.msgapp.Common;
import in.vnl.msgapp.Operations;
import in.vnl.scheduler.timertasks.UpdateStatus;

@Path("/Jammer")
public class Jammer {

	
	static Logger fileLogger = Logger.getLogger("file");
	static Logger statusLogger = Logger.getLogger("status");
	 
	 
	@POST
	@Path("/JammerSave")
	//@Produces(MediaType.APPLICATION_JSON)
	//@Consumes("application/json")
	public String JammerSave(@FormParam("data") String dataReceived,@FormParam("ip") String ip,@FormParam("txPower") String txPower,@FormParam("loIfOffsetKhz") String loIfOffsetKhz,@FormParam("appliedFrequency") String appliedFrequency2) {
		 fileLogger.info("Inside Function : JammerSave");
		 String  rs ="";
		try 
		{
			
			String[] parts = dataReceived.split(" ");
			String[] startSubBand = new String[parts.length];
			String[] EndSubBand = new String[parts.length];
			String[] addrStart = new String[parts.length];
			String[] addrEnd = new String[parts.length];
			String[] dwTimeCtr = new String[parts.length];
			String[] ramOpMode = new String[parts.length];
			String[] dwBit = new String[parts.length];
			String[] mode = new String[parts.length];
			JSONObject obj = new JSONObject();
		    JSONArray ja_data = new JSONArray();
		    appliedFrequency2=appliedFrequency2.replaceAll("\"", "");
		    appliedFrequency2=appliedFrequency2.replaceAll("\\p{P}",",");
		    appliedFrequency2=appliedFrequency2.substring(1);
		    String[] appliedFrequency=appliedFrequency2.split(",");
		    
		   
			
//			String querywa1 = "select * from btsmaster where devicetypeid = 21;";
//			JSONArray ja1 = new Operations().getJson(querywa1);
//			String ip=ja1.getJSONObject(0).getString("ip");
			String url="/jammerProfile";
			JSONObject objToSendToJammer = new JSONObject();
		    JSONArray ja_dataToSendToJammer = new JSONArray();
		    
		    int flag=-1;
		    for(int i=0;i<4;i++)
			{
		    	objToSendToJammer = new JSONObject();
		    	startSubBand[i]=(parts[i].split("-")[0]);
				EndSubBand[i]=(parts[i].split("-")[1]);
				addrStart[i]=(parts[i].split("-")[2]);
				addrEnd[i]=(parts[i].split("-")[3]);
				dwTimeCtr[i]=(parts[i].split("-")[4]);
				ramOpMode[i]=(parts[i].split("-")[5]);
				dwBit[i]=(parts[i].split("-")[6]);
				mode[i]=(parts[i].split("-")[7]);
		    	objToSendToJammer.put("profileMode", appliedFrequency[i].replaceAll("\"", ""));
		    	objToSendToJammer.put("freqStart", startSubBand[i]+"000");
		    	objToSendToJammer.put("freqEnd", EndSubBand[i]+"000");
		    	objToSendToJammer.put("addrStart", addrStart[i]+"");
		    	objToSendToJammer.put("addrEnd", addrEnd[i]+"");
		    	objToSendToJammer.put("dwTimeCtr", dwTimeCtr[i]+"");
		    	objToSendToJammer.put("ramOpMode", ramOpMode[i]+"");
		    	objToSendToJammer.put("dwBit", dwBit[i]+"");
		    	//objToSendToJammer.put("mode", mode[i]+"");
		    	
		        //rs =  new ApiCommon().HTTPpostThroughSocket(url, ip, objToSendToJammer.toString(), 80);
		    	
		    	fileLogger.debug("JammerSave: objToSendToJammer="+objToSendToJammer);
				String makeDataSendString="";
				makeDataSendString+="{'profileMode':'"+objToSendToJammer.get("profileMode")+"','addrStart':'"+objToSendToJammer.get("addrStart")+"','addrEnd':'"+objToSendToJammer.get("addrEnd")+"','freqStart':'"+objToSendToJammer.get("freqStart")+"','freqEnd':'"+objToSendToJammer.get("freqEnd")+"','ramOpMode':'"+objToSendToJammer.get("ramOpMode")+"','dwBit':'"+objToSendToJammer.get("dwBit")+"','dwTimeCtr':'"+objToSendToJammer.get("dwTimeCtr")+"'}";
				makeDataSendString=makeDataSendString.replace("\'",""+"\"");
				System.out.println(makeDataSendString);
		    	rs = new ApiCommon().HTTP_Request(ip, 80 ,url+i+".json", "POST" ,makeDataSendString);
			    
			    if (rs.equalsIgnoreCase("-100")==true||rs==null || rs.equalsIgnoreCase("") )
				{
					//return  "error";
			    	return "Failure at Socket Level";  //error	
				}
			    else if(!rs.contains("\"status\":\"200\""))
			    		
			    {
			    	return rs;
			    }
				else
				{
					flag=1; // success
				
					
				}
	    	
		    	ja_dataToSendToJammer.put(objToSendToJammer);
		    	
			}

		   
			String flag2= JammertxPowerSave(txPower,ip);
			String flag3= JammerloIfOffsetKhz(loIfOffsetKhz,ip);
			
			
			if(!flag2.equalsIgnoreCase("1")) {
				return flag2;
			}
			if(!flag3.equalsIgnoreCase("1")) {
				return flag3;
			}
			else {
				 for(int i=0;i<parts.length;i++)
					{
				    	obj = new JSONObject();
						
						
						obj.put("profileMode", appliedFrequency[i].replaceAll("\"", ""));
					    obj.put("freqStart", startSubBand[i]+"000");
					    obj.put("freqEnd", EndSubBand[i]+"000");
					    obj.put("addrStart", addrStart[i]+"");
					    obj.put("addrEnd", addrEnd[i]+"");
					    obj.put("dwTimeCtr", dwTimeCtr[i]+"");
					    obj.put("ramOpMode", ramOpMode[i]+"");
					    obj.put("dwBit", dwBit[i]+"");
					    obj.put("dwBit", dwBit[i]+"");
					    obj.put("mode", mode[i]+"");
					    ja_data.put(obj);
					}
					
				 	obj = new JSONObject();
					obj.put("txPower", txPower);
					
					ja_data.put(obj);
					
					obj = new JSONObject();
					obj.put("loIfOffsetKhz", loIfOffsetKhz);
					ja_data.put(obj);
					
					String tempQuery="update btsmaster set config = '"+ja_data +"'  where b_id=1190";
					new Common().executeDLOperation(tempQuery);
					LinkedHashMap<String,String> log = new LinkedHashMap<String,String>();
					String logDataDB=ja_data.toString().substring(1);
					logDataDB = logDataDB.substring(0, logDataDB.length() - 1);
					 
					   
					log.put("IP",ip);
				
					//log.put("Data",logDataDB);
					log.put("component_type","SPOILER\" , "+logDataDB);
					new AuditHandler().auditConfigJammer(log);
					//log put here
					
			
			
			
			
			
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		 fileLogger.info("Exit Function : JammerSave");
		//return Response.status(201).entity(rs.toString()).build();
		 return "1";
	}
 	
 	
 	
 	
 	
 	
 		
		public String JammertxPowerSave(String txPower,String ip) {
			 fileLogger.info("Inside Function : JammertxPowerSave");
			 String  rs = "";
 	
			 JSONObject objToSendToJammer = new JSONObject();
			 try {
				objToSendToJammer.put("txPower", txPower);
				JSONArray ja_dataToSendToJammer = new JSONArray();
				ja_dataToSendToJammer.put(objToSendToJammer);
				
			    String url="/txPower.json";
				
			    
			    
			    String dataToSendJammerTxPower=ja_dataToSendToJammer.toString();
	    		dataToSendJammerTxPower=dataToSendJammerTxPower.replace("[","'");
			    dataToSendJammerTxPower=dataToSendJammerTxPower.replace("]","'");
			    dataToSendJammerTxPower=dataToSendJammerTxPower.replace("\"",("\\"+"\""));
			    rs=  new ApiCommon().HTTP_Request(ip, 80, url, "POST", dataToSendJammerTxPower);
			    if (rs.equalsIgnoreCase("-100")==true)
				{
					return "Failure at Socket Level";  //fail
				}
				 else if(!rs.contains("\"status\":\"200\""))
			    {
			    	return rs;
			    }
				
				else
				{
					return "1"; //success
				}
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return e.getMessage();
			} //20 to 40 dbm
			
 	}
		
		
		
		public String JammerloIfOffsetKhz(String loIfOffsetKhz,String ip ) {
			 fileLogger.info("Inside Function : JammerloIfOffsetKhz");
			 String  rs = "";
	
			 JSONObject objToSendToJammer = new JSONObject();
			 try {
				objToSendToJammer.put("loIfOffsetKhz", loIfOffsetKhz);
				JSONArray ja_dataToSendToJammer = new JSONArray();
				ja_dataToSendToJammer.put(objToSendToJammer);
				
				
//				String querywa1 = "select * from btsmaster where devicetypeid = 21;";
//			    JSONArray ja1 = new Operations().getJson(querywa1);
//			    String ip="";
//				try {
//					ip = ja1.getJSONObject(0).getString("ip");
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			    String url="/loIfOffsetKhz.json";
				
			    
			    
			    String dataToSendJammerJammerloIfOffsetKhz=ja_dataToSendToJammer.toString();
	    		dataToSendJammerJammerloIfOffsetKhz=dataToSendJammerJammerloIfOffsetKhz.replace("[","'");
			    dataToSendJammerJammerloIfOffsetKhz=dataToSendJammerJammerloIfOffsetKhz.replace("]","'");
			    dataToSendJammerJammerloIfOffsetKhz=dataToSendJammerJammerloIfOffsetKhz.replace("\"",("\\"+"\""));
			    rs=  new ApiCommon().HTTP_Request(ip, 80, url, "POST", dataToSendJammerJammerloIfOffsetKhz);
			    //Response rs =  new ApiCommon().sendRequestToUrl(url,null,ja_dataToSendToJammer.toString());
				//if rs.equalsIgnoreCase(anotherString)!=-100)
				if (rs.equalsIgnoreCase("-100")==true)
				{
					return "Failure at Socket Level"; //fail
				}
				 else if(!rs.contains("\"status\":\"200\""))
			    {
			    	return rs;
			    }
				
				else
				{
					return "1"; //success
				}
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return e.getMessage();
			} //20 to 40 dbm
			
	}
		
		
		public int JammertxPowerGet(String ip) {
			 fileLogger.info("Inside Function : JammertxPowerGet");
	
	
			 JSONObject objToSendToJammer ;
			 try {
				
			    String url="/txPower.json";
				
				
				
			    String  rs =  new ApiCommon().HTTP_Request(ip, 80,url,"GET" ,"");
			    if (rs.equalsIgnoreCase("-100")==true)
				{
					return 0; //fail
				}
				else if(!rs.contains("\"status\":\"200\""))
			    {
			    	return 0;
			    }
					
				objToSendToJammer = new JSONObject(rs);
				int txPower=Integer.parseInt(objToSendToJammer.getJSONObject("data").getString("txPower"));
				return txPower;
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return -1;
			} //20 to 40 dbm
			
	}
	
 	
 	 @POST
 	 @Path("/getJammerData")
 	 @Produces(MediaType.APPLICATION_JSON)
 	 public Response getJammerData(@FormParam("JammerBand") String JammerBand ) {
		 	String query="select * from supported_band  where c_band = '"+JammerBand + "' limit 1;";
		 	 JSONArray rs = new Operations().getJson(query);
		 	 JSONObject obj= new JSONObject();
		 	 try {
			obj= rs.getJSONObject(0);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		 return Response.status(201).entity(rs.toString()).build();
 	 }
 	 
 	 
 	@POST
	 @Path("/getJammerDataDB")
	 @Produces(MediaType.APPLICATION_JSON)
	 public Response getJammerDataDB( @FormParam("ip") String ip) {
		 	String query="select * from btsmaster  where ip = '"+ ip +"' ;";
		 	JSONArray rs = new Operations().getJson(query);
		 	String ipJammer="";
				
		 return Response.status(201).entity(rs.toString()).build();
	 }

	
 	 
 	 
 	 @POST
 	 @Path("/JammerOnOff")
 	 @Produces(MediaType.APPLICATION_JSON)
 	 public int JammerOnOff(@FormParam("startStop") String startStop,@FormParam("ip") String ip)  {
	
	    String url="/jammingMode.json";
		   
	    JSONObject objToSendToJammer = new JSONObject();
	    try {
			objToSendToJammer.put("jammingMode", startStop);
		} catch (JSONException e) {
			
			e.printStackTrace();
		} 
	    JSONArray ja_dataToSendToJammer=new JSONArray();
	    ja_dataToSendToJammer.put(objToSendToJammer);
	    try {
	    	String dataTosendOnOFF=ja_dataToSendToJammer.toString();
	    	dataTosendOnOFF=dataTosendOnOFF.replace("[","'");
	    	dataTosendOnOFF=dataTosendOnOFF.replace("]","'");
	    	dataTosendOnOFF=dataTosendOnOFF.replace("\"",("\\"+"\""));
	    	String rs =  new ApiCommon().HTTP_Request(ip,80,url,"POST",ja_dataToSendToJammer.toString());
	    	if (rs.equalsIgnoreCase("-100")==true||rs==null || rs.equalsIgnoreCase(""))
			{
				return -1;
			}
	    	new TwogOperations().updateStatusOfBts(ip);
	    }
	    
	    
		 	catch(Exception ex){
		 		fileLogger.debug("JammerOnOff Exception came "+ex);
		 		return -1;
		 	}
 		 return 1;//Response.status(201).entity();
 	 }
	
	
	
	
}
