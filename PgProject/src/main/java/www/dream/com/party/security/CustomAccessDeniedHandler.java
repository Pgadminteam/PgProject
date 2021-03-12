package www.dream.com.party.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	/*
	 * 접근 불가능한 곳에 접근한다던가, role이 할당되지 않아 사용이 불가능한다던가, 
	 * 서버상에서 어떤 error가 발생할 때 등으로 인해 접근 제한이 되었을 시 보이는 화면을 처리하기 위함
	 */
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		accessDeniedException.printStackTrace();
		response.sendRedirect("/accessError");
	}

}
