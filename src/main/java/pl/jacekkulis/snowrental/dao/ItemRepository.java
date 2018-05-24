package pl.jacekkulis.snowrental.dao;

import javax.transaction.Transactional;
import org.springframework.stereotype.Repository;
import pl.jacekkulis.snowrental.models.Item;

@Transactional
@Repository
public interface ItemRepository extends ItemBaseRepository<Item> {
	
}
