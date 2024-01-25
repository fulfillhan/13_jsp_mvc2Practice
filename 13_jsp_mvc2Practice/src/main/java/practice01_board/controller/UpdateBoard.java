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

// 오류= boardId 형변환에서 계속 오류가발생함(delete에서도 마찬가지)
@WebServlet("/bUpdate")
public class UpdateBoard extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// long boardId =
		// Long.parseLong(request.getParameter("boardOd"));//request.getParameter("boardOd");
		BoardDTO boardDTO = BoardDAO.getInstance().getBoardDetail(Long.parseLong(request.getParameter("boardId")));
		request.setAttribute("boardDTO", boardDTO);

		RequestDispatcher dis = request.getRequestDispatcher("practice01_boardEx/bUpdate.jsp");
		dis.forward(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");// 수정한거 인코딩

		// dto형태로 묶기(boardId, subject, content 받기)
		BoardDTO boardDTO = new BoardDTO();
		boardDTO.setBoardId(Long.parseLong(request.getParameter("boardId")));
		boardDTO.setSubject(request.getParameter("subject"));
		boardDTO.setContent(request.getParameter("content"));

		// dao로 전달하기
		BoardDAO.getInstance().updateBoard(boardDTO);

		// 수정이 완료되면 알림 설정
		
		String jsScript = """
				<script>
					alert("수정되었습니다");
					location.href="bList";
					</script>
				""";
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(jsScript);

	}

}
