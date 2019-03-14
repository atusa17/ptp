package edu.msudenver.tsp.website;

import edu.msudenver.tsp.website.forms.Theorem;
import edu.msudenver.tsp.website.service.ProofDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class TheoremEntryController {

    @Autowired
    ProofDriver proofDriver;

    @RequestMapping("/welcome")
    public ModelAndView firstPage()
    {
        return new ModelAndView("welcome");
    }

    @RequestMapping("/theorem")
    public ModelAndView theoremPage()
    {
        return new ModelAndView("Theorem");
    }


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveTheorem(@Validated Theorem theorem, Model model) {
        proofDriver.processProof(theorem.getTheoremName());
        model.addAttribute("theromName", theorem.getTheoremName());
        return "success";
    }


}


