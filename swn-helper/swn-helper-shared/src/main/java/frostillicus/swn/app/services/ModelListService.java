package frostillicus.swn.app.services;

import java.util.Optional;

import org.jnosql.artemis.Repository;

import com.darwino.commons.json.JsonObject;
import com.darwino.commons.services.AbstractHttpService;
import com.darwino.commons.services.HttpServiceContext;
import com.darwino.commons.util.StringUtil;

import frostillicus.swn.app.model.util.ModelUtil;

public class ModelListService extends AbstractHttpService {
	
	private final String modelName;

	public ModelListService(String modelName) {
		if(StringUtil.isEmpty(modelName)) {
			throw new IllegalArgumentException("modelName cannot be empty"); //$NON-NLS-1$
		}
		this.modelName = modelName;
	}

	@Override
	protected void doGet(HttpServiceContext context) throws Exception {
		Optional<Class<?>> modelClass = ModelUtil.getModelClass(modelName);
		if(!modelClass.isPresent()) {
			throw new IllegalArgumentException("Could not find model class for name " + modelName);
		}
		
		Repository<?, String> repository = ModelUtil.getRepository(modelClass.get());
		if(repository == null) {
			throw new NullPointerException("Could not find repository for class " + modelClass.get().getName());
		} else {
			context.emitJson(JsonObject.of("repository", repository.toString()));
		}
	}
}
