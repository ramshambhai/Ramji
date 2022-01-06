package in.vnl.api.netscan;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class CurrentNetscanAlarm {
	
	static Logger fileLogger = Logger.getLogger("file");
	public static		String cmdCode;
	public static   	Long tstmp;		
	public static    	int orign ;		
	public static    	int alarmType; 
	public static    	int cause ;		
	public static    	String desc;
	public static		int id;
	public static      String opTech;
	
	
	
	public CurrentNetscanAlarm(JSONObject jo) 
	{
				
				try {
					fileLogger.debug("in argument constructor with jo :"+jo.toString());
					this.cmdCode 	=	jo.getString("CMD_CODE");
					this.id 		=	jo.getInt("NS_ID");;
					this.tstmp 		=	jo.getLong("TIME_STAMP");   
					this.orign 		=	jo.getInt("ORIGIN");        
					this.alarmType 	=	jo.getInt("ALARM_TYPE");    
					this.cause 		=	jo.getInt("CAUSE");         
					this.desc 		=	jo.getString("DESCRIPTION");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}
	
	
	public CurrentNetscanAlarm() 
	{
				
				
					this.cmdCode 	=	null;
					this.id 		=	-1;
					this.tstmp 		=	-1L;   
					this.orign 		=	-1;        
					this.alarmType 	=	-1;    
					this.cause 		=	-1;         
					this.desc 		=	null;
					this.opTech    =    null;
				
	}
	

}
