package com.spring.bulletin_board.member.vo;

import java.sql.Date;

import org.springframework.stereotype.Component;

import lombok.Data;


@Data
@Component("memberVO")
public class MemberVO {
	private String id;
	private String pwd;
	private String name;
	private String email;
	private Date joinDate;

	public MemberVO() {
		
	}

}
