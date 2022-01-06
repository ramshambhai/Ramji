package in.vnl.report;
import in.vnl.msgapp.Common;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import in.vnl.api.twog.TwogOperations;
import in.vnl.msgapp.Operations;

public class ReportServer {
	static Logger fileLogger = Logger.getLogger("file");
	
	public JSONObject getDetailedData(String operationId)
	{
		fileLogger.info("Inside Function :getDetailedData" );
		String startTime = null;
		String endTime = null;
		
		String nodeWiseTableQuery="";
		String countryWiseTableQuery="";
		
		
		
		nodeWiseTableQuery="select * from getnodewisedatastats_detailed('"+startTime+"','"+endTime+"',"+operationId+");";
		countryWiseTableQuery="select * from courntrywisedatastats_detailed('"+startTime+"','"+endTime+"',"+operationId+");";
		
		
		fileLogger.debug(nodeWiseTableQuery);
		fileLogger.debug(countryWiseTableQuery);
		
		TwogOperations twogOperations=new TwogOperations();
		String nodeWiseTable=twogOperations.converToHtmlForNodeWiseData(nodeWiseTableQuery);
		String countryWiseTable=twogOperations.converToHtmlForCountryWiseData(countryWiseTableQuery);
		JSONObject detailedData=new JSONObject();
		
		try
		{
			detailedData.put("nodeWiseDetailedDataTable",nodeWiseTable);
			detailedData.put("countryWiseDetailedDataTable",countryWiseTable);
		}
		catch(Exception e)
		{
		
		}
		fileLogger.info("Exit Function :getDetailedData" );
		return detailedData;		
	}
	
	
	public JSONArray getOprLogsData(String operationId)
	{	
		fileLogger.info("Inside Function :getOprLogsData" );
		String scannedOprQueryColumns=getReportColumnValues("scannedoprquerycolumns");
		String query = "select "+scannedOprQueryColumns+" from oprlogs where oprid="+operationId+" and oprlogs.mcc != 'NA' and oprlogs.mnc != 'NA'  order by to_timestamp(tstmp,'DDMMYY-HH24-MI-SS')::timestamp without time zone desc";
		fileLogger.debug(query);
		fileLogger.info("Exit Function :getOprLogsData" );
		return new Operations().getJson(query);
		
	}

/*	public JSONArray getOprReportData(String oprId,String startDate,String endDate)
	{	
		String cdrQueryColumns=getReportColumnValues("cdrquerycolumns");
		String whereClause="";
		if(startDate!=null){
			whereClause="inserttime between '"+startDate+"' and '"+endDate+"'";
		}else{
			whereClause="oprid="+oprId+"";
		}
		String query = "select date_trunc('second',logtime + '05:30:00'::interval) log_date,log_type,log_data from log_audit la left join audit_log_type alt on(la.log_type_id = alt.log_code) where "+whereClause+" and log_type_id in (5) order by logtime asc";	
		return new Operations().getJson(query);
	}*/
	
	public JSONArray getOprReportData(String oprId,String startDate,String endDate)
	{	
		String cdrQueryColumns=getReportColumnValues("cdrquerycolumns");
		String whereClause="";
		if(startDate!=null){
			whereClause="inserttime between '"+startDate+"' and '"+endDate+"'";
		}else{
			whereClause="oprid="+oprId+"";
		}
		//String query = "select "+cdrQueryColumns+" from cdrlogs left join antenna on cdrlogs.ant_id=antenna.id where "+whereClause+" and imsi!='null' order by inserttime asc";	
		//String query = "select "+cdrQueryColumns+" from cdrgps_view where "+whereClause+" and imsi!='null' order by inserttime asc";	
		String query = "select "+cdrQueryColumns+" from cdrlogs left join antenna on cdrlogs.ant_id=antenna.id where "+whereClause+" and imsi!='null' order by inserttime asc";
		return new Operations().getJson(query);
	}
	
	public JSONArray getTargetData()
	{	
		String targetQueryColumns=getReportColumnValues("targetquerycolumns");
		String query = "select "+targetQueryColumns+" from target_list  where istarget is true order by name asc";	
		return new Operations().getJson(query);
	}
	
	public JSONArray getInventoryData()
	{	
		String targetQueryColumns=getReportColumnValues("inventoryquerycolumns");
		String query = "select "+targetQueryColumns+" from view_btsinfo where ip not in('0.0.0.0','1.1.1.1') order by show_name asc";	
		return new Operations().getJson(query);
	}
	
