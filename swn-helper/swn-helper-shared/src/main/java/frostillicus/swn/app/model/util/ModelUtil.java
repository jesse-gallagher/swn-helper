package frostillicus.swn.app.model.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;
import java.util.Set;

import org.jnosql.artemis.DatabaseQualifier;
import org.jnosql.artemis.Entity;
import org.jnosql.artemis.Repository;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import com.darwino.commons.util.StringUtil;

import frostillicus.swn.app.WeldContext;

public enum ModelUtil {
	;
	
	private static Reflections reflections;
	private static Set<Class<?>> modelClasses;
	private static Set<Class<? extends Repository<?, ?>>> repositoryClasses;
	
	static {
		reflections = new Reflections(
				new ConfigurationBuilder()
				.addUrls(ClasspathHelper.forPackage("frostillicus.swn.app.model")) //$NON-NLS-1$
				.setScanners(new TypeAnnotationsScanner(), new SubTypesScanner())
		);
	}
	
	public static synchronized Set<Class<?>> getModelClasses() {
		if(modelClasses == null) {
			modelClasses = reflections.getTypesAnnotatedWith(Entity.class);
		}
		return modelClasses;
	}
	
	public static Optional<Class<?>> getModelClass(String modelName) {
		return getModelClasses()
			.stream()
			.filter(c -> StringUtil.equals(c.getSimpleName(), modelName))
			.findFirst();
	}
	
	@SuppressWarnings("unchecked")
	public static synchronized <C, T extends Repository<C, String>> T getRepository(Class<C> modelClass) {
		if(repositoryClasses == null) {
			repositoryClasses = (Set<Class<? extends Repository<?, ?>>>)(Set<?>)reflections.getSubTypesOf(Repository.class);
		}
		for(Class<? extends Repository<?, ?>> clazz : repositoryClasses) {
			// Check for the first matching generic
			Type[] interfaces = clazz.getGenericInterfaces();
			for(Type type : interfaces) {
				if(type instanceof ParameterizedType) {
					ParameterizedType pt = (ParameterizedType)type;
					if(pt.getRawType().equals(Repository.class)) {
						// Check the first param for the desired model class
						if(modelClass.equals(pt.getActualTypeArguments()[0])) {
							return (T) WeldContext.INSTANCE.getBean(clazz, DatabaseQualifier.ofDocument());
						}
					}
				}
			}
		}
		return null;
	}
}
