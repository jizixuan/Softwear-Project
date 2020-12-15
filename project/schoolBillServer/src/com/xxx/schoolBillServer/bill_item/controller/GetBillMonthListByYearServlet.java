package com.xxx.schoolBillServer.bill_item.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.mysql.fabric.xmlrpc.base.Array;
import com.xxx.schoolBillServer.bill_item.service.BillItemServiceImpl;
import com.xxx.schoolBillServer.entity.BillMonth;

/**
 * Servlet implementation class GetBillMonthListByYearServlet
 */
@WebServlet("/GetBillMonthListByYearServlet")
public class GetBillMonthListByYearServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetBillMonthListByYearServlet() {
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
		String ss = request.getParameter("year");
		String ss1 = request.getParameter("id");
		int year = Integer.parseInt(ss);
		int id = Integer.parseInt(ss1);
		BillItemServiceImpl billItemServiceImpl = new BillItemServiceImpl();
		List<BillMonth> billMonths = new ArrayList<>();
		billMonths = billItemServiceImpl.getBillMonthListByYear(year,id);
		PrintWriter writer = response.getWriter();
		Gson gson = new Gson();
		String json = gson.toJson(billMonths);
		System.out.println(json);
		writer.print(json);
		writer.flush();
		writer.close();
	}

}
