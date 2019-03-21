package edu.msudenver.tsp.persistence.controller;

import edu.msudenver.tsp.persistence.dto.Theorem;
import edu.msudenver.tsp.persistence.repository.TheoremRepository;
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
@RequestMapping(path = "/theorems")
public class TheoremController {
    private final TheoremRepository theoremRepository;

    @GetMapping({"","/"})
    public @ResponseBody
    ResponseEntity<Iterable<Theorem>> getAllTheorems() {
        LOG.info("Received request to list all theorems");

        LOG.debug("Querying for list of all theorems");
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final List<Theorem> listOfTheorems = theoremRepository.findAll();

        stopWatch.stop();

        LOG.debug("Successfully completed query. Query took {}ms to complete", stopWatch.getTotalTimeMillis());
        LOG.info("Returning list of all theorems with size {}", listOfTheorems.size());

        return new ResponseEntity<>(listOfTheorems, HttpStatus.OK);
    }

    @GetMapping("/branch")
    public @ResponseBody
    ResponseEntity<List<Theorem>> getAllTheoremsByBranch(@RequestParam("branch") final String branch) {
        LOG.info("Received request to query for theorems related to the {} branch of mathematics", branch);
        if (branch == null) {
            LOG.error("ERROR: branch was null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        LOG.debug("Querying for theorems with branch {}", branch);

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final List<Theorem> listOfTheorems = theoremRepository.findByBranch(branch);

        stopWatch.stop();

        LOG.debug("Received response from server: query took {}ms to complete", stopWatch.getTotalTimeMillis());
        LOG.info("Returning list of all theorems with size {}", listOfTheorems.size());

        if (listOfTheorems.isEmpty()) {
            LOG.warn("No theorems were found for branch {}", branch);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        LOG.info("Returning list of theorems with branch {}", branch);
        return new ResponseEntity<>(listOfTheorems, HttpStatus.OK);
    }

    @GetMapping("/proven_status")
    public @ResponseBody
    ResponseEntity<List<Theorem>> getAllTheoremsByProvenStatus(@RequestParam("proven_status") final String provenStatus) {
        LOG.info("Received request to query for theorems whose proven status is {}", provenStatus);
        if (provenStatus == null) {
            LOG.error("ERROR: status was null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        final Boolean isProven = Boolean.parseBoolean(provenStatus);

        LOG.debug("Querying for theorems with proven status {}", isProven);

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final List<Theorem> listOfTheorems = theoremRepository.findByProvenStatus(isProven);

        stopWatch.stop();

        LOG.debug("Received response from server: query took {}ms to complete", stopWatch.getTotalTimeMillis());
        LOG.info("Returning list of all theorems with size {}", listOfTheorems.size());

        if (listOfTheorems.isEmpty()) {
            LOG.warn("No theorems were found for proven status {}", isProven);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        LOG.info("Returning list of theorems with proven status {}", isProven);
        return new ResponseEntity<>(listOfTheorems, HttpStatus.OK);
    }

    @GetMapping("/name")
    public @ResponseBody
    ResponseEntity<List<Theorem>> getAllTheoremsByName(@RequestParam("name") final String name) {
        LOG.info("Received request to query for theorems whose name is {}", name);
        if (name == null) {
            LOG.error("ERROR: name was null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        LOG.debug("Querying for theorems with name {}", name);

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final List<Theorem> listOfTheorems = theoremRepository.findByName(name);

        stopWatch.stop();

        LOG.debug("Received response from server: query took {}ms to complete", stopWatch.getTotalTimeMillis());
        LOG.info("Returning list of all theorems with size {}", listOfTheorems.size());

        if (listOfTheorems.isEmpty()) {
            LOG.warn("No theorems were found with name {}", name);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        LOG.info("Returning list of theorems with name {}", name);
        return new ResponseEntity<>(listOfTheorems, HttpStatus.OK);
    }

    @GetMapping("/id")
    public @ResponseBody
    ResponseEntity<Theorem> getTheoremById(@RequestParam("id") final Integer id) {
        LOG.info("Received request to query for theorem with id {}", id);
        if (id == null) {
            LOG.error("ERROR: ID was null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        LOG.debug("Querying for theorem with id {}", id);

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final Optional<Theorem> theorem = theoremRepository.findById(id);

        stopWatch.stop();

        LOG.debug("Received response from server: query took {}ms to complete", stopWatch.getTotalTimeMillis());
        return theorem.map(theoremDto -> {
            LOG.info("Returning theorem with id {}", id);
            return new ResponseEntity<>(theoremDto, HttpStatus.OK);
        }).orElseGet(
                () -> {
                    LOG.warn("No theorem was found with id {}", id);
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                });

    }

    @PostMapping({"","/"})
    @Validated({Theorem.Insert.class, Default.class})
    public @ResponseBody ResponseEntity<Theorem> insertTheorem(
            @Valid @RequestBody final Theorem theorem,
            final BindingResult bindingResult) {
        LOG.info("Received request to insert a new theorem");
        if (bindingResult.hasErrors()) {
            LOG.error("Binding result is unprocessable");
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (theorem == null) {
            LOG.error("Passed entity is null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        LOG.debug("Saving new theorem");

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final Theorem savedTheorem = theoremRepository.save(theorem);

        stopWatch.stop();
        LOG.debug("Received response from server: query took {}ms to complete", stopWatch.getTotalTimeMillis());

        LOG.info("Returning the newly created theorem with id {}", savedTheorem.getId());
        return new ResponseEntity<>(savedTheorem, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public @ResponseBody ResponseEntity<Theorem> updateTheorem(
            @PathVariable("id") final Integer id,
            @RequestBody final Theorem theorem, final BindingResult bindingResult) {

        LOG.info("Received request to update a theorem");
        if (bindingResult.hasErrors()) {
            LOG.error("Binding result is unprocessable");
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (theorem == null) {
            LOG.error("Passed entity is null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (id == null) {
            LOG.error("Theorem ID must be specified");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        LOG.debug("Checking for existence of theorem with id {}", id);

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final Optional<Theorem> existingTheorem = theoremRepository.findById(id);

        stopWatch.stop();

        LOG.debug("Received response from server: query took {}ms to complete", stopWatch.getTotalTimeMillis());

        if (!existingTheorem.isPresent()) {
            LOG.error("No theorem associated with id {}", id);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        PersistenceUtilities.copyNonNullProperties(theorem, existingTheorem.get());
        existingTheorem.get().setVersion(existingTheorem.get().getVersion()+ 1);

        LOG.info("Updating theorem with id {}", id);
        LOG.debug("Querying for theorem with id {}", id);

        stopWatch.start();

        final Theorem updatedTheorem = theoremRepository.save(existingTheorem.get());

        stopWatch.stop();

        LOG.debug("Received response from server: query took {}ms to complete", stopWatch.getTotalTimeMillis());

        return new ResponseEntity<>(updatedTheorem, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public @ResponseBody ResponseEntity<Void> deleteTheoremById(@PathVariable("id") final Integer id) {
        LOG.info("Received request to delete theorem with id {}", id);
        if (id == null) {
            LOG.error("Specified id is null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        LOG.debug("Deleting theorem with id {}", id);

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        theoremRepository.deleteById(id);

        stopWatch.stop();

        LOG.debug("Received response from server: query took {}ms to complete", stopWatch.getTotalTimeMillis());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