	public JSONArray getBMSData(String startDate,String endDate)
	{	
		String whereClause;
		whereClause="logtime between '"+startDate+"' and '"+endDate+"'";
		String query = "select * from view_bmsstatus where "+ whereClause +" order by logtime asc";	
		return new Operations().getJson(query);
	}
	
	public JSONArray getAuditLog(String oprId,String startDate,String endDate)
	{
		//String auditLogsQueryColumns=getReportColumnValues("auditlogsquerycolumns");
		String whereClause="";
		if(startDate!=null){
			whereClause="logtime between '"+startDate+"' and '"+endDate+"'";
		}else{
			whereClause="opr_id="+oprId+"";
		}
		String query = "select date_trunc('second',logtime + '05:30:00'::interval) log_date,log_type,log_data from log_audit la left join audit_log_type alt on(la.log_type_id = alt.log_code) where "+whereClause+" and log_type_id not in (5) order by logtime asc";	
		return new Operations().getJson(query);
	}
	
	public JSONArray getCellReportData(String oprId,String startDate,String endDate)
	{
		//String auditLogsQueryColumns=getReportColumnValues("auditlogsquerycolumns");
		String whereClause="";
		if(startDate!=null){
			try {
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
			    Date date1 = format.parse(startDate);
			    Calendar cal = Calendar.getInstance();
			    cal.setTime(date1);
			    cal.add(Calendar.DATE, -1);
			    Date date2=cal.getTime();
			    System.out.println(date2);
		       String pattern = "yyyy-MM-dd HH:mm:ss";
		       DateFormat df = new SimpleDateFormat(pattern);
		       startDate = df.format(date2);
				
				
		       
		       
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				fileLogger.error("Unable to parse the date startdate given"+ e.getMessage()); 
			}  
			whereClause="logtime between '"+startDate+"' and '"+endDate+"'";
			
		}else{
			whereClause="opr_id="+oprId+"";
		}
		String query = "select date_trunc('second',logtime + '05:30:00'::interval) log_date,log_type,log_data from log_audit la left join audit_log_type alt on(la.log_type_id = alt.log_code) where "+whereClause+" and log_type_id=6 order by logtime asc";	
		return new Operations().getJson(query);
	}
	
	
	
	public JSONArray getStatusLog(String operationId)
	{
		fileLogger.info("Inside Function :getStatusLog" );
		String statusLogsQueryColumns=getReportColumnValues("statuslogsquerycolumns");
		String query = "select "+statusLogsQueryColumns+" from status_log where operation_id="+operationId+";";
		fileLogger.debug(query);
		fileLogger.info("Exit Function :getStatusLog" );
		return new Operations().getJson(query);			
	}
	
	
	public JSONArray getLogAuditReport(String startTime,String endTime) 
	{
	
		String query = "select alt.log_type,la.logtime,log_data from log_audit la left join audit_log_type alt on(la.log_type_id = alt.id) where logtime between '"+startTime+"' and '"+endTime+"' and log_type_id not in (5) and status = 'true' order by logtime asc";	
		return new Operations().getJson(query);
	}
	
