package com.xxx.schoolBillServer.bill_item.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.xxx.schoolBillServer.entity.BillItem;
import com.xxx.schoolBillServer.util.DbUtil;

public class BillItemDaoImpl {
	private List<BillItem> billItems;
	public int insertBillItem(Double num,String note,Date date,int year,int month,int day,int typeId,int userId){
		Connection con = null;
		PreparedStatement pstm = null;
		con = DbUtil.getCon();
		int id=-1;
		String sql="insert into bill_item (num,note,date,year,month,day,type_id,user_id) values ("+num+",'"+note+"','"+new java.sql.Date(date.getTime())+
				"',"+year+","+month+","+day+","+typeId+","+userId+")";
		try {
			pstm = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			pstm.executeUpdate();
			ResultSet rs = pstm.getGeneratedKeys(); //获取结果   
			if (rs.next()) {
				id = rs.getInt(1);//取得ID
			} else {
				System.out.println("error");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}
	public List<BillItem> getBillItemListByDate(int year,int month,int userId){
		billItems=new ArrayList<BillItem>();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		con = DbUtil.getCon();
		String sql="select * from bill_item where year=? and month=? and user_id=? order by day";
		try {
			pstm = con.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			pstm.setInt(1, year);
			pstm.setInt(2, month);
			pstm.setInt(3, userId);
			rs = pstm.executeQuery();
			while(rs.next()) {
				BillItem billItem=new BillItem();
				billItem.setId(rs.getInt("id"));
				billItem.setTypeId(rs.getInt("type_id"));
				billItem.setDay(rs.getInt("day"));
				billItem.setDate(rs.getDate("date"));
				billItem.setNote(rs.getString("note"));
				billItem.setNum(rs.getDouble("num"));
				billItem.setYear(year);
				billItem.setMonth(month);
				billItems.add(billItem);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return billItems;
	}
	public boolean updateBill(BillItem billItem,int userId) {
		Connection con = null;
		PreparedStatement pstm = null;
		int i=-1;
		con = DbUtil.getCon();
		Date date=stringToDate(billItem.getYear()+"-"+billItem.getMonth()+"-"+billItem.getDay(),"yyyy-MM-dd");
		String sql="update bill_item set num="+billItem.getNum()+",note='"+billItem.getNote()+"',date='"+new java.sql.Date(date.getTime())+"',year="+billItem.getYear()+
				",month="+billItem.getMonth()+",day="+billItem.getDay()+",type_id="+billItem.getTypeId()+" where user_id="+userId+" and id="+billItem.getId();
		try {
			pstm = con.prepareStatement(sql);
			i=pstm.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(i>0)
			return true;
		else
			return false;
	}
	public boolean deleteBill(int id,int userId) {
		Connection con = null;
		PreparedStatement pstm = null;
		int i=-1;
		con = DbUtil.getCon();
		String sql="delete from bill_item where id="+id+" and user_id="+userId;
		try {
			pstm = con.prepareStatement(sql);
			i=pstm.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(i>0)
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
}
