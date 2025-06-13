package com.authyfier.authyfier.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

	private final MailSender mailSender;

	@Value("${spring.mail.properties.mail.smtp.from}")
	private String formEmail;

	public void sendWelcomeMail(String toEmail, String username) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(formEmail);
		message.setTo(toEmail);
		message.setSubject("Welcome to Authyfier");
		message.setText("Hello " + username + ",\n\nWelcome to Authyfier! We are excited to have you on board.\n\nBest regards,\nThe Authyfier Team");
		mailSender.send(message);
	}

}
