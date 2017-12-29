package frostillicus.swn.test;

import java.util.Optional;
import java.util.UUID;

import org.darwino.jnosql.diana.driver.DarwinoDocumentCollectionManager;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.jnosql.artemis.document.DocumentTemplate;
import org.jnosql.diana.api.document.Document;
import org.jnosql.diana.api.document.DocumentCollectionManager;
import org.jnosql.diana.api.document.DocumentQuery;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import frostillicus.swn.app.model.DocumentCollectionManagerProducer;
import frostillicus.swn.app.model.Player;

import static org.jnosql.diana.api.document.DocumentCondition.eq;
import static org.jnosql.diana.api.document.query.DocumentQueryBuilder.select;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class PlayerTest extends AbstractDarwinoAppTest {
	static Weld weld;
	static WeldContainer container;
	
	@BeforeClass
	public static void init() {
		weld = new Weld();
		weld.addBeanClass(DocumentCollectionManagerProducer.class);
		weld.addBeanClass(Player.class);
	    container = weld.initialize();
	}
	
	@AfterClass
	public static void term() {
	    weld.shutdown();
	}

	@Test
	public void testCreatePlayer() {
		try {
	        String id = UUID.randomUUID().toString();
	
	    		Player player = new Player();
	    		player.setId(id);
	    		player.setName("Foo Fooson");
	    		
	        DocumentTemplate documentTemplate = container.select(DocumentTemplate.class).get();
	        Player saved = documentTemplate.insert(player);
	        assertNotEquals("Saved should not be null", null, saved);
	        System.out.println("Person saved: " + saved);
	
	
	        DocumentQuery query = select().from("Player").where(eq(Document.of("_id", id))).build();
	
	        Optional<Player> playerOptional = documentTemplate.singleResult(query);
	        assertTrue("Should have player", playerOptional.isPresent());
	        System.out.println("Entity found: " + playerOptional);
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
