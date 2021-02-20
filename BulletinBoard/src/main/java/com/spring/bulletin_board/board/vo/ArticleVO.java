package com.spring.bulletin_board.board.vo;

import java.sql.Date;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component("articleVO")
public class ArticleVO {

	private int lvl;
	private int articleNO;
	private int parentNO;
	private String title;
	private String content;
	private String imageFileName;
	private String id;
	private Date writeDate;
	private int viewCount;
	
	public ArticleVO() {
		
	}


	public ArticleVO(int lvl, int articleNO, int parentNO, String title, String content, String imageFileName,
			String id, int viewCount) {
		super();
		this.lvl = lvl;
		this.articleNO = articleNO;
		this.parentNO = parentNO;
		this.title = title;
		this.content = content;
		this.imageFileName = imageFileName;
		this.id = id;
		this.viewCount = viewCount;
	}
}
