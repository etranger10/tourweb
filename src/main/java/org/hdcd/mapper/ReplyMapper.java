package org.hdcd.mapper;

import java.util.List;

import org.hdcd.domain.Reply;

public interface ReplyMapper {
	
	public List<Reply> replyList(int board_no) throws Exception;
	
	public void replyCreate(Reply reply) throws Exception;

	public void replyModify(Reply reply) throws Exception;

	public void replyDelete(Reply reply) throws Exception;
	

}
