package com.xxx.schoolBillServer.bill_item.controller;

import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.xxx.schoolBillServer.bill_item.service.BillItemServiceImpl;
import com.xxx.schoolBillServer.bill_type.service.BillTypeServiceImpl;
import com.xxx.schoolBillServer.entity.BillItem;
import com.xxx.schoolBillServer.entity.BillMonth;
import com.xxx.schoolBillServer.entity.BillType;

@Controller
public class BillItemController {
	
	@Resource
	private BillItemServiceImpl billItemServiceImpl;
	@Resource
	private BillTypeServiceImpl billTypeServiceImpl;
	
	@RequestMapping(value = "InsertBillItemServlet", method=RequestMethod.POST)
	@ResponseBody 
	public int insertBillItem(@RequestParam("num") String numValue, @RequestParam("date") String dateValue,
			@RequestParam("note") String note,@RequestParam("typeName") String typeName,@RequestParam("userId") String userIdValue) {
		int userId=Integer.parseInt(userIdValue);
		Double num=Double.parseDouble(numValue);
		int year=Integer.parseInt(dateValue.split("/")[0]);
		int month=Integer.parseInt(dateValue.split("/")[1]);
		int day=Integer.parseInt(dateValue.split("/")[2]);
		Date date=stringToDate(year+"-"+month+"-"+day,"yyyy-MM-dd");
		int typeId=billTypeServiceImpl.getTypeIdByName(typeName);
		BillItem billItem=new BillItem();
		billItem.setDate(date);
		billItem.setDay(day);
		billItem.setMonth(month);
		billItem.setUserId(userId);
		billItem.setYear(year);
		billItem.setNum(num);
		billItem.setNote(note);
		billItem.setTypeId(typeId);
		int id=billItemServiceImpl.insertBillItem(billItem);
		return id;
	}
	public static Date stringToDate(String dateStr, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        try {
            return formatter.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }
	
	@RequestMapping(value = "GetBillItemListByDateServlet", method=RequestMethod.POST,produces = "application/json; charset=utf-8")
	@ResponseBody
	public String GetBillItemListByDateServlet(@RequestParam("year") String yearV, @RequestParam("month") String monthV,@RequestParam("userId") String userIdV) {
		int year=Integer.parseInt(yearV);
		int month=Integer.parseInt(monthV);
		int userId=Integer.parseInt(userIdV);
		List<BillItem> billItems=billItemServiceImpl.getBillItemListByDate(year, month, userId);
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
		System.out.println(jsonArray.toString());
		return jsonArray.toString();
	}
	
	@RequestMapping(value = "DeleteBillItemServlet", method=RequestMethod.POST,produces = "application/json; charset=utf-8")
	@ResponseBody
	public String GetBillItemListByDateServlet(@RequestParam("id") String idValue, @RequestParam("userId") String userIdValue) {
		int id=Integer.parseInt(idValue);
		int userId=Integer.parseInt(userIdValue);
		boolean b=billItemServiceImpl.deleteBill(id, userId);
		if(b) {
			return "成功";
		}else {
			return "失败";
		}
	}
	
	@RequestMapping(value = "GetBillItemListOrderByNum", method=RequestMethod.POST,produces = "application/json; charset=utf-8")
	@ResponseBody
	public String GetBillItemListOrderByNum(@RequestParam("year") String yearV, @RequestParam("month") String monthV,@RequestParam("userId") String userIdV) {
		int year=Integer.parseInt(yearV);
		int month=Integer.parseInt(monthV);
		int userId=Integer.parseInt(userIdV);
		List<BillItem> billItems=billItemServiceImpl.getBillItemListOrderByNum(year, month, userId);
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
		System.out.println(jsonArray.toString());
		return jsonArray.toString();
	}
	
	@RequestMapping(value = "GetBillMonthListByYearServlet", method=RequestMethod.POST,produces = "application/json; charset=utf-8")
	@ResponseBody 
	public String GetBillMonthListByYearServlet(@RequestParam("year") String yearV, @RequestParam("id") String idV) {
		int year=Integer.parseInt(yearV);
		int id=Integer.parseInt(idV);
		List<BillMonth> billMonths = new ArrayList<>();
		billMonths = billItemServiceImpl.getBillMonthListByYear(year,id);
		Gson gson = new Gson();
		String json = gson.toJson(billMonths);
		System.out.println(json);
		return json;
	}
	
	@RequestMapping(value = "GetBillNumServlet", method=RequestMethod.POST,produces = "application/json; charset=utf-8")
	@ResponseBody 
	public String GetBillNumServlet(@RequestParam("month") String month, @RequestParam("id") String id) {
		int a = billItemServiceImpl.getBillNum(month,id);
		System.out.println(a+"");
		String aString = ""+a;
		return aString;
	}
	
	@RequestMapping(value = "UpdateBillItemServlet", method=RequestMethod.POST,produces = "application/json; charset=utf-8")
	@ResponseBody 
	public String UpdateBillItemServlet(@RequestParam("num") String numV, @RequestParam("note") String note,@RequestParam("date") String dateValue
			,@RequestParam("typeName") String typeName, @RequestParam("id") String idV, @RequestParam("userId") String userIdV) {
		Double num=Double.parseDouble(numV);
		int id=Integer.parseInt(idV);
		int userId=Integer.parseInt(userIdV);
		int year=Integer.parseInt(dateValue.split("/")[0]);
		int month=Integer.parseInt(dateValue.split("/")[1]);
		int day=Integer.parseInt(dateValue.split("/")[2]);
		Date date=stringToDate(year+"-"+month+"-"+day,"yyyy-MM-dd");
		int typeId=billTypeServiceImpl.getTypeIdByName(typeName);
		BillItem billItem=new BillItem();
		billItem.setDate(date);
		billItem.setId(id);
		billItem.setTypeId(typeId);
		billItem.setNote(note);
		billItem.setDay(day);
		billItem.setMonth(month);
		billItem.setYear(year);
		billItem.setNum(num);
		if(billItemServiceImpl.updateBill(billItem, userId)) {
			System.out.println(typeId);
			return typeId+"";
			
		}else {
			System.out.println("失败");
			return "失败";
		}
	}
	

}
