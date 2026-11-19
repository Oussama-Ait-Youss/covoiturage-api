package com.covoiturage.api.exception;

public class CapacityNumberInvalideException extends RuntimeException{
    public CapacityNumberInvalideException(String message){
        super(message);
    }
}
