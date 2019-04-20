package edu.msudenver.tsp.persistence.controller;

import edu.msudenver.tsp.persistence.exception.BadRequestException;
import edu.msudenver.tsp.persistence.exception.UnprocessableEntityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlingController {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleMessageNotReadableException(final HttpServletRequest request, final HttpMessageNotReadableException e) {
        LOG.error("Unable to bind post data sent to: {} \nCaught Exception: {}", request.getRequestURI(), e.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleMessageInvalidRequest(final HttpServletRequest request, final ValidationException e) {
        LOG.error("Request did not validate. Experienced the following error: {}", e.getMessage());
        return "Request did not evaluate. Experienced the following error: " + e.getMessage();
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public void handleHttpRequestMethodNotSupported(final HttpServletRequest request) {
        LOG.error("Unsupported request method ({}) for URL: {}", request.getMethod(), request.getRequestURI());
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public void handleHttpMediaTypeNotSupportedException(final HttpServletRequest request, final HttpMediaTypeNotSupportedException e) {
        LOG.error("Unsupported media type ({}) for URL: {}", e.getContentType(), request.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleException(final HttpServletRequest request, final Exception e) {
        LOG.error("Exception caught for URL: {}", request.getRequestURI(), e);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<String> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        LOG.error("Request did not evaluate. Experienced the following error: {}", e.getMessage());
        return e.getBindingResult().getFieldErrors().stream().map(x -> x.getField() + " " + x.getDefaultMessage()).collect(Collectors.toList());
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<String> handleBadRequestException(final BadRequestException e) {
        return e.getErrors();
    }

    @ExceptionHandler(UnprocessableEntityException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public List<String> handleUnprocessableEntityException(final UnprocessableEntityException e) {
        return Collections.singletonList("Request did not evaluate. Experienced the following error: " + e.getMessage());
    }
}
