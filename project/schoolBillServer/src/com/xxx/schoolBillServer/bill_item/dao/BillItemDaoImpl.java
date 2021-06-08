package com.xxx.schoolBillServer.bill_item.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.xxx.schoolBillServer.entity.BillItem;
import com.xxx.schoolBillServer.entity.BillMonth;
import com.xxx.schoolBillServer.util.DbUtil;
@Repository
public class BillItemDaoImpl {
	@Resource
	private SessionFactory sessionFactory;
	
	private List<BillItem> billItems;
	private List<BillMonth> billMonths;
//	public int insertBillItem(Double num,String note,Date date,int year,int month,int day,int typeId,int userId){
//		Connection con = null;
//		PreparedStatement pstm = null;
//		con = DbUtil.getCon();
//		int id=-1;
//		String sql="insert into bill_item (num,note,date,year,month,day,type_id,user_id) values ("+num+",'"+note+"','"+new java.sql.Date(date.getTime())+
//				"',"+year+","+month+","+day+","+typeId+","+userId+")";
//		try {
//			pstm = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
//			pstm.executeUpdate();
//			ResultSet rs = pstm.getGeneratedKeys(); //获取结果   
//			if (rs.next()) {
//				id = rs.getInt(1);//取得ID
//			} else {
//				System.out.println("error");
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return id;
//	}
	//修改后
	public int insertBillItem(BillItem billItem) {
		Session session = this.sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.save(billItem);
		tx.commit();
		session.close();
		int key=billItem.getId();
		return key;
	}
//	public List<BillItem> getBillItemListByDate(int year,int month,int userId){
//		billItems=new ArrayList<BillItem>();
//		Connection con = null;
//		PreparedStatement pstm = null;
//		ResultSet rs = null;
//		con = DbUtil.getCon();
//		String sql="select * from bill_item where year=? and month=? and user_id=? order by day";
//		try {
//			pstm = con.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
//			pstm.setInt(1, year);
//			pstm.setInt(2, month);
//			pstm.setInt(3, userId);
//			rs = pstm.executeQuery();
//			while(rs.next()) {
//				BillItem billItem=new BillItem();
//				billItem.setId(rs.getInt("id"));
//				billItem.setTypeId(rs.getInt("type_id"));
//				billItem.setDay(rs.getInt("day"));
//				billItem.setDate(rs.getDate("date"));
//				billItem.setNote(rs.getString("note"));
//				billItem.setNum(rs.getDouble("num"));
//				billItem.setYear(year);
//				billItem.setMonth(month);
//				billItems.add(billItem);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return billItems;
//	}
	public List<BillItem> getBillItemListByDate(int year,int month,int userId){
		String hql = "from BillItem i where i.year=:year and month=:month and userId=:userId order by day";
		
		Session session = sessionFactory.openSession();
		Query query = session.createQuery(hql);
		query.setParameter("year", year);
		query.setParameter("month", month);
		query.setParameter("userId", userId);
		billItems = query.list();
		session.close();
		return billItems;
	}
//	public List<BillItem> getBillItemListOrderByNum(int year,int month,int userId){
//		billItems=new ArrayList<BillItem>();
//		Connection con = null;
//		PreparedStatement pstm = null;
//		ResultSet rs = null;
//		con = DbUtil.getCon();
//		String sql="select * from bill_item where year=? and month=? and user_id=? order by num desc";
//		try {
//			pstm = con.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
//			pstm.setInt(1, year);
//			pstm.setInt(2, month);
//			pstm.setInt(3, userId);
//			rs = pstm.executeQuery();
//			while(rs.next()) {
//				BillItem billItem=new BillItem();
//				billItem.setId(rs.getInt("id"));
//				billItem.setTypeId(rs.getInt("type_id"));
//				billItem.setDay(rs.getInt("day"));
//				billItem.setDate(rs.getDate("date"));
//				billItem.setNote(rs.getString("note"));
//				billItem.setNum(rs.getDouble("num"));
//				billItem.setYear(year);
//				billItem.setMonth(month);
//				billItems.add(billItem);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return billItems;
//	}
	public List<BillItem> getBillItemListOrderByNum(int year,int month,int userId){
		String hql = "from BillItem i where i.year=:year and month=:month and userId=:userId order by num desc";
		Session session = sessionFactory.openSession();
		Query query = session.createQuery(hql);
		query.setParameter("year", year);
		query.setParameter("month", month);
		query.setParameter("userId", userId);
		billItems = query.list();
		session.close();
		return billItems;
	}
//	public boolean updateBill(BillItem billItem,int userId) {
//		Connection con = null;
//		PreparedStatement pstm = null;
//		int i=-1;
//		con = DbUtil.getCon();
//		Date date=stringToDate(billItem.getYear()+"-"+billItem.getMonth()+"-"+billItem.getDay(),"yyyy-MM-dd");
//		String sql="update bill_item set num="+billItem.getNum()+",note='"+billItem.getNote()+"',date='"+new java.sql.Date(date.getTime())+"',year="+billItem.getYear()+
//				",month="+billItem.getMonth()+",day="+billItem.getDay()+",type_id="+billItem.getTypeId()+" where user_id="+userId+" and id="+billItem.getId();
//		try {
//			pstm = con.prepareStatement(sql);
//			i=pstm.executeUpdate(sql);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		if(i>0)
//			return true;
//		else
//			return false;
//	}
	public boolean updateBill(BillItem billItem,int userId) {
		Date date=stringToDate(billItem.getYear()+"-"+billItem.getMonth()+"-"+billItem.getDay(),"yyyy-MM-dd");
		Session session = this.sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		String hql = "update BillItem i set i.num=:num, i.note=:note, i.date=:date , i.year=:year, i.month=:month, i.day=:day, i.typeId=:typeId where i.userId=:userId and i.id=:id";
		Query query  = session.createQuery(hql);
		query.setParameter("num", billItem.getNum());
		query.setParameter("note", billItem.getNote());
		query.setParameter("date", new java.sql.Date(date.getTime()));
		query.setParameter("year", billItem.getYear());
		query.setParameter("month", billItem.getMonth());
		query.setParameter("day", billItem.getDay());
		query.setParameter("typeId", billItem.getTypeId());
		query.setParameter("userId", userId);
		query.setParameter("id", billItem.getId());
		int ret = query.executeUpdate();
		tx.commit();
		session.close();
		if(ret>0)
			return true;
		else
			return false;

	}
//	public boolean deleteBill(int id,int userId) {
//		Connection con = null;
//		PreparedStatement pstm = null;
//		int i=-1;
//		con = DbUtil.getCon();
//		String sql="delete from bill_item where id="+id+" and user_id="+userId;
//		try {
//			pstm = con.prepareStatement(sql);
//			i=pstm.executeUpdate(sql);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		if(i>0)
//			return true;
//		else
//			return false;
//	}
	public boolean deleteBill(int id,int userId) {
		Session session = this.sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		String hql = "delete from BillItem i where i.id = :id and i.userId=:userId";
		Query query  = session.createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("userId", userId);
		int ret = query.executeUpdate();
		tx.commit();
		session.close();
		if(ret>0)
			return true;
		else
			return false;
	}
	public static Date stringToDate(String dateStr, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        try {
            return formatter.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }
	/**
	 * 获取每个的收入总额
	 * @param year
	 * @return
	 */
	//没改
	public List<BillMonth> getBillMonthListByYear(int year,int id){
		billMonths = new ArrayList<>();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		con = DbUtil.getCon();
		String sql = "select month,sum(num),bill_type.num_type from bill_item,bill_type where bill_item.type_id = bill_type.id  and year = '"+year+"' and user_id = '"+id+"' group by month ,bill_type.num_type order by month desc";
		System.out.println(sql);
		try {
			pstm = con.prepareStatement(sql);
			rs = pstm.executeQuery();
			while (rs.next()) {
				BillMonth billMonth = new BillMonth();
				billMonth.setYear(year);
				billMonth.setMonth(rs.getInt(1));
				billMonth.setBill(rs.getDouble(2));
				billMonth.setType(rs.getString(3));
				billMonths.add(billMonth);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return billMonths;
	}
//	public int getBillNum(String month,String id) {
//		int a = 0;
//		Connection con = null;
//		PreparedStatement pstm = null;
//		ResultSet rs = null;
//		con = DbUtil.getCon();
//		String sql = "select count(*) from bill_item where month = '"+month+"' and user_id = '"+id+"'";
//		System.out.println(sql);
//		try {
//			pstm = con.prepareStatement(sql);
//			rs = pstm.executeQuery();
//			while(rs.next()) {
//				a = rs.getInt(1);
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return a;
//	}
	public int getBillNum(String month,String id) {
		String hql = "select count(*) from BillItem i where i.month=:month and i.id=:id";
		Session session = this.sessionFactory.openSession();
		long count=(Long)session.createQuery("select count(*) from BillItem b where b.month='"+month+"' and b.userId='"+id+"'")
                .uniqueResult();
		int num=Integer.parseInt(String.valueOf(count));;
		System.out.println(count+"");
		session.close();
		return num;
	}
	/*根据日期获得数据
	 * */
	public List<BillItem> getBillItemListOrderByDay(int year,int month,int day,int userId){
		String hql = "from BillItem i where i.year=:year and month=:month and day=:day and userId=:userId order by num desc";
		Session session = sessionFactory.openSession();
		Query query = session.createQuery(hql);
		query.setParameter("year", year);
		query.setParameter("month", month);
		query.setParameter("day", day);
		query.setParameter("userId", userId);
		billItems = query.list();
		session.close();
		return billItems;
	}
	public List<Integer> getBillItemMark(int year,int month,int userId){
		String hql = "select distinct i.day FROM BillItem i where i.year=:year and month=:month and userId=:userId";
		
		Session session = sessionFactory.openSession();
		Query query = session.createQuery(hql);
		query.setParameter("year", year);
		query.setParameter("month", month);
		query.setParameter("userId", userId);
		List<Integer> list= query.list();
		session.close();
		return list;
	}
}
