package edu.msudenver.tsp.website.controller.forms;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class Theorem {
    private String theoremName1 ;
     private String theoremName2 ;

    // @NotBlank(message = "Theorem name must not be blank") private String theoremName;


}
