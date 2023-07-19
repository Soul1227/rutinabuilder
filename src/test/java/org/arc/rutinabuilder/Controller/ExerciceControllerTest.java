package org.arc.rutinabuilder.Controller;

import org.arc.rutinabuilder.Entity.Exercice;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ExerciceControllerTest {

    private String jsonTest = """
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
    private Exercice exerciceTest;

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

    @DisplayName("Get: Getting Exercice by its Id")
    @Order(2)
    @Test
    void findById() {
        String url = "/exercice/" + exerciceTest.getId().toString();
        System.out.println(url);
        ResponseEntity<Exercice> response = testRestTemplate.getForEntity(url, Exercice.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Exercice exercice = response.getBody();
        assertEquals(exerciceTest.getId(), exercice.getId());
    }

    @DisplayName("Post: Creating new Exercice")
    @Order(1)
    @Test
    void saveExercice() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<String> request = new HttpEntity<>(jsonTest, headers);
        ResponseEntity<Exercice> response = testRestTemplate.exchange("/exercice", HttpMethod.POST, request, Exercice.class);
        exerciceTest = response.getBody();
        System.out.println(exerciceTest.toString());
        System.out.println(exerciceTest.getId());
        assertEquals(exerciceTest.getName(), "Sentadillas");
        assertEquals(exerciceTest.getRep(), 50);
    }

    /**
     *     void findOneById() {
     *         ResponseEntity<Book> response = testRestTemplate.getForEntity("/api/book/1",Book.class);
     *         assertEquals(HttpStatus.OK,response.getStatusCode());
     *     }
     */
}