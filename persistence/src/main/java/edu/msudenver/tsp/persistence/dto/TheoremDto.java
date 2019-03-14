package edu.msudenver.tsp.persistence.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @NotNull @Column(name = "theorem_type") private TheoremType theoremType;
    @NotNull(message = "a branch of mathematics that this theorem is associated with must be specified") private String branch;
    @Type(type = "json") @Column(name = "referenced_definitions", columnDefinition = "jsonb") private List<String> referencedDefinitions;
    @Type(type = "json") @Column(name = "referenced_theorems", columnDefinition = "jsonb") private List<String> referencedTheorems;
    @NotNull @Column(name = "proven_status") private boolean provenStatus;

    @JsonProperty("theorem_type")
    public TheoremType getTheoremType() {
        return theoremType;
    }

    @JsonProperty("theorem_type")
    public void setTheoremType(final TheoremType theoremType) {
        this.theoremType = theoremType;
    }

    @JsonProperty("referenced_definitions")
    public List<String> getReferencedDefinitions() {
        return referencedDefinitions;
    }

    @JsonProperty("referenced_definitions")
    public void setReferencedDefinitions(final List<String> referencedDefinitions) {
        this.referencedDefinitions = referencedDefinitions;
    }

    @JsonProperty("referenced_theorems")
    public List<String> getReferencedTheorems() {
        return referencedTheorems;
    }

    @JsonProperty("referenced_theorems")
    public void setReferencedTheorems(final List<String> referencedTheorems) {
        this.referencedTheorems = referencedTheorems;
    }

    @JsonProperty("proven_status")
    public boolean getProvenStatus() {
        return provenStatus;
    }

    @JsonProperty("proven_status")
    public void setProvenStatus(final boolean provenStatus) {
        this.provenStatus = provenStatus;
    }

    private static final long serialVersionUID = 1545568391140364425L;
}
