package pl.jacekkulis.snowrental.services;

import java.util.List;
import pl.jacekkulis.snowrental.models.User;
import pl.jacekkulis.snowrental.models.VerificationToken;

public interface IUserService {
	public User create(User user);
	public User delete(int id);
	public User update(User user);
	public List<User> findAll();
	public User findById(int id);
	public User findByEmail(String email);
	public String hashPassword(String password);
	
	public void saveRegisteredUser(User user);
	public void createVerificationToken(User user, String token);
	public VerificationToken getVerificationToken(String VerificationToken);
}
