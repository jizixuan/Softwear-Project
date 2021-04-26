package com.xxx.schoolBillServer.note.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xxx.schoolBillServer.entity.Note;
import com.xxx.schoolBillServer.note.dao.NoteDaolImpl;

@Service
@Transactional(readOnly = false)
public class NoteServiceImpl {
	@Resource
	private NoteDaolImpl noteDaolImpl;
	public List<Note> getNoteList(int userId){
		return noteDaolImpl.getNoteList(userId);
	}
	public int insertNote(Note note) {
		return noteDaolImpl.insertNote(note);
	}
	public boolean deleteNote(int id) {
		return noteDaolImpl.deleteNote(id);
	}
	public boolean updateNote(String content, String createTime, String title, String subContent,int userId,int id) {
		return noteDaolImpl.updateNote(content, createTime, title, subContent, userId, id);
	}
}
