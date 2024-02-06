package practice03_boardAdvanced.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

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
}
