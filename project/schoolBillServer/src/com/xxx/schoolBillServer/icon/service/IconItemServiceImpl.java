package com.xxx.schoolBillServer.icon.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xxx.schoolBillServer.entity.BillItem;
import com.xxx.schoolBillServer.icon.dao.IconItemDaoImpl;
@Service
@Transactional(readOnly = false)
public class IconItemServiceImpl {
	@Resource
	public IconItemDaoImpl iconItemDaoImpl;
	public  List<BillItem> getIconItemByDate(int userId,Date date,Date lastW){
		return this.iconItemDaoImpl.getIconItemListByDate(userId,date,lastW);
	}
	public List<BillItem> getIconItemListByTypeId(int id){
		return this.iconItemDaoImpl.getIconItemListByTypeId(id);
	}
	public List<BillItem> getIconItemListByTypeIdOrderByDay(int typeId,int userId,Date firstW,Date lastW){
		return this.iconItemDaoImpl.getIconItemListByTypeIdOrderByDay(typeId,userId,firstW,lastW);
	}
}
