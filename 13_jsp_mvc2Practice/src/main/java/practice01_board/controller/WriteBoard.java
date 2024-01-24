package practice01_board.controller;

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

@WebServlet("/bWrite")
public class WriteBoard extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dis = request.getRequestDispatcher("practice01_boardEx/bWrite.jsp");
		dis.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		
		
		//요청한 데이터를 DTO형태로 만들기
		BoardDTO boardDTO = new BoardDTO();
		boardDTO.setWriter(request.getParameter("writer"));
		boardDTO.setSubject(request.getParameter("subject"));
		boardDTO.setEmail(request.getParameter("email"));
		boardDTO.setPassword(request.getParameter("password"));
		boardDTO.setContent(request.getParameter("content"));
		
		//만든DTO를 DAO 메서드 호출하여 정보를 데이터베이스에 등록한다.	
		BoardDAO.getInstance().insertBoard(boardDTO);
		
		response.setContentType("text/html; charset=UTF-8");
		
		PrintWriter out = response.getWriter();
		
		String jsScript = """
				<script>
					alert("게시글이 등록되었습니다.");
					location.href='bList';
				</script>
				""";
		out.print(jsScript);
		
	}

}
