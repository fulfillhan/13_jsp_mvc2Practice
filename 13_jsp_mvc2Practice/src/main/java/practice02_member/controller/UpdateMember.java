package practice02_member.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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
				
				if (files.hasMoreElements()) {
					String element = (String) files.nextElement();// 파일 요소를 가지고와서 String 타입의 element변수에 저장
					if (multi.getOriginalFileName(element) != null) {

						// 식별자인 memberId를 넣어 detail정보에 있던 기존의 파일uuid를 가지고온다.
						String deleteProfileUUID = MemberDAO.getInstance().getMemberDetail(multi.getParameter("memberId")).getProfileUUID();
						//  파일을 삭제한다.(File 객체를 생성해야함)
						new File(profileRepositoryPath + deleteProfileUUID).delete();
						
						String originalFileName = multi.getOriginalFileName(element);// 새로 수정하여 업로드하는 원본파일명을 가지고와서 변수에 할당한다.
						String profileUUID = UUID.randomUUID()+ originalFileName.substring(originalFileName.lastIndexOf("."));

						// 새롭게 업로드한 파일과 UUID를 dto형태로 보낸다.
						memberDTO.setProfile(originalFileName);
						memberDTO.setProfileUUID(profileUUID);

						// 새로운 업로드된 파일을 file객체에 생성
						File file = new File(profileRepositoryPath + originalFileName);
						// 새로운 uuid에 해당하는 파일도 File객체에 생성
						File renameFile = new File(profileRepositoryPath + profileUUID);
						// 마지막으로 업로드된 새로운 파일을 uuid가 있는 새로운 파일 이름을 변경한다.
						file.renameTo(renameFile);
						
					}
					
				}
				
		        MemberDAO.getInstance().updateMember(memberDTO);
		        

			    String jsScript = """
						<script>
							alert('수정 되었습니다.');
						  location.href='detailMember';
					    </script>""";	   
					   
				response.setContentType("text/html; charset=utf-8");
				PrintWriter out = response.getWriter();	
				out.print(jsScript);
		
	}

}
