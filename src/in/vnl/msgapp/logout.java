package in.vnl.msgapp;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import in.vnl.api.common.AuditHandler;


/**
 * Servlet implementation class logout
 */
//@WebServlet("/logout")
public class logout extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public logout() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		HttpSession ses = request.getSession();
		
		LinkedHashMap<String,String> data = new LinkedHashMap<String,String>();
		data.put("type", "Logout");
		data.put("username", ses.getAttribute("username").toString());
		//data.put("role", ses.getAttribute("role").toString());
		//data.put("userid", ses.getAttribute("userid").toString());
		data.put("ip",request.getRemoteAddr());
		new AuditHandler().auditLog(data, 1);

		
		ses.removeAttribute("userid");
		ses.removeAttribute("mobtime");
		response.sendRedirect("views/login.jsp");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
