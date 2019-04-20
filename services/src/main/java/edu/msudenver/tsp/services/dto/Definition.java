package edu.msudenver.tsp.services.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class Definition extends BaseDto implements Serializable {
    @NotBlank(groups = Insert.class, message = "A name must be specified")
    @Size(min = 1, max = 200, message = "Must be between 1 and 200 characters")
    private String name;
    @NotBlank(groups = Insert.class, message = "At least one (1) definition must be specified")
    private List<String> definition;
    private List<String> notation;

    private static final long serialVersionUID = 3412178112996807691L;

    public interface Insert {}
}
