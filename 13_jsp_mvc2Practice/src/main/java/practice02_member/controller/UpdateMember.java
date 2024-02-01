package practice02_member.controller;

import java.io.IOException;
import java.util.Enumeration;
import java.util.UUID;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;

import practice02_member.dao.MemberDAO;
import practice02_member.dto.MemberDTO;


@WebServlet("/updateMember")
public class UpdateMember extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String profileRepositoryPath = FileConfig.PROFILE_REPOSITORY_PATH;
       
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		MemberDTO memberDTO =  MemberDAO.getInstance().getMemberDetail((String)session.getAttribute("memberId"));
		request.setAttribute("membberDTO", memberDTO);	
		
		RequestDispatcher dis = request.getRequestDispatcher("practice02_memberEx/mUpdate.jsp");
		dis.forward(request, response);
	
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		MultipartRequest multi = new MultipartRequest(request, getServletInfo());
		
				MemberDTO memberDTO = new MemberDTO();
				memberDTO.setMemberId(multi.getParameter("memberId"));
				memberDTO.setMemberNm(multi.getParameter("memberNm"));
				memberDTO.setProfileUUID(multi.getParameter("profileUUID"));
				memberDTO.setProfile(multi.getParameter("profile"));
				memberDTO.setSex(multi.getParameter("sex"));
				memberDTO.setBirthAt(multi.getParameter("birthAt"));
				memberDTO.setHp(multi.getParameter("hp"));
				if(multi.getParameter("smsRecvAgreeYn") != null) { memberDTO.setSmsRecvAgreeYn(multi.getParameter("SmsRecvAgreeYn"));}
				else {                                             memberDTO.setSmsRecvAgreeYn("N");}
				memberDTO.setEmail(multi.getParameter("email"));
				if(multi.getParameter("emailRecvAgreeYn") != null) { memberDTO.setEmailRecvAgreeYn(profileRepositoryPath);}
				else {                                               memberDTO.setEmailRecvAgreeYn("N");}
				memberDTO.setZipcode(multi.getParameter("zipcode"));
				memberDTO.setRoadAddress(multi.getParameter("roadAddress"));
				memberDTO.setJibunAddress(multi.getParameter("jibunAddress"));
				memberDTO.setNamujiAddress(multi.getParameter("namujiAddress"));
				
				Enumeration<?> files = multi.getFileNames();
				
				
				if(files.hasMoreElements()) {
					String element = (String)files.nextElement();
					if(multi.getOriginalFileName(element) != null) {
						
						String originalFileName = multi.getOriginalFileName(element);
						String profileUUID = UUID.randomUUID()+ originalFileName.substring(originalFileName.lastIndexOf("."));
						
					}
					
					
				}
		        
		
	}

}
