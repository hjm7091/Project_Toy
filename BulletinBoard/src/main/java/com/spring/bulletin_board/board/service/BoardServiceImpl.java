package com.spring.bulletin_board.board.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.bulletin_board.board.dao.BoardDAO;
import com.spring.bulletin_board.board.vo.ArticleVO;

@Service("boardService")
public class BoardServiceImpl implements BoardService {

	@Autowired
	BoardDAO boardDAO;
	
	@Override
	public List<ArticleVO> listArticles(Map<String, Integer> map) throws Exception {
		List<ArticleVO> articlesList = boardDAO.selectAllArticlesList(map);
		return articlesList;
	}
	
	@Override
	public int countTotalArticles() throws Exception {
		return boardDAO.countTotalArticles();
	}
	
	@Override
	public int addNewArticle(Map<String, Object> articleMap) throws Exception {
		return boardDAO.insertNewArticle(articleMap);
	}

	@Override
	public ArticleVO viewArticle(int articleNO) throws Exception {
		return boardDAO.selectArticle(articleNO);
	}
	
	@Override
	public void modArticle(Map<String, Object> articleMap) throws Exception {
		boardDAO.updateArticle(articleMap);
	}
	
	@Override
	public void removeArticle(int articleNO) throws Exception {
		boardDAO.deleteArticle(articleNO);
	}
}
