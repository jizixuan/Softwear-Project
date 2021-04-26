package com.xxx.schoolBillServer.user.controller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.xxx.schoolBillServer.entity.BillItem;
import com.xxx.schoolBillServer.entity.Note;
import com.xxx.schoolBillServer.entity.User;
import com.xxx.schoolBillServer.user.service.UserServiceImpl;

@Controller
public class UserController {
	@Resource UserServiceImpl serviceImpl;
	
	@RequestMapping(value = "AddUserServlet", method=RequestMethod.POST)
	@ResponseBody 
	public void AddUserServlet(HttpServletRequest request,HttpServletResponse response) {
		BufferedReader reader;
		try {
			reader = request.getReader();
			String json = reader.readLine();
			System.out.println("接收帐户密码"+json);
			Gson gson = new Gson();
			User user = gson.fromJson(json, User.class);
			User user2 = serviceImpl.findUser(user.getPhone());
			PrintWriter writer = response.getWriter();
			if(user2.getId()==0) {
				boolean tag = serviceImpl.addUser(user);
				if(tag) {
					writer.write("1");
					System.out.println("添加用户信息成功");
				}else {
					System.out.println("添加用户信息失败");
					writer.write("2");
				}
			}else {
				System.out.println("该号码已经注册，请登录");
				writer.write("3");
			}
			reader.close();
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@RequestMapping(value = "DownImgServlet", method=RequestMethod.POST)
	@ResponseBody 
	public void DownImgServlet(@RequestParam("img") String name,HttpServletResponse response,HttpServletRequest request) {
		String path = request.getServletContext().getRealPath("/imgs/");
		String path1 = path + name;
		if(name != null) {
			InputStream in;
			try {
				in = new FileInputStream(path1);
				OutputStream writer = response.getOutputStream();
				//循环读写
				int n = -1;
				while((n = in.read())!=-1) {
					writer.write(n);
				}
				writer.close();
				in.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	@RequestMapping(value = "FindUserServlet", method=RequestMethod.POST)
	@ResponseBody 
	public void FindUserServlet(HttpServletResponse response,HttpServletRequest request) {
		BufferedReader reader;
		try {
			reader = request.getReader();
			String json = reader.readLine();
			System.out.println("接收帐户密码"+json);
			Gson gson = new Gson();
			User user = gson.fromJson(json, User.class);
			User user2 = serviceImpl.findUser(user.getPhone());
			String str = gson.toJson(user2);
			PrintWriter writer = response.getWriter();
			if(user2.getId()==0) {
				System.out.println("提示注册返回1");
				String string = "1";
				writer.print(string);
			}else {
				if(user2.getPwd().equals(user.getPwd())) {
					System.out.println("提示登录成功返回用户信息");
					writer.print(str);
				}else {
					System.out.println("提示密码错误返回3");
					writer.print("3");
				}
			}
			reader.close();
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@RequestMapping(value = "UpdateUserServlet", method=RequestMethod.POST)
	@ResponseBody 
	public void UpdateUserServlet(HttpServletResponse response,HttpServletRequest request) {
		BufferedReader reader;
		try {
			reader = request.getReader();
			String json = reader.readLine();
			System.out.println("接收帐户密码"+json);
			Gson gson = new Gson();
			User user = gson.fromJson(json, User.class);
			boolean tag = serviceImpl.updateUser(user);
			PrintWriter writer = response.getWriter();
			if(tag) {
				writer.write("1");
				System.out.println("更改用户信息成功");
			}else {
				System.out.println("更改用户信息失败");
				writer.write("2");
			}
			reader.close();
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	@RequestMapping(value = "UpImgServlet", method=RequestMethod.POST)
	@ResponseBody 
	public void UpImgServlet(HttpServletResponse response,HttpServletRequest request) {
		DiskFileItemFactory factory = new DiskFileItemFactory(); 
		String path = request.getServletContext().getRealPath("/imgs/");
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
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
