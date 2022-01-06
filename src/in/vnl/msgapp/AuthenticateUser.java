package in.vnl.msgapp;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import in.vnl.api.common.ApiCommon;
import in.vnl.api.common.AuditHandler;


/**
 * Servlet implementation class AuthenticateUser
 */
//@WebServlet("/AuthenticateUser")
public class AuthenticateUser extends HttpServlet {
	static Logger fileLogger = Logger.getLogger("file");
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AuthenticateUser() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		fileLogger.info("Inside Function : doPost");
		PrintWriter out = response.getWriter();
		Statement smt = null;
		Common co = new Common();
		
		Connection con = co.getDbConnection();
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String userid = null;
		String userName = null;
		String themeCss = "";
		Statement smt1 = null;
		String role = null;
		String level = null;
		int isUserPresent = 0;
		try
		{
			smt = con.createStatement();
			smt1 = con.createStatement();
			String query = "select count(*)::integer as isUserPresent from users where user_name = '"+username+"' and password='"+password+"'";
			fileLogger.debug(query);
			ResultSet rs = smt.executeQuery(query);
			while(rs.next())
			{
				isUserPresent = rs.getInt("isUserPresent");
			}
			if(isUserPresent == 1)
			{
				query = "select * from users where user_name = '"+username+"' and password='"+password+"'";
				ResultSet rs1 = smt1.executeQuery(query);
				fileLogger.debug(query);
				while(rs1.next())
				{
					userName = rs1.getString("user_name");
					userid = rs1.getString("id");
					role = rs1.getString("role");
					themeCss = rs1.getString("theme_css");
					level = rs1.getString("level");
				}
				HttpSession session = request.getSession();
				session.setAttribute("userName", userName);
				session.setAttribute("userid", userid);
				session.setAttribute("role", role);
				session.setAttribute("username", username);
				session.setAttribute("themeCss", themeCss);
				session.setAttribute("level", level);
				
				LinkedHashMap<String,String> log = new LinkedHashMap<String,String>();
				log.put("type", "Login");
				log.put("username", username);
				//data.put("role", role);
				//data.put("userid", userid);
				log.put("ip",request.getRemoteAddr());
				new AuditHandler().auditLog(log, 1);
			}
		}
		catch(Exception E)
		{
			fileLogger.error("Exeption while authenticating the user "+E.getMessage());
		}
		finally
		{
			try
			{
				smt.close();
				smt1.close();
				con.close();
			}
			catch(Exception E)
			{
				
			}
		}
		fileLogger.info("Exit Function : doPost");	
		out.print(isUserPresent);
		
	}
	

}
