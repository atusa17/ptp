package edu.msudenver.tsp.persistence;

import edu.msudenver.tsp.persistence.dto.Definition;
import edu.msudenver.tsp.persistence.dto.DefinitionDto;
import edu.msudenver.tsp.persistence.dto.Notation;
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
        // Create a new definition
        final DefinitionDto definitionDto = createDefinition();
        final DefinitionDto savedDefinition = definitionRepository.save(definitionDto);

        assertNotNull(savedDefinition);
        assertEquals(Integer.valueOf(0), savedDefinition.getVersion());

        final int id = savedDefinition.getId();

        assertEquals("Test Name", savedDefinition.getName());
        assertNotNull(savedDefinition.getDefinition());
        assertNotNull(savedDefinition.getNotation());

        final List<String> definitionsList = savedDefinition.getDefinition().getDefinitions();
        final List<String> notationList = savedDefinition.getNotation().getNotations();

        assertEquals(2, definitionsList.size());
        assertEquals(1, notationList.size());
        assertEquals("Test definition 1", definitionsList.get(0));
        assertEquals("Test definition 2", definitionsList.get(1));
        assertEquals("\\testLaTeX", notationList.get(0));

        definitionRepository.delete(savedDefinition);
        final Optional<DefinitionDto> deletedDefinition = definitionRepository.findById(id);
        assertFalse(deletedDefinition.isPresent());
    }

    private DefinitionDto createDefinition() {
        final List<String> definitionList = new ArrayList<>();
        definitionList.add("Test definition 1");
        definitionList.add("Test definition 2");

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
