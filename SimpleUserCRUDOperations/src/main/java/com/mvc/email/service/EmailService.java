package com.mvc.email.service;

public interface EmailService {

	void sendRegistrationMail(String email,String subject,String body);
	
	void sendLoginDetailsMail(String email,String subject,String body);
	
	void sendDeletionMail(String email,String subject,String body);
}//interface