package edu.msudenver.tsp.persistence.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Entity(name = "theorems")
@EntityListeners(AuditingEntityListener.class)
@Data
@EqualsAndHashCode(callSuper = true)
public class TheoremDto extends BaseDto implements Serializable {
    @NotBlank @Size(min = 1, max = 512, message = "theorem name must be between 1 and 512 characters") private String name;
    @NotNull private TheoremType theorem_type;
    @NotNull(message = "a branch of mathematics that this theorem is associated with must be specified") private String branch;
    @Type(type = "json") @Column(columnDefinition = "jsonb") private List<String> referenced_definitions;
    @Type(type = "json") @Column(columnDefinition = "jsonb") private List<String> referenced_theorems;
    @NotNull private boolean proven_status;

    private static final long serialVersionUID = 1545568391140364425L;
}
