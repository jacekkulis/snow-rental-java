package pl.jacekkulis.snowrental.configuration;

import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Resource(name = "myUserDetailsService")
	private UserDetailsService userDetailsService;

	@Autowired
	private AuthenticationFailureHandler authenticationFailureHandler;
	
	@Autowired
	private LogoutSuccessHandler myLogoutSuccessHandler;

	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	
	@Autowired
	public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(this.passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		CharacterEncodingFilter filter = new CharacterEncodingFilter();
		filter.setEncoding("UTF-8");
		filter.setForceEncoding(true);
		http.addFilterBefore(filter, CsrfFilter.class);

		http.authorizeRequests()
				.antMatchers("/signin", "/register", "/registrationConfirm", "/confirmationProblem", "/accountActivated", "/403").permitAll()
				.antMatchers("/", "/basket", "/userReadMessage", "/orders", "/ski", "/snowboard", 
						"/itemImage", "/basket", "/checkout").access("hasRole('ROLE_USER')")
				.antMatchers("/userOrders", "/order/changeStatus").access("hasRole('ROLE_SERVICEMAN')")
				.and()
					.formLogin()
					.loginPage("/signin").permitAll()
					.usernameParameter("email").passwordParameter("password")
					.defaultSuccessUrl("/")
					.failureUrl("/signin")
					.failureHandler(authenticationFailureHandler)
				.and()
					.exceptionHandling().accessDeniedPage("/403")
				.and()
				 	.logout()
				 	.logoutUrl("/userLogout")
	                .logoutSuccessHandler(myLogoutSuccessHandler)
	                .permitAll();
	}
	
	@Bean
	public SpringSecurityDialect springSecurityDialect() {
	    return new SpringSecurityDialect();
	}
}