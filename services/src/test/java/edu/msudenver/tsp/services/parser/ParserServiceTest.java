package edu.msudenver.tsp.services.parser;

import edu.msudenver.tsp.persistence.manager.DefinitionManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class ParserServiceTest {

    @Mock
    DefinitionManager definitionManagerMock = mock(DefinitionManager.class);

    ParserService ps = new ParserService(definitionManagerMock, null,
                                         null,  null);

    @Test
    public void testEmptyStringEqualsEmptyString() {
        assertEquals("0: \n", ps.parseRawInput("").toString());
    }

    @Test
    public void testUselessStringEqualsUselessString() {
        assertEquals("0: cat\n", ps.parseRawInput("cat").toString());
    }

    @Test
    public void testSingleIfReturnsIfPlusEmptyString() {
        assertEquals("0: if\n... 1: if\n... 2: \n\n", ps.parseRawInput("if").toString());
    }

    @Test
    public void testBaseCaseIfXIsEvenThenXSquaredIsEven() {
        assertEquals("0: if x is even then x^2 is even\n" +
                        "... 1: if\n" +
                        "... 2:  x is even \n" +
                        "... 1: then\n" +
                        "... 2:  x^2 is even\n\n",
                ps.parseRawInput("if x is even then x^2 is even").toString());
    }

    @Test
    public void testEmptyStringReturnsEmptyList() {
        ArrayList<String> expectation = new ArrayList<String>();
        expectation.add("");

        assertEquals(expectation, ps.retrieveStatements(ps.parseRawInput("")));
    }

    @Test
    public void testBaseCaseReturnsXIsEven() {
        ArrayList<String> expectation = new ArrayList<String>();
        expectation.add("x is even");
        expectation.add("x^2 is even");

        assertEquals(expectation, ps.retrieveStatements(ps.parseRawInput("if x is even then x^2 is even")));
    }
}