package practice02_member.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet("/logoutMember")
public class LogoutMember extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 세션 전체 제거
		HttpSession session = request.getSession();
		session.invalidate();// session 전체 속성 제거, 세션 종료
		
		 String jsScript = """
					<script>
						alert('로그아웃 되었습니다.');
					  location.href = 'mainMember';
				    </script>""";
		 
		 response.setContentType("text/html; charset=utf-8");
		 PrintWriter out = response.getWriter();
		 out.print(jsScript);
		
	}

}
