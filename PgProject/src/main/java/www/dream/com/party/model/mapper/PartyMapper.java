package www.dream.com.party.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import www.dream.com.framework.model.Criteria;
import www.dream.com.party.model.PartyVO;

public interface PartyMapper {
	
	// authority가 같은 모든 회원정보를 list에 담는다.
	public List<PartyVO> selectAllParty(String authority);
	// id값을 이용해 id값이 일치하는 회원의 모든 정보를 PartyVO에 담는다.
	public PartyVO findPartyById(long id);

	/* src/test/java에서 junitTest를 통해 값이 변하는지 printout으로 확인하기 위해 만듦 실제 웹에서 사용되지 않음 */
	public PartyVO findPersonByLoginId(@Param("loginId") String loginId);
	public boolean changePwd(PartyVO person);

	/* 중복체크 */
	public boolean chkIdDup(String newId);
	public boolean chkNickDup(String newNick);
	public boolean chkNickDupInModify(@Param("id") long id, @Param("newNick") String newNick);

	// 회원 CRUD를 위해 사용
	// CREATE
	public void registerParty(PartyVO newbie);
	public void registerAuthority(long id, String authority);
	// UPDATE
	public boolean modifyParty(PartyVO newbie);
	public void changePW(PartyVO newbie);
	// DELETE
	public boolean removeParty(PartyVO newbie);
	public boolean removeAuthority(PartyVO newbie);
	public int removeAdminParty(long[] arrTargetParty);
	public boolean removeAdminAuthority(long[] arrTargetParty);
	
	// t_party's loginId
	public PartyVO findPartyByLoginId(String loginId);
	// 사용자가 입력한 name, loginId 인증 각각 체크 
	public boolean chkNameExist(String chkName);
	public boolean chkLoginIdExist(String chkLoginId);
	// temp pw 바꾸기
	public void updateTmpPw(PartyVO newbie);
	
	/* 관리자가 "ROLE_MEMBER"인 모든 회원 정보를 볼수 있게 countTotalPartyWithPaging로 총 개수를 구하고
	 * listPartyWithPaging로 criteria에 맞춰 리스트를 출력한다.
	*/
	public long countTotalPartyWithPaging(Criteria criteria);
	public List<PartyVO> listPartyWithPaging(Criteria criteria);
}
