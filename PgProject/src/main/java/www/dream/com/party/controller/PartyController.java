package www.dream.com.party.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import www.dream.com.framework.model.Criteria;
import www.dream.com.party.model.PartyVO;
import www.dream.com.party.service.PartyService;

@RestController
@RequestMapping("/party/*")
public class PartyController {
	@Autowired
	private PartyService partyService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	/**
	 * "ROLE_MEMBER"를 가진 회원이 몇명인지 세고 countTotal에 담는다.
	 * listPartyWithPaging를 이용해 "ROLE_MEMBER"를 가진 회원의 정보를 배열로 만든다.
	 * 
	 * ModelAndView의 model에 회원정보 배열을 담는다.
	 * ModelAndView의 model에 criteria를 담는다.
	 * ModelAndView의 view에 listParty.jsp를 연결하고 ModelAndView를 리턴한다.
	 * @param criteria
	 * @param model
	 */
	@GetMapping("listParty")
	public ModelAndView listParty(Criteria criteria) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/party/listParty");

		// countTotal의 값이 여기서 마지막으로 바뀌어 저장되고 이후로 변경될 일이 없어서 final을 붙여줌 final이 없어도 구동에는 지장이 없으나 선언해주는 것이 더 나음
		final long countTotal = partyService.countTotalPartyWithPaging(criteria);
		criteria.setTotalDataCount(countTotal);
		
		List<PartyVO> listParty = partyService.listPartyWithPaging(criteria);
		
