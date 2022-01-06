package in.vnl.scheduler;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.vnl.msgapp.Common;
import in.vnl.msgapp.Operations;
import in.vnl.scheduler.timertasks.UpdateStatus;
import static java.util.concurrent.TimeUnit.*;

import in.vnl.api.common.CommonService;
import in.vnl.api.netscan.*;


public class NetscanSchedulerListener implements ServletContextListener 
{
	static Logger fileLogger = Logger.getLogger("file");
	public static ScheduledExecutorService scheduler =   null;
	public static boolean isScannerInterrupted = false;
	@Override
	public void contextDestroyed(ServletContextEvent arg0) 
	{
	//	fileLogger.debug("******************************************************");
		fileLogger.debug("NetscanSchedulerListener Context Destroyed...");
	//	fileLogger.debug("******************************************************");
		if(scheduler!=null){
			scheduler.shutdownNow();
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) 
	{
		//fileLogger.debug("******************************************************");
		fileLogger.debug("NetscanSchedulerListener Context Initialized...");
	//	fileLogger.debug("******************************************************");
		stopNetscanScheduler();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		startNetscanScheduler();
	}
	
	public void stopNetscanScheduler(){
		fileLogger.info("Inside Function :stopNetscanScheduler" );
		try {
			fileLogger.debug("@scan about to stop netscan");
			CommonService commonService = new CommonService();
			Operations operations = new Operations();
			NetscanOperations netscanOperations = new NetscanOperations();
			NetscanSchedulerListener.isScannerInterrupted=true;
			JSONArray netscanArray = operations.getJson("select * from view_btsinfo where code=3");
			if(netscanArray.length()>0){
				JSONObject netscanData = netscanArray.getJSONObject(0);
				LinkedHashMap<String,String> param = new LinkedHashMap<String,String>();
				param = commonService.prepareParamForStopNetscan(netscanData,"GSM");
				netscanOperations.sendToServer(param);
//				param = commonService.prepareParamForStopNetscan(netscanData,"UMTS");
//				netscanOperations.sendToServer(param);
//				param = commonService.prepareParamForStopNetscan(netscanData,"LTE");
//				netscanOperations.sendToServer(param);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		//fileLogger.debug("@scan stopped netscan");
		fileLogger.info("Exit Function :stopNetscanScheduler" );
	}
	
	public void startNetscanScheduler()
	{	
		fileLogger.info("Inside Function :startNetscanScheduler" );
		try 
		{
			NetscanTask netscanTask=new NetscanTask();
			HashMap<String,String> ll = new Common().getDbCredential();
			Operations operations = new Operations();
		    NetscanSchedulerListener.isScannerInterrupted=false;
			int scannerPeriodicity = operations.getJson("select value from system_properties where key='schedulertime'").getJSONObject(0).getInt("value");
			fileLogger.debug("scanner periodicity is :"+scannerPeriodicity+"and scheduler is :"+scheduler);
			//ScheduledFuture future=scheduler.scheduleAtFixedRate(netscanTask, 2, scannerPeriodicity*60, TimeUnit.SECONDS);
			scheduler=Executors.newScheduledThreadPool(1);
			ScheduledFuture future=scheduler.scheduleWithFixedDelay(netscanTask, 2, scannerPeriodicity*60, TimeUnit.SECONDS);	
			//NetscanSingletonExecutor.getInstance().setFuture(future);	
		}
		catch(Exception E) 
		{
			//fileLogger.debug("*****************************************");
			//fileLogger.debug("Class = Scheduler , Method : startNetscanScheduler");
			fileLogger.error(E.getMessage());
			E.printStackTrace();
			//fileLogger.debug("*****************************************");
		}
		fileLogger.info("Exit Function :startNetscanScheduler" );
		
		
	}
	
}
