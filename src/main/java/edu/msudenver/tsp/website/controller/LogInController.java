package edu.msudenver.tsp.website.controller;

import edu.msudenver.tsp.website.forms.TheoremForm;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    @RequestMapping("/login")
    public class LogInController {
        @GetMapping({"/",""})
        public ModelAndView enterTheoremPage() {
            LOG.info("Received request to display the theorem entry page: returning model with name 'Theorem'");
            return new ModelAndView("Theorem");
        }

        @PostMapping({"/",""})
        public String saveTheorem(@Validated final TheoremForm theoremForm, final Model model) {
            model.addAttribute("theoremName", theoremForm.getTheoremName());
            model.addAttribute("theorem", theoremForm.getTheorem());
            LOG.info("Saving theorem {}...", theoremForm);

            return "success";
        }
    }
}
