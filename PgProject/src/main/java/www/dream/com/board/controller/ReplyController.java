package www.dream.com.board.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import www.dream.com.board.model.ReplyVO;
import www.dream.com.board.service.ReplyService;
import www.dream.com.framework.dataType.DreamPair;
import www.dream.com.framework.model.Criteria;
import www.dream.com.party.security.CustomUser;

@RestController
@RequestMapping("/replies/*")
public class ReplyController {
	//함수 배치 순서는 목록, 상세, 생성, 수정, 삭제
	@Autowired
	private ReplyService replyService;

	/**
	 * 댓글 총 개수 세기
	 */
	@GetMapping(value = "countTotalReply/{originalId}")
	public ResponseEntity<Long> countTotalReply(@PathVariable("originalId") long originalId) {
		return new ResponseEntity<>(replyService.countTotalReplyWithPaging(originalId), HttpStatus.OK);
	}

	/**
	 * 댓글 paging 처리
	 */
	@GetMapping(value = "pages/{originalId}/{pageNum}", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<DreamPair<Criteria, List<ReplyVO>>> listReplyWithPaging(
			@PathVariable("originalId") long originalId, @PathVariable("pageNum") long pageNum) {
		Criteria criteria = new Criteria(pageNum, replyService.countTotalReplyWithPaging(originalId));
		List<ReplyVO> listReply = replyService.listReplyWithPaging(originalId, criteria);
		DreamPair<Criteria, List<ReplyVO>> dreamPair = new DreamPair<>(criteria, listReply);
		return new ResponseEntity<>(dreamPair, HttpStatus.OK);
	}

	/**
	 * 댓글 찾기
	 */
	@GetMapping(value = "{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<ReplyVO> findReplyById(@PathVariable("id") long id) {
		return new ResponseEntity<>(replyService.findReplyById(id), HttpStatus.OK);
	}

	/**
	 * 댓글 달기
	 */
	@PreAuthorize("isAuthenticated()")
	@PostMapping(value = "new", consumes = "application/json", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<DreamPair<Long, Integer>> registerReply(
			@AuthenticationPrincipal Principal principal, @RequestBody ReplyVO reply) {
		UsernamePasswordAuthenticationToken upat = (UsernamePasswordAuthenticationToken) principal;
		reply.setWriter(((CustomUser) upat.getPrincipal()).getLoginUser());
		
		long cnt = replyService.registerReply(reply);
		return new ResponseEntity<>(new DreamPair(cnt, Criteria.DEFAULT_AMOUNT), HttpStatus.OK);
	}

	/**
	 * 댓글 수정
	 */
	@PreAuthorize("principal.loginUser.id == #reply.writer.id")
	@RequestMapping(method= {RequestMethod.PUT, RequestMethod.PATCH}, value = "/{id}", consumes = "application/json", produces = {MediaType.TEXT_PLAIN_VALUE})
	public ResponseEntity<String> updateReply(@PathVariable("id") long id, @RequestBody ReplyVO reply) {
		return replyService.updateReply(reply) ? 
				new ResponseEntity<>("success", HttpStatus.OK) 
				: new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * 댓글 삭제
	 */
	@PreAuthorize("principal.loginUser.id == #replyerId")
	@DeleteMapping(value = "/{id}/{replyerId}", produces = {MediaType.TEXT_PLAIN_VALUE})
	public ResponseEntity<String> removeReply(@PathVariable("id") long id, @PathVariable("replyerId") long replyerId) {
		return replyService.removeReply(id) ? 
				new ResponseEntity<>("success", HttpStatus.OK) 
				: new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
