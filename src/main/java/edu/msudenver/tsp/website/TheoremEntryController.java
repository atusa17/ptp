package edu.msudenver.tsp.website;

import edu.msudenver.tsp.website.forms.Theorem;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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


    @RequestMapping("/welcome")
    public ModelAndView firstPage()
    {
        return new ModelAndView("welcome");
    }

    @RequestMapping("/theorem")
    public ModelAndView theoremPage()
    {
        //System.out.println("Inside controller");
        return new ModelAndView("Theorem");
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveTheorem(@Validated Theorem theorem, Model model) {

        model.addAttribute("theromName1", theorem.getTheoremName1());
        model.addAttribute("theromName2", theorem.getTheoremName2());
        return "success";
    }


}


