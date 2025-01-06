package com.retail.exception;

public class OperationFailureException  extends Exception{

    public OperationFailureException(String errorMsg) {
        super(errorMsg);
    }
}