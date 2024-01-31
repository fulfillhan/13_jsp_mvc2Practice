package practice02_member.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import practice02_member.dto.MemberDTO;


@WebServlet("/loginMember")
public class LoginMember extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
 
 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dis = request.getRequestDispatcher("practice02_memberEx/bLogin.jsp");
		dis.forward(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		MemberDTO memberDTO = new MemberDTO();
	    memberDTO.setMemberId(request.getParameter("memberId"));  
		memberDTO.setPasswd( request.getParameter("passwd"));   
		
		//-> 여기서부터 작성 및 JQEURY쓰임 다시 복습필요..
		
	}

}
