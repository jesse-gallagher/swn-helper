/*!COPYRIGHT HEADER! 
 *
 */

package frostillicus.swn.app;

import java.util.List;

import com.darwino.commons.services.HttpService;
import com.darwino.commons.services.HttpServiceContext;
import com.darwino.commons.services.rest.RestServiceBinder;
import com.darwino.commons.services.rest.RestServiceFactory;
import com.darwino.platform.DarwinoHttpConstants;

import frostillicus.swn.app.services.AppInformationRest;
import frostillicus.swn.app.services.ModelListService;
import frostillicus.swn.app.services.ModelsService;


/**
 * Application REST Services Factory.
 * 
 * This is the place where to define custom application services.
 * 
 * @author Philippe Riand
 */
public class AppRestServiceFactory extends RestServiceFactory {
	
	public AppRestServiceFactory() {
		super(DarwinoHttpConstants.APPSERVICES_PATH);
	}
	
	@Override
	protected void createServicesBinders(List<RestServiceBinder> binders) {
		/////////////////////////////////////////////////////////////////////////////////
		// APP INFORMATION
		binders.add(new RestServiceBinder() {
			@Override
			public HttpService createService(HttpServiceContext context, String[] parts) {
				return new AppInformationRest();
			}
		});
		
		
		binders.add(new RestServiceBinder("models") { //$NON-NLS-1$
			@Override public HttpService createService(HttpServiceContext context, String[] parts) {
				return new ModelsService();
			}
		});
		binders.add(new RestServiceBinder("models", null) { //$NON-NLS-1$
			@Override public HttpService createService(HttpServiceContext context, String[] parts) {
				String modelName = parts[1];
				return new ModelListService(modelName);
			}
			
		});
	}	
}
