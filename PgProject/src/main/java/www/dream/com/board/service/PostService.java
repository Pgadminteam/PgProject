package www.dream.com.board.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import www.dream.com.board.model.PostVO;
import www.dream.com.board.model.ReplyVO;
import www.dream.com.board.model.mapper.ReplyMapper;
import www.dream.com.framework.attachFile.model.mapper.AttachMapper;
import www.dream.com.framework.dataType.DreamPair;
import www.dream.com.framework.hashTagAnalyzer.model.HashTagVO;
import www.dream.com.framework.hashTagAnalyzer.service.HashTagService;
import www.dream.com.framework.model.Criteria;
import www.dream.com.framework.util.BeanUtil;

@Service
public class PostService {
	@Autowired
	private ReplyMapper replyMapper;
	@Autowired
	private AttachMapper attachMapper;
	
	/**
	 * 게시글 총 개수와 이를 이용해 paging 처리
	 * @param boardId
	 * @param criteria
	 * @return cnt 총개수
	 */
	public long countTotalPostWithPaging(long boardId, Criteria criteria) {
		long cnt = replyMapper.countTotalPostWithPaging(boardId, criteria);
		return cnt;
	}

	/**
	 * 게시글 목록 paging 처리
	 * @param boardId
	 * @param criteria
	 * @return
	 */
	public List<ReplyVO> listPostWithPaging(long boardId, Criteria criteria) {
		return replyMapper.listPostWithPaging(boardId, criteria);
	}

	/**
	 * 게시글 찾기
	 */
	public ReplyVO findPostById(long id) {
		//조회수
		replyMapper.incViewCnt(id);
		PostVO post = (PostVO) replyMapper.findReplyById(id);
		return post;
	}

	/**
	 * 게시글 등록 + 해시태그 동시 등록
	 */
	@Transactional
	public void registerPost(PostVO post) {
		HashTagService hashTagService = BeanUtil.getBean(HashTagService.class);

		List<HashTagVO> listHashTag =PostService.identifyOldAndNew(post, hashTagService);

		replyMapper.registerPost(post);
		hashTagService.createRelWithReply(post.getId(), listHashTag);
		if (post.hasAttach())
			attachMapper.createAttach(post);
	}

	/**
	 * 게시글 수정 + 해시태그 동시 수정
	 */
	@Transactional
	public boolean updatePost(PostVO post) {
		HashTagService hashTagService = BeanUtil.getBean(HashTagService.class);
		hashTagService.deleteRelWithReply(post.getId());

		List<HashTagVO> listHashTag = PostService.identifyOldAndNew(post, hashTagService);

		hashTagService.createRelWithReply(post.getId(), listHashTag);

		attachMapper.deleteAttach(post);
		if (post.hasAttach()) {
			attachMapper.createAttach(post);
		}

		return replyMapper.updatePost(post);
	}

	/**
	 * 게시글 삭제 + 해시태그 동시 삭제(DB말고 게시글에 달린 관계만 삭제됨)
	 */
	@Transactional
	public boolean removePost(PostVO post) {
		HashTagService hashTagService = BeanUtil.getBean(HashTagService.class);
		hashTagService.deleteRelWithReply(post.getId());
		if (post.hasAttach()) {
			attachMapper.deleteAttach(post);
		}
		return replyMapper.removeReply(post.getId());
	}

	/**
	 * 게시글 좋아요
	 */
	public int likePost(long postId, int likeToggle) {
		replyMapper.likePost(postId, likeToggle);
		return replyMapper.readLikeCount(postId);
	}

	/**
	 * 게시글 싫어요
	 */
	public int dislikePost(long postId, int dislikeToggle) {
		replyMapper.dislikePost(postId, dislikeToggle);
		return replyMapper.readDislikeCount(postId);
	}

	/**
	 * 해시태그 기존 + 신규
	 */
	protected static List<HashTagVO> identifyOldAndNew(PostVO post, HashTagService hashTagService) {
		String hashTag = post.getHashTag();
		String[] arrHashTag = {""};
		if (hashTag != null) {
			arrHashTag = hashTag.split(" ");
		}
		//교집합 되는 것들, 새것들
		DreamPair<List<HashTagVO>, List<HashTagVO>> pair = hashTagService.split(arrHashTag);
		/* 신규 단어 등록 */
		hashTagService.createHashTag(pair.getSecond());
		//전체 단어와 게시글 사이의 연결 고리를 만들어 줍니다.
		//이 기능을 만들곳이 HashTag에 있어야하나? 아니면 각 사용 주체에게 달려있어야 할까?
		pair.getFirst().addAll(pair.getSecond());
		return pair.getFirst();
	}
}
