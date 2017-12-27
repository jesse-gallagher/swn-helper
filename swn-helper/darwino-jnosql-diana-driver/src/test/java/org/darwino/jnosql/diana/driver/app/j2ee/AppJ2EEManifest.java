/*!COPYRIGHT HEADER! 
 *
 */

package org.darwino.jnosql.diana.driver.app.j2ee;

import com.darwino.j2ee.application.DarwinoJ2EEManifest;

/**
 * J2EE Application Manifest.
 * 
 * @author Philippe Riand
 */
public class AppJ2EEManifest extends DarwinoJ2EEManifest {
	
	public AppJ2EEManifest() {
	}
	
	/**
	 * Properties to push down to the device
	 */
	@Override
	protected String[] getMobilePushedPropertyKeys() {
		return new String[] {
			"swnhelper.instances"
		};
	}
}
