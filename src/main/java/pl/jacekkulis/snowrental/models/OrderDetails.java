package pl.jacekkulis.snowrental.models;

import javax.persistence.*;

@Entity
public class OrderDetails {
	@Id
	@Column(unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private int quantity;

	@OneToOne(targetEntity = Item.class, cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	private Item item;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "orderId")
	private Order order;

	public OrderDetails() {
		super();
	}
	
	public OrderDetails(int quantity, Item item, Order order) {
		super();
		this.quantity = quantity;
		this.item = item;
		this.order = order;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + (int) id;
		result = (prime * result) + (int) quantity;
		result = (prime * result) + ((order == null) ? 0 : order.hashCode());
		result = (prime * result) + ((item == null) ? 0 : item.hashCode());
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

		final OrderDetails orderDetails = (OrderDetails) obj;
		if (id != orderDetails.id)
			return false;
		if (quantity != orderDetails.quantity)
			return false;
		if (!order.equals(orderDetails.order))
			return false;
		if (!item.equals(orderDetails.item))
			return false;

		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("OrderDetails [id=").append(id).append(", quantity=").append(quantity).append(", order=").append(", item=").append(item)
				.append(order).append("]");
		return builder.toString();
	}
}
