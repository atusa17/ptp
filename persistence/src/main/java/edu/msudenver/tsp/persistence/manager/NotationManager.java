package edu.msudenver.tsp.persistence.manager;

import edu.msudenver.tsp.persistence.dao.NotationDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class NotationManager {
    final private NotationDao notationDao;
}
