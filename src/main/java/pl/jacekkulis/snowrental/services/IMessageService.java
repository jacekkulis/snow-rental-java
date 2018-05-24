package pl.jacekkulis.snowrental.services;

import java.util.List;

import pl.jacekkulis.snowrental.models.Message;
import pl.jacekkulis.snowrental.models.User;

public interface IMessageService {
	public Message create(Message message);
	public Message update(Message message);
	
	public List<Message> findAll();
	public Message findById(int id);
	public Message findByUser(User user);
	public Message findByContent(String content);
}
