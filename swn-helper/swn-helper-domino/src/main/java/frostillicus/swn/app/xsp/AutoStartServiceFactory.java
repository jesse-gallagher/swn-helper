/*!COPYRIGHT HEADER! - CONFIDENTIAL 
 *
 * Darwino Inc Confidential.
 *
 * (c) Copyright Darwino Inc. 2014-2016.
 *
 * Notice: The information contained in the source code for these files is the property 
 * of Darwino Inc. which, with its licensors, if any, owns all the intellectual property 
 * rights, including all copyright rights thereto.  Such information may only be used 
 * for debugging, troubleshooting and informational purposes.  All other uses of this information, 
 * including any production or commercial uses, are prohibited. 
 */

package frostillicus.swn.app.xsp;

import com.darwino.domino.application.AbstractDarwinoServiceFactory;

import frostillicus.swn.app.AppActivator;

/**
 * Class to auto-start the plugin.
 * 
 * This acts as the application listener in regular J2EE to initialize the application.
 *
 */
public class AutoStartServiceFactory extends AbstractDarwinoServiceFactory {
	
	public AutoStartServiceFactory() {
		super(AppActivator.getDefault());
	}

}
