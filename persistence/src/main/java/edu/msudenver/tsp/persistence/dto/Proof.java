package edu.msudenver.tsp.persistence.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity(name = "proofs")
@EntityListeners(AuditingEntityListener.class)
@Data
@EqualsAndHashCode(callSuper = true)
public class Proof extends BaseDto implements Serializable {
    @NotBlank(groups = Insert.class)
    @Size(min = 1, max = 512, message = "The name must be at least 1 character and at most 512 characters")
    @Column(name = "theorem_name")
    private String theoremName;
    @NotNull(groups = Insert.class)
    @Size(min = 1, max = 4096, message = "The proof must be at least 1 character and at most 4096 characters")
    private String proof;
    @NotBlank(groups = Insert.class)
    @Size(min = 1, max = 512, message = "The branch must be at least 1 character and at most 512 characters")
    private String branch;
    @Type(type = "json") @Column(name = "referenced_definitions", columnDefinition = "jsonb") private List<String> referencedDefinitions;
    @Type(type = "json") @Column(name = "referenced_theorems", columnDefinition = "jsonb") private List<String> referencedTheorems;
    @Temporal(TemporalType.DATE) @Column(name = "date_created") private Date dateCreated;
    @Temporal(TemporalType.DATE) @Column(name = "last_updated") private Date lastUpdated;

    @JsonProperty("theorem_name")
    public String getTheoremName() {
        return theoremName;
    }

    @JsonProperty("theorem_name")
    public void setTheoremName(final String theoremName) {
        this.theoremName = theoremName;
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

    @JsonProperty("date_created")
    public Date getDateCreated() {
        return dateCreated;
    }

    @JsonProperty("date_created")
    public void setDateCreated(final Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    @JsonProperty("last_updated")
    public Date getLastUpdated() {
        return lastUpdated;
    }

    @JsonProperty("last_updated")
    public void setLastUpdated(final Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @PrePersist
    public void prePersist() {
        lastUpdated = new Date();
    }

    private static final long serialVersionUID = -7731220940349760402L;

    public interface Insert {}
}
