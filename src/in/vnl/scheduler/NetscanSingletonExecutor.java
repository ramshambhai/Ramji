package in.vnl.scheduler;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import org.apache.log4j.Logger;
public class NetscanSingletonExecutor {
	static Logger fileLogger = Logger.getLogger("file"); 
    private static NetscanSingletonExecutor myObj;
    private ScheduledExecutorService scheduler;
    private ScheduledFuture future =null;
    public ScheduledFuture getFuture() {
		return future;
	}
	public void setFuture(ScheduledFuture future) {
		this.future = future;
	}
	private NetscanSingletonExecutor(){
      
    }
    public static NetscanSingletonExecutor getInstance(){
        if(myObj == null){
            myObj = new NetscanSingletonExecutor();
        }
        return myObj;
    }
     
    public ScheduledExecutorService getScheduler(){
        if(scheduler==null){
        	scheduler = Executors.newScheduledThreadPool(1);
        }
        	return scheduler;
    }
    
	public void shutdownScannerSchedulerOperation()
	{
		fileLogger.debug("in shutdownScannerSchedulerOperation");
		if(scheduler!=null)
			scheduler.shutdownNow();
			scheduler=null;
		fileLogger.debug("out shutdownScannerSchedulerOperation");
	}
}