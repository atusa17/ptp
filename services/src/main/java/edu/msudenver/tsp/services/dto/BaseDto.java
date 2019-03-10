package edu.msudenver.tsp.services.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseDto implements Serializable {
    private int id;
    private Integer version;

    private static final long serialVersionUID = 5343705942114910963L;
}
