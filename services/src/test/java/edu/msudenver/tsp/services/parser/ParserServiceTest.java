package edu.msudenver.tsp.services.parser;

import edu.msudenver.tsp.persistence.controller.DefinitionController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ParserServiceTest {

    @Mock
    DefinitionController definitionControllerMock = mock(DefinitionController.class);

    @Mock
    ParserService mps = mock(ParserService.class);

    ParserService ps = new ParserService(definitionControllerMock, null,
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
        final String expected = "0: if x is even then x^2 is even\n" +
                "... 1: if\n" +
                "... 2:  x is even \n" +
                "... 1: then\n" +
                "... 2:  x^2 is even\n\n";

        final String testCase = "if x is even then x^2 is even";

        assertEquals(expected, ps.parseRawInput(testCase).toString());
    }

    @Test
    public void testLetXBeEvenThenXSquaredIsEven() {
        final String expected = "0: let x be even. then x^2 is even.\n" +
                "... 1: let\n" +
                "... 2:  x be even. \n" +
                "... 1: then\n" +
                "... 2:  x^2 is even.\n\n";

        final String testCase = "Let x be even. Then x^2 is even.";

        assertEquals(expected, ps.parseRawInput(testCase).toString());
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

        final String testCase = "Let a. If b, then c.";

        assertEquals(expected, ps.parseRawInput(testCase).toString());
    }

    @Test
    public void testLetAlone() {
        final String expected = "0: let a be equal to b.\n" +
                "... 1: let\n" +
                "... 2:  a be equal to b.\n\n";

        final String testCase = "Let a be equal to b.";

        assertEquals(expected, ps.parseRawInput(testCase).toString());
    }

    @Test
    public void testEmptyStringReturnsEmptyList() {
        final ArrayList<String> expectedList = new ArrayList<>();
        expectedList.add("");

        when(mps.parseRawInput(anyString())).thenReturn(new Node("", null));

        assertEquals(expectedList, ps.retrieveStatements(mps.parseRawInput("")));
    }

    @Test
    public void testBaseCaseReturnsXIsEven() {
        final ArrayList<String> expectation = new ArrayList<>();
        expectation.add("x is even");
        expectation.add("x^2 is even");

        ps.root = new Node("if x is even then x^2 is even", null);
        ps.root.center = new Node("if", ps.root);
        ps.root.center.center = new Node(" x is even ", ps.root.center);
        ps.root.right = new Node("then", ps.root);
        ps.root.right.center = new Node(" x^2 is even", ps.root.right);
        when(mps.parseRawInput(anyString())).thenReturn(ps.root);

        assertEquals(expectation, ps.retrieveStatements(mps.parseRawInput("baseCase")));
    }

    @Test
    public void testDriveParsingProcess() {
        mps.root = new Node("", null);
        final ArrayList<String> testdummy = new ArrayList<>();
        when(mps.parseRawInput(anyString())).thenReturn(mps.root);
        when(mps.retrieveStatements(mps.root)).thenReturn(testdummy);

        ps.driveParsingProcess("");
    }
}