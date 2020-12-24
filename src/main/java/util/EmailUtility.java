package util;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailUtility
{
	private static String RECIPIENT1 = "amitoli2012@gmail.com";
	//private static String RECIPIENT2 = "kamalmedhi68@gmail.com";
	private static String RECIPIENT3 = "kamalmedhi68@gmail.com";
	private static String[] to = { RECIPIENT1 };
/*	public static void main(String[] args) {

		final String FROM_USER_NAME = "nsebseupdates@gmail.com";
		final String PASSWORD = "zihwycnmzbxwlilu";
		final String TO = "zihwycnmzbxwlilu";

		Properties props = new Properties();
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(FROM_USER_NAME, PASSWORD);
					}
				});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("nsebseupdates@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse("amitoli2012@gmail.com"));
			message.setSubject("Promoter Details");
			//message.setText("Dear Mail Crawler," + "\n\n No spam to my email, please!");
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			Multipart multipart = new MimeMultipart();
			String file = "C:\\Projects\\Work\\mkt\\output.csv";
			String fileName = "Promo.csv";
			DataSource source = new FileDataSource(file);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(fileName);
			multipart.addBodyPart(messageBodyPart);

			message.setContent(multipart);

			System.out.println("Sending");
			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}*/
	public void send(String subject, String filepath, String filename){
		final String FROM_USER_NAME = "nsebseupdates@gmail.com";
		final String PASSWORD = "zihwycnmzbxwlilu";

		Properties props = new Properties();
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(FROM_USER_NAME, PASSWORD);
					}
				});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("nsebseupdates@gmail.com"));
			/*message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse("amitoli2012@gmail.com"));*/
			InternetAddress[] toAddress = new InternetAddress[to.length];

			// To get the array of addresses
			for( int i = 0; i < to.length; i++ ) {
				toAddress[i] = new InternetAddress(to[i]);
			}

			for( int i = 0; i < toAddress.length; i++) {
				message.addRecipient(Message.RecipientType.BCC, toAddress[i]);
			}
			message.setSubject(subject);
			//message.setText("Dear Mail Crawler," + "\n\n No spam to my email, please!");
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			Multipart multipart = new MimeMultipart();
			String file = filepath;
			String fileName = filename;
			DataSource source = new FileDataSource(file);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(fileName);
			multipart.addBodyPart(messageBodyPart);

			message.setContent(multipart);

			System.out.println("Sending");
			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}