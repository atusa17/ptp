package edu.msudenver.tsp.website.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class TheoremEntryControllerTest {

    @Mock private TheoremEntryController theoremEntryController;

    @Test
    public void testEnterTheoremPage() {
        final ModelAndView modelAndView = theoremEntryController.enterTheoremPage();

        assertNotNull(modelAndView);
        assertEquals("Theorem", modelAndView.getViewName());
    }
}
