package com.ripple.takehome.trustlineserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This exception is thrown when a transaction with receiving node results in a failure
 * @author Somnath Nirakari
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class TrustLineTransactionFailureException extends RuntimeException {

    public TrustLineTransactionFailureException(String message) {
        super(message);
    }

    public TrustLineTransactionFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
