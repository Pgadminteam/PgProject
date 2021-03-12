package www.dream.com.framework.hashTagAnalyzer.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import www.dream.com.board.model.PostVO;
import www.dream.com.framework.hashTagAnalyzer.service.HashTagService;

@RestController
@RequestMapping("/hashtag/*")
public class HashTagController {
	//함수 배치 순서는 목록, 상세, 생성, 수정, 삭제
	@Autowired
	private HashTagService komoranService;
	
	/**
	 * 해시태그 추출
	 */
	@PostMapping(value = "extractHashTag", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Set<String>> extractHashTag(PostVO post) {
		Set<String> listHashTag = komoranService.extractHashTag(post.getTitle(), post.getContent());
		
		ResponseEntity<Set<String>> ret = null;
		
		if (listHashTag != null && !listHashTag.isEmpty()) {
			ret = new ResponseEntity(listHashTag, HttpStatus.OK);
		} else {
			ret = new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return ret;
	}
}
