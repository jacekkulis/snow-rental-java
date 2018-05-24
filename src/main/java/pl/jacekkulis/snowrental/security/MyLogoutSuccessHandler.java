package pl.jacekkulis.snowrental.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Component("myLogoutSuccessHandler")
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {

	private static final Logger logger = LoggerFactory.getLogger(MyLogoutSuccessHandler.class);
	
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        final HttpSession session = request.getSession();
        
        if (session != null) {
            session.removeAttribute("user");
    		session.removeAttribute("basket");
        }
        
        if (authentication != null && authentication.getDetails() != null) {
            try {
            	request.getSession().invalidate();
            	logger.info("onLogoutSuccess: User Successfully Logout");
                //you can add more codes here when the user successfully logs out,
                //such as updating the database for last active.
            } catch (Exception e) {
            	logger.error(e.getMessage());
            }
        }
        
        response.setStatus(HttpServletResponse.SC_OK);
        response.sendRedirect(request.getContextPath() + "/signin?logout=true");
    }
}
