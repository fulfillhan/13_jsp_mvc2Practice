package practice02_member.controller;

//문자열.substring(index1) 특정문자열의 index1부터 끝까지.
			//lastIndexOf = 문자열을 오른쪽에서왼쪽으로 검색하며, 발견한 문자열의 위치를 반환

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

import com.oreilly.servlet.MultipartRequest;

import practice02_member.dao.MemberDAO;
import practice02_member.dto.MemberDTO;

@WebServlet("/registerMember")
public class RegisterMember extends HttpServlet {
	private static final long serialVersionUID = 1L;  
	private String profileRepositoryPath = FileConfig.PROFILE_REPOSITORY_PATH;
  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dis = request.getRequestDispatcher("practice02_memberEx/mRegister.jsp");
		dis.forward(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//memberId passwd memberNm profile sex birthAt hp email
		//String profileRepositoryPath = "C:\\Users\\15_web_hsh\\git\\13_jsp_mvc2Practice\\13_jsp_mvc2Practice\\src\\main\\webapp\\practice02_memberEx\\profileRepository\\";
		MultipartRequest multi = new MultipartRequest(request,profileRepositoryPath, 1024 * 1024 * 30 , "utf-8" );
		
		MemberDTO memberDTO = new MemberDTO();
		memberDTO.setMemberId( multi.getParameter("memberId")); 
		memberDTO.setPasswd(multi.getParameter("password"));
		memberDTO.setMemberNm(multi.getParameter("memberNm"));
		memberDTO.setSex(multi.getParameter("sex"));
		memberDTO.setBirthAt(multi.getParameter("birthAt"));
		memberDTO.setHp(multi.getParameter("hp"));
		if(multi.getParameter("smsRecvAgreeYn") == null) {memberDTO.setSmsRecvAgreeYn("N"); //'N'를 적용
		}
		else {                                            memberDTO.setSmsRecvAgreeYn(multi.getParameter("smsRecvAgreeYn"));//smsRecvAgreeYn DTO형태로 저장한다.
		}
		memberDTO.setEmail(multi.getParameter("email"));
		if(multi.getParameter("emailRecvAgreeYn") == null) {memberDTO.setEmailRecvAgreeYn("N");}
		else												memberDTO.setEmailRecvAgreeYn(multi.getParameter("emailRecvAgreeYn"));
		memberDTO.setZipcode(multi.getParameter("zipcode"));
		memberDTO.setRoadAddress(multi.getParameter("roadAddress"));
		memberDTO.setJibunAddress(multi.getParameter("jibunAddress"));
		memberDTO.setNamujiAddress(multi.getParameter("namujiAddress"));
		
		Enumeration<?> files = multi.getFileNames();
		
		
		// 프로파일 db로 넘기기
		if(files.hasMoreElements()) {
			String element = (String)files.nextElement();// 요소 가져오기
			//내가 올린파일을 갖고 와서 uuid 고유번호 넣어주기
			String originalFileName = multi.getOriginalFileName(element);
			String profileUUID = UUID.randomUUID() + originalFileName.substring(originalFileName.lastIndexOf("."));
			
			File file = new File(profileRepositoryPath+originalFileName);
			File renameFile = new File(profileRepositoryPath+profileUUID);
			file.renameTo(renameFile);
			
			//db에 저장
			memberDTO.setProfile(originalFileName);
			memberDTO.setProfileUUID(profileUUID);
			
		}
		
		MemberDAO.getInstance().registerMember(memberDTO);
		
		String jsScript = """
				<script>
					alert('회원가입 되었습니다.');
				  location.href='mainMember';
			    </script>""";
		
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();	
		out.print(jsScript);
		
		
		
	}
}
