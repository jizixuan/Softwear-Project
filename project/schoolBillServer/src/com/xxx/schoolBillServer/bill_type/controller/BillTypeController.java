package com.xxx.schoolBillServer.bill_type.controller;

import java.io.File;
import java.text.SimpleDateFormat;
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

import com.xxx.schoolBillServer.bill_item.service.BillItemServiceImpl;
import com.xxx.schoolBillServer.bill_type.service.BillTypeServiceImpl;
import com.xxx.schoolBillServer.entity.BillItem;
import com.xxx.schoolBillServer.entity.BillType;

@Controller
public class BillTypeController {
	
	@Resource
	private BillTypeServiceImpl billTypeServiceImpl;
	
	@RequestMapping(value = "GetBillTypeListByNumType", method=RequestMethod.POST,produces = "application/json; charset=utf-8")
	@ResponseBody 
	public String getTypeListByNumType(HttpServletRequest request,@RequestParam("numType") String numType) {
		List<BillType> billTypes=billTypeServiceImpl.getTypeListByNumType(numType);
		JSONArray jsonArray=new JSONArray();
		for(BillType billType:billTypes) {
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("name", billType.getName());
			jsonObject.put("id", billType.getId());
			jsonObject.put("img", billType.getImg());
			jsonObject.put("numType", billType.getNumType());
			jsonArray.put(jsonObject);
		}
		return jsonArray.toString();
	}
	
	//WelcomeActivity
	@RequestMapping(value = "GetBillTypeList", method=RequestMethod.POST,produces = "application/json; charset=utf-8")
	@ResponseBody 
	public String getTypeList(HttpServletRequest request) {
		List<BillType> billTypes=billTypeServiceImpl.getTypeList();
		JSONArray jsonArray=new JSONArray();
		for(BillType billType:billTypes) {
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("name", billType.getName());
			jsonObject.put("id", billType.getId());
			jsonObject.put("img", billType.getImg());
			jsonObject.put("numType", billType.getNumType());
			jsonArray.put(jsonObject);
		}
		System.out.println(jsonArray.toString());
		return jsonArray.toString();
	}
	
	

}
