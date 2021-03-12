package www.dream.com.party.model.mapper;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import www.dream.com.party.model.PartyVO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"file:src\\main\\webapp\\WEB-INF\\spring\\root-context.xml",
	"file:src\\main\\webapp\\WEB-INF\\spring\\security-context.xml"})
public class PartyMapperTest {
	@Autowired
	private PartyMapper partyMapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	/* 1-assertnotnull 이용해 partymapper가 null이 아닌지 확인
       2-foreach통해 selectallparty로 “ROLE_MEMBER”를 가진 회원정보 다 가져와 partyVO party에 하나씩 넣고 sysout함
    */
	@Test
	public void testPerson() {
		try { 
		assertNotNull(partyMapper);
		for (PartyVO party : partyMapper.selectAllParty("ROLE_MEMBER"))
			System.out.println(party);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* 1-partymapper의 findpersonbtloginid를 통해 loginid가 “a”인 회원을 partyVO test로 저장(db에 있음)
       2-test에 pw를 passwordencoder의 encode함수로 “a”를 set
       3-partymapper의 changepwd를 이용해 test를 바꿔줌
    */
	//@Test
	public void testChgPwd() {
		try {
			/*
			PartyVO creator = partyMapper.findPersonByLoginId("creator");
			creator.setPassword(passwordEncoder.encode("creator"));
			partyMapper.changePwd(creator);

			PartyVO hongkildong = partyMapper.findPersonByLoginId("hong");
			hongkildong.setPassword(passwordEncoder.encode("hong"));
			partyMapper.changePwd(hongkildong);
			 */
			
			PartyVO test = partyMapper.findPersonByLoginId("a");
			test.setPassword(passwordEncoder.encode("a"));
			partyMapper.changePwd(test);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* 1-partyVO test에 findpersonbyloginid를 이용해  “a”의 회원정보 찾음(“a”말고 db상에 존재하는 아무 loginid써도 상관없음 다만 testchgpwd를 거쳤어야 가능)
       2-sysout test를 하고 pw가 encoding되어 들어갔는지 확인함 
	*/
	//@Test
	public void testPwd() {
		try {
			PartyVO creator = partyMapper.findPersonByLoginId("creator");
			System.out.println(creator);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
