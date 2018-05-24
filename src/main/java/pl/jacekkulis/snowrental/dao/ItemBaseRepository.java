package pl.jacekkulis.snowrental.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import pl.jacekkulis.snowrental.models.Item;

@NoRepositoryBean
public interface ItemBaseRepository<T extends Item> extends JpaRepository<T, Integer> {
	public List<T> findAll();
	
	public T findById(int id);
	
	public T findByName(String name);
	
	public T findByUniqueCode(String uniqueCode);
}
