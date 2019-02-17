package edu.msudenver.tsp.persistence.repository;


import edu.msudenver.tsp.persistence.dto.Definition;
import edu.msudenver.tsp.persistence.dto.DefinitionDto;
import edu.msudenver.tsp.persistence.dto.Notation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DefinitionRepository extends JpaRepository<DefinitionDto, Integer> {

    DefinitionDto findByName(final String name);

    DefinitionDto findByDefinition(final Definition definition);

    DefinitionDto findByNotation(final Notation notation);
}
