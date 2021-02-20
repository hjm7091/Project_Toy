package com.spring.bulletin_board.board.dao;

import java.util.List;
import java.util.Map;

import com.spring.bulletin_board.board.vo.ArticleVO;

public interface BoardDAO {
	public List<ArticleVO> selectAllArticlesList(Map<String, Integer> map) throws Exception;
	public int countTotalArticles() throws Exception;
	public int insertNewArticle(Map<String, Object> articleMap) throws Exception;
	public int selectNewArticleNO() throws Exception;
	public ArticleVO selectArticle(int articleNO) throws Exception;
	public void updateArticle(Map<String, Object> articleMap) throws Exception;
	public void deleteArticle(int articleNO) throws Exception;
}
