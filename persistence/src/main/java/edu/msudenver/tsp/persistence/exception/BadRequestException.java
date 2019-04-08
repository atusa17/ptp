package edu.msudenver.tsp.persistence.exception;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

public class BadRequestException extends Exception {
    @Getter private final List<String> errors;

    public BadRequestException(final String message) {
        super(message);
        errors = Collections.singletonList(message);
    }

    public BadRequestException(final List<String> errorMessages) {
        super(String.join(" ", errorMessages));
        errors = errorMessages;
    }
}
