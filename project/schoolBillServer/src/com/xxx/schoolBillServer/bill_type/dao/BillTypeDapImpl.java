package com.xxx.schoolBillServer.bill_type.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.xxx.schoolBillServer.entity.BillType;
import com.xxx.schoolBillServer.util.DbUtil;

public class BillTypeDapImpl {
	private List<BillType> billTypes;
	public List<BillType> getTypeList(){
		billTypes=new ArrayList<BillType>();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		con = DbUtil.getCon();
		String sql="select * from bill_type order by id";
		try {
			pstm = con.prepareStatement(sql);
			rs = pstm.executeQuery();
			while(rs.next()) {
				BillType billType=new BillType();
				billType.setImg(rs.getString("img"));
				billType.setName(rs.getString("name"));
				billType.setNumType(rs.getString("num_type"));
				billType.setId(rs.getInt("id"));			
				billTypes.add(billType);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return billTypes;
	
	}
	public List<BillType> getTypeListByNumType(String numType){
		billTypes=new ArrayList<BillType>();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		con = DbUtil.getCon();
		String sql="select * from bill_type where num_type=?";
		try {
			pstm = con.prepareStatement(sql);
			pstm.setString(1,numType);
			rs = pstm.executeQuery();
			while(rs.next()) {
				BillType billType=new BillType();
				billType.setImg(rs.getString("img"));
				billType.setName(rs.getString("name"));
				billType.setNumType(rs.getString("num_type"));
				billType.setId(rs.getInt("id"));
				billTypes.add(billType);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return billTypes;
	}
	public int getTypeIdByName(String name){
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int id=0;
		con = DbUtil.getCon();
		String sql="select * from bill_type where name=?";
		try {
			pstm = con.prepareStatement(sql);
			pstm.setString(1,name);
			rs = pstm.executeQuery();
			if(rs.next()) {
				id=rs.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}
	public BillType getTypeById(int id) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		BillType billType=new BillType();
		con = DbUtil.getCon();
		String sql="select * from bill_type where id=?";
		try {
			pstm = con.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			pstm.setInt(1,id);
			rs = pstm.executeQuery();
			if(rs.next()) {
				billType.setId(id);
				billType.setImg(rs.getString("img"));
				billType.setName(rs.getString("name"));
				billType.setNumType(rs.getString("num_type"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return billType;
	}
}
