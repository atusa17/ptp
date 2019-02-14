package edu.msudenver.tsp.persistence.manager;

import edu.msudenver.tsp.persistence.repository.TheoremRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TheoremManager {
    final private TheoremRepository theoremRepository;
}
