package www.dream.com.framework.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class BeanUtil implements ApplicationContextAware {
 
    private static ApplicationContext context;
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
 
    public static <T> T getBean(Class<T> beanClass) {
        return context.getBean(beanClass);
    }
}
/* 
 * ApplicationContext는 BeanFactory의 모든 기능을 포함하며 일반적으로 사용됨
 * Bean 인스턴스화/연결, 자동 BeanPostProcessor, BeanFactoryPostProcessor 등록, 편리한 MessageSource 액세스 (i18n 용), ApplicationEvent 게시
 * 위의 기능들을 다 수행한다. 트랜잭션 관리, 메시지 기반의 다국어 처리, AOP 처리등등 DI(Dependency Injection) 과 Ioc(Inverse of Conversion) 외에도 많은 부분을 지원
 * BeanFactory는 Bean 인스턴스화/연결 만 가능하다.
 * ApplicationContext는 Pre-loading을 하고 즉, 즉시 인스턴스를 만들고 BeanFactory는 lazy-loading을 하여 실제 요청 받는 시점에서 인스턴스를 만든다.
 * 정리하자면 두 인터페이스의 차이점은 인스턴스화 시점이 다르다.
 * 기본적인 구현함수로는 getBean("")을 사용한다.
 * 
 * @Autuwired,@Inject 등의 어노테이션으로 의존주입을 하기 위해서는 해당 객체가 빈으로 등록되어 있어야만 가능하다.
 * 빈으로 등록되지 않은 객체에 빈으로 등록된 객체를 의존주입해야 할 상황이 생길 때 사용할 수 있는 하나의 UtilClass 이다.
 * ApplicationContextAware를 구현한 BeanUtil 클래스를 하나 만들고 setApplicationContext() 메소드로 ApplicationContext를 주입받고 있는 상황이다.
 * 그리고 static으로 선언된 getBean 메소드를 이용하여 빈주입을 원하는 어딘가에서 BeanUtil.getBean()를 호출하여 빈을 주입받을 수 있다.
 * 
 * di를 할때 어노테이션 Autowired 주입받아 사용하는데 코모란을 이용해 만든 객체들은 빈으로 등록이 안돼서 Autowired 안됨
 * 그래서 beanutil을 생성해 getbean을 이용해 빈으로 등록하여 해시태그서비스를 사용함
 * 
 * 스프링에서 빈을 생성할땐 모든 빈이 싱글톤으로 생성됨 어디에서든 빈을 주입받으면 동일한 빈을 주입받도록 보장됨
 * 필요에 따라 빈주입마다 새로운 빈이 생성되야 할 경우가 있을 때 그때 beanutil class를 만들어서 사용
 * 해시태그는 객체가 계속 변하기때문에 더욱 beanutil class를 사용해야함
 * 
 * 서비스에서 메소드를 호출할때  클래스를 빈으로 등록하지않고 서비스에서 nullpointexception 됨
 */
