package edu.msudenver.tsp.persistence.controller;

import edu.msudenver.tsp.persistence.dto.AccountDto;
import edu.msudenver.tsp.persistence.repository.AccountsRepository;
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
@RequestMapping("/accounts")
public class AccountController {
    private final AccountsRepository accountsRepository;

    @GetMapping("/")
    public @ResponseBody
    ResponseEntity<Iterable<AccountDto>> getListOfAccounts() {
        LOG.info("Received request to list all accounts");

        LOG.debug("Querying for list of accounts");

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final List<AccountDto> listOfAccounts = (List<AccountDto>) accountsRepository.findAll();

        stopWatch.stop();

        LOG.debug("Successfully completed query. Query took " + stopWatch.getTotalTimeMillis() + "ms to complete");
        LOG.info("Returning list of all accounts with size of " + listOfAccounts.size());

        return new ResponseEntity<>(listOfAccounts, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public @ResponseBody
    ResponseEntity<AccountDto> getAccountById(@PathVariable("id") final Integer id) {
        LOG.info("Received request to query for account with id " + id);
        if (id == null) {
            LOG.error("ERROR: ID was null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        LOG.debug("Querying for account with id " + id);
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final Optional<AccountDto> account = accountsRepository.findById(id);

        stopWatch.stop();

        LOG.debug("Received response from server: query took " + stopWatch.getTotalTimeMillis() + "ms to complete");
        return account.map(accountDto -> {
            LOG.info("Returning account with id " + id);
            return new ResponseEntity<>(accountDto, HttpStatus.OK);
        }).orElseGet(
                () -> {
                    LOG.warn("No account was found with id " + id);
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
        );
    }

    @PostMapping("/")
    @Validated({AccountDto.Insert.class, Default.class})
    public @ResponseBody ResponseEntity<AccountDto> insertAccount(
            @Valid @RequestBody final AccountDto accountDto, final BindingResult bindingResult) {

        LOG.info("Received request to insert a new account");
        if (bindingResult.hasErrors()) {
            LOG.error("Binding result is unprocessable");
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (accountDto == null) {
            LOG.error("Passed account is unprocessable");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        LOG.debug("Saving new account");

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final AccountDto savedAccount = accountsRepository.save(accountDto);

        stopWatch.stop();

        LOG.debug("Received response from server: query took " + stopWatch.getTotalTimeMillis() + "ms to complete");
        LOG.info("Returning the newly created account");
        return new ResponseEntity<>(savedAccount, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public @ResponseBody ResponseEntity<AccountDto> updateAccount(
            @PathVariable("id") final Integer id,
            @RequestBody final AccountDto accountDto, final BindingResult bindingResult) {

        LOG.info("Received request to update an account");
        if (bindingResult.hasErrors()) {
            LOG.error("Binding result is unprocessable");
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (accountDto == null) {
            LOG.error("Passed entity is null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (id == null) {
            LOG.error("Account ID must be specified");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        LOG.debug("Checking for existence of account with id " + id);

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final Optional<AccountDto> existingAccount = accountsRepository.findById(id);

        stopWatch.stop();

        LOG.debug("Received response from server: query took " + stopWatch.getTotalTimeMillis() + "ms to complete");

        if (!existingAccount.isPresent()) {
            LOG.error("No account associated with id " + id);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        PersistenceUtilities.copyNonNullProperties(accountDto, existingAccount.get());
        existingAccount.get().setVersion(existingAccount.get().getVersion()+ 1);

        LOG.info("Updating account with id " + id);
        LOG.debug("Querying for account with ID " + id);

        stopWatch.start();

        final AccountDto updatedAccount = accountsRepository.save(existingAccount.get());

        stopWatch.stop();

        LOG.debug("Received response from server: query took " + stopWatch.getTotalTimeMillis() + "ms to complete");

        return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public @ResponseBody ResponseEntity<Void> deleteAccountById(@PathVariable("id") final Integer id) {
        LOG.info("Received request to delete account with id " + id);
        if (id == null) {
            LOG.error("Specified Id is null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        LOG.debug("Deleting account with id " + id);

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        accountsRepository.deleteById(id);

        stopWatch.stop();

        LOG.debug("Received response from server: query took " + stopWatch.getTotalTimeMillis() + "ms to complete");

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
