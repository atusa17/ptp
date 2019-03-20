package edu.msudenver.tsp.persistence.repository;

import edu.msudenver.tsp.persistence.dto.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountsRepository extends CrudRepository<Account, Integer> {
    Optional<Account> findByUsername(String username);
}
