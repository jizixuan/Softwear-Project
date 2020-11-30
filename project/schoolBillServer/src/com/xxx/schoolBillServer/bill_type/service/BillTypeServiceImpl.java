package com.xxx.schoolBillServer.bill_type.service;

import java.util.List;

import com.xxx.schoolBillServer.bill_type.dao.BillTypeDapImpl;
import com.xxx.schoolBillServer.entity.BillType;

public class BillTypeServiceImpl {
	public BillTypeDapImpl billTypeDapImpl=new BillTypeDapImpl();
	public List<BillType> getTypeList(){
		return billTypeDapImpl.getTypeList();
	}
	public List<BillType> getTypeListByNumType(String numType){
		return billTypeDapImpl.getTypeListByNumType(numType);
	}
	public int getTypeIdByName(String name){
		return billTypeDapImpl.getTypeIdByName(name);
	}
	public BillType getTypeById(int id) {
		return billTypeDapImpl.getTypeById(id);
	}
}
