package pl.jacekkulis.snowrental.services;

import java.util.List;
import pl.jacekkulis.snowrental.models.Ski;

public interface ISkiService {
	public List<Ski> findAll();

	public Ski findById(int id);

	public Ski findByName(String name);

	public Ski create(Ski ski);

	public Ski findByUniqueCode(String uniqueCode);
}
