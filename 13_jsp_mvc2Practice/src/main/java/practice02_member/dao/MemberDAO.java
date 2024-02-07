package practice02_member.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.security.auth.Subject;
import javax.sql.DataSource;

import org.apache.jasper.tagplugins.jstl.core.If;

import practice02_member.dto.MemberDTO;
import practice03_boardAdvanced.dto.MainBoardDTO;

public class MemberDAO {
	
	private MemberDAO() {}
	private static MemberDAO instance = new MemberDAO();
	public static MemberDAO getInstance() {
		return instance;
	}
	
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	private void getConnection() {
		try {
			Context initCtx = new InitialContext();
    		Context envCtx = (Context)initCtx.lookup("java:comp/env");
    		DataSource ds = (DataSource)envCtx.lookup("jdbc/member");
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

	public void registerMember(MemberDTO memberDTO) {
		try {
			getConnection();
			
//			String sql = """
//					INSERT INTO MEMBER VALUES(? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , NOW())");
//					""";
			pstmt = conn.prepareStatement("INSERT INTO MEMBER VALUES(? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , NOW())");
			pstmt.setString(1, memberDTO.getMemberId());
			pstmt.setString(2, memberDTO.getMemberNm());
			pstmt.setString(3, memberDTO.getPasswd());
			pstmt.setString(4, memberDTO.getProfile());
			pstmt.setString(5, memberDTO.getProfileUUID());
			pstmt.setString(6, memberDTO.getSex());
			pstmt.setString(7, memberDTO.getBirthAt());
			pstmt.setString(8, memberDTO.getHp());
			pstmt.setString(9, memberDTO.getSmsRecvAgreeYn());
			pstmt.setString(10, memberDTO.getEmail());
			pstmt.setString(11, memberDTO.getEmailRecvAgreeYn());
			pstmt.setString(12, memberDTO.getZipcode());
			pstmt.setString(13, memberDTO.getRoadAddress());
			pstmt.setString(14, memberDTO.getJibunAddress());
			pstmt.setString(15, memberDTO.getNamujiAddress());
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			getClose();
		}
		//System.out.println(memberDTO);
		
	}

	public boolean loginMember(MemberDTO memberDTO) {
		boolean isLogin = false;

		try {
			getConnection();

			pstmt = conn.prepareStatement("SELECT * FROM MEMBER WHERE MEMBER_ID=? AND PASSWD=?");
			pstmt.setString(1, memberDTO.getMemberId());
			pstmt.setString(2, memberDTO.getPasswd());
			rs = pstmt.executeQuery();

			if (rs.next()) {
				isLogin = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getClose();
		}

		return isLogin;

	}

	public MemberDTO getMemberDetail(String memberId) {
		MemberDTO memberDTO = new MemberDTO();
		
		try {
			getConnection();
			
			pstmt = conn.prepareStatement("SELECT * FROM MEMBER WHERE MEMBER_ID = ?");
			pstmt.setString(1, memberId);
			 rs = pstmt.executeQuery();
			 
			 if(rs.next()) {
				memberDTO.setMemberId(rs.getString("MEMBER_ID")); 
				memberDTO.setMemberNm(rs.getString("MEMBER_NM"));
				memberDTO.setProfileUUID(rs.getString("PROFILE_UUID"));
				memberDTO.setSex(rs.getString("SEX"));
				memberDTO.setBirthAt(rs.getString("BIRTH_AT"));
			    memberDTO.setHp(rs.getString("HP"));
			    memberDTO.setSmsRecvAgreeYn(rs.getString("SMS_RECV_AGREE_YN"));
			    memberDTO.setEmail(rs.getString("EMAIL"));
			    memberDTO.setEmailRecvAgreeYn(rs.getString("EMAIL_RECV_AGREE_YN"));
			    memberDTO.setZipcode(rs.getString("ZIPCODE"));
            	memberDTO.setRoadAddress(rs.getString("ROAD_ADDRESS"));
            	memberDTO.setJibunAddress(rs.getString("JIBUN_ADDRESS"));
            	memberDTO.setNamujiAddress(rs.getString("NAMUJI_ADDRESS"));
			    memberDTO.setJoinAt(rs.getDate("JOIN_AT"));
				
			 }
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			getClose();
		}
		//System.out.println(memberDTO);
		return memberDTO;  // 정보들을 'memberDTO' 에 담아서 DAO로 가지고가야한다.
		 
	}

	public void updateMember(MemberDTO memberDTO) {
		
		try {
			getConnection();
			String sql = """
					UPDATE MEMBR
            		SET	   MEMBER_NM = ?,
            			   SEX = ?,
            		       BIRTH_AT = ?,
            		       HP = ?,
            		       SMS_RECV_AGREE_YN = ?,
            		       EMAIL = ?,
            		       EMAIL_RECV_AGREE_YN = ?,
            		       ZIPCODE = ?,
            		       ROAD_ADDRESS = ?,
            		       JIBUN_ADDRESS = ?,
            		       NAMUJI_ADDRESS = ?
						""";
          if(memberDTO.getProfile() != null) { // NULL 일 경우에도 SQL쿼리에 프로필 열이 추가되어 불필요한 값이 생성될 수 있다.
        	  sql += """
        	  		,
        	  		PROFILE=?,
        	  		PROFILE_UUID=?
        	  		""";
          }
          sql += "WHERE MEMBER_ID=?;";
          
			pstmt =conn.prepareStatement(sql);
			pstmt.setString(1, memberDTO.getMemberNm());
			pstmt.setString(2, memberDTO.getSex());
			pstmt.setString(3, memberDTO.getBirthAt());
			pstmt.setString(4, memberDTO.getHp());
            pstmt.setString(5, memberDTO.getSmsRecvAgreeYn());
			pstmt.setString(6, memberDTO.getEmail());
			pstmt.setString(7, memberDTO.getEmailRecvAgreeYn());
            pstmt.setString(8, memberDTO.getZipcode());
            pstmt.setString(9, memberDTO.getRoadAddress());
            pstmt.setString(10, memberDTO.getJibunAddress());
            pstmt.setString(11, memberDTO.getNamujiAddress());
            if(memberDTO.getProfile() == null) { // null이면 아이디값을 쿼리에 저장한다.
            	pstmt.setString(12, memberDTO.getMemberId());
            }else {
				pstmt.setString(12, memberDTO.getProfile());
				pstmt.setString(13, memberDTO.getProfileUUID());
				pstmt.setString(14, memberDTO.getMemberId());
			}
            pstmt.executeUpdate();
            
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			getClose();
		}
	}

	public void deleteMember(String memberId) {
		
		try {
			getConnection();
			
			String sql = "DELETE FROM MEMBER WHERE MEMBER_ID=?";
			 pstmt = conn.prepareStatement(sql);
			 pstmt.setString(1, memberId);
			 pstmt.executeUpdate();
			 
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			
			getClose();
		}
	}

	public boolean checkDuplicateId(String memberId) {
		
		boolean isDuple = false;
		
		try {
			getConnection();
			pstmt = conn.prepareStatement("SELECT * FROM MEMBER WHERE MEMBER_ID=?");
			pstmt.setString(1, memberId);
			 rs = pstmt.executeQuery();
			 
			 if(rs.next()) {
				 isDuple=true;
			 }
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			getClose();
		}
		return isDuple;
	}

	public void insertBoard(MainBoardDTO mainBoardDTO) {
		try {
			getConnection();
			
			String sql = """
					INSERT INTO MAIN_BOARD (WRITER, SUBJECT, CONTENT, PASSWD)
					VALUES (?,?,?,?);
					""";
			 pstmt = conn.prepareStatement(sql);
			 pstmt.setString(1, mainBoardDTO.getWriter());
			 pstmt.setString(2, mainBoardDTO.getSubject());
			 pstmt.setString(3, mainBoardDTO.getContent());
			 pstmt.setString(4, mainBoardDTO.getPasswd());
			 pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			getClose();
		}
		
	}

	public int getAllVBoardCnt(String searchKeyword, String searchWord) {
		int allBoardCnt = 0;
		
		try {
			getConnection();
			String sql = "";
			
			//검색키워드가 기본값(전체검색)일 때
			if(searchKeyword.equals("total")) {
				if(searchWord.equals("")) {
					// 검색어의 입력값이 없을때-> 전체 데이터를 조회한다.
					sql = """
							SELECT COUNT(*)
							FROM MAIN_BOARD
							""";
					 pstmt = conn.prepareStatement(sql);
				}
				else {
					//검색어의 입력값이 있을때->검색어의 입력값의 전체를 조회한다.
					sql = """
							SELECT COUNT(*)
							FROM MAIN_BOARD
							WHERE SUBJECT LIKE CONCAT('%', ? , '%')
							OR CONTENT LIKE CONCAT('%' , ? , '%')
							""";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, searchWord);
					pstmt.setString(2, searchWord);
				}
					
			}
			else {
				//검색키워드를 입력값이 있다면
				sql= "SELECT COUNT(*)"+
					"FROM MAIN_BOARD"+
					"WHERE "+ searchKeyword+"LIKE CONCAT('%' , ? , '%')";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, searchWord);
				
			}
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				allBoardCnt= rs.getInt(1);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			getClose();
		}
		
		return allBoardCnt;
	}

	public ArrayList<MainBoardDTO> getBoardList(String searchKeyword, String searchWord, int startBoardIdx, int onePageViewCnt) {
	
		ArrayList<MainBoardDTO> boardList= new ArrayList<>();
		
		try {
			getConnection();
			
			//-> 여기서부터 업데이트 필요
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			getClose();
		}
		
		
		return boardList;
	}

	
	
	
	
}
