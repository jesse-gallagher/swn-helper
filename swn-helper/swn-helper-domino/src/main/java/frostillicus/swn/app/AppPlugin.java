/*!COPYRIGHT HEADER! 
 *
 */

package frostillicus.swn.app;

import java.util.List;

import com.darwino.domino.application.DominoApplicationPlugin;



/**
 * Domino Plugin for registering the services.
 */
public class AppPlugin extends DominoApplicationPlugin {
	
	public AppPlugin() {
		super(AppActivator.getDefault(),"Domino Application");
	}

	@Override
	public void findExtensions(Class<?> serviceClass, List<Object> extensions) {
		AppBasePlugin.findExtensions(serviceClass, extensions);
		
		// Add custom extensions here
	}
}
