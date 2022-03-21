package org.hdcd.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Reply {
	private int reply_no;
	private int boardNo;
	private String writer;
	private String content;
	private Date regDate;
	
}
