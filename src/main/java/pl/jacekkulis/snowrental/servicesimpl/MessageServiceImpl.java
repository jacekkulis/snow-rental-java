package pl.jacekkulis.snowrental.servicesimpl;

import pl.jacekkulis.snowrental.dao.MessageRepository;
import pl.jacekkulis.snowrental.exceptions.MessageNotFoundException;
import pl.jacekkulis.snowrental.models.Message;
import pl.jacekkulis.snowrental.models.User;
import pl.jacekkulis.snowrental.services.IMessageService;

import java.util.List;

import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MessageServiceImpl implements IMessageService {
	private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

	@Resource
	private MessageRepository messageRepository;

	@Override
	@Transactional
	public Message create(Message message) {
		logger.info("create");
		Message newMessage = message;
		//logger.info("UserServiceImpl: create: " + user);
		return messageRepository.save(newMessage);
	}
	
	@Override
	@Transactional
	public Message update(Message message) {
		logger.info("update");
		Message foundMessage = messageRepository.findById(message.getId());
		
		if (foundMessage == null) {
			logger.info("MessageNotFoundException(Message is not found)");
			throw new MessageNotFoundException("Message is not found.");
		}
		
		foundMessage = message;
		return messageRepository.save(foundMessage);
	}

	@Override
	public List<Message> findAll() {
		logger.info("findAll");
		return messageRepository.findAll();
	}

	@Override
	public Message findById(int id) {
		logger.info("findById");
		return messageRepository.findById(id);
	}

	@Override
	public Message findByUser(User user) {
		logger.info("findByUser");
		return messageRepository.findByUser(user);
	}

	@Override
	public Message findByContent(String content) {
		logger.info("findByContent");
		return messageRepository.findByContent(content);
	}
}