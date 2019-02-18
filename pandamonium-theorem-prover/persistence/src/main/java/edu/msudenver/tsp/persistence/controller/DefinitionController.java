package edu.msudenver.tsp.persistence.controller;

import edu.msudenver.tsp.persistence.dto.DefinitionDto;
import edu.msudenver.tsp.persistence.repository.DefinitionRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/definitions/")
public class DefinitionController {
    private final DefinitionRepository definitionRepository;

    @GetMapping("/")
    public @ResponseBody
    ResponseEntity<Iterable<DefinitionDto>> getAllDefinitions() {
        final Iterable<DefinitionDto> listOfDefinitions = definitionRepository.findAll();
        return new ResponseEntity<>(listOfDefinitions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public @ResponseBody
    ResponseEntity<DefinitionDto> getDefinitionById(@PathVariable("id") final Integer id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        final Optional<DefinitionDto> definition = definitionRepository.findById(id);
        return definition.map(definitionDto ->
                new ResponseEntity<>(definitionDto, HttpStatus.OK)).orElseGet(
                        () -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    @PostMapping("/add")
    public @ResponseBody ResponseEntity<DefinitionDto> insertDefinition(
            @Valid @ModelAttribute("definitionDto") final DefinitionDto definitionDto,
            final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (definitionDto == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        final DefinitionDto savedDefinition = definitionRepository.save(definitionDto);
        return new ResponseEntity<>(savedDefinition, HttpStatus.CREATED);
    }
}
