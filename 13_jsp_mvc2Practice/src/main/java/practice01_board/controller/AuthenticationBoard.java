package practice01_board.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import practice01_board.dao.BoardDAO;
import practice01_board.dto.BoardDTO;


@WebServlet("/bAuthentication")
public class AuthenticationBoard extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
		
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		 long boardId = Long.parseLong(request.getParameter("boardId"));
		  BoardDTO boardDTO = BoardDAO.getInstance().getBoardDetail(boardId);
		 
		 
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
