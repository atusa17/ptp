package edu.msudenver.tsp.persistence;

import edu.msudenver.tsp.persistence.dto.Definition;
import edu.msudenver.tsp.persistence.repository.DefinitionRepository;
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
public class DefinitionsIntegrationTest {
    @Autowired private DefinitionRepository definitionRepository;

    @Test
    public void testCRUDFunctionality() {
        final Definition definition = createDefinition();
        final Definition savedDefinition = definitionRepository.save(definition);

        assertNotNull(savedDefinition);
        assertEquals(Integer.valueOf(0), savedDefinition.getVersion());

        final int id = savedDefinition.getId();

        assertEquals("Test Name", savedDefinition.getName());
        assertNotNull(savedDefinition.getDefinition());
        assertNotNull(savedDefinition.getNotation());

        final List<String> definitionsList = savedDefinition.getDefinition();
        final List<String> notationList = savedDefinition.getNotation();

        assertEquals(2, definitionsList.size());
        assertEquals(1, notationList.size());
        assertEquals("Test definition 1", definitionsList.get(0));
        assertEquals("Test definition 2", definitionsList.get(1));
        assertEquals("\\testLaTeX", notationList.get(0));

        savedDefinition.setName("Test Update");

        final Definition updatedDefinition = definitionRepository.save(savedDefinition);

        assertEquals("Test Update", updatedDefinition.getName());
        assertNotNull(updatedDefinition.getDefinition());
        assertNotNull(updatedDefinition.getNotation());

        final List<String> updatedDefinitionsList = updatedDefinition.getDefinition();
        final List<String> updatedNotationsList = updatedDefinition.getNotation();

        assertEquals(2, updatedDefinitionsList.size());
        assertEquals(1, updatedNotationsList.size());
        assertEquals("Test definition 1", updatedDefinitionsList.get(0));
        assertEquals("Test definition 2", updatedDefinitionsList.get(1));
        assertEquals("\\testLaTeX", updatedNotationsList.get(0));
        assertEquals(id, updatedDefinition.getId());

        definitionRepository.delete(updatedDefinition);
        final Optional<Definition> deletedDefinition = definitionRepository.findById(id);
        assertFalse(deletedDefinition.isPresent());
    }

    private Definition createDefinition() {
        final List<String> definitionList = new ArrayList<>();
        definitionList.add("Test definition 1");
        definitionList.add("Test definition 2");

        final List<String> notationList = new ArrayList<>();
        notationList.add("\\testLaTeX");

        final Definition definition = new Definition();
        definition.setName("Test Name");
        definition.setDefinition(definitionList);
        definition.setNotation(notationList);

        return definition;
    }
}
