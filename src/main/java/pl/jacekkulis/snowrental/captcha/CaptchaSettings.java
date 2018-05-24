package pl.jacekkulis.snowrental.captcha;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.properties")
public class CaptchaSettings {

	@Value("${google.recaptcha.key.url}")
	private String url;
	@Value("${google.recaptcha.key.site}")
	private String site;
	@Value("${google.recaptcha.key.secret}")
	private String secret;

	public CaptchaSettings() {
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
