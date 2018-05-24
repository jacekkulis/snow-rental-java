package pl.jacekkulis.snowrental.captcha;


public interface ICaptchaService {
	boolean validate(String reCaptchaResponse);
	String getReCaptchaSite();
	String getReCaptchaSecret();
}
