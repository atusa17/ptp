package edu.msudenver.tsp.persistence.controller;

import edu.msudenver.tsp.persistence.dto.Proof;
import edu.msudenver.tsp.persistence.exception.BadRequestException;
import edu.msudenver.tsp.persistence.exception.UnprocessableEntityException;
import edu.msudenver.tsp.persistence.repository.ProofRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.*;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProofControllerTest {
    @Mock private ProofRepository proofRepository;
    @InjectMocks private ProofController proofController;
    @Mock private BindingResult bindingResult;

    @Test
    public void testGetAllProofs() {
        final Proof proofDto = createProof();
        final List<Proof> listOfProofs = new ArrayList<>();
        listOfProofs.add(proofDto);
        listOfProofs.add(proofDto);

        when(proofRepository.findAll()).thenReturn(listOfProofs);

        final ResponseEntity<Iterable<Proof>> responseEntity = proofController.getAllProofs();

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        responseEntity.getBody().forEach(proof -> assertEquals(proofDto, proof));
    }

    @Test
    public void testGetAllProofsByBranch() throws BadRequestException {
        final Proof proofDto = createProof();
        final List<Proof> listOfProofs = new ArrayList<>();
        listOfProofs.add(proofDto);
        listOfProofs.add(proofDto);

        when(proofRepository.findByBranch(anyString())).thenReturn(listOfProofs);

        final ResponseEntity<List<Proof>> responseEntity = proofController.getAllProofsByBranch("Test_branch");

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        responseEntity.getBody().forEach(proof -> assertEquals(proofDto, proof));
    }

    @Test(expected = BadRequestException.class)
    public void testGetAllProfsByBranch_nullBranch() throws BadRequestException {
        proofController.getAllProofsByBranch(null);
    }

    @Test
    public void testGetAllProofsByBranch_noProofsFound() throws BadRequestException {
        when(proofRepository.findByBranch(anyString())).thenReturn(Collections.emptyList());

        final ResponseEntity<List<Proof>> responseEntity = proofController.getAllProofsByBranch("test-nonexistent-branch");

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testGetAllProofsByTheoremName() throws BadRequestException {
        final Proof proofDto = createProof();
        final List<Proof> listOfProofs = new ArrayList<>();
        listOfProofs.add(proofDto);
        listOfProofs.add(proofDto);

        when(proofRepository.findByTheoremName(anyString())).thenReturn(listOfProofs);

        final ResponseEntity<List<Proof>> responseEntity = proofController.getAllProofsByTheoremName("Test_proof");

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        responseEntity.getBody().forEach(proof -> assertEquals(proofDto, proof));
    }

    @Test(expected = BadRequestException.class)
    public void testGetAllProofsByTheoremName_nullTheoremName() throws BadRequestException {
        proofController.getAllProofsByTheoremName(null);
    }

    @Test
    public void testGetAllProofsByTheoremName_noProofsFound() throws BadRequestException {
        when(proofRepository.findByTheoremName(anyString())).thenReturn(Collections.emptyList());

        final ResponseEntity<List<Proof>> responseEntity = proofController.getAllProofsByTheoremName("test-nonexistent-proof");

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testGetProofById() throws BadRequestException {
        final Proof proof = createProof();
        when(proofRepository.findById(anyInt())).thenReturn(Optional.ofNullable(proof));

        final ResponseEntity<Proof> responseEntity = proofController.getProofById(1);

        assertNotNull(responseEntity);
        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(proof, responseEntity.getBody());
        verify(proofRepository).findById(anyInt());
    }

    @Test(expected = BadRequestException.class)
    public void testGetProofById_nullId() throws BadRequestException {
        proofController.getProofById(null);
    }

    @Test
    public void testGetProofById_noProofFound() throws BadRequestException {
        when(proofRepository.findById(anyInt())).thenReturn(Optional.empty());

        final ResponseEntity responseEntity = proofController.getProofById(1);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        verify(proofRepository).findById(anyInt());
    }

    @Test
    public void testInsertProof() throws BadRequestException, UnprocessableEntityException {
        final Proof proof = createProof();
        when(proofRepository.save(any(Proof.class))).thenReturn(proof);

        final ResponseEntity<Proof> responseEntity = proofController.insertProof(proof, bindingResult);

        assertNotNull(responseEntity);
        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(proof, responseEntity.getBody());
        verify(proofRepository).save(any(Proof.class));
    }

    @Test(expected = BadRequestException.class)
    public void testInsertProof_proofDtoIsNull() throws BadRequestException, UnprocessableEntityException {
        proofController.insertProof(null, bindingResult);
    }

    @Test(expected = UnprocessableEntityException.class)
    public void testInsertProof_bindingResultHasErrors() throws BadRequestException, UnprocessableEntityException {
        final Proof proof = createProof();
        when(bindingResult.hasErrors()).thenReturn(true);

        proofController.insertProof(proof, bindingResult);
    }

    @Test
    public void testUpdateProof() throws BadRequestException, UnprocessableEntityException {
        final Proof existingProof = createProof();
        existingProof.setId(1);
        existingProof.setVersion(1);
        final Proof proofUpdate = new Proof();
        proofUpdate.setTheoremName("Test Update");
        final Proof updatedProof = existingProof;
        updatedProof.setTheoremName("Test Update");
        when(proofRepository.findById(anyInt())).thenReturn(Optional.of(existingProof));
        when(proofRepository.save(any(Proof.class))).thenReturn(updatedProof);

        final ResponseEntity<Proof> responseEntity = proofController.updateProof(1, proofUpdate, bindingResult);

        assertNotNull(responseEntity);
        assertTrue(responseEntity.hasBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedProof, responseEntity.getBody());
        verify(proofRepository).findById(anyInt());
        verify(proofRepository).save(any(Proof.class));
    }

    @Test(expected = UnprocessableEntityException.class)
    public void testUpdateProof_bindingResultHasErrors() throws BadRequestException, UnprocessableEntityException {
        when(bindingResult.hasErrors()).thenReturn(true);

        proofController.updateProof(1, createProof(), bindingResult);
    }

    @Test(expected = BadRequestException.class)
    public void testUpdateProof_proofDtoIsNull() throws BadRequestException, UnprocessableEntityException {
        proofController.updateProof(1, null, bindingResult);
    }

    @Test(expected = BadRequestException.class)
    public void testUpdateProof_idIsNull() throws BadRequestException, UnprocessableEntityException {
        proofController.updateProof(null, createProof(), bindingResult);
    }

    @Test
    public void testUpdateProof_theoremDoesNotExist() throws BadRequestException, UnprocessableEntityException {
        when(proofRepository.findById(anyInt())).thenReturn(Optional.empty());

        final ResponseEntity<Proof> responseEntity = proofController.updateProof(1, createProof(), bindingResult);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        verify(proofRepository, times(0)).save(any(Proof.class));
    }

    @Test
    public void testDeleteProofById() throws BadRequestException {
        doNothing().when(proofRepository).deleteById(anyInt());

        final ResponseEntity responseEntity = proofController.deleteProofById(1);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(proofRepository).deleteById(anyInt());
    }

    @Test(expected = BadRequestException.class)
    public void testDeleteProofById_idIsNull() throws BadRequestException {
        proofController.deleteProofById(null);
    }

    private Proof createProof() {
        final List<String> referencedTheoremsList = new ArrayList<>();
        referencedTheoremsList.add("test theorem 1");
        referencedTheoremsList.add("test theorem 2");

        final List<String> referencedDefinitionsList = new ArrayList<>();
        referencedDefinitionsList.add("test definition 1");
        referencedDefinitionsList.add("test definition 2");

        final Proof proof = new Proof();
        proof.setTheoremName("Test proof");
        proof.setTheorem(1);
        proof.setBranch("Test branch");
        proof.setProof("test proof");
        proof.setDateCreated(new Date());
        proof.setReferencedTheorems(referencedTheoremsList);
        proof.setReferencedDefinitions(referencedDefinitionsList);

        return proof;
    }
}