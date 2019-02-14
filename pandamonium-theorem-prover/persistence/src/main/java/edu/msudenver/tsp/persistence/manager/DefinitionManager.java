package edu.msudenver.tsp.persistence.manager;

import edu.msudenver.tsp.persistence.entity.DefinitionEntity;
import edu.msudenver.tsp.persistence.repository.DefinitionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@AllArgsConstructor
@RequestMapping(path = "/definitions/")
public class DefinitionManager {
    private final DefinitionRepository definitionRepository;

    @GetMapping("/all")
    public @ResponseBody Iterable<DefinitionEntity> getAllDefinitions() {
        return definitionRepository.findAll();
    }
}
