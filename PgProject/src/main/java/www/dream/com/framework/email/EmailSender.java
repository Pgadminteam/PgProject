package www.dream.com.framework.email;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;

import www.dream.com.framework.email.model.Email;

public class EmailSender {
	/* 
	 * JavaMailSender 객체를 통해 실제로 이메일을 보내주는 역할을 하는 EmailSender클래스 입니다. 
	 * root-context의 설정에서 빈으로 등록된 mailSender 객체는 
	 * PartyService 클래스에서 인젝션하여 사용됩니다.
	 * */
	@Autowired
    protected JavaMailSender mailSender;
	
    public void SendEmail(Email email) throws Exception {
         
        MimeMessage msg = mailSender.createMimeMessage();
        try {
            msg.setSubject(email.getSubject());
            msg.setText(email.getContent());
            //받는사람 : 가입되어 있는 유저
            msg.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(email.getReciver()));            
        }catch(MessagingException e) {
            System.out.println("MessagingException");
            e.printStackTrace();
        }
        try {
        	// 실제 메일 발송 부분
            mailSender.send(msg);
        }catch(MailException e) {
        	// MailException은 RuntimeException이므로 예외 처리가 필요한 경우에 처리
            System.out.println("MailException발생");
            e.printStackTrace();
        }
    }
}
