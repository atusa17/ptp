package edu.msudenver.tsp.persistence.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity(name = "definitions")
@EntityListeners(AuditingEntityListener.class)
@Data
@EqualsAndHashCode(callSuper = true)
public class DefinitionDto extends BaseDto implements Serializable {
    @Size(min = 1, max = 200, message = "Must be between 1 and 200 characters") String name;
    Definition definition;
    Notation notation;

    public static final long serialVersionUID = -5314619286352932857L;
}
