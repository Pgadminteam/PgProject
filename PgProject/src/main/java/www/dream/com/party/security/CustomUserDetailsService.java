package www.dream.com.party.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import www.dream.com.party.model.PartyVO;
import www.dream.com.party.model.mapper.PartyMapper;

//Test에서 사용
public class CustomUserDetailsService implements UserDetailsService {
	@Autowired
	private PartyMapper partyMapper;

	//username : 사용자의 로그인 아이디입니다.
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		PartyVO loginUser = partyMapper.findPersonByLoginId(username);
		//loginUser이 null이면 CustomUser 생성자에서 NullpointerException 발생
		return loginUser == null ? null : new CustomUser(loginUser);
	}

}
