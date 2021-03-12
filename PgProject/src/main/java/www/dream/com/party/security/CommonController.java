package www.dream.com.party.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/*
 * Error가 발생할 시 띄우는 메시지, 로그인 시 아이디,비번이 맞지않아 생기는 error + logout에 각각의 메시지 생성해 띄워 줌
 */
@Controller
public class CommonController {
	@GetMapping("/accessError")
	public void accessDenied(Authentication auth, Model model) {
		model.addAttribute("msg", "Denied because you do not have access rights.");
	}

	@GetMapping("/customLogin")
	public void loginInput(String error, String logout, Model model) {
		if (error != null) {
			model.addAttribute("error", "Login failed! Please check your account.");
		} else if (logout != null) {
			model.addAttribute("logout", "Goodbye! We look forward to seeing you again.");
		}
	}
	
	@GetMapping("/customLogout")
	public void logoutGet(String error, String logout, Model model) {
	}

	@PostMapping("/customLogout")
	public void logoutPost() {
	}

}
