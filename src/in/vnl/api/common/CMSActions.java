package in.vnl.api.common;

import java.util.ArrayList;

public interface CMSActions 
{
	public void sendEventToCMS(String data);
	public boolean sotreEvent(String eventMessage,String type);
	public String createEventMessageFormat(String timeStamp,String id,String ip,String ta,String lat,String lon,String imsi,String eventType,String imei,String freq,String rxl,String cueId,String distance,String angle,String sector);	
	public String genrateTransId();
	public void sendCurrentModeCMS();
}
