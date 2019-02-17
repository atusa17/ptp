package edu.msudenver.tsp.persistence.controller;

import edu.msudenver.tsp.persistence.dto.DefinitionDto;
import edu.msudenver.tsp.persistence.repository.DefinitionRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/definitions/")
public class DefinitionController {
    private final DefinitionRepository definitionRepository;

    @GetMapping("/all")
    public @ResponseBody Iterable<DefinitionDto> getAllDefinitions() {
        return definitionRepository.findAll();
    }
}
