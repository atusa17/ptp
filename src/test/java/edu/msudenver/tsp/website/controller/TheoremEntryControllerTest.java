package edu.msudenver.tsp.website.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Map;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class TheoremEntryControllerTest {

    @InjectMocks
    private TheoremEntryController theoremEntryController;

    @Test
    public void theoremPage(){

        final ModelAndView modelAndView= theoremEntryController.enterTheoremPage();

        assertNotNull(modelAndView);
        assertEquals("Theorem",modelAndView.getViewName());
    }

    @Test
    public void saveTheorem(){

        final ModelAndView modelAndView= theoremEntryController.enterTheoremPage();

        assertNotNull(modelAndView);
        assertEquals("Theorem",modelAndView.getViewName());
    }


}
