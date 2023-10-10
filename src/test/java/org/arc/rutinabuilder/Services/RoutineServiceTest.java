package org.arc.rutinabuilder.Services;

import org.arc.rutinabuilder.Entity.Exercise;
import org.arc.rutinabuilder.Entity.Routine;
import org.junit.jupiter.api.*;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RoutineServiceTest {
    Exercise exerciseTest = new Exercise(5L, "test", 5, 4, null, "test", null, false, 5, "test");
    Set<String> collectionNames = new HashSet<>();
    ArrayList<String> colectionNames = new ArrayList<>(Arrays.asList("chest", "legs", "shoulders", "bicpes", "triceps", "back", "abs"));
    MongoTemplate mongoTemplateMock;

    ExerciceService exerciceServiceMock;
    ExerciceService exerciceService;

    RoutineService routineService;
    RoutineService routineServiceMock;

    @BeforeEach
    void setUp() {
        collectionNames.clear();
        fillCollectionNames();
        mongoTemplateMock = mock(MongoTemplate.class);

        exerciceService = new ExerciceService(mongoTemplateMock);
        exerciceServiceMock = mock(ExerciceService.class);

        routineService = new RoutineService(exerciceServiceMock);
        routineServiceMock = mock(RoutineService.class);
    }

    @AfterEach
    void tearDown() {
    }

    @DisplayName("Create Routine with 3 collections given")
    @Test
    void createRoutine3CollectionGiven() {
        when(exerciceServiceMock.getAllCollectionNames()).thenReturn(fillCollectionNames());
        when(exerciceServiceMock.findLeastPerformedExercise(anyString())).thenReturn(exerciseTest);
        ArrayList<Exercise> exerciseList = new ArrayList<>();
        exerciseList.add(new Exercise(null, null, null, null, null, null, null, null, null, "legs_guide"));
        exerciseList.add(new Exercise(null, null, null, null, null, null, null, null, null, "chest_guide"));
        exerciseList.add(new Exercise(null, null, null, null, null, null, null, null, null, "legs_guide"));
        exerciseList.add(new Exercise(null, null, null, null, null, null, null, null, null, null));
        exerciseList.add(new Exercise(null, null, null, null, null, null, null, null, null, null));
        exerciseList.add(new Exercise(null, null, null, null, null, null, null, null, null, null));
        Routine routine = new Routine(exerciseList);
        Routine routine1 = routineService.CreateRoutine(routine);
        routine1.getExercices().forEach(exercise -> System.out.println(exercise.toString()));
    }

    @DisplayName("Create Routine with 3 collections given")
    @Test
    void createRoutine3CollectionGiven2() {
        when(exerciceServiceMock.getAllCollectionNames()).thenReturn(fillCollectionNames());
        when(exerciceServiceMock.findLeastPerformedExercise(anyString())).thenReturn(exerciseTest);
        ArrayList<Exercise> exerciseList = new ArrayList<>();
        exerciseList.add(new Exercise(null, null, null, null, null, null, null, null, null, "legs_guide"));
        exerciseList.add(new Exercise(null, null, null, null, null, null, null, null, null, null));
        exerciseList.add(new Exercise(null, null, null, null, null, null, null, null, null, null));
        exerciseList.add(new Exercise(null, null, null, null, null, null, null, null, null, "legs_guide"));
        exerciseList.add(new Exercise(null, null, null, null, null, null, null, null, null, null));
        exerciseList.add(new Exercise(null, null, null, null, null, null, null, null, null, null));
        exerciseList.add(new Exercise(null, null, null, null, null, null, null, null, null, "legs_guide"));
        exerciseList.add(new Exercise(null, null, null, null, null, null, null, null, null, null));
        exerciseList.add(new Exercise(null, null, null, null, null, null, null, null, null, null));
        Routine routine = new Routine(exerciseList);
        Routine routine1 = routineService.CreateRoutine(routine);
        routine1.getExercices().forEach(exercise -> System.out.println(exercise.toString()));
    }

    @DisplayName("Create Routine with 0 collections given")
    @Test
    void createRoutine0CollectionGiven() {
        when(exerciceServiceMock.getAllCollectionNames()).thenReturn(fillCollectionNames());
        when(exerciceServiceMock.findLeastPerformedExercise(anyString())).thenReturn(exerciseTest);
        ArrayList<Exercise> exerciseList = new ArrayList<>();
        exerciseList.add(new Exercise(null, null, null, null, null, null, null, null, null, null));
        exerciseList.add(new Exercise(null, null, null, null, null, null, null, null, null, null));
        exerciseList.add(new Exercise(null, null, null, null, null, null, null, null, null, null));
        exerciseList.add(new Exercise(null, null, null, null, null, null, null, null, null, null));
        exerciseList.add(new Exercise(null, null, null, null, null, null, null, null, null, null));
        exerciseList.add(new Exercise(null, null, null, null, null, null, null, null, null, null));
        Routine routine = new Routine(exerciseList);
        Routine routine1 = routineService.CreateRoutine(routine);
        routine1.getExercices().forEach(exercise -> System.out.println(exercise.toString()));
    }

    @DisplayName("Preparing List")
    @Test
    void preparingList() {

    }

    @DisplayName("Fill Exercises Map")
    @Test
    void fillExerciseMap() {
        when(exerciceServiceMock.getAllCollectionNames()).thenReturn(fillCollectionNames());
        HashMap<String, Exercise> map = routineService.fillExerciseMap();
        assertEquals(true, map.isEmpty());
    }

    @Test
    void setListCollectionNameBanned() {
        ArrayList<Exercise> exerciseList = new ArrayList<>();
        exerciseList.add(new Exercise(null, null, null, null, null, null, null, null, null, "legs"));
        exerciseList.add(new Exercise(null, null, null, null, null, null, null, null, null, "chest"));
        exerciseList.add(new Exercise(null, null, null, null, null, null, null, null, null, "legs"));
        exerciseList.add(new Exercise(null, null, null, null, null, null, null, null, null, null));
        exerciseList.add(new Exercise(null, null, null, null, null, null, null, null, null, null));
        exerciseList.add(new Exercise(null, null, null, null, null, null, null, null, null, null));
        Routine routine = new Routine(exerciseList);
        routineService.setListCollectionNameBanned(routine);
        System.out.println("Colecciones disponibles" + colectionNames);
        System.out.println("Colecciones no disponibles" + routineService.getListCollectionNameBanned());
        assertFalse(routineService.getListCollectionNameBanned().isEmpty());

        routineService.getListCollectionNameBanned().clear();
        exerciseList.clear();
        routine.getExercices().clear();

        exerciseList.add(new Exercise(null, null, null, null, null, null, null, null, null, null));
        exerciseList.add(new Exercise(null, null, null, null, null, null, null, null, null, null));
        exerciseList.add(new Exercise(null, null, null, null, null, null, null, null, null, null));
        exerciseList.add(new Exercise(null, null, null, null, null, null, null, null, null, null));
        exerciseList.add(new Exercise(null, null, null, null, null, null, null, null, null, null));
        exerciseList.add(new Exercise(null, null, null, null, null, null, null, null, null, null));
        routine.setExercices(exerciseList);
        routineService.setListCollectionNameBanned(routine);
        System.out.println("Colecciones disponibles" + colectionNames);
        System.out.println("Colecciones no disponibles" + routineService.getListCollectionNameBanned());
        assertTrue(routineService.getListCollectionNameBanned().isEmpty());
    }

    public Set<String> fillCollectionNames() {
        collectionNames.add("chest_guide");
        collectionNames.add("legs_guide");
        collectionNames.add("shoulders_guide");
        collectionNames.add("bicpes_guide");
        collectionNames.add("triceps_guide");
        collectionNames.add("back_guide");
        return collectionNames;
    }
}