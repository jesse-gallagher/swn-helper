package org.darwino.jnosql.diana.driver;

import org.jnosql.diana.api.Sort;
import org.jnosql.diana.api.document.Document;
import org.jnosql.diana.api.document.DocumentEntity;
import org.jnosql.diana.api.document.DocumentQuery;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.contains;
import static org.jnosql.diana.api.Sort.SortType.ASC;
import static org.jnosql.diana.api.Sort.SortType.DESC;
import static org.jnosql.diana.api.document.DocumentCondition.eq;
import static org.jnosql.diana.api.document.query.DocumentQueryBuilder.select;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class DocumentQueryTest extends AbstractDarwinoAppTest  {

    public static final String COLLECTION_NAME = "person";
    private DarwinoDocumentCollectionManager entityManager;

    {
        DarwinoDocumentConfiguration configuration = new DarwinoDocumentConfiguration();
        DarwinoDocumentCollectionManagerFactory managerFactory = configuration.get();
        entityManager = managerFactory.get("default");
    }

    @AfterClass
    public static void afterClass() {
    }

    @BeforeClass
    public static void beforeClass() throws InterruptedException {
        DarwinoDocumentConfiguration configuration = new DarwinoDocumentConfiguration();
        DarwinoDocumentCollectionManagerFactory managerFactory = configuration.get();
        DarwinoDocumentCollectionManager entityManager = managerFactory.get("default");

        DocumentEntity entity = DocumentEntity.of("person", asList(Document.of("_id", "id")
                , Document.of("name", "name")));
        DocumentEntity entity2 = DocumentEntity.of("person", asList(Document.of("_id", "id2")
                , Document.of("name", "name")));
        DocumentEntity entity3 = DocumentEntity.of("person", asList(Document.of("_id", "id3")
                , Document.of("name", "name")));
        DocumentEntity entity4 = DocumentEntity.of("person", asList(Document.of("_id", "id4")
                , Document.of("name", "name3")));

        entityManager.insert(Arrays.asList(entity, entity2, entity3, entity4));
        Thread.sleep(2_000L);

    }


    @Test
    public void shouldShouldDefineLimit() {

        DocumentEntity entity = DocumentEntity.of("person", asList(Document.of("_id", "id")
                , Document.of("name", "name")));

        Optional<Document> name = entity.find("name");

        DocumentQuery query = select().from(COLLECTION_NAME)
                .where(eq(name.get()))
                .limit(2L)
                .build();

        List<DocumentEntity> entities = entityManager.select(query);
        assertEquals(2, entities.size());

    }

    @Test
    public void shouldShouldDefineStart()  {
        DocumentEntity entity = DocumentEntity.of("person", asList(Document.of("_id", "id")
                , Document.of("name", "name")));

        Optional<Document> name = entity.find("name");

        DocumentQuery query = select().from(COLLECTION_NAME)
                .where(eq(name.get()))
                .start(1L)
                .build();
        List<DocumentEntity> entities = entityManager.select(query);
        assertEquals(2, entities.size());

    }

    @Test
    public void shouldShouldDefineLimitAndStart() {

        DocumentEntity entity = DocumentEntity.of("person", asList(Document.of("_id", "id")
                , Document.of("name", "name")));

        Optional<Document> name = entity.find("name");
        DocumentQuery query = select().from(COLLECTION_NAME)
                .where(eq(name.get()))
                .start(2L)
                .limit(2L)
                .build();

        List<DocumentEntity> entities = entityManager.select(query);
        assertEquals(1, entities.size());

    }


    @Test
    public void shouldSelectAll(){
        DocumentEntity entity = DocumentEntity.of("person", asList(Document.of("_id", "id")
                , Document.of("name", "name")));


        DocumentQuery query = select().from(COLLECTION_NAME).build();
        Optional<Document> name = entity.find("name");
        List<DocumentEntity> entities = entityManager.select(query);
        assertFalse(entities.isEmpty());
        assertTrue(entities.size() >= 4);
    }


    @Test
    public void shouldFindDocumentByName() {
        DocumentEntity entity = DocumentEntity.of("person", asList(Document.of("_id", "id4")
                , Document.of("name", "name3"), Document.of("_key", "person:id4")));

        Optional<Document> name = entity.find("name");
        DocumentQuery query = select().from(COLLECTION_NAME).where(eq(name.get())).build();
        List<DocumentEntity> entities = entityManager.select(query);
        assertFalse(entities.isEmpty());
        assertThat(entities, contains(entity));
    }

    @Test
    public void shouldFindDocumentByNameSortAsc() {
        DocumentEntity entity = DocumentEntity.of("person", asList(Document.of("_id", "id4")
                , Document.of("name", "name3"), Document.of("_key", "person:id4")));

        Optional<Document> name = entity.find("name");

        DocumentQuery query = select().from(COLLECTION_NAME)
                .orderBy(Sort.of("name", ASC))
                .build();

        List<DocumentEntity> entities = entityManager.select(query);
        List<String> result = entities.stream().flatMap(e -> e.getDocuments().stream())
                .filter(d -> "name".equals(d.getName()))
                .map(d -> d.get(String.class))
                .collect(Collectors.toList());

        assertFalse(result.isEmpty());
        assertThat(result, contains("name", "name", "name", "name3"));
    }

    @Test
    public void shouldFindDocumentByNameSortDesc() {
        DocumentEntity entity = DocumentEntity.of("person", asList(Document.of("_id", "id4")
                , Document.of("name", "name3"), Document.of("_key", "person:id4")));

        Optional<Document> name = entity.find("name");

        DocumentQuery query = select().from(COLLECTION_NAME)
                .orderBy(Sort.of("name", DESC))
                .build();
        List<DocumentEntity> entities = entityManager.select(query);
        List<String> result = entities.stream().flatMap(e -> e.getDocuments().stream())
                .filter(d -> "name".equals(d.getName()))
                .map(d -> d.get(String.class))
                .collect(Collectors.toList());

        assertFalse(result.isEmpty());
        assertThat(result, contains("name3", "name", "name", "name"));
    }



    @Test
    public void shouldFindDocumentById() {
        DocumentEntity entity = DocumentEntity.of("person", asList(Document.of("_id", "id")
                , Document.of("name", "name"), Document.of("_key", "person:id")));
        Optional<Document> id = entity.find("_id");

        DocumentQuery query = select().from(COLLECTION_NAME)
                .where(eq(id.get()))
                .build();

        List<DocumentEntity> entities = entityManager.select(query);
        assertFalse(entities.isEmpty());
        assertThat(entities, contains(entity));
    }

    @Test
    public void shouldFindDocumentByKey() {
        DocumentEntity entity = DocumentEntity.of("person", asList(Document.of("_id", "id")
                , Document.of("name", "name"), Document.of("_key", "person:id")));

        Optional<Document> id = entity.find("_key");
        DocumentQuery query = select().from(COLLECTION_NAME)
                .where(eq(id.get()))
                .build();
        List<DocumentEntity> entities = entityManager.select(query);
        assertFalse(entities.isEmpty());
        assertThat(entities, contains(entity));
    }
}