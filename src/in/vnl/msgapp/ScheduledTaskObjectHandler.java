package in.vnl.msgapp;
import java.util.*;
import in.vnl.msgapp.GeoTask;
public class ScheduledTaskObjectHandler {
	public static HashMap<String,GeoTask> taskObjectMap=new HashMap<String,GeoTask>();
public static HashMap<String,GeoTask> getTaskObjectMap(){
	return taskObjectMap;
}
public static void setScheduledTaskObject(String id,GeoTask geoTask){
	taskObjectMap.put(id,geoTask);
}
public static GeoTask getScheduledTaskObject(String id){
return taskObjectMap.get(id);	
}
public static void removeScheduledTaskObject(String id){
	taskObjectMap.remove(id);	
	}
public static int getTaskObjectMapSize(){
return taskObjectMap.size();	
}
}
