package in.vnl.api.ptz;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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

import org.apache.commons.io.FileUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.core.Response.ResponseBuilder;

import org.json.JSONException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import in.vnl.EventProcess.DBDataService;
import in.vnl.EventProcess.EventData;
import in.vnl.api.common.ApiCommon;
import in.vnl.api.common.CommonOperation;
import in.vnl.api.threeg.ThreegOperations;
import in.vnl.api.twog.TwogOperations;
import in.vnl.api.twog.livescreens.TriggerCueServer;
import in.vnl.bist.Bist;
import in.vnl.msgapp.Common;
import in.vnl.msgapp.GPSSocketServer;
import in.vnl.msgapp.GeoSchedulerServer;
import in.vnl.msgapp.Operations;
import in.vnl.scheduler.NetscanSchedulerListener;
import in.vnl.scheduler.NetscanSingletonExecutor;
import in.vnl.sockets.UdpServerClient;
import in.vnl.api.common.livescreens.AutoOperationServer;
import in.vnl.api.common.livescreens.AutoStateServer;
import in.vnl.api.common.livescreens.ScanTrackModeServer;
import in.vnl.api.config.PossibleConfigurations;
import in.vnl.api.netscan.CurrentNetscanAlarm;
import in.vnl.api.netscan.NetscanOperations;
import in.vnl.api.netscan.NetscanTask;
import in.vnl.report.Compress;
import in.vnl.report.ReportServer;

@Path("/ptz")
public class PTZ 
{
	//static Logger logger = Logger.getLogger(CommonService.class);
    static Logger fileLogger = Logger.getLogger("file");
    static Logger statusLogger = Logger.getLogger("status");
	static String  oprStartTime=null;   
   @POST
   @Path("/updateMoveStepAngle")
   @Produces(MediaType.APPLICATION_JSON)
   public Response updateConfigSibInfo(@FormParam("ip") String ip ,@FormParam("id") String id,@FormParam("angle") String config)
   {	

		 fileLogger.info("Inside Function : updateConfigSibInfo");	
	   //String query = "select  btsmaster.b_id,btsmaster.ip,btsmaster.status,btsnetworktype.name from btsmaster inner join btsnetworktype on(typeid = n_id)";
	   Common co = new Common();
	   String query = "update btsmaster set config = jsonb_set(config,'{step}','"+config+"') where ip='"+ip+"' and b_id="+id;
		fileLogger.debug(query);
		JSONArray rs =  new Operations().getJson(query);
		 fileLogger.info("Exit Function : updateConfigSibInfo");
        return Response.status(201).entity(rs.toString()).build();
    }
   
   @GET
   @Path("/setp_angle")
   @Produces(MediaType.APPLICATION_JSON)
   public Response redirecholdmeseventtionEvnt(@QueryParam("ip") String ip,@QueryParam("id") String id)
   {	
	   fileLogger.info("Inside Function : redirecholdmeseventtionEvnt");
	   //String query = "select  btsmaster.b_id,btsmaster.ip,btsmaster.status,btsnetworktype.name from btsmaster inner join btsnetworktype on(typeid = n_id)";
	   Common co = new Common();
	   
	   String query = "select  config from btsmaster where ip='"+ip+"' and b_id="+id;
		fileLogger.debug(query);
		JSONArray rs =  new Operations().getJson(query);
		   fileLogger.info("Exit Function : redirecholdmeseventtionEvnt");
        return Response.status(201).entity(rs.toString()).build();
    }
   
   @GET
   @Path("/angle")
   @Produces(MediaType.APPLICATION_JSON)
   public Response angle(@QueryParam("ip") String ip)
   {	
	   fileLogger.info("Inside Function : angle");
	   //String query = "select  btsmaster.b_id,btsmaster.ip,btsmaster.status,btsnetworktype.name from btsmaster inner join btsnetworktype on(typeid = n_id)";
	   	Common co = new Common();
	   	String query = "select angle_offset from  antenna where atype='1' limit 1";
	   	JSONArray angle = new Operations().getJson(query);
		fileLogger.debug(query);
		JSONArray rs =  new Operations().getJson(query);
		 fileLogger.info("Exit Function : angle");
        return Response.status(201).entity(angle.toString()).build();
    }
   
