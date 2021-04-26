package com.xxx.schoolBillServer.test;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.xxx.schoolBillServer.entity.BillType;


@Repository
public class CakeDao {
	@Resource
	private SessionFactory sessionFactory;
	public List<BillType> getList(){
		String hql = "from BillType";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(hql);
		List<BillType> list=query.list();
		return list;
	}
	
}
