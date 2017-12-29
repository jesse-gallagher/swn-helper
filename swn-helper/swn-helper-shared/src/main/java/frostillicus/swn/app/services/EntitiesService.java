package frostillicus.swn.app.services;

import java.util.Set;
import java.util.stream.Collectors;

import org.jnosql.artemis.Entity;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import com.darwino.commons.json.JsonObject;
import com.darwino.commons.services.AbstractHttpService;
import com.darwino.commons.services.HttpServiceContext;

public class EntitiesService extends AbstractHttpService {

	@Override
	protected void doGet(HttpServiceContext context) throws Exception {
		Reflections reflections = new Reflections(
				new ConfigurationBuilder()
				.addUrls(ClasspathHelper.forPackage("frostillicus.swn.app.model")) //$NON-NLS-1$
				.setScanners(new TypeAnnotationsScanner(), new SubTypesScanner())
		);
		Set<Class<?>> entities = reflections.getTypesAnnotatedWith(Entity.class);
		JsonObject result = new JsonObject();
		
		result.put("entities", //$NON-NLS-1$
			entities.stream()
				.map(c -> c.getSimpleName())
				.collect(Collectors.toList())
		);
		
		context.emitJson(result);
	}

}
