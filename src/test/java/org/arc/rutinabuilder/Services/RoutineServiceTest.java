package org.arc.rutinabuilder.Services;

import org.arc.rutinabuilder.Entity.Exercise;
import org.arc.rutinabuilder.Entity.Routine;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class RoutineServiceTest {
    ArrayList<String> colectionNames = new ArrayList<>(Arrays.asList("chest", "legs", "shoulders", "bicpes", "triceps", "back", "abs"));

    @Mock
    private Routine routineTest;

    @Mock
    private ExerciceService exerciceService;

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private RoutineService routineService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this); // Inicializar las anotaciones Mockito
    }

    @AfterEach
    void tearDown() {
    }

    @DisplayName("Create Routine")
    @Test
    void createRoutine() {
        when(routineService.getListCollectionName()).thenReturn(colectionNames);
        ArrayList<Exercise> exerciseList = new ArrayList<>();
        exerciseList.add(new Exercise(null, null, null, null, null, null, null, null, null, "legs"));
        exerciseList.add(new Exercise(null, null, null, null, null, null, null, null, null, "chest"));
        exerciseList.add(new Exercise(null, null, null, null, null, null, null, null, null, "legs"));
        exerciseList.add(new Exercise(null, null, null, null, null, null, null, null, null, null));
        exerciseList.add(new Exercise(null, null, null, null, null, null, null, null, null, null));
        exerciseList.add(new Exercise(null, null, null, null, null, null, null, null, null, null));
        Routine routine = new Routine(exerciseList);
        Routine routine1 = routineService.CreateRoutine(routine);
        System.out.println(routine1);
    }

    @Test
    void fillExerciseMap() {
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
}