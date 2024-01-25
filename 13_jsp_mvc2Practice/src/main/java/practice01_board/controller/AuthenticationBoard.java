package practice01_board.controller;

import java.awt.Menu;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
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

		  
		  BoardDTO boardDTO = BoardDAO.getInstance().getBoardDetail( Long.parseLong(request.getParameter("boardId")));
		  request.setAttribute("boardDTO", boardDTO);
		  
		  request.setAttribute("menu", request.getParameter("menu"));
		  
		  RequestDispatcher dis = request.getRequestDispatcher("practice01_boardEx/bAuthentication.jsp");
		 dis.forward(request, response);
		  //System.out.println(boardDTO);
		 
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//유저가 작성한 아이디아 비번을 받고 boardDTO형태로 저장하기
		BoardDTO boardDTO = new BoardDTO();
		boardDTO.setBoardId(Long.parseLong(request.getParameter("boardId"))); 
		boardDTO.setPassword(request.getParameter("password"));     

		//DAO로 보내기
		 String menu = request.getParameter("menu");//menu = 삭제,수정
		
		 
		 String jsScript = "";
		 
		if(BoardDAO.getInstance().checkAuthorizedUser(boardDTO)) {
			if(menu.equals("delete")){
				jsScript ="<script>";
				jsScript +="location.href='bDelete?boardId="+ boardDTO.getBoardId()+"';";
				jsScript +="alert('okay');";
				jsScript +="</script>";
			}
			else if(menu.equals("update")) {
				jsScript ="<script>";
				jsScript +="location.href='bUpdate?boardId="+ boardDTO.getBoardId()+"';";
				jsScript +="alert('okay');";
				jsScript +="</script>";
			}
		}else {
			jsScript = "<script>";
			jsScript += "alert('패스워드를 확인하세요');";
			jsScript += "history.go(-1);";
			jsScript += "</script>";
		}
		
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(jsScript);
		
		
	}

}
