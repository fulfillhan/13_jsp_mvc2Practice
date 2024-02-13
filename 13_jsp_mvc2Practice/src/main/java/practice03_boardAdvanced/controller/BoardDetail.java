package practice03_boardAdvanced.controller;

import java.io.IOException;

import javax.security.auth.message.callback.SecretKeyCallback.Request;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import practice03_boardAdvanced.dao.BoardAdvancedDAO;


@WebServlet("/boardDetail")
public class BoardDetail extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 Long boardId = Long.parseLong(request.getParameter("boardId")); 
		 //  BoardAdvancedDAO.getInstance().getBoardDetail(boardId);
		   request.setAttribute("mainBoardDTO",BoardAdvancedDAO.getInstance().getBoardDetail(boardId));
		 // 댓글 갯수
		// BoardAdvancedDAO.getInstance().getAllReplyCnt(boardId);
		   request.setAttribute("allReplyCnt",  BoardAdvancedDAO.getInstance().getAllReplyCnt(boardId));
		 //댓글 리스트
		 //BoardAdvancedDAO.getInstance().getReplyList(boardId);
		 request.setAttribute("replyList",  BoardAdvancedDAO.getInstance().getReplyList(boardId));
		 
		 RequestDispatcher dis = request.getRequestDispatcher("practice03_boardAdvancedEx/board/boardList.jsp");
		 dis.forward(request, response);
	}


}
