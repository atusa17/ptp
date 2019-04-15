package edu.msudenver.tsp.persistence.controller;

import edu.msudenver.tsp.persistence.dto.Definition;
import edu.msudenver.tsp.persistence.exception.BadRequestException;
import edu.msudenver.tsp.persistence.exception.UnprocessableEntityException;
import edu.msudenver.tsp.persistence.repository.DefinitionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(controllers = DefinitionController.class)
public class DefinitionControllerTest {
    @Mock private DefinitionRepository definitionRepository;
    @InjectMocks private DefinitionController definitionController;
    @Mock private BindingResult bindingResult;

    @Test
    public void testGetAllDefinitions() {
        final Definition definitionDto = createDefinition();
        final List<Definition> definitionList = new ArrayList<>();
        definitionList.add(definitionDto);
        definitionList.add(definitionDto);

        when(definitionRepository.findAll()).thenReturn(definitionList);

        final ResponseEntity<Iterable<Definition>> responseEntity = definitionController.getAllDefinitions();

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        responseEntity.getBody().forEach(definition -> assertEquals(definitionDto, definition));
    }

    @Test
    public void testGetDefinitionsById() throws BadRequestException {
        final Definition definition = createDefinition();
        when(definitionRepository.findById(anyInt())).thenReturn(Optional.ofNullable(definition));

        final ResponseEntity<Definition> responseEntity = definitionController.getDefinitionById(1);

        assertNotNull(responseEntity);
        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(definition, responseEntity.getBody());
        verify(definitionRepository).findById(anyInt());
    }

    @Test(expected = BadRequestException.class)
    public void testGetDefinitionById_nullId() throws BadRequestException {
        definitionController.getDefinitionById(null);
    }

    @Test
    public void testGetDefinitionById_noDefinitionFound() throws BadRequestException {
        when(definitionRepository.findById(anyInt())).thenReturn(Optional.empty());

        final ResponseEntity responseEntity = definitionController.getDefinitionById(1);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        verify(definitionRepository).findById(anyInt());
    }

    @Test
    public void testInsertDefinition() throws BadRequestException, UnprocessableEntityException {
        final Definition definition = createDefinition();
        when(definitionRepository.save(any(Definition.class))).thenReturn(definition);

        final ResponseEntity<Definition> responseEntity = definitionController.insertDefinition(definition, bindingResult);

        assertNotNull(responseEntity);
        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(definition, responseEntity.getBody());
        verify(definitionRepository).save(any(Definition.class));
    }

    @Test(expected = BadRequestException.class)
    public void testInsertDefinition_definitionDtoIsNull() throws BadRequestException, UnprocessableEntityException {
        definitionController.insertDefinition(null, bindingResult);
    }

    @Test(expected = UnprocessableEntityException.class)
    public void testInsertDefinition_bindingResultHasErrors() throws BadRequestException, UnprocessableEntityException {
        final Definition definition = createDefinition();
        when(bindingResult.hasErrors()).thenReturn(true);

        definitionController.insertDefinition(definition, bindingResult);
    }

    @Test
    public void testUpdateDefinition() throws BadRequestException, UnprocessableEntityException {
        final Definition existingDefinition = createDefinition();
        existingDefinition.setId(1);
        existingDefinition.setVersion(1);
        final Definition definitionUpdate = new Definition();
        definitionUpdate.setName("Test Update");
        final Definition updatedDefinition = existingDefinition;
        updatedDefinition.setName("Test Update");
        when(definitionRepository.findById(anyInt())).thenReturn(Optional.of(existingDefinition));
        when(definitionRepository.save(any(Definition.class))).thenReturn(updatedDefinition);

        final ResponseEntity<Definition> responseEntity = definitionController.updateDefinition(1, definitionUpdate, bindingResult);

        assertNotNull(responseEntity);
        assertTrue(responseEntity.hasBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedDefinition, responseEntity.getBody());
        verify(definitionRepository).findById(anyInt());
        verify(definitionRepository).save(any(Definition.class));
    }

    @Test(expected = UnprocessableEntityException.class)
    public void testUpdateDefinition_bindingResultErrors() throws BadRequestException, UnprocessableEntityException {
        when(bindingResult.hasErrors()).thenReturn(true);

        definitionController.updateDefinition(1, createDefinition(), bindingResult);
    }

    @Test(expected = BadRequestException.class)
    public void testUpdateDefinition_definitionDtoIsNull() throws BadRequestException, UnprocessableEntityException {
        definitionController.updateDefinition(1, null, bindingResult);
    }

    @Test(expected = BadRequestException.class)
    public void testUpdateDefinition_idIsNull() throws BadRequestException, UnprocessableEntityException {
        definitionController.updateDefinition(null, createDefinition(), bindingResult);
    }

    @Test
    public void testUpdateDefinition_definitionDoesntExist() throws BadRequestException, UnprocessableEntityException {
        when(definitionRepository.findById(anyInt())).thenReturn(Optional.empty());

        final ResponseEntity<Definition> responseEntity = definitionController.updateDefinition(1, createDefinition(), bindingResult);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        verify(definitionRepository, times(0)).save(any(Definition.class));
    }

    @Test
    public void testDeleteDefinitionById() throws BadRequestException {
        doNothing().when(definitionRepository).deleteById(anyInt());

        final ResponseEntity responseEntity = definitionController.deleteDefinitionById(1);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(definitionRepository).deleteById(anyInt());
    }

    @Test(expected = BadRequestException.class)
    public void testDeleteDefinitionById_nullId() throws BadRequestException {
        definitionController.deleteDefinitionById(null);
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