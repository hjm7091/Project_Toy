package com.spring.bulletin_board.member.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.spring.bulletin_board.member.dao.MemberDAO;
import com.spring.bulletin_board.member.vo.MemberVO;

@Service("memberService")
public class MemberServiceImpl implements MemberService {
	
	@Autowired
	private MemberDAO memberDAO;

	@Override
	public List<MemberVO> listMembers() throws DataAccessException {
		List<MemberVO> membersList = null;
		membersList = memberDAO.selectAllMemberList();
		return membersList;
	}
	
	@Override
	public int addMember(MemberVO memberVO) throws DataAccessException {
		return memberDAO.insertMember(memberVO);	
	}
	
	@Override
	public int removeMember(String id) throws DataAccessException {
		return memberDAO.deleteMember(id);
	}
	
	@Override
	public int modMember(MemberVO memberVO) throws DataAccessException {
		return memberDAO.updateMember(memberVO);
	}
	
	@Override
	public MemberVO login(MemberVO memberVO) throws DataAccessException {
		return memberDAO.loginById(memberVO);
	}
	
	@Override
	public boolean overlappedID(String id) throws DataAccessException {
		return memberDAO.overlappedID(id);
	}
	
	@Override
	public MemberVO findMember(String id) throws DataAccessException {
		return memberDAO.findMember(id);
	}

}
