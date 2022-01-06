package in.vnl.scheduler;

import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import in.vnl.api.common.ApiCommon;
import in.vnl.msgapp.Common;
import in.vnl.scheduler.timertasks.UpdateStatus;
import static java.util.concurrent.TimeUnit.*;


public class Scheduler implements ServletContextListener 
{
	static Logger statusLogger = Logger.getLogger("status");
	final ScheduledExecutorService scheduler =   Executors.newScheduledThreadPool(1);
	@Override
	public void contextDestroyed(ServletContextEvent arg0) 
	{
	//	statusLogger.debug("******************************************************");
		statusLogger.debug("Stoping Locator...");
		//statusLogger.debug("******************************************************");
		scheduler.shutdownNow();
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) 
	{
		//statusLogger.debug("******************************************************");
		statusLogger.debug("Starting Status Scheduler");
		//statusLogger.debug("******************************************************");
		setupStatusScheduler();
		
	}
	
	private void setupStatusScheduler()
	{	
		//Timer timer = new Timer();
		
		//timer.schedule(usobj, new Date(), Integer.parseInt(statusUpdatePeriodicity));//line 1
		try 
		{
			new ApiCommon().SetNodeStatusToNotReachable();
			
			UpdateStatus usobj=new UpdateStatus();
			HashMap<String,String> ll = new Common().getDbCredential();
			String statusUpdatePeriodicity =  ll.get("statusUpdatePeriodicity");
			scheduler.scheduleWithFixedDelay(usobj, 10, Integer.parseInt(statusUpdatePeriodicity), TimeUnit.MILLISECONDS);
			
		}
		catch(Exception E) 
		{
			//statusLogger.debug("*****************************************");
			//statusLogger.debug("Class = Scheduler , Method : setupStatusScheduler");
			statusLogger.error(E.getMessage());
			//E.printStackTrace();
			//statusLogger.debug("*****************************************");
		}
		
		
	}
	
}
