package com.spring.bulletin_board.member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.bulletin_board.member.vo.MemberVO;


public interface MemberController {
	//메인페이지
	public ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws Exception;
	
	//멤버 리스트
	public ModelAndView listMembers(HttpServletRequest request, HttpServletResponse response) throws Exception;
	
	//멤버 폼
	public ModelAndView insertForm(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public ModelAndView loginForm(@RequestParam(value = "action", required = false) String action, 
									@RequestParam(value = "parentNO", required = false) String parentNO,
										HttpServletRequest request, HttpServletResponse response) throws Exception;
	public ModelAndView modForm(@RequestParam("id") String id, HttpServletRequest request, HttpServletResponse response) throws Exception;
	
	//삽입, 삭제, 수정
	public ModelAndView insertMember(@ModelAttribute("member") MemberVO member, RedirectAttributes rAttr, HttpServletRequest request, HttpServletResponse response) throws Exception;
	public ModelAndView removeMember(@RequestParam("id") String id, HttpServletRequest request, HttpServletResponse response) throws Exception;
	public ModelAndView modMember(@ModelAttribute("member") MemberVO member, HttpServletRequest request, HttpServletResponse response) throws Exception;
	
	//로그인, 로그아웃
	public ModelAndView login(@ModelAttribute("member") MemberVO member, RedirectAttributes rAttr, HttpServletRequest request, HttpServletResponse response) throws Exception;
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
