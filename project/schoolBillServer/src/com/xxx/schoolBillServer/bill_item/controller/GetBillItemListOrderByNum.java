package com.xxx.schoolBillServer.bill_item.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.xxx.schoolBillServer.bill_item.service.BillItemServiceImpl;
import com.xxx.schoolBillServer.entity.BillItem;

/**
 * Servlet implementation class GetBillItemListOrderByNum
 */
@WebServlet("/GetBillItemListOrderByNum")
public class GetBillItemListOrderByNum extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetBillItemListOrderByNum() {
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
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		int year=Integer.parseInt(request.getParameter("year"));
		int month=Integer.parseInt(request.getParameter("month"));
		int userId=Integer.parseInt(request.getParameter("userId"));
		List<BillItem> billItems=new BillItemServiceImpl().getBillItemListOrderByNum(year, month, userId);
		JSONArray jsonArray=new JSONArray();
		for(BillItem billItem:billItems) {
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("id", billItem.getId());
			jsonObject.put("day", billItem.getDay());
			jsonObject.put("month", billItem.getMonth());
			jsonObject.put("year", billItem.getYear());
			jsonObject.put("num", billItem.getNum());
			jsonObject.put("note", billItem.getNote());
			jsonObject.put("date", billItem.getDate());
			jsonObject.put("typeId", billItem.getTypeId());
			jsonArray.put(jsonObject);
		}
		PrintWriter w = response.getWriter();
		w.write(jsonArray.toString());
		doGet(request, response);
	}

}
