package in.vnl.msgapp;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.Calendar;
import java.util.Timer;
import java.text.DecimalFormat;

import javax.json.JsonObject;
import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.*;

import java.net.URI;
import java.net.URISyntaxException;

import com.oreilly.servlet.*;
import com.oreilly.servlet.multipart.FileRenamePolicy;

/**
 * Servlet implementation class Operations
 */



import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.Socket;
import javassist.compiler.Parser;

import org.apache.log4j.Logger;
import in.vnl.EventProcess.DBDataService;
import in.vnl.api.common.ApiCommon;
import in.vnl.api.common.AuditHandler;
import in.vnl.api.common.CMSController;
import in.vnl.api.common.CommonService;
import in.vnl.api.common.Constants;
import in.vnl.api.common.CurrentOperationType;
import in.vnl.api.common.OperationCalculations;
import in.vnl.api.common.TRGLController;
import in.vnl.api.common.livescreens.ConfigOprServer;
import in.vnl.api.netscan.NetscanOperations;
import in.vnl.api.threeg.ThreegOperations;
import in.vnl.msgapp.Common;
import in.vnl.report.ReportServer;
import in.vnl.api.twog.TwogOperations;
import in.vnl.api.twog.livescreens.SMSIntercept;
import in.vnl.api.twog.livescreens.TrackedImsiServer;
import in.vnl.api.twog.livescreens.TriggerCueServer;
import in.vnl.api.twog.livescreens.opretorDataServer;
import in.vnl.bist.Bist;
import in.vnl.api.config.PossibleConfigurations;

