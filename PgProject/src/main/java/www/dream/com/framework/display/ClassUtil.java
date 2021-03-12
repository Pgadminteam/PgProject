package www.dream.com.framework.display;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
 * 리스트를 순회하면서 특정 값을 수정 할 때,
 * 반복문 안에서 크기, 인덱스가 변해 ConcurrentModificationException 예외가 생기는 오류를 Iterator를 사용하여 방지하였습니다.
 */
public class ClassUtil {

	public static List<Field> getListField(Class<?> targetClass, Class<Caption> annotation) {
		List<Field> listField = new ArrayList<>();
		//부수효과(Side Effect) 활용. 인자에 내용이 호출로 바뀝니다
		getAllField(targetClass, listField);
		//ConcurrentModificationException 발생을 거부합니다.
		Iterator<Field> iter = listField.iterator();
		while (iter.hasNext()) {
			Field field = iter.next();
			Caption annoCaption = field.getAnnotation(annotation);
			if (annoCaption == null)
				iter.remove();
		}
		return listField;
	}

	private static void getAllField(Class<?> targetClass, List<Field> listField) {
		for (Field field : targetClass.getDeclaredFields()) {
			listField.add(field);
		}
		if (targetClass.getSuperclass() != Object.class)
			getAllField(targetClass.getSuperclass(), listField);
	}

}
