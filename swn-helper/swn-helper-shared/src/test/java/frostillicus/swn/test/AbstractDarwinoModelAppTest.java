package frostillicus.swn.test;

import org.junit.BeforeClass;

import com.darwino.commons.Platform;
import com.darwino.commons.json.JsonJavaFactory;
import com.darwino.jre.application.DarwinoJreApplication;
import com.darwino.jsonstore.LocalJsonDBServer;

import frostillicus.swn.test.app.AppDatabaseDef;
import frostillicus.swn.test.app.j2ee.AppJ2EEApplication;
import frostillicus.swn.test.app.j2ee.AppPlugin;

public abstract class AbstractDarwinoModelAppTest {

	@BeforeClass
	public static void setUpDarwinoApp() throws Exception {
		try {
			Platform.registerPlugin(AppPlugin.class);
			DarwinoJreApplication app = AppJ2EEApplication.create(null);
			app.initDatabase(AppDatabaseDef.DATABASE_NAME, LocalJsonDBServer.DEPLOY_FORCE);
			app.getLocalJsonDBServer().setJsonFactory(JsonJavaFactory.LinkedMapFactory.instance);
		} catch (Throwable t) {
			t.printStackTrace();
			throw t;
		}
	}
}
