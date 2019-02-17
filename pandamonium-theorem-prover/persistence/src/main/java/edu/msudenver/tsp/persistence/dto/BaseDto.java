package edu.msudenver.tsp.persistence.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@MappedSuperclass
public class BaseDto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;
    @Version
    int version;
}
