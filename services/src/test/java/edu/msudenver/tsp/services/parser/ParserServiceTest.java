package edu.msudenver.tsp.services.parser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ParserServiceTest {

    @Mock private ParserService mockParserService;
    @InjectMocks private ParserService parserService;

    @Test
    public void testParseRawInput_EmptyString() {
        final String expected = "0: \n";
        final String actual;

        when(parserService.parseRawInput("")).thenReturn(new Node("", null));

        actual = parserService.parseRawInput("").toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testParseRawInputAndRecurse_EmptyStringEqualsEmptyString() {
        final String expected = "0: \n";
        final String actual = parserService.parseRawInput("").toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testParseRawInput_UselessStringEqualsUselessString() {
        final String expected = "0: cat\n";
        final String actual = parserService.parseRawInput("cat").toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testParseRawInput_SingleIfReturnsIfPlusEmptyString() {
        final String expected = "0: if\n... 1: if\n... 2: \n\n";
        final String actual = parserService.parseRawInput("if").toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testParseRawInput_BaseCaseIfXIsEvenThenXSquaredIsEven() {
        final String expected = "0: if x is even then x^2 is even\n" +
                "... 1: if\n" +
                "... 2:  x is even \n" +
                "... 1: then\n" +
                "... 2:  x^2 is even\n\n";

        final String actual = parserService.parseRawInput("if x is even then x^2 is even").toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testParseRawInput_LetXBeEvenThenXSquaredIsEven() {
        final String expected = "0: let x be even. then x^2 is even.\n" +
                "... 1: let\n" +
                "... 2:  x be even. \n" +
                "... 1: then\n" +
                "... 2:  x^2 is even.\n\n";

        final String actual = parserService.parseRawInput("Let x be even. Then x^2 is even.").toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testParseRawInput_LetIfThen() {
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
    public void testParseRawInput_LetStatementWithoutAnyIfOrThenStatements() {
        final String expected = "0: let a be equal to b.\n" +
                "... 1: let\n" +
                "... 2:  a be equal to b.\n\n";

        final String actual = parserService.parseRawInput("Let a be equal to b.").toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testRetrieveStatements_EmptyStringReturnsEmptyList() {
        final List<String> expectedList = new ArrayList<>();
        expectedList.add("");

        when(mockParserService.parseRawInput(anyString())).thenReturn(new Node("", null));
        final List<String> actualList = parserService.retrieveStatements(mockParserService.parseRawInput(""));

        assertEquals(expectedList, actualList);
    }

    @Test
    public void testRetrieveStatements_BaseCaseReturnsXIsEven() {
        final List<String> expectedList = new ArrayList<>();
        expectedList.add("x is even");
        expectedList.add("x^2 is even");

        final Node testNode = new Node("if x is even then x^2 is even", null);
        testNode.setCenter(new Node("if", testNode));
        testNode.getCenter().setCenter(new Node(" x is even ", testNode.getCenter()));
        testNode.setRight(new Node("then", testNode));
        testNode.getRight().setCenter(new Node(" x^2 is even", testNode.getRight()));

        when(mockParserService.parseRawInput(anyString())).thenReturn(testNode);
        final List<String> actualList = parserService.retrieveStatements(mockParserService.parseRawInput("baseCase"));

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