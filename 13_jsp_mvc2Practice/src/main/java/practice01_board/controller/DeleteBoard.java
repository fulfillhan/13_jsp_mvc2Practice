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
// 오류 발생 : boardId 형변환에서 오류 발생 .. 이유는?
@WebServlet("/bDelete")
public class DeleteBoard extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setAttribute("boardId", request.getParameter("boardId"));
		
		RequestDispatcher dis = request.getRequestDispatcher("practice01_boardEx/bDelete.jsp");
		dis.forward(request, response);
		//-> boardId를 받아서 jsp화면 넘겨주기
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// 여기서 계속 오류..
		BoardDAO.getInstance().deleteBoard(Long.parseLong(request.getParameter("boardId")));
	
		String jsScript = """ 
				<script>
					alert('삭제되었습니다.');
					location.href = 'bList';
				</script>""";
	
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(jsScript);
		
		 
		 
	}

}
