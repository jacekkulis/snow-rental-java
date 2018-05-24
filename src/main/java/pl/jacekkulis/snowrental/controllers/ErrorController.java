package pl.jacekkulis.snowrental.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.jacekkulis.snowrental.exceptions.ResourceNotFoundException;
import pl.jacekkulis.snowrental.models.MessageType;

@ControllerAdvice
public class ErrorController {

	private static final Logger logger = LoggerFactory.getLogger(ErrorController.class);

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String exception(final Exception exception, final Model model) {
		logger.error("Exception during execution of SpringSecurity application {}", exception);
		String errorMessage = (exception != null ? exception.getMessage() : "Unknown error");

		model.addAttribute("message", errorMessage);
		model.addAttribute("type", MessageType.danger);
		return "error/errorPage";
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String notFound(final ResourceNotFoundException resourceNotFoundException, final Model model) {
		logger.error("Exception during execution of SpringSecurity application {}", resourceNotFoundException);
		String errorMessage = (resourceNotFoundException != null ? resourceNotFoundException.getMessage()
				: "Unknown error");

		model.addAttribute("message", errorMessage);
		model.addAttribute("type", MessageType.danger);
		return "error/404";
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String accessDenied(final AccessDeniedException accessDeniedException) {
		logger.error("Exception during execution of SpringSecurity application {}", accessDeniedException);

		return "error/accessDenied";
	}
}
