package www.dream.com.framework.hashTagAnalyzer.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import www.dream.com.framework.hashTagAnalyzer.model.HashTagVO;

public interface HashTagMapper {
	public List<HashTagVO> findExisting(String[] arrHashTag);

	public long selectNewID();
	public int createHashTag(List<HashTagVO> listHashTag);

	public void createRelWithReply(@Param("replyId") long replyId, @Param("listHashTag") List<HashTagVO> listHashTag);

	public void deleteRelWithReply(long replyId);
}
