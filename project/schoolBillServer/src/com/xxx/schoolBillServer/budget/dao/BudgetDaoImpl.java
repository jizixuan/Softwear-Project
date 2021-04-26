package com.xxx.schoolBillServer.budget.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.xxx.schoolBillServer.entity.Budget;
import com.xxx.schoolBillServer.util.DbUtil;
@Repository
public class BudgetDaoImpl {
	@Resource
	private SessionFactory sessionFactory;
	/**
	 * 查找用预算
	 * @param id
	 * @return
	 */
//	public Budget getBudgetById(int id) {
//		Budget budget = new Budget();
//		Connection con = null;
//		PreparedStatement pstm = null;
//		ResultSet rs = null;
//		con = DbUtil.getCon();
//		String sql = "select * from budget where user_id = '"+ id+"'";
//		System.out.println(sql);
//		try {
//			pstm = con.prepareStatement(sql);
//			rs = pstm.executeQuery();
//			while(rs.next()) {
//				budget.setId(id);
//				budget.setBudget(rs.getDouble(2));
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return budget;
//	}
	public Budget getBudgetById(int id) {
		String hql = "from Budget b where b.id=:id";
		
		Session session = sessionFactory.openSession();
		Query query = session.createQuery(hql);
		query.setParameter("id", id);
		Budget budget = (Budget) query.uniqueResult();
		session.close();
		return budget;
	}
	/**
	 * 添加用户预算
	 * @param budget
	 * @return
	 */
//	public boolean addBudget(Budget budget) {
//		boolean tag = false;
//		Connection con = null;
//		PreparedStatement pstm = null;
//		ResultSet rs = null;
//		con = DbUtil.getCon();
//		String sql = "insert into budget (user_id,budget) values('"+budget.getId()+"','"+budget.getBudget()+"')";
//		System.out.println(sql);
//		try {
//			pstm = con.prepareStatement(sql);
//			tag = pstm.execute();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return tag;
//	}
	public boolean addBudget(Budget budget) {
		Session session = this.sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.save(budget);
		tx.commit();
		session.close();
		int key=budget.getId();
		if(key>0) {
			return true;
		}
		else {
			return false;
		}
	}
	/**
	 * 更新用户预算
	 * @param budget
	 * @return
	 */
//	public boolean updateBudget(Budget budget) {
//		boolean tag = false;
//		Connection con = null;
//		PreparedStatement pstm = null;
//		ResultSet rs = null;
//		con = DbUtil.getCon();
//		String sql = "update budget set budget = '"+budget.getBudget()+"' where user_id = '"+budget.getId()+"'";
//		System.out.println(sql);
//		try {
//			pstm = con.prepareStatement(sql);
//			tag = pstm.execute();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return tag;
//	}
	public boolean updateBudget(Budget budget) {
		Session session = this.sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		String hql = "update Budget b set b.budget=:budget where b.id=:id";
		Query query  = session.createQuery(hql);
		query.setParameter("budget", budget.getBudget());
		query.setParameter("id", budget.getId());
		int ret = query.executeUpdate();
		tx.commit();
		session.close();
		if(ret>0)
			return true;
		else
			return false;
	}

}
