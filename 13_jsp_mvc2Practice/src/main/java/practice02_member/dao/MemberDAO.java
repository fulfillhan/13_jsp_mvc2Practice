package practice02_member.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import practice02_member.dto.MemberDTO;

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
			
			String sql = """
					INSERT INTO MEMBER VALUES(? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , NOW()));
				
					""";
			pstmt = conn.prepareStatement(sql);
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
			
			pstmt = conn.prepareStatement("SELECT * FROM MEMBER WHERE MEMBER_ID");
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

	
	
	
	
}
