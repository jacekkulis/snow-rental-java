package pl.jacekkulis.snowrental.registration;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import pl.jacekkulis.snowrental.models.User;
import pl.jacekkulis.snowrental.services.IUserService;


@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

	private static String WEBSITE_ADDRESS = "http://localhost:8080";
	
	private static final Logger logger = LoggerFactory.getLogger(RegistrationListener.class);
	
	@Autowired
	private IUserService service;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private JavaMailSender mailSender;

	@Override
	public void onApplicationEvent(OnRegistrationCompleteEvent event) {
		this.confirmRegistration(event);
	}

	private void confirmRegistration(OnRegistrationCompleteEvent event) {
		logger.info("confirmRegistration");
		
		User user = event.getUser();
		logger.info("confirmRegistration: user: " + user);
		
		String token = UUID.randomUUID().toString();
		service.createVerificationToken(user, token);

		String recipientAddress = user.getEmail();
		String subject = "Activate registrated account";
		String confirmationUrl = event.getAppUrl() + "/registrationConfirm.html?token=" + token;
		String message = messageSource.getMessage("message.confirmRegistration", null, event.getLocale());

		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(recipientAddress);
		email.setSubject(subject);
		email.setText(message + " \n" + WEBSITE_ADDRESS + confirmationUrl);
		mailSender.send(email);
	}

}