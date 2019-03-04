package edu.msudenver.tsp.persistence.repository;

import edu.msudenver.tsp.persistence.dto.TheoremDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TheoremRepository extends JpaRepository<TheoremDto, Long> {
}
