package com.xxx.schoolBillServer.bill_item.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xxx.schoolBillServer.bill_item.service.BillItemServiceImpl;

/**
 * Servlet implementation class GetBillNumServlet
 */
@WebServlet("/GetBillNumServlet")
public class GetBillNumServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetBillNumServlet() {
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
		String month = request.getParameter("month");
		String id = request.getParameter("id");
		BillItemServiceImpl billItemServiceImpl = new BillItemServiceImpl();
		int a = billItemServiceImpl.getBillNum(month,id);
		System.out.println(a+"");
		PrintWriter writer = response.getWriter();
		String aString = ""+a;
		writer.write(aString);
		writer.flush();
		writer.close();
		doGet(request, response);
	}

}
