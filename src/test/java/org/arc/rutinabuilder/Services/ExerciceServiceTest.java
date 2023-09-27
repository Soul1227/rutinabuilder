package org.arc.rutinabuilder.Services;

import com.mongodb.client.result.DeleteResult;
import org.arc.rutinabuilder.Entity.Counter;
import org.arc.rutinabuilder.Entity.Exercise;
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
    Exercise exerciceTest = new Exercise(4L, "test", 50, 3, null, "test", Date.valueOf(LocalDate.now()), true, 50, "test");
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
        when(exerciceServiceMock.saveExercice(exerciceTest)).thenReturn(exerciceTest);

        Exercise response = exerciceServiceMock.saveExercice(exerciceTest);

        assertEquals(exerciceTest, response);
        verify(exerciceServiceMock).saveExercice(exerciceTest);
    }

    @DisplayName("saveExercice_Failure")
    @Test
    void saveExercice_Failure() {
        when(exerciceServiceMock.saveExercice(exerciceTest)).thenReturn(null);
        Exercise response = exerciceServiceMock.saveExercice(exerciceTest);
        assertNull(response);
        verify(exerciceServiceMock).saveExercice(exerciceTest);
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
        when(exerciceServiceMock.updateExercice(exerciceTest)).thenReturn(exerciceTest);
        Exercise response = exerciceServiceMock.updateExercice(exerciceTest);
        assertEquals(exerciceTest, response);
        verify(exerciceServiceMock).updateExercice(exerciceTest);
    }

    @DisplayName("updateExercice_Failure")
    @Test
    void updateExercice_Failure() {
        when(exerciceServiceMock.updateExercice(exerciceTest)).thenReturn(new Exercise());
        Exercise response = exerciceServiceMock.updateExercice(exerciceTest);
        assertNotEquals(exerciceTest, response);
        verify(exerciceServiceMock).updateExercice(exerciceTest);
    }

    @DisplayName("findOneById_Success")
    @Test
    void findOneById_Success() {
        Query query = new Query(Criteria.where("id").is(exerciceTest.getId()));
        when(mongoTemplateMock.findOne(query, Exercise.class, exerciceTest.getCollection())).thenReturn(exerciceTest);

        Exercise exercice = exerciceService.findOneById(exerciceTest.getId(), exerciceTest.getCollection());

        assertEquals(exerciceTest, exercice);
    }

    @DisplayName("findOneById_Failure")
    @Test
    void findOneById_Failure() {
        Query query = new Query(Criteria.where("id").is(exerciceTest.getId()));
        when(mongoTemplateMock.find(query, Exercise.class, exerciceTest.getCollection())).thenReturn(null);

        Exercise exercice = exerciceService.findOneById(exerciceTest.getId(), exerciceTest.getCollection());

        assertNull(exercice);
    }

    @DisplayName("deleteExercice_Success")
    @Test
    void deleteExercice_Success() {
        Query query = new Query(Criteria.where("id").is(exerciceTest.getId()));
        when(mongoTemplateMock.remove(query, Exercise.class, exerciceTest.getCollection())).thenReturn(new DeleteResult() {
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
        verify(mongoTemplateMock).remove(query, Exercise.class, exerciceTest.getCollection());
    }

    @DisplayName("deleteExercice_Failure")
    @Test
    void deleteExercice_Failure() {
        Query query = new Query(Criteria.where("id").is(exerciceTest.getId()));
        when(mongoTemplateMock.remove(query, Exercise.class, exerciceTest.getCollection())).thenReturn(new DeleteResult() {
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
        verify(mongoTemplateMock).remove(query, Exercise.class, exerciceTest.getCollection());
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
        List<Exercise> emptyList = new ArrayList<>();
        when(exerciceServiceMock.getAllExerciceOfOneType("legs")).thenReturn(emptyList);

        List<Exercise> list = exerciceServiceMock.getAllExerciceOfOneType("legs");
        assertTrue(list.isEmpty());
    }

    @DisplayName("getAllExerciceDocuments_EmptyList")
    @Test
    void getAllExerciceDocuments_filled() {
        Exercise document1 = new Exercise();
        Exercise document2 = new Exercise();
        Exercise document3 = new Exercise();
        List<Exercise> filledList = Arrays.asList(document1, document2, document3);
        when(exerciceServiceMock.getAllExerciceOfOneType("legs")).thenReturn(filledList);

        List<Exercise> list = exerciceServiceMock.getAllExerciceOfOneType("legs");
        assertFalse(list.isEmpty());
        assertEquals(filledList.size(), list.size());
        assertTrue(list.containsAll(filledList));
    }

}