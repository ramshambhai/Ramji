package in.vnl.scheduler;

import java.util.Calendar;
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
import in.vnl.scheduler.timertasks.DataBackupImpl;
import in.vnl.scheduler.timertasks.UpdateStatus;
import static java.util.concurrent.TimeUnit.*;


public class AutoDataBackup implements ServletContextListener 
{
	static Logger fileLogger = Logger.getLogger("file");
	final ScheduledExecutorService scheduler =   Executors.newScheduledThreadPool(1);
	@Override
	public void contextDestroyed(ServletContextEvent arg0) 
	{
		fileLogger.debug("Context Destroyed for AutoDataBackup...");
		scheduler.shutdownNow();
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) 
	{
		fileLogger.debug("Context Initialized for AutoDataBackup...");
		setupDataBackup();
		
	}
	
	private void setupDataBackup()
	{	
/*		DataBackupImpl backupObj=new DataBackupImpl();
		HashMap<String,String> ll = new Common().getDbCredential();
		String statusUpdatePeriodicity =  ll.get("statusUpdatePeriodicity");
		//Timer timer = new Timer();
		
		//timer.schedule(usobj, new Date(), Integer.parseInt(statusUpdatePeriodicity));//line 1
		try 
		{
			Calendar calendar = Calendar.getInstance(); // creates calendar
			int timeDiffFromUtcInMillis = calendar.getTimeZone().getRawOffset();
			if(timeDiffFromUtcInMillis==0){
				calendar.add(Calendar.HOUR, 5);
				calendar.add(Calendar.MINUTE, 30);
			}
		
			int hourOfDay=calendar.get(Calendar.HOUR_OF_DAY);
		    cal.getTime(); // returns new date object, one hour in the future
			
			scheduler.scheduleAtFixedRate(backupObj, 10, 24, TimeUnit.HOURS);
			
		}
		catch(Exception E) 
		{
			fileLogger.debug("*****************************************");
			fileLogger.debug("Class = Scheduler , Method : setupStatusScheduler");
			fileLogger.debug(E.getMessage());
			E.printStackTrace();
			fileLogger.debug("*****************************************");
		}*/
		
		
	}
	
}

