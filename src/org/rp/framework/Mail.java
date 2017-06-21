package org.rp.framework;

import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.rp.baseuser.GzBaseUser;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

public class Mail 
{
	private static Logger log = Logger.getLogger(Mail.class);
	
	private JavaMailSender mailSender;
	private String mailCcNotifications;
	private String mailSendFilter;
	private String mailDisabled;
	private String mailFrom;
	
	
	public String getMailFrom() {
		return mailFrom;
	}

	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	public static String disclaimer()
	{
		return "IMPORTANT: This e-mail (including any attachment hereto) is intended solely "
			 + "for the addressee and is confidential and privileged. If you are not an "
			 + "intended recipient or you have received this email in error, you are "
			 + "to immediately notify the sender by a reply email and to delete the "
			 + "transmission including all attachment. In such instances you are further "
			 + "prohibited from reproducing, disclosing, distributing or taking any "
			 + "action in reliance on it. Please be cautioned that URBAN PARADIGM SDN BHD "
			 + "will not be responsible for any viruses or other interfering or damaging "
			 + "elements which may be contained in this e-mail (including any attachments hereto).";
	}
	
	private void doSendMail(String dear,String content, List<String> attactments,SimpleMailMessage simpleMailMessage) {

		simpleMailMessage.setFrom(mailFrom);
		MimeMessage message = mailSender.createMimeMessage();
		
		log.info("creating message");
		try{
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setFrom(simpleMailMessage.getFrom());
			helper.setTo(simpleMailMessage.getTo());
			helper.setSubject(simpleMailMessage.getSubject());
			
	//		content += "\n\n" + Mail.disclaimer();
			helper.setText(content);		
			
			for (String cc : simpleMailMessage.getCc())
			{
				log.info("adding cc: " + cc);
				helper.addCc(cc);
			}
			
			for (String attach : attactments)
			{
				FileSystemResource file = new FileSystemResource(attach);
				helper.addAttachment(file.getFilename(), file);
			}
			log.info("sending message to : " + simpleMailMessage.getTo()[0] + " from : " + simpleMailMessage.getFrom());
			mailSender.send(message);
			log.info("sended message");
			
		}catch (Exception e) {
			e.printStackTrace();
			log.error("Sending mail failed : " + e.getMessage());
		}
	}
	
