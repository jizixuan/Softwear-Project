package com.xxx.schoolBillServer.icon.controller;

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

import com.xxx.schoolBillServer.bill_type.service.BillTypeServiceImpl;
import com.xxx.schoolBillServer.entity.BillItem;
import com.xxx.schoolBillServer.entity.BillType;
import com.xxx.schoolBillServer.icon.service.IconItemServiceImpl;

/**
 * Servlet implementation class IconItemGetByTypeId
 */
@WebServlet("/IconItemGetByTypeId")
public class IconItemGetByTypeId extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public IconItemGetByTypeId() {
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
		String type=request.getParameter("type");
		int typeId=new BillTypeServiceImpl().getTypeIdByName(type);
		System.out.println(typeId);
		List<BillItem> billItems=new IconItemServiceImpl().getIconItemListByTypeId(typeId);
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
		System.out.println(jsonArray.toString());
		doGet(request, response);
	}

}
