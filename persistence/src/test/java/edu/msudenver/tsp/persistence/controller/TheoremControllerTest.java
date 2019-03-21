package edu.msudenver.tsp.persistence.controller;

import edu.msudenver.tsp.persistence.dto.Theorem;
import edu.msudenver.tsp.persistence.dto.TheoremType;
import edu.msudenver.tsp.persistence.repository.TheoremRepository;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(controllers = TheoremController.class)
public class TheoremControllerTest {
   @Mock private TheoremRepository theoremRepository;
   @InjectMocks private TheoremController theoremController;
   @Mock private BindingResult bindingResult;

   @Test
   public void testGetAllTheorems() {
       final Theorem theoremDto = createTheorem();
       final List<Theorem> listOfTheorems = new ArrayList<>();
       listOfTheorems.add(theoremDto);
       listOfTheorems.add(theoremDto);

       when(theoremRepository.findAll()).thenReturn(listOfTheorems);

       final ResponseEntity<Iterable<Theorem>> responseEntity = theoremController.getAllTheorems();

       assertNotNull(responseEntity);
       assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
       assertTrue(responseEntity.hasBody());
       assertNotNull(responseEntity.getBody());

       responseEntity.getBody().forEach(theorem -> assertEquals(theoremDto, theorem));
   }

    @Test
    public void testGetTheoremById() {
        final Theorem theorem = createTheorem();
        when(theoremRepository.findById(anyInt())).thenReturn(Optional.ofNullable(theorem));

        final ResponseEntity<Theorem> responseEntity = theoremController.getTheoremById(1);

        assertNotNull(responseEntity);
        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(theorem, responseEntity.getBody());
        verify(theoremRepository).findById(anyInt());
    }

    @Test
    public void testGetTheoremById_nullId() {
        final ResponseEntity responseEntity = theoremController.getTheoremById(null);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verifyZeroInteractions(theoremRepository);
    }

    @Test
    public void testGetTheoremById_noTheoremFound() {
        when(theoremRepository.findById(anyInt())).thenReturn(Optional.empty());

        final ResponseEntity responseEntity = theoremController.getTheoremById(1);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        verify(theoremRepository).findById(anyInt());
    }

    @Test
    public void testGetAllTheoremsByBranch() {
        final Theorem theoremDto = createTheorem();
        final List<Theorem> listOfTheorems = new ArrayList<>();
        listOfTheorems.add(theoremDto);
        listOfTheorems.add(theoremDto);

        when(theoremRepository.findByBranch(anyString())).thenReturn(listOfTheorems);

        final ResponseEntity<List<Theorem>> responseEntity = theoremController.getAllTheoremsByBranch("test-branch");

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        responseEntity.getBody().forEach(theorem -> assertEquals(theoremDto, theorem));
    }

    @Test
    public void testGetAllTheoremsByBranch_nullBranch() {
        final ResponseEntity<List<Theorem>> responseEntity = theoremController.getAllTheoremsByBranch(null);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verifyZeroInteractions(theoremRepository);
    }

