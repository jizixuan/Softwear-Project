package com.xxx.schoolBillServer.note.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.xxx.schoolBillServer.entity.Note;
import com.xxx.schoolBillServer.note.service.NoteServiceImpl;

/**
 * Servlet implementation class GetNoteListServlet
 */
@WebServlet("/GetNoteListServlet")
public class GetNoteListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetNoteListServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		int userId=Integer.parseInt(request.getParameter("userId"));
		List<Note> notes=new NoteServiceImpl().getNoteList(userId);
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
		PrintWriter w = response.getWriter();
		w.write(jsonArray.toString());
		doGet(request, response);
	}

}
