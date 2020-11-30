package com.xxx.schoolBillServer.bill_item.service;

import java.util.Date;
import java.util.List;

import com.xxx.schoolBillServer.bill_item.dao.BillItemDaoImpl;
import com.xxx.schoolBillServer.entity.BillItem;

public class BillItemServiceImpl {
	public BillItemDaoImpl billItemDaoImpl=new BillItemDaoImpl();
	public void insertBillItem(Double num,String note,Date date,int year,int month,int day,int typeId,int userId){
		billItemDaoImpl.insertBillItem(num, note, date, year, month, day, typeId, userId);
	}
	public List<BillItem> getBillItemListByDate(int year,int month,int userId){
		return billItemDaoImpl.getBillItemListByDate(year, month, userId);
	}
}
