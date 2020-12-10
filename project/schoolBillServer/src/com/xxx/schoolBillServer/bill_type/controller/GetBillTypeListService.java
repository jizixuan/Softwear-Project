package com.xxx.schoolBillServer.bill_type.controller;

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
import com.xxx.schoolBillServer.entity.BillType;

/**
 * Servlet implementation class GetBillTypeListService
 */
@WebServlet("/GetBillTypeListService")
public class GetBillTypeListService extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetBillTypeListService() {
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
		PrintWriter w = response.getWriter();
		List<BillType> billTypes=new BillTypeServiceImpl().getTypeList();
		JSONArray jsonArray=new JSONArray();
		for(BillType billType:billTypes) {
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("name", billType.getName());
			jsonObject.put("id", billType.getId());
			jsonObject.put("img", billType.getImg());
			jsonObject.put("numType", billType.getNumType());
			jsonArray.put(jsonObject);
		}
		System.out.print(jsonArray.toString());
		w.write(jsonArray.toString());
		doGet(request, response);
	}

}
