package edu.msudenver.tsp.website.controller;

import edu.msudenver.tsp.services.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;



public class LogInControllerTest {
    @Autowired UserService userService;
    private final LogInController logInController = new LogInController(userService);

    @Test
    public void testEnterTheoremPage() {
        ModelAndView modelAndView = logInController.enterLogInPage();
        assertNotNull(modelAndView);
        assertEquals("LogIn", modelAndView.getViewName());

    }
}
