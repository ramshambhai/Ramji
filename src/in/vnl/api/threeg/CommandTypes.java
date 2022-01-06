package in.vnl.api.threeg;

import java.util.HashMap;

public class CommandTypes 
{
	
	
	public static final HashMap<String,String> commands = new HashMap<String,String>();
	static
	{
		commands.put("GET_CURR_STATUS", "getCurrentStausOfBts");
		commands.put("SET_CURR_STATUS", "setCurrentStausOfBts");
		commands.put("SET_SUFI_CONFIG", "setSufiConfig");
		commands.put("GET_ACTIVE_CONFIG", "getSufiConfig");
		commands.put("SET_CELL_LOCK", "setCellLock");
		commands.put("SET_CELL_UNLOCK", "setCellUnlock");
		commands.put("SET_CELL_UNLOCK", "setCellUnlock");
		commands.put("SET_REDIRECTION_INFO", "setRedirectionInfo");
		commands.put("SET_ALARM_SIGNAL", "alarmData");
		commands.put("SET_LAC_CHANGE_EVENT", "updateLac");
		commands.put("SET_TRACK_SUB_LIST", "udpateSubscriberTrackList");
		commands.put("SET_HOLD_SUB", "updateSubHold");
		commands.put("SET_MEAS_TRIGGER", "triggerMes");
		commands.put("SET_SUB_HOLD_EVENT", "setSubHoldEvent");
		commands.put("SET_GEN_MEAS_EVENT", "setGEBMesEvent");
		commands.put("SET_DEDICATED_MEAS_EVENT", "dedicatedMeasEvent");
		commands.put("SET_SUB_REDIRECTION_EVENT", "setSubRedirectionEvent");
		commands.put("SET_SIB_DYN_UPDATE_EVENT","updateSibInfo");
		commands.put("SET_SUB_RELEASE_EVENT","releaseEvent");
		
		
	}
	
	public static String getFunctionName(String cmdType)
	{
		return commands.get(cmdType);
	}
}
