package in.vnl.api.config;

import org.apache.log4j.Logger;

public class Test {
	static Logger fileLogger = Logger.getLogger("file");
	public static void main(String[] args) 
	{
		fileLogger.debug("hello");
	}

}
