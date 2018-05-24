package pl.jacekkulis.snowrental.dao;


import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.jacekkulis.snowrental.models.Image;


@Transactional
@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {
	
}
