package com.xxx.schoolBillServer.icon.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.xxx.schoolBillServer.bill_item.service.BillItemServiceImpl;
import com.xxx.schoolBillServer.bill_type.service.BillTypeServiceImpl;
import com.xxx.schoolBillServer.entity.BillItem;
import com.xxx.schoolBillServer.icon.service.IconItemServiceImpl;

/**
 * Servlet implementation class IconItemGetByDateServlet
 */
@WebServlet("/IconItemGetByDateServlet")
public class IconItemGetByDateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public IconItemGetByDateServlet() {
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
		String firstW=request.getParameter("first");
		String lastW=request.getParameter("last");
		int userId=Integer.parseInt(request.getParameter("userId"));
		System.out.println(firstW+" "+lastW+" "+userId);
		List<BillItem> billItems=new IconItemServiceImpl().getIconItemByDate(userId,firstW,lastW);
		for (int i = 0; i < billItems.size(); i++) {
	    	for (int j = 0; j < billItems.size(); ) {
	    		if (i != j && billItems.get(j).getTypeId()==billItems.get(i).getTypeId()) {
	    			billItems.get(i).setNum(billItems.get(i).getNum() + billItems.get(j).getNum());
	    			billItems.get(i).setDate(billItems.get(j).getDate());
	    			billItems.remove(j);
	    		} else {
	    			j++;
	    		}
	    	}
	    }
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
