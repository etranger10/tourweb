package org.hdcd.service;

import java.util.List;

import org.hdcd.domain.Reply;
import org.hdcd.mapper.ReplyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ReplyServiceImpl implements ReplyService {
	
	@Autowired
	private final ReplyMapper mapperreply;

	@Override
	public List<Reply> list(int boardNo) throws Exception {
		// TODO Auto-generated method stub
		return mapperreply.replyList(boardNo);
	}

	@Override
	public void writer(Reply reply) throws Exception {
		// TODO Auto-generated method stub
		mapperreply.replyCreate(reply);

	}

	@Override
	public void modify(Reply reply) throws Exception {
		// TODO Auto-generated method stub
		mapperreply.replyModify(reply);

	}

	@Override
	public void delete(Reply reply) throws Exception {
		// TODO Auto-generated method stub
		mapperreply.replyDelete(reply);

	}

}
