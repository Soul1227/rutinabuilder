package org.arc.rutinabuilder.Services;

import com.mongodb.client.result.DeleteResult;
import org.arc.rutinabuilder.Entity.Counter;
import org.arc.rutinabuilder.Entity.Exercise;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExerciceServiceTest {

    Exercise exerciceTest;
    Exercise exerciseUpdate;
    MongoTemplate mongoTemplateMock;
    ExerciceService exerciceService;
    ExerciceService exerciceServiceMock;

    @BeforeEach
    void setUp() {
        exerciceTest = new Exercise(4L, "test", 50, 3, null, "test", Date.valueOf(LocalDate.now()), true, 50, "test");
        exerciseUpdate = new Exercise(4L, "test", 60, 3, null, "test", Date.valueOf(LocalDate.now()), true, 80, "test");
        mongoTemplateMock = mock(MongoTemplate.class);
        exerciceService = new ExerciceService(mongoTemplateMock);
        exerciceServiceMock = mock(ExerciceService.class);
    }

    @AfterEach
    void tearDown() {
    }

    /**
     * SaveExercice
     */
    @DisplayName("saveExercice_Success")
    @Test
    void saveExercice_Success() {
        when(mongoTemplateMock.save(eq(exerciceTest), any(String.class))).thenReturn(exerciceTest);
        Exercise savedExercise = exerciceService.saveExercice(exerciceTest);

        // Assert
        assertNotNull(savedExercise);
        assertEquals(exerciceTest, savedExercise);

        // Verificar que mongoTemplateMock.save se llamó con los argumentos correctos
        ArgumentCaptor<Exercise> exerciseCaptor = ArgumentCaptor.forClass(Exercise.class);
        ArgumentCaptor<String> collectionCaptor = ArgumentCaptor.forClass(String.class);

        verify(mongoTemplateMock).save(exerciseCaptor.capture(), collectionCaptor.capture());

        assertEquals(exerciceTest, exerciseCaptor.getValue());
        assertEquals("test_done", collectionCaptor.getValue()); // Asegúrate de que la colección sea la esperada
    }

    @DisplayName("saveExercice_Failure")
    @Test
    void saveExercice_Failure() {
        when(mongoTemplateMock.save(eq(exerciceTest), any(String.class))).thenReturn(null);
        Exercise failedExercise = exerciceService.saveExercice(exerciceTest);

        //Assert
        assertNull(failedExercise);

        ArgumentCaptor<Exercise> exerciseCaptor = ArgumentCaptor.forClass(Exercise.class);
        ArgumentCaptor<String> collectionCaptor = ArgumentCaptor.forClass(String.class);

        verify(mongoTemplateMock).save(exerciseCaptor.capture(), collectionCaptor.capture());

        assertEquals(exerciceTest, exerciseCaptor.getValue());
        assertEquals("test_done", collectionCaptor.getValue()); // Asegúrate de que la colección sea la esperada
    }

    /**
     * GetNextIdForNewObject
     */
    @DisplayName("getNextIdForNewObject_noCounterFound")
    @Test
    void getNextIdForNewObject_noCounterFound() {
        String collectionName = "counter";
        Query query = new Query();
        when(mongoTemplateMock.findOne(query, Counter.class, collectionName)).thenReturn(null);
        /*Al no encontrarse un objeto counter, el método genera uno nuevo con valor 1
         por eso el valor esperado es 1.
         */
        Long counterId = exerciceService.getNextIdForNewObject();
        System.out.println("Respuesta obtenida: " + counterId);
        System.out.println("Respuesta esperada: " + 1L);
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
        System.out.println("Respuesta obtenida: " + counterId);
        System.out.println("Respuesta esperada: " + counter.getNextId());
        assertEquals(counter.getNextId(), counterId);
    }

    /**
     * UpdateExercice
     */
    @DisplayName("updateExercice_Success")
    @Test
    void updateExercice_Success() {

    }


    @DisplayName("updateExercice_Failure")
    @Test
    void updateExercice_Failure() {
    }

    /**
     * FindOneById
     */
    @DisplayName("findOneById_Success")
    @Test
    void findOneById_Success() {
        Query query = new Query(Criteria.where("id").is(exerciceTest.getId()));
        when(mongoTemplateMock.findOne(query, Exercise.class, exerciceTest.getCollection())).thenReturn(exerciceTest);
        Exercise exercice = exerciceService.findOneById(exerciceTest.getId(), exerciceTest.getCollection());
        System.out.println("respuesta obtenida: " + exercice);
        System.out.println("respuesta esperada: " + exerciceTest);
        assertNotNull(exercice);
        assertEquals(exerciceTest, exercice);
    }

    @DisplayName("findOneById_Failure")
    @Test
    void findOneById_Failure() {
        Query query = new Query(Criteria.where("id").is(exerciceTest.getId()));
        when(mongoTemplateMock.findOne(query, Exercise.class, exerciceTest.getCollection())).thenReturn(null);
        Exercise exercice = exerciceService.findOneById(exerciceTest.getId(), exerciceTest.getCollection());
        System.out.println("respuesta obtenida: " + exercice);
        System.out.println("respuesta esperada: " + null);
        assertNull(exercice);
        verify(mongoTemplateMock).findOne(query,Exercise.class,exerciceTest.getCollection());
    }

    /**
     * DeleteExercice
     */
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

        boolean result = exerciceService.deleteExercice(exerciceTest.getId(), exerciceTest.getCollection());
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

    /**
     * GetAllCollectionNames
     */
    @DisplayName("getAllCollectionNames")
    @Test
    void getAllCollectionNames() {
        List<String> expectedCollectionNames = Arrays.asList("collection1", "collection2", "collection3");
        when(mongoTemplateMock.getCollectionNames()).thenReturn(new HashSet<>(expectedCollectionNames));
        Set<String> actualCollectionNames = exerciceService.getAllCollectionNames();
        assertTrue(actualCollectionNames.containsAll(expectedCollectionNames));
    }

    /**
     * GetAllExerciceDocuments
     */
    @DisplayName("getAllExerciceDocuments_EmptyList")
    @Test
    void getAllExerciceDocuments_empty() {
        List<Exercise> emptyList = new ArrayList<>();
        String collectionName = "legs";
        when(mongoTemplateMock.findAll(Exercise.class,collectionName)).thenReturn(emptyList);

        List<Exercise> list = exerciceService.getAllExerciceOfOneType("legs");
        assertTrue(list.isEmpty());
        verify(mongoTemplateMock).findAll(Exercise.class,collectionName);
    }

    @DisplayName("getAllExerciceDocuments_FilledList")
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