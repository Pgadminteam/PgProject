package www.dream.com.board.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

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

import www.dream.com.board.model.ReplyVO;
import www.dream.com.party.model.PartyVO;

/*
 * @RunWith으로 junitClassRunner class를 따로 지정해주어 junit에 지정된 runner가 아닌 내가 설정한 class를 실행함
 * @ContextConfiguration에 xml파일을 지정해주어 내가 사용하고자 하는 xml을 사용하게 함
 * 그 외 mockmvc나 webapplicationcontext는 post랑 동일함
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"file:src\\main\\webapp\\WEB-INF\\spring\\appServlet\\servlet-context.xml",
	"file:src\\main\\webapp\\WEB-INF\\spring\\root-context.xml"})
public class ReplyControllerTest {
	@Autowired
	private WebApplicationContext ctx;
	
	private MockMvc mockMvc;	//Browser 흉내
	
	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
	}
	
	// 댓글 생성 테스트
	//@Test
	public void testCreateReply() {
		ReplyVO reply = new ReplyVO();
		reply.setOriginalId(19955384);
		PartyVO writer = new PartyVO();
		writer.setId(1);
		reply.setWriter(writer);
		reply.setContent("비동기 방식은 웹페이지를 리로드하지 않고 데이터를 불러오는 방식이며 Ajax를 통해서 서버에 요청을 한 후 멈추어 있는 것이 아니라 그 프로그램은 계속 돌아간다는 의미를 내포하고 있다.");

		ObjectMapper mapper = new ObjectMapper();
		
		try {
			String jsonReply = mapper.writeValueAsString(reply);
			ResultActions resultActions = mockMvc.perform(post("/replies/new")
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.content(jsonReply));
			MvcResult mvcResult = resultActions.andReturn();
			System.out.println(mvcResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 댓글 수정 테스트
	@Test
	public void testUpdateReply() {
		ReplyVO reply = new ReplyVO();
		reply.setId(19955385);
		reply.setContent("sdtyhwe thywujsrtgj");

		ObjectMapper mapper = new ObjectMapper();
		
		try {
			String jsonReply = mapper.writeValueAsString(reply);
			ResultActions resultActions = mockMvc.perform(put("/replies/" + reply.getId())
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.content(jsonReply));
			MvcResult mvcResult = resultActions.andReturn();
			System.out.println(mvcResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
