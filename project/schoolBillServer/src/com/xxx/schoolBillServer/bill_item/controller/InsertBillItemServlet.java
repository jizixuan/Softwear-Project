package com.xxx.schoolBillServer.bill_item.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xxx.schoolBillServer.bill_item.service.BillItemServiceImpl;
import com.xxx.schoolBillServer.bill_type.service.BillTypeServiceImpl;

/**
 * Servlet implementation class InsertBillItemServlet
 */
@WebServlet("/InsertBillItemServlet")
public class InsertBillItemServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InsertBillItemServlet() {
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
		Double num=Double.parseDouble(request.getParameter("num"));
		String note=request.getParameter("note");
		String dateValue=request.getParameter("date");
		String typeName=request.getParameter("typeName");
		int userId=Integer.parseInt(request.getParameter("userId"));
		int year=Integer.parseInt(dateValue.split("/")[0]);
		int month=Integer.parseInt(dateValue.split("/")[1]);
		int day=Integer.parseInt(dateValue.split("/")[2]);
		Date date=stringToDate(year+"-"+month+"-"+day,"yyyy-MM-dd");
		int typeId=new BillTypeServiceImpl().getTypeIdByName(typeName);
		new BillItemServiceImpl().insertBillItem(num, note, date, year, month, day, typeId, userId);
		doGet(request, response);
	}
	public static Date stringToDate(String dateStr, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        try {
            return formatter.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }

}
