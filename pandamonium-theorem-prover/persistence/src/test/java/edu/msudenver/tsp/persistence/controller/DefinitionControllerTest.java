package edu.msudenver.tsp.persistence.controller;

import edu.msudenver.tsp.persistence.dto.Definition;
import edu.msudenver.tsp.persistence.dto.DefinitionDto;
import edu.msudenver.tsp.persistence.dto.Notation;
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
        final DefinitionDto definitionDto = createDefinition();
        final List<DefinitionDto> definitionDtoList = new ArrayList<>();
        definitionDtoList.add(definitionDto);
        definitionDtoList.add(definitionDto);

        when(definitionRepository.findAll()).thenReturn(definitionDtoList);

        final ResponseEntity<Iterable<DefinitionDto>> responseEntity = definitionController.getAllDefinitions();

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        responseEntity.getBody().forEach(definition -> assertEquals(definition, definitionDto));
    }

    @Test
    public void testGetDefinitionsById() {
        final DefinitionDto definitionDto = createDefinition();
        when(definitionRepository.findById(anyInt())).thenReturn(Optional.ofNullable(definitionDto));

        final ResponseEntity<DefinitionDto> responseEntity = definitionController.getDefinitionById(1);

        assertNotNull(responseEntity);
        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(definitionDto, responseEntity.getBody());
        verify(definitionRepository).findById(anyInt());
    }

    @Test
    public void testGetDefinitionById_nullId() {
        final ResponseEntity responseEntity = definitionController.getDefinitionById(null);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verifyZeroInteractions(definitionRepository);
    }

    @Test
    public void testGetDefinitionById_noDefinitionFound() {
        when(definitionRepository.findById(anyInt())).thenReturn(Optional.empty());

        final ResponseEntity responseEntity = definitionController.getDefinitionById(1);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        verify(definitionRepository).findById(anyInt());
    }

    @Test
    public void testInsertDefinition() {
        final DefinitionDto definitionDto = createDefinition();
        when(definitionRepository.save(any(DefinitionDto.class))).thenReturn(definitionDto);

        final ResponseEntity<DefinitionDto> responseEntity = definitionController.insertDefinition(definitionDto, bindingResult);

        assertNotNull(responseEntity);
        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(definitionDto, responseEntity.getBody());
        verify(definitionRepository).save(any(DefinitionDto.class));
    }

    @Test
    public void testInsertDefinition_definitionDtoIsNull() {
        final ResponseEntity responseEntity = definitionController.insertDefinition(null, bindingResult);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verifyZeroInteractions(definitionRepository);
    }

    @Test
    public void testInsertDefinition_bindingResultHasErrors() {
        final DefinitionDto definitionDto = createDefinition();
        when(bindingResult.hasErrors()).thenReturn(true);

        final ResponseEntity responseEntity = definitionController.insertDefinition(definitionDto, bindingResult);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
        verifyZeroInteractions(definitionRepository);
    }

    @Test
    public void testDeleteDefinitionById() {
        doNothing().when(definitionRepository).deleteById(anyInt());

        final ResponseEntity responseEntity = definitionController.deleteDefinitionById(1);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(definitionRepository).deleteById(anyInt());
    }

    @Test
    public void testDeleteDefinitionById_nullId() {
        final ResponseEntity responseEntity = definitionController.deleteDefinitionById(null);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verifyZeroInteractions(definitionRepository);
    }

    private DefinitionDto createDefinition() {
        final List<String> definitionList = new ArrayList<>();
        definitionList.add("Test definition 1");

        final Definition definition = new Definition();
        definition.setDefinitions(definitionList);

        final List<String> notationList = new ArrayList<>();
        notationList.add("\\testLaTeX");

        final Notation notation = new Notation();
        notation.setNotations(notationList);

        final DefinitionDto definitionDto = new DefinitionDto();
        definitionDto.setName("Test Name");
        definitionDto.setDefinition(definition);
        definitionDto.setNotation(notation);

        return definitionDto;
    }
}