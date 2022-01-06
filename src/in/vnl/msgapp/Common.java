package in.vnl.msgapp;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import in.vnl.EventProcess.DBDataService;
import in.vnl.api.threeg.ThreegOperations;
import in.vnl.api.fourg.FourgOperations;

public class Common {
	static Logger fileLogger = Logger.getLogger("file");
	public String callPostDataUrl(String myURL,List<NameValuePair> params)
	{
		fileLogger.info("Inside Function : callPostDataUrl");	
		String content = null;
		HttpClient httpClient = new DefaultHttpClient();
		fileLogger.debug("Called URL"+myURL);
		HttpPost httpPost = new HttpPost(myURL);		
		try 
		{
			fileLogger.debug(params);
			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			
			
			auditLog(convertToMapFor2g(myURL,params.toString(),"S"));
			
			
		} catch (UnsupportedEncodingException e) {
		    // writing error to Log
			auditLog(convertToMapFor2g(myURL,e.getMessage(),"E"));
			e.printStackTrace();
		}
		/*
		 * Execute the HTTP Request
		 */
		
		try {
		    
			HttpResponse response = httpClient.execute(httpPost);
		    HttpEntity respEntity = response.getEntity();

		    if (respEntity != null) 
		    {
		        //EntityUtils to get the response content
		        content =  EntityUtils.toString(respEntity);
		        fileLogger.debug(content);
		        
		      
		        
				auditLog(convertToMapFor2g("response",response+" : "+content,"R"));
				
		        
		    }
		} 
		catch (ClientProtocolException e) 
		{
		    // writing exception to log
			auditLog(convertToMapFor2g(myURL,e.getMessage(),"E"));
			e.printStackTrace();
		    
		    
		} 
		catch (IOException e) 
		{
		    // writing exception to log
			auditLog(convertToMapFor2g(myURL,e.getMessage(),"E"));
		    e.printStackTrace();
		}
		fileLogger.info("Exit Function : callPostDataUrl");	
		return content;
	}
	
	public String callPostDataUrlForStatus(String myURL,List<NameValuePair> params) throws Exception
	{
		fileLogger.info("Inside Function : callPostDataUrlForStatus");	
		String content = null;
		HttpClient httpClient = new DefaultHttpClient();
		fileLogger.debug("Called URL"+myURL);
		HttpPost httpPost = new HttpPost(myURL);		
		try 
		{
			fileLogger.debug(params);
			HttpParams httpParams = httpClient.getParams();
			httpParams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60000);
			httpParams.setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
			httpParams.setParameter(ClientPNames.CONN_MANAGER_TIMEOUT, 60000L);
			
			auditLog(convertToMapFor2g(myURL,params.toString(),"S"));
			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
		    // writing error to Log
			fileLogger.debug("@status exception in callPostDataUrlForStatus message :"+e.getMessage());
			fileLogger.debug("url is :"+myURL+": and  params are :"+params.toString());
			auditLog(convertToMapFor2g(myURL,e.getMessage(),"E"));
		    e.printStackTrace();
		}
		/*
		 * Execute the HTTP Request
		 */
		
