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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.AdditionalMatchers.not;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ServiceTestConfig.class)
@TestPropertySource(locations = "classpath:test.properties")
public class DefinitionServiceIntegrationTest {
    @Autowired private DefinitionService definitionService;

    @Test
    public void testCreateDefinition() {
        final Definition testDefinition = createDefinition();
        final Optional<Definition> createdDefinition = definitionService.createDefinition(testDefinition);

        assertNotNull(createdDefinition);
        assertTrue(createdDefinition.isPresent());
        assertThat(createdDefinition.get().getId(), is(not(0)));
        assertThat(createdDefinition.get().getVersion(), is(0));
        assertThat(createdDefinition.get().getName(), is("Test Name"));
        assertNotNull(createdDefinition.get().getDefinition());
        assertThat(createdDefinition.get().getDefinition().size(), is(1));
        assertThat(createdDefinition.get().getDefinition().get(0), is("Test definition 1"));
        assertNotNull(createdDefinition.get().getNotation());
        assertThat(createdDefinition.get().getNotation().size(), is(1));
        assertThat(createdDefinition.get().getNotation().get(0), is("\\testLaTeX"));
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
