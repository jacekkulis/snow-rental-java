package pl.jacekkulis.snowrental.servicesimpl;

import java.util.List;
import pl.jacekkulis.snowrental.dao.RoleRepository;
import pl.jacekkulis.snowrental.dao.UserRepository;
import pl.jacekkulis.snowrental.dao.VerificationTokenRepository;
import pl.jacekkulis.snowrental.models.User;
import pl.jacekkulis.snowrental.models.VerificationToken;
import pl.jacekkulis.snowrental.services.IUserService;

import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements IUserService {
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Resource
	private UserRepository userRepository;

	@Resource
	private RoleRepository roleRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private VerificationTokenRepository tokenRepository;

	@Override
	@Transactional
	public User create(User user) {
		User newUser = user;
		newUser.getRoles().add(roleRepository.findByName("ROLE_USER"));
		newUser.setPassword(hashPassword(user.getPassword()));
		logger.info("UserServiceImpl: create: " + user);
		return userRepository.save(newUser);
	}
	
	@Override
	@Transactional
	public User update(User user) {
		User updateUser = user;
		logger.info("UserServiceImpl: update: " + updateUser);
		return userRepository.save(updateUser);
	}

	@Override
	@Transactional
	public User findById(int id) {
		logger.info("UserServiceImpl: findById");
		return userRepository.findById(id);
	}

	@Override
	@Transactional
	public User delete(int id) {
		logger.info("UserServiceImpl: delete");
		User deletedUser = userRepository.findById(id);
		userRepository.delete(deletedUser);
		return deletedUser;
	}

	@Override
	@Transactional
	public List<User> findAll() {
		logger.info("UserServiceImpl: findAll");
		return userRepository.findAll(new Sort(Sort.Direction.ASC, "id"));
	}
	
	

	@Override
	@Transactional
	public User findByEmail(String email) {
		logger.info("UserServiceImpl: findByEmail: " + email);
		return userRepository.findByEmail(email);
	}

	@Override
	@Transactional
	public String hashPassword(String password) {
		logger.info("UserServiceImpl: hashPassword: " + password);
		return passwordEncoder.encode(password);
	}

	@Override
	@Transactional
	public void saveRegisteredUser(User user) {
		logger.info("UserServiceImpl: saveRegisteredUser:  " + user);
		userRepository.save(user);
	}

	@Override
	@Transactional
	public void createVerificationToken(User user, String token) {
		logger.info("UserServiceImpl: saveRegisteredUser: user: " + user);
		VerificationToken createdToken = new VerificationToken(token, user);
		logger.info("UserServiceImpl: saveRegisteredUser: createdToken: " + createdToken);
        tokenRepository.save(createdToken);
	}

	@Override
	@Transactional
	public VerificationToken getVerificationToken(String verificationToken) {
		logger.info("UserServiceImpl: getVerificationToken: " + verificationToken);
		return tokenRepository.findByToken(verificationToken);
	}
}