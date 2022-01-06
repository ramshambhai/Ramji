package in.vnl.api.common;
import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

import in.vnl.report.ReportServer;


public class DbBackup {
  private File f;
  private FileChannel channel;
  private FileLock lock;
  private static final Object MKDIRS_LOCK = new Object();
  private static final String ALREADY_RUNNING = "Back-Up Process Already Running!!";
  private final static String separator = File.separator;

  //path of postgres bin
  private final static String POSTGRES_PATH = "/usr/local/pgsql/bin/";
  //private final static String DUMP_NAME = TEMPDIRPATH + separator + "db.dump";
  //private final static String CMD = POSTGRES_PATH + "pg_dump -f " + DUMP_NAME + " -Fc -v -U postgres -w locator";

  /**
   * the new Log4j logger
   */
  static Logger logger = Logger.getLogger("file");

  /**
   * This method is written to make sure that at one time
   * only one Back-Up Process is running in OMC either through
   * OMC Client or Job Scheduler.
   *
   * @return
   */
  private synchronized boolean getLock() {
    try {
    	logger.info("Inside Function : getLock");
      f = new File(".BackUpLock");
      logger.debug("Lock File f.getAbsolutePath() = " + f.getAbsolutePath());
      // Check if the lock exist
      // Try to get the lock

      if (f.exists()) {
        logger.debug("BackUpProcess.getLock:: Only 1 instance of BackUpProcess can run. File Exists");
        return false;
      }

      channel = new RandomAccessFile(f, "rw").getChannel();
      try {
        lock = channel.tryLock();
      } catch (OverlappingFileLockException e) {
        // File is lock by other application
        channel.close();
        logger.debug("BackUpProcess.getLock:: Only 1 instance of BackUpProcess can run.");
        return false;
      }
      if (lock == null) {
        // File is lock by other application
        channel.close();
        logger.debug("BackUpProcess.getLock:: Only 1 instance of BackUpProcess can run.");
        return false;
      }
      // Add shutdown hook to release lock when application shutdown
      ShutdownHook shutdownHook = new ShutdownHook();
      Runtime.getRuntime().addShutdownHook(shutdownHook);
    } catch (Exception e) {
      logger.error("BackUpProcess.getLock Exception :: " + e.getMessage());
      return false;
    }
 	logger.info("Exit Function : getLock");
    return true;
  }

  /**
   * This method is called when the Back-Up Process exits
   * and when the JVM in which it is running goes down.
   * It clears the lock taken by the Back-Up Process.
   */
  private void unlockFile() {
	 	logger.info("Inside Function : unlockFile");
    // release and delete file lock
    try {
      if (lock != null) {
        logger.debug("BackUp Lock Released");
        lock.release();
        channel.close();
        f.delete();
        channel = null;
        lock = null;
        f = null;
      }
    } catch (Exception e) {
      logger.error("BackUpProcess.unlockFile Exception :: " + e.getMessage());
    }
	logger.info("Exit Function : unlockFile");
  }

  private boolean makeBackUpDir(String destination) {
    System.out.println("destination = " + destination);
    try {
      File dir = new File(destination);
      if (!dir.exists()) {
        return dir.mkdirs();
      }
    } catch (Exception e) {
      return false;
    }
    return true;
  }



  public String performBackUp() {
	  SimpleDateFormat formater = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
	  formater.setTimeZone(TimeZone.getTimeZone("IST"));
      String reportFolderName = "FALCON_C2_db_backup_"+formater.format(new Date());
	  String childDirectoryPath = new ReportServer().createChildReportDirectoryForFullDbBackup(reportFolderName);
	  String DUMP_NAME = childDirectoryPath+"/locator.dump";
	  String CMD = POSTGRES_PATH + "pg_dump -f " + DUMP_NAME + " -Fc -v -U postgres -w locator";
    
      String res = performDbBackUp(CMD,childDirectoryPath);
    


    return res;
  }

