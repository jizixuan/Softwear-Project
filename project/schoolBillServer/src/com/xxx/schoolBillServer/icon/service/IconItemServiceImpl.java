package com.xxx.schoolBillServer.icon.service;

import java.util.List;

import com.xxx.schoolBillServer.entity.BillItem;
import com.xxx.schoolBillServer.icon.dao.IconItemDaoImpl;

public class IconItemServiceImpl {
	public IconItemDaoImpl iconItemDaoImpl = new IconItemDaoImpl();
	public List<BillItem> getIconItemByDate(int userId,String firstW,String lastW){
		return iconItemDaoImpl.getIconItemListByDate(userId,firstW,lastW);
	}
	public List<BillItem> getIconItemListByTypeId(int id){
		return iconItemDaoImpl.getIconItemListByTypeId(id);
	}
	public List<BillItem> getIconItemListByTypeIdOrderByDay(int typeId,int userId,String firstW,String lastW){
		return iconItemDaoImpl.getIconItemListByTypeIdOrderByDay(typeId,userId,firstW,lastW);
	}
}
