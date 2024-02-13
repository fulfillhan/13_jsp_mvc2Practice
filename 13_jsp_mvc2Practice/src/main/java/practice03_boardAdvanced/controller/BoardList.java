package practice03_boardAdvanced.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.management.loading.PrivateClassLoader;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.jasper.compiler.NewlineReductionServletWriter;

import practice02_member.dao.MemberDAO;
import practice02_member.dto.MemberDTO;
import practice03_boardAdvanced.dao.BoardAdvancedDAO;
import practice03_boardAdvanced.dto.MainBoardDTO;


@WebServlet("/boardList")
public class BoardList extends HttpServlet {
	private static final long serialVersionUID = 1L;
       //변수: 검색키워드,검색어,한개페이지의 게시물수,현재페이지번호,전체게시글의 개수,전체페이지의 수,시작페이지, 끝 페이지, 시작하는 게시글의 인덱스
	
	private int onePageViewCnt=10;// 10을 기준으로
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		// searchKeyword 의 기준은 'total'
		 String searchKeyword = request.getParameter("searchKeyword");
		 if(searchKeyword == null) searchKeyword = "total";
		 
		 //searchWord 의 기준은 ""
		 String searchWord = request.getParameter("searchWord");
		 if(searchWord == null)   searchWord="";
		 
		 //하나의 페이지에서 보여지는 게시물의개수
		 //형변환 해주기 request.getParameter("onePageViewCnt") = 반환값은 String
		 //근데 무조건 반환이 아닌 해당값이 null 이 아닐때
		 if(request.getParameter("onePageViewCnt") != null) {
			 onePageViewCnt = Integer.parseInt(request.getParameter("onePageViewCnt"));
		 }
		 
		 
		 String temp =  request.getParameter("currentPageNumber");
		 if(temp == null) {// 첫 페이지는 '1'
			 temp="1";
		 }
		 int currentPageNumber =  Integer.parseInt(temp);//형변환 해주기
		 
		 // 전체 게시글의 개수은 db에서 가져온다
		 int allBoardCnt = BoardAdvancedDAO.getInstance().getAllBoardCnt(searchKeyword,searchWord);
		 
		 //전체 페이지의 수
		 int allPageCount = allBoardCnt / onePageViewCnt;
		 if(allBoardCnt % onePageViewCnt != 0) {
			 allPageCount++;
		 }
		 
		 int startPage = (currentPageNumber-1)/10*10+1;
		 if(startPage <1) {
			 startPage=1;
		 }
		 int endPage = startPage+9;
		 if(endPage > allPageCount) {
			 endPage=allPageCount;
		 }
		 int startBoardIdx = (currentPageNumber-1)*onePageViewCnt;
		 
			ArrayList<MainBoardDTO> boardList = BoardAdvancedDAO.getInstance().getBoardList(searchKeyword, searchWord,
					startBoardIdx, onePageViewCnt);
			request.setAttribute("boardList", boardList);
			request.setAttribute("onePageViewCnt", onePageViewCnt);
			request.setAttribute("allBoardCnt", allBoardCnt);
			request.setAttribute("startBoardIdx", startBoardIdx);
			request.setAttribute("currentPageNumber", currentPageNumber);
			request.setAttribute("startPage", startPage);
			request.setAttribute("endPage", endPage);
			request.setAttribute("allPageCnt", allPageCount);
			request.setAttribute("searchKeyword", searchKeyword);
			request.setAttribute("searchWord", searchWord);
			
			SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
			request.setAttribute("todday", sdf.format(new Date() ));
			
		
		RequestDispatcher dis = request.getRequestDispatcher("practice03_boardAdvancedEx/board/boardList.jsp");
		dis.forward(request, response);
	}
	
}
