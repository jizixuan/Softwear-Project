package com.xxx.schoolBillServer.bill_item.service;

import java.util.List;

import javax.annotation.Resource;

import org.eclipse.jdt.internal.compiler.env.IBinaryNestedType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xxx.schoolBillServer.bill_item.dao.BillItemDaoImpl;
import com.xxx.schoolBillServer.entity.BillItem;
import com.xxx.schoolBillServer.entity.BillMonth;
@Service
@Transactional(readOnly = false)
public class BillItemServiceImpl {
	
	@Resource
	private BillItemDaoImpl billItemDaoImpl;
	
	public int insertBillItem(BillItem billItem) {
		return this.billItemDaoImpl.insertBillItem(billItem);
	}
	public List<BillItem> getBillItemListByDate(int year,int month,int userId){
		return this.billItemDaoImpl.getBillItemListByDate(year, month, userId);
	}
	public boolean updateBill(BillItem billItem,int userId) {
		return this.billItemDaoImpl.updateBill(billItem, userId);
	}
	public boolean deleteBill(int id,int userId) {
		return this.billItemDaoImpl.deleteBill(id, userId);
	}
	public List<BillItem> getBillItemListOrderByNum(int year,int month,int userId){
		return this.billItemDaoImpl.getBillItemListOrderByNum(year, month, userId);
	}
	public List<BillMonth> getBillMonthListByYear(int year,int id) {
		return this.billItemDaoImpl.getBillMonthListByYear(year,id);
	}
	public int getBillNum(String month,String id) {
		return this.billItemDaoImpl.getBillNum(month,id);
	}
	public List<BillItem> getBillItemListOrderByDay(int year,int month,int day,int userId) {
		return this.billItemDaoImpl.getBillItemListOrderByDay(year, month, day, userId);
		
	}
	public List<Integer> getBillItemMark(int year,int month,int userId){
		return this.billItemDaoImpl.getBillItemMark(year, month, userId);
	}

}
