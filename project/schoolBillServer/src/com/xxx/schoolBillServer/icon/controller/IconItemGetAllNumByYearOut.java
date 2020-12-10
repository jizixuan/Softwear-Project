package com.xxx.schoolBillServer.icon.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.xxx.schoolBillServer.bill_type.service.BillTypeServiceImpl;
import com.xxx.schoolBillServer.entity.BillItem;
import com.xxx.schoolBillServer.entity.BillType;
import com.xxx.schoolBillServer.icon.service.IconItemServiceImpl;
import com.xxx.schoolBillServer.util.ComparatorMonth;

/**
 * Servlet implementation class IconItemGetAllNumByYearOut
 */
@WebServlet("/IconItemGetAllNumByYearOut")
public class IconItemGetAllNumByYearOut extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public IconItemGetAllNumByYearOut() {
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
		String firstW=request.getParameter("first");
		String lastW=request.getParameter("last");
		int userId=Integer.parseInt(request.getParameter("userId"));
		List<BillItem> billItems1=new IconItemServiceImpl().getIconItemByDate(userId,firstW,lastW);
		List<BillItem> billItems = new ArrayList();
		List<BillType> billTypes = new BillTypeServiceImpl().getTypeList();
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
			jsonObject.put("month", billItem.getMonth()+"ÔÂ");
//			jsonObject.put("date",new SimpleDateFormat("yyyy-MM-dd").format(billItem.getDate()));
			jsonArray.put(jsonObject);
        }
        PrintWriter w = response.getWriter();
		w.write(jsonArray.toString());
		System.out.println("outyear"+jsonArray.toString());
		doGet(request, response);
	}

}
