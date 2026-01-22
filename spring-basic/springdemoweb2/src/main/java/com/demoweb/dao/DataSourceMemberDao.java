package com.demoweb.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Component;

import com.demoweb.dto.MemberDto;

@Component("dataSourceMemberDao")
public class DataSourceMemberDao implements MemberDao {
	
	private DataSource dataSource;
	public DataSourceMemberDao(DataSource dataSource) {
		this.dataSource = dataSource;
	}
		
	@Override
	public void insertMember(MemberDto member) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			
			// Class.forName("com.mysql.cj.jdbc.Driver"); -> 필요없어짐.
			conn = dataSource.getConnection(); // 커넥션 풀(CP)에서 빌려옴.
			
			String sql = "insert into tbl_member (memberid, passwd, email) values (?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member.getMemberId());
			pstmt.setString(2, member.getPasswd());
			pstmt.setString(3, member.getEmail());
			pstmt.executeUpdate();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			JdbcUtils.closeStatement(pstmt); // closeStatement 먼저.
			JdbcUtils.closeConnection(conn); // 연결 반납.
		}
	}
	
	// selectMemberByIdAndPasswd 메서드 만들기
	// 전달인자 memberId, passwd
	// 결과형 MemberDto
	// tbl_member 테이블에서 데이터 조회
	@Override
	public MemberDto checkMember(String memberId, String passwd) {
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		MemberDto member = null;
		
		try {
			
			connection = dataSource.getConnection();
			
			String sql = "select * from tbl_member where memberid = ? and passwd = ?";
			statement = connection.prepareStatement(sql);
			
			statement.setString(1, memberId);
			statement.setString(2, passwd);
			rs=statement.executeQuery();
			
			if(rs.next()) {
				member = new MemberDto();
				member.setMemberId(rs.getString(1));
				member.setEmail(rs.getString(3));
				member.setUserType(rs.getString(4));
				member.setJoinDate(rs.getDate(5));
				member.setActive(rs.getBoolean(6));
				member.setUserGrade(rs.getInt(7));
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeStatement(statement); // closeStatement 먼저.
			JdbcUtils.closeConnection(connection); // 연결 반납.
		}
		return member;
	}

	@Override
	public int selectMemberCountById(String memberId) {
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		int count = 0;
		
		try {
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = dataSource.getConnection();
			String sql = "select count(*) from tbl_member where memberid = ?";
			statement = connection.prepareStatement(sql);
			statement.setString(1, memberId);
			rs=statement.executeQuery();
			
			if(rs.next()) {
				count = rs.getInt(1);
			}
						
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeStatement(statement); // closeStatement 먼저.
			JdbcUtils.closeConnection(connection); // 연결 반납.
		}

		return count;
		
	}
}
