package frostillicus.swn.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;

import org.jnosql.artemis.Database;
import org.jnosql.artemis.DatabaseType;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.darwino.commons.util.StringUtil;

import frostillicus.swn.app.model.Planet;
import frostillicus.swn.app.model.PlanetRepository;
import frostillicus.swn.app.model.Player;
import frostillicus.swn.app.model.PlayerCharacter;
import frostillicus.swn.app.model.PlayerCharacterRepository;
import frostillicus.swn.app.model.PlayerClass;
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
			
			String playerId;
			String homeworldId;
			String pcId;
			{
				Player player = new Player();
				player.setName("Foo Fooson");
				playerId = playerRepository.save(player).getId();
				
	
				Planet planet = new Planet();
				planet.setName("Albion");
				homeworldId = planetRepository.save(planet).getId();
	
				PlayerCharacter pc = new PlayerCharacter();
				pc.setName("Fenton Courtenay Tenison");
				pc.setPlayerId(playerId);
				pc.setPlayerClass(PlayerClass.PSYCHIC);
				pc.setLevel(1);
				pc.setExperience(1300);
				pc.setHomeworldId(homeworldId);
				pc.setHitPoints(4);
	
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
				pc.setSaves(saves);
				
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
	
				PlayerCharacter saved = playerCharacterRepository.save(pc);
				pcId = saved.getId();
				assertFalse("PlayerCharacter ID should not be empty", StringUtil.isEmpty(pcId));
			}
			
			Optional<PlayerCharacter> pcOptional = playerCharacterRepository.findById(pcId);
			assertTrue("Should have pc", pcOptional.isPresent());
			System.out.println("PC found: " + pcOptional);
			
			PlayerCharacter pc = pcOptional.get();
			
			Optional<Player> playerOptional = playerRepository.findById(pc.getPlayerId());
			assertTrue("Should have player", playerOptional.isPresent());
			System.out.println("Player found: " + playerOptional);
			
			Optional<Planet> homeworldOptional = planetRepository.findById(pc.getHomeworldId());
			assertTrue("Should have homeworld", homeworldOptional.isPresent());
			System.out.println("Homeworld found: " + homeworldOptional);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	@Test
	public void testCreateInvalidPlayerCharacter() throws Throwable {
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
				pc.setPlayerClass(PlayerClass.PSYCHIC);
				pc.setLevel(-1);
				pc.setExperience(1300);
				pc.setHomeworldId(homeworldId);
	
				playerCharacterRepository.save(pc);
			}
		} catch(Throwable e) {
			// Make sure that the root cause is a ConstraintViolationException
			Throwable t = e;
			while(t != null) {
				if(t instanceof ConstraintViolationException) {
					// Good!
					return;
				} else {
					t = t.getCause();
				}
			}
			throw e;
		}
	}

}
