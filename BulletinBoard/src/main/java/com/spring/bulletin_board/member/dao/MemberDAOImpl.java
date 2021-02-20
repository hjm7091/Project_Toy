package com.spring.bulletin_board.member.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.spring.bulletin_board.member.vo.MemberVO;


@Repository("memberDAO")
public class MemberDAOImpl implements MemberDAO {

	@Autowired
	private SqlSession sqlSession;
	
	@Override
	public List<MemberVO> selectAllMemberList() throws DataAccessException {
		@SuppressWarnings("unchecked")
		List<MemberVO> membersList = sqlSession.selectList("mapper.member.selectAllMemberList");
		return membersList;
	}

	@Override
	public int insertMember(MemberVO memberVO) throws DataAccessException {
		int result = sqlSession.insert("mapper.member.insertMember", memberVO);
		return result;
	}
	
	@Override
	public int deleteMember(String id) throws DataAccessException {
		int result = sqlSession.delete("mapper.member.deleteMember", id);
		return result;
	}
	
	@Override
	public int updateMember(MemberVO memberVO) throws DataAccessException {
		int result = sqlSession.update("mapper.member.updateMember", memberVO);
		return result;
	}
	
	@Override
	public MemberVO loginById(MemberVO memberVO) throws DataAccessException {
		MemberVO vo = sqlSession.selectOne("mapper.member.loginById", memberVO);
		return vo;
	}
	
	@Override
	public boolean overlappedID(String id) throws DataAccessException {
		String result = sqlSession.selectOne("mapper.member.overlapped", id);
		if(result.equals("true"))
			return true;
		else
			return false;
	}
	
	@Override
	public MemberVO findMember(String id) throws DataAccessException {
		MemberVO vo = sqlSession.selectOne("mapper.member.findMember", id);
		return vo;
	}

}
