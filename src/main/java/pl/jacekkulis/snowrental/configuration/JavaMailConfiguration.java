package pl.jacekkulis.snowrental.configuration;

import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@PropertySource("classpath:email.properties")
public class JavaMailConfiguration {
	
	@Value("${mail.host}")
	private String host;
	@Value("${mail.port}")
	private String port;
	@Value("${mail.protocol}")
	private String protocol;
	@Value("${mail.username}")
	private String emailUsername;
	@Value("${mail.password}")
	private String emailPassword;

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean(name = "mailSender")
	public JavaMailSenderImpl javaMailSenderImpl() {
		final JavaMailSenderImpl mailSenderImpl = new JavaMailSenderImpl();
		mailSenderImpl.setHost(host);
		mailSenderImpl.setPort(Integer.parseInt(port));
		mailSenderImpl.setProtocol(protocol);
		mailSenderImpl.setUsername(emailUsername);
		mailSenderImpl.setPassword(emailPassword);
		final Properties javaMailProps = new Properties();
		javaMailProps.put("mail.smtp.auth", true);
		javaMailProps.put("mail.smtp.starttls.enable", true);
		
		mailSenderImpl.setJavaMailProperties(javaMailProps);
		return mailSenderImpl;
	}
}