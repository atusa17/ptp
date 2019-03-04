package edu.msudenver.tsp.persistence.controller;

import edu.msudenver.tsp.persistence.dto.TheoremDto;
import edu.msudenver.tsp.persistence.repository.TheoremRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
