package edu.msudenver.tsp.persistence.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class ExceptionHandlingControllerTest {
    @Mock
    ExceptionHandlingController exceptionHandlingController;

    @Test
    public void testHandleMessageNotReadableException() {
        exceptionHandlingController.handleMessageNotReadableException(any(), any());
    }

    @Test
    public void testHandleMessageInvalidRequestException() {
        exceptionHandlingController.handleMessageInvalidRequest(any(), any());
    }

    @Test
    public void testHandleHttpRequestMethodNotSupportedException() {
        exceptionHandlingController.handleHttpRequestMethodNotSupported(any());
    }

    @Test
    public void testHandleHttpMediaTypeNotSupportedException() {
        exceptionHandlingController.handleHttpMediaTypeNotSupportedException(any(), any());
    }

    @Test
    public void testHandleException() {
        exceptionHandlingController.handleException(any(), any());
    }

    @Test
    public void testHandleMethodArgumentNotValidException() {
        exceptionHandlingController.handleMethodArgumentNotValidException(any());
    }

    @Test
    public void testHandleBadRequestException() {
        exceptionHandlingController.handleBadRequestException(any());
    }

    @Test
    public void testHandleUnprocessableEntityException() {
        exceptionHandlingController.handleUnprocessableEntityException(any());
    }
}