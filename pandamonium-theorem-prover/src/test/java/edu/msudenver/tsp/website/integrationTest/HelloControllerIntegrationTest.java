package edu.msudenver.tsp.website.integrationTest;

import hello.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.URL;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
public class HelloControllerIntegrationTest {
    @LocalServerPort private int port;
    private URL base;
    @Autowired private TestRestTemplate testRestTemplate;

    @Before
    public void setUp() throws Exception {
        this.base = new URL("http://localhost:" + port + "/");
    }

    @Test
    public void testGetHello() {
        final ResponseEntity<String> response = testRestTemplate.getForEntity(base.toString(),
                String.class);
        assertEquals("Greetings from Spring Boot!", response.getBody());
    }
}
