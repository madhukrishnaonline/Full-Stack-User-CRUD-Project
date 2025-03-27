package com.mvc.email.service;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.mvc.binding.UserDataBindResponse;
import com.mvc.service.UserInfoService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EmailServiceImpl implements EmailService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private UserInfoService userInfoService;

	@Override
	public void sendRegistrationMail(String email, String subject, String body) {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		log.info("MimeMessage Object Created");
		try {
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
			log.info("MimeMessageHelper Object Created");
			helper.setTo(email);
			helper.setSubject(subject);
			helper.setText(body, true);

			/*UserDataBindResponse userData = userInfoService.findByUserMail(email);
			String profilePath = userData.getProfilePath();
			File file = new File(profilePath);
			helper.addAttachment(profilePath, file);*/
			 helper.addInline("registrationImage",findResourceByUserMail(email));
		} // if
		catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} // catch
		mailSender.send(mimeMessage);
	}

	@Override
	public void sendLoginDetailsMail(String email, String subject, String body) {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
			helper.setTo(email);
			helper.setSubject(subject);
			helper.setText(body, true);

			/*UserDataBindResponse userData = userInfoService.findByUserMail(email);
			String profilePath = userData.getProfilePath();
			File file = new File(profilePath);
			helper.addAttachment(profilePath, file);*/
			helper.addInline("registrationImage",findResourceByUserMail(email));
		} // if
		catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} // catch
		mailSender.send(mimeMessage);
	}

	@Override
	public void sendDeletionMail(String email, String subject, String body) {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		try {
			helper.setTo(email);
			helper.setSubject(subject);
			helper.setText(body, true);
		} // if
		catch (MessagingException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} // catch
		mailSender.send(message);
	}

	private Resource findResourceByUserMail(String email) throws Exception {
		UserDataBindResponse userDetails = userInfoService.findByUserMail(email);
		String imagePath = userDetails.getProfilePath();
	
		String fileName = imagePath.substring(20);
	
		String uploadDir = imagePath.substring(0, 20);
	
		Path filePath = Paths.get(uploadDir).resolve(fileName).normalize();
		return new UrlResource(filePath.toUri());
	}
}// class