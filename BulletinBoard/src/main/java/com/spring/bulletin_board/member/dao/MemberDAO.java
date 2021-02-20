package com.spring.bulletin_board.member.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.spring.bulletin_board.member.vo.MemberVO;


public interface MemberDAO {
	public List<MemberVO> selectAllMemberList() throws DataAccessException ;
	public int insertMember(MemberVO memberVO) throws DataAccessException ;
	public int deleteMember(String id) throws DataAccessException; 
	public int updateMember(MemberVO memberVO) throws DataAccessException; 
	public MemberVO loginById(MemberVO memberVO) throws DataAccessException;
	public boolean overlappedID(String id) throws DataAccessException;
	public MemberVO findMember(String id) throws DataAccessException;
}
