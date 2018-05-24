package pl.jacekkulis.snowrental.security;

import java.io.IOException;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

@Component("authenticationFailureHandler")
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	@Autowired
	private LocaleResolver localeResolver;

	private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationFailureHandler.class);

	@Autowired
	private MessageSource messageSource;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		setDefaultFailureUrl("/signin.html?error=true");
		logger.info("onAuthenticationFailure");
		
		Locale locale = localeResolver.resolveLocale(request);

		super.onAuthenticationFailure(request, response, exception);
		
		logger.info("onAuthenticationFailure: exception: " + exception + ", message: " + exception.getMessage());

		String errorMessage = messageSource.getMessage("error.message.defaultMessage", null, locale);

		if (exception.getMessage().equalsIgnoreCase("User is disabled")) {
			errorMessage = messageSource.getMessage("error.message.disabled", null, locale);
		} else if (exception.getMessage().equalsIgnoreCase("User account has expired")) {
			errorMessage = messageSource.getMessage("error.message.expired", null, locale);
		} else if (exception.getMessage().equalsIgnoreCase("Bad credentials")) {
			errorMessage = messageSource.getMessage("error.message.userNotFound", null, locale);
		}

		request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, errorMessage);
	}
}
