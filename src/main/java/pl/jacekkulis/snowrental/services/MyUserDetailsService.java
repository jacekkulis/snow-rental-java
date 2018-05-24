package pl.jacekkulis.snowrental.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.jacekkulis.snowrental.models.Role;

@Service("myUserDetailsService")
public class MyUserDetailsService implements UserDetailsService {

	private static final Logger logger = LoggerFactory.getLogger(MyUserDetailsService.class);
	
	private boolean accountNonExpired = true;
	private boolean credentialsNonExpired = true;
	private boolean accountNonLocked = true;

	@Autowired
	private IUserService iUserService;

	@Transactional(readOnly = true)
	@Override
	public UserDetails loadUserByUsername(final String login) throws UsernameNotFoundException {
		logger.info("loadUserByUsername");
		pl.jacekkulis.snowrental.models.User user = iUserService.findByEmail(login);

        if (user == null) {
        	logger.info("loadUserByUsername: UsernameNotFoundException: no user found with login: " + login);
            throw new UsernameNotFoundException("No user found with login: " + login);
        }

		List<GrantedAuthority> authorities = buildUserAuthority(user.getRoles());
		return buildUserForAuthentication(user, authorities);
	}

	// Converts service.User user to
	// org.springframework.security.core.userdetails.User
	private User buildUserForAuthentication(pl.jacekkulis.snowrental.models.User user,
			List<GrantedAuthority> authorities) {
		return new User(user.getEmail(), user.getPassword(), user.isEnabled(), accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
	}

	private List<GrantedAuthority> buildUserAuthority(Set<Role> roles) {

		Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();
		// Build user's authorities
		for (Role role : roles) {
			setAuths.add(new SimpleGrantedAuthority(role.getName()));
		}
		List<GrantedAuthority> Result = new ArrayList<GrantedAuthority>(setAuths);
		return Result;
	}

	public IUserService getUserService() {
		return iUserService;
	}

	public void setUserService(IUserService iUserService) {
		this.iUserService = iUserService;
	}
}
