/*!COPYRIGHT HEADER! 
 *
 */

package frostillicus.swn.app;

import javax.servlet.ServletContext;

import org.osgi.framework.Bundle;

import com.darwino.commons.json.JsonException;
import com.darwino.commons.platform.ManagedBeansService;
import com.darwino.domino.application.DarwinoDominoApplication;
import com.darwino.platform.DarwinoManifest;


/**
 * Domino application.
 * 
 * @author Philippe Riand
 */
public class DarwinoApplication extends DarwinoDominoApplication {
	
	public static DarwinoDominoApplication create(ServletContext context) throws JsonException {
		if(!DarwinoDominoApplication.isInitialized()) {
			DarwinoApplication app = new DarwinoApplication(
					context,
					new AppManifest(new AppDominoManifest())
			);
			app.init();
		}
		return DarwinoDominoApplication.get();
	}
	
	protected DarwinoApplication(ServletContext context, DarwinoManifest manifest) {
		super(context,manifest);
	}

	@Override
	public Bundle getBundle() {
		return AppActivator.getDefault().getBundle();
	}

	@Override
	public String[] getResourcesBundleNames() {
		return new String[] {
			"frostillicus.swn.domino", //$NON-NLS-1$
			"frostillicus.swn.webui", //$NON-NLS-1$
			"frostillicus.swn.shared", //$NON-NLS-1$
			"com.darwino.domino.libs" //$NON-NLS-1$
		};
	}
	
	@Override
	public String[] getConfigurationBeanNames() {
		return new String[] {"swnhelper",ManagedBeansService.LOCAL_NAME,ManagedBeansService.DEFAULT_NAME}; //$NON-NLS-1$
	}
}
