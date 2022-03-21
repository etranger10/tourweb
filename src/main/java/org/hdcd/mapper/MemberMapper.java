package org.hdcd.mapper;

import org.hdcd.domain.Member;
import org.hdcd.domain.MemberAuth;

public interface MemberMapper {

	public Member readByUserId(String userId);
	
	public void create(Member member) throws Exception;

	public void createAuth(MemberAuth memberAuth) throws Exception;

}
