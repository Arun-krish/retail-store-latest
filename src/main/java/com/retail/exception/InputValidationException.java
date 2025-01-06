package com.retail.exception;

public class InputValidationException extends Exception{

    public InputValidationException(String errorMsg) {
        super(errorMsg);
    }
}
