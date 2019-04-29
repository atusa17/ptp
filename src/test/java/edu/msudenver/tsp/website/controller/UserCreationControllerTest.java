package edu.msudenver.tsp.website.controller;

import edu.msudenver.tsp.services.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class UserCreationControllerTest {

    private final UserService userService = mock(UserService.class);
    private final UserCreationController userCreationController = new UserCreationController();//userService);

    @Test
    public void testCreateUserPage() {
        final ModelAndView modelAndView = userCreationController.createUserPage();

        assertNotNull(modelAndView);
        assertEquals("User", modelAndView.getViewName());
    }
}