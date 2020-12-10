package com.xxx.schoolBillServer.note.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.xxx.schoolBillServer.entity.Note;
import com.xxx.schoolBillServer.util.DbUtil;

public class NoteDaolImpl {
	private List<Note> notes;
	public List<Note> getNoteList(int userId){
		notes=new ArrayList<Note>();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		con = DbUtil.getCon();
		String sql="select * from note where user_id=?";
		try {
			pstm = con.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			pstm.setInt(1, userId);
			rs = pstm.executeQuery();
			while(rs.next()) {
				Note note=new Note();
				note.setId(rs.getInt("id"));
				note.setContent(rs.getString("content"));
				note.setCreateTime(rs.getString("createTime"));
				note.setSubContent(rs.getString("subContent"));
				note.setTitle(rs.getString("title"));
				notes.add(note);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return notes;
	}
	public int insertNote(String content, String createTime, String title, String subContent,int userId) {
		Connection con = null;
		PreparedStatement pstm = null;
		con = DbUtil.getCon();
		int id=-1;
		String sql="insert into note(content,createTime,title,subContent,user_id) values (?,?,?,?,?)";
		try {
			pstm = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			pstm.setString(1, content);
			pstm.setString(2, createTime);
			pstm.setString(3, title);
			pstm.setString(4, subContent);
			pstm.setInt(5, userId);
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
	public boolean deleteNote(int id) {
		Connection con = null;
		PreparedStatement pstm = null;
		int i=-1;
		con = DbUtil.getCon();
		String sql="delete from note where id="+id;
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
	public boolean updateNote(String content, String createTime, String title, String subContent,int userId,int id) {
		Connection con = null;
		PreparedStatement pstm = null;
		int i=-1;
		con = DbUtil.getCon();
		String sql="update note set content=?,createTime=?,title=?,subContent=? where id=? and user_id=?";
		try {
			pstm = con.prepareStatement(sql);
			pstm.setString(1, content);
			pstm.setString(2, createTime);
			pstm.setString(3, title);
			pstm.setString(4, subContent);
			pstm.setInt(5, id);
			pstm.setInt(6, userId);
			i=pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(i>0)
			return true;
		else
			return false;
	}
}
