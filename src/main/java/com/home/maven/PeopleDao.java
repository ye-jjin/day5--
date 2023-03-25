package com.home.maven;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class PeopleDao {
	
	//싱글톤
	private static PeopleDao instance;
	private PeopleDao() {}
	public static PeopleDao getInstance() {
		if(instance == null) {
			instance = new PeopleDao();
		}
		return instance;
	}
	
	//jdbc
	private final String DRIVER_NAME = "oracle.jdbc.driver.OracleDriver";
	private final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
	private final String ID = "yejin";
	private final String PW = "1234";
	
	//getConnection
	private Connection getConnection() {		
			try {
				// 오라클 드라이버를 메모리에 로딩
				Class.forName(DRIVER_NAME);
				Connection conn = DriverManager.getConnection(
						URL, ID, PW);
				//System.out.println("접속성공");
				return conn;
			} catch (Exception e) {
				e.printStackTrace(); 
			}
			return null;
		} 
		
		private void closeAll(Connection conn, PreparedStatement pstmt, ResultSet rs) {
			try {
				if (rs != null) rs.close();
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		//insert
		public boolean insert(PeopleVo peopleVo) {
			Connection conn = null; //접속
			PreparedStatement pstmt = null; //SQL 문장
			
			try {
				conn = getConnection();
				String sql = "insert into tbl_people(no,name,age,job)"
						+ "   	values(no.nextval,?, ?, ?)";
				
				pstmt = conn.prepareStatement(sql);
				
				
				pstmt.setString(1, peopleVo.getName());
				pstmt.setString(2, peopleVo.getAge());
				pstmt.setString(3, peopleVo.getJob());
				
				int count = pstmt.executeUpdate();
				
				if (count > 0) {
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				closeAll(conn, pstmt, null);
			}
			
			return false;
		}
		
		//select
		public List<PeopleVo> selectAll() {
			Connection conn = null; // 접속
			PreparedStatement pstmt = null; // SQL 문장
			ResultSet rs = null; // 결과 저장
			
			try {
				conn = getConnection();
				String sql = "select * from tbl_people";
				
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
				
				List<PeopleVo> list = new ArrayList<>();
				
				while (rs.next()) {
					
					String name = rs.getString("name");
					String age = rs.getString("age");
					String job = rs.getString("job");
					
					PeopleVo vo = new PeopleVo(name,age,job);				
					
					list.add(vo);
				}
				return list;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				closeAll(conn, pstmt, rs);
			}
			return null;
		}

		
		
		
}
