package edu.msudenver.tsp.persistence.controller;

import edu.msudenver.tsp.persistence.dto.Theorem;
import edu.msudenver.tsp.persistence.dto.TheoremType;
import edu.msudenver.tsp.persistence.exception.BadRequestException;
import edu.msudenver.tsp.persistence.exception.UnprocessableEntityException;
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
    public void testGetTheoremById() throws BadRequestException {
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

    @Test(expected = BadRequestException.class)
    public void testGetTheoremById_nullId() throws BadRequestException {
        theoremController.getTheoremById(null);
    }

    @Test
    public void testGetTheoremById_noTheoremFound() throws BadRequestException {
        when(theoremRepository.findById(anyInt())).thenReturn(Optional.empty());

        final ResponseEntity responseEntity = theoremController.getTheoremById(1);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        verify(theoremRepository).findById(anyInt());
    }

    @Test
    public void testGetAllTheoremsByBranch() throws BadRequestException {
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

    @Test(expected = BadRequestException.class)
    public void testGetAllTheoremsByBranch_nullBranch() throws BadRequestException {
        theoremController.getAllTheoremsByBranch(null);
    }

    @Test
    public void testGetAllTheoremsByBranch_noTheoremsFound() throws BadRequestException {
       when(theoremRepository.findByBranch(anyString())).thenReturn(Collections.emptyList());

       final ResponseEntity<List<Theorem>> responseEntity = theoremController.getAllTheoremsByBranch("test_nonexistent_branch");

       assertNotNull(responseEntity);
       assertFalse(responseEntity.hasBody());
       assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testGetAllTheoremsByProvenStatus() throws BadRequestException {
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

    @Test(expected = BadRequestException.class)
    public void testGetAllTheoremsByProvenStatus_nullProvenStatus() throws BadRequestException {
        theoremController.getAllTheoremsByProvenStatus(null);
    }

    @Test
    public void testGetAllTheoremsByProvenStatus_invalidProvenStatus() throws BadRequestException {
        final ResponseEntity<List<Theorem>> responseEntity = theoremController.getAllTheoremsByProvenStatus("test");

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testGetAllTheoremsByProvenStatus_noTheoremsFound() throws BadRequestException {
        when(theoremRepository.findByProvenStatus(anyBoolean())).thenReturn(Collections.emptyList());

        final ResponseEntity<List<Theorem>> responseEntity = theoremController.getAllTheoremsByProvenStatus("false");

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testGetAllTheoremsByName() throws BadRequestException {
        final Theorem theoremDto = createTheorem();
        final List<Theorem> listOfTheorems = new ArrayList<>();
        listOfTheorems.add(theoremDto);
        listOfTheorems.add(theoremDto);

        when(theoremRepository.findByName(anyString())).thenReturn(listOfTheorems);

        final ResponseEntity<List<Theorem>> responseEntity = theoremController.getAllTheoremsByName("Test_Theorem");

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        responseEntity.getBody().forEach(theorem -> assertEquals(theoremDto, theorem));
    }

    @Test(expected = BadRequestException.class)
    public void testGetAllTheoremsByName_nullName() throws BadRequestException {
        theoremController.getAllTheoremsByName(null);
    }

    @Test
    public void testGetAllTheoremsByName_noNameFound() throws BadRequestException {
        when(theoremRepository.findByName(anyString())).thenReturn(Collections.emptyList());

        final ResponseEntity<List<Theorem>> responseEntity = theoremController.getAllTheoremsByName("No-name");

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testInsertTheorem() throws BadRequestException, UnprocessableEntityException {
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

    @Test(expected = BadRequestException.class)
    public void testInsertTheorem_theoremDtoIsNull() throws BadRequestException, UnprocessableEntityException {
        theoremController.insertTheorem(null, bindingResult);
    }

    @Test(expected = UnprocessableEntityException.class)
    public void testInsertTheorem_bindingResultHasErrors() throws BadRequestException, UnprocessableEntityException {
        final Theorem theorem = createTheorem();
        when(bindingResult.hasErrors()).thenReturn(true);

        theoremController.insertTheorem(theorem, bindingResult);
    }

    @Test
    public void testUpdateTheorem() throws BadRequestException, UnprocessableEntityException {
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

    @Test(expected = UnprocessableEntityException.class)
    public void testUpdateTheorem_bindingResultHasErrors() throws BadRequestException, UnprocessableEntityException {
        when(bindingResult.hasErrors()).thenReturn(true);

        theoremController.updateTheorem(1, createTheorem(), bindingResult);
    }

    @Test(expected = BadRequestException.class)
    public void testUpdateTheorem_theoremDtoIsNull() throws BadRequestException, UnprocessableEntityException {
        theoremController.updateTheorem(1, null, bindingResult);
    }

    @Test(expected = BadRequestException.class)
    public void testUpdateTheorem_idIsNull() throws BadRequestException, UnprocessableEntityException {
        theoremController.updateTheorem(null, createTheorem(), bindingResult);
    }

    @Test
    public void testUpdateTheorem_theoremDoesNotExist() throws BadRequestException, UnprocessableEntityException {
        when(theoremRepository.findById(anyInt())).thenReturn(Optional.empty());

        final ResponseEntity<Theorem> responseEntity = theoremController.updateTheorem(1, createTheorem(), bindingResult);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        verify(theoremRepository, times(0)).save(any(Theorem.class));
    }

    @Test
    public void testDeleteTheoremById() throws BadRequestException {
        doNothing().when(theoremRepository).deleteById(anyInt());

        final ResponseEntity responseEntity = theoremController.deleteTheoremById(1);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(theoremRepository).deleteById(anyInt());
    }

    @Test(expected = BadRequestException.class)
    public void testDeleteTheoremById_idIsNull() throws BadRequestException {
        theoremController.deleteTheoremById(null);
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