package in.vnl.api.common;

import in.vnl.EventProcess.DBDataService;
import in.vnl.api.common.livescreens.UsedSpaceServer;
import in.vnl.msgapp.Common;
import in.vnl.msgapp.Operations;
import in.vnl.scheduler.timertasks.UpdateStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;


public class AutoOprStopListener implements ServletContextListener 
{
	static Logger fileLogger = Logger.getLogger("file");
	final ScheduledExecutorService scheduler =   Executors.newScheduledThreadPool(1);
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) 
	{
		fileLogger.info("Inside Function : contextDestroyed");
		//fileLogger.debug("******************************************************");
		fileLogger.debug("Stopping AutoOprStopListener...");
	//	fileLogger.debug("******************************************************");
		try {
			if(scheduler!=null){
				scheduler.shutdownNow();
			}
			DBDataService dbDataService = DBDataService.getInstance();
			String currentEventName = dbDataService.currentEventName;
			if(currentEventName!=null){
			if(currentEventName.equalsIgnoreCase("scheduler")){
				ScheduledExecutorService scheduledExecutorService=dbDataService.getScheduledExecutorService();
				if(scheduledExecutorService!=null){
					scheduledExecutorService.shutdownNow();
				}
			}else if(currentEventName.equalsIgnoreCase(Constants.automaticEvent)){
				ExecutorService executorTimerService=dbDataService.getExecutorTimerService();
				if(executorTimerService!=null){
				executorTimerService.shutdownNow();
				}
			}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			fileLogger.error("exception in contextDestroyed of AutoOprStopListener message:"+e.getMessage());
			//e.printStackTrace();
		}
		fileLogger.info("Exit Function : contextDestroyed");
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) 
	{
		fileLogger.info("Inside Function : contextInitialized");
		try {
			//fileLogger.debug("******************************************************");
			fileLogger.debug("Starting AutoOprStopListener...");
			CommonService commonService = new CommonService();
			commonService.stopAllOpr();
			Common common = new Common();
			common.executeDLOperation("update running_mode set mode_status='Idle',applied_antenna=null");
			common.executeDLOperation("update config_status set status=0");
			DBDataService.getInstance().setCurrentManualEventName(null);
			fileLogger.debug("Running Used Space Scheduler");
			setupUsedSpaceScheduler();
			HashMap<String,ArrayList<HashMap<String,String>>> devicesMapOverTech = new Operations().getAllBtsInfoByTech();
			new ApiCommon().lockUnlockAllDevices(devicesMapOverTech,1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			fileLogger.debug("in contextInitialized finally");
		}
		fileLogger.info("Exit Function : contextInitialized");
	}
	private void setupUsedSpaceScheduler()
	{	
		fileLogger.info("Inside Function : setupUsedSpaceScheduler");
		//Timer timer = new Timer();
		
		//timer.schedule(usobj, new Date(), Integer.parseInt(statusUpdatePeriodicity));//line 1
		try 
		{
			scheduler.scheduleWithFixedDelay(new Runnable() {
			    public void run() {
			    		try {
							String usedSpace=new CmsTrigger().getUsedSpacePercentage();
							new UsedSpaceServer().sendText(usedSpace);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							fileLogger.error("exception in run method of setupUsedSpaceScheduler message:"+e.getMessage());
							//e.printStackTrace();
						}
			    }}, 10,60000, TimeUnit.MILLISECONDS);
			
		}
		catch(Exception E) 
		{
			//fileLogger.debug("*****************************************");
		//	fileLogger.debug("Class = Scheduler , Method : setupStatusScheduler");
			fileLogger.error(E.getMessage());
			E.printStackTrace();
		//fileLogger.debug("*****************************************");
		}
		fileLogger.info("Exit Function : setupUsedSpaceScheduler");
		
	}
	
}
