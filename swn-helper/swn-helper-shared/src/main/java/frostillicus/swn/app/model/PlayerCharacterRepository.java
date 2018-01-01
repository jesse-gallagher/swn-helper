package frostillicus.swn.app.model;

import java.util.List;

import org.darwino.jnosql.artemis.extension.DarwinoRepository;

public interface PlayerCharacterRepository extends DarwinoRepository<PlayerCharacter, String> {
	public List<PlayerCharacter> findAll();
}
