package practice01_board.dao;

import java.nio.channels.SelectableChannel;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.print.attribute.PrintServiceAttribute;
import javax.sql.DataSource;

import practice01_board.dto.BoardDTO;

public class BoardDAO {
	
	private BoardDAO() {}
	private static BoardDAO instance = new BoardDAO();
	public static BoardDAO getInstance() {
		return instance;
	}
	
	//데이터베이스 연동객체 생성
	private Connection conn= null;//연동하기
	private PreparedStatement pstmt= null;//쿼리 준비하기
	private ResultSet rs = null;//쿼리 저장하기
	
	//연결 메서드
	private void getConnection() {
		
		try {
			
			Context initctx = new InitialContext();						
			Context envctx = (Context) initctx.lookup("java:comp/env");      
			DataSource ds = (DataSource) envctx.lookup("jdbc/board"); 	 //  envctx.lookup("이건연습임"); DB연결할때 SERVER에서 context.xml확장에서 변경해줘야함 
			conn = ds.getConnection();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//해지 메서드
	private void getClose() {
		
		if(rs != null)    try {rs.close();} catch (SQLException e) {e.printStackTrace();}
		if(pstmt != null) try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
		if(conn != null)  try {conn.close();} catch (SQLException e) {e.printStackTrace();}
	
	}
	
	public void insertBoard(BoardDTO boardDTO) {
		
		try {
			
			getConnection();
			// 가지고온 데이터를 데이터베이스에 저장하기
			String sql = """
					INSERT INTO BOARD (WRITER , PASSWORD , EMAIL , SUBJECT , CONTENT , READ_CNT , ENROLL_DT )
					VALUES (?,?,?,?,?,0,NOW())
					""";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, boardDTO.getWriter());
			pstmt.setString(2, boardDTO.getPassword());
			pstmt.setString(3, boardDTO.getEmail());
			pstmt.setString(4, boardDTO.getSubject());
			pstmt.setString(5, boardDTO.getContent());
			 
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getClose();
		}
	}
	
	public ArrayList<BoardDTO> getBoardList() {
		ArrayList<BoardDTO> boardList = new ArrayList<>();
		try {
			getConnection();

			// 데이터를 dao로 가지고 오기
			String sql = """
					SELECT * FROM BOARD
					""";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				BoardDTO boardDTO = new BoardDTO();
				boardDTO.setBoardId(rs.getLong("BOARD_ID"));
				boardDTO.setWriter(rs.getString("WRITER"));
				boardDTO.setSubject(rs.getString("SUBJECT"));
				boardDTO.setEnrollDt(rs.getDate("ENROLL_DT"));
				boardDTO.setReadCnt(rs.getLong("READ_CNT"));
				boardList.add(boardDTO);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getClose();
		}

		return boardList;
	}
	
	public BoardDTO getBoardDetail(long boardId) {
		BoardDTO boardDTO = new BoardDTO();
		try {
			getConnection();

			// 조회수 변경
			String sql = """
					UPDATE BOARD SET READ_CNT = READ_CNT+1 WHERE BOARD_ID=?
					""";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, boardId);
			pstmt.executeUpdate();

			// 일반 데이터 조회하기
			pstmt = conn.prepareStatement("SELECT * FROM BOARD WHERE BOARD_ID = ?");
			pstmt.setLong(1, boardId);// 이부분 알아보기!
			rs = pstmt.executeQuery();

			if (rs.next()) {
				boardDTO.setBoardId(boardId);
				boardDTO.setWriter(rs.getString("WRITER"));
				boardDTO.setEmail(rs.getString("EMAIL"));
				boardDTO.setSubject(rs.getString("SUBJECT"));
				boardDTO.setContent(rs.getString("CONTENT"));
				boardDTO.setReadCnt(rs.getLong("READ_CNT"));
				boardDTO.setEnrollDt(rs.getDate("ENROLL_DT"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getClose();
		}
		//System.out.println(boardDTO);
		return  boardDTO;
	}
	


}