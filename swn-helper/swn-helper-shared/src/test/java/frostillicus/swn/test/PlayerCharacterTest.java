package frostillicus.swn.test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;

import org.jnosql.artemis.Database;
import org.jnosql.artemis.DatabaseType;
import org.junit.Test;
import org.junit.runner.RunWith;

import frostillicus.swn.app.model.Planet;
import frostillicus.swn.app.model.PlanetRepository;
import frostillicus.swn.app.model.Player;
import frostillicus.swn.app.model.PlayerCharacter;
import frostillicus.swn.app.model.PlayerCharacterRepository;
import frostillicus.swn.app.model.PlayerRepository;
import frostillicus.swn.test.runner.WeldJUnit4Runner;

@RunWith(WeldJUnit4Runner.class)
@SuppressWarnings("nls")
public class PlayerCharacterTest extends AbstractDarwinoModelAppTest {
	
	@Inject
	@Database(DatabaseType.DOCUMENT)
	private PlayerRepository playerRepository;
	
	@Inject
	@Database(DatabaseType.DOCUMENT)
	private PlanetRepository planetRepository;
	
	@Inject
	@Database(DatabaseType.DOCUMENT)
	private PlayerCharacterRepository playerCharacterRepository;

	@Test
	public void testCreatePlayerCharacter() {
		try {
			String playerId = UUID.randomUUID().toString();
			String pcId = UUID.randomUUID().toString();
			String homeworldId = UUID.randomUUID().toString();

			{
				Player player = new Player();
				player.setId(playerId);
				player.setName("Foo Fooson");
				playerRepository.save(player);
	
				Planet planet = new Planet();
				planet.setId(homeworldId);
				planet.setName("Albion");
				planetRepository.save(planet);
	
				PlayerCharacter pc = new PlayerCharacter();
				pc.setId(pcId);
				pc.setPlayerId(playerId);
				pc.setName("Fenton Courtenay Tenison");
				pc.setClassName("Psychic");
				pc.setLevel(1);
				pc.setExperience(1300);
				pc.setHomeworldId(homeworldId);
	
				PlayerCharacter.Stats stats = new PlayerCharacter.Stats();
				stats.setStrength(12);
				stats.setIntelligence(10);
				stats.setDexterity(6);
				stats.setWisdom(14);
				stats.setConstitution(13);
				stats.setCharisma(7);
				pc.setStats(stats);
				
				pc.setAttackBonus(0);
				PlayerCharacter.Saves saves = new PlayerCharacter.Saves();
				saves.setPhysicalEffect(13);
				saves.setMentalEffect(12);
				saves.setEvasion(15);
				saves.setTechnology(16);
				saves.setLuck(14);
				
				List<PlayerCharacter.Skill> skills = new ArrayList<>();
				{
					skills.add(new PlayerCharacter.Skill("Combat/Primitive", 0));
					skills.add(new PlayerCharacter.Skill("Culture/World", 0));
					skills.add(new PlayerCharacter.Skill("Leadership", 0));
					skills.add(new PlayerCharacter.Skill("Persuasion", 1));
					skills.add(new PlayerCharacter.Skill("Combat/Psitech", 0));
					skills.add(new PlayerCharacter.Skill("Tech/Medical", 0));
					skills.add(new PlayerCharacter.Skill("Tech/Psitech", 0));
				}
				pc.setSkills(skills);
	
				playerCharacterRepository.save(pc);
			}
			
			Optional<PlayerCharacter> pcOptional = playerCharacterRepository.findById(pcId);
			assertTrue("Should have pc", pcOptional.isPresent());
			System.out.println("PC found: " + pcOptional);
			
			PlayerCharacter pc = pcOptional.get();
			
			Optional<Player> playerOptional = pc.getPlayer();
			assertTrue("Should have player", playerOptional.isPresent());
			System.out.println("Player found: " + playerOptional);
			
			Optional<Planet> homeworldOptional = pc.getHomeworld();
			assertTrue("Should have homeworld", homeworldOptional.isPresent());
			System.out.println("Homeworld found: " + homeworldOptional);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
