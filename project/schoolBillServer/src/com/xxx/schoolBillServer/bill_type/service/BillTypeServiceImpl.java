package com.xxx.schoolBillServer.bill_type.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xxx.schoolBillServer.bill_item.dao.BillItemDaoImpl;
import com.xxx.schoolBillServer.bill_type.dao.BillTypeDapImpl;
import com.xxx.schoolBillServer.entity.BillType;
@Service
@Transactional(readOnly = false)
public class BillTypeServiceImpl {
	@Resource
	private BillTypeDapImpl billTypeDapImpl;
	public List<BillType> getTypeList(){
		return this.billTypeDapImpl.getTypeList();
	}
	public List<BillType> getTypeListByNumType(String numType){
		return this.billTypeDapImpl.getTypeListByNumType(numType);
	}
	public int getTypeIdByName(String name){
		return this.billTypeDapImpl.getTypeIdByName(name);
	}
	public BillType getTypeById(int id) {
		return this.billTypeDapImpl.getTypeById(id);
	}
}
