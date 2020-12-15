package com.xxx.schoolBillServer.bill_item.service;

import java.util.Date;
import java.util.List;

import com.xxx.schoolBillServer.bill_item.dao.BillItemDaoImpl;
import com.xxx.schoolBillServer.entity.BillItem;
import com.xxx.schoolBillServer.entity.BillMonth;

public class BillItemServiceImpl {
	public BillItemDaoImpl billItemDaoImpl=new BillItemDaoImpl();
	public int insertBillItem(Double num,String note,Date date,int year,int month,int day,int typeId,int userId){
		return billItemDaoImpl.insertBillItem(num, note, date, year, month, day, typeId, userId);
	}
	public List<BillItem> getBillItemListByDate(int year,int month,int userId){
		return billItemDaoImpl.getBillItemListByDate(year, month, userId);
	}
	public boolean updateBill(BillItem billItem,int userId) {
		return billItemDaoImpl.updateBill(billItem, userId);
	}
	public boolean deleteBill(int id,int userId) {
		return billItemDaoImpl.deleteBill(id, userId);
	}
	public List<BillItem> getBillItemListOrderByNum(int year,int month,int userId){
		return billItemDaoImpl.getBillItemListOrderByNum(year, month, userId);
	}
	public List<BillMonth> getBillMonthListByYear(int year,int id) {
		return billItemDaoImpl.getBillMonthListByYear(year,id);
	}
	public int getBillNum(String month,String id) {
		return billItemDaoImpl.getBillNum(month,id);
	}
}
