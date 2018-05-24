package pl.jacekkulis.snowrental.dao;


import java.awt.TrayIcon.MessageType;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.jacekkulis.snowrental.models.Message;
import pl.jacekkulis.snowrental.models.User;

@Transactional
@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
	Message findById(int id);
	Message findByUser(User user);
	Message findByContent(String content);
	Message findByMessageType(MessageType messageType);
}