	public JSONArray getEventAuditReport(String startTime,String endTime) 
	{
	
		String query = "select * from cdrlogs where inserttime between '"+startTime+"' and '"+endTime+"' order by inserttime asc";	
		return new Operations().getJson(query);
	}
	
	
	public void writeToFile(String header,JSONArray data ,String reportColumns,String childDirectoryPath,String separator) 
	{
		fileLogger.info("Inside Function :writeToFile" );
		File f = new File(childDirectoryPath+"/"+header);
		ArrayList<String> columnHead = new ArrayList<String>(Arrays.asList(reportColumns.split(",")));

		fileLogger.debug("column are"+columnHead.toString());
		PrintWriter writer = null;
		//if(columnHead.size()>=1) 
		//{

			try {
				
				writer = new PrintWriter(f, "UTF-8");
				if(!f.exists()) 
				{
					try {
						f.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				writer.println(header);
				
				//creating columns head
				StringBuilder colsHead = new StringBuilder();
				for(int i=0;i<columnHead.size();i++) 
				{
					if(i==0) 
					{
						colsHead.append(columnHead.get(i));
					}
					else 
					{
						colsHead.append(separator);
						colsHead.append(columnHead.get(i));
					}
				}
				fileLogger.debug("colsHead is :"+colsHead);
				
				writer.println(colsHead.toString());
				
				
				for(int j=0;j<data.length();j++) 
				{
					StringBuilder bb = new StringBuilder();
					for(int i=0;i<columnHead.size();i++) 
					{
						if(i==0) 
						{
							bb.append(data.getJSONObject(j).get(columnHead.get(i)));
						}
						else 
						{
							bb.append(separator);
							bb.append(data.getJSONObject(j).get(columnHead.get(i)));
						}
					}
					writer.println(bb.toString());
				}
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			finally 
			{
				writer.close();
				
			}
			fileLogger.info("Exit Function :writeToFile" );
		//}	
	}
	
	public  String createReport(String startTime,String endTime,String seprator) 
	{
		SimpleDateFormat formater = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		formater.setTimeZone(TimeZone.getTimeZone("IST"));
		String reportFolderName = "FALCON_C2_"+formater.format(new Date());
		
		String childDirectoryPath = createChildReportDirectory(reportFolderName);
		
		JSONArray auditLogData = getLogAuditReport(startTime, endTime);
		//createAuditLogReport(auditLogData,childDirectoryPath,"Audit Log",seprator);
		
		
		JSONArray eventLogData = getEventAuditReport(startTime, endTime);
		//createAuditLogReport(eventLogData,childDirectoryPath,"Event Log",seprator);
		
		
		
		try {
			zipFolder(childDirectoryPath,childDirectoryPath+".zip");
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "{\"result\":\"fail\",\"msg\":\"Exception\"}";
		}
		return childDirectoryPath+".zip";
		
	}
	
	public String createReports(String oprId,String startTime,String endTime,String separator,String lastRelativeDir,String reportType) 
	{
		String reportFolderName=null;
		if(reportType.equalsIgnoreCase("active")){
			SimpleDateFormat formater = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
			formater.setTimeZone(TimeZone.getTimeZone("IST"));
			reportFolderName = "FALCON_C2_"+formater.format(new Date());
		}else{
			SimpleDateFormat formater = new SimpleDateFormat("yyyy_MM_dd");
			formater.setTimeZone(TimeZone.getTimeZone("IST"));
			//String currDate=formater.format(new Date()).split(":")[0];
			reportFolderName = "FALCON_C2_"+formater.format(new Date())+"_02_00_00";
		}
		String childDirectoryPath = createChildReportDirectory(lastRelativeDir,reportFolderName);
		
		String auditLogsDisplayColumns=getReportColumnValues("auditlogsdisplaycolumns");	
		//String auditLogsDisplayColumns="Node Type,Event Type,Detail (TSV)";
		JSONArray auditLog = getAuditLog(oprId,startTime,endTime);
		//writeToFile("Audit Log Report",auditLog,auditLogsDisplayColumns,childDirectoryPath,separator);
		createAuditLogReport(auditLog,auditLogsDisplayColumns,childDirectoryPath,"Audit_Log_Report",separator);
		
		
		//String auditLogsDisplayColumns="Node Type,Event Type,Detail (TSV)";
		String bmsColumns=getReportColumnValues("bmsColumns");
		JSONArray bmsLog = getBMSData( startTime,endTime);
		//writeToFile("Audit Log Report",auditLog,auditLogsDisplayColumns,childDirectoryPath,separator);
		if(bmsLog.length() != 0){
			
			writeToFile("BMSStatusReport",bmsLog,bmsColumns,childDirectoryPath,separator);
		}
		
		String cdrColumns=getReportColumnValues("cdrdisplaycolumns");
		JSONArray cdrData = getOprReportData(oprId,startTime,endTime);
		//writeToFile("Event Log Report",cdrData,cdrColumns,childDirectoryPath,separator);
		createEventLogReport(cdrData,cdrColumns,childDirectoryPath,"Event_Log_Report",separator);
		
		String cellColumns=getReportColumnValues("celldisplaycolumns");
		
		JSONArray cellData = getCellReportData(oprId,startTime,endTime);
		//writeToFile("Event Log Report",cdrData,cdrColumns,childDirectoryPath,separator);
		createCellLogReport(cellData,cellColumns,childDirectoryPath,"Cell_Infrastructure_Report",separator);
		
		String targetColumns=getReportColumnValues("targetdisplaycolumns");
		JSONArray targetData = getTargetData();
		writeToFile("Target_List",targetData,targetColumns,childDirectoryPath,separator);
		
		String inventoryColumns=getReportColumnValues("inventorydisplaycolumns");
		JSONArray inventoryData = getInventoryData();
		writeToFile("Inventory",inventoryData,inventoryColumns,childDirectoryPath,separator);
		
		try {
			zipFolder(childDirectoryPath,childDirectoryPath+".zip");
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "{\"result\":\"fail\",\"msg\":\"Exception\"}";
		}
		return "{\"result\":\"success\",\"msg\":\""+reportFolderName+".zip\"}";
	}
	
	public String createSystemLogs() 
	{
		fileLogger.info("Inside Function :createSystemLogs" );
		SimpleDateFormat formater = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		formater.setTimeZone(TimeZone.getTimeZone("IST"));
		String reportFolderName = "FALCON_C2_system_logs_"+formater.format(new Date());
		String childDirectoryPath = createChildReportDirectoryForSystemLogs(reportFolderName);
		
		//Compress compress = null;
		try {
			File sysLogDir=new File(childDirectoryPath);
			File fileOsLogsDir=new File(childDirectoryPath+"/log");
			if(!fileOsLogsDir.exists())
			{
				fileOsLogsDir.mkdir();
			}
			
			HashMap<String,String> syslogFiles = getSysLogFiles();
			FileUtils.copyDirectory(new File(syslogFiles.get("syslogs")),fileOsLogsDir);
			FileUtils.copyFileToDirectory(new File(syslogFiles.get("restlogs")), sysLogDir);
			FileUtils.copyFileToDirectory(new File(syslogFiles.get("statuslogs")), sysLogDir);
			
			fileLogger.debug("@systemlogs root directory path is :"+childDirectoryPath);
			zipFolder(childDirectoryPath,childDirectoryPath+".zip");
		    //String sysLogZippedFile = childDirectoryPath+".tar.gz";
		    
		    
			//compress=new Compress(sysLogZippedFile, "System_logs");
			//childDirectoryPath=childDirectoryPath.substring(1);
			//compress.writedir(Paths.get(childDirectoryPath));
			try {
				FileUtils.deleteDirectory(new File(childDirectoryPath));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "{\"result\":\"fail\",\"msg\":\"Exception\"}";
		}/*finally{
			try {
				compress.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/fileLogger.info("Exit Function :createSystemLogs" );
		return "{\"result\":\"success\",\"msg\":\""+reportFolderName+".zip\"}";
	}
	
	public void createAuditLogReport(JSONArray data,String columns,String filePath,String fileName,String seprator) 
	{
		File f = new File(filePath+"/"+fileName);
		PrintWriter writer = null;
			try {
				
				writer = new PrintWriter(f, "UTF-8");
				if(!f.exists()) 
				{
					try {
						f.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				StringBuilder bb = new StringBuilder();
				String columnArr[] = columns.split(",");
				int columnLength=columnArr.length;
				for(int columnCount=0;columnCount<columnLength;columnCount++){
					bb.append(columnArr[columnCount]);
					bb.append(seprator);
				}
				bb.append("\n");
				
				try {
					for (int j = 0; j < data.length(); j++) {
						
						if (j != 0) {
							bb.append("\n");
						}

						bb.append(data.getJSONObject(j).get("log_date"));
						bb.append(seprator);
						bb.append("FALCON");
						bb.append(seprator);
						bb.append(data.getJSONObject(j).get("log_type"));
						

						JSONObject jj = new JSONObject(data.getJSONObject(j).getString("log_data"));

						Iterator<String> itr = jj.keys();
						while (itr.hasNext()) {
							String key = itr.next();
							bb.append(seprator);
							if (jj.isNull(key))
								bb.append(key+": ");
							else
								bb.append(key+":"+jj.getString(key));
								
						}
					}
					writer.println(bb.toString());
				} catch (Exception e) {
					System.out.println(e.getMessage());
					// TODO: handle exception
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 	
			finally 
			{
				writer.close();
				
			}
	}
	
	
	public void createEventLogReport(JSONArray data,String columns,String filePath,String fileName,String seprator) 
	{
		File f = new File(filePath+"/"+fileName);
		PrintWriter writer = null;
			try {
				
				writer = new PrintWriter(f, "UTF-8");
				if(!f.exists()) 
				{
					try {
						f.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				StringBuilder bb = new StringBuilder();
				String columnArr[] = columns.split(",");
				int columnLength=columnArr.length;
				for(int columnCount=0;columnCount<columnLength;columnCount++){
					bb.append(columnArr[columnCount]);
					bb.append(seprator);
				}
				bb.append("\n");
/*				
				bb.append("");
				bb.append(seprator);
				bb.append("");
				bb.append(seprator);
				bb.append("");
				bb.append(seprator);
				bb.append("");
				bb.append(seprator);
				bb.append("Tech");
				bb.append(seprator);
				bb.append("Band");
				bb.append(seprator);				
				bb.append("Target");
				bb.append(seprator);
				bb.append("IMSI");
				bb.append(seprator);
				bb.append("IMEI");
				bb.append(seprator);
				bb.append("UL Freq");
				bb.append(seprator);
				bb.append("Operator");
				bb.append(seprator);
				bb.append("C Operator");
				bb.append(seprator);
				bb.append("CGI");
				bb.append(seprator);
				bb.append("RxL");
				bb.append(seprator);
				bb.append("TA");
				bb.append(seprator);
				bb.append("Distance(m)");
				bb.append(seprator);
				bb.append("Based On");
				bb.append(seprator);
				bb.append("Location");
				bb.append(seprator);
				bb.append("Antenna");
				bb.append("\n");*/
				
				
				try {
					for (int j = 0; j < data.length(); j++) {
						
						if (j != 0) {
							bb.append("\n");
						}
                        JSONObject tempObject = data.getJSONObject(j);
						bb.append(tempObject.get("Date"));
						bb.append(" ");
						bb.append(tempObject.get("Time"));
						bb.append(seprator);
						bb.append("FALCON");
						bb.append(seprator);
						bb.append("MEAS");
						bb.append(seprator);
						bb.append(tempObject.getString("stype"));
						bb.append(seprator);
						bb.append(tempObject.getString("band"));
						bb.append(seprator);
						bb.append(tempObject.getString("Operator"));
						bb.append(seprator);
						bb.append(tempObject.getString("dlarfcn"));
						bb.append(seprator);
						bb.append(tempObject.getString("ulrfcn"));
						bb.append(seprator);
						bb.append(tempObject.getString("freq"));
						bb.append(seprator);
						bb.append(tempObject.getString("rxl"));
						bb.append(seprator);

						//Location
						if((tempObject.getString("msloc")!=null && (tempObject.getString("msloc").toLowerCase().contains("na")))||(!(tempObject.getString("calc_basis").equalsIgnoreCase("GPS")))){
							bb.append("NR");
						}else{
							bb.append(tempObject.getString("msloc"));	
						}
						bb.append(seprator);
						
						//Self Location
						if(tempObject.getString("self_loc")==null){
							bb.append("");
						}else{
							bb.append(tempObject.getString("self_loc"));	
						}
						bb.append(seprator);
						
						
						//Antenna
						bb.append(tempObject.getString("profile_name"));
						bb.append(seprator);
						
						//North Offset
						bb.append(tempObject.getInt("off_angle"));
						bb.append(seprator);
						

						//C Operator
						bb.append(tempObject.getString("c_opr"));
						bb.append(seprator);
						
						
						//CGI
						bb.append(tempObject.getString("cgi"));
						bb.append(seprator);
						
						//IMSI
						bb.append(tempObject.getString("imsi"));
						bb.append(seprator);
						
						//IMEI
						bb.append(tempObject.getString("imei"));
						bb.append(seprator);
						

						//TA
						bb.append(tempObject.getString("ta"));
						bb.append(seprator);
							
						//Distance(m)
						bb.append(tempObject.getString("distance"));
						bb.append(seprator);
						
						//Based On
						bb.append(tempObject.getString("calc_basis"));
						bb.append(seprator);									
						
						//Trigger
						String tmp =tempObject.getString("show_cue_st");
						if((tmp.equalsIgnoreCase("t")) || (tmp.equalsIgnoreCase("true"))){
						//						if(tempObject.getBoolean("show_cue_st")){
							bb.append(tempObject.getString("trigger_source")+"("+tempObject.getString("cue_id")+")");
						}else{
							bb.append(tempObject.getString("trigger_source"));
						}
						bb.append(seprator);
						
						//Target
						bb.append(tempObject.getString("traget_type"));
						bb.append(seprator);
						
						
						//Gps_error
						bb.append(tempObject.getString("gps_cause"));
						bb.append(seprator);
						
					
						
								
					}
					writer.println(bb.toString());
				} catch (Exception e) {
					System.out.println(e.getMessage());
					// TODO: handle exception
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 	
			finally 
			{
				writer.close();
				
			}
	}
	
	
	public void createCellLogReport(JSONArray data,String columns,String filePath,String fileName,String seprator) 
	{
		File f = new File(filePath+"/"+fileName);
		PrintWriter writer = null;
			try {
				
				writer = new PrintWriter(f, "UTF-8");
				if(!f.exists()) 
				{
					try {
						f.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				StringBuilder bb = new StringBuilder();
				String columnArr[] = columns.split(",");
				int columnLength=columnArr.length;
				for(int columnCount=0;columnCount<columnLength;columnCount++){
					bb.append(columnArr[columnCount]);
					bb.append(seprator);
				}
				bb.append("\n");
				
/*				bb.append("");
				bb.append(seprator);
				bb.append("");
				bb.append(seprator);
				bb.append("");
				bb.append(seprator);
				bb.append("");
				bb.append(seprator);
				bb.append("Tech");
				bb.append(seprator);
				bb.append("Band");
				bb.append(seprator);				
				bb.append("Target");
				bb.append(seprator);
				bb.append("IMSI");
				bb.append(seprator);
				bb.append("IMEI");
				bb.append(seprator);
				bb.append("UL Freq");
				bb.append(seprator);
				bb.append("Operator");
				bb.append(seprator);
				bb.append("C Operator");
				bb.append(seprator);
				bb.append("CGI");
				bb.append(seprator);
				bb.append("RxL");
				bb.append(seprator);
				bb.append("TA");
				bb.append(seprator);
				bb.append("Distance(m)");
				bb.append(seprator);
				bb.append("Based On");
				bb.append(seprator);
				bb.append("Location");
				bb.append(seprator);
				bb.append("Antenna");
				bb.append("\n");*/
				
				
				try {
					for (int j = 0; j < data.length(); j++) {
						
						if (j != 0) {
							bb.append("\n");
						}

						bb.append(data.getJSONObject(j).get("log_date"));
						bb.append(seprator);
						bb.append("FALCON");
						bb.append(seprator);
						bb.append("CELL SCAN");
						bb.append(seprator);
						
                        JSONObject tempObject = new JSONObject(data.getJSONObject(j).getString("log_data"));
                        
                        bb.append(tempObject.getString("TECH"));
                        bb.append(seprator);
                        
                        //Band
						bb.append(tempObject.getString("name"));
						bb.append(seprator);
						
						
						bb.append(tempObject.getString("Operator_Name"));
						bb.append(seprator);
						
						bb.append(tempObject.getString("ARFCN")+tempObject.getString("EARFCN")+tempObject.getString("UARFCN"));
						bb.append(seprator);
						//bb.append(tempObject.getString("UARFCN"));
						//bb.append(seprator);
						bb.append(tempObject.getString("DL_Absolute_Frequency"));
						bb.append(seprator);
						
						//RxL
						bb.append(tempObject.getString("Receive_Level_dBm"));
						bb.append(seprator);
						
						//Location
						bb.append(tempObject.getString("Scanned_Cell_Coordinate"));
						bb.append(seprator);
						
						
						if(tempObject.getString("SELF_LOCATION")==null){
							bb.append("");
						}else{
							bb.append(tempObject.getString("SELF_LOCATION"));	
						}
						bb.append(seprator);
						
						//Antenna
						bb.append(tempObject.getString("ANTENNA_NAME"));
						bb.append(seprator);
						
						//S1 North Offset(in deg)
						bb.append(tempObject.getString("ANGLE_OFFSET"));
						bb.append(seprator);
												
						bb.append(tempObject.getString("MCC"));
						bb.append(seprator);
						bb.append(tempObject.getString("MNC"));
						bb.append(seprator);
						bb.append(tempObject.getString("LAC"));
						bb.append(seprator);
						bb.append(tempObject.getString("CELL"));
						bb.append(seprator);
						String bsic="";
						if (tempObject.getString("TECH")=="2g") {
						bsic =Integer.toString(tempObject.getInt("bcc"))+(tempObject.getInt("ncc")*8);
						}
						bb.append(tempObject.getString("PSC")+tempObject.getString("PCI")+bsic);
						bb.append(seprator);
						
						//bb.append(tempObject.getString("BCC"));
						//bb.append(seprator);
						//bb.append(tempObject.getString("NCC"));
						//bb.append(seprator);
						
						bb.append(tempObject.getString("Country_Name"));
						bb.append(seprator);
						

						//bb.append(tempObject.getString("PCI"));
						//bb.append(seprator);
						try {
							bb.append(tempObject.getString("bandwidth"));
							bb.append(seprator);
						}
						catch(Exception ex) {
							bb.append("");
							bb.append(seprator);
							
						}
						try {
							bb.append(tempObject.getString("NEIGHBOURS"));
							bb.append(seprator);
						}
						catch(Exception ex) {
							bb.append("");
							bb.append(seprator);
							
						}
						
/*						if(tempObject.getString("msloc")!=null && (tempObject.getString("msloc").toLowerCase().contains("na"))){
							bb.append("NR");
						}else{
							bb.append(tempObject.getString("msloc"));	
						}
						bb.append(seprator);*/
						
					
						
					}
					writer.println(bb.toString());
				} catch (Exception e) {
					System.out.println(e.getMessage());
					// TODO: handle exception
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 	
			finally 
			{
				writer.close();
				
			}
	}
	
	
	
	
	
	public ArrayList<String> getColumns(JSONArray data)
	{
		ArrayList la = new ArrayList<String>();
		if(data.length()>=1) 
		{
			try {
				 Iterator itr = data.getJSONObject(0).keys();
				 
				 while(itr.hasNext())
				 {
					 la.add(itr.next());
				 }
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		return la;
	}
	
	
	public String createChildReportDirectory(String reportFolderName) 
	{
		fileLogger.info("Inside Function :createChildReportDirectory" );
		String path=null;
		String absolutePath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		absolutePath = absolutePath.substring(0,absolutePath.lastIndexOf("WEB-INF"));
		fileLogger.debug("@vishal"+absolutePath);
		path = absolutePath+ "resources/reports/";
		fileLogger.debug("@vishal"+path);
		String childDirectoryPath = path + reportFolderName;
		fileLogger.debug("@vishal"+childDirectoryPath);
		new Operations().checkOrCreateParentChildDirectory(path,reportFolderName);
		fileLogger.info("Exit Function :createChildReportDirectory" );
		return childDirectoryPath;
	}
	
	public String createChildReportDirectory(String lastRelativeDir,String reportFolderName) 
	{
		fileLogger.info("Inside Function :createChildReportDirectory" );
		String path=null;
		String absolutePath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		absolutePath = absolutePath.substring(0,absolutePath.lastIndexOf("WEB-INF"));
		fileLogger.debug("@vishal"+absolutePath);
		path = absolutePath+ "resources/"+lastRelativeDir+"/";
		fileLogger.debug("@vishal"+path);
		String childDirectoryPath = path + reportFolderName;
		fileLogger.debug("@vishal"+childDirectoryPath);
		new Operations().checkOrCreateParentChildDirectory(path,reportFolderName);
		fileLogger.info("Exit Function :createChildReportDirectory" );

		return childDirectoryPath;
	}
	
	public String createChildReportDirectoryForSystemLogs(String reportFolderName) 
	{
		fileLogger.info("Inside Function :createChildReportDirectoryForSystemLogs" );

		String path=null;
		String absolutePath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		absolutePath = absolutePath.substring(0,absolutePath.lastIndexOf("WEB-INF"));
		fileLogger.debug("@vishal"+absolutePath);
		path = absolutePath+ "resources/syslogs/";
		fileLogger.debug("@vishal"+path);
		String childDirectoryPath = path + reportFolderName;
		fileLogger.debug("@vishal"+childDirectoryPath);
		new Operations().checkOrCreateParentChildDirectory(path,reportFolderName);
		fileLogger.info("Exit Function :createChildReportDirectoryForSystemLogs" );
		return childDirectoryPath;
	}
	
	public String createChildReportDirectoryForFullDbBackup(String reportFolderName) 
	{
		fileLogger.info("Inside Function :createChildReportDirectoryForFullDbBackup" );
		String path=null;
		String absolutePath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		absolutePath = absolutePath.substring(0,absolutePath.lastIndexOf("WEB-INF"));
		fileLogger.debug("@vishal"+absolutePath);
		path = absolutePath+ "resources/Backup_Reports/";
		fileLogger.debug("@vishal"+path);
		String childDirectoryPath = path + reportFolderName;
		fileLogger.debug("@vishal"+childDirectoryPath);
		new Operations().checkOrCreateParentChildDirectory(path,reportFolderName);
		fileLogger.info("	Exit Function :createChildReportDirectoryForFullDbBackup" );
		return childDirectoryPath;
	}
	
	
/*	public String createReports(String oprId) 
	{
		fileLogger.debug("@vishal"+oprId);
		String reportFolderName = System.currentTimeMillis()+"";
		String childDirectoryPath = createChildReportDirectory(reportFolderName);
		
		String cdrColumns=getReportColumnValues("cdrdisplaycolumns");
		JSONArray cdrData = getOprReportData(oprId);
		writeToFile("CDR Detailed Report",cdrData,cdrColumns,childDirectoryPath);
		
		String scannedOprDisplayColumns=getReportColumnValues("scannedoprdisplaycolumns");
		JSONArray oprData = getOprLogsData(oprId);
		writeToFile("Operators Detailed Report",oprData,scannedOprDisplayColumns,childDirectoryPath);
		
		String auditLogsDisplayColumns=getReportColumnValues("auditlogsdisplaycolumns");		
		JSONArray auditLog = getAuditLog(oprId);
		writeToFile("Audit Log Report",auditLog,auditLogsDisplayColumns,childDirectoryPath);
		
		String statusLogsDisplayColumns=getReportColumnValues("statuslogsdisplaycolumns");
		JSONArray statusLog = getStatusLog(oprId);
		writeToFile("Device Status Log Report",statusLog,statusLogsDisplayColumns,childDirectoryPath);
		
		try {
			zipFolder(childDirectoryPath,childDirectoryPath+".zip");
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "{\"result\":\"fail\",\"msg\":\"Exception\"}";
		}
		return "{\"result\":\"success\",\"msg\":\""+reportFolderName+".zip\"}";
	}*/
	
	static public void zipFolder(String srcFolder, String destZipFile) throws Exception {
	    ZipOutputStream zip = null;
	    FileOutputStream fileWriter = null;

	    fileWriter = new FileOutputStream(destZipFile);
	    zip = new ZipOutputStream(fileWriter);

	    addFolderToZip("", srcFolder, zip);
	    zip.flush();
	    zip.close();
	  }

	  static private void addFileToZip(String path, String srcFile, ZipOutputStream zip)
	      throws Exception {

	    File folder = new File(srcFile);
	    if (folder.isDirectory()) {
	      addFolderToZip(path, srcFile, zip);
	    } else {
	      byte[] buf = new byte[1024];
	      int len;
	      FileInputStream in = new FileInputStream(srcFile);
	      zip.putNextEntry(new ZipEntry(path + "/" + folder.getName()));
	      while ((len = in.read(buf)) > 0) {
	        zip.write(buf, 0, len);
	      }
	    }
	  }

	  static private void addFolderToZip(String path, String srcFolder, ZipOutputStream zip)
	      throws Exception {
	    File folder = new File(srcFolder);

	    for (String fileName : folder.list()) {
	      if (path.equals("")) {
	        addFileToZip(folder.getName(), srcFolder + "/" + fileName, zip);
	      } else {
	        addFileToZip(path + "/" + folder.getName(), srcFolder + "/" + fileName, zip);
	      }
	    }
	  }
	  
		public String getReportColumnValues(String ReportColumnKey)
		{
			fileLogger.info("Inside Function :getReportColumnValues" );
			String absolutePath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
			absolutePath = absolutePath.substring(0,absolutePath.lastIndexOf("WEB-INF"));
			Properties prop = new Properties();
			try
			{
				prop.load(new FileInputStream(absolutePath	+ "/resources/config/report_columns.properties"));
			}
			catch(Exception E)
			{
				fileLogger.debug("report_columns.properties not found" + E.getMessage());
				
			}		
			fileLogger.info("Exit Function :getReportColumnValues" );
			return prop.getProperty(ReportColumnKey);
		}
		
		public HashMap<String,String> getSysLogFiles()
		{
			fileLogger.info("Inside Function :getSysLogFiles" );
			String absolutePath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
			absolutePath = absolutePath.substring(0,absolutePath.lastIndexOf("WEB-INF"));
			Properties prop = new Properties();
			try
			{
				prop.load(new FileInputStream(absolutePath	+ "/resources/config/db.properties"));
			}
			catch(Exception E)
			{
				fileLogger.debug("db .properties not found" + E.getMessage());
				
			}		
			
			//fileLogger.debug(new FileInputStream(absolutePath	+ "/resources/event.properties"));
			HashMap<String,String> hm = new HashMap<String,String>(); 
			hm.put("syslogs",prop.getProperty("syslogs"));
			hm.put("restlogs",prop.getProperty("restlogs"));
			hm.put("statuslogs",prop.getProperty("statuslogs"));  
			fileLogger.info("Exit Function :getSysLogFiles" );
	        return hm;
		}
		
}
