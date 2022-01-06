package in.vnl.api.common;

import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import in.vnl.EventProcess.DBDataService;
import in.vnl.api.common.ApiCommon;
import in.vnl.api.common.livescreens.DeviceStatusServer;
import in.vnl.api.common.livescreens.ScanTrackModeServer;
import in.vnl.api.common.livescreens.SoftVerServer;
import in.vnl.api.twog.TwogOperations;
import in.vnl.msgapp.Common;
import in.vnl.scheduler.timertasks.UpdateStatus;
import static java.util.concurrent.TimeUnit.*;

import java.text.SimpleDateFormat;


public class SoftVerListener implements ServletContextListener 
{
	static Logger fileLogger = Logger.getLogger("file");
	static String applicationStartTime="";
	final ScheduledExecutorService scheduler =   Executors.newScheduledThreadPool(1);
	@Override
	public void contextDestroyed(ServletContextEvent arg0) 
	{
		fileLogger.info("Inside Function : contextDestroyed");
	//	fileLogger.debug("******************************************************");
		fileLogger.debug("context destroyed for  SoftVerListener...");
	//fileLogger.debug("******************************************************");
		fileLogger.info("Exit Function : contextDestroyed");
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) 
	{
		fileLogger.info("Inside Function : contextInitialized");
		//fileLogger.debug("******************************************************");
		fileLogger.debug("context initialised for  SoftVerListener...");
		//fileLogger.debug("******************************************************");
		findApplicationStartTime();
		fetchSoftVersion();	
		fileLogger.info("Exit Function : contextInitialized");
	}
	
	public void findApplicationStartTime(){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("IST"));
		applicationStartTime=formatter.format(new Date());
	}
	
	private void fetchSoftVersion()
	{
		fileLogger.info("Inside Function : fetchSoftVersion");
		try 
		{
			ExecutorService executorTimerService = Executors.newSingleThreadExecutor();
			
			Future future = executorTimerService.submit(new Runnable() {
			    public void run() {
					try {
						new TwogOperations().updateSoftVerOfDevices("all");
						new SoftVerServer().sendText("ok");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						fileLogger.error("exception in run method of fetchSoftVersion message:"+e.getMessage());
						//e.printStackTrace();
					}
			    }});
		}
		catch(Exception E) 
		{
			//fileLogger.debug("*****************************************");
			fileLogger.debug("Class = Scheduler , Method : setupStatusScheduler");
			fileLogger.debug(E.getMessage());
			E.printStackTrace();
			//fileLogger.debug("*****************************************");
		}
		
		fileLogger.info("Exit Function : fetchSoftVersion");
	}
	
}
