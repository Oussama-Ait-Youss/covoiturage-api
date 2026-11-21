package com.covoiturage.api.exception;

public class InvalidTrajetDataException extends RuntimeException{
    public InvalidTrajetDataException(String message){
        super(message);
    }
}
