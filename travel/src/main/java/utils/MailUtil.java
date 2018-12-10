package utils;


import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

/**
 * 发送邮件工具类
 */
public final class MailUtil {
	private MailUtil(){}
	/**
	 * 发送邮件
	 * 参数一:发送邮件给谁
	 * 参数二:发送邮件的内容
	 */
	public static void sendMail(String toEmail, String emailMsg) throws Exception {

		//1_创建Java程序与163邮件服务器的连接对象
		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp");   // 使用的协议（JavaMail规范要求）
		props.setProperty("mail.smtp.host", "smtp.163.com");   // 发件人的邮箱的 SMTP 服务器地址
		props.setProperty("mail.smtp.auth", "true");            // 需要请求认证

		Authenticator auth = new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("itcast_javaee@163.com", "dongzi407900432");
			}
		};
		Session session = Session.getInstance(props, auth);
		//2_创建一封邮件
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress("itcast_javaee@163.com"));
		message.setRecipient(RecipientType.TO, new InternetAddress(toEmail));
		message.setSubject("用户");
		message.setContent(emailMsg, "text/html;charset=UTF-8");
		//3_发送邮件
		Transport.send(message);
	}
	/**
	 * 测试类
	 */
	public static void main(String[] args) throws Exception{
		String toEmail = "1063023601@qq.com";
		String emailMsg = "<a href='http://localhost:8080/travel_demo/user?action=active&code=222'>用户</a>";
		sendMail(toEmail,emailMsg);
		System.out.println("发送成功。。。");
	}
}