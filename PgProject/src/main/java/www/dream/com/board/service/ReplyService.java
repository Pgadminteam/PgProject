package www.dream.com.board.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import www.dream.com.board.model.ReplyVO;
import www.dream.com.board.model.mapper.ReplyMapper;
import www.dream.com.framework.model.Criteria;

@Service
public class ReplyService {
	@Autowired
	private ReplyMapper replyMapper;

	/**
	 * 댓글 총개수와 이를 이용해 paging 처리
	 */
	public long countTotalReplyWithPaging(long originalId) {
		return replyMapper.countTotalReplyWithPaging(originalId);
	}
	
	/**
	 * 댓글 paging 처리
	 */
	public List<ReplyVO> listReplyWithPaging(long originalId, Criteria criteria) {
		return replyMapper.listReplyWithPaging(originalId, criteria);
	}
	
	public ReplyVO findReplyById(long id) {
		return replyMapper.findReplyById(id);
	}
	
	/**
	 * 댓글 등록
	 * @param reply
	 * @return 원글에 달린 총 댓글 개수가 반환
	 */
	public long registerReply(ReplyVO reply) {
		long cnt = replyMapper.registerReply(reply);
		//원글에 댓글을 추가해 줄 때 댓글 개수를 파악하여 반환하자
		cnt = replyMapper.countTotalReplyWithPaging(reply.getOriginalId());
		return cnt;
	}
	
	/**
	 * 댓글 수정
	 */
	public boolean updateReply(ReplyVO reply) {
		return replyMapper.updateReply(reply);
	}
	
	/**
	 * 댓글 삭제
	 */
	public boolean removeReply(long id) {
		return replyMapper.removeReply(id);
	}

}
