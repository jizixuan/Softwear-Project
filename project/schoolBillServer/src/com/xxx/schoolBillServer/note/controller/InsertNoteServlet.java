package com.xxx.schoolBillServer.note.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xxx.schoolBillServer.note.service.NoteServiceImpl;

/**
 * Servlet implementation class InsertNoteServlet
 */
@WebServlet("/InsertNoteServlet")
public class InsertNoteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InsertNoteServlet() {
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
		String content=request.getParameter("content");
		String createTime=request.getParameter("createTime");
		String title=request.getParameter("title");
		String subContent=request.getParameter("subContent");
		int userId=Integer.parseInt(request.getParameter("userId"));
		int id=new NoteServiceImpl().insertNote(content, createTime, title, subContent, userId);
		PrintWriter w = response.getWriter();
		w.write(id+"");
		doGet(request, response);
	}

}
