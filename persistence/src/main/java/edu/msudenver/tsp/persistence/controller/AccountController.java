package edu.msudenver.tsp.persistence.controller;

import edu.msudenver.tsp.persistence.dto.Account;
import edu.msudenver.tsp.persistence.exception.BadRequestException;
import edu.msudenver.tsp.persistence.exception.UnprocessableEntityException;
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
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/accounts")
@Validated
public class AccountController {
    private final AccountsRepository accountsRepository;

    @GetMapping({"","/"})
    public @ResponseBody
    ResponseEntity<Iterable<Account>> getListOfAccounts() {
        LOG.info("Received request to list all accounts");

        LOG.debug("Querying for list of accounts");

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final List<Account> listOfAccounts = (List<Account>) accountsRepository.findAll();

        stopWatch.stop();

        LOG.debug("Successfully completed query. Query took {}ms to complete", stopWatch.getTotalTimeMillis());
        LOG.info("Returning list of all accounts with size of {}", listOfAccounts.size());

        return new ResponseEntity<>(listOfAccounts, HttpStatus.OK);
    }

    @GetMapping("/id")
    public @ResponseBody
    ResponseEntity<Account> getAccountById(@RequestParam("id") final Integer id) throws BadRequestException {
        LOG.info("Received request to query for account with id {}", id);
        if (id == null) {
            LOG.error("ERROR: ID was null");
            throw new BadRequestException("ERROR: ID cannot be null");
        }

        LOG.debug("Querying for account with id {}", id);
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final Optional<Account> account = accountsRepository.findById(id);

        stopWatch.stop();

        LOG.debug("Received response from server: query took {}ms to complete", stopWatch.getTotalTimeMillis());
        return account.map(accountDto -> {
            LOG.info("Returning account with id {}", id);
            return new ResponseEntity<>(accountDto, HttpStatus.OK);
        }).orElseGet(
                () -> {
                    LOG.warn("No account was found with id {}", id);
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
        );
    }

    @GetMapping("/username")
    public @ResponseBody
    ResponseEntity<Account> getAccountByUsername(@RequestParam("username") final String username) throws BadRequestException {
        LOG.info("Received request to query for account with username {}", username);
        if (username == null) {
            LOG.error("ERROR: username was null");
            throw new BadRequestException("ERROR: Username cannot be null");
        }

        LOG.debug("Querying for account with username {}", username);
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final Optional<Account> account = accountsRepository.findByUsername(username);

        stopWatch.stop();

        LOG.debug("Received response from server: query took {}ms to complete", stopWatch.getTotalTimeMillis());
        return account.map(accountDto -> {
            LOG.info("Returning account with username {}", username);
            return new ResponseEntity<>(accountDto, HttpStatus.OK);
        }).orElseGet(
                () -> {
                    LOG.warn("No account was found with username {}", username);
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
        );
    }

    @PostMapping({"","/"})
    @Validated({Account.Insert.class, Default.class})
    public @ResponseBody ResponseEntity<Account> insertAccount(
            @Valid @RequestBody final Account account, final BindingResult bindingResult)
            throws UnprocessableEntityException, BadRequestException {

        LOG.info("Received request to insert a new account");
        if (bindingResult.hasErrors()) {
            LOG.error("Binding result is unprocessable");
            throw new UnprocessableEntityException(bindingResult.getAllErrors().toString());
        }

        if (account == null) {
            LOG.error("Passed account is unprocessable");
            throw new BadRequestException("Passed account is unprocessable");
        }

        LOG.info("Checking for any existing users with username {}", account.getUsername());

        final Instant start = Instant.now();

        LOG.debug("Querying for existing accounts");

        final Optional<Account> existingAccount = accountsRepository.findByUsername(account.getUsername());

        LOG.debug("Received response from the server: query took {} ms", Duration.between(start, Instant.now()).toMillis());

        if (existingAccount.isPresent()) {
            LOG.warn("An account already exists with username {}", account.getUsername());
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        LOG.debug("Saving new account");

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final Account savedAccount = accountsRepository.save(account);

        stopWatch.stop();

        LOG.debug("Received response from server: query took {}ms to complete", stopWatch.getTotalTimeMillis());
        LOG.info("Returning the newly created account");
        return new ResponseEntity<>(savedAccount, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public @ResponseBody ResponseEntity<Account> updateAccount(
            @PathVariable("id") final Integer id,
            @RequestBody final Account account, final BindingResult bindingResult)
            throws UnprocessableEntityException, BadRequestException {

        LOG.info("Received request to update an account");
        if (bindingResult.hasErrors()) {
            LOG.error("Binding result is unprocessable");
            throw new UnprocessableEntityException(bindingResult.getAllErrors().toString());
        }

        if (account == null) {
            LOG.error("Passed entity is null");
            throw new BadRequestException("Passed account is null");
        }

        if (id == null) {
            LOG.error("Account ID must be specified");
            throw new BadRequestException("Account ID must be specified");
        }

        LOG.debug("Checking for existence of account with id {}", id);

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final Optional<Account> existingAccount = accountsRepository.findById(id);

        stopWatch.stop();

        LOG.debug("Received response from server: query took {}ms to complete", stopWatch.getTotalTimeMillis());

        if (!existingAccount.isPresent()) {
            LOG.error("No account associated with id {}", id);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        PersistenceUtilities.copyNonNullProperties(account, existingAccount.get());
        existingAccount.get().setVersion(existingAccount.get().getVersion()+ 1);

        LOG.info("Updating account with id {}", id);
        LOG.debug("Querying for account with id {}", id);

        stopWatch.start();

        final Account updatedAccount = accountsRepository.save(existingAccount.get());

        stopWatch.stop();

        LOG.debug("Received response from server: query took {}ms to complete", stopWatch.getTotalTimeMillis());

        return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public @ResponseBody ResponseEntity<Void> deleteAccountById(@PathVariable("id") final Integer id) throws BadRequestException {
        LOG.info("Received request to delete account with id {}", id);
        if (id == null) {
            LOG.error("Specified Id is null");
            throw new BadRequestException("Specified ID is null");
        }

        LOG.debug("Deleting account with id {}", id);

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        accountsRepository.deleteById(id);

        stopWatch.stop();

        LOG.debug("Received response from server: query took {}ms to complete", stopWatch.getTotalTimeMillis());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
