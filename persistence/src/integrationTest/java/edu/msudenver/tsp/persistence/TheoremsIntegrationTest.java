package edu.msudenver.tsp.persistence;

import edu.msudenver.tsp.persistence.dto.Theorem;
import edu.msudenver.tsp.persistence.dto.TheoremType;
import edu.msudenver.tsp.persistence.repository.TheoremRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PersistenceTestConfig.class)
public class TheoremsIntegrationTest {
    @Autowired private TheoremRepository theoremRepository;

    @Test
    public void testCRUDFunctionality() {
        final Theorem theorem = createTheorem();
        final Theorem savedTheorem = theoremRepository.save(theorem);

        assertNotNull(savedTheorem);
        assertEquals(Integer.valueOf(0), savedTheorem.getVersion());

        final int id = savedTheorem.getId();

        assertEquals("Test theorem", savedTheorem.getName());
        assertEquals("Test branch", savedTheorem.getBranch());
        assertTrue(savedTheorem.getProvenStatus());
        assertEquals(2, savedTheorem.getReferencedTheorems().size());
        assertEquals(2, savedTheorem.getReferencedDefinitions().size());
        assertEquals("test theorem 1", savedTheorem.getReferencedTheorems().get(0));
        assertEquals("test theorem 2", savedTheorem.getReferencedTheorems().get(1));
        assertEquals("test definition 1", savedTheorem.getReferencedDefinitions().get(0));
        assertEquals("test definition 2", savedTheorem.getReferencedDefinitions().get(1));

        savedTheorem.setBranch("Test Update");

        final Theorem updatedTheorem = theoremRepository.save(savedTheorem);

        assertNotNull(updatedTheorem);
        assertEquals(Integer.valueOf(0), updatedTheorem.getVersion());
        assertEquals("Test theorem", updatedTheorem.getName());
        assertEquals("Test Update", updatedTheorem.getBranch());
        assertTrue(updatedTheorem.getProvenStatus());
        assertEquals(2, updatedTheorem.getReferencedTheorems().size());
        assertEquals(2, updatedTheorem.getReferencedDefinitions().size());
        assertEquals("test theorem 1", updatedTheorem.getReferencedTheorems().get(0));
        assertEquals("test theorem 2", updatedTheorem.getReferencedTheorems().get(1));
        assertEquals("test definition 1", updatedTheorem.getReferencedDefinitions().get(0));
        assertEquals("test definition 2", updatedTheorem.getReferencedDefinitions().get(1));
        assertEquals(updatedTheorem.getId(), id);

        theoremRepository.delete(theorem);
        final Optional<Theorem> deletedTheorem = theoremRepository.findById(id);
        assertFalse(deletedTheorem.isPresent());
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
        theorem.setBranch("Test branch");
        theorem.setProvenStatus(true);
        theorem.setTheoremType(TheoremType.THEOREM);
        theorem.setReferencedTheorems(referencedTheoremsList);
        theorem.setReferencedDefinitions(referencedDefinitionsList);

        return theorem;
    }
}
