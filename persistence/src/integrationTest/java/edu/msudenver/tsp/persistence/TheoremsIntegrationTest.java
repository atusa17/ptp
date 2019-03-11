package edu.msudenver.tsp.persistence;

import edu.msudenver.tsp.persistence.dto.TheoremDto;
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
        final TheoremDto theoremDto = createTheorem();
        final TheoremDto savedTheorem = theoremRepository.save(theoremDto);

        assertNotNull(savedTheorem);
        assertEquals(Integer.valueOf(0), savedTheorem.getVersion());

        final int id = savedTheorem.getId();

        assertEquals("Test theorem", savedTheorem.getName());
        assertEquals("Test branch", savedTheorem.getBranch());
        assertTrue(savedTheorem.isProvenStatus());
        assertEquals(2, savedTheorem.getReferencedTheorems().size());
        assertEquals(2, savedTheorem.getReferencedDefinitions().size());
        assertEquals("test theorem 1", savedTheorem.getReferencedTheorems().get(0));
        assertEquals("test theorem 2", savedTheorem.getReferencedTheorems().get(1));
        assertEquals("test definition 1", savedTheorem.getReferencedDefinitions().get(0));
        assertEquals("test definition 2", savedTheorem.getReferencedDefinitions().get(1));

        savedTheorem.setBranch("Test Update");

        final TheoremDto updatedTheorem = theoremRepository.save(savedTheorem);

        assertNotNull(updatedTheorem);
        assertEquals(Integer.valueOf(0), updatedTheorem.getVersion());
        assertEquals("Test theorem", updatedTheorem.getName());
        assertEquals("Test Update", updatedTheorem.getBranch());
        assertTrue(updatedTheorem.isProvenStatus());
        assertEquals(2, updatedTheorem.getReferencedTheorems().size());
        assertEquals(2, updatedTheorem.getReferencedDefinitions().size());
        assertEquals("test theorem 1", updatedTheorem.getReferencedTheorems().get(0));
        assertEquals("test theorem 2", updatedTheorem.getReferencedTheorems().get(1));
        assertEquals("test definition 1", updatedTheorem.getReferencedDefinitions().get(0));
        assertEquals("test definition 2", updatedTheorem.getReferencedDefinitions().get(1));
        assertEquals(updatedTheorem.getId(), id);

        final List<TheoremDto> listOfTheoremsByBranch = theoremRepository.findByBranch("Test Update");

        assertNotNull(listOfTheoremsByBranch);
        assertEquals(1, listOfTheoremsByBranch.size());
        assertEquals(updatedTheorem, listOfTheoremsByBranch.get(0));

        theoremRepository.delete(theoremDto);
        final Optional<TheoremDto> deletedTheorem = theoremRepository.findById(id);
        assertFalse(deletedTheorem.isPresent());
    }

    private TheoremDto createTheorem() {
        final List<String> referencedTheoremsList = new ArrayList<>();
        referencedTheoremsList.add("test theorem 1");
        referencedTheoremsList.add("test theorem 2");

        final List<String> referencedDefinitionsList = new ArrayList<>();
        referencedDefinitionsList.add("test definition 1");
        referencedDefinitionsList.add("test definition 2");

        final TheoremDto theoremDto = new TheoremDto();
        theoremDto.setName("Test theorem");
        theoremDto.setBranch("Test branch");
        theoremDto.setProvenStatus(true);
        theoremDto.setTheoremType(TheoremType.THEOREM);
        theoremDto.setReferencedTheorems(referencedTheoremsList);
        theoremDto.setReferencedDefinitions(referencedDefinitionsList);

        return theoremDto;
    }
}
