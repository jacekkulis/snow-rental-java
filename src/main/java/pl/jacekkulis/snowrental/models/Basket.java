package pl.jacekkulis.snowrental.models;

import java.util.ArrayList;
import java.util.List;

/*
 *	Session container to hold items added to cart. After checkout it becomes an Order
 *  Basket => Order
 */
public class Basket {
    private int id;
    
    private List<BasketItem> itemsInBasket;

	public Basket() {
		this.itemsInBasket = new ArrayList<BasketItem>();
	}
	
	public Basket(List<BasketItem>  itemsInCart) {
		super();
		this.itemsInBasket = itemsInCart;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<BasketItem> getItemsInBasket() {
		return itemsInBasket;
	}

	public void setItemsInBasket(List<BasketItem> itemsInCart) {
		this.itemsInBasket = itemsInCart;
	}
}