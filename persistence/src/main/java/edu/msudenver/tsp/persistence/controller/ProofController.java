package edu.msudenver.tsp.persistence.controller;

import edu.msudenver.tsp.persistence.repository.ProofRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProofController {
    final private ProofRepository proofRepository;
}
