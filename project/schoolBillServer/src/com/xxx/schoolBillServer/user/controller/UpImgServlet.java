package com.xxx.schoolBillServer.user.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ProcessBuilder.Redirect;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

/**
 * Servlet implementation class UpImgServlet
 */
@WebServlet("/UpImgServlet")
public class UpImgServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpImgServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		DiskFileItemFactory factory = new DiskFileItemFactory(); 
		String path = getServletContext().getRealPath("/imgs/");
		factory.setSizeThreshold(4096*4096);  
        ServletFileUpload upload = new ServletFileUpload(factory);  
        try {
			Map<String , List<FileItem>> aMap = upload.parseParameterMap(request);
			//获取路径名  
			List<FileItem> fileItems = aMap.get("img");
			for(FileItem item : fileItems) {
				String name = item.getName();
				InputStream inputStream = item.getInputStream();
				FileOutputStream out = new FileOutputStream(path+name );
				byte[] b = new byte[2048];
				int len = 0;
				while((len = inputStream.read(b))>0) {
					out.write(b,0,len);
				}
				out.flush();
				inputStream.close();
				out.close();
			}
			 
		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
		doGet(request, response);
	}

}
