package www.dream.com.board.model;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import www.dream.com.framework.display.Caption;
import www.dream.com.framework.display.Caption.WhenUse;
import www.dream.com.framework.hashTagAnalyzer.model.HashTagVO;
import www.dream.com.framework.model.CommonMngInfoVO;
import www.dream.com.party.model.PartyVO;

public class ReplyVO extends CommonMngInfoVO implements Serializable {
	@Getter
	private long id;
	@Getter @Setter
	private long originalId;

	@Getter
	@Caption(whenUse=WhenUse.detail, caption="Content")
	private String content;
	
	@Getter
	@Caption(whenUse=WhenUse.detail, caption="HashTag")
	private String hashTag;
	
	/* ------------   조회수 정보 정의 영역    ------------------ */
	@Getter @Setter
	@Caption(whenUse=WhenUse.all, caption="View")
	private long viewcnt;
	
	/* ------------   연관 정보 정의 영역    ------------------ */
	@Getter
	@Caption(whenUse=WhenUse.all, caption="Writer")
	private PartyVO writer;
	@Getter
	private List<HashTagVO> listHashTag;
	

	@Getter
	private int countOfReply;
	
	public ReplyVO() {
		int i = 0;
		i++;
	}
	
	public ReplyVO(Long id) {
		super();
		this.id = id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setHashTag(String hashTag) {
		this.hashTag = hashTag;
	}

	public long getViewcnt() {
		return viewcnt;
	}
	
	public void setViewcnt(long viewcnt) {
		this.viewcnt = viewcnt;
	}

	public void setWriter(PartyVO writer) {
		this.writer = writer;
	}
	
	
	public String getHashTagAsString() {
		StringBuilder sb = new StringBuilder();
		if (listHashTag != null)
			for (HashTagVO ht : listHashTag)
				sb.append(ht.getWord()).append(" ");
		return sb.toString();
	}
	
	@Override
	public String toString() {
		return "ReplyVO [id=" + id + ", content=" + content 
				+ ", viewcnt=" + viewcnt + ", writer=" + writer 
				+ ", regDate=" + regDate + ", updateDate=" + updateDate + "]";
	}
	
	protected String toString4ChildPrev() {
		return "id=" + id;
	}
	
	protected String toString4ChildPost() {
		return ", content=" + content 
				+ ", viewcnt=" + viewcnt
				+ ", writer=" + writer
				+ ", regDate=" + regDate + ", updateDate=" + updateDate + "]";
	}
}
