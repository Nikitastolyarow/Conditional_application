package ru.netology.demo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DemoApplicationTests {
    @Autowired
    private TestRestTemplate restTemplate;

    private static GenericContainer<?> devContainer;
    private static GenericContainer<?> prodContainer;

    @BeforeAll
    public static void setUp() {
        devContainer = new GenericContainer<>("devapp").withExposedPorts(8080);
        prodContainer = new GenericContainer<>("prodapp").withExposedPorts(8081);
        devContainer.start();
        prodContainer.start();
    }

    @Test
    void contextLoadsDev() {
        Integer devPort = devContainer.getMappedPort(8080);
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + devPort, String.class);
        assertEquals("Expected Response", response.getBody());
    }

    @Test
    void contextLoadsProd() {
        Integer prodPort = prodContainer.getMappedPort(8081);
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + prodPort, String.class);
        assertEquals("Expected Response", response.getBody());
    }
}
