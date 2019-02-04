package edu.msudenver.tsp.persistence.manager;

import edu.msudenver.tsp.persistence.dao.ProofDao;

class ProofManager {
    final private ProofDao proofDao;

    ProofManager(final ProofDao proofDao) {
        this.proofDao = proofDao;
    }
}
