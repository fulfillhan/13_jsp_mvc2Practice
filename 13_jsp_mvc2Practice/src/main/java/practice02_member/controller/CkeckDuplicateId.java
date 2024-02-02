package practice02_member.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import practice02_member.dao.MemberDAO;


@WebServlet("/checkDuplicateId")
public class CkeckDuplicateId extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();

		boolean dupleCheck = MemberDAO.getInstance().checkDuplicateId(request.getParameter("memberId"));

		if (dupleCheck) {
			out.print("isDuple");
		} else {
			out.print("isNotDuple");
		}
	}
	
}
