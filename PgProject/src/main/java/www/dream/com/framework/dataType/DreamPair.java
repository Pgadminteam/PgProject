package www.dream.com.framework.dataType;

import java.io.Serializable;

//다른 자료형의 두 값을 비교 및 일괄 처리하기 위해 사용함
public class DreamPair<F, S> implements Serializable {
	private F first;
	private S second;
	
	public DreamPair(F first, S second) {
		this.first = first;
		this.second = second;
	}
	
	public F getFirst() {
		return first;
	}
	public S getSecond() {
		return second;
	}
}