   @GET
   @Path("/getcurrentangle")
   @Produces(MediaType.APPLICATION_JSON)
   public Response angle()
   {	
	   fileLogger.info("Inside Function : angle");
	   //String query = "select  btsmaster.b_id,btsmaster.ip,btsmaster.status,btsnetworktype.name from btsmaster inner join btsnetworktype on(typeid = n_id)";
		JSONArray ptzDataArray = new Operations().getJson("select * from view_btsinfo where code = 9");
		JSONObject gpsObject=new JSONObject();
		String currentPtzVal="";
		HashMap<String,String> rs = new HashMap<String,String>();
		try {
			JSONObject hummerDataObject = ptzDataArray.getJSONObject(0); 
			currentPtzVal=getCurrentAngle(hummerDataObject.getString("ip"));
			String[] ptzArr = currentPtzVal.split(":");
			rs.put("angle", ptzArr[0]);
			rs.put("tilt", ptzArr[1]);
		} catch (NumberFormatException e) {
			fileLogger.error("numberformatexception in getcurrentangle");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			fileLogger.error("UnknownHostException in getcurrentangle");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			fileLogger.error("JSONException in getcurrentangle");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			fileLogger.error("IOException in getcurrentangle");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		 fileLogger.info("Exit Function : angle");
        return Response.status(201).entity(rs).build();
    }
   
   
   @GET
   @Path("/move")
   @Produces(MediaType.APPLICATION_JSON)
   public Response move(@QueryParam("ip") String ip,@QueryParam("direction") int direction,@QueryParam("angle") int angle)
   {	
	   fileLogger.info("Inside Function : move");
	   //String query = "select  btsmaster.b_id,btsmaster.ip,btsmaster.status,btsnetworktype.name from btsmaster inner join btsnetworktype on(typeid = n_id)";
	   int currentAngle = -1;
	  
	   int maxAngle=Integer.parseInt(DBDataService.getConfigParamMap().get("maxAngle"));
	   int minAngle=Integer.parseInt(DBDataService.getConfigParamMap().get("minAngle"));
	   int TiltMin=Integer.parseInt(DBDataService.getConfigParamMap().get("TiltMin"));
	   int TiltMax=Integer.parseInt(DBDataService.getConfigParamMap().get("TiltMax"));
	   
	   
		String currentAngleSaved1="";
		int currentAngleSaved=-1;
		String[]  ptzArr = {};
		HashMap < String, String > rs = new HashMap < String, String > ();
		try {
			currentAngleSaved1 = PTZ.getCurrentAngle(ip);
			fileLogger.info("Inside Function : checkantenatypeandmovesection ... currentAngleSaved1 = "+currentAngleSaved1);
			//currentAngleSaved1.split(":");
			ptzArr = currentAngleSaved1.split(":");
			fileLogger.debug("INside fxn move ptzArr:"+ptzArr);
			currentAngleSaved=Integer.parseInt(ptzArr[0]);
			fileLogger.debug("INside fxn move currentAngleSaved:"+currentAngleSaved);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			
			rs.put("result", "error");
		    return Response.status(201).entity(rs).build();
		}
		
		
		if(direction==4 || direction==5) 
		{
			fileLogger.debug("INside fxn move diretion =4 or 5 angle:"+angle);
				if(direction==4)
				{
					if( (currentAngleSaved-angle) < minAngle)
					{
						rs = new HashMap < String, String > ();
						rs.put("result", "error--- MinAngle="+minAngle);
					    return Response.status(201).entity(rs).build();
				
					}
				}
				else{
					if((currentAngleSaved+angle>maxAngle))
					{
						rs = new HashMap < String, String > ();
						rs.put("result", "error---MaxAngle="+maxAngle );
					    return Response.status(201).entity(rs).build();
				
					}
				}
		}
		else if(direction==2||direction==7)
		{
			try
			{
				
				currentAngleSaved=Integer.parseInt(ptzArr[1]);
			}
			
			catch(Exception ex)
			{
				fileLogger.debug("Exception occureed1wa fxn move :"+ex);
			}
			fileLogger.debug("INside fxn move diretion =2 or 7 currentAngleSaved:"+currentAngleSaved);
			fileLogger.debug("INside fxn move diretion =2 or 7 angle:"+angle);
		    if (direction == 2) {
		    	if((currentAngleSaved-angle) < TiltMin)
				{
					rs.put("result", "error--- MinTilt="+TiltMin);
					
				    return Response.status(201).entity(rs).build();
				}
				
		    }
		    else if((currentAngleSaved+angle)>TiltMax)
			{
				rs.put("result", "error---MaxTilt="+TiltMax );
				
			    return Response.status(201).entity(rs).build();
			}
			//up or down
		}
		else {
			
		}
//		
		
		try 
		{
			
			if(direction ==-1) 
			{
				//jumpToPositionPacket(ip,direction,angle);
				int struOffset=Integer.parseInt(DBDataService.getConfigParamMap().get("struoffset"));
				jumpToPositionPacket(ip,0, 0, true);
				jumpToPositionPacket(ip,struOffset, 0, false);
			}
			else 
			{
				sendPacket(ip, direction, angle);
			}
			
/*			currentAngle = getCurrentAngle(ip);
			if(currentAngle !=-1) 
			{
				String query = "update antenna set angle_offset = "+currentAngle+" where atype=1";
				Common co = new Common();
				co.executeDLOperation(query);
			}*/
			
			try {
				Thread.sleep(3000);
			} catch (Exception e1) {
				fileLogger.error("exception is :"+e1.getMessage());
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
/*			JSONArray ptzDataArray = new Operations().getJson("select * from view_btsinfo where code = 9");
			try {
				JSONObject hummerDataObject = ptzDataArray.getJSONObject(0); 
				JSONObject gpsObject = sendGPSCommand(hummerDataObject.getString("ip"),Integer.parseInt(DBDataService.getConfigParamMap().get("ptz_port")), false, false);
				return Response.status(201).entity(gpsObject.toString()).build();
			} catch (NumberFormatException e) {
				fileLogger.debug("numberformatexception in getnorthheading");
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnknownHostException e) {
				fileLogger.debug("UnknownHostException in getnorthheading");
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				fileLogger.debug("JSONException in getnorthheading");
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				fileLogger.debug("IOException in getnorthheading");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		} 
		catch (Exception e) 
		{
			fileLogger.debug("Unable to move "+e.getMessage());
			rs.put("result", "error");
			
		    return Response.status(201).entity(rs).build();
		}
		 fileLogger.info("Exit Function : move");
		 rs.put("result", "success");
	   
        return Response.status(201).entity(rs).build();
    }
   
   @GET
   @Path("/getnorthheading")
   @Produces(MediaType.APPLICATION_JSON)
   public Response getNorthHeading()
   {	
	   fileLogger.info("Inside Function : getNorthHeading");
	   //String query = "select  btsmaster.b_id,btsmaster.ip,btsmaster.status,btsnetworktype.name from btsmaster inner join btsnetworktype on(typeid = n_id)";
		JSONArray ptzDataArray = new Operations().getJson("select * from view_btsinfo where code = 9");
		JSONObject gpsObject=new JSONObject();
		try {
			JSONObject hummerDataObject = ptzDataArray.getJSONObject(0); 
			gpsObject = sendGPSCommand(hummerDataObject.getString("ip"),Integer.parseInt(DBDataService.getConfigParamMap().get("ptz_port")), false, false);
		} catch (NumberFormatException e) {
			fileLogger.error("numberformatexception in getnorthheading");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			fileLogger.error("UnknownHostException in getnorthheading");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			fileLogger.error("JSONException in getnorthheading");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			fileLogger.error("IOException in getnorthheading");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 fileLogger.info("Exit Function : getNorthHeading");
        return Response.status(201).entity(gpsObject.toString()).build();
    }
   
   @GET
   @Path("/sendgpscommand")
   @Produces(MediaType.APPLICATION_JSON)
   public Response sendGPSCommand(@QueryParam("ip") String ip,@QueryParam("direction") int direction,@QueryParam("angle") int angle)
   {	
	   fileLogger.info("Inside Function : sendGPSCommand");
	   //String query = "select  btsmaster.b_id,btsmaster.ip,btsmaster.status,btsnetworktype.name from btsmaster inner join btsnetworktype on(typeid = n_id)";
	   int currentAngle = -1;
		try 
		{
			
			sendGPSCommand("192.168.20.8", 2000, false, false);
		} 
		catch (Exception e) 
		{
			fileLogger.debug("Unable to move "+e.getMessage());
		}
		 fileLogger.info("Exit Function : sendGPSCommand");
        return Response.status(201).entity(currentAngle).build();
    }
   
   public static String getCurrentAngle(String serverName) throws IOException 
   {
	   fileLogger.info("Inside Function : getCurrentAngle");
	   int port = Integer.parseInt(new Common().getDbCredential().get("ptz_port"));
	   int tryConnect = 1;
       int current_angle =-1;
       int currentTilt =-1;
       Socket client = new Socket(serverName, port);
       
       while (tryConnect < 3) {
           OutputStream outToServer;
           DataOutputStream out = null;
           
           try 
           {   
                
           
           
               outToServer = client.getOutputStream();
               out = new DataOutputStream(outToServer);


               ByteBuffer bb = ByteBuffer.allocate(2);
               bb.order(ByteOrder.LITTLE_ENDIAN);
               bb.putShort((short) 10);
               //bb.put((byte)0x0A);

               out.write(bb.array());

               byte[] resp = new byte[10];
               int bytesRead = -1;
               do {
                   bytesRead = client.getInputStream().read(resp);
               } while (bytesRead == -1);
               fileLogger.debug("PTZPacketSender: getCurrentAngle: Received " + bytesRead + " bytes from server");
               fileLogger.debug("PTZPacketSender: " + resp[2] + "   " + resp[3]);
               String angleInHex = String.format("%02X ", resp[5]).trim() + String.format("%02X ", resp[4]).trim() + String.format("%02X ", resp[3]).trim() + String.format("%02X ", resp[2]).trim();
               Long i = Long.parseLong(angleInHex, 16);
               String tiltInHex = String.format("%02X ", resp[9]).trim() + String.format("%02X ", resp[8]).trim() + String.format("%02X ", resp[7]).trim() + String.format("%02X ", resp[6]).trim();
               Long tiltInLong = Long.parseLong(tiltInHex, 16);
               Float angle = Float.intBitsToFloat(i.intValue());
               Float tilt = Float.intBitsToFloat(tiltInLong.intValue());
               int struoffset=Integer.parseInt(DBDataService.configParamMap.get("struoffset")); 
               tilt-=struoffset;
               fileLogger.debug("PTZPacketSender: Angle rec : " + angle+"and tilt is :"+tilt);

               tryConnect++;
               tryConnect++;

               out.close();
               client.close();
               client = null;
               current_angle = Math.round(angle);
               currentTilt = Math.round(tilt);
               //int struOffset=Integer.parseInt(DBDataService.getConfigParamMap().get("struoffset"));
               fileLogger.debug("struOffset is :"+struoffset);
               //currentTilt=currentTilt-struOffset;
               if(current_angle < 0) {
                   current_angle = 0;
               }

           } 
           catch (IOException e) 
           {
        	   fileLogger.error("Exception Unable to get angle from ptz "+e.getMessage());
           }catch(Exception ex){
        	   fileLogger.error("Exception Unable to get angle from ex "+ex.getMessage()); 
           }
       }
       fileLogger.info("Exit Function : getCurrentAngle");
       return current_angle+":"+currentTilt;
       
   }
   
   
   
   public void sendPacket(String serverName, int direction, int target) throws UnknownHostException, IOException 
   {
       
	   fileLogger.info("Inside Function : sendPacket");
	   int port = Integer.parseInt(new Common().getDbCredential().get("ptz_port"));
	   int speed = Integer.parseInt(new Common().getDbCredential().get("ptz_speed"));
	   int tryConnect = 1;
       Socket client =null;
       while (tryConnect < 3) 
       {
           DataOutputStream out = null;
           try 
           {
        	   client = new Socket(serverName, port);
        	   
               OutputStream outToServer = client.getOutputStream();
               
               out = new DataOutputStream(outToServer);
               
               ByteBuffer bb;
               if (direction == 28)
                   bb = ByteBuffer.allocate(11);
               else
                   bb = ByteBuffer.allocate(7);
               bb.order(ByteOrder.LITTLE_ENDIAN);
               
               bb.putShort((short) direction);
               bb.put((byte) 18);
               bb.putFloat((float) target);
               if (direction == 28)
                   bb.putFloat((float) target);
               out.write(bb.array());
               out.close();
               client.close();
               client = null;
               tryConnect++;
               tryConnect++;

           } catch (IOException e) {
        	   if (tryConnect < 2) {
        		   fileLogger.debug("PTZPacketSender : Trying to set one more time after 2 min");
                   try {
                       Thread.sleep(1000);
                   } catch (InterruptedException e1) {
                       // TODO Auto-generated catch block
                       e1.printStackTrace();
                   }
                   tryConnect++;
               } else {
                   tryConnect++;
                   tryConnect++;
                   e.printStackTrace();
                   out.close();
                   client = null;
                   throw e;
               }
        	   fileLogger.error("Exception Unable to move ptz "+e.getMessage());
           }
       }
       fileLogger.info("Exit Function : sendPacket");
   }
   
   
   
   public void jumpToPositionPacket(String serverName, int goToPosition, int reportingTime, boolean isPanCommand) throws UnknownHostException, IOException{
	   fileLogger.info("Inside Function : jumpToPositionPacket");	 
	   int port = Integer.parseInt(new Common().getDbCredential().get("ptz_port"));
	   int tryConnect=1;
	   Socket client = new Socket(serverName, port);
	   while(tryConnect <3)
	   {
	   DataOutputStream out = null;
	   try {
	   if(client==null)
	   {
	   fileLogger.debug("PTZPacketSender: Connecting to " + serverName + " on port " + port);
	   client = new Socket(serverName, port);
	   }
	   OutputStream outToServer = client.getOutputStream();
	   out = new DataOutputStream(outToServer);
	   fileLogger.debug("PTZPacketSender: Sending Jump Command");
	   ByteBuffer bb = ByteBuffer.allocate(12);
	   bb.order(ByteOrder.LITTLE_ENDIAN);
	   bb.putShort((short)24);
	   if(isPanCommand)
	   {
	   bb.put((byte)0);
	   }else {
	   bb.put((byte)1);
	   }
	   bb.put((byte)18);
	   bb.putFloat((float)goToPosition);
	   bb.putInt((int)reportingTime);
	   out.write(bb.array());
	   out.close();
	   client.close();
	   client = null;
	   tryConnect++;
	   tryConnect++;
	   /*InputStream inFromServer = client.getInputStream();
	   DataInputStream in = new DataInputStream(inFromServer);

	   System.out.println("Server says " + in.readUTF());*/
	   } catch (IOException e) {
	   if(tryConnect<2)
	   {
	   fileLogger.debug("PTZPacketSender : Trying to set one more time after 2 min");
	   try {
	   Thread.sleep(1000);
	   } catch (InterruptedException e1) {
	   // TODO Auto-generated catch block
	   e1.printStackTrace();
	   }
	   client = new Socket(serverName, port);
	   tryConnect++;
	   }else {
	   tryConnect++;
	   tryConnect++;
	   e.printStackTrace();
	   out.close();
	   client = null;
	   throw e;
	   }
	   }
	   }
	   fileLogger.info("Exit Function : jumpToPositionPacket");	
	   }
   
   public JSONObject sendGPSCommand(String serverName, int port, boolean sync, boolean syncValue) throws UnknownHostException, IOException{
	   fileLogger.info("Inside Function : sendGPSCommand");	
	   //  fileLogger.debug("in sendGPSCommand entry");
	   int tryConnect=1;
	   float values[] = null;
	   Long valid=-1L;
	   Long disconnectStatus=-1L;
	   Socket client = new Socket(serverName, port);
	   JSONObject gpsObject=new JSONObject();
	   while(tryConnect <3)
	   {
	   DataOutputStream out = null;
	   try {
	   if(client==null)
	   {
	   fileLogger.debug("PTZPacketSender: Connecting to " + serverName + " on port " + port);
	   client = new Socket(serverName, port);
	   }
	   OutputStream outToServer = client.getOutputStream();
	   out = new DataOutputStream(outToServer);
	   //System.out.println("PTZPacketSender: Sending stop scan Command");
	   ByteBuffer bb;
	   if(sync) {
	   bb = ByteBuffer.allocate(3);
	   bb.order(ByteOrder.LITTLE_ENDIAN);
	   bb.putShort((short)42);
	   bb.put((byte)(syncValue?1:0));
	   out.write(bb.array());
	   }
	   else {
	   bb = ByteBuffer.allocate(2);
	   bb.order(ByteOrder.LITTLE_ENDIAN);
	   bb.putShort((short)41);
	   out.write(bb.array());
	   byte[] resp = new byte[36];
	   int bytesRead = -1;
	   do {
	   bytesRead = client.getInputStream().read(resp);
	   } while(bytesRead == -1);
	   fileLogger.debug("Received " + bytesRead + " bytes from server");
	   //System.out.println(resp[2]+"   "+ resp[3]);
	   values = new float[4];
	   

	   
	   String value = String.format("%02X ", resp[11]).trim()+String.format("%02X ", resp[10]).trim()+String.format("%02X ", resp[9]).trim()+String.format("%02X ", resp[8]).trim();
	   Long i = Long.parseLong(value, 16);
	           values[0] = Float.intBitsToFloat(i.intValue());
	           fileLogger.debug("PTZPacketSender: Pan : "+ values[0]);
	           
	           value = String.format("%02X ", resp[27]).trim()+String.format("%02X ", resp[26]).trim()+String.format("%02X ", resp[25]).trim()+String.format("%02X ", resp[24]).trim();
	   i = Long.parseLong(value, 16);
	   values[1] = Float.intBitsToFloat(i.intValue());
	   		  fileLogger.debug("PTZPacketSender: Yaw : "+ values[1]);
	           
	           value = String.format("%02X ", resp[31]).trim()+String.format("%02X ", resp[30]).trim()+String.format("%02X ", resp[29]).trim()+String.format("%02X ", resp[28]).trim();
	   i = Long.parseLong(value, 16);
	   values[2] = Float.intBitsToFloat(i.intValue());
	   		fileLogger.debug("PTZPacketSender: Lat : "+ values[2]);
	           
	           value = String.format("%02X ", resp[35]).trim()+String.format("%02X ", resp[34]).trim()+String.format("%02X ", resp[33]).trim()+String.format("%02X ", resp[32]).trim();
	   i = Long.parseLong(value, 16);
	   values[3] = Float.intBitsToFloat(i.intValue());
	   fileLogger.debug("PTZPacketSender: Lon : "+ values[3]);
	           String gpsCmd = String.format("%02X ", resp[7]).trim();
	           valid = Long.parseLong(gpsCmd, 16);
	           String disConnectCmd=String.format("%02X ", resp[6]).trim();
	           disconnectStatus = Long.parseLong(disConnectCmd, 16);;
	           
	   }
	   
       //int offset=((int)(values[1]-values[0])+360)%360;
	   int offset=((int)(values[1])+360)%360;
       int current_angle = (int)values[0];
       fileLogger.debug("PTZPacketSender: offset : "+ offset);
       String latitude=String.valueOf(values[2]);
       String longitude=String.valueOf(values[3]);
       
       String offsetStr="";
       try{
    	if(disconnectStatus==0){
    		gpsObject.put("status", "0");
    		gpsObject.put("type", "connection");
    		return gpsObject;
    		//longitude="0";
    	}else if(valid==0){
			fileLogger.debug("gps from stru is under stabiilization in stationary");
			gpsObject.put("status", "0");
			gpsObject.put("type", "stabilization");
			offsetStr="NA";
		}else{
			gpsObject.put("status", "1");
			gpsObject.put("ptAngle",current_angle);
			offsetStr=Integer.toString(offset);
			String offsetQuery="update antenna set angle_offset="+offset+" where atype='1'";
			new Common().executeDLOperation(offsetQuery);
			DBDataService.setAngleOffset(offset);
		}
       }catch(Exception er2){
    	   offsetStr="NA";
    	   fileLogger.debug("error is :"+er2.getMessage());
       }
       if(latitude.charAt(0)=='0' || longitude.charAt(0)=='0'){
			fileLogger.debug("invalid gps coordinates from stru stationary");
			return null;
		}
		
		if(offsetStr.equalsIgnoreCase("NA")){
			Operations operations = new Operations();
			int angleOffset=0;
			JSONArray angleOffsetArr = operations.getJson("select angle_offset from antenna where atype='1' limit 1");
			if(angleOffsetArr.length()!=0){
				try {
					angleOffset = angleOffsetArr.getJSONObject(0).getInt("angle_offset");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Common common = new Common();
			JSONArray lastGpsArr=operations.getJson("select lat,lon from gpsdata order by logtime desc limit 1");
			double oldLat=0.00;
			double oldLon=0.00;
			if(lastGpsArr.length()!=0){
				try {
					JSONObject lastGpsObject = lastGpsArr.getJSONObject(0);
					oldLat=lastGpsObject.getDouble("lat");
					oldLon=lastGpsObject.getDouble("lon");
				    fileLogger.debug("@gpsdata oldLat is :"+oldLat+",oldLon is :"+oldLon);
				} catch (JSONException e) {
					fileLogger.error("exception in gpsData message:"+e.getMessage());
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				int bearing=Common.calcBearingBetweenTwoGpsLoc(oldLat,oldLon,Double.parseDouble(latitude),Double.parseDouble(longitude));
				if(DBDataService.getAntennaToVehicleDiffAngle()>360){
					int antennaToVehicleDiffAngle = bearing-angleOffset;
					DBDataService.setAntennaToVehicleDiffAngle(antennaToVehicleDiffAngle);
				}
				angleOffset=Common.calcNewAngleOffset(bearing);
				
				fileLogger.debug("@gpsdata PTZ is bearing is :"+bearing);
				
/*				angleOffset+=bearing;
				if(angleOffset<0){
					angleOffset=360+angleOffset;
				}
				
				if(angleOffset>360){
					angleOffset=angleOffset%360;
				}*/
				String query_temp1="update antenna set angle_offset="+angleOffset+",antenna_angle="+ current_angle+" where atype='1'";
				common.executeDLOperation(query_temp1);
				fileLogger.debug("@sendGPSCommand feb23_21 "+query_temp1);
				DBDataService.setAngleOffset(angleOffset);
				offsetStr=Integer.toString(angleOffset);
			}else{
				offsetStr=Integer.toString(angleOffset);
			}
		}
		
		ArrayList<String> serverData = new ArrayList<String>();
		
		serverData.add("0");
		serverData.add("0");
		serverData.add("0");
		serverData.add("0");
		serverData.add(latitude);
		serverData.add("0");
		serverData.add(longitude);
		serverData.add("stationary");
		serverData.add(offsetStr);
		
		new GPSSocketServer().sendText(serverData);
		Common co = new Common();				
		//String query = "INSERT INTO cdrlogs(ip, count, packet_type, imsi, imei, msisdn, ptmsi, tmsi,ol, nl, cid, ta, rxl, tstmp, ftyp, inserttime)"
		//		+ "values('"+ip+"','"+count+"',"+packetString.toString()+")";
        String query = "INSERT INTO gpsData(lat,lon)"
				+ "values('"+latitude+"','"+longitude+"')";
		co.executeDLOperation(query);

	    
            try{
			gpsObject.put("offset", offsetStr);
			gpsObject.put("latitude", latitude);
			gpsObject.put("longitude", longitude);
		} catch (JSONException e) {
			fileLogger.error("exception in creating gps packet");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   out.close();
	   client.close();
	   client = null;
	   tryConnect++;
	   tryConnect++;
	   }catch(IOException e) {
	   if(tryConnect<2)
	   {
	   fileLogger.debug("PTZPacketSender : Trying to set one more time after 2 min");
	   try {
	   Thread.sleep(1000);
	   } catch (InterruptedException e1) {
	   // TODO Auto-generated catch block
	   e1.printStackTrace();
	   }
	   client = new Socket(serverName, port);
	   tryConnect++;
	   }else {
	   tryConnect++;
	   tryConnect++;
	   e.printStackTrace();
	   out.close();
	   client = null;
	   throw e;
	   }
	   }
	   }
	   //return values;
	   fileLogger.info("Exit Function : sendGPSCommand");	
	   return gpsObject;
   	   }
   
}
