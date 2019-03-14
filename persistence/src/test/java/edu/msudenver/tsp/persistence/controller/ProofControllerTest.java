package edu.msudenver.tsp.persistence.controller;

import edu.msudenver.tsp.persistence.dto.ProofDto;
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
    public void testGetAllTheorems() {
        final ProofDto proofDto = createProof();
        final List<ProofDto> listOfProofs = new ArrayList<>();
        listOfProofs.add(proofDto);
        listOfProofs.add(proofDto);

        when(proofRepository.findAll()).thenReturn(listOfProofs);

        final ResponseEntity<Iterable<ProofDto>> responseEntity = proofController.getAllProofs();

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        responseEntity.getBody().forEach(proof -> assertEquals(proofDto, proof));
    }

    @Test
    public void testGetAllProofsByBranch() {
        final ProofDto proofDto = createProof();
        final List<ProofDto> listOfProofs = new ArrayList<>();
        listOfProofs.add(proofDto);
        listOfProofs.add(proofDto);

        when(proofRepository.findByBranch(anyString())).thenReturn(listOfProofs);

        final ResponseEntity<List<ProofDto>> responseEntity = proofController.getAllProofsByBranch("test");

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        responseEntity.getBody().forEach(proof -> assertEquals(proofDto, proof));
    }

    @Test
    public void testGetAllProfsByBranch_nullBranch() {
        final ResponseEntity<List<ProofDto>> responseEntity = proofController.getAllProofsByBranch(null);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verifyZeroInteractions(proofRepository);
    }

    @Test
    public void testGetAllProofsByBranch_noProofsFound() {
        when(proofRepository.findByBranch(anyString())).thenReturn(Collections.emptyList());

        final ResponseEntity<List<ProofDto>> responseEntity = proofController.getAllProofsByBranch("test nonexistent branch");

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testGetProofById() {
        final ProofDto proofDto = createProof();
        when(proofRepository.findById(anyInt())).thenReturn(Optional.ofNullable(proofDto));

        final ResponseEntity<ProofDto> responseEntity = proofController.getProofById(1);

        assertNotNull(responseEntity);
        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(proofDto, responseEntity.getBody());
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

    private ProofDto createProof() {
        final List<String> referencedTheoremsList = new ArrayList<>();
        referencedTheoremsList.add("test theorem 1");
        referencedTheoremsList.add("test theorem 2");

        final List<String> referencedDefinitionsList = new ArrayList<>();
        referencedDefinitionsList.add("test definition 1");
        referencedDefinitionsList.add("test definition 2");

        final ProofDto proofDto = new ProofDto();
        proofDto.setName("Test proof");
        proofDto.setBranch("Test branch");
        proofDto.setDateCreated(new Date());
        proofDto.setReferencedTheorems(referencedTheoremsList);
        proofDto.setReferencedDefinitions(referencedDefinitionsList);

        return proofDto;
    }
}