
package com.covoiturage.api.exception;

public class NumeroPermisAlreadyExistsException extends RuntimeException {

    public NumeroPermisAlreadyExistsException(String message) {
        super(message);
    }
}
