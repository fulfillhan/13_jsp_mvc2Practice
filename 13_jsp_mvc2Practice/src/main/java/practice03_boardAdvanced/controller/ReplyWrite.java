package practice03_boardAdvanced.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import practice03_boardAdvanced.dao.BoardAdvancedDAO;
import practice03_boardAdvanced.dto.ReplyDTO;


@WebServlet("/replyWrite")
public class ReplyWrite extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		 request.setAttribute("boardId",  Long.parseLong(request.getParameter("boardId")));
		 
		 RequestDispatcher dis = request.getRequestDispatcher("practice03_boardAdvancedEx/reply/replyWriter.jsp");
		 dis.forward(request, response);
	}
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		
		ReplyDTO replyDTO = new ReplyDTO();
		replyDTO.setBoardId(Long.parseLong(request.getParameter("boardId")));
		replyDTO.setWriter(request.getParameter("writer"));
		replyDTO.setPasswd(request.getParameter("passwd"));
		replyDTO.setContent(request.getParameter("content"));
		
		BoardAdvancedDAO.getInstance().insertReply(replyDTO);

		String jsScript = "<script>";
		jsScript += "alert('댓글이 등록되었습니다.');";
		jsScript += "location.href='boardDetail?boardId=" + replyDTO.getBoardId() + "';";
		jsScript += "</script>";

		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(jsScript);

	}

}
