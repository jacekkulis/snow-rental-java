package pl.jacekkulis.snowrental.servicesimpl;

import pl.jacekkulis.snowrental.dao.SkiRepository;
import pl.jacekkulis.snowrental.models.Ski;
import pl.jacekkulis.snowrental.services.ISkiService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SkiServiceImpl implements ISkiService {
	private static final Logger logger = LoggerFactory.getLogger(SkiServiceImpl.class);

	@Autowired
	private SkiRepository skiRepository;

	@Override
	@Transactional
	public List<Ski> findAll() {
		logger.info("listSkis");
		return skiRepository.findAll();
	}

	@Override
	@Transactional
	public Ski findById(int skiId) {
		logger.info("findById");
		return skiRepository.findById(skiId);
	}

	@Override
	@Transactional
	public Ski findByName(String name) {
		logger.info("findByName");
		return skiRepository.findByName(name);
	}

	@Override
	public Ski create(Ski ski) {
		logger.info("create");
		Ski newSki = ski;
		return skiRepository.save(newSki);
	}

	@Override
	public Ski findByUniqueCode(String uniqueCode) {
		logger.info("findByUniqueCode");
		return skiRepository.findByUniqueCode(uniqueCode);
	}
}