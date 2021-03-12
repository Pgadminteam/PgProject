package www.dream.com.party.service;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import www.dream.com.framework.email.EmailSender;
import www.dream.com.framework.email.model.Email;
import www.dream.com.framework.model.Criteria;
import www.dream.com.party.model.PartyVO;
import www.dream.com.party.model.mapper.PartyMapper;

@Service
public class PartyService {
	@Autowired
	private PartyMapper partyMapper;
	
	@Autowired
	private EmailSender emailSender;
	
	@Autowired
	private Email email;

	@Autowired
	private PasswordEncoder passwordEncoder;

	// test에서 사용
	public List<PartyVO> selectAllParty(String authority) {
		return partyMapper.selectAllParty(authority);
	}
	// 회원정보의 총개수를 찾는다. countTotalPartyWithPaging를 이용해 찾은 값을 cnt에 담고 cnt로 리턴한다.
	public long countTotalPartyWithPaging(Criteria criteria) {
		long cnt = partyMapper.countTotalPartyWithPaging(criteria);
		return cnt;
	}

	// listPartyWithPaging를 이용해 criteria를 담아 리턴한다
	public List<PartyVO> listPartyWithPaging(Criteria criteria) {
		return partyMapper.listPartyWithPaging(criteria);
	}

	// findPartyById를 이용해 회원정보를 다 찾은 후 PartyVO newbie에 넣고 newbie로 리턴한다.
	public PartyVO findPartyById(long id) {
		PartyVO newbie = (PartyVO) partyMapper.findPartyById(id);
		return newbie;
	}
	
	// 임시비밀번호 발송을 위해 loginId는 중복값이 없는 유일한 값이기 때문에 loginId값을 이용해 findPartyByLoginId를 한 후 리턴한다.
	public PartyVO findPartyByLoginId(String loginId) {
		return partyMapper.findPartyByLoginId(loginId);
	}
	
	// 입력된 정보를 db로 넘길 때 password는 encodePassword 함수를 이용해 암호화 하고 registerParty를 이용해 newbie를 db에 저장한다.
	// newbie의 id값을 받아와 "ROLE_MEMBER"라는 권한을 부여해준다.
	public void registerParty(PartyVO newbie) {
		newbie.encodePassword(passwordEncoder);
		partyMapper.registerParty(newbie);
		partyMapper.registerAuthority(newbie.getId(), "ROLE_MEMBER");
	}
	
	// modifyParty를 이용해 newbie를 담아 리턴한다.
	public boolean modifyParty(PartyVO newbie) {
		return partyMapper.modifyParty(newbie);
	}
	
	// removeParty를 이용해 newbie를 담아 리턴한다.
	public boolean removeParty(PartyVO newbie) {
		partyMapper.removeAuthority(newbie);
		return partyMapper.removeParty(newbie);
	}
	
	public int removeAdminParty(long[] arrTargetParty) {
		partyMapper.removeAdminAuthority(arrTargetParty);
		return partyMapper.removeAdminParty(arrTargetParty);
	}

	// 바꾼 비밀번호 값을 encodePassword 함수를 이용해 암호화하고 changePW를 이용해 newbie를 담아 리턴한다.
	public void changePW(PartyVO newbie) {
		newbie.encodePassword(passwordEncoder);
		partyMapper.changePW(newbie);
	}
	
	// new(~~)값을 받아 중복을 검사하고 그 값을 리턴한다
	public boolean chkIdDup(String newId) {
		return partyMapper.chkIdDup(newId);
	}
	
	public boolean chkNickDup(String newNick) {
		return partyMapper.chkNickDup(newNick);
	}
	
	public boolean chkNickDupInModify(long id, String newNick) {
		return partyMapper.chkNickDupInModify(id, newNick);
	}
	
	/* 인증절차를 위해, name, loginId를 각각 받음 */
	public boolean chkNameExist(String chkName) {
		return partyMapper.chkNameExist(chkName);
	}

	public boolean chkLoginIdExist(String chkLoginId) {
		return partyMapper.chkLoginIdExist(chkLoginId);
	}
	
	/**
	 * ascii code에 따라 '영문소문자, 영문대문자, 숫자'를 랜덤으로 생성
	 * 정규식의 최소자리수인 8자리로 생성되도록 만큼 맞춤
	 * 기존의 비밀번호를 새롭게 임시 발급한 비밀번호로 바꿀 때, 암호화하여 저장
	 * @param newbie
	 * @throws Exception
	 */
	public void updateTmpPw(PartyVO newbie) throws Exception {
		
		String tempPassword = "";	
		
		for (int i = 0; i < 8; i++) {
			// ascii code에 따라 a~z까지 임의의 영문 소문자 8자리 만들기
			tempPassword += (char) ((Math.random() * 26) + 97);
		}

		// person에 임시비밀번호 입력
		newbie.setPassword(tempPassword);
		
		/* 
		1) 먼저 이메일 객체를 생성하고 제목과 내용을 채운뒤 (setSubject, setContent) 
		2) 수신자(사용자로 부터 입력받은 loginId : gmail)의 메일 주소에
		3) 미리 정의한 emailSender객체를 사용하여 이메일을 발송합니다.
		*/
		email.setSubject("안녕하세요 " + newbie.getName() + "님  재설정된 비밀번호를 확인해주세요");
		email.setContent("새로운 비밀번호 " + newbie.getPassword() + " 입니다.");
		email.setReciver(newbie.getLoginId()+"@gmail.com");
		emailSender.SendEmail(email);

		// 비밀번호를 암호화하여, newbie에 저장
		newbie.encodePassword(passwordEncoder);
		partyMapper.updateTmpPw(newbie);
	}	
}
