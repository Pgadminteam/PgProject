package www.dream.com.board.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;
import www.dream.com.framework.attachFile.model.AttachVO;
import www.dream.com.framework.display.Caption;
import www.dream.com.framework.display.Caption.WhenUse;
import www.dream.com.framework.model.IAttacher;

public class PostVO extends ReplyVO implements IAttacher, Serializable {
	@Getter
	@Caption(whenUse=WhenUse.all, caption="Title")
	private String title;
	@Getter
	private long boardId;
	
	@Getter @Setter
	@Caption(whenUse=WhenUse.all, caption="Like")
	private int likeCount;
	@Getter @Setter
	@Caption(whenUse=WhenUse.all, caption="Dislike")
	private int dislikeCount;
	
	//IAttacher를 통하여 관리되어야 할 정보입니다.
	@Getter @Setter
	private String[] attachJson;

	@Getter @Setter	//@Getter가 없을 때 화면에서 넘어오는 배열 이름을 못찾아서 null이 됩니다.
	private List<AttachVO> listAttach;
	
	public PostVO() {
		super();
		int i = 0;
		i++;
	}
	
	public PostVO(Long id) {
		super(id);
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setBoardId(long boardId) {
		this.boardId = boardId;
	}

	public String getOwnerType() {
		return "post";
	}

	@Override
	public String toString() { 
		return "PostVO [" + toString4ChildPrev() + ", title=" + title 
				+ ", likeCount=" + likeCount 
				+ ", dislikeCount=" + dislikeCount 
				+ "," + toString4ChildPost() + "]";
	}
	
	public boolean hasAttach() {
		if (attachJson != null && attachJson.length > 0) {
			listAttach = new ArrayList<>();
			Gson gson = new Gson();
			for (String strAttach : attachJson) {
				listAttach.add(gson.fromJson(strAttach, AttachVO.class));
			}
			return true;
		}
		return false;
	}
}
