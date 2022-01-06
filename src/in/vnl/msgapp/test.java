package in.vnl.msgapp;

import java.util.ArrayList;
import java.util.Collections;

import org.apache.log4j.Logger;
import org.json.JSONObject;

public class test {
	static Logger fileLogger = Logger.getLogger("file");
	
	public static void main(String args[])
	{
		String aa = "'{\"value\":\"Normal_LU    IMSI:404045000053267 IMEI:867466028723794 MSISDN:                 PTMSI:0x00000000 TMSI:0x00000000 OL:0xfffe NL:0x0006 CID:0x0001 Ta:0   RxL:-110 TStmp:310170-18:22:26 FTyp:FAIL_NO_SUITABLE_CELLS_IN_LOCATION_AREA\",\"channel\":\"stdout\"}'";
		aa = aa.substring(1,aa.length()-1);
		String jsonString = "{\"stat\": { \"sdr\": \"aa:bb:cc:dd:ee:ff\", \"rcv\": \"aa:bb:cc:dd:ee:ff\", \"time\": \"UTC in millis\", \"type\": 1, \"subt\": 1, \"argv\": [{\"type\": 1, \"val\":\"stackoverflow\"}]}}";
		//fileLogger.debug(aa);
		//aa= aa.sp
		try
		{	
			JSONObject dd = new JSONObject(aa);
			fileLogger.debug(dd.toString());
			String [] bb = dd.get("value").toString().split(" ");
			ArrayList<String> packet = new ArrayList<String>();
			Collections.addAll(packet, bb);
			ArrayList<String> finalPacket = new ArrayList<String>();
			for(int i=0;i< packet.size();i++)
			{
				
				if(!packet.get(i).trim().equalsIgnoreCase(""))
				{
					
					finalPacket.add(packet.get(i));
				}
			}
			fileLogger.debug(finalPacket.toString());
			for(String cc:finalPacket)
			{
				fileLogger.debug(cc);
			}
		}
		catch(Exception E)
		{
			fileLogger.debug(E.getMessage());
		}
		
		

	}

}
