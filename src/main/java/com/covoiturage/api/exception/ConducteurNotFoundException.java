package com.covoiturage.api.exception;

public class ConducteurNotFoundException extends RuntimeException{
    public ConducteurNotFoundException(String message){
        super(message);
    }
}
