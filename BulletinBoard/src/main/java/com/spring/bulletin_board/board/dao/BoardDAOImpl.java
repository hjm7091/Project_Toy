package com.spring.bulletin_board.board.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.bulletin_board.board.vo.ArticleVO;

@Repository("boardDAO")
public class BoardDAOImpl implements BoardDAO {

	@Autowired
	private SqlSession sqlSession;
	
	@Override
	public List<ArticleVO> selectAllArticlesList(Map<String, Integer> map) throws Exception {
		List<ArticleVO> articlesList = sqlSession.selectList("mapper.board.selectAllArticlesList", map);
		return articlesList;
	}
	
	@Override
	public int countTotalArticles() throws Exception {
		return sqlSession.selectOne("mapper.board.countTotalArticles");
	}
	
	@Override
	public int insertNewArticle(Map<String, Object> articleMap) throws Exception {
		int articleNO = selectNewArticleNO();
		articleMap.put("articleNO", articleNO);
		sqlSession.insert("mapper.board.insertNewArticle", articleMap);
		return articleNO;
	}
	
	@Override
	public int selectNewArticleNO() throws Exception {
		return sqlSession.selectOne("mapper.board.selectNewArticleNO");
	}
	
	@Override
	public ArticleVO selectArticle(int articleNO) throws Exception {
		sqlSession.update("mapper.board.increaseViewCount", articleNO); //조회수 증가
		return sqlSession.selectOne("mapper.board.selectArticle", articleNO);
	}
	
	@Override
	public void updateArticle(Map<String, Object> articleMap) throws Exception {
		sqlSession.update("mapper.board.updateArticle", articleMap);
	}
	
	@Override
	public void deleteArticle(int articleNO) throws Exception {
		sqlSession.delete("mapper.board.deleteArticle", articleNO);
	}

}
