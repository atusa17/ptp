package edu.msudenver.tsp.persistence.exception;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

public class UnprocessableEntityException extends Exception {
    @Getter
    private final List<String> errors;

    public UnprocessableEntityException(final String message) {
        super(message);
        errors = Collections.singletonList(message);
    }

    public UnprocessableEntityException(final List<String> errorMessages) {
        super(String.join(" ", errorMessages));
        errors = errorMessages;
    }
}
