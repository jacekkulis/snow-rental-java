package pl.jacekkulis.snowrental.models;

/*
 *	It is container to hold Item and quantity of item in ShoppingCart (stored in session) 
 *  BasketItem => OrderDetails
 */
public class BasketItem {
	
	private Item item;
	private int quantity;

	public BasketItem() {
		this.quantity = 1;
	}

	public BasketItem(Item item, int quantity) {
		this.item = item;
		this.quantity = quantity;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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

		final BasketItem basketItem = (BasketItem) obj;
		if (!item.equals(basketItem.getItem())) 
			return false;
		
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("BasketItem [Item=").append(item).append(", quantity=").append(quantity).append("]");
		return builder.toString();
	}
}
