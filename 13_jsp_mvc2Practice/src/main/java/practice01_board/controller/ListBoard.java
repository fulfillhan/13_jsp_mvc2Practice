package practice01_board.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import practice01_board.dao.BoardDAO;
import practice01_board.dto.BoardDTO;


@WebServlet("/bList")// 해당 서블릿이 bList와 일치하는 url을 처리한다.
public class ListBoard extends HttpServlet {
	private static final long serialVersionUID = 1L;
       //저장한 데이터를 가지고와서 사용자에게 보여주기
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//ArrayList<BoardDTO> boardList = BoardDAO.getInstance().getBoardList();
		
		//서블릿에서 jsp페이지로 데이터를 전달하는 메서드
		request.setAttribute("boardList", BoardDAO.getInstance().getBoardList());
		
		RequestDispatcher dis = request.getRequestDispatcher("practice01_boardEx/bList.jsp");
		dis.forward(request, response);
	}

	

}
