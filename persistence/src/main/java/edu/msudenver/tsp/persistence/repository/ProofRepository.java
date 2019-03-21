package edu.msudenver.tsp.persistence.repository;

import edu.msudenver.tsp.persistence.dto.Proof;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProofRepository extends JpaRepository<Proof, Integer> {

    List<Proof> findByBranch(String branch);

    List<Proof> findByTheoremName(String name);
}
