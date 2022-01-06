package in.vnl.scheduler;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TimeZone;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import in.vnl.api.common.ApiCommon;
import in.vnl.api.common.AuditHandler;
import in.vnl.api.common.CmsTrigger;
import in.vnl.api.common.livescreens.UsedSpaceServer;
import in.vnl.msgapp.Common;
import in.vnl.report.ReportServer;
import in.vnl.scheduler.timertasks.UpdateStatus;
import static java.util.concurrent.TimeUnit.*;


public class AutoBackupSchedulerListener implements ServletContextListener 
{
	static Logger fileLogger = Logger.getLogger("file");
	
	final ScheduledExecutorService scheduler =   Executors.newScheduledThreadPool(1);
	@Override
	public void contextDestroyed(ServletContextEvent arg0) 
	{
		//fileLogger.debug("******************************************************");
		fileLogger.debug("Stoping Locator...");
         //fileLogger.debug("******************************************************");
		scheduler.shutdownNow();
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) 
	{
		//fileLogger.debug("******************************************************");
		fileLogger.debug("Starting Locator...");
		//fileLogger.debug("******************************************************");
		setupBackupScheduler();
		
	}
	
	private void setupBackupScheduler()
	{	
		try 
		{
		long timeDiffInMillis=0L;
        Calendar now = Calendar.getInstance();
        //Date d=now.getTime();
        TimeZone timeZone = now.getTimeZone();
        String timeZoneStr = timeZone.getDisplayName();
        
        //Calendar calendar = Calendar.getInstance();
        //display current TimeZone
        //String startTime="";
        //String endTime="";
        fileLogger.debug("Current TimeZone at startup is :" + timeZoneStr);
        //Date endDate=null;
        //Date startDate=null;
        
        if(timeZoneStr.equalsIgnoreCase("India Standard Time")){
        	//Date currDate=calendar.getTime();
        	//calendar.add(Calendar.DAY_OF_MONTH, -1);
            Calendar c = Calendar.getInstance();
            c.getTime();
            //get current TimeZone using getTimeZone method of Calendar class
            c.add(Calendar.DAY_OF_MONTH, 0);
            c.set(Calendar.HOUR_OF_DAY, 2);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            c.getTime();
            Long currTimeInMillis=System.currentTimeMillis();
            timeDiffInMillis = (c.getTimeInMillis()-currTimeInMillis);
            if(timeDiffInMillis<0){
                Calendar tempCal=Calendar.getInstance();
                tempCal.add(Calendar.DAY_OF_MONTH, 1);
                tempCal.set(Calendar.HOUR_OF_DAY, 2);
                tempCal.set(Calendar.MINUTE, 0);
                tempCal.set(Calendar.SECOND, 0);
                tempCal.set(Calendar.MILLISECOND, 0);
                tempCal.getTime();
            	timeDiffInMillis=tempCal.getTimeInMillis()-currTimeInMillis;
            }
        	
        	//endDate=calendar.getTime();
        	//calendar.add(Calendar.DAY_OF_MONTH, -1);
        	//startDate=calendar.getTime();
        	//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        	//String dateStr=sdf.format(date);
        	//startTime=sdf.format(startDate)+" 00:00:00";
        	//endTime=sdf.format(startDate) + " 23:59:59";
        }else{
            Calendar c = Calendar.getInstance();
            c.getTime();
            //get current TimeZone using getTimeZone method of Calendar class
            c.add(Calendar.DAY_OF_MONTH, 0);
            c.set(Calendar.HOUR_OF_DAY, -3);
            c.set(Calendar.MINUTE, -30);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            c.getTime();
            Long currTimeInMillis=System.currentTimeMillis();
            timeDiffInMillis = (c.getTimeInMillis()-currTimeInMillis);

            if(timeDiffInMillis<0){
                Calendar tempCal=Calendar.getInstance();
                tempCal.getTime();
                tempCal.add(Calendar.DAY_OF_MONTH, 1);
                tempCal.set(Calendar.HOUR_OF_DAY, -3);
                tempCal.set(Calendar.MINUTE, -30);
                tempCal.set(Calendar.SECOND, 0);
                tempCal.set(Calendar.MILLISECOND, 0);
                tempCal.getTime();
            	timeDiffInMillis=tempCal.getTimeInMillis()-currTimeInMillis;
            }
        	//Date currDate=calendar.getTime();
        	//calendar.add(Calendar.DAY_OF_MONTH, -1);
        	//endDate=calendar.getTime();
        	//calendar.add(Calendar.DAY_OF_MONTH, -1);
        	//startDate=calendar.getTime();
        	//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        	//String dateStr=sdf.format(date);
        	//startTime=sdf.format(startDate)+" 18:30:00";
        	//endTime=sdf.format(endDate) + " 18:29:59";
        }
    	
        
/*        if(timeDiffInMillis<=60000){
        	new ReportServer().createReports(null, startTime, endTime, "\t","Backup_Reports");
        }*/
        long delayForBackup=timeDiffInMillis;
		long backupStartPeriodicity = 24*60*60*1000;

			
			scheduler.scheduleAtFixedRate(new Runnable() {
			    public void run() {
			    	try {
						Calendar calendar = Calendar.getInstance();
						TimeZone timeZone = calendar.getTimeZone();
						String timeZoneStr = timeZone.getDisplayName();
						fileLogger.debug("Current TimeZone in scheduler is :" + timeZoneStr);
						Date endDate=null;
						Date startDate=null;
						String startTime="";
						String endTime="";
						if(timeZoneStr.equalsIgnoreCase("India Standard Time")){
							//Date currDate=calendar.getTime();
							calendar.add(Calendar.DAY_OF_MONTH, -1);
							endDate=calendar.getTime();
							calendar.add(Calendar.DAY_OF_MONTH, -1);
							startDate=calendar.getTime();
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							//String dateStr=sdf.format(date);
							startTime=sdf.format(startDate)+" 18:30:00";
							endTime=sdf.format(endDate) + " 18:29:59";
						}else{
							//Date currDate=calendar.getTime();
							//calendar.add(Calendar.DAY_OF_MONTH, -1);
							endDate=calendar.getTime();
							calendar.add(Calendar.DAY_OF_MONTH, -1);
							startDate=calendar.getTime();
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							//String dateStr=sdf.format(date);
							startTime=sdf.format(startDate)+" 18:30:00";
							endTime=sdf.format(endDate) + " 18:29:59";
						}
						

						new ReportServer().createReports(null, startTime, endTime, "\t","Backup_Reports","passive");
						//full db backup audit log
						LinkedHashMap < String, String > log = new LinkedHashMap < String, String > ();
						log.put("action", "Daily Automatic Backup");
						new AuditHandler().auditBackup(log);
						//full db backup audit log
					} catch (Exception e) {
						// TODO Auto-generated catch block
						fileLogger.error("exception in setupBackupScheduler inside run method message:"+e.getMessage());
						//e.printStackTrace();
					}
		    }}, delayForBackup, backupStartPeriodicity, TimeUnit.MILLISECONDS);
			
		}
		catch(Exception E) 
		{
		//	fileLogger.debug("*****************************************");
			//fileLogger.debug("Class = AutoBackupSchedulerListener , Method : setupBackupScheduler");
			fileLogger.error(E.getMessage());
			//E.printStackTrace();
			//fileLogger.debug("*****************************************");
		}
		
		
	}
	
}
