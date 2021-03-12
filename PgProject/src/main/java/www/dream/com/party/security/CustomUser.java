package www.dream.com.party.security;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;
import www.dream.com.party.model.PartyVO;

@Getter
public class CustomUser extends User {
	private static final long serialVersionUID = 2L;

	private PartyVO loginUser;
	
	public CustomUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}

	public CustomUser(PartyVO user) {
		/* Lambda expression map : 목록에서 빨대 꼽아서 나오는 각 객체에 지정된 함수를 적용하여 나오는 결과를 다시 스트림으로 만들어 줍니다.
		 * repltController에서 registerReply에 데이터를 활용하려고 사용함
		 * 정리하자면, 컬렉션, 배열등의 저장 요소를 하나씩 참조하며, 함수형 인터페이스(람다식)를 적용하며 반복적으로 처리할 수 있도록 해주는 기능인데
		 * 스트림을 이용하면 한 줄의 코딩만으로 count값을 구할 수 있다. 즉, 불필요한 코딩(for, if 문법)을 걷어낼 수 있고 직관적이기 때문에 가독성이 좋아진다.
		 */
		super(user.getLoginId(), user.getPassword(), 
				user.getListAuthority().stream().map(
				auth -> new SimpleGrantedAuthority(auth.getAuthority()))
				.collect(Collectors.toList()));
		this.loginUser = user;
	}

}
