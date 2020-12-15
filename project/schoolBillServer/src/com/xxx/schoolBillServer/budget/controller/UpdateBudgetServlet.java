package com.xxx.schoolBillServer.budget.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.xxx.schoolBillServer.budget.service.BudgetServiceImpl;
import com.xxx.schoolBillServer.entity.Budget;

/**
 * Servlet implementation class UpdateBudgetServlet
 */
@WebServlet("/UpdateBudgetServlet")
public class UpdateBudgetServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateBudgetServlet() {
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
	 * @see HttpServlet#doPost(HttpServletRe		response.setContentType("text/html;charset=utf-8");
quest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		BufferedReader reader = request.getReader();
		String json = reader.readLine();
		Gson gson = new Gson();
		Budget budget = gson.fromJson(json, Budget.class);
		BudgetServiceImpl budgetServiceImpl = new BudgetServiceImpl();
		PrintWriter writer = response.getWriter();
		if(budgetServiceImpl.updateBudget(budget)) {
			
			System.out.println("1111");
		}else {
			writer.write(json);
			System.out.println("222");
		}
		reader.close();
		writer.flush();
		writer.close();
		doGet(request, response);
	}

}
