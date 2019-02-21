package edu.msudenver.tsp.persistence;

import edu.msudenver.tsp.persistence.dto.Definition;
import edu.msudenver.tsp.persistence.dto.DefinitionDto;
import edu.msudenver.tsp.persistence.dto.Notation;
import edu.msudenver.tsp.persistence.repository.DefinitionRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class DefinitionsIntegrationTest {
    private final DefinitionRepository definitionRepository;

    @Autowired DefinitionsIntegrationTest(final DefinitionRepository definitionRepository) {
        this.definitionRepository = definitionRepository;
    }

    @Test
    public void testCRUDFunctionality() {
        // Create a new definition
        final DefinitionDto definitionDto = createDefinition();
        final DefinitionDto savedDefinition = definitionRepository.save(definitionDto);
    }

    private DefinitionDto createDefinition() {
        final List<String> definitionList = new ArrayList<>();
        definitionList.add("testDefinition1");

        final Definition definition = new Definition();
        definition.setDefinitions(definitionList);

        final List<String> notationList = new ArrayList<>();
        notationList.add("\\textLaTeX");

        final Notation notation = new Notation();
        notation.setNotations(notationList);

        final DefinitionDto definitionDto = new DefinitionDto();
        definitionDto.setDefinition(definition);
        definitionDto.setNotation(notation);

        return definitionDto;
    }
}
