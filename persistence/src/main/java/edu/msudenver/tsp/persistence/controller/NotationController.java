package edu.msudenver.tsp.persistence.controller;

import edu.msudenver.tsp.persistence.repository.NotationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class NotationController {
    final private NotationRepository notationRepository;
}
