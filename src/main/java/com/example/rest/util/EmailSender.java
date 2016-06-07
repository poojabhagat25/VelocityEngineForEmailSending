package com.example.rest.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.example.rest.dto.EmailDTO;
import com.example.rest.dto.UserDTO;

import static com.example.rest.constants.RestConstants.REGISTRATION_CONFIRMATION_EMAIL_PATH;
import static com.example.rest.constants.RestConstants.FORGOT_PASSWORD_EMAIL_PATH;

public class EmailSender {
	Logger logger = Logger.getLogger(EmailSender.class);

	//private MailSender mailSender;
	
    private JavaMailSender mailSender;
    
    @Autowired
    private VelocityEngine velocityEngine;	
	

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	
	public EmailDTO sendRegistrationConfirmationMail( final EmailDTO email , final UserDTO userDTO) throws Exception{
		try{
			MimeMessagePreparator preparator = new MimeMessagePreparator() {
		    //@SuppressWarnings({ "rawtypes" })
				public void prepare(MimeMessage mimeMessage) throws Exception {
		             MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
		             message.setTo(email.getTo());
		             message.setSubject("Registration Confirmation Mail");
		             message.setSentDate(new Date());
		             Map<String, Object> model = new HashMap<String, Object>();	             
		             model.put("newMessage", "Demo");
		             
		             
		             String text = VelocityEngineUtils.mergeTemplateIntoString(
				                velocityEngine , 
				                String.format(ResourceManager.getMessage(REGISTRATION_CONFIRMATION_EMAIL_PATH,
				                		null, REGISTRATION_CONFIRMATION_EMAIL_PATH, null)) , 
				                "UTF-8", model);

//		             text = text.replace("[verificationCode]" , userDTO.getVerificationCode());
		             text = text.replace("userName" , email.getUsername());
		             text = text.replace("passWord" , email.getPassword());
		             message.setText(text, true);
		          }
		       };
		       mailSender.send(preparator);			
		       return email;
		}catch(Exception ex){
			logger.error(ex.getStackTrace(), ex);
			 return email;
		}
		
		
	}
	
	
	public EmailDTO sendForgotPasswordMail(final EmailDTO email) throws Exception{
		try{
		  MimeMessagePreparator preparator = new MimeMessagePreparator() {
		        //@SuppressWarnings({ "rawtypes" })
				public void prepare(MimeMessage mimeMessage) throws Exception {

		             MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
		             message.setTo(email.getTo());
		             message.setSubject("Forgot Password Mail");
		             message.setSentDate(new Date());
		             Map<String, Object> model = new HashMap<String, Object>();	             
		             model.put("newMessage", "Demo");

		             
		             String text = VelocityEngineUtils.mergeTemplateIntoString(
				                velocityEngine , 
				                String.format(ResourceManager.getMessage(FORGOT_PASSWORD_EMAIL_PATH,
				                		null, FORGOT_PASSWORD_EMAIL_PATH, null)) , 
				                "UTF-8", model);
		             
		             text = text.replace("[userName]", email.getUsername());
		             text = text.replace("[emailId]", email.getTo());
		             text = text.replace("[password]", email.getPassword());
		             message.setText(text, true);
		          }
		       };
		       mailSender.send(preparator);			
		       return email;
		}catch(Exception ex){
			throw ex;
		}
	}
		
}