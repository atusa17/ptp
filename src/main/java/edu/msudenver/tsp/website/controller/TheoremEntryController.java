package edu.msudenver.tsp.website.controller;

import edu.msudenver.tsp.website.controller.forms.Theorem;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@AllArgsConstructor
public class TheoremEntryController {



    @RequestMapping("/theorem")
    public ModelAndView theoremPage()
    {


        return new ModelAndView("Theorem");
    }


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveTheorem(@Validated Theorem theorem, Model model) {

        model.addAttribute("theromName", theorem.getTheoremName());

        return "success";
    }


    public ModelAndView firstPage() {
        return new ModelAndView("welcome");
    }
}


