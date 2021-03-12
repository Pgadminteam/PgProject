package www.dream.com.framework.display;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;


/*
 * VO의 field에 whenuse=언제사용할 것인지, caption=어느이름으로 사용할것인지 를 지정해준다.
 * enum으로 whenuse기준을 만들고, boolean을 이용해 whenuse가 all이나 list로 사용됐을 때만 true값을 반환해준다=사용가능
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface Caption {
	public enum WhenUse {
		all, detail, list, none;
		
		public static boolean isTableTarget(WhenUse whenUse) {
			if (whenUse == all || whenUse == list)
				return true;
			return false;
		}
	};
	
	public WhenUse whenUse();
	public String caption();
	
}
