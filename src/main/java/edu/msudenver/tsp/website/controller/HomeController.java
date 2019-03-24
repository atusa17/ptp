package edu.msudenver.tsp.website.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/")
public class HomeController {

    @GetMapping("")
    public String home(Map<String, Object> model) {
        model.put("message", "Welcome to Theorem Prover !!");
        return "index";
    }

    @RequestMapping("/next")
    public String next(Map<String, Object> model) {
        model.put("message", "You are in new page !!");
        return "Theorem";
    }
}
