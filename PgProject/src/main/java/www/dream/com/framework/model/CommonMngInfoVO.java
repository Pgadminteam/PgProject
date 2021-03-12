package www.dream.com.framework.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import www.dream.com.framework.display.Caption;
import www.dream.com.framework.display.Caption.WhenUse;

/**
 * 모든 테이블에 있는 관리 정보를 각 VO에 중복 개발하지 않고,
 * framework패키지 내부에 상속 구조를 활용하여 공통화합니다.
 */
public abstract class CommonMngInfoVO {
	@Getter @Setter
	@Caption(whenUse=WhenUse.all, caption="RegistrationDate")
	protected Date regDate;
	@Getter @Setter
	protected Date updateDate;
}


