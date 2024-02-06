package practice02_member.controller;

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
import practice02_member.dto.MemberDTO;


@WebServlet("/loginMember")
public class LoginMember extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dis = request.getRequestDispatcher("practice02_memberEx/mLogin.jsp");
		dis.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		MemberDTO memberDTO = new MemberDTO();
		memberDTO.setMemberId(request.getParameter("memberId"));
		memberDTO.setPasswd(request.getParameter("passwd"));

		boolean isLogin = MemberDAO.getInstance().loginMember(memberDTO);

		String isAuthorized = "false";
		if (isLogin) {
			HttpSession session = request.getSession();
			session.setAttribute("memberId", request.getParameter("memberId"));
			isAuthorized = "true";

		}
		PrintWriter out = response.getWriter();
		out.print(isAuthorized);

	}

}
