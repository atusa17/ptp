package edu.msudenver.tsp.services.parser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ParserServiceTest {

    @Mock private ParserService mockParserService;
    @InjectMocks private ParserService parserService;

    @Test
    public void testParseUserInput_noErrors() {
        final String userInput = "string";
        final Node root = new Node(userInput, null);

        final ParserService parse = mock(ParserService.class);
        when(parse.parseRawInput(anyString())).thenReturn(root);
        when(parse.retrieveStatements(root)).thenReturn(new ArrayList<>());
        when(parse.parseUserInput(userInput)).thenCallRealMethod();

        assertTrue(parserService.parseUserInput(userInput));
    }

    @Test
    public void testParseUserInput_thrownError() {
        final String userInput = "string";
        final Node root = new Node(userInput, null);
        final Exception error = new NullPointerException();

        final ParserService parse = mock(ParserService.class);
        when(parse.parseRawInput(anyString())).thenReturn(root);
        when(parse.retrieveStatements(root)).thenThrow(error);
        when(parse.parseUserInput(userInput)).thenCallRealMethod();

        assertFalse(parse.parseUserInput(userInput));
    }

    @Test
    public void testRecurse_NullNode() {
        parserService.recurse(null);
    }

    @Test
    public void testRecurse_StatementContainsLet() {
        final String statement = "let";
        final Node test = new Node(statement, null);

        final ParserService spy = spy(new ParserService());
        doNothing().when(spy).separateByLet(test, statement);

        spy.recurse(test);
    }

    @Test
    public void testRecurse_StatementContainsIf() {
        final String statement = "if";
        final Node test = new Node(statement, null);

        final ParserService spy = spy(new ParserService());
        doNothing().when(spy).separateByIf(test, statement);

        spy.recurse(test);
    }

    @Test
    public void testRecurse_StatementContainsThen() {
        final String statement = "then";
        final Node test = new Node(statement, null);

        final ParserService spy = spy(new ParserService());
        doNothing().when(spy).separateByThen(test, statement);

        spy.recurse(test);
    }

    @Test
    public void testSeparateByLet_StatementContainsOnlyLet() {
        final String statement = "let x be even";
        final Node test = new Node(statement, null);

        final ParserService spy = spy(new ParserService());
        doNothing().when(spy).recurse(anyObject());
        spy.separateByLet(test, statement);

        final String keywordLet = test.getLeft().getStatement();
        assertEquals("let\n", keywordLet);

        final String restOfIt = test.getLeft().getCenter().getStatement();
        assertEquals(" x be even\n", restOfIt);
    }

    @Test
    public void testSeparateByLet_StatementContainsLetIf() {
        final String statement = "let x be even. if x is prime then x is 2.";
        final Node test = new Node(statement, null);

        final ParserService spy = spy(new ParserService());
        doNothing().when(spy).recurse(anyObject());
        spy.separateByLet(test, statement);

        final String keywordLet = test.getLeft().getStatement();
        assertEquals("let\n", keywordLet);

        final String restOfIt = test.getLeft().getCenter().getStatement();
        assertEquals(" x be even. \n", restOfIt);
    }

    @Test
    public void testSeparateByLet_StatementContainsLetThen() {
        final String statement = "let x be an even prime. then x is 2.";
        final Node test = new Node(statement, null);

        final ParserService spy = spy(new ParserService());
        doNothing().when(spy).recurse(anyObject());
        spy.separateByLet(test, statement);

        final String keywordLet = test.getLeft().getStatement();
        assertEquals("let\n", keywordLet);

        final String restOfIt = test.getLeft().getCenter().getStatement();
        assertEquals(" x be an even prime. \n", restOfIt);
    }

    @Test
    public void testSeparateByIf_StatementContainsOnlyIf() {
        final String statement = "if x is even";
        final Node test = new Node(statement, null);

        final ParserService spy = spy(new ParserService());
        doNothing().when(spy).recurse(anyObject());
        spy.separateByIf(test, statement);

        final String keywordIf = test.getCenter().getStatement();
        assertEquals("if\n", keywordIf);

        final String restOfIt = test.getCenter().getCenter().getStatement();
        assertEquals(" x is even\n", restOfIt);
    }

    @Test
    public void testSeparateByIf_StatementContainsThen() {
        final String statement = "if x is even then x^2 is even";
        final Node test = new Node(statement, null);

        final ParserService spy = spy(new ParserService());
        doNothing().when(spy).recurse(anyObject());
        spy.separateByIf(test, statement);

        final String keywordIf = test.getCenter().getStatement();
        assertEquals("if\n", keywordIf);

        final String restOfIt = test.getCenter().getCenter().getStatement();
        assertEquals(" x is even \n", restOfIt);
    }

    @Test
    public void testSeparateByThen_StatementContainsOnlyThen() {
        final String statement = "then x^2 is even";
        final Node test = new Node(statement, null);

        final ParserService spy = spy(new ParserService());
        doNothing().when(spy).recurse(anyObject());
        spy.separateByThen(test, statement);

        final String keywordThen = test.getRight().getStatement();
        assertEquals("then\n", keywordThen);

        final String restOfIt = test.getRight().getCenter().getStatement();
        assertEquals(" x^2 is even\n", restOfIt);
    }

    @Test
    public void testPopulateStatementList_NodeIsNull(){
        final ArrayList<String> list = new ArrayList<>();
        final ArrayList<String> result = (ArrayList)parserService.populateStatementList(null, list);

        assertEquals(list, result);
    }

    @Test
    public void testPopulateStatementList_NodeExists(){
        final ArrayList<String> list = new ArrayList<>();
        final String statement = "this is a statement   ";
        final Node input = new Node(statement, null);

        final ArrayList<String> result = (ArrayList)parserService.populateStatementList(input, list);

        assertTrue(result.contains("this is a statement"));
    }

    @Test
    public void testParseRawInput_EmptyString() {
        final String expected = "0: \n";
        final String actual;

        final ParserService spy = spy(new ParserService());
        when(spy.parseRawInput("")).thenReturn(new Node("", null));
        actual = spy.parseRawInput("").toString();

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