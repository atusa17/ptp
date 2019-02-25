package edu.msudenver.tsp.persistence.repository;

import edu.msudenver.tsp.persistence.dto.AccountDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountsRepository extends CrudRepository<AccountDto, Integer> {
}
