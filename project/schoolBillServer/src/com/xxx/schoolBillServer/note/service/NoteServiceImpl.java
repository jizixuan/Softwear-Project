package com.xxx.schoolBillServer.note.service;

import java.util.List;

import com.xxx.schoolBillServer.entity.Note;
import com.xxx.schoolBillServer.note.dao.NoteDaolImpl;

public class NoteServiceImpl {
	private NoteDaolImpl noteDaolImpl=new NoteDaolImpl();
	public List<Note> getNoteList(int userId){
		return noteDaolImpl.getNoteList(userId);
	}
	public int insertNote(String content, String createTime, String title, String subContent,int userId) {
		return noteDaolImpl.insertNote(content, createTime, title, subContent, userId);
	}
	public boolean deleteNote(int id) {
		return noteDaolImpl.deleteNote(id);
	}
	public boolean updateNote(String content, String createTime, String title, String subContent,int userId,int id) {
		return noteDaolImpl.updateNote(content, createTime, title, subContent, userId, id);
	}
}
