package pl.jacekkulis.snowrental.dao;

import javax.transaction.Transactional;
import org.springframework.stereotype.Repository;
import pl.jacekkulis.snowrental.models.Snowboard;

@Transactional
@Repository
public interface SnowboardRepository extends ItemBaseRepository<Snowboard> {

}
