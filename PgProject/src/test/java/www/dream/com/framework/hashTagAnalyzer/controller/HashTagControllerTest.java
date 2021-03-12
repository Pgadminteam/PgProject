package www.dream.com.framework.hashTagAnalyzer.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import www.dream.com.board.model.PostVO;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"file:src\\main\\webapp\\WEB-INF\\spring\\appServlet\\servlet-context.xml",
	"file:src\\main\\webapp\\WEB-INF\\spring\\root-context.xml"})
public class HashTagControllerTest {
	@Autowired
	private WebApplicationContext ctx;
	
	private MockMvc mockMvc;	//Browser 흉내
	
	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
	}
	
	// 게시글 제목, 내용을 넣고 komoran 통해 해시태그 생성되는지 확인
	@Test
	public void testListPost() {
		PostVO post = new PostVO();
		post.setTitle("비동기 방식이란");
		post.setContent("비동기 방식은 웹페이지를 리로드하지 않고 데이터를 불러오는 방식이며 Ajax를 통해서 서버에 요청을 한 후 멈추어 있는 것이 아니라 그 프로그램은 계속 돌아간다는 의미를 내포하고 있다.");

		ObjectMapper mapper = new ObjectMapper();
		
		try {
			String jsonPost = mapper.writeValueAsString(post);
			ResultActions resultActions = mockMvc.perform(post("/hashtag/extractHashTag.json")
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.content(jsonPost));
			MvcResult mvcResult = resultActions.andReturn();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
