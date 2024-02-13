package practice03_boardAdvanced.dao;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.commons.dbcp2.PStmtKey;

import com.mysql.cj.exceptions.RSAException;

import practice03_boardAdvanced.dto.MainBoardDTO;
import practice03_boardAdvanced.dto.ReplyDTO;

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
	public MainBoardDTO getBoardDetail(Long boardId) {
		MainBoardDTO mainBoardDTO = new MainBoardDTO();
		
		String sql = ""; 
		
		try {
			getConnection();
			
			// 조회수 증가
			sql="""
					UPDATE MAIN_BOARD
					SET READ_CNT = READ_CNT+1
					WHERE BOARD_ID= ?
					""";
			 pstmt = conn.prepareStatement(sql);
			 pstmt.setLong(1, boardId);
			 pstmt.executeUpdate();
			 
			 sql="""
			 		SELECT * 
			 		FROM MAIN_BOARD
			 		WHERE BOARD_ID =?
			 		""";
			pstmt =  conn.prepareStatement(sql);
			pstmt.setLong(1,boardId);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				mainBoardDTO.setBoardId(rs.getLong("BOARD_ID"));
				mainBoardDTO.setSubject(rs.getString("SUBJECT"));
				mainBoardDTO.setReadCnt(rs.getLong("READ_CNT"));
				mainBoardDTO.setWriter(rs.getString("WRITER"));
				mainBoardDTO.setEnrollAt(rs.getDate("ENROLL_AT"));
				mainBoardDTO.setContent(rs.getString("CONTENT"));
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			getClose();
		}
		return mainBoardDTO;
	}
	public int getAllReplyCnt(Long boardId) {
		
		int totalReplyCnt = 0;
		
		try {
			getConnection();
			String sql= """
					SELECT COUNT(*)
					FROM REPLY_BOARD
					WHERE BOARD_ID = ?
					""";
			 pstmt = conn.prepareStatement(sql);
			 pstmt.setLong(1, boardId);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				 totalReplyCnt= rs.getInt(1);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			getClose();
		}
		return totalReplyCnt;
	}
	public ArrayList<ReplyDTO> getReplyList(Long boardId) {

		ArrayList<ReplyDTO> replyList= new ArrayList<ReplyDTO>();
		
		try {
			getConnection();
			
			String sql = """
					SELECT *
					FROM REPLY_BOARD
					WHERE BOARD_ID=?
					""";
			 pstmt = conn.prepareStatement(sql);
			 pstmt.setLong(1, boardId);
			  rs = pstmt.executeQuery();
			 
			  while(rs.next()) {
				  ReplyDTO replyDTO = new ReplyDTO();
				  replyDTO.setBoardId(rs.getLong("BOARD_ID"));
				  replyDTO.setWriter(rs.getString("WRITER"));
				  replyDTO.setEnrollAt(rs.getDate("ENROLL_AT"));
				  replyDTO.setReplyId(rs.getLong("REPLY_ID"));
				  replyDTO.setContent(rs.getString("CONTENT"));
				  replyDTO.setPasswd(rs.getString("PASSWD"));
				  replyList.add(replyDTO);
			  }
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			getClose();
		}
		return replyList;
	}
	public void insertReply(ReplyDTO replyDTO) {
		
		
		try {
			getConnection();
			
			String sql = """
					INSERT INTO REPLY_BOARD (WRITER, CONTENT, PASSWD, BOARD_ID)
					VALUES(?,?,?,?)
					
					""";
			 pstmt = conn.prepareStatement(sql);
			 pstmt.setString(1, replyDTO.getWriter());
			 pstmt.setString(2, replyDTO.getContent());
			 pstmt.setString(3, replyDTO.getPasswd());
			 pstmt.setLong(4, replyDTO.getBoardId());
			 pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			getClose();
		}
	}
	public ReplyDTO getReplyDetail(long replyId) {
		 ReplyDTO replyDTO = new ReplyDTO();
		 
		try {
			getConnection();
			
			String sql = """
					SELECT *
					FROM REPLY_BOARD
					WHERE REPLY_ID=?
					""";
			 pstmt = conn.prepareStatement(sql);
			 pstmt.setLong(1, replyId);
			  rs = pstmt.executeQuery();
			  
			  if(rs.next()) {
				 
				  replyDTO.setReplyId(rs.getLong("REPLY_ID"));
				  replyDTO.setWriter(rs.getString("WRITER"));
				  replyDTO.setEnrollAt(rs.getDate("ENROLL_AT"));
				  replyDTO.setPasswd(rs.getString("PASSWD"));
				  replyDTO.setContent(rs.getString("CONTENT"));
				  replyDTO.setBoardId(rs.getLong("BOARD_ID"));
				  
			  }
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			getClose();
		}
		return replyDTO;
	}
	public boolean checkValidId(ReplyDTO replyDTO) {
		boolean isValidId = false;
		
		try {
			getConnection();
			
			String sql = """
					SELECT * 
					FROM REPLY_BOARD
					WHERE REPLY_ID=? AND PASSWD=?
					""";
			 pstmt = conn.prepareStatement(sql);
			 pstmt.setLong(1, replyDTO.getReplyId());
			 pstmt.setString(2, replyDTO.getPasswd());
			  rs =  pstmt.executeQuery();
			  
			  if(rs.next()) {
				  isValidId=true;
			  }
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			getClose();
		}
		return isValidId;
		
	}
	public boolean updateReply(ReplyDTO replyDTO) {
		boolean isCheck = false;
		
		try {
			
			getConnection();
			
			if(checkValidId(replyDTO)) {// 회원인증이 true 라면
			
			String sql = """
					UPDATE REPLY_BOARD 
					SET CONTENT=?,
					    ENROLL_AT = NOW()
					WHERE REPLY_ID=?
					""";
			 pstmt = conn.prepareStatement(sql);
			 pstmt.setString(1, replyDTO.getContent());
			 pstmt.setLong(2, replyDTO.getReplyId());
			 pstmt.executeUpdate();
			 isCheck=true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			getClose();
		}
		return isCheck;
	}

	public boolean deleteReply(ReplyDTO replyDTO) {
		boolean isDelete = false;

		try {
			getConnection();

			if (checkValidId(replyDTO)) {
				String sql = """
						DELETE FROM REPLY_BOARD
						WHERE REPLY_ID=?
						""";
				pstmt = conn.prepareStatement(sql);
				pstmt.setLong(1, replyDTO.getReplyId());
				pstmt.executeUpdate();
				isDelete = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getClose();
		}

		return isDelete;

	}
	public boolean checkValidUser(MainBoardDTO mainBoardDTO) {
		boolean isChekcUser = false;
		
		try {
			getConnection();
			
			String sql = """
					SELECT *
					FROM MAIN_BOARD
					WHERE BOARD_ID=? AND PASSWD=?
					""";
			 pstmt = conn.prepareStatement(sql);
			 pstmt.setLong(1, mainBoardDTO.getBoardId());
			 pstmt.setString(2, mainBoardDTO.getPasswd());
			 rs = pstmt.executeQuery();
			
			 if(rs.next()) {
				 isChekcUser=true;
			 }
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			getClose();
		}
		
		return isChekcUser;
	}
	public void updateBoard(MainBoardDTO mainBoardDTO) {
		
		try {
			getConnection();
			
			String sql = """
					UPDATE MAIN_BOARD
					SET SUBJECT=?,
					CONTENT=?
					WHERE BOARD_ID=?
					""";
			 pstmt = conn.prepareStatement(sql);
			 pstmt.setString(1, mainBoardDTO.getSubject());
			 pstmt.setString(2, mainBoardDTO.getContent());
			 pstmt.setLong(3, mainBoardDTO.getBoardId());
			 pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			getClose();
		}
	}
	public void deleteBoard(long boardId) {
		
		try {
			getConnection();
			
			String sql = """
					DELETE FROM MAIN_BOARD
					WHERE BOARD_ID=?
					""";
			 pstmt = conn.prepareStatement(sql);
			 pstmt.setLong(1, boardId);
			 pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			getClose();
		}
	}
	
	
}
