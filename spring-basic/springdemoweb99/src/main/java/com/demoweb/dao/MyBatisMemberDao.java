package com.demoweb.dao;

import java.util.HashMap;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Component;

import com.demoweb.dto.MemberDto;

@Component("myBatisMemberDao") // xml 에서 <bean id="mybatisMemberDao"... -> MyBatisMemberDao mybatisMemberDao = new...
public class MyBatisMemberDao implements MemberDao {
	
	private SqlSessionTemplate sessionTemplate;
	public MyBatisMemberDao(SqlSessionTemplate sessionTemplate) {
		this.sessionTemplate = sessionTemplate;
	}
	final String MEMBER_MAPPER = "com.demoweb.mapper.MemberMapper";
	
	@Override
	public void insertMember (MemberDto member) {
		sessionTemplate.insert(MEMBER_MAPPER + ".insertMember", member);
	}
	
	@Override
	public MemberDto checkMember(String memberId, String passwd) {
		
		HashMap<String, String> params = new HashMap<>();
		params.put("memberId", memberId);
		params.put("passwd", passwd);
		MemberDto member = sessionTemplate.selectOne(MEMBER_MAPPER + ".checkMember", params);
		return member;
	}

	@Override
	public int selectMemberCountById(String memberId) {
		
		int count = sessionTemplate.selectOne(MEMBER_MAPPER + ".selectMemberCountById", memberId);
		
		return count;
	}
}
