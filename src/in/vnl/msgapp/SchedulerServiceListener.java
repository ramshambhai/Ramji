package in.vnl.msgapp;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;
//import com.oss.vihaan.dashboard.DBService;
//import com.oss.vihaan.dashboard.dbopr.DbOperations;
//import com.oss.vihaan.dashboard.scripthandlers.ScriptHandler;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class SchedulerServiceListener implements ServletContextListener
{
		static Logger fileLogger = Logger.getLogger("file");
		private ServletContext context = null;

		public void contextDestroyed(ServletContextEvent event) 
		{
			fileLogger.debug("Application is undeploying");
		}
		public void contextInitialized(ServletContextEvent event) 
		{
			fileLogger.info("Inside Function :contextInitialized" );
			fileLogger.debug("Application is deploying now");
			fileLogger.debug("In contextInitialized for SchedulerServiceListener");
			Common co = new Common();
			Statement smt = null;
			Connection con = co.getDbConnection();
			Timer timer=SingleTimer.getInstance().getTimer();
			fileLogger.debug("size at Listener start:"+ScheduledTaskObjectHandler.getTaskObjectMapSize());
			try
			{
				smt = con.createStatement();			
				String query = "select * from job_details";
				fileLogger.debug(query);
				ResultSet rs = smt.executeQuery(query);
				while(rs.next())
				{
					if(rs.getString("status").equalsIgnoreCase("start")){
						String id=rs.getString("id");
						String cmdType=rs.getString("cmd_type");
						String fileName=rs.getString("file_name");
						String ftn=rs.getString("ftn");
						String hlr=rs.getString("hlr");
						String msc=rs.getString("msc");
						String idType=rs.getString("id_type");
						String reqType=rs.getString("req_type");
						String typeVal=rs.getString("type_value");
						String vlr=rs.getString("vlr");
						String periodicity=rs.getString("periodicity");
						String nibIp=rs.getString("nibip");
						GeoTask geoTask=new GeoTask(cmdType,fileName,ftn,hlr,msc,idType,reqType,typeVal,vlr,nibIp);
						timer.scheduleAtFixedRate(geoTask,0,Long.parseLong(periodicity));
						ScheduledTaskObjectHandler.setScheduledTaskObject(id,geoTask);
					}
					
				}
				fileLogger.debug("size at Listener end:"+ScheduledTaskObjectHandler.getTaskObjectMapSize());
			}catch(Exception e){
				
			}finally{
				try
				{
					smt.close();
					con.close();
				}
				catch(Exception E)
				{
				}
			}fileLogger.info("Exit Function :contextInitialized" );	
			
			
		}
		

}
