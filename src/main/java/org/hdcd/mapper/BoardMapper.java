package org.hdcd.mapper;

import java.util.List;

import org.hdcd.common.domain.PageRequest;
import org.hdcd.domain.Board;

public interface BoardMapper {

	public void create(Board board) throws Exception;

	public Board read(Integer boardNo) throws Exception;

	public void update(Board board) throws Exception;

	public void delete(Integer boardNo) throws Exception;

	public List<Board> list(PageRequest pageRequest) throws Exception;
	
	public void updateViewCnt(Integer boardNo) throws Exception;

	public String getPreview(Integer boardNo) throws Exception;

	public String getPicture(Integer boardNo) throws Exception;
	
	//페이징 카운트
	public int count(PageRequest pageRequest) throws Exception;


}
