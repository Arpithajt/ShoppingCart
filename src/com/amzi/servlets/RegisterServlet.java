package com.amzi.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.amzi.dao.DBManager;

public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {



	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");  
		PrintWriter out = response.getWriter();
	
		String name=request.getParameter("name");  
		String email=request.getParameter("email"); 
		String user=request.getParameter("user");
		String pass=request.getParameter("pass");
		if (name.length()<10)
		{
			DBManager db = new DBManager();
			db.database(name,email, user,pass);
		}	
		else
		{
			HttpSession session = request.getSession();
			session.setAttribute("msg","name length should be 10");
			out.println(request.getParameter("msg"));
			RequestDispatcher rd=request.getRequestDispatcher("register.jsp"); 
			rd.forward(request,response); 
		}

		
	}

}
