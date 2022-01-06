package in.vnl.msgapp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import in.vnl.msgapp.Common;
public class GeoTask extends TimerTask{
	static Logger fileLogger = Logger.getLogger("file");
	List<NameValuePair> params = null;
	String nibIp=null;

public GeoTask(String cmdType,String fileName,String ftn,String hlr,String msc,String idType,String reqType,String typeVal,String vlr,String nibIp){
params = new ArrayList<NameValuePair>();
params.add(new BasicNameValuePair("CMD_TYPE",cmdType ));
params.add(new BasicNameValuePair("TAGS00", reqType));
params.add(new BasicNameValuePair("TAGS01", idType));
params.add(new BasicNameValuePair("TAGS02", typeVal));
params.add(new BasicNameValuePair("TAGS03", hlr));			
params.add(new BasicNameValuePair("TAGS04", vlr));			
params.add(new BasicNameValuePair("TAGS05", ftn));
params.add(new BasicNameValuePair("TAGS06", msc));
this.nibIp=nibIp;
}

public GeoTask(List<NameValuePair> params){
this.params=params;
}

@Override
public void run(){
	fileLogger.debug("in GeoTask timertask run method");
	Common co = new Common();	
	String myURL = "http://"+nibIp+"/cgi-bin/processData_CLI.sh";
	try
	{			
		co.callPostDataUrl(myURL,params);				
		//mdr = co.callURL("http://"+getNibIp(request)+"/temp/"+fileName);
	}
	catch(Exception E)
	{
		fileLogger.error("Exeption while request"+E.getMessage());
	}
}

public List<NameValuePair> getParams(){
	return params;
}
}
