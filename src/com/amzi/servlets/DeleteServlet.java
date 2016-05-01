package com.amzi.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DeleteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

public class DeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		  AmazonDynamoDBClient client = new AmazonDynamoDBClient(new ProfileCredentialsProvider());
		  client.setRegion(Region.getRegion(Regions.US_WEST_2));
		DynamoDB dynamoDB = new DynamoDB(client);
		response.setContentType("text/html");  
		PrintWriter out = response.getWriter();  
		String id = (String) request.getParameter("picid");
		System.out.println("pic id -------"+id);
		Table table = dynamoDB.getTable("pictures");
		DeleteItemOutcome outcome = table.deleteItem("pictureid", id);
		RequestDispatcher rd=request.getRequestDispatcher("welcome.jsp");  
		rd.forward(request,response);

	}

}
