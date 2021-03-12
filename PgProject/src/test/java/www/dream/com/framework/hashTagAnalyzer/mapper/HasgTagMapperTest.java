package www.dream.com.framework.hashTagAnalyzer.mapper;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import www.dream.com.framework.hashTagAnalyzer.model.HashTagVO;
import www.dream.com.framework.hashTagAnalyzer.model.mapper.HashTagMapper;
import www.dream.com.framework.hashTagAnalyzer.service.HashTagService;
import www.dream.com.framework.util.BeanUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src\\main\\webapp\\WEB-INF\\spring\\root-context.xml")
public class HasgTagMapperTest {
	@Autowired
	private HashTagMapper hashTagMapper;
	
	// db상에 입력한 String이 있는지 sysout으로 확인
	//@Test
	public void findExisting() {
		try { 
			for (HashTagVO hashTagVO : hashTagMapper.findExisting(new String[] {"IT", "자두", "영등포"}))
				System.out.println(hashTagVO);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 경쟁이 있는 곳(ID select가 같은 값이 나온다면 충돌 발생)은 언제나 
	 * 준 동시적으로 처리 요청하는 기능을 만들어서 안정성을 검사하여야 합니다.
	 */
	@Test
	public void createHashTagByBatch() {
		try {
			List<HashTagVO> listNewTag = new ArrayList<>();
			HashTagVO ddd = new HashTagVO();
			ddd.setWord("AAAA");
			listNewTag.add(ddd);
			ddd = new HashTagVO();
			ddd.setWord("BBBBB");
			listNewTag.add(ddd);
			
			HashTagService hashTagService = BeanUtil.getBean(HashTagService.class);
			hashTagService.createHashTag(listNewTag);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
