package in.vnl.msgapp;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.vnl.EventProcess.DBDataService;
import in.vnl.msgapp.Common;

public class PTZUdpListener implements Runnable {
	
	/*@Value("${ptz_udp_port}")
	int ptz_udp_port;*/
	
	int ptAngleRec;
	DatagramSocket serverSocket1;
	byte[] receiveData;
	byte[] sendData;
	
	static Logger fileLogger = Logger.getLogger("file");

	public PTZUdpListener() throws SocketException {
		//serverSocket1 = new DatagramSocket(140);
	    receiveData = new byte[36];
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			serverSocket1 = new DatagramSocket(1000);
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while(true)
        {
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			Long valid=-1L;
            try {          	
            	System.out.println("PTZUdpListener: Port: " + serverSocket1.getLocalPort());
				serverSocket1.receive(receivePacket);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            String sentence = new String( receivePacket.getData());
            System.out.println("PacketListener: RECEIVED: " + sentence);
            int gpsNode=DBDataService.getGpsNode();
            if(gpsNode==1){
            String command = String.format("%02X ", receiveData[1]).trim()+String.format("%02X ", receiveData[0]).trim();
			Long cmdId = Long.parseLong(command, 16);
			
	        if(cmdId == 41) {
	        	
	        	//String value = String.format("%02X ", receiveData[11]).trim()+String.format("%02X ", receiveData[10]).trim()+String.format("%02X ", receiveData[9]).trim()+String.format("%02X ", receiveData[8]).trim();
				//Long i = Long.parseLong(value, 16);
		        //float pan = Float.intBitsToFloat(i.intValue());
		        //System.out.println("PTZPacketSender: Pan : "+ pan);
		        
		        //value = String.format("%02X ", receiveData[27]).trim()+String.format("%02X ", receiveData[26]).trim()+String.format("%02X ", receiveData[25]).trim()+String.format("%02X ", receiveData[24]).trim();
				//i = Long.parseLong(value, 16);
				//float northOffset = Float.intBitsToFloat(i.intValue());
		        //System.out.println("PTZPacketSender: Yaw : "+ northOffset);
		        
	        	String value = String.format("%02X ", receiveData[31]).trim()+String.format("%02X ", receiveData[30]).trim()+String.format("%02X ", receiveData[29]).trim()+String.format("%02X ", receiveData[28]).trim();
				Long i = Long.parseLong(value, 16);
				float lat = Float.intBitsToFloat(i.intValue());
		        System.out.println("PTZUdpListener: Lat : "+ lat);
		        
		        value = String.format("%02X ", receiveData[35]).trim()+String.format("%02X ", receiveData[34]).trim()+String.format("%02X ", receiveData[33]).trim()+String.format("%02X ", receiveData[32]).trim();
				i = Long.parseLong(value, 16);
				float lon = Float.intBitsToFloat(i.intValue());
		        System.out.println("PTZUdpListener: Lon : "+ lon);
	        	
		        //dbserv.updateOffset(pan, northOffset);
		        
		        //dbserv.updateGPSData(receivePacket.getAddress().getHostAddress(), String.valueOf(lat), String.valueOf(lon), DBDataService.gpsAccuracy);
		        //int offset=((int)(northOffset-pan)+360)%360;
		        String offsetStr="";
		           String statusCommand = String.format("%02X ", receiveData[7]).trim();
					valid = Long.parseLong(statusCommand, 16);
/*		        try{
		 		if(valid==0){
		 			fileLogger.debug("gps from stru is under stabiilization in stationary");
		 			offsetStr="NA";
		 		}else{
		 			offsetStr=Integer.toString(offset);
		 			String offsetQuery="update antenna set angle_offset="+offset+" where atype='1'";
		 			new Common().executeDLOperation(offsetQuery);
		 		}
		        }catch(Exception er2){
		     	   offsetStr="NA";
		     	   fileLogger.debug("error is :"+er2.getMessage());
		        }*/
		        String latitude=String.valueOf(lat);
		        String longitude=String.valueOf(lon);
				if(latitude.charAt(0)=='0' || longitude.charAt(0)=='0'){
					fileLogger.debug("invalid gps coordinates from stru moving");
				}else{
						Operations operations = new Operations();
						Common common = new Common();
						JSONArray lastGpsArr=operations.getJson("select lat,lon from gpsdata order by logtime desc limit 1");
						double oldLat=0.00;
						double oldLon=0.00;
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
							
							double distance = operations.distanceFromLatLon(oldLat, oldLon,lat, lon, "K")*1000;
							distance = Math.round(distance*100.0)/100.0;
							double gpsAccuracy=0.00;
							JSONArray gpsAccuracyArr = operations.getJson("select accuracy from gps_accuracy order by id desc limit 1");
							if(gpsAccuracyArr.length()!=0){
								try {
									gpsAccuracy = gpsAccuracyArr.getJSONObject(0).getDouble("accuracy");
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						
							if(gpsAccuracyArr.length()!=0){
							int bearing=Common.calcBearingBetweenTwoGpsLoc(oldLat,oldLon,Double.parseDouble(latitude),Double.parseDouble(longitude));
							if(DBDataService.getAntennaToVehicleDiffAngle()>360){
								int antennaToVehicleDiffAngle = bearing-angleOffset;
								DBDataService.setAntennaToVehicleDiffAngle(antennaToVehicleDiffAngle);
							}
							angleOffset=Common.calcNewAngleOffset(bearing);
							
							fileLogger.debug("@gpsdata PTZUdpListener bearing is :"+bearing);
							offsetStr=Integer.toString(angleOffset);
							ArrayList<String> serverData = new ArrayList<String>();
							
							serverData.add("0");
							serverData.add("0");
							serverData.add("0");
							serverData.add("0");
							serverData.add(latitude);
							serverData.add("0");
							serverData.add(longitude);
							serverData.add("moving");
							serverData.add(offsetStr);
							
							new GPSSocketServer().sendText(serverData);
							Common co = new Common();				
							//String query = "INSERT INTO cdrlogs(ip, count, packet_type, imsi, imei, msisdn, ptmsi, tmsi,ol, nl, cid, ta, rxl, tstmp, ftyp, inserttime)"
							//		+ "values('"+ip+"','"+count+"',"+packetString.toString()+")";
							String query = "INSERT INTO gpsData(lat,lon)"
									+ "values('"+latitude+"','"+longitude+"')";
							co.executeDLOperation(query);
							query="update antenna set angle_offset="+angleOffset+" where atype='1'";
							co.executeDLOperation(query);
							DBDataService.setAngleOffset(angleOffset);
							}
						}else{
							offsetStr=Integer.toString(angleOffset);
							ArrayList<String> serverData = new ArrayList<String>();
							serverData.add("0");
							serverData.add("0");
							serverData.add("0");
							serverData.add("0");
							serverData.add(latitude);
							serverData.add("0");
							serverData.add(longitude);
							serverData.add("moving");
							serverData.add(offsetStr);
							
							new GPSSocketServer().sendText(serverData);
							Common co = new Common();				
							//String query = "INSERT INTO cdrlogs(ip, count, packet_type, imsi, imei, msisdn, ptmsi, tmsi,ol, nl, cid, ta, rxl, tstmp, ftyp, inserttime)"
							//		+ "values('"+ip+"','"+count+"',"+packetString.toString()+")";
							String query = "INSERT INTO gpsData(lat,lon)"
									+ "values('"+latitude+"','"+longitude+"')";
							co.executeDLOperation(query);
							query="update antenna set angle_offset="+angleOffset+" where atype='1'";
							co.executeDLOperation(query);
							DBDataService.setAngleOffset(angleOffset);
						}
							
							//common.executeDLOperation("update antenna set angle_offset="+angleOffset+" where atype='1'");
				}
				
	        }
	        else if(cmdId == 34) {
	        	
/*	        	String angleInHex = String.format("%02X ", receiveData[5]).trim()+String.format("%02X ", receiveData[4]).trim()+String.format("%02X ", receiveData[3]).trim()+String.format("%02X ", receiveData[2]).trim();
				Long i = Long.parseLong(angleInHex, 16);
		        Float angle = Float.intBitsToFloat(i.intValue());
		        System.out.println("Angle rec : "+ angle);
		        
		        String angleTiltInHex = String.format("%02X ", receiveData[9]).trim()+String.format("%02X ", receiveData[8]).trim()+String.format("%02X ", receiveData[7]).trim()+String.format("%02X ", receiveData[6]).trim();
				Long j = Long.parseLong(angleTiltInHex, 16);
		        Float angleTilt = Float.intBitsToFloat(j.intValue());
		        System.out.println("Tilt rec : "+ angleTilt);
		        
		        String epochTime1 = String.format("%02X ", receiveData[17]).trim()+String.format("%02X ", receiveData[16]).trim()+String.format("%02X ", receiveData[15]).trim()+String.format("%02X ", receiveData[14]).trim();
				Long epoch_Time = Long.parseLong(epochTime1, 16);
				System.out.println("EPOCH TIME RECEIVED: " + epoch_Time);
				
				Date date = new Date(epoch_Time * 1000);
		        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		        format.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
		        String formatted_pt_time = format.format(date);
		        String system_time = format.format(new Date(System.currentTimeMillis()));
		        
				ptAngleRec = Math.round(angle);
				if(ptAngleRec <0)
				{
					ptAngleRec = 0;
				}
				System.out.println("PTZUdpListener: Angle received from PT: "+ptAngleRec);
				logger.debug("PTZData - "+"\tTime : "+epoch_Time+"\tAngle : "+ptAngleRec);
				
				//PTZPacketSender.current_angle = ptAngleRec;
				try {
					PtzDataThread.queue.put("PTZAngle="+ptAngleRec);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//PTZPacketSender.setCurrentAngle(ptAngleRec);
				
				dbserv.recordPTZPositionData(receivePacket.getAddress().getHostAddress(), ptAngleRec, Math.round(angleTilt), formatted_pt_time, system_time);*/
	        }
			
            }
        }

	}

}
