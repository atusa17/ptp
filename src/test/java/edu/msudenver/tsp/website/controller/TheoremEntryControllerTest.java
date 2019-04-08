package edu.msudenver.tsp.website.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class TheoremEntryControllerTest {

    private final TheoremEntryController theoremEntryController = new TheoremEntryController();


   @Test
    public void saveTheorem(){

        final ModelAndView modelAndView= theoremEntryController.enterTheoremPage();

    @Test
    public void testEnterTheoremPage() {
        final ModelAndView modelAndView = theoremEntryController.enterTheoremPage();


        assertNotNull(modelAndView);
        assertEquals("Theorem", modelAndView.getViewName());
    }


}
}
