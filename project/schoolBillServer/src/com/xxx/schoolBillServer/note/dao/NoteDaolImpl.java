package com.xxx.schoolBillServer.note.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.xxx.schoolBillServer.entity.Note;
import com.xxx.schoolBillServer.util.DbUtil;
@Repository
public class NoteDaolImpl {
	private List<Note> notes;
	@Resource
	private SessionFactory sessionFactory;
//	public List<Note> getNoteList(int userId){
//		notes=new ArrayList<Note>();
//		Connection con = null;
//		PreparedStatement pstm = null;
//		ResultSet rs = null;
//		con = DbUtil.getCon();
//		String sql="select * from note where user_id=?";
//		try {
//			pstm = con.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
//			pstm.setInt(1, userId);
//			rs = pstm.executeQuery();
//			while(rs.next()) {
//				Note note=new Note();
//				note.setId(rs.getInt("id"));
//				note.setContent(rs.getString("content"));
//				note.setCreateTime(rs.getString("createTime"));
//				note.setSubContent(rs.getString("subContent"));
//				note.setTitle(rs.getString("title"));
//				notes.add(note);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return notes;
//	}
	public List<Note> getNoteList(int userId){
		String hql = "from Note n where n.userId=:userId";
		Session session = sessionFactory.openSession();
		Query query = session.createQuery(hql);
		query.setParameter("userId", userId);
		notes = query.list();
		session.close();
		return notes;
	}
	
//	public int insertNote(String content, String createTime, String title, String subContent,int userId) {
//		Connection con = null;
//		PreparedStatement pstm = null;
//		con = DbUtil.getCon();
//		int id=-1;
//		String sql="insert into note(content,createTime,title,subContent,user_id) values (?,?,?,?,?)";
//		try {
//			pstm = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
//			pstm.setString(1, content);
//			pstm.setString(2, createTime);
//			pstm.setString(3, title);
//			pstm.setString(4, subContent);
//			pstm.setInt(5, userId);
//			pstm.executeUpdate();
//			ResultSet rs = pstm.getGeneratedKeys(); //��ȡ���   
//			if (rs.next()) {
//				id = rs.getInt(1);//ȡ��ID
//			} else {
//				System.out.println("error");
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return id;
//	}
	
	public int insertNote(Note note) {
		Session session = this.sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.save(note);
		tx.commit();
		session.close();
		int key=note.getId();
		return key;
	}
//	public boolean deleteNote(int id) {
//		Connection con = null;
//		PreparedStatement pstm = null;
//		int i=-1;
//		con = DbUtil.getCon();
//		String sql="delete from note where id="+id;
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
	public boolean deleteNote(int id) {
		Session session = this.sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		String hql = "delete from Note n where n.id = :id";
		Query query  = session.createQuery(hql);
		query.setParameter("id", id);
		int ret = query.executeUpdate();
		tx.commit();
		session.close();
		if(ret>0)
			return true;
		else
			return false;
	}
//	public boolean updateNote(String content, String createTime, String title, String subContent,int userId,int id) {
//		Connection con = null;
//		PreparedStatement pstm = null;
//		int i=-1;
//		con = DbUtil.getCon();
//		String sql="update note set content=?,createTime=?,title=?,subContent=? where id=? and user_id=?";
//		try {
//			pstm = con.prepareStatement(sql);
//			pstm.setString(1, content);
//			pstm.setString(2, createTime);
//			pstm.setString(3, title);
//			pstm.setString(4, subContent);
//			pstm.setInt(5, id);
//			pstm.setInt(6, userId);
//			i=pstm.executeUpdate();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		if(i>0)
//			return true;
//		else
//			return false;
//	}
	public boolean updateNote(String content, String createTime, String title, String subContent,int userId,int id) {
		Session session = this.sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		String hql = "update Note n set n.content=:content, n.createTime=:createTime, n.title=:title , n.subContent=:subContent where n.userId=:userId and n.id=:id";
		Query query  = session.createQuery(hql);
		query.setParameter("content", content);
		query.setParameter("createTime", createTime);
		query.setParameter("title", title);
		query.setParameter("subContent", subContent);
		query.setParameter("userId", userId);
		query.setParameter("id", id);
		int ret = query.executeUpdate();
		tx.commit();
		session.close();
		if(ret>0)
			return true;
		else
			return false;
	}
}
