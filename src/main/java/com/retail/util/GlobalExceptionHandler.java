package com.retail.util;

import com.retail.exception.InputValidationException;
import com.retail.exception.OperationFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ InputValidationException.class, OperationFailureException.class })
    public ResponseEntity<ResponsePojo> handleInputValidationException(Exception  exception) {

        ResponsePojo errorResponse = new ResponsePojo(ApplicationConstants.FAILURE,exception.getLocalizedMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
