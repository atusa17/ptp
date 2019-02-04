package edu.msudenver.tsp.persistence.manager;

import edu.msudenver.tsp.persistence.dao.TheoremDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
class TheoremManager {
    final private TheoremDao theoremDao;
}
