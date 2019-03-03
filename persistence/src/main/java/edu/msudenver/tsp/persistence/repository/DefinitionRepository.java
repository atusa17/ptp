package edu.msudenver.tsp.persistence.repository;


import edu.msudenver.tsp.persistence.dto.DefinitionDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DefinitionRepository extends JpaRepository<DefinitionDto, Integer> {

}
