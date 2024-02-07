package practice03_boardAdvanced.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import practice02_member.dao.MemberDAO;
import practice02_member.dto.MemberDTO;
import practice03_boardAdvanced.dto.MainBoardDTO;


@WebServlet("/boardList")
public class BoardList extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   private int onePageViewCnt; 
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String searchKeyword = request.getParameter("searchKeyword");
		if(searchKeyword == null) searchKeyword="total";// searchKeyword에 데이터를 입력하지않으면 기본값은 total
		
		 String searchWord = request.getParameter("searchWord");
		 if(searchWord == null) searchWord=""; //  데이터가 아무것도 없으면 작성할 수 있다.
		 
		 // 보여지는 페이지의 게시물 개수
		 if(request.getParameter("onePageViewCnt") != null ) {//
			onePageViewCnt = Integer.parseInt(request.getParameter("onePageViewCnt")) ;
		 }
		 //현재 보여지는 페이지의 번호
		 String temp =  request.getParameter("currentPageNumber");
		// 현재 보여지는 페이지가 없다면 즉, 사용자가 첫페이지를 열기전이라면
		 if(temp == null) {
			 temp="1";// '1 페이지'
		 }
		 int currentPageNumber = Integer.parseInt(temp);
		 
		 int allBoardCnt =  MemberDAO.getInstance().getAllVBoardCnt(searchKeyword,searchWord);// 전체 게시글의 개수의 데이터를 가지고 온다.
		 
		 //전체 페이지의 개수
		 int allPageCnt = allBoardCnt / onePageViewCnt;
		 if(allBoardCnt % onePageViewCnt != 0) { 
			 allPageCnt++;
		 }
		 
		int prevPageNumber = currentPageNumber -1;// 이전페이지의 숫자
		//현재 페이지가 속한 블록의 시작페이지
		int startPage = (prevPageNumber/10)*10+1;
		//시작 페이지가 음수가되는 걸 방지
		if(startPage < 1) {
			startPage = 1;
		}
		
		// 끝지점 알려주기
		int endPage = startPage + 9;
		//조건:끝나는 페이지가 전체 페이지 개수보다 크다면/ 끝나는 페이지는 전체페이지이다.
		if(endPage > allPageCnt) {
			endPage = allBoardCnt;
		}
		
		// 현재 보여질 게시글의의 시작 인덱스를 설정한다.
		
		int startBoardIdx = (currentPageNumber-1) * onePageViewCnt;
		
		//list를 배열화한다
		MemberDAO.getInstance().getBoardList(searchKeyword,searchWord,startBoardIdx,onePageViewCnt);
		
		 
	
	}


}
