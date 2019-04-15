package edu.msudenver.tsp.services;

import edu.msudenver.tsp.services.dto.Definition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ServicesTestConfig.class)
@TestPropertySource(locations = "classpath:test.properties")
public class DefinitionServiceIntegrationTest {
    @Autowired private DefinitionService definitionService;

    @Test
    public void testCRUD() {
        final Definition testDefinition = createDefinition();
        final Optional<Definition> createdDefinition = definitionService.createDefinition(testDefinition);

        assertTrue(createdDefinition.isPresent());
        assertNotEquals(0, createdDefinition.get().getId());
        assertThat(createdDefinition.get().getVersion(), is(0));
        assertThat(createdDefinition.get().getName(), is("Test Name"));
        assertNotNull(createdDefinition.get().getDefinition());
        assertThat(createdDefinition.get().getDefinition().size(), is(1));
        assertThat(createdDefinition.get().getDefinition().get(0), is("Test definition 1"));
        assertNotNull(createdDefinition.get().getNotation());
        assertThat(createdDefinition.get().getNotation().size(), is(1));
        assertThat(createdDefinition.get().getNotation().get(0), is("\\testLaTeX"));

        final Optional<Definition> definitionFoundById = definitionService.findById(createdDefinition.get().getId());

        assertThat(definitionFoundById.get(), is(equalTo(createdDefinition.get())));

        final Definition definitionUpdate = new Definition();
        definitionUpdate.setId(createdDefinition.get().getId());
        definitionUpdate.setName("Test Update");

        final Optional<Definition> updatedDefinition = definitionService.updateDefinition(definitionUpdate);

        assertTrue(updatedDefinition.isPresent());
        assertNotEquals(0, updatedDefinition.get().getId());
        assertThat(updatedDefinition.get().getVersion(), is(1));
        assertThat(updatedDefinition.get().getName(), is("Test Update"));
        assertNotNull(updatedDefinition.get().getDefinition());
        assertThat(updatedDefinition.get().getDefinition().size(), is(1));
        assertThat(updatedDefinition.get().getDefinition().get(0), is("Test definition 1"));
        assertNotNull(updatedDefinition.get().getNotation());
        assertThat(updatedDefinition.get().getNotation().size(), is(1));
        assertThat(updatedDefinition.get().getNotation().get(0), is("\\testLaTeX"));

        final boolean deletionWasSuccessful = definitionService.deleteDefinition(updatedDefinition.get());

        assertThat(deletionWasSuccessful, is(true));

        final Optional<Definition> deletedDefinitionFoundById = definitionService.findById(createdDefinition.get().getId());

        assertFalse(deletedDefinitionFoundById.isPresent());
    }

    private Definition createDefinition() {
        final List<String> definitionList = new ArrayList<>();
        definitionList.add("Test definition 1");

        final List<String> notationList = new ArrayList<>();
        notationList.add("\\testLaTeX");

        final Definition definition = new Definition();
        definition.setName("Test Name");
        definition.setDefinition(definitionList);
        definition.setNotation(notationList);

        return definition;
    }
}
