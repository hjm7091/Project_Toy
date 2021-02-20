package com.spring.bulletin_board.board.service;

import java.util.List;
import java.util.Map;

import com.spring.bulletin_board.board.vo.ArticleVO;

public interface BoardService {
	public List<ArticleVO> listArticles(Map<String, Integer> map) throws Exception;
	public int countTotalArticles() throws Exception;
	public int addNewArticle(Map<String, Object> articleMap) throws Exception;
	public ArticleVO viewArticle(int articleNO) throws Exception;
	public void modArticle(Map<String, Object> articleMap) throws Exception;
	public void removeArticle(int articleNO) throws Exception;
}
