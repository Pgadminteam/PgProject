package www.dream.com.board.model.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import www.dream.com.board.model.ReplyVO;
import www.dream.com.framework.model.Criteria;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src\\main\\webapp\\WEB-INF\\spring\\root-context.xml")
public class ReplyMapperTest {
	@Autowired
	private ReplyMapper replyMapper;
	
	/*
	 * replymapper의 listpostwithpaging에 3L, new criteria넣고 foreach통해 reply로 빼서 sysout
	 * (3L의 L은 long type알려주기 위해 넣었고 3은 boardId 아무 값이나 db에 있는거 가져오기)
	 */
	@Test
	public void findPostWithPaging() {
		try { 
			for (ReplyVO reply : replyMapper.listPostWithPaging(3L, new Criteria()))
				System.out.println(reply);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
