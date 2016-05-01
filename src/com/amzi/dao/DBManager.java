package com.amzi.dao;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;


public class DBManager {
	 
	       static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
		   static final String DB_URL = "jdbc:mysql://mydbinstance.ctbgj7jjpbfr.us-west-2.rds.amazonaws.com/shoppingcart";

		   //  Database credentials
		   static final String USER = "appijt";
		   static final String PASS = "intern123";
		  
		   public void database(String name, String email, String user, String pass1)
			{
		   Connection conn = null;
		   Statement stmt = null;
		   try{
		      //STEP 2: Register JDBC driver
		      Class.forName("com.mysql.jdbc.Driver");

		      //STEP 3: Open a connection
		      System.out.println("Connecting to database...");
		      conn = DriverManager.getConnection(DB_URL, USER, PASS);
		      
		      stmt = conn.createStatement();
		      
		      String sql = "insert into USER values ('" + user + "','" + pass1 + "')";
		      stmt.executeUpdate(sql);
		      System.out.println("Inserted successfully...");
		   }catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		   }catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		   }finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		            stmt.close();
		      }catch(SQLException se2){
		      }// nothing we can do
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }//end try
		}//end main
		   public boolean databaselogin(String user, String pass1)
			{
			   
		   Connection conn = null;
		   Statement stmt = null;
		   try{
		      //STEP 2: Register JDBC driver
		      Class.forName("com.mysql.jdbc.Driver");

		      //STEP 3: Open a connection
		      System.out.println("Connecting to database...");
		      conn = DriverManager.getConnection(DB_URL, USER, PASS);
		      
		       Statement st=conn.createStatement();
	           String query="select * from shoppingcart.USER";
	           System.out.println("query");
	           ResultSet rs=st.executeQuery(query);
	            while(rs.next())
	            {
	            	if(rs.getString("user").equals(user) && rs.getString("pass1").equals(pass1))
	            	{
	            		System.out.println("hi i enn");
	            		return true;
	            	}
	            }
		   }catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		   }catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		   }finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		            stmt.close();
		      }catch(SQLException se2){
		      }// nothing we can do
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }return false;
		}//end main
		}
	

