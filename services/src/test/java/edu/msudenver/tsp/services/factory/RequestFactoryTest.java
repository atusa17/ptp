package edu.msudenver.tsp.services.factory;

import org.apache.http.client.fluent.Request;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class RequestFactoryTest {
    private final RequestFactory requestFactory = new RequestFactory();

    @Test
    public void testDelete() {
        final Request testRequest = requestFactory.delete("testUri");

        assertNotNull(testRequest);
    }

    @Test
    public void testGet() {
        final Request testRequest = requestFactory.get("testUri");

        assertNotNull(testRequest);
    }

    @Test
    public void testPost() {
        final Request testRequest = requestFactory.post("testUri", "testJson");

        assertNotNull(testRequest);
    }

    @Test
    public void testPost_blankRequestJson() {
        final Request testRequest = requestFactory.post("testUri", null);

        assertNotNull(testRequest);
    }

    @Test
    public void testPut() {
        final Request testRequest = requestFactory.put("testUri", "testJson");

        assertNotNull(testRequest);
    }

    @Test
    public void testPut_blankRequestJson() {
        final Request testRequest = requestFactory.put("testUri", null);

        assertNotNull(testRequest);
    }

    @Test
    public void testPatch() {
        final Request testRequest = requestFactory.patch("testUri", "testJson");

        assertNotNull(testRequest);
    }

    @Test
    public void testPatch_blankRequestJson() {
        final Request testRequest = requestFactory.patch("testUri", null);

        assertNotNull(testRequest);
    }
}