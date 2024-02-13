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
import practice03_boardAdvanced.dto.MainBoardDTO;

@WebServlet("/boardUpdate")
public class BoardUpdate extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		MainBoardDTO mainBoardDTO = BoardAdvancedDAO.getInstance()
				.getBoardDetail(Long.parseLong(request.getParameter("boardId")));
		request.setAttribute("mainBoardDTO", mainBoardDTO);

		RequestDispatcher dis = request.getRequestDispatcher("practice03_boardAdvancedEx/board/boardUpdate.jsp");
		dis.forward(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		long boardId = Long.parseLong(request.getParameter("boardId"));

		MainBoardDTO mainBoardDTO = new MainBoardDTO();
		mainBoardDTO.setSubject(request.getParameter("subject"));
		mainBoardDTO.setContent(request.getParameter("content"));
		mainBoardDTO.setBoardId(boardId);

		BoardAdvancedDAO.getInstance().updateBoard(mainBoardDTO);

		String jsScript = "<script>";
		jsScript += "alert('수정 되었습니다.');";
		jsScript += "location.href='boardList';";
		jsScript += "</script>";

		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(jsScript);

	}

}
