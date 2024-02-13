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


@WebServlet("/replyDelete")
public class ReplyDelete extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		
		ReplyDTO replyDetail=  BoardAdvancedDAO.getInstance().getReplyDetail(Long.parseLong(request.getParameter("boardId")));
		request.setAttribute("replyDTO", replyDetail);
		
		RequestDispatcher dis = request.getRequestDispatcher("practice03_boardAdvancedEx/reply/replyDelete.jsp");
		 dis.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		
		 ReplyDTO replyDTO = new ReplyDTO();
		 Long.parseLong(request.getParameter("booardId"));
		 Long.parseLong( request.getParameter("replyId"));
		 request.getParameter("passwd");
		 
		boolean isDeleteReply = BoardAdvancedDAO.getInstance().deleteReply(replyDTO);
		
		String jsScript ="";
		if(isDeleteReply) {
			jsScript = "<script>";
			jsScript += "alert('삭제 되었습니다.');";
			jsScript += "location.href='boardDetail?boardId=" + replyDTO.getBoardId() + "'";
			jsScript += "</script>";
		}
		else {
			jsScript = "<script>";
			jsScript += "alert('패스워드를 확인하세요.');";
			jsScript += "history.go(-1);";
			jsScript += "</script>";
		}
		 response.setContentType("text/html; charset=utf-8");
		   PrintWriter out = response.getWriter();	
		   out.print(jsScript);
		   
	}

}
