package edu.msudenver.tsp.persistence.manager;

import edu.msudenver.tsp.persistence.dao.DefinitionDao;
import edu.msudenver.tsp.persistence.dto.DefinitionDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DefinitionManager {
    final private DefinitionDao definitionDao;

    public DefinitionDto findByID(String s){
        // search Database for string
        return new DefinitionDto();
        // return object?
    }
}
