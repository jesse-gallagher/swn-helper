/*!COPYRIGHT HEADER! 
 *
 */

package org.darwino.jnosql.diana.driver.app;

import com.darwino.jsonstore.extensions.DefaultExtensionRegistry;
import com.darwino.jsonstore.impl.DarwinoInfCursorFactory;
import com.darwino.jsonstore.local.DefaultDatabaseACLFactory;

/**
 * Database Business logic - event handlers.
 * 
 * @author Philippe Riand
 */
public  class AppDBBusinessLogic extends DefaultExtensionRegistry {
	
	@SuppressWarnings("unused")
	public AppDBBusinessLogic() {
		// Add here the database events to register to the JSON store
//		registerDocumentEvents("<My Database Id>", "<My Store Id>", new DocumentEvents() {
//			@Override
//			public void querySaveDocument(Document doc) throws JsonException {
//			}
//		});
		
		// Use a query factory
		setQueryFactory(new DarwinoInfCursorFactory(getClass()));
		
		// Default database ACL factory read the design element coming from Domino
		if(false) {
			setDatabaseACLFactory(new DefaultDatabaseACLFactory());
		}
	}
}
