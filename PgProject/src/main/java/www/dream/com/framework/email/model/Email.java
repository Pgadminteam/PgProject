package www.dream.com.framework.email.model;

public class Email {
	/* 이메일 정보를 담기 위한 Email클래스 입니다. */
	//subject : 제목 , content : 내용, reciver: 수신자의 메일주소
	private String reciver;
    private String subject;
    private String content;
     
    public String getReciver() {
        return reciver;
    }
    public void setReciver(String reciver) {
        this.reciver = reciver;
    }

    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
     
}
