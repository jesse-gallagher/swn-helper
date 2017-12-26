/*!COPYRIGHT HEADER! 
 *
 */

package frostillicus.swn.app;

import com.darwino.commons.services.HttpServiceFactories;
import com.darwino.domino.application.DarwinoActivator;
import com.darwino.domino.application.DarwinoApplicationServlet;



/**
 * Domino Application Servlet.
 * 
 * @author priand
 */
public class AppServlet extends DarwinoApplicationServlet {
	
	private static final long serialVersionUID = 1L;

	public AppServlet() {
	}
	
	@Override
	public DarwinoActivator getActivator() {
		return AppActivator.getDefault();
	}
	
	/**
	 * 
	 * Add the application specific services. 
	 */
	@Override
	protected void addApplicationServiceFactories(HttpServiceFactories factories) {
		// The service should always executed locally when running on a server
		factories.add(new AppRestServiceFactory());
	}
}
