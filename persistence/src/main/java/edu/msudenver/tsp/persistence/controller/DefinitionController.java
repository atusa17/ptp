package edu.msudenver.tsp.persistence.controller;

import edu.msudenver.tsp.persistence.dto.Definition;
import edu.msudenver.tsp.persistence.exception.BadRequestException;
import edu.msudenver.tsp.persistence.exception.UnprocessableEntityException;
import edu.msudenver.tsp.persistence.repository.DefinitionRepository;
import edu.msudenver.tsp.utilities.PersistenceUtilities;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.groups.Default;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(path = "/definitions")
@Validated
public class DefinitionController {
    private final DefinitionRepository definitionRepository;

    @GetMapping({"","/"})
    public @ResponseBody
    ResponseEntity<Iterable<Definition>> getAllDefinitions() {
        LOG.info("Received request to list all definitions");

        LOG.debug("Querying for list of all definitions");
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final List<Definition> listOfDefinitions = definitionRepository.findAll();

        stopWatch.stop();

        LOG.debug("Successfully completed query. Query took {}ms to complete", stopWatch.getTotalTimeMillis());
        LOG.info("Returning list of all definition with size {}", listOfDefinitions.size());

        return new ResponseEntity<>(listOfDefinitions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public @ResponseBody
    ResponseEntity<Definition> getDefinitionById(@PathVariable("id") final Integer id)
            throws BadRequestException {
        LOG.info("Received request to query for definition with id {}", id);
        if (id == null) {
            LOG.error("ERROR: ID cannot be null");
            throw new BadRequestException("ERROR: ID cannot be null");
        }

        LOG.debug("Querying for definition with id {}", id);

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final Optional<Definition> definition = definitionRepository.findById(id);

        stopWatch.stop();

        LOG.debug("Received response from server: query took {}ms to complete", stopWatch.getTotalTimeMillis());
        return definition.map(definitionDto -> {
                LOG.info("Returning definition with id {}", id);
                return new ResponseEntity<>(definitionDto, HttpStatus.OK);
        }).orElseGet(
                        () -> {
                            LOG.warn("No definition was found with id {}",id);
                            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                        });

    }

    @PostMapping({"","/"})
    @Validated({Definition.Insert.class, Default.class})
    public @ResponseBody ResponseEntity<Definition> insertDefinition(
            @Valid @RequestBody final Definition definition,
            final BindingResult bindingResult)
            throws UnprocessableEntityException, BadRequestException {
        LOG.info("Received request to insert a new definition");
        if (bindingResult.hasErrors()) {
            LOG.error("Binding result is unprocessable");
            throw new UnprocessableEntityException(bindingResult.getAllErrors().toString());
        }

        if (definition == null) {
            LOG.error("Passed entity is null");
            throw new BadRequestException("Passed definition is be null");
        }

        LOG.debug("Saving new definition");

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final Definition savedDefinition = definitionRepository.save(definition);

        stopWatch.stop();
        LOG.debug("Received response from server: query took {}ms to complete", stopWatch.getTotalTimeMillis());

        LOG.info("Returning the newly created definition with id {}", savedDefinition.getId());
        return new ResponseEntity<>(savedDefinition, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public @ResponseBody ResponseEntity<Definition> updateDefinition(
            @PathVariable("id") final Integer id,
            @RequestBody final Definition definition, final BindingResult bindingResult) throws UnprocessableEntityException, BadRequestException {

        LOG.info("Received request to update an account");
        if (bindingResult.hasErrors()) {
            LOG.error("Binding result is unprocessable");
            throw new UnprocessableEntityException(bindingResult.getAllErrors().toString());
        }

        if (definition == null) {
            LOG.error("Passed entity is null");
            throw new BadRequestException("Passed definition is null");
        }

        if (id == null) {
            LOG.error("Definition ID must be specified");
            throw new BadRequestException("Definition ID must be specified");
        }

        LOG.debug("Checking for existence of definition with id {}", id);

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final Optional<Definition> existingDefinition = definitionRepository.findById(id);

        stopWatch.stop();

        LOG.debug("Received response from server: query took {}ms to complete", stopWatch.getTotalTimeMillis());

        if (!existingDefinition.isPresent()) {
            LOG.error("No definition associated with id {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        PersistenceUtilities.copyNonNullProperties(definition, existingDefinition.get());
        existingDefinition.get().setVersion(existingDefinition.get().getVersion()+ 1);

        LOG.info("Updating definition with id {}", id);
        LOG.debug("Querying for definition with id {}", id);

        stopWatch.start();

        final Definition updatedDefinition = definitionRepository.save(existingDefinition.get());

        stopWatch.stop();

        LOG.debug("Received response from server: query took {}ms to complete", stopWatch.getTotalTimeMillis());

        return new ResponseEntity<>(updatedDefinition, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public @ResponseBody ResponseEntity<Void> deleteDefinitionById(@PathVariable("id") final Integer id) throws BadRequestException {
        LOG.info("Received request to delete definition with id {}", id);
        if (id == null) {
            LOG.error("Specified id is null");
            throw new BadRequestException("Specified ID is null");
        }

        LOG.debug("Deleting definition with id {}", id);

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        definitionRepository.deleteById(id);

        stopWatch.stop();

        LOG.debug("Received response from server: query took {}ms to complete", stopWatch.getTotalTimeMillis());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
