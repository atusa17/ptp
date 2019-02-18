package edu.msudenver.tsp.persistence.controller;

import edu.msudenver.tsp.persistence.dto.AccountsDto;
import edu.msudenver.tsp.persistence.repository.AccountsRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/accounts")
public class AccountController {
    private final AccountsRepository accountsRepository;

    @GetMapping("/")
    public @ResponseBody Iterable<AccountsDto> getListOfAccounts() {
        return accountsRepository.findAll();
    }

    @GetMapping("/{id}")
    public @ResponseBody
    Optional<AccountsDto> getAccountById(@PathVariable("id") final Integer id) {
        return accountsRepository.findById(id);
    }
}
