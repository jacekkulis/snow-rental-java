package pl.jacekkulis.snowrental.captcha;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

@Service("captchaService")
public class CaptchaService implements ICaptchaService {
	private final static Logger logger = LoggerFactory.getLogger(CaptchaService.class);

	@Autowired
	private CaptchaSettings captchaSettings;

	@Autowired
	private RestOperations restTemplate;
	
	@Autowired
	private HttpServletRequest request;

    @Override
    public boolean validate(String reCaptchaResponse) {
    	logger.info("validate");
    	logger.info("validateReCaptcha: site: " + captchaSettings.getSite());
		logger.info("validateReCaptcha: secret: " + captchaSettings.getSecret());
		logger.info("validateReCaptcha: remoteIp: " + request.getRemoteAddr());
		logger.info("validateReCaptcha: reCaptchaResponse: " + reCaptchaResponse);
		
        if (reCaptchaResponse == null || reCaptchaResponse.isEmpty()){
        	logger.info("isValid: null or is empty");
            return false;
        }

        GoogleResponse googleResponse;
        try {
            googleResponse = restTemplate
                    .postForEntity(captchaSettings.getUrl(), createBody(captchaSettings.getSecret(), request.getRemoteAddr(), reCaptchaResponse), GoogleResponse.class)
                    .getBody();
        	return googleResponse.isSuccess();
        } catch (RestClientException ex) {
        	logger.error("", ex);
			logger.error("", ex.getMessage());
        }

        return false;
    }

    private MultiValueMap<String, String> createBody(String secret, String remoteIp, String response) {

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("secret", secret);
        form.add("remoteip", remoteIp);
        form.add("response", response);

        return form;
    }

	@Override
	public String getReCaptchaSite() {
		return captchaSettings.getSite();
	}

	@Override
	public String getReCaptchaSecret() {
		return captchaSettings.getSecret();
	}
}
