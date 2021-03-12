package www.dream.com.framework.model;

import java.util.List;

import www.dream.com.framework.attachFile.model.AttachVO;
/**공통 컴포넌트 설계
 * 첨부 파일인 객체들은 본 인터페이스를 기반으로 
 * AttachMapper에서 정의한 함수를 통하게 합니다.
 */
public interface IAttacher {
	// 1) id(해당 게시글이 있는 게시판의 id)
	public long getId();
	// 2) ownerType(첨부파일을 가진 게시글)
	public String getOwnerType();
	// 3) ListAttach(파일 목록)을 get으로 받게됩니다.
	public List<AttachVO> getListAttach();
}



