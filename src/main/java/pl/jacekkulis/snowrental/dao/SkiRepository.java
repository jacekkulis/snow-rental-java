package pl.jacekkulis.snowrental.dao;

import javax.transaction.Transactional;
import org.springframework.stereotype.Repository;
import pl.jacekkulis.snowrental.models.Ski;

@Transactional
@Repository
public interface SkiRepository extends ItemBaseRepository<Ski> {
	
}