    @Test
    public void testGetAllTheoremsByBranch_noTheoremsFound() {
       when(theoremRepository.findByBranch(anyString())).thenReturn(Collections.emptyList());

       final ResponseEntity<List<Theorem>> responseEntity = theoremController.getAllTheoremsByBranch("test_nonexistent_branch");

       assertNotNull(responseEntity);
       assertFalse(responseEntity.hasBody());
       assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testGetAllTheoremsByProvenStatus() {
        final Theorem theoremDto = createTheorem();
        final List<Theorem> listOfTheorems = new ArrayList<>();
        listOfTheorems.add(theoremDto);
        listOfTheorems.add(theoremDto);

        when(theoremRepository.findByProvenStatus(anyBoolean())).thenReturn(listOfTheorems);

        final ResponseEntity<List<Theorem>> responseEntity = theoremController.getAllTheoremsByProvenStatus("true");

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        responseEntity.getBody().forEach(theorem -> assertEquals(theoremDto, theorem));
    }

    @Test
    public void testGetAllTheoremsByProvenStatus_nullProvenStatus() {
        final ResponseEntity<List<Theorem>> responseEntity = theoremController.getAllTheoremsByProvenStatus(null);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verifyZeroInteractions(theoremRepository);
    }

    @Test
    public void testGetAllTheoremsByProvenStatus_invalidProvenStatus() {
        final ResponseEntity<List<Theorem>> responseEntity = theoremController.getAllTheoremsByProvenStatus("test");

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testGetAllTheoremsByProvenStatus_noTheoremsFound() {
        when(theoremRepository.findByProvenStatus(anyBoolean())).thenReturn(Collections.emptyList());

        final ResponseEntity<List<Theorem>> responseEntity = theoremController.getAllTheoremsByProvenStatus("false");

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testGetAllTheoremsByName() {
        final Theorem theoremDto = createTheorem();
        final List<Theorem> listOfTheorems = new ArrayList<>();
        listOfTheorems.add(theoremDto);
        listOfTheorems.add(theoremDto);

        when(theoremRepository.findByName(anyString())).thenReturn(listOfTheorems);

        final ResponseEntity<List<Theorem>> responseEntity = theoremController.getAllTheoremsByName("Test Theorem");

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        responseEntity.getBody().forEach(theorem -> assertEquals(theoremDto, theorem));
    }

    @Test
    public void testGetAllTheoremsByName_nullName() {
        final ResponseEntity<List<Theorem>> responseEntity = theoremController.getAllTheoremsByName(null);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verifyZeroInteractions(theoremRepository);
    }

    @Test
    public void testGetAllTheoremsByName_noNameFound() {
        when(theoremRepository.findByName(anyString())).thenReturn(Collections.emptyList());

        final ResponseEntity<List<Theorem>> responseEntity = theoremController.getAllTheoremsByName("No name");

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testInsertTheorem() {
        final Theorem theorem = createTheorem();
        when(theoremRepository.save(any(Theorem.class))).thenReturn(theorem);

        final ResponseEntity<Theorem> responseEntity = theoremController.insertTheorem(theorem, bindingResult);

        assertNotNull(responseEntity);
        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(theorem, responseEntity.getBody());
        verify(theoremRepository).save(any(Theorem.class));
    }

    @Test
    public void testInsertTheorem_theoremDtoIsNull() {
        final ResponseEntity responseEntity = theoremController.insertTheorem(null, bindingResult);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verifyZeroInteractions(theoremRepository);
    }

    @Test
    public void testInsertTheorem_bindingResultHasErrors() {
        final Theorem theorem = createTheorem();
        when(bindingResult.hasErrors()).thenReturn(true);

        final ResponseEntity responseEntity = theoremController.insertTheorem(theorem, bindingResult);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
        verifyZeroInteractions(theoremRepository);
    }

    @Test
    public void testUpdateTheorem() {
        final Theorem existingTheorem = createTheorem();
        existingTheorem.setId(1);
        existingTheorem.setVersion(1);
        final Theorem theoremUpdate = new Theorem();
        theoremUpdate.setName("Test Update");
        final Theorem updatedTheorem = existingTheorem;
        updatedTheorem.setName("Test Update");
        when(theoremRepository.findById(anyInt())).thenReturn(Optional.of(existingTheorem));
        when(theoremRepository.save(any(Theorem.class))).thenReturn(updatedTheorem);

        final ResponseEntity<Theorem> responseEntity = theoremController.updateTheorem(1, theoremUpdate, bindingResult);

        assertNotNull(responseEntity);
        assertTrue(responseEntity.hasBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedTheorem, responseEntity.getBody());
        verify(theoremRepository).findById(anyInt());
        verify(theoremRepository).save(any(Theorem.class));
    }

    @Test
    public void testUpdateTheorem_bindingResultHasErrors() {
        when(bindingResult.hasErrors()).thenReturn(true);

        final ResponseEntity<Theorem> responseEntity = theoremController.updateTheorem(1, createTheorem(), bindingResult);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
        verifyZeroInteractions(theoremRepository);
    }

    @Test
    public void testUpdateTheorem_theoremDtoIsNull() {
        final ResponseEntity<Theorem> responseEntity = theoremController.updateTheorem(1, null, bindingResult);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verifyZeroInteractions(theoremRepository);
    }

    @Test
    public void testUpdateTheorem_idIsNull() {
        final ResponseEntity<Theorem> responseEntity = theoremController.updateTheorem(null, createTheorem(), bindingResult);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verifyZeroInteractions(theoremRepository);
    }

    @Test
    public void testUpdateTheorem_theoremDoesNotExist() {
        when(theoremRepository.findById(anyInt())).thenReturn(Optional.empty());

        final ResponseEntity<Theorem> responseEntity = theoremController.updateTheorem(1, createTheorem(), bindingResult);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verify(theoremRepository, times(0)).save(any(Theorem.class));
    }

    @Test
    public void testDeleteTheoremById() {
        doNothing().when(theoremRepository).deleteById(anyInt());

        final ResponseEntity responseEntity = theoremController.deleteTheoremById(1);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(theoremRepository).deleteById(anyInt());
    }

    @Test
    public void testDeleteTheoremById_idIsNull() {
        final ResponseEntity responseEntity = theoremController.deleteTheoremById(null);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verifyZeroInteractions(theoremRepository);
    }

   private Theorem createTheorem() {
       final List<String> referencedTheoremsList = new ArrayList<>();
       referencedTheoremsList.add("test theorem 1");
       referencedTheoremsList.add("test theorem 2");

       final List<String> referencedDefinitionsList = new ArrayList<>();
       referencedDefinitionsList.add("test definition 1");
       referencedDefinitionsList.add("test definition 2");

       final Theorem theorem = new Theorem();
       theorem.setName("Test theorem");
       theorem.setTheorem("test theorem thing here");
       theorem.setBranch("Test branch");
       theorem.setProvenStatus(true);
       theorem.setTheoremType(TheoremType.THEOREM);
       theorem.setReferencedTheorems(referencedTheoremsList);
       theorem.setReferencedDefinitions(referencedDefinitionsList);

       return theorem;
   }
}