	private void sendMail(final String dear,final String content, final List<String> attactments,final SimpleMailMessage simpleMailMessage)
	{
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				doSendMail(dear,content,attactments,simpleMailMessage);
			}
		}).start();
	}
	
	public String getMailCcNotifications() {
		return mailCcNotifications;
	}

	public void setMailCcNotifications(String mailCcNotifications) {
		this.mailCcNotifications = mailCcNotifications;
	}
	
	public void sendMail(GzBaseUser user, String subject, String msg, List<String> attactments) throws GzMailException 
	{
		if (mailDisabled.equals("true"))
		{
			log.info("Message to : " + user.getEmail() + " not sent - mail disabled..");
			return;
		}
		
		if (mailSendFilter!=null && !mailSendFilter.isEmpty())
		{
			String[] filters = mailSendFilter.split(";");
			for (String filter : filters)
			{
				if (user.getEmail().equals(filter) || user.getEmail().endsWith(filter))
				{
					log.info("Message to : " + user.getEmail() + " filtered..");
					sendFilteredEmail(user,subject,msg,attactments);
					return;
				}
			}
		}
		
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setCc(getMailCcNotifications().split(";"));
		simpleMailMessage.setTo(user.getEmail());
		simpleMailMessage.setSubject(subject);
		sendMail(user.getContact(),msg,attactments,simpleMailMessage);
	}

	public void sendFilteredEmail(GzBaseUser user, String subject, String msg, List<String> attactments) throws GzMailException 
	{
		String ccs[] = getMailCcNotifications().split(";");
		msg = "Message to : " + user.getEmail() + " was Filtered:\n" + msg;
		for (String cc : ccs)
		{
			log.info("Sending Filtered Message to : " + user.getEmail() + " to cc: " + cc);
			SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
			simpleMailMessage.setCc(new String[0]);
			simpleMailMessage.setTo(cc);
			simpleMailMessage.setSubject(subject);
			sendMail(user.getContact(),msg,attactments,simpleMailMessage);
		}
	}
	
	public String getMailSendFilter() {
		return mailSendFilter;
	}

	public void setMailSendFilter(String mailSendFilter) {
		this.mailSendFilter = mailSendFilter;
	}
	
	public String getMailDisabled() {
		return mailDisabled;
	}

	public void setMailDisabled(String mailDisabled) {
		this.mailDisabled = mailDisabled;
	}
	
	public void notifyRegistration(GzBaseUser baseUser,String imagePath, String password) throws Exception {
		
		String subject = "RedPacket Gaming Registration Notification";
		String msg = "Hi " + baseUser.getContact() + "\nYou have successfully been registered with RedPacket Gaming as a " + baseUser.getRole().getDesc() + ".\n"
				+"Your password has been generated as:\n" +
				password + "\n - please logon with your email and change at your convenience.\nKind regards - RedPacket Gaming Support Team.";
		
		List<String> attactments = new  ArrayList<String>();
		if (imagePath != null)
			attactments.add(imagePath);
		
		try
		{
			log.info("#### sending email to " + baseUser.getEmail() + "#######");
			log.info("subject : " + subject); 
			log.info("message : " + msg);
			log.info("#####################################################");
			
			sendMail(baseUser, subject, msg, attactments);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.error("Couldn't send message : " + e.getMessage());
			throw e;
		}
	}

	public void notifyPasswordReset(GzBaseUser baseUser,String imagePath, String password) throws Exception {
		
		String subject = "RedPacket Gaming Password Reset";
		String msg = "Hi " + baseUser.getContact() + "\nYour password has been reset to\n" +
				password + "\n - please logon with your email and change at your convenience.\nKind regards - RedPacket Gaming Support Team.";
		
		List<String> attactments = new  ArrayList<String>();
		if (imagePath != null)
			attactments.add(imagePath);
		
		try
		{
			log.info("#### sending email to " + baseUser.getEmail() + "#######");
			log.info("subject : " + subject); 
			log.info("message : " + msg);
			log.info("#####################################################");
			
			sendMail(baseUser, subject, msg, attactments);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.error("Couldn't send message : " + e.getMessage());
			throw e;
		}
	}
	
	public void notifyEmailVerification(GzBaseUser baseUser, String domainTarget,String imagePath) throws Exception {
		String subject = "Please Verify Your Registration";
		
		// @RequestMapping(value = "/verify", params="code", method = RequestMethod.GET)
		String link = "http://" + domainTarget+"/redpacket/rp/logon/verify?code&id=" + baseUser.getId().toString();
		
		String msg = "Hi " + baseUser.getContact() + "\nYour RedPacket Gaming Registration is set to your email : " + baseUser.getEmail() 
				+ ".\nPlease click on the link below to activate your registration.\n\n"
				+ link
				+"\n\nKind regards - RedPacket Gaming Support Team.";
		
		List<String> attactments = new  ArrayList<String>();
		attactments.add(imagePath);
		
		try
		{
			log.info("#### sending email to " + baseUser.getEmail() + "#######");
			log.info("subject : " + subject); 
			log.info("message : " + msg);
			log.info("#####################################################");
			
			sendMail(baseUser, subject, msg, attactments);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.error("Couldn't send message : " + e.getMessage());
			throw e;
		}
	}
/*
	public static void main(String[] args)
	{
		try
		{
			@SuppressWarnings("resource")
			ApplicationContext context = 
			new ClassPathXmlApplicationContext("spring-mail.xml");

			Mail mm = (Mail) context.getBean("mail");
			mm.simpleMailMessage.setCc(mm.getMailCcNotifications().split(";"));
			
			mm.sendMail("Yong Mook Kim", "This is text content",new ArrayList<String>());

			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
*/

	
	
}