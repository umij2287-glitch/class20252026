package com.demoweb.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.demoweb.dto.MemberDto;

@Mapper // root-context.xml 에서 <bean id="memberMapper"... 한 것과 동일한 어노테이션.
public interface MemberMapper {
	
	void insertMember(MemberDto member);
	MemberDto checkMember(@Param("memberId") String memberId, 
						  @Param("passwd")String passwd);	
	int selectMemberCountById(String memberId);
}
