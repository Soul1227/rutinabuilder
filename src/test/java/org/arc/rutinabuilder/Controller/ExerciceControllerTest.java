package org.arc.rutinabuilder.Controller;

import org.arc.rutinabuilder.Entity.Exercice;
import org.arc.rutinabuilder.Services.ExerciceService;
import org.junit.jupiter.api.*;

import static org.mockito.Mockito.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ExerciceControllerTest {

    private final String jsonTest = """
            {
                "name":"Sentadillas",
                "rep":50,
                "set":3,
                "time":null,
                "description":"Sentadillas normales",
                "date":"2023-07-12",
                "weight":null,
                "collection":"legs"
            }
            """;

    // TestRestTemplate es una clase proporcionada por el framework de pruebas de Spring para realizar solicitudes HTTP en pruebas de integración
    private TestRestTemplate testRestTemplate;

    // RestTemplateBuilder es una clase utilizada para construir instancias de RestTemplate
    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    // @LocalServerPort es una anotación que inyecta el puerto aleatorio del servidor local en la variable 'port'
    @LocalServerPort
    private int port;

    // @BeforeEach es una anotación que indica que este método se ejecutará antes de cada prueba
    @BeforeEach
    void setUp() {
        // Configurar el RestTemplateBuilder con la URL raíz del servidor local utilizando el puerto inyectado
        restTemplateBuilder = restTemplateBuilder.rootUri("http://localhost:" + port);

        // Crear una instancia de TestRestTemplate utilizando el RestTemplateBuilder configurado
        testRestTemplate = new TestRestTemplate(restTemplateBuilder);
    }

    private ExerciceService createMockExerciceService(boolean expectedResult) {
        ExerciceService exerciceServiceMock = mock(ExerciceService.class);
        when(exerciceServiceMock.saveExercice(any(Exercice.class))).thenReturn(expectedResult);
        return exerciceServiceMock;
    }

    @DisplayName("Post: Creating new Exercice")
    @Order(1)
    @Test
    void testSaveExercice() {
        ExerciceService exerciceServiceMock = createMockExerciceService(true);
        ExerciceController exerciceController = new ExerciceController(exerciceServiceMock);

        ResponseEntity<Exercice> response = exerciceController.saveExercice(new Exercice());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        exerciceServiceMock = createMockExerciceService(false);
        ResponseEntity<Exercice> errorResponse = exerciceController.saveExercice(new Exercice());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, errorResponse.getStatusCode());
    }

    @DisplayName("PUT: Updating Exercice")
    @Order(2)
    @Test
    void testUpdateExercice() {
        ExerciceService exerciceServiceMock = createMockExerciceService(true);
        ExerciceController exerciceController = new ExerciceController(exerciceServiceMock);

        ResponseEntity<Exercice> expectedResponse = exerciceController.updateExercice(new Exercice());
        assertEquals(HttpStatus.OK, expectedResponse.getStatusCode());

        exerciceServiceMock = createMockExerciceService(false);
        ResponseEntity<Exercice> errorResponse = exerciceController.updateExercice(new Exercice());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, errorResponse.getStatusCode());
    }


}