package www.dream.com.framework.email;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

//sender를 사용하기 쉽게 하기 위해 도와주는 보조 클래스입니다.
public class MailHandler { 
	private JavaMailSender sender;
	private MimeMessage message;
	private MimeMessageHelper messageHelper;
	
	/* JavaMailSender sender는 MailSender 인터페이스를 상속받았으며, 
	 * MIME를 지원하여, 멀티파트 데이터를 처리할 수 있는 MimeMessage 를 사용할 수 있습니다.
	 * */

	public MailHandler(JavaMailSender sender) throws MessagingException {
		this.sender = sender;
		this.message = this.sender.createMimeMessage();
		this.messageHelper = new MimeMessageHelper(message, true, "UTF-8");
		// MimeMessageHelper 생성자의 두번째 인자 true는 멀티파트 메세지를 사용하겠다는 의미 입니다.

	}
	// 보내는 사람 이메일
	public void setFrom(String mail, String name) throws UnsupportedEncodingException, MessagingException {
		messageHelper.setFrom(mail, name);
	}
	// 받는 사람 이메일
	public void setTo(String mail) throws MessagingException {
		messageHelper.setTo(mail);
	}
	// 제목
	public void setSubject(String subject) throws MessagingException {
		messageHelper.setSubject(subject);
	}
	// 내용
	public void setText(String text) throws MessagingException {
		messageHelper.setText(text, true);
	}
	// 메일을 발송합니다.
	public void send() {
		try {
			sender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}