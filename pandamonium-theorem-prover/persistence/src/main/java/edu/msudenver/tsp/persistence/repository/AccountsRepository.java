package edu.msudenver.tsp.persistence.repository;

import edu.msudenver.tsp.persistence.dto.AccountsDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountsRepository extends JpaRepository<AccountsDto, Integer> {
}
