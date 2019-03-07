package edu.msudenver.tsp.persistence.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
public class Definition implements Serializable {
    private List<String> definitions;

    private static final long serialVersionUID = -2208496232532214840L;
}
