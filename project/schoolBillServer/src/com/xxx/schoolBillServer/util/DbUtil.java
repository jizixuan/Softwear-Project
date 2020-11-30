package com.xxx.schoolBillServer.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbUtil {
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取数据库连接
	 * 
	 * @return
	 */
	public static Connection getCon() {
		try {
			return DriverManager.getConnection("jdbc:mysql://rm-2ze05m8d943jo7ndxjo.mysql.rds.aliyuncs.com:3306/school_bill?useUnicode=true&characterEncoding=utf-8&useSSL=false","user2","Qwer1234");
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void close(ResultSet rs, PreparedStatement pstm, Connection con) {
		try {
			if(rs!=null)
				rs.close();
			if(pstm!=null)
				pstm.close();
			if(con!=null)
				con.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
