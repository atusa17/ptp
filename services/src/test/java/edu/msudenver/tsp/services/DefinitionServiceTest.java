package edu.msudenver.tsp.services;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefinitionServiceTest {
    @Mock private RestService restService;
    @InjectMocks private DefinitionService definitionService;

    @Test
    public void testGetAllDefinitions() {
        final List<Definition> definitionList = new ArrayList<>();
        final Definition testDefinition = createDefinition();
        definitionList.add(testDefinition);
        definitionList.add(testDefinition);

        when(restService.get(anyString(), any(TypeToken.class), anyInt(), anyInt(), anyString()))
                .thenReturn(Optional.of(definitionList));

        final Optional<List<Definition>> listOfDefinitions = definitionService.getAllDefinitions();

        assertTrue(listOfDefinitions.isPresent());
        assertThat(listOfDefinitions.get().size(), is(2));
        listOfDefinitions.get().forEach(definition -> assertThat(definition, equalTo(testDefinition)));
    }

    @Test
    public void testGetAllDefinitions_ReturnsEmptyOptional() {
        when(restService.get(anyString(), any(TypeToken.class), anyInt(), anyInt(), anyString()))
                .thenReturn(Optional.empty());

        final Optional<List<Definition>> listOfDefinitions = definitionService.getAllDefinitions();

        assertFalse(listOfDefinitions.isPresent());
    }

    @Test
    public void testGetAllDefinitions_ExceptionThrown() {
        when(restService.get(anyString(), any(TypeToken.class), anyInt(), anyInt(), anyString()))
                .thenThrow(new UnsupportedOperationException("Test exception"));

        final Optional<List<Definition>> listOfDefinitions = definitionService.getAllDefinitions();

        assertFalse(listOfDefinitions.isPresent());
    }

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