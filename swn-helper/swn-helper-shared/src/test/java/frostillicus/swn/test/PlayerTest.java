package frostillicus.swn.test;

import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;

import org.jnosql.artemis.Database;
import org.jnosql.artemis.DatabaseType;
import org.junit.Test;
import org.junit.runner.RunWith;

import frostillicus.swn.app.model.Player;
import frostillicus.swn.app.model.PlayerRepository;
import frostillicus.swn.test.runner.WeldJUnit4Runner;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("nls")
@RunWith(WeldJUnit4Runner.class)
public class PlayerTest extends AbstractDarwinoModelAppTest {
	
	@Inject
	@Database(DatabaseType.DOCUMENT)
	private PlayerRepository playerRepository;

	@Test
	public void testCreatePlayer() {
		try {
	        String id = UUID.randomUUID().toString();
	
	    		Player player = new Player();
	    		player.setId(id);
	    		player.setName("Foo Fooson");
	    		
	        Player saved = playerRepository.save(player);
	        assertNotEquals("Saved should not be null", null, saved);
	        System.out.println("Person saved: " + saved);

	        Optional<Player> playerOptional = playerRepository.findById(id);
	        assertTrue("Should have player", playerOptional.isPresent());
	        System.out.println("Entity found: " + playerOptional);
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
