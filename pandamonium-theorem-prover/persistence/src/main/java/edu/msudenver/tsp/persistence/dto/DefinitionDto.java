package edu.msudenver.tsp.persistence.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity(name = "definitions")
@Table(name = "definitions")
@EntityListeners(AuditingEntityListener.class)
@Data
@EqualsAndHashCode(callSuper = true)
public class DefinitionDto extends BaseDto implements Serializable {
    @NotNull(groups = Insert.class) @Size(min = 1, max = 200, message = "Must be between 1 and 200 characters") private String name;
    @NotNull(groups = Insert.class) @Type(type = "json") @Column(columnDefinition = "jsonb") private Definition definition;
    @Type(type = "json") @Column(columnDefinition = "jsonb") private Notation notation;

    public static final long serialVersionUID = -5314619286352932857L;

    public interface Insert {}
}
