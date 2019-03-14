package edu.msudenver.tsp.persistence.controller;

import edu.msudenver.tsp.persistence.dto.ProofDto;
import edu.msudenver.tsp.persistence.repository.ProofRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
