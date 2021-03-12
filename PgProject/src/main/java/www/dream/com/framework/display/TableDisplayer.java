package www.dream.com.framework.display;

import java.lang.reflect.Field;
import java.util.List;

import www.dream.com.framework.display.Caption.WhenUse;

/*
 * StringBuilder를 사용
 * 가변길이로 생성될 수 있는 테이블 가로줄(tr)을  새로운 문자열 객체로 작성하지 않고, 기존의 데이터에 더하는 방식을 사용하여 시스템 부하를 줄였습니다.
 */
public class TableDisplayer {
	public static String displayHeader(Class<?> targetClass) {
		StringBuilder sb = new StringBuilder("<tr>");
		List<Field> listField = ClassUtil.getListField(targetClass, Caption.class);
		for (Field field : listField) {
			Caption annoCaption = field.getAnnotation(Caption.class);
			if (WhenUse.isTableTarget(annoCaption.whenUse())) {
				sb.append("<th>").append(annoCaption.caption()).append("</th>");
			}
		}

		sb.append("</tr>");
		return sb.toString();
	}

}
