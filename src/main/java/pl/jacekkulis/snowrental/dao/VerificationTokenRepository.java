package pl.jacekkulis.snowrental.dao;


import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.jacekkulis.snowrental.models.User;
import pl.jacekkulis.snowrental.models.VerificationToken;

@Transactional
@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Integer> {
	VerificationToken findByToken(String token);
	VerificationToken findByUser(User user);
}
