package edu.msudenver.tsp.persistence.repository;


import edu.msudenver.tsp.persistence.entity.Definition;
import edu.msudenver.tsp.persistence.entity.DefinitionEntity;
import edu.msudenver.tsp.persistence.entity.Notation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DefinitionRepository extends CrudRepository<DefinitionEntity, Integer> {

    DefinitionEntity findByName(final String name);

    DefinitionEntity findByDefinition(final Definition definition);

    DefinitionEntity findByNotation(final Notation notation);
}
