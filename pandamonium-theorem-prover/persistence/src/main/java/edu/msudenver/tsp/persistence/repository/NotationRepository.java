package edu.msudenver.tsp.persistence.repository;

import edu.msudenver.tsp.persistence.entity.BaseEntity;
import org.springframework.data.repository.CrudRepository;

public interface NotationRepository extends CrudRepository<BaseEntity, Long> {
}
