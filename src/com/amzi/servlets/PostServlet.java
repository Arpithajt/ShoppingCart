package com.amzi.servlets;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.TreeMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import aj.org.objectweb.asm.Attribute;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DeleteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amzi.dao.*;
import com.sun.corba.se.impl.oa.poa.ActiveObjectMap.Key;

public class PostServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public PostServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		AWSDynamodb awsDynamo = new AWSDynamodb();
		TreeMap<String, String> val = new TreeMap<String, String>();
		try {
			List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
			for (FileItem item : items) {
				if (item.isFormField()) {
					// Process regular form field (input type="text|radio|checkbox|etc", select, etc).
					String fieldName = item.getFieldName();
					String fieldValue = item.getString();
					val.put(fieldName, fieldValue);
				} else {
					// Process form file field (input type="file").

					String fileName = FilenameUtils.getName(item.getName());
					InputStream fileContent = item.getInputStream();
					File file = new File("/usr/share/tomcat7/"+fileName);
					FileOutputStream  outputStream = new FileOutputStream(file);

					int read = 0;
					byte[] bytes = new byte[1024];

					while ((read = fileContent.read(bytes)) != -1) {
						outputStream.write(bytes, 0, read);
					}
					awsDynamo.s3upload(file);
					val.put("fileNm", file.getName());
				}
			}
	
			HttpSession session = request.getSession();
			String name = (String) session.getAttribute("name");

			awsDynamo.insertintotable(val.get("description"), val.get("price"), name, val.get("fileNm"),val.get("category"));
			RequestDispatcher rd=request.getRequestDispatcher("welcome.jsp"); 
			rd.forward(request,response);

		} catch (FileUploadException e) {
			throw new ServletException("Cannot parse multipart request.", e);
		}
	}

}
