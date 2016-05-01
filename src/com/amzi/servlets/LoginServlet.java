package com.amzi.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.amzi.dao.DBManager;


public class LoginServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)  
			throws ServletException, IOException
	{ 
		DBManager db = new DBManager();
		response.setContentType("text/html");  
		PrintWriter out = response.getWriter();  
		String name=request.getParameter("username");  
		String pass=request.getParameter("userpass"); 
		
		
		if((name != null) && (pass!= null))
		{ 
			if(db.databaselogin(name, pass))
			{
				HttpSession session = request.getSession();
				session.setAttribute("name", name);
				RequestDispatcher rd=request.getRequestDispatcher("welcome.jsp");  
				rd.forward(request,response); 
			}
			else
				out.println("<h1>Username or password error</h1>");
		} 
	
		out.close();  
	}  
}  