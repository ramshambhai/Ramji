package in.vnl.msgapp;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.*;
/**
 * Servlet implementation class Operations
 */
//@WebServlet("/userOperations")
public class userOperations extends HttpServlet {
	static Logger fileLogger = Logger.getLogger("file");
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public userOperations() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
		String methodName = request.getParameter("methodName");
		switch(methodName)
		{
			case "getAllUsers":
			out.print(getAllUsers());
			break;			
			case "createUser" : 
				out.print(createUser(request));
				break;
			case "deleteUser" :
				out.print(deleteUser(request));
				break;			
		}
	}
	
	public JSONArray getAllUsers()
	{
		fileLogger.info("Inside Function :getAllUsers" );
		Common co = new Common();
		Statement smt = null;
		Connection con = co.getDbConnection();
		JSONArray ja = new JSONArray();
		try
		{
			smt = con.createStatement();			
			String query = "select * from users";
			fileLogger.debug(query);
			ResultSet rs = smt.executeQuery(query);
			
			while(rs.next())
			{
				JSONObject jo = new JSONObject();
				jo.put("id", rs.getString("id"));
				jo.put("name", rs.getString("user_name"));
				ja.put(jo);
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
				con.close();
			}
			catch(Exception E)
			{
				
			}
		}	
		fileLogger.info("Exit Function :getAllUsers" );
		return ja;
	}
	
	
	
	public JSONArray getAllNumbers()
	{
		fileLogger.info("Inside Function :getAllNumbers" );
		Common co = new Common();
		Statement smt = null;
		Connection con = co.getDbConnection();
		JSONArray ja = new JSONArray();
		try
		{
			smt = con.createStatement();			
			String query = "select * from phone_numbers";
			fileLogger.debug(query);
			ResultSet rs = smt.executeQuery(query);
			
			while(rs.next())
			{
				JSONObject jo = new JSONObject();
				jo.put("id", rs.getString("id"));
				jo.put("number", rs.getString("phone_number"));
				ja.put(jo);
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
				con.close();
			}
			catch(Exception E)
			{
				
			}
		}	
		fileLogger.info("Exit Function :getAllNumbers" );
		return ja;
	}
	
	
	public JSONArray getPhoneNumbersForGroups(HttpServletRequest request)
	{
		fileLogger.info("Inside Function :getPhoneNumbersForGroups" );
		Common co = new Common();
		Statement smt = null;
		Connection con = co.getDbConnection();
		JSONArray ja = new JSONArray();
		String grp = request.getParameter("groups");
		try
		{
			smt = con.createStatement();			
			String query = "select * from phone_numbers where id in (select num_id from group_phone_mapping where grp_id in("+grp+"));";
			fileLogger.debug(query);
			ResultSet rs = smt.executeQuery(query);			
			while(rs.next())
			{				
				ja.put(rs.getString("phone_number"));
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
				con.close();
			}
			catch(Exception E)
			{
				
			}
		}	
		fileLogger.info("Exit Function :getPhoneNumbersForGroups" );
		return ja;
	}
	
	/*This function will send the message */
	
	public int sendMessage(HttpServletRequest request)
	{
		String message = request.getParameter("msg");
		String numbers = request.getParameter("numbers");
		return 1;
	}
	
	//this functon will be used to create the user	
	public boolean createUser(HttpServletRequest request)
	{
		String userName = request.getParameter("user_name");
		String password = request.getParameter("password");
		String role = request.getParameter("role");
		
		String query = "insert into users(user_name,password,role,isactive) values('"+userName+"','"+password+"','"+role+"','t')";
		Common co = new Common();
		return co.executeDLOperation(query);		
	}
	//this function will delete the group
	public boolean deleteUser(HttpServletRequest request)
	{
		String userid = request.getParameter("id");
		
		String query = "delete from users  where id = "+userid;
		
		Common co = new Common();		
		return co.executeDLOperation(query);
	}
	
	
}
