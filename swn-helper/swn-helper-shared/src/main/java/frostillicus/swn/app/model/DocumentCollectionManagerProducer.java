package frostillicus.swn.app.model;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.darwino.jnosql.diana.driver.DarwinoDocumentConfiguration;
import org.jnosql.diana.api.document.DocumentCollectionManager;
import org.jnosql.diana.api.document.DocumentCollectionManagerFactory;
import org.jnosql.diana.api.document.DocumentConfiguration;

import com.darwino.jsonstore.Database;

@ApplicationScoped
public class DocumentCollectionManagerProducer {

	private DocumentConfiguration configuration;
	private DocumentCollectionManagerFactory managerFactory;
	
	@PostConstruct
	public void init() {
		configuration = new DarwinoDocumentConfiguration();
		managerFactory = configuration.get();
	}
	
	@Produces
	public DocumentCollectionManager getManager() {
		return managerFactory.get(Database.STORE_DEFAULT);
	}
}
