package edu.msudenver.tsp.services;

import com.google.gson.GsonBuilder;
import edu.msudenver.tsp.services.dto.Definition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefinitionServiceTest {
    @Mock private RestService restService;
    @InjectMocks private DefinitionService definitionService;

    @Test
    public void testCreateDefinition() {
        final Definition testDefinition = createDefinition();
        final String testDefinitionJson = new GsonBuilder().create().toJson(testDefinition);

        when(restService.post(anyString(), anyString(), any(), anyInt(), anyInt()))
                .thenReturn(Optional.of(testDefinition));

        final Optional<Definition> createdDefinition = definitionService.createDefinition(testDefinition);

        assertNotNull(createdDefinition);
        assertTrue(createdDefinition.isPresent());
        assertEquals(testDefinition, createdDefinition.get());
        verify(restService).post(anyString(), eq(testDefinitionJson), any(), anyInt(), anyInt());
    }

    @Test
    public void testCreateDefinition_unableToCreateDefinition() {
        final Definition testDefinition = createDefinition();
        final String testDefinitionJson = new GsonBuilder().create().toJson(testDefinition);

        when(restService.post(anyString(), anyString(), any(), anyInt(), anyInt()))
                .thenReturn(Optional.empty());

        final Optional<Definition> createdDefinition = definitionService.createDefinition(testDefinition);

        assertNotNull(createdDefinition);
        assertFalse(createdDefinition.isPresent());
        verify(restService).post(anyString(), eq(testDefinitionJson), any(), anyInt(), anyInt());
    }

    @Test
    public void testCreateDefinition_restServiceThrowsException() {
        final Definition testDefinition = createDefinition();
        final String testDefinitionJson = new GsonBuilder().create().toJson(testDefinition);

        when(restService.post(anyString(), anyString(), any(), anyInt(), anyInt()))
                .thenThrow(new UnsupportedOperationException("test exception"));

        final Optional<Definition> createdDefinition = definitionService.createDefinition(testDefinition);

        assertNotNull(createdDefinition);
        assertFalse(createdDefinition.isPresent());
        verify(restService).post(anyString(), eq(testDefinitionJson), any(), anyInt(), anyInt());
    }

    private Definition createDefinition() {
        final List<String> definitionList = new ArrayList<>();
        definitionList.add("Test definition 1");

        final List<String> notationList = new ArrayList<>();
        notationList.add("\\testLaTeX");

        final Definition definition = new Definition();
        definition.setName("Test Name");
        definition.setDefinition(definitionList);
        definition.setNotation(notationList);

        return definition;
    }
}