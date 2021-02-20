package com.spring.bulletin_board.member.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.bulletin_board.member.service.MemberService;
import com.spring.bulletin_board.member.vo.MemberVO;

@Controller("memberController")
public class MemberControllerImpl extends MultiActionController implements MemberController {

	private static final Logger logger = LoggerFactory.getLogger(MemberControllerImpl.class);
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	MemberVO memberVO;
	
	@Override
	@RequestMapping(value = {"/main.do"}, method = RequestMethod.GET)
	public ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewName = (String) request.getAttribute("viewName");
		logger.info("info 레벨 : viewName = " + viewName);
		ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);
		return mav;
	}
	
	@Override
	@RequestMapping(value = "/member/listMembers.do", method = RequestMethod.GET)
	public ModelAndView listMembers(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewName = (String) request.getAttribute("viewName");
		logger.info("info 레벨 : viewName = " + viewName);
		List<MemberVO> membersList = memberService.listMembers();
		ModelAndView mav = new ModelAndView(viewName);
		mav.addObject("membersList", membersList);
		return mav;
	}
	
	@Override
	@RequestMapping(value = "/member/insertForm.do", method = RequestMethod.GET)
	public ModelAndView insertForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		String viewName = (String) request.getAttribute("viewName");
		logger.info("info 레벨 : viewName = " + viewName);
		ModelAndView mav = new ModelAndView(viewName);
		return mav;
	}
	
	@Override
	@RequestMapping(value = "/member/loginForm.do", method = RequestMethod.GET)
	public ModelAndView loginForm(@RequestParam(value = "action", required = false) String action, 
									@RequestParam(value = "parentNO", required = false) String parentNO,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		String viewName = (String) request.getAttribute("viewName");
		logger.info("info 레벨 : viewName = " + viewName);
		HttpSession session = request.getSession();
		session.setAttribute("action", action);
		session.setAttribute("parentNO", parentNO);
		ModelAndView mav = new ModelAndView(viewName);
		return mav;
	}
	
	@Override
	@RequestMapping(value = "/member/modForm.do", method = RequestMethod.GET)
	public ModelAndView modForm(@RequestParam("id") String id,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		String viewName = (String) request.getAttribute("viewName");
		logger.info("info 레벨 : viewName = " + viewName);
		ModelAndView mav = new ModelAndView(viewName);
		MemberVO memInfo = memberService.findMember(id);
		mav.addObject("memInfo", memInfo);
		return mav;
	}
	
	@Override
	@RequestMapping(value = "/member/insertMember.do", method = RequestMethod.POST)
	public ModelAndView insertMember(@ModelAttribute("member") MemberVO member, RedirectAttributes rAttr,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		ModelAndView mav = new ModelAndView();
		if(memberService.overlappedID(member.getId())) {
			rAttr.addAttribute("result", "insertFailed");
			mav.setViewName("redirect:/member/insertForm.do");
		}else {
			int result = memberService.addMember(member);
			logger.info("info 레벨 : result = " + result);
			rAttr.addAttribute("result", "insertSuccessed");
			mav.setViewName("redirect:/main.do");
		}
		return mav;
	}
	
	@Override
	@RequestMapping(value = "/member/removeMember.do", method = RequestMethod.GET)
	public ModelAndView removeMember(@RequestParam("id") String id, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		memberService.removeMember(id);
		ModelAndView mav = new ModelAndView("redirect:/member/listMembers.do");
		return mav;
	}
	
	@Override
	@RequestMapping(value = "/member/modMember.do", method = RequestMethod.POST)
	public ModelAndView modMember(@ModelAttribute("member") MemberVO member, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		memberService.modMember(member);
		ModelAndView mav = new ModelAndView("redirect:/member/listMembers.do");
		return mav;
	}
	
	@Override
	@RequestMapping(value = "/member/login.do", method = RequestMethod.POST)
	public ModelAndView login(@ModelAttribute("member") MemberVO member, RedirectAttributes rAttr, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		memberVO = memberService.login(member);
		if(memberVO != null) {
			HttpSession session = request.getSession();
			session.setAttribute("member", memberVO);
			session.setAttribute("isLogOn", true);
			String action = (String)session.getAttribute("action"); //action은 로그인하지 않은 상태에서 글쓰기나, 답변하기를 눌렀을 때 값이 존재한다.
			session.removeAttribute("action");
			if(action != null) {
				String parentNO = (String)session.getAttribute("parentNO");
				session.removeAttribute("parentNO");
				if(parentNO != null) 
					mav.setViewName("redirect:" + action + "?parentNO=" + parentNO);
				else
					mav.setViewName("redirect:" + action);
			}
			else
				mav.setViewName("redirect:/main.do");
		} else {
			rAttr.addAttribute("result", "loginFailed");
			mav.setViewName("redirect:/member/loginForm.do");
		}
		return mav;
	}
	
	@Override
	@RequestMapping(value = "/member/logout.do", method = RequestMethod.GET)
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		session.removeAttribute("member");
		session.removeAttribute("isLogOn");
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/main.do");
		return mav;
	}

}
