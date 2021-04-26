package com.xxx.schoolBillServer.note.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xxx.schoolBillServer.entity.BillItem;
import com.xxx.schoolBillServer.entity.Note;
import com.xxx.schoolBillServer.note.service.NoteServiceImpl;

@Controller
public class NoteController {
	@Resource 
	private NoteServiceImpl serviceImpl;
	
	@RequestMapping(value = "InsertNoteServlet", method=RequestMethod.POST)
	@ResponseBody 
	public String InsertNoteServlet(@RequestParam("content") String content, @RequestParam("createTime") String createTime,
			@RequestParam("title") String title,@RequestParam("subContent") String subContent,@RequestParam("userId") String userIdValue) {
		int userId=Integer.parseInt(userIdValue);
		Note note=new Note();
		note.setContent(subContent);
		note.setCreateTime(createTime);
		note.setSubContent(subContent);
		note.setTitle(title);
		note.setUserId(userId);
		int id=serviceImpl.insertNote(note);
		return id+"";
		
	}
	
	@RequestMapping(value = "GetNoteListServlet", method=RequestMethod.POST,produces = "application/json; charset=utf-8")
	@ResponseBody 
	public String InsertNoteServlet(@RequestParam("userId") String userIdValue) {
		int userId=Integer.parseInt(userIdValue);
		List<Note> notes=serviceImpl.getNoteList(userId);
		JSONArray jsonArray=new JSONArray();
		for(Note note:notes) {
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("content", note.getContent());
			jsonObject.put("id", note.getId());
			jsonObject.put("createTime", note.getCreateTime());
			jsonObject.put("title", note.getTitle());
			jsonObject.put("subContent", note.getSubContent());
			jsonArray.put(jsonObject);
		}
		return jsonArray.toString();
		
	}
	
	@RequestMapping(value = "UpdateNoteServlet", method=RequestMethod.POST)
	@ResponseBody 
	public void UpdateNoteServlet(@RequestParam("content") String content, @RequestParam("createTime") String createTime,
			@RequestParam("title") String title,@RequestParam("subContent") String subContent,@RequestParam("userId") String userIdValue,@RequestParam("id") String idValue) {
		int userId=Integer.parseInt(userIdValue);
		int id=Integer.parseInt(idValue);
		boolean b=serviceImpl.updateNote(content, createTime, title, subContent, userId, id);
		
	}
	
	@RequestMapping(value = "DeleteNoteServlet", method=RequestMethod.POST)
	@ResponseBody 
	public void DeleteNoteServlet(@RequestParam("id") String idValue) {
		int id=Integer.parseInt(idValue);
		boolean b=serviceImpl.deleteNote(id);
		
	}
}
