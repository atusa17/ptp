package edu.msudenver.tsp.persistence.repository;

import edu.msudenver.tsp.persistence.dto.BaseDto;
import org.springframework.data.repository.CrudRepository;

public interface ProofRepository extends CrudRepository<BaseDto, Long> {
}
