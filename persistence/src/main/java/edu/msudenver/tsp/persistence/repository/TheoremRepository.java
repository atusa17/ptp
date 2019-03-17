package edu.msudenver.tsp.persistence.repository;

import edu.msudenver.tsp.persistence.dto.Theorem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TheoremRepository extends JpaRepository<Theorem, Integer> {

    List<Theorem> findByBranch(String branch);

    List<Theorem> findByProvenStatus(Boolean provenStatus);

    List<Theorem> findByName(String name);
}
