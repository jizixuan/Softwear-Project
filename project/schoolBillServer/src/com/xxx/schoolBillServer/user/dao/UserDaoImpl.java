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
	 * 查找用户，并验证登录信息
	 * 注册时，验证电话号码是否已经被注册
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
	 * 添加用户信息
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
		
		System.out.println(sql +"添加用户信息："+tag);
		return tag;
	}
	/**
	 * 更改用户信息
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
		System.out.println(sql +"更改用户信息："+tag);
		return tag;
	}
}
