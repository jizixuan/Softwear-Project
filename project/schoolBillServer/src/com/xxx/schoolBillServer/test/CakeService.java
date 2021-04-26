package com.xxx.schoolBillServer.test;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xxx.schoolBillServer.entity.BillType;


@Service
@Transactional(readOnly = false)
public class CakeService {
	
	@Resource
	private CakeDao cakeDao;
	public List<BillType> getList(){
		return cakeDao.getList();
		
	}
	
	

}
