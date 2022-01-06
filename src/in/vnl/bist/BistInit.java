package in.vnl.bist;

import java.io.FileInputStream;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.json.JSONArray;

public class BistInit implements ServletContextListener{
	static Logger fileLogger = Logger.getLogger("file");
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		fileLogger.info("Inside Function : contextInitialized");	
		try
		{
			String absolutePath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
			absolutePath = absolutePath.substring(0,absolutePath.lastIndexOf("WEB-INF"));
			FileInputStream fi = new FileInputStream(absolutePath	+ "/resources/config/bist_config.json");
			Bist bist=Bist.getInstance();
			JSONArray js = new JSONArray(bist.getFileContent(fi));
			bist.setupBistTestCases(js);
			
			
		}
		catch(Exception E)
		{
			fileLogger.debug("bist_config,json not found" + E.getMessage());
		}
		    fileLogger.info("Exit Function : contextInitialized");	
	}		
}
