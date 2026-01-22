package com.demoweb.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Component;

import com.demoweb.dto.MemberDto;

@Component("jdbcTemplateMemberDao")
public class JdbcTemplateMemberDao implements MemberDao {
	
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	public JdbcTemplateMemberDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public void insertMember (MemberDto member) {
			
		String sql = "insert into tbl_member (memberid, passwd, email) values (?, ?, ?)";
		jdbcTemplate.update(sql,member.getMemberId(), member.getPasswd(), member.getEmail());	
	}
	
	// selectMemberByIdAndPasswd 메서드 만들기
	// 전달인자 memberId, passwd
	// 결과형 MemberDto
	// tbl_member 테이블에서 데이터 조회
	@Override
	public MemberDto checkMember(String memberId, String passwd) {
		
		String sql = "select * from tbl_member where memberid = ? and passwd = ?";
		MemberDto member = jdbcTemplate.queryForObject(
				
			sql,
			
			new RowMapper<MemberDto>() {
				@Override
				public MemberDto mapRow(ResultSet rs, int rowNum) throws SQLException {
					MemberDto member = new MemberDto();
					member.setMemberId(rs.getString(1));
					member.setEmail(rs.getString(3));
					member.setUserType(rs.getString(4));
					member.setJoinDate(rs.getDate(5));
					member.setActive(rs.getBoolean(6));
					member.setUserGrade(rs.getInt(7));
					return member;
				}
			},
			
			memberId, 
			
			passwd);
		
		return member;
	}

	@Override
	public int selectMemberCountById(String memberId) {
		
		String sql = "select count(*) from tbl_member where memberid = ?";
		int count = jdbcTemplate.queryForObject(
				
			sql,
			
			new RowMapper<Integer>() {
				@Override
				public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
					return rs.getInt(1);
				}
			},
			
			memberId);
		
		return count;
	}
}
