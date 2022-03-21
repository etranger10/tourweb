package org.hdcd.domain;

import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Board {

		private Integer boardNo;
		private String title;
		private String writer;
		private String content;
		private Integer viewCnt;
		private LocalDateTime regDate;
		private MultipartFile picture;
		private String pictureUrl;
		private MultipartFile preview;
		private String previewUrl;	
	
}
