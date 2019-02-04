package edu.msudenver.tsp.persistence.manager;

import edu.msudenver.tsp.persistence.dao.ProofDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProofManager {
    final private ProofDao proofDao;
}
