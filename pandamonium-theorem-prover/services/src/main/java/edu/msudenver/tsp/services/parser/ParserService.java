package edu.msudenver.tsp.services.parser;

import edu.msudenver.tsp.persistence.manager.DefinitionManager;
import edu.msudenver.tsp.persistence.manager.NotationManager;
import edu.msudenver.tsp.persistence.manager.ProofManager;
import edu.msudenver.tsp.persistence.manager.TheoremManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class ParserService {
    private final DefinitionManager definitionManager;
    private final TheoremManager theoremManager;
    private final NotationManager notationManager;
    private final ProofManager proofManager;

    @Autowired
    public ParserService(final DefinitionManager definitionManager, final TheoremManager theoremManager,
                         final NotationManager notationManager, final ProofManager proofManager) {
        this.definitionManager = definitionManager;
        this.theoremManager = theoremManager;
        this.notationManager = notationManager;
        this.proofManager = proofManager;
    }

}
