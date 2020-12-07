package com.xxx.schoolBillServer.bill_item.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xxx.schoolBillServer.bill_item.service.BillItemServiceImpl;
import com.xxx.schoolBillServer.bill_type.service.BillTypeServiceImpl;
import com.xxx.schoolBillServer.entity.BillItem;

/**
 * Servlet implementation class UpdateBillItemServlet
 */
@WebServlet("/UpdateBillItemServlet")
public class UpdateBillItemServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateBillItemServlet() {
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
		int id=Integer.parseInt(request.getParameter("id"));
		int userId=Integer.parseInt(request.getParameter("userId"));
		int year=Integer.parseInt(dateValue.split("/")[0]);
		int month=Integer.parseInt(dateValue.split("/")[1]);
		int day=Integer.parseInt(dateValue.split("/")[2]);
		Date date=stringToDate(year+"-"+month+"-"+day,"yyyy-MM-dd");
		int typeId=new BillTypeServiceImpl().getTypeIdByName(typeName);
		BillItem billItem=new BillItem();
		billItem.setDate(date);
		billItem.setId(id);
		billItem.setTypeId(typeId);
		billItem.setNote(note);
		billItem.setDay(day);
		billItem.setMonth(month);
		billItem.setYear(year);
		billItem.setNum(num);
		PrintWriter w = response.getWriter();
		if(new BillItemServiceImpl().updateBill(billItem, userId)) {
			w.write(typeId+"");
		}else {
			w.write("Ê§°Ü");
		}
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
