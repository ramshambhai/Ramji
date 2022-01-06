package in.vnl.api.common;

public class CurrentOperationType {
	
	public static String trans_id;
	public static boolean trigger;
	public static String cueId;

	public void setTransId(String id) 
	{
		trans_id = id;
	}
	
	public String getTransId() 
	{
		return trans_id;
	}
	
	public static String genrateTransId() 
	{
		trans_id = "FAL_"+System.currentTimeMillis();
		return trans_id;
	}
	
	public void setCueId(String id) 
	{
		cueId = id;
	}
	
	public static String getCueId() {
		return cueId;
	}

	public static String generateCueId() {
		cueId ="F_"+System.currentTimeMillis();
		return cueId;
	}
	
	public void setTrigger(boolean flag) 
	{
		trigger=flag;
	}
	
	public boolean isTriggerActive() 
	{
		return trigger;
	}
	
}
