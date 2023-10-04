package org.arc.rutinabuilder.Controller;

import org.arc.rutinabuilder.Entity.Exercise;
import org.arc.rutinabuilder.Services.ExerciceService;
import org.junit.jupiter.api.*;

import static org.mockito.Mockito.*;

import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ExerciceControllerTest {
    ExerciceService exerciceServiceMock = mock(ExerciceService.class);

    @BeforeEach
    void setUp() {
    }

    @DisplayName("Post: Creating new Exercice")
    @Test
    void testSaveExercice_Created() {
        Exercise e = new Exercise();
        when(exerciceServiceMock.saveExercice(any(Exercise.class))).thenReturn(e);
        ExerciceController exerciceController = new ExerciceController(exerciceServiceMock);

        ResponseEntity<Exercise> response = exerciceController.saveExercise(new Exercise());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @DisplayName("Post: Creating new Exercice")
    @Test
    void testSaveExercice_Error() {
        when(exerciceServiceMock.saveExercice(any(Exercise.class))).thenReturn(null);
        ExerciceController exerciceController = new ExerciceController(exerciceServiceMock);

        ResponseEntity<Exercise> errorResponse = exerciceController.saveExercise(new Exercise());
        assertEquals(HttpStatus.BAD_REQUEST, errorResponse.getStatusCode());
    }

    @DisplayName("Put: Updating Exercice")
    @Test
    void testUpdateExercice_OK() {
        when(exerciceServiceMock.updateExercice(any(Exercise.class))).thenReturn(new Exercise());
        ExerciceController exerciceController = new ExerciceController(exerciceServiceMock);

        ResponseEntity<Exercise> expectedResponse = exerciceController.updateExercice(new Exercise());
        assertEquals(HttpStatus.OK, expectedResponse.getStatusCode());

    }

    @DisplayName("Put: Updating Exercice")
    @Test
    void testUpdateExercice_Error() {
        when(exerciceServiceMock.updateExercice(any(Exercise.class))).thenReturn(null);
        ExerciceController exerciceController = new ExerciceController(exerciceServiceMock);
        
        ResponseEntity<Exercise> errorResponse = exerciceController.updateExercice(new Exercise());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, errorResponse.getStatusCode());
    }
}