package edu.msudenver.tsp.persistence.controller;

import edu.msudenver.tsp.persistence.dto.Proof;
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
    public void testGetAllProofsByBranch() {
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

    @Test
    public void testGetAllProfsByBranch_nullBranch() {
        final ResponseEntity<List<Proof>> responseEntity = proofController.getAllProofsByBranch(null);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verifyZeroInteractions(proofRepository);
    }

    @Test
    public void testGetAllProofsByBranch_noProofsFound() {
        when(proofRepository.findByBranch(anyString())).thenReturn(Collections.emptyList());

        final ResponseEntity<List<Proof>> responseEntity = proofController.getAllProofsByBranch("test-nonexistent-branch");

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testGetAllProofsByTheoremName() {
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

    @Test
    public void testGetAllProofsByTheoremName_nullTheoremName() {
        final ResponseEntity<List<Proof>> responseEntity = proofController.getAllProofsByTheoremName(null);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verifyZeroInteractions(proofRepository);
    }

    @Test
    public void testGetAllProofsByTheoremName_noProofsFound() {
        when(proofRepository.findByTheoremName(anyString())).thenReturn(Collections.emptyList());

        final ResponseEntity<List<Proof>> responseEntity = proofController.getAllProofsByTheoremName("test-nonexistent-proof");

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testGetProofById() {
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

    @Test
    public void testGetProofById_nullId() {
        final ResponseEntity responseEntity = proofController.getProofById(null);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verifyZeroInteractions(proofRepository);
    }

    @Test
    public void testGetProofById_noProofFound() {
        when(proofRepository.findById(anyInt())).thenReturn(Optional.empty());

        final ResponseEntity responseEntity = proofController.getProofById(1);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        verify(proofRepository).findById(anyInt());
    }

    @Test
    public void testInsertProof() {
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

    @Test
    public void testInsertProof_proofDtoIsNull() {
        final ResponseEntity responseEntity = proofController.insertProof(null, bindingResult);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verifyZeroInteractions(proofRepository);
    }

    @Test
    public void testInsertProof_bindingResultHasErrors() {
        final Proof proof = createProof();
        when(bindingResult.hasErrors()).thenReturn(true);

        final ResponseEntity responseEntity = proofController.insertProof(proof, bindingResult);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
        verifyZeroInteractions(proofRepository);
    }

    @Test
    public void testUpdateProof() {
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

    @Test
    public void testUpdateProof_bindingResultHasErrors() {
        when(bindingResult.hasErrors()).thenReturn(true);

        final ResponseEntity<Proof> responseEntity = proofController.updateProof(1, createProof(), bindingResult);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
        verifyZeroInteractions(proofRepository);
    }

    @Test
    public void testUpdateProof_proofDtoIsNull() {
        final ResponseEntity<Proof> responseEntity = proofController.updateProof(1, null, bindingResult);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verifyZeroInteractions(proofRepository);
    }

    @Test
    public void testUpdateProof_idIsNull() {
        final ResponseEntity<Proof> responseEntity = proofController.updateProof(null, createProof(), bindingResult);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verifyZeroInteractions(proofRepository);
    }

    @Test
    public void testUpdateProof_theoremDoesNotExist() {
        when(proofRepository.findById(anyInt())).thenReturn(Optional.empty());

        final ResponseEntity<Proof> responseEntity = proofController.updateProof(1, createProof(), bindingResult);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verify(proofRepository, times(0)).save(any(Proof.class));
    }

    @Test
    public void testDeleteProofById() {
        doNothing().when(proofRepository).deleteById(anyInt());

        final ResponseEntity responseEntity = proofController.deleteProofById(1);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(proofRepository).deleteById(anyInt());
    }

    @Test
    public void testDeleteProofById_idIsNull() {
        final ResponseEntity responseEntity = proofController.deleteProofById(null);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verifyZeroInteractions(proofRepository);
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
        proof.setBranch("Test branch");
        proof.setProof("test proof");
        proof.setDateCreated(new Date());
        proof.setReferencedTheorems(referencedTheoremsList);
        proof.setReferencedDefinitions(referencedDefinitionsList);

        return proof;
    }
}