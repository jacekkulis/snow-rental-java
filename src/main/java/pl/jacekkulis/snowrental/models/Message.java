package pl.jacekkulis.snowrental.models;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;


@Entity
public class Message {
	@Id
	@Column(unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private boolean isRead;

	@ManyToOne
	@JoinColumn(name = "userId", foreignKey = @ForeignKey(name = "userIdFK"))
	@JsonBackReference
	private User user;
	
	@Column(length=1000)
	private String content;
	
	@Enumerated
	@Column(columnDefinition = "smallint")
	private MessageType messageType;
	
	public Message() {
	
	}
	
	public Message(User user, String content, MessageType messageType) {
		this.user = user;
		this.content = content;
		this.messageType = messageType;
		this.isRead = false;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + (int) id;
		result = (prime * result) + ((user == null) ? 0 : user.hashCode());
		result = (prime * result) + ((content == null) ? 0 : content.hashCode());
		result = (prime * result) + ((messageType == null) ? 0 : messageType.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		final Message message = (Message) obj;
		if (id != message.id)
			return false;
		if (!user.equals(message.user))
			return false;
		if (!content.equals(message.content))
			return false;
		if (!messageType.equals(message.messageType))
			return false;

		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("PendingMessage [id=").append(id).append(", user=").append(user).append(", content=").append(content).append(", messageType=")
		.append(messageType).append("]");
		return builder.toString();
	}
}
