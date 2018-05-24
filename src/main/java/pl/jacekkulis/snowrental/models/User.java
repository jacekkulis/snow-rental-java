package pl.jacekkulis.snowrental.models;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Email;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class User {
	@Id
	@Column(unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@NotNull
	@Email
	@Size(min = 7, max = 30)
	private String email;

	@NotNull
	private String password;

	@Transient
	private String repeatPassword;

	private String firstName;
	private String lastName;
	private boolean enabled;

	@Transient
	private String reCaptchaResponse;

	@ManyToMany(fetch = FetchType.EAGER)
	private Set<Role> roles = new HashSet<Role>(0);
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private Set<Order> orders;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JsonManagedReference
	private Set<Message> messages;
	
	@OneToOne(mappedBy="user", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
    private VerificationToken verificationToken; 


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRepeatPassword() {
		return repeatPassword;
	}

	public void setRepeatPassword(String repeatPassword) {
		this.repeatPassword = repeatPassword;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getReCaptchaResponse() {
		return reCaptchaResponse;
	}

	public void setReCaptchaResponse(String reCaptchaResponse) {
		this.reCaptchaResponse = reCaptchaResponse;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Set<Order> getOrders() {
		return orders;
	}

	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}

	public Set<Message> getMessages() {
		return messages;
	}

	public void setMessages(Set<Message> messages) {
		this.messages = messages;
	}
	
	public void addRole(Role role) {
		this.roles.add(role);
	}
	
	public void removeRole(Role role) {
		this.roles.remove(role);
	}
	
	public void addRoles(List<Role> roles) {
		for(Role role : roles) {
			this.roles.add(role);
		}
	}


	public VerificationToken getVerificationToken() {
		return verificationToken;
	}

	public void setVerificationToken(VerificationToken verificationToken) {
		this.verificationToken = verificationToken;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + (int) id;
		result = (prime * result) + ((email == null) ? 0 : email.hashCode());
		result = (prime * result) + ((firstName == null) ? 0 : firstName.hashCode());
		result = (prime * result) + ((lastName == null) ? 0 : lastName.hashCode());
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

		final User user = (User) obj;
		if (id != user.id)
			return false;
		if (!email.equals(user.email))
			return false;
		if (!firstName.equals(user.firstName))
			return false;
		if (!lastName.equals(user.lastName))
			return false;
		if (!String.valueOf(enabled).equals(String.valueOf(user.enabled)))
			return false;

		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("User [id=").append(id).append(", firstName=").append(firstName).append(", lastName=")
				.append(lastName).append(", email=").append(email).append(", password=").append(password)
				.append(", enabled=").append(enabled).append(", roles=").append(roles).append("]");
		return builder.toString();
	}
}
