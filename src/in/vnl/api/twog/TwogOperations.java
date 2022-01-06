package in.vnl.api.twog;

import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.icmp4j.*;
import javax.servlet.http.HttpServletRequest;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.RestoreAction;
import javax.ws.rs.core.Response;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import in.vnl.msgapp.Common;
import in.vnl.msgapp.Operations;
import in.vnl.msgapp.SocketServer;
import in.vnl.sockets.UdpServerClient;
import in.vnl.EventProcess.DBDataService;
import in.vnl.api.common.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import in.vnl.api.common.livescreens.DeviceStatusServer;
import in.vnl.api.netscan.NetscanOperations;
import in.vnl.api.threeg.livescreens.*;
import in.vnl.api.threeg.ThreegOperations; 
public class TwogOperations 
{
	static Logger fileLogger = Logger.getLogger("file");
	static Logger statusLogger = Logger.getLogger("status");
	
	public String converToHtmlForNodeWiseData(String query)
	{
		fileLogger.info("Inside Function : converToHtmlForNodeWiseData");
		StringBuilder str = new StringBuilder();
		Common co = new Common();
		Statement smt = null;
		Connection con = co.getDbConnection();
		JSONArray ja = new JSONArray();
		try
		{
			smt = con.createStatement();
			fileLogger.debug(query);
			ResultSet rs = smt.executeQuery(query);
			
			int total = 0;
			int ta0 = 0;
			int ta1 = 0;
			int ta2 = 0;
			int taess5 = 0;
			int tamore5 = 0;
			int tana = 0;
			int rxl55 = 0;
			int rxl75 = 0;
			int rxlmore95 = 0;
			int rxlless95 = 0;
			int rxlna = 0;
			
			
			
			while(rs.next())
			{
				String node=rs.getString("node");
				str.append("<tr class='node_row'>");
					str.append("<td style=\"font-weight: 700;\">"+rs.getString("node")+"</td>");
					str.append("<td style=\"text-align: center;\">"+rs.getString("total")+"</td>");
					str.append("<td style=\"cursor: pointer;\" onclick=\"getNodeWiseReport('"+node+"','ta','0')\">"+rs.getString("ta0")+"</td>");
					str.append("<td style=\"cursor: pointer;\" onclick=\"getNodeWiseReport('"+node+"','ta','1')\">"+rs.getString("ta1")+"</td>");
					str.append("<td style=\"cursor: pointer;\" onclick=\"getNodeWiseReport('"+node+"','ta','2')\">"+rs.getString("ta2")+"</td>");
					str.append("<td style=\"cursor: pointer;\" onclick=\"getNodeWiseReport('"+node+"','ta','lessthan5')\">"+rs.getString("taless5")+"</td>");
					str.append("<td style=\"cursor: pointer;\" onclick=\"getNodeWiseReport('"+node+"','ta','morethan5')\">"+rs.getString("tamore5")+"</td>");
					str.append("<td style=\"cursor: pointer;\" onclick=\"getNodeWiseReport('"+node+"','ta','NA')\">"+rs.getString("tana")+"</td>");
					str.append("<td style=\"cursor: pointer;\" onclick=\"getNodeWiseReport('"+node+"','rxl','morethan-55')\">"+rs.getString("rxl55")+"</td>");
					str.append("<td style=\"cursor: pointer;\" onclick=\"getNodeWiseReport('"+node+"','rxl','morethan-75')\">"+rs.getString("rxl75")+"</td>");
					str.append("<td style=\"cursor: pointer;\" onclick=\"getNodeWiseReport('"+node+"','rxl','morethan-95')\">"+rs.getString("rxl95")+"</td>");
					str.append("<td style=\"cursor: pointer;\" onclick=\"getNodeWiseReport('"+node+"','rxl','lessthan-95')\">"+rs.getString("rxlless95")+"</td>");
					str.append("<td style=\"cursor: pointer;\" onclick=\"getNodeWiseReport('"+node+"','rxl','NA')\">"+rs.getString("rxlna")+"</td>");
				str.append("</tr>");
				
				total = total + rs.getInt("total");
				ta0 = ta0 + rs.getInt("ta0");
				ta1 = ta1 + rs.getInt("ta1");
				ta2 = ta2 + rs.getInt("ta2");
				taess5 = taess5 + rs.getInt("taless5");
				tamore5 = tamore5 + rs.getInt("tamore5");
				tana = tana + rs.getInt("tana");
				rxl55 = rxl55 + rs.getInt("rxl55");
				rxl75 = rxl75 + rs.getInt("rxl75");
				rxlmore95 = rxlmore95 + rs.getInt("rxl95");
				rxlless95 = rxlless95 + rs.getInt("rxlless95");
				rxlna = rxlna + rs.getInt("rxlna");				
			}
			
			str.append("<tr class='node_row'>");
				str.append("<td style=\"font-weight: 700;\">Total</td>");
				str.append("<td style=\"cursor: pointer;text-align: center;\" onclick=\"getNodeWiseReport('total')\">"+total+"</td>");
				str.append("<td style=\"cursor: pointer;\" onclick=\"getNodeWiseReport('subTotal','ta','0')\">"+ta0+"</td>");
				str.append("<td style=\"cursor: pointer;\" onclick=\"getNodeWiseReport('subTotal','ta','1')\">"+ta1+"</td>");
				str.append("<td style=\"cursor: pointer;\" onclick=\"getNodeWiseReport('subTotal','ta','2')\">"+ta2+"</td>");
				str.append("<td style=\"cursor: pointer;\" onclick=\"getNodeWiseReport('subTotal','ta','lessthan5')\">"+taess5+"</td>");
				str.append("<td style=\"cursor: pointer;\" onclick=\"getNodeWiseReport('subTotal','ta','morethan5')\">"+tamore5+"</td>");
				str.append("<td style=\"cursor: pointer;\" onclick=\"getNodeWiseReport('subTotal','ta','NA')\">"+tana+"</td>");
				str.append("<td style=\"cursor: pointer;\" onclick=\"getNodeWiseReport('subTotal','rxl','morethan-55')\">"+rxl55+"</td>");
				str.append("<td style=\"cursor: pointer;\" onclick=\"getNodeWiseReport('subTotal','rxl','morethan-75')\">"+rxl75+"</td>");
				str.append("<td style=\"cursor: pointer;\" onclick=\"getNodeWiseReport('subTotal','rxl','morethan-95')\">"+rxlmore95+"</td>");
				str.append("<td style=\"cursor: pointer;\" onclick=\"getNodeWiseReport('subTotal','rxl','lessthan-95')\">"+rxlless95+"</td>");
				str.append("<td style=\"cursor: pointer;\" onclick=\"getNodeWiseReport('subTotal','rxl','NA')\">"+rxlna+"</td>");
			str.append("</tr>");
		}
		catch(Exception E)
		{
			fileLogger.error("Exeption while authenticating the user "+E.getMessage());
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
		fileLogger.info("Exit Function : converToHtmlForNodeWiseData");
		return str.toString();
	}
	
	
	public String converToHtmlForCountryWiseData(String query)
	{
		fileLogger.info("Inside Function : converToHtmlForCountryWiseData");
		StringBuilder str = null;
		StringBuilder table = new StringBuilder();
		Common co = new Common();
		Statement smt = null;
		Connection con = co.getDbConnection();
		
		try
		{
			smt = con.createStatement();
			fileLogger.debug(query);
			ResultSet rs = smt.executeQuery(query);
			
			String currentCountry = null;
			String previousCountry = null;
			int colspan = 0;
			
			int count1=0;
			int count2=0;
			int count3=0;
			int count4=0;
			int count5=0;
			int count6=0;
			int count7=0;
			int count8=0;
			int count9=0;
			int count10=0;
			int count11=0;
			int count12=0;
			while(rs.next())
			{
				currentCountry = rs.getString("country");
				//str.append("<tr class='count_row'>");
				if(previousCountry != null && previousCountry.equalsIgnoreCase(currentCountry))
				{
					//fileLogger.debug("1"+colspan);
					colspan++;
					//fileLogger.debug("2"+colspan);
				
					
				}
				else
				{
					if(previousCountry != null)
					{
						table.append("<tr class='count_row'><td rowspan = ");
						table.append(colspan);table.append(" style=\"font-weight: 700;\">");
						table.append(previousCountry);
						table.append("</td>");
						table.append(str.toString());
						//table.append("</tr>");
						str = null;
					}
					str = new StringBuilder();
					colspan = 1;
				}
				
				
					String oprName=rs.getString("oprname");
					str.append("<td style=\"font-weight: 700;\">"+oprName+"</td>");
					str.append("<td style=\"text-align: center;\">"+rs.getString("total")+"</td>");
					str.append("<td style=\"cursor: pointer;\" onclick=\"getCountryWiseReport('"+currentCountry+"','"+oprName+"','ta','0')\">"+rs.getString("ta0")+"</td>");
					str.append("<td style=\"cursor: pointer;\" onclick=\"getCountryWiseReport('"+currentCountry+"','"+oprName+"','ta','1')\">"+rs.getString("ta1")+"</td>");
					str.append("<td style=\"cursor: pointer;\" onclick=\"getCountryWiseReport('"+currentCountry+"','"+oprName+"','ta','2')\">"+rs.getString("ta2")+"</td>");
					str.append("<td style=\"cursor: pointer;\" onclick=\"getCountryWiseReport('"+currentCountry+"','"+oprName+"','ta','lessthan5')\">"+rs.getString("taless5")+"</td>");
					str.append("<td style=\"cursor: pointer;\" onclick=\"getCountryWiseReport('"+currentCountry+"','"+oprName+"','ta','morethan5')\">"+rs.getString("tamore5")+"</td>");
					str.append("<td style=\"cursor: pointer;\" onclick=\"getCountryWiseReport('"+currentCountry+"','"+oprName+"','ta','NA')\">"+rs.getString("tana")+"</td>");
					str.append("<td style=\"cursor: pointer;\" onclick=\"getCountryWiseReport('"+currentCountry+"','"+oprName+"','rxl','morethan-55')\">"+rs.getString("rxl55")+"</td>");
					str.append("<td style=\"cursor: pointer;\" onclick=\"getCountryWiseReport('"+currentCountry+"','"+oprName+"','rxl','morethan-75')\">"+rs.getString("rxl75")+"</td>");
					str.append("<td style=\"cursor: pointer;\" onclick=\"getCountryWiseReport('"+currentCountry+"','"+oprName+"','rxl','morethan-95')\">"+rs.getString("rxl95")+"</td>");
					str.append("<td style=\"cursor: pointer;\" onclick=\"getCountryWiseReport('"+currentCountry+"','"+oprName+"','rxl','lessthan-95')\">"+rs.getString("rxlless95")+"</td>");
					str.append("<td style=\"cursor: pointer;\" onclick=\"getCountryWiseReport('"+currentCountry+"','"+oprName+"','rxl','NA')\">"+rs.getString("rxlna")+"</td>");
					str.append("</tr>");
					str.append("<tr class='count_row'>");
					count1+=rs.getInt("total");
					count2+=rs.getInt("ta0");
					count3+=rs.getInt("ta1");
					count4+=rs.getInt("ta2");
					count5+=rs.getInt("taless5");
					count6+=rs.getInt("tamore5");
					count7+=rs.getInt("tana");
					count8+=rs.getInt("rxl55");
					count9+=rs.getInt("rxl75");
					count10+=rs.getInt("rxl95");
					count11+=rs.getInt("rxlless95");
					count12+=rs.getInt("rxlna");
				
				
				previousCountry = currentCountry;
				if(rs.isLast())
			    {
			     table.append("<tr class='count_row'><td rowspan = ");
			     table.append(colspan);table.append(" style=\"font-weight: 700;\">");
			     table.append(previousCountry);
			     table.append("</td>");
			     table.append(str.toString());
			    }
			}
			str=null;
			str = new StringBuilder(); 
			str.append("<tr class='count_row'>");
			str.append("<td colspan=\"2\" style=\"font-weight: 700;\">Total</td>");
			str.append("<td style=\"text-align: center;\">"+count1+"</td>");
			str.append("<td>"+count2+"</td>");
			str.append("<td>"+count3+"</td>");
			str.append("<td>"+count4+"</td>");
			str.append("<td>"+count5+"</td>");
			str.append("<td>"+count6+"</td>");
			str.append("<td>"+count7+"</td>");
			str.append("<td>"+count8+"</td>");
			str.append("<td>"+count9+"</td>");
			str.append("<td>"+count10+"</td>");
			str.append("<td>"+count11+"</td>");
			str.append("<td>"+count12+"</td>");
			str.append("</tr>");
			table.append(str.toString());
		}
		catch(Exception E)
		{
			fileLogger.error("Exeption while authenticating the user "+E.getMessage());
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
		fileLogger.info("Exit Function : converToHtmlForCountryWiseData");
		return table.toString();
	}	
	

	public String converToHtmlForNetworkInfoNodeWiseData(String query)
	{
		fileLogger.info("Inside Function : converToHtmlForNetworkInfoNodeWiseData");
		StringBuilder str = null;
		StringBuilder table = new StringBuilder();
		Common co = new Common();
		Statement smt = null;
		Connection con = co.getDbConnection();
	
		int cellTotal = 0;
		
		try
		{
			smt = con.createStatement();
			fileLogger.debug(query);
			ResultSet rs = smt.executeQuery(query);
			
			String currentCountry = null;
			String previousCountry = null;
			int colspan = 0;
			
			
			
			while(rs.next())
			{
				currentCountry = rs.getString("oprname");
				//str.append("<tr class='count_row'>");
				if(previousCountry != null && previousCountry.equalsIgnoreCase(currentCountry))
				{
					//fileLogger.debug("1"+colspan);
					colspan++;
					//fileLogger.debug("2"+colspan);
				
					
				}
				else
				{
					if(previousCountry != null)
					{
						table.append("<tr class='count_row_net'><td rowspan = ");table.append(colspan);table.append(" style=\"font-weight: 700;\">");
							table.append(previousCountry);
						table.append("</td>");
						table.append(str.toString());
						str = null;
					}
					str = new StringBuilder();
					colspan = 1;
				}
				
				str.append("<td style=\"font-weight: 700;\">"+rs.getString("bts")+"</td>");
				str.append("<td style=\"cursor: pointer;\" onclick=\"getNetworkScanReport('"+currentCountry+"','"+rs.getString("bts")+"')\">"+rs.getString("count")+"</td>");
				str.append("</tr>");
				str.append("<tr class='count_row_net'>");
				if(currentCountry !=null)
				cellTotal = cellTotal +rs.getInt("count");
				previousCountry = currentCountry;
				if(rs.isLast())
			    {
			     table.append("<tr class='count_row_net'><td rowspan = ");table.append(colspan);table.append(" style=\"font-weight: 700;\">");
			      table.append(previousCountry);
			     table.append("</td>");
			     table.append(str.toString());
			    }
			}
			
			
			
		}
		catch(Exception E)
		{
			fileLogger.error("Exeption while authenticating the user "+E.getMessage());
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
		
		table.append("<tr class='count_row_net'>");
			table.append("<td></td>");
			table.append("<td style=\"font-weight: 700;\">Total</td>");
			table.append("<td style=\"cursor: pointer;\" onclick=\"getNetworkScanReport('total')\">"+cellTotal+"</td>");
		table.append("</tr>");
		
		fileLogger.info("Exit Function : converToHtmlForNetworkInfoNodeWiseData");
		return table.toString();
	}
	
	
	
	public String converToHtmlForNodesInfo(String query)
	{
		fileLogger.info("Inside Function : converToHtmlForNodesInfo");
		StringBuilder str = new StringBuilder();
		Common co = new Common();
		Statement smt = null;
		Connection con = co.getDbConnection();	
		try
		{
			smt = con.createStatement();
			fileLogger.debug(query);
			ResultSet rs = smt.executeQuery(query);
			while(rs.next())
			{
				
				String adminState = "NA";
				int admin = rs.getInt("adminstate");
				switch(admin)
				{
					case 0:
						adminState = "NA";
						break;
					case 1:
						adminState = "LOCK";
						break;
					case 2:
						adminState = "UNLOCK";
						break;
				}
				if(!rs.getString("ip").equalsIgnoreCase("1.1.1.1") && !rs.getString("ip").equalsIgnoreCase("0.0.0.0")) 
				{
					str.append("<tr>");
						str.append("<td>"+rs.getString("ip")+"</td>");
						str.append("<td>"+rs.getString("cellstatus")+"</td>");
						str.append("<td>"+adminState+"</td>");
					str.append("</tr>");
				}
				
			}
			
		}
		catch(Exception E)
		{
			fileLogger.error("Error while getting 2g device status"+E.getMessage());
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
		fileLogger.info("Exit Function : converToHtmlForNodesInfo");
		return str.toString();
	}
	
	public String setLockUnlock(String ip,String flag)
	{	
		
		
		Common co = new Common();
		HashMap<String,String> ll = co.getDbCredential();
		
		String myURL = "http://"+ip+"/cgi-bin/processData_CLI.sh";
		String mdr = "";
		String CMD_TYPE="SET_SET_CELL_LOCK";
		String CMD_CODE="SET_CELL_LOCK";
		
		if(flag.equalsIgnoreCase("2"))
		{
			CMD_TYPE="SET_SET_CELL_UNLOCK";
			CMD_CODE="SET_CELL_UNLOCK";
		}
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		
		params.add(new BasicNameValuePair("CMD_TYPE",CMD_TYPE ));
		params.add(new BasicNameValuePair("CMD_CODE", CMD_CODE));
		params.add(new BasicNameValuePair("CELL_ID", "0"));
		params.add(new BasicNameValuePair("LAC", "0"));
		
		try
		{
			mdr = co.callPostDataUrl(myURL,params);
		}
		catch(Exception E)
		{
			
		}
		return mdr;
	}
	
	
	
	
	public String updateStatusOfBts(String ipListString)
	{	
		fileLogger.debug("@updateStatusOfBts Inside Function ");
		Common co = new Common();
		Statement smt = null;
		Connection con = co.getDbConnection();
		
		try
		{
			smt = con.createStatement();
			String query=null;
			if(ipListString.equalsIgnoreCase("all")){
				query = "select * from view_btsinfo where ip not in('0.0.0.0','1.1.1.1')";
			}else{
				query = "select * from view_btsinfo where ip in('"+ipListString+"')";
				fileLogger.debug("@updateStatusOfBts query = "+query);
			}
			statusLogger.debug(query);
			ResultSet rs = smt.executeQuery(query);
			ArrayList<Thread> threadList = new ArrayList<Thread>();
			
			while(rs.next())
			{
				final LinkedHashMap<String,String> param = new  LinkedHashMap<String,String>();
				param.put("cmdType", "GET_GET_CURR_STATUS");
			    param.put("CMD_CODE", "GET_CURR_STATUS");
			    param.put("systemIP", rs.getString("ip"));
			    param.put("systemId", rs.getString("sytemid"));
			    param.put("systemCode", rs.getString("code"));
			    param.put("code",rs.getString("code"));
			    param.put("status", rs.getString("statuscode"));
			    param.put("adminState", rs.getString("adminstate"));
			    param.put("id",rs.getString("b_id"));
			    param.put("config",rs.getString("config"));
			    param.put("devicetypeid",rs.getString("dcode"));
			    param.put("name",rs.getString("dname"));
			    param.put("status_name",rs.getString("status"));
			    fileLogger.debug("@updateStatusOfBts Param =   "+param);
			    Thread t = new Thread()
			     {
			          public void run()
			          {
			        	  
			        	  statusLogger.debug("starting Thread to get status for  "+param.get("systemIP"));
					     getCurrentStatusOfBts(param);
					     new DeviceStatusServer().sendText("ok");
					     statusLogger.debug("Ending Thread to get status for  "+param.get("systemIP"));
			          }
			     };
			     t.start();	    
			     threadList.add(t);
			   //getCurrentStatusOfBts(param);
			}
			
			for (Thread thread : threadList) {
		        thread.join();
		    }
			
		
		}
		catch(Exception E)
		{
		
			
			//statusLogger.debug("*****************************************");
			//statusLogger.debug("Class = TwogOperations , Method : updateStatusOfAllBts");
			statusLogger.error("Erorr During updating the status"+ E.getMessage());
			//statusLogger.debug(E.getMessage());
			E.printStackTrace();
		//	statusLogger.debug("*****************************************");
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
		
		fileLogger.debug("@updateStatusOfBts Exit Function ");
		return "";
	}
	
	
	public String updateSoftVerOfDevices(String ipListString)
	{	
		Common co = new Common();
		Statement smt = null;
		Connection con = co.getDbConnection();
		
		try
		{
			smt = con.createStatement();
			String query=null;
			if(ipListString.equalsIgnoreCase("all")){
				query = "select * from view_btsinfo where ip not in('0.0.0.0','1.1.1.1')";
			}else{
				query = "select * from view_btsinfo where ip in("+ipListString+")";
			}
			statusLogger.debug(query);
			ResultSet rs = smt.executeQuery(query);
			ArrayList<Thread> threadList = new ArrayList<Thread>();
			
			while(rs.next())
			{
				final LinkedHashMap<String,String> param = new  LinkedHashMap<String,String>();
				param.put("cmdType", "GETALL_ACTIVATE_SW");
			    param.put("CMD_CODE", "GET_SW_VERSION");
			    param.put("systemIP", rs.getString("ip"));
			    param.put("systemId", rs.getString("sytemid"));
			    param.put("systemCode", rs.getString("code"));
			    param.put("code",rs.getString("code"));
			    param.put("status", rs.getString("statuscode"));
			    param.put("adminState", rs.getString("adminstate"));
			    param.put("id",rs.getString("b_id"));
			    param.put("config",rs.getString("config"));
			    param.put("devicetypeid",rs.getString("dcode"));
			    param.put("name",rs.getString("dname"));
			    param.put("status_name",rs.getString("status"));
			    
			    Thread t = new Thread()
			     {
			          public void run()
			          {
			        	  statusLogger.debug("starting Thread to get status for  "+param.get("systemIP"));
			        	  getCurrentSoftVerOfDevices(param);
					     //new DeviceStatusServer().sendText("ok");
					     statusLogger.debug("Ending Thread to get status for  "+param.get("systemIP"));
			          }
			     };
			     t.start();	    
			     threadList.add(t);
			   //getCurrentStatusOfBts(param);
			}
			
			for (Thread thread : threadList) {
		        thread.join();
		    }
			
		
		}
		catch(Exception E)
		{
		
			
			//statusLogger.debug("*****************************************");
			//statusLogger.debug("Class = TwogOperations , Method : updateStatusOfAllBts");
			statusLogger.error("Erorr During updating the status");
			//statusLogger.debug(E.getMessage());
			E.printStackTrace();
			//statusLogger.debug("*****************************************");
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
		
		
		return "";
	}
	
	//
	public String getStatusFor2gDevice(LinkedHashMap<String,String> data) 
	{
		
		String myURL = "http://"+data.get("systemIP")+"/cgi-bin/processData_CLI.sh";
		String mdr = "";
		Common co = new Common();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("CMD_TYPE",data.get("cmdType") ));
		params.add(new BasicNameValuePair("CMD_CODE", data.get("CMD_CODE")));
		
		try
		{
			mdr = co.callPostDataUrlForStatus(myURL,params);
			
			if(mdr != null && !mdr.contains("ERR"))
			{
				
				int statusCode = data.get("status")!=null?Integer.parseInt(data.get("status")):-1;
				
				JSONObject jo = new JSONObject(mdr);
				
				int newStatusCode = jo.getInt("SYSTEM_STATUS");
				
				new ApiCommon().insertNodStatusChange(statusCode,newStatusCode,data.get("systemIP"));
				
				this.updateBtsStatus(mdr,data.get("systemIP"));
				
				new AuditHandler().auditStatus(data, newStatusCode, statusCode);
				
			}
			else if(mdr == null)
			{				
			}
		}
		catch(Exception e)
		{
			try{
				int newStatus = 2;
				JSONObject jb = new JSONObject();
				jb.put("CMD_CODE", "SET_CURR_STATUS");
				jb.put("CELL_OP_STATE", 0);
				jb.put("CELL_ADM_STATE", 0);
				String message = e.getMessage().toLowerCase();
				if(message.indexOf("connection refused")!=-1){
					jb.put("SYSTEM_STATUS", 3);
					newStatus = 3;
				}else{
					jb.put("SYSTEM_STATUS", 2);
				}
				this.updateBtsStatus(jb.toString(),data.get("systemIP"));
				new AuditHandler().auditStatus(data,newStatus,Integer.parseInt(data.get("status")));
			}catch(Exception exception){
				statusLogger.debug("Exception in getStatusFor2gDevice :"+exception.getMessage());
			}
			
		}
		return mdr;
	}
	
	public String getSoftVerOf2gDevice(LinkedHashMap<String,String> data) 
	{
		
		String myURL = "http://"+data.get("systemIP")+"/cgi-bin/processData_CLI.sh";
		String mdr = "";
		Common co = new Common();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("CMD_TYPE","GETALL_ACTIVATE_SW" ));
		params.add(new BasicNameValuePair("TAGS00", "Undefined"));
		params.add(new BasicNameValuePair("TAGS01", "Undefined"));
		params.add(new BasicNameValuePair("TAGS02", "Undefined"));
		params.add(new BasicNameValuePair("TAGS03", "Undefined"));
		params.add(new BasicNameValuePair("TAGS04", "Undefined"));
		
		try
		{
			mdr = co.callPostDataUrlForStatus(myURL,params);
			
			if(mdr != null && !mdr.contains("ERR") && mdr.length()<10){
				try {
					Thread.sleep(500);
					mdr = co.callPostDataUrlForStatus(myURL,params);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(mdr != null && !mdr.contains("ERR"))
			{	
				
				fileLogger.debug("@sw_version getSoftVerOf2gDevice mdr is :"+mdr);
				this.updateDeviceSoftVer(mdr,data.get("systemIP"));
				
			}
			else if(mdr == null)
			{				
			}
		}
		catch(Exception e)
		{
			try{
			}catch(Exception exception){
				fileLogger.debug("Exception in getSoftVerOf2gDevice :"+exception.getMessage());
			}
			
		}
		return mdr;
	}	
	
	public String getSoftVerOfSpoilerDevice(LinkedHashMap<String,String> data) 
	{
		
		String url="/systemInfo.json";
		
		String  rs="";
		String swRevNumber="";
		try
		{
			
			
			 rs =  new ApiCommon().HTTP_Request(data.get("systemIP"), 80,url,"GET" ,"");
		    //Response rs =  new ApiCommon().sendRequestToUrl(url,null,ja_dataToSendToJammer.toString());
			//if rs.equalsIgnoreCase(anotherString)!=-100)
			if (rs.equalsIgnoreCase("-100")==true)
			{
				return "Failure at socket Level "; //fail
			}
			else if(!rs.contains("\"status\":\"200\""))
		    {
		    	return "Failure "+rs;
		    }
			JSONObject objToSendToJammer = new JSONObject(rs);
			swRevNumber=(objToSendToJammer.getJSONObject("data").getString("swRevNumber"));
			String query="update btsmaster set sw_version = '"+swRevNumber + "'update btsmaster set sw_version =A2.1.1.5 where devicetypeid=21; where devicetypeid=21;";
			new Common().executeDLOperation(query);
			
		}
		catch(Exception e)
		{
			try{
			}catch(Exception exception){
				fileLogger.debug("Exception in getSoftVerOf2gDevice :"+exception.getMessage());
			}
			
		}
		return swRevNumber;
	}	
		
	public String getStatusFor3gDevice(LinkedHashMap<String,String> data) throws Exception 
	{
				
		LinkedHashMap<String,String> queryParam = new LinkedHashMap<String,String>();
		
		queryParam.put("cmdType",data.get("CMD_CODE"));
		queryParam.put("CMD_TYPE",data.get("CMD_CODE"));
		queryParam.put("SYSTEM_CODE",data.get("systemCode"));
		queryParam.put("SYSTEM_ID",data.get("systemId"));
		
		data.put("data", "{\"CMD_CODE\":\"GET_CURR_STATUS\"}");
		statusLogger.debug(data.get("data"));
		//Common.log(data.get("data"));
		String url = new Common().getDbCredential().get("3gserviceurl");
		Response rs =  new ApiCommon().sendRequestToUrl("http://"+data.get("systemIP")+url,queryParam,data.get("data"));
		String resultData = "";
		if(rs != null)
		{
			
			resultData = rs.readEntity(String.class);
			new ThreegOperations().updateBtsStatus(resultData,data.get("systemIP"),data.get("id"));
			
			int statusCode = data.get("status")!=null?Integer.parseInt(data.get("status")):-1;
			
			JSONObject jo = new JSONObject(resultData);
			
			int adminState = jo.getInt("CELL_ADM_STATE");
			
			int newStatusCode = jo.getInt("SYSTEM_STATUS");
			
			new ApiCommon().insertNodStatusChange(statusCode,newStatusCode,data.get("systemIP"));
			
			
			statusLogger.debug("status update Done");
			statusLogger.debug("status update Done "+statusCode +"new "+ newStatusCode);
			if((statusCode == 2 || statusCode == 3) && (newStatusCode ==0 ||  newStatusCode == 1)) 
			{	
				statusLogger.debug("sending configuration ip="+data.get("systemIP"));
				//new Common().sendConfigurationToNode(adminState,data);
		    	  new  Common().ConfigurationOnStatusChangeUMTS(adminState,data);
				statusLogger.debug("done sending confifuration ip="+data.get("systemIP"));
			}
			
			
			
			statusLogger.debug("status update Done 2");
		}
		else 
		{
			statusLogger.debug("throwing exception as rs = "+rs);
			throw new Exception("error while connecting");
		}
		Common.log(resultData);
		return "ok";
	}
	
	public String getSoftVerOf3gDevice(LinkedHashMap<String,String> data) throws Exception 
	{
		fileLogger.info("Inside Function : getSoftVerOf3gDevice");		
		LinkedHashMap<String,String> queryParam = new LinkedHashMap<String,String>();
		
		queryParam.put("cmdType",data.get("CMD_CODE"));
		queryParam.put("CMD_TYPE",data.get("CMD_CODE"));
		queryParam.put("SYSTEM_CODE",data.get("systemCode"));
		queryParam.put("SYSTEM_ID",data.get("systemId"));
		
		data.put("data", "{\"CMD_CODE\":\"GET_SW_VERSION\"}");
		fileLogger.debug(data.get("data"));
		//Common.log(data.get("data"));
		String url = new Common().getDbCredential().get("3gserviceurl");
		Response rs =  new ApiCommon().sendRequestToUrl("http://"+data.get("systemIP")+url,queryParam,data.get("data"));
		String resultData = "";
		if(rs != null)
		{
			
			resultData = rs.readEntity(String.class);
			fileLogger.debug("@sw_version getSoftVerOf3gDevice resultData is :"+resultData);
			new ThreegOperations().updateDeviceSoftVer(resultData,data.get("systemIP"),data.get("id"));
			fileLogger.debug("status update Done 2");
		}
		else 
		{
			fileLogger.error("throwing exception as rs = "+rs);
			throw new Exception("error while connecting");
		}
		Common.log(resultData);
		fileLogger.info("Exit Function : getSoftVerOf3gDevice");		
		return "ok";
	}
	
	
	
	public String getStatusForScanerDevice(LinkedHashMap<String,String> data) throws Exception 
	{
		fileLogger.info("Inside Function : getStatusForScanerDevice");		
		LinkedHashMap<String,String> queryParam = new LinkedHashMap<String,String>();
		queryParam.put("CMD_TYPE",data.get("CMD_CODE"));
		queryParam.put("SYSTEM_CODE",data.get("systemCode"));
		queryParam.put("SYSTEM_ID",data.get("systemId"));
		
		data.put("data", "{\"CMD_CODE\":\"GET_CURR_STATUS\"}");
		//Common.log(data.get("data"));
		String url = new Common().getDbCredential().get("netscanserviceurl");
		Response rs =  new ApiCommon().sendRequestToUrl("http://"+data.get("systemIP")+url,queryParam,data.get("data"));
		String resultData = "";
		if(rs != null)
		{
			NetscanOperations netscanOperations = new NetscanOperations();
			resultData = rs.readEntity(String.class);
			int statusCode = data.get("status")!=null?Integer.parseInt(data.get("status")):-1;
			JSONObject jo = new JSONObject(resultData);
			int newStatusCode = jo.getInt("STATUS_CODE");
			fileLogger.debug("@scanstatus old statuscode is :"+statusCode);
			fileLogger.debug("@scanstatus new statuscode is :"+newStatusCode);
			if((statusCode == 2 || statusCode == 3) && newStatusCode ==1) 
			{	
				new ApiCommon().setGpsDataRequest(data.get("systemCode"), data.get("systemId"), data.get("systemIP"));
				fileLogger.debug("@Since scanstatus is changed restarting scanner ");
				if(!DBDataService.scannerManuallyStopped) {
					fileLogger.debug("@getStatusForScanerDevice HUA RESTART Abhi 2");
					DBDataService.statusScannerRestart=true;
					new CommonService().restartScanner();
					
				}
			}
			new ApiCommon().insertNodStatusChange(statusCode,newStatusCode,data.get("systemIP"));
			netscanOperations.updateBtsStatus(resultData,data.get("systemIP"),data.get("id"));		
			new AuditHandler().auditStatus(data, newStatusCode, statusCode);
		}
		fileLogger.info("Exit Function : getStatusForScanerDevice");		
		return "ok";
	}
	
	public String getSoftVerOfScanerDevice(LinkedHashMap<String,String> data) throws Exception 
	{
		fileLogger.info("Inside Function : getSoftVerOfScanerDevice");		
		LinkedHashMap<String,String> queryParam = new LinkedHashMap<String,String>();
		queryParam.put("CMD_TYPE",data.get("CMD_CODE"));
		queryParam.put("SYSTEM_CODE",data.get("systemCode"));
		queryParam.put("SYSTEM_ID",data.get("systemId"));
		
		data.put("data", "{\"CMD_CODE\":\"GET_SW_VERSION\"}");
		Common.log(data.get("data"));
		String url = new Common().getDbCredential().get("netscanserviceurl");
		Response rs =  new ApiCommon().sendRequestToUrl("http://"+data.get("systemIP")+url,queryParam,data.get("data"));
		String resultData = "";
		if(rs != null)
		{
			resultData = rs.readEntity(String.class);
			fileLogger.debug("@sw_version getSoftVerOfScanerDevice resultData is :"+resultData);
			new NetscanOperations().updateDeviceSoftVer(resultData,data.get("systemIP"),data.get("id"));		
		}
		fileLogger.info("Exit Function : getSoftVerOfScanerDevice");
		return "ok";
	}
	
	
	public String getStatusForSystemManagerDevice(LinkedHashMap<String,String> data) throws Exception 
	{	
		LinkedHashMap<String,String> queryParam = new LinkedHashMap<String,String>();
		queryParam.put("CMD_TYPE","GET_SYSTEM_STATUS");
		queryParam.put("SYSTEM_CODE",data.get("systemCode"));
		queryParam.put("SYSTEM_ID",data.get("systemId"));
		data.put("data", "{\"CMD_CODE\":\"GET_SYSTEM_STATUS\"}");
		Common.log(data.get("data"));
		String url = new Common().getDbCredential().get("netscanserviceurl");
		Response rs =  new ApiCommon().sendRequestToUrl("http://"+data.get("systemIP")+url,queryParam,data.get("data"));
		String resultData = "";
		if(rs != null)
		{
			resultData = rs.readEntity(String.class);
			int statusCode = data.get("status")!=null?Integer.parseInt(data.get("status")):-1;
			JSONObject jo = new JSONObject(resultData);
			fileLogger.debug("@result data= "+resultData);
			int newStatusCode = jo.getInt("STATUS_CODE");
			statusLogger.debug("status update Done");
			statusLogger.debug("status update Done "+statusCode +"new "+ newStatusCode);
			new ApiCommon().insertNodStatusChange(statusCode,newStatusCode,data.get("systemIP"));
			new NetscanOperations().updateSystemManagerStatus(resultData,data.get("systemIP"),data.get("id"));
			data.put("TEMP", jo.getString("TEMP"));
			data.put("CPU", jo.getString("CPU"));
			data.put("DISK",jo.getString("DISK"));
			data.put("MEM",jo.getString("MEM"));
			
			new AuditHandler().auditStatus(data, newStatusCode, statusCode);
			statusLogger.debug("status update Done 2");
			  
		    //int checkIfSystemManager=Integer.parseInt(data.get("devicetypeid"));
			//if(checkIfSystemManager==14)
		//	{
				//int statusSysManager=Integer.parseInt(data.get("status"));
				if(newStatusCode==3||newStatusCode==0)
				{
					
					if(DBDataService.systemManagerUpdatedStatus) {
							String respData=new ApiCommon().switchDsp("start_scanner",-1,-1,999,"");
							fileLogger.debug("@Since scanstatus is changed  getStatusForSystemManagerDevice restarting scanner ");
							//new CommonService().restartScanner();
							DBDataService.systemManagerUpdatedStatus=false;
					}
				}
				else {
						DBDataService.systemManagerUpdatedStatus=true;
					}
					
				
				//old code==0 //setting up restart scanner 
		//	}
		}
		return "ok";
	}
	
	public String getStatusForFinleyDevice(LinkedHashMap<String,String> data) throws Exception 
	{	
		Response rs =  new ApiCommon().sendRequestToUrl("http://"+data.get("systemIP")+":9000/finley/api/GET_STATUS",null);
		//String resultData = "";
		if(rs != null)
		{
			//resultData = rs.readEntity(String.class);
			int statusCode = data.get("status")!=null?Integer.parseInt(data.get("status")):-1;
			//JSONObject jo = new JSONObject(resultData);
			//int newStatusCode = jo.getInt("STATUS_CODE");
			int newStatusCode = 1;
			statusLogger.debug("status update Done");
			statusLogger.debug("status update Done "+statusCode +"new "+ newStatusCode);
			new ApiCommon().insertNodStatusChange(statusCode,newStatusCode,data.get("systemIP"));
			//new NetscanOperations().updateSystemManagerStatus(resultData,data.get("systemIP"),data.get("id"));
			new NetscanOperations().updateIDSStatus(newStatusCode,data.get("systemIP"),data.get("id"));
			//new AuditHandler().auditStatus(data, newStatusCode, statusCode);
			new AuditHandler().auditIDSStatus(data, newStatusCode, statusCode);
			statusLogger.debug("status update Done 2");
		}
		return "ok";
	}
	
	public String getStatusForHummerDevice(LinkedHashMap<String,String> data) throws Exception 
	{	
		Response rs =  new ApiCommon().sendRequestToUrl("http://"+data.get("systemIP")+":9000/api/GET_STATUS",null);
		//String resultData = "";
		if(rs != null)
		{
			//resultData = rs.readEntity(String.class);
			int statusCode = data.get("status")!=null?Integer.parseInt(data.get("status")):-1;
			//JSONObject jo = new JSONObject(resultData);
			//int newStatusCode = jo.getInt("STATUS_CODE");
			int newStatusCode = 1;
			statusLogger.debug("status update Done");
			statusLogger.debug("status update Done "+statusCode +"new "+ newStatusCode);
			new ApiCommon().insertNodStatusChange(statusCode,newStatusCode,data.get("systemIP"));
			//new NetscanOperations().updateSystemManagerStatus(resultData,data.get("systemIP"),data.get("id"));
			new NetscanOperations().updateIDSStatus(newStatusCode,data.get("systemIP"),data.get("id"));
			//new AuditHandler().auditStatus(data, newStatusCode, statusCode);
			new AuditHandler().auditIDSStatus(data, newStatusCode, statusCode);
			statusLogger.debug("status update Done 2");
		}
		return "ok";
	}
	
	public String getSoftVerOfSystemManagerDevice(LinkedHashMap<String,String> data) throws Exception 
	{	
		LinkedHashMap<String,String> queryParam = new LinkedHashMap<String,String>();
		queryParam.put("CMD_TYPE","GET_SW_VERSION");
		queryParam.put("SYSTEM_CODE",data.get("systemCode"));
		queryParam.put("SYSTEM_ID",data.get("systemId"));
		data.put("data", "{\"CMD_CODE\":\"GET_SW_VERSION\"}");
		Common.log(data.get("data"));
		String url = new Common().getDbCredential().get("netscanserviceurl");
		Response rs =  new ApiCommon().sendRequestToUrl("http://"+data.get("systemIP")+url,queryParam,data.get("data"));
		String resultData = "";
		if(rs != null)
		{
			resultData = rs.readEntity(String.class);
			fileLogger.debug("@sw_version getSoftVerOfSystemManagerDevice resultData is :"+resultData);
			new NetscanOperations().updateDeviceSoftVer(resultData,data.get("systemIP"),data.get("id"));		
		}
		return "ok";
	}
	
	  public String getStatusForPingTypeDevices(LinkedHashMap<String,String> data) throws Exception
	  {
		int oldStatus = Integer.parseInt(data.get("status")); 
		String statusCode="2";
	    //InetAddress was used earlier was throwing error 
		//InetAddress inetAddr = InetAddress.getByName(data.get("systemIP"));
	    statusLogger.debug("Sending Ping Request to " + data.get("systemIP"));
//	    if (inetAddr.isReachable(5000)){
	    if(checkPing(data.get("systemIP"))){
	    	statusLogger.debug("Host is reachable");
	    	statusCode="1";
	    }else{
	    	statusLogger.debug("Host is unreachable");
	    }
	    this.updateStatusForPingTypeDevices(statusCode,data.get("systemIP"),data.get("id"));
	    new AuditHandler().auditStatus(data,Integer.parseInt(statusCode),oldStatus);
	    return "ok";	
	    }
	  
	  
	  public String getSGNStatusOnUdp(LinkedHashMap<String,String> data) throws Exception
	  {
			fileLogger.info("Inside Function : getSGNStatusOnUdp");
		  int oldStatus = Integer.parseInt(data.get("status")); 
		  String statusCode="2";
		  String ip=data.get("systemIP");
		  String cmd="";
		   String query="select cmd_code from octasic_cmd_code where  cmd_type='status'";
		   Operations operations = new Operations();
		   JSONArray jo = operations.getJson(query);
		   cmd = jo.getJSONObject(0).getString("cmd_code");
		   int port = 2000;		   
		   String key= "paport";
		   
		   port = Integer.parseInt(new Common().getDbCredential().get(key));
		   statusLogger.debug("Sending Ping Request to " + data.get("systemIP"));
	 
	    String result2=new UdpServerClient().send(ip, port, cmd,3);
	    
	    if(result2!=null) {
		    String [] result=result2.split(",");
		    if(result[0]== null||result[0].equalsIgnoreCase("fail")) {
		    	statusLogger.debug("Host is unreachable");
		    }
		    else {
		    	statusLogger.debug("Host is reachable");
		    	statusCode=result[2];
		    	data.put("TEMP",result[3]);
		    }
	    
	    }
	    this.updateStatusForSgnDevice(data,statusCode,data.get("systemIP"),data.get("id"));
	    new AuditHandler().auditStatus(data,Integer.parseInt(statusCode),oldStatus);
	    fileLogger.info("Exit Function : getSGNStatusOnUdp");
	    return "ok";	
	    }
	//Scheduler Update Status
	public String getCurrentStatusOfBts(LinkedHashMap<String,String> data)
	{
		fileLogger.info("Inside Function : getCurrentStatusOfBts");
		try 
		{
			switch(data.get("systemCode"))
			{
				case "0":
					getStatusFor3gDevice(data);
				break;
				case "1":
					getStatusFor3gDevice(data);
				break;
				case "2":
					getStatusFor3gDevice(data);
				break;
				case "3":
					getStatusForScanerDevice(data);
				break;
				case "4":
					getStatusFor2gDevice(data);
				break;
				case "5":
					getStatusFor2gDevice(data);
				break;
				case "6":
					getStatusFor2gDevice(data);
				break;
				case "7":
					getSGNStatusOnUdp(data);
				//	getStatusForPingTypeDevices(data);
				break;
				case "8":
				//	getStatusForPingTypeDevices(data);
				//break;
				case "16":
				case "9":
					getStatusForPingTypeDevices(data);
				break;
				case "10":
					getStatusForSystemManagerDevice(data);
					break;
				case "11":
					getStatusForFinleyDevice(data);
					break;
				case "12":
					getStatusForHummerDevice(data);
					break;
				case "13":
					getStatusFor4gDevice(data);
					break;
				case "14":
					getStatusFor4gDevice(data);
					break;
			case "15":
					getStatusFor4gDevice(data);
			case "17":
				    getStatusForJammer(data);
					break;
					
					
				
				
			}
		}catch(Exception e) 
		{
			
			//fileLogger.debug("==============Exception while getting status = =================");
			e.printStackTrace();
			//fileLogger.debug("==============Exception while getting status = =================");
			try 
			{
				if(data.get("systemCode").equals("11") || data.get("systemCode").equals("12")){
					new NetscanOperations().updateIDSStatus(2,data.get("systemIP"),data.get("id"));
					//new AuditHandler().auditStatus(data, newStatusCode, statusCode);
					int statusCode = data.get("status")!=null?Integer.parseInt(data.get("status")):-1;
					new AuditHandler().auditIDSStatus(data, 2, statusCode);
				}else{
					int newStatus = 2;
					JSONObject jb = new JSONObject();	
					jb.put("CMD_CODE", "SET_CURR_STATUS");
					jb.put("CELL_OP_STATE", 0);
					jb.put("CELL_ADM_STATE", 0);
					
					String exceptionMsg=e.getMessage();
					if(exceptionMsg.indexOf("ConnectException")!=-1){
						jb.put("SYSTEM_STATUS", 3);
						newStatus = 3;
					}else{
						jb.put("SYSTEM_STATUS", 2);	
					}
					this.updateBtsStatus(jb.toString(),data.get("systemIP"));
					new AuditHandler().auditStatus(data,newStatus,Integer.parseInt(data.get("status")));
				}
			}
			catch(Exception e1) 
			{			
			}
		}
		fileLogger.info("Exit Function : getCurrentStatusOfBts");
		return "";
	}
	
	public String getCurrentSoftVerOfDevices(LinkedHashMap<String,String> data)
	{
		
		try 
		{
			switch(data.get("systemCode"))
			{
				case "0":
					getSoftVerOf3gDevice(data);
				break;
				case "1":
					getSoftVerOf3gDevice(data);
				break;
				case "2":
					getSoftVerOf3gDevice(data);
				break;
				case "3":
					getSoftVerOfScanerDevice(data);
				break;
				case "4":
					getSoftVerOf2gDevice(data);
				break;
				case "5":
					getSoftVerOf2gDevice(data);
				break;
				case "6":
					getSoftVerOf2gDevice(data);
				break;
/*				case "7":
					getSoftVerOfPingTypeDevices(data);
				break;
				case "8":
					getSoftVerOfPingTypeDevices(data);
				break;
				case "9":
					getSoftVerOfPingTypeDevices(data);
				break;*/
				case "10":
					getSoftVerOfSystemManagerDevice(data);
					break;
/*				case "11":
					getSoftVerOfPingTypeDevices(data);
					break;
				case "12":
					getSoftVerOfPingTypeDevices(data);
					break;*/
				case "17":
					getSoftVerOfSpoilerDevice(data);
					break;
					
				
				
			}
		}catch(Exception e) 
		{
			
			//fileLogger.debug("==============Exception while getting status = =================");
			e.printStackTrace();
			//fileLogger.debug("==============Exception while getting status = =================");
			try 
			{
/*				int newStatus = 2;
				JSONObject jb = new JSONObject();	
				jb.put("CMD_CODE", "SET_CURR_STATUS");
				jb.put("CELL_OP_STATE", 0);
				jb.put("CELL_ADM_STATE", 0);	
				String exceptionMsg=e.getMessage();
				if(exceptionMsg.indexOf("ConnectException")!=-1){
					jb.put("SYSTEM_STATUS", 3);
					newStatus = 3;
				}else{
					jb.put("SYSTEM_STATUS", 2);	
				}
				this.updateBtsStatus(jb.toString(),data.get("systemIP"));
				new AuditHandler().auditStatus(data,newStatus,Integer.parseInt(data.get("status")));*/
			}
			catch(Exception e1) 
			{			
			}
		}
		
		return "";
	}
	
	
	public void updateBtsStatus(String jsonData,String ip)
	{
		fileLogger.info("Inside Function : updateBtsStatus");
		try
		{
			Common.log("updateBtsStatus"+jsonData);
			JSONObject jo = new JSONObject(jsonData);
			statusLogger.debug(jo.toString());		
					
			
	        String cmdCode = jo.getString("CMD_CODE");
	        int systemStatus = jo.getInt("SYSTEM_STATUS");
	        int cellStatus = jo.getInt("CELL_OP_STATE");
	        int adminState = jo.getInt("CELL_ADM_STATE");
			Common co  = new Common();
			String query ="update btsmaster set status="+systemStatus+" ,cellstatus = "+cellStatus+",adminstate="+adminState+" where ip = '"+ip+"'";
			statusLogger.debug(query);
			co.executeDLOperation(query);
		}
		catch(Exception E)
		{
			
			statusLogger.error("Parsing 2g Json updateBtsStatus Exception msg : "+E.getMessage());
		}
		fileLogger.info("Exit Function : updateBtsStatus");
	}
	
	public void updateDeviceSoftVer(String respStr,String ip)
	{
		fileLogger.info("Inside Function : updateDeviceSoftVer");
		try
		{
			//Common.log("updateDeviceSoftVer"+jsonData);
			//JSONObject jo = new JSONObject(jsonData);
			statusLogger.debug(respStr);		
					
			
	        int tag2Index = respStr.indexOf("TAGS02");
	        int startPos = tag2Index + 7;
	        int tag3Index = respStr.indexOf("TAGS03");
	        int endPos = tag3Index -1 ;
	        String swVersion = respStr.substring(startPos,endPos);
			Common co  = new Common();
			String query ="update btsmaster set sw_version='"+swVersion+"' where ip = '"+ip+"'";
			fileLogger.debug(query);
			co.executeDLOperation(query);
		}
		catch(Exception E)
		{
			
			statusLogger.error("Parsing 2g Json updateDeviceSoftVer Exception msg : "+E.getMessage());
		}
		fileLogger.info("Exit Function : updateDeviceSoftVer");
	}
	
	public void updateStatusForPingTypeDevices(String statusCode,String ip,String id)
	{
		
			Common co  = new Common();
			String query ="update btsmaster set status="+statusCode+" where ip = '"+ip+"' and b_id="+id;
			statusLogger.debug(query);
			co.executeDLOperation(query);
	} 
	public void updateStatusForSgnDevice(LinkedHashMap<String,String> data,String statusCode,String ip,String id)
	{
		try {
		fileLogger.info("Inside Function : updateStatusForSgnDevice");
		String Temperature=data.get("TEMP");
		String query="";
			Common co  = new Common();
			if(Temperature!=null){
				query ="update btsmaster set status="+statusCode+",tmp="+Temperature+" where ip = '"+ip+"' and b_id="+id;
			}else {
			   query ="update btsmaster set status="+statusCode+" where ip = '"+ip+"' and b_id="+id;
			}
			//String query ="update btsmaster set status="+statusCode+" where ip = '"+ip+"' and b_id="+id;
			statusLogger.debug(query);
			co.executeDLOperation(query);
			}
		catch(Exception E)
		{
			statusLogger.error(" updateStatusForSgnDevice Exception msg : "+E.getMessage());
		}
		
			fileLogger.info("Exit Function : updateStatusForSgnDevice");
	}
	
	
		public String getStatusFor4gDevice(LinkedHashMap<String, String> data) throws Exception {

		LinkedHashMap<String, String> queryParam = new LinkedHashMap<String, String>();

		queryParam.put("cmdType", data.get("CMD_CODE"));
		queryParam.put("CMD_TYPE", data.get("CMD_CODE"));
		queryParam.put("SYSTEM_CODE", data.get("systemCode"));
		queryParam.put("SYSTEM_ID", data.get("systemId"));

		data.put("data", "{\"CMD_CODE\":\"GET_CURR_STATUS\"}");
		statusLogger.debug(data.get("data"));
		// Common.log(data.get("data"));
		String url = new Common().getDbCredential().get("4gserviceurl");
		Response rs = new ApiCommon().sendRequestToUrl("http://" + data.get("systemIP") + url, queryParam,
				data.get("data"));
		String resultData = "";
		if (rs != null) {

			resultData = rs.readEntity(String.class);
			new ThreegOperations().updateBtsStatus(resultData, data.get("systemIP"), data.get("id"));

			int statusCode = data.get("status") != null ? Integer.parseInt(data.get("status")) : -1;

			JSONObject jo = new JSONObject(resultData);

			int adminState = jo.getInt("CELL_ADM_STATE");

			int newStatusCode = jo.getInt("SYSTEM_STATUS");

			new ApiCommon().insertNodStatusChange(statusCode, newStatusCode, data.get("systemIP"));

			statusLogger.debug("status update Done");
			statusLogger.debug("status update Done " + statusCode + "new " + newStatusCode);
			
			if ((statusCode == 2 || statusCode == 3) && (newStatusCode == 0 || newStatusCode == 1)) {
				statusLogger.debug("sending configuration ip=" + data.get("systemIP"));
				new Common().sendConfigurationToNodeLTE(adminState, data);
				statusLogger.debug("done sending confifuration ip=" + data.get("systemIP"));
			}

			statusLogger.debug("status update Done 2");
		} else {
			statusLogger.debug("throwing exception as rs = " + rs);
			throw new Exception("error while connecting");
		}
		Common.log(resultData);
		return "ok";
	}
		
		
		
		
		
		
		
		
		public String getStatusForJammer(LinkedHashMap<String, String> data) throws Exception {

			// Common.log(data.get("data"));
			//String url = new Common().getDbCredential().get("Jammerserviceurl");
 			String url= "/jammingMode.json";
			// line below is making a post request see the code inside
			String resultData = "";
			//int rs = Integer.parseInt(new ApiCommon().HTTPgetThroughSocket("http://" + data.get("systemIP") + url ,        80));
			resultData = new ApiCommon().HTTP_Request(data.get("systemIP"), 80, url, "GET",""); 
					//(url,data.get("systemIP")  , 80));
			
			//resultData = (new ApiCommon().HTTPgetThroughSocket(url,data.get("systemIP")  , 80));
			if (resultData.equalsIgnoreCase("-100") !=true) {
			
				 JSONObject jsonobj = new JSONObject(resultData); 
				 int rs=Integer.parseInt((jsonobj.getJSONObject("data").getString("jammingMode")));

 				 //resultData = rs.readEntity(String.class);
 				 //resultData="{\"status\":\"200\",\"data\":{\"jammingMode\":\"1\"}}";
				 new ThreegOperations().updateJammerStatus(rs, data.get("systemIP"), data.get("id"));
				
				 int statusCode = data.get("status") != null ? Integer.parseInt(data.get("status")) : -1;
				
				 JSONObject jo = new JSONObject(resultData);

				 int newStatusCode = Integer.parseInt(jo.getJSONObject("data").getString("jammingMode"));
				
				 new ApiCommon().insertNodStatusChange(statusCode, newStatusCode, data.get("systemIP"));
				
				 new AuditHandler().auditStatus(data,(newStatusCode),statusCode);
			     statusLogger.debug("status update Jammer =  " + statusCode + "new " + newStatusCode);
				
				

				
				}
			    else 
			    {
					statusLogger.debug("throwing exception as resultData = " + resultData);
					new ThreegOperations().updateJammerStatus(2, data.get("systemIP"), data.get("id"));
					
					throw new Exception("error while connecting");
				}
				Common.log(resultData);
				return "ok";
		}
		
public boolean checkPing(String ip){
	boolean isReachable=true;
	
	
	final IcmpPingRequest request = IcmpPingUtil.createIcmpPingRequest();
	request.setHost (ip);

	// repeat a few times
	for (int count = 1; count <= 2; count ++) {

	// delegate
	final IcmpPingResponse response = IcmpPingUtil.executePingRequest (request);

	// log
	final String formattedResponse = IcmpPingUtil.formatResponse (response);
	//System.out.println (formattedResponse);
	if(formattedResponse.contains("Error")){
		isReachable=false;
	}
	else{
		isReachable=true;
		break;
	}
	}
	// rest
	
//	}
	
	return isReachable;
	     
}
		
}
