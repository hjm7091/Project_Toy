package com.spring.bulletin_board.member.service;


import java.util.List;

import org.springframework.dao.DataAccessException;

import com.spring.bulletin_board.member.vo.MemberVO;

public interface MemberService {
	public List<MemberVO> listMembers() throws DataAccessException;
	public int addMember(MemberVO memberVO) throws DataAccessException;
	public int removeMember(String id) throws DataAccessException;
	public int modMember(MemberVO memberVO) throws DataAccessException;
	public MemberVO login(MemberVO memberVO) throws DataAccessException;
	public boolean overlappedID(String id) throws DataAccessException;
	public MemberVO findMember(String id) throws DataAccessException;
}
