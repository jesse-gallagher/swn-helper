package org.darwino.jnosql.diana.driver;

import org.jnosql.diana.api.Condition;
import org.jnosql.diana.api.TypeReference;
import org.jnosql.diana.api.document.Document;
import org.jnosql.diana.api.document.DocumentCondition;
import org.jnosql.diana.api.document.DocumentDeleteQuery;
import org.jnosql.diana.api.document.DocumentQuery;

import com.darwino.commons.json.JsonArray;
import com.darwino.commons.json.JsonException;
import com.darwino.commons.json.JsonObject;
import com.darwino.commons.json.query.parser.BaseParser;
import com.darwino.jsonstore.Cursor;
import com.darwino.jsonstore.query.nodes.SpecialFieldNode;
import com.darwino.platform.DarwinoContext;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static java.util.Objects.nonNull;
import static org.jnosql.diana.api.Condition.EQUALS;
import static org.jnosql.diana.api.Condition.IN;
import static org.darwino.jnosql.diana.driver.StatementFactory.create;

final class QueryConverter {

    private static final Set<Condition> NOT_APPENDABLE = EnumSet.of(IN, Condition.AND, Condition.OR);

    private static final char PARAM_PREFIX = '$';
    private static final String[] ALL_SELECT = {"*"}; //$NON-NLS-1$

    private QueryConverter() {
    }

    static QueryConverterResult select(DocumentQuery query, String database, String store) throws JsonException {
        JsonObject params = new JsonObject();
        List<String> keys = new ArrayList<>();
        String[] documents = query.getDocuments().stream().toArray(size -> new String[size]);
        if (documents.length == 0) {
            documents = ALL_SELECT;
        }

        Cursor statement = null;
        int firstResult = (int) query.getFirstResult();
        int maxResult = (int) query.getMaxResults();
        
        // TODO ordering
        String[] sorts = query.getSorts().stream().map(s -> s.getName()).toArray(String[]::new);

        if (query.getCondition().isPresent()) {
            JsonObject condition = getCondition(query.getCondition().get(), params, keys);
            if (nonNull(condition)) {
                statement = create(database, store, documents, firstResult, maxResult, sorts, condition);
            } else {
                statement = null;
            }
        } else {
            statement = create(database, store, documents, firstResult, maxResult, sorts);
        }
        return new QueryConverterResult(params, statement, keys);
    }


    static QueryConverterResult delete(DocumentDeleteQuery query, String database, String store) throws JsonException {
        JsonObject params = new JsonObject();
        List<String> ids = new ArrayList<>();
        JsonObject condition = getCondition(query.getCondition().orElseThrow(() -> new IllegalArgumentException("Condigtion is required")), params, ids);
        Cursor cursor = DarwinoContext.get().getSession().getDatabase(database).getStore(store).openCursor();
        if (nonNull(condition)) {
            cursor.query(condition);
        }

        return new QueryConverterResult(params, cursor, ids);
    }

    private static JsonObject getCondition(DocumentCondition condition, JsonObject params, List<String> keys) {
        Document document = condition.getDocument();

        if (!NOT_APPENDABLE.contains(condition.getCondition()) && isKeyField(document)) {
            if (IN.equals(condition.getCondition())) {
                keys.addAll(document.get(new TypeReference<List<String>>() {
                }));
            } else if (EQUALS.equals(condition.getCondition())) {
                keys.add(document.get(String.class));
            }

            return null;
        }
        if (!NOT_APPENDABLE.contains(condition.getCondition())) {
            params.put(document.getName(), document.get());
        }

        String placeholder = PARAM_PREFIX + document.getName();
        switch (condition.getCondition()) {
            case EQUALS:
            		return JsonObject.of(document.getName(), JsonObject.of(BaseParser.Op.EQ.getValue(), placeholder));
            case LESSER_THAN:
            		return JsonObject.of(document.getName(), JsonObject.of(BaseParser.Op.LT.getValue(), placeholder));
            case LESSER_EQUALS_THAN:
            		return JsonObject.of(document.getName(), JsonObject.of(BaseParser.Op.LTE.getValue(), placeholder));
            case GREATER_THAN:
        			return JsonObject.of(document.getName(), JsonObject.of(BaseParser.Op.GT.getValue(), placeholder));
            case GREATER_EQUALS_THAN:
        			return JsonObject.of(document.getName(), JsonObject.of(BaseParser.Op.GTE.getValue(), placeholder));
            case LIKE:
        			return JsonObject.of(document.getName(), JsonObject.of(BaseParser.Op.LIKE.getValue(), placeholder));
            case IN:
        			return JsonObject.of(document.getName(), JsonObject.of(BaseParser.Op.IN.getValue(), placeholder));
            case AND:
            		return JsonObject.of(BaseParser.Op.AND.getValue(), JsonArray.of(
        				document.get(new TypeReference<List<DocumentCondition>>() { })
                                .stream()
                                .map(d -> getCondition(d, params, keys))
                                .filter(Objects::nonNull)
                                .toArray()
        				)
            		);
            case OR:
            	return JsonObject.of(BaseParser.Op.OR.getValue(), JsonArray.of(
        				document.get(new TypeReference<List<DocumentCondition>>() { })
                                .stream()
                                .map(d -> getCondition(d, params, keys))
                                .filter(Objects::nonNull)
                                .toArray()
        				)
            		);
            case NOT:
                DocumentCondition dc = document.get(DocumentCondition.class);
                return JsonObject.of(BaseParser.Op.NOT.getValue(), getCondition(dc, params, keys));
            default:
                throw new IllegalStateException("This condition is not supported in Darwino: " + condition.getCondition());
        }
    }

    private static boolean isKeyField(Document document) {
        return SpecialFieldNode.UNID.equals(document.getName());
    }

    static class QueryConverterResult {

        private final JsonObject params;

        private final Cursor cursor;

        private final List<String> keys;

        QueryConverterResult(JsonObject params, Cursor cursor, List<String> keys) {
            this.params = params;
            this.cursor = cursor;
            this.keys = keys;
        }

        JsonObject getParams() {
            return params;
        }

        Cursor getStatement() {
            return cursor;
        }

        List<String> getKeys() {
            return keys;
        }
    }


}