package pl.jacekkulis.snowrental.captcha;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.Collection;
import org.springframework.stereotype.Service;

@Service
public class GoogleResponse implements Serializable {
    
	private static final long serialVersionUID = 3293870672973277528L;

	@JsonProperty("success")
    private boolean success;

    @JsonProperty("error-codes")
    private Collection<String> errorCodes;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Collection<String> getErrorCodes() {
        return errorCodes;
    }

    public void setErrorCodes(Collection<String> errorCodes) {
        this.errorCodes = errorCodes;
    }
}