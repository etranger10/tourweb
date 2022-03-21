package org.hdcd.controller;

import org.hdcd.domain.Reply;
import org.hdcd.service.ReplyService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/reply/*")
public class ReplyController {
	
	private final ReplyService replyService;

	//댓글 작성
	@RequestMapping(value = "/write", method = RequestMethod.POST)
	public String posttWirte(Reply reply) throws Exception {
		
		replyService.writer(reply);
		
		return "redirect:/board/view?bno=" + reply.getBoardNo();
	}
}