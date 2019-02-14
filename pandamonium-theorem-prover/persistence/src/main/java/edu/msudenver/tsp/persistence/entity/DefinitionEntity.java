package edu.msudenver.tsp.persistence.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.validation.constraints.Size;

@Entity(name = "definitions")
@Data
public class DefinitionEntity extends BaseEntity {
    @Size(min = 1, max = 200, message = "Must be between 1 and 200 characters") String name;
    Definition definition;
    Notation notation;
}
