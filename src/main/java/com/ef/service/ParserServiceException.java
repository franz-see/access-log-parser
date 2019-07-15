package com.ef.service;

import com.ef.ParserException;

public class ParserServiceException extends ParserException {
    public ParserServiceException(String message) {
        super(message);
    }

    public ParserServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
