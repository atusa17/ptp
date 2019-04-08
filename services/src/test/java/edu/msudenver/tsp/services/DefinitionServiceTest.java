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
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

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

        when(restService.get(anyString(), any(), anyInt(), anyInt(), anyString()))
                .thenReturn(Optional.of(definitionList));

        final Optional<List<Definition>> listOfDefinitions = definitionService.getAllDefinitions();

        assertTrue(listOfDefinitions.isPresent());
        assertThat(listOfDefinitions.get().size(), is(2));
        listOfDefinitions.get().forEach(definition -> assertThat(definition, equalTo(testDefinition)));
        verify(restService).get(anyString(), any(), anyInt(), anyInt(), anyString());
    }

    @Test
    public void testGetAllDefinitions_RequestReturnsEmptyOptional() {
        when(restService.get(anyString(), any(), anyInt(), anyInt(), anyString()))
                .thenReturn(Optional.empty());

        final Optional<List<Definition>> listOfDefinitions = definitionService.getAllDefinitions();

        assertFalse(listOfDefinitions.isPresent());
        verify(restService).get(anyString(), any(), anyInt(), anyInt(), anyString());
    }

    @Test
    public void testGetAllDefinitions_ExceptionThrownWhenSendingRequest() {
        when(restService.get(anyString(), any(), anyInt(), anyInt(), anyString()))
                .thenThrow(new UnsupportedOperationException("Test exception"));

        final Optional<List<Definition>> listOfDefinitions = definitionService.getAllDefinitions();

        assertFalse(listOfDefinitions.isPresent());
        verify(restService).get(anyString(), any(), anyInt(), anyInt(), anyString());
    }

    @Test
    public void testFindById() {
        final Definition testDefinition = createDefinition();
        when(restService.get(anyString(), any(), anyInt(), anyInt(), anyString()))
                .thenReturn(Optional.of(testDefinition));

        final Optional<Definition> foundDefinition = definitionService.findById(testDefinition.getId());

        assertTrue(foundDefinition.isPresent());
        assertThat(foundDefinition.get().getId(), is(1));
        assertThat(foundDefinition.get(), is(equalTo(testDefinition)));
        verify(restService).get(anyString(), any(), anyInt(), anyInt(), anyString());
    }

    @Test
    public void testFindById_RequestReturnsEmptyOptional() {
        when(restService.get(anyString(), any(), anyInt(), anyInt(), anyString()))
                .thenReturn(Optional.empty());

        final Optional<Definition> nonExistentDefinition = definitionService.findById(1);

        assertFalse(nonExistentDefinition.isPresent());
        verify(restService).get(anyString(), any(), anyInt(), anyInt(), anyString());
    }

    @Test
    public void testFindById_ExceptionThrownWhenSendingRequest() {
        when(restService.get(anyString(), any(), anyInt(), anyInt(), anyString()))
                .thenThrow(new UnsupportedOperationException("test exception"));

        final Optional<Definition> exceptionThrowingDefinition = definitionService.findById(1);

        assertFalse(exceptionThrowingDefinition.isPresent());
        verify(restService).get(anyString(), any(), anyInt(), anyInt(), anyString());
    }

    @Test
    public void testFindById_IdIsZero() {
        final Optional<Definition> impossibleDefinition = definitionService.findById(0);

        assertFalse(impossibleDefinition.isPresent());
        verifyZeroInteractions(restService);
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
    public void testCreateDefinition_NullDefinition() {
        final Optional<Definition> nullDefinition = definitionService.createDefinition(null);

        assertFalse(nullDefinition.isPresent());
        verifyZeroInteractions(restService);
    }

    @Test
    public void testCreateDefinition_UnableToCreateDefinition() {
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
    public void testCreateDefinition_RestServiceThrowsException() {
        final Definition testDefinition = createDefinition();
        final String testDefinitionJson = new GsonBuilder().create().toJson(testDefinition);

        when(restService.post(anyString(), anyString(), any(), anyInt(), anyInt()))
                .thenThrow(new UnsupportedOperationException("test exception"));

        final Optional<Definition> createdDefinition = definitionService.createDefinition(testDefinition);

        assertNotNull(createdDefinition);
        assertFalse(createdDefinition.isPresent());
        verify(restService).post(anyString(), eq(testDefinitionJson), any(), anyInt(), anyInt());
    }

    @Test
    public void testUpdateDefinition() {
        when(restService.patch(anyString(), anyString(), any(), anyInt(), anyInt()))
                .thenReturn(Optional.of(createDefinition().setName("Test update")));

        final Definition testDefinition = new Definition();
        testDefinition.setName("Test update");
        testDefinition.setId(1);

        final Optional<Definition> updatedDefinition = definitionService.updateDefinition(testDefinition);

        assertTrue(updatedDefinition.isPresent());
        assertThat(updatedDefinition.get().getId(), is(1));
        assertThat(updatedDefinition.get().getName(), is(equalTo("Test update")));
        verify(restService).patch(anyString(), anyString(), any(), anyInt(), anyInt());
    }

    @Test
    public void testUpdateDefinition_nullDefinition() {
        final Optional<Definition> testUpdate = definitionService.updateDefinition(null);

        assertFalse(testUpdate.isPresent());
        verifyZeroInteractions(restService);
    }

    @Test
    public void testUpdateDefinition_IdIsZero() {
        final Definition impossibleDefinition = createDefinition();
        impossibleDefinition.setId(0);

        final Optional<Definition> testUpdate = definitionService.updateDefinition(impossibleDefinition);

        assertFalse(testUpdate.isPresent());
        verifyZeroInteractions(restService);
    }

    @Test
    public void testUpdateDefinition_RequestReturnsEmptyOptional() {
        when(restService.patch(anyString(), anyString(), any(), anyInt(), anyInt()))
                .thenReturn(Optional.empty());

        final Optional<Definition> nonExistentDefinition = definitionService.updateDefinition(createDefinition());

        assertFalse(nonExistentDefinition.isPresent());
        verify(restService).patch(anyString(), anyString(), any(), anyInt(), anyInt());
    }

    @Test
    public void testUpdateDefinition_ExceptionThrownWhenSendingRequest() {
        when(restService.patch(anyString(), anyString(), any(), anyInt(), anyInt()))
                .thenThrow(new UnsupportedOperationException("test exception"));

        final Optional<Definition> exceptionThrowingDefinition = definitionService.updateDefinition(createDefinition());

        assertFalse(exceptionThrowingDefinition.isPresent());
        verify(restService).patch(anyString(), anyString(), any(), anyInt(), anyInt());
    }

    @Test
    public void testDeleteDefinition() {
        when(restService.delete(anyString(), anyInt(), anyInt(), any()))
                .thenReturn(true);

        final boolean deleteIsSuccessful = definitionService.deleteDefinition(createDefinition());

        assertTrue(deleteIsSuccessful);
        verify(restService).delete(anyString(), anyInt(), anyInt(), any());
    }

    @Test
    public void testDeleteDefinition_NullDefinition() {
        final boolean deleteIsSuccessful = definitionService.deleteDefinition(null);

        assertFalse(deleteIsSuccessful);
        verifyZeroInteractions(restService);
    }

    @Test
    public void testDeleteDefinition_IdIsZero() {
        final Definition testDefinition = createDefinition();
        testDefinition.setId(0);

        final boolean deleteIsSuccessful = definitionService.deleteDefinition(testDefinition);

        assertFalse(deleteIsSuccessful);
        verifyZeroInteractions(restService);
    }

    @Test
    public void testDeleteDefinition_RequestReturnsFalse() {
        when(restService.delete(anyString(), anyInt(), anyInt(), any()))
                .thenReturn(false);

        final boolean deleteIsSuccessful = definitionService.deleteDefinition(createDefinition());

        assertFalse(deleteIsSuccessful);
        verify(restService).delete(anyString(), anyInt(), anyInt(), any());
    }

    @Test
    public void testDeleteDefinition_ExceptionThrownWhenSendingRequest() {
        when(restService.delete(anyString(), anyInt(), anyInt(), any()))
                .thenThrow(new UnsupportedOperationException("test exception"));

        final boolean deleteIsSuccessful = definitionService.deleteDefinition(createDefinition());

        assertFalse(deleteIsSuccessful);
        verify(restService).delete(anyString(), anyInt(), anyInt(), any());
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
        definition.setId(1);

        return definition;
    }
}