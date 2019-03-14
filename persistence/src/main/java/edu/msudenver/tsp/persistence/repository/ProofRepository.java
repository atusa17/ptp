package edu.msudenver.tsp.persistence.repository;

import edu.msudenver.tsp.persistence.dto.BaseDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProofRepository extends JpaRepository<BaseDto, Integer> {
}
