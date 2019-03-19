package edu.msudenver.tsp.website.controller;

import edu.msudenver.tsp.website.controller.forms.Theorem;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/theorem")
public class TheoremEntryController {
    @GetMapping({"/",""})
    public ModelAndView enterTheoremPage()
    {
        return new ModelAndView("Theorem");
    }

    @PostMapping({"/",""})
    public String saveTheorem(@Validated Theorem theorem, Model model) {

        model.addAttribute("theromName1", theorem.getTheoremName1());
        model.addAttribute("theromName2", theorem.getTheoremName2());

        return "success";
    }
}