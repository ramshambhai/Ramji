package in.vnl.msgapp;


//Import required java libraries
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.log4j.Logger;

import java.util.*;

//Implements Filter class
public class LoginFilter implements Filter  {
	static Logger fileLogger = Logger.getLogger("file");
	public void  init(FilterConfig config) 
                      throws ServletException{
	
	
	// Get init parameter 
    String path = config.getInitParameter("ignore-login-path"); 

    //Print the init parameter 
    fileLogger.debug("Test Param: " + path); 
 
}
public void  doFilter(ServletRequest request, 
              ServletResponse response,
              FilterChain chain) 
              throws java.io.IOException, ServletException {
	
	HttpServletRequest req = (HttpServletRequest)request;
	HttpServletResponse res = (HttpServletResponse)response;

	HttpSession session = req.getSession();
	
	String userid = (String)session.getAttribute("userid"); 

	String path = ((HttpServletRequest) request).getRequestURI();
	fileLogger.debug(path);
	try
    {
		
		if(userid == null && !path.equalsIgnoreCase("/locator/views/login.jsp"))
	    {
	    
	    	if(path.startsWith("/locator/views"))
    			res.sendRedirect("/locator/views/login.jsp");
			else
			{
				res.sendRedirect("views/login.jsp");
			}
	    }
    	else if(path.equalsIgnoreCase("/locator/"))
    	{
    		res.sendRedirect("/locator/views/dashboard/maps.jsp");
    	}
    	else
    	{
    		chain.doFilter(request,response);
    	}
    }
    catch(Exception E)
    {
    	fileLogger.error("Error Whilre redirecting : "+E.getMessage());
    }

   // Pass request back down the filter chain
   //chain.doFilter(request,response);
}
public void destroy( ){
   /* Called before the Filter instance is removed 
   from service by the web container*/
}
}
