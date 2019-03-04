package edu.msudenver.tsp.persistence.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class TheoremDto extends BaseDto {
    @NotBlank @Size(min = 1, max = 512, message = "theorem name must be between 1 and 512 characters") private String name;
    @NotNull @JsonProperty("theorem_type") private TheoremType theoremType;
    @NotNull private String branch;
    @Type(type = "json") @Column(columnDefinition = "jsonb") @JsonProperty("referenced_definitions") private List<String> referencedDefinitions;
    @Type(type = "json") @Column(columnDefinition = "jsonb") @JsonProperty("referenced_theorems") private List<String> referencedTheorems;
    @NotNull @JsonProperty("proven_status") private boolean provenStatus;
}
