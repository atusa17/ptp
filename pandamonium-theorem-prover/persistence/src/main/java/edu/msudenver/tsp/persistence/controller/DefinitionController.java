package edu.msudenver.tsp.persistence.controller;

import edu.msudenver.tsp.persistence.dto.DefinitionDto;
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
@RequestMapping(path = "/definitions/")
public class DefinitionController {
    private final DefinitionRepository definitionRepository;

    @GetMapping("/")
    public @ResponseBody
    ResponseEntity<Iterable<DefinitionDto>> getAllDefinitions() {
        LOG.info("Received request to list all definitions");

        LOG.debug("Querying for list of all definitions");
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final List<DefinitionDto> listOfDefinitions = definitionRepository.findAll();

        stopWatch.stop();

        LOG.debug("Successfully completed query. Query took " + stopWatch.getTotalTimeMillis() + "ms to complete");
        LOG.info("Returning list of all definition with size " + listOfDefinitions.size());

        return new ResponseEntity<>(listOfDefinitions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public @ResponseBody
    ResponseEntity<DefinitionDto> getDefinitionById(@PathVariable("id") final Integer id) {
        LOG.info("Received request to query for definition with id " + id);
        if (id == null) {
            LOG.error("ERROR: ID was null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        LOG.debug("Querying for definition with id " + id);

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final Optional<DefinitionDto> definition = definitionRepository.findById(id);

        stopWatch.stop();

        LOG.debug("Received response from server: query took " + stopWatch.getTotalTimeMillis() + "ms to complete");
        return definition.map(definitionDto -> {
                LOG.info("Returning definition with id " + id);
                return new ResponseEntity<>(definitionDto, HttpStatus.OK);
        }).orElseGet(
                        () -> {
                            LOG.warn("No definition was found with id " + id);
                            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                        });

    }

    @PostMapping("/")
    @Validated({DefinitionDto.Insert.class, Default.class})
    public @ResponseBody ResponseEntity<DefinitionDto> insertDefinition(
            @Valid @RequestBody final DefinitionDto definitionDto,
            final BindingResult bindingResult) {
        LOG.info("Received request to insert a new definition");
        if (bindingResult.hasErrors()) {
            LOG.error("Binding result is unprocessable");
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (definitionDto == null) {
            LOG.error("Passed entity is unprocessable");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        LOG.debug("Saving new definition");

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final DefinitionDto savedDefinition = definitionRepository.save(definitionDto);

        stopWatch.stop();
        LOG.debug("Received response from server: query took " + stopWatch.getTotalTimeMillis() + "ms to complete");

        LOG.info("Returning the newly created definition with id " + savedDefinition.getId());
        return new ResponseEntity<>(savedDefinition, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public @ResponseBody ResponseEntity<DefinitionDto> updateDefinition(
            @PathVariable("id") final Integer id,
            @RequestBody final DefinitionDto definitionDto, final BindingResult bindingResult) {

        LOG.info("Received request to update an account");
        if (bindingResult.hasErrors()) {
            LOG.error("Binding result is unprocessable");
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (definitionDto == null) {
            LOG.error("Passed entity is null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (id == null) {
            LOG.error("Definition ID must be specified");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        LOG.debug("Checking for existence of definition with id " + id);

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final Optional<DefinitionDto> existingDefinition = definitionRepository.findById(id);

        stopWatch.stop();

        LOG.debug("Received response from server: query took " + stopWatch.getTotalTimeMillis() + "ms to complete");

        if (!existingDefinition.isPresent()) {
            LOG.error("No definition associated with id " + id);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        PersistenceUtilities.copyNonNullProperties(definitionDto, existingDefinition.get());
        existingDefinition.get().setVersion(existingDefinition.get().getVersion()+ 1);

        LOG.info("Updating definition with id " + id);
        LOG.debug("Querying for definition with ID " + id);

        stopWatch.start();

        final DefinitionDto updatedDefinition = definitionRepository.save(existingDefinition.get());

        stopWatch.stop();

        LOG.debug("Received response from server: query took " + stopWatch.getTotalTimeMillis() + "ms to complete");

        return new ResponseEntity<>(updatedDefinition, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public @ResponseBody ResponseEntity<Void> deleteDefinitionById(@PathVariable("id") final Integer id) {
        LOG.info("Received request to delete definition with id " + id);
        if (id == null) {
            LOG.error("Specified id is null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        LOG.debug("Deleting definition with id " + id);

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        definitionRepository.deleteById(id);

        stopWatch.stop();

        LOG.debug("Received response from server: query took " + stopWatch.getTotalTimeMillis() + "ms to complete");

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
