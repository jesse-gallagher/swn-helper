package frostillicus.swn.app.util;

import java.util.Objects;

import com.darwino.commons.json.JsonObject;

public class ArtemisJsonSchemaGenerator {
	
	public static final String SCHEMA_VERSION = "http://json-schema.org/draft-06/schema#"; //$NON-NLS-1$

	public String generateSchema(Class<?> clazz) {
		Objects.requireNonNull(clazz);
		
		JsonObject result = new JsonObject.LinkedMap();
		
		result.put("$schema", SCHEMA_VERSION); //$NON-NLS-1$
		result.put("description", clazz.getName()); //$NON-NLS-1$
		result.put("type", "object"); //$NON-NLS-1$ //$NON-NLS-2$
		
		
		return result.toString();
	}
	
}
