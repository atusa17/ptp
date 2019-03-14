package edu.msudenver.tsp.persistence.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "proofs")
@EntityListeners(AuditingEntityListener.class)
@Data
@EqualsAndHashCode(callSuper = true)
public class ProofDto extends BaseDto implements Serializable {
    @NotBlank(groups = Insert.class)
    @Size(min = 1, max = 512, message = "The branch must be at least 1 character and at most 512 characters")
    private String branch;
    @Type(type = "json") @Column(name = "referenced_definitions", columnDefinition = "jsonb") private String referencedDefinitions;
    @Type(type = "json") @Column(name = "referenced_theorems", columnDefinition = "jsonb") private String referencedTheorems;
    @Temporal(TemporalType.DATE) @Column(name = "date_created") private Date dateCreated;
    @Temporal(TemporalType.DATE) @Column(name = "last_updated") private Date lastUpdated;


    @JsonProperty("referenced_definitions")
    public String getReferencedDefinitions() {
        return referencedDefinitions;
    }

    @JsonProperty("referenced_definitions")
    public void setReferencedDefinitions(final String referencedDefinitions) {
        this.referencedDefinitions = referencedDefinitions;
    }

    @JsonProperty("referenced_theorems")
    public String getReferencedTheorems() {
        return referencedTheorems;
    }

    @JsonProperty("referenced_theorems")
    public void setReferencedTheorems(final String referencedTheorems) {
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
