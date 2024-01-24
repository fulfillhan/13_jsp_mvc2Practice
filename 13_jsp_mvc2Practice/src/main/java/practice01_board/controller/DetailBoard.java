package practice01_board.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import practice01_board.dao.BoardDAO;
import practice01_board.dto.BoardDTO;

@WebServlet("/bDetail")
public class DetailBoard extends HttpServlet {
	private static final long serialVersionUID = 1L;
 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		 long boardId =	Long.parseLong(request.getParameter("boardId"));
		  BoardDTO boardDTO=  BoardDAO.getInstance().getBoardDetail(boardId);
		  request.setAttribute("boardDTO", boardDTO);// BoardDTO 형태로 데이터가 왔기 때문에
	
		  RequestDispatcher dis = request.getRequestDispatcher("practice01_boardEx/bDetail.jsp");// 이곳의 jsp형태로 화면 출력한다.
		  dis.forward(request, response);
	}
		

}
