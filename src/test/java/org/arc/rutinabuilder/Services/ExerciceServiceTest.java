package org.arc.rutinabuilder.Services;

import com.mongodb.client.result.DeleteResult;
import org.arc.rutinabuilder.Entity.Counter;
import org.arc.rutinabuilder.Entity.Exercice;
import org.bson.BsonDocument;
import org.bson.BsonElement;
import org.bson.BsonValue;
import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExerciceServiceTest {
    Exercice exerciceTest = new Exercice(4L, "test", 50, 3, null, "test", Date.valueOf(LocalDate.now()), 50, "test");
    MongoTemplate mongoTemplateMock = mock(MongoTemplate.class);
    ExerciceService exerciceService = new ExerciceService(mongoTemplateMock);
    ExerciceService exerciceServiceMock = mock(ExerciceService.class);

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
    }

    @DisplayName("saveExercice_Success")
    @Test
    void saveExercice_Success() {
        when(mongoTemplateMock.save(exerciceTest, exerciceTest.getCollection())).thenReturn(exerciceTest);

        Boolean response = exerciceService.saveExercice(exerciceTest);

        assertTrue(response);
        verify(mongoTemplateMock).save(exerciceTest, exerciceTest.getCollection());
    }

    @DisplayName("saveExercice_Failure")
    @Test
    void saveExercice_Failure() {
        when(mongoTemplateMock.save(exerciceTest, exerciceTest.getCollection())).thenThrow(new RuntimeException("Failed to save"));

        Boolean response = exerciceService.saveExercice(exerciceTest);

        assertFalse(response);
        verify(mongoTemplateMock).save(exerciceTest, exerciceTest.getCollection());
    }

    @DisplayName("getNextIdForNewObject_noCounterFound")
    @Test
    void getNextIdForNewObject_noCounterFound() {
        String collectionName = "counter";
        Query query = new Query();
        when(mongoTemplateMock.findOne(query, Counter.class, collectionName)).thenReturn(null);

        Long counterId = exerciceService.getNextIdForNewObject();
        assertEquals(1L, counterId);
    }

    @DisplayName("getNextIdForNewObject_CounterFound")
    @Test
    void getNextIdForNewObject_CounterFound() {
        String collectionName = "counter";
        Counter counter = new Counter(5L);
        Query query = new Query();
        when(mongoTemplateMock.findOne(query, Counter.class, collectionName)).thenReturn(counter);

        Long counterId = exerciceService.getNextIdForNewObject();
        assertEquals(5L, counterId);
    }

    @DisplayName("updateExercice_Success")
    @Test
    void updateExercice_Success() {
        when(exerciceServiceMock.updateExercice(exerciceTest)).thenReturn(true);
        Boolean response = exerciceServiceMock.updateExercice(exerciceTest);
        assertTrue(response);
        verify(exerciceServiceMock).updateExercice(exerciceTest);
    }

    @DisplayName("updateExercice_Failure")
    @Test
    void updateExercice_Failure() {
        when(exerciceServiceMock.updateExercice(exerciceTest)).thenReturn(false);
        Boolean response = exerciceServiceMock.updateExercice(exerciceTest);
        assertFalse(response);
        verify(exerciceServiceMock).updateExercice(exerciceTest);
    }

    @DisplayName("findOneById_Success")
    @Test
    void findOneById_Success() {
        Query query = new Query(Criteria.where("id").is(exerciceTest.getId()));
        when(mongoTemplateMock.findOne(query, Exercice.class, exerciceTest.getCollection())).thenReturn(exerciceTest);

        Exercice exercice = exerciceService.findOneById(exerciceTest.getId(), exerciceTest.getCollection());

        assertEquals(exerciceTest, exercice);
    }

    @DisplayName("findOneById_Failure")
    @Test
    void findOneById_Failure() {
        Query query = new Query(Criteria.where("id").is(exerciceTest.getId()));
        when(mongoTemplateMock.find(query, Exercice.class, exerciceTest.getCollection())).thenReturn(null);

        Exercice exercice = exerciceService.findOneById(exerciceTest.getId(), exerciceTest.getCollection());

        assertNull(exercice);
    }

    @DisplayName("deleteExercice_Success")
    @Test
    void deleteExercice_Success() {
        Query query = new Query(Criteria.where("id").is(exerciceTest.getId()));
        when(mongoTemplateMock.remove(query, Exercice.class, exerciceTest.getCollection())).thenReturn(new DeleteResult() {
            @Override
            public boolean wasAcknowledged() {
                return true;
            }

            @Override
            public long getDeletedCount() {
                return 0;
            }
        });

        Boolean result = exerciceService.deleteExercice(exerciceTest.getId(), exerciceTest.getCollection());
        assertTrue(result);
        verify(mongoTemplateMock).remove(query, Exercice.class, exerciceTest.getCollection());
    }

    @DisplayName("deleteExercice_Failure")
    @Test
    void deleteExercice_Failure() {
        Query query = new Query(Criteria.where("id").is(exerciceTest.getId()));
        when(mongoTemplateMock.remove(query, Exercice.class, exerciceTest.getCollection())).thenReturn(new DeleteResult() {
            @Override
            public boolean wasAcknowledged() {
                return false;
            }

            @Override
            public long getDeletedCount() {
                return 0;
            }
        });

        boolean result = exerciceService.deleteExercice(exerciceTest.getId(), exerciceTest.getCollection());
        assertFalse(result);
        verify(mongoTemplateMock).remove(query, Exercice.class, exerciceTest.getCollection());
    }

    @DisplayName("getAllCollectionNames")
    @Test
    void getAllCollectionNames() {
        List<String> expectedCollectionNames = Arrays.asList("collection1", "collection2", "collection3");
        when(mongoTemplateMock.getCollectionNames()).thenReturn(new HashSet<>(expectedCollectionNames));
        Set<String> actualCollectionNames = exerciceService.getAllCollectionNames();
        assertTrue(actualCollectionNames.containsAll(expectedCollectionNames));
    }

    @DisplayName("getAllExerciceDocuments_EmptyList")
    @Test
    void getAllExerciceDocuments_empty() {
        List<Document> emptyList = new ArrayList<>();
        when(exerciceServiceMock.getAllExerciceDocuments("legs")).thenReturn(emptyList);

        List<Document> list = exerciceServiceMock.getAllExerciceDocuments("legs");
        assertTrue(list.isEmpty());
    }

    @DisplayName("getAllExerciceDocuments_EmptyList")
    @Test
    void getAllExerciceDocuments_filled() {
        Document document1 = new Document();
        Document document2 = new Document();
        Document document3 = new Document();
        List<Document> filledList = Arrays.asList(document1, document2, document3);
        when(exerciceServiceMock.getAllExerciceDocuments("legs")).thenReturn(filledList);

        List<Document> list = exerciceServiceMock.getAllExerciceDocuments("legs");
        assertFalse(list.isEmpty());
        assertEquals(filledList.size(), list.size());
        assertTrue(list.containsAll(filledList));
    }

    @DisplayName("convertToJSON_nullEntry")
    @Test
    void convertToJSON_nullEntry() {
        List<String> json = exerciceService.convertToJSON(null);
        assertTrue(json.isEmpty());
    }

    @DisplayName("convertToJSON_ValidInput")
    @Test
    void convertToJSON_ValidInput() {
        ExerciceService exerciceService = new ExerciceService();

        Document document1 = new Document("field1", "value1");
        Document document2 = new Document("field2", "value2");
        List<Document> bsonDocuments = Arrays.asList(document1, document2);

        List<String> jsonDocuments = exerciceService.convertToJSON(bsonDocuments);

        assertEquals(2, jsonDocuments.size());
        assertTrue(jsonDocuments.get(0).contains("field1"));
        assertTrue(jsonDocuments.get(1).contains("field2"));
    }
}