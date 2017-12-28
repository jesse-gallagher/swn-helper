package org.darwino.jnosql.diana.driver;

import org.jnosql.diana.api.document.Document;
import org.jnosql.diana.api.document.DocumentEntity;
import org.jnosql.diana.driver.ValueUtil;

import com.darwino.commons.json.JsonArray;
import com.darwino.commons.json.JsonException;
import com.darwino.commons.json.JsonJavaFactory;
import com.darwino.commons.json.JsonObject;
import com.darwino.commons.util.StringUtil;
import com.darwino.jsonstore.Cursor;
import com.darwino.jsonstore.Store;
import com.darwino.jsonstore.query.nodes.SpecialFieldNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static java.util.stream.StreamSupport.stream;

final class EntityConverter {
	/**
	 * The field used to store the UNID of the document during JSON
	 * serialization, currently "_id"
	 */
	public static final String ID_FIELD = "_id"; //$NON-NLS-1$
	/**
	 * The expected field containing the collection name of the document in
	 * Darwino, currently "form"
	 */
	// TODO consider making this the store ID
	public static final String NAME_FIELD = "form"; //$NON-NLS-1$

	private EntityConverter() {
	}

	static List<DocumentEntity> convert(Collection<String> keys, Store store) {
		return keys.stream().map(t -> {
			try {
				return store.loadDocument(t, Cursor.JSON_METADATA);
			} catch (JsonException e) {
				throw new RuntimeException(e);
			}
		}).filter(Objects::nonNull).map(doc -> {
			try {
				List<Document> documents = toDocuments(doc);
				String name = StringUtil.toString(doc.get(NAME_FIELD));
				return DocumentEntity.of(name, documents);
			} catch (NullPointerException | JsonException e) {
				throw new RuntimeException(e);
			}
		}).collect(Collectors.toList());
	}

	static List<DocumentEntity> convert(Cursor cursor) throws JsonException {
		List<DocumentEntity> result = new ArrayList<>();
		cursor.find((entry) -> {
			com.darwino.jsonstore.Document doc = entry.loadDocument();
			String name = StringUtil.toString(doc.get(NAME_FIELD));
			List<Document> documents = toDocuments(doc);
			result.add(DocumentEntity.of(name, documents));
			return true;
		});
		return result;
	}

	private static List<Document> toDocuments(com.darwino.jsonstore.Document doc) throws JsonException {
		List<Document> result = new ArrayList<>();
		result.add(Document.of(ID_FIELD, doc.getUnid()));
		JsonObject json = (JsonObject)doc.getJson();
		json.remove(NAME_FIELD);
		result.addAll(toDocuments(json));
		return result;
	}

	@SuppressWarnings("unchecked")
	private static List<Document> toDocuments(Map<String, Object> map) {
		List<Document> documents = new ArrayList<>();
		for (String key : map.keySet()) {
			Object value = map.get(key);
			if (Map.class.isInstance(value)) {
				documents.add(Document.of(key, toDocuments(Map.class.cast(value))));
			} else if (isADocumentIterable(value)) {
				List<Document> subDocuments = new ArrayList<>();
				for (Object object : Iterable.class.cast(value)) {
					subDocuments.addAll(toDocuments(Map.class.cast(object)));
				}
				documents.add(Document.of(key, subDocuments));
			} else {
				documents.add(Document.of(key, value));
			}
		}
		return documents;
	}

	@SuppressWarnings("unchecked")
	private static boolean isADocumentIterable(Object value) {
		return Iterable.class.isInstance(value) && stream(Iterable.class.cast(value).spliterator(), false).allMatch(d -> Map.class.isInstance(d));
	}

	static JsonObject convert(DocumentEntity entity) {
		requireNonNull(entity, "entity is required"); //$NON-NLS-1$

		JsonObject jsonObject = new JsonObject.LinkedMap();
		entity.getDocuments().stream().forEach(toJsonObject(jsonObject));
		jsonObject.put(NAME_FIELD, entity.getName());
		jsonObject.remove(ID_FIELD);
		return jsonObject;
	}

	private static Consumer<Document> toJsonObject(JsonObject jsonObject) {
        return d -> {
        		// Swap out sensitive names
            Object value = ValueUtil.convert(d.getValue());
            
            if (Document.class.isInstance(value)) {
                convertDocument(jsonObject, d, value);
            } else if (Iterable.class.isInstance(value)) {
                convertIterable(jsonObject, d, value);
            } else {
                jsonObject.put(d.getName(), value);
            }
        };
    }

	private static void convertDocument(JsonObject jsonObject, Document d, Object value) {
		Document document = Document.class.cast(value);
		jsonObject.put(d.getName(), Collections.singletonMap(document.getName(), document.get()));
	}

	@SuppressWarnings("unchecked")
	private static void convertIterable(JsonObject jsonObject, Document d, Object value) {
		JsonObject map = new JsonObject.LinkedMap();
		JsonArray array = new JsonArray();
		Iterable.class.cast(value).forEach(o -> {
			if (Document.class.isInstance(o)) {
				Document document = Document.class.cast(o);
				map.put(document.getName(), document.get());
			} else {
				array.add(o);
			}
		});
		if (array.isEmpty()) {
			jsonObject.put(d.getName(), map);
		} else {
			jsonObject.put(d.getName(), array);
		}
	}
}