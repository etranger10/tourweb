package org.hdcd.service;

import java.util.List;

import org.hdcd.domain.Reply;

public interface ReplyService {
	
	public List<Reply> list(int boardNo) throws Exception;
	
	public void writer(Reply reply) throws Exception;

	public void modify(Reply reply) throws Exception;

	public void delete(Reply reply) throws Exception;

}
