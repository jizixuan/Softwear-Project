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
 * Servlet implementation class DeleteBillItemServlet
 */
@WebServlet("/DeleteBillItemServlet")
public class DeleteBillItemServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteBillItemServlet() {
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
		int id=Integer.parseInt(request.getParameter("id"));
		int userId=Integer.parseInt(request.getParameter("userId"));
		boolean b=new BillItemServiceImpl().deleteBill(id, userId);
		PrintWriter w = response.getWriter();
		if(b) {
			w.write("³É¹¦");
		}else {
			w.write("Ê§°Ü");
		}
		doGet(request, response);
	}

}
