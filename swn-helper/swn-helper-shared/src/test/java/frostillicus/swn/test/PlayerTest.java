package frostillicus.swn.test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;

import frostillicus.swn.app.model.Player;
import frostillicus.swn.app.model.PlayerRepository;
import frostillicus.swn.test.runner.WeldJUnit4Runner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("nls")
@RunWith(WeldJUnit4Runner.class)
public class PlayerTest extends AbstractDarwinoModelAppTest {
	
	@Inject
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

	@Test
	public void testFindPlayers() {
		{
			Player player1 = new Player();
			player1.setName("Player 1");
			playerRepository.save(player1);
			Player player2 = new Player();
			player2.setName("Player 2");
			playerRepository.save(player2);
		}
		
		{
			List<Player> players = playerRepository.findAll();
			assertNotEquals("players should not be null", null, players);
			assertTrue("players should have at least 2 entries", players.size() >= 2);
			assertTrue("Should find player 1", players.stream().anyMatch(p -> p.getName().equals("Player 1")));
			assertTrue("Should find player 2", players.stream().anyMatch(p -> p.getName().equals("Player 2")));
		}
		
		Optional<Player> player1 = playerRepository.findByName("Player 1");
		assertTrue("player 1 should be found", player1.isPresent());
		assertEquals("player 1's name should match", "Player 1", player1.get().getName());
	}
}
