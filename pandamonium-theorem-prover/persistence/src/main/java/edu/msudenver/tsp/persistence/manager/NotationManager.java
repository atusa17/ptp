package edu.msudenver.tsp.persistence.manager;

import edu.msudenver.tsp.persistence.dao.NotationDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
class NotationManager {
    final private NotationDao notationDao;
}
