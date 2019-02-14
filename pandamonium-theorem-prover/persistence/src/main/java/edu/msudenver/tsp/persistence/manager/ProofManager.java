package edu.msudenver.tsp.persistence.manager;

import edu.msudenver.tsp.persistence.repository.ProofRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProofManager {
    final private ProofRepository proofRepository;
}
