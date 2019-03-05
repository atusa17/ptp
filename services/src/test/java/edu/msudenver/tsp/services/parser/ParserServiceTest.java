package edu.msudenver.tsp.services.parser;

import edu.msudenver.tsp.persistence.controller.DefinitionController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ParserServiceTest {

    private final DefinitionController definitionControllerMock = mock(DefinitionController.class);
    private final ParserService mockParserService = mock(ParserService.class);

    @InjectMocks
    private final ParserService parserService = new ParserService(definitionControllerMock, null,
            null,  null);

    @Test
    public void testEmptyStringEqualsEmptyString() {
        final String expected = "0: \n";
        final String actual = parserService.parseRawInput("").toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testUselessStringEqualsUselessString() {
        final String expected = "0: cat\n";
        final String actual = parserService.parseRawInput("cat").toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testSingleIfReturnsIfPlusEmptyString() {
        final String expected = "0: if\n... 1: if\n... 2: \n\n";
        final String actual = parserService.parseRawInput("if").toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testBaseCaseIfXIsEvenThenXSquaredIsEven() {
        final String expected = "0: if x is even then x^2 is even\n" +
                "... 1: if\n" +
                "... 2:  x is even \n" +
                "... 1: then\n" +
                "... 2:  x^2 is even\n\n";

        final String actual = parserService.parseRawInput("if x is even then x^2 is even").toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testLetXBeEvenThenXSquaredIsEven() {
        final String expected = "0: let x be even. then x^2 is even.\n" +
                "... 1: let\n" +
                "... 2:  x be even. \n" +
                "... 1: then\n" +
                "... 2:  x^2 is even.\n\n";

        final String actual = parserService.parseRawInput("Let x be even. Then x^2 is even.").toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testLetIfThen() {
        final String expected = "0: let a. if b, then c.\n" +
                "... 1: let\n" +
                "... 2:  a. \n" +
                "... 1: if\n" +
                "... 2:  b, \n" +
                "... 1: then\n" +
                "... 2:  c.\n\n";

        final String actual = parserService.parseRawInput("Let a. If b, then c.").toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testLetStatementWithoutAnyIfOrThenStatements() {
        final String expected = "0: let a be equal to b.\n" +
                "... 1: let\n" +
                "... 2:  a be equal to b.\n\n";

        final String actual = parserService.parseRawInput("Let a be equal to b.").toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testEmptyStringReturnsEmptyList() {
        final ArrayList<String> expectedList = new ArrayList<>();
        expectedList.add("");

        when(mockParserService.parseRawInput(anyString())).thenReturn(new Node("", null));
        final ArrayList<String> actualList = parserService.retrieveStatements(mockParserService.parseRawInput(""));

        assertEquals(expectedList, actualList);
    }

    @Test
    public void testBaseCaseReturnsXIsEven() {
        final ArrayList<String> expectedList = new ArrayList<>();
        expectedList.add("x is even");
        expectedList.add("x^2 is even");

        final Node testNode = new Node("if x is even then x^2 is even", null);
        testNode.center = new Node("if", testNode);
        testNode.center.center = new Node(" x is even ", testNode.center);
        testNode.right = new Node("then", testNode);
        testNode.right.center = new Node(" x^2 is even", testNode.right);

        when(mockParserService.parseRawInput(anyString())).thenReturn(testNode);
        final ArrayList<String> actualList = parserService.retrieveStatements(mockParserService.parseRawInput("baseCase"));

        assertEquals(expectedList, actualList);
    }

    @Test
    public void testDriveParseUserInput() {
        final Node testNode = new Node("", null);
        when(mockParserService.parseRawInput(anyString())).thenReturn(testNode);
        when(mockParserService.retrieveStatements(testNode)).thenReturn(new ArrayList<>());

        final boolean successfulTestDrive = parserService.parseUserInput("");

        assertTrue(successfulTestDrive);
    }
}