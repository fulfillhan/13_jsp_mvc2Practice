package practice03_boardAdvanced.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import com.mysql.cj.exceptions.RSAException;

import practice03_boardAdvanced.dto.MainBoardDTO;

public class BoardAdvancedDAO {
	
	private  BoardAdvancedDAO() {}
	private static BoardAdvancedDAO instance = new BoardAdvancedDAO();
	public static BoardAdvancedDAO getInstance() {
		return instance;
	}
	
	private Connection conn= null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	
	private void getConnection() {
		
		try {
			Context initctx = new InitialContext();
			Context envctx = (Context)initctx.lookup("java:comp/env");
			DataSource ds = (DataSource)envctx.lookup("jdbc/boardAdvanced");
			conn = ds.getConnection();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	private void getClose() {
		if(rs != null) {try {rs.close();} catch (Exception e) {e.printStackTrace();}}
		if(pstmt != null) {try {pstmt.close();} catch (Exception e) {e.printStackTrace();}}
		if(conn != null) {try {conn.close();} catch (Exception e) {e.printStackTrace();}}
		
		
	}
	public int getAllBoardCnt(String searchKeyword, String searchWord) {
		int allBoardCnt = 0;
		
		try {
			getConnection();
			
			String sql="";
			if(searchKeyword.equals("total")) {
				if(searchWord.equals("")) {// 완전 기본(아무것도 입력하지 않을때)
					//전체 조회
					sql="""
							SELECT COUNT(*)
							FROM MAIN_BOARD
							""";
					 pstmt = conn.prepareStatement(sql);
				}
				else {
					//searchWord에 입력값이 하나라도 있을때 조건은 제목, 작가중에 검색하게됨
					sql = """
							SELECT COUNT(*)
							FROM MAIN_BOARD
							WHERE SUBJECT LIKE CONCAT('%', ? , '%')
							OR WRITER LIKE CONCAT('%', ? , '%')
							""";
					pstmt  = conn.prepareStatement(sql);
					pstmt.setString(1, searchWord);
					pstmt.setString(2, searchWord);
				}
			}
			else {
				//검색어에 'total' 이 아닌 다른값도 들어올때
				sql = "SELECT COUNT(*)"+
				      "FROM MAIN_BOARD"+
					   "WHERE"+searchKeyword+"LIKE CONCAT('%', ? , '%')";
				 pstmt = conn.prepareStatement(sql);
				 pstmt.setString(1, searchWord);
			}
			 rs = pstmt.executeQuery();
			
			if(rs.next()) {
				allBoardCnt = rs.getInt(1);// 첫번째 열의 값을 가져온다. 총 게시글의 수
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			getClose();
		}
		return allBoardCnt;
	}
	public ArrayList<MainBoardDTO> getBoardList(String searchKeyword, String searchWord, int startBoardIdx, int onePageViewCnt) {
		ArrayList<MainBoardDTO> boardList = new ArrayList<>();
		
		try {
			getClose();
			String sql = "";
			if(searchKeyword.equals("total")) {
				if(searchWord.equals("")) {
					//전체 조회
					sql = """
							SELECT * FROM MAIN_BOARD
							ORDER BY ENROLL_AT DESC
							LIMIT ?,?
							""";
					 pstmt = conn.prepareStatement(sql);
					 pstmt.setInt(1, startBoardIdx);
					 pstmt.setInt(2, onePageViewCnt);
				}
				else {
					//검색어에 입력이 들어왔을때
					sql="""
							SELECT * 
							FROM MAIN_BOARD
							WHERE SUBJECT LIKE CONCAT('%', ? , '%')
							OR WRITER LIKE CONCAT('%', ? , '%')
							ORDER BY ENROLL_AT DESC
							LIMIT ?,?
							""";
					 pstmt = conn.prepareStatement(sql);
					 pstmt.setString(1, searchWord);
					 pstmt.setString(2, searchWord);
					 pstmt.setInt(3, startBoardIdx);
					 pstmt.setInt(4, onePageViewCnt);
				}
			}
			else {
				// 검색키워드가 입력되었을때
				sql = "SELECT * FROM MAIN_BOARD"+
				      "WHERE"+searchKeyword+"LIKE CONCAT('%', ? , '%')"+
				      "ORDER BY ENROLL_AT DESC"+
				      "LIMIT ?,?";
				 pstmt = conn.prepareStatement(sql);
				 pstmt.setString(1, searchWord);
				 pstmt.setInt(2, startBoardIdx);
				 pstmt.setInt(3, onePageViewCnt);
			}
			 rs = pstmt.executeQuery();
			 
			 while(rs.next()) {
				 MainBoardDTO mainBoardDTO = new MainBoardDTO();
				 mainBoardDTO.setBoardId(rs.getLong("BOARD_ID"));
				 mainBoardDTO.setWriter(rs.getString("WRITER"));
				 mainBoardDTO.setSubject(rs.getString("SUBJECT"));
				 mainBoardDTO.setEnrollAt(rs.getDate("ENROLL_AT"));
				 mainBoardDTO.setReadCnt(rs.getLong("READ_CNT"));
				 boardList.add(mainBoardDTO);
			 }
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			getConnection();
			
		}
		return boardList;
	}
	
}
