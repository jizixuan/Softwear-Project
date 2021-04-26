package com.xxx.schoolBillServer.icon.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xxx.schoolBillServer.bill_type.service.BillTypeServiceImpl;
import com.xxx.schoolBillServer.entity.BillItem;
import com.xxx.schoolBillServer.entity.BillType;
import com.xxx.schoolBillServer.icon.service.IconItemServiceImpl;
import com.xxx.schoolBillServer.util.ComparatorDate;
import com.xxx.schoolBillServer.util.ComparatorMonth;

@Controller
public class IconController {

	@Resource
	private IconItemServiceImpl iconItemServiceImpl;
	@Resource
	private BillTypeServiceImpl billTypeServiceImpl;
	@RequestMapping(value = "IconItemGetAllNumByYearOut", method=RequestMethod.POST,produces = "application/json; charset=utf-8")
	@ResponseBody
	public String getIconItemGetAllNumByYearOut(HttpServletRequest request,@RequestParam int userId,
			@RequestParam String first,@RequestParam String last){
		Date first1 = null,last1 = null;
		try {
			first1 = new SimpleDateFormat("yyyy-MM-dd").parse(first);
			last1 = new SimpleDateFormat("yyyy-MM-dd").parse(last);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<BillItem> billItems1=iconItemServiceImpl.getIconItemByDate(userId,first1,last1);
		List<BillItem> billItems = new ArrayList();
		List<BillType> billTypes = billTypeServiceImpl.getTypeList();
		JSONArray jsonArray=new JSONArray();
		for(BillItem billItem:billItems1) {
			BillType billType = billTypes.get(billItem.getTypeId()-1);
			if(billType.getNumType().equals("+")) {
				billItems.add(billItem);
			}
		}
	    for (int i = 0; i < billItems.size(); i++) {
	    	for (int j = 0; j < billItems.size(); ) {
	    		if (i != j && billItems.get(j).getMonth()==billItems.get(i).getMonth()) {
	    			billItems.get(i).setNum(billItems.get(i).getNum() + billItems.get(j).getNum());
	    			billItems.remove(j);
	    		} else {
	    			j++;
	    		}
	    	}
	    }

	    List<BillItem> list = new ArrayList<BillItem>();
		for(int i = 0; i < 12; i ++){
			BillItem b = new BillItem();
	        b.setMonth(i+1);
	        b.setNum(0);
	        list.add(b);
		}
		
        billItems.addAll(list);
        System.out.println("addyear"+billItems.toString());
        System.out.println("addyear"+list.toString());
        
        for (int i = 0; i < billItems.size(); i++) {
	    	for (int j = 0; j < billItems.size(); ) {
	    		if (i != j && billItems.get(j).getMonth()==billItems.get(i).getMonth()) {
	    			billItems.get(i).setNum(billItems.get(i).getNum() + billItems.get(j).getNum());
	    			System.out.println("jj"+billItems.get(i));
	    			billItems.remove(j);
	    		} else {
	    			j++;
	    		}
	    	}
	    }
        ComparatorMonth c = new ComparatorMonth();
        Collections.sort(billItems,c);
        for(BillItem billItem:billItems) {
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("num", billItem.getNum());
			jsonObject.put("month", billItem.getMonth()+"月");
//			jsonObject.put("date",new SimpleDateFormat("yyyy-MM-dd").format(billItem.getDate()));
			jsonArray.put(jsonObject);
        }
        System.out.println("IconItemGetAllNumByYearOut"+jsonArray.toString());
        return jsonArray.toString();
	}
	
	@RequestMapping(value = "IconItemGetAllNumByMonthOut", method=RequestMethod.POST,produces = "application/json; charset=utf-8")
	@ResponseBody
	public String getIconItemGetAllNumByMounthOut(HttpServletRequest request,@RequestParam int userId,
			@RequestParam String first,@RequestParam String last){
		Date first1 = null,last1 = null;
		try {
			first1 = new SimpleDateFormat("yyyy-MM-dd").parse(first);
			last1 = new SimpleDateFormat("yyyy-MM-dd").parse(last);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<BillItem> billItems1=iconItemServiceImpl.getIconItemByDate(userId,first1,last1);
		List<BillItem> billItems = new ArrayList();
		List<BillType> billTypes = billTypeServiceImpl.getTypeList();
		JSONArray jsonArray=new JSONArray();
		for(BillItem billItem:billItems1) {
			BillType billType = billTypes.get(billItem.getTypeId()-1);
			if(billType.getNumType().equals("+")) {
				billItems.add(billItem);
			}
		}
	    for (int i = 0; i < billItems.size(); i++) {
	    	for (int j = 0; j < billItems.size(); ) {
	    		if (i != j && billItems.get(j).getDate().equals(billItems.get(i).getDate())) {
	    			billItems.get(i).setNum(billItems.get(i).getNum() + billItems.get(j).getNum());
	    			billItems.remove(j);
	    		} else {
	    			j++;
	    		}
	    	}
	    }


	    List<BillItem> list = new ArrayList<BillItem>();
        Calendar aCalendar = Calendar.getInstance(Locale.CHINA);
        int year = aCalendar.get(Calendar.YEAR);//年份
        int month = aCalendar.get(Calendar.MONTH) + 1;//月份
        int day = aCalendar.getActualMaximum(Calendar.DATE);
        for (int i = 1; i <= day; i++) {
            String aDate = String.valueOf(year)+"-"+month+"-"+i;
            BillItem b = new BillItem();
            try {
				b.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(aDate));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            b.setDay(i);
            b.setNum(0);
            list.add(b);
        }
        
        billItems.addAll(list);
        System.out.println("addMonth"+billItems.toString());
        System.out.println("addMonth"+list.toString());
        for (int i = 0; i < billItems.size(); i++) {
	    	for (int j = 0; j < billItems.size(); ) {
	    		if (i != j && new SimpleDateFormat("yyyy-MM-dd").format(billItems.get(j).getDate()).equals(new SimpleDateFormat("yyyy-MM-dd").format(billItems.get(i).getDate()))) {
	    			billItems.get(i).setNum(billItems.get(i).getNum() + billItems.get(j).getNum());
	    			System.out.println("jj"+billItems.get(i));
	    			billItems.remove(j);
	    		} else {
	    			j++;
	    		}
	    	}
	    }
        ComparatorDate c = new ComparatorDate();
        Collections.sort(billItems,c);
        for(BillItem billItem:billItems) {
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("num", billItem.getNum());
			jsonObject.put("date", new SimpleDateFormat("yyyy-MM-dd").format(billItem.getDate()));
			jsonArray.put(jsonObject);
        }
        System.out.println("IconItemGetAllNumByMonthOut"+jsonArray.toString());
        return jsonArray.toString();
	}
	
	@RequestMapping(value = "IconItemGetAllByWeekOut", method=RequestMethod.POST,produces = "application/json; charset=utf-8")
	@ResponseBody
	public String getIconItemGetAllByWeekOut(HttpServletRequest request,@RequestParam int userId,
			@RequestParam String first,@RequestParam String last){
		Date first1 = null,last1 = null;
		try {
			first1 = new SimpleDateFormat("yyyy-MM-dd").parse(first);
			last1 = new SimpleDateFormat("yyyy-MM-dd").parse(last);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<BillItem> billItems1=iconItemServiceImpl.getIconItemByDate(userId,first1,last1);
		List<BillItem> billItems = new ArrayList();
		List<BillType> billTypes = billTypeServiceImpl.getTypeList();
		JSONArray jsonArray=new JSONArray();
		for(BillItem billItem:billItems1) {
			BillType billType = billTypes.get(billItem.getTypeId()-1);
			if(billType.getNumType().equals("+")) {
				billItems.add(billItem);
			}
		}
	    for (int i = 0; i < billItems.size(); i++) {
	    	for (int j = 0; j < billItems.size(); ) {
	    		if (i != j && billItems.get(j).getDate().equals(billItems.get(i).getDate())) {
	    			billItems.get(i).setNum(billItems.get(i).getNum() + billItems.get(j).getNum());
	    			billItems.remove(j);
	    		} else {
	    			j++;
	    		}
	    	}
	    }
	    Calendar calendar = Calendar.getInstance();
        // 获取本周的第一天
        int firstDayOfWeek = calendar.getFirstDayOfWeek();
        List<BillItem> list = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            calendar.set(Calendar.DAY_OF_WEEK, firstDayOfWeek + i);
            // 获取星期的显示名称，例如：周一、星期一、Monday等等
            String day = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
            BillItem b = new BillItem();
            try {
				b.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(day));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            b.setDay(Integer.parseInt(new SimpleDateFormat("d").format(calendar.getTime())));
            b.setNum(0);
            list.add(b);
        }
        billItems.addAll(list);
        System.out.println("add"+billItems.toString());
        System.out.println("add"+list.toString());
        for (int i = 0; i < billItems.size(); i++) {
	    	for (int j = 0; j < billItems.size(); ) {
	    		if (i != j && new SimpleDateFormat("yyyy-MM-dd").format(billItems.get(j).getDate()).equals(new SimpleDateFormat("yyyy-MM-dd").format(billItems.get(i).getDate()))) {
	    			billItems.get(i).setNum(billItems.get(i).getNum() + billItems.get(j).getNum());
	    			System.out.println("jj"+billItems.get(i));
	    			billItems.remove(j);
	    		} else {
	    			j++;
	    		}
	    	}
	    }
        ComparatorDate c = new ComparatorDate();
        Collections.sort(billItems,c);
        for(BillItem billItem:billItems) {
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("num", billItem.getNum());
			jsonObject.put("date", new SimpleDateFormat("yyyy-MM-dd").format(billItem.getDate()));
			jsonArray.put(jsonObject);
        }
        return jsonArray.toString();
	}
	
	@RequestMapping(value = "IconItemGetAllNumByYear", method=RequestMethod.POST,produces = "application/json; charset=utf-8")
	@ResponseBody
	public String getIconItemGetAllNumByYear(HttpServletRequest request,@RequestParam int userId,
			@RequestParam String first,@RequestParam String last){
		Date first1 = null,last1 = null;
		try {
			first1 = new SimpleDateFormat("yyyy-MM-dd").parse(first);
			last1 = new SimpleDateFormat("yyyy-MM-dd").parse(last);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<BillItem> billItems1=iconItemServiceImpl.getIconItemByDate(userId,first1,last1);
		List<BillItem> billItems = new ArrayList();
		List<BillType> billTypes = billTypeServiceImpl.getTypeList();
		JSONArray jsonArray=new JSONArray();
		for(BillItem billItem:billItems1) {
			BillType billType = billTypes.get(billItem.getTypeId()-1);
			if(billType.getNumType().equals("-")) {
				billItems.add(billItem);
			}
		}
	    for (int i = 0; i < billItems.size(); i++) {
	    	for (int j = 0; j < billItems.size(); ) {
	    		if (i != j && billItems.get(j).getMonth()==billItems.get(i).getMonth()) {
	    			billItems.get(i).setNum(billItems.get(i).getNum() + billItems.get(j).getNum());
	    			billItems.remove(j);
	    		} else {
	    			j++;
	    		}
	    	}
	    }

	    List<BillItem> list = new ArrayList<BillItem>();
		for(int i = 0; i < 12; i ++){
			BillItem b = new BillItem();
	        b.setMonth(i+1);
	        b.setNum(0);
	        list.add(b);
		}
		
        billItems.addAll(list);
        System.out.println("addyear"+billItems.toString());
        System.out.println("addyear"+list.toString());
        
        for (int i = 0; i < billItems.size(); i++) {
	    	for (int j = 0; j < billItems.size(); ) {
	    		if (i != j && billItems.get(j).getMonth()==billItems.get(i).getMonth()) {
	    			billItems.get(i).setNum(billItems.get(i).getNum() + billItems.get(j).getNum());
	    			System.out.println("jj"+billItems.get(i));
	    			billItems.remove(j);
	    		} else {
	    			j++;
	    		}
	    	}
	    }
        ComparatorMonth c = new ComparatorMonth();
        Collections.sort(billItems,c);
        for(BillItem billItem:billItems) {
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("num", billItem.getNum());
			jsonObject.put("month", billItem.getMonth()+"月");
//			jsonObject.put("date",new SimpleDateFormat("yyyy-MM-dd").format(billItem.getDate()));
			jsonArray.put(jsonObject);
        }
		return jsonArray.toString();
	}
	
	@RequestMapping(value = "IconItemGetAllNumByWeek", method=RequestMethod.POST,produces = "application/json; charset=utf-8")
	@ResponseBody
	public String getIconItemGetAllNumByWeek(HttpServletRequest request,@RequestParam int userId,
			@RequestParam String first,@RequestParam String last){
		Date first1 = null,last1 = null;
		try {
			first1 = new SimpleDateFormat("yyyy-MM-dd").parse(first);
			last1 = new SimpleDateFormat("yyyy-MM-dd").parse(last);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<BillItem> billItems1=iconItemServiceImpl.getIconItemByDate(userId,first1,last1);
		List<BillItem> billItems = new ArrayList();
		List<BillType> billTypes = billTypeServiceImpl.getTypeList();
		JSONArray jsonArray=new JSONArray();
		for(BillItem billItem:billItems1) {
			BillType billType = billTypes.get(billItem.getTypeId()-1);
			if(billType.getNumType().equals("-")) {
				billItems.add(billItem);
			}
		}
	    for (int i = 0; i < billItems.size(); i++) {
	    	for (int j = 0; j < billItems.size(); ) {
	    		if (i != j && billItems.get(j).getDate().equals(billItems.get(i).getDate())) {
	    			billItems.get(i).setNum(billItems.get(i).getNum() + billItems.get(j).getNum());
	    			billItems.remove(j);
	    		} else {
	    			j++;
	    		}
	    	}
	    }
	    Calendar calendar = Calendar.getInstance();
        // 获取本周的第一天
        int firstDayOfWeek = calendar.getFirstDayOfWeek();
        List<BillItem> list = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            calendar.set(Calendar.DAY_OF_WEEK, firstDayOfWeek + i);
            // 获取星期的显示名称，例如：周一、星期一、Monday等等
            String day = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
            BillItem b = new BillItem();
            try {
				b.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(day));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            b.setDay(Integer.parseInt(new SimpleDateFormat("d").format(calendar.getTime())));
            b.setNum(0);
            list.add(b);
        }
        billItems.addAll(list);
        System.out.println("add"+billItems.toString());
        System.out.println("add"+list.toString());
        for (int i = 0; i < billItems.size(); i++) {
	    	for (int j = 0; j < billItems.size(); ) {
	    		if (i != j && new SimpleDateFormat("yyyy-MM-dd").format(billItems.get(j).getDate()).equals(new SimpleDateFormat("yyyy-MM-dd").format(billItems.get(i).getDate()))) {
	    			billItems.get(i).setNum(billItems.get(i).getNum() + billItems.get(j).getNum());
	    			System.out.println("jj"+billItems.get(i));
	    			billItems.remove(j);
	    		} else {
	    			j++;
	    		}
	    	}
	    }
        ComparatorDate c = new ComparatorDate();
        Collections.sort(billItems,c);
        for(BillItem billItem:billItems) {
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("num", billItem.getNum());
			jsonObject.put("date", new SimpleDateFormat("yyyy-MM-dd").format(billItem.getDate()));
			jsonArray.put(jsonObject);
        }
		return jsonArray.toString();
	}
	
	@RequestMapping(value = "IconItemGetAllNumByMonth", method=RequestMethod.POST,produces = "application/json; charset=utf-8")
	@ResponseBody
	public String getIconItemGetAllNumByMonth(HttpServletRequest request,@RequestParam int userId,
			@RequestParam String first,@RequestParam String last){
		Date first1 = null,last1 = null;
		try {
			first1 = new SimpleDateFormat("yyyy-MM-dd").parse(first);
			last1 = new SimpleDateFormat("yyyy-MM-dd").parse(last);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<BillItem> billItems1=iconItemServiceImpl.getIconItemByDate(userId,first1,last1);
		List<BillItem> billItems = new ArrayList();
		List<BillType> billTypes = billTypeServiceImpl.getTypeList();
		JSONArray jsonArray=new JSONArray();
		for(BillItem billItem:billItems1) {
			BillType billType = billTypes.get(billItem.getTypeId()-1);
			if(billType.getNumType().equals("-")) {
				billItems.add(billItem);
			}
		}
	    for (int i = 0; i < billItems.size(); i++) {
	    	for (int j = 0; j < billItems.size(); ) {
	    		if (i != j && billItems.get(j).getDate().equals(billItems.get(i).getDate())) {
	    			billItems.get(i).setNum(billItems.get(i).getNum() + billItems.get(j).getNum());
	    			billItems.remove(j);
	    		} else {
	    			j++;
	    		}
	    	}
	    }


	    List<BillItem> list = new ArrayList<BillItem>();
        Calendar aCalendar = Calendar.getInstance(Locale.CHINA);
        int year = aCalendar.get(Calendar.YEAR);//年份
        int month = aCalendar.get(Calendar.MONTH) + 1;//月份
        int day = aCalendar.getActualMaximum(Calendar.DATE);
        for (int i = 1; i <= day; i++) {
            String aDate = String.valueOf(year)+"-"+month+"-"+i;
            BillItem b = new BillItem();
            try {
				b.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(aDate));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            b.setDay(i);
            b.setNum(0);
            list.add(b);
        }
        
        billItems.addAll(list);
        System.out.println("addMonth"+billItems.toString());
        System.out.println("addMonth"+list.toString());
        for (int i = 0; i < billItems.size(); i++) {
	    	for (int j = 0; j < billItems.size(); ) {
	    		if (i != j && new SimpleDateFormat("yyyy-MM-dd").format(billItems.get(j).getDate()).equals(new SimpleDateFormat("yyyy-MM-dd").format(billItems.get(i).getDate()))) {
	    			billItems.get(i).setNum(billItems.get(i).getNum() + billItems.get(j).getNum());
	    			System.out.println("jj"+billItems.get(i));
	    			billItems.remove(j);
	    		} else {
	    			j++;
	    		}
	    	}
	    }
        ComparatorDate c = new ComparatorDate();
        Collections.sort(billItems,c);
        for(BillItem billItem:billItems) {
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("num", billItem.getNum());
			jsonObject.put("date", new SimpleDateFormat("yyyy-MM-dd").format(billItem.getDate()));
			jsonArray.put(jsonObject);
        }
		return jsonArray.toString();
	}
	
	@RequestMapping(value = "IconItemGetAllNumByMonthDetial", method=RequestMethod.POST,produces = "application/json; charset=utf-8")
	@ResponseBody
	public String getIconItemListByTypeIdOrderByDay(HttpServletRequest request,@RequestParam int userId,
			@RequestParam String first,@RequestParam String last,@RequestParam String type){
		int typeId=billTypeServiceImpl.getTypeIdByName(type);
		Date first1 = null,last1 = null;
		try {
			first1 = new SimpleDateFormat("yyyy-MM-dd").parse(first);
			last1 = new SimpleDateFormat("yyyy-MM-dd").parse(last);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<BillItem> billItems=iconItemServiceImpl.getIconItemListByTypeIdOrderByDay(typeId,userId,first1,last1);
		JSONArray jsonArray=new JSONArray();
		List<BillItem> list = new ArrayList<BillItem>();
        Calendar aCalendar = Calendar.getInstance(Locale.CHINA);
        int year = aCalendar.get(Calendar.YEAR);//年份
        int month = aCalendar.get(Calendar.MONTH) + 1;//月份
        int day = aCalendar.getActualMaximum(Calendar.DATE);
        for (int i = 1; i <= day; i++) {
            String aDate = String.valueOf(year)+"-"+month+"-"+i;
            BillItem b = new BillItem();
            try {
				b.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(aDate));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            b.setDay(i);
            b.setNum(0);
            list.add(b);
        }
        
        billItems.addAll(list);
        System.out.println("addMonth"+billItems.toString());
        System.out.println("addMonth"+list.toString());
        for (int i = 0; i < billItems.size(); i++) {
	    	for (int j = 0; j < billItems.size(); ) {
	    		if (i != j && new SimpleDateFormat("yyyy-MM-dd").format(billItems.get(j).getDate()).equals(new SimpleDateFormat("yyyy-MM-dd").format(billItems.get(i).getDate()))) {
	    			billItems.get(i).setNum(billItems.get(i).getNum() + billItems.get(j).getNum());
	    			System.out.println("jj"+billItems.get(i));
	    			billItems.remove(j);
	    		} else {
	    			j++;
	    		}
	    	}
	    }
        ComparatorDate c = new ComparatorDate();
        Collections.sort(billItems,c);
        for(BillItem billItem:billItems) {
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("num", billItem.getNum());
			jsonObject.put("date", new SimpleDateFormat("yyyy-MM-dd").format(billItem.getDate()));
			jsonArray.put(jsonObject);
        }
		return jsonArray.toString();
	}
	
	@RequestMapping(value = "IconItemGetNumByYearDetial", method=RequestMethod.POST,produces = "application/json; charset=utf-8")
	@ResponseBody
	public String getIconItemGetNumByYearDetial(HttpServletRequest request,@RequestParam int userId,
			@RequestParam String first,@RequestParam String last,@RequestParam String type){
		int typeId=billTypeServiceImpl.getTypeIdByName(type);
		Date first1 = null,last1 = null;
		try {
			first1 = new SimpleDateFormat("yyyy-MM-dd").parse(first);
			last1 = new SimpleDateFormat("yyyy-MM-dd").parse(last);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<BillItem> billItems=iconItemServiceImpl.getIconItemListByTypeIdOrderByDay(typeId,userId,first1,last1);
		JSONArray jsonArray=new JSONArray();
		
		 List<BillItem> list = new ArrayList<BillItem>();
			for(int i = 0; i < 12; i ++){
				BillItem b = new BillItem();
		        b.setMonth(i+1);
		        b.setNum(0);
		        list.add(b);
			}
			
	        billItems.addAll(list);
	        System.out.println("addyear"+billItems.toString());
	        System.out.println("addyear"+list.toString());
	        
	        for (int i = 0; i < billItems.size(); i++) {
		    	for (int j = 0; j < billItems.size(); ) {
		    		if (i != j && billItems.get(j).getMonth()==billItems.get(i).getMonth()) {
		    			billItems.get(i).setNum(billItems.get(i).getNum() + billItems.get(j).getNum());
		    			System.out.println("jj"+billItems.get(i));
		    			billItems.remove(j);
		    		} else {
		    			j++;
		    		}
		    	}
		    }
	        ComparatorMonth c = new ComparatorMonth();
	        Collections.sort(billItems,c);
	        for(BillItem billItem:billItems) {
				JSONObject jsonObject=new JSONObject();
				jsonObject.put("num", billItem.getNum());
				jsonObject.put("month", billItem.getMonth()+"月");
//				jsonObject.put("date",new SimpleDateFormat("yyyy-MM-dd").format(billItem.getDate()));
				jsonArray.put(jsonObject);
	        }
		return jsonArray.toString();
	}
	
	@RequestMapping(value = "IconItemGetAllNumByWeekDetial", method=RequestMethod.POST,produces = "application/json; charset=utf-8")
	@ResponseBody
	public String getIconItemGetAllNumByWeekDetial(HttpServletRequest request,@RequestParam int userId,
			@RequestParam String first,@RequestParam String last,@RequestParam String type){
		int typeId=billTypeServiceImpl.getTypeIdByName(type);
		Date first1 = null,last1 = null;
		try {
			first1 = new SimpleDateFormat("yyyy-MM-dd").parse(first);
			last1 = new SimpleDateFormat("yyyy-MM-dd").parse(last);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<BillItem> billItems=iconItemServiceImpl.getIconItemListByTypeIdOrderByDay(typeId,userId,first1,last1);
		JSONArray jsonArray=new JSONArray();
		
		Calendar calendar = Calendar.getInstance();
        // 获取本周的第一天
        int firstDayOfWeek = calendar.getFirstDayOfWeek();
        List<BillItem> list = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            calendar.set(Calendar.DAY_OF_WEEK, firstDayOfWeek + i);
            // 获取星期的显示名称，例如：周一、星期一、Monday等等
            String day = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
            BillItem b = new BillItem();
            try {
				b.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(day));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            b.setDay(Integer.parseInt(new SimpleDateFormat("d").format(calendar.getTime())));
            b.setNum(0);
            list.add(b);
        }
        billItems.addAll(list);
        System.out.println("add"+billItems.toString());
        System.out.println("add"+list.toString());
        for (int i = 0; i < billItems.size(); i++) {
	    	for (int j = 0; j < billItems.size(); ) {
	    		if (i != j && new SimpleDateFormat("yyyy-MM-dd").format(billItems.get(j).getDate()).equals(new SimpleDateFormat("yyyy-MM-dd").format(billItems.get(i).getDate()))) {
	    			billItems.get(i).setNum(billItems.get(i).getNum() + billItems.get(j).getNum());
	    			System.out.println("jj"+billItems.get(i));
	    			billItems.remove(j);
	    		} else {
	    			j++;
	    		}
	    	}
	    }
        ComparatorDate c = new ComparatorDate();
        Collections.sort(billItems,c);
        for(BillItem billItem:billItems) {
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("num", billItem.getNum());
			jsonObject.put("date", new SimpleDateFormat("yyyy-MM-dd").format(billItem.getDate()));
			jsonArray.put(jsonObject);
        }
		return jsonArray.toString();
	}
	
	@RequestMapping(value = "IconItemGetByDateServlet", method=RequestMethod.POST,produces = "application/json; charset=utf-8")
	@ResponseBody
	public String getIconItemGetByDateServlet(HttpServletRequest request,@RequestParam int userId,
			@RequestParam String first,@RequestParam String last ) {
		Date first1 = null,last1 = null;
		try {
			first1 = new SimpleDateFormat("yyyy-MM-dd").parse(first);
			last1 = new SimpleDateFormat("yyyy-MM-dd").parse(last);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<BillItem> billItems=iconItemServiceImpl.getIconItemByDate(userId,first1,last1);
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
			jsonObject.put("date", new SimpleDateFormat("yyyy-MM-dd").format(billItem.getDate()));
			jsonObject.put("typeId", billItem.getTypeId());
			jsonArray.put(jsonObject);
		}
		if(jsonArray.length()==0) {
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("day", 18);
			jsonObject.put("month",4);
			jsonObject.put("year", 2021);
			jsonObject.put("num", 85.0);
			jsonObject.put("note", "demo");
			jsonObject.put("date", "2021-4-18");
			jsonObject.put("typeId", 5);
			jsonArray.put(jsonObject);
		}
		System.out.println("IconItemGetByDateServlet");
		System.out.println(jsonArray.toString());
		return jsonArray.toString();
	}
	
	@RequestMapping(value = "IconItemGetByTypeId", method=RequestMethod.POST,produces = "application/json; charset=utf-8")
	@ResponseBody
	public String getIconItemGetByTypeId(HttpServletRequest request,@RequestParam String type) {
		int typeId=billTypeServiceImpl.getTypeIdByName(type);
		System.out.println(typeId);
		List<BillItem> billItems=iconItemServiceImpl.getIconItemListByTypeId(typeId);
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
		return jsonArray.toString();
	}
}
