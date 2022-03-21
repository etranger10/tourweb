package org.hdcd.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.hdcd.common.domain.CodeLabelValue;
import org.hdcd.common.domain.PageRequest;
import org.hdcd.common.domain.Pagination;
import org.hdcd.domain.Board;
import org.hdcd.domain.Reply;
import org.hdcd.prop.BoardProperties;
import org.hdcd.service.BoardService;
import org.hdcd.service.ReplyService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/board")
public class BoardController {

	private final BoardService boardService;
	
	private final BoardProperties boardProperties;
	
	private final ReplyService replyService;

	//등록화면
	@GetMapping("/register")
	public String registerForm(Model model) throws Exception {

		model.addAttribute(new Board());
		return "board/register";
	}

	//등록 처리
	@PostMapping("/register")
	public String register(Board board, RedirectAttributes rttr) throws Exception {
		MultipartFile pictureFile = board.getPicture();
		MultipartFile previewFile = board.getPreview();
		
		String createdPictureFilename = uploadFile(pictureFile.getOriginalFilename(), pictureFile.getBytes());
		String createdPreviewFilename = uploadFile(previewFile.getOriginalFilename(), previewFile.getBytes());
		
		board.setPictureUrl(createdPictureFilename);
		board.setPreviewUrl(createdPreviewFilename);

		boardService.register(board);

		rttr.addFlashAttribute("msg", "SUCCESS");
		return "redirect:/board/list";
	}

	private String uploadFile(String originalName, byte[] fileData) throws Exception {
		UUID uid = UUID.randomUUID();

		String createdFileName = uid.toString() + "_" + originalName;

		String uploadPath = boardProperties.getUploadPath();
		File target = new File(uploadPath, createdFileName);

		FileCopyUtils.copy(fileData, target);

		return createdFileName;
	}
	//목록 화면
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public void list(@ModelAttribute("pgrq") PageRequest pageRequest, Model model) throws Exception {
		model.addAttribute("list", boardService.list(pageRequest));
		
		Pagination pagination = new Pagination();
		pagination.setPageRequest(pageRequest);
	    
		pagination.setTotalCount(boardService.count(pageRequest));

		model.addAttribute("pagination", pagination);
	    
		List<CodeLabelValue> searchTypeCodeValueList = new ArrayList<CodeLabelValue>();
		searchTypeCodeValueList.add(new CodeLabelValue("n", "선택"));
		searchTypeCodeValueList.add(new CodeLabelValue("t", "제목"));
		searchTypeCodeValueList.add(new CodeLabelValue("w", "작성자"));
		searchTypeCodeValueList.add(new CodeLabelValue("tc", "제목+내용"));

		model.addAttribute("searchTypeCodeValueList", searchTypeCodeValueList);
	}
//	@GetMapping("/list")
//	public void list(Model model) throws Exception {
//		
//		 List<Board> boardList = boardService.list(); 
//		 model.addAttribute("list", boardList);
//		 
//	}

	//상세 화면
	@GetMapping("/read")
	public void read(int boardNo, Model model) throws Exception {
		Board board = boardService.read(boardNo);
		
		model.addAttribute(board);
	
		//댓글 조회
		List<Reply> reply = null;
		reply = replyService.list(boardNo);
		model.addAttribute("reply", reply);
		
		
	}
	
	//수정 화면
	@GetMapping("/modify")
	public String modifyForm(Integer boardNo, Model model) throws Exception {
		Board board = boardService.read(boardNo);
		model.addAttribute(board);
		return "board/modify";
	}

	//수정 처리
	@PostMapping("/modify")
	public String modify(Board board, RedirectAttributes rttr) throws Exception {
		MultipartFile pictureFile = board.getPicture();

		if (pictureFile != null && pictureFile.getSize() > 0) {
			String createdFilename = uploadFile(pictureFile.getOriginalFilename(), pictureFile.getBytes());

			board.setPictureUrl(createdFilename);
		}
		
		MultipartFile previewFile = board.getPreview();

		if (previewFile != null && previewFile.getSize() > 0) {
			String createdFilename = uploadFile(previewFile.getOriginalFilename(), previewFile.getBytes());

			board.setPreviewUrl(createdFilename);
		}

		boardService.modify(board);

		rttr.addFlashAttribute("msg", "SUCCESS");

	    return "redirect:/board/list";
	}
	
	//삭제 화면
	@GetMapping("/remove")
	public String removeForm(Integer boardNo, Model model) throws Exception {
		Board board = boardService.read(boardNo);

		model.addAttribute(board);

		return "board/remove";
	}
	
	//삭제 처리
	@PostMapping("/remove")
	public String remove(Board board, RedirectAttributes rttr) throws Exception {
		boardService.remove(board.getBoardNo());
		rttr.addFlashAttribute(board);

		return "redirect:/board/list";
	}

	@GetMapping("/display")
	@ResponseBody
	public ResponseEntity<byte[]> displayFile(Integer boardNo) throws Exception {
		InputStream in = null;
		ResponseEntity<byte[]> entity = null;

		String fileName = boardService.getPreview(boardNo);

		try {
			String formatName = fileName.substring(fileName.lastIndexOf(".") + 1);

			MediaType mType = getMediaType(formatName);

			HttpHeaders headers = new HttpHeaders();

			String uploadPath = boardProperties.getUploadPath();
			in = new FileInputStream(uploadPath + File.separator + fileName);

			if (mType != null) {
				headers.setContentType(mType);
			}

			entity = new ResponseEntity<byte[]>(IOUtils.toByteArray(in), headers, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
		} finally {
			in.close();
		}
		return entity;
	}
	
	private MediaType getMediaType(String formatName){
		if(formatName != null) {
			if(formatName.equals("JPG")) {
				return MediaType.IMAGE_JPEG;
			}
			
			if(formatName.equals("GIF")) {
				return MediaType.IMAGE_GIF;
			}
			
			if(formatName.equals("PNG")) {
				return MediaType.IMAGE_PNG;
			}
		}
		
		return null;
	}
	
	@GetMapping("/picture")
	@ResponseBody
	public ResponseEntity<byte[]> pictureFile(Integer boardNo) throws Exception {
		InputStream in = null;
		ResponseEntity<byte[]> entity = null;

		String fileName = boardService.getPicture(boardNo);

		try {
			String formatName = fileName.substring(fileName.lastIndexOf(".") + 1);

			MediaType mType = getMediaType(formatName);

			HttpHeaders headers = new HttpHeaders();

			String uploadPath = boardProperties.getUploadPath();
			in = new FileInputStream(uploadPath + File.separator + fileName);

			if (mType != null) {
				headers.setContentType(mType);
			}

			entity = new ResponseEntity<byte[]>(IOUtils.toByteArray(in), headers, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
		} finally {
			in.close();
		}
		return entity;
	}

}
	

