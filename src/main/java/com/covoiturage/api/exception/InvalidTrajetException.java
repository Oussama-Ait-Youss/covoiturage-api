
package com.covoiturage.api.exception;

public class InvalidTrajetException extends RuntimeException {

    public InvalidTrajetException(String message) {
        super(message);
    }
}
