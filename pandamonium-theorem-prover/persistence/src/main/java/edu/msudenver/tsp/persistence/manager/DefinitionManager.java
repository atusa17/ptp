package edu.msudenver.tsp.persistence.manager;

import edu.msudenver.tsp.persistence.dao.DefinitionDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
class DefinitionManager {
    final private DefinitionDao definitionDao;
}
