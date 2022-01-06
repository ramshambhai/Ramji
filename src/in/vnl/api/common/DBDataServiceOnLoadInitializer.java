package in.vnl.api.common;
import in.vnl.EventProcess.DBDataService;
import in.vnl.api.netscan.NetscanOperations;
import in.vnl.msgapp.PTZUdpListener;

import java.net.SocketException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;


public class  DBDataServiceOnLoadInitializer implements ServletContextListener 
{
	static Logger fileLogger = Logger.getLogger("file");
	@Override
	public void contextDestroyed(ServletContextEvent arg0) 
	{		
		fileLogger.info("Inside Function : contextDestroyed");
	
		
		fileLogger.debug("in contextDestroyed DBDataServiceOnLoadInitializer...");
		fileLogger.info("Exit Function : contextDestroyed");
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) 
	{
 		fileLogger.info("Inside Function : contextInitialized");
		fileLogger.debug("in  contextInitialized DBDataServiceOnLoadInitializer...");
		final DBDataService dbDataService=DBDataService.getInstance();
		try {
			dbDataService.updateAntennaMode();
			dbDataService.updatePriorityMap();
			dbDataService.updateConfigParamMap();
			dbDataService.updateSystemMode();
			dbDataService.updateSystemType();
			dbDataService.updateGpsNode();
			dbDataService.updateAngleOffset();
			dbDataService.updateSystem_id();
			new NetscanOperations().syncSystemTimeWithScanner();
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			fileLogger.error("exception in updating  values in dbDataService class message:"+e2.getMessage());
			//e2.printStackTrace();
		}
		
		try {
			new Thread(new Runnable() {

			    @Override
			    public void run() {
			    	fileLogger.debug("in run method thread DBDataServiceOnLoadInitializer");
			    	dbDataService.executeQueue();
			    }
			    
			}).start();
		} catch (Exception e1) {
			fileLogger.error("exception in executing queue message:"+e1.getMessage());
			// TODO Auto-generated catch block
			
			//e1.printStackTrace();
		}
		try {
			Thread ptzListenerThread = new Thread(new PTZUdpListener());
			ptzListenerThread.start();
		} catch (SocketException e) {
			fileLogger.error("exception in starting ptzListenerThread message:"+e.getMessage());
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
		//fileLogger.debug("out contextInitialized DBDataServiceOnLoadInitializer...");
		fileLogger.info("Exit Function : contextInitialized");
	}
	
}

