package practice02_member.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import practice02_member.dao.MemberDAO;
import practice02_member.dto.MemberDTO;


@WebServlet("/detailMember")
public class DetailMember extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		HttpSession session = request.getSession();
		
		 String memberId = (String)session.getAttribute("memeberId");
		 MemberDTO memberDTO = MemberDAO.getInstance().getMemberDetail(memberId);//세션에서 가져온 'memberId' 속성을 매개변수로 넣어준다
		 request.setAttribute("memberDTO", memberDTO);
		 
		 RequestDispatcher dis = request.getRequestDispatcher("practice02_memberEx/mDetail.jsp");
		 dis.forward(request, response);
	
	}

	
	

}
	