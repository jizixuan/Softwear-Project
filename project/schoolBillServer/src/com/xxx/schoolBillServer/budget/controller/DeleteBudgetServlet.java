package com.xxx.schoolBillServer.budget.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xxx.schoolBillServer.budget.service.BudgetServiceImpl;

/**
 * Servlet implementation class DeleteBudgetServlet
 */
@WebServlet("/DeleteBudgetServlet")
public class DeleteBudgetServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteBudgetServlet() {
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
		String id = request.getParameter("id");
		BudgetServiceImpl budgetServiceImpl = new BudgetServiceImpl();
		boolean tag = budgetServiceImpl.deleteBudget(Integer.parseInt(id));
		PrintWriter writer = response.getWriter();
		if(tag) {
			writer.write("1");
		}else {
			writer.write("2");
		}
		doGet(request, response);
	}

}
