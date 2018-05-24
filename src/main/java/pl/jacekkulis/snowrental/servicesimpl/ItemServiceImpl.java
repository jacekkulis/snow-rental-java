package pl.jacekkulis.snowrental.servicesimpl;

import pl.jacekkulis.snowrental.dao.ItemRepository;
import pl.jacekkulis.snowrental.models.Item;
import pl.jacekkulis.snowrental.services.IItemService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemServiceImpl implements IItemService {
	private static final Logger logger = LoggerFactory.getLogger(ItemServiceImpl.class);

	@Autowired
	private ItemRepository itemRepository;

	@Override
	@Transactional
	public List<Item> findAll() {
		logger.info("findAll");
		return itemRepository.findAll();
	}

	@Override
	@Transactional
	public Item findById(int id) {
		logger.info("findById");
		return itemRepository.findById(id);
	}

	@Override
	@Transactional
	public Item findByName(String name) {
		logger.info("findByName");
		return itemRepository.findByName(name);
	}

	@Override
	@Transactional
	public Item create(Item ski) {
		logger.info("create");
		Item newSki = ski;
		return itemRepository.save(newSki);
	}

	@Override
	@Transactional
	public Item findByUniqueCode(String uniqueCode) {
		logger.info("findByUniqueCode");
		return itemRepository.findByUniqueCode(uniqueCode);
	}
}