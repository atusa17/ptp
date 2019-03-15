package edu.msudenver.tsp.persistence.controller;

import edu.msudenver.tsp.persistence.dto.ProofDto;
import edu.msudenver.tsp.persistence.repository.ProofRepository;
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
@RequestMapping("/proofs")
public class ProofController {
    private final ProofRepository proofRepository;

    @GetMapping("/")
    public @ResponseBody
    ResponseEntity<Iterable<ProofDto>> getAllProofs() {
        LOG.info("Received request to list all theorems");

        LOG.debug("Querying for list of all theorems");
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final List<ProofDto> listOfProofs = proofRepository.findAll();

        stopWatch.stop();

        LOG.debug("Successfully completed query. Query took " + stopWatch.getTotalTimeMillis() + "ms to complete");
        LOG.info("Returning list of all theorems with size " + listOfProofs.size());

        return new ResponseEntity<>(listOfProofs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public @ResponseBody
    ResponseEntity<ProofDto> getProofById(@PathVariable("id") final Integer id) {
        LOG.info("Received request to query for proof with id " + id);
        if (id == null) {
            LOG.error("ERROR: ID was null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        LOG.debug("Querying for proof with id " + id);

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final Optional<ProofDto> proof = proofRepository.findById(id);

        stopWatch.stop();

        LOG.debug("Received response from server: query took " + stopWatch.getTotalTimeMillis() + "ms to complete");
        return proof.map(proofDto -> {
            LOG.info("Returning proof with id " + id);
            return new ResponseEntity<>(proofDto, HttpStatus.OK);
        }).orElseGet(
                () -> {
                    LOG.warn("No proof was found with id " + id);
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                });

    }

    @GetMapping("/{branch}")
    public @ResponseBody
    ResponseEntity<List<ProofDto>> getAllProofsByBranch(@PathVariable("branch") final String branch) {
        LOG.info("Received request to query for proofs related to the " + branch + " branch of mathematics");
        if (branch == null) {
            LOG.error("ERROR: branch was null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        LOG.debug("Querying for proofs with branch " + branch);

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final List<ProofDto> listOfProofs = proofRepository.findByBranch(branch);

        stopWatch.stop();

        LOG.debug("Received response from server: query took " + stopWatch.getTotalTimeMillis() + "ms to complete");
        LOG.info("Returning list of all proofs with size " + listOfProofs.size());

        if (listOfProofs.isEmpty()) {
            LOG.warn("No proofs were found for branch {}", branch);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        LOG.info("Returning list of proofs with branch {}", branch);
        return new ResponseEntity<>(listOfProofs, HttpStatus.OK);
    }

    @GetMapping("/{theorem_name}")
    public @ResponseBody
    ResponseEntity<List<ProofDto>> getAllProofsByTheoremName(@PathVariable("theorem_name") final String theoremName) {
        LOG.info("Received request to query for proofs of the theorem {}", theoremName);
        if (theoremName == null) {
            LOG.error("ERROR: theorem name was null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        LOG.debug("Querying for proofs of the theorem {}", theoremName);

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final List<ProofDto> listOfProofs = proofRepository.findByTheoremName(theoremName);

        stopWatch.stop();

        LOG.debug("Received response from server: query took " + stopWatch.getTotalTimeMillis() + "ms to complete");
        LOG.info("Returning list of all proofs with size " + listOfProofs.size());

        if (listOfProofs.isEmpty()) {
            LOG.warn("No proofs were found of the theorem {}", theoremName);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        LOG.info("Returning list of proofs for the theorem {}", theoremName);
        return new ResponseEntity<>(listOfProofs, HttpStatus.OK);
    }

    @PostMapping("/")
    @Validated({ProofDto.Insert.class, Default.class})
    public @ResponseBody ResponseEntity<ProofDto> insertProof(
            @Valid @RequestBody final ProofDto proofDto,
            final BindingResult bindingResult) {
        LOG.info("Received request to insert a new proof");
        if (bindingResult.hasErrors()) {
            LOG.error("Binding result is unprocessable");
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (proofDto == null) {
            LOG.error("Passed entity is null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        LOG.debug("Saving new proof");

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final ProofDto savedProof = proofRepository.save(proofDto);

        stopWatch.stop();
        LOG.debug("Received response from server: query took {}ms to complete", stopWatch.getTotalTimeMillis());

        LOG.info("Returning the newly created proof with id {}", savedProof.getId());
        return new ResponseEntity<>(savedProof, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public @ResponseBody ResponseEntity<ProofDto> updateProof(
            @PathVariable("id") final Integer id,
            @RequestBody final ProofDto proofDto, final BindingResult bindingResult) {

        LOG.info("Received request to update a proof");
        if (bindingResult.hasErrors()) {
            LOG.error("Binding result is unprocessable");
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (proofDto == null) {
            LOG.error("Passed entity is null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (id == null) {
            LOG.error("Proof ID must be specified");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        LOG.debug("Checking for existence of proof with id " + id);

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final Optional<ProofDto> existingProof = proofRepository.findById(id);

        stopWatch.stop();

        LOG.debug("Received response from server: query took {}ms to complete", stopWatch.getTotalTimeMillis());

        if (!existingProof.isPresent()) {
            LOG.error("No proof associated with id {}", id);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        PersistenceUtilities.copyNonNullProperties(proofDto, existingProof.get());
        existingProof.get().setVersion(existingProof.get().getVersion()+ 1);

        LOG.info("Updating proof with id {}", id);
        LOG.debug("Querying for proof with ID {}", id);

        stopWatch.start();

        final ProofDto updatedProof = proofRepository.save(existingProof.get());

        stopWatch.stop();

        LOG.debug("Received response from server: query took {}ms to complete", stopWatch.getTotalTimeMillis());

        return new ResponseEntity<>(updatedProof, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public @ResponseBody ResponseEntity<Void> deleteProofById(@PathVariable("id") final Integer id) {
        LOG.info("Received request to delete proof with id {}", id);
        if (id == null) {
            LOG.error("Specified id is null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        LOG.debug("Deleting proof with id {}", id);

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        proofRepository.deleteById(id);

        stopWatch.stop();

        LOG.debug("Received response from server: query took {}ms to complete", stopWatch.getTotalTimeMillis());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
