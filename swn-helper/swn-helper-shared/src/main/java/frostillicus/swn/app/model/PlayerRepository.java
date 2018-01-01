package frostillicus.swn.app.model;

import java.util.List;
import java.util.Optional;

import org.darwino.jnosql.artemis.extension.DarwinoRepository;

public interface PlayerRepository extends DarwinoRepository<Player, String> {
	public List<Player> findAll();
	
	public Optional<Player> findByName(String name);
}