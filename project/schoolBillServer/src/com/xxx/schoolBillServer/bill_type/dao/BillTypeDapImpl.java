package com.xxx.schoolBillServer.bill_type.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.xxx.schoolBillServer.entity.BillType;
import com.xxx.schoolBillServer.util.DbUtil;
@Repository
public class BillTypeDapImpl {
	@Resource
	private SessionFactory sessionFactory;
	private List<BillType> billTypes;
//	public List<BillType> getTypeList(){
//		billTypes=new ArrayList<BillType>();
//		Connection con = null;
//		PreparedStatement pstm = null;
//		ResultSet rs = null;
//		con = DbUtil.getCon();
//		String sql="select * from bill_type order by id";
//		try {
//			pstm = con.prepareStatement(sql);
//			rs = pstm.executeQuery();
//			while(rs.next()) {
//				BillType billType=new BillType();
//				billType.setImg(rs.getString("img"));
//				billType.setName(rs.getString("name"));
//				billType.setNumType(rs.getString("num_type"));
//				billType.setId(rs.getInt("id"));			
//				billTypes.add(billType);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return billTypes;
//	
//	}
	public List<BillType> getTypeList(){
		String hql = "from BillType";
		Session session = sessionFactory.openSession();
		Query query = session.createQuery(hql);
		billTypes = query.list();
		session.close();
		return billTypes;
	
	}
//	public List<BillType> getTypeListByNumType(String numType){
//		billTypes=new ArrayList<BillType>();
//		Connection con = null;
//		PreparedStatement pstm = null;
//		ResultSet rs = null;
//		con = DbUtil.getCon();
//		String sql="select * from bill_type where num_type=?";
//		try {
//			pstm = con.prepareStatement(sql);
//			pstm.setString(1,numType);
//			rs = pstm.executeQuery();
//			while(rs.next()) {
//				BillType billType=new BillType();
//				billType.setImg(rs.getString("img"));
//				billType.setName(rs.getString("name"));
//				billType.setNumType(rs.getString("num_type"));
//				billType.setId(rs.getInt("id"));
//				billTypes.add(billType);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return billTypes;
//	}
	public List<BillType> getTypeListByNumType(String numType){
		String hql = "from BillType t where t.numType=:numType";
		Session session = sessionFactory.openSession();
		Query query = session.createQuery(hql);
		query.setParameter("numType", numType);
		billTypes = query.list();
		session.close();
		return billTypes;
	}
//	public int getTypeIdByName(String name){
//		Connection con = null;
//		PreparedStatement pstm = null;
//		ResultSet rs = null;
//		int id=0;
//		con = DbUtil.getCon();
//		String sql="select * from bill_type where name=?";
//		try {
//			pstm = con.prepareStatement(sql);
//			pstm.setString(1,name);
//			rs = pstm.executeQuery();
//			if(rs.next()) {
//				id=rs.getInt("id");
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return id;
//	}
	public int getTypeIdByName(String name){
		int id;
		String hql = "select t.id from BillType t where t.name=:name";
		Session session = sessionFactory.openSession();
		Query query = session.createQuery(hql);
		query.setParameter("name", name);
		id = (int) query.uniqueResult();
		session.close();
		return id;
	}
//	public BillType getTypeById(int id) {
//		Connection con = null;
//		PreparedStatement pstm = null;
//		ResultSet rs = null;
//		BillType billType=new BillType();
//		con = DbUtil.getCon();
//		String sql="select * from bill_type where id=?";
//		try {
//			pstm = con.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
//			pstm.setInt(1,id);
//			rs = pstm.executeQuery();
//			if(rs.next()) {
//				billType.setId(id);
//				billType.setImg(rs.getString("img"));
//				billType.setName(rs.getString("name"));
//				billType.setNumType(rs.getString("num_type"));
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return billType;
//	}
	public BillType getTypeById(int id) {
		String hql = "from BillType t where t.id=:id";
		Session session = sessionFactory.openSession();
		Query query = session.createQuery(hql);
		query.setParameter("id", id);
		BillType billType = (BillType) query.uniqueResult();
		session.close();
		return billType;
	}
}
