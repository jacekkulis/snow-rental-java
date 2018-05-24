package pl.jacekkulis.snowrental.services;

import java.util.List;
import pl.jacekkulis.snowrental.models.Snowboard;

public interface ISnowboardService {
	public List<Snowboard> findAll();

	public Snowboard findById(int id);

	public Snowboard findByName(String name);

	public Snowboard findByUniqueCode(String uniqueCode);

	Snowboard create(Snowboard snowboard);
}
