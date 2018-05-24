package pl.jacekkulis.snowrental.servicesimpl;

import pl.jacekkulis.snowrental.dao.SnowboardRepository;
import pl.jacekkulis.snowrental.models.Snowboard;
import pl.jacekkulis.snowrental.services.ISnowboardService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SnowboardServiceImpl implements ISnowboardService {
	private static final Logger logger = LoggerFactory.getLogger(SnowboardServiceImpl.class);

	@Autowired
	private SnowboardRepository snowboardRepository;

	@Override
	@Transactional
	public List<Snowboard> findAll() {
		logger.info("findAll");
		return snowboardRepository.findAll();
	}

	@Override
	@Transactional
	public Snowboard findById(int id) {
		logger.info("findById");
		return snowboardRepository.findById(id);
	}

	@Override
	@Transactional
	public Snowboard findByName(String name) {
		logger.info("findByName");
		return snowboardRepository.findByName(name);
	}

	@Override
	@Transactional
	public Snowboard create(Snowboard snowboard) {
		logger.info("create");
		Snowboard newSnowboard = snowboard;
		return snowboardRepository.save(newSnowboard);
	}

	@Override
	@Transactional
	public Snowboard findByUniqueCode(String uniqueCode) {
		logger.info("findByUniqueCode");
		return snowboardRepository.findByUniqueCode(uniqueCode);
	}
}