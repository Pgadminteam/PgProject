package www.dream.com.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import www.dream.com.board.service.PostService;

@RestController
@RequestMapping("/postlike/*")
public class PostLikeRestController {
	//함수 배치 순서는 목록, 상세, 생성, 수정, 삭제
	@Autowired
	private PostService postService;
	
	/**
	 * 좋아요 기능 토글
	 * @param postId
	 * @param likeToggle
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "like/{postId}/{likeToggle}", produces = {MediaType.TEXT_PLAIN_VALUE})
	public ResponseEntity<String> likePost(
			@PathVariable("postId") long postId, 
			@PathVariable("likeToggle") int likeToggle) {
		int likeCnt = postService.likePost(postId, likeToggle);
		return new ResponseEntity<>(String.valueOf(likeCnt), HttpStatus.OK);
	}
	
	/**
	 * 싫어요 기능 토글
	 * @param postId
	 * @param dislikeToggle
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "dislike/{postId}/{dislikeToggle}", produces = {MediaType.TEXT_PLAIN_VALUE})
	public ResponseEntity<String> dislikePost(
			@PathVariable("postId") long postId, 
			@PathVariable("dislikeToggle") int dislikeToggle) {
		int dislikeCnt = postService.dislikePost(postId, dislikeToggle);
		return new ResponseEntity<>(String.valueOf(dislikeCnt), HttpStatus.OK);
	}
}
