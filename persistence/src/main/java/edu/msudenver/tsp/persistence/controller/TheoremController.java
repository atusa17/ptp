package edu.msudenver.tsp.persistence.controller;

import edu.msudenver.tsp.persistence.dto.TheoremDto;
import edu.msudenver.tsp.persistence.repository.TheoremRepository;
import edu.msudenver.tsp.utilities.PersistenceUtilities;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(path = "/theorems")
public class TheoremController {
    private final TheoremRepository theoremRepository;

    @GetMapping("/")
    public @ResponseBody
    ResponseEntity<Iterable<TheoremDto>> getAllTheorems() {
        LOG.info("Received request to list all theorems");

        LOG.debug("Querying for list of all theorems");
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final List<TheoremDto> listOfTheorems = theoremRepository.findAll();

        stopWatch.stop();

        LOG.debug("Successfully completed query. Query took " + stopWatch.getTotalTimeMillis() + "ms to complete");
        LOG.info("Returning list of all theorems with size " + listOfTheorems.size());

        return new ResponseEntity<>(listOfTheorems, HttpStatus.OK);
    }

    @GetMapping("/{branch}")
    public @ResponseBody
    ResponseEntity<List<TheoremDto>> getAllTheoremsByBranch(@PathVariable("branch") final String branch) {
        LOG.info("Received request to query for theorems related to the " + branch + " branch of mathematics");
        if (branch == null) {
            LOG.error("ERROR: branch was null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        LOG.debug("Querying for theorems with branch " + branch);

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final List<TheoremDto> listOfTheorems = theoremRepository.findByBranch(branch);

        stopWatch.stop();

        LOG.debug("Received response from server: query took " + stopWatch.getTotalTimeMillis() + "ms to complete");
        LOG.info("Returning list of all theorems with size " + listOfTheorems.size());

        return new ResponseEntity<>(listOfTheorems, HttpStatus.OK);
    }

    @GetMapping("/{proven_status}")
    public @ResponseBody
    ResponseEntity<List<TheoremDto>> getAllTheoremsByBranch(@PathVariable("proven_status") final Boolean proven_status) {
        LOG.info("Received request to query for theorems whose proven status is " + proven_status);
        if (proven_status == null) {
            LOG.error("ERROR: branch was null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        LOG.debug("Querying for theorems with proven status " + proven_status);

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final List<TheoremDto> listOfTheorems = theoremRepository.findByProven_status(proven_status);

        stopWatch.stop();

        LOG.debug("Received response from server: query took " + stopWatch.getTotalTimeMillis() + "ms to complete");
        LOG.info("Returning list of all theorems with size " + listOfTheorems.size());

        return new ResponseEntity<>(listOfTheorems, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public @ResponseBody
    ResponseEntity<TheoremDto> getTheoremById(@PathVariable("id") final Integer id) {
        LOG.info("Received request to query for theorem with id " + id);
        if (id == null) {
            LOG.error("ERROR: ID was null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        LOG.debug("Querying for theorem with id " + id);

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final Optional<TheoremDto> theorem = theoremRepository.findById(id);

        stopWatch.stop();

        LOG.debug("Received response from server: query took " + stopWatch.getTotalTimeMillis() + "ms to complete");
        return theorem.map(theoremDto -> {
            LOG.info("Returning theorem with id " + id);
            return new ResponseEntity<>(theoremDto, HttpStatus.OK);
        }).orElseGet(
                () -> {
                    LOG.warn("No theorem was found with id " + id);
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                });

    }

    @PostMapping("/")
    public @ResponseBody ResponseEntity<TheoremDto> insertTheorem(
            @Valid @RequestBody final TheoremDto theoremDto,
            final BindingResult bindingResult) {
        LOG.info("Received request to insert a new theorem");
        if (bindingResult.hasErrors()) {
            LOG.error("Binding result is unprocessable");
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (theoremDto == null) {
            LOG.error("Passed entity is unprocessable");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        LOG.debug("Saving new theorem");

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final TheoremDto savedTheorem = theoremRepository.save(theoremDto);

        stopWatch.stop();
        LOG.debug("Received response from server: query took " + stopWatch.getTotalTimeMillis() + "ms to complete");

        LOG.info("Returning the newly created theorem with id " + savedTheorem.getId());
        return new ResponseEntity<>(savedTheorem, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public @ResponseBody ResponseEntity<TheoremDto> updateTheorem(
            @PathVariable("id") final Integer id,
            @RequestBody final TheoremDto theoremDto, final BindingResult bindingResult) {

        LOG.info("Received request to update an account");
        if (bindingResult.hasErrors()) {
            LOG.error("Binding result is unprocessable");
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (theoremDto == null) {
            LOG.error("Passed entity is null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (id == null) {
            LOG.error("Theorem ID must be specified");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        LOG.debug("Checking for existence of theorem with id " + id);

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final Optional<TheoremDto> existingTheorem = theoremRepository.findById(id);

        stopWatch.stop();

        LOG.debug("Received response from server: query took " + stopWatch.getTotalTimeMillis() + "ms to complete");

        if (!existingTheorem.isPresent()) {
            LOG.error("No theorem associated with id " + id);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        PersistenceUtilities.copyNonNullProperties(theoremDto, existingTheorem.get());
        existingTheorem.get().setVersion(existingTheorem.get().getVersion()+ 1);

        LOG.info("Updating theorem with id " + id);
        LOG.debug("Querying for theorem with ID " + id);

        stopWatch.start();

        final TheoremDto updatedTheorem = theoremRepository.save(existingTheorem.get());

        stopWatch.stop();

        LOG.debug("Received response from server: query took " + stopWatch.getTotalTimeMillis() + "ms to complete");

        return new ResponseEntity<>(updatedTheorem, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public @ResponseBody ResponseEntity<Void> deleteTheoremById(@PathVariable("id") final Integer id) {
        LOG.info("Received request to delete theorem with id " + id);
        if (id == null) {
            LOG.error("Specified id is null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        LOG.debug("Deleting theorem with id " + id);

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        theoremRepository.deleteById(id);

        stopWatch.stop();

        LOG.debug("Received response from server: query took " + stopWatch.getTotalTimeMillis() + "ms to complete");

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
