package edu.msudenver.tsp.website.controller;


import edu.msudenver.tsp.services.UserService;
import edu.msudenver.tsp.services.dto.Account;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/login")
public class LogInController {
    @Autowired
    private final UserService userService;

    @GetMapping({"/", ""})
    public ModelAndView enterLogInPage() {
        LOG.info("Received request to display the log in page: returning model with name 'LogIn'");
        return new ModelAndView("LogIn");
    }

    @PostMapping({"/", ""})
    public String singIn(Model model, @RequestParam("username") String username, @RequestParam("password") String password) {
        Optional<Account> findUserByUsername = userService.findAccountByUsername(username);
        if(findUserByUsername.isPresent()){
            if (password.equals(findUserByUsername.get().getPassword())) {
                model.addAttribute("username", username);
                model.addAttribute("password", password);
                return "index";
            }
            else {
                model.addAttribute("error", "your username and password is invalid");
                return "LogIn";
            }
        }
        else {
            model.addAttribute("error", "username and password can not be empty");
            return "LogIn";
        }
    }
}