		try {
		    
			HttpResponse response = httpClient.execute(httpPost);
		    HttpEntity respEntity = response.getEntity();

		    if (respEntity != null) {
		        // EntityUtils to get the response content
		        content =  EntityUtils.toString(respEntity);
		        auditLog(convertToMapFor2g("response",content,"S"));
		        fileLogger.debug(content);
		    }
		} catch (Exception e){
			auditLog(convertToMapFor2g(myURL,e.getMessage(),"E"));
			throw e;
		}
		fileLogger.info("Exit Function : callPostDataUrlForStatus");
		return content;
	}
	
	
	public String aa(String myURL,String CMD_TYPE,String TAGS00,String TAGS01,String TAGS02)
	{
		fileLogger.info("Inside Function : aa");
		HttpClient httpClient = new DefaultHttpClient();
		
		fileLogger.debug("Called URL"+myURL);
		
		HttpPost httpPost = new HttpPost(myURL);
		
		// Request parameters and other properties.
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("CMD_TYPE",CMD_TYPE ));
		params.add(new BasicNameValuePair("TAGS00", TAGS00));
		params.add(new BasicNameValuePair("TAGS01", TAGS01));
		params.add(new BasicNameValuePair("TAGS02", ""));
		params.add(new BasicNameValuePair("TAGS03", TAGS02));
		try 
		{
			fileLogger.debug(params);
			auditLog(convertToMapFor2g(myURL,params.toString(),"S"));
			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
		    // writing error to Log
			auditLog(convertToMapFor2g(myURL,e.getMessage(),"E"));
			e.printStackTrace();
		}
		/*
		 * Execute the HTTP Request
		 */
		String content = null;
		try {
		    HttpResponse response = httpClient.execute(httpPost);
		    HttpEntity respEntity = response.getEntity();

		    if (respEntity != null) {
		        // EntityUtils to get the response content
		        content =  EntityUtils.toString(respEntity);
		        auditLog(convertToMapFor2g("response",response+" : "+content,"R"));
		        fileLogger.debug(content);
		    }
		} catch (ClientProtocolException e) {
		    // writing exception to log
			auditLog(convertToMapFor2g(myURL,e.getMessage(),"E"));
		    e.printStackTrace();
		} catch (IOException e) {
		    // writing exception to log
			auditLog(convertToMapFor2g(myURL,e.getMessage(),"E"));
		    e.printStackTrace();
		}
		fileLogger.info("Exit Function : aa");
		return content;
	}
	
	
	public String bb(String myURL,String CMD_TYPE,String TAGS00,String TAGS01,String TAGS02,String TAGS03,String TAGS04,String TAGS05)
	{
		fileLogger.info("Inside Function : bb");
		HttpClient httpClient = new DefaultHttpClient();
		fileLogger.debug("Called URL"+myURL);
		HttpPost httpPost = new HttpPost(myURL);
		// Request parameters and other properties.
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("CMD_TYPE",CMD_TYPE ));
		params.add(new BasicNameValuePair("TAGS00", TAGS00));
		params.add(new BasicNameValuePair("TAGS01", TAGS01));
		params.add(new BasicNameValuePair("TAGS02", TAGS02));
		params.add(new BasicNameValuePair("TAGS03", TAGS03));
		params.add(new BasicNameValuePair("TAGS04", TAGS04));
		params.add(new BasicNameValuePair("TAGS05", TAGS05));
		try 
		{
			fileLogger.debug(params);
			auditLog(convertToMapFor2g(myURL,params.toString(),"S"));
			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
		    // writing error to Log
			auditLog(convertToMapFor2g(myURL,e.getMessage(),"E"));
		    e.printStackTrace();
		}
		/*
		 * Execute the HTTP Request
		 */
		String content = null;
		try {
		    HttpResponse response = httpClient.execute(httpPost);
		    HttpEntity respEntity = response.getEntity();

		    if (respEntity != null) {
		        // EntityUtils to get the response content
		        content =  EntityUtils.toString(respEntity);
		        auditLog(convertToMapFor2g("response",response+" : "+content,"R"));
		        fileLogger.debug(content);
		    }
		} catch (ClientProtocolException e) {
		    // writing exception to log
			auditLog(convertToMapFor2g(myURL,e.getMessage(),"E"));
		    e.printStackTrace();
		} catch (IOException e) {
		    // writing exception to log
			auditLog(convertToMapFor2g(myURL,e.getMessage(),"E"));
		    e.printStackTrace();
		}
		fileLogger.info("Exit Function : bb");
		return content;
	}
	
	
	public HashMap<String,String> getDbCredential()
	{
		fileLogger.info("Inside Function : getDbCredential");
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
		hm.put("user", prop.getProperty("dbuser"));
		hm.put("pass", prop.getProperty("dbpassword"));
		hm.put("db", prop.getProperty("dbname"));
		hm.put("port", prop.getProperty("dbport"));
		hm.put("host", prop.getProperty("dbhost"));
		hm.put("RecadgTime_GPS", prop.getProperty("RecadgTime_GPS"));
		hm.put("nib", prop.getProperty("nib"));
		hm.put("allnibs", prop.getProperty("allnibs"));
		hm.put("mapserver", prop.getProperty("mapserver"));
		hm.put("3gserviceurl", prop.getProperty("3gserviceurl"));
		hm.put("netscanserviceurl", prop.getProperty("netscanserviceurl"));
		hm.put("statusUpdatePeriodicity", prop.getProperty("statusUpdatePeriodicity"));
		hm.put("lat",prop.getProperty("lat"));
		hm.put("lon",prop.getProperty("lon"));
		hm.put("bmsDataPort",prop.getProperty("bmsDataPort"));
		hm.put("udpserverport",prop.getProperty("udpserverport"));
		hm.put("paport",prop.getProperty("paport"));
		hm.put("lnaport",prop.getProperty("lnaport"));
		hm.put("scantime",prop.getProperty("scantime"));
		hm.put("tracktime",prop.getProperty("tracktime"));
		hm.put("backupdir",prop.getProperty("backupdir"));
		hm.put("usedspacelimit",prop.getProperty("usedspacelimit"));
		hm.put("ptz_port",prop.getProperty("ptz_port"));
		hm.put("faultIP",prop.getProperty("faultIP"));
		hm.put("faultPort",prop.getProperty("faultPort"));
		hm.put("ptz_speed",prop.getProperty("ptz_speed"));
		hm.put("4gserviceurl", prop.getProperty("4gserviceurl"));
		hm.put("Jammerserviceurl", prop.getProperty("Jammerserviceurl"));
		hm.put("UIscannerStopStart_restartScanner", prop.getProperty("UIscannerStopStart_restartScanner"));
		fileLogger.info("Exit Function : getDbCredential");
		
        return hm;
	}
	
	public Connection getDbConnection()
	{
		fileLogger.info("Inside Function : getDbConnection");
		Connection con = null;
		HashMap<String,String> dbData = getDbCredential();
		try
		{
			Class.forName("org.postgresql.Driver");
			
			con = DriverManager.getConnection(
			   "jdbc:postgresql://"+dbData.get("host")+":"+dbData.get("port")+"/"+dbData.get("db"),dbData.get("user"), dbData.get("pass"));
			/*con = DriverManager.getConnection(
					"jdbc:postgresql://"+dbData.get("host")+":"+dbData.get("port")+"/locator", "postgres",
					"postgres");*/
		}
		catch(Exception E)
		{
			
			//fileLogger.debug("*****************************************");
			//fileLogger.debug("Class = Common , Method : getDbConnection");
			fileLogger.error("Erorr During createing the connection with the database"+ E.getMessage());
			//fileLogger.debug(E.getMessage());
			E.printStackTrace();
			//fileLogger.debug("*****************************************");
			
		}	
		fileLogger.info("Exit Function : getDbConnection");
		return con;
	}
	
	public boolean executeDLOperation(String query)
	{
		fileLogger.info("Inside Function : executeDLOperation");
		Connection con = getDbConnection();
		Statement smt = null;
		int returnValue = 0;
		try
		{
			smt=con.createStatement();
			fileLogger.debug("query"+query);
			returnValue = smt.executeUpdate(query);
			smt.close();
			
		}
		catch(Exception E)
		{
			fileLogger.error("Error While Executing the query"+E.getMessage());
			fileLogger.debug("query"+query);
			E.printStackTrace();
			
		}
		finally
		{
			try
			{
				con.close();
			}
			catch(Exception E)
			{
				fileLogger.error("Error while closing the connection inside the executeDLOperation method "+E.getMessage());
				E.printStackTrace();
			}
		}
		fileLogger.info("Exit Function : executeDLOperation");
		return returnValue >0?true:false;
		
	}
	
	
	
	public boolean executeBatchOperation(List<String> sqls)
	{
		fileLogger.info("Inside Function : executeBatchOperation");
		Connection con = getDbConnection();
		Statement stmt = null;
		
		try
		{
			stmt = con.createStatement();
			for (String sql : sqls) {
				//fileLogger.debug(temp);
				stmt.addBatch(sql);
			}
			int[] count = stmt.executeBatch();
			stmt.close();
			
		}
		catch(Exception E)
		{
			fileLogger.error("Error While Executing the query"+E.getMessage());
			fileLogger.debug("query"+sqls);
			
		}
		finally
		{
			try
			{
				con.close();
			}
			catch(Exception E)
			{
				fileLogger.error("Error while closing the connection inside the executeDLOperation method "+E.getMessage());
			}
		}
		int returnValue = 0;
		fileLogger.info("Exit Function : executeBatchOperation");
		return returnValue >0?true:false;
		
	}
	
	public void checkIfUserIsLogedIn(HttpServletRequest request,HttpServletResponse response)
	{}
	
	public static String callURL(String myURL) {
		fileLogger.info("Inside Function : callURL");
		fileLogger.debug("Requeted URL:" + myURL);
		
		StringBuilder sb = new StringBuilder();
		URLConnection urlConn = null;
		InputStreamReader in = null;
		try {
			myURL = myURL.replaceAll(" ", "%20");
			URL url = new URL(myURL);
			urlConn = url.openConnection();
			//if (urlConn != null)
			//	urlConn.setReadTimeout(60 * 1000);
			if (urlConn != null && urlConn.getInputStream() != null) {
				in = new InputStreamReader(urlConn.getInputStream(),
						Charset.defaultCharset());
				BufferedReader bufferedReader = new BufferedReader(in);
				if (bufferedReader != null) {
					int cp;
					while ((cp = bufferedReader.read()) != -1) {
						sb.append((char) cp);
					}
					bufferedReader.close();
				}
			}
		in.close();
		} catch (Exception e) {
			throw new RuntimeException("Exception while calling URL:"+ myURL, e);
		} 
 
		return sb.toString();
	}

	public long convertToMilliSec(String dateTime)
{
	long millis = 0L;
	try
	{
		String myDate = dateTime;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = sdf.parse(myDate);
		millis = date.getTime();
		
		return millis;
	}
	catch(Exception E)
	{
		
	}
	fileLogger.info("Exit Function : callURL");
return millis;
}


	public String convertMilliSecToDateTimeFormat(Long dateTime)
{	
	String convertedStringFromMilliSec = "";
	try
	{
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy-HH-mm-ss");
		convertedStringFromMilliSec = sdf.format(new Date(dateTime));
		
	}
	catch(Exception E)
	{
		
	}
	return convertedStringFromMilliSec;
}


	public static void log(String message)
	{
		fileLogger.debug(message);
	}



	public int executeQueryAndReturnId(String query)
{
		fileLogger.info("Inside Function : executeQueryAndReturnId");
	Connection con = getDbConnection();
	
	Statement smt = null;
	ResultSet rs = null;
	int returnValue=0;
	
	try
	{
		smt=con.createStatement();
		fileLogger.debug("query"+query);
		rs=smt.executeQuery(query);
		
		while(rs.next())
		{
			fileLogger.debug(rs.getString(1));
			returnValue= Integer.parseInt(rs.getString(1));
		}		smt.close();
	}  
	catch(Exception E)
	{
		fileLogger.error("Error While Executing the query"+E.getMessage());
		fileLogger.debug("query"+query);
	}
	finally
	{
		try
		{
			try {
				rs.close();
			} catch (Exception e) {
				// TODO: handle exception
			}
			con.close();
		}
		catch(Exception E)
		{
			fileLogger.error("Error while closing the connection inside the executeQueryAndReturnId method "+E.getMessage());
		}
	}

	fileLogger.info("Exit Function : executeQueryAndReturnId");
	return returnValue;
}

	public boolean sendConfigurationToNode(int adminState,LinkedHashMap<String,String> data)
{

		fileLogger.info("Inside Function : sendConfigurationToNode");
	fileLogger.debug("config 1");
	try 
	{
		String config = data.get("config");
		String cellId = "";
		int deviceTypeId = Integer.parseInt(data.get("devicetypeid"));
		fileLogger.debug("config 2");
		try 
		{
			cellId = new JSONObject(config).getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").getString("CELL_ID");
		}
		catch(Exception e) 
		{
			fileLogger.error("Error while getting celling method sendConfigurationToNode class Common");
		}
		
		fileLogger.debug("config 3");
		if(adminState == 0 || adminState == 2) 
		{
			
			data.put("CMD_TYPE", "SET_CELL_LOCK");
			data.put("cmdType", "SET_CELL_LOCK");
			data.put("data", "{\"CMD_CODE\":\"SET_CELL_LOCK\",\"CELL_ID\":\""+cellId+"\",\"LAC\":\"0\"}");
			new ThreegOperations().setCellLock(data);
			
		}
		
		fileLogger.debug("config 4");
		data.put("CMD_TYPE", "SET_SUFI_CONFIG");
		data.put("cmdType", "SET_SUFI_CONFIG");
		data.put("data", get3gconfigurationSturcture(config));
		new ThreegOperations().setSufiConfig(data);
		if(adminState == 2) 
		{
			data.put("CMD_TYPE", "SET_CELL_UNLOCK");
			data.put("cmdType", "SET_CELL_UNLOCK");
			data.put("data", "{\"CMD_CODE\":\"SET_CELL_UNLOCK\",\"CELL_ID\":\""+cellId+"\",\"LAC\":\"0\"}");
			new ThreegOperations().setCellLock(data);
		}
		fileLogger.debug("config 5");
		if( deviceTypeId == 2|| deviceTypeId == 3) 
		{
			data.put("CMD_TYPE", "SET_MEAS_TRIGGER");
			data.put("cmdType", "SET_MEAS_TRIGGER");
			data.put("data", "{\"CMD_CODE\":\"SET_MEAS_TRIGGER\",\"TRIGGER\":\"1\"}");
			new ThreegOperations().triggerMes(data);
		}
		fileLogger.debug("config 6");
	}
	catch(Exception e) 
	{
		fileLogger.debug(e.getMessage());
		return false;
	}
	fileLogger.info("Exit Function : sendConfigurationToNode");
	
	return true;
	
}	
	
	public boolean ConfigurationOnStatusChangeUMTS(int adminState,LinkedHashMap<String,String> data)
{

	fileLogger.info("Inside Function : ConfigurationOnStatusChangeUMTS");
	fileLogger.debug("config 1");
	try 
	{
		String config = data.get("config");
		String cellId = "";
		int deviceTypeId = Integer.parseInt(data.get("devicetypeid"));
		fileLogger.debug("config 2");
		try 
		{
			cellId = new JSONObject(config).getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").getString("CELL_ID");
		}
		catch(Exception e) 
		{
			fileLogger.error("Error while getting celling method sendConfigurationToNode class Common");
		}
		
		JSONArray ja = new Operations().getJson("select count(*) as count from running_mode where mode_status='Active' and mode_type='track'");
		
		int count = ja.getJSONObject(0).getInt("count");
		
		if (adminState == 2 && count == 0) {
			data.put("CMD_TYPE", "SET_CELL_LOCK");
			data.put("cmdType", "SET_CELL_LOCK");
			data.put("data", "{\"CMD_CODE\":\"SET_CELL_LOCK\",\"CELL_ID\":\""+cellId+"\",\"LAC\":\"0\"}");
			new ThreegOperations().setCellLock(data);
			fileLogger.debug("configuration on status change_locked");
		}
		
		if( deviceTypeId == 2|| deviceTypeId == 3) 
		{
			data.put("CMD_TYPE", "SET_MEAS_TRIGGER");
			data.put("cmdType", "SET_MEAS_TRIGGER");
			data.put("data", "{\"CMD_CODE\":\"SET_MEAS_TRIGGER\",\"TRIGGER\":\"1\"}");
			new ThreegOperations().triggerMes(data);
			fileLogger.debug("configuarton on status change triggerd_MEAS");
		}
	
	}
	catch(Exception e) 
	{
		fileLogger.debug(e.getMessage());
		return false;
	}
	fileLogger.info("Exit Function : ConfigurationOnStatusChangeUMTS");
	
	return true;
	
}	
	
	public boolean send4gConfigurationToNode(int adminState,LinkedHashMap<String,String> data)
{
	fileLogger.info("Inside Function : send4gConfigurationToNode");
	fileLogger.debug("config 4g 1");
	try 
	{
		String config = data.get("config");
		String cellId = "";
		int deviceTypeId = Integer.parseInt(data.get("devicetypeid"));
		fileLogger.debug("config 4G 2");
		try 
		{
			cellId = new JSONObject(config).getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").getString("CELL_ID");
		}
		catch(Exception e) 
		{
			fileLogger.error("Error while getting celling method send4gConfigurationToNode class Common");
		}
		
		fileLogger.debug("config 3");
		if(adminState == 0 || adminState == 2) 
		{
			
			data.put("CMD_TYPE", "SET_CELL_LOCK");
			data.put("cmdType", "SET_CELL_LOCK");
			data.put("data", "{\"CMD_CODE\":\"SET_CELL_LOCK\",\"CELL_ID\":\""+cellId+"\",\"LAC\":\"0\"}");
			new FourgOperations().setCellLock(data);
			
		}
		
		fileLogger.debug("config 4");
		data.put("CMD_TYPE", "SET_SUFI_CONFIG");
		data.put("cmdType", "SET_SUFI_CONFIG");
		data.put("data", get4gconfigurationSturcture(config));
		new FourgOperations().setSufiConfigAuto(data);
		if(adminState == 2) 
		{
			data.put("CMD_TYPE", "SET_CELL_UNLOCK");
			data.put("cmdType", "SET_CELL_UNLOCK");
			data.put("data", "{\"CMD_CODE\":\"SET_CELL_UNLOCK\",\"CELL_ID\":\""+cellId+"\",\"LAC\":\"0\"}");
			new FourgOperations().setCellUnlock(data);
		}
		fileLogger.debug("config 5");
		if( deviceTypeId == 2|| deviceTypeId == 3) 
		{
			data.put("CMD_TYPE", "SET_MEAS_TRIGGER");
			data.put("cmdType", "SET_MEAS_TRIGGER");
			data.put("data", "{\"CMD_CODE\":\"SET_MEAS_TRIGGER\",\"TRIGGER\":\"1\"}");
			new FourgOperations().triggerMes(data);
		}
		fileLogger.debug("config 6");
	}
	catch(Exception e) 
	{
		fileLogger.debug(e.getMessage());
		return false;
	}
	fileLogger.info("Inside Function : send4gConfigurationToNode");
	return true;
	
}
	
	public boolean sendDefaultConfigurationToNode(HashMap<String,String> data)
{
    fileLogger.info("Inside Function : sendDefaultConfigurationToNode");
	fileLogger.debug("config 1");
	try 
	{
		String config = data.get("config");
		int deviceTypeId = Integer.parseInt(data.get("devicetypeid"));
		fileLogger.debug("config 2");
		
		fileLogger.debug("config 3");
		data.put("CMD_TYPE", "SET_SUFI_CONFIG");
		data.put("cmdType", "SET_SUFI_CONFIG");
		data.put("data", get3gconfigurationSturcture(config));
		new ThreegOperations().setDefaultSufiConfig(data);
		fileLogger.debug("config 5");
	}
	catch(Exception e) 
	{
		fileLogger.debug(e.getMessage());
		return false;
	}
    fileLogger.info("Exit Function : sendDefaultConfigurationToNode");
	return true;
	
}




