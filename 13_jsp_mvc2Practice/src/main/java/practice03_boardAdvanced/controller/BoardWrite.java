package practice03_boardAdvanced.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import practice02_member.dao.MemberDAO;
import practice03_boardAdvanced.dto.MainBoardDTO;


@WebServlet("/boardWrite")
public class BoardWrite extends HttpServlet {
	private static final long serialVersionUID = 1L;
  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	RequestDispatcher dis = request.getRequestDispatcher("practice03_boardAdvancedEx/boardWrite.jsp");
	dis.forward(request, response);
	
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		
		MainBoardDTO mainBoardDTO = new MainBoardDTO();
		mainBoardDTO.setWriter(request.getParameter("writer"));
		mainBoardDTO.setSubject(request.getParameter("subject"));
		mainBoardDTO.setPasswd(request.getParameter("passwd"));
		mainBoardDTO.setContent(request.getParameter("content"));
		
		MemberDAO.getInstance().insertBoard(mainBoardDTO);
		
		String jsScript = """
				<script>
					alert("게시글이 등록되었습니다.");
					location.href='boardList';
				</script>
					
				""";
		
		 response.setContentType("text/html; charset=utf-8");
		 PrintWriter out = response.getWriter();
		 out.print(jsScript);
		
	}

}
