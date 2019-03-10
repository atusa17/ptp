package edu.msudenver.tsp.persistence.controller;

import edu.msudenver.tsp.persistence.repository.TheoremRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TheoremController {
    private final TheoremRepository theoremRepository;
}
