package edu.msudenver.tsp.persistence.manager;

import edu.msudenver.tsp.persistence.dao.ProofDao;
import org.springframework.stereotype.Component;

@Component
class ProofManager {
    final private ProofDao proofDao;

    ProofManager(final ProofDao proofDao) {
        this.proofDao = proofDao;
    }
}
