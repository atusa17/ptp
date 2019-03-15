package edu.msudenver.tsp.persistence.repository;

import edu.msudenver.tsp.persistence.dto.ProofDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProofRepository extends JpaRepository<ProofDto, Integer> {

    List<ProofDto> findByBranch(String branch);

    List<ProofDto> findByTheoremName(String name);
}