//@WebServlet("/Operations")
public class Operations extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static Bist bist = null;
	static Logger fileLogger = Logger.getLogger("file");
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Operations() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
		String methodName = request.getParameter("methodName");
		
		switch(methodName)
		{
		case "getOprReportData":
			out.print(getOprReportData(request));				
			break;

		case "getCdrReports":
			out.print(getCdrReports(request));				
			break;
			
		case "getMobileType":
			out.print(getMobileType(request));				
			break;
		case "getMobileTypeAll":
			out.print(getMobileTypeAll(request));				
			break;	
		case "saveCdrOperations":
			out.print(saveCdrOperations(request));				
			break;
		case "saveBcdrOperations":
			out.print(saveBcdrOperations(request));				
			break;
		case "getLocBTStype":
			out.print(getLocBTStype(request));				
			break;
		case "getLocOprations":
			out.print(getLocOprations(request));				
			break;
		case "locate":
			out.print(locate(request));				
			break;
		case "locateWithNeighbour":
			out.print(locateWithNeighbour(request));				
			break;				
		case "triggerSms":
			out.print(triggerSms(request));
			break;
		case "getLatLon":
				out.print(getLatLon(request));
			break;
		case "getCellsData":
				out.print(getCellsData(request));
			break;
		case "getMapData":
				out.print(getMapData(request));
			break;
		case "InsertSearchData":
				out.print(InsertSearchData(request));
			break;
		case "updateNib":
				out.print(updateNib(request));
			break;
		case "getNibIp":
				out.print(getNibIp(request));
			break;	
		case "getMapServerIp":
				out.print(getMapServerIp(request));
			break;
		case "getScanedData":
				out.print(getScanedData(request));
			break;
		case "getDistinctIMSI":
				out.print(getDistinctIMSI(request));
			break;
		case "getScanedDataCenter":
				out.print(getScanedDataCenter(request));
			break;
		case "triggerPage":
				out.print(triggerPage(request));
			break;
		case "livepoll":
				out.print(livepoll(request));
			break;
		case "getListForImsiImei":
				out.print(getListForImsiImei(request));
			break;
		case "serverCall":
				out.print(serverCall(request));
			break;
		case "getAllNibs":
				out.print(getAllNibs());
			break;
		case "getRunningMode":
				out.print(getRunningMode(request));
			break;
		case "setStatus":
				out.print(setStatus(request));
			break;
		case "getSpectrumData":
				out.print(getSpectrumData(request));
			break;
		case "getSpectrumDataAngle":
				out.print(getSpectrumDataAngle(request));
			break;	
		case "getGPSData":
				out.print(getGPSData(request));
			break;
		case "getCDRdata":
				out.print(getCDRdata(request));
			break;
		case "getReq":
				out.print(getReq(request));
			break;
		case "insertGeoLocData":
				out.print(insertGeoLocData(request));
			break;
		case "getGeoLocdata":
				out.print(getGeoLocdata(request));
			break;
		case "cdrData":
				out.print(cdrData(request));
			break;
			case "pollData":
				out.print(pollData(request));
			break;
		case "mtSmsGeoData":
				out.print(mtSmsGeoData(request));
			break;
		case "getGeoLocdataLocal":
				out.print(getGeoLocdataLocal(request));
			break;
		case "spectrumData":
				out.print(spectrumData(request));
			break;
		case "spectrumDataAntena":
				out.print(spectrumDataAntena(request));
			break;
		case "gpsData":
				out.print(gpsData(request));
			break;
		case "getCDRGPSData":
				out.print(getCDRGPSData(request));
			break;
		case "truncateDb":
				out.print(truncateDb(request));
			break;
		case "truncateDbOpr":
				out.print(truncateDbOpr(request));
			break;			
		case "uploadCSV":
				out.print(uploadCSV(request,response));
			break;
		case "getCurrentActiveOperation":
				out.print(getCurrentActiveOperation(request));
			break;
		case "getWidsReportData":
				out.print(getWidsReportData(request));
			break;
		case "oprData":
				out.print(oprData(request));
			break;
		case "getOprLogsData":
				out.print(getOprLogsData(request));
			break;
			
		case "getOprCellsData":
			out.print(getOprCellsData(request));
			break;
		case "getAllBtsInfo":
			    out.print(getAllBtsInfo());
			break;
		case "uploadGpsData":
				out.print(uploadGpsData(request,response));
			break;
		case "scheduleSubscriberSearch":
				out.print(scheduleSubscriberSearch(request));
			break;
		case "getAllScheduledSubscribers":
			    out.print(getAllScheduledSubscribers(request));
			break;
		case "startScheduledTask":
			    out.print(startScheduledTask(request));
			break;
		case "stopScheduledTask":
			    out.print(stopScheduledTask(request));
			break;
		case "changePeriodicityOfScheduledTask":
			    out.print(changePeriodicityOfScheduledTask(request));
			break;
		case "deleteScheduledTask":
			    out.print(deleteScheduledTask(request));
			break;
		case "setSelectedNibIp":
		       out.print(setSelectedNibIp(request));
			break;
		case "getBtsInfoPerApplication":
			   out.print(getBtsInfoPerApplication(request));
			break;
		case "getNodeWiseReport":
			out.print(getNodeWiseReport(request));
			break;
		case "addTarget":
			out.print(addTarget(request));
			break;
		case "getCountryWiseReport":
			out.print(getCountryWiseReport(request));
			break;
		case "getNetworkScanReport":
			out.print(getNetworkScanReport(request));
			break;
		case "getAllTargetImsi":
			out.print(getAllTargetImsi(request));
			break;
		case "deleteTargetImsi":
			out.print(deleteTargetImsi(request));
			break;	
		case "getDetailedData":
			out.print(getDetailedData(request));
			break;
		case "getAllDataTypes":
			out.print(getAllDataTypes());
			break;	
		case "getAllOperations":
			out.print(getAllOperations());
			break;
		case "getAllCountryList":
			out.print(getAllCountryList());
		break;
	}
	}
	
		public JSONArray getAllBtsInfo()
	{
	    fileLogger.info("Inside Function : getAllBtsInfo");		
		Common co = new Common();
		Statement smt = null;
		Connection con = co.getDbConnection();
		JSONArray ja = new JSONArray();
		try
		{
			smt = con.createStatement();			
			String query = "select * from view_btsinfo order by grp_name";
			fileLogger.debug(query);
			ResultSet rs = smt.executeQuery(query);
			
			while(rs.next())
			{
				JSONObject jo = new JSONObject();
				jo.put("b_id", rs.getString("b_id"));
				jo.put("ip", rs.getString("ip"));
				jo.put("statuscode", rs.getString("statuscode"));
				jo.put("cellcode", rs.getString("cellcode"));
				jo.put("typeid", rs.getString("typeid"));
				jo.put("typename", rs.getString("typename"));
				jo.put("status", rs.getString("status"));
				jo.put("cellstatus", rs.getString("cellstatus"));
				jo.put("dcode", rs.getString("dcode"));
				jo.put("dname", rs.getString("dname"));
				jo.put("sytemid", rs.getString("sytemid"));
				jo.put("code", rs.getString("code"));
				jo.put("grp_id", rs.getString("grp_id"));
				jo.put("grp_name", rs.getString("grp_name"));
				jo.put("application_id", rs.getString("application_id"));
				jo.put("application_name", rs.getString("application_name"));
				jo.put("application_status", rs.getString("application_status"));
				ja.put(jo);
			}			
		}
		catch(Exception E)
		{
			fileLogger.error("Exception in getAllBtsInfo :"+E.getMessage());
		}
		finally
		{
			try
			{
				smt.close();
				con.close();
			}
			catch(Exception E)
			{
				
			}
		}	
		  fileLogger.info("Exit Function : getAllBtsInfo");	
		return ja;
}	
	
	public JSONArray getAllGroups()
	{
		  fileLogger.info("Inside Function : getAllGroups");	
		Common co = new Common();
		Statement smt = null;
		Connection con = co.getDbConnection();
		JSONArray ja = new JSONArray();
		try
		{
			smt = con.createStatement();			
			String query = "select * from groups";
			fileLogger.debug(query);
			ResultSet rs = smt.executeQuery(query);
			
			while(rs.next())
			{
				JSONObject jo = new JSONObject();
				jo.put("id", rs.getString("id"));
				jo.put("name", rs.getString("group_name"));
				ja.put(jo);
			}			
		}
		catch(Exception E)
		{
			fileLogger.error("Exception in getAllGroups :"+E.getMessage());
		}
		finally
		{
			try
			{
				smt.close();
				con.close();
			}
			catch(Exception E)
			{
				
			}
		}	
		  fileLogger.info("Exit Function : getAllGroups");	
		return ja;
	}
	
	
	public JSONArray getAllNibs()
	{
		  fileLogger.info("Inside Function : getAllNibs");	
		Common co = new Common();
		JSONArray ja = new JSONArray();
		try
		{
			HashMap<String,String> ll = co.getDbCredential();
			
			String[] nibs = ll.get("allnibs").split(",");
			for(String nib:nibs)
			{
				ja.put(nib);
			}
		}
		catch(Exception E)
		{
			fileLogger.error("Exception in getAllNibs :"+E.getMessage());
		}	
		  fileLogger.info("Exit Function : getAllNibs");	

		return ja;
	}
	
	
	
	public JSONArray getAllNumbers()
	{
		  fileLogger.info("Inside Function : getAllNumbers");	

		Common co = new Common();
		Statement smt = null;
		Connection con = co.getDbConnection();
		JSONArray ja = new JSONArray();
		try
		{
			smt = con.createStatement();			
			String query = "select * from phone_numbers";
			fileLogger.debug(query);
			ResultSet rs = smt.executeQuery(query);
			
			while(rs.next())
			{
				JSONObject jo = new JSONObject();
				jo.put("id", rs.getString("id"));
				jo.put("number", rs.getString("phone_number"));
				ja.put(jo);
			}			
		}
		catch(Exception E)
		{
			fileLogger.error("Exception in getAllNumbers :"+E.getMessage());
		}
		finally
		{
			try
			{
				smt.close();
				con.close();
			}
			catch(Exception E)
			{
				
			}
		}	
		fileLogger.info("Exit Function : getAllNumbers");	
		return ja;
	}
	
	
	public JSONArray getPhoneNumbersForGroups(HttpServletRequest request)
	{
		fileLogger.info("Inside Function : getPhoneNumbersForGroups");	
		Common co = new Common();
		Statement smt = null;
		Connection con = co.getDbConnection();
		JSONArray ja = new JSONArray();
		String grp = request.getParameter("groups");
		try
		{
			smt = con.createStatement();			
			String query = "select * from phone_numbers where id in (select num_id from group_phone_mapping where grp_id in("+grp+"));";
			fileLogger.debug(query);
			ResultSet rs = smt.executeQuery(query);			
			while(rs.next())
			{				
				ja.put(rs.getString("phone_number"));
			}			
		}
		catch(Exception E)
		{
			fileLogger.error("Exception in getPhoneNumbersForGroups :"+E.getMessage());
		}
		finally
		{
			try
			{
				smt.close();
				con.close();
			}
			catch(Exception E)
			{
				
			}
		}	
		fileLogger.info("Exit Function : getPhoneNumbersForGroups");	
		return ja;
	}
	
	
	public JSONArray getPhoneNumbersForGroupsWithName(HttpServletRequest request)
	{
		fileLogger.info("Inside Function : getPhoneNumbersForGroupsWithName");	
		Common co = new Common();
		Statement smt = null;
		Connection con = co.getDbConnection();
		
		JSONArray ja = new JSONArray();
		
		String grp = request.getParameter("groups");
		try
		{
			smt = con.createStatement();			
			String query = "select * from phone_numbers where id in (select num_id from group_phone_mapping where grp_id in("+grp+"));";
			fileLogger.debug(query);
			ResultSet rs = smt.executeQuery(query);			
			while(rs.next())
			{				
				JSONObject jb = new JSONObject();
				
				jb.append("name",rs.getString("name") );
				jb.append("number",rs.getString("phone_number") );
				//ja.put(rs.getString("phone_number"));
				ja.put(jb);
			}			
		}
		catch(Exception E)
		{
			fileLogger.error("Exception while authenticating the user "+E.getMessage());
		}
		finally
		{
			try
			{
				smt.close();
				con.close();
			}
			catch(Exception E)
			{
				
			}
		}	
		fileLogger.info("Exit Function : getPhoneNumbersForGroupsWithName");	
		return ja;
	}
	
	
	/*This function will send the message */
	
	
	
	
	/*This function will send the message */
	
	@SuppressWarnings("deprecation")
	public String locate(HttpServletRequest request)
	{
		fileLogger.info("Inside Function : locate");	
		String imsi = request.getParameter("imsi");
		String tId = request.getParameter("t_id");
		String flag = request.getParameter("flag");		
		Common co = new Common();
		HashMap<String,String> ll = co.getDbCredential();
		//String myURL = "http://"+ll.get("nib")+"/Trace.html?CMD_TYPE=GETALL_SUB_TRACK_REQ&TAGS00="+tId+"&TAGS001="+imsi+"&TAGS02="+flag+"&";
		//String myURL = "http://"+ll.get("nib")+"/cgi-bin/processData.sh";
		String myURL = "http://"+getNibIp(request)+"/cgi-bin/processData_CLI.sh";
		
		String CMD_TYPE="GETALL_SUB_TRACK_REQ";
		
		String TAGS00=tId;
		String TAGS01=imsi;
		String TAGS02=flag;
		
		//params.add(new BasicNameValuePair("TAGS00", tId));
		//params.add(new BasicNameValuePair("TAGS01", imsi));
		//params.add(new BasicNameValuePair("TAGS02", imei));
		//params.add(new BasicNameValuePair("TAGS03", flag));
		//params.add(new BasicNameValuePair("TAGS04", "2"));
		
		//String myURL = "http://10.100.208.172/temp/text.json";
		
		String mdr = "";
		
		try
		{
			if(!flag.equalsIgnoreCase("2") || !flag.equalsIgnoreCase("3"))
			Thread.sleep(10000);
			mdr=co.aa(myURL,CMD_TYPE,TAGS00,TAGS01,TAGS02);			
			//mdr = co.callURL("http://"+getNibIp(request)+"/temp/target.json");
			
						
		}
		catch(Exception E)
		{
			fileLogger.error("Exception while request"+E.getMessage());
		}
		if(mdr.equalsIgnoreCase("ERROR"))
		{
			mdr="{\"STATUS\":\"3\"}";
		}		
		fileLogger.info("Exit Function : locate");	
		return mdr;		
	}

	

	@SuppressWarnings("deprecation")
	public String livepoll(HttpServletRequest request)
	{
		fileLogger.info("Inside Function : livepoll");	
		String imsi = request.getParameter("imsi");
		String imei = request.getParameter("imei");
		String tId = request.getParameter("t_id");
		String flag = request.getParameter("flag");		
		Common co = new Common();
		HashMap<String,String> ll = co.getDbCredential();
		//String myURL = "http://"+ll.get("nib")+"/Trace.html?CMD_TYPE=GETALL_SUB_TRACK_REQ&TAGS00="+tId+"&TAGS001="+imsi+"&TAGS02="+flag+"&";
		//String myURL = "http://"+ll.get("nib")+"/cgi-bin/processData.sh";
		String myURL = "http://"+getNibIp(request)+"/cgi-bin/processData_CLI.sh";
		
		String CMD_TYPE="GETALL_SUB_TRACK_REQ";
		
		String TAGS00=tId;
		String TAGS01=imsi;
		String TAGS02=imei;
		String TAGS03=flag;
		
		//String myURL = "http://10.100.208.172/temp/text.json";
		
		String mdr = "";
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("CMD_TYPE",CMD_TYPE ));
		params.add(new BasicNameValuePair("TAGS00", tId));
		params.add(new BasicNameValuePair("TAGS01", imsi));
		params.add(new BasicNameValuePair("TAGS02", imei));
		params.add(new BasicNameValuePair("TAGS03", flag));
		
		
		try
		{
			//if(!flag.equalsIgnoreCase("2") || !flag.equalsIgnoreCase("3"))
			//Thread.sleep(1000);
			mdr = co.callPostDataUrl(myURL,params);			
			//mdr = co.callURL("http://"+ll.get("nib")+"/temp/target.json");
			Thread.sleep(1000);
			//mdr = co.callURL("http://"+getNibIp(request)+"/temp/target.json");
			
			
			//mdr = co.callURL("http://10.100.200.110/celldata.json");			
		}
		catch(Exception E)
		{
			fileLogger.error("Exception while request"+E.getMessage());
		}
		if(mdr.equalsIgnoreCase("ERROR"))
		{
			mdr="{\"STATUS\":\"3\"}";
		}		
		fileLogger.info("Exit Function : livepoll");	
		return mdr;		
	}	
	
	@SuppressWarnings("deprecation")
	public String locateWithNeighbour(HttpServletRequest request)
	{
		fileLogger.info("Inside Function : locateWithNeighbour");	
		String imsi = request.getParameter("imsi");
		String imei = request.getParameter("imei");
		String tId = request.getParameter("t_id");
		String flag = request.getParameter("flag");
		int length = Integer.parseInt(request.getParameter("lengthOfNeigh"));		
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();		
		
		params.add(new BasicNameValuePair("CMD_TYPE","GETALL_SUB_TRACK_REQ" ));
		params.add(new BasicNameValuePair("TAGS00", tId));
		params.add(new BasicNameValuePair("TAGS01", imsi));
		params.add(new BasicNameValuePair("TAGS02", imei));
		params.add(new BasicNameValuePair("TAGS03", flag));
		params.add(new BasicNameValuePair("TAGS04", "2"));
		int j = 5;
		for(int i=4;i<length+4;i++)
		{
			
			String key = null;
			
			if(i<=9)
			{
				key = "TAGS0"+i;
			}
			else
			{
				key = "TAGS"+i;
			}
			
			if(j<=9)
			{
				params.add(new BasicNameValuePair("TAGS0"+j, request.getParameter(key)));
			}
			else
			{
				params.add(new BasicNameValuePair("TAGS"+j, request.getParameter(key)));
			}
			j++;
			
		}
		
		
		
		Common co = new Common();
		HashMap<String,String> ll = co.getDbCredential();
		//String myURL = "http://"+ll.get("nib")+"/Trace.html?CMD_TYPE=GETALL_SUB_TRACK_REQ&TAGS00="+tId+"&TAGS001="+imsi+"&TAGS02="+flag+"&";
		//String myURL = "http://"+ll.get("nib")+"/cgi-bin/processData.sh";
		String myURL = "http://"+getNibIp(request)+"/cgi-bin/processData_CLI.sh";
		
		
		String mdr = "";
		
		try
		{
			if(!flag.equalsIgnoreCase("2"))
				Thread.sleep(10000);
			
			mdr = co.callPostDataUrl(myURL,params);			
			//mdr = co.callURL("http://"+getNibIp(request)+"/temp/target.json");
			
						
		}
		catch(Exception E)
		{
			fileLogger.error("Exception while request"+E.getMessage());
		}
		if(mdr.equalsIgnoreCase("ERROR"))
		{
			mdr="{\"STATUS\":\"3\"}";
		}
		fileLogger.info("Exit Function : locateWithNeighbour");	
		return mdr;		
	}
	
	//this functon will be used to create the gruop	
	public boolean createGroup(HttpServletRequest request)
	{
		String groupName = request.getParameter("group_name");
		
		String query = "insert into groups(group_name) values('"+groupName+"')";
		Common co = new Common();
		return co.executeDLOperation(query);		
	}
	//this function will delete the group
	public boolean deleteGroup(HttpServletRequest request)
	{
		String groupid = request.getParameter("group_id");
		
		String query = "delete from group_phone_mapping  where grp_id = "+groupid;
		
		Common co = new Common();
		co.executeDLOperation(query);
		query = "delete from groups where id = "+groupid;
		return co.executeDLOperation(query);
	}
	//this function will add the new number
	public boolean addNewNumber(HttpServletRequest request)
	{
		String phone_number = request.getParameter("number");
		String name = request.getParameter("owner_name");
		String query = "insert into phone_numbers(phone_number,name) values('"+phone_number+"','"+name+"')";
		Common co = new Common();
		return co.executeDLOperation(query);		
	}
	//this function will delete the number
	public boolean deleteNumber(HttpServletRequest request)
	{
		String id = request.getParameter("id");
		String query = "delete from group_phone_mapping  where num_id = "+id;
		
		Common co = new Common();
		co.executeDLOperation(query);
		 query = "delete from phone_numbers where id = "+id;
		return co.executeDLOperation(query);
	}
	//this function will be used to add the number to the groups
	public boolean assignNumberToGroup(HttpServletRequest request)
	{
		String groupid = request.getParameter("group_id");
		String numberid = request.getParameter("number_id");
		String query = "insert into group_phone_mapping(grp_id,num_id) values("+groupid+","+numberid+")";
		Common co = new Common();		
		return co.executeDLOperation(query);
	}	
	//this function will be used to add the number to the groups
	public boolean deleteNumbersFromGroup(HttpServletRequest request)
	{
		String groupid = request.getParameter("group_id");
		String numberids = request.getParameter("numbers");
		String query = "delete from group_phone_mapping where grp_id = "+groupid+" and num_id in(select id from phone_numbers where phone_number in('"+numberids+"'))";
		Common co = new Common();		
		return co.executeDLOperation(query);
	}
	
	//this function will be used to add the number to the groups
	public JSONArray getmsgLogs(HttpServletRequest request)
	{
		fileLogger.info("Inside Function : getmsgLogs");	
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String query = "select sendingtime,user_name,phone_number,msg,status from msglogs left join users on(msglogs.userid = users.id) where sendingtime between '"+startDate+"' and '"+endDate+"';";
		fileLogger.debug(query);		
		Common co = new Common();
		Statement smt = null;
		Connection con = co.getDbConnection();
		JSONArray ja = new JSONArray();
		try
		{
			smt = con.createStatement();			
			fileLogger.debug(query);
			ResultSet rs = smt.executeQuery(query);
			
			while(rs.next())
			{
				JSONObject jo = new JSONObject();
				String status = "";
				if(rs.getString("status") != null && !rs.getString("status").equalsIgnoreCase(""))
				{
					status = rs.getString("status");
				}
				
				String message = URLDecoder.decode(rs.getString("msg"), "ASCII");
				
				jo.put("time", rs.getString("sendingtime"));
				jo.put("name", rs.getString("user_name"));
				jo.put("number", rs.getString("phone_number"));
				jo.put("msg", message);
				jo.put("status", status);
				ja.put(jo);
			}			
		}
		catch(Exception E)
		{
			fileLogger.error("Exception while getting the msg logs "+E.getMessage());
		}
		finally
		{
			try
			{
				smt.close();
				con.close();
			}
			catch(Exception E)
			{
				fileLogger.error("Exception : "+E.getMessage());
			}
		}
		fileLogger.info("Exit Function : getmsgLogs");	
		return ja;
		
	}
	

	
	//this function will be used to add the number to the groups
		public String triggerPage(HttpServletRequest request)
		{
			fileLogger.info("Inside Function : triggerPage");	
			String msisdn = request.getParameter("msisdn");
			String param = request.getParameter("param");
			Common co = new Common();
			HashMap<String,String> ll = co.getDbCredential();
			
			String myURL = "http://"+getNibIp(request)+"/cgi-bin/processData.sh";
			
			String CMD_TYPE="UPD_SMSREQ";
			
			
			String TAGS00="1";
			String TAGS01=msisdn;
			String TAGS02="Dummy";
			String TAGS03="2";
			String TAGS04="DEFAULT";
			String TAGS05=param;
			
			
			String mdr = "";			
			try
			{
				//Thread.sleep(10000);
				mdr = co.bb(myURL, CMD_TYPE, TAGS00, TAGS01, TAGS02,TAGS03, TAGS04, TAGS05);				
				mdr = co.callURL(myURL);
				//mdr = co.callURL("http://10.100.200.110/celldata.json");
			}
			catch(Exception E)
			{
				
			}
			fileLogger.info("Exit Function : triggerPage");	
			return mdr;			
		}
	
	
	//this function will be used to add the number to the groups
		public String triggerSms(HttpServletRequest request)
		{
			fileLogger.info("Inside Function : triggerSms");	
			String msisdn = request.getParameter("msisdn");	
			Common co = new Common();
			HashMap<String,String> ll = co.getDbCredential();
			//String myURL = "http://10.151.96.36:13013/cgi-bin/sendsms?username=tester&password=foobar&to="+number+"&text="+message;			
			
			//String myURL = "http://"+ll.get("nib")+"/Trace.html?CMD_TYPE= UPD_SMSREQ&TAGS00=1&TAGS01="+msisdn+"&TAGS02=Dummy&TAGS03=2&TAGS04=DEFAULT&TAGS05=dummy";			
			
			
			//String myURL = "http://"+ll.get("nib")+"/cgi-bin/processData.sh";
			String myURL = "http://"+getNibIp(request)+"/cgi-bin/processData.sh";
			
			String CMD_TYPE="UPD_SMSREQ";
			
			
			String TAGS00="1";
			String TAGS01=msisdn;
			String TAGS02="Dummy";
			String TAGS03="2";
			String TAGS04="DEFAULT";
			String TAGS05="dummy";
			
			
			String mdr = "";			
			try
			{
				Thread.sleep(10000);
				mdr = co.bb(myURL, CMD_TYPE, TAGS00, TAGS01, TAGS02,TAGS03, TAGS04, TAGS05);				
				mdr = co.callURL(myURL);
				//mdr = co.callURL("http://10.100.200.110/celldata.json");
			}
			catch(Exception E)
			{
				
			}
			fileLogger.info("Exit Function : triggerSms");	
			return mdr;			
		}
		
		public String getCellsData(HttpServletRequest request)
		{
				
			Common co = new Common();
			HashMap<String,String> ll = co.getDbCredential();
			
			//String myURL = "http://"+ll.get("nib")+"/cgi-bin/processData.sh";
			String myURL = "http://"+getNibIp(request)+"/cgi-bin/processData.sh";
			
			String CMD_TYPE="GET_SON";			
			String TAGS00="1";
			
			
			String mdr = "";
			
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			
			
			params.add(new BasicNameValuePair("CMD_TYPE",CMD_TYPE ));
			params.add(new BasicNameValuePair("TAGS00", TAGS00));		
			
			try
			{
				//Thread.sleep(10000);
				
				mdr = co.callPostDataUrl(myURL,params);
				mdr = co.callURL("http://"+getNibIp(request)+"/temp/SON-NS.json");
				
				
			}
			catch(Exception E)
			{
				
			}
			return mdr;			
		}
	
		public JSONArray getLatLon(HttpServletRequest request)
		{
			fileLogger.info("Inside Function : getLatLon");	
			String MCC = request.getParameter("MCC");
			String MNC = request.getParameter("MNC");
			String LAC = request.getParameter("LAC");
			String CELL = request.getParameter("CELL");
			String query = "select lat,lon from cell_data where mcc = "+MCC+" and net = "+MNC+" and area= "+LAC+" and cell = "+CELL+" and lat is not null and lat >0 and lon is not null and lon >0 limit 1;";
			String query2 = "select lat,lon from cell_data2 where mcc = "+MCC+" and net = "+MNC+" and area= "+LAC+" and cell = "+CELL+" and lat is not null and lat >0 and lon is not null and lon >0 limit 1;";
			String query3 = "select oprname("+MCC+""+MNC+"::numeric) as opr , getcountry("+MCC+"::numeric) as country;";
			fileLogger.debug(query3);		
			Common co = new Common();
			Statement smt = null;
			Statement smt1 = null;
			Statement smt2 = null;
			Connection con = co.getDbConnection();
			JSONArray ja = new JSONArray();
			try
			{
				smt = con.createStatement();			
				smt1 = con.createStatement();
				
				ResultSet rs = smt.executeQuery(query);
				boolean bb= false;
				JSONObject jo = new JSONObject();
				while(rs.next())
				{
					
					if(rs.getString("lat") != null && rs.getString("lon") !=null )
					{
						jo.put("lat", rs.getString("lat"));
						jo.put("lon", rs.getString("lon"));
					}
					else
					{
						jo.put("lat", "-1");
						jo.put("lon", "-1");
					}
					bb = true;
					
				}				
				if(!bb)
				{
					
					ResultSet rs1 = smt1.executeQuery(query2);
					while(rs1.next())
					{						
						if(rs1.getString("lat") != null && rs1.getString("lon") !=null )
						{
							jo.put("lat", rs.getString("lat"));
							jo.put("lon", rs.getString("lon"));
						}
						else
						{
							jo.put("lat", "-1");
							jo.put("lon", "-1");
						}
						bb = true;
						//ja.put(jo);
					}
				}				
				if(!bb)
				{
					jo.put("lat", "-1");
					jo.put("lon", "-1");
					//ja.put(jo);
				}
				
				smt2 = con.createStatement();
				ResultSet rs2 = smt2.executeQuery(query3);
				while(rs2.next())
				{	
					jo.put("count", rs2.getString("country")==null?"":rs2.getString("country"));
					jo.put("opr",rs2.getString("opr")==null?"":rs2.getString("opr"));
				}
				ja.put(jo);
				
				smt.close();
				smt1.close();
				smt2.close();
				
			}
			catch(Exception E)
			{
				fileLogger.error("Exception while getting the lat lon "+E.getMessage());
			}
			finally
			{
				try
				{
					con.close();
				}
				catch(Exception E)
				{
					fileLogger.error("Exception : "+E.getMessage());
				}
			}			
			fileLogger.info("Exit Function : getLatLon");	
			return ja;			
		}
		
		
		public JSONArray getMapData(HttpServletRequest request)
		{
			fileLogger.info("Inside Function : getMapData");	
			//String CELL = request.getParameter("CELL");
			String query = "select distinct * from operators where tech != 'LTE'";
			
			fileLogger.debug(query);		
			Common co = new Common();
			Statement smt = null;
			Connection con = co.getDbConnection();
			JSONArray ja = new JSONArray();
			try
			{
				smt = con.createStatement();				
				ResultSet rs = smt.executeQuery(query);				
				
				while(rs.next())
				{					
					JSONObject jo = new JSONObject();
					if(rs.getString("lat") != null && rs.getString("lon") !=null )
					{
						jo.put("lat", rs.getString("lat"));
						jo.put("lon", rs.getString("lon"));
						jo.put("tech", rs.getString("tech"));
						jo.put("mcc", rs.getString("mcc"));
						jo.put("mnc", rs.getString("mnc"));
						jo.put("lac", rs.getString("lac"));
						jo.put("cell", rs.getString("cell"));
						jo.put("name", rs.getString("operator_name"));
						jo.put("img", rs.getString("imagename"));
					}					
					ja.put(jo);
				}
				//ja.put(jo);
			}
			catch(Exception E)
			{
				fileLogger.error("Exception while getting the"+E.getMessage());
			}
			finally
			{
				try
				{
					smt.close();					
					con.close();
				}
				catch(Exception E)
				{
					fileLogger.error("Exception : "+E.getMessage());
				}
			}		
			fileLogger.info("Exit Function : getMapData");	
			return ja;			
		}
		
		public String InsertSearchData(HttpServletRequest request)
		{
			String[] data = request.getParameterValues("data[]");
			
			String sql1 = "INSERT INTO scaneddata(packetId,nibip,imei, imsi, ta, mcc, mnc, lac, cell, rxlvl,lat, lon, isneorself,log)"
					+ " VALUES (";
			String sql2 = ",now());";
			List<String> la = new ArrayList<String>();
			try
			{				
				for (String value : data) 
				{				
					la.add(sql1+value+sql2);
				}
				new Common().executeBatchOperation(la);
				
			}
			catch(Exception E)
			{
				
			}
			return "OK";
			
		}
		
		public String updateNib(HttpServletRequest request)
		{
			
			HttpSession ses = request.getSession();
			String nib = request.getParameter("nib");			
			
			
			ses.setAttribute("currentNib", nib);
			
			return "Updated";
		}
		
		public String getNibIp(HttpServletRequest request)
		{			
			HttpSession ses = request.getSession();
			
			//Object nib = ses.getAttribute("nib");			
			String nibIp = "";
			if( ses.getAttribute("currentNib")  == null )
			{
				HashMap<String,String> ll = new Common().getDbCredential();
				nibIp =  ll.get("nib");				
			}				
			else
			{
				String aa = (String)ses.getAttribute("currentNib");
				nibIp = aa;
			}
			return nibIp;
		}
		
		public String getMapServerIp(HttpServletRequest request)
		{	
				HashMap<String,String> ll = new Common().getDbCredential();
				String mapserverIp =  ll.get("mapserver");			
				return mapserverIp;
		}
		
		
		
		public JSONArray getScanedData(HttpServletRequest request)
		{
			fileLogger.info("Inside Function : getScanedData");	
			String startTime = request.getParameter("startDate");
			String endTime = request.getParameter("endDate");
			String imsi = request.getParameter("imsi");
			
			String query = "select * from get_scan_data where log between '"+startTime+"' and  '"+endTime+"' and imsi = "+imsi +" and lat != -1 and lon != -1 and lat is not null and lon is not null;";
			
			fileLogger.debug(query);		
			Common co = new Common();
			Statement smt = null;
			Connection con = co.getDbConnection();
			JSONArray ja = new JSONArray();
			try
			{
				smt = con.createStatement();				
				ResultSet rs = smt.executeQuery(query);				
				
				while(rs.next())
				{					
					JSONObject jo = new JSONObject();
					if(rs.getString("lat") != null && rs.getString("lon") !=null && !rs.getString("lat").equalsIgnoreCase("-1") && !rs.getString("lon").equalsIgnoreCase("-1"))
					{
						jo.put("lat", rs.getString("lat"));
						jo.put("lon", rs.getString("lon"));
						jo.put("t_id", rs.getString("t_id"));
						jo.put("mcc", rs.getString("mcc"));
						jo.put("mnc", rs.getString("mnc"));
						jo.put("lac", rs.getString("lac"));
						jo.put("cell", rs.getString("cell"));
						jo.put("packetid", rs.getString("packetid"));
						jo.put("nibip", rs.getString("nibip"));
						jo.put("imei", rs.getString("imei"));
						jo.put("imsi", rs.getString("imsi"));
						jo.put("ta", rs.getString("ta"));
						jo.put("isneorself", rs.getString("isneorself"));
						jo.put("log", rs.getString("log"));
						jo.put("rxlvl", rs.getString("rxlvl"));
						ja.put(jo);
					}					
					
				}
				//ja.put(jo);
			}
			catch(Exception E)
			{
				fileLogger.error("Exception while getting the"+E.getMessage());
			}
			finally
			{
				try
				{
					smt.close();					
					con.close();
				}
				catch(Exception E)
				{
					fileLogger.error("Exception : "+E.getMessage());
				}
			}	
			fileLogger.info("Exit Function : getScanedData");	
			return ja;			
		}
		
		
		
		public JSONArray getScanedDataCenter(HttpServletRequest request)
		{
			fileLogger.info("Inside Function : getScanedDataCenter");	
			String startTime = request.getParameter("startDate");
			String endTime = request.getParameter("endDate");
			String imsi = request.getParameter("imsi");
			
			String query = "select * from get_scan_data where log between '"+startTime+"' and  '"+endTime+"' and imsi = "+imsi+" and isneorself = 'S' order by log desc limit 1";
			
			fileLogger.debug(query);		
			Common co = new Common();
			Statement smt = null;
			Connection con = co.getDbConnection();
			JSONArray ja = new JSONArray();
			try
			{
				smt = con.createStatement();				
				ResultSet rs = smt.executeQuery(query);				
				
				while(rs.next())
				{					
					JSONObject jo = new JSONObject();
					
						jo.put("lat", rs.getString("lat"));
						jo.put("lon", rs.getString("lon"));
						jo.put("t_id", rs.getString("t_id"));
						jo.put("mcc", rs.getString("mcc"));
						jo.put("mnc", rs.getString("mnc"));
						jo.put("lac", rs.getString("lac"));
						jo.put("cell", rs.getString("cell"));
						jo.put("packetid", rs.getString("packetid"));
						jo.put("nibip", rs.getString("nibip"));
						jo.put("imei", rs.getString("imei"));
						jo.put("imsi", rs.getString("imsi"));
						jo.put("ta", rs.getString("ta"));
						jo.put("isneorself", rs.getString("isneorself"));
						jo.put("log", rs.getString("log"));
						ja.put(jo);
										
					
				}
				//ja.put(jo);
			}
			catch(Exception E)
			{
				fileLogger.error("Exception while getting the"+E.getMessage());
			}
			finally
			{
				try
				{
					smt.close();					
					con.close();
				}
				catch(Exception E)
				{
					fileLogger.debug("Exception : "+E.getMessage());
				}
			}		
			fileLogger.info("Exit Function : getScanedDataCenter");	
			return ja;			
		}
		
		
		public JSONArray getDistinctIMSI(HttpServletRequest request)
		{
			fileLogger.info("Inside Function : getDistinctIMSI");	
			//String startTime = request.getParameter("startDate");
			//String endTime = request.getParameter("endDate");
			
			String query = "select distinct(imsi) from scaneddata";
			
			fileLogger.debug(query);		
			Common co = new Common();
			Statement smt = null;
			Connection con = co.getDbConnection();
			JSONArray ja = new JSONArray();
			try
			{
				smt = con.createStatement();				
				ResultSet rs = smt.executeQuery(query);
				while(rs.next())
				{					
					ja.put(rs.getString("imsi"));
				}
			}
			catch(Exception E)
			{
				fileLogger.error("Exception while getting the"+E.getMessage());
			}
			finally
			{
				try
				{
					smt.close();					
					con.close();
				}
				catch(Exception E)
				{
					fileLogger.error("Exception : "+E.getMessage());
				}
			}			
			fileLogger.info("Exit Function : getDistinctIMSI");	
			return ja;			
		}
		
		
		@SuppressWarnings("deprecation")
		public String getListForImsiImei(HttpServletRequest request)
		{	
			fileLogger.info("Inside Function : getListForImsiImei");	
			String cmdType = request.getParameter("cmd");
			String fileName = request.getParameter("fileName");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("CMD_TYPE",cmdType ));
			//params.add(new BasicNameValuePair("TAGS00", tId));
			Common co = new Common();
			HashMap<String,String> ll = co.getDbCredential();
			String myURL = "http://"+getNibIp(request)+"/cgi-bin/processData.sh";
			String mdr = "";
			try
			{			
				mdr=co.callPostDataUrl(myURL,params);
				mdr = co.callURL("http://"+getNibIp(request)+"/temp/"+fileName);
			}
			catch(Exception E)
			{
				fileLogger.error("Exception while request"+E.getMessage());
			}
			if(mdr.equalsIgnoreCase("ERROR"))
			{
				mdr="{\"STATUS\":\"3\"}";
			}	
			fileLogger.info("Exit Function : getListForImsiImei");	
			return mdr;		
		}
		
		
		@SuppressWarnings("deprecation")
		public String serverCall(HttpServletRequest request)
		{	
			fileLogger.info("Inside Function : serverCall");	
			String cmdType = request.getParameter("cmd");
			String fileName = request.getParameter("fileName");
			String flag = request.getParameter("flag");
			String imei = request.getParameter("imei");
			String imsi = request.getParameter("imsi");
			
			
			
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("CMD_TYPE",cmdType ));
			params.add(new BasicNameValuePair("TAGS00", ""));
			params.add(new BasicNameValuePair("TAGS01", imsi));
			params.add(new BasicNameValuePair("TAGS02", imei));
			params.add(new BasicNameValuePair("TAGS03", flag));
			Common co = new Common();
			HashMap<String,String> ll = co.getDbCredential();
			String myURL = "http://"+getNibIp(request)+"/cgi-bin/processData_CLI.sh";
			String mdr = "";
			try
			{			
				mdr =  co.callPostDataUrl(myURL,params);
				//mdr = co.callURL("http://"+getNibIp(request)+"/temp/"+fileName);
			}
			catch(Exception E)
			{
				fileLogger.error("Exception while request"+E.getMessage());
			}
			if(mdr.equalsIgnoreCase("ERROR"))
			{
				mdr="{\"STATUS\":\"3\"}";
			}		
			fileLogger.info("Exit Function : serverCall");	
			return mdr;		
		}
		
		/*public String getRunningMode()
		{			
			
			return "exclusion";
		} */
		
		@SuppressWarnings("deprecation")
		public String setStatus(HttpServletRequest request)
		{
			fileLogger.info("Inside Function : setStatus");	
			String MONITMODE = request.getParameter("MONITMODE");
			String INVOKETRACE = request.getParameter("INVOKETRACE");
			String IMEITRACEMODE = request.getParameter("IMEITRACEMODE");
			String AUTOCONNNECT = request.getParameter("AUTOCONNNECT");
			String RFU2 = request.getParameter("RFU2");
			String RFU3 = request.getParameter("RFU3");
			String RFU4 = request.getParameter("RFU4");
			String CMD_TYPE = request.getParameter("cmdType");
			
			Common co = new Common();
			
			HashMap<String,String> ll = co.getDbCredential();
			
			String myURL = "http://"+getNibIp(request)+"/cgi-bin/processData.sh";
			
			//String CMD_TYPE="GET_MONIT_MODE";			
			String TAGS00=MONITMODE;
			String TAGS01=INVOKETRACE;
			String TAGS02=IMEITRACEMODE;
			String TAGS03=AUTOCONNNECT;
			String TAGS04=RFU2;
			String TAGS05=RFU3;
			String TAGS06=RFU4;
			
			//String myURL = "http://10.100.208.172/temp/text.json";			
			String mdr = "";			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("CMD_TYPE",CMD_TYPE ));
			params.add(new BasicNameValuePair("TAGS00", TAGS00));
			params.add(new BasicNameValuePair("TAGS01", TAGS01));
			params.add(new BasicNameValuePair("TAGS02", TAGS02));
			params.add(new BasicNameValuePair("TAGS03", TAGS03));
			params.add(new BasicNameValuePair("TAGS04", TAGS04));
			params.add(new BasicNameValuePair("TAGS05", TAGS05));
			params.add(new BasicNameValuePair("TAGS06", TAGS06));
			
			
			try
			{	
				mdr = co.callPostDataUrl(myURL,params);
				mdr = co.callURL("http://"+getNibIp(request)+"/temp/nsFlagList.json");
			}
			catch(Exception E)
			{
				fileLogger.error("Exception while request"+E.getMessage());
			}
			if(mdr.equalsIgnoreCase("ERROR"))
			{
				mdr="{\"STATUS\":\"3\"}";
			}		
			fileLogger.info("Exit Function : setStatus");	
			return mdr;		
		}
		
		
		
		@SuppressWarnings("deprecation")
		public String getRunningMode(HttpServletRequest request)
		{	
			fileLogger.info("Inside Function : getRunningMode");	
			String cmdType = request.getParameter("cmd");
			String fileName = request.getParameter("fileName");
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("CMD_TYPE",cmdType ));
			Common co = new Common();
			HashMap<String,String> ll = co.getDbCredential();
			//String myURL = "http://"+getNibIp(request)+"/cgi-bin/processData.sh";
			String myURL = "http://"+getNibIp(request)+"/cgi-bin/processData_CLI.sh";
			String mdr = "";
			try
			{			
				mdr = co.callPostDataUrl(myURL,params);
				//mdr = co.callURL("http://"+getNibIp(request)+"/temp/"+fileName);
			}
			catch(Exception E)
			{
				fileLogger.error("Exception while request"+E.getMessage());
			}
			if(mdr.equalsIgnoreCase("ERROR"))
			{
				mdr="{\"STATUS\":\"3\"}";
			}		
			fileLogger.info("Exit Function : getRunningMode");	
			return mdr;		
		}
		
		public void insertAlarm(String data)
		{			
			String query = "insert into cdrAlarm ";	
			new Common().executeDLOperation(query);			
		}
		
		
		public JSONArray getCDRdata(HttpServletRequest request)
		{
			fileLogger.info("Inside Function : getCDRdata");	
			String startTime = request.getParameter("startTime");
			String endTime = request.getParameter("endTime");
			String query = "select * from cdrlogs where inserttime between '"+startTime+"' and '"+endTime+"' and packet_type='Normal_LU'";
			fileLogger.debug(query);
			fileLogger.info("Exit Function : getCDRdata");	
			return getJson(query);			
		}
		
		
		/*public JSONArray getCDRdata(HttpServletRequest request)
		{
			Common co = new Common();
			Statement smt = null;
			Connection con = co.getDbConnection();
			
			JSONArray ja = new JSONArray();
			
			
			
			String startTime = request.getParameter("startTime");
			String endTime = request.getParameter("endTime");
			try
			{
				smt = con.createStatement();			
				String query = "select * from cdrlogs where inserttime between '"+startTime+"' and '"+endTime+"' and packet_type='Normal_LU'";
				fileLogger.debug(query);
				ResultSet rs = smt.executeQuery(query);
				ResultSetMetaData rm = rs.getMetaData();
				int totalColumns = rm.getColumnCount();
				ArrayList<String> columns = new ArrayList<String>();
				
				for(int i=1;i<=totalColumns;i++ )
				{
					columns.add(rm.getColumnName(i));
				}
				
				while(rs.next())
				{				
					JSONObject jb = new JSONObject();
					for(String cname:columns)
					{
						jb.put(cname, rs.getString(cname));
					}
					ja.put(jb);
				}			
			}
			catch(Exception E)
			{
				fileLogger.debug("Exception while authenticating the user "+E.getMessage());
			}
			finally
			{
				try
				{
					smt.close();
					con.close();
				}
				catch(Exception E)
				{
					
				}
			}	
			return ja;
		}*/
		
		
		public JSONArray getSpectrumData(HttpServletRequest request)
		{
			fileLogger.info("Inside Function : getSpectrumData");	
			String startTime = request.getParameter("startTime");
			String endTime = request.getParameter("endTime");
			String type = request.getParameter("type");
			String query = "select a.id,a.ip,a.count,a.power,a.freq,a.tstmp,a.logtime,b.lat,b.lon,wsdiDataangle.angle,wsdiDataangle.anglet from wsdiData a left join dis_gps b on(a.tstmp = b.tstmp) left join wsdiDataangle on(a.tstmp= wsdiDataangle.tstmp) where a.logtime between '"+startTime+"' and '"+endTime+"'";
			fileLogger.debug("Type"+type);
			
			switch(Integer.parseInt(type))
			{
				case 1:
					query="select a.id,a.ip,a.count,a.power,a.freq,a.tstmp,a.logtime,b.lat,b.lon,wsdiDataangle.angle,wsdiDataangle.anglet from (select * from wsdiData where(freq,ip,power::numeric,DATE_TRUNC('min',to_timestamp(tstmp,'DDMMYY-HH24-MI-SS')::timestamp without time zone)) in (select distinct freq,ip,max(power::numeric),DATE_TRUNC('min',to_timestamp(tstmp,'DDMMYY-HH24-MI-SS')::timestamp without time zone) from wsdiData WHERE (to_timestamp(tstmp,'DDMMYY-HH24-MI-SS')::timestamp without time zone) between '"+startTime+"' and '"+endTime+"' group by freq,DATE_TRUNC('min',to_timestamp(tstmp,'DDMMYY-HH24-MI-SS')::timestamp without time zone),ip) ) a  left join dis_gps b on(a.tstmp = b.tstmp) left join wsdiDataangle on(a.tstmp= wsdiDataangle.tstmp) order by a.logtime asc";;
				break;
				case 2:
					query="select a.id,a.ip,a.count,a.power,a.freq,a.tstmp,a.logtime,b.lat,b.lon,wsdiDataangle.angle,wsdiDataangle.anglet from (select * from wsdiData where(freq,ip,power::numeric,DATE_TRUNC('second',to_timestamp(tstmp,'DDMMYY-HH24-MI-SS')::timestamp without time zone)) in (select distinct freq,ip,max(power::numeric),DATE_TRUNC('second',to_timestamp(tstmp,'DDMMYY-HH24-MI-SS')::timestamp without time zone) from wsdiData WHERE (to_timestamp(tstmp,'DDMMYY-HH24-MI-SS')::timestamp without time zone) between '"+startTime+"' and '"+endTime+"' group by freq,DATE_TRUNC('second',to_timestamp(tstmp,'DDMMYY-HH24-MI-SS')::timestamp without time zone),ip) ) a  left join dis_gps b on(a.tstmp = b.tstmp) left join wsdiDataangle on(a.tstmp= wsdiDataangle.tstmp) order by a.logtime asc";
				break;
			}
			fileLogger.debug(query);
			fileLogger.info("Exit Function : getSpectrumData");
			return getJson(query);			
		}
		
		
		
		
		/*public JSONArray getSpectrumData(HttpServletRequest request)
		{
			Common co = new Common();
			Statement smt = null;
			Connection con = co.getDbConnection();
			
			JSONArray ja = new JSONArray();
			
			
			
			String startTime = request.getParameter("startTime");
			String endTime = request.getParameter("endTime");
			try
			{
				smt = con.createStatement();			
				//String query = "select * from wsdiData where logtime between '"+startTime+"' and '"+endTime+"'";
				String query = "select a.id,a.ip,a.count,a.power,a.freq,a.tstmp,a.logtime,b.lat,b.lon,wsdiDataangle.angle,wsdiDataangle.anglet from wsdiData a left join dis_gps b on(a.tstmp = b.tstmp) left join wsdiDataangle on(a.tstmp= wsdiDataangle.tstmp) where a.logtime between '"+startTime+"' and '"+endTime+"'";
				fileLogger.debug(query);
				ResultSet rs = smt.executeQuery(query);
				ResultSetMetaData rm = rs.getMetaData();
				int totalColumns = rm.getColumnCount();
				ArrayList<String> columns = new ArrayList<String>();
				
				for(int i=1;i<=totalColumns;i++ )
				{
					columns.add(rm.getColumnName(i));
				}
				
				while(rs.next())
				{				
					JSONObject jb = new JSONObject();
					for(String cname:columns)
					{
						jb.put(cname, rs.getString(cname));
					}
					ja.put(jb);
				}			
			}
			catch(Exception E)
			{
				fileLogger.debug("Exception while authenticating the user "+E.getMessage());
			}
			finally
			{
				try
				{
					smt.close();
					con.close();
				}
				catch(Exception E)
				{
					
				}
			}	
			return ja;
		}*/
		
		public String getReq(HttpServletRequest request)
		{	
			fileLogger.info("Inside Function : getReq");
			String cmdType = request.getParameter("cmdType");
			String reqType = request.getParameter("reqType");
			String idType = request.getParameter("idType");
			String type_value = request.getParameter("type_value");
			String vlr = request.getParameter("vlr");
			String hlr = request.getParameter("hlr");
			String ftn = request.getParameter("ftn");
			String msc = request.getParameter("msc");
			String fileName = request.getParameter("fileName");
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("CMD_TYPE",cmdType ));
			params.add(new BasicNameValuePair("TAGS00", reqType));
			params.add(new BasicNameValuePair("TAGS01", idType));
			params.add(new BasicNameValuePair("TAGS02", type_value));
			params.add(new BasicNameValuePair("TAGS03", hlr));			
			params.add(new BasicNameValuePair("TAGS04", vlr));			
			params.add(new BasicNameValuePair("TAGS05", ftn));
			params.add(new BasicNameValuePair("TAGS06", msc));
			
			Common co = new Common();
			HashMap<String,String> ll = co.getDbCredential();
			String myURL = "http://"+getNibIp(request)+"/cgi-bin/processData_CLI.sh";
			String mdr = "";
			try
			{			
				mdr = co.callPostDataUrl(myURL,params);				
				//mdr = co.callURL("http://"+getNibIp(request)+"/temp/"+fileName);
				
			}
			catch(Exception E)
			{
				fileLogger.error("Exception while request"+E.getMessage());
			}
			if(mdr.equalsIgnoreCase("ERROR"))
			{
				mdr="{\"STATUS\":\"3\"}";
			}		
			fileLogger.info("Exit Function : getReq");
			return mdr;		
		}
		
		public String mtSmsGeoData(HttpServletRequest request)
		{  
			fileLogger.info("Inside Function : mtSmsGeoData");
			String data = request.getParameter("data");
			String ip = request.getParameter("ip");
			String count = request.getParameter("count");
			
			fileLogger.debug("DATA RECIVED mtSmsGeoData "+data+",ip = "+ip+",count = "+count);
			
			new SMSIntercept().sendText(ip+"$"+count+"$"+data);
			storeSms(data,ip,count);
			fileLogger.info("Exit Function : mtSmsGeoData");
			return "";
		}
		
		public void storeSms(String data,String ip,String count)
		{
			String[] msgData = data.split("\\$");
			String query = "insert into geo_loc_mtsms (ip,count,calling_party_num,smsc_num,smsc_time,system_timestamp,sms_mtdata,logtime)"+
							"values('"+ip+"','"+count+"','"+msgData[0]+"','"+msgData[1]+"','"+msgData[2]+"','"+msgData[3]+"','"+msgData[4]+"',now());";
			new Common().executeDLOperation(query); 
			
		}
		
		public String cdrData(HttpServletRequest request)
		{	  
			fileLogger.info("Inside Function : cdrData");
			   final HttpServletRequest req = request;
			   //String count = request.getParameter("count");
	           final String data = request.getParameter("data");
			   final String ip = request.getParameter("ip");
			   final String count = request.getParameter("count");
			   fileLogger.debug("data in cdrData is :"+request.getParameter("data")+"--Count--"+count);
			   new Common().auditLog(new Common().convertToMapFor2g(ip,data,"R")); 
			   new Thread()
			     {
			          public void run()
			    {
			     fileLogger.debug("starting Thread for count "+count);
			           ArrayList<String> pack = formtaPacket(data,ip,count);
			     
			     /*if(pack.get(20) != null && pack.get(20).equalsIgnoreCase("success"))
			     {
			      new ThreegOperations().set2gHoldStatus(pack.get(4));;
			     }*/
				if(Operations.bist != null && Operations.bist.cdr) 
				{
					bist.compareCdr(pack.get(4), pack.get(5));
				}
			     insertCDRData(pack,ip,count);
			     //new SocketServer().sendText(pack);
			     fileLogger.debug("ending cdrdata Count := "+count);
			          }
			        }.start();
			        fileLogger.info("Exit Function : cdrData");
			        return "cdrdata Count := "+count;
		}
		
		public String pollData(HttpServletRequest request)
		  {
		   fileLogger.info("Inside Function : pollData");
		   fileLogger.debug("data in pollData is :"+request.getParameter("data")+"--Count--"+request.getParameter("count"));
		   final String  data = request.getParameter("data");
		   final String  ip = request.getParameter("ip");
		   final String  count = request.getParameter("count");
		   new Common().auditLog(new Common().convertToMapFor2g(ip,data,"R")); 
		   fileLogger.debug("data in pollData is :"+data+"--Count--"+count);
		   
		   try
		   {
		    
		     new Thread()
		        {
		             public void run()
		       {
		           try
		           {
		        	DecimalFormat df2 = new DecimalFormat(".######");
		            
		            fileLogger.debug("starting thread for polldata "+count);
		            JSONObject jo = new JSONObject(data);
		      if(jo.getString("LDFLAG").equalsIgnoreCase("0"))
		      {
		       String latlng = getLatitudeLongitudeForcalc(jo);
		       if(!latlng.equalsIgnoreCase("false"))
		       {
		        Common co = new Common();
		        String[] loc = latlng.split(",");
		        String query = "INSERT INTO tracked_imsi(imsi, imei, ta,power, lat, lon, acc, packet,stype)"
		          + "values("+jo.getString("IMSI")+","+jo.getString("IMEI")+","+jo.getString("TA")+","+(Integer.parseInt(jo.getString("RXLVL"))-110)+","+df2.format(Double.parseDouble(loc[0]))+","+df2.format(Double.parseDouble(loc[1]))+",'"+(jo.getString("LDFLAG").equalsIgnoreCase("0")?'N':'Y')+"','"+data+"','2G')";
		        int rowId=co.executeQueryAndReturnId(query);
		        
		 	    query = "select ti.imsi,ti.imei,ti.ta,ti.power,ti.lat,ti.lon,ti.acc,date_trunc('second',ti.logtime + '05:30:00'::interval) arrival_time,tl.istarget,tl.name target_name,tl.type from tracked_imsi ti left join target_list tl on (ti.imsi=tl.imsi or ti.imei=tl.imei) where (tl.imei='"+jo.getString("IMEI")+"' or tl.imsi='"+jo.getString("IMSI")+"') and ti.id="+rowId+" and tl.type='Blacklist'";
				JSONArray rs =  new Operations().getJson(query);
				if(rs.length()>0){
					new TrackedImsiServer().sendText(rs.getJSONObject(0).toString());
				}
		       }
		       else
		       {
		        fileLogger.debug(jo.toString());
		       }
		      }else
		      {
		       Common co = new Common();
		       String query = "INSERT INTO tracked_imsi(imsi, imei, ta,power, lat, lon, acc, packet,stype)"
		         + "values('"+jo.getString("IMSI")+"','"+jo.getString("IMEI")+"',"+jo.getString("TA")+","+(Integer.parseInt(jo.getString("RXLVL"))-110)+","+df2.format(Double.parseDouble(jo.getString("LAT")))+","+df2.format(Double.parseDouble(jo.getString("LONG")))+",'"+(jo.getString("LDFLAG").equalsIgnoreCase("0")?'N':'Y')+"','"+data+"','2G')";
		        int rowId=co.executeQueryAndReturnId(query);
		        
		 	    query = "select ti.imsi,ti.imei,ti.ta,ti.power,ti.lat,ti.lon,ti.acc,date_trunc('second',ti.logtime + '05:30:00'::interval) arrival_time,tl.istarget,tl.name target_name,tl.type from tracked_imsi ti left join target_list tl on (ti.imsi=tl.imsi or ti.imei=tl.imei) where (tl.imei='"+jo.getString("IMEI")+"' or tl.imsi='"+jo.getString("IMSI")+"') and ti.id="+rowId+" and tl.type='Blacklist'";
				JSONArray rs =  new Operations().getJson(query);
				if(rs.length()>0){
					new TrackedImsiServer().sendText(rs.getJSONObject(0).toString());
				}
		      }
		      
		      fileLogger.debug("ending thread for polldata "+count);
		           }
		           catch(Exception e)
		           {
		            fileLogger.debug(""+e.getMessage());
		           }
		           }
		         }.start(); 
		   }
		   catch(Exception E)
		   {
		    fileLogger.error("POLL DATA Exception"+E.getMessage());
		   }  
			fileLogger.info("Exit Function : pollData");
		   return count;
		  }
		
		
		
		public String pollData(JSONObject data1,String ip1,String count1)
		  { 
		   fileLogger.info("Inside Function : pollData");
		   fileLogger.debug("data in pollData is :"+data1+"--Count--"+count1);
		   final JSONObject  data = data1;
		   final String  ip =ip1;
		   final String  count = count1;
		   fileLogger.debug("data in pollData is :"+data+"--Count--"+count);
		   
		   try
		   {
		    
		    
		    
		    
		     new Thread()
		        {
		             public void run()
		       {
		           try
		           {
		            DecimalFormat df2 = new DecimalFormat(".######");
		            fileLogger.debug("starting thread for polldata "+count);
		            JSONObject jo = data;
		      if(jo.getString("LDFLAG").equalsIgnoreCase("0"))
		      {
		       String latlng = getLatitudeLongitudeForcalc(jo);
		       if(!latlng.equalsIgnoreCase("false"))
		       {
		        Common co = new Common();
		        String[] loc = latlng.split(",");		        
		        String query = "INSERT INTO tracked_imsi(imsi, imei, ta,power, lat, lon, acc, packet,stype)"
		          + "values('"+jo.getString("IMSI")+"','"+jo.getString("IMEI")+"',"+jo.getString("TA")+","+(Integer.parseInt(jo.getString("RXLVL")))+","+df2.format(Double.parseDouble(loc[0]))+","+df2.format(Double.parseDouble(loc[1]))+",'"+(jo.getString("LDFLAG").equalsIgnoreCase("0")?'N':'Y')+"','"+data+"','3G')";
		        int rowId=co.executeQueryAndReturnId(query);
		        
		 	    query = "select ti.imsi,ti.imei,ti.ta,ti.power,ti.lat,ti.lon,ti.acc,date_trunc('second',ti.logtime + '05:30:00'::interval) arrival_time,tl.istarget,tl.name target_name,tl.type from tracked_imsi ti left join target_list tl on (ti.imsi=tl.imsi or ti.imei=tl.imei) where (tl.imei='"+jo.getString("IMEI")+"' or tl.imsi='"+jo.getString("IMSI")+"') and ti.id="+rowId+" and tl.type='Blacklist'";
				JSONArray rs =  new Operations().getJson(query);
				if(rs.length()>0){
					new TrackedImsiServer().sendText(rs.getJSONObject(0).toString());
				}
		        //sendTriggerToTRGL();
		       }
		       else
		       {
		        fileLogger.debug(jo.toString());
		       }
		      }/*else
		      {
		       Common co = new Common();
		       String query = "INSERT INTO tracked_imsi(imsi, imei, ta,power, lat, lon, acc, packet)"
		         + "values('"+jo.getString("IMSI")+"','"+jo.getString("IMEI")+"',"+jo.getString("TA")+","+(Integer.parseInt(jo.getString("RXLVL"))-110)+","+jo.getString("LAT")+","+jo.getString("LONG")+",'"+(jo.getString("LDFLAG").equalsIgnoreCase("0")?'N':'Y')+"','"+data+"')";
		        co.executeDLOperation(query);
		       new TrackedImsiServer().sendText("recived");
		      }*/
		      
		      fileLogger.debug("ending thread for polldata "+count);
		           }
		           catch(Exception e)
		           {
		            fileLogger.debug(""+e.getMessage());
		           }
		            
		           }
		         }.start();
		    
		   }
		   catch(Exception E)
		   {
		    fileLogger.error("POLL DATA Exception"+E.getMessage());
		   }
		   
		   
		   fileLogger.info("Exit Function : pollData");
		   return count;
		  }
		
		
		public void cdrData(String data,String ip,String count)
		{	
			ArrayList<String> pack = formtaPacket(data,ip,count);
			if(Operations.bist != null && Operations.bist.cdr) 
		    {
		    	 bist.compareCdr(pack.get(4), pack.get(5));
		    }
			insertCDRData(pack,ip,count);
		}
		public ArrayList<String> formtaPacket(String dataPacket,String ip,String count)
		{
			  fileLogger.info("Inside Function : formtaPacket");
			String aa = dataPacket;
			//String aa = "'{\"value\":\"Normal_LU    IMSI:404045000053267 IMEI:867466028723794 MSISDN:                 PTMSI:0x00000000 TMSI:0x00000000 OL:0xfffe NL:0x0006 CID:0x0001 Ta:0   RxL:-110 TStmp:310170-18:22:26 FTyp:FAIL_NO_SUITABLE_CELLS_IN_LOCATION_AREA\",\"channel\":\"stdout\"}'";
			
			//aa = aa.substring(1,aa.length()-1);
			//fileLogger.debug("LENGTH : "+aa.length());
			fileLogger.debug("data formated"+aa);
			ArrayList<String> finalPacket = new ArrayList<String>();
			ArrayList<String> returnPacket = new ArrayList<String>();
			try
			{	
				
				//JSONObject dd = new JSONObject(aa);
				//fileLogger.debug(dd.toString());
				//String [] bb = dd.get("value").toString().split(" ");
				fileLogger.debug(aa);
				String [] bb = aa.toString().split(" ");
				ArrayList<String> packet = new ArrayList<String>();
				Collections.addAll(packet, bb);
				returnPacket.add(ip);
				returnPacket.add(count);
				for(int i=0;i< packet.size();i++)
				{
					
					if(!packet.get(i).trim().equalsIgnoreCase(""))
					{
						
						finalPacket.add(packet.get(i));
					}
				}
				for(int i=0;i< finalPacket.size();i++)
				{
					
					if(finalPacket.get(i).contains(":"))
					{
						String[] spilPack = finalPacket.get(i).split(":");
						if(spilPack.length == 1)
							returnPacket.add("null");
						else
							returnPacket.add(finalPacket.get(i).split(":")[1]);
					}
					else
					{
						returnPacket.add(finalPacket.get(i));
					}			
				}
				
				fileLogger.debug(returnPacket.toString());			
			}
			catch(Exception E)
			{
				fileLogger.debug(E.getMessage());
			}
			 fileLogger.info("Exit Function : formtaPacket");
			return returnPacket;
		}
		
		public String insertCDRData(ArrayList<String> packet,String ip,String count)
		{
			 fileLogger.info("Inside Function : insertCDRData");
			if(!packet.get(4).equalsIgnoreCase("null")){
			if(packet.size()>0)
			{
			if(!packet.get(20).equalsIgnoreCase("failure")){
				Operations operations=new Operations();

				StringBuilder packetString = new StringBuilder();		
			/*	
				for(int i=0;i<packet.size();i++)
				{
					if(i==0)
						packetString.append("'"+packet.get(i)+"'");
					else
						packetString.append(",'"+packet.get(i)+"'");			
				}
				*/
				Common co = new Common();
				boolean showCueStatus=false;
				//String query = "INSERT INTO cdrlogs(ip, count, packet_type, imsi, imei, msisdn, ptmsi, tmsi,ol, nl, cid, ta, rxl, tstmp, ftyp, inserttime)"
				//		+ "values('"+ip+"','"+count+"',"+packetString.toString()+")";
				
				JSONArray jj = getJson("select * from target_list where imsi ='"+packet.get(4)+"' or imei = '"+packet.get(5)+"'");
				String targgetType = "other";
				String targetName = "";
				if(jj.length()>0) 
				{
					try {
						targgetType = jj.getJSONObject(0).getString("type");
						targetName = jj.getJSONObject(0).getString("name");
						
					} catch (Exception e) {
						// TODO: handle exception
					}	
				}
				
				//public static String autoEvent = "auto";
				
				DBDataService dbDataService = DBDataService.getInstance();
				String currentEventName = dbDataService.getCurrentEventName();
				fileLogger.debug("@event currenteventname 1 is :"+currentEventName);
				if(currentEventName==null){
					currentEventName="manual";
				}
				fileLogger.debug("@event currenteventname 2 is :"+currentEventName);
				String eventType="1";
				if(targgetType.equalsIgnoreCase("blacklist")){
						eventType="7";
				}else{
					if(currentEventName.equalsIgnoreCase("ugs") || currentEventName.equalsIgnoreCase("trgl")){
						eventType="6";
					}
				}
		
				int antId=-1;
				try {
					antId = new Operations().getJson("select antenna_id from active_antenna where opr_type='tracking'").getJSONObject(0).getInt("antenna_id");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
				
				String transId="";
				int rxlOld=0;
				int rxlValid=0;
				
				
//				fileLogger.debug("CDRlogs check 2g msloc doesn't contain , check 1");
				
				String gpsError="";
				String packTech=packet.get(2);
				fileLogger.debug("CDRlogs check 2g msloc doesn't contain , check 1 and packTech = "+packTech);
				if(packTech.equalsIgnoreCase("2g")){
					String msLoc=packet.get(14);
					fileLogger.debug("CDRlogs check 2g msloc doesn't contain , check 1 and msLoc = "+msLoc);
					if(!msLoc.contains(",")) {
						gpsError=msLoc;
						msLoc="Err_NA,Err_NA";
						packet.set(14, msLoc);
						
					}
				}
				
				
				fileLogger.debug("CDRlogs check 2g msloc doesn't contain , Out");
				
				if(packet.get(20).equalsIgnoreCase("success")){
					transId=CurrentOperationType.genrateTransId();
					rxlValid=Integer.parseInt(packet.get(11));
				}else{
					//JSONArray cdrOld=new Operations().getJson("select trans_id,rxl from cdrlogs where imsi='"+packet.get(4)+"' and ant_id='"+antId+"' order by id desc limit 1");
					JSONArray cdrOld=new Operations().getJson("select trans_id,rxl,msloc from cdrlogs where imsi='"+packet.get(4)+"' and ant_id='"+antId+"' order by id desc limit 1");
					try {
						JSONObject cdrOldObj=cdrOld.getJSONObject(0);
						transId=cdrOldObj.getString("trans_id");
						rxlOld=cdrOldObj.getInt("rxl");
						packTech=packet.get(2);
						String msLoc=packet.get(14);
						if(packTech.equalsIgnoreCase("2g")||(msLoc.contains("0.0,0.0"))){
							packet.set(14, msLoc);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					co.executeDLOperation("delete from cdrlogs where trans_id='"+transId+"'");
					co.executeDLOperation("delete from cdrlogs_current where trans_id='"+transId+"'");
					int rxlNew=Integer.parseInt(packet.get(11));
				    if(rxlNew>rxlOld){
				    	rxlValid=rxlNew;
				    }else{
				    	rxlValid=rxlOld;
				    }
				    if(rxlValid==0){
				    	rxlValid=Integer.parseInt(packet.get(11));
				    	transId=CurrentOperationType.genrateTransId();
				    }
				    packet.set(11,Integer.toString(rxlValid));
				
					//getting rxl and transid of previous success package
					
				}
				fileLogger.debug("trans_id cdrlogs is :"+transId);
				
				fileLogger.debug("antId cdrlogs is :"+antId);
				
				double distance=0.0;
				String calcBasic="";
				
				JSONArray gpsArr=new Operations().getJson("select lat,lon from gpsdata order by logtime desc limit 1");
				double selfLat=0.0;
				double selfLon=0.0;
				try {
					if(gpsArr.length()>0){
					JSONObject gpsObj=gpsArr.getJSONObject(0);
					selfLat=gpsObj.getDouble("lat");
					selfLon=gpsObj.getDouble("lon");
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				selfLat=Math.round(selfLat*1000000.0)/1000000.0;
				selfLon=Math.round(selfLon*1000000.0)/1000000.0;
				
				String selfLoc=selfLat+","+selfLon;
				
				
				
				try {
                    if(packet.get(14).toLowerCase().contains("na")){
                    String tmsloc="inserttime >= timezone('UTC', now()) - INTERVAL '" +DBDataService.configParamMap.get("RecadgTime_GPS")+" minutes'and ";
                    JSONArray cdrOld=new Operations().getJson("select trans_id,rxl,msloc from cdrlogs where "+tmsloc+" imsi='"+packet.get(4)+"'and msloc not like '%NA%' order by id desc limit 1");
                    String reqString="select trans_id,rxl,msloc from cdrlogs where "+tmsloc+" imsi='"+packet.get(4)+"'and msloc not like '%NA%' order by id desc limit 1";
                    fileLogger.debug("Query is here  made = "+reqString);
                    JSONObject cdrOldObj=cdrOld.getJSONObject(0);
                    //if(packTech.equalsIgnoreCase("2g")){
                    String mslocP= cdrOldObj.getString("msloc");
                    if(mslocP.contains("0.0,0.0")) {
                    	fileLogger.debug("Invalid GPS Found to replace so ignoring");
                    }
                    else {
                    	packet.set(14,mslocP);
                    	fileLogger.debug("Replacing GPS with Trans Id=   "+cdrOldObj.getString("trans_id"));
                    }
                    //}
                        }
                    } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    //e.printStackTrace();
                    fileLogger.debug("GPS not found from previous record");
                   
                }
				
				double ta=-1;				
				int actta=-1;
				int y =-1;
				actta =Integer.parseInt(packet.get(10));
				fileLogger.debug("@acctual value of ta"+actta);
				fileLogger.debug("@tech "+ packet.get(2));
				String packTech1=packet.get(2);
				y =Integer.parseInt(packet.get(10));
				if (packet.get(2).equalsIgnoreCase("3g") ) {
					ta=Integer.parseInt(packet.get(10))/2;
					fileLogger.debug("@ before floor value of ta"+ta);
					y= (int) Math.floor(ta);
					fileLogger.debug("@ floor value of ta"+y);
					if (y==0 )
					{
						y =1 ;
					}
				
					
				}
				else if (packet.get(2).equalsIgnoreCase("4g") ) {
					if (y==0)
					{
						y =1 ;
					}
					else if(y>3) {
						y=y-1;
					}
					
				}
				packet.set(10,Integer.toString(y));
				fileLogger.debug("@ value of ta"+y);
				if(packet.get(14).toLowerCase().contains("na")){
					String appliedAntenna="";
					try {
						appliedAntenna=operations.getJson("select applied_antenna from running_mode where mode_type='track'").getJSONObject(0).getString("applied_antenna");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					int aType=getAtypeFromAntenna(appliedAntenna);
					
					
					
					
					String[] distanceArr=getDistanceFromTAAndRxl(packet.get(2),packet.get(15),aType,Integer.parseInt(packet.get(10)),rxlValid);
					distance=Double.parseDouble(distanceArr[0]);
					calcBasic=distanceArr[1];
					fileLogger.debug("@distance from rxl or ta :"+distance);
				}else{
					//distance=getDIstanceFromLatLon(packet.get(14));
					String[] latLon=packet.get(14).split(",");
					double msLat=Double.parseDouble(latLon[0]);
					double msLon=Double.parseDouble(latLon[1]);
					distance=distanceFromLatLon(selfLat,selfLon,msLat,msLon,"K")*1000;
					calcBasic="GPS";
					fileLogger.debug("@distance from lat lon :"+distance);
				}
				distance=Math.round(distance*100.0)/100.0;
				
				int ulrfcn = Integer.parseInt(packet.get(17));
				double freq = new OperationCalculations().calulateFreqFromArfcn(ulrfcn,packet.get(2).toString());
				double ulFrequency=getULFrequencyFromDL(freq);
				ulFrequency=Math.floor(ulFrequency*10.0)/10.0;
				String frequency=Double.toString(ulFrequency);
				String eventInitType="";
				fileLogger.debug("@cdrlogevent currentEventName is :"+currentEventName);
				if(currentEventName.equalsIgnoreCase(Constants.automaticEvent)){
					eventInitType=Constants.automaticEvent;
				}else if(currentEventName.equalsIgnoreCase(Constants.schedulerEvent)){
					eventInitType=Constants.schedulerEvent;
				}else if(currentEventName.equalsIgnoreCase("trgl")){
					eventInitType="hummer";
				}else if(currentEventName.equalsIgnoreCase("ugs")){
					eventInitType="oxfam";
				}else{
					eventInitType="manual";
				}
				
				String cueId="";
				if(eventInitType.equals(Constants.automaticEvent) || eventInitType.equals(Constants.schedulerEvent) || eventInitType.equals("manual")){
					cueId=transId;	
				}else{
					cueId=CurrentOperationType.getCueId();
				}
				
				JSONArray angleOffsetArr = getJson("select angle_offset from antenna where atype='1' limit 1");
				int angleOffset=0;
				try {
					if(angleOffsetArr.length()!=0){
						angleOffset = angleOffsetArr.getJSONObject(0).getInt("angle_offset");
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				String probMsloc="";
				String sector=Integer.toString(new CommonService().getSectorFromAntennaId(antId));
				int cmsSectorAngle=0;
				if(sector.equalsIgnoreCase("1")){
					cmsSectorAngle=30;
				}else if(sector.equalsIgnoreCase("2")){
					cmsSectorAngle=90;
				}else if(sector.equalsIgnoreCase("3")){
					cmsSectorAngle=150;
				}else{
					cmsSectorAngle=210;
				}
				fileLogger.debug("@sector Angle :"+cmsSectorAngle);
				if(packet.get(14).toLowerCase().indexOf("na")!=-1){
					fileLogger.debug("@@setgenmeas 3g ,about to get probable lat,long");
				probMsloc=Common.calulateLatLongAtGivenAngleAndDistance(selfLat, selfLon, angleOffset+cmsSectorAngle, distance);
				}
				
				if(eventInitType.equalsIgnoreCase("oxfam") || eventInitType.equalsIgnoreCase("hummer")){
					showCueStatus=true;
				}else{
					//if(targgetType.equalsIgnoreCase("blacklist") && packet.get(20).equalsIgnoreCase("success")){
					if(targgetType.equalsIgnoreCase("blacklist") ){
						showCueStatus=true;
					}
				}
				for(int i=0;i<packet.size();i++)
				{
					if(i==0)
						packetString.append("'"+packet.get(i)+"'");
					else
						packetString.append(",'"+packet.get(i)+"'");			
				}
				String query = "INSERT INTO cdrlogs(ip, count, stype,packet_type, imsi, imei, msisdn, ptmsi, tmsi, ol, ta, rxl, cgi, sysloc, msloc, band, ulrfcn, dlarfcn, outpow, tstmp, ftyp,psc,ant_id,traget_type,trans_id,trigger_source,freq,target_name,cue_id,distance,calc_basis,prob_msloc,self_loc,off_angle,show_cue_st,gpserror,actta)"
								+ "values("+packetString.toString()+","+antId+",'"+targgetType+"','"+transId+"','"+eventInitType+"','"+frequency+"','"+targetName+"','"+cueId+"',"+distance+",'"+calcBasic+"','"+probMsloc+"','"+selfLoc+"',"+angleOffset+","+showCueStatus+",'" +gpsError+"','" +actta+"') returning id";
				//fileLogger.debug("The querrrry1 is ==  "+query);
				int id = co.executeQueryAndReturnId(query);
				LinkedHashMap<String,String> log = null;
				JSONObject cdrRestData = null;
				try {
					JSONObject  antennaObject = operations.getJson("select angle_covered,profile_name from antenna where id="+antId).getJSONObject(0);
					String angleCovered = antennaObject.getString("angle_covered");
				    String profileName = antennaObject.getString("profile_name");
					String cdrQuery="select *,date_trunc('second',logtime + '05:30:00'::interval) logtime1 from cdroprreport where id="+id+"";
					cdrRestData = getJson(cdrQuery).getJSONObject(0);
					  JSONObject cdrEventData = new JSONObject();
					  try {
						  String cdrLogtime=cdrRestData.getString("logtime1");
						  //cdrLogtime=cdrLogtime.split(".")[0];
						  cdrEventData.put("insert_time",cdrLogtime);
						  cdrEventData.put("trigger_source",eventInitType);
						  cdrEventData.put("operator",cdrRestData.getString("oprname"));
						  cdrEventData.put("country",cdrRestData.getString("country"));
						  cdrEventData.put("imsi",cdrRestData.getString("imsi"));
						  cdrEventData.put("imei",cdrRestData.getString("imei"));
						  cdrEventData.put("ta",cdrRestData.getString("ta"));
						  cdrEventData.put("rxl",cdrRestData.getString("realrxl"));
						  cdrEventData.put("dlarfcn",cdrRestData.getString("dlarfcn"));
						  cdrEventData.put("ulrfcn",cdrRestData.getString("ulrfcn"));
						  cdrEventData.put("stype",packet.get(2));
						  cdrEventData.put("band",packet.get(15));
						  cdrEventData.put("freq",frequency);
						  cdrEventData.put("cgi",cdrRestData.getString("cgi"));
						  cdrEventData.put("target_type",targgetType);
						  cdrEventData.put("target_name",targetName);
						  cdrEventData.put("profile_name", profileName);
						  cdrEventData.put("msloc", cdrRestData.getString("msloc"));
						  cdrEventData.put("trans_id",transId);
						  cdrEventData.put("cueId",cueId);
						  cdrEventData.put("distance",Double.toString(distance));
						  cdrEventData.put("calc_basis",calcBasic);
						  cdrEventData.put("prob_msloc",probMsloc);
						  cdrEventData.put("self_loc",selfLoc);
						  cdrEventData.put("show_cue_st",Boolean.toString(showCueStatus));
						  new SocketServer().sendText(cdrEventData.toString());
					} catch (JSONException e) {
						fileLogger.error("exception is :"+e.getMessage());
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					int systemTypeCode=DBDataService.getSystemType();
//					String sector=Integer.toString(new CommonService().getSectorFromAntennaId(antId));
//					int cmsSectorAngle=0;
//					if(sector.equalsIgnoreCase("S1")){
//						cmsSectorAngle=30;
//					}else if(sector.equalsIgnoreCase("S2")){
//						cmsSectorAngle=90;
//					}else if(sector.equalsIgnoreCase("S3")){
//						cmsSectorAngle=150;
//					}else{
//						cmsSectorAngle=210;
//					}
					JSONArray CMSArray = new Operations().getJson("select * from view_btsinfo where code = 11");
					if(CMSArray.length()>0){
						if(targgetType.equalsIgnoreCase("blacklist") || targgetType.equalsIgnoreCase("other")){
						fileLogger.debug("@cms before sending cms trigger");
						String latLon = cdrRestData.getString("msloc");
						String cdrLat="";
						String cdrLon="";
						if(latLon!=null && latLon.toLowerCase().contains("na")){
							cdrLat="-1.1";
							cdrLon="-1.1";
						}else{
							String[] latLonArray=latLon.split(",");
							cdrLat=latLonArray[0];
							cdrLon=latLonArray[1];
						}
						
						String cdrDeviceIp=Common.getSystemIPAddress();
						SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						//JSONArray cdrArr=operations.getJson("select *,now() from cdrlogs_current where inserttime > timezone('utc',now())-interval '30 second' and imsi ='"+cdrRestData.getString("imsi")+"'");
						//if(cdrArr.length()==0){
						if(systemTypeCode==2){
						if(eventInitType.equalsIgnoreCase("automatic") || eventInitType.equalsIgnoreCase("scheduler") || eventInitType.equalsIgnoreCase("manual")){
							new CMSController(cdrRestData.getString("logtime1"), transId,cdrDeviceIp, cdrRestData.getString("ta"), cdrLat, cdrLon, cdrRestData.getString("imsi"), eventType, cdrRestData.getString("imei"), frequency, cdrRestData.getString("realrxl"),transId,Double.toString(distance),Integer.toString(cmsSectorAngle),sector);
						}else{
							new CMSController(cdrRestData.getString("logtime1"), transId,cdrDeviceIp, cdrRestData.getString("ta"), cdrLat, cdrLon, cdrRestData.getString("imsi"), eventType, cdrRestData.getString("imei"), frequency, cdrRestData.getString("realrxl"),cueId,Double.toString(distance),Integer.toString(cmsSectorAngle),sector);
						}
						}
						fileLogger.debug("@cms after sending cms trigger");

					}
					}
//;					trans_id="falon_123456";
					String hummerQueStatus = operations.getJson("select value from system_properties where key='cue_hummer_to_falcon'").getJSONObject(0).getString("value");
					fileLogger.debug("@Cue Enable/Disable status"+ hummerQueStatus);
					JSONArray hummerDataArray = new Operations().getJson("select * from view_btsinfo where code = 12");
					//currentEventName="automatic";
					fileLogger.debug("@Cue Hummer Data"+ hummerDataArray);
					if(hummerDataArray.length()>0){
						fileLogger.debug("Hummmer found for cue in falcon");
						fileLogger.debug("Event Type "+ eventInitType +" Hummer Cue Status"+ hummerQueStatus +" Target Type "+targgetType +"FType"+ packet.get(20));
					if(((eventInitType.equalsIgnoreCase(Constants.automaticEvent) || eventInitType.equalsIgnoreCase(Constants.schedulerEvent) || eventInitType.equalsIgnoreCase("manual") || eventInitType.equalsIgnoreCase("oxfam")) &&  hummerQueStatus.equalsIgnoreCase("enable")) && targgetType.equalsIgnoreCase("blacklist") && packet.get(20).equalsIgnoreCase("success")){
						fileLogger.debug("InSide sending hummer Cue");
						fileLogger.debug("This sends the cue or not depends on the true or false of Falcon Hummer Cue Status");
						String bandwidth="";
						packTech=packet.get(2);
						bandwidth="5";
						if(packTech.equalsIgnoreCase("2g")){
							bandwidth="0.2";
						}else if(packTech.equalsIgnoreCase("3g"))
						{
							bandwidth="5";
						}
						else if(packTech.equalsIgnoreCase("4g"))
						{
							bandwidth="20";
						}
						String timeout = "";
						String validity = "";
						JSONArray eventParamArray=operations.getJson("select key,value from system_properties where key in('timeout','validity')");
						for(int configCount=0;configCount<eventParamArray.length();configCount++){
							JSONObject eventParam = eventParamArray.getJSONObject(configCount);
						if(eventParam.getString("key").equalsIgnoreCase("timeout")){
							timeout=eventParam.getString("value");
						}else{
							validity=eventParam.getString("value");
						}
						}
						fileLogger.debug("Cue TimeOut "+ timeout +" validity "+ validity);
						if(systemTypeCode==2){
						if(eventInitType.equalsIgnoreCase("ugs")){
							new TRGLController(cdrRestData.getString("logtime1"),transId,cdrRestData.getString("ip"),frequency,angleCovered,sector,bandwidth,timeout,validity,cueId);
							dbDataService.storeTriggerOnDb(new Date(),cueId,"Falcon","Sent Hummer,Cue ID:"+cueId+",Frequency:"+frequency+",Angle:"+angleCovered+",Sector:"+sector+",Bandwidth:"+bandwidth+",Timeout:"+timeout+",Validity:"+validity);
							new TriggerCueServer().sendText("event");
						}else{
							new TRGLController(cdrRestData.getString("logtime1"),transId,cdrRestData.getString("ip"),frequency,angleCovered,sector,bandwidth,timeout,validity,transId);
							dbDataService.storeTriggerOnDb(new Date(),cueId,"Falcon","Sent Hummer,Cue ID:"+transId+",Frequency:"+frequency+",Angle:"+angleCovered+",Sector:"+sector+",Bandwidth:"+bandwidth+",Timeout:"+timeout+",Validity:"+validity);
							new TriggerCueServer().sendText("event");
						}
						}
						LinkedHashMap<String,String> queLog = new LinkedHashMap<String,String>();
				   		queLog.put("action", "Sent");
				   		queLog.put("source", "Falcon");
				   		queLog.put("trans_id",transId);
						queLog.put("timeout",timeout);
						queLog.put("validity", validity);
			   			//new AuditHandler().auditLog(log, 3);
			   			new AuditHandler().audit_que(queLog);
					}
					}
					log = new LinkedHashMap<String, String>();
					log.put("mobile", "CDR");
					log.put("target type", targgetType);
					//log.put("name", targetName);
					log.put("imsi", packet.get(4));
					log.put("imei", packet.get(5));
					log.put("freq",targgetType);
					log.put("operator_home", cdrRestData.getString("oprname"));
					log.put("operator_visitor", cdrRestData.getString("c_opr"));
					log.put("cgi", cdrRestData.getString("cgi"));
					log.put("dbm", cdrRestData.getString("realrxl"));
					log.put("ta", cdrRestData.getString("ta"));
					log.put("location", cdrRestData.getString("msloc"));
					log.put("antenna",profileName);
				} catch (Exception e) 
				{
					fileLogger.error("@cdrcms exception");
					fileLogger.error("exception is :"+e.getMessage());
					e.printStackTrace();
					// TODO: handle exception
				}
				new AuditHandler().auditLog(log, 5);
			
			}	
			}
			}
			 fileLogger.info("Exit Function : insertCDRData");
			return "";
		}
		
		public String insertGeoLocData(HttpServletRequest req)
		{		
				String data =req.getParameter("data");
				Common co = new Common();				
				String query = "INSERT INTO geolocdata(msisdn, imsi, imei, hmcc, hmnc, mcc, mnc, lac, cell, state,coordinate, hlr, vlr, ftn, logtime,mscaddr)"+
								"values("+data+")";		
				fileLogger.debug(query);
				co.executeDLOperation(query);			
				return "";
		}
		
		
		public JSONArray getGeoLocdata(HttpServletRequest request)
		{
			String startTime = request.getParameter("startTime");
			String endTime = request.getParameter("endTime");
			String query = "select * from geolocdata where logtime between '"+startTime+"' and '"+endTime+"' order by logtime desc";
			fileLogger.debug(query);
			return getJson(query);			
		}
		
		
		
		/*public JSONArray getGeoLocdata(HttpServletRequest request)
		{
			Common co = new Common();
			Statement smt = null;
			Connection con = co.getDbConnection();
			
			JSONArray ja = new JSONArray();			
			
			
			String startTime = request.getParameter("startTime");
			String endTime = request.getParameter("endTime");
			try
			{
				smt = con.createStatement();			
				
				String query = "select * from geolocdata where logtime between '"+startTime+"' and '"+endTime+"' order by logtime desc";
				
				fileLogger.debug(query);
				
				ResultSet rs = smt.executeQuery(query);
				ResultSetMetaData rm = rs.getMetaData();
				int totalColumns = rm.getColumnCount();
				ArrayList<String> columns = new ArrayList<String>();
				
				for(int i=1;i<=totalColumns;i++ )
				{
					columns.add(rm.getColumnName(i));
				}
				
				while(rs.next())
				{				
					JSONObject jb = new JSONObject();
					for(String cname:columns)
					{
						jb.put(cname, rs.getString(cname));
					}
					ja.put(jb);
				}			
			}
			catch(Exception E)
			{
				fileLogger.debug("Exception while authenticating the user "+E.getMessage());
			}
			finally
			{
				try
				{
					smt.close();
					con.close();
				}
				catch(Exception E)
				{
					
				}
			}	
			return ja;
		}*/
		
		 
		public JSONArray getGeoLocdataLocal(HttpServletRequest request)
		{
			String startTime = request.getParameter("startTime");
			String endTime = request.getParameter("endTime");
			String id = request.getParameter("id");
			//String query = "select * from geolocdata where logtime between '"+startTime+"' and '"+endTime+"' order by logtime desc";
			String query = "select * from geolocdata where id="+id;
			fileLogger.debug(query);
			return getJson(query);			
		}
		
		/*public JSONArray getGeoLocdataLocal(HttpServletRequest request)
		{
			Common co = new Common();
			Statement smt = null;
			Connection con = co.getDbConnection();
			
			JSONArray ja = new JSONArray();			
			
			
			String id = request.getParameter("id");
			try
			{
				smt = con.createStatement();			
				String query = "select * from geolocdata where id="+id;
				fileLogger.debug(query);
				ResultSet rs = smt.executeQuery(query);
				ResultSetMetaData rm = rs.getMetaData();
				int totalColumns = rm.getColumnCount();
				ArrayList<String> columns = new ArrayList<String>();
				
				for(int i=1;i<=totalColumns;i++ )
				{
					columns.add(rm.getColumnName(i));
				}
				
				while(rs.next())
				{				
					JSONObject jb = new JSONObject();
					for(String cname:columns)
					{
						jb.put(cname, rs.getString(cname));
					}
					ja.put(jb);
				}			
			}
			catch(Exception E)
			{
				fileLogger.debug("Exception while authenticating the user "+E.getMessage());
			}
			finally
			{
				try
				{
					smt.close();
					con.close();
				}
				catch(Exception E)
				{
					
				}
			}	
			return ja;
		}*/
		
		public String spectrumData(HttpServletRequest request)
		{	
			String data1 = request.getParameter("data");			
			String ip = request.getParameter("ip");
			String count = request.getParameter("count");
		
			fileLogger.debug(data1);
			fileLogger.debug(ip);
			fileLogger.debug(count);
		
			
			String [] bb = null;
			String [] lineFormat = null;
			//String data1 = "1,2,3,4,\n,1,2,3,4,";	
			lineFormat = data1.toString().split("\\n");
				
				//for(String lineData:lineFormat)
			for(int i=0;i<lineFormat.length;i++)
			{
					
				
			String data = lineFormat[i];
			
			String[] pack = formtaSpecturmDataPacket(data,ip,count);
			ArrayList<String> serverData = new ArrayList<String>();
			serverData.add(ip);
			serverData.add(count);			
			/*for(String s:pack)
			{
				serverData.add(s);
			}*/
			for(int j=0;j<pack.length-1;j++)
			{
				if(j != 2)
					serverData.add(pack[j]);
				else
				{
					//String[] time = packet[j].trim().split(":");						
					String dateTime = pack[j].trim()+"_"+pack[j+1].trim();
					dateTime=dateTimeFormaterWids(dateTime);
					serverData.add(dateTime);
				}
								
			}
			new SpectrumSocketServer().sendText(serverData);
			insertSpecturmDataData(pack,ip,count);
			
			}
			return "";
		}		
		
		
		public String[] formtaSpecturmDataPacket(String dataPacket,String ip,String count)
		{
			 fileLogger.info("Inside Function : formtaSpecturmDataPacket");
			String aa = dataPacket;
			
			fileLogger.debug("\n********data formated specturmData **********\n "+aa);
			String [] bb = null;
			
			try
			{	
				fileLogger.debug(aa);
				bb = aa.toString().split(",");				
			}
			catch(Exception E)
			{
				fileLogger.debug(E.getMessage());
			}
			 fileLogger.info("Exit Function : formtaSpecturmDataPacket");
			return bb;
		}
		
		public String insertSpecturmDataData(String[] packet,String ip,String count)
		{
			if(packet.length>0)
			{
				StringBuilder packetString = new StringBuilder();
				
				for(int i=0;i<packet.length-1;i++)
				{
					if(i==0)
						packetString.append("'"+packet[i].trim()+"'");
					else if(i==2)
					{
						String[] time = packet[i+1].trim().split(":");						
						String dateTime = packet[i].trim()+"_"+packet[i+1].trim();
						dateTime=dateTimeFormaterWids(dateTime);
						packetString.append(",'"+dateTime+"'");
					}
					else
					{
						
						packetString.append(",'"+packet[i].trim()+"'");
					}
									
				}
				Common co = new Common();				
				//String query = "INSERT INTO cdrlogs(ip, count, packet_type, imsi, imei, msisdn, ptmsi, tmsi,ol, nl, cid, ta, rxl, tstmp, ftyp, inserttime)"
				//		+ "values('"+ip+"','"+count+"',"+packetString.toString()+")";
				String query = "INSERT INTO wsdiData(ip,count,freq,power,tstmp)"
								+ "values('"+ip+"','"+count+"',"+packetString.toString()+")";
				co.executeDLOperation(query);
			}
			return "";
		}
		
		
		public String gpsData(HttpServletRequest request)
		{	
			 fileLogger.info("Inside Function : gpsData");
			int gpsNode=DBDataService.getGpsNode();
			int systemMode=DBDataService.getSystemMode();
			fileLogger.debug("@gpsdata systemMode is :"+systemMode);
			try {
				new Common().executeDLOperation("update gpsdata set logtime =timezone('utc',now()) where id in (select max(id) from gpsdata)");
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(gpsNode==1 || gpsNode==2){ //0 denotes to System manager,1 denotes to STRU and 2 denotes to Hummer
				fileLogger.debug("discarded as gpsNode is STRU or Hummer");
				return "";
			}
			String data = request.getParameter("data");
			
			String ip = request.getParameter("ip");
			String count = request.getParameter("count");
			//fileLogger.debug("###############################GPS dat###########################");
			fileLogger.debug(data);
			fileLogger.debug(ip);
			fileLogger.debug(count);
			//fileLogger.debug("###############################GPS dat###########################");
			new Common().auditLog(new Common().convertToMapFor2g(ip,data,"R")); 
			
			String[] pack = formatGPSDataPacket(data,ip,count);
			if(pack[2].charAt(0)=='0' || pack[4].charAt(0)=='0'){
				fileLogger.debug("invalid gps coordinates from system manager");
				return "";
			}
			ArrayList<String> serverData = new ArrayList<String>();
			serverData.add(ip);
			serverData.add(count);
			
			for(String s:pack)
			{
				serverData.add(s);
			}
				JSONArray lastGpsArr=getJson("select lat,lon from gpsdata order by logtime desc limit 1");
				double oldLat=0.00;
				double oldLon=0.00;
				if(lastGpsArr.length()!=0){
					try {
						JSONObject lastGpsObject = lastGpsArr.getJSONObject(0);
						oldLat=lastGpsObject.getDouble("lat");
						oldLon=lastGpsObject.getDouble("lon");
						fileLogger.debug("@gpsdata oldLat is :"+oldLat+",oldLon is :"+oldLon);
					} catch (JSONException e) {
						fileLogger.debug("exception in gpsData message:"+e.getMessage());
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				double newLat=Double.parseDouble(pack[2]);
				double newLon=Double.parseDouble(pack[4]);
				fileLogger.debug("@gpsdata newLat is :"+newLat+",newLon is :"+newLon);
				
				double distance = distanceFromLatLon(oldLat, oldLon, newLat, newLon, "K")*1000;
				distance = Math.round(distance*100.0)/100.0;
				double gpsAccuracy=0.00;
				JSONArray gpsAccuracyArr = getJson("select accuracy from gps_accuracy order by id desc limit 1");
				if(gpsAccuracyArr.length()!=0){
					try {
						gpsAccuracy = gpsAccuracyArr.getJSONObject(0).getDouble("accuracy");
						new Common().executeDLOperation("update gpsdata set logtime =timezone('utc',now()) where id in (select max(id) from gpsdata)");
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				if(systemMode==0){	
					if(distance>gpsAccuracy){
					/*int bearing=Common.calcBearingBetweenTwoGpsLoc(oldLat,oldLon,newLat,newLon);
					if(DBDataService.getAntennaToVehicleDiffAngle()>360){
						int antennaToVehicleDiffAngle = bearing-angleOffset;
						DBDataService.setAntennaToVehicleDiffAngle(antennaToVehicleDiffAngle);
					}
					angleOffset=Common.calcNewAngleOffset(bearing);
					
					fileLogger.debug("@gpsdata Operations bearing is :"+bearing);
					new Common().executeDLOperation("update antenna set angle_offset="+angleOffset+" where atype='1'");*/
					serverData.add("stationary");
					serverData.add("NA");
					new GPSSocketServer().sendText(serverData);
					insertGPSData(pack,ip,count);
					return "";
					}
				}else{
					if(distance>gpsAccuracy){
						int angleOffset=0;
						JSONArray angleOffsetArr = getJson("select angle_offset from antenna where atype='1' limit 1");
						if(angleOffsetArr.length()!=0){
							try {
								angleOffset = angleOffsetArr.getJSONObject(0).getInt("angle_offset");
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					int bearing=Common.calcBearingBetweenTwoGpsLoc(oldLat,oldLon,newLat,newLon);
					if(DBDataService.getAntennaToVehicleDiffAngle()>360){
						int antennaToVehicleDiffAngle = bearing-angleOffset;
						DBDataService.setAntennaToVehicleDiffAngle(antennaToVehicleDiffAngle);
					}
					angleOffset=Common.calcNewAngleOffset(bearing);
					
					fileLogger.debug("@gpsdata operations bearing is :"+bearing);
					new Common().executeDLOperation("update antenna set angle_offset="+angleOffset+" where atype='1'");
					DBDataService.setAngleOffset(angleOffset);
					serverData.add("moving");
					serverData.add(Integer.toString(angleOffset));
					new GPSSocketServer().sendText(serverData);
					insertGPSData(pack,ip,count);
					return "";
					}
				}

				}else{
					if(systemMode==0){
						serverData.add("stationary");
					}else{
						serverData.add("moving");
					}
					serverData.add("NA");
					new GPSSocketServer().sendText(serverData);
					insertGPSData(pack,ip,count);
					return "";
				}
				 fileLogger.info("Exit Function : gpsData");
			return "";
		}
		
		public String[] formatGPSDataPacket(String dataPacket,String ip,String count)
		{
			 fileLogger.info("Inside Function : formatGPSDataPacket");
			String aa = dataPacket;			
			fileLogger.debug("\n********data formated GPS **********\n "+aa);
			String [] bb = null;
			
			try
			{	
				fileLogger.debug(aa);
				bb = aa.toString().split("\\$");			
			}
			catch(Exception E)
			{
				fileLogger.debug(E.getMessage());
			}
			 fileLogger.info("Exit Function : formatGPSDataPacket");
			return bb;
		}
		
		public String insertGPSData(String[] packet,String ip,String count)
		{
			if(packet.length>0)
			{
				StringBuilder packetString = new StringBuilder();		
				
				for(int i=0;i<packet.length;i++)
				{
					if(i==0)
						packetString.append("'"+packet[i]+"'");
					else
						packetString.append(",'"+packet[i]+"'");			
				}
				Common co = new Common();				
				//String query = "INSERT INTO cdrlogs(ip, count, packet_type, imsi, imei, msisdn, ptmsi, tmsi,ol, nl, cid, ta, rxl, tstmp, ftyp, inserttime)"
				//		+ "values('"+ip+"','"+count+"',"+packetString.toString()+")";
				String query = "INSERT INTO gpsData(ip, count, tstmp, s_status, lat, lato, lon, lono, speed,course, satellites, elev, eunit, roll, pres, acc, comp, temp1,temp2, gyx, gyy, gyz, tiltx, tilty, tiltz)"
						+ "values('"+ip+"','"+count+"',"+packetString.toString()+")";
				co.executeDLOperation(query);
			}
			return "";
		}
		
		public JSONArray getGPSData(HttpServletRequest request)
		{
			String startTime = request.getParameter("startTime");
			String endTime = request.getParameter("endTime");
			String query = "select * from gpsData where to_timestamp(tstmp,'DDMMYY-HH24-MI-SS')::timestamp without time zone between '"+startTime+"' and '"+endTime+"' and lat::numeric != 0 and lon::numeric !=0 and lat != '' and lon != '' order by logtime desc";
			fileLogger.debug(query);
			return getJson(query);			
		}
		
		
		/*public JSONArray getGPSData(HttpServletRequest request)
		{
			Common co = new Common();
			Statement smt = null;
			Connection con = co.getDbConnection();
			
			JSONArray ja = new JSONArray();
			
			
			
			String startTime = request.getParameter("startTime");
			String endTime = request.getParameter("endTime");
			try
			{
				smt = con.createStatement();			
				String query = "select * from gpsData where logtime between '"+startTime+"' and '"+endTime+"' and lat != '-0.000000' and lon != '-0.000000' and lat != '' and lon != '' order by logtime desc";
				fileLogger.debug(query);
				ResultSet rs = smt.executeQuery(query);
				ResultSetMetaData rm = rs.getMetaData();
				int totalColumns = rm.getColumnCount();
				ArrayList<String> columns = new ArrayList<String>();
				
				for(int i=1;i<=totalColumns;i++ )
				{
					columns.add(rm.getColumnName(i));
				}
				
				while(rs.next())
				{				
					JSONObject jb = new JSONObject();
					for(String cname:columns)
					{
						jb.put(cname, rs.getString(cname));
					}
					ja.put(jb);
				}			
			}
			catch(Exception E)
			{
				fileLogger.debug("Exception while authenticating the user "+E.getMessage());
			}
			finally
			{
				try
				{
					smt.close();
					con.close();
				}
				catch(Exception E)
				{
					
				}
			}	
			return ja;
		}*/
		public JSONArray getCDRGPSData(HttpServletRequest request)
		{
			String startTime = request.getParameter("startTime");
			String endTime = request.getParameter("endTime");
			String query = "select stype,ip,count,packet_type,imsi,imei,ta,rxl,cgi,sysloc,band,ulrfcn,dlarfcn,outpow,tstmp,inserttime,lat,lon,mobile_type,psc,angle,anglet from cdrGpsData where logtime between '"+startTime+"' and '"+endTime+"' order by logtime desc";
			fileLogger.debug(query);
			return getJson(query);			
		}
		
		/*public JSONArray getCDRGPSData(HttpServletRequest request)
		{
			Common co = new Common();
			Statement smt = null;
			Connection con = co.getDbConnection();
			
			JSONArray ja = new JSONArray();
			
			
			
			String startTime = request.getParameter("startTime");
			String endTime = request.getParameter("endTime");
			try
			{
				smt = con.createStatement();			
				String query = "select stype,ip,count,packet_type,imsi,imei,ta,rxl,cgi,sysloc,band,ulrfcn,dlarfcn,outpow,tstmp,inserttime,lat,lon,mobile_type from cdrGpsData where logtime between '"+startTime+"' and '"+endTime+"' order by logtime desc";
				fileLogger.debug(query);
				ResultSet rs = smt.executeQuery(query);
				ResultSetMetaData rm = rs.getMetaData();
				int totalColumns = rm.getColumnCount();
				ArrayList<String> columns = new ArrayList<String>();
				
				for(int i=1;i<=totalColumns;i++ )
				{
					columns.add(rm.getColumnName(i));
				}
				
				while(rs.next())
				{				
					JSONObject jb = new JSONObject();
					for(String cname:columns)
					{
						
						jb.put(cname, rs.getString(cname));
					}
					ja.put(jb);
				}			
			}
			catch(Exception E)
			{
				fileLogger.debug("Exception while authenticating the user "+E.getMessage());
			}
			finally
			{
				try
				{
					smt.close();
					con.close();
				}
				catch(Exception E)
				{
					
				}
			}	
			return ja;
		}*/
		
		public String dateTimeFormaterWids(String dateTime)
		{
			 fileLogger.info("Inside Function : dateTimeFormaterWids");
			try
			{	
				/*String[] miliSeconds = dateTime.split("\\.");
				if(miliSeconds[1].length() >=3)
				{
					miliSeconds[1] = miliSeconds[1].substring(0, 2);
				}
				else if(miliSeconds[1].length() ==1 )
				{
					miliSeconds[1] = "0"+miliSeconds[1];
				}*/
				
				Date date = new SimpleDateFormat("dd/MM/yyyy_HH:mm:ss").parse(dateTime);				
				dateTime = new SimpleDateFormat("ddMMyy-HH-mm-ss").format(date);
				//dateTime = dateTime+"."+miliSeconds[1];
			}
			catch(Exception E)
			{
				fileLogger.debug(E.getMessage());
			}
			 fileLogger.info("Exit Function : dateTimeFormaterWids");
			
			
			return dateTime;
		}
		
		public boolean truncateDb(HttpServletRequest request)
		{
			 fileLogger.info("Inside Function : truncateDb");
			String dataTables=request.getParameter("dataTables");
			fileLogger.debug("truncated table at :"+new Date()+" is :"+dataTables);
			String[] dataTableArray=dataTables.split(",");
			List<String> queries =  new ArrayList<String>();
			for(String dataTable:dataTableArray){
				queries.add("truncate "+dataTable);
			}
			Common co = new Common();
			//co.executeDLOperation(query);
			co.executeBatchOperation(queries);
			fileLogger.info("Exit Function : truncateDb");
			return true;
		}
		
		
		public boolean truncateDbOpr(HttpServletRequest request)
		{
			String query = "truncate cdrlogs;";
			List<String> queries =  new ArrayList<String>();
			queries.add("delete from oprrationdata");
			queries.add("delete from btsoprparma");;
			Common co = new Common();
			//co.executeDLOperation(query);
			co.executeBatchOperation(queries);
			return true;
		}
		
		public boolean uploadCSV(HttpServletRequest request,HttpServletResponse res)
		{
			fileLogger.info("Inside Function : uploadCSV");
			res.setContentType("text/html");
			int uploadSuccess = 0;
			
				 try
				 {
					 	PrintWriter out = res.getWriter();
					 	Random randomGenerator = new Random();
						
						String tempUploadFolderName = ""+System.currentTimeMillis();
						
						String path=null;
						
						String absolutePath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
						absolutePath = absolutePath.substring(0,absolutePath.lastIndexOf("WEB-INF"));
						path = absolutePath+ "/tempUploadFolder/";
						
						String childDirectoryPath = path + "\\" + tempUploadFolderName;
						
					//	fileLogger.debug("\n------------------------------------------------\n");
						fileLogger.debug(" temporary folder path temp : "+path);
					//	fileLogger.debug("\n------------------------------------------------\n");
						
						//creating the directory if not exist
						checkOrCreateParentChildDirectory_1(path,tempUploadFolderName);			
						
						///fileLogger.debug("\n------------------------------------------------\n");
						fileLogger.debug(" checkOrCreateParentChildDirectory method execution done 1");
					//.debug("\n------------------------------------------------\n");
						
						
						//uploading the file file upload limit is 5mb
						MultipartRequest multi = new MultipartRequest(request,childDirectoryPath,5000000);
						
						//fileLogger.debug("\n------------------------------------------------\n");
						fileLogger.debug(" uploading the file file upload limit is 5mb done 2");
						//fileLogger.debug("\n------------------------------------------------\n");
						Enumeration files = multi.getFileNames();						
						while (files.hasMoreElements())
						{
							//getting the file details
							String name = (String)files.nextElement();						
							String filename = multi.getFilesystemName(name);
							String type = multi.getContentType(name);
							//getting the file object
							File f = multi.getFile(name);
							saveCsvData(f);
						}
						uploadSuccess = 1;
				 }
				 catch(Exception E)
				 {
					 uploadSuccess=0;
					 fileLogger.debug(E.getMessage());
					 
				 }
				 finally
				 {
					 try
					 {
						 
						 res.sendRedirect(String.format("%s%s", request.getContextPath(), "/views/administration.jsp?up="+uploadSuccess));
					 }
					 catch(Exception E)
					 {
						 fileLogger.debug(E.getMessage());
					 }
					 
				 }
				 fileLogger.info("Exit Function : uploadCSV");
			return true;
		}

		public void checkOrCreateParentChildDirectory(String parentPath,String childDirectoryName)
		{
		
			File file=new File(parentPath);
			
			if(!file.exists())
			{
				file.mkdir();
			}

			String childPath = parentPath + "/" + childDirectoryName;

			File file1 = new File(childPath);

			if (!file1.exists()){
					file1.mkdir();
			  }

			
			/*String mobilePhotos = childPath + "\\" + "photos";
			File file2 = new File(mobilePhotos);
			if (!file2.exists()){
					file2.mkdir();
			  }*/
			
		}
		
		public void checkOrCreateParentChildDirectory_1(String parentPath,String childDirectoryName)
		{
		
			File file=new File(parentPath);
			
			if(!file.exists())
			{
				file.mkdir();
			}

			String childPath = parentPath + "\\" + childDirectoryName;

			File file1 = new File(childPath);

			if (!file1.exists()){
					file1.mkdir();
			  }

			
			/*String mobilePhotos = childPath + "\\" + "photos";
			File file2 = new File(mobilePhotos);
			if (!file2.exists()){
					file2.mkdir();
			  }*/
			
		}
		
		public static void saveCsvData(File csvFile) 
		{
	       
			BufferedReader br = null;
	        
			String line = "";
	        String cvsSplitBy = ",";

	        try 
	        {

	            
	        	br = new BufferedReader(new FileReader(csvFile));
	            int count=0;
	        	while ((line = br.readLine()) != null) 
	            {

	                if(count !=0)
	                {
	                		String[] data = line.split(cvsSplitBy);
		                	Common co = new Common();
		                	for(int i =0;i<data.length;i++) 
		                	{
		                		if(data[i].equalsIgnoreCase("")) 
		                		{
		                			data[i]="null";
		                		}
		                	}
		    				
		                	String query = "INSERT INTO mobiletype(imsi, imei, mobile_type) values("+data[0]+","+data[1]+",'"+data[2]+"')";
		    				
		    				co.executeDLOperation(query);
		    				
	                }
	                count ++;
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            if (br != null) {
	                try {
	                    br.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }

	    }
		
		
		/*public String getQueryForFileUpload(String type,String data[]) 
		{
			String query = "select 1";
			switch(Integer.parseInt(type)) 
			{
			
			case 1:
				query = "INSERT INTO mobiletype(imsi, imei, mobile_type) values("+data[0]+","+data[1]+",'"+data[2]+"')";
			break;
			case 2:
				query = "INSERT INTO mobiletype(imsi, imei, mobile_type) values("+data[0]+","+data[1]+",'"+data[2]+"')";
			break;
			}
		}*/
		
		public JSONArray getMobileType(HttpServletRequest request)
		{
			String startTime = request.getParameter("startTime");
			String endTime = request.getParameter("endTime");
			//String query = "select * from cdroprreport where logtime between '"+startTime+"' and '"+endTime+"'order by logtime desc";
			String query = "select imsi,imei,mobile_type from mobiletype where logtim between '"+startTime+"' and '"+endTime+"' order by logtim desc";
			fileLogger.debug(query);
			return getJson(query);			
		}
		
		/*public JSONArray getMobileType(HttpServletRequest request)
		{
			Common co = new Common();
			Statement smt = null;
			Connection con = co.getDbConnection();
			
			JSONArray ja = new JSONArray();
			
			
			
			String startTime = request.getParameter("startTime");
			String endTime = request.getParameter("endTime");
			try
			{
				smt = con.createStatement();			
				String query = "select imsi,imei,mobile_type from mobiletype where logtim between '"+startTime+"' and '"+endTime+"' order by logtim desc";
				fileLogger.debug(query);
				ResultSet rs = smt.executeQuery(query);
				ResultSetMetaData rm = rs.getMetaData();
				int totalColumns = rm.getColumnCount();
				ArrayList<String> columns = new ArrayList<String>();
				
				for(int i=1;i<=totalColumns;i++ )
				{
					columns.add(rm.getColumnName(i));
				}
				
				while(rs.next())
				{				
					JSONObject jb = new JSONObject();
					for(String cname:columns)
					{
						jb.put(cname, rs.getString(cname));
					}
					ja.put(jb);
				}			
			}
			catch(Exception E)
			{
				fileLogger.debug("Exception while authenticating the user "+E.getMessage());
			}
			finally
			{
				try
				{
					smt.close();
					con.close();
				}
				catch(Exception E)
				{
					
				}
			}	
			return ja;
		}*/
		
		public boolean saveCdrOperations(HttpServletRequest request)
		{
			String name = request.getParameter("oname");
			String addr = request.getParameter("oaddr");
			Common co = new Common();
			
			String query = "insert into   oprrationdata(name,loc) values('"+name+"','"+addr+"')";
			co.executeDLOperation(query);
			return true;
			
		}
		
		
		public boolean saveBcdrOperations(HttpServletRequest request)
		{
			String oprid = request.getParameter("baoprname");
			String baopbts = request.getParameter("baopbts");
			String batype = request.getParameter("batype");
			String bagain = request.getParameter("bagain");
			String baheight = request.getParameter("baheight");
			String baelevation = request.getParameter("baelevation");
			String badirection = request.getParameter("badirection");
			String ttype = request.getParameter("ttype");
			
			Common co = new Common();
			
			//String query = "insert into   oprrationdata(name,loc) values('"+name+"','"+addr+"')";
			String query = "INSERT INTO btsoprparma(oprid, btsid, atype, again, aheight, aelevation, adirection,ttype) VALUES ("+oprid+",'"+baopbts+"','"+batype+"','"+bagain+"','"+baheight+"','"+baelevation+"','"+badirection+"','"+ttype+"')";
			co.executeDLOperation(query);
			return true;
			
		}
		
		public JSONArray getLocBTStype(HttpServletRequest request)
		{
			String query = "select * from btstype order by name";
			fileLogger.debug(query);
			return getJson(query);			
		}
		
		/*public JSONArray getLocBTStype(HttpServletRequest request)
		{
			Common co = new Common();
			Statement smt = null;
			Connection con = co.getDbConnection();
			
			JSONArray ja = new JSONArray();
			
			
			
			try
			{
				smt = con.createStatement();			
				String query = "select * from btstype order by name";
				fileLogger.debug(query);
				ResultSet rs = smt.executeQuery(query);
				ResultSetMetaData rm = rs.getMetaData();
				int totalColumns = rm.getColumnCount();
				ArrayList<String> columns = new ArrayList<String>();
				
				for(int i=1;i<=totalColumns;i++ )
				{
					columns.add(rm.getColumnName(i));
				}
				
				while(rs.next())
				{				
					JSONObject jb = new JSONObject();
					for(String cname:columns)
					{
						jb.put(cname, rs.getString(cname));
					}
					ja.put(jb);
				}			
			}
			catch(Exception E)
			{
				fileLogger.debug("Exception while authenticating the user "+E.getMessage());
			}
			finally
			{
				try
				{
					smt.close();
					con.close();
				}
				catch(Exception E)
				{
					
				}
			}	
			return ja;
		}*/

		
		public JSONArray getLocOprations(HttpServletRequest request)
		{
			String query = "select id,name from oprrationdata order by name";
			fileLogger.debug(query);
			return getJson(query);			
		}
		
		
		
		/*public JSONArray getLocOprations(HttpServletRequest request)
		{
			Common co = new Common();
			Statement smt = null;
			Connection con = co.getDbConnection();
			
			JSONArray ja = new JSONArray();
			
			
			
			try
			{
				smt = con.createStatement();			
				String query = "select id,name from oprrationdata order by name";
				fileLogger.debug(query);
				ResultSet rs = smt.executeQuery(query);
				ResultSetMetaData rm = rs.getMetaData();
				int totalColumns = rm.getColumnCount();
				ArrayList<String> columns = new ArrayList<String>();
				
				for(int i=1;i<=totalColumns;i++ )
				{
					columns.add(rm.getColumnName(i));
				}
				
				while(rs.next())
				{				
					JSONObject jb = new JSONObject();
					for(String cname:columns)
					{
						jb.put(cname, rs.getString(cname));
					}
					ja.put(jb);
				}			
			}
			catch(Exception E)
			{
				fileLogger.debug("Exception while authenticating the user "+E.getMessage());
			}
			finally
			{
				try
				{
					smt.close();
					con.close();
				}
				catch(Exception E)
				{
					
				}
			}	
			return ja;
		}*/
		
		
		
		
		public JSONArray getOprReportData(HttpServletRequest request)
		{
			String startTime = request.getParameter("startTime");
			String endTime = request.getParameter("endTime");
			String operationId = request.getParameter("operationId");
			String query="";
			if(operationId.equals("-1")){
			query = "select * from cdroprreport where to_timestamp(tstmp,'DDMMYY-HH24-MI-SS')::timestamp without time zone between '"+startTime+"' and '"+endTime+"' order by to_timestamp(tstmp,'DDMMYY-HH24-MI-SS')::timestamp without time zone desc";
			}else{
			query = "select * from cdroprreport where oprid="+operationId+" and to_timestamp(tstmp,'DDMMYY-HH24-MI-SS')::timestamp without time zone between '"+startTime+"' and '"+endTime+"' order by to_timestamp(tstmp,'DDMMYY-HH24-MI-SS')::timestamp without time zone desc";
			}
			fileLogger.debug(query);
			return getJson(query);			
		}
		
		public JSONArray getOprLogsData(HttpServletRequest request)
		{
			String startTime = request.getParameter("startTime");
			String endTime = request.getParameter("endTime");
			String operationId = request.getParameter("operationId");
			String query="";
			if(operationId.equals("-1")){
			query = "select *,oprname((mcc||mnc)::numeric) opr,getcountry(mcc::numeric) country from oprlogs where to_timestamp(tstmp,'DDMMYY-HH24-MI-SS')::timestamp without time zone between '"+startTime+"' and '"+endTime+"'  and oprlogs.mcc != 'NA' and oprlogs.mnc != 'NA'  order by to_timestamp(tstmp,'DDMMYY-HH24-MI-SS')::timestamp without time zone desc";
			}else{
			query = "select *,oprname((mcc||mnc)::numeric) opr,getcountry(mcc::numeric) country from oprlogs where oprid="+operationId+" and to_timestamp(tstmp,'DDMMYY-HH24-MI-SS')::timestamp without time zone between '"+startTime+"' and '"+endTime+"'  and oprlogs.mcc != 'NA' and oprlogs.mnc != 'NA'  order by to_timestamp(tstmp,'DDMMYY-HH24-MI-SS')::timestamp without time zone desc";
			}
			fileLogger.debug(query);
			return getJson(query);			
		}
		
		
		public JSONArray getWidsReportData(HttpServletRequest request)
		{
			String startTime = request.getParameter("startTime");
			String endTime = request.getParameter("endTime");
			String type = request.getParameter("type");
			String operationId = request.getParameter("operationId");
			String timeSpanClause = "to_timestamp(tstmp,'DDMMYY-HH24-MI-SS')::timestamp without time zone";
			String operationIdClause="";
			if(!operationId.equals("-1")){
			operationIdClause=" and oprid="+operationId;
			}
			String query = "select * from widsoprdata where "+timeSpanClause+" between '"+startTime+"' and '"+endTime+"'"+operationIdClause+" order by "+timeSpanClause+" desc";

			switch(Integer.parseInt(type))
			{
				case 1:
					query="select * from widsoprdata where(freq,ip,power::numeric,DATE_TRUNC('min',"+timeSpanClause+")) in (select distinct freq,ip,max(power::numeric),DATE_TRUNC('min',"+timeSpanClause+") from widsoprdata where ("+timeSpanClause+") between '"+startTime+"' and '"+endTime+"'"+operationIdClause+" group by freq,DATE_TRUNC('min',"+timeSpanClause+"),ip) order by "+timeSpanClause+" asc";
				break;
				case 2:
					query="select * from widsoprdata where(freq,ip,power::numeric,DATE_TRUNC('second',"+timeSpanClause+")) in (select distinct freq,ip,max(power::numeric),DATE_TRUNC('second',"+timeSpanClause+") from widsoprdata where ("+timeSpanClause+") between '"+startTime+"' and '"+endTime+"'"+operationIdClause+" group by freq,DATE_TRUNC('second',"+timeSpanClause+"),ip) order by "+timeSpanClause+" asc";
				break;
			}
			return getJson(query);			
		}
		
		public JSONArray getCurrentActiveOperation(HttpServletRequest request)
		{
			fileLogger.info("Inside Function : getCurrentActiveOperation");
			Common co = new Common();
			Statement smt = null;
			Connection con = co.getDbConnection();
			
			JSONArray ja = new JSONArray();
			
			
			
			try
			{
				smt = con.createStatement();			
				String query = "select oprrationdata.id,btsoprparma.id btsprmid,oprrationdata.name,loc,btstype.name as bts,atype,again,aheight,aelevation,adirection,ttype from oprrationdata inner join btsoprparma on (oprrationdata.id = btsoprparma.oprid) left join btstype on(btsoprparma.btsid = btstype.id) order by btsprmid desc;";
				fileLogger.debug(query);
				ResultSet rs = smt.executeQuery(query);
				ResultSetMetaData rm = rs.getMetaData();
				int totalColumns = rm.getColumnCount();
				ArrayList<String> columns = new ArrayList<String>();
				
				for(int i=1;i<=totalColumns;i++ )
				{
					columns.add(rm.getColumnName(i));
				}
				
				while(rs.next())
				{				
					JSONObject jb = new JSONObject();
					for(String cname:columns)
					{
						jb.put(cname, rs.getString(cname));
					}
					ja.put(jb);
				}			
			}
			catch(Exception E)
			{
				fileLogger.error("Exception while authenticating the user "+E.getMessage());
			}
			finally
			{
				try
				{
					smt.close();
					con.close();
				}
				catch(Exception E)
				{
					
				}
			}	
			fileLogger.info("Exit Function : getCurrentActiveOperation");
			return ja;
		}
		
		
		public JSONArray getJson(String query)
		{
			fileLogger.info("Inside Function : getJson");
			Common co = new Common();
			Statement smt = null;
			Connection con = co.getDbConnection();
			
			JSONArray ja = new JSONArray();
			
			
			
			try
			{
				smt = con.createStatement();			
				//String query = "select oprrationdata.id,btsoprparma.id btsprmid,oprrationdata.name,loc,btstype.name as bts,atype,again,aheight,aelevation,adirection,ttype from oprrationdata inner join btsoprparma on (oprrationdata.id = btsoprparma.oprid) left join btstype on(btsoprparma.btsid = btstype.id) order by btsprmid desc;";
				fileLogger.debug(query);
				ResultSet rs = smt.executeQuery(query);
				ResultSetMetaData rm = rs.getMetaData();
				int totalColumns = rm.getColumnCount();
				ArrayList<String> columns = new ArrayList<String>();
				
				for(int i=1;i<=totalColumns;i++ )
				{
					columns.add(rm.getColumnName(i));
				}
				int count = 0;
				while(rs.next())
				{				
					
					count++;
					if(count >= 100000)
					{
						JSONObject jb = new JSONObject();
						jb.put("result","fail");
						jb.put("msg","Limit exceed");
						ja = null;
						ja = new JSONArray();
						ja.put(jb);
						break;
					}
					
					JSONObject jb = new JSONObject();
					for(String cname:columns)
					{
						if(rs.getString(cname) == null)
						{
							jb.put(cname, "");
						}
						else
						{
							jb.put(cname, rs.getString(cname));
						}
						
					}
					ja.put(jb);
				}			
			}
			catch(Exception E)
			{
				fileLogger.error("Exception while authenticating the user "+E.getMessage());
			}
			finally
			{
				try
				{
					smt.close();
					con.close();
				}
				catch(Exception E)
				{
					
				}
			}	
			fileLogger.info("Exit Function : getJson");
			return ja;
		}
		
		public JSONArray getMobileTypeAll(HttpServletRequest request)
		{
				String query = "select imsi,imei,mobile_type from mobiletype";
				fileLogger.debug(query);
				return getJson(query);
		}
		
		
		/*********************************new spectrum data*************************/
		
		public String spectrumDataAntena(HttpServletRequest request)
		{	
			fileLogger.info("Inside Function : spectrumDataAntena");
			String data1 = request.getParameter("data");			
			String ip = request.getParameter("ip");
			String count = request.getParameter("count");
		//	fileLogger.debug("###############################mandeep dat###########################");
			fileLogger.debug(data1);
			fileLogger.debug(ip);
			fileLogger.debug(count);
			//fileLogger.debug("###############################mandeep dat###########################");
			
			
			String [] bb = null;
			String [] lineFormat = null;
			//String data1 = "1,2,3,4,\n,1,2,3,4,";	
			lineFormat = data1.toString().split("\\n");
				
				//for(String lineData:lineFormat)
			for(int i=0;i<lineFormat.length;i++)
			{
					
				
			String data = lineFormat[i];
			
			String[] pack = formtaSpecturmDataPacketAngle(data,ip,count);
			ArrayList<String> serverData = new ArrayList<String>();
			serverData.add(ip);
			serverData.add(count);			
			/*for(String s:pack)
			{
				serverData.add(s);
			}*/
			for(int j=0;j<pack.length-1;j++)
			{
				if(j != 2)
					serverData.add(pack[j]);
				else
				{
					//String[] time = packet[j].trim().split(":");						
					String dateTime = pack[j].trim()+"_"+pack[j+1].trim();
					dateTime=dateTimeFormaterWids(dateTime);
					serverData.add(dateTime);
				}
								
			}
			new SpectrumAngleSocketServer().sendText(serverData);
			insertSpecturmDataDataAngle(pack,ip,count);
			
			}
			fileLogger.info("Exit Function : spectrumDataAntena");
			return "";
		}		
		
		
		public String[] formtaSpecturmDataPacketAngle(String dataPacket,String ip,String count)
		{
			fileLogger.info("Inside Function : formtaSpecturmDataPacketAngle");
			String aa = dataPacket;
			
			fileLogger.debug("\n********data formated specturmData **********\n "+aa);
			String [] bb = null;
			
			try
			{	
				fileLogger.debug(aa);
				bb = aa.toString().split(",");				
			}
			catch(Exception E)
			{
				fileLogger.debug(E.getMessage());
			}
			return bb;
		}
		
		public String insertSpecturmDataDataAngle(String[] packet,String ip,String count)
		{
			if(packet.length>0)
			{
				StringBuilder packetString = new StringBuilder();
				
				for(int i=0;i<packet.length-1;i++)
				{
					if(i==0)
						packetString.append("'"+packet[i].trim()+"'");
					else if(i==2)
					{
						String[] time = packet[i+1].trim().split(":");						
						String dateTime = packet[i].trim()+"_"+packet[i+1].trim();
						dateTime=dateTimeFormaterWids(dateTime);
						packetString.append(",'"+dateTime+"'");
					}
					else
					{
						
						packetString.append(",'"+packet[i].trim()+"'");
					}
									
				}
				Common co = new Common();				
				//String query = "INSERT INTO cdrlogs(ip, count, packet_type, imsi, imei, msisdn, ptmsi, tmsi,ol, nl, cid, ta, rxl, tstmp, ftyp, inserttime)"
				//		+ "values('"+ip+"','"+count+"',"+packetString.toString()+")";
				String query = "INSERT INTO wsdiDataAngle(ip,count,anglet,angle,tstmp)"
								+ "values('"+ip+"','"+count+"',"+packetString.toString()+")";
				co.executeDLOperation(query);
			}
			fileLogger.info("Exit Function : formtaSpecturmDataPacketAngle");
			return "";
		}
		
		
		public JSONArray getSpectrumDataAngle(HttpServletRequest request)
		{	
			String startTime = request.getParameter("startTime");
			String endTime = request.getParameter("endTime");
			String query = "Select  * from wsdidataangle where logtime between '"+startTime+"' and '"+endTime+"'";
			fileLogger.debug(query);
			return getJson(query);
		}
		
		public String oprData(HttpServletRequest request)
		{	
			String data = request.getParameter("data");
			String ip = request.getParameter("ip");
			String count = request.getParameter("count");
			data = "'"+data.replaceAll("\\$", "','")+"'";
			//new SocketServer().sendText(pack);
			Common co = new Common();	
			String query = "INSERT INTO oprlogs(ip, count, packet_type, freq, band, arfcn, mcc, mnc, lac,cell, ncc, bcc, rssi, snr, ta, sysloc, tstmp"+
			") VALUES ('"+ip+"','"+count+"',"+data+")";
			co.executeDLOperation(query);
			oprDataCurrent(ip,count,data);
			new opretorDataServer().sendText("network");
			return "";
		}
		
		public String oprData(LinkedHashMap<String,String> data,String ip,String id,String tstmp,int index,int activeAntennaId,String selfLoc)
		{	
			fileLogger.info("Inside Function : oprData");
			Common co = new Common();
			String lacOrTacDeleteQuerySubStr="";
			String cell="";
			
			String lacTac = null;
			if(data.get("TECH").equalsIgnoreCase("LTE")){
				lacTac = data.get("TAC");
				lacOrTacDeleteQuerySubStr="tac='"+data.get("TAC")+"'";
				cell=data.get("CELLID");
				//data.get("EARFCN")+"-"+ data.get("PCI");//data.get("PCI");
			}else{
				lacTac = data.get("LAC");
				lacOrTacDeleteQuerySubStr="lac='"+data.get("LAC")+"'";
				cell=data.get("CELL_ID");
			}
			if(cell.equals("0") || cell.equals("1") || lacTac.equals("0")){
				return "";
			}
			String query = getQueryForOprLogs(data,ip,id,tstmp,index,"oprlogs",activeAntennaId,selfLoc);
			co.executeDLOperation(query);
			String mcc = data.get("MCC");
			String mnc = data.get("MNC");
			
			if(mcc.equalsIgnoreCase("na")|| mnc.equalsIgnoreCase("na")) 
			{
				return "";
			}
			
			
			String query2 = getQueryForOprLogs(data,ip,id,tstmp,index,"current",activeAntennaId,selfLoc);
			synchronized(this)
			{
				String query1="delete from oprlogs_current where mcc='"+mcc+"' and mnc='"+mnc+"' and "+lacOrTacDeleteQuerySubStr+" and cell='"+cell+"' and antenna_id="+activeAntennaId+"";
				boolean deleteStatus=co.executeDLOperation(query1);
				int id1 = co.executeQueryAndReturnId(query2);
				String currentOprlogExtractQuery="select ip,packet_type,trunc(freq::numeric,1) freq,oc.band,arfcn,mcc,mnc,lac,tac,cell,ncc,"+
												 "bcc,rssi,snr,ta,sysloc,tstmp,date_trunc('second',inserttime + '05:30:00'::interval) insert_time,"+
												 "lat,lon,country,operators,oprid,uarfcn,round(rscp::numeric,2) rscp,round(ecno::numeric,2) ecno,psc"+
												 ",earfcn,pci,round(rsrp::numeric,2) rsrp,round(rsrq::numeric,2) rsrq,packet_id,index_id,ant.profile_name,bl.name,off_angle,self_loc,bandwidth"+
												 " from oprlogs_current oc left join band_list bl on oc.packet_type=bl.tech and oc.band=bl.band left join antenna ant on oc.antenna_id=ant.id where oc.id="+id1+"";
				JSONObject oprlogsCurrentData=null;
				try {
					oprlogsCurrentData = new Operations().getJson(currentOprlogExtractQuery).getJSONObject(0);
				} catch (JSONException e) {
					fileLogger.error("@cell exception in getting data for sending");
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				new opretorDataServer().sendText(oprlogsCurrentData.toString());
				new AuditHandler().audit_oprData(oprlogsCurrentData);
			} 
			
			if(Operations.bist != null && Operations.bist.netscan) 
		    {
		    	String fcn = data.get("TECH") == "UMTS"?data.get("UARFCN"):(data.get("TECH") == "GSM")?data.get("ARFCN"):data.get("EARFCN");
				bist.compareOprData(mcc+mnc, lacTac, cell,fcn, data.get("TECH"));
		    }
			fileLogger.info("Exit Function : oprData");
 			return "";
		}
		
	
		
	public String getQueryForOprLogs(LinkedHashMap<String,String> data,String ip,String id,String tstmp,int index,String type,int activeAntennaId,String selfLoc) 
	{
		fileLogger.info("Inside Function : getQueryForOprLogs");
		int angleOffset=DBDataService.getAngleOffset();
		fileLogger.debug("in getQueryForOprLogs for entry in oprlogs or oprlogs_current");
		fileLogger.debug("activeAntennaId is :"+activeAntennaId);
		String query = "";
		
		String mcc = data.get("MCC");
		String mnc = data.get("MNC");
		String bandwidth=data.get("BANDWIDTH");
		
		
		fileLogger.info("@getQueryForOprLogs Bhai Bandwidth received =  " + bandwidth);
		String lac="";
		String cell="";
		if(data.get("TECH").equalsIgnoreCase("LTE")){
			lac=data.get("TAC");
			//cell=data.get("EARFCN")+"-"+ data.get("PCI");
				cell=data.get("CELLID");
		}else{
			lac=data.get("LAC");
			cell=data.get("CELL_ID");
		}

		String tableName = "oprlogs";
		String oprCurrentTableColumns = "";
		String oprCurrentTableColumnsValue = "";
		
		try 
		{
			if(type.equalsIgnoreCase("current")) 
			{
				JSONArray latLonCountOpr = getLatLon(mcc,mnc,lac,cell);
				oprCurrentTableColumns = ",lat,lon,country,operators";
				oprCurrentTableColumnsValue = ",'"+latLonCountOpr.getJSONObject(0).getString("lat")+"','"+latLonCountOpr.getJSONObject(0).getString("lon")+"','"+latLonCountOpr.getJSONObject(0).getString("count")+"','"+latLonCountOpr.getJSONObject(0).getString("opr")+"'";
				tableName = "oprlogs_current";
			}
		
		
		String lat="";
		String lon="";
		String rssi="";
		String snr="";
		switch (data.get("TECH")) 
		{
			case "LTE":
				lat=data.get("LAT");
				lon=data.get("LONG");
				rssi=data.get("RSSI");
				if(lat!=null){
					lat=Double.toString(Math.round(Double.parseDouble(data.get("LAT"))*1000000.0)/1000000.0);
				}
				if(lon!=null){	
					lon=Double.toString(Math.round(Double.parseDouble(data.get("LONG"))*1000000.0)/1000000.0);
				}
				if(rssi!=null){	
					rssi=Integer.toString((int)Double.parseDouble(data.get("RSSI")));
				}
			
				query = "INSERT INTO "+tableName+"(antenna_id,ip, count, packet_type, freq, band,mcc, mnc, tac,cell, rssi,ta, earfcn,pci,rsrp,rsrq,sysloc, tstmp,packet_id,index_id"+oprCurrentTableColumns+",off_angle,self_loc,bandwidth)"
						+ " VALUES ("+activeAntennaId+",'"+ip+"','NA','"+data.get("TECH")+"','"+data.get("FREQ")+"','"+data.get("BAND")+"','"+data.get("MCC")+"','"+data.get("MNC")+"','"+data.get("TAC")+"','"
								+data.get("CELLID")+"','"+rssi+"','"+data.get("PD")+"','"+data.get("EARFCN")+"','"+data.get("PCI")+"','"+data.get("RSRP")+"'"
								+ ",'"+data.get("RSRQ")+"','"+lat+","+lon+"','"+tstmp+"',"+id+","+index+oprCurrentTableColumnsValue+","+angleOffset+",'"+selfLoc+"','"+bandwidth+"')";
				
				break;
			case "UMTS":
				lat=data.get("LAT");
				lon=data.get("LONG");
				rssi=data.get("RSSI");
				if(lat!=null){
					lat=Double.toString(Math.round(Double.parseDouble(data.get("LAT"))*1000000.0)/1000000.0);
				}
				if(lon!=null){	
					lon=Double.toString(Math.round(Double.parseDouble(data.get("LONG"))*1000000.0)/1000000.0);
				}
				if(rssi!=null){	
					rssi=Integer.toString((int)Double.parseDouble(data.get("RSSI")));
				}
				query = "INSERT INTO "+tableName+"(antenna_id,ip, count, packet_type, freq, band,mcc, mnc, lac,cell, rssi,ta, uarfcn,rscp,ecno,psc,sysloc, tstmp,packet_id,index_id"+oprCurrentTableColumns+",off_angle,self_loc,bandwidth) VALUES ("+activeAntennaId+",'"+ip+"','NA','"+data.get("TECH")+"','"+data.get("FREQ")+"','"+data.get("BAND")+"','"+data.get("MCC")+"','"+data.get("MNC")+"','"+data.get("LAC")+"','"
								+data.get("CELL_ID")+"','"+rssi+"','"+data.get("PD")+"','"+data.get("UARFCN")+"','"+data.get("RSCP")+"','"+data.get("ECNO")+"'"
								+ ",'"+data.get("PSC")+"','"+lat+","+lon+"','"+tstmp+"',"+id+","+index+oprCurrentTableColumnsValue+","+angleOffset+",'"+selfLoc+"','"+bandwidth+"')";
				break;
			case "GSM":
				lat=data.get("LAT");
				lon=data.get("LONG");
				rssi=data.get("RSSI");
				snr=data.get("SNR");
				if(lat!=null){
					lat=Double.toString(Math.round(Double.parseDouble(data.get("LAT"))*1000000.0)/1000000.0);
				}
				if(lon!=null){	
					lon=Double.toString(Math.round(Double.parseDouble(data.get("LONG"))*1000000.0)/1000000.0);
				}
				if(rssi!=null){	
					rssi=Integer.toString((int)Double.parseDouble(data.get("RSSI")));
				}
				if(snr!=null){	
					snr=Integer.toString((int)Double.parseDouble(data.get("SNR")));
				}
				query = "INSERT INTO "+tableName+"(antenna_id,ip, count, packet_type, freq, band, arfcn, mcc, mnc, lac,cell, ncc, bcc, rssi, snr, ta, sysloc, tstmp,packet_id,index_id"+oprCurrentTableColumns+",off_angle,self_loc,bandwidth) "
						+ "VALUES ("+activeAntennaId+",'"+ip+"','NA','"+data.get("TECH")+"','"+data.get("FREQ")+"','"+data.get("BAND")+"','"+data.get("ARFCN")+"','"+data.get("MCC")+"',"
								+ "'"+data.get("MNC")+"','"+data.get("LAC")+"','"+data.get("CELL_ID")+"',"
								+ "'"+data.get("NCC")+"','"+data.get("BCC")+"','"+rssi+"','"+snr+"',"
								+ "'"+data.get("TA")+"','"+lat+","+lon+"','"+tstmp+"',"+id+","+index+oprCurrentTableColumnsValue+","+angleOffset+",'"+selfLoc+"','"+bandwidth+"')";
				break;
		}
		fileLogger.info("@getQueryForOprLogs Function : Query =" + query);
		
		if(type.equalsIgnoreCase("current")) 
		{
			query = query+" returning id";
		}
		fileLogger.info("Exit Function : getQueryForOprLogs");
		}
		catch(Exception e) 
		{
			fileLogger.debug("Exception occured getQueryForOprLogs exception = "+e);
		}
		return query;
	}
		
		
		public JSONArray getOprCellsData(HttpServletRequest request)
		{
			fileLogger.info("Inside Function : getOprCellsData");
			//String startTime = request.getParameter("startTime");
			//String endTime = request.getParameter("endTime");
				HttpSession session=request.getSession(false);
				fileLogger.debug("the selected nib ip");
				String time = session.getAttribute("startTime").toString();
			String query = "select * from oprlogs_current where inserttime >='"+time+"' and packet_type in('2G','GSM','Loc_2g') and oprid = (select max(id) from oprrationdata) order by inserttime desc;";
			fileLogger.debug(query);
			fileLogger.info("Exit Function : getOprCellsData");

			return getJson(query);			
		}
		
		
		
		
		public void oprDataCurrent(String ip,String count,String data)
		{	
			fileLogger.info("Inside Function : oprDataCurrent");

			try
			{
				String[] splitData = data.split("','");
				
				String mcc = splitData[4];
				String mnc = splitData[5];
				String lac = splitData[6];
				String cell = splitData[7];
				
				String query1="delete from oprlogs_current where mcc='"+mcc+"' and mnc='"+mnc+"' and lac='"+lac+"' and cell='"+cell+"'";
				Common co = new Common();
				boolean deleteStatus=true;
				try
				{			
					deleteStatus=co.executeDLOperation(query1);
				}
				catch(Exception E)
				{
					fileLogger.error("Exception while authenticating the user "+E.getMessage());
				}
				
				
				JSONArray latLonCountOpr = getLatLon(mcc,mnc,lac,cell);
				
					
				String query = "INSERT INTO oprlogs_current(ip, count, packet_type, freq, band, arfcn, mcc, mnc, lac,cell, ncc, bcc, rssi, snr, ta, sysloc, tstmp ,lat,lon,country,operators,off_angle"+
				") VALUES ('"+ip+"','"+count+"',"+data+",'"+latLonCountOpr.getJSONObject(0).getString("lat")+"','"+latLonCountOpr.getJSONObject(0).getString("lon")+"','"+latLonCountOpr.getJSONObject(0).getString("count")+"','"+latLonCountOpr.getJSONObject(0).getString("opr")+"',"+DBDataService.getAngleOffset()+")";
				co.executeDLOperation(query);
			}
			catch(Exception e)
			{
				fileLogger.error("Exception while oprlogs current"+e.getMessage());
			}
			fileLogger.info("Exit Function : oprDataCurrent");
		}
		
		
		public boolean uploadGpsData(HttpServletRequest request,HttpServletResponse res)
		{
			fileLogger.info("Inside Function : uploadGpsData");
			res.setContentType("text/html");
			int uploadSuccess = 0;
			
				 try
				 {
					 PrintWriter out = res.getWriter();
						
						String tempUploadFolderName = ""+System.currentTimeMillis();
						
						String path=null;
						
						String absolutePath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
						absolutePath = absolutePath.substring(0,absolutePath.lastIndexOf("WEB-INF"));
						path = absolutePath+ "/GpsDataUploadFolder/";
						
						String childDirectoryPath = path + "\\" + tempUploadFolderName;
						
							//fileLogger.debug("\n------------------------------------------------\n");
							fileLogger.debug(" temporary folder path temp : "+path);
							//fileLogger.debug("\n------------------------------------------------\n");
						
						//creating the directory if not exist
						checkOrCreateParentChildDirectory_1(path,tempUploadFolderName);			
						
						//fileLogger.debug("\n------------------------------------------------\n");
						fileLogger.debug(" checkOrCreateParentChildDirectory method execution done 1");
					//	fileLogger.debug("\n------------------------------------------------\n");
						
						
						//uploading the file file upload limit is 5gb
						MultipartRequest multi = new MultipartRequest(request,childDirectoryPath,500000000);
						
						//fileLogger.debug("\n------------------------------------------------\n");
						fileLogger.debug(" uploading the file,file upload limit is 5gb done 2");
					//	fileLogger.debug("\n------------------------------------------------\n");
						Enumeration files = multi.getFileNames();						
						while (files.hasMoreElements())
						{
							//getting the file details
							String name = (String)files.nextElement();						
							String filename = multi.getFilesystemName(name);
							String type = multi.getContentType(name);
							//getting the file object
							File f = multi.getFile(name);
							saveGpsData(f);
						}
						uploadSuccess = 1;
				 }
				 catch(Exception E)
				 {
					 uploadSuccess=0;
					 fileLogger.debug(E.getMessage());
					 
				 }
				 finally
				 {
					 try
					 {
						 
						 res.sendRedirect(String.format("%s%s", request.getContextPath(), "/views/administration.jsp?up="+uploadSuccess));
					 }
					 catch(Exception E)
					 {
						 fileLogger.debug(E.getMessage());
					 }
					 
				 }
				 fileLogger.info("Exit Function : uploadGpsData");
			return true;
		}


public static void saveGpsData(File csvFile) 
		{
	       
			BufferedReader br = null;
	        Common co = new Common();
			String line = "";
	        String cvsSplitBy = ";";
			List<String> queries = new ArrayList<String>();

	        try 
	        {

	            
	        	br = new BufferedReader(new FileReader(csvFile));
	            long count=0;
				long rowCount=0;
				String query=null;
				String gpsDate=null;
				String gpsTime=null;
				String gpsAltitude=null;
				String gpsLat=null;
				String gpsLong=null;
				String gpsDateAndTime=null;
	        	while ((line = br.readLine()) != null) 
	            {
                    count++;
	                if(count !=1)
	                {
	                		String[] data = line.split(cvsSplitBy);
							gpsDate=data[0];
							gpsTime=data[1];
							gpsAltitude=data[2];
							gpsLong=data[3];
							gpsLat=data[4];
							double doubleLong=Double.parseDouble(gpsLong);
							double doubleLat=Double.parseDouble(gpsLat);
							if(!(doubleLong==0.00 && doubleLat==0.00))
							{
								if(gpsAltitude.indexOf(".")!=-1){
								String[] gpsAltitudeArr=gpsAltitude.split("\\.");
							gpsAltitude=gpsAltitudeArr[0]+"."+ ((gpsAltitudeArr[1].length()>6)?gpsAltitudeArr[1].substring(0,6):gpsAltitudeArr[1]);
							}
							if(gpsLong.indexOf(".")!=-1){
							String[] gpsLongArr=gpsLong.split("\\.");
							gpsLong=gpsLongArr[0]+"."+ ((gpsLongArr[1].length()>6)?gpsLongArr[1].substring(0,6):gpsLongArr[1]);
							}
                            if(gpsLat.indexOf(".")!=-1){
							String[] gpsLatArr=gpsLat.split("\\.");
							gpsLat=gpsLatArr[0]+"."+ ((gpsLatArr[1].length()>6)?gpsLatArr[1].substring(0,6):gpsLatArr[1]);
						    } 
							gpsTime=gpsTime.substring(0,gpsTime.indexOf("."));
							gpsDateAndTime=gpsDate+" "+gpsTime;
				            SimpleDateFormat sdfParser = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
							Date date = sdfParser.parse(gpsDateAndTime);
							Calendar cal = Calendar.getInstance();
							cal.setTime(date);
							cal.add(Calendar.HOUR,-5);
							cal.add(Calendar.MINUTE,-30);
							date=cal.getTime();
			                SimpleDateFormat sdfFormatter = new SimpleDateFormat( "ddMMyy-hh-mm-ss" );
							gpsDateAndTime=sdfFormatter.format(date);
		                	
							rowCount++;
query="insert into gpsdata(ip,count,tstmp,s_status,lat,lato,lon,lono,speed,course,satellites,elev,eunit,roll,pres,acc,comp,temp1,temp2,gyx,gyy,gyz,tiltx,tilty,tiltz) values('aronia','"+rowCount+"','"+gpsDateAndTime+"','NA','"+gpsLat+"','NA','"+gpsLong+"','NA','0','0','0','"+gpsAltitude+"','NA','0','0','0','0','0','0','0','0','0','0','0','0')";
							queries.add(query);
							}	
	                }
	            }
				co.executeBatchOperation(queries);

	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            if (br != null) {
	                try {
	                    br.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }

	    }

public String scheduleSubscriberSearch(HttpServletRequest request)
{
	fileLogger.info("Inside Function : scheduleSubscriberSearch");
	String scheduleStatus="failed";
	String cmdType = request.getParameter("cmdType");
	String reqType = request.getParameter("reqType");
	String idType = request.getParameter("idType");
	String type_value = request.getParameter("type_value");
	String vlr = request.getParameter("vlr");
	String hlr = request.getParameter("hlr");
	String ftn = request.getParameter("ftn");
	String msc = request.getParameter("msc");
	String fileName = request.getParameter("fileName");
	String firstDelayRequired=request.getParameter("firstDelayRequired");
	String periodicity=request.getParameter("periodicity");
	String nibIp=request.getParameter("nibIp");
	Common co = new Common();	
	String query = "insert into job_details(cmd_type, file_name, ftn, hlr, msc, id_type, req_type, type_value,vlr,status,periodicity,nibip) values ('"+cmdType+"','"+fileName+"','"+ftn+"','"+hlr+"','"+msc+"','"+idType+"','"+reqType+"','"+type_value+"','"+vlr+"','start',"+periodicity+",'"+nibIp+"')";
	if(co.executeDLOperation(query)){
		try
		{
			scheduleStatus="successful";
			Statement smt = co.getDbConnection().createStatement();			
			query = "select id from job_details where type_value='"+type_value+"'";
			String id="";
			fileLogger.debug(query);
			ResultSet rs = smt.executeQuery(query);
			while(rs.next())
			{
					id=rs.getString("id");
			}
		Timer timer=SingleTimer.getInstance().getTimer();
		GeoTask geoTask=new GeoTask(cmdType,fileName,ftn,hlr,msc,idType,reqType,type_value,vlr,nibIp);
		
		if(firstDelayRequired.equalsIgnoreCase("yes")){
		timer.scheduleAtFixedRate(geoTask, Long.parseLong(periodicity), Long.parseLong(periodicity));
		fileLogger.debug("size at scheduleSubscriberSearch start:"+ScheduledTaskObjectHandler.getTaskObjectMapSize());
		ScheduledTaskObjectHandler.setScheduledTaskObject(id,geoTask);
		fileLogger.debug("size at scheduleSubscriberSearch end:"+ScheduledTaskObjectHandler.getTaskObjectMapSize());
		}else{
		timer.scheduleAtFixedRate(geoTask, 0, Long.parseLong(periodicity));
		fileLogger.debug("size at scheduleSubscriberSearch start:"+ScheduledTaskObjectHandler.getTaskObjectMapSize());
		ScheduledTaskObjectHandler.setScheduledTaskObject(id,geoTask);
		fileLogger.debug("size at scheduleSubscriberSearch end:"+ScheduledTaskObjectHandler.getTaskObjectMapSize());
		}
	}catch(Exception e){
		fileLogger.debug("problem is :"+e.getMessage());
	}
	}
	
	fileLogger.info("Exit Function : scheduleSubscriberSearch");
	return scheduleStatus;
}

public JSONArray getAllScheduledSubscribers(HttpServletRequest request)
{
	fileLogger.info("Inside Function : getAllScheduledSubscribers");
	Common co = new Common();
	Statement smt = null;
	Connection con = co.getDbConnection();
	JSONArray ja = new JSONArray();
	try
	{
		smt = con.createStatement();			
		String query = "select * from job_details";
		fileLogger.debug(query);
		ResultSet rs = smt.executeQuery(query);
		
		while(rs.next())
		{
			JSONObject jo = new JSONObject();
			jo.put("id", rs.getString("id"));
			jo.put("cmd_type", rs.getString("cmd_type"));
			jo.put("file_name", rs.getString("file_name"));
			jo.put("ftn", rs.getString("ftn"));
			jo.put("hlr", rs.getString("hlr"));
			jo.put("msc", rs.getString("msc"));
			jo.put("id_type", rs.getString("id_type"));
			jo.put("req_type", rs.getString("req_type"));
			jo.put("type_value", rs.getString("type_value"));
			jo.put("vlr", rs.getString("vlr"));
			jo.put("status", rs.getString("status"));
			jo.put("periodicity", rs.getString("periodicity"));
			ja.put(jo);
		}			
	}
	catch(Exception E)
	{
		fileLogger.error("Exception while authenticating the user "+E.getMessage());
	}
	finally
	{
		try
		{
			smt.close();
			con.close();
		}
		catch(Exception E)
		{
			
		}
	}	
	fileLogger.info("Exit Function : getAllScheduledSubscribers");
	return ja;
}

public String startScheduledTask(HttpServletRequest request)
{
	fileLogger.info("Inside Function : startScheduledTask");
	Common co = new Common();
	Statement smt = null;
	Connection con = co.getDbConnection();
	Timer timer=SingleTimer.getInstance().getTimer();
	String startStatus="failed";
	String id=request.getParameter("id");
	String periodicity=request.getParameter("periodicity");
	try
	{
		smt = con.createStatement();			
		String query = "select * from job_details where id="+id;
		fileLogger.debug(query);
		ResultSet rs = smt.executeQuery(query);
		while(rs.next())
		{
			fileLogger.debug("size at startScheduledTask start:"+ScheduledTaskObjectHandler.getTaskObjectMapSize());
			GeoTask geoTask=new GeoTask(rs.getString("cmd_type"),rs.getString("file_name"),rs.getString("ftn"),rs.getString("hlr"),rs.getString("msc"),rs.getString("id_type"),rs.getString("req_type"),rs.getString("type_value"),rs.getString("vlr"),rs.getString("nibip"));
			timer.scheduleAtFixedRate(geoTask, 0, Long.parseLong(periodicity));
			ScheduledTaskObjectHandler.setScheduledTaskObject(id,geoTask);
			query="update job_details set status='start',periodicity="+Long.parseLong(request.getParameter("periodicity"))+" where id="+Integer.parseInt(request.getParameter("id"));
			fileLogger.debug(query);
			co.executeDLOperation(query);
			fileLogger.debug("size at startScheduledTask end:"+ScheduledTaskObjectHandler.getTaskObjectMapSize());
			startStatus="successful";
		}			
	}
	catch(Exception E)
	{
		E.printStackTrace();
		//fileLogger.debug("Exception is:"+E.getMessage());
	}
	finally
	{
		try
		{
			smt.close();
			con.close();
		}
		catch(Exception E)
		{
			
		}
	}
	fileLogger.info("Exit Function : startScheduledTask");
	return startStatus;
}

public  String stopScheduledTask(HttpServletRequest request)
{
	fileLogger.info("Inside Function : stopScheduledTask");
	Common co = new Common();
	Statement smt = null;
	String id=request.getParameter("id");
	Timer timer=SingleTimer.getInstance().getTimer();
	GeoTask geoTask=ScheduledTaskObjectHandler.getScheduledTaskObject(id);
	geoTask.cancel();
	timer.purge();
	fileLogger.debug("size at stopScheduledTask start:"+ScheduledTaskObjectHandler.getTaskObjectMapSize());
	ScheduledTaskObjectHandler.removeScheduledTaskObject(id);
	String query="update job_details set status='stop' where id="+Integer.parseInt(id);
	fileLogger.debug(query);
	co.executeDLOperation(query);
	fileLogger.debug("size at stopScheduledTask end:"+ScheduledTaskObjectHandler.getTaskObjectMapSize());
	fileLogger.info("Exit Function : stopScheduledTask");
	return "successful";
}

public String changePeriodicityOfScheduledTask(HttpServletRequest request)
{
	fileLogger.info("Inside Function : changePeriodicityOfScheduledTask");
	String changePeriodicityStatus="failed";
	Common co = new Common();
	Statement smt = null;
	Connection con = co.getDbConnection();
	String id=request.getParameter("id");
	String periodicity=request.getParameter("periodicity");
	String status=request.getParameter("status");
	String query="";
	try
	{
		if(status.equalsIgnoreCase("stop")){
		query = "update job_details set periodicity="+periodicity+" where id="+id;
		fileLogger.debug(query);
		boolean updationStatus=co.executeDLOperation(query);
		if(updationStatus){
			changePeriodicityStatus="successful";
			fileLogger.debug("periodicity changed successfully with status:"+status);
		}
		}else
		{
			fileLogger.debug("1");
		Timer timer=SingleTimer.getInstance().getTimer();;
		GeoTask geoTask=ScheduledTaskObjectHandler.getScheduledTaskObject(id);
		geoTask.cancel();
		timer.purge();
		fileLogger.debug("size at changePeriodicityOfScheduledTask start:"+ScheduledTaskObjectHandler.getTaskObjectMapSize());
		ScheduledTaskObjectHandler.removeScheduledTaskObject(id);		
		fileLogger.debug("size at changePeriodicityOfScheduledTask mid:"+ScheduledTaskObjectHandler.getTaskObjectMapSize());
		query="select * from job_details where id="+id;
		fileLogger.debug(query);
		smt = con.createStatement();
		ResultSet rs = smt.executeQuery(query);
		while(rs.next())
		{
				String cmdType=rs.getString("cmd_type");
				String fileName=rs.getString("file_name");
				String ftn=rs.getString("ftn");
				String hlr=rs.getString("hlr");
				String msc=rs.getString("msc");
				String idType=rs.getString("id_type");
				String reqType=rs.getString("req_type");
				String typeVal=rs.getString("type_value");
				String vlr=rs.getString("vlr");
				String nibIp=rs.getString("nibip");
				GeoTask newGeoTask=new GeoTask(cmdType,fileName,ftn,hlr,msc,idType,reqType,typeVal,vlr,nibIp);
				timer.scheduleAtFixedRate(newGeoTask, 0, Long.parseLong(periodicity));
				ScheduledTaskObjectHandler.setScheduledTaskObject(id,newGeoTask);
			
		}
		fileLogger.debug("size at changePeriodicityOfScheduledTask end:"+ScheduledTaskObjectHandler.getTaskObjectMapSize());
		query = "update job_details set periodicity="+periodicity+" where id="+id;
		fileLogger.debug(query);
		boolean updationStatus=co.executeDLOperation(query);
		if(updationStatus){
			changePeriodicityStatus="successful";
			fileLogger.debug("periodicity changed successfully with status:"+status);
	}
	}
	}
	catch(Exception E)
	{
		E.printStackTrace();
		//fileLogger.debug("Exception is:"+E.getMessage());
	}
	finally
	{
		try
		{
			smt.close();
			con.close();
		}
		catch(Exception E)
		{
			
		}
	}
	fileLogger.info("Exit Function : changePeriodicityOfScheduledTask");
	return changePeriodicityStatus;
}

public String deleteScheduledTask(HttpServletRequest request)
{
	fileLogger.info("Inside Function : deleteScheduledTask");
	String deleteStatus="failed";
	Common co = new Common();
	String id=request.getParameter("id");
	String status=request.getParameter("status");
	try
	{			
		String query = "delete from  job_details where id="+id;
		fileLogger.debug(query);
        if(co.executeDLOperation(query)){
        	fileLogger.debug("Scheduled Task Deleted");
        }
        if(status.equalsIgnoreCase("start")){
        	fileLogger.debug("size at deleteScheduledTask start:"+ScheduledTaskObjectHandler.getTaskObjectMapSize());
    		Timer timer=SingleTimer.getInstance().getTimer();
    		GeoTask geoTask=ScheduledTaskObjectHandler.getScheduledTaskObject(id);
    		fileLogger.debug("size at deleteScheduledTask mid:"+ScheduledTaskObjectHandler.getTaskObjectMapSize());
    		geoTask.cancel();
    		timer.purge();
    		fileLogger.debug("size at deleteScheduledTask end:"+ScheduledTaskObjectHandler.getTaskObjectMapSize());
    		ScheduledTaskObjectHandler.removeScheduledTaskObject(id);	
        }
        deleteStatus="successful";
	}
	catch(Exception E)
	{
		E.printStackTrace();
		//fileLogger.debug("Exception is:"+E.getMessage());
	}
	finally
	{
		try
		{
		}
		catch(Exception E)
		{
			
		}
	}
	fileLogger.info("Exit Function : deleteScheduledTask");
	return deleteStatus;
}

	public String setSelectedNibIp(HttpServletRequest request){
		fileLogger.info("Inside Function : setSelectedNibIp");
		String selectedNibIp=request.getParameter("selectedNibIp");
		fileLogger.debug("the selected nib ip is :"+selectedNibIp);
	HttpSession session=request.getSession(false);
	fileLogger.debug("the selected nib ip");
	session.setAttribute("currentNib",selectedNibIp);
	fileLogger.debug("the selected nib ip is :");
	fileLogger.info("Exit Function : setSelectedNibIp");
	return "successful";
	}
	
	public JSONArray getBtsInfoPerApplication(HttpServletRequest request)
	{
		fileLogger.info("Inside Function : getBtsInfoPerApplication");
		Common co = new Common();
		Statement smt = null;
		Connection con = co.getDbConnection();
		JSONArray ja = new JSONArray();
		try
		{
			smt = con.createStatement();			
			String query = "select * from view_btsinfo where application_name='"+request.getParameter("applicationName")+"' order by grp_name";
			fileLogger.debug(query);
			ResultSet rs = smt.executeQuery(query);
			
			while(rs.next())
			{
				JSONObject jo = new JSONObject();
				jo.put("b_id", rs.getString("b_id"));
				jo.put("ip", rs.getString("ip"));
				jo.put("statuscode", rs.getString("statuscode"));
				jo.put("cellcode", rs.getString("cellcode"));
				jo.put("typeid", rs.getString("typeid"));
				jo.put("typename", rs.getString("typename"));
				jo.put("status", rs.getString("status"));
				jo.put("cellstatus", rs.getString("cellstatus"));
				jo.put("dcode", rs.getString("dcode"));
				jo.put("dname", rs.getString("dname"));
				jo.put("sytemid", rs.getString("sytemid"));
				jo.put("code", rs.getString("code"));
				jo.put("grp_id", rs.getString("grp_id"));
				jo.put("grp_name", rs.getString("grp_name"));
				jo.put("application_id", rs.getString("application_id"));
				jo.put("application_name", rs.getString("application_name"));
				jo.put("application_status", rs.getString("application_status"));
				ja.put(jo);
			}			
		}
		catch(Exception E)
		{
			fileLogger.error("Exception while authenticating the user "+E.getMessage());
		}
		finally
		{
			try
			{
				smt.close();
				con.close();
			}
			catch(Exception E)
			{
				
			}
		}	
		fileLogger.info("Exit Function : getBtsInfoPerApplication");
		return ja;
}
	
	public JSONArray getNodeWiseReport(HttpServletRequest request)
	{
		//String operationStartTime = request.getParameter("operationStartTime");
		
		String query="";
			//query="select stype,ip,traget_type,count,imsi,imei,ta,rxl,cgi,sysloc,band,ulrfcn,dlarfcn,outpow,tstmp,psc,date_trunc('second',inserttime + '05:30:00'::interval) insert_time,oprname(imsi::numeric) as operator,getcountry(substr(imsi::text, 1, 3)::numeric) as country,trigger_source,freq,msloc from cdrlogs_current  where inserttime >= (CURRENT_DATE - '05:30:00'::interval) order by inserttime desc limit 1000";
	
		//query="select stype,ip,traget_type,count,imsi,imei,ta,rxl,cgi,sysloc,cc.band,ulrfcn,dlarfcn,outpow,tstmp,psc,date_trunc('second',inserttime + '05:30:00'::interval) insert_time,oprname(imsi) as operator,getcountry(substr(imsi::text, 1, 3)) as country,trigger_source,TRUNC(freq::numeric,1) as freq,msloc,target_name,profile_name,trans_id,distance,calc_basis,prob_msloc,self_loc,cue_id,show_cue_st from cdrlogs_current cc left join antenna ant on cc.ant_id=ant.id where inserttime >= (CURRENT_DATE - '05:30:00'::interval) and imsi != 'null' order by inserttime desc limit 1000";
		// query for blacklist phones issue fix below
		//query="(select stype,ip,traget_type,count,imsi,imei,ta,rxl,cgi,sysloc,cc.band,ulrfcn,dlarfcn,outpow,tstmp,psc,date_trunc('second',inserttime + '05:30:00'::interval) insert_time,oprname(imsi) as operator,getcountry(substr(imsi::text, 1, 3)) as country,trigger_source,TRUNC(freq::numeric,1) as freq,msloc,target_name,profile_name,trans_id,distance,calc_basis,prob_msloc,self_loc,cue_id,show_cue_st from cdrlogs_current cc left join antenna ant on cc.ant_id=ant.id where (inserttime >= CURRENT_DATE - '05:30:00'::interval)  and imsi != 'null'  order by inserttime desc limit 1000) 	union  (select stype,ip,traget_type,count,imsi,imei,ta,rxl,cgi,sysloc,cc.band,ulrfcn,dlarfcn,outpow,tstmp,psc,date_trunc('second',inserttime + '05:30:00'::interval) insert_time,oprname(imsi) as operator,getcountry(substr(imsi::text, 1, 3)) as country,trigger_source,TRUNC(freq::numeric,1) as freq,msloc,target_name,profile_name,trans_id,distance,calc_basis,prob_msloc,self_loc,cue_id,show_cue_st from cdrlogs_current cc left join antenna ant on cc.ant_id=ant.id where (inserttime >= CURRENT_DATE - '05:30:00'::interval)  and imsi != 'null' and  cc.traget_type='Blacklist' order by inserttime desc );";
		query="((select stype,ip,traget_type,count,imsi,imei,ta,rxl,cgi,sysloc,cc.band,ulrfcn,dlarfcn,outpow,tstmp,psc,date_trunc('second',inserttime + '05:30:00'::interval) insert_time,oprname(imsi) as operator,getcountry(substr(imsi::text, 1, 3)) as country,trigger_source,TRUNC(freq::numeric,1) as freq,msloc,target_name,profile_name,trans_id,distance,calc_basis,prob_msloc,self_loc,cue_id,show_cue_st from cdrlogs_current cc left join antenna ant on cc.ant_id=ant.id where (inserttime >= CURRENT_DATE - '05:30:00'::interval)  and imsi != 'null'  order by inserttime desc limit 1000) 	union  (select stype,ip,traget_type,count,imsi,imei,ta,rxl,cgi,sysloc,cc.band,ulrfcn,dlarfcn,outpow,tstmp,psc,date_trunc('second',inserttime + '05:30:00'::interval) insert_time,oprname(imsi) as operator,getcountry(substr(imsi::text, 1, 3)) as country,trigger_source,TRUNC(freq::numeric,1) as freq,msloc,target_name,profile_name,trans_id,distance,calc_basis,prob_msloc,self_loc,cue_id,show_cue_st from cdrlogs_current cc left join antenna ant on cc.ant_id=ant.id where (inserttime >= CURRENT_DATE - '05:30:00'::interval)  and imsi != 'null' and  cc.traget_type='Blacklist' order by inserttime desc ) ) order by insert_time desc;";		
		
		JSONArray ja = getJson(query);
		return ja;
}
	
	
	public JSONArray getCdrReports(HttpServletRequest request)
	{
		//String operationStartTime = request.getParameter("operationStartTime");
		String start_date=request.getParameter("start_date");
		//start_date=new CommonService().getUTCTime(start_date,null);
		//start_date=;
			String end_date=request.getParameter("end_date");;
			//end_date=new CommonService().getUTCTime(end_date,null);
			String query="";
				//query="select stype,ip,traget_type,count,imsi,imei,ta,rxl,cgi,sysloc,band,ulrfcn,dlarfcn,outpow,tstmp,psc,date_trunc('second',inserttime + '05:30:00'::interval) insert_time,oprname(imsi::numeric) as operator,getcountry(substr(imsi::text, 1, 3)::numeric) as country,trigger_source,freq,msloc from cdrlogs_current  where inserttime >= (CURRENT_DATE - '05:30:00'::interval) order by inserttime desc limit 1000";

			query="select stype,ip,traget_type,count,imsi,imei,ta,rxl,cgi,sysloc,cc.band,ulrfcn,dlarfcn,outpow,tstmp,psc,date_trunc('second',inserttime + '05:30:00'::interval) insert_time,oprname(substr(imsi::text, 1, 6)) as operator,getcountry(substr(imsi::text, 1, 3)::numeric) as country,trigger_source,TRUNC(freq::numeric,1) as freq,msloc,target_name,profile_name,trans_id,distance,calc_basis,prob_msloc,self_loc,cue_id,show_cue_st from cdrlogs cc left join antenna ant on cc.ant_id=ant.id where inserttime >= '" +start_date +"' and inserttime <= '"+ end_date+"'     and imsi != 'null' order by inserttime desc";
			
			
		JSONArray ja = getJson(query);
		return ja;
}
	
	
	public boolean addTarget(HttpServletRequest request)
	{
		String targetImsi = request.getParameter("targetImsi");
		String targetImei = request.getParameter("targetImei");
		
		String query = "insert into target_list(imsi,imei) values('"+targetImsi+"','"+targetImei+"')";
		Common co = new Common();
		return co.executeDLOperation(query);		
	}
	
	public JSONArray getCountryWiseReport(HttpServletRequest request)
	 {
		fileLogger.info("Inside Function : getCountryWiseReport");
	  String countryName = request.getParameter("countryName");
	  String oprName = request.getParameter("oprName");
	  String mainFilter = request.getParameter("mainFilter"); 
	  String subFilter = request.getParameter("subFilter");
	  String operationStartTime = request.getParameter("operationStartTime");
	  String whereParam = "";
	  String country = countryName.equalsIgnoreCase("unknown")?"country is null":"country='"+countryName+"'";
	  String opr_name = oprName.equalsIgnoreCase("unknown")?"opr is null":"opr='"+oprName+"'";
	  String whereCondition=" "+country+" and "+opr_name+" and ";
	  String preQuery="select * from (select * from cdrlogs_current left join plmn_opr  on (plmn=substr(cdrlogs_current.imsi::text, 1, 5)::int) where imsi <> 'null' and  imsi not  in ( select imsi from cdrlogs_current ,plmn_opr  where imsi <> 'null' and substr(cdrlogs_current.imsi::text, 1, 6)::int= plmn) union select distinct * from cdrlogs_current ,plmn_opr  where imsi <> 'null' and plmn=substr(cdrlogs_current.imsi::text, 1, 6)::int) cdrlogs_current left join mcc_country mc on(substr(cdrlogs_current.imsi::text, 1, 3)::numeric = mc.mcc )";
	  String query="";
	  if(mainFilter.equalsIgnoreCase("ta")){
	   if(subFilter.equalsIgnoreCase("lessthan5")){
	    whereCondition+="ta::numeric >2 and ta::numeric<=5";
	   }else if(subFilter.equalsIgnoreCase("morethan5")){
	    whereCondition+="ta::numeric >5"; 
	   }else{
	    whereCondition+="ta='"+subFilter+"'";
	   }
	   query=preQuery+" where "+whereCondition+" and inserttime>='"+operationStartTime+"'";
	  }else{
	   if(subFilter.equalsIgnoreCase("morethan-55")){
	    whereCondition+="rxl::numeric>'-55'";
	   }else if(subFilter.equalsIgnoreCase("morethan-75")){
	    whereCondition+="rxl::numeric<=-55 and rxl::numeric>-75"; 
	   }else if(subFilter.equalsIgnoreCase("morethan-95")){
	    whereCondition+="rxl::numeric<=-75 and rxl::numeric>-95"; 
	   }else if(subFilter.equalsIgnoreCase("lessthan-95")){
	    whereCondition+="rxl::numeric<=-95";
	   }else{
	    whereCondition+="rxl='"+subFilter+"'"; 
	   }
	   query=preQuery+" where "+whereCondition+" and inserttime>='"+operationStartTime+"'";
	  }
	  Common co = new Common();
	  Statement smt = null;
	  Connection con = co.getDbConnection();
	  JSONArray ja = new JSONArray();
	  try
	  {
	   smt = con.createStatement();   
	   query = query+" order by inserttime desc";
	   fileLogger.debug(query);
	   
	   ResultSet rs = smt.executeQuery(query);
	   
	   while(rs.next())
	   { 
	    JSONObject jo = new JSONObject();
	    jo.put("stype", rs.getString("stype"));
	    jo.put("ip", rs.getString("ip"));
	    jo.put("count", rs.getString("count"));
	    jo.put("imsi", rs.getString("imsi"));
	    jo.put("imei", rs.getString("imei"));
	    jo.put("ta", rs.getString("ta"));
	    jo.put("rxl", rs.getString("rxl"));
	    jo.put("cgi", rs.getString("cgi"));
	    jo.put("sysloc", rs.getString("sysloc"));
	    jo.put("band", rs.getString("band"));
	    jo.put("ulrfcn", rs.getString("ulrfcn"));
	    jo.put("dlarfcn", rs.getString("dlarfcn"));
	    jo.put("outpow", rs.getString("outpow"));
	    jo.put("tstmp", rs.getString("tstmp"));
	    jo.put("psc", rs.getString("psc"));
	    jo.put("inserttime", rs.getString("inserttime"));
	    ja.put(jo);
	   }   
	  }
	  catch(Exception E)
	  {
	   fileLogger.error("Exception while authenticating the user "+E.getMessage());
	  }
	  finally
	  {
	   try
	   {
	    smt.close();
	    con.close();
	   }
	   catch(Exception E)
	   {
	    
	   }
	  } 
	  fileLogger.info("Exit Function : getCountryWiseReport");
	  return ja;
	}
	
	
	public JSONArray getNetworkScanReport(HttpServletRequest request)
	{
		fileLogger.info("Inside Function : getNetworkScanReport");
		String query="";
		String tableColumns="oc.id,ip,count,packet_type,trunc(freq::numeric,1) freq,oc.band,arfcn,bandwidth,mcc,mnc,lac,cell,ncc,bcc,rssi,snr,ta,sysloc,tstmp,date_trunc('second',inserttime + '05:30:00'::interval) insert_time,lat,lon,country,operators,oprid,uarfcn,round(rscp::numeric,2) rscp,round(ecno::numeric,2) ecno,psc,earfcn,pci,round(rsrp::numeric,2) rsrp,round(rsrq::numeric,2) rsrq,packet_id,index_id,ant.profile_name,bl.name,tac";
		//String operationStartTime = request.getParameter("operationStartTime");
		//query="select * from oprlogs join plmn_opr on (oprlogs.mcc||oprlogs.mnc)::numeric = plmn_opr.plmn where (mcc,mnc,lac,cell,inserttime) in (select mcc,mnc,lac,cell,max(inserttime) from oprlogs where inserttime >= '"+operationStartTime+"' group by mcc,mnc,lac,cell)";	
		query  = "select "+tableColumns+" from oprlogs_current oc left join band_list bl on oc.packet_type=bl.tech and oc.band=bl.band left join antenna ant on oc.antenna_id=ant.id  where ((inserttime >= (timezone('utc'::text, now()) - '24:00:00'::interval)) or count = 'locator_count') AND inserttime <= timezone('utc'::text, now()) order by inserttime desc";
		JSONArray ja = new JSONArray();
		try
		{			
			ja=getJson(query);
		}
		catch(Exception E)
		{
			fileLogger.error("Exception in getting the data :"+E.getMessage());
		}
		return ja;
}	
	
	public JSONArray getAllTargetImsi(HttpServletRequest request)
	{
		fileLogger.info("Inside Function : getAllTargetImsi");
		String query="select * from target_list order by imsi";
		JSONArray ja = new JSONArray();
		try
		{			
			ja=getJson(query);
		}
		catch(Exception E)
		{
			fileLogger.error("Exception in getAllTargetImsi :"+E.getMessage());
		}
		fileLogger.info("Exit Function : getAllTargetImsi");
		return ja;
}
	
	public boolean deleteTargetImsi(HttpServletRequest request)
	{
		fileLogger.info("Inside Function : deleteTargetImsi");
		String targetImsi=request.getParameter("targetImsi");
		String query="delete from target_list where imsi="+targetImsi;
		Common co = new Common();
		boolean deleteStatus=true;
		try
		{			
			deleteStatus=co.executeDLOperation(query);
		}
		catch(Exception E)
		{
			fileLogger.error("Exception while authenticating the user "+E.getMessage());
		}
		fileLogger.info("Exit Function : deleteTargetImsi");
		return deleteStatus;
}
	
	
	public String getLatitudeLongitudeForcalc(JSONObject jo)
	{	
		fileLogger.info("Inside Function : getLatitudeLongitudeForcalc");
		try
		{
			if(!jo.getString("STATUS").contains("ERR") && jo.getInt("NEIGH_FLAG") == 2)
			{
				
				int count =0;
				JSONObject neighData = jo.getJSONObject("NEIGH_DATA");
				
				Iterator keys = jo.getJSONObject("NEIGH_DATA").keys();

				while( keys.hasNext() ) {
					String key = (String)keys.next();
					if(neighData.getJSONObject(key).getString("STATUS").equalsIgnoreCase("2"))
					{
						count++;
					}
				}
				
				if(count >= 3)
				{
				   return parseData(jo);	
				}
				else
				{
					return "false";
				}
				
				
			}
		}
		catch(Exception e)
		{
			fileLogger.error("Exception while getting lat lon getLatitudeLongitudeForcalc"+e.getMessage());
		}
		
		fileLogger.info("Exit Function : getLatitudeLongitudeForcalc");
		return "false";
	}
	
	public String parseData(JSONObject jo)
	{
		fileLogger.info("Inside Function : parseData");
		try
		{
			int cellLatCount =0;
			
			JSONObject neighData = jo.getJSONObject("NEIGH_DATA");
			
			Iterator keys = jo.getJSONObject("NEIGH_DATA").keys();

			while( keys.hasNext() ) {
				String i = (String)keys.next();
				if(neighData.getJSONObject(i).getString("STATUS").equalsIgnoreCase("2"))
				{
					String mcc = neighData.getJSONObject(i).getString("MCC");
					String mnc = neighData.getJSONObject(i).getString("MNC");
					String lac = neighData.getJSONObject(i).getString("LAC");
					String cell = neighData.getJSONObject(i).getString("CELL");
					JSONArray latlon = getLatLon(mcc,mnc,lac,cell);
					if(!latlon.getJSONObject(0).getString("lat").equalsIgnoreCase("-1") && !latlon.getJSONObject(0).getString("lon").equalsIgnoreCase("-1"))
					{
						jo.getJSONObject("NEIGH_DATA").getJSONObject(i).put("lat",latlon.getJSONObject(0).getString("lat"));
						jo.getJSONObject("NEIGH_DATA").getJSONObject(i).put("lon", latlon.getJSONObject(0).getString("lon"));
						cellLatCount++;
					}
					else
					{
						
					}
					
				}
			}
			
			
			if(cellLatCount <3)
			{
				//return "false";
				return "-1.0,-1.0";
			}
			else 
			{
				return calulateSubscriberLocation(jo);
			}
			
			
				
		}
		catch(Exception E)
		{
			fileLogger.debug(E.getMessage());
		}
		
		fileLogger.info("Exit Function : parseData");
		return "false";
	}
	
	
	public String calulateSubscriberLocation(JSONObject jo)
	{
		fileLogger.info("Inside Function : calulateSubscriberLocation");
		double x=-1.1;double y=-1.1;
		try
		{
			int sum = 0;
			//JSONArray neighData = jo.getJSONArray("NEIGH_DATA");
			
			JSONObject neighData = jo.getJSONObject("NEIGH_DATA");
			
			Iterator keys = jo.getJSONObject("NEIGH_DATA").keys();

			while( keys.hasNext() ) 
			{
				String i = (String)keys.next();
				if(neighData.getJSONObject(i).getString("STATUS").equalsIgnoreCase("2") && neighData.getJSONObject(i).getString("lat")!= null && neighData.getJSONObject(i).getString("lon")!= null )
				{
					sum = sum+neighData.getJSONObject(i).getInt("RXLVL");				
				}
			}
			
			
			keys = jo.getJSONObject("NEIGH_DATA").keys();
			while( keys.hasNext() ) 
			{
				String i = (String)keys.next();
				if(neighData.getJSONObject(i).getString("STATUS").equalsIgnoreCase("2") && neighData.getJSONObject(i).getString("lat")!= null && neighData.getJSONObject(i).getString("lon")!= null )
				{
					jo.getJSONObject("NEIGH_DATA").getJSONObject(i).put("work", (neighData.getJSONObject(i).getDouble("RXLVL"))/sum);				
				}
			}
			
			
			
			
			x=0.0;y=0.0;
			
			
			keys = jo.getJSONObject("NEIGH_DATA").keys();
			while( keys.hasNext() ) 
			{
				String i = (String)keys.next();
				if(neighData.getJSONObject(i).getString("STATUS").equalsIgnoreCase("2") && neighData.getJSONObject(i).getString("lat")!= null && neighData.getJSONObject(i).getString("lon")!= null )
				{
					Double w = jo.getJSONObject("NEIGH_DATA").getJSONObject(i).getDouble("work");
					x=x+w*neighData.getJSONObject(i).getDouble("lat");
					y=y+w*neighData.getJSONObject(i).getDouble("lon");				
				}
			}
			
			
		}
		catch(Exception e)
		{
			fileLogger.debug(e.getMessage());
		}
		
		HashMap<String,Double> locmap = new HashMap<String,Double>();
		locmap.put("x", x);
		locmap.put("y", y);
		fileLogger.info("Exit Function : calulateSubscriberLocation");
		return x+","+y;
		
	}
	
	public JSONArray getLatLon(String MCC,String MNC,String LACorTAC,String CELL)
	{
		fileLogger.info("Inside Function : getLatLon");
		//String MCC = request.getParameter("MCC");
		//String MNC = request.getParameter("MNC");
		//String LAC = request.getParameter("LAC");
		//String CELL = request.getParameter("CELL");
		String query = "select lat,lon from cell_data where mcc = "+MCC+" and net = "+MNC+" and area= "+LACorTAC+" and cell = "+CELL+" and lat is not null and lat >0 and lon is not null and lon >0 limit 1;";
		String query2 = "select lat,lon from cell_data2 where mcc = "+MCC+" and net = "+MNC+" and area= "+LACorTAC+" and cell = "+CELL+" and lat is not null and lat >0 and lon is not null and lon >0 limit 1;";
		String query3 = "select oprname("+MCC+""+MNC+"::numeric) as opr , getcountry("+MCC+"::numeric) as country;";
		fileLogger.debug(query3);		
		Common co = new Common();
		Statement smt = null;
		Statement smt1 = null;
		Statement smt2 = null;
		Connection con = co.getDbConnection();
		JSONArray ja = new JSONArray();
		try
		{
			smt = con.createStatement();			
			smt1 = con.createStatement();
			
			ResultSet rs = smt.executeQuery(query);
			boolean bb= false;
			JSONObject jo = new JSONObject();
			while(rs.next())
			{
				
				if(rs.getString("lat") != null && rs.getString("lon") !=null )
				{
					jo.put("lat", rs.getString("lat"));
					jo.put("lon", rs.getString("lon"));
				}
				else
				{
					jo.put("lat", "-1");
					jo.put("lon", "-1");
				}
				bb = true;
				
			}				
			if(!bb)
			{
				
				ResultSet rs1 = smt1.executeQuery(query2);
				while(rs1.next())
				{						
					if(rs1.getString("lat") != null && rs1.getString("lon") !=null )
					{
						jo.put("lat", rs.getString("lat"));
						jo.put("lon", rs.getString("lon"));
					}
					else
					{
						jo.put("lat", "-1");
						jo.put("lon", "-1");
					}
					bb = true;
					//ja.put(jo);
				}
			}				
			if(!bb)
			{
				jo.put("lat", "-1");
				jo.put("lon", "-1");
				//ja.put(jo);
			}
			
			smt2 = con.createStatement();
			ResultSet rs2 = smt2.executeQuery(query3);
			while(rs2.next())
			{	
				jo.put("count", rs2.getString("country")==null?"":rs2.getString("country"));
				jo.put("opr",rs2.getString("opr")==null?"":rs2.getString("opr"));
			}
			ja.put(jo);
			
			smt.close();
			smt1.close();
			smt2.close();
			
		}
		catch(Exception E)
		{
			fileLogger.error("Exception while getting the lat lon "+E.getMessage());
		}
		finally
		{
			try
			{
				
				con.close();
			}
			catch(Exception E)
			{
				fileLogger.error("Exception : "+E.getMessage());
			}
		}		
		fileLogger.info("Exit Function : getLatLon");
		return ja;			
	}
	
		public JSONObject getDetailedData(HttpServletRequest request)
	{
			fileLogger.info("Inside Function : getDetailedData");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String operationId = request.getParameter("operationId");
		String nodeWiseTableQuery="";
		String countryWiseTableQuery="";
		if(operationId.equals("-1")){
			nodeWiseTableQuery="select * from getnodewisedatastats_detailed('"+startTime+"','"+endTime+"');";
			countryWiseTableQuery="select * from courntrywisedatastats_detailed('"+startTime+"','"+endTime+"');";
		}else{
			nodeWiseTableQuery="select * from getnodewisedatastats_detailed('"+startTime+"','"+endTime+"',"+operationId+");";
			countryWiseTableQuery="select * from courntrywisedatastats_detailed('"+startTime+"','"+endTime+"',"+operationId+");";
			}
		fileLogger.debug(nodeWiseTableQuery);
		fileLogger.debug(countryWiseTableQuery);
		TwogOperations twogOperations=new TwogOperations();
		String nodeWiseTable=twogOperations.converToHtmlForNodeWiseData(nodeWiseTableQuery);
		String countryWiseTable=twogOperations.converToHtmlForCountryWiseData(countryWiseTableQuery);
		JSONObject detailedData=new JSONObject();
		try{
		detailedData.put("nodeWiseDetailedDataTable",nodeWiseTable);
		detailedData.put("countryWiseDetailedDataTable",countryWiseTable);
		}catch(Exception e){
		}
		fileLogger.info("Exit Function : getDetailedData");
		return detailedData;		
	}
		
		public JSONArray getAllDataTypes()
		{
			fileLogger.info("Inside Function : getAllDataTypes");
			Common co = new Common();
			Statement smt = null;
			Connection con = co.getDbConnection();
			JSONArray ja = new JSONArray();
			try
			{
				smt = con.createStatement();			
				String query = "select * from data_type order by name";
				fileLogger.debug(query);
				ResultSet rs = smt.executeQuery(query);
				
				while(rs.next())
				{
					JSONObject jo = new JSONObject();
					jo.put("name", rs.getString("name"));
					jo.put("description", rs.getString("description"));
					jo.put("related_tables", rs.getString("related_tables"));
					jo.put("status", rs.getString("status"));
					ja.put(jo);
				}			
			}
			catch(Exception E)
			{
				fileLogger.error("Exception while authenticating the user "+E.getMessage());
			}
			finally
			{
				try
				{
					smt.close();
					con.close();
				}
				catch(Exception E)
				{
					
				}
			}	
			fileLogger.info("Exit Function : getAllDataTypes");
			return ja;
	}
	
		public JSONArray getAllOperations()
		{
			fileLogger.info("Inside Function : getAllOperations");
			Common co = new Common();
			Statement smt = null;
			Connection con = co.getDbConnection();
			JSONArray ja = new JSONArray();
			try
			{
				smt = con.createStatement();			
				String query = "select * from oprrationdata order by name";
				fileLogger.debug(query);
				ResultSet rs = smt.executeQuery(query);
				
				while(rs.next())
				{
					JSONObject jo = new JSONObject();
					jo.put("id", rs.getString("id"));
					jo.put("name", rs.getString("name"));
					ja.put(jo);
				}			
			}
			catch(Exception E)
			{
				fileLogger.error("Exception while authenticating the user "+E.getMessage());
			}
			finally
			{
				try
				{
					smt.close();
					con.close();
				}
				catch(Exception E)
				{
					
				}
			}	
			fileLogger.info("Exit Function : getAllOperations");
			return ja;
	}
		
		public HashMap<String,ArrayList<HashMap<String,String>>> getAllBtsInfoByTech()
		{
			fileLogger.info("Inside Function : getAllBtsInfoByTech");
			Common co = new Common();
			Statement smt = null;
			Connection con = co.getDbConnection();
			HashMap<String,ArrayList<HashMap<String,String>>> devicesMapOverTech = new HashMap<String,ArrayList<HashMap<String,String>>>();
			try
			{
				smt = con.createStatement();			
				String query = "select * from view_btsinfo where code in(0,5,13) and ip not in('0.0.0.0','1.1.1.1')";
				fileLogger.debug(query);
				ResultSet rs = smt.executeQuery(query);
				ArrayList<HashMap<String,String>> devicesListOver2G = new ArrayList<HashMap<String,String>>(); 
				ArrayList<HashMap<String,String>> devicesListOver3G = new ArrayList<HashMap<String,String>>();
				ArrayList<HashMap<String,String>> devicesListOver4G = new ArrayList<HashMap<String,String>>();
				while(rs.next())
				{
					HashMap<String,String> devicesMap = new HashMap<String,String>();
					String deviceCode = rs.getString("code");
					devicesMap.put("b_id", rs.getString("b_id"));
					devicesMap.put("ip", rs.getString("ip"));
					devicesMap.put("statuscode", rs.getString("statuscode"));
					devicesMap.put("cellcode", rs.getString("cellcode"));
					devicesMap.put("systemmanager", rs.getString("systemmanager"));
					devicesMap.put("typeid", rs.getString("typeid"));
					devicesMap.put("typename", rs.getString("typename"));
					devicesMap.put("status", rs.getString("status"));
					devicesMap.put("cellstatus", rs.getString("cellstatus"));
					devicesMap.put("dcode", rs.getString("dcode"));
					devicesMap.put("dname", rs.getString("dname"));
					devicesMap.put("sytemid", rs.getString("sytemid"));
					devicesMap.put("code", deviceCode);
					devicesMap.put("grp_id", rs.getString("grp_id"));
					devicesMap.put("config",rs.getString("config"));
					devicesMap.put("hw",rs.getString("hw_capability_name"));
					
					if(deviceCode.equals("5")){
						devicesListOver2G.add(devicesMap);	
					}else if(deviceCode.equals("0") || deviceCode.equals("1") || deviceCode.equals("2")){
						devicesListOver3G.add(devicesMap);	
					}else if(deviceCode.equals("13")){
						devicesListOver4G.add(devicesMap);
					}
				}
					devicesMapOverTech.put("2G",devicesListOver2G);
					devicesMapOverTech.put("3G",devicesListOver3G);
					devicesMapOverTech.put("4G",devicesListOver4G);
			}
			catch(Exception E)
			{
				fileLogger.error("Exception while authenticating the user "+E.getMessage());
			}
			finally
			{
				try
				{
					smt.close();
					con.close();
				}
				catch(Exception E)
				{
					
				}
			}	
			fileLogger.info("Exit Function : getAllBtsInfoByTech");
			return devicesMapOverTech;
	}
		
		public String locateWithNeighbour(JSONObject jsonObjFor2g,String nibIp,int power)
		{
			fileLogger.info("Inside Function : locateWithNeighbour");
			String imsi = "";
			String imei = "";
			String tId = Long.toString(System.currentTimeMillis());
			String flag = "2";
			String mdr = "";
			String HoldTime2g=DBDataService.configParamMap.get("HoldTime");
			fileLogger.error("Inside locateWithNeighbour 3 - HoldTime2g="+HoldTime2g);

			// String tracktimeUpdate="select value from system_properties where key='hold_time';";
			// int holdTimeint=0;
//			 try {
//
//				   JSONArray js = new Operations().getJson(tracktimeUpdate);
//				
//				   holdTimeint=js.getJSONObject(0).getInt("value");
//				  
//			   
//		      }  catch (JSONException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			   }
//			
			
		//	HoldTime2g=holdTimeint+"";
			try
			{
			JSONArray packetArrayFor2g = jsonObjFor2g.getJSONArray("data");
			
			int length = packetArrayFor2g.length();		
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();		
			
			params.add(new BasicNameValuePair("CMD_TYPE","GETALL_SUB_TRACK_REQ" ));
			//2G IMSI HOLD TIME in Trans ID
			params.add(new BasicNameValuePair("TAGS00", HoldTime2g));
			
			params.add(new BasicNameValuePair("TAGS01", imsi));
			params.add(new BasicNameValuePair("TAGS02", imei));
			params.add(new BasicNameValuePair("TAGS03", flag));
			params.add(new BasicNameValuePair("TAGS04", "2"));
			params.add(new BasicNameValuePair("TAGS05", ""));
			params.add(new BasicNameValuePair("TAGS06", ""));
			int i = 7;
			for(int j=0;j<length;j++)
			{
				JSONObject tempObj = packetArrayFor2g.getJSONObject(j);
				String selfOrNeigh="2";
				String cell="1";
				if(tempObj.getString("flag").equalsIgnoreCase("self")){
					selfOrNeigh="3";
				}else{
					cell=tempObj.getString("cell");
				}
				String plmn=tempObj.getString("plmn");
				String lac=tempObj.getString("lac");		
				String arfcn=tempObj.getString("arfcn");
				String bsic=tempObj.getString("bsic");
				String rssi=tempObj.getString("rssi");
				if(power != -999 && tempObj.getString("flag").equalsIgnoreCase("self")) 
				{
					rssi=power+"";
				}
				
				
				if(i<=9){
					params.add(new BasicNameValuePair("TAGS0"+(i++),selfOrNeigh ));
				}else{
					params.add(new BasicNameValuePair("TAGS"+(i++),selfOrNeigh));
				}
				if(i<=9){
					params.add(new BasicNameValuePair("TAGS0"+(i++),plmn));
				}else{
					params.add(new BasicNameValuePair("TAGS"+(i++),plmn));
				}
				if(i<=9){
					params.add(new BasicNameValuePair("TAGS0"+(i++),lac));
				}else{
					params.add(new BasicNameValuePair("TAGS"+(i++),lac));
				}
				if(i<=9){
					params.add(new BasicNameValuePair("TAGS0"+(i++),cell ));
				}else
				{
					params.add(new BasicNameValuePair("TAGS"+(i++),cell));
				}
				if(i<=9){
					params.add(new BasicNameValuePair("TAGS0"+(i++),arfcn));
				}else{
					params.add(new BasicNameValuePair("TAGS"+(i++),arfcn));
				}
				if(i<=9){
					params.add(new BasicNameValuePair("TAGS0"+(i++),bsic));
				}else{
					params.add(new BasicNameValuePair("TAGS"+(i++),bsic));
				}
				if(i<=9){
					params.add(new BasicNameValuePair("TAGS0"+(i++),rssi));
				}else{
					params.add(new BasicNameValuePair("TAGS"+(i++),rssi));
				}
				
			}
			
			
			
			Common co = new Common();
			HashMap<String,String> ll = co.getDbCredential();
			//String myURL = "http://"+ll.get("nib")+"/Trace.html?CMD_TYPE=GETALL_SUB_TRACK_REQ&TAGS00="+tId+"&TAGS001="+imsi+"&TAGS02="+flag+"&";
			//String myURL = "http://"+ll.get("nib")+"/cgi-bin/processData.sh";
			String myURL = "http://"+nibIp+"/cgi-bin/processData_CLI.sh";
			
			
			
			
			try
			{
				if(!flag.equalsIgnoreCase("2"))
					Thread.sleep(10000);
				
				mdr = co.callPostDataUrl(myURL,params);			
				//mdr = co.callURL("http://"+getNibIp(request)+"/temp/target.json");
				
							
			}
			catch(Exception E)
			{
				fileLogger.error("Exception while request"+E.getMessage());
			}
			}
			catch(Exception E)
			{
				fileLogger.error("Exception in locateWithNeighbour method:"+E.getMessage());
			}
			if(mdr != null && mdr.equalsIgnoreCase("ERROR"))
			{
				mdr="{\"STATUS\":\"3\"}";
			}
			return mdr;		
}
		
		public String createNeighbourServerData(JSONObject jsonObjFor2g,String ip,int power)
		{
			fileLogger.info("Inside Function : createNeighbourServerData");
			String selfCellData="";
			String neighbourCellsData="";
			try
			{
			JSONArray packetArrayFor2g = jsonObjFor2g.getJSONArray("data");	
			for(int j=0;j<packetArrayFor2g.length();j++)
			{
				JSONObject tempObj = packetArrayFor2g.getJSONObject(j);
				String plmn=tempObj.getString("plmn");
				String lac=tempObj.getString("lac");
				String arfcn=tempObj.getString("arfcn");
				String bsic=tempObj.getString("bsic");
				String rssi=tempObj.getString("rssi");
				String rowData="";
				if(tempObj.getString("flag").equalsIgnoreCase("self")){
					if(power != -999) 
					{
						rssi=power+"";
					}
					
					rowData=plmn+","+lac+",1,"+arfcn+","+bsic+","+rssi;
					selfCellData="#3,"+rowData;
				}else{
					rowData=plmn+","+lac+","+tempObj.getString("cell")+","+arfcn+","+bsic+","+rssi;
					neighbourCellsData+="$2,"+rowData;
				}
			}
			String completeCellsData=neighbourCellsData+selfCellData;
			completeCellsData=completeCellsData.substring(1);
	   		Common co = new Common();
	   		String[] aa = completeCellsData.split("#");
	   		String[] neighbourData = aa[0].split("\\$");
   			if(aa.length==1){
	   			String insertData = "'"+aa[0].replaceAll(",","','")+"'";
	   			String deletequery = "delete from n_cells_data where ip = '"+ip+"'";
	   			co.executeDLOperation(deletequery);
	   			String query = "INSERT INTO n_cells_data(ip,flag,plmn,lac,cell,arfcn,bsic,rxs,type) values('"+ip+"',"+insertData+",'S')";
	   			if(co.executeDLOperation(query)){
	   				query="update btsmaster set config_applied_status='y' where ip='"+ip+"'";
	   				co.executeDLOperation(query);
	   			}
	   			String plmn=aa[0].split(",")[1];
	   			JSONArray ja=new Operations().getJson("select oprname("+plmn+"::numeric)");
	   			HashMap<String,String> tempHm=new HashMap<String,String>();
	   			tempHm.put(ip,ja.getJSONObject(0).getString("oprname"));
	   			new ConfigOprServer().sendText(tempHm);
   			}else{
   				
	   		//String selfData = aa[1];
		   		int count =0;
	   			for(String val :neighbourData) 
		   		{
		   			String insertData = "'"+val.replaceAll(",","','")+"'";
		   			if(count ==0) 
		   			{
		   				String deletequery = "delete from n_cells_data where ip = '"+ip+"' and type='N'";
			   			co.executeDLOperation(deletequery);
			   			count++;
		   			}
		   			
		   			
		   			String query = "INSERT INTO n_cells_data(ip,flag,plmn,lac,cell,arfcn,bsic,rxs,type) values('"+ip+"',"+insertData+",'N')";
		   			co.executeDLOperation(query);
		   		}
		   			String insertData = "'"+aa[1].replaceAll(",","','")+"'";
		   			String deletequery = "delete from n_cells_data where ip = '"+ip+"' and type='S'";
		   			co.executeDLOperation(deletequery);
		   			String query = "INSERT INTO n_cells_data(ip,flag,plmn,lac,cell,arfcn,bsic,rxs,type) values('"+ip+"',"+insertData+",'S')";
		   			if(co.executeDLOperation(query)){
		   				query = "update btsmaster set config_applied_status='y' where ip='"+ip+"'";
		   				co.executeDLOperation(query);
		   			}
		   			String plmn=aa[1].split(",")[1];
		   			JSONArray ja=new Operations().getJson("select oprname("+plmn+"::numeric)");
		   			HashMap<String,String> tempHm=new HashMap<String,String>();
		   			tempHm.put(ip,ja.getJSONObject(0).getString("oprname"));
		   			fileLogger.debug("sending to browser websocket");
		   			new ConfigOprServer().sendText(tempHm);
		   			fileLogger.debug("sent");
   			}
		   		
			}
	   		catch(Exception e)
	   		{
				e.printStackTrace();
				
				return "fail";
				
			}
			fileLogger.info("Exit Function : createNeighbourServerData");
	   		return "SUCCESS";
}
		
		public JSONArray getAllCountryList()
		{		
				String query = "select distinct country from view_plmn_opr_country where country is not null";
				fileLogger.debug(query);
				return getJson(query);		
		}
		
		public LinkedHashMap<String,String> setParamsForConfig(JSONObject tempJsonObjectFor3g,String ip,int power){
			fileLogger.info("Inside Function : setParamsForConfig");
			Common co = new Common();
			Statement smt = null;
			Connection con = co.getDbConnection();
			LinkedHashMap<String,String> param = new  LinkedHashMap<String,String>();
			try{
			smt = con.createStatement();
			String query=null;
			query = "select * from btsmaster where ip ='"+ip+"'";
			fileLogger.debug(query);
			ResultSet rs = smt.executeQuery(query);
			String config="";
			while(rs.next())
			{
				param.put("cmdType", "GET_GET_CURR_STATUS");
			    param.put("CMD_CODE", "GET_CURR_STATUS");
			    param.put("systemIP", rs.getString("ip"));
			    param.put("systemId", rs.getString("sytemid"));
			    param.put("systemCode", rs.getString("devicetypeid"));
			    param.put("status", rs.getString("status"));
			    param.put("adminState", rs.getString("adminstate"));
			    param.put("id",rs.getString("b_id"));
			    config=rs.getString("config");
			    param.put("devicetypeid",rs.getString("devicetypeid"));
			}
			JSONObject configTree=new JSONObject(config);
			JSONArray cellConfigurationArr=tempJsonObjectFor3g.getJSONArray("data");
			JSONArray intraFreqJsonArray=new JSONArray();
			JSONArray interFreqJsonArray=new JSONArray();
			JSONArray interRatJsonArray=new JSONArray();
			String oldDluarfcn="";
			String newDluarfcn="";
			String newUluarfcn="";
			String mccForIntraFreqNeigh="";
			String mncForIntraFreqNeigh="";
			String lacForIntraFreqNeigh="";
			String cellForIntraFreqNeigh="";
			String pscForIntraFreqNeigh="";
			int dlUarfcnToUluarfcnFormula=0;
			for(int i=0;i<cellConfigurationArr.length();i++){
				JSONObject tempJsonObjectForCell=cellConfigurationArr.getJSONObject(i);	
				if(tempJsonObjectForCell.getString("flag").equalsIgnoreCase("self")){
					mccForIntraFreqNeigh=tempJsonObjectForCell.getString("plmn").substring(0,3);
					mncForIntraFreqNeigh=tempJsonObjectForCell.getString("plmn").substring(3);
					lacForIntraFreqNeigh=tempJsonObjectForCell.getString("lac");
					cellForIntraFreqNeigh=tempJsonObjectForCell.getString("cell");
					pscForIntraFreqNeigh=tempJsonObjectForCell.getString("psc");
					oldDluarfcn=configTree.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").getString("DL_UARFCN");
					break;
				}
			}
			
			for(int i=0;i<cellConfigurationArr.length();i++){
				JSONObject tempJsonObjectForCell=cellConfigurationArr.getJSONObject(i);
				if(tempJsonObjectForCell.getString("flag").equalsIgnoreCase("self")){
					configTree.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").getJSONObject("PLMN_ID").put("MCC",tempJsonObjectForCell.getString("plmn").substring(0,3));
					configTree.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").getJSONObject("PLMN_ID").put("MNC",tempJsonObjectForCell.getString("plmn").substring(3));
					//configTree.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").put("CELL_ID",tempJsonObjectForCell.getString("cell"));
					configTree.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").put("CELL_ID","1");
					newDluarfcn=tempJsonObjectForCell.getString("uarfcn");
					configTree.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").put("DL_UARFCN",newDluarfcn);
					dlUarfcnToUluarfcnFormula=new PossibleConfigurations().getFormulaForTheGivenUarfcn(Integer.parseInt(newDluarfcn));
					if(dlUarfcnToUluarfcnFormula!=0){
					newUluarfcn=Integer.toString(Integer.parseInt(newDluarfcn)+(dlUarfcnToUluarfcnFormula)); 
					configTree.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").put("UL_UARFCN",newUluarfcn);
					}	
					configTree.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").put("UL_UARFCN",newUluarfcn);
					configTree.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").put("PRI_SCRAM_CODE",tempJsonObjectForCell.getString("psc"));
					
					
					if(power != -999) 
					{
						configTree.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").put("TOTAL_TX_POWER",power+"");
					}
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					int HoldTime=Integer.parseInt(DBDataService.configParamMap.get("HoldTime"));

//					String tracktimeUpdate="select value from system_properties where key='hold_time';";
//					int holdTimeint=0;
//				    try {
//				    	JSONArray js = new Operations().getJson(tracktimeUpdate);
//					
//				    	holdTimeint=js.getJSONObject(0).getInt("value");
//					  
//						
//				    } catch (JSONException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//				    }
//					
//					
//					
					
					
					HoldTime*=1000;
					configTree.getJSONObject("SYS_PARAMS").getJSONObject("SUFI_PARAMS").put("SUB_HOLD_TIMER",HoldTime+"");
					fileLogger.error("Inside setParamsForConfig - 3g HoldTime="+HoldTime);
					
					
					
					
					
					
					
					
					
					
					
					
					configTree.getJSONObject("CELL_PARAMS").getJSONObject("REDIRECTION_INFO").getJSONObject("SUFI_OF_REDIR_INFO").put("RDL_UARFCN",newDluarfcn);
					configTree.getJSONObject("CELL_PARAMS").getJSONObject("REDIRECTION_INFO").getJSONObject("SUFI_OPER_REDIR_INFO").put("RDL_UARFCN",newDluarfcn);
				}else{
					JSONObject tempJsonObject=new JSONObject();
					if(!tempJsonObjectForCell.getString("uarfcn").equals("")){
						if(tempJsonObjectForCell.getString("uarfcn").equals(oldDluarfcn)){
							fileLogger.debug("intra freq neighbour");
							tempJsonObject.put("PSC",pscForIntraFreqNeigh);
							tempJsonObject.put("Q_OFFSET_1S","0");
							tempJsonObject.put("Q_OFFSET_2S","0");
							tempJsonObject.put("Q_QUALMIN","-24");
							tempJsonObject.put("Q_RXLEVMIN","-25");
							tempJsonObject.put("CELL_ID",cellForIntraFreqNeigh);
							tempJsonObject.put("LAC",lacForIntraFreqNeigh);
							JSONObject jo=new JSONObject();
							jo.put("MCC",mccForIntraFreqNeigh);
							jo.put("MNC",mncForIntraFreqNeigh);
							tempJsonObject.put("INTRA_PLMN_ID",jo);
							intraFreqJsonArray.put(tempJsonObject);
						}else{
							tempJsonObject.put("PSC",tempJsonObjectForCell.getString("psc"));
							tempJsonObject.put("DL_UARFCN",tempJsonObjectForCell.getString("uarfcn"));
							tempJsonObject.put("PCPICH_TX_POWER","10");
							tempJsonObject.put("Q_OFFSET_1S","0");
							tempJsonObject.put("Q_OFFSET_2S","0");
							tempJsonObject.put("Q_QUALMIN","-24");
							tempJsonObject.put("Q_RXLEVMIN","-25");
							tempJsonObject.put("CELL_ID",tempJsonObjectForCell.getString("cell"));
							tempJsonObject.put("LAC",tempJsonObjectForCell.getString("lac"));
							JSONObject jo=new JSONObject();
							jo.put("MCC",tempJsonObjectForCell.getString("plmn").substring(0,3));
							jo.put("MNC",tempJsonObjectForCell.getString("plmn").substring(3));
							tempJsonObject.put("INTER_PLMN_ID",jo);
							interFreqJsonArray.put(tempJsonObject);
						}
					}else{
						tempJsonObject.put("Q_RXLEVMIN","-25");
						tempJsonObject.put("LAC",tempJsonObjectForCell.getString("lac"));
						tempJsonObject.put("CELL_ID",tempJsonObjectForCell.getString("cell"));
						tempJsonObject.put("Q_OFFSET1S_N","0");
						tempJsonObject.put("CELLINDIVIDUALOFFSET","1");
						tempJsonObject.put("NCC","1");
						tempJsonObject.put("BCC","1");
						tempJsonObject.put("FREQ_BAND","0");
						tempJsonObject.put("BCCH_ARFCN","1023");
						JSONObject jo=new JSONObject();
						jo.put("MCC",tempJsonObjectForCell.getString("plmn").substring(0,3));
						jo.put("MNC",tempJsonObjectForCell.getString("plmn").substring(3));
						tempJsonObject.put("RAT_PLMN_ID",jo);
						interRatJsonArray.put(tempJsonObject);
					}
				}
			}
			ApiCommon apiCommon=new ApiCommon();
		    String defaultSufiConfig=apiCommon.getSufiConfigurationWithDefaultValues();
		    JSONObject defaultJson=new JSONObject(defaultSufiConfig);
			if(intraFreqJsonArray.length()==0){
				fileLogger.debug("in intra freq");
				JSONObject selfJsonObject=new JSONObject();
				selfJsonObject.put("PSC",pscForIntraFreqNeigh);
				selfJsonObject.put("Q_OFFSET_1S","0");
				selfJsonObject.put("Q_OFFSET_2S","0");
				selfJsonObject.put("Q_QUALMIN","-24");
				selfJsonObject.put("Q_RXLEVMIN","-25");
				selfJsonObject.put("CELL_ID",cellForIntraFreqNeigh);
				selfJsonObject.put("LAC",lacForIntraFreqNeigh);
				JSONObject joSelf=new JSONObject();
				joSelf.put("MCC",mccForIntraFreqNeigh);
				joSelf.put("MNC",mncForIntraFreqNeigh);
				selfJsonObject.put("INTRA_PLMN_ID",joSelf);
				intraFreqJsonArray.put(selfJsonObject);
				//intraFreqJsonArray.put(defaultJson.getJSONObject("CELL_PARAMS").getJSONObject("SIB_INFO").getJSONObject("NEIGH_CELL_LIST").getJSONArray("INTRA_FREQ").getJSONObject(0));
			}
			if(interFreqJsonArray.length()==0){
				fileLogger.debug("in inter freq");
				interFreqJsonArray.put(defaultJson.getJSONObject("CELL_PARAMS").getJSONObject("SIB_INFO").getJSONObject("NEIGH_CELL_LIST").getJSONArray("INTER_FREQ").getJSONObject(0));
			}
			if(interRatJsonArray.length()==0){
				fileLogger.debug("in inter rat");
				interRatJsonArray.put(defaultJson.getJSONObject("CELL_PARAMS").getJSONObject("SIB_INFO").getJSONObject("NEIGH_CELL_LIST").getJSONArray("INTER_RAT").getJSONObject(0));
			}
			configTree.getJSONObject("CELL_PARAMS").getJSONObject("SIB_INFO").getJSONObject("NEIGH_CELL_LIST").put("INTRA_FREQ",intraFreqJsonArray);
			configTree.getJSONObject("CELL_PARAMS").getJSONObject("SIB_INFO").getJSONObject("NEIGH_CELL_LIST").put("INTER_FREQ",interFreqJsonArray);
			configTree.getJSONObject("CELL_PARAMS").getJSONObject("SIB_INFO").getJSONObject("NEIGH_CELL_LIST").put("INTER_RAT",interRatJsonArray);
			param.put("config",configTree.toString());
			}catch(Exception E){
			fileLogger.error("Exception in setSufiConfigOnRemoteDevice :"+E.getMessage());
			}
			fileLogger.info("Exit Function : setParamsForConfig");
			return param;
			}
		
		public LinkedHashMap<String,String> setParamsFor4gConfig(JSONObject tempJsonObjectFor4g,String ip,int power,int l1_att ){
			fileLogger.info("Inside Function : setParamsFor4gConfig");
			Common co = new Common();
			Statement smt = null;
			Connection con = co.getDbConnection();
			LinkedHashMap<String,String> param = new  LinkedHashMap<String,String>();
			try{
			smt = con.createStatement();
			String query=null;
			query = "select * from btsmaster where ip ='"+ip+"'";
			fileLogger.debug(query);
			ResultSet rs = smt.executeQuery(query);
			String config="";
			while(rs.next())
			{
				
				int templ1= Integer.parseInt((rs.getString("tmp")==null) ? "99" : rs.getString("tmp"));
				
				param.put("cmdType", "GET_GET_CURR_STATUS");
			    param.put("CMD_CODE", "GET_CURR_STATUS");
			    param.put("systemIP", rs.getString("ip"));
			    param.put("systemId", rs.getString("sytemid"));
			    param.put("systemCode", rs.getString("devicetypeid"));
			    param.put("status", rs.getString("status"));
			    param.put("adminState", rs.getString("adminstate"));
			    
			    param.put("id",rs.getString("b_id"));
			    config=rs.getString("config");
			    param.put("devicetypeid",rs.getString("devicetypeid"));
			    if(templ1!=l1_att&& l1_att!=-999) {
						DBDataService.IsRestartRequired=true;
					}
			}
			JSONObject configTree=new JSONObject(config);
			JSONArray cellConfigurationArr=tempJsonObjectFor4g.getJSONArray("data");
			JSONArray intraFreqJsonArray=new JSONArray();
			JSONArray interFreqJsonArray=new JSONArray();
			JSONArray interRat2gJsonArray=new JSONArray();
			JSONArray interRat3gJsonArray=new JSONArray();
			String oldDlearfcn="";
			String newDlearfcn="";
			String newUlearfcn="";
			String mccForIntraFreqNeigh="";
			String mncForIntraFreqNeigh="";
			String tacForIntraFreqNeigh="";
			String cellForIntraFreqNeigh="";
			String pciForIntraFreqNeigh="";
			int dlEarfcnToUlearfcnFormula=0;
			for(int i=0;i<cellConfigurationArr.length();i++){
				JSONObject tempJsonObjectForCell=cellConfigurationArr.getJSONObject(i);	
				if(tempJsonObjectForCell.getString("flag").equalsIgnoreCase("self")){
					mccForIntraFreqNeigh=tempJsonObjectForCell.getString("plmn").substring(0,3);
					mncForIntraFreqNeigh=tempJsonObjectForCell.getString("plmn").substring(3);
					tacForIntraFreqNeigh=tempJsonObjectForCell.getString("tac");
					cellForIntraFreqNeigh=tempJsonObjectForCell.getString("cell");
					pciForIntraFreqNeigh=tempJsonObjectForCell.getString("pci");
					oldDlearfcn=configTree.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").getString("DL_EARFCN");
					break;
				}
			}
			
			for(int i=0;i<cellConfigurationArr.length();i++){
				JSONObject tempJsonObjectForCell=cellConfigurationArr.getJSONObject(i);
				if(tempJsonObjectForCell.getString("flag").equalsIgnoreCase("self")){
					configTree.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").getJSONObject("PLMN_ID").put("MCC",tempJsonObjectForCell.getString("plmn").substring(0,3));
					configTree.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").getJSONObject("PLMN_ID").put("MNC",tempJsonObjectForCell.getString("plmn").substring(3));
					//configTree.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").put("CELL_ID",tempJsonObjectForCell.getString("cell"));
					configTree.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").put("CELL_ID","1");
					configTree.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").put("Freq_Band_Indicator",tempJsonObjectForCell.getString("band"));
					newDlearfcn=tempJsonObjectForCell.getString("earfcn");
					configTree.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").put("DL_EARFCN",newDlearfcn);
					dlEarfcnToUlearfcnFormula=new PossibleConfigurations().getFormulaForTheGivenEarfcn(Integer.parseInt(newDlearfcn));
					if(dlEarfcnToUlearfcnFormula!=0){
					newUlearfcn=Integer.toString(Integer.parseInt(newDlearfcn)+(dlEarfcnToUlearfcnFormula)); 
					configTree.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").put("UL_EARFCN",newUlearfcn);
					}
					else
					{
						configTree.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").put("UL_EARFCN",newDlearfcn);
					}
					configTree.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").put("PHY_CELL_ID",tempJsonObjectForCell.getString("pci"));
					
//					int templ1=Integer.parseInt(configTree.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").get("L1_ATT").toString());
//					if(templ1!=l1_att&& l1_att!=-999) {
					//	DBDataService.IsRestartRequired=true;
//					}
					
					
					if(l1_att != -999) {
						
						configTree.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").put("L1_ATT",l1_att+"");
					}
					if(power != -999) 
					{
						configTree.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").put("Reference_Signal_Power",power+"");
						
						
					}
					
					
					
					
					
					int HoldTime=1;//Roli requirement Sanjay sir 24 Feb 21 Integer.parseInt(DBDataService.configParamMap.get("HoldTime"));
//					
//					String tracktimeUpdate="select value from system_properties where key='hold_time';";
//					int holdTimeint=0;
//				    try {
//				    		JSONArray js = new Operations().getJson(tracktimeUpdate);
//						
//						   holdTimeint=js.getJSONObject(0).getInt("value");
//						  
//				    
//						 
//				    } catch (JSONException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//				    }
//					
//					
//					
//					
					
					HoldTime*=1000;
					fileLogger.debug("Inside setParamsForConfig - 4g holdTimeint now ="+HoldTime);
					
					  
					
					
					configTree.getJSONObject("SYS_PARAMS").getJSONObject("SUFI_PARAMS").put("SUB_HOLD_TIMER",HoldTime+"");
					
					
					
					
					
					
					
					
					
					
					
					
					
					configTree.getJSONObject("CELL_PARAMS").getJSONObject("REDIRECTION_INFO").getJSONObject("SUFI_OF_REDIR_INFO").put("RDL_EARFCN",newDlearfcn);
					configTree.getJSONObject("CELL_PARAMS").getJSONObject("REDIRECTION_INFO").getJSONObject("SUFI_OPER_REDIR_INFO").put("RDL_EARFCN",newDlearfcn);
				}else{
					JSONObject tempJsonObject=new JSONObject();
					if(!tempJsonObjectForCell.getString("earfcn").equals("")){
						if(tempJsonObjectForCell.getString("earfcn").equals(oldDlearfcn)){
							fileLogger.debug("intra freq neighbour");
							tempJsonObject.put("PCI",pciForIntraFreqNeigh);
							//tempJsonObject.put("CELL_ID",cellForIntraFreqNeigh);
							tempJsonObject.put("CELL_ID","1");
							tempJsonObject.put("TAC",tacForIntraFreqNeigh);
							JSONObject jo=new JSONObject();
							jo.put("MCC",mccForIntraFreqNeigh);
							jo.put("MNC",mncForIntraFreqNeigh);
							tempJsonObject.put("INTRA_PLMN_ID",jo);
							intraFreqJsonArray.put(tempJsonObject);
						}else{
							JSONObject jo=new JSONObject();
							jo.put("PCI1",tempJsonObjectForCell.getString("pci"));
							tempJsonObject.put("PCI_List",jo);
							tempJsonObject.put("PCI",tempJsonObjectForCell.getString("psc"));
							tempJsonObject.put("DL_Carrier_Frequency",tempJsonObjectForCell.getString("earfcn"));
							//tempJsonObject.put("CELL_ID",tempJsonObjectForCell.getString("cell"));
							tempJsonObject.put("CELL_ID","1");
							tempJsonObject.put("TAC",tempJsonObjectForCell.getString("tac"));
							jo=new JSONObject();
							jo.put("MCC",tempJsonObjectForCell.getString("plmn").substring(0,3));
							jo.put("MNC",tempJsonObjectForCell.getString("plmn").substring(3));
							tempJsonObject.put("INTER_PLMN_ID",jo);
							interFreqJsonArray.put(tempJsonObject);
						}
					}else if(!tempJsonObjectForCell.getString("arfcn").equals("")){
						tempJsonObject.put("Starting_ARFCN",tempJsonObjectForCell.getString("arfcn"));
						tempJsonObject.put("NCC_Permitted","1");
						tempJsonObject.put("Band_Indicator","0");
						tempJsonObject.put("Priority","1");
						JSONObject jo=new JSONObject();
						jo.put("ARFCN1",tempJsonObjectForCell.getString("arfcn"));
						tempJsonObject.put("Explicit_List_ARFCN",jo);
						tempJsonObject.put("CELL_ID",tempJsonObjectForCell.getString("cell"));
						tempJsonObject.put("LAC",tempJsonObjectForCell.getString("lac"));
						jo=new JSONObject();
						jo.put("MCC",tempJsonObjectForCell.getString("plmn").substring(0,3));
						jo.put("MNC",tempJsonObjectForCell.getString("plmn").substring(3));
						tempJsonObject.put("INTER_RAT_PLMN_ID",jo);
						interRat2gJsonArray.put(tempJsonObject);
					}else{
						tempJsonObject.put("DL_Carrier_Frequency",tempJsonObjectForCell.getString("uarfcn"));
						tempJsonObject.put("Priority","1");
						tempJsonObject.put("CELL_ID",tempJsonObjectForCell.getString("cell"));
						tempJsonObject.put("LAC",tempJsonObjectForCell.getString("lac"));
						JSONObject jo=new JSONObject();
						jo.put("MCC",tempJsonObjectForCell.getString("plmn").substring(0,3));
						jo.put("MNC",tempJsonObjectForCell.getString("plmn").substring(3));
						tempJsonObject.put("INTER_RAT_PLMN_ID",jo);
						interRat3gJsonArray.put(tempJsonObject);
					}
				}
			}
			ApiCommon apiCommon=new ApiCommon();
		    String defaultSufiConfig=apiCommon.getESufiConfigurationWithDefaultValues();
		    JSONObject defaultJson=new JSONObject(defaultSufiConfig);
			if(intraFreqJsonArray.length()==0){
				fileLogger.debug("in intra freq");
				JSONObject selfJsonObject=new JSONObject();
				selfJsonObject.put("PCI",pciForIntraFreqNeigh);
				//selfJsonObject.put("CELL_ID",cellForIntraFreqNeigh);
				selfJsonObject.put("CELL_ID","1");
				selfJsonObject.put("TAC",tacForIntraFreqNeigh);
				JSONObject joSelf=new JSONObject();
				joSelf.put("MCC",mccForIntraFreqNeigh);
				joSelf.put("MNC",mncForIntraFreqNeigh);
				selfJsonObject.put("INTRA_PLMN_ID",joSelf);
				intraFreqJsonArray.put(selfJsonObject);
				//intraFreqJsonArray.put(defaultJson.getJSONObject("CELL_PARAMS").getJSONObject("SIB_INFO").getJSONObject("NEIGH_CELL_LIST").getJSONArray("INTRA_FREQ").getJSONObject(0));
			}
			
			if(interFreqJsonArray.length()==0){
				fileLogger.debug("in inter freq");
				interFreqJsonArray.put(defaultJson.getJSONObject("CELL_PARAMS").getJSONObject("SIB_INFO").getJSONObject("NEIGH_CELL_LIST").getJSONArray("INTER_FREQ").getJSONObject(0));
			}
			if(interRat2gJsonArray.length()==0){
				fileLogger.debug("in inter rat 2g");
				interRat2gJsonArray.put(defaultJson.getJSONObject("CELL_PARAMS").getJSONObject("SIB_INFO").getJSONObject("NEIGH_CELL_LIST").getJSONArray("INTER_RAT_2G").getJSONObject(0));
			}
			if(interRat3gJsonArray.length()==0){
				fileLogger.debug("in inter rat 3g");
				interRat3gJsonArray.put(defaultJson.getJSONObject("CELL_PARAMS").getJSONObject("SIB_INFO").getJSONObject("NEIGH_CELL_LIST").getJSONArray("INTER_RAT_3G").getJSONObject(0));
			}
			
			configTree.getJSONObject("CELL_PARAMS").getJSONObject("SIB_INFO").getJSONObject("NEIGH_CELL_LIST").put("INTRA_FREQ",intraFreqJsonArray);
			configTree.getJSONObject("CELL_PARAMS").getJSONObject("SIB_INFO").getJSONObject("NEIGH_CELL_LIST").put("INTER_FREQ",interFreqJsonArray);
			configTree.getJSONObject("CELL_PARAMS").getJSONObject("SIB_INFO").getJSONObject("NEIGH_CELL_LIST").put("INTER_RAT_2G",interRat2gJsonArray);
			configTree.getJSONObject("CELL_PARAMS").getJSONObject("SIB_INFO").getJSONObject("NEIGH_CELL_LIST").put("INTER_RAT_3G",interRat3gJsonArray);
			param.put("config",configTree.toString());
			}catch(Exception E){
			fileLogger.debug("Exception in setEsufiConfigOnRemoteDevice :"+E.getMessage());
			}
			return param;
			}
		

		   public void setSufiConfigOnDb(LinkedHashMap<String,String> param)
		   {	//String query = "select  btsmaster.b_id,btsmaster.ip,btsmaster.status,btsnetworktype.name from btsmaster inner join btsnetworktype on(typeid = n_id)";
			   Common co = new Common();
			   String query = "update btsmaster set config = '"+param.get("config")+"',config_applied_status='y' where ip='"+param.get("ip")+"' and b_id="+param.get("id");
			   fileLogger.debug(query);
			   co.executeDLOperation(query);
				try{
					JSONObject configTree=new JSONObject(param.get("config"));
					String mcc=configTree.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").getJSONObject("PLMN_ID").getString("MCC");
					String mnc=configTree.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").getJSONObject("PLMN_ID").getString("MNC");
					JSONArray ja=new Operations().getJson("select oprname("+mcc+mnc+"::numeric)");
					HashMap<String,String> tempHm=new HashMap<String,String>();
					tempHm.put(param.get("ip"),ja.getJSONObject(0).getString("oprname"));
					new ConfigOprServer().sendText(tempHm);
					}catch(Exception E){
						fileLogger.error("Exception in setSufiConfigOnDb of class Operations with message:"+E.getMessage());
					}
		    }
		   
		   public void setEsufiConfigOnDb(LinkedHashMap<String,String> param)
		   {	//String query = "select  btsmaster.b_id,btsmaster.ip,btsmaster.status,btsnetworktype.name from btsmaster inner join btsnetworktype on(typeid = n_id)";
			   Common co = new Common();
			   String query = "update btsmaster set config = '"+param.get("config")+"',config_applied_status='y' where ip='"+param.get("ip")+"' and b_id="+param.get("id");
			   fileLogger.debug(query);
			   co.executeDLOperation(query);
				try{
					JSONObject configTree=new JSONObject(param.get("config"));
					String mcc=configTree.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").getJSONObject("PLMN_ID").getString("MCC");
					String mnc=configTree.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").getJSONObject("PLMN_ID").getString("MNC");
					JSONArray ja=new Operations().getJson("select oprname("+mcc+mnc+"::numeric)");
					HashMap<String,String> tempHm=new HashMap<String,String>();
					tempHm.put(param.get("ip"),ja.getJSONObject(0).getString("oprname"));
					new ConfigOprServer().sendText(tempHm);
					}catch(Exception E){
						fileLogger.error("Exception in setEsufiConfigOnDb of class Operations with message:"+E.getMessage());
					}
				fileLogger.info("Exit Function : setParamsFor4gConfig");
		    }
		   
		   public static void setBistObject(Bist bistObj) 
		   {
			   bist=bistObj;
		   }
		   
			public JSONArray getEventReport(String query)
			{
				fileLogger.info("Inside Function : getEventReport");
				Common co = new Common();
				Statement smt = null;
				Connection con = co.getDbConnection();
				
				JSONArray ja = new JSONArray();
				
				
				
				try
				{
					smt = con.createStatement();			
					//String query = "select oprrationdata.id,btsoprparma.id btsprmid,oprrationdata.name,loc,btstype.name as bts,atype,again,aheight,aelevation,adirection,ttype from oprrationdata inner join btsoprparma on (oprrationdata.id = btsoprparma.oprid) left join btstype on(btsoprparma.btsid = btstype.id) order by btsprmid desc;";
					fileLogger.debug(query);
					ResultSet rs = smt.executeQuery(query);
					ResultSetMetaData rm = rs.getMetaData();
					int totalColumns = rm.getColumnCount();
					ArrayList<String> columns = new ArrayList<String>();
					
					for(int i=1;i<=totalColumns;i++ )
					{
						columns.add(rm.getColumnName(i));
					}
					int count = 0;
					while(rs.next())
					{				
						
						count++;
						if(count >= 100000)
						{
							JSONObject jb = new JSONObject();
							jb.put("result","fail");
							jb.put("msg","Limit exceed");
							ja = null;
							ja = new JSONArray();
							ja.put(jb);
							break;
						}
						
						//JSONObject jb = new JSONObject();
						for(String cname:columns)
						{
							if(rs.getString(cname) == null)
							{
								//jb.put(cname, "");
								ja.put(new JSONObject(""));
							}
							else
							{
								//jb.put(cname, rs.getString(cname));
								ja.put(new JSONObject(rs.getString(cname)));
							}
							
						}
						//ja.put(jb);
					}			
				}
				catch(Exception E)
				{
					fileLogger.error("Exception while authenticating the user "+E.getMessage());
				}
				finally
				{
					try
					{
						smt.close();
						con.close();
					}
					catch(Exception E)
					{
						
					}
				}	
				return ja;
			}
			
			public JSONArray getSubInventory(String query)
			{
				Common co = new Common();
				Statement smt = null;
				Connection con = co.getDbConnection();
				
				
				//ArrayList<LinkedHashMap<String,String>> subInventoryList= new ArrayList<LinkedHashMap<String,String>>();
				JSONArray subInventoryList = new JSONArray();
				try
				{
					smt = con.createStatement();			
					//String query = "select oprrationdata.id,btsoprparma.id btsprmid,oprrationdata.name,loc,btstype.name as bts,atype,again,aheight,aelevation,adirection,ttype from oprrationdata inner join btsoprparma on (oprrationdata.id = btsoprparma.oprid) left join btstype on(btsoprparma.btsid = btstype.id) order by btsprmid desc;";
					fileLogger.debug(query);
					ResultSet rs = smt.executeQuery(query);
					int count = 0;
					while(rs.next())
					{				
						LinkedHashMap<String,String> subInventoryMap = new LinkedHashMap<String,String>();
						subInventoryMap.put("Name", rs.getString("show_name"));
						subInventoryMap.put("IP", rs.getString("ip"));
						subInventoryMap.put("Status", rs.getString("status"));
						subInventoryList.put(subInventoryMap);
						count++;
					}
					
					if(count==0){
						LinkedHashMap<String,String> subInventoryMap = new LinkedHashMap<String,String>();
						subInventoryMap.put("Status","Inventory Not Available");
						subInventoryList.put(subInventoryMap);
					}
					
				}
				catch(Exception E)
				{
					LinkedHashMap<String,String> subInventoryMap = new LinkedHashMap<String,String>();
					subInventoryMap.put("Status","Inventory Not Available");
					subInventoryList.put(subInventoryMap);
					fileLogger.error("Exception in  "+E.getMessage());
				}
				finally
				{
					try
					{
						smt.close();
						con.close();
					}
					catch(Exception E)
					{
						
					}
				}	
				fileLogger.info("Exit Function : getEventReport");
				return subInventoryList;
			}
			
			public String[] getDistanceFromTAAndRxl(String stype,String band,int aType,int ta,int rxlValid){
				fileLogger.info("Inside Function : getDistanceFromTAAndRxl");
				String[] distanceArr=new String[2];
				String calcBasis="";
				String delayCalcBasis="";
				fileLogger.debug("@distance in getDistanceFromTAAndRxl");
				double distance=0.0;
				double distanceTA=0.0;
				String falconType=DBDataService.configParamMap.get("falcontype");
				fileLogger.debug("@track falconType is :"+falconType);
				String techForPowerComp="3G";
				
				if(stype.equalsIgnoreCase("2g")){
					distanceTA=(ta+1)*550;
					delayCalcBasis="TA";
					//if(falconType.equalsIgnoreCase("standard")){
						techForPowerComp="2G";
					//}
				} else if(stype.equalsIgnoreCase("3g")) {
					distanceTA = ta * 234;
					delayCalcBasis = "PD";
				}
				else if(stype.equalsIgnoreCase("4g")) {
					distanceTA = (ta) * 78;
					delayCalcBasis = "TA";
					distanceArr[0]=Double.toString(distanceTA);
					distanceArr[1]=delayCalcBasis;
					
					return distanceArr;
				}
				else{
					distanceTA = (ta) * 118;
					delayCalcBasis = "PD";
				}
				fileLogger.debug("@distance ta:"+ta);
				fileLogger.debug("@distance rxlValid"+rxlValid);
				//double configurdTxPower=DBDataService.configurdTxPower;
				double configuredFreq=DBDataService.configuredFreq;
				fileLogger.debug("@distance configuredFreq:"+configuredFreq);
				double compensation=0.0;
				
				int systemType=DBDataService.getSystemType();
                String compType="";
                if(systemType==0){
                	compType="comp_single_no_rffe";
                }else if(systemType==1){
                	compType="comp_single_rffe";
                }else{
                	compType="compensation";
                }
                
				JSONArray compArr=new Operations().getJson("select "+compType+" from pow_compensation where system_id ='"+DBDataService.SystemId +"' and tech='"+techForPowerComp+"' and band='"+band+"' and ant_type="+aType+"");
				if(compArr.length()>0){
					try {
						JSONObject compObj=compArr.getJSONObject(0);
						compensation=compObj.getDouble(compType);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				fileLogger.debug("@distance compensation is :"+compensation);
				//configurdTxPower=configurdTxPower+compensation;
				double configurdTxPower=compensation;
				fileLogger.debug("@distance configurdTxPower:"+configurdTxPower);
				double log10DistKm = (configurdTxPower - rxlValid - (20*Math.log10(configuredFreq)) - 32.44)/20 ;
				fileLogger.debug("@distance log10DistKm:"+log10DistKm);
				double distanceRSSI = 1000*Math.pow(10, log10DistKm);
				fileLogger.debug("@distance rssi distance:"+distanceRSSI);
				if(distanceTA>distanceRSSI){
					distance=distanceRSSI;
					calcBasis="Rxl";
				}else{
					distance=distanceTA;
					calcBasis=delayCalcBasis;
				}
				distanceArr[0]=Double.toString(distance);
				distanceArr[1]=calcBasis;
				fileLogger.info("Exit Function : getDistanceFromTAAndRxl");
				return distanceArr;
			}
			
			public double getDIstanceFromLatLon(String msloc){
				double distance=0.0;
				String[] latLon=msloc.split(",");
				double lat1=Double.parseDouble(latLon[0]);
				double lon1=Double.parseDouble(latLon[1]);
				distance=distanceFromLatLon(lat1,lon1,lat1,lon1,"K");
				JSONArray gpsArr=new Operations().getJson("select lat,lon from gpsdata order by logtime desc limit 1");
				double lat2=0.0;
				double lon2=0.0;
				try {
					JSONObject gpsObj=gpsArr.getJSONObject(0);
					lat2=gpsObj.getDouble("lat");
					lon2=gpsObj.getDouble("lon");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				distance=distanceFromLatLon(lat1,lon1,lat2,lon2,"K")*1000;
				
				return distance;
			
			}
			
			public static double distanceFromLatLon(double lat1, double lon1, double lat2, double lon2, String unit) {
				if ((lat1 == lat2) && (lon1 == lon2)) {
					return 0;
				}
				else {
					double theta = lon1 - lon2;
					double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
					dist = Math.acos(dist);
					dist = Math.toDegrees(dist);
					dist = dist * 60 * 1.1515;
					if (unit == "K") {
						dist = dist * 1.609344;
					} else if (unit == "N") {
						dist = dist * 0.8684;
					}
					return (dist);
				}
			}
			
			public double getULFrequencyFromDL(double freq){
				fileLogger.info("Inside Function : getULFrequencyFromDL");
				double frequency=-1.0;
				try {
					freq=freq*1000;
					int separator=new Operations().getJson("select distinct(dl_frq_start-ul_frq_start) as separator from arfcn_freq_band_map where dl_frq_start <="+freq+" and dl_frq_end>="+freq+" limit 1").getJSONObject(0).getInt("separator");
				    fileLogger.debug("@separator is :"+separator);
					frequency=(freq-separator)/1000;
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				fileLogger.debug("@separator frequency :"+frequency);
				fileLogger.info("Exit Function : getULFrequencyFromDL");
				return frequency;
			}
			
			public int getAtypeFromAntenna(String antenna){
				int aType=1;
				try{
					aType=Integer.parseInt(new Operations().getJson("select atype from antenna where profile_name='"+antenna+"'").getJSONObject(0).getString("atype"));
				}catch(Exception e){
					fileLogger.error("exception is getAtypeFromAntenna :"+e.getMessage());
				}
				return aType;
				}

			public int getSectorAngle(int offset, String sector){
				sector = sector.toLowerCase();
				int angle=0;
				if(sector.equalsIgnoreCase("S1")){
					angle=60;
				}else if(sector.equalsIgnoreCase("s2")){
					angle=120;
				}else if(sector.equalsIgnoreCase("s3")){
					angle=180;
				}else if(sector.equalsIgnoreCase("ov1")){
					angle=0;
				}else{
					angle=0;
				}
				angle+=offset;
				
				angle-=30;
				return angle;
			}
}