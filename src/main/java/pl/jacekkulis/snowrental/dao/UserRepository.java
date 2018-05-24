package pl.jacekkulis.snowrental.dao;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.jacekkulis.snowrental.models.User;

@Transactional
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	List<User> findByLastName(String lastName);
	User findById(int id);
	User findByEmail(String email);
}