		mav.getModel().put("listParty", listParty);
		mav.getModel().put("criteria", criteria);
		return mav;
	}

	/** READ
	 * id를 받아와 그 id값을 가진 회원의 전체 정보를 findPartyById로 받아서 PartyVO newbie에 저장한다.
	 * ModelAndView를 선언하고 view에 partyDetail.jsp를 담는다.
	 * ModelAndView의 model에 newbie를 담고, 그 newbie의 id값을 받아와 id값도 담는다.
	 * ModelAndView를 리턴한다.
	 * @param id
	 * @param session
	 * @return
	 */
	@GetMapping("partyDetail")
	public ModelAndView showPartyDetail(@RequestParam("id") long id, HttpSession session) {
		PartyVO newbie = (PartyVO) partyService.findPartyById(id);
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/party/partyDetail");
		mav.getModel().put("newbie", newbie);
		mav.getModel().put("id", newbie.getId());
		return mav;
	}

	/**
	 * PostMapping("registerParty")를 열기 위해 ModelAndView의 view에 담고 ModelAndView를 리턴한다.
	 * @return
	 */
	@GetMapping("openRegisterParty")
	public ModelAndView registerParty() {
		ModelAndView mav = new ModelAndView("/party/registerParty");
		return mav;
	}

	/** CREATE
	 * 회원이 폼에 입력한 값을 받아와 registerParty로 db에 등록하고 이를 PartyVO newbie에 저장한다.
	 * rttr에 새로 생성된 id값을 넣어준다.
	 * ModelAndView의 view에 customLogin.jsp를 연결하고 ModelAndView로 리턴한다.
	 * @param newbie
	 * @param rttr
	 * @return
	 */
	@PostMapping("registerParty")
	public ModelAndView registerParty(PartyVO newbie, RedirectAttributes rttr) {
		ModelAndView mav = new ModelAndView("redirect:/customLogin");
		partyService.registerParty(newbie);

		rttr.addAttribute("id", newbie.getId());
		return mav;
	}
	
	/**
	 * ModelAndView를 선언한다.
	 * RequestParam으로 id값을 받아와 findPartyById를 이용해 회원정보를 얻고 PartyVO newbie에 저장한다.
	 * 만약 newbie가 null값이 아니라면 ModelAndView의 view에 PostMapping("modifyParty")를 연결하고
	 * ModelAndView의 model에 newbie를 담아 ModelAndView를 리턴한다.
	 * else = newbie가 null값일 경우 ModelAndView의 view에 홈을 연결하고 ModelAndView를 리턴한다.
	 * @param id
	 * @return
	 */
	@GetMapping("modifyParty")
	public ModelAndView showModifyParty(@RequestParam("id") long id) {
		//id로 정보 조회하여 수정화면에 출력한다
		ModelAndView mav = new ModelAndView();
		PartyVO newbie = (PartyVO) partyService.findPartyById(id);
		if (newbie != null) {
			mav.setViewName("/party/modifyParty");
			mav.getModel().put("newbie", newbie);
			return mav;
		} else {
			mav.setViewName("redirect:/");
			return mav;
		}
	}
	
	/** UPDATE
	 * PreAuthorize를 이용해 지금 로그인된 유저의 id와 GetMapping("modifyParty")에서 넘긴 newbie의 id가 같으면 실행한다
	 * 회원이 변경하고자 하는 정보를 폼에 입력하면 그값을 modifyParty를 이용해 db에 update한다
	 * session에 그전에 이미 연결된 정보가 계속 유지되기 때문에 update된 정보를 적용할수 없어서 session을 초기화한다.
	 * ModelAndView의 view에 customLogin.jsp를 연결하고 ModelAndView를 리턴한다.
	 * @param newbie
	 * @param rttr
	 * @param session
	 * @return
	 */
	@PreAuthorize("principal.loginUser.id == #newbie.id")
	@PostMapping("modifyParty")
	public ModelAndView modifyParty(PartyVO newbie, HttpSession session) {
		ModelAndView mav = new ModelAndView("redirect:/customLogin");
		partyService.modifyParty(newbie);
		session.invalidate();
		return mav;
	}
	
	/**
	 * id값을 받아와 findPartyById를 이용해 PartyVO newbie에 회원정보를 담는다.
	 * 만약 newbie가 null이 아니라면 ModelAndView의 model에 newbie를 담고 view에 PostMapping("removeParty")를 연결하여 ModelAndView를 리턴한다.
	 * else = newbie가 null이라면 ModelAndView의 view에 홈을 연결해 ModelAndView를 리턴한다.
	 * @param id
	 * @return
	 */
	@GetMapping("openRemoveParty")
	public ModelAndView removeParty(@RequestParam("id") long id) {
		ModelAndView mav = new ModelAndView("/party/removeParty");
		PartyVO newbie = (PartyVO) partyService.findPartyById(id);
		if (newbie != null) {
			mav.setViewName("/party/removeParty");
			mav.getModel().put("newbie", newbie);
			return mav;
		} else {
			mav.setViewName("redirect:/");
			return mav;
		}
	}
	
	/** DELETE
	 * GetMapping("openRemoveParty")에서 넘긴 newbie의 id값을 받아서 findPartyById로 회원 정보를 받아 PartyVO newbie에 담는다.
	 * removeParty를 이용해 db에 newbie를 삭제한다.
	 * session은 유지되기때문에 초기화시켜 연결을 해제한다.
	 * ModelAndView의 view에 홈을 연결하여 ModelAndView를 리턴한다.
	 * @param id
	 * @param session
	 * @return
	 */
	@PostMapping("removeParty")
	public ModelAndView removeParty(@RequestParam("id") long id, HttpSession session) {
		ModelAndView mav = new ModelAndView("redirect:/");
		PartyVO newbie = (PartyVO) partyService.findPartyById(id);
		partyService.removeParty(newbie);
		session.invalidate();
		return mav;
	}
	

	@PostMapping("removeAdminParty")
	public ResponseEntity<Integer> removeAdminParty(long[] arrTargetParty){
		System.out.println(arrTargetParty);
		return new ResponseEntity<>(partyService.removeAdminParty(arrTargetParty), HttpStatus.OK);
	}
	
	/**
	 * id를 받아와 findPartyById를 이용해 얻은 회원 정보를 PartyVO newbie에 담는다
	 * 만약 newbie가 null이 아니라면 ModelAndView의 model에 newbie를 담고 view에 PostMapping("changePW")를 연결하여 ModelAndView를 리턴한다.
	 * else = newbie가 null이라면 ModelAndView의 view에 홈을 연결해 ModelAndView를 리턴한다.
	 * @param id
	 * @return
	 */
	@GetMapping("openChangePW")
	public ModelAndView showChangePW(@RequestParam("id") long id) {
		ModelAndView mav = new ModelAndView("/party/changePW");
		PartyVO newbie = (PartyVO) partyService.findPartyById(id);
		if (newbie != null) {
			mav.setViewName("/party/changePW");
			mav.getModel().put("newbie", newbie);
			return mav;
		} else {
			mav.setViewName("redirect:/");
			return mav;
		}
	}
	
	/**
	 * GetMapping("openChangePW")에서 받아온 newbie값을 이용해 changePW로 비밀번호만 변경한다.
	 * 바뀐 비밀번호로 다시 로그인하게 만들기 위해 session을 초기화해서 연결을 해제한다.
	 * ModelAndView의 view에 홈을 연결해 ModelAndView로 리턴한다.
	 * @param newbie
	 * @param rttr
	 * @param session
	 * @return
	 */
	@PostMapping("changePW")
	public ModelAndView changePW(PartyVO newbie, HttpSession session) {
		ModelAndView mav = new ModelAndView("redirect:/");
		partyService.changePW(newbie);
		session.invalidate();
		return mav;
	}

	/**
	 * registerParty.jsp의 ajax에서 사용한다.
	 * 새로 입력한 loginId값을 newId로 칭하고 chkIdDup을 이용해 db에서 중복된 값이 있는지 찾는다.
	 * @param newId
	 * @return
	 */
	@GetMapping(value = "chkIdDup/{newId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Boolean> chkIdDup(@PathVariable("newId") String newId) {
		return new ResponseEntity<>(partyService.chkIdDup(newId), HttpStatus.OK);
	}

	/**
	 * registerParty.jsp의 ajax에서 사용한다.
	 * 새로 입력한 nickname값을 newNick로 칭하고 chkNickDup을 이용해 db에서 중복된 값이 있는지 찾는다.
	 * @param newNick
	 * @return
	 */
	@GetMapping(value = "chkNickDup/{newNick}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Boolean> chkNickDup(@PathVariable("newNick") String newNick) {
		return new ResponseEntity<>(partyService.chkNickDup(newNick), HttpStatus.OK);
	}
	
	/**
	 * modifyParty.jsp의 ajax에서 사용한다.
	 * 새로 입력한 nickname값을 newNick로 칭하고 chkNickDupInModify을 이용해 db에서 자신의 id값의 nickname을 제외하고 나머지 중 중복된 값이 있는지 찾는다.
	 * @param newNick
	 * @return
	 */
	@GetMapping(value = "chkNickDupInModify/{id}/{newNick}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Boolean> chkNickDupInModify(@PathVariable("id") long id, @PathVariable("newNick") String newNick) {
		ResponseEntity<Boolean> ret = null;
		try {
			boolean isDup = partyService.chkNickDupInModify(id, newNick);
			ret = new ResponseEntity<>(isDup, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
			
	/* 하단은 임시비밀번호 발급 및 이메일 전송 기능을 위한 영역 입니다. */
	/* 1) 사용자가 비밀번호를 잊어버려서, 로그인을 할 수 없는 상황이다. 따라서 뿌려줄 정보가 아무것도 없음. */
	@GetMapping("openFindPassword")
	public ModelAndView showFindPassword() {
		ModelAndView mav = new ModelAndView("/party/tmpPwSendMail");
		return mav;
	}
	
	/* 2-1) 사용자 정보 인증 : 정보(name, loginId)가 일치하면, 
	 * 임시 password 발급 후, change하여 db에 저장
	 * 이를 이메일로 발송
	 */
	// 가입된 사용자인지 확인: name, loginId를 입력 받아, 인증절차를 거친다.
	@GetMapping(value = "chkNameExist/{chkName}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Boolean> chkNameExist(@PathVariable("chkName") String chkName) {
		return new ResponseEntity<>(partyService.chkNameExist(chkName), HttpStatus.OK);
	}
	
	@GetMapping(value = "chkLoginIdExist/{chkLoginId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Boolean> chkLoginIdExist(@PathVariable("chkLoginId") String chkLoginId) {
		return new ResponseEntity<>(partyService.chkLoginIdExist(chkLoginId), HttpStatus.OK);
	}	
	
	// 새로운 임시 비밀번호가 생성되어 PartyVO(DB)의 비밀번호를 바꾼다.
	@PostMapping("/tmpPwSendMail")
	public ModelAndView updateTmpPw(@RequestParam("loginId") String loginId) throws Exception {		
		// 이후, 로그인 화면으로 다시 넘어간다.
		ModelAndView mav = new ModelAndView("redirect:/customLogin");
		PartyVO newbie = (PartyVO) partyService.findPartyByLoginId(loginId);
		
		// 발급한 임시비밀번호 -> DB에 있는 party테이블의 기존 비번을 변경시킴
		partyService.updateTmpPw(newbie);
		return mav;
	}

}
