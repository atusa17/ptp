package edu.msudenver.tsp.website.controller;

import edu.msudenver.tsp.services.UserService;
import edu.msudenver.tsp.services.dto.Account;
import edu.msudenver.tsp.website.forms.UserCreationForm;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/registration")
public class UserCreationController {
    @Autowired
    private final UserService userService;

    @GetMapping({"/",""})
    public ModelAndView createUserPage() {
        LOG.info("Received request to display the user creation page: returning model with name 'User'");
        return new ModelAndView("User");
    }

    @PostMapping({"/",""})
    public String registerUser(@Validated final UserCreationForm userCreationForm, final Model model) {
        model.addAttribute("username", userCreationForm.getUsername());
        model.addAttribute("password", userCreationForm.getPassword());
        model.addAttribute("emailAddress", userCreationForm.getEmailAddress());

        LOG.info("Saving user {}...", userCreationForm);

        final Account newUser = new Account();
        newUser.setUsername(userCreationForm.getUsername());
        newUser.setPassword(userCreationForm.getPassword());
        userService.createAccount(newUser);

        return "successfulRegistration";
    }
}
