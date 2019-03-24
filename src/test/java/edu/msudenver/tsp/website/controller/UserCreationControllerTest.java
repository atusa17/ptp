package edu.msudenver.tsp.website.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class UserCreationControllerTest {

    private final UserCreationController userCreationController = new UserCreationController();

    @Test
    public void testUserCreationPage() {
        final ModelAndView modelAndView = userCreationController.createUserPage();

        assertNotNull(modelAndView);
        assertEquals("User", modelAndView.getViewName());
    }
}