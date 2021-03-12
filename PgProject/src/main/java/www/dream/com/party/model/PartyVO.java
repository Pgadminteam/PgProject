package www.dream.com.party.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import www.dream.com.framework.model.CommonMngInfoVO;

@Data
@NoArgsConstructor
public class PartyVO extends CommonMngInfoVO implements Serializable {
	// caption을 활용해 list를 출력할 table의 th를 자동으로 출력하게 만듦
	@Getter @Setter
	private long id;
	@Getter @Setter
	private String loginId;
	@Getter @Setter
	private String name;
	
	@Getter @Setter
	private String nickname;

	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Getter @Setter
	private Date birthDate;
	
	@Getter @Setter
	private String password;
	
	/* 연관 정보 정의 부분 */
	@Getter @Setter
	private List<AuthorityVO> listAuthority;
	
	public PartyVO(long id) {
		this.id = id;
	}

	// 비밀번호를 암호화하게 해주는 함수
	public void encodePassword(PasswordEncoder passwordEncoder) {
		password = passwordEncoder.encode(password);
	}
}
