package pl.jacekkulis.snowrental.services;

import java.util.List;
import pl.jacekkulis.snowrental.models.Item;

public interface IItemService {
	public List<Item> findAll();

	public Item findById(int id);

	public Item findByName(String name);

	public Item create(Item ski);

	public Item findByUniqueCode(String uniqueCode);
}
