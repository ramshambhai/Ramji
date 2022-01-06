package in.vnl.api.netscan;

import java.util.HashMap;

public class NetscanCommands 
{
	
	
	public static final HashMap<String,String> commands = new HashMap<String,String>();
	static
	{
		commands.put("GET_CURR_STATUS", "getCurrentStausOfBts");
		commands.put("SET_CURR_STATUS", "setCurrentStausOfBts");
		commands.put("START_FREQ_SCAN", "startFreqScan");
		commands.put("START_CELL_SCAN", "startCellScan");
		commands.put("GET_CELL_REPORT", "cellReport");
		commands.put("GET_FREQ_REPORT", "freqReport");
		commands.put("SET_ALARM_SIGNAL", "storeAlarm");
		commands.put("CELL_SCAN_REPORT", "insertCellReportData");
		commands.put("FREQ_SCAN_REPORT", "insertFreqReportData");
		commands.put("GPS_SCAN_REPORT", "insertGPSData");
		commands.put("GET_SCAN_REPORT", "insertGPSData");
		commands.put("SYS_ALARM", "storeSystemManagerAlarm");
	}
	
	public static String getFunctionName(String cmdType)
	{
		return commands.get(cmdType);
	}
}
