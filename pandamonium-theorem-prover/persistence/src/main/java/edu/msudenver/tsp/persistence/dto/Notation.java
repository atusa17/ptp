package edu.msudenver.tsp.persistence.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
public class Notation implements Serializable {
    private List<String> notations;
    public static final long serialVersionUID = 2301438318932336121L;
}
