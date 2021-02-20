package com.spring.bulletin_board.board.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

public interface BoardController {
	public ResponseEntity addNewArticle(MultipartHttpServletRequest multipartRequest, HttpServletResponse response) throws Exception;
	public ResponseEntity modArticle(MultipartHttpServletRequest multipartRequest, HttpServletResponse response) throws Exception;
	public ResponseEntity removeArticle(@RequestParam("articleNO") int articleNO, HttpServletRequest request, HttpServletResponse response) throws Exception;
	public ResponseEntity addReply(MultipartHttpServletRequest multipartRequest, HttpServletResponse response) throws Exception;
	
	public ModelAndView listArticles(@RequestParam(value = "section", required = false) Integer section, @RequestParam(value = "pageNum", required = false) Integer pageNum, HttpServletRequest request, HttpServletResponse response) throws Exception;
	public ModelAndView articleForm(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public ModelAndView viewArticle(@RequestParam("articleNO") int articleNO, @RequestParam("section") int section,
			@RequestParam("pageNum") int pageNum,HttpServletRequest request, HttpServletResponse response) throws Exception;
	public ModelAndView replyForm(@RequestParam("parentNO") String parentNO, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
