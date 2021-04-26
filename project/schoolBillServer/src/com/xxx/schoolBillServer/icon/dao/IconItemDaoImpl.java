package com.xxx.schoolBillServer.icon.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.xxx.schoolBillServer.entity.BillItem;
import com.xxx.schoolBillServer.util.DbUtil;
@Repository
public class IconItemDaoImpl {
	@Resource
	private SessionFactory sessionFactory;
	private List<BillItem> billItems;
//	public List<BillItem> getIconItemListByTypeIdOrderByDay(int typeId,int userId,String firstW,String lastW){
//		billItems=new ArrayList<BillItem>();
//		Connection con = null;
//		PreparedStatement pstm = null;
//		ResultSet rs = null;
//		con = DbUtil.getCon();
//		String sql="select * from bill_item where type_id=? and user_id=? and date between? and? order by month,day";
//		try {
//			pstm = con.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
//			pstm.setInt(1, typeId);
//			pstm.setInt(2, userId);
//			pstm.setString(3,firstW);
//			pstm.setString(4,lastW);
//			rs = pstm.executeQuery();
//			while(rs.next()) {
//				BillItem billItem=new BillItem();
//				billItem.setId(rs.getInt("id"));
//				billItem.setTypeId(rs.getInt("type_id"));
//				billItem.setDay(rs.getInt("day"));
//				billItem.setDate(rs.getDate("date"));
//				billItem.setNote(rs.getString("note"));
//				billItem.setNum(rs.getDouble("num"));
//				billItem.setYear(rs.getInt("year"));
//				billItem.setMonth(rs.getInt("month"));
//				billItems.add(billItem);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		System.out.println(billItems);
//		return billItems;
//	}
	public List<BillItem> getIconItemListByTypeIdOrderByDay(int typeId,int userId,Date firstW,Date lastW){
		String hql="from BillItem b where b.typeId=:typeId and b.userId=:userId and b.date between:firstW and:lastW order by month,day";
		Session session = sessionFactory.openSession();
		Query query = session.createQuery(hql);
		query.setParameter("typeId", typeId);
		query.setParameter("userId",userId);
		query.setTimestamp("firstW",firstW);
		query.setTimestamp("lastW",lastW);
		billItems = query.list();
		session.close();
		return billItems;
	}
//	public List<BillItem> getIconItemListByTypeId(int typeId){
//		billItems=new ArrayList<BillItem>();
//		Connection con = null;
//		PreparedStatement pstm = null;
//		ResultSet rs = null;
//		con = DbUtil.getCon();
//		String sql="select * from bill_item where type_id=?";
//		try {
//			pstm = con.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
//			pstm.setInt(1, typeId);
//			rs = pstm.executeQuery();
//			while(rs.next()) {
//				BillItem billItem=new BillItem();
//				billItem.setId(rs.getInt("id"));
//				billItem.setTypeId(rs.getInt("type_id"));
//				billItem.setDay(rs.getInt("day"));
//				billItem.setDate(rs.getDate("date"));
//				billItem.setNote(rs.getString("note"));
//				billItem.setNum(rs.getDouble("num"));
//				billItem.setYear(rs.getInt("year"));
//				billItem.setMonth(rs.getInt("month"));
//				billItems.add(billItem);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		System.out.println(billItems);
//		return billItems;
//	}
	public List<BillItem> getIconItemListByTypeId(int typeId){
		String hql = "from BillItem b where b.typeId=:typeId";
		Session session = sessionFactory.openSession();
		Query query = session.createQuery(hql);
		query.setParameter("typeId", typeId);
		billItems = query.list();
		session.close();
		return billItems;
	}
//	public List<BillItem> getIconItemListByDate(int userId,String firstW,String lastW){
//		billItems=new ArrayList<BillItem>();
//		Connection con = null;
//		PreparedStatement pstm = null;
//		ResultSet rs = null;
//		con = DbUtil.getCon();
//		String sql="select * from bill_item where user_id=? and date between? and? order by month,day";
//		try {
//			pstm = con.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
//			pstm.setInt(1, userId);
//			pstm.setString(2,firstW);
//			pstm.setString(3,lastW);
//			rs = pstm.executeQuery();
//			while(rs.next()) {
//				BillItem billItem=new BillItem();
//				billItem.setId(rs.getInt("id"));
//				billItem.setTypeId(rs.getInt("type_id"));
//				billItem.setDay(rs.getInt("day"));
//				billItem.setDate(rs.getDate("date"));
//				billItem.setNote(rs.getString("note"));
//				billItem.setNum(rs.getDouble("num"));
//				billItem.setYear(rs.getInt("year"));
//				billItem.setMonth(rs.getInt("month"));
//				billItems.add(billItem);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		System.out.println(billItems);
//		return billItems;
//	}
	public List<BillItem> getIconItemListByDate(int userId,Date firstW,Date lastW){
		String hql="from BillItem b where b.userId=:userId and b.date between:firstW and:lastW order by month,day";
		Session session = sessionFactory.openSession();
		Query query = session.createQuery(hql);
		query.setParameter("userId",userId);
		query.setTimestamp("firstW",firstW);
		query.setTimestamp("lastW",lastW);
		billItems = query.list();
		session.close();
		return billItems;
	}
}