/*
 * 
 * Returns 3g configuration string
 * */

	
	public String get3gconfigurationSturcture(String config) 
{
		
	JSONObject js = null;
	String lines = "";
	try 
	{
		js = new JSONObject(config);
	
	
	
	lines = "{" + 
			"  \"CMD_CODE\": \""+js.get("CMD_CODE")+"\"," + 
			"  \"BITMASK\": \""+js.get("BITMASK")+"\"," + 
			"  \"SYS_PARAMS\": {" + 
			"    \"BITMASK\": \""+js.getJSONObject("SYS_PARAMS").get("BITMASK")+"\"," + 
			"    \"SUFI_PARAMS\": {" + 
			"      \"LOG_LEVEL\": \""+js.getJSONObject("SYS_PARAMS").getJSONObject("SUFI_PARAMS").get("LOG_LEVEL")+"\"," + 
			"      \"SUB_HOLD_TIMER\": \""+js.getJSONObject("SYS_PARAMS").getJSONObject("SUFI_PARAMS").get("SUB_HOLD_TIMER")+"\"," + 
			"      \"SUB_REDIR_TIMER\": \""+js.getJSONObject("SYS_PARAMS").getJSONObject("SUFI_PARAMS").get("SUB_REDIR_TIMER")+"\"," + 
			"      \"SUFI_OP_MODE\": \""+js.getJSONObject("SYS_PARAMS").getJSONObject("SUFI_PARAMS").get("SUFI_OP_MODE")+"\"," + 
			"      \"SUB_TRACK_MODE\": {"+
			"        \"TRACK_MODE\": \""+js.getJSONObject("SYS_PARAMS").getJSONObject("SUFI_PARAMS").getJSONObject("SUB_TRACK_MODE").get("TRACK_MODE")+"\"" + 
			"      }" + 
			"    }," + 
			"    \"SUB_INFO\": {" + 
			"      \"SUB_LIST_MODE\": \""+js.getJSONObject("SYS_PARAMS").getJSONObject("SUB_INFO").get("SUB_LIST_MODE")+"\"," + 
			"      \"SUB_LIST\": "+js.getJSONObject("SYS_PARAMS").getJSONObject("SUB_INFO").getJSONArray("SUB_LIST").toString() +"," + 
			"      \"HOLD_SUB\": {" + 
			"        \"SUB_ID\": \""+js.getJSONObject("SYS_PARAMS").getJSONObject("SUB_INFO").getJSONObject("HOLD_SUB").get("SUB_ID")+"\"," + 
			"        \"SUB_ID_TYPE\": \""+js.getJSONObject("SYS_PARAMS").getJSONObject("SUB_INFO").getJSONObject("HOLD_SUB").get("SUB_ID_TYPE")+"\"" + 
			"      }" + 
			"    }," + 
			"    \"CELL_INFO\": {" + 
			"      \"SUFI_ID\": \""+js.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").get("SUFI_ID")+"\"," + 
			"      \"PRI_SCRAM_CODE\": \""+js.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").get("PRI_SCRAM_CODE")+"\"," + 
			"      \"LAC_POOL_START\": \""+js.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").get("LAC_POOL_START")+"\"," + 
			"      \"LAC_POOL_END\": \""+js.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").get("LAC_POOL_END")+"\"," + 
			"      \"CELL_ID\": \""+js.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").get("CELL_ID")+"\"," + 
			"      \"TOTAL_TX_POWER\": \""+js.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").get("TOTAL_TX_POWER")+"\"," + 
			"      \"PCPICH_POWER_PERC\": \""+js.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").get("PCPICH_POWER_PERC")+"\"," + 
			"      \"DL_UARFCN\": \""+js.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").get("DL_UARFCN")+"\"," + 
			"      \"UL_UARFCN\": \""+js.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").get("UL_UARFCN")+"\"," + 
			"      \"PLMN_ID\": {" + 
			"        \"MCC\": \""+js.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").getJSONObject("PLMN_ID").get("MCC")+"\"," + 
			"        \"MNC\": \""+js.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").getJSONObject("PLMN_ID").get("MNC")+"\"" + 
			"      }" + 
			"    }" + 
			"  }," + 
			"  \"CELL_PARAMS\": {" + 
			"    \"BITMASK\": \""+js.getJSONObject("CELL_PARAMS").get("BITMASK")+"\"," + 
			"    \"DCH\": {" + 
			"      \"DPCCH_POWER_OFF\": \""+js.getJSONObject("CELL_PARAMS").getJSONObject("DCH").get("DPCCH_POWER_OFF")+"\"" + 
			"    }," + 
			"    \"RRC_SETUP\": {" + 
			"      \"DPCH_FRAME_OFF\": \""+js.getJSONObject("CELL_PARAMS").getJSONObject("RRC_SETUP").get("DPCH_FRAME_OFF")+"\"" + 
			"    }," + 
			"    \"REDIRECTION_INFO\": "+js.getJSONObject("CELL_PARAMS").getJSONObject("REDIRECTION_INFO").toString()+"," + 
			"    \"SIB_INFO\": {" + 
			"      \"CELLSELECTIONQUALITYMEASURE\": \""+js.getJSONObject("CELL_PARAMS").getJSONObject("SIB_INFO").get("CELLSELECTIONQUALITYMEASURE")+"\"," + 
			"      \"MAXIMUMREPORTEDCELLSONRACH\": \""+js.getJSONObject("CELL_PARAMS").getJSONObject("SIB_INFO").get("MAXIMUMREPORTEDCELLSONRACH")+"\"," + 
			"      \"NEIGH_CELL_LIST\": {" + 
			//"        \"INTRA_FREQ\": "+js.getJSONObject("CELL_PARAMS").getJSONObject("SIB_INFO").getJSONObject("NEIGH_CELL_LIST").getJSONArray("INTRA_FREQ").toString()+"," + 
			//"        \"INTER_FREQ\": "+js.getJSONObject("CELL_PARAMS").getJSONObject("SIB_INFO").getJSONObject("NEIGH_CELL_LIST").getJSONArray("INTER_FREQ").toString()+"," + 
			//"        \"INTER_RAT\": "+js.getJSONObject("CELL_PARAMS").getJSONObject("SIB_INFO").getJSONObject("NEIGH_CELL_LIST").getJSONArray("INTER_RAT").toString()+"" + 
			"        \"INTRA_FREQ\": ["+createStringForIntra_freq(js.getJSONObject("CELL_PARAMS").getJSONObject("SIB_INFO").getJSONObject("NEIGH_CELL_LIST").getJSONArray("INTRA_FREQ"))+"]," +
			"        \"INTER_FREQ\": ["+createStringForINTER_FREQ(js.getJSONObject("CELL_PARAMS").getJSONObject("SIB_INFO").getJSONObject("NEIGH_CELL_LIST").getJSONArray("INTER_FREQ"))+"]," +
			"        \"INTER_RAT\":  ["+createStringForINTER_RAT(js.getJSONObject("CELL_PARAMS").getJSONObject("SIB_INFO").getJSONObject("NEIGH_CELL_LIST").getJSONArray("INTER_RAT"))+"]" + 
			"      }" + 
			"    }" + 
			"  }" + 
			"}";

	}
	catch(Exception e) 
	{
		
	}

return lines;
}
	
	/*
	 * 
	 * Returns 3g configuration string
	 * */

		
		public String get4gconfigurationSturcture(String config) 
	{
		JSONObject js = null;
		String lines = "";
		try 
		{
			js = new JSONObject(config);
		
		
		
		lines = "{" + 
				"  \"CMD_CODE\": \""+js.get("CMD_CODE")+"\"," + 
				"  \"BITMASK\": \""+js.get("BITMASK")+"\"," + 
				"  \"SYS_PARAMS\": {" + 
				"    \"BITMASK\": \""+js.getJSONObject("SYS_PARAMS").get("BITMASK")+"\"," + 
				"    \"SUFI_PARAMS\": {" + 
				"      \"LOG_LEVEL\": \""+js.getJSONObject("SYS_PARAMS").getJSONObject("SUFI_PARAMS").get("LOG_LEVEL")+"\"," + 
				"      \"SUB_HOLD_TIMER\": \""+js.getJSONObject("SYS_PARAMS").getJSONObject("SUFI_PARAMS").get("SUB_HOLD_TIMER")+"\"," + 
				"      \"SUB_REDIR_TIMER\": \""+js.getJSONObject("SYS_PARAMS").getJSONObject("SUFI_PARAMS").get("SUB_REDIR_TIMER")+"\"," + 
				"      \"SUFI_OP_MODE\": \""+js.getJSONObject("SYS_PARAMS").getJSONObject("SUFI_PARAMS").get("SUFI_OP_MODE")+"\"," + 
				"      \"SUB_TRACK_MODE\": {"+
				"        \"TRACK_MODE\": \""+js.getJSONObject("SYS_PARAMS").getJSONObject("SUFI_PARAMS").getJSONObject("SUB_TRACK_MODE").get("TRACK_MODE")+"\"" + 
				"      }," +
				"      \"SUFI_OFFLINE_MODE_CONFIG\": {"+
				"        \"SUFI_OFFLINE_MODE\": \""+js.getJSONObject("SYS_PARAMS").getJSONObject("SUFI_PARAMS").getJSONObject("SUFI_OFFLINE_MODE_CONFIG").get("SUFI_OFFLINE_MODE")+"\"," + 
				"      \"SUFI_OFFLINE_FILE_CONF\": {"+
				"       \"FILE_SIZE\": \""+js.getJSONObject("SYS_PARAMS").getJSONObject("SUFI_PARAMS").getJSONObject("SUFI_OFFLINE_MODE_CONFIG").getJSONObject("SUFI_OFFLINE_FILE_CONF").get("FILE_SIZE")+"\"," + 
				"	     \"MEMORY_THRESHOLD\": \""+js.getJSONObject("SYS_PARAMS").getJSONObject("SUFI_PARAMS").getJSONObject("SUFI_OFFLINE_MODE_CONFIG").getJSONObject("SUFI_OFFLINE_FILE_CONF").get("MEMORY_THRESHOLD")+"\"," + 
				"      }" +
				"      }," +
				"        \"SUFI_REDIR_RAT_PREFER\": \""+js.getJSONObject("SYS_PARAMS").getJSONObject("SUFI_PARAMS").get("SUFI_REDIR_RAT_PREFER")+"\"," + 
				"    }," + 
				"    \"SUB_INFO\": {" + 
				"      \"SUB_LIST_MODE\": \""+js.getJSONObject("SYS_PARAMS").getJSONObject("SUB_INFO").get("SUB_LIST_MODE")+"\"," + 
				"      \"SUB_LIST\": "+js.getJSONObject("SYS_PARAMS").getJSONObject("SUB_INFO").getJSONArray("SUB_LIST").toString() +"," +
/*				"      \"SUB_LIST\": [{" + 
				"        \"SUB_ID\": \""+js.getJSONObject("SYS_PARAMS").getJSONObject("SUB_INFO").getJSONObject("SUB_LIST").get("SUB_ID")+"\"," + 
				"        \"SUB_ID_TYPE\": \""+js.getJSONObject("SYS_PARAMS").getJSONObject("SUB_INFO").getJSONObject("SUB_LIST").get("SUB_ID_TYPE")+"\"" + 
				"      }]," + */
				"      \"HOLD_SUB\": "+js.getJSONObject("SYS_PARAMS").getJSONObject("SUB_INFO").getJSONArray("HOLD_SUB").toString() +
				"    }," + 
				"    \"CELL_INFO\": {" + 
				"      \"SUFI_ID\": \""+js.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").get("SUFI_ID")+"\"," + 
				"      \"PHY_CELL_ID\": \""+js.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").get("PHY_CELL_ID")+"\"," + 
				"      \"TAC_POOL_START\": \""+js.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").get("TAC_POOL_START")+"\"," + 
				"      \"TAC_POOL_END\": \""+js.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").get("TAC_POOL_END")+"\"," + 
				"      \"CELL_ID\": \""+js.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").get("CELL_ID")+"\"," + 
				"      \"Freq_Band_Indicator\": \""+js.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").get("Freq_Band_Indicator")+"\"," + 
				"      \"L1_ATT\": \""+js.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").get("L1_ATT")+"\"," +
				"      \"Reference_Signal_Power\": \""+js.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").get("Reference_Signal_Power")+"\"," +
				"      \"p-b\": \""+js.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").get("p-b")+"\"," + 
				"      \"DL_EARFCN\": \""+js.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").get("DL_EARFCN")+"\"," + 
				"      \"UL_EARFCN\": \""+js.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").get("UL_EARFCN")+"\"," + 
				"      \"DL_Channel_BandWidth\": \""+js.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").get("DL_Channel_BandWidth")+"\"," + 
				"      \"PLMN_ID\": {" + 
				"        \"MCC\": \""+js.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").getJSONObject("PLMN_ID").get("MCC")+"\"," + 
				"        \"MNC\": \""+js.getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO").getJSONObject("PLMN_ID").get("MNC")+"\"" + 
				"      }" + 
				"    }" + 
				"  }," + 
				"  \"CELL_PARAMS\": {" + 
				"    \"BITMASK\": \""+js.getJSONObject("CELL_PARAMS").get("BITMASK")+"\"," + 
				"    \"REDIRECTION_INFO\": "+js.getJSONObject("CELL_PARAMS").getJSONObject("REDIRECTION_INFO").toString()+"," + 
				"    \"SIB_INFO\": {" +
				"      \"NEIGH_CELL_LIST\": {" + 
				//"        \"INTRA_FREQ\": "+js.getJSONObject("CELL_PARAMS").getJSONObject("SIB_INFO").getJSONObject("NEIGH_CELL_LIST").getJSONArray("INTRA_FREQ").toString()+"," + 
				//"        \"INTER_FREQ\": "+js.getJSONObject("CELL_PARAMS").getJSONObject("SIB_INFO").getJSONObject("NEIGH_CELL_LIST").getJSONArray("INTER_FREQ").toString()+"," + 
				//"        \"INTER_RAT\": "+js.getJSONObject("CELL_PARAMS").getJSONObject("SIB_INFO").getJSONObject("NEIGH_CELL_LIST").getJSONArray("INTER_RAT").toString()+"" + 
				"        \"INTRA_FREQ\": ["+createStringForEsufiIntraFreq(js.getJSONObject("CELL_PARAMS").getJSONObject("SIB_INFO").getJSONObject("NEIGH_CELL_LIST").getJSONArray("INTRA_FREQ"))+"]," +
				"        \"INTER_FREQ\": ["+createStringForEsufiInterFreq(js.getJSONObject("CELL_PARAMS").getJSONObject("SIB_INFO").getJSONObject("NEIGH_CELL_LIST").getJSONArray("INTER_FREQ"))+"]," +
				"        \"INTER_RAT_2G\":  ["+createStringForEsufiInterRat2G(js.getJSONObject("CELL_PARAMS").getJSONObject("SIB_INFO").getJSONObject("NEIGH_CELL_LIST").getJSONArray("INTER_RAT_2G"))+"]," + 
				"        \"INTER_RAT_3G\":  ["+createStringForEsufiInterRat3G(js.getJSONObject("CELL_PARAMS").getJSONObject("SIB_INFO").getJSONObject("NEIGH_CELL_LIST").getJSONArray("INTER_RAT_3G"))+"]," + 				
				"      }" + 
				"    }" + 
				"  }" + 
				"}";

		}
		catch(Exception e) 
		{
		fileLogger.error("error with message :"+e.getMessage());	
		}

	return lines;
	}

	public String createStringForIntra_freq(JSONArray JsonObj) 
{
	    fileLogger.info("Inside Function : createStringForIntra_freq");
	StringBuilder ss = new StringBuilder();
	for(int i = 0; i < JsonObj.length(); i++)
	{
	    try 
	    {
	    	JSONObject objects = JsonObj.getJSONObject(i);
	    	
	    	String freq = "{" + 
	    			"            \"PSC\": \""+objects.getString("PSC")+"\"," + 
	    			"            \"Q_OFFSET_1S\": \""+objects.getString("Q_OFFSET_1S")+"\"," + 
	    			"            \"Q_OFFSET_2S\": \""+objects.getString("Q_OFFSET_2S")+"\"," + 
	    			"            \"Q_QUALMIN\": \""+objects.getString("Q_QUALMIN")+"\"," + 
	    			"            \"Q_RXLEVMIN\": \""+objects.getString("Q_RXLEVMIN")+"\"," + 
	    			"            \"CELL_ID\": \""+objects.getString("CELL_ID")+"\"," + 
	    			"            \"LAC\": \""+objects.getString("LAC")+"\"," + 
	    			"            \"INTRA_PLMN_ID\": {" + 
	    			"              \"MCC\": \""+objects.getJSONObject("INTRA_PLMN_ID").getString("MCC")+"\"," + 
	    			"              \"MNC\": \""+objects.getJSONObject("INTRA_PLMN_ID").getString("MNC")+"\"" + 
	    			"            }" + 
	    			"          }";
	    	if(i==0)
	    		ss.append(freq);
	    	else
	    		ss.append(","+freq);	
	    }  
	    catch(Exception e) 
	    {
	    	fileLogger.error("eroor while careateiing intra list");
	    }
	}
    fileLogger.info("Exit Function : createStringForIntra_freq");

	return ss.toString();
}


	public String createStringForINTER_FREQ(JSONArray JsonObj) 
{
	    fileLogger.info("Inside Function : createStringForINTER_FREQ");

	StringBuilder ss = new StringBuilder();
	for(int i = 0; i < JsonObj.length(); i++)
	{
	    try 
	    {
	    	JSONObject objects = JsonObj.getJSONObject(i);
	    	
	    	/*String freq = "{" + 
	    			"            \"PSC\": \""+objects.getString("PSC")+"\"," + 
	    			"            \"Q_OFFSET_1S\": \""+objects.getString("Q_OFFSET_1S")+"\"," + 
	    			"            \"Q_OFFSET_2S\": \""+objects.getString("Q_OFFSET_2S")+"\"," + 
	    			"            \"Q_QUALMIN\": \""+objects.getString("Q_QUALMIN")+"\"," + 
	    			"            \"Q_RXLEVMIN\": \""+objects.getString("Q_RXLEVMIN")+"\"," + 
	    			"            \"CELL_ID\": \""+objects.getString("CELL_ID")+"\"," + 
	    			"            \"LAC\": \""+objects.getString("LAC")+"\"," + 
	    			"            \"INTRA_PLMN_ID\": {" + 
	    			"              \"MCC\": \""+objects.getJSONObject("INTRA_PLMN_ID").getString("MCC")+"\"," + 
	    			"              \"MNC\": \""+objects.getJSONObject("INTRA_PLMN_ID").getString("MNC")+"\"" + 
	    			"            }" + 
	    			"          }";*/
	    	
	    	
	    	String freq = "{" + 
	    			"            \"PSC\": \""+objects.getString("PSC")+"\"," + 
	    			"            \"DL_UARFCN\": \""+objects.getString("DL_UARFCN")+"\"," + 
	    			"            \"PCPICH_TX_POWER\": \""+objects.getString("PCPICH_TX_POWER")+"\"," + 
	    			"            \"Q_OFFSET_1S\": \""+objects.getString("Q_OFFSET_1S")+"\"," + 
	    			"            \"Q_OFFSET_2S\": \""+objects.getString("Q_OFFSET_2S")+"\"," + 
	    			"            \"Q_QUALMIN\": \""+objects.getString("Q_QUALMIN")+"\"," + 
	    			"            \"Q_RXLEVMIN\": \""+objects.getString("Q_RXLEVMIN")+"\"," + 
	    			"            \"CELL_ID\": \""+objects.getString("CELL_ID")+"\"," + 
	    			"            \"LAC\": \""+objects.getString("LAC")+"\"," + 
	    			"            \"INTER_PLMN_ID\": {" + 
	    			"              \"MCC\": \""+objects.getJSONObject("INTER_PLMN_ID").getString("MCC")+"\"," + 
	    			"              \"MNC\": \""+objects.getJSONObject("INTER_PLMN_ID").getString("MNC")+"\"" + 
	    			"            }" + 
	    			"          }";
	    	
	    	if(i==0)
	    		ss.append(freq);
	    	else
	    		ss.append(","+freq);	
	    }  
	    catch(Exception e) 
	    {
	    	fileLogger.error("eroor while careateiing INTER_FREQ");
	    }
	    
	}
	  fileLogger.info("Exit Function : createStringForINTER_FREQ");
	return ss.toString();
}


	public String createStringForINTER_RAT(JSONArray JsonObj) 
{
		  fileLogger.info("Inside Function : createStringForINTER_RAT");
	StringBuilder ss = new StringBuilder();
	for(int i = 0; i < JsonObj.length(); i++)
	{
	    try 
	    {
	    	JSONObject objects = JsonObj.getJSONObject(i);
	    	
	    	/*String freq = "{" + 
	    			"            \"PSC\": \""+objects.getString("PSC")+"\"," + 
	    			"            \"Q_OFFSET_1S\": \""+objects.getString("Q_OFFSET_1S")+"\"," + 
	    			"            \"Q_OFFSET_2S\": \""+objects.getString("Q_OFFSET_2S")+"\"," + 
	    			"            \"Q_QUALMIN\": \""+objects.getString("Q_QUALMIN")+"\"," + 
	    			"            \"Q_RXLEVMIN\": \""+objects.getString("Q_RXLEVMIN")+"\"," + 
	    			"            \"CELL_ID\": \""+objects.getString("CELL_ID")+"\"," + 
	    			"            \"LAC\": \""+objects.getString("LAC")+"\"," + 
	    			"            \"INTRA_PLMN_ID\": {" + 
	    			"              \"MCC\": \""+objects.getJSONObject("INTRA_PLMN_ID").getString("MCC")+"\"," + 
	    			"              \"MNC\": \""+objects.getJSONObject("INTRA_PLMN_ID").getString("MNC")+"\"" + 
	    			"            }" + 
	    			"          }";*/
	    	
	    	String freq = "{" + 
	    			"            \"Q_RXLEVMIN\": \""+objects.getString("Q_RXLEVMIN")+"\"," + 
	    			"            \"LAC\": \""+objects.getString("LAC")+"\"," + 
	    			"            \"CELL_ID\": \""+objects.getString("CELL_ID")+"\"," + 
	    			"            \"Q_OFFSET1S_N\": \""+objects.getString("Q_OFFSET1S_N")+"\"," + 
	    			"            \"CELLINDIVIDUALOFFSET\": \""+objects.getString("CELLINDIVIDUALOFFSET")+"\"," + 
	    			"            \"NCC\": \""+objects.getString("NCC")+"\"," + 
	    			"            \"BCC\": \""+objects.getString("BCC")+"\"," + 
	    			"            \"FREQ_BAND\": \""+objects.getString("FREQ_BAND")+"\"," + 
	    			"            \"BCCH_ARFCN\": \""+objects.getString("BCCH_ARFCN")+"\"," + 
	    			"            \"RAT_PLMN_ID\": {" + 
	    			"              \"MCC\": \""+objects.getJSONObject("RAT_PLMN_ID").getString("MCC")+"\"," + 
	    			"              \"MNC\": \""+objects.getJSONObject("RAT_PLMN_ID").getString("MNC")+"\"" + 
	    			"            }" + 
	    			"          }";
	    	if(i==0)
	    		ss.append(freq);
	    	else
	    		ss.append(","+freq);	
	    }  
	    catch(Exception e) 
	    {
	    	fileLogger.error("eroor while careateiing INTER_RAT");
	    }
	}
	 fileLogger.info("Exit Function : createStringForINTER_RAT");
	return ss.toString();
}
	
	
	public String createStringForEsufiIntraFreq(JSONArray JsonObj) 
{
		 fileLogger.info("Inside Function : createStringForEsufiIntraFreq");
	StringBuilder ss = new StringBuilder();
	for(int i = 0; i < JsonObj.length(); i++)
	{
	    try 
	    {
	    	JSONObject objects = JsonObj.getJSONObject(i);
	    	String dummyCell="1";
	    	String freq = "{" + 
	    			"            \"PCI\": \""+objects.getString("PCI")+"\"," + 
	    			//"            \"CELL_ID\": \""+objects.getString("PCI")+"\"," + 
	    			"            \"CELL_ID\": \""+dummyCell+"\"," +
	    			"            \"TAC\": \""+objects.getString("TAC")+"\"," + 
	    			"            \"INTRA_PLMN_ID\": {" + 
	    			"              \"MCC\": \""+objects.getJSONObject("INTRA_PLMN_ID").getString("MCC")+"\"," + 
	    			"              \"MNC\": \""+objects.getJSONObject("INTRA_PLMN_ID").getString("MNC")+"\"" + 
	    			"            }" + 
	    			"          }";
	    	if(i==0)
	    		ss.append(freq);
	    	else
	    		ss.append(","+freq);	
	    }  
	    catch(Exception e) 
	    {
	    	fileLogger.error("error while creating 4g intra freq");
	    }
	}
	 fileLogger.info("Exit Function : createStringForEsufiIntraFreq");
	return ss.toString();
}


	public String createStringForEsufiInterFreq(JSONArray JsonObj) 
{
		 fileLogger.info("Inside Function : createStringForEsufiInterFreq");
	StringBuilder ss = new StringBuilder();
	for(int i = 0; i < JsonObj.length(); i++)
	{
	    try 
	    {
	    	JSONObject objects = JsonObj.getJSONObject(i);
	    	
	    	/*String freq = "{" + 
	    			"            \"PSC\": \""+objects.getString("PSC")+"\"," + 
	    			"            \"Q_OFFSET_1S\": \""+objects.getString("Q_OFFSET_1S")+"\"," + 
	    			"            \"Q_OFFSET_2S\": \""+objects.getString("Q_OFFSET_2S")+"\"," + 
	    			"            \"Q_QUALMIN\": \""+objects.getString("Q_QUALMIN")+"\"," + 
	    			"            \"Q_RXLEVMIN\": \""+objects.getString("Q_RXLEVMIN")+"\"," + 
	    			"            \"CELL_ID\": \""+objects.getString("CELL_ID")+"\"," + 
	    			"            \"LAC\": \""+objects.getString("LAC")+"\"," + 
	    			"            \"INTRA_PLMN_ID\": {" + 
	    			"              \"MCC\": \""+objects.getJSONObject("INTRA_PLMN_ID").getString("MCC")+"\"," + 
	    			"              \"MNC\": \""+objects.getJSONObject("INTRA_PLMN_ID").getString("MNC")+"\"" + 
	    			"            }" + 
	    			"          }";*/
	    	
	    	
	    	String freq = "{" + 
	    			"            \"PCI_List\": {" + 
	    			"              \"PCI1\": \""+objects.getJSONObject("PCI_List").getString("PCI1")+"\"" + 
	    			"            }," +  
	    			"            \"DL_Carrier_Frequency\": \""+objects.getString("DL_Carrier_Frequency")+"\"," + 
	    			"            \"Priority\": \""+objects.getString("Priority")+"\"," +
	    			"            \"CELL_ID\": \""+objects.getString("CELL_ID")+"\"," + 
	    			"            \"TAC\": \""+objects.getString("TAC")+"\"," + 
	    			"            \"INTER_PLMN_ID\": {" + 
	    			"              \"MCC\": \""+objects.getJSONObject("INTER_PLMN_ID").getString("MCC")+"\"," + 
	    			"              \"MNC\": \""+objects.getJSONObject("INTER_PLMN_ID").getString("MNC")+"\"" + 
	    			"            }" + 
	    			"          }";
	    	
	    	if(i==0)
	    		ss.append(freq);
	    	else
	    		ss.append(","+freq);	
	    }  
	    catch(Exception e) 
	    {
	    	fileLogger.error("error while creating 4g INTER FREQ");
	    }
	}
	 fileLogger.info("Exit Function : createStringForEsufiInterFreq");
	return ss.toString();
}


	public String createStringForEsufiInterRat2G(JSONArray JsonObj) 
{
		 fileLogger.info("Inside Function : createStringForEsufiInterRat2G");
	StringBuilder ss = new StringBuilder();
	for(int i = 0; i < JsonObj.length(); i++)
	{
	    try 
	    {
	    	JSONObject objects = JsonObj.getJSONObject(i);
	    	
	    	/*String freq = "{" + 
	    			"            \"PSC\": \""+objects.getString("PSC")+"\"," + 
	    			"            \"Q_OFFSET_1S\": \""+objects.getString("Q_OFFSET_1S")+"\"," + 
	    			"            \"Q_OFFSET_2S\": \""+objects.getString("Q_OFFSET_2S")+"\"," + 
	    			"            \"Q_QUALMIN\": \""+objects.getString("Q_QUALMIN")+"\"," + 
	    			"            \"Q_RXLEVMIN\": \""+objects.getString("Q_RXLEVMIN")+"\"," + 
	    			"            \"CELL_ID\": \""+objects.getString("CELL_ID")+"\"," + 
	    			"            \"LAC\": \""+objects.getString("LAC")+"\"," + 
	    			"            \"INTRA_PLMN_ID\": {" + 
	    			"              \"MCC\": \""+objects.getJSONObject("INTRA_PLMN_ID").getString("MCC")+"\"," + 
	    			"              \"MNC\": \""+objects.getJSONObject("INTRA_PLMN_ID").getString("MNC")+"\"" + 
	    			"            }" + 
	    			"          }";*/
	    	
	    	String freq = "{" + 
	    			"            \"Starting_ARFCN\": \""+objects.getString("Starting_ARFCN")+"\"," + 
	    			"            \"NCC_Permitted\": \""+objects.getString("NCC_Permitted")+"\"," + 
	    			"            \"Band_Indicator\": \""+objects.getString("Band_Indicator")+"\"," + 
	    			"            \"Priority\": \""+objects.getString("Priority")+"\"," + 
	    			"            \"Explicit_List_ARFCN\": {" + 
	    			"              \"ARFCN1\": \""+objects.getJSONObject("Explicit_List_ARFCN").getString("ARFCN1")+"\"" +
	    			"            }," +
	    			"            \"CELL_ID\": \""+objects.getString("CELL_ID")+"\"," +
	    			"            \"LAC\": \""+objects.getString("LAC")+"\"," +  
	    			"            \"INTER_RAT_PLMN_ID\": {" + 
	    			"              \"MCC\": \""+objects.getJSONObject("INTER_RAT_PLMN_ID").getString("MCC")+"\"," + 
	    			"              \"MNC\": \""+objects.getJSONObject("INTER_RAT_PLMN_ID").getString("MNC")+"\"" + 
	    			"            }" + 
	    			"          }";
	    	if(i==0)
	    		ss.append(freq);
	    	else
	    		ss.append(","+freq);	
	    }  
	    catch(Exception e) 
	    {
	    	fileLogger.error("error while creating INTER RAT 2G");
	    }
	}
	 fileLogger.info("Exit Function : createStringForEsufiInterRat2G");
	return ss.toString();
}
	
	public String createStringForEsufiInterRat3G(JSONArray JsonObj) 
{
		 fileLogger.info("Inside Function : createStringForEsufiInterRat3G");
	StringBuilder ss = new StringBuilder();
	for(int i = 0; i < JsonObj.length(); i++)
	{
	    try 
	    {
	    	JSONObject objects = JsonObj.getJSONObject(i);
	    	
	    	/*String freq = "{" + 
	    			"            \"PSC\": \""+objects.getString("PSC")+"\"," + 
	    			"            \"Q_OFFSET_1S\": \""+objects.getString("Q_OFFSET_1S")+"\"," + 
	    			"            \"Q_OFFSET_2S\": \""+objects.getString("Q_OFFSET_2S")+"\"," + 
	    			"            \"Q_QUALMIN\": \""+objects.getString("Q_QUALMIN")+"\"," + 
	    			"            \"Q_RXLEVMIN\": \""+objects.getString("Q_RXLEVMIN")+"\"," + 
	    			"            \"CELL_ID\": \""+objects.getString("CELL_ID")+"\"," + 
	    			"            \"LAC\": \""+objects.getString("LAC")+"\"," + 
	    			"            \"INTRA_PLMN_ID\": {" + 
	    			"              \"MCC\": \""+objects.getJSONObject("INTRA_PLMN_ID").getString("MCC")+"\"," + 
	    			"              \"MNC\": \""+objects.getJSONObject("INTRA_PLMN_ID").getString("MNC")+"\"" + 
	    			"            }" + 
	    			"          }";*/
	    	
	    	String freq = "{" + 
	    			"            \"DL_Carrier_Frequency\": \""+objects.getString("DL_Carrier_Frequency")+"\"," + 
	    			"            \"Priority\": \""+objects.getString("Priority")+"\"," +
	    			"            \"CELL_ID\": \""+objects.getString("CELL_ID")+"\"," +
	    			"            \"LAC\": \""+objects.getString("LAC")+"\"," +
	    			"            \"INTER_RAT_PLMN_ID\": {" + 
	    			"              \"MCC\": \""+objects.getJSONObject("INTER_RAT_PLMN_ID").getString("MCC")+"\"," + 
	    			"              \"MNC\": \""+objects.getJSONObject("INTER_RAT_PLMN_ID").getString("MNC")+"\"" + 
	    			"            }" + 
	    			"          }";
	    	if(i==0)
	    		ss.append(freq);
	    	else
	    		ss.append(","+freq);	
	    }  
	    catch(Exception e) 
	    {
	    	fileLogger.error("eroor while creating 4g INTER RAT 3g");
	    }
	}
	 fileLogger.info("Exit Function : createStringForEsufiInterRat3G");
	return ss.toString();
}


	public boolean auditLog(LinkedHashMap ls) 
	{
		 fileLogger.info("Inside Function : auditLog");
		
		Common co = new Common();
		
		 String query = "select max(id) id from oprrationdata";
			fileLogger.debug(query);
			JSONArray rs =  new Operations().getJson(query);
			String id =null;
					
			try {
				id = rs.getJSONObject(0).getString("id");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			
			if(id != null )
			{
				co.executeDLOperation("INSERT INTO public.audit_log(cmd_data, ip, type,operation_id) VALUES ('"+ls.get("cmd")+"', '"+ls.get("ip")+"', '"+ls.get("type")+"',"+id+")");
			}
			else 
			{
				co.executeDLOperation("INSERT INTO public.audit_log(cmd_data, ip, type) VALUES ('"+ls.get("cmd")+"', '"+ls.get("ip")+"', '"+ls.get("type")+"')");
			}
			 fileLogger.info("Exit Function : auditLog");
		return true;	
	}
	
	
	public LinkedHashMap<String,String> convertToMapFor2g(String ip,String cmd,String type)
	{
		LinkedHashMap<String,String> ls = new LinkedHashMap<String,String>();
		
		ls.put("cmd",cmd);
		ls.put("ip",ip);
		ls.put("type",type);
		
		return ls;
		
	}
	
	public JSONArray listFiles(String directoryName){
		 fileLogger.info("Inside Function : listFiles");
	    File directory = new File(directoryName);
	    //get all the files from a directory
	    File[] fList = directory.listFiles();
	     Arrays.sort(fList, new Comparator<File>(){
	         public int compare(File f1, File f2) {
	           return Long.valueOf(f2.lastModified()).compareTo(f1.lastModified());
	         } 
	     });
	    JSONArray fileListArray = new JSONArray();
	    for (File file : fList){
	    	if(file.isFile()){
	    	fileLogger.debug("file is in common:"+file.getName());
	    	fileListArray.put(file.getName());
	    	}
	    }
	    fileLogger.info("Exit Function : listFiles");
	    return fileListArray;
	}
	
    /**
     * Method for Get System IP Address
     * @return IP Address
     */
    public static String getSystemIPAddress(){
    	 fileLogger.info("Inside Function : getSystemIPAddress");
        try{
            String sysIP="";
            String OSName=  System.getProperty("os.name");
	    if(OSName.toLowerCase().indexOf("win")!=-1){
                sysIP   =InetAddress.getLocalHost().getHostAddress();
	    }
	    else{
	    	sysIP=getSystemIP4Linux("eth0");
	    	if(sysIP==null){
                    sysIP=getSystemIP4Linux("eth1");
		    if(sysIP==null){
		  	sysIP=getSystemIP4Linux("eth2");
                        if(sysIP==null){
                            sysIP=getSystemIP4Linux("usb0");
                        }
                    }
	   	}
	    }
	    return sysIP;
	}
	catch(Exception E){
			fileLogger.debug("System IP Exp : "+E.getMessage());
			   fileLogger.info("Inside Function : getSystemIPAddress");  
            return null;
	}
     
    }
    
    /**
     * method for get IP of linux System
     * @param name
     * @return 
     */
    private static String getSystemIP4Linux(String name){
        try{
            String ip="";
            NetworkInterface networkInterface = NetworkInterface.getByName(name);
            Enumeration<InetAddress> inetAddress = networkInterface.getInetAddresses();
            InetAddress currentAddress = inetAddress.nextElement();
            while(inetAddress.hasMoreElements()){
                currentAddress = inetAddress.nextElement();
                if(currentAddress instanceof Inet4Address && !currentAddress.isLoopbackAddress()){
                    ip = currentAddress.toString();
                    break;
                }
            }
            if(ip.startsWith("/")){
                ip=ip.substring(1);
            }
            return ip;
        } 
        catch (Exception E) {
            fileLogger.debug("System Linux IP Exp : "+E.getMessage());
            return null;
        }
    }
    
/*    public static double calcBearingBetweenTwoGpsLoc(double lat1, double long1, double lat2, double long2) {
    	double bearing = 0.0;
    	lat1 = Math.toRadians(lat1);
    	long1 = Math.toRadians(long1);
    	lat2 = Math.toRadians(lat2);
    	long2 = Math.toRadians(long2);

    	double bearingradians = Math.atan2(Math.asin(long2-long1)*Math.cos(lat2),Math.cos(lat1)*Math.sin(lat2) - Math.sin(lat1)*Math.cos(lat2)*Math.cos(long2-long1));
    	double bearingdegrees = Math.toDegrees(bearingradians);
    	bearingdegrees=Math.round(bearingdegrees*100.0)/100.0;
    	//bearingdegrees = bearingdegrees < 0? 360 + $bearingdegrees : $bearingdegrees; this is php code
    	if (bearingdegrees < 0)
    	{
    		bearingdegrees = 360 + bearingdegrees;
    	}

    	return bearingdegrees; 
    }*/
    
    public static int calcBearingBetweenTwoGpsLoc(double lat1, double lon1, double lat2, double lon2){
  	  double longitude1 = lon1;
  	  double longitude2 = lon2;
  	  double latitude1 = Math.toRadians(lat1);
  	  double latitude2 = Math.toRadians(lat2);
  	  double longDiff= Math.toRadians(longitude2-longitude1);
  	  double y= Math.sin(longDiff)*Math.cos(latitude2);
  	  double x=Math.cos(latitude1)*Math.sin(latitude2)-Math.sin(latitude1)*Math.cos(latitude2)*Math.cos(longDiff);
  	  double bearing = Math.toDegrees(Math.atan2(y, x));
  	  int bearingInInt=(int) Math.round(bearing);
  	  //return bearingInInt;
  	  return (bearingInInt+360)%360;
  	}
    
    public static int calcNewAngleOffset(int bearing){
    	int angleOffset=bearing-DBDataService.getAntennaToVehicleDiffAngle();
		if(angleOffset<0){
			angleOffset=360+angleOffset;
		}
		if(angleOffset>360){
			angleOffset=angleOffset%360;
		}
    	return angleOffset;
    	}
    
    public static double getSectorAngle(double offset,String sector){
    	double angle=0.00;
    	if(sector.equalsIgnoreCase("s1")){
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
    
    public static String calulateLatLongAtGivenAngleAndDistance(double latitude1,double longitude2,double bearing,double distance){
    	distance=distance/1000;
    	double earthRadius=6371.0;
    	double dist = distance/earthRadius;
    	double brng = Math.toRadians(bearing);
    	double lat1 = Math.toRadians(latitude1);
    	double lon1 = Math.toRadians(longitude2);

    	double lat2 = Math.asin( Math.sin(lat1)*Math.cos(dist) + Math.cos(lat1)*Math.sin(dist)*Math.cos(brng) );
    	double a = Math.atan2(Math.sin(brng)*Math.sin(dist)*Math.cos(lat1), Math.cos(dist)-Math.sin(lat1)*Math.sin(lat2));
    	double lon2 = lon1 + a;
    	double resLat=Math.toDegrees(lat2);
    	resLat=Math.round(resLat*1000000.0)/1000000.0;
    	double resLon=Math.toDegrees(lon2);
    	resLon=Math.round(resLon*1000000.0)/1000000.0;
    	

    	//lon2 = (lon2+ 3*Math.PI) % (2*Math.PI) - Math.PI;
        
          return resLat + "," + resLon;
    	}
    
    public HashMap<String,String> getPowerSetting1()
	{
		fileLogger.info("Inside Function : getPowerSetting1");  
		HashMap<String,String> map= new HashMap<String,String>(); 
		//fileLogger.info("Inside Function : updateStatusOfBts4g");
	//	Common co = new Common();
		Statement smt = null;
		Connection con = getDbConnection();
		
		try
		{
			smt = con.createStatement();
			String query=null;
			query = "select * from pow_compensation where ant_type =1";
			fileLogger.debug(query);
			ResultSet rs = smt.executeQuery(query);
			
			while(rs.next())
			{
				//system id -tech-band-antenna take the key values 
				//map.put(rs.getString("system_id") +"-"+ rs.getString("tech")+"-"+rs.getString("band")+"-"+rs.getString("ant_type"),rs.getString("power_setting"));
			//	map.put("900",prop.getProperty("900"));
			//	map.put("1800",prop.getProperty("1800"));
			//	map.put("2100",prop.getProperty("2100"));
			//	map.put("2300",prop.getProperty("2300"));
				
			}
			
		
		}
		catch(Exception E)
		{
		
			
		//	statusLogger.debug("*****************************************");
			fileLogger.debug("Class = TwogOperations , Method : updateStatusOfAllBts");
			fileLogger.error("Erorr During updating the status");
			fileLogger.debug(E.getMessage());
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

		 fileLogger.info("Exit Function : getPowerSetting1");  
		return map;
	}
	public HashMap<String,String> getPowerSetting()
	{
		   fileLogger.info("Inside Function : getPowerSetting");  
		HashMap<String,String> map= new HashMap<String,String>(); 
		String absolutePath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		absolutePath = absolutePath.substring(0,absolutePath.lastIndexOf("WEB-INF"));
		Properties prop = new Properties();
		try
		{
			prop.load(new FileInputStream(absolutePath	+ "/resources/config/power_setting.properties"));
		}
		catch(Exception E)
		{
			fileLogger.debug("power_setting.properties not found" + E.getMessage());
			
		}
		map.put("850",prop.getProperty("850"));
		map.put("900",prop.getProperty("900"));
		map.put("1800",prop.getProperty("1800"));
		map.put("2100",prop.getProperty("2100"));
		map.put("2300",prop.getProperty("2300"));
		   fileLogger.info("Exit Function : getPowerSetting");  
		return map;
	}
	public String getColumnValues(String fileName,String columnKey)
	{
		   fileLogger.info("Inside Function : getColumnValues");  
		String absolutePath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		absolutePath = absolutePath.substring(0,absolutePath.lastIndexOf("WEB-INF"));
		Properties prop = new Properties();
		try
		{
			prop.load(new FileInputStream(absolutePath	+ "/resources/config/"+fileName+".properties"));
		}
		catch(Exception E)
		{
			fileLogger.debug("report_columns.properties not found" + E.getMessage());
			
		}		
		 fileLogger.info("Exit Function : getColumnValues");  
		return prop.getProperty(columnKey);
	}
		
		public boolean sendConfigurationToNodeLTE(int adminState, LinkedHashMap<String, String> data) {
			 fileLogger.info("Inside Function : sendConfigurationToNodeLTE");  
		fileLogger.debug("config 1");
		try {
			String config = data.get("config");
			String cellId = "";
			int deviceTypeId = Integer.parseInt(data.get("devicetypeid"));
			fileLogger.debug("config 2");
			/*try {
				cellId = new JSONObject(config).getJSONObject("SYS_PARAMS").getJSONObject("CELL_INFO")
						.getString("CELL_ID");
			} catch (Exception e) {
				fileLogger.debug("Error while getting celling method sendConfigurationToNode class Common");
			}*/

			/*
			 *Unlock in case if any operation is active

			 * */
			JSONArray ja = new Operations().getJson("select count(*) as count from running_mode where mode_status='Active' and mode_type='track'");
			
			int count = ja.getJSONObject(0).getInt("count");
			
			if (adminState == 2 && count == 0) {
				data.put("CMD_TYPE", "SET_CELL_LOCK");
				data.put("cmdType", "SET_CELL_LOCK");
				data.put("data", "{\"CMD_CODE\":\"SET_CELL_LOCK\",\"CELL_ID\":\"" + 1 + "\",\"LAC\":\"0\"}");
				new FourgOperations().setCellLock(data);

			}
				
			fileLogger.debug("config 3");
			/*
			 * if (adminState == 0 || adminState == 2) {
			 * 
			 * data.put("CMD_TYPE", "SET_CELL_LOCK"); data.put("cmdType", "SET_CELL_LOCK");
			 * data.put("data", "{\"CMD_CODE\":\"SET_CELL_LOCK\",\"CELL_ID\":\"" + 1 +
			 * "\",\"LAC\":\"0\"}"); new FourgOperations().setCellLock(data);
			 * 
			 * }
			 * 
			 * fileLogger.debug("config 4"); data.put("CMD_TYPE", "SET_SUFI_CONFIG");
			 * data.put("cmdType", "SET_SUFI_CONFIG"); data.put("data", config); new
			 * FourgOperations().setSufiConfig(data);
			 * 
			 * if (adminState == 2 && count >0) { data.put("CMD_TYPE", "SET_CELL_UNLOCK");
			 * data.put("cmdType", "SET_CELL_UNLOCK"); data.put("data",
			 * "{\"CMD_CODE\":\"SET_CELL_UNLOCK\",\"CELL_ID\":\"" + 1 +
			 * "\",\"LAC\":\"0\"}"); new FourgOperations().setCellLock(data); }
			 */
			fileLogger.debug("config 5");
			if (deviceTypeId == 2 || deviceTypeId == 3) {
				data.put("CMD_TYPE", "SET_MEAS_TRIGGER");
				data.put("cmdType", "SET_MEAS_TRIGGER");
				data.put("data", "{\"CMD_CODE\":\"SET_MEAS_TRIGGER\",\"TRIGGER\":\"1\"}");
				new FourgOperations().triggerMes(data);
			}
			fileLogger.debug("config 6");
		} catch (Exception e) {
			fileLogger.debug(e.getMessage());
			return false;
		}
		 fileLogger.info("Exit Function : sendConfigurationToNodeLTE");  
		return true;
	}
		
		
		
		
		
		
		
		
//		public boolean sendConfigurationToJammer(int adminState, LinkedHashMap<String, String> data) {
//			 fileLogger.info("Inside Function : sendConfigurationToNodeLTE");  
//		fileLogger.debug("config 1");
//		try {
//			String config = data.get("config");
//			String cellId = "";
//			int deviceTypeId = Integer.parseInt(data.get("devicetypeid"));
//			fileLogger.debug("config 2");
//		
//			JSONArray ja = new Operations().getJson("select count(*) as count from running_mode where mode_status='Active' and mode_type='track'");
//			
//			int count = ja.getJSONObject(0).getInt("count");
//			
//			if (adminState == 2 && count == 0) {
//				data.put("CMD_TYPE", "SET_CELL_LOCK");
//				data.put("cmdType", "SET_CELL_LOCK");
//				data.put("data", "{\"CMD_CODE\":\"SET_CELL_LOCK\",\"CELL_ID\":\"" + 1 + "\",\"LAC\":\"0\"}");
//				new FourgOperations().setCellLock(data);
//
//			}
//				
//			fileLogger.debug("config 3");
//			
//			fileLogger.debug("config 5");
//			if (deviceTypeId == 2 || deviceTypeId == 3) {
//				data.put("CMD_TYPE", "SET_MEAS_TRIGGER");
//				data.put("cmdType", "SET_MEAS_TRIGGER");
//				data.put("data", "{\"CMD_CODE\":\"SET_MEAS_TRIGGER\",\"TRIGGER\":\"1\"}");
//				//new FourgOperations().triggerMes(data);
//			}
//			fileLogger.debug("config 6");
//		} catch (Exception e) {
//			fileLogger.debug(e.getMessage());
//			return false;
//		}
//		 fileLogger.info("Exit Function : sendConfigurationToNodeLTE");  
//		return true;
//	}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
}

