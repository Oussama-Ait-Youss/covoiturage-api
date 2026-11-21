package com.covoiturage.api.exception;

public class NomberplacesNotValideException extends RuntimeException{
    public NomberplacesNotValideException(String message){
        super(message);
    }
}
