package pl.jacekkulis.snowrental.models;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

@Entity
public class Order {
	@Id
	@Column(unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private String orderName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId", foreignKey = @ForeignKey(name = "FK_USER_ID"), nullable = false )
	private User user;

	@Enumerated
	@Column(columnDefinition = "smallint")
	private OrderStatusCode statusCode;

	/* All item ordered with quantity etc. */
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<OrderDetails> orderDetailsList = new HashSet<OrderDetails>();

	@Temporal(TemporalType.DATE)
	private Date createdDate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public OrderStatusCode getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(OrderStatusCode statusCode) {
		this.statusCode = statusCode;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public Set<OrderDetails> getOrderDetailsList() {
		return orderDetailsList;
	}

	public void setOrderDetailsList(Set<OrderDetails> orderDetailsList) {
		this.orderDetailsList = orderDetailsList;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + (int) id;
		result = (prime * result) + ((orderName == null) ? 0 : orderName.hashCode());
		result = (prime * result) + ((user == null) ? 0 : user.hashCode());
		result = (prime * result) + ((statusCode == null) ? 0 : statusCode.hashCode());
		result = (prime * result) + ((createdDate == null) ? 0 : createdDate.hashCode());
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

		final Order order = (Order) obj;
		if (id != order.id)
			return false;
		if (!orderName.equals(order.orderName))
			return false;
		if (!user.equals(order.user))
			return false;
		if (!statusCode.equals(order.statusCode))
			return false;
		if (!createdDate.equals(order.createdDate))
			return false;

		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Order [id=").append(id).append(", orderName=").append(orderName).append(", statusCode=").append(statusCode).append(", createdDate=").append(createdDate).append("]");
		return builder.toString();
	}
}
