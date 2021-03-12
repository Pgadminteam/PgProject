package www.dream.com.framework.model;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Criteria {
	/** 표시할 '페이지의 전체 개수' */
	private static final long PAGING_AMOUNT = 10;
	/** 한 페이지에서 보여줄 '게시글의 개수' */
	public static final int DEFAULT_AMOUNT = 10;
	
	@Setter
	private String search;
	//전체 건수 나누기 쪽 당 나타낼 정보의 개수 
	private long pageNum;	
	private long startPage, endPage;
	private long totalDataCount;
	private boolean hasPrev, hasNext;
	
	//기본 범위
	public Criteria() {
		this(1, 1241);
	}
	
	public Criteria(long pageNum, long totalDataCount) {
		this.totalDataCount = totalDataCount;
		setPageNum(pageNum);
	}
	public long getOffset() {
		return (pageNum - 1) * DEFAULT_AMOUNT;
	}	
	public long getLimit() {
		return pageNum * DEFAULT_AMOUNT;
	}
	
	/**
	 * Pagenum은 구글스타일을 활용해 내가 7page에 있다면 1~10페이지 중 7번에 위치하는 것이 아닌 3~12페이지의 가운데 위치하게 됨
	 * 내가 8이나 9페이지로 넘어가면 startPage와 endPage가 그에 맞게 계속 변함
	 * ex. <3 4 5 6 "7" 8 9 10 11 12> -> <5 6 7 8 "9" 10 11 12 13 14> 
	 */
	public void setPageNum(long pageNum) {
		this.pageNum = pageNum;	
		//아래는 구글 스타일을 활용합니다.
		endPage = pageNum + PAGING_AMOUNT / 2;	
		endPage = endPage < PAGING_AMOUNT ? PAGING_AMOUNT : endPage;
		startPage = endPage - PAGING_AMOUNT + 1;
		int realEnd = (int) Math.ceil((float) totalDataCount / DEFAULT_AMOUNT);
		if (realEnd < endPage)
			endPage = realEnd;
		hasPrev = startPage > 1;
		hasNext = endPage < realEnd;
	}
	
	/**
	 * 작성된 해시태그들을 1개 이상 띄어쓰기로 검색할 때 (ex.hi haha 검색) 
	 * 앞의 조건을 만족하면 search에 들어온 공백을 제거하고 검색한 값에 대해 true로 반환하고 false면 null로 반환함
	 */
	public String[] getSearchArr() {
		return (search != null && !search.isEmpty()) ? search.split(" ") : null;
	}
	
	/**
	 * 데이터 총 개수를 위해 사용함 만약 데이터 총 개수의 끝보다 endPage가 크면 둘을 같게 설정하고 endPage가 작으면 hasNext 만듦
	 */
	public void setTotalDataCount(long totalDataCount) {
		this.totalDataCount = totalDataCount;
		int realEnd = (int) Math.ceil((float) totalDataCount / DEFAULT_AMOUNT);
		if (realEnd < endPage)
			endPage = realEnd;
		hasNext = endPage < realEnd;
	}
}




