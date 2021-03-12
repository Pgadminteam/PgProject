package www.dream.com.framework.hashTagAnalyzer.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.util.common.model.Pair;
import www.dream.com.framework.dataType.DreamPair;
import www.dream.com.framework.hashTagAnalyzer.model.HashTagVO;
import www.dream.com.framework.hashTagAnalyzer.model.mapper.HashTagMapper;

@Service
/*
 * 컨텍스트에서 빈을 만드는 기본은 Singleton(단 한개 - Service, Control Bean은 처리 객체다.)
 * 다중 처리 등으로 여러 객체을 만들 필요가 있다면 @Scope("prototype")을 적용해 놓고
 */
@Scope("prototype")
public class HashTagService {
	private static Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);
	
	@Autowired
	private HashTagMapper hashTagMapper;
	
	// 데이터의 추가나 삭제시 트리가 한쪽으로 치우쳐지지 않도록 균형을 맞췄습니다.
	// 여기서 사용하는 이유는 해시태그 생성시 한쪽(품사)으로 쏠리지 않게 하기 위해 사용합니다.
	private static Set<String> setTargetLexiconType = new TreeSet<>();
	static {
		//코모란 형태소 품사정보 분류에 따라 필요한 것만 골라 정의함
		setTargetLexiconType.add("SL");  // 외국어
		setTargetLexiconType.add("NA");  // 형용사
		setTargetLexiconType.add("NNG"); // 일반명사
		setTargetLexiconType.add("NNP"); // 고유명사
	}

	/**
	 * treeset 사용해 추가와 삭제에는 시간이 조금 걸리지만 검색과 정렬면에서는 높은 성능을 보여 해시태그 생성 시 한 품사로 쏠리지 않게 균형을 유지하기 위해 사용했음.
	 * String...의 "..."은 java의 가변인자(var args)인데, 함수는 같지만 매개변수가 여러 개 일 때(매개변수가 매번 다르게 들어갈 때)를 위해 사용했음
	 * forEach를 이용해 게시글을 komoran을 통해 varText에서 lexiconType에 맞게 분류해서 나온 단어들을 pair구조를 이용해 단어,품사정보를 담고
	 * 또 forEach와 pair를 이용해 treeset에 담는다.
	 */
	public Set<String> extractHashTag(String... varText) {
		Set<String> ret = new TreeSet<>();

		for (String text : varText) {
			KomoranResult komoranResult = komoran.analyze(text);
			List<Pair<String, String>> sentence = komoranResult.getList();
			for (Pair<String, String> token : sentence) {
				if (setTargetLexiconType.contains(token.getSecond()))
					ret.add(token.getFirst());
			}
		}
		return ret;
	}

	/**
	 * 
	 * @param arrHashTag
	 * @return first : 있는 것. 즉 교집합, second : 새로운 단어
	 */
	public DreamPair<List<HashTagVO>, List<HashTagVO>> split(String[] arrHashTag) {
		/* 아래와 동일한 기능을 고전적 잘못된 방식으로 개발한 사례
		List<HashTagVO> listExistingㅇㅇㅇㅇ = hashTagMapper.findExisting(arrHashTag);
		List<HashTagVO> listNewTag = new ArrayList<>();
		O :
		for (String aWord : arrHashTag) {
			for (HashTagVO e : listExistingㅇㅇㅇㅇ) {
				if (e.getWord().equals(aWord)) {
					continue O;
				}
			}
			HashTagVO ddd = new HashTagVO();
			ddd.setWord(aWord);
			listNewTag.add(ddd);
		}
		*/
		
		//배열에서 주어진 단어를 바탕으로 전체 단어 객체를 만들었음
		List<HashTagVO> listFullTag = new ArrayList<>();
		for (String aWord : arrHashTag) {
			if (aWord.isEmpty())
				continue;
			HashTagVO ht = new HashTagVO();
			ht.setWord(aWord);
			listFullTag.add(ht);
		}
		//기존 테이블에서 관리 중인 단어 객체를 찾음
		List<HashTagVO> listExisting = hashTagMapper.findExisting(arrHashTag);
		//HashTagVO.equals 기능을 word를 바탕으로 만들어서 일괄 삭제함.
		//따라서 새로이 나타난 단어들을 찾음
		listFullTag.removeAll(listExisting);
		
		return new DreamPair<>(listExisting, listFullTag);
	}

	/**
	 * 해시태그 생성, selectNewID를 통해 입력될 id값을 받아와 forEach를 이용해 id,해시태그 배열로 만듦
	 */
	@Transactional
	public void createHashTag(List<HashTagVO> listHashTag) {
		if (!listHashTag.isEmpty()) {
			long newId = hashTagMapper.selectNewID();
			for (HashTagVO obj : listHashTag)
				obj.setId(newId++);
			hashTagMapper.createHashTag(listHashTag);
		}
	}

	/** createRelWithReply / deleteRelWithReply
	 * hashtag는 고유의 id값이 존재하고 그 hashtag들이 어느 reply에서 쓰였는지 저장하기 위해 reply_id와 연결시켜 하나의 m_ht_reply라는 table을 만들어 놓음.
	 * postService에서 게시글을 생성, 수정, 삭제할 때마다 사용함
	 * 생성=create / 수정=전체 delete후 다시 create / 삭제=delete
	 */
	public void createRelWithReply(long replyId, List<HashTagVO> listHashTag) {
		if (!listHashTag.isEmpty()) 
			hashTagMapper.createRelWithReply(replyId, listHashTag);
	}

	public void deleteRelWithReply(long replyId) {
		hashTagMapper.deleteRelWithReply(replyId);
	}
}