  /**
   * This is the actual method where the omc back up is done.
   *
   * @param destination destination directory where the dump is to be stored.
   * @return BackUpResult
   */
  public String performDbBackUp(String CMD,String childDirectoryPath) {
		logger.info("Inside Function : performDbBackUp");
    // Step 1 Take a Back-Up Process lock
    boolean lock = getLock();
    if (lock) {
      try {
        //Step 2 Make a temp back-up directory
        //boolean makeDir = safeMkdirs(new File(TEMPDIRPATH));
       // if (makeDir) {

          //Step 2.1 make /usr/local/omc-back-up directory if not exists
          //boolean backDir = makeBackUpDir(destination);
            //Step 3  Take db dump
            boolean isDumpTaken = takeDBDump(CMD);
            
            if (isDumpTaken) {
              //Step 4 Take /home/omc folder dump
              //boolean isZip = CreatingZip.createZipArchive(FILE_ARCHIVE_PATH, ARCHIVE_NAME, "omc");
            } else {
              return "{\"result\":\"fail\",\"msg\":\"Can't take DB Dump\"}";
            }

        }catch (Exception e) {
        logger.error("BackUpProcess.performBackUp Exception :: " + e.getMessage());
        return "{\"result\":\"fail\",\"msg\":\"Generic Error\"}";
      } finally {
        unlockFile();
        //delete gz
      }
    } else {
      return "{\"result\":\"fail\",\"msg\":\""+ALREADY_RUNNING+"\"}";
    }

  logger.debug("result = success");
	logger.info("Exit Function : performDbBackUp");
    return "{\"result\":\"success\",\"msg\":\"Backup created successfully\",\"dir\":\""+childDirectoryPath+"\"}";
  }




  /**
   * This method takes the db dump of the OMC using pg_dump utility.   *
   *
   * @return
   */
  private boolean takeDBDump(String CMD) {
		logger.info("Inside Function : takeDBDump");
    try {
      final StringBuffer output = new StringBuffer();
      final StringBuffer error = new StringBuffer();

      logger.error("Executing command: " + CMD);
      String[] commands = {"sh", "-c", CMD};
      final ProcessBuilder pb = new ProcessBuilder(commands);
      final Process process = pb.start();

      final BufferedReader inputStreamReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
      final BufferedReader errStreamReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

      new Thread(new Runnable() {
        public void run() {
          try {
            while (inputStreamReader.readLine() != null) {
            }
          } catch (Exception e) {
            logger.error("BackUpProcess.run Exception :: " + e.getMessage());
          }
        }
      }).start();

      new Thread(new Runnable() {
        public void run() {
          try {
            while (errStreamReader.readLine() != null) {
            }
          } catch (Exception e) {
            logger.error("BackUpProcess.run Exception :: " + e.getMessage());
          }
        }
      }).start();


      for (String line; (line = inputStreamReader.readLine()) != null;) {
        output.append(line + "\n");
      }

      for (String line; (line = errStreamReader.readLine()) != null;) {
        error.append(line + "\n");
      }

      logger.debug("output = " + output);
      logger.error("error = " + error);

      if (error.length() == 0) {
        logger.debug("DB DUMP SUCCESSFULL");
      } else {
        String err = error.toString().toLowerCase();
        if (err.contains("failed") || err.contains("abort") || err.contains("exception")) {
          logger.debug("DB DUMP ERROR");
          return false;
        }
      }

      if (inputStreamReader != null) {
        inputStreamReader.close();
      }

      if (errStreamReader != null) {
        errStreamReader.close();
      }

    } catch (Exception e) {
      logger.debug("BackUpProcess.takeDBDump Exception :: " + e.getMessage());
      return false;
    }
    logger.debug("DB DUMP SUCCESSFULL");
	logger.info("Exit Function : takeDBDump");
    return true;
  }

  /**
   * Deletes the directories recursively
   *
   * @param directory
   */
  /**
   * It is shutdown hook for abnormal termination of server.
   */
  class ShutdownHook extends Thread {
    public void run() {
      logger.debug("BackUpProcess$ShutdownHook.run :: Running ShutdownHook");
      unlockFile();
    }
  }

  public static void main(String[] args) {
    /*BackUpResult val = BackUpProcess.INSTANCE.performBackUp("/tmp/sumeer", "");
    System.out.println("val = " + val);*/
  }
}
