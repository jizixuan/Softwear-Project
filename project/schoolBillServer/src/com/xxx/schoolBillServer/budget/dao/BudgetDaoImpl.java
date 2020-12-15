package com.xxx.schoolBillServer.budget.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.xxx.schoolBillServer.entity.Budget;
import com.xxx.schoolBillServer.util.DbUtil;

public class BudgetDaoImpl {
	/**
	 * ������Ԥ��
	 * @param id
	 * @return
	 */
	public Budget getBudgetById(int id) {
		Budget budget = new Budget();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		con = DbUtil.getCon();
		String sql = "select * from budget where user_id = '"+ id+"'";
		System.out.println(sql);
		try {
			pstm = con.prepareStatement(sql);
			rs = pstm.executeQuery();
			while(rs.next()) {
				budget.setId(id);
				budget.setBudget(rs.getDouble(2));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return budget;
	}
	/**
	 * ����û�Ԥ��
	 * @param budget
	 * @return
	 */
	public boolean addBudget(Budget budget) {
		boolean tag = false;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		con = DbUtil.getCon();
		String sql = "insert into budget (user_id,budget) values('"+budget.getId()+"','"+budget.getBudget()+"')";
		System.out.println(sql);
		try {
			pstm = con.prepareStatement(sql);
			tag = pstm.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tag;
	}
	/**
	 * �����û�Ԥ��
	 * @param budget
	 * @return
	 */
	public boolean updateBudget(Budget budget) {
		boolean tag = false;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		con = DbUtil.getCon();
		String sql = "update budget set budget = '"+budget.getBudget()+"' where user_id = '"+budget.getId()+"'";
		System.out.println(sql);
		try {
			pstm = con.prepareStatement(sql);
			tag = pstm.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tag;
	}
	/**
	 * ɾ���û�Ԥ��
	 * @param id
	 * @return
	 */
	public boolean deleteBudget(int id) {
		boolean tag = false;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		con = DbUtil.getCon();
		String sql = "delete from budget where user_id = '"+id+"'";
		System.out.println(sql);
		try {
			pstm = con.prepareStatement(sql);
			tag = pstm.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tag;
	}
	
}
