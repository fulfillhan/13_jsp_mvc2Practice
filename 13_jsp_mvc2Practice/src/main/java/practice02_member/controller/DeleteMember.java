package practice02_member.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import practice02_member.dao.MemberDAO;


@WebServlet("/deleteMember")
public class DeleteMember extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private String  profileRepositoryPath = FileConfig.PROFILE_REPOSITORY_PATH;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		request.setAttribute("memberId", (String) session.getAttribute("memberId"));
		
		RequestDispatcher dis = request.getRequestDispatcher("practice02_memberEx/mDelete.jsp");
		dis.forward(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			// 회원 탈퇴(세션 종료+ 프로파일 버리기)
		
		HttpSession session = request.getSession();
		String memberId = (String)session.getAttribute("memberId");
		String profileUUID = MemberDAO.getInstance().getMemberDetail(memberId).getProfileUUID();
		
		//먼저, 프로파일 삭제하기
		new File(profileRepositoryPath+profileUUID).delete();
		//sql삭제 하기
		MemberDAO.getInstance().deleteMember(memberId);
		// 세션 삭제하기
		session.invalidate();
		
		String jsScript="""
				<script>
					alert("회원 탈퇴 되었습니다.");
					loction.href='mainBoard';
				<script>
				""";
		 PrintWriter out= response.getWriter();
		 out.print(jsScript);
	}

}
