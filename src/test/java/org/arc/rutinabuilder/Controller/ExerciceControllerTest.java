package org.arc.rutinabuilder.Controller;

import org.arc.rutinabuilder.Entity.Exercice;
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
        when(exerciceServiceMock.saveExercice(any(Exercice.class))).thenReturn(true);
        ExerciceController exerciceController = new ExerciceController(exerciceServiceMock);

        ResponseEntity<Exercice> response = exerciceController.saveExercice(new Exercice());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @DisplayName("Post: Creating new Exercice")
    @Test
    void testSaveExercice_Error() {
        when(exerciceServiceMock.saveExercice(any(Exercice.class))).thenReturn(false);
        ExerciceController exerciceController = new ExerciceController(exerciceServiceMock);

        ResponseEntity<Exercice> errorResponse = exerciceController.saveExercice(new Exercice());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, errorResponse.getStatusCode());
    }

    @DisplayName("Put: Updating Exercice")
    @Test
    void testUpdateExercice_OK() {
        when(exerciceServiceMock.updateExercice(any(Exercice.class))).thenReturn(true);
        ExerciceController exerciceController = new ExerciceController(exerciceServiceMock);

        ResponseEntity<Exercice> expectedResponse = exerciceController.updateExercice(new Exercice());
        assertEquals(HttpStatus.OK, expectedResponse.getStatusCode());

    }

    @DisplayName("Put: Updating Exercice")
    @Test
    void testUpdateExercice_Error() {
        when(exerciceServiceMock.updateExercice(any(Exercice.class))).thenReturn(false);
        ExerciceController exerciceController = new ExerciceController(exerciceServiceMock);
        
        ResponseEntity<Exercice> errorResponse = exerciceController.updateExercice(new Exercice());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, errorResponse.getStatusCode());
    }
}