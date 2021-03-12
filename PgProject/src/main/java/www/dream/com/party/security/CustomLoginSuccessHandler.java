package www.dream.com.party.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

	/*
	 * 로그인을 하고나면 회원의 authority를 확인해 할당된 권한에 맞게 페이지를 redirect시키기 위해 사용함
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication auth) throws IOException, ServletException {
		List<String> listRoleName = new ArrayList<>();
		
		auth.getAuthorities().forEach(authority-> {listRoleName.add(authority.getAuthority());
		});
		
		if (listRoleName.contains("ROLE_ADMIN")) {
			response.sendRedirect("/post/listPost?boardId=2");
			return;
		}
		if (listRoleName.contains("ROLE_MEMBER")) {
			response.sendRedirect("/post/listPost?boardId=1");
			return;
		}
		
		response.sendRedirect("/");
	}
}
