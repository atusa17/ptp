package edu.msudenver.tsp.persistence.repository;

import edu.msudenver.tsp.persistence.dto.TheoremDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TheoremRepository extends JpaRepository<TheoremDto, Integer> {

    List<TheoremDto> findByBranch(String branch);

    List<TheoremDto> findByProvenStatus(Boolean provenStatus);

    List<TheoremDto> findByName(String name);
}
