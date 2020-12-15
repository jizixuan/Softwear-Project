package com.xxx.schoolBillServer.user.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.xxx.schoolBillServer.entity.User;
import com.xxx.schoolBillServer.user.service.UserServiceImpl;

/**
 * Servlet implementation class FindUserServlet
 */
@WebServlet("/FindUserServlet")
public class FindUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FindUserServlet() {
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
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		BufferedReader reader = request.getReader();
		String json = reader.readLine();
		System.out.println("接收帐户密码"+json);
		Gson gson = new Gson();
		User user = gson.fromJson(json, User.class);
		UserServiceImpl userServiceImpl = new UserServiceImpl();
		User user2 = userServiceImpl.findUser(user.getPhone());
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
		doGet(request,response);
	}
}
