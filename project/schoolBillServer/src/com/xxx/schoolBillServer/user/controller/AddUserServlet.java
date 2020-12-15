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
 * Servlet implementation class AddUserServlet
 */
@WebServlet("/AddUserServlet")
public class AddUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddUserServlet() {
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
		PrintWriter writer = response.getWriter();
		if(user2.getId()==0) {
			boolean tag = userServiceImpl.addUser(user);
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
		doGet(request, response);
	}

}
