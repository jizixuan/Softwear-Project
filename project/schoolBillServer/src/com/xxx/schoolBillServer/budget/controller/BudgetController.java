package com.xxx.schoolBillServer.budget.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.xxx.schoolBillServer.budget.service.BudgetServiceImpl;
import com.xxx.schoolBillServer.entity.BillItem;
import com.xxx.schoolBillServer.entity.Budget;
import com.xxx.schoolBillServer.entity.Note;
import com.xxx.schoolBillServer.note.service.NoteServiceImpl;

@Controller
public class BudgetController {
	@Resource BudgetServiceImpl serviceImpl;
	
	@RequestMapping(value = "AddBudgetServlet", method=RequestMethod.POST)
	@ResponseBody 
	public String AddBudgetServlet(HttpServletRequest request) throws IOException {
		BufferedReader reader;
		reader = request.getReader();
		String json = reader.readLine();
		Gson gson = new Gson();
		Budget budget = gson.fromJson(json, Budget.class);
		reader.close();
		if(serviceImpl.updateBudget(budget)) {			
			System.out.println("1111");
			return json;
		}else {
			System.out.println("2222");
			return "0";
		}	
	}
	

	
	@RequestMapping(value = "GetBudgetByIdServlet", method=RequestMethod.POST)
	@ResponseBody 
	public String GetBudgetByIdServlet(@RequestParam("id") String id) {
		Budget budget = serviceImpl.getBudgetById(Integer.parseInt(id));
		Gson gson = new Gson();
		String json = gson.toJson(budget);
		System.out.println("huode"+json);
		
		return json;
		
		
	}
	
	@RequestMapping(value = "UpdateBudgetServlet", method=RequestMethod.POST)
	@ResponseBody 
	public String UpdateBudgetServlet(HttpServletRequest request) throws IOException {
		BufferedReader reader;
		boolean b;
		
		reader = request.getReader();
		String json = reader.readLine();
		Gson gson = new Gson();
		Budget budget = gson.fromJson(json, Budget.class);
		reader.close();
		if(serviceImpl.updateBudget(budget)) {
		System.out.println("1111");
		System.out.println("222");
		return json;
		
		}else {
			return "0";
		}	
	}
	
	@RequestMapping(value = "DeleteBudgetServlet", method=RequestMethod.POST)
	@ResponseBody 
	public String UpdateBudgetServlet(@RequestParam("id") String id) throws IOException {
		Budget budget = serviceImpl.getBudgetById(Integer.parseInt(id));
		budget.setBudget(0.0);
		if(serviceImpl.updateBudget(budget)) {
			return "0";
		
		}else {
			
			return "1";
		}	
	}
}
