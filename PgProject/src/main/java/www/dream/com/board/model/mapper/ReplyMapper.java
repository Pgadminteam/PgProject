package www.dream.com.board.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import www.dream.com.board.model.PostVO;
import www.dream.com.board.model.ReplyVO;
import www.dream.com.framework.model.Criteria;

public interface ReplyMapper {
	//MyBatis에 파라메터 개수는 기본적으로 한개일 때는 잘 작동합니다.
	//그런데 두개 이상이 되면은 @Param을 사용해서 이름을 달아 주어야지 연동 됩니다.
	public long countTotalPostWithPaging(@Param("boardId") long boardId, @Param("criteria") Criteria criteria);
	public List<ReplyVO> listPostWithPaging(@Param("boardId") long boardId, @Param("criteria") Criteria criteria);

	public void registerPost(PostVO post);
	public boolean updatePost(PostVO post);
	public int likePost(@Param("postId") long postId, @Param("likeToggle") int likeToggle);
	public int readLikeCount(long postId);
	public int dislikePost(@Param("postId") long postId, @Param("dislikeToggle") int dislikeToggle);
	public int readDislikeCount(long postId);

	public long countTotalReplyWithPaging(@Param("originalId") long originalId);
	public List<ReplyVO> listReplyWithPaging(@Param("originalId") long originalId, @Param("criteria") Criteria criteria);
	public ReplyVO findReplyById(long id);
	public int registerReply(ReplyVO reply);
	public boolean updateReply(ReplyVO reply);
	public boolean removeReply(long id);
	public long incViewCnt(long id);
}
