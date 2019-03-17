package edu.msudenver.tsp.persistence;

import edu.msudenver.tsp.persistence.dto.ProofDto;
import edu.msudenver.tsp.persistence.repository.ProofRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PersistenceTestConfig.class)
public class ProofsIntegrationTest {
    @Autowired
    private ProofRepository proofRepository;

    @Test
    public void testCRUDFunctionality() {
        final ProofDto proofDto = createProof();
        final ProofDto savedProof = proofRepository.save(proofDto);

        assertNotNull(savedProof);
        assertEquals(Integer.valueOf(0), savedProof.getVersion());

        final int id = savedProof.getId();

        assertEquals("Test proof", savedProof.getTheoremName());
        assertEquals("Test branch", savedProof.getBranch());
        assertNotNull(savedProof.getDateCreated());
        assertNotNull(savedProof.getLastUpdated());
        assertEquals(2, savedProof.getReferencedTheorems().size());
        assertEquals(2, savedProof.getReferencedDefinitions().size());
        assertEquals("test theorem 1", savedProof.getReferencedTheorems().get(0));
        assertEquals("test theorem 2", savedProof.getReferencedTheorems().get(1));
        assertEquals("test definition 1", savedProof.getReferencedDefinitions().get(0));
        assertEquals("test definition 2", savedProof.getReferencedDefinitions().get(1));

        savedProof.setBranch("Test Update");

        final ProofDto updatedProof = proofRepository.save(savedProof);

        assertNotNull(updatedProof);
        assertEquals(Integer.valueOf(0), updatedProof.getVersion());
        assertEquals("Test proof", updatedProof.getTheoremName());
        assertEquals("Test Update", updatedProof.getBranch());
        assertNotNull(updatedProof.getLastUpdated());
        assertNotNull(updatedProof.getDateCreated());
        assertNotEquals(updatedProof.getDateCreated().toInstant(), updatedProof.getLastUpdated().toInstant());
        assertEquals(2, updatedProof.getReferencedTheorems().size());
        assertEquals(2, updatedProof.getReferencedDefinitions().size());
        assertEquals("test theorem 1", updatedProof.getReferencedTheorems().get(0));
        assertEquals("test theorem 2", updatedProof.getReferencedTheorems().get(1));
        assertEquals("test definition 1", updatedProof.getReferencedDefinitions().get(0));
        assertEquals("test definition 2", updatedProof.getReferencedDefinitions().get(1));
        assertEquals(updatedProof.getId(), id);

        proofRepository.delete(proofDto);
        final Optional<ProofDto> deletedProof = proofRepository.findById(id);
        assertFalse(deletedProof.isPresent());
    }

    private ProofDto createProof() {
        final List<String> referencedTheoremsList = new ArrayList<>();
        referencedTheoremsList.add("test theorem 1");
        referencedTheoremsList.add("test theorem 2");

        final List<String> referencedDefinitionsList = new ArrayList<>();
        referencedDefinitionsList.add("test definition 1");
        referencedDefinitionsList.add("test definition 2");

        final ProofDto proofDto = new ProofDto();
        proofDto.setTheoremName("Test proof");
        proofDto.setBranch("Test branch");
        proofDto.setDateCreated(new Date());
        proofDto.setReferencedTheorems(referencedTheoremsList);
        proofDto.setReferencedDefinitions(referencedDefinitionsList);

        return proofDto;
    }
}
