package com.covoiturage.api.exception;

public class PassagerNotFoundException extends RuntimeException{
    public PassagerNotFoundException(String message){
        super(message);
    }
}
