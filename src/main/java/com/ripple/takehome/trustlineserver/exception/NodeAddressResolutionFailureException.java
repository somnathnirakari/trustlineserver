package com.ripple.takehome.trustlineserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This exception is thrown when a node's given name translation to its address fails
 * @author Somnath Nirakari
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class NodeAddressResolutionFailureException extends RuntimeException {

    public NodeAddressResolutionFailureException(String message) {
        super(message);
    }

    public NodeAddressResolutionFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
