package www.dream.com.board.controller;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import www.dream.com.board.model.ReplyVO;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"file:src\\main\\webapp\\WEB-INF\\spring\\appServlet\\servlet-context.xml",
	"file:src\\main\\webapp\\WEB-INF\\spring\\root-context.xml"})
public class PostControllerTest {
	/*
	 * Mockmvc와 WebApplicationContext를 선언해 browser를 흉내내는 형식으로 test program을 한다.
	 * (mockmvc:스프링 mvc의 설정을 적용한 di컨테이너를 만들어 이를 사용해 스프링mvc동작을 재현함 애플리케이션 서버에 배포한 것과 같은 것처럼 테스트 가능하게 해줌
	 *  webapplicationcontext: 서블릿에서만 이용됨 DispatcherServlet이 직접 사용하는 컨트롤러를 포함한 웹 관련 빈을 등록하는 데 사용)
	 */
	@Autowired
	private WebApplicationContext ctx;
	
	private MockMvc mockMvc;	//Browser 흉내
	
	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
	}
	
	// 게시글을 목록형식으로 만들어 게시글 출력
	@Test
	public void testListPost() {
		try {
			MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/post/list?boardId=1");
			ResultActions resultActions = mockMvc.perform(requestBuilder);
			MvcResult mvcResult = resultActions.andReturn();
			ModelAndView mav = mvcResult.getModelAndView();
			ModelMap mm = mav.getModelMap();
			List<ReplyVO> list = (List<ReplyVO>) mm.getAttribute("listPost");
			for (ReplyVO post : list) {
				System.out.println(post);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
