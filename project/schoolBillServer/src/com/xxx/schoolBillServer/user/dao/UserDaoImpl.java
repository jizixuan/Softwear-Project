package com.xxx.schoolBillServer.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Statement;
import com.xxx.schoolBillServer.entity.User;
import com.xxx.schoolBillServer.util.DbUtil;

public class UserDaoImpl {
	/**
	 * �����û�������֤��¼��Ϣ
	 * ע��ʱ����֤�绰�����Ƿ��Ѿ���ע��
	 * @param phone
	 * @return
	 */
	public User findUser(String phone) {
		User user = new User();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		con = DbUtil.getCon();
		String sql = "select * from user where phone='"+phone+"'";
		System.out.println(sql);
		try {
			pstm = con.prepareStatement(sql);
			rs = pstm.executeQuery();
			while(rs.next()) {
				user.setId(rs.getInt(6));
				user.setName(rs.getString(5));
				user.setPwd(rs.getString(4));
				user.setPhone(rs.getString(3));
				user.setPhoto(rs.getString(2));
				user.setSex(rs.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}
	/**
	 * ����û���Ϣ
	 * @param user
	 * @return
	 */
	public boolean addUser(User user) {
		boolean tag = false;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int id = -1;
		con = DbUtil.getCon();
		String sql = "insert into user (phone,name,pwd,sex,img) "
				+ "values ('"+user.getPhone()+"',"
						+ "'"+user.getName()+"','"+user.getPwd()+"','"+user.getSex()+"','"+user.getPhone()+"+.jpg')";
		try {
			pstm = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			tag = !pstm.execute();
			rs = pstm.getGeneratedKeys();
			if(rs.next()) {
				id = rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();;
		}
		String sqlString = "insert into budget (user_id,budget) values('"+id+"','0.0')";
		try {
			pstm = con.prepareStatement(sqlString);
			pstm.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(sql +"����û���Ϣ��"+tag);
		return tag;
	}
	/**
	 * �����û���Ϣ
	 * @param user
	 * @return
	 */
	public boolean updateUser(User user) {
		boolean tag = false;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		con = DbUtil.getCon();
		String sql = "update user set phone='"+user.getPhone()+"',name='"+user.getName()+"',pwd ='"+user.getPwd()+"',sex ='"+user.getSex()+"'"
				+ ",img='"+user.getPhoto()+"'"
				+ " where id ='"+user.getId()+"'";
		try {
			pstm = con.prepareStatement(sql);
			tag = !pstm.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(sql +"�����û���Ϣ��"+tag);
		return tag;
	}
